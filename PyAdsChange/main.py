import os
import cv2
import random
import itertools
import numpy as np
import tensorflow as tf
#from sklearn.utils import shuffle


def load_data(path):
    data = dict()
    id_list = os.listdir(path)
    for id in id_list:
        image_name_list = os.listdir(os.path.join(path, id))
        data[id] = [load_image(os.path.join(path, id, image_name)) for image_name in image_name_list]

    return id_list, data


def load_image(filename):
    img = cv2.imread(filename, flags=cv2.IMREAD_COLOR).astype(np.float32)
    img /= 255.0
    return img


def make_batch(id_list, data, batch_num, dtype=np.float32):
    x = list()
    y = list()
    for i in range(batch_num):
        # make batch x
        id = random.choice(id_list)            # choice age
        img_pick = random.choice(data[id])
        x.append(img_pick)

        # make label
        label = np.zeros(shape=[10,1], dtype=np.float32)
        label[(int(id)-1),0] = 1
        y.append(label)
        print(id)
        print(y)

    return np.array(x).astype(dtype), np.array(y).astype(dtype)


def build_model():
    model = tf.keras.models.Sequential()
    model.add(tf.keras.layers.Conv2D(8, (3, 3), activation='relu', padding='same', input_shape=(100, 100, 3)))
    model.add(tf.keras.layers.MaxPooling2D((2, 2)))
    model.add(tf.keras.layers.Conv2D(16, (3, 3), activation='relu', padding='same'))
    model.add(tf.keras.layers.MaxPooling2D((2, 2)))
    model.add(tf.keras.layers.Conv2D(32, (3, 3), activation='relu', padding='same'))
    model.add(tf.keras.layers.MaxPooling2D((2, 2)))
    model.add(tf.keras.layers.Conv2D(64, (2, 2), activation='relu', padding='same'))
    model.add(tf.keras.layers.Conv2D(64, (2, 2), activation='relu', padding='same'))
    model.add(tf.keras.layers.MaxPooling2D((2, 2)))
    model.add(tf.keras.layers.Conv2D(128, (2, 2), activation='relu', padding='same'))
    model.add(tf.keras.layers.MaxPooling2D((2, 2)))
    model.add(tf.keras.layers.Conv2D(256, (2, 2), activation='relu', padding='same'))
    model.add(tf.keras.layers.MaxPooling2D((2, 2)))

    # 출력층(Dense) 추가
    model.add(tf.keras.layers.Flatten())
    model.add(tf.keras.layers.Dense(256, activation='relu'))
    model.add(tf.keras.layers.Dense(12, activation='softmax'))

    return model


if __name__ == "__main__":
    data_path = "D:/Study/All-Age-Faces/F_crop"      #data path
    save_path = "/Ads_model0"       # model save path
    log_path = "/Ads_log0"          # log save path

    # parameter
    train_epoch_num = 100000
    test_epoch_num = 1000

    id_list, data = load_data(data_path)

    model = build_model()
    optimizer = tf.keras.optimizers.Adam(learning_rate=0.001)
    loss = tf.losses.BinaryCrossentropy()
    model.compile(optimizer=optimizer, loss=loss, metrics=['accuracy'])

    # Create summary writer
    writer = tf.summary.create_file_writer(logdir=log_dir)

    # load model
    # model.load_weights(model_name)

    for epoch in range(train_epoch_num):

        batch_x, batch_y = make_batch(id_list, data, batch_num=50)
        train_loss = model.evaluate(batch_x, batch_y)

        with writer.as_default():
            tf.summary.scalar("Train Loss", train_loss, step=epoch)
            #tf.summary.scalar("Train Acc", train_acc, step=epoch)

        if epoch != 0 and epoch % 1000 == 0:
            test_loss_list = []
            test_x, test_y = make_batch(id_list, data, batch_num=10)

            for test_epoch in range(train_epoch_num):
                y_pred = model.predict(batch_x)
                test_loss = loss(batch_y, y_pred)
                test_loss_list.append(test_loss)

            with writer.as_default():
                tf.summary.scalar("Test Loss", np.mean(test_loss_list), step=epoch)

        if epoch != 0 and epoch % 10000 == 0:
            filepath = os.path.join(save_path, "chkpt-" + str(epoch))
            model.save_weights(filepath)

        print("Epoch : {}, Train Loss : {}".format(epoch, "%1.3f" % train_loss))


    print("Trainning done")