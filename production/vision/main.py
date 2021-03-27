from pathlib import Path

import cv2
import numpy as np

from img_utils import DisplayUtils, GeneralUtils, ShapeDetector
from depth_prediction import DepthPredModel

width = 1280
height = 720

# zero for completely silent, 1 for just console logs, 2 for displaying frames
CONSOLE_OUTPUT = True
DRAW_OUTPUT = True
RESOLUTION_SCALE = 1
DETECT_EVERY_N_FRAMES = 15

pixel_to_dist_path = Path().cwd() / "target_points.json"
meta_path = Path().cwd() / "meta.json"


utils = GeneralUtils()
shape_detector = ShapeDetector()
display_utils = DisplayUtils()

depth_model = DepthPredModel()
depth_model.load_from_json(meta_path, pixel_to_dist_path)


# Parameters for lucas kanade optical flow
lk_params = dict(
    winSize=(27, 27),
    maxLevel=5,
    criteria=(cv2.TERM_CRITERIA_EPS | cv2.TERM_CRITERIA_COUNT, 10, 0.03),
)


def get_hexagon_points(frame):
    cv2.normalize(frame, frame, 0, 255, cv2.NORM_MINMAX)

    blurred = cv2.medianBlur(frame, 5)
    hsv = cv2.cvtColor(blurred, cv2.COLOR_BGR2HSV)

    lower_filter = np.array([0, 50, 0])
    upper_filter = np.array([32, 255, 255])
    color_mask = cv2.inRange(hsv, lower_filter, upper_filter)
    color_res = cv2.bitwise_and(frame, frame, mask=color_mask)
    cv2.morphologyEx(
        color_res, cv2.MORPH_OPEN, (5, 5), iterations=1, dst=color_res
    )
    cv2.GaussianBlur(color_res, (3, 3), 0, dst=color_res)

    smoothed = utils.smoother_edges(color_res, (7, 7), (1, 1))

    gray = cv2.cvtColor(smoothed, cv2.COLOR_BGR2GRAY)
    blurred = cv2.GaussianBlur(gray, (3, 3), 0)
    _, thresh = cv2.threshold(blurred, 0, 255, cv2.THRESH_OTSU)

    median = np.median(gray)
    sigma = 0.33
    lower_thresh = int(max(0, (1.0 - sigma) * median))
    upper_thresh = int(min(255, (1.0 + sigma) * median))
    canny = cv2.Canny(thresh, lower_thresh, upper_thresh)

    contours, _ = cv2.findContours(
        canny, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE
    )
    contours = utils.filter_contours_area(contours, 1000)

    hexagons = shape_detector.get_contours_of_shape(contours, 6)

    if len(hexagons) == 0:
        return hexagons

    hexagon_contour = utils.get_largest_contour(hexagons)
    if len(hexagon_contour) == 0:
        return hexagon_contour

    perimeter = cv2.arcLength(hexagon_contour, True)
    hexagon = cv2.approxPolyDP(hexagon_contour, 0.05 * perimeter, True)

    return hexagon


cap = cv2.VideoCapture(0)
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

    # every n frames, or if there isn't a target, detect
    if frame_count % DETECT_EVERY_N_FRAMES == 0 or no_target:
        hexagon = get_hexagon_points(frame)
        # if hexagon is found, set good points to detection and set no_target to False
        if len(hexagon) != 0:
            no_target = False
            good_new = hexagon
            depth_ft = (
                depth_model.predict_contour(hexagon, RESOLUTION_SCALE)
                * 0.00328084
            )
        # else, set no_target to True
        else:
            no_target = True
            good_new = np.array([])

    # if it is time to track or nothing was detected
    if frame_count % DETECT_EVERY_N_FRAMES != 0 or good_new.size == 0:
        new_points, status, error = cv2.calcOpticalFlowPyrLK(
            old_gray, frame_gray, old_points, None, **lk_params
        )
        depth_ft = -1
        if new_points is None:  # no points left in frame
            good_new = np.array([])
        else:
            good_new = new_points[status == 1]

    old_points = good_new.reshape(-1, 1, 2).astype(np.float32)
    old_gray = frame_gray.copy()

    centroid = utils.get_contour_centers([good_new])

    fps = cv2.getTickFrequency() / (cv2.getTickCount() - timer)

    x = float(centroid[0][0] / width)
    y = float(centroid[0][1] / height)
    z = float(depth_ft)
    fps = round(fps, 2)

    ### Displaying ###
    if CONSOLE_OUTPUT:
        print(f"{x}, {y}, {depth_ft} || fps:{fps}")
        print("Res: ", frame.shape)

    if DRAW_OUTPUT:
        display_utils.draw_circles(
            frame, good_new.reshape((-1, 2)), radius=3, color=(255, 0, 0)
        )
        display_utils.draw_circles(frame, centroid, radius=5)

        cv2.putText(
            frame,
            f"FPS {int(fps)}||Depth {round(depth_ft, 2)}",
            (0, 50),
            cv2.FONT_HERSHEY_SIMPLEX,
            0.5,
            (50, 170, 50),
            2,
        )

        cv2.imshow("frame", frame)

    if cv2.waitKey(5) & 0xFF == 27:
        break


# Release the capture at end
cap.release()
cv2.destroyAllWindows()