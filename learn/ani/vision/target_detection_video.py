from pathlib import Path
import cv2
import numpy as np
import matplotlib.pyplot as plt

from img_utils import DisplayUtils, GeneralUtils, ShapeDetector
import shape_detection_img, color_filtering_img

# accesible at drive folder: https://drive.google.com/drive/folders/11khSnQNsxnt0JStAec8j-widmBLOzPsO?usp=sharing
# video name (can use others too): vision-video-trees-lowres-0.mp4
video_path = Path(
    "E:/code/projects/frc-vision/datasets/target-dataset/vision-videos/vision-video-trees-lowres-0.mp4"
)
cap = cv2.VideoCapture(str(video_path))
if not cap.isOpened():
    print("Cannot open camera")
    exit()

utils = GeneralUtils()
shape_detector = ShapeDetector()
display_utils = DisplayUtils()


while True:
    # Capture frame-by-frame
    ret, frame = cap.read()

    # if frame is not correctly read
    if not ret:
        print("End of video")
        break

    # Apply operations on the frame
    frame = cv2.resize(
        frame,
        (frame.shape[1] // 2, frame.shape[0] // 2),
    )
    filtered = color_filtering_img.apply_color_filter(frame)
    grid = shape_detection_img.apply_ops(filtered)
    cv2.imshow("grid", grid)

    # Display the new frame
    # cv2.imshow("new frame", frame)
    if cv2.waitKey(5) & 0xFF == 27:
        break


# Eelease the capture at end
cap.release()
cv2.destroyAllWindows()