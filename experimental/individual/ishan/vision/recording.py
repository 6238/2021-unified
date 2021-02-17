import cv2
import os
import sys
import select

cap = cv2.VideoCapture(0)

width = int(cap.get(cv2.CAP_PROP_FRAME_WIDTH))
height = int(cap.get(cv2.CAP_PROP_FRAME_HEIGHT))

i = 0
while os.path.isfile("/home/pi/video" + str(i) + ".mp4"):
    i += 1

writer = cv2.VideoWriter(
    'video' + str(i) + '.mp4', cv2.VideoWriter_fourcc(*'mp4v'), 20, (width, height))


while True:
    ret, frame = cap.read()

    writer.write(frame)

    #cv2.imshow('frame', frame)

    # os.system('cls' if os.name == 'nt' else 'clear')
    # print("I'm doing stuff. Press Enter to stop me!")
    if sys.stdin in select.select([sys.stdin], [], [], 0)[0]:
        line = input()
        break


cap.release()
writer.release()
cv2.destroyAllWindows()
