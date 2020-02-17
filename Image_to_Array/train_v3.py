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
        data[student_id] = [load_image(os.path.join(path, student_id, image_name)) for image_name in
                            image_name_list]
    return student_id_list, data


def test_data(path):
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
        if n % 2 == 0:
            i = np.random.randint(low=0, high=len(id_list) - 1)
            j = np.random.randint(low=0, high=len(id_list) - 1)
            while i == j:
                j = np.random.randint(low=0, high=len(id_list) - 1)
            id_i = id_list[i]
            id_j = id_list[j]
            # 이미지 랜덤 추출
            i = np.random.randint(low=0, high=len(data[id_i]) - 1)
            j = np.random.randint(low=0, high=len(data[id_j]) - 1)
            img_i = data[id_i][i]
            img_j = data[id_j][j]
        else:
            i = np.random.randint(low=0, high=len(id_list) - 1)
            id_i = id_list[i]
            id_j = id_list[i]
            # 이미지 랜덤 추출
            i = np.random.randint(low=0, high=len(data[id_i]) - 1)
            j = np.random.randint(low=0, high=len(data[id_j]) - 1)
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
    model.add(tf.keras.layers.Conv2D(8, (2, 2), activation='relu', padding='same'))
    model.add(tf.keras.layers.MaxPooling2D((2, 2)))
    model.add(tf.keras.layers.Conv2D(16, (2, 2), activation='relu', padding='same'))
    model.add(tf.keras.layers.MaxPooling2D((2, 2)))
    model.add(tf.keras.layers.Conv2D(32, (3, 3), activation='relu', padding='same', input_shape=(100, 200, 3)))
    model.add(tf.keras.layers.MaxPooling2D((2, 2)))
    model.add(tf.keras.layers.Conv2D(64, (3, 3), activation='relu', padding='same'))
    model.add(tf.keras.layers.MaxPooling2D((2, 2)))
    model.add(tf.keras.layers.Conv2D(128, (3, 3), activation='relu', padding='same'))

    # 출력층(Dense) 추가
    model.add(tf.keras.layers.Flatten())
    model.add(tf.keras.layers.Dense(256, activation='relu'))
    model.add(tf.keras.layers.Dense(2, activation='softmax'))

    return model


if __name__ == "__main__":
    # set data directories
    train_data_dir = "../../FaceDataSet/crop/"
    test_data_dir = "../../FaceDataSet/crop_test/"
    model_dir = "trained_model2/"
    save_path = "../../FaceDataSet/"
    log_dir = "logs/ver4"  # tensorboard --logdir logs/ver1

    # set hyper parameter
    train_epoch_num = 100000
    test_data_num = 1000

    batch_size = 100
    summary_interval = 1
    validation_interval = 100
    store_interval = 5000

    # train model
    model = build_model()

    # 컴파일
    model.compile(optimizer='adam', loss='binary_crossentropy', metrics=['accuracy'])

    #model.load_weights(save_path + model_dir + "model-epoch-99000")

    print("model loaded")
    # load data
    train_id_list, train_data = load_data(train_data_dir)
    test_id_list, test_data = load_data(test_data_dir)
    print("Number of ID in train data : {}".format(len(train_id_list)))

    # crate summary wirter
    writer = tf.summary.create_file_writer(log_dir)

    # test 데이터는 한번만 만듦
    test_x, test_y = make_x_y(test_id_list, test_data, test_data_num)
    test_x = test_x / 255.0

    for epoch in range(train_epoch_num):
        batch_x, batch_y = make_x_y(train_id_list, train_data, batch_size)
        batch_x /= 255.0

        train_loss, train_acc = model.train_on_batch(batch_x, batch_y)

        if epoch % summary_interval == 0:
            with writer.as_default():
                tf.summary.scalar('train_loss', train_loss, step=epoch)
                tf.summary.scalar('train_acc', train_acc, step=epoch)

        if epoch % validation_interval == 0:
            test_loss, test_acc = model.evaluate(test_x, test_y)
            with writer.as_default():
                tf.summary.scalar('test_loss', test_loss, step=epoch)
                tf.summary.scalar('test_acc', test_acc, step=epoch)

        if epoch % store_interval == 0:
            if not os.path.isdir(save_path + model_dir):
                os.mkdir(save_path + model_dir)
            model_name = save_path + model_dir + "model-epoch-" + str(epoch) + "_v2"
            model.save_weights(model_name)
            print("Model saved as {}".format(model_name))

print("Epoch : {}, Train Loss : {}".format(epoch+1, '%1.2f' % train_loss))