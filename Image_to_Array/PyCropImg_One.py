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
    img = load_image("D:/Study/Capstone/Image_to_Array/C7CFC7CFBEC8B0E6_BAEDB7B9B5E5png")
    face_detector = dlib.get_frontal_face_detector()
    detected_faces = face_detector(img, 1)
    for j, face_rect in enumerate(detected_faces):
        left, right, top, bottom = face_rect.left(), face_rect.right(), face_rect.top(), face_rect.bottom()
        face = img[top:bottom, left:right, :]
        face = cv2.resize(face, dsize=input_size)
        cv2.imwrite("D:/Study/Capstone/Image_to_Array" + "/crop_img" + str(j) + ".jpg", face)