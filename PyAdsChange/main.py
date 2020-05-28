import os
import cv2
import random
import itertools
import numpy as np
import tensorflow as tf
from model import myModel


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
        label = np.zeros(shape=[10], dtype=np.float32)
        label[(int(id))] = 1
        y.append(label)

    return np.array(x).astype(dtype), np.array(y).astype(dtype)


if __name__ == "__main__":
    data_path = "D:/Study/All-Age-Faces/M_crop"      #data path
    save_path = "D:/Study/All-Age-Faces/Ads_model0"       # model save path
    log_path = "./Ads_log0"          # log save path

    # parameter
    train_epoch_num = 100000
    input_size = (100, 100)
    id_list, data = load_data(data_path)
    
    model = myModel()
    lr_schedule = tf.keras.optimizers.schedules.PolynomialDecay(
        initial_learning_rate=0.01,
        decay_steps=1000,
        end_learning_rate=0.001
    )
    optimizer = tf.keras.optimizers.Adam(learning_rate=lr_schedule)
    loss = tf.losses.BinaryCrossentropy()
    model.compile(optimizer=optimizer, loss=loss, metrics=['accuracy'])

    # Create summary writer
    writer = tf.summary.create_file_writer(logdir=log_path)

    # load model
    # model.load_weights(model_name)

    for epoch in range(train_epoch_num):
        batch_x, batch_y = make_batch(id_list, data, batch_num=50)
        train_loss, train_acc = model.train_on_batch(batch_x, batch_y)

        with writer.as_default():
            tf.summary.scalar("Train Loss", train_loss, step=epoch)
            tf.summary.scalar("Train Acc", train_acc, step=epoch)

        if epoch != 0 and epoch % 1000 == 0:
            test_x, test_y = make_batch(id_list, data, batch_num=10)
            test_loss, test_acc = model.evaluate(test_x, test_y)

            with writer.as_default():
                tf.summary.scalar("Test Loss", test_loss, step=epoch)

        if epoch != 0 and epoch % 10000 == 0:
            filepath = os.path.join(save_path, "chkpt-" + str(epoch))
            model.save_weights(filepath)

        print("Epoch : {}, Train Loss : {}, Train Acc : {}".format(epoch, "%1.3f" % train_loss, "%1.3f" % train_acc))

    print("Trainning done")