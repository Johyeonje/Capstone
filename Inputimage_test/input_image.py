import tensorflow as tf
import numpy as np

# Set constants
input_size = 100
output_size = 2


def load_data(file_name):
    data = np.loadtxt(file_name, delimiter=',', dtype=np.int32)
    x = data[:, 1:].astype(np.float32) / 255  # normalize data
    x = np.reshape(x, [x.shape[0], input_size, input_size])
    x = np.expand_dims(x, -1)
    y = data[:, 0].astype(np.int32)

    img = pilimg.open()
    img.show()

    pix = list(img.getdata())

    return x, y


def create_model():
    model = tf.keras.models.Sequential()
    model.add(tf.keras.layers.Conv2D(32, (3, 3), activation='relu', padding='same'))
    model.add(tf.keras.layers.Conv2D(32, (3, 3), activation='relu', padding='same'))
    model.add(tf.keras.layers.MaxPooling2D((3, 3)))
    model.add(tf.keras.layers.Conv2D(32, (3, 3), activation='relu', padding='same'))
    model.add(tf.keras.layers.Conv2D(32, (3, 3), activation='relu', padding='same'))
    model.add(tf.keras.layers.Flatten())
    model.add(tf.keras.layers.Dense(100, activation='relu'))
    model.add(tf.keras.layers.Dense(output_size, activation='softmax'))

    model.compile(optimizer='adam', loss='binary_crossentropy', metrics=['accuracy'])

    return model


def convert_data(x, y, num=100):
    new_x = np.zeros(shape=[num, 2 * input_size, input_size, 1], dtype=np.float32)
    new_y = np.zeros(shape=[num, 2], dtype=np.int32)  # [0,0] --> [1, 0], [0, 1]

    for i in range(num):
        idx1 = np.random.randint(0, len(x))
        idx2 = np.random.randint(0, len(x))
        new_x[i, 0:input_size, :, :] = x[idx1]
        new_x[i, input_size:, :, :] = x[idx2]
        if y[idx1] == y[idx2]:
            new_y[i, 0] = 1
        else:
            new_y[i, 1] = 1

    return new_x, new_y


if __name__ == "__main__":
    # Load data
    train_x, train_y = load_data("mnist_train.csv")
    test_x, test_y = load_data("mnist_test.csv")

    # Convert data
    train_x, train_y = convert_data(train_x, train_y, num=100000)
    test_x, test_y = convert_data(test_x, test_y, num=1000)

    # Create model
    model = create_model()

    # Train model
    model.fit(train_x, train_y, epochs=5)

    # Evaluate
    test_loss, test_acc = model.evaluate(test_x, test_y)
    print(test_loss, test_acc)
