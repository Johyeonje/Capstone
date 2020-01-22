import numpy as np
import cv2
import dlib
import os
import glob
from random import *
import tensorflow as tf


def load_image(file_name, mode=cv2.IMREAD_ANYCOLOR):
    img = cv2.imread(file_name, mode)  # load 3 dimensional array 	# 이미지를 3차원 배열로 읽어오는 함수
    return img


def setting(times, img_list, stu_num, set_x, set_y):
    for i in range(0, times, 1):
        a = randint(0, len(img_list) - 1)
        b = randint(0, len(img_list) - 1)
        while a == b:
            b = randint(0, len(img_list) - 1)

        for j, img in enumerate(img_list):
            if j == a:
                first_img = load_image(img)  # 위에서 정해진 랜덤한 함수에 대한

            if j == b:
                second_img = load_image(img)  # 위에서 정해진 랜덤한 함수에 대한

        if stu_num[a] == stu_num[b]:
            set_y.extend("1")
        else:
            set_y.extend("0")

        cat = cv2.hconcat([first_img, second_img])
        set_x.append(cat)
        array_x = np.array(set_x)


    return array_x, set_y


if __name__ == "__main__":
    img_path = "./img/"
    traing_times = 10000
    testing_times = 1000
    stu_num = []
    img_list = []
    set_x = []
    set_y = []
    x_type_size = 9


    for i in range(1, x_type_size, 1):
        all_img = glob.glob(img_path + "000" + str(i) + "/*.jpg")
        for k in range(0, len(all_img), 1):
            stu_num.extend(str(i))
        img_list.extend(all_img)

    train_images, train_labels = setting(traing_times, img_list, stu_num, set_x, set_y)
    test_images, test_labels = setting(testing_times, img_list, stu_num, set_x, set_y)

    # 픽셀 값을 0~1 사이로 정규화합니다.
    train_images, test_images = train_images / 255.0, test_images / 255.0

    # 합성곱 층(convolution layer) 만들기
    model = tf.keras.models.Sequential()
    model.add(tf.keras.layers.Conv2D(32, (3, 3), activation='relu', padding='same', input_shape=(100, 200, 3)))
    model.add(tf.keras.layers.Conv2D(32, (3, 3), activation='relu', padding='same'))
    model.add(tf.keras.layers.MaxPooling2D((3, 3)))
    model.add(tf.keras.layers.Conv2D(32, (3, 3), activation='relu', padding='same'))
    model.add(tf.keras.layers.Conv2D(32, (3, 3), activation='relu', padding='same'))

    # 모델 구조 출력
    model.summary()

    # 출력층(Dense) 추가
    model.add(layers.Flatten())
    model.add(layers.Dense(64, activation='relu'))
    model.add(layers.Dense(2, activation='softmax'))

    # 컴파일
    model.compile(optimizer='adam', loss='sparse_categorical_crossentropy', metrics=['accuracy'])

    # 실행
    model.fit(train_images, train_labels, epochs=5)

    # 모델 평가
    test_loss, test_acc = model.evaluate(test_images, test_labels, verbose=2)

    # 정확도
    print(test_loss, test_acc)