import numpy as np
import cv2
import tensorflow as tf
import os


def load_image(file_name, mode=cv2.IMREAD_ANYCOLOR):
    img = cv2.imread(file_name, mode)  # load 3 dimensional array    # 이미지를 3차원 배열로 읽어오는 함수
    return img


def load_data(path):
    data = dict()
    student_id_list = os.listdir(path)
    for student_id in student_id_list:
        image_name_list = os.listdir(os.path.join(path, student_id))
        data[student_id] = [load_image(os.path.join(path, student_id, image_name)) for image_name in
                                  image_name_list]

    return student_id_list, data


def make_x_y(id_list, data, num, dtype=np.float32):
    x = list()
    y = list()

    for n in range(num):
        # 학번 랜덤 추출
        i = np.random.randint(low=0, high=len(id_list)-1)
        j = np.random.randint(low=0, high=len(id_list)-1)
        id_i = id_list[i]
        id_j = id_list[j]

        # 이미지 랜덤 추출
        i = np.random.randint(low=0, high=len(data[id_i])-1)
        j = np.random.randint(low=0, high=len(data[id_j])-1)
        img_i = data[id_i][i]
        img_j = data[id_j][j]

        # x 만들기
        _x = cv2.hconcat([img_i, img_j])

        # y 만들기
        if id_i == id_j:
            _y = [1, 0]
        else:
            _y = [0, 1]

        x.append(_x)
        y.append(_y)

    return np.array(x).astype(dtype), np.array(y).astype(dtype)


def build_model():
    model = tf.keras.models.Sequential()
    model.add(tf.keras.layers.Conv2D(64, (3, 3), activation='relu', padding='same', input_shape=(100, 200, 3)))
    model.add(tf.keras.layers.MaxPooling2D((5, 5)))
    model.add(tf.keras.layers.Conv2D(64, (3, 3), activation='relu', padding='same'))
    model.add(tf.keras.layers.MaxPooling2D((5, 5)))
    model.add(tf.keras.layers.Conv2D(64, (5, 5), activation='relu', padding='same'))
    model.add(tf.keras.layers.Conv2D(64, (5, 5), activation='relu', padding='same'))

    # 출력층(Dense) 추가
    model.add(tf.keras.layers.Flatten())
    model.add(tf.keras.layers.Dense(64, activation='relu'))
    model.add(tf.keras.layers.Dense(2, activation='softmax'))

    return model


if __name__ == "__main__":
    # set data directories
    test_data_dir = "./test"
    model_name = "trained_model/model"

    # set hyper parameter
    test_data_num = 50

    # load data
    test_id_list, test_data = load_data(test_data_dir)

    # make data set
    test_x, test_y = make_x_y(test_id_list, test_data, test_data_num)

    # build model
    model = build_model()
    print("Yee~")

    # 컴파일
    optimizer = tf.keras.optimizers.Adam()
    model.compile(optimizer=optimizer, loss='binary_crossentropy', metrics=['accuracy'])

    # load model
    model.load_weights(model_name)

    # evaluate
    test_loss, test_acc = model.evaluate(test_x, test_y)
    print(test_loss, test_acc)


