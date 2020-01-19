import numpy as np
import cv2
import dlib
import glob
from random import *

def load_image(file_name, mode=cv2.IMREAD_ANYCOLOR):
    img = cv2.imread(file_name, mode)  # 이미지를 3차원 배열로 읽어오는 함수
    return img

if __name__ == "__main__":
    img_path = "./img/"
    repeat_times = 2

    for i in range(1, repeat_times, 1):
        a = randint(1, 9)           # 학번별로 랜덤한 한명을 선택
        b = randint(1, 9)
        train_img_list_a = glob.glob(img_path+"000"+str(a)+"/*.jpg")  # 랜덤하게 선택된 학번파일안에 모든 이미지를 받아옴
        train_img_list_b = glob.glob(img_path+"000"+str(b)+"/*.jpg")
        train_set = []              # 이미지 두개와 라벨을 저장할 배열

        c = randint(0, len(train_img_list_a) - 1)       # 해당 학번에 대한 사진 중 하나를 랜덤하게 선택

        for j, train_img_a in enumerate(train_img_list_a):
            if j == c:
                first_img = load_image(train_img_a)     # 위에서 정해진 랜덤한 함수에 대한
                cv2.imshow("1", first_img)
                cv2.waitKey(0)
                break


        d = randint(0, len(train_img_list_b) - 1)

        for k, train_img_b in enumerate(train_img_list_b):
            if k == d:
                second_img = load_image(train_img_b)     # 위에서 정해진 랜덤한 함수에 대한
                cv2.imshow("2", second_img)
                cv2.waitKey(0)
                break

        train_set.append(first_img)
        train_set.append(second_img)
        if c == d:
            train_set.extend("1")
        else:
            train_set.extend("0")

        print(train_set)
