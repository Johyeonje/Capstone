import tensorflow as tf
import utils
import argparse
import os
from model import FaceEmbedder
import numpy as np


def get_filename_dict(data_dir):
    filename_dict = dict()
    for person_id in os.listdir(data_dir):
        tmp = []
        path = os.path.join(data_dir, person_id)
        for filename in os.listdir(path):
            filepath = os.path.join(path, filename)
            tmp.append(filepath)
        filename_dict[person_id] = tmp
    return filename_dict


if __name__ == "__main__":
    # Parsing arguments
    parser = argparse.ArgumentParser()
    parser.add_argument("--data_dir", default="../../FaceDataSet/ncrop", help="Data directory")
    parser.add_argument("--chkpt_dir", default="../../FaceDataSet/train_model0420")
    parser.add_argument("--train_person_num", default=5, type=int, help="하나의 훈련용 배치를 구성할 사람의 수")
    parser.add_argument("--train_face_num", default=3, type=int, help="하나의 훈련용 배치를 구성할 사람마다 사용할 얼굴 사진의 수")
    #parser.add_argument("--test_person_num", default=5, type=int, help="하나의 평가용 배치를 구성할 사람의 수")
    #parser.add_argument("--test_face_num", default=3, type=int, help="하나의 가용 배치를 구성할 사람마다 사용할 얼굴 사진의 수")
    args = parser.parse_args()

    # Set optimizer
    lr_schedule = tf.keras.optimizers.schedules.PolynomialDecay(
        initial_learning_rate=0.01,
        decay_steps=1000,
        end_learning_rate=0.001
    )
    optimizer = tf.keras.optimizers.Adam(learning_rate=lr_schedule)

    # Configuration
    config = {
        "train_person_num" : args.train_person_num,
        "train_face_num" : args.train_face_num,
        "embedding_dim" : 256,
        "apply_gradient_clipping" : True,
        "gradient_clip_norm" : 1,
        "loss_type" : "ge2e",
        # "loss_type" : "binary_cross_entropy",
        "optimizer" : optimizer
    }

    # Get file dictionary
    filename_dict = get_filename_dict(args.data_dir)

    # Build model
    model = FaceEmbedder(config)

    # Create label
    batch_y = np.zeros(shape=[args.train_person_num, args.train_face_num, args.train_person_num], dtype=np.float32)
    for i in range(args.train_person_num):
        batch_y[i, :, i] = 1
    batch_y = np.reshape(batch_y, [args.train_person_num*args.train_face_num, args.train_person_num])

    # Create summary writer
    writer = tf.summary.create_file_writer(logdir=log_dir)

    # Train
    for epoch in range(10):
        train_batch_x = utils.get_batch(filename_dict, args.train_person_num, args.train_face_num, train=True)

        train_loss = model.train_on_batch(train_batch_x, batch_y)

        train_acc, train_eer = model.get_eer(train_batch_x, args.train_person_num, args.train_face_num)

        with writer.as_default():
            tf.summary.scalar("Train Loss", train_loss, step=epoch)
            tf.summary.scalar("Train EER", train_eer, step=epoch)

        if epoch != 0 and epoch % 1000 == 0:
            test_loss_list = []; test_eer_list = []
            test_batch_x = utils.get_batch(filename_dict, args.train_person_num, args.train_face_num, train=False)

            test_loss = model.predict(test_batch_x)
            test_acc, test_eer = model.get_eer(test_batch_x, args.train_person_num, args.train_face_num)
            test_loss_list.append(test_loss)
            test_eer_list.append(test_eer)

            with writer.as_default():
                tf.summary.scalar("Test Loss", np.mean(test_loss_list), step=epoch)
                tf.summary.scalar("Test EER", np.mean(test_eer_list), step=epoch)

        if epoch != 0 and epoch % 10000 == 0:
            filepath = os.path.join(args.chkpt_dir, "chkpt-" + str(epoch))
            model.save_weights(filepath)

        print("Epoch : {}, Train Loss : {}, Train Acc : {}, Train EER : {}".format(epoch, "%1.4f" % train_loss, "%1.4f" % train_acc, "%1.4f" % train_eer))

    print("Optimization is Done!")
