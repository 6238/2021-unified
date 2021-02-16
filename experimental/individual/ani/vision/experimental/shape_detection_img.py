from color_filtering_img import apply_color_filter
from pathlib import Path
import cv2
import numpy as np

from img_utils import DisplayUtils, GeneralUtils, ShapeDetector


shape_detector = ShapeDetector()
utils = GeneralUtils()
display_utils = DisplayUtils()


def apply_ops(frame):
    """DEPRECATED, used for testing appying various operations to the frame"""
    gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
    # cv2.imshow("gray before", gray)

    # blurred = cv2.bilateralFilter(gray, 25, 15, 75)
    blurred = cv2.GaussianBlur(gray, (5, 5), 0)
    # _, thresh = cv2.threshold(gray, 150, 255, cv2.THRESH_BINARY)
    thresh = cv2.adaptiveThreshold(
        blurred, 255, cv2.ADAPTIVE_THRESH_GAUSSIAN_C, cv2.THRESH_BINARY, 201, 0
    )
    # thresh = cv2.bitwise_or(thresh_norm, thresh_adapt)
    # cv2.imshow("blurred", blurred)
    # cv2.imshow("thresh", thresh)

    median = np.median(gray)
    sigma = 0.33
    lower_thresh = int(max(0, (1.0 - sigma) * median))
    upper_thresh = int(min(255, (1.0 + sigma) * median))

    canny = cv2.Canny(thresh, lower_thresh, upper_thresh)
    # cv2.imshow("canny", canny)
    # canny_erode = cv2.erode(canny, (15, 15))
    # canny_dilate = cv2.dilate(canny_erode, (15, 15), iterations=1)
    # cv2.imshow("canny dilated", canny)

    contours, hierarchy = cv2.findContours(
        canny, cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE
    )
    contours = utils.filter_contours_area(contours, 1000)

    hexagons = shape_detector.get_contours_of_shape(contours, 6)

    ### Drawing ###
    contour_img = frame.copy()
    hexagons_img = frame.copy()

    # bboxes = utils.generate_bboxes(contours)
    # iou_bboxes = utils.get_distinct_bboxes(bboxes, 0.8)
    # iou_bboxes_centeriods = utils.get_bbox_centers(iou_bboxes)

    cv2.drawContours(contour_img, contours, -1, (0, 0, 255), 3)
    cv2.drawContours(hexagons_img, hexagons, -1, (0, 255, 0), 3)
    # utils.draw_bboxes(frame, iou_bboxes, (255, 255, 255), 2)
    # utils.draw_circles(frame, iou_bboxes_centeriods, color=(255, 255, 255))

    # grid = display_utils.create_img_grid(
    #     [[frame, blurred, thresh], [canny, contour_img, hexagons_img]]
    # )
    # return grid
    frame_list = [frame, thresh, canny, contour_img, hexagons_img]
    return frame_list


if __name__ == "__main__":

    img_path = Path("./target-imgs/mid-target.jpg")
    # img_path = Path("./2012-retroreflective-targets.jpg")
    img = cv2.imread(str(img_path))
    img = cv2.resize(img, (img.shape[1] // 2, img.shape[0] // 2))
    target_img = img.copy()  # for drawing bounding box on

    # cv2.imshow("original", img)
    # cv2.waitKey(1)

    img = apply_color_filter(img)
    # cv2.imshow("filtered", img)
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

    canny = cv2.dilate(canny, (15, 15), iterations=1)
    # canny = cv2.morphologyEx(canny, cv2.MORPH_DILATE, (15, 15))
    # cv2.imshow("canny dilated", canny)
    # cv2.waitKey(1)

    contours, hierarchy = cv2.findContours(
        canny, cv2.RETR_CCOMP, cv2.CHAIN_APPROX_SIMPLE
    )

    cv2.drawContours(target_img, contours, -1, (0, 0, 255), 3)
    # cv2.imshow("contours before", target_img)
    # cv2.waitKey(1)

    # hierarchy is of form [Next, Previous, First_Child, Parent]

    print("number of shapes found:", len(contours))
    print("shapes of each shape:", [contours[i].shape for i, _ in enumerate(contours)])
    print("hierarchy", hierarchy)

    # visualizing the different approxes
    for i, contour in enumerate(contours):
        print(f"Is {i} a hexagon? {shape_detector.detect_shape(contour, 6)}")
        temp_img = img.copy()
        # [contour] to show connected lines, contour (w/o []) to only show points

        perimeter = cv2.arcLength(contour, True)
        approx = cv2.approxPolyDP(contour, 0.01 * perimeter, True)
        is_convex = cv2.isContourConvex(approx)
        cv2.drawContours(temp_img, approx, -1, (0, 255, 255), 12)

        # cv2.imshow(f"contour {i}, sides {len(approx)}, convex {is_convex}", temp_img)
        # cv2.waitKey(1)

    ### drawing bboxes ###

    # draw bboxes of all contours in green
    bboxes = utils.generate_bboxes(contours)
    print("all bboxes:", bboxes)
    display_utils.draw_bboxes(target_img, bboxes, (0, 255, 0), 2)

    # draw common bboxes of contours in blue
    # the groupRectangles doesn't work well, my function works better
    common_bboxes, weights = cv2.groupRectangles(bboxes, 1, 0.2)
    print("common bboxes:", common_bboxes)
    display_utils.draw_bboxes(target_img, common_bboxes, (255, 0, 0), 2)

    # draw iou common bboxes of contours in white
    iou_bboxes = utils.get_distinct_bboxes(bboxes, 0.8)
    print("iou bboxes:", common_bboxes)
    display_utils.draw_bboxes(target_img, iou_bboxes, (255, 255, 255), 2)

    ### drawing centeriods ###

    # draw centeriods of contours in red
    contour_centeriods = utils.get_contour_centers(contours)
    display_utils.draw_circles(target_img, contour_centeriods, color=(0, 0, 255))

    # draw centeriods of all bboxes in green
    bboxes_centeriods = utils.get_bbox_centers(bboxes)
    display_utils.draw_circles(target_img, bboxes_centeriods, color=(0, 255, 0))

    # draw centeriods of common bboxes in blue
    common_bboxes_centeriods = utils.get_bbox_centers(common_bboxes)
    display_utils.draw_circles(target_img, common_bboxes_centeriods, color=(255, 0, 0))

    # draw centeriods of iou common bboxes in white
    iou_bboxes_centeriods = utils.get_bbox_centers(iou_bboxes)
    display_utils.draw_circles(target_img, iou_bboxes_centeriods, color=(255, 255, 255))

    [
        print(f"is contour {i} a hex: {shape_detector.detect_shape(contour, 6)}")
        for i, contour in enumerate(contours)
    ]

    # cv2.imshow("target", target_img)
    cv2.waitKey(-1)