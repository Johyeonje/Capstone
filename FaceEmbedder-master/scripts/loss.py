import tensorflow as tf
import numpy as np


def normalize(x):
    """ normalize the last dimension vector of the input matrix
    :return: normalized input
    """
    norm = tf.sqrt(tf.reduce_sum(x**2, axis=-1, keepdims=True) + 1e-6)
    return x / norm


def get_similarity_mat(embedded, N=7, M=3, center=None):
    """ Calculate similarity matrix from embedded utterance batch (NM x embed_dim) eq. (9)
        Input center to test enrollment. (embedded for verification)
    :return: tf similarity matrix (NM x N)
    """
    embedded_split = tf.reshape(embedded, shape=[N, M, -1])

    if center is None:
        center = normalize(tf.reduce_mean(embedded_split, axis=1))              # [N,P] normalized center vectors eq.(1)
        center_except = normalize(tf.reshape(tf.reduce_sum(embedded_split, axis=1, keepdims=True)
                                             - embedded_split, shape=[N*M,-1]))  # [NM,P] center vectors eq.(8)
        # make similarity matrix eq.(9)
        S = tf.concat(
            [tf.concat([tf.reduce_sum(center_except[i*M:(i+1)*M,:]*embedded_split[j,:,:], axis=1, keepdims=True) if i==j
                        else tf.reduce_sum(center[i:(i+1),:]*embedded_split[j,:,:], axis=1, keepdims=True) for i in range(N)],
                       axis=1) for j in range(N)], axis=0)
    else :
        # If center(enrollment) exist, use it.
        S = tf.concat(
            [tf.concat([tf.reduce_sum(center[i:(i + 1), :] * embedded_split[j, :, :], axis=1, keepdims=True) for i
                        in range(N)],
                       axis=1) for j in range(N)], axis=0)

    return S


def calculate_loss(S, N=7, M=3):
    """ Calculate loss with similarity matrix S
        Use softmax method
    """
    S_correct = tf.concat([S[i*M:(i+1)*M, i:(i+1)] for i in range(N)], axis=0)  # colored entries

    total = tf.reduce_sum(tf.exp(S), axis=1, keepdims=True) + 1e-6
    total = -tf.reduce_sum(S_correct - tf.math.log(total))

    return total


