import os
import cv2
import random
import itertools
import numpy as np
import tensorflow as tf
#from sklearn.utils import shuffle


# Set global variables
width = 100
height = 100
n_channels = 3


def load_image(filename):
    """ Function to load image file """
    img = cv2.imread(filename, flags=cv2.IMREAD_COLOR).astype(np.float32)
    img /= 255.0  # normalize image
    return img


class DataGenerator:
    def __init__(self, indir, people_num=7):
        self.indir = indir
        self.people_num = people_num

        self.id_list = os.listdir(indir)

    def generator(self):
        for iter in itertools.count(1):
            x = np.zeros(shape=[self.people_num * 2, height * 2, width, n_channels], dtype=np.float32)
            y = np.zeros(shape=[self.people_num * 2, 2], dtype=np.float32)

            for i in range(self.people_num):
                # Same
                id = random.choice(self.id_list)
                path = os.path.join(self.indir, id)
                f_name_1 = os.path.join(path, random.choice(os.listdir(path)))
                f_name_2 = os.path.join(path, random.choice(os.listdir(path)))
                x[i, 0:height, :, :] = load_image(f_name_1)
                x[i, height:2*height, :, :] = load_image(f_name_2)
                y[i, 0] = 1

                # Different
                id = random.choice(self.id_list)
                path = os.path.join(self.indir, id)
                f_name_1 = os.path.join(path, random.choice(os.listdir(path)))
                id = random.choice(self.id_list)
                path = os.path.join(self.indir, id)
                f_name_2 = os.path.join(path, random.choice(os.listdir(path)))
                x[i+self.people_num, 0:height, :, :] = load_image(f_name_1)
                x[i+self.people_num, height:2 * height, :, :] = load_image(f_name_2)
                y[i+self.people_num, 1] = 1

            #x, y = shuffle(x, y)
            x, y = np.array(x), np.array(y)
            yield x, y

    def create_data_loader(self):
        x_shape = [self.people_num*2, height*2, width, n_channels]
        y_shape = [self.people_num*2, 2]
        dataloader = tf.data.Dataset.from_generator(
            generator=self.generator,
            output_shapes=(tf.TensorShape(x_shape), tf.TensorShape(y_shape)),
            output_types=(tf.float32, tf.float32)
        )
        return dataloader



def create_model():
    """ Function to create model """
    model = tf.keras.models.Sequential()
    model.add(tf.keras.layers.Conv2D(8, (3, 3), activation='relu', padding='same'))
    model.add(tf.keras.layers.MaxPooling2D((2, 2)))
    model.add(tf.keras.layers.Conv2D(32, (3, 3), activation='relu', padding='same'))
    model.add(tf.keras.layers.MaxPooling2D((2, 2)))
    model.add(tf.keras.layers.Conv2D(64, (2, 2), activation='relu', padding='same'))
    model.add(tf.keras.layers.MaxPooling2D((2, 2)))
    model.add(tf.keras.layers.Conv2D(128, (2, 2), activation='relu', padding='same'))
    model.add(tf.keras.layers.MaxPooling2D((2, 2)))
    model.add(tf.keras.layers.Conv2D(256, (2, 2), activation='relu', padding='same'))

    model.add(tf.keras.layers.Flatten())
    model.add(tf.keras.layers.Dense(128, activation='relu'))
    model.add(tf.keras.layers.Dense(2, activation='softmax'))

    return model


def get_eer(y_pred, y_true):
    # Count number of true & false
    true_num = 0
    false_num = 0
    for i in range(len(y_true)):
        #
        if y_true[i, 0] == 1:
            true_num += 1
        else:
            false_num += 1

    # Get Equal Error Rate
    diff = len(y_pred)
    eer = 1
    for threshold in [i*0.001 for i in range(1000)]:
        false_positive_num = 0; false_negative_num = 0
        for i in range(len(y_pred)):
            # False Positive : Predict "same person", but answer is "different person"
            if (y_pred[i, 0] >= threshold) and (y_true[i, 1] == 1):
                false_positive_num += 1

            # False Negative : Predict "different person", but answer is "same person"
            if (y_pred[i, 0] < threshold) and (y_true[i, 0] == 1):
                false_negative_num += 1

        if diff > abs(false_positive_num - false_negative_num):
            diff = abs(false_positive_num - false_negative_num)
            eer = ((false_positive_num / true_num) + (false_negative_num / false_num)) / 2

        print("threshold : {}, eer : {}".format("%1.3f" % threshold, "%1.7f" % eer))

    return eer


if __name__ == "__main__":
    # Set data directories
    indir = "../../FaceDataSet/"
    train_dir = os.path.join(indir, "crop")
    test_dir = os.path.join(indir, "ncrop")
    log_dir = "logs/zinzin-ver1"
    chkpt_dir = os.path.join(indir, "zin_trained_model1")

    # Set hyper parameters
    train_epoch_num = 200000
    people_num = 50  # Note that (batch size) == 2 * (people_num)
    learning_rate = 0.01

    test_data_num = 1

    # Create model
    model = create_model()

    # Compile model
    # initial_lr = 0.01
    # end_lr = 0.0001
    # decay_steps = train_epoch_num
    # learning_rate = tf.keras.optimizers.schedules.PolynomialDecay(initial_lr, decay_steps, end_lr)
    optimizer = tf.optimizers.Adam(learning_rate=learning_rate)
    loss = tf.losses.BinaryCrossentropy()
    model.compile(optimizer=optimizer, loss=loss)

    # Load model
    load_path = os.path.join(chkpt_dir, "chkpt-200000")
    model.load_weights(load_path)

    # Create data loaders
    # train_data_loader = DataGenerator(train_dir, people_num=people_num).create_data_loader()
    test_data_loader = DataGenerator(test_dir, people_num=10).create_data_loader()

    # Create summary writer
    # writer = tf.summary.create_file_writer(logdir=log_dir)

    # training
    # for epoch, (batch_x, batch_y) in enumerate(train_data_loader.repeat(), 103000):
    #     train_loss = model.train_on_batch(batch_x, batch_y)
    #     #train_eer = get_eer(model.predict(batch_x), batch_y)
    #
    #     with writer.as_default():
    #         tf.summary.scalar("Train Loss", train_loss, step=epoch)
    #         #tf.summary.scalar("Train EER", train_eer, step=epoch)
    #
    #     if epoch != 0 and epoch % 1000 == 0:
    #         test_loss_list = []; test_eer_list = []
    #         for i, (batch_x, batch_y) in enumerate(test_data_loader):
    #             if i == test_data_num: break
    #             y_pred = model.predict(batch_x)
    #             test_loss = loss(batch_y, y_pred)
    #             test_eer = get_eer(y_pred, batch_y)
    #             test_loss_list.append(test_loss)
    #             test_eer_list.append(test_eer)
    #
    #         with writer.as_default():
    #             tf.summary.scalar("Test Loss", np.mean(test_loss_list), step=epoch)
    #             tf.summary.scalar("Test EER", np.mean(test_eer_list), step=epoch)
    #
    #     if epoch != 0 and epoch % 10000 == 0:
    #         filepath = os.path.join(chkpt_dir, "chkpt-" + str(epoch))
    #         model.save_weights(filepath)
    #
    #     if epoch >= train_epoch_num:
    #         break

    for i, (batch_x, batch_y) in enumerate(test_data_loader):
        if i == test_data_num: break
        y_pred = model.predict(batch_x)
        test_loss = loss(batch_y, y_pred)
        test_eer = get_eer(y_pred, batch_y)
        # test_loss_list.append(test_loss)
        # test_eer_list.append(test_eer)

    #print("Epoch : {}, Train Loss : {}, Train EER : {}".format(epoch, "%1.3f" % train_loss, "%1.3f" % train_eer))
    # print("Epoch : {}, Train Loss : {}".format(epoch, "%1.3f" % train_loss))
    # print("Optimization is Done!")