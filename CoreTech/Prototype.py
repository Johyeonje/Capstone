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
    model.add(tf.keras.layers.Conv2D(8, (3, 3), activation='relu', padding='same'))
    model.add(tf.keras.layers.MaxPooling2D((2, 2)))
    model.add(tf.keras.layers.Conv2D(32, (3, 3), activation='relu', padding='same'))
    model.add(tf.keras.layers.MaxPooling2D((2, 2)))
    model.add(tf.keras.layers.Conv2D(64, (2, 2), activation='relu', padding='same'))
    model.add(tf.keras.layers.MaxPooling2D((2, 2)))
    model.add(tf.keras.layers.Conv2D(128, (2, 2), activation='relu', padding='same'))
    model.add(tf.keras.layers.MaxPooling2D((2, 2)))
    model.add(tf.keras.layers.Conv2D(256, (2, 2), activation='relu', padding='same'))

    model.add(tf.keras.layers.Flatten())
    model.add(tf.keras.layers.Dense(128, activation='relu'))
    model.add(tf.keras.layers.Dense(2, activation='softmax'))

    return model


if __name__ == "__main__":
    # file_path = sys.argv[1]
    # file_name = sys.argv[2]
    file_path = "D:/Study/Capstone/CoreTech"
    file_name = "1546955030.5424013.jpg"
    predictor_model = os.path.join(file_path, "../../FaceDataSet/shape_predictor_68_face_landmarks.dat")
    cmp_img_path = os.path.join(file_path, "RealTest/CmpImg")
    org_img_path = file_path + "/" + file_name
    model_name = os.path.join(file_path, "../../FaceDataSet/zin_trained_model1/chkpt-200000")
    cmp_stu_list = []
    cmp_data_list = []
    cmp_img_list = []
    input_data_list = []
    cmp_num = 0
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
    #os.remove(org_img_path)
