import numpy as np
import cv2
import dlib
import os
import glob
from random import *

def load_image(file_name, mode=cv2.IMREAD_ANYCOLOR):
    img = cv2.imread(file_name, mode)  # load 3 dimensional array 	# 이미지를 3차원 배열로 읽어오는 함수
    return img

if __name__ == "__main__":
    img_path = "./img/"
    repeat_times = 10000
    stu_num = []
    train_img_list = []
    train_set_x = []
    train_set_y = []
    x_type_size = 10

    for i in range(1, x_type_size, 1):
        train_img = glob.glob(img_path + "000" + str(i) + "/*.jpg")
        for k in range(0, len(train_img), 1):
            stu_num.extend(str(i))
        train_img_list.extend(train_img)

    for i in range(0, repeat_times, 1):
        a = randint(0, len(train_img_list) - 1)
        b = randint(0, len(train_img_list) - 1)
        while a == b:
            b = randint(0, len(train_img_list) - 1)

        for j, train_img in enumerate(train_img_list):
            if j == a:
                first_img = load_image(train_img)  # 위에서 정해진 랜덤한 함수에 대한

            if j == b:
                second_img = load_image(train_img)  # 위에서 정해진 랜덤한 함수에 대한

        if stu_num[a] == stu_num[b]:
            train_set_y.extend("1")
        else:
            train_set_y.extend("0")

        cat = cv2.hconcat([first_img, second_img])
        train_set_x.append(cat)
        train_array_x = np.array(train_set_x)

        print(train_array_x.shape)
        print(train_set_y)




    #train_set_x.append(cat)
    #train_set_y.extend(train_y)