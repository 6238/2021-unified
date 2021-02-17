import cv2
import numpy as np

from img_utils import DisplayUtils, GeneralUtils, ShapeDetector


shape_detector = ShapeDetector()
utils = GeneralUtils()
display_utils = DisplayUtils()


def draw_targets(frame):
    gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)

    blurred = cv2.GaussianBlur(gray, (3, 3), 0)
    _, thresh = cv2.threshold(blurred, 0, 255, cv2.THRESH_OTSU)

    median = np.median(gray)
    sigma = 0.33
    lower_thresh = int(max(0, (1.0 - sigma) * median))
    upper_thresh = int(min(255, (1.0 + sigma) * median))
    canny = cv2.Canny(thresh, lower_thresh, upper_thresh)

    contours, hierarchy = cv2.findContours(
        canny, cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE
    )
    contours = utils.filter_contours_area(contours, 1000)

    hexagons = shape_detector.get_contours_of_shape(contours, 6)

    ### Drawing ###
    contour_img = frame.copy()
    hexagons_img = frame.copy()

    cv2.drawContours(contour_img, contours, -1, (0, 0, 255), 3)
    cv2.drawContours(hexagons_img, hexagons, -1, (0, 255, 0), 3)

    frame_list = [frame, thresh, canny, contour_img, hexagons_img]
    return frame_list


def get_hexagon_contour(frame):
    gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)

    blurred = cv2.GaussianBlur(gray, (3, 3), 0)
    # thresh = cv2.adaptiveThreshold(
    #     blurred, 255, cv2.ADAPTIVE_THRESH_GAUSSIAN_C, cv2.THRESH_BINARY, 201, 0
    # )
    _, thresh = cv2.threshold(blurred, 0, 255, cv2.THRESH_OTSU)

    median = np.median(gray)
    sigma = 0.33
    lower_thresh = int(max(0, (1.0 - sigma) * median))
    upper_thresh = int(min(255, (1.0 + sigma) * median))
    canny = cv2.Canny(thresh, lower_thresh, upper_thresh)

    contours, hierarchy = cv2.findContours(
        canny, cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE
    )
    contours = utils.filter_contours_area(contours, 1000)

    hexagons = shape_detector.get_contours_of_shape(contours, 6)

    if len(hexagons) == 0:
        return hexagons

    hexagon = utils.get_largest_contour(hexagons)

    return hexagon


def get_target_centroid(frame):
    hexagon_contour = get_hexagon_contour(frame)

    if len(hexagon_contour) == 0:
        return hexagon_contour

    centroids = utils.get_contour_centers([hexagon_contour])

    return centroids[0]


def get_target_corners(frame):

    hexagon_contour = get_hexagon_contour(frame)
    if len(hexagon_contour) == 0:
        return hexagon_contour

    perimeter = cv2.arcLength(hexagon_contour, True)
    approx = cv2.approxPolyDP(hexagon_contour, 0.05 * perimeter, True)

    return approx