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
    img = cv2.imread(filename, flags=cv2.IMREAD_COLOR).astype(np.float32)
    img /= 255.0
    return img

def make_batch(img):
    batch_num = 10
    data_path = "D:\\"      #data path
    age_list = os.listdir(data_path)
    for i in range(batch_num):
        id = random.choice(age_list)            # choice age
        id_path = os.path.join(data_path, id)
        image_list = os.listdir(id_path)        # choice image
        image_id = random.chice(image_list)
        image_path = os.path.join(id_path, image_id)
        image = load_image(image_path)          # load image
        x.append(image)

        # make label
        label = np.zeros(shape=[12,1], dtype=np.float32)
        label[(int(id)-1),0] = 1
        y.append(label)

        x, y = np.array(x), np.array(y)

        print(x.shape)
        print(y.shape)

    return x, y

if __name__ == "__main__":

    batch_x = make_batch();