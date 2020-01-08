import cv2
import sys

mod = sys.modules[__name__]
# Load the cascade
face_cascade = cv2.CascadeClassifier('haarcascade_frontalface_default.xml')
# Read the input image
<<<<<<< HEAD:StudyPython/Face_detection/face.py
img = cv2.imread('../Image/far1x4000.jpg', cv2.IMREAD_ANYCOLOR)
=======
img = cv2.imread('siba.jpg', cv2.IMREAD_ANYCOLOR)
>>>>>>> c62eb45a71d7292e0fd2214061f947d889c24a53:StudyPython/face.py
# Convert into grayscale
gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
# Detect faces
faces = face_cascade.detectMultiScale(gray, 1.05, 10)
a = 0
# Draw rectangle around the faces
for (x, y, w, h) in faces:
    crop_img = img[y:y+h, x:x+w]
<<<<<<< HEAD
<<<<<<< HEAD:StudyPython/face.py
    setattr(mod, 'dst_{}'.format(a), cv2.resize(crop_img, dsize=(150, 150), interpolation=cv2.INTER_AREA))
    #cv2.imwrite(str(a)+'.jpg', getattr(mod, 'dst_{}'.format(a)))
    #cv2.imshow(str(a), getattr(mod, 'dst_{}'.format(a)))
=======
    setattr(mod, 'dst_{}'.format(a), cv2.resize(crop_img, dsize=(200, 200), interpolation=cv2.INTER_AREA))
    cv2.imshow(str(a), getattr(mod, 'dst_{}'.format(a)))
    cv2.imwrite("../Image/Resize/"+str(a)+'.jpg', getattr(mod, 'dst_{}'.format(a)))
>>>>>>> c491757c5b5ac1c32347bb31831c634a7b288499:StudyPython/Face_detection/face.py
=======
<<<<<<< HEAD:StudyPython/Face_detection/face.py
    setattr(mod, 'dst_{}'.format(a), cv2.resize(crop_img, dsize=(200, 200), interpolation=cv2.INTER_AREA))
    cv2.imshow(str(a), getattr(mod, 'dst_{}'.format(a)))
    cv2.imwrite("Image/Resize/"+str(a)+'.jpg', getattr(mod, 'dst_{}'.format(a)))
=======
    setattr(mod, 'dst_{}'.format(a), cv2.resize(crop_img, dsize=(150, 150), interpolation=cv2.INTER_AREA))
    cv2.imwrite(str(a)+'.jpg', getattr(mod, 'dst_{}'.format(a)))
    cv2.imshow(str(a), getattr(mod, 'dst_{}'.format(a)))
>>>>>>> parent of c491757... Update face.py
    cv2.rectangle(img, (x, y), (x + w, y + h), (255, 0, 0), 2)
>>>>>>> c62eb45a71d7292e0fd2214061f947d889c24a53:StudyPython/face.py
    a = a + 1
# Display the output
img = cv2.resize(img, dsize=(0, 0), fx=0.25, fy=0.25, interpolation=cv2.INTER_LINEAR)
cv2.imwrite("../Image/far1x2000.jpg", img)
cv2.imshow('img', img)
cv2.waitKey()