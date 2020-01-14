import os
import cv2
import tensorflow as tf
from inception_resnet_v2 import inception_resnet_v2
from random import shuffle
import numpy as np

# parameters
IMG_H, IMG_W, IMG_C = 160, 160, 3
embedding_size = 512
num_class = 500
learning_rate = 1e-3
regularization_factor = 1e-2
max_epoch = 200
batch_size = 128


class loader():
    def __init__(self, dir_name, img_h, img_w):
        label = 0
        self.index = 0
        self.batch_list = []
        self.cnt = 0
        self.epoch = 0
        self.img_h, self.img_w = img_h, img_w
        for root, dirs, files in os.walk(dir_name):
            if files:
                for file_name in files:
                    path = os.path.join(root, file_name)
                    self.batch_list.append([path, label])
                label += 1

        self.size = len(self.batch_list)
        shuffle(self.batch_list)

    def batch(self, n):
        batch_image, batch_label = [], []
        for i in range(n):
            image = cv2.imread(self.batch_list[self.index][0])
            image = cv2.resize(image, (self.img_w, self.img_h))
            batch_image.append(image)
            batch_label.append(self.batch_list[self.index][1])

            self.index += 1
            if self.index > self.size:
                shuffle(self.batch_list)
                self.epoch += 1
                self.index = 0

        return batch_image, batch_label

with tf.Graph().as_default() as g:
    image_placeholder = tf.placeholder(tf.float32, [None, IMG_H, IMG_W, IMG_C], name='image')
    label_placeholder = tf.placeholder(tf.int32, [None], name='label')
    istrain_placeholder = tf.placeholder(tf.bool)
    dropout_placeholder = tf.placeholder(tf.float32)

    embedding, end_points = inception_resnet_v2(image_placeholder, istrain_placeholder, dropout_placeholder,
                                                embedding_size)
    prelogit = tf.layers.dense(embedding, num_class)

    with tf.name_scope('loss'):
        softmax_loss = tf.reduce_mean(
            tf.nn.sparse_softmax_cross_entropy_with_logits(logits=prelogit, labels=label_placeholder))
        l2_regularization_loss = tf.add_n([tf.nn.l2_loss(var) for var in tf.get_collection('trainable_variables')])
        total_loss = softmax_loss + regularization_factor * l2_regularization_loss

    with tf.name_scope('optimizer'):
        opt = tf.train.AdamOptimizer(learning_rate).minimize(total_loss)

    with tf.name_scope('saver'):
        saver = tf.train.Saver(max_to_keep=1)

dir_image = 'D:/Study/FaceDataSet/vgg_face2/train'
dir_model = 'model_face'
data_loader = loader(dir_image, IMG_H, IMG_W)

# training part
with tf.Session(graph=g) as sess:
    checkpoint = tf.train.latest_checkpoint(dir_model)
    if checkpoint:
        saver.restore(sess, checkpoint)
    else:
        sess.run(tf.global_variables_initializer())

    while data_loader.epoch < max_epoch:
        batch_image, batch_label = data_loader.batch(batch_size)
        feed_dict = {
            image_placeholder: batch_image,
            label_placeholder: batch_label,
            istrain_placeholder: True,
            dropout_placeholder: 0.8
        }
        _, loss = sess.run([opt, total_loss], feed_dict=feed_dict)
        print(loss)

    saver.save(sess, os.path.join(dir_model, 'model.ckpt'))

# test part
with tf.Session(graph=g) as sess:
    checkpoint = tf.train.latest_checkpoint(dir_model)
    saver.restore(sess, checkpoint)

    face1 = cv2.resize(cv2.imread('0001_01.jpg'), (IMG_W, IMG_H))
    face2 = cv2.resize(cv2.imread('0002_01.jpg'), (IMG_W, IMG_H))

    feed_dict = {
        image_placeholder: [face1, face2],
        istrain_placeholder: False,
        dropout_placeholder: 1
    }
    embedding_vector = sess.run(embedding, feed_dict=feed_dict)

#inner_product calculation
prod_sum = np.sum(np.multiply(embedding_vector[0], embedding_vector[1]))
norm0 = np.sqrt(np.sum(embedding_vector[0] ** 2))
norm1 = np.sqrt(np.sum(embedding_vector[1] ** 2))
inner_product = prod_sum / norm0 / norm1