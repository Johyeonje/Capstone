import sys
import os
os.environ['TF_CPP_MIN_LOG_LEVEL'] = '3'
import cv2
import numpy as np
import tensorflow as tf
import dlib
import glob
import openface


def load_image(file_name, mode=cv2.IMREAD_ANYCOLOR):
    img = cv2.imread(file_name, mode)
    return img


def make_x_y(input_list, cmp_list, dtype=np.float32):
    x = []
    _x = []
    num_list = []
    for i, cmp_img in enumerate(cmp_list):
        for j, input_img in enumerate(input_list):
            _x = cv2.hconcat([cmp_img, input_img])
            x.append(_x)
            num_list.append(cmp_stu_list[i])
    return np.array(x).astype(dtype), num_list


def build_model():
    # CNN
    model = tf.keras.models.Sequential()
    model.add(tf.keras.layers.Conv2D(8, (2, 2), activation='relu', padding='same', input_shape=(200, 400, 3)))
    model.add(tf.keras.layers.MaxPooling2D((2, 2)))
    model.add(tf.keras.layers.Conv2D(16, (2, 2), activation='relu', padding='same'))
    model.add(tf.keras.layers.MaxPooling2D((2, 2)))
    model.add(tf.keras.layers.Conv2D(32, (3, 3), activation='relu', padding='same'))
    model.add(tf.keras.layers.MaxPooling2D((2, 2)))
    model.add(tf.keras.layers.Conv2D(64, (3, 3), activation='relu', padding='same'))
    model.add(tf.keras.layers.MaxPooling2D((2, 2)))
    model.add(tf.keras.layers.Dropout(0.25))
    model.add(tf.keras.layers.Conv2D(128, (3, 3), activation='relu', padding='same'))

    # 출력층(Dense) 추가
    model.add(tf.keras.layers.Flatten())
    model.add(tf.keras.layers.Dense(64, activation='relu'))
    model.add(tf.keras.layers.Dropout(0.5))
    model.add(tf.keras.layers.Dense(2, activation='softmax'))

    return model


if __name__ == "__main__":
    # file_path = sys.argv[1]
    # file_name = sys.argv[2]
    file_path = "D:/Study/Capstone/DoNotTouch"
    file_name = "9.jpg"
    predictor_model = "../../FaceDataSet/shape_predictor_68_face_landmarks.dat"
    cmp_img_path = os.path.join(file_path, "RealTest/CmpImg")
    org_img_path = file_path + "/" + file_name
    model_name = os.path.join(file_path, "../../FaceDataSet/trained_model8/model-epoch-2500")
    cmp_stu_list = []
    cmp_data_list = []
    cmp_img_list = []
    input_data_list = []
    cmp_num = 0
    trigger = 0
    cmp_stu_list = glob.glob(cmp_img_path + "/*.jpg")
    for i, cmp_stu in enumerate(cmp_stu_list):
        cmp_img = load_image(cmp_stu)
        cmp_data_list.append(cmp_img)
    org_img = load_image(org_img_path)
    face_detector = dlib.get_frontal_face_detector()
    face_pose_predictor = dlib.shape_predictor(predictor_model)
    face_aligner = openface.AlignDlib(predictor_model)
    detected_faces = face_detector(org_img, 1)
    for j, face_rect in enumerate(detected_faces):
        trigger = 1
        left, right, top, bottom = face_rect.left(), face_rect.right(), face_rect.top(), face_rect.bottom()
        try:
            pose_landmarks = face_pose_predictor(org_img, face_rect)
            alignedFace = face_aligner.align(200, org_img, face_rect, landmarkIndices=openface.AlignDlib.OUTER_EYES_AND_NOSE)
            input_data_list.append(alignedFace)
        except Exception as ex:
            print(ex)
    if trigger == 0:
        os.remove(org_img_path)
        print("얼굴 못 찾음")
    input_img_list = np.array(input_data_list)
    cmp_img_list = np.array(cmp_data_list)
    cat_set, num_list = make_x_y(input_img_list, cmp_img_list)
    model = build_model()
    optimizer = tf.keras.optimizers.Adam()
    model.compile(optimizer=optimizer, loss='binary_crossentropy', metrics=['accuracy'])
    model.load_weights(model_name)
    prediction = model.predict(cat_set)
    k = None
    for i, compare in enumerate(prediction):
        if prediction[i][0] > cmp_num:
            if k != num_list[i]:
                print(num_list[i])
                k = num_list[i]
    os.remove(org_img_path)
