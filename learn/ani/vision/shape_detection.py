import cv2
import numpy as np
import matplotlib.pyplot as plt

cap = cv2.VideoCapture("./datasets/vision-example-video-camera0.mp4")
if not cap.isOpened():
    print("Cannot open camera")
    exit()

frame_num = 0
while True:
    # Capture frame-by-frame
    ret, frame = cap.read()
    
    frame_num += 1
    if frame_num % 10 != 0:
        continue

    # if frame is not correctly read
    if not ret:
        print("Can't receive frame (stream end?). Exiting ...")
        break

    # Apply operations on the frame
    frame = cv2.resize(frame, (1920, 1080))
    gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)

    corners = cv2.goodFeaturesToTrack(gray,25,0.01,10)
    corners = np.int0(corners)
    for i in corners:
        x,y = i.ravel()
        cv2.circle(frame,(x, y),3,255,-1)
        

    # Display the new frame
    cv2.imshow('frame', frame)
    if cv2.waitKey(1) == ord('q'):
        break


# Eelease the capture at end
cap.release()
cv2.destroyAllWindows()