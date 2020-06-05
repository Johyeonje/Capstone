import tensorflow as tf
import numpy as np


class myModel(tf.keras.Model):
    def __init__(self, config):
        super(myModel, self).__init__()
        self.optimizer = config["optimizer"]
        # x : [B, 100, 100, 3]
        # Convolution layer
        self.conv = tf.keras.models.Sequential()
        self.conv.add(tf.keras.layers.Conv2D(8, (3, 3), activation='relu', padding='same'))
        # x : [B, 100, 100, 8]
        self.conv.add(tf.keras.layers.Conv2D(16, (3, 3), activation='relu'))
        # x : [B, 100, 100, 16]

        # Residual network
        # Input shape of resnet : [B, H, C, 16]
        self.resnet1 = tf.keras.models.Sequential()
        self.resnet1.add(tf.keras.layers.Conv2D(16, (3, 3), activation='relu', padding='same'))
        self.resnet1.add(tf.keras.layers.Conv2D(16, (3, 3), activation='relu', padding='same'))
        self.resnet1.add(tf.keras.layers.BatchNormalization())

        self.resnet1 = tf.keras.models.Sequential()
        self.resnet1.add(tf.keras.layers.Conv2D(16, (3, 3), activation='relu', padding='same'))
        self.resnet1.add(tf.keras.layers.Conv2D(16, (3, 3), activation='relu', padding='same'))
        self.resnet1.add(tf.keras.layers.BatchNormalization())

        self.resnet1 = tf.keras.models.Sequential()
        self.resnet1.add(tf.keras.layers.Conv2D(16, (3, 3), activation='relu', padding='same'))
        self.resnet1.add(tf.keras.layers.Conv2D(16, (3, 3), activation='relu', padding='same'))
        self.resnet1.add(tf.keras.layers.BatchNormalization())

        # Last layer
        self.last = tf.keras.models.Sequential()
        self.last.add(tf.keras.layers.Flatten())
        self.last.add(tf.keras.layers.Dense(10, activation='softmax'))

    def call(self, x, training=False):
        x = self.conv(x)
        x = self.resnet1(x, training) + x
        x = self.last(x)
        return x

