import numpy as np
import cv2
import os
import glob
from random import *
import tensorflow as tf
from tensorflow.keras import datasets, layers, models


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
            set_y.append([1, 0])
        else:
            set_y.append([0, 1])

        cat = cv2.hconcat([first_img, second_img])
        set_x.append(cat)
        array_x = np.array(set_x)
        array_y = np.array(set_y)
        print(array_x.shape)
        print(array_y.shape)

    return array_x, array_y


if __name__ == "__main__":
    train_img_path = "../../FaceDataSet/crop/"
    test_img_path = "./test/"
    train_times = 3000
    test_times = 300
    test_stu_num = []
    train_stu_num = []
    train_img_list = []
    test_img_list = []
    set_x = []
    set_y = []
    x_type_size = 15

    for i in range(1, x_type_size, 1):
        train_all_img = glob.glob(train_img_path + "Face" + str(i) + "/*.jpg")
        for k in range(0, len(train_all_img), 1):
            train_stu_num.extend(str(i))
        train_img_list.extend(train_all_img)

    for i in range(1, x_type_size, 1):
        test_all_img = glob.glob(train_img_path + "Face" + str(i) + "/*.jpg")
        for k in range(0, len(test_all_img), 1):
            test_stu_num.extend(str(i))
        test_img_list.extend(test_all_img)

    train_images, train_labels = setting(train_times, train_img_list, train_stu_num, set_x, set_y)
    test_images, test_labels = setting(test_times, test_img_list, test_stu_num, set_x, set_y)

    # 픽셀 값을 0~1 사이로 정규화합니다.
    train_images, test_images = train_images / 255.0, test_images / 255.0
    train_images = train_images.astype(np.float32)
    test_images = test_images.astype(np.float32)
    train_labels = train_labels.astype(np.float32)
    test_labels = test_labels.astype(np.float32)

    # 합성곱 층(convolution layer) 만들기
    model = tf.keras.models.Sequential()
    model.add(tf.keras.layers.Conv2D(64, (3, 3), activation='relu', padding='same', input_shape=(100, 200, 3)))
    model.add(tf.keras.layers.MaxPooling2D((3, 3)))
    model.add(tf.keras.layers.Conv2D(64, (3, 3), activation='relu', padding='same'))
    model.add(tf.keras.layers.MaxPooling2D((3, 3)))
    model.add(tf.keras.layers.Conv2D(64, (5, 5), activation='relu', padding='same'))
    model.add(tf.keras.layers.Conv2D(64, (5, 5), activation='relu', padding='same'))

    # 모델 구조 출력
    model.summary()

    # 출력층(Dense) 추가
    model.add(layers.Flatten())
    model.add(layers.Dense(64, activation='relu'))
    model.add(layers.Dense(2, activation='softmax'))

    # 컴파일
    model.compile(optimizer='adam', loss='binary_crossentropy', metrics=['accuracy'])


    # 실행
    model.fit(train_images, train_labels, epochs=5)

    # 모델 평가
    test_loss, test_acc = model.evaluate(test_images, test_labels, verbose=2)

    # 정확도
    print(test_loss, test_acc)