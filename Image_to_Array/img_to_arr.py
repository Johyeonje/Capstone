import numpy as np
import cv2
import dlib
import glob

def load_image(file_name, mode=cv2.IMREAD_ANYCOLOR):
    img = cv2.imread(file_name, mode)  # load 3 dimensional array 	# 이미지를 3차원 배열로 읽어오는 함수
    return img


if __name__ == "__main__":					# 본 코드를 import 시에 이 코드가 자동 실행되지 않음 (메인문 일때만 실행)
    # 훈련 데이터 만들기 -> openCV or dlib 적용해서 얼굴 짤라내고 저장
    # 얼굴 사진 하나씩 확인해서 이름 변경
    # 전부다 불러내서 4차원 배열로 만들기

    train_img_dir = "../../FaceDataSet/"			# 현재 위치
    face_num = input()
    train_img_list = glob.glob(train_img_dir+face_num + "train/*.jpg")		# .jpg로 끝나는 파일들을 모두 리스트로 읽어들임
    #print(train_data_list)
    #exit()
    #train_img_list = ["sample.jpg"]		# 이거를 직접 하나하나 입력할 필요없이 위에 glob.glob를 사용해서 만들어주면됨
    save_path = train_img_dir + "crop/"
    input_size = (100, 100)			# resize 단계에서 미리 세로 가로 길이를 배열로 지정해놓는 것

    face_list = []				# 놀랍게도 파이썬에서는 따로 동적배열을 구현하려고 힘쓸필요없이 단순 배열 선언으로 누적 가능
    for i, file_name in enumerate(train_img_list):		# ★enumerate : ()안의 리스트를 불러오는데 index(=i)도 같이 불러오는 것이다. *Like c언어 for문
        img = load_image(file_name)  		# [세로, 가로, 채널]

        #print(type(img))
        #print(img.shape)			# 이미지의 사이즈 (채널 포함)

        face_detector = dlib.get_frontal_face_detector()	# dlib의 face detection 적용 -> face_rect.상하좌우 에 값이 반환되는 것으로 보임
        detected_faces = face_detector(img, 1)		# detected_faces에 찾아진 얼굴들에 대한 좌표가 리스트로 저장되어있음

        for j, face_rect in enumerate(detected_faces):		# 원본 이미지에서 뽑아낸 얼굴들 리스트에 대한 좌표값들을 하나하나 이용할 시간, j에 대해 enumerate를 사용
            left, right, top, bottom = face_rect.left(), face_rect.right(), face_rect.top(), face_rect.bottom()	#좌우상하 값을 옮겨받는다.

            #print(j, left, right, top, bottom)
            face = img[top:bottom, left:right, :]		# 좌표값들을 통해서 실제 얼굴이 있는 위치를 범위로 뽑아내는 것
            face = cv2.resize(face, dsize=input_size)		# resize 단계 (dsize가 기존에 저장된 사이즈를 불러와 진행)
            cv2.imwrite(save_path + "detected_face-" + str(i) + ".jpg", face)
		# 동적 할당을 진행하는 format 같은걸 사용할 필요없이 enumerate로 사용할 수 있게된 index i, j로 얼굴마다 다른 이름을 가진 이미지를 저장할 수 있음
            #face_list.append(face)		# 이전에 선언했던 얼굴 리스트에 방금 추출하여 resize까지 끝낸 이미지 3차원 배열을 쌓아 4차원으로 만든다.

    #print(len(face_list))  # face_list = [face1, face2, ..., face6], face=[세로, 가로, 3]
    # face_list : [train data 개수, 세로, 가로, 3]
    #train_x = np.array(face_list)  # list -> array	# face_list가 list의 형태로 되어있기 때문에 이것을 모델에 train data에 적합한 4차원 배열의 형태로 변환함
    #print(train_x.shape)			# 배열로 잘 만들어 졌는지 확인한다. (갯수, 세로, 가로, 채널)
