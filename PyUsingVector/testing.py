import tensorflow as tf
import utils
import cv2
import dlib
import glob
import argparse
import os
from model import FaceEmbedder
import numpy as np

def build_model():
    model = tf.keras.models.Sequential()
    model.add(tf.keras.layers.Conv2D(filters=8, kernel_size=3, activation='relu'))
    model.add(tf.keras.layers.Conv2D(filters=8, kernel_size=3, activation='relu'))
    model.add(tf.keras.layers.BatchNormalization())
    model.add(tf.keras.layers.Conv2D(filters=16, kernel_size=3, activation='relu'))
    model.add(tf.keras.layers.Conv2D(filters=16, kernel_size=3, activation='relu'))
    model.add(tf.keras.layers.BatchNormalization())
    model.add(tf.keras.layers.Conv2D(filters=32, kernel_size=3, activation='relu'))
    model.add(tf.keras.layers.Conv2D(filters=64, kernel_size=3, activation='relu'))
    model.add(tf.keras.layers.BatchNormalization())

    model.add(tf.keras.layers.Flatten())
    model.add(tf.keras.layers.Dense(units=256, activation=None))

    return model


if __name__ == "__main__":

    # hyperparameter
    enroll_path = "./enroll_img"
    test_path = "./test_img/9.jpg"
    model_path = "../../FaceDataSet/train_model0420/chkpt-190000"
    input_size = (100, 100)
    enroll_images = []
    test_images = []

    # image load
    cmp_img_list = glob.glob(enroll_path + "/*.jpg")
    for i, cmp_img in enumerate(cmp_img_list):
        cmp_img = utils.read_image(enroll_path)
        enroll_images.append(cmp_img)

    org_img = utils.read_image(test_path)
    face_detector = dlib.get_frontal_face_detector()
    detected_faces = face_detector(org_img, 1)
    for j, face_rect in enumerate(detected_faces):
        left, right, top, bottom = face_rect.left(), face_rect.right(), face_rect.top(), face_rect.bottom()
        try:
            face = org_img[top:bottom, left:right, :]
            face = cv2.resize(face, dsize=input_size)
            test_images.append(face)
        except Exception as ex:
            print(ex)

    #model load
    model = build_model(model_path)

    #create vector
    enroll_vec = model(enroll_images)
    test_vec = model(test_images)

    # 등록인원과 입력인원에 대한 비교 행렬 생성
    S = np.matmul(test_vec, enroll_vec.T)

    #get shape
    test_num, enroll_num = S.shape

    threshold = 0.5

    # 동일인물 확인
    for i in range(test_num):
        max_score_idx = np.argmax(S[i, :])
        max_score = S[i, max_score_idx]
        if (max_score < threshold):
            print("점수는 최대지만 threshold보다 낮음")
        else:
            print(max_score_idx + "번째 사람이랑 같은 사람인것 같음.")
