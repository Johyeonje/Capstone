import os
import cv2
import random
import itertools
import numpy as np
import tensorflow as tf
from model import myModel


def load_image(filename):
    img = cv2.imread(filename, flags=cv2.IMREAD_COLOR).astype(np.float32)
    img /= 255.0
    return img


def get_acc(y_pred, y_true):
    acc = tf.reduce_mean(
        tf.cast(
            tf.equal(
                tf.argmax(y_pred, axis=1),
                tf.argmax(y_true, axis=1)
            ), tf.float32
        )
    )
    return acc


def load_data(data_dir):
    data = dict()
    id_list = os.listdir(data_dir)
    for id in id_list:
        tmp = []
        path = os.path.join(data_dir, id)
        for filename in os.listdir(path):
            filepath = os.path.join(path, filename)
            tmp.append(filepath)
        data[id] = tmp

    return id_list, data


def make_batch(id_list, data, batch_num, dtype=np.float32):
    x = list()
    y = list()
    for i in range(batch_num):
        # make batch x
        id = random.choice(id_list)            # choice age
        img_pick = random.choice(data[id])
        picked_image = load_image(img_pick)
        x.append(picked_image)

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
    id_list, data = load_data(data_path)
    lr_schedule = tf.keras.optimizers.schedules.PolynomialDecay(
        initial_learning_rate=0.01,
        decay_steps=1000,
        end_learning_rate=0.001
    )
    optimizer = tf.keras.optimizers.Adam(learning_rate=lr_schedule)
    loss = tf.losses.BinaryCrossentropy()
    config = {
        #"loss_type": "ge2e",
        "loss_type" : "binary_cross_entropy",
        "optimizer": optimizer,
        "train_epoch_num": 100000,
    }
    model = myModel(config)
    # model.compile(optimizer=tf.keras.optimizers.Adam(learning_rate=lr_schedule),
    #               loss=tf.losses.BinaryCrossentropy(), metrics=['accuracy'])

    # Create summary writer
    writer = tf.summary.create_file_writer(logdir=log_path)

    # load model
    # model.load_weights(model_name)

    for epoch in range(config["train_epoch_num"]):
        batch_x, batch_y = make_batch(id_list, data, batch_num=100)

        with tf.GradientTape() as tape:
            logits = model(batch_x)
            loss_value = loss(logits, batch_y)
            grads = tape.gradient(loss_value, model.trainable_variables)
            optimizer.apply_gradients(zip(grads, model.trainable_variables))
            train_acc = get_acc(logits, batch_y)

        with writer.as_default():
            tf.summary.scalar("Train Loss", loss_value, step=epoch)
            tf.summary.scalar("Train Acc", train_acc, step=epoch)

        if epoch != 0 and epoch % 1000 == 0:
            test_x, test_y = make_batch(id_list, data, batch_num=10)
            test_loss, test_acc = model.evaluate(test_x, test_y)

            with writer.as_default():
                tf.summary.scalar("Test Loss", test_loss, step=epoch)

        if epoch != 0 and epoch % 5000 == 0:
            filepath = os.path.join(save_path, "chkpt-" + str(epoch))
            model.save_weights(filepath)

        print("Epoch : {}, Train Loss : {}, Train Acc : {}".format(epoch, "%1.3f" % loss_value, "%1.3f" % train_acc))

    print("Trainning done")