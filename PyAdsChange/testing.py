import tensorflow as tf
import utils
import cv2
import dlib
import glob
import argparse
from model import myModel
import numpy as np


def load_image(filename):
    img = cv2.imread(filename, flags=cv2.IMREAD_COLOR).astype(np.float32)
    img /= 255.0
    return img


if __name__ == "__main__":

    # hyperparameter
    test_path = ""
    model_path = ""
    input_size = (100, 100)
    test_images = []
    result_list = []

    parser = argparse.ArgumentParser()
    parser.add_argument("--data_dir", default="../../FaceDataSet/ncrop", help="Data directory")
    parser.add_argument("--chkpt_dir", default="../../FaceDataSet/train_model0420")
    parser.add_argument("--log_dir", default="./logs/logs0420")
    parser.add_argument("--train_person_num", default=20, type=int, help="하나의 훈련용 배치를 구성할 사람의 수")
    parser.add_argument("--train_face_num", default=5, type=int, help="하나의 훈련용 배치를 구성할 사람마다 사용할 얼굴 사진의 수")
    parser.add_argument("--test_person_num", default=20, type=int, help="하나의 평가용 배치를 구성할 사람의 수")
    parser.add_argument("--test_face_num", default=5, type=int, help="하나의 가용 배치를 구성할 사람마다 사용할 얼굴 사진의 수")
    args = parser.parse_args()

    # Set optimizer
    lr_schedule = tf.keras.optimizers.schedules.PolynomialDecay(
        initial_learning_rate=0.01,
        decay_steps=1000,
        end_learning_rate=0.001
    )
    optimizer = tf.keras.optimizers.Adam(learning_rate=lr_schedule)

    # Configuration
    config = {
        "train_person_num": args.train_person_num,
        "train_face_num": args.train_face_num,
        "embedding_dim": 256,
        "apply_gradient_clipping": True,
        "gradient_clip_norm": 1,
        "loss_type": "ge2e",
        # "loss_type" : "binary_cross_entropy",
        "optimizer": optimizer,

        "train_epoch_num": 100000
    }

    org_img = load_image(test_path)
    face_detector = dlib.get_frontal_face_detector()
    detected_faces = face_detector(org_img, 1)
    for j, face_rect in enumerate(detected_faces):
        left, right, top, bottom = face_rect.left(), face_rect.right(), face_rect.top(), face_rect.bottom()
        try:
            face = org_img[top:bottom, left:right, :]
            face = cv2.resize(face, dsize=input_size)
            cv2.imshow(str(j), face)
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