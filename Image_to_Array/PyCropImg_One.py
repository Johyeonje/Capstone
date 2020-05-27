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
    for x in range(6,8):
        org_img_path = "D:/Study/All-Age-Faces/M/"+str(x)+"0-"+str(x)+"9"
        save_path = "D:/Study/All-Age-Faces/M_crop/AGE"+str(x)
        img_list = glob.glob(org_img_path+"/*.jpg")
        for i, org_img in enumerate(img_list):
            img = load_image(org_img)
            face_detector = dlib.get_frontal_face_detector()
            detected_faces = face_detector(img, 1)
            for j, face_rect in enumerate(detected_faces):
                left, right, top, bottom = face_rect.left(), face_rect.right(), face_rect.top(), face_rect.bottom()
                face = img[top:bottom, left:right, :]
                face = cv2.resize(face, dsize=input_size)

            cv2.imwrite(save_path + "/crop_img" + str(i) + ".jpg", face)