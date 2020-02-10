import os
import argparse
import datetime
import tensorflow as tf
from scripts.model import FaceEmbedder
from scripts.loss import *
from utils.hparams import HParam
from utils.dataloader import create_dataloader

# Parsing arguments
parser = argparse.ArgumentParser()
parser.add_argument('-c', '--config', required=True, help='configuration file')
args = parser.parse_args()

# Set hyper parameters
hp = HParam(args.config)

# Create summary writer
current_time = datetime.datetime.now().strftime("%Y%m%d-%H%M")
log_dir = os.path.join(hp.dir.log_dir, current_time)
writer = tf.summary.create_file_writer(log_dir)

# Get file lists
data_list = os.listdir(hp.data.data_path)
train_list = data_list[0:hp.data.train_num]
test_list = data_list[hp.data.train_num:]

# Create data loaders
train_data_loader = create_dataloader(hp, train_list)
test_data_loader = create_dataloader(hp, test_list)

# Build model
model = FaceEmbedder(hp)

# Compile model
optimizer = tf.optimizers.Adam(learning_rate=hp.train.learning_rate)
model.compile(optimizer)

# train model
model.fit(train_data_loader, test_data_loader, hp, writer)