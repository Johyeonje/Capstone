import tensorflow as tf
import cv2
import dlib
import glob
import argparse
from model import myModel
import numpy as np


def read_image(filepath, mode=cv2.IMREAD_ANYCOLOR):
    image = cv2.imread(filepath, mode)
    return image


if __name__ == "__main__":

    # hyperparameter
    test_path = "D:/Study/Capstone/PyAdsChange/test_img/1.jpg"
    model_path = "D:/Study/All-Age-Faces/Ads_model0/chkpt-500"
    input_size = (100, 100)
    test_images = []
    result_list = []

    # Set optimizer
    lr_schedule = tf.keras.optimizers.schedules.PolynomialDecay(
        initial_learning_rate=0.01,
        decay_steps=1000,
        end_learning_rate=0.001
    )
    optimizer = tf.keras.optimizers.Adam(learning_rate=lr_schedule)

    # Configuration
    config = {
        # "loss_type": "ge2e",
        "loss_type": "binary_cross_entropy",
        "optimizer": optimizer,
    }

    org_img = read_image(test_path)
    face_detector = dlib.get_frontal_face_detector()
    detected_faces = face_detector(org_img, 1)
    for j, face_rect in enumerate(detected_faces):
        left, right, top, bottom = face_rect.left(), face_rect.right(), face_rect.top(), face_rect.bottom()
        try:
            face = org_img[top:bottom, left:right, :]
            face = cv2.resize(face, dsize=input_size)
            cv2.imshow(str(j), face)
            cv2.waitKey(0)
            test_images.append(face)
        except Exception as ex:
            print(ex)

    #model load
    model = myModel(config)
    loss = tf.losses.BinaryCrossentropy()
    model.compile(optimizer=optimizer, loss=loss, metrics=['accuracy'])

    #create vector
    test_images = np.array(test_images).astype(float)
    print(test_images.shape)

    S = model.predict(test_images)

    threshold = 0.5

    # 동일인물 확인
    for i in range(len(test_images)):
        max_score_idx = np.argmax(S[i, :])
        result_list.append(max_score_idx)
        print(result_list)