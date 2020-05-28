import tensorflow as tf
import numpy as np


class myModel(tf.keras.Model):
    def __init__(self):
        # x : [B, 100, 100, 3]
        # Convolution layer
        self.conv = tf.keras.models.Sequential()
        self.conv.add(tf.keras.layers.Conv2d(8, (3, 3), activation='relu', padding='same'))
        # x : [B, 100, 100, 8]
        self.conv.add(tf.keras.layers.Conv2d(16, (3, 3), activation='relu'))
        # x : [B, 100, 100, 16]

        # Residual network
        # Input shape of resnet : [B, H, C, 16]
        self.resnet1 = tf.kears.models.Sequential()
        self.resnet1.add(tf.keras.layers.Conv2d(16, (3, 3), activation='relu', padding='same'))
        self.resnet1.add(tf.keras.layers.Conv2d(16, (3, 3), activation='relu', padding='same'))
        self.resnet1.add(tf.keras.layers.BatchNormalize())

        self.resnet1 = tf.kears.models.Sequential()
        self.resnet1.add(tf.keras.layers.Conv2d(16, (3, 3), activation='relu', padding='same'))
        self.resnet1.add(tf.keras.layers.Conv2d(16, (3, 3), activation='relu', padding='same'))
        self.resnet1.add(tf.keras.layers.BatchNormalize())

        self.resnet1 = tf.kears.models.Sequential()
        self.resnet1.add(tf.keras.layers.Conv2d(16, (3, 3), activation='relu', padding='same'))
        self.resnet1.add(tf.keras.layers.Conv2d(16, (3, 3), activation='relu', padding='same'))
        self.resnet1.add(tf.keras.layers.BatchNormalize())

        # Last layer
        self.last = tf.keras.models.Sequential()
        self.last.add(tf.kears.layers.Flatten())
        self.last.add(tf.keras.layers.Dense(10))

    def call(self, x, training=False):
        x = self.conv(x)
        x = self.resnet1(x, training) + x
        x = self.last(x)
        return x

    def train_on_batch(self, batch_x, batch_y):
        y_pred = self.call(batch_x, training=True)
        loss = self.loss(batch_y, y_pred)
        
        return loss
