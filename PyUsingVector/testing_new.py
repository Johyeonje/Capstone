import tensorflow as tf
import utils
import cv2
import dlib
import glob
import argparse
import openface
import os
from model_new import FaceEmbedder
import numpy as np


if __name__ == "__main__":

    # hyperparameter
    enroll_path = "D:/Study/Capstone/CoreTech/enroll_img"
    test_path = "C:/Users/Lee/Desktop/신발.png"
    model_path = "../../FaceDataSet/aligned_model/chkpt-80000"
    input_size = (100, 100)
    enroll_images = []
    test_images = []

    parser = argparse.ArgumentParser()
    parser.add_argument("--data_dir", default="D:/Study/FaceDataSet/aligned", help="Data directory")
    parser.add_argument("--chkpt_dir", default="D:/Study/FaceDataSet/aligned_model")
    parser.add_argument("--log_dir", default="D:/Study/Capstone/PyUsingVector/logs/aligned")
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

        "train_epoch_num": 200000,
        "evaluate_step_interval": 1000,
        "save_chkpt_interval": 10000
    }

    # image load
    predictor_model ="../../FaceDataSet/shape_predictor_68_face_landmarks.dat"
    cmp_img_list = glob.glob(enroll_path + "/*.jpg")
    for i, cmp_img in enumerate(cmp_img_list):
        img = utils.read_image(cmp_img)
        # cv2.imshow(str(i), img)
        # cv2.waitKey(0)
        enroll_images.append(img)

    org_img = utils.read_image(test_path)
    face_detector = dlib.get_frontal_face_detector()
    face_pose_predictor = dlib.shape_predictor(predictor_model)
    face_aligner = openface.AlignDlib(predictor_model)

    detected_faces = face_detector(org_img, 1)
    print(detected_faces)
    for j, face_rect in enumerate(detected_faces):
        left, right, top, bottom = face_rect.left(), face_rect.right(), face_rect.top(), face_rect.bottom()
        try:
            pose_landmarks = face_pose_predictor(org_img, face_rect)
            alignedFace = face_aligner.align(100, org_img, face_rect,
                                             landmarkIndices=openface.AlignDlib.OUTER_EYES_AND_NOSE)
            cv2.imshow(str(j), alignedFace)
            cv2.waitKey(0)
            test_images.append(alignedFace)
        except Exception as ex:
            print(ex)

    #model load
    model = FaceEmbedder(config)
    model.load_weights(model_path)
    network = model.build_network()

    #create vector
    enroll_images = np.array(enroll_images).astype(float)
    test_images = np.array(test_images).astype(float)
    print(enroll_images.shape)
    print(test_images.shape)
    enroll_vec = network(enroll_images)
    test_vec = network(test_images)

    # 등록인원과 입력인원에 대한 비교 행렬 생성
    S = tf.matmul(test_vec, tf.transpose(enroll_vec))

    #get shape
    test_num, enroll_num = S.shape

    threshold = 0.5

    # 동일인물 확인
    for i in range(test_num):
        max_score_idx = np.argmax(S[i, :])
        max_score = S[i, max_score_idx]
        if max_score < threshold:
            print("점수는 최대지만 threshold보다 낮음")
        else:
            print(str(cmp_img_list[max_score_idx]) + "번째 사람이랑 같은 사람인것 같음.")