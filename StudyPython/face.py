import cv2
import sys

mod = sys.modules[__name__]
# Load the cascade
face_cascade = cv2.CascadeClassifier('haarcascade_frontalface_default.xml')
# Read the input image
img = cv2.imread('ss.jpg', cv2.IMREAD_ANYCOLOR)
# Convert into grayscale
gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
# Detect faces
faces = face_cascade.detectMultiScale(gray, 1.1, 4)
a = 0
# Draw rectangle around the faces
for (x, y, w, h) in faces:
    cv2.rectangle(img, (x, y), (x+w, y+h), (255, 0, 0), 2)
    crop_img = img[y:y+h, x:x+w]
    setattr(mod, 'dst_{}'.format(a), cv2.resize(crop_img, dsize=(150, 150), interpolation=cv2.INTER_AREA))
    cv2.imshow(str(a), getattr(mod, 'dst_{}'.format(a)))
    cv2.imwrite(str(a)+'.jpg', getattr(mod, 'dst_{}'.format(a)))
    a = a + 1
# Display the output
cv2.imshow('img', img)
cv2.waitKey()