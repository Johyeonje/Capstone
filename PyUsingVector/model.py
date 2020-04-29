import tensorflow as tf
from sklearn.metrics import roc_curve
import numpy as np


class FaceEmbedder(tf.keras.Model):
    def __init__(self, config):
        super(FaceEmbedder, self).__init__()

        self.train_person_num = config["train_person_num"]
        self.train_face_num = config["train_face_num"]
        self.embedding_dim = config["embedding_dim"]
        self.apply_gradient_clipping = config["apply_gradient_clipping"]
        self.gradient_clip_norm = config["gradient_clip_norm"]

        # Set loss function
        self.loss_type = config["loss_type"]
        if not self.loss_type in ["ge2e", "binary_cross_entropy"]:
            raise ValueError("'loss_type' must be 'ge2e' or 'binary_cross_entropy', not %s", self.loss_type)

        # Set optimizer
        self.optimizer = config["optimizer"]

        # Create network
        # 여기부터 ~
        self.conv1 = tf.keras.Sequential([
            tf.keras.layers.Conv2D(filters=8, kernel_size=3, activation=None),
            tf.keras.layers.ReLU(),
            tf.keras.layers.Conv2D(filters=8, kernel_size=3, activation=None),
            tf.keras.layers.ReLU()
        ])
        self.batch_norm1 = tf.keras.layers.BatchNormalization()

        self.conv2 = tf.keras.Sequential([
            tf.keras.layers.Conv2D(filters=16, kernel_size=3, activation=None),
            tf.keras.layers.ReLU(),
            tf.keras.layers.Conv2D(filters=16, kernel_size=3, activation=None),
            tf.keras.layers.ReLU()
        ])
        self.batch_norm2 = tf.keras.layers.BatchNormalization()

        self.conv3 = tf.keras.Sequential([
            tf.keras.layers.Conv2D(filters=32, kernel_size=3, activation=None),
            tf.keras.layers.ReLU(),
            tf.keras.layers.Conv2D(filters=64, kernel_size=3, activation=None),
            tf.keras.layers.ReLU()
        ])
        self.batch_norm3 = tf.keras.layers.BatchNormalization()

        # 여기까지 수정 가능

        self.dense = tf.keras.Sequential([
            tf.keras.layers.Flatten(),
            tf.keras.layers.Dense(units=self.embedding_dim, activation=None)
        ])


    def call(self, inputs, training=False, mask=None):
        x = self.conv1(inputs)
        x = self.batch_norm1(x, training=training)
        x = self.conv2(x)
        x = self.batch_norm2(x, training=training)
        x = self.conv3(x)
        x = self.batch_norm3(x, training=training)
        x = self.dense(x)
        x = tf.math.l2_normalize(x, axis=1)
        return x

    def evaluate(self, inputs, person_num, face_num, epsilon=1e-10):
        # Get embeddings
        embeddings = self.call(inputs, training=False)

        # Get centroids
        # 등록파트
        # 사람별 벡터 합침
        centroids = np.reshape(embeddings, [person_num, face_num, self.embedding_dim])
        # 더함
        centroids = np.sum(centroids, axis=1)
        # 다 더해지면 너무 크니까 조절
        centroids = centroids / np.linalg.norm(centroids, ord=2)

        # Get label
        label = np.zeros(shape=[person_num, face_num, person_num], dtype=np.float32)
        for i in range(person_num):
            label[i, :, i] = 1
        label = np.reshape(label, [person_num * face_num, person_num])

        # Calculate similarity matrix S
        # shape of S : [train_person_num * train_face_num, train_person_num]
        # 내적  ex ) (x1, x2), (y1, y2) => x1y1 + x2y2
        S = np.matmul(embeddings, centroids.T)

        # Calculate loss
        if self.loss_type == "ge2e":
            # Calculate original generalized end-to-end (GE2E) loss
            softmax = np.sum(np.exp(S), axis=-1, keepdims=True)
            softmax = np.where(softmax > epsilon, softmax, np.zeros_like(softmax) + epsilon)
            softmax = -S + np.log(softmax)  # log-softmax
            loss = np.mean(label * softmax)
        elif self.loss_type == "binary_cross_entropy":
            # Calculate binary crossentropy GE2E loss
            softmax = np.sum(np.exp(S), axis=-1, keepdims=True)
            softmax = np.clip(S / softmax, epsilon, 1 - epsilon)
            loss = -np.mean(label * np.log(softmax) + (1 - label) * np.log(1 - softmax))

        # Calculate accuracy
        correct_num = 0
        S = np.reshape(S, [-1])
        label = np.reshape(label, [-1])
        for i in range(person_num * face_num):
            if (label[i] == 0.0) and (S[i] < 0.5):
                correct_num += 1
            elif (label[i] == 1.0) and (S[i] >= 0.5):
                correct_num += 1
        accuracy = correct_num / (person_num * face_num)

        # Calculate EER
        fpr, tpr, thresholds = roc_curve(label, S)
        fnr = 1 - tpr
        idx = np.argmin(np.abs(fnr - fpr))
        eer = fnr[idx]

        return accuracy, eer, loss

    def train_on_batch(self, batch_x, batch_y, epsilon=1e-10):
        with tf.GradientTape() as tape:
            # Create embeddings
            # shape of embeddings : [train_person_num * train_face_num, embedding_dim]
            embeddings = self.call(batch_x, training=True)

            # Create centroid
            # shape of centroids : [train_person_num, embedding_dim]
            centroids = tf.reshape(embeddings, [self.train_person_num, self.train_face_num, self.embedding_dim])
            centroids = tf.reduce_sum(centroids, axis=1)
            centroids = tf.math.l2_normalize(centroids, axis=1)

            # Calculate similarity matrix S
            # shape of S : [train_person_num * train_face_num, train_person_num]
            S = tf.matmul(embeddings, tf.transpose(centroids))

            if self.loss_type == "ge2e":
                # Calculate original generalized end-to-end (GE2E) loss
                softmax = tf.reduce_sum(tf.exp(S), axis=-1, keepdims=True)
                softmax = tf.where(softmax > epsilon, softmax, tf.zeros_like(softmax) + epsilon)
                softmax = -S + tf.math.log(softmax)  # log-softmax
                loss = tf.reduce_mean(batch_y * softmax)
            elif self.loss_type == "binary_cross_entropy":
                # Calculate binary crossentropy GE2E loss
                softmax = tf.reduce_sum(tf.exp(S), axis=-1, keepdims=True)
                softmax = tf.clip_by_value(S / softmax, epsilon, 1 - epsilon)
                loss = -tf.reduce_mean(batch_y * tf.math.log(softmax) + (1 - batch_y) * tf.math.log(1 - softmax))

            # Calculate gradients
            gradients = tape.gradient(loss, self.trainable_variables)
            # if self.apply_gradient_clipping:
            #    gradients = tf.clip_by_norm(gradients, clip_norm=self.gradient_clip_norm)  # gradient clipping

            # Apply gradients
            self.optimizer.apply_gradients(zip(gradients, self.trainable_variables))

        return loss