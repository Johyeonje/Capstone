import tensorflow as tf
import numpy as np
import matplotlib.pyplot as plt
import cv2
import pathlib

# load data, nomalization
def load_data(load_path):
  all_image_path = list(load_path.glob('*/*'))
  # print(images[:10])
  train_X = []
  train_Y = []
  for i, image_path in enumerate(all_image_path):
    image = cv2.imread(image_path)
    image = cv2.resize(image, dsize=(50, 50))
    image /= 255.0
    train_X.append(image)
    path = image_path[-6:-4]
    train_Y.append(path)
  train_X = np.array(train_X)

  print(train_X.shape, len(train_Y))

  return train_X, train_Y

# train data augmentation
def augmentation(train_X, train_Y):
  image_generator = ImageDataGenerator(
      rotation_range = 10,
      zoom_range = 0.10,
      shear_range = 0.5,
      width_shift_range=0.10,
      height_shift_range=0.10,
      horizontal_flip=False,
      vertical_flip=False)

  augment_size = 500

  randidx = np.random.randint(train_X.shape[0], size=augment_size)
  x_augmented = train_X[randidx].copy()
  y_augmented = train_Y[randidx].copy()
  x_augmented = image_generator.flow(x_augmented, np.zeros(augment_size), batch_size=augment_size, shuffle=False).next()[0]

  train_X = np.concatenate((train_X, x_augmented))
  train_Y = np.concatenate((train_Y, y_augmented))

  print(train_X.shape)
  return train_X, train_Y


# build model
def build_model():
    model = tf.keras.Sequential([
        tf.keras.layers.Conv2D(input_shape=(50, 50, 1), kernel_size=(3, 3), padding='same', filters=32,
                               activation='relu'),
        tf.keras.layers.Conv2D(kernel_size=(3, 3), filters=64, padding='same', activation='relu'),
        tf.keras.layers.MaxPool2D(pool_size=(2, 2)),
        tf.keras.layers.Dropout(rate=0.5),
        tf.keras.layers.Conv2D(kernel_size=(3, 3), filters=128, padding='same', activation='relu'),
        tf.keras.layers.Conv2D(kernel_size=(3, 3), filters=256, padding='valid', activation='relu'),
        tf.keras.layers.MaxPool2D(pool_size=(2, 2)),
        tf.keras.layers.Dropout(rate=0.5),
        tf.keras.layers.Flatten(),
        tf.keras.layers.Dense(units=512, activation='relu'),
        tf.keras.layers.Dropout(rate=0.5),
        tf.keras.layers.Dense(units=256, activation='relu'),
        tf.keras.layers.Dropout(rate=0.5),
        tf.keras.layers.Dense(units=16, activation='softmax')
    ])

    model.compile(optimizer=tf.keras.optimizers.Adam(),
                  loss='sparse_categorical_crossentropy',
                  metrics=['accuracy'])
    return model


if __name__ == '__main__':
    load_path = pathlib.Path('')
    # load data
    train_X, train_Y = load_data(load_path)

    # data augmentation
    train_X, train_Y = augmentation(train_X, train_Y)

    # model build
    model = build_model()

    # model fitting
    history = model.fit(train_X, train_Y, epochs=10, validation_split=0.25, batch_size=32)

    # history visualization
    plt.figure(figsize=(12, 4))
    plt.subplot(1, 2, 1)
    plt.plot(history.history['loss'], 'b-', label='loss')
    plt.plot(history.history['val_loss'], 'r--', label='val_loss')
    plt.xlabel('Epoch')
    plt.legend()

    plt.subplot(1, 2, 2)
    plt.plot(history.history['accuracy'], 'g-', label='accuracy')
    plt.plot(history.history['val_accuracy'], 'k--', label='val_accuracy')
    plt.xlabel('Epoch')
    plt.ylim(0, 0.1)
    plt.legend()

    plt.show()
    model.summary()