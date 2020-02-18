import cv2
import sys

mod = sys.modules[__name__]
# Load the cascade
face_cascade = cv2.CascadeClassifier('haarcascade_frontalface_default.xml')
# Read the input image
img = cv2.imread('img (1).jpg', cv2.IMREAD_ANYCOLOR)
# Convert into grayscale
gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
# Detect faces
faces = face_cascade.detectMultiScale(gray, 1.1, 4)
a = 0
# Draw rectangle around the faces
for (x, y, w, h) in faces:
    crop_img = img[y:y+h, x:x+w]
    setattr(mod, 'dst_{}'.format(a), cv2.resize(crop_img, dsize=(100, 100), interpolation=cv2.INTER_AREA))

    cv2.imshow(str(a), getattr(mod, 'dst_{}'.format(a)))
    cv2.imwrite(str(a)+'.jpg', getattr(mod, 'dst_{}'.format(a)))
    print("- Face #{} found at Left: {} Top: {} Right: {} Bottom: {}".format(a, y, x, y+h, x+w))
    a = a + 1
# Display the output
img = cv2.resize(img, dsize=(0, 0), fx=0.25, fy=0.25, interpolation=cv2.INTER_LINEAR)
cv2.imshow('img', img)
cv2.waitKey()