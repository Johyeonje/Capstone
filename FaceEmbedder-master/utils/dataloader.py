import tensorflow as tf
import numpy as np
import cv2
import os
import random
import itertools


def load_image(filename):
    img = cv2.imread(filename, cv2.IMREAD_COLOR)
    img = img.astype(np.float32)
    img /= 255
    return img


class DataGenerator:
    def __init__(self, hp, id_list):
        self.hp = hp
        self.id_list = id_list

    def gen(self):
        N = self.hp.train.img_num

        for iter in itertools.count(1):
            id = self.id_list[iter]
            path = os.path.join(self.hp.data.data_path, id)
            x = np.zeros(shape=[N, self.hp.data.size, self.hp.data.size, 3], dtype=np.float32)
            for n in range(N):
                filename = os.path.join(path, random.choice(os.listdir(path)))
                x[n, :, :, :] = load_image(filename)

            x = np.array(x)
            yield x


def create_dataloader(hp, id_list):
    x_shape = [hp.train.img_num, hp.data.size, hp.data.size, 3]
    generator = DataGenerator(hp, id_list)
    dataloader = tf.data.Dataset.from_generator(
        generator=generator.gen,
        output_types=(tf.float32),
        output_shapes=(tf.TensorShape(x_shape))
    )

    return dataloader.repeat().batch(hp.train.people_num)

