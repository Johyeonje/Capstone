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

def load_data(path):
    data = dict()
    id_list = os.listdir(path)
    for id in id_list:
        image_name_list = os.listdir(os.path.join(path, id))
        data[id] = [load_image(os.path.join(path, id, image_name)) for image_name in image_name_list]

    return data

def load_image(filename):
    img = cv2.imread(filename, flags=cv2.IMREAD_COLOR).astype(np.float32)
    img /= 255.0
    return img

def make_batch(id_list, data, dtype=np.float32):
    x = list()
    y = list()
    batch_num = 10
    age_list = os.listdir(data_path)
    for i in range(batch_num):
        id = random.choice(age_list)            # choice age
        id_path = os.path.join(data_path, id)
        image_list = os.listdir(id_path)        # choice image
        image_id = random.choice(image_list)
        image_path = os.path.join(id_path, image_id)
        image = load_image(image_path)          # load image

        # make label
        label = np.zeros(shape=[12,1], dtype=np.float32)
        label[(int(id)-1),0] = 1
        print(y)

        x.append(image)
        y.append(label)

        print(x.shape)
        print(y.shape)

    return np.array(x).astype(dtype), np.array(y).astype(dtype)

if __name__ == "__main__":
    data_path = "D:/Study/All-Age-Faces/F_crop"      #data path

    id_list, data = load_data(data_path)

    batch_x, batch_y = make_batch(id_list, data)