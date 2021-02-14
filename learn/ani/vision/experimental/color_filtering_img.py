from pathlib import Path
import cv2
import numpy as np

from img_utils import DisplayUtils, GeneralUtils, ShapeDetector


shape_detector = ShapeDetector()
utils = GeneralUtils()
display_utils = DisplayUtils()


def apply_color_filter(img, lower=[5, 0, 60], upper=[20, 180, 255]):

    blurred = cv2.medianBlur(img, 5)
    hsv = cv2.cvtColor(blurred, cv2.COLOR_BGR2HSV)

    lower_brown = np.array(lower)
    upper_brown = np.array(upper)
    brown_mask = cv2.inRange(hsv, lower_brown, upper_brown)
    brown_res = cv2.bitwise_and(img, img, mask=brown_mask)

    brown_cleaned = cv2.morphologyEx(brown_res, cv2.MORPH_OPEN, (5, 5), iterations=1)

    brown_blurred = cv2.GaussianBlur(brown_cleaned, (3, 3), 0)

    ### Temp Displaying ###
    # grid = display_utils.create_img_grid(
    #     [
    #         [brown_res, brown_dilated, brown_blurred],
    #         [gray_res, gray_dilated, gray_blurred],
    #     ]
    # )
    # cv2.imshow("color grid", grid)

    return brown_blurred


if __name__ == "__main__":

    img_path = Path("./target-imgs/right-upshot-target-notape.jpg")
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
    # cv2.imshow("green res", green_res)

    # brown
    # blurred = cv2.GaussianBlur(img, (5, 5), 0)
    blurred = cv2.medianBlur(img, 5)
    hsv = cv2.cvtColor(blurred, cv2.COLOR_BGR2HSV)
    lower_brown = np.array([5, 0, 60])
    upper_brown = np.array([20, 180, 255])
    brown_mask = cv2.inRange(hsv, lower_brown, upper_brown)
    brown_res = cv2.bitwise_and(img, img, mask=brown_mask)
    # cv2.imshow("hsv filter brown", brown_mask)
    cv2.imshow("brown res", brown_res)
    brown_cleaned = cv2.morphologyEx(brown_res, cv2.MORPH_OPEN, (5, 5), iterations=5)
    cv2.imshow("brown cleaned", brown_cleaned)

    # gray
    # reflect shape over top of contour of tape then find center of that
    # blurred = cv2.GaussianBlur(img, (5, 5), 0)
    blurred = cv2.medianBlur(img, 5)
    hsv = cv2.cvtColor(blurred, cv2.COLOR_BGR2HSV)
    lower_gray = np.array([0, 0, 150])
    upper_gray = np.array([200, 25, 255])
    gray_mask = cv2.inRange(hsv, lower_gray, upper_gray)
    gray_res = cv2.bitwise_and(img, img, mask=gray_mask)
    # cv2.imshow("hsv filter gray", gray_mask)
    # cv2.imshow("gray res", gray_res)

    # white
    blurred = cv2.medianBlur(img, 5)
    hsv = cv2.cvtColor(blurred, cv2.COLOR_BGR2HSV)
    lower_white = np.array([100, 10, 180])
    upper_white = np.array([150, 50, 225])
    white_mask = cv2.inRange(hsv, lower_white, upper_white)
    white_res = cv2.bitwise_and(img, img, mask=white_mask)
    # cv2.imshow("white res", white_res)

    aoi = cv2.bitwise_or(brown_res, gray_res)
    # aoi = cv2.bitwise_or(aoi, white_res)
    # cv2.imshow("aoi brown gray white", aoi)

    new_img = aoi.copy()
    new_img = cv2.erode(new_img, (5, 5))
    new_img = cv2.GaussianBlur(new_img, (5, 5), 0)
    # canny = cv2.Canny(new_img, 500, 20)
    # cv2.imshow("canny", canny)

    # contours, hierarchy = cv2.findContours(
    #     canny, cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE
    # )
    # cleaned = utils.blacken_small_noise(canny, contours, 100)
    # cv2.imshow("cleaned", cleaned)

    # intersection = cv2.bitwise_and(aoi, green_res)
    # _, intersection = cv2.threshold(intersection, 1, 255, cv2.THRESH_BINARY)
    # new_aoi = cv2.bitwise_and(aoi, aoi, mask=intersection)
    # cv2.imshow("aoi brown gray green", new_aoi)

    cv2.waitKey(-1)