from pathlib import Path
import cv2
import numpy as np
import imutils

from img_utils import GeneralUtils, ShapeDetector


shape_detector = ShapeDetector()
utils = GeneralUtils()

img_path = Path("./target-imgs/mid-target.jpg")
# img_path = Path("./2012-retroreflective-targets.jpg")
img = cv2.imread(str(img_path))
img = cv2.resize(img, (img.shape[1] // 2, img.shape[0] // 2))
target_img = img.copy()  # for drawing bounding box on

cv2.imshow("original", img)
cv2.waitKey(1)

gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
blurred = cv2.GaussianBlur(gray, (25, 25), 0)
_, thresh = cv2.threshold(blurred, 150, 255, cv2.THRESH_BINARY)
thresh_mean = cv2.adaptiveThreshold(
    blurred, 255, cv2.ADAPTIVE_THRESH_MEAN_C, cv2.THRESH_BINARY_INV, 11, 0
)
thresh_gaussian = cv2.adaptiveThreshold(
    blurred, 255, cv2.ADAPTIVE_THRESH_GAUSSIAN_C, cv2.THRESH_BINARY_INV, 11, 0
)

# cv2.imshow("thresh", thresh)
# cv2.imshow("thresh_mean", thresh_mean)
# cv2.imshow("thresh_gaussian", thresh_gaussian)
# cv2.waitKey(1)

canny = cv2.Canny(thresh, 10, 20)
# cv2.imshow("canny", canny)
# cv2.waitKey(1)

canny = cv2.morphologyEx(canny, cv2.MORPH_DILATE, (15, 15))
# cv2.imshow("canny dilated", canny)
# cv2.waitKey(1)

contours, hierarchy = cv2.findContours(
    canny, cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE
)  # RETR_EXTERNAL
cv2.drawContours(target_img, contours, -1, (0, 0, 255), 3)

print("number of shapes found:", len(contours))
print("shapes of each shape:", [contours[i].shape for i, _ in enumerate(contours)])
print("hierarchy", hierarchy)

# visualizing the different contours
for i, contour in enumerate(contours):
    print(f"Is {i} a hexagon? {shape_detector.detect_shape(contour, 6)}")
    temp_img = img.copy()
    # [contour] to show connected lines, contour (w/o []) to only show points
    cv2.drawContours(temp_img, [contour], -1, (0, 0, 255), 3)

    # cv2.imshow(f"contour {i}", temp_img)
    # cv2.waitKey(1)

### drawing bboxes ###

# draw bboxes of all contours in green
bboxes = utils.generate_bboxes(contours)
print("all bboxes:", bboxes)
utils.draw_bboxes(target_img, bboxes, (0, 255, 0), 2)

# draw common bboxes of contours in blue
common_bboxes, weights = cv2.groupRectangles(bboxes, 1, 0.2)
print("common bboxes:", common_bboxes)
utils.draw_bboxes(target_img, common_bboxes, (255, 0, 0), 4)


### drawing centeriods ###

# draw centeriods of contours in red
contour_centeriods = utils.get_contour_centers(contours)
utils.draw_circles(target_img, contour_centeriods, color=(0, 0, 255))

# draw centeriods of all bboxes in green
bboxes_centeriods = utils.get_bbox_centers(bboxes)
utils.draw_circles(target_img, bboxes_centeriods, color=(0, 255, 0))

# draw centeriods of common bboxes in blue
common_bboxes_centeriods = utils.get_bbox_centers(common_bboxes)
utils.draw_circles(target_img, common_bboxes_centeriods, color=(255, 0, 0))


cv2.imshow("target", target_img)
cv2.waitKey(-1)