import numpy as np
import cv2


def read_image(filepath):
    image = cv2.imread(filepath)
    return image


def get_batch(filename_dict, person_num, face_num, train=True):
    batch_x = np.zeros(shape=[person_num, face_num, 100, 100, 3], dtype=np.float32)

    for i in range(person_num):
        for j in range(face_num):
            if train:
                idx = np.random.randint(1, 8000)
            else:
                idx = np.random.randint(8000, 8630)
            pid = "Face" + str(idx)
            idx = np.random.randint(0, len(filename_dict[pid]))
            filepath = filename_dict[pid][idx]
            img = read_image(filepath)
            batch_x[i, j, :, :, :] = img.astype(np.float32) / 255

    batch_x = np.reshape(batch_x, [person_num*face_num, 100, 100, 3])

    return batch_x