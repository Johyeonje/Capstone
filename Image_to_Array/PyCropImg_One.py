import numpy as np
import cv2
import dlib
import glob
import openface
import os

def load_image(file_name, mode=cv2.IMREAD_ANYCOLOR):
    img = cv2.imread(file_name, mode)
    return img

if __name__ == "__main__":
    input_size = (100, 100)
    org_img_path = "./RealTest/OrgImg/1.jpg"
    save_path = "./RealTest/CmpImg"
    org_img = load_image(org_img_path)
    # cv2.imshow("1", org_img)
    # cv2.waitKey(0)
    predictor_model = "../../FaceDataSet/shape_predictor_68_face_landmarks.dat"
    face_detector = dlib.get_frontal_face_detector()
    face_pose_predictor = dlib.shape_predictor(predictor_model)
    face_aligner = openface.AlignDlib(predictor_model)
    detected_faces = face_detector(org_img, 1)
    for i, face_rect in enumerate(detected_faces):
        left, right, top, bottom = face_rlect.left(), face_rect.right(), face_rect.top(), face_rect.bottom()

        try:
            pose_landmarks = face_pose_predictor(img, face_rect)
            alignedFace = face_aligner.align(100, img, face_rect,
                                             landmarkIndices=openface.AlignDlib.OUTER_EYES_AND_NOSE)
            cv2.imwrite(save_path +"/crop_img" + str(i)+".jpg", alignedFace)
            print(str(i)+" picture saved")
        except Exception as ex:
            print(ex)