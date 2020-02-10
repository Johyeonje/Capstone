import tensorflow as tf
from tensorflow.keras.layers import Conv2D, MaxPool2D, Flatten, Dense
from .loss import *
import os


class FaceEmbedder(tf.keras.Model):
    def __init__(self, hp):
        super(FaceEmbedder, self).__init__()
        self.hp = hp
        self.model = tf.keras.Sequential([
            Conv2D(filters=8, kernel_size=[2,2], strides=[1,1], padding='same', activation='relu'),
            MaxPool2D(pool_size=[2,2]),
            Conv2D(filters=16, kernel_size=[2,2], strides=[1,1], padding='same', activation='relu'),
            MaxPool2D(pool_size=[2,2]),
            Conv2D(filters=32, kernel_size=[2, 2], strides=[1, 1], padding='same', activation='relu'),
            MaxPool2D(pool_size=[2, 2]),
            Conv2D(filters=64, kernel_size=[2, 2], strides=[1, 1], padding='same', activation='relu'),
            MaxPool2D(pool_size=[2, 2]),
            Flatten(),
            Dense(units=2048, activation='relu'),
            Dense(units=1024, activation='relu'),
            Dense(units=hp.model.emb_dims, activation='sigmoid')
        ])

    def compile(self, optimizer):
        self.optimizer = optimizer

    def call(self, x):
        return self.model(x)

    def train_on_batch(self, x):
        if self.optimizer is None:
            raise TypeError("Optimizer is Nonetype! Compile model first!")

        with tf.GradientTape() as tape:
            embeddings = self.model(x)
            similarities = get_similarity_mat(embeddings, N=self.hp.train.people_num, M=self.hp.train.img_num, center=None)
            loss_value = calculate_loss(similarities)
        grads = tape.gradient(loss_value, self.trainable_variables)
        self.optimizer.apply_gradients(zip(grads, self.trainable_variables))
        return loss_value

    def evaluate(self, x, center):
        embeddings = self.model(x)
        similarities = get_similarity_mat(embeddings, N=self.hp.test.people_num, M=self.hp.test.img_num, center=center)
        loss_value = calculate_loss(similarities)
        return loss_value

    def fit(self, train_dataloader, test_dataloader, hp, writer):
        train_epoch_num = hp.train.train_epoch_num
        learning_rate = hp.train.learning_rate

        for epoch in range(1, train_epoch_num+1):
            N = self.hp.train.people_num
            M = self.hp.train.img_num
            train_loss = []
            for x in train_dataloader.take(1):
                x = tf.reshape(x, shape=[M * N, hp.data.size, hp.data.size, 3])
                loss = self.train_on_batch(x)
                train_loss.append(loss)

            with writer.as_default():
                train_loss = np.mean(train_loss)
                tf.summary.scalar('train_loss', train_loss, step=epoch)

            print('Epoch : {}, Train loss : {}'.format(epoch, '%2.02f' % train_loss))

            """
            if epoch % hp.log.summary_interval == 0:
                test_loss = []
                for x in test_dataloader.take(1):
                    x = tf.reshape(x, shape=[])
                with writer.as_default():
                    test_loss = []

                    tf.summary.scalar('test_loss', test_loss, step=epoch)
            """

            if epoch % hp.train.save_interval == 0:
                chkpt_name = os.path.join(self.hp.dir.chkpt_dir, 'checkpoint-' + str(epoch))
                self.save_weights(chkpt_name)
                