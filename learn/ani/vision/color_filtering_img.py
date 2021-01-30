from pathlib import Path
import cv2
import numpy as np

from img_utils import GeneralUtils, ShapeDetector


shape_detector = ShapeDetector()
utils = GeneralUtils()


def apply_color_filter(img):
    blurred = cv2.medianBlur(img, 5)
    hsv = cv2.cvtColor(blurred, cv2.COLOR_BGR2HSV)
    lower_brown = np.array([5, 0, 0])
    upper_brown = np.array([20, 255, 255])
    brown_mask = cv2.inRange(hsv, lower_brown, upper_brown)
    brown_res = cv2.bitwise_and(img, img, mask=brown_mask)

    lower_gray = np.array([0, 0, 0])
    upper_gray = np.array([255, 20, 255])
    gray_mask = cv2.inRange(hsv, lower_gray, upper_gray)
    gray_res = cv2.bitwise_and(img, img, mask=gray_mask)

    brown_res = cv2.dilate(brown_res, (7, 7))
    gray_res = cv2.dilate(gray_res, (17, 17))
    # brown_res = cv2.bilateralFilter(brown_res, 25, 15, 75)
    # gray_res = cv2.bilateralFilter(gray_res, 25, 15, 75)
    brown_res = cv2.medianBlur(brown_res, 3)
    gray_res = cv2.medianBlur(gray_res, 3)

    combined = cv2.bitwise_or(brown_res, gray_res)
    # kernel = np.ones((3, 3), dtype=float) / 9
    # combined = cv2.filter2D(combined, -1, kernel)

    return combined


if __name__ == "__main__":
    img_path = Path("./target-imgs/mid-target.jpg")
    img = cv2.imread(str(img_path))
    img = cv2.resize(img, (img.shape[1] // 2, img.shape[0] // 2))
    target_img = img.copy()  # for drawing bounding box on

    cv2.imshow("original", img)
    # cv2.waitKey(1)

    # hue, sat, value
    blurred = cv2.medianBlur(img, 5)
    hsv = cv2.cvtColor(blurred, cv2.COLOR_BGR2HSV)
    lower_green = np.array([20, 50, 0])
    upper_green = np.array([80, 255, 255])
    green_mask = cv2.inRange(hsv, lower_green, upper_green)
    green_res = cv2.bitwise_and(img, img, mask=green_mask)
    # cv2.imshow("hsv filter green", green_mask)
    cv2.imshow("green res", green_res)

    # brown
    # blurred = cv2.GaussianBlur(img, (5, 5), 0)
    blurred = cv2.medianBlur(img, 5)
    hsv = cv2.cvtColor(blurred, cv2.COLOR_BGR2HSV)
    lower_brown = np.array([5, 0, 0])
    upper_brown = np.array([20, 255, 255])
    brown_mask = cv2.inRange(hsv, lower_brown, upper_brown)
    brown_res = cv2.bitwise_and(img, img, mask=brown_mask)
    # cv2.imshow("hsv filter brown", brown_mask)
    cv2.imshow("brown res", brown_res)

    # gray
    # reflect shape over top of contour of tape then find center of that
    # blurred = cv2.GaussianBlur(img, (5, 5), 0)
    blurred = cv2.medianBlur(img, 5)
    hsv = cv2.cvtColor(blurred, cv2.COLOR_BGR2HSV)
    lower_gray = np.array([0, 0, 0])
    upper_gray = np.array([255, 20, 255])
    gray_mask = cv2.inRange(hsv, lower_gray, upper_gray)
    gray_res = cv2.bitwise_and(img, img, mask=gray_mask)
    # cv2.imshow("hsv filter gray", gray_mask)
    cv2.imshow("gray res", gray_res)

    aoi = cv2.bitwise_or(brown_res, gray_res)
    cv2.imshow("aoi brown gray", aoi)

    # intersection = cv2.bitwise_and(aoi, green_res)
    # _, intersection = cv2.threshold(intersection, 1, 255, cv2.THRESH_BINARY)
    # new_aoi = cv2.bitwise_and(aoi, aoi, mask=intersection)
    # cv2.imshow("aoi brown gray green", new_aoi)

    cv2.waitKey(-1)