import numpy as np
import cv2
import tensorflow as tf
import os
import dlib
import glob


def load_image(file_name, mode=cv2.IMREAD_ANYCOLOR):
    img = cv2.imread(file_name, mode)  # load 3 dimensional array    # 이미지를 3차원 배열로 읽어오는 함수
    return img


def load_data(path):
    student_id_list = os.listdir(path)

    return student_id_list


def make_x_y(input_list, cmp_list, dtype=np.float32):
    x = []
    _x = []

    for i, cmp_img in enumerate(cmp_list):
        for j, input_img in enumerate(input_list):
            _x = cv2.hconcat([cmp_img, input_img])
            cv2.imshow(str(i)+str(j), _x)
            x.append(_x)
    cv2.waitKey(0)
    print(x)
    return np.array(x).astype(dtype)


def build_model():
    model = tf.keras.models.Sequential()
    model.add(tf.keras.layers.Conv2D(32, (3, 3), activation='relu', padding='same', input_shape=(100, 200, 3)))
    model.add(tf.keras.layers.MaxPooling2D((2, 2)))
    model.add(tf.keras.layers.Conv2D(64, (3, 3), activation='relu', padding='same'))
    model.add(tf.keras.layers.MaxPooling2D((2, 2)))
    model.add(tf.keras.layers.Conv2D(128, (3, 3), activation='relu', padding='same'))
    model.add(tf.keras.layers.MaxPooling2D((2, 2)))
    model.add(tf.keras.layers.Conv2D(256, (2, 2), activation='relu', padding='same'))
    model.add(tf.keras.layers.Conv2D(256, (2, 2), activation='relu', padding='same'))
    model.add(tf.keras.layers.Conv2D(256, (2, 2), activation='relu', padding='same'))
    model.add(tf.keras.layers.Conv2D(256, (2, 2), activation='relu', padding='same'))
    model.add(tf.keras.layers.MaxPooling2D((2, 2)))
    model.add(tf.keras.layers.Conv2D(512, (2, 2), activation='relu', padding='same'))

    # 출력층(Dense) 추가
    model.add(tf.keras.layers.Flatten())
    model.add(tf.keras.layers.Dense(256, activation='relu'))
    model.add(tf.keras.layers.Dense(2, activation='softmax'))

    return model


if __name__ == "__main__":
    # set data directories
    #input_img_path = "./RealTest/InputImg"
    cmp_img_path = "./RealTest/CmpImg"
    org_img_path = "./RealTest/OrgImg/9.jpg"
    model_name = "../../FaceDataSet/trained_model/model"
    cmp_stu_list = []
    cmp_data_list = []
    cmp_img_list = []
    input_data_list = []

    # set hyper parameter
    input_size = (100, 100)

    # load data
    cmp_stu_list = glob.glob(cmp_img_path + "/*.jpg")  # 기존 등록되어 있던 이미지의 리스트 생성
    for i, cmp_stu in enumerate(cmp_stu_list):
        cmp_img = load_image(cmp_stu)
        cmp_data_list.append(cmp_img)

    org_img = load_image(org_img_path)  # [세로, 가로, 채널]
    face_detector = dlib.get_frontal_face_detector()  # dlib의 face detection 적용 -> face_rect.상하좌우 에 값이 반환되는 것으로 보임
    detected_faces = face_detector(org_img, 1)  # detected_faces에 찾아진 얼굴들에 대한 좌표가 리스트로 저장되어있음
    for j, face_rect in enumerate(detected_faces):  # 원본 이미지에서 뽑아낸 얼굴들 리스트에 대한 좌표값들을 하나하나 이용할 시간, j에 대해 enumerate를 사용
        left, right, top, bottom = face_rect.left(), face_rect.right(), face_rect.top(), face_rect.bottom()  # 좌우상하 값을 옮겨받는다.
        # print(j, left, right, top, bottom)
        try:
            face = org_img[top:bottom, left:right, :]  # 좌표값들을 통해서 실제 얼굴이 있는 위치를 범위로 뽑아내는 것
            face = cv2.resize(face, dsize=input_size)  # resize 단계 (dsize가 기존에 저장된 사이즈를 불러와 진행)
            input_data_list.append(face)
        except Exception as ex:
            print(ex)

    input_img_list = np.array(input_data_list)
    cmp_img_list = np.array(cmp_data_list)
    print("input_img_list : ")
    print(input_img_list.shape)
    print(cmp_img_list.shape)

    # make data set
    cat_set = make_x_y(input_img_list, cmp_img_list)
    print("cat_set :")
    print(cat_set.shape)
    # build model
    model = build_model()

    # 컴파일
    optimizer = tf.keras.optimizers.Adam()
    model.compile(optimizer=optimizer, loss='binary_crossentropy', metrics=['accuracy'])

    # load model
    model.load_weights(model_name)

    # Predict model
    prediction = model.predict(cat_set)
    for i, compare in enumerate(prediction):
        if compare[0] > compare[1]:
            print(i)