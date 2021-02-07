from pathlib import Path
from collections import deque
import itertools

import cv2
import numpy as np

from img_utils import DisplayUtils, GeneralUtils, ShapeDetector
import target_detection_img, color_filtering_img
from center_prediction import CenterPredModel

# TODO ideas
# find contours after each mask, then blacken everything under a certain area.
# crop into brown target after using contours to find a rectangle that is large on brown mask
# fix flatlining issue where predictions using poly fit don't update over time


# accesible at drive folder: https://drive.google.com/drive/folders/11khSnQNsxnt0JStAec8j-widmBLOzPsO?usp=sharing
# video name (can use others too): vision-video-trees-white-notape-lowres-0.mp4

utils = GeneralUtils()
shape_detector = ShapeDetector()
display_utils = DisplayUtils()

# keep last 150 frames or last 5 seconds at 30fps
centroids_deque = deque(maxlen=150)
NO_TARGET_THRESH = 15
frames_since_target = 0

FRAMES_FOR_TRAIN = 15
MODEL_DEGREE = 4
center_pred_model = CenterPredModel(degree=MODEL_DEGREE)

frame_count = 0
truth_centroids = []
pred_centroids = []


# video_path = Path(
#     "E:/code/projects/frc-vision/datasets/target-dataset/vision-videos/vision-video-trees-notape-lowres-0.mp4"
# )
video_path = Path(
    "E:/code/projects/frc-vision/datasets/target-dataset/vision-videos/vision-video-trees-white-notape-lowres-0.mp4"
)
cap = cv2.VideoCapture(str(video_path))
if not cap.isOpened():
    print("Cannot open camera")
    exit()


while True:
    # Capture frame-by-frame
    ret, frame = cap.read()
    frame_count += 1

    # if frame is not correctly read
    if not ret:
        print("End of video")
        break

    # halve frame size
    frame = cv2.resize(
        frame,
        (frame.shape[1] // 2, frame.shape[0] // 2),
    )
    original = frame.copy()  # saving copy before edits

    # equalize color and brightness
    equalized = cv2.normalize(frame, frame, 0, 255, cv2.NORM_MINMAX)
    # filter by color
    filtered = color_filtering_img.apply_color_filter(equalized)
    # smooth to remove jagged edges
    smoothed = utils.smoother_edges(filtered, (7, 7), (1, 1))
    frame_list = target_detection_img.draw_targets(smoothed)

    # acutal data
    centroid = target_detection_img.get_target_centroid(smoothed)

    if len(centroid) != 0:
        centroids_deque.append(centroid)
        truth_centroids.append(np.array([frame_count, centroid[0], centroid[1]]))
        frames_since_target = 0
    else:
        frames_since_target += 1
        if frames_since_target < NO_TARGET_THRESH and len(centroids_deque) != 0:
            # train on last n frames
            # max is to make sure we don't go out of bounds when len(centroids_deque) < FRAMES_FOR_TRAIN
            frames = list(
                itertools.islice(
                    centroids_deque,
                    max(0, len(centroids_deque) - FRAMES_FOR_TRAIN),
                    len(centroids_deque),
                )
            )
            center_pred_model.fit_frames(frames)

            current_time = np.array([FRAMES_FOR_TRAIN - 1])
            centroid = center_pred_model.predict_single_scalar(current_time, dtype=int)
            pred_centroids.append(np.array([frame_count, centroid[0], centroid[1]]))
            # centroid = centroids_deque[-1]
            print("pred: ", end="")

    print(frame_count, centroid)

    # displaying image grid
    if len(centroid) != 0:
        display_utils.draw_circles(original, [centroid])

    # grid = [original] + frame_list
    # grid = display_utils.create_img_grid_list(grid, 3, 2)
    # cv2.imshow("grid", grid)
    cv2.imshow("frame", original)

    if cv2.waitKey(5) & 0xFF == 27:
        break


# Eelease the capture at end
cap.release()
cv2.destroyAllWindows()

truth_centroids = np.array(truth_centroids)
np.save(
    f"./centroid-pred-data/truth-centroid-history-deg{MODEL_DEGREE}-frames{FRAMES_FOR_TRAIN}",
    truth_centroids,
)

pred_centroids = np.array(pred_centroids)
np.save(
    f"./centroid-pred-data/pred-centroid-history-deg{MODEL_DEGREE}-frames{FRAMES_FOR_TRAIN}",
    pred_centroids,
)
