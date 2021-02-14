from pathlib import Path

import cv2
import numpy as np

from img_utils import DisplayUtils, GeneralUtils, ShapeDetector
import target_detection_img, color_filtering_img

# video_path = Path(
#     "E:/code/projects/frc-vision/datasets/target-dataset/vision-videos/vision-video-horizontal-robot-driving-720p-0.mp4"
# )
# video_path = Path(
#     "E:/code/projects/frc-vision/datasets/target-dataset/vision-videos/vision-video-vertical-robot-driving-1080p-0.mp4"
# )
video_path = Path(
    "E:/code/projects/frc-vision/datasets/target-dataset/vision-videos/vision-video-trees-white-notape-lowres-0.mp4"
)


utils = GeneralUtils()
shape_detector = ShapeDetector()
display_utils = DisplayUtils()


def get_hexagon_points(frame):
    # equalize color and brightness, 2000+ fps
    equalized = cv2.normalize(frame, frame, 0, 255, cv2.NORM_MINMAX)
    # filter by color, ~60fps
    filtered = color_filtering_img.apply_color_filter(equalized)
    # smooth to remove jagged edges, 200+ fps
    smoothed = utils.smoother_edges(filtered, (7, 7), (1, 1))
    # acutal data, ~18 fps
    hexagon = target_detection_img.get_target_corners(smoothed)
    return hexagon


def expanded_good_features(
    frame_gray, hexagon_points, num_features=3, expansion_percent=0.15
):
    if hexagon_points.size == 0:
        return np.empty((0, 1, 2), dtype=np.float32)

    frame_height, frame_width = frame_gray.shape
    x0, y0, width, height = cv2.boundingRect(hexagon_points)

    x_expansion = width * expansion_percent
    y_expansion = height * expansion_percent
    # cv2.rectangle(frame_gray, (x0, y0), (x0 + width, y0 + height), (0, 0, 255), thickness=4)

    x1 = min(frame_width, x0 + width + x_expansion / 2)
    y1 = min(frame_height, y0 + height + y_expansion / 2)
    x0 = max(0, x0 - x_expansion / 2)
    y0 = max(0, y0 - y_expansion / 2)
    x0, y0, x1, y1 = int(x0), int(y0), int(x1), int(y1)
    # cv2.rectangle(frame_gray, (x0, y0), (x1, y1), (0, 255, 0), thickness=4)

    roi = frame_gray[y0:y1, x0:x1]

    corners = cv2.goodFeaturesToTrack(roi, num_features, 0.001, 10)
    if corners is None:
        return np.empty((0, 1, 2), dtype=np.float32)

    corners[:, :, 0] += x0
    corners[:, :, 1] += y0
    return corners


# Parameters for lucas kanade optical flow
lk_params = dict(
    winSize=(25, 25),
    maxLevel=2,
    criteria=(cv2.TERM_CRITERIA_EPS | cv2.TERM_CRITERIA_COUNT, 10, 0.03),
)

cap = cv2.VideoCapture(str(video_path))
if not cap.isOpened():
    print("Cannot open video/camera")
    exit()


ret, old_frame = cap.read()
old_gray = cv2.cvtColor(old_frame, cv2.COLOR_BGR2GRAY)
old_points = np.empty((0, 1, 2), dtype=np.float32)

frame_count = -1
no_target = True
while True:
    # Capture frame-by-frame
    ret, frame = cap.read()
    if not ret:
        break
    frame_count += 1
    timer = cv2.getTickCount()

    frame_gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)

    # every 10 frames, or if there isn't a target, detect
    if frame_count % 15 == 0 or no_target:
        hexagon = get_hexagon_points(frame)
        # if hexagon is found, set good points to detection and set no_target to False
        if len(hexagon) != 0:
            no_target = False
            good_new = hexagon
            corners = expanded_good_features(frame_gray, good_new)
            if corners.size != 0:
                print(good_new.shape, corners.shape)
                good_new = np.concatenate([good_new, corners])
        # else, set no_target to True
        else:
            no_target = True
            good_new = np.array([])

    # if it is time to track or nothing was detected
    if frame_count % 10 != 0 or good_new.size == 0:
        new_points, status, error = cv2.calcOpticalFlowPyrLK(
            old_gray, frame_gray, old_points, None, **lk_params
        )
        if new_points is None:  # no points left in frame
            good_new = np.array([])
        else:
            good_new = new_points[status == 1]

    old_points = good_new.reshape(-1, 1, 2).astype(np.float32)
    old_gray = frame_gray.copy()

    ### Displaying ###
    display_utils.draw_circles(
        frame, good_new.reshape((-1, 2)).astype(int), radius=3, color=(255, 0, 0)
    )
    display_utils.draw_circles(
        frame, utils.get_contour_centers([good_new.astype(int)]), radius=5
    )

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
    if cv2.waitKey(5) & 0xFF == 27:
        break

    if cv2.waitKey(1) == ord(" "):
        cv2.waitKey(-1)


# Release the capture at end
cap.release()
cv2.destroyAllWindows()