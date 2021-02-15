from pathlib import Path

import cv2
import numpy as np

from img_utils import DisplayUtils, GeneralUtils, ShapeDetector
from depth_prediction import DepthPredModel
import target_detection_img
import color_filtering_img

pixel_to_dist_path = Path(r"./target_points.json")
meta_path = Path(r"./meta.json")

# video_path = Path(
#     "E:/code/projects/frc-vision/datasets/target-dataset/vision-videos/orange-teal-target-720p.mp4"
# )
video_path = Path(
    "E:/code/projects/frc-vision/datasets/target-dataset/vision-videos/orange-teal-target-1080p.mp4"
)
# video_path = Path(
#     "E:/code/projects/frc-vision/datasets/target-dataset/vision-videos/vision-video-trees-white-notape-lowres-0.mp4"
# )

RESOLUTION_SCALE = 0.5
utils = GeneralUtils()
shape_detector = ShapeDetector()
display_utils = DisplayUtils()

depth_model = DepthPredModel()
depth_model.load_from_json(meta_path, pixel_to_dist_path)


# Parameters for lucas kanade optical flow
lk_params = dict(
    winSize=(25, 25),
    maxLevel=2,
    criteria=(cv2.TERM_CRITERIA_EPS | cv2.TERM_CRITERIA_COUNT, 10, 0.03),
)


def get_hexagon_points(frame):
    # equalize color and brightness, 2000+ fps
    equalized = cv2.normalize(frame, None, 0, 255, cv2.NORM_MINMAX)
    # filter by color, ~60fps
    # orange filter: [0, 75, 200], [16, 255, 255]
    filtered = color_filtering_img.apply_color_filter(
        equalized, [0, 50, 0], [16, 255, 255]
    )
    # smooth to remove jagged edges, 200+ fps
    smoothed = utils.smoother_edges(filtered, (7, 7), (1, 1))
    # acutal data, ~18 fps
    hexagon = target_detection_img.get_target_corners(smoothed)

    # frame_list = target_detection_img.draw_targets(smoothed)
    # grid = [frame] + frame_list
    # grid = display_utils.create_img_grid_list(grid, 3, 2)
    # cv2.imshow("grid", grid)
    return hexagon


cap = cv2.VideoCapture(str(video_path))
if not cap.isOpened():
    print("Cannot open video/camera")
    exit()


ret, old_frame = cap.read()
old_frame = cv2.resize(
    old_frame,
    (
        int(old_frame.shape[1] * RESOLUTION_SCALE),
        int(old_frame.shape[0] * RESOLUTION_SCALE),
    ),
)

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
    frame = cv2.resize(
        frame,
        (
            int(frame.shape[1] * RESOLUTION_SCALE),
            int(frame.shape[0] * RESOLUTION_SCALE),
        ),
    )
    frame_gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)

    # every 10 frames, or if there isn't a target, detect
    if frame_count % 15 == 0 or no_target:
        hexagon = get_hexagon_points(frame)
        # if hexagon is found, set good points to detection and set no_target to False
        if len(hexagon) != 0:
            no_target = False
            good_new = hexagon
            depth_mm = depth_model.predict_contour(hexagon, RESOLUTION_SCALE)
            print(depth_mm * 0.00328084)
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

    centroid = utils.get_contour_centers([good_new]) if good_new.size != 0 else []

    ### Displaying ###
    print(centroid)
    display_utils.draw_circles(
        frame, good_new.reshape((-1, 2)), radius=3, color=(255, 0, 0)
    )
    display_utils.draw_circles(frame, utils.get_contour_centers([good_new]), radius=5)

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
