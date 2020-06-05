import os
os.environ['TF_CPP_MIN_LOG_LEVEL'] = '2'
import tensorflow as tf
import dlib
import sys
import cv2
from model import FaceEmbedder
import utils
import numpy as np

if __name__ == "__main__":

    # hyperparameter
    file_path = sys.argv[1]
    file_name = sys.argv[2]
    STU_IDs = sys.argv[3]
    # file_path = "D:/Study/Capstone/CoreTech"
    # file_name = "20004.jpg"
    # STU_IDs = "201221892201421927201421936201521889"
    enroll_path = file_path + "/enroll_img"
    test_path = file_path + "/" + file_name
    model_path = "D:/Study/FaceDataSet/train_model0420/chkpt-190000"
    input_size = (100, 100)
    enroll_images = []
    test_images = []
    STU_ID = []
    length = 9

    # Set optimizer
    lr_schedule = tf.keras.optimizers.schedules.PolynomialDecay(
        initial_learning_rate=0.01,
        decay_steps=1000,
        end_learning_rate=0.001
    )
    optimizer = tf.keras.optimizers.Adam(learning_rate=lr_schedule)

    # Configuration
    config = {
        "train_person_num": 20,
        "train_face_num": 5,
        "embedding_dim": 256,
        "apply_gradient_clipping": True,
        "gradient_clip_norm": 1,
        "loss_type": "ge2e",
        "optimizer": optimizer,
        "train_epoch_num": 200000,
        "evaluate_step_interval": 1000,
        "save_chkpt_interval": 10000
    }

    for i in [STU_IDs[i:i + length] for i in range(0, len(STU_IDs), length)]:
        STU_ID.append(i)
    # image load
    for i, cmp_img in enumerate(STU_ID):
        img = utils.read_image(enroll_path + "/" + cmp_img + ".jpg")
        enroll_images.append(img)

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

    if len(test_images) != 0:
        # model load
        model = FaceEmbedder(config)
        model.load_weights(model_path)
        network = model.build_network()

        # create vector
        enroll_images = np.array(enroll_images).astype("float32")
        test_images = np.array(test_images).astype("float32")
        enroll_vec = network(enroll_images)
        test_vec = network(test_images)

        # 등록인원과 입력인원에 대한 비교 행렬 생성
        S = tf.matmul(test_vec, tf.transpose(enroll_vec))

        # get shape
        test_num, enroll_num = S.shape

        threshold = 0.7
        # 동일인물 확인
        for i in range(test_num):
            max_score_idx = np.argmax(S[i, :])
            max_score = S[i, max_score_idx]
            if max_score < threshold:
                print("점수는 최대지만 threshold보다 낮음")
            else:
                print(STU_ID[max_score_idx])
    else:
        print("사람 없음")
    os.remove(test_path)
