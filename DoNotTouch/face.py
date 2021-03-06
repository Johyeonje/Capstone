import sys
import dlib
import cv2

# Take the image file name from the command line
file_path = sys.argv[1]
file_name = sys.argv[2]
mod = sys.modules[__name__]
# Create a HOG face detector using the built-in dlib class
face_detector = dlib.get_frontal_face_detector()
cvimg = cv2.imread(file_path+"/"+file_name, cv2.IMREAD_ANYCOLOR)
# Load the image into an array
image = cv2.imread(file_path+"/"+file_name)

# Run the HOG face detector on the image data.
# The result will be the bounding boxes of the faces in our image.
detected_faces = face_detector(image, 1)

print("I found {} faces in the file {}".format(len(detected_faces), file_name))

# Open a window on the desktop showing the image

# Loop through each face we found in the image
for i, face_rect in enumerate(detected_faces):
    # Detected faces are returned as an object with the coordinates
    # of the top, left, right and bottom edges
    crop_img = cvimg[face_rect.top():face_rect.bottom(), face_rect.left():face_rect.right()]
    print("- Face #{} found at Left: {} Top: {} Right: {} Bottom: {}".format(i, face_rect.left(), face_rect.top(),
                                                                             face_rect.right(), face_rect.bottom()))
    setattr(mod, 'dst_{}'.format(i), cv2.resize(crop_img, dsize=(100, 100), interpolation=cv2.INTER_AREA))
    cv2.imwrite(file_path+"/"+str(i) + '.jpg', getattr(mod, 'dst_{}'.format(i)))
    # Draw a box around each face we found

# Wait until the user hits <enter> to close the window
dlib.hit_enter_to_continue()