import tensorflow as tf
import sys
import numpy as np
import skimage

# Set constants
input_size = 28
output_size = 2

def cat_data(file_name):


def load_data(file_name):

    data = np.loadtxt(file_name, delimiter=',', dtype=np.int32)
    x = data[:, 1:].astype(np.float32) / 255  # normalize data
    x = np.reshape(x, [x.shape[0], input_size, input_size])
    x = np.expand_dims(x, -1)
    y = data[:, 0].astype(np.int32)

    img12 = skimage.io.imread("0.jpg")
    x1 = img12.load_data()
    x1 = x1 / 255.0
    img12.show()
    pix = list(img12.getdata())

    return x, y

