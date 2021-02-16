from pathlib import Path
from collections import deque
import itertools

import cv2
import numpy as np

from img_utils import DisplayUtils, GeneralUtils, ShapeDetector
import target_detection_img, color_filtering_img
from center_prediction import CenterPredModel

utils = GeneralUtils()
shape_detector = ShapeDetector()
display_utils = DisplayUtils()

tracker_types = [
    "BOOSTING",
    "MIL",
    "KCF",
    "TLD",
    "MEDIANFLOW",
    "GOTURN",
    "MOSSE",
    "CSRT",
]
# best: 2, 4, 7 (good but slow)
tracker_type = tracker_types[7]

if tracker_type == "BOOSTING":
    tracker = cv2.TrackerBoosting_create()
if tracker_type == "MIL":
    tracker = cv2.TrackerMIL_create()
if tracker_type == "KCF":
    tracker = cv2.TrackerKCF_create()
if tracker_type == "TLD":
    tracker = cv2.TrackerTLD_create()
if tracker_type == "MEDIANFLOW":
    tracker = cv2.TrackerMedianFlow_create()
if tracker_type == "GOTURN":
    tracker = cv2.TrackerGOTURN_create()
if tracker_type == "MOSSE":
    tracker = cv2.TrackerMOSSE_create()
if tracker_type == "CSRT":
    tracker = cv2.TrackerCSRT_create()


video_path = Path(
    "E:/code/projects/frc-vision/datasets/target-dataset/vision-videos/vision-video-horizontal-robot-driving-720p-0.mp4"
)
# video_path = Path(
#     "E:/code/projects/frc-vision/datasets/target-dataset/vision-videos/vision-video-vertical-robot-driving-1080p-0.mp4"
# )
# video_path = Path(
#     "E:/code/projects/frc-vision/datasets/target-dataset/vision-videos/vision-video-trees-white-notape-lowres-0.mp4"
# )

cap = cv2.VideoCapture(str(video_path))
if not cap.isOpened():
    print("Cannot open video/camera")
    exit()


frame = cap.read()[1]
frame = cv2.resize(
    frame,
    (frame.shape[1] // 2, frame.shape[0] // 2),
)
bbox = cv2.selectROI(frame, False)
cv2.rectangle(
    frame,
    (int(bbox[0]), int(bbox[1])),
    (int(bbox[0] + bbox[2]), int(bbox[1] + bbox[3])),
    (0, 255, 0),
    3,
)
track_success = tracker.init(frame, bbox)

while True:
    # Capture frame-by-frame
    ret, frame = cap.read()

    # if frame is not correctly read
    if not ret:
        print("End of video")
        break

    timer = cv2.getTickCount()

    # halve frame size
    frame = cv2.resize(
        frame,
        (frame.shape[1] // 2, frame.shape[0] // 2),
    )
    track_success, bbox = tracker.update(frame)
    if track_success:
        cv2.rectangle(
            frame,
            (int(bbox[0]), int(bbox[1])),
            (int(bbox[0] + bbox[2]), int(bbox[1] + bbox[3])),
            (0, 255, 0),
            3,
        )

    # # equalize color and brightness
    # equalized = cv2.normalize(frame, frame, 0, 255, cv2.NORM_MINMAX)
    # # filter by color
    # filtered = color_filtering_img.apply_color_filter(equalized)
    # # smooth to remove jagged edges
    # smoothed = utils.smoother_edges(filtered, (7, 7), (1, 1))

    # frame_list = target_detection_img.draw_targets(smoothed)

    # # acutal data
    # centroid = target_detection_img.get_target_centroid(smoothed)

    # # displaying image grid
    # if len(centroid) != 0:
    #     display_utils.draw_circles(original, [centroid])

    # grid = [original] + frame_list
    # grid = display_utils.create_img_grid_list(grid, 3, 2)
    # cv2.imshow("grid", grid)

    fps = cv2.getTickFrequency() / (cv2.getTickCount() - timer)
    cv2.putText(
        frame,
        "FPS: " + str(int(fps)),
        (0, 50),
        cv2.FONT_HERSHEY_SIMPLEX,
        0.75,
        (50, 170, 50),
        2,
    )

    cv2.imshow("frame", frame)

    if cv2.waitKey(1) == ord(" "):
        cv2.waitKey(-1)

    if cv2.waitKey(5) & 0xFF == 27:
        break


# Eelease the capture at end
cap.release()
cv2.destroyAllWindows()