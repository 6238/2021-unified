from pathlib import Path
import cv2
import numpy as np
import imutils

from img_utils import GeneralUtils, ShapeDetector


shape_detector = ShapeDetector()
utils = GeneralUtils()

img_path = Path("./target.jpg")
# img_path = Path("./2012-retroreflective-targets.jpg")
img = cv2.imread(str(img_path))
img = cv2.resize(img, (img.shape[1] // 2, img.shape[0] // 2))

cv2.imshow("original", img)
cv2.waitKey(1)

gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
blurred = cv2.GaussianBlur(gray, (25, 25), 0)
_, thresh = cv2.threshold(blurred, 150, 255, cv2.THRESH_BINARY)
thresh_mean = cv2.adaptiveThreshold(blurred, 255, cv2.ADAPTIVE_THRESH_MEAN_C, cv2.THRESH_BINARY_INV, 11, 0)
thresh_gaussian = cv2.adaptiveThreshold(blurred, 255, cv2.ADAPTIVE_THRESH_GAUSSIAN_C, cv2.THRESH_BINARY_INV, 11, 0)

cv2.imshow("thresh", thresh)
cv2.imshow("thresh_mean", thresh_mean)
cv2.imshow("thresh_gaussian", thresh_gaussian)
cv2.waitKey(1)

target_img = img.copy()

shape_detector.detect_draw_contours(thresh, target_img, 4)
shape_detector.detect_draw_contours(thresh, target_img, 6)

cv2.imshow("target", target_img)
cv2.waitKey(-1)