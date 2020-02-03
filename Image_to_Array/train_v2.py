import numpy as np
import cv2
import glob
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
        data[student_id] = [load_image(os.path.join(path, student_id, image_name)) for i, image_name in enumerate(
                                  image_name_list) if i < 16]

    return student_id_list, data


def make_x_y(id_list, data, num, dtype=np.float32):
    c_or_n = 1
    x = list()
    y = list()

    for c_or_n in range(num):
        # 학번 랜덤 추출
        if c_or_n % 2 == 0:
            i = np.random.randint(low=0, high=len(id_list))
            j = np.random.randint(low=0, high=len(id_list))
            while i==j:
                j = np.random.randint(low=0, high=len(id_list))
            id_i = id_list[i]
            id_j = id_list[j]
            # 이미지 랜덤 추출
            i = np.random.randint(low=0, high=len(data[id_i]))
            j = np.random.randint(low=0, high=len(data[id_j]))
            img_i = data[id_i][i]
            img_j = data[id_j][j]
            # y 만들기
            _y = [0, 1]
        else:
            i = np.random.randint(low=0, high=len(id_list))
            id_i = id_list[i]

            i = np.random.randint(low=0, high=len(data[id_i]))
            j = np.random.randint(low=0, high=len(data[id_i]))
            img_i = data[id_i][i]
            img_j = data[id_i][j]
            # y 만들기
            _y = [1, 0]

        # x 만들기
        _x = cv2.hconcat([img_i, img_j])

        x.append(_x)
        y.append(_y)

    return np.array(x).astype(dtype), np.array(y).astype(dtype)


def build_model():
    model = tf.keras.models.Sequential()
    model.add(tf.keras.layers.Conv2D(32, (3, 3), activation='relu', padding='same', input_shape=(100, 200, 3)))
    model.add(tf.keras.layers.MaxPooling2D((2, 2)))
    model.add(tf.keras.layers.Conv2D(64, (3, 3), activation='relu'))
    model.add(tf.keras.layers.MaxPooling2D((2, 2)))
    model.add(tf.keras.layers.Conv2D(128, (3, 3), activation='relu'))
    model.add(tf.keras.layers.MaxPooling2D((2, 2)))
    model.add(tf.keras.layers.Conv2D(256, (2, 2), activation='relu'))
    #model.add(tf.keras.layers.MaxPooling2D((2, 2)))
    #model.add(tf.keras.layers.Conv2D(512, (2, 2), activation='relu'))

    # 출력층(Dense) 추가
    model.add(tf.keras.layers.Flatten())
    model.add(tf.keras.layers.Dropout(0.55))
    model.add(tf.keras.layers.Dense(256, activation='relu'))
    model.add(tf.keras.layers.Dense(2, activation='softmax'))

    return model


if __name__ == "__main__":
    # set data directories
    train_data_dir = "./img"
    test_data_dir = "./test"
    model_dir = "trained_model"
    version = ".1.0"


    # set hyper parameter
    train_epoch_num = 10
    train_data_num = 10000
    test_data_num = 1000
    repeat_time = 50

    # build model
    model = build_model()
    model_name = os.path.join(model_dir, "model")
    # 컴파일
    model.compile(optimizer='adam', loss='binary_crossentropy', metrics=['accuracy'])
    if os.path.exists("./trained_model/model"):
        model.load_weights(model_name)
    # load data
    train_id_list, train_data = load_data(train_data_dir)
    test_id_list, test_data = load_data(test_data_dir)
    # train_id_list = ['0001', ...], 학번 리스트
    # train_data = {'0001':[img1, img2, ...], '0002':[], ...}, 이미지 딕셔너리
    print("Number of ID in train data : {}".format(len(train_id_list)))
    print("Number of ID in test data : {}".format(len(test_id_list)))

    if not os.path.isdir(model_dir):
        os.mkdir(model_dir)
    # make data set
    for i in range(repeat_time):
        train_x, train_y = make_x_y(train_id_list, train_data, train_data_num)
        print("Shape of train_x : {}".format(train_x.shape))
        print("Shape of train_y : {}".format(train_y.shape))
        # train model
        model = create_model()
        model.compile(optimizer='adam',
                      loss='sparse_categorical_crossentropy',
                      metrics=['accuracy'])

        log_dir = "../../logs/fit/" + datetime.datetime.now().strftime("%Y%m%d-%H%M%S")  # 로그 저장 폴더명 지정
        tensorboard_callback = tf.keras.callbacks.TensorBoard(log_dir=log_dir, histogram_freq=1)  # 콜백 함수 만드는 것

        model.fit(train_x,
                  train_y,
                  epochs=train_epoch_num,
                  validation_data=(x_test, y_test),
                  callbacks=[tensorboard_callback])

    #tmp = model.predict(test_x)
    #print(tmp.shape)
    #exit()
    test_x, test_y = make_x_y(test_id_list, test_data, test_data_num)

    # evaluate
    test_loss, test_acc = model.evaluate(test_x, test_y)
    print(test_loss, test_acc)
    # save model
    model.save_weights(model_name+"ver"+version)
