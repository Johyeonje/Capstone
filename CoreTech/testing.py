import os
os.environ['TF_CPP_MIN_LOG_LEVEL'] = '2'
import tensorflow as tf
import dlib
import sys
import openface
from model import FaceEmbedder
import utils
import numpy as np

if __name__ == "__main__":

    # hyperparameter
    file_path = sys.argv[1]
    file_name = sys.argv[2]
    STU_IDs = sys.argv[3]
    enroll_path = file_path + "/enroll_img"
    test_path = file_path + "/" + file_name
    model_path = "D:/Study/FaceDataSet/aligned_model/chkpt-80000"
    predictor_model = "D:/Study/FaceDataSet/shape_predictor_68_face_landmarks.dat"
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
    face_pose_predictor = dlib.shape_predictor(predictor_model)
    face_aligner = openface.AlignDlib(predictor_model)
    try:
        detected_faces = face_detector(org_img, 1)
    except Exception as ex:
        print("사람 없음")
        os.remove(test_path)
        
    for j, face_rect in enumerate(detected_faces):
        left, right, top, bottom = face_rect.left(), face_rect.right(), face_rect.top(), face_rect.bottom()
        try:
            pose_landmarks = face_pose_predictor(org_img, face_rect)
            alignedFace = face_aligner.align(100, org_img, face_rect, landmarkIndices=openface.AlignDlib.OUTER_EYES_AND_NOSE)
            test_images.append(alignedFace)
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

        threshold = 0.5
        # 동일인물 확인
        for i in range(test_num):
            max_score_idx = np.argmax(S[i, :])
            max_score = S[i, max_score_idx]
            print(STU_ID[max_score_idx])
    else:
        print("사람 없음")
    os.remove(test_path)
