import os
import cv2
import random
import itertools
import numpy as np
import tensorflow as tf


# Set global variables
width = 100
height = 100
n_channels = 3


def load_image(filename):
    """ Function to load image file """
    img = cv2.imread(filename, flags=cv2.IMREAD_COLOR).astype(np.float32)
    img /= 255.0  # normalize image
    return img


def get_test_batch():
    input_img = []
    enroll_img =[]
    for i in range(7):
        id = random.choice(os.listdir(indir))
        path = os.path.join(indir, id)

        img_name = os.path.join(path, random.choice(os.listdir(path)))
        img = load_image(img_name)
        input_img.append(img)

        img_name = os.path.join(path, random.choice(os.listdir(path)))
        img = load_image(img_name)
        enroll_img.append(img)

    batch_x = []
    for i in range(7):
        for j in range(7):
            img = cv2.vconcat([input_img[i], enroll_img[j]])
            batch_x.append(img)

    batch_x = np.array(batch_x)
    return batch_x


def create_model():
    model = tf.keras.models.Sequential()
    model.add(tf.keras.layers.Conv2D(32, (3, 3), activation='relu', padding='same', input_shape=(100, 200, 3)))
    model.add(tf.keras.layers.MaxPooling2D((2, 2)))
    model.add(tf.keras.layers.Conv2D(64, (3, 3), activation='relu', padding='same'))
    model.add(tf.keras.layers.MaxPooling2D((2, 2)))
    model.add(tf.keras.layers.Conv2D(128, (3, 3), activation='relu', padding='same'))
    model.add(tf.keras.layers.MaxPooling2D((2, 2)))
    model.add(tf.keras.layers.Conv2D(256, (2, 2), activation='relu', padding='same'))
    model.add(tf.keras.layers.Conv2D(256, (2, 2), activation='relu', padding='same'))
    model.add(tf.keras.layers.Conv2D(256, (2, 2), activation='relu', padding='same'))
    model.add(tf.keras.layers.Conv2D(256, (2, 2), activation='relu', padding='same'))
    model.add(tf.keras.layers.MaxPooling2D((2, 2)))
    model.add(tf.keras.layers.Conv2D(512, (2, 2), activation='relu', padding='same'))

    # 출력층(Dense) 추가
    model.add(tf.keras.layers.Flatten())
    model.add(tf.keras.layers.Dense(256, activation='relu'))
    model.add(tf.keras.layers.Dense(2, activation='softmax'))

    return model


if __name__ == "__main__":
    # Set data directories
    indir = "../../FaceDataSet/ncrop"
    chkpt_dir = "../../FaceDataSet/trained_model2/"

    # Create model
    model = create_model()
    optimizer = tf.optimizers.Adam(learning_rate=0.001)
    loss = tf.losses.BinaryCrossentropy()
    model.compile(optimizer=optimizer, loss=loss)

    # Load model
    model_name = os.path.join(chkpt_dir, "model-epoch-100000_v2")
    model.load_weights(model_name).expect_partial()

    # Read data
    batch_x = get_test_batch()

    # Test
    y_pred = model(batch_x)
    y_pred = tf.reshape(y_pred, [7, 7, 2])

    answer = np.eye(7)
    print(answer)
    for threshold in [i * 0.001 for i in range(900, 1000)]:
        result = []
        for i in range(7):
            tmp = []
            for j in range(7):
                if y_pred[i, j, 0] > threshold:
                    tmp.append(1)
                else:
                    tmp.append(0)
            result.append(tmp)
        result = np.array(result)
        acc = np.sum(np.equal(result, answer).astype(np.float32)) / 49
        print("Threshold : {}, Accuracy : {}".format('%0.4f' % threshold, '%1.4f' % acc))

    print(result)

