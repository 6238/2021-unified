import numpy as np
import cv2
import threading


class CameraThread(threading.Thread):
    def __init__(self, preview_name, cam_id):
        threading.Thread.__init__(self)
        self.preview_name = preview_name
        self.cam_id = cam_id

    def run(self):
        print("Starting " + self.preview_name)
        cam_preview(self.preview_name, self.cam_id)


def cam_preview(preview_name, cam_id):
    cv2.namedWindow(preview_name)
    cam = cv2.VideoCapture(cam_id)
    if cam.isOpened():  # try to get the first frame
        ret, frame = cam.read()
    else:
        ret = False

    while ret:
        cv2.imshow(preview_name, frame)
        ret, frame = cam.read()
        key = cv2.waitKey(20)
        if key == 27:  # exit on ESC
            break
    cv2.destroyWindow(preview_name)


# Create two threads as follows
thread1 = CameraThread("Camera 1", 1)
thread2 = CameraThread("Camera 2", 2)
thread1.start()
thread2.start()


# cap_l = cv2.VideoCapture(1)
# cap_r = cv2.VideoCapture(2)

# if not cap_l.isOpened() or not cap_r.isOpened():
#     print("Cannot open video/camera")
#     exit()

# while True:
#     ret_l, img_l = cap_l.read()
#     ret_r, img_r = cap_r.read()

#     print(ret_l, ret_r)

#     img_l = cv2.cvtColor(img_l, cv2.COLOR_BGR2GRAY)
#     img_r = cv2.cvtColor(img_r, cv2.COLOR_BGR2GRAY)

#     cv2.imshow("left", img_l)
#     cv2.imshow("right", img_r)

#     # stereo = cv2.StereoBM_create(numDisparities=16, blockSize=15)

#     # disparity = stereo.compute(img_l, img_r)
#     # cv2.imshow("disparity", disparity)

#     if cv2.waitKey(5) & 0xFF == 27:
#         break


# cap_l.release()
# cap_r.release()