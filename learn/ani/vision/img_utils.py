from pathlib import Path
import cv2
import numpy as np


class GeneralUtils:
    def display_img(self, img, window_name="img", wait_key=-1):
        cv2.imshow(window_name, img)
        cv2.waitKey(wait_key)

    def detect_draw_contours(self, src, dest=None):
        """Draws contours from given src on given dst (drawn on src if no dst specified) and returns said contours."""
        contours = cv2.findContours(src, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
        for contour in contours:
            if dest is not None:
                cv2.drawContours(dest, [contour], -1, (255, 0, 0), 3)
            else:
                cv2.drawContours(src, [contour], -1, (255, 0, 0), 3)

        return contours

    def draw_bboxes(self, dest, rects, color=(0, 255, 0), thickness=2):
        for rect in rects:
            cv2.rectangle(
                dest, (rect[0], rect[1]), (rect[2], rect[3]), color, thickness
            )

    def draw_circles(self, dest, circles, radius=5, color=(0, 255, 0), thickness=-1):
        for circle in circles:
            cv2.circle(dest, (circle[0], circle[1]), radius, color, thickness)

    def get_contour_centers(self, contours):
        centeriods = []
        for contour in contours:
            M = cv2.moments(contour)
            center_x = int(M["m10"] / M["m00"]) if M["m00"] != 0 else 0
            center_y = int(M["m01"] / M["m00"]) if M["m00"] != 0 else 0
            centeriods.append(np.array([center_x, center_y]))
        return centeriods

    def get_bbox_centers(self, bboxes):
        centeriods = []
        for bbox in bboxes:
            centeriods.append(
                np.array([(bbox[0] + bbox[2]) // 2, (bbox[1] + bbox[3]) // 2])
            )
        return centeriods

    def calculate_iou(self, bbox1, bbox2):
        """Calculates IOU of given two bounding boxes in form of numpy array [x1, y1, x2, y2]"""
        assert bbox1[0] <= bbox1[2]
        assert bbox1[1] <= bbox1[3]
        assert bbox2[0] <= bbox2[2]
        assert bbox2[1] <= bbox2[3]

        x_left = max(bbox1[0], bbox2[0])
        y_top = max(bbox1[1], bbox2[1])

        x_right = min(bbox1[2], bbox2[2])
        y_bottom = min(bbox1[3], bbox2[3])

        if x_right < x_left or y_bottom < y_top:
            return 0.0

        intersection_area = (x_right - x_left + 1) * (y_bottom - y_top + 1)
        bbox1_area = (bbox1[2] - bbox1[0] + 1) * (bbox1[3] - bbox1[1] + 1)
        bbox2_area = (bbox2[2] - bbox2[0] + 1) * (bbox2[3] - bbox2[1] + 1)

        iou = intersection_area / float(bbox1_area + bbox2_area - intersection_area)
        assert iou >= 0.0
        assert iou <= 1.0
        return iou

    def generate_bboxes(self, contours):
        bounding_rects = []
        for contour in contours:
            x, y, w, h = cv2.boundingRect(contour)
            bounding_rects.append(np.array([x, y, x + w, y + h]))

        return bounding_rects

    def get_distinct_bboxes(self, bboxes, threshold):
        distinct_bboxes = []
        for i, bbox1 in enumerate(bboxes):
            distinct = True
            for j in range(i + 1, len(bboxes)):
                bbox2 = bboxes[j]
                if self.calculate_iou(bbox1, bbox2) > threshold:
                    distinct = False
                    break

            if distinct:
                distinct_bboxes.append(bbox1)

        return distinct_bboxes

    def connect_similar_contours(self, contours):
        rects = self.generate_bboxes(contours)
        new_rects, weights = cv2.groupRectangles(rects, 1)
        return new_rects, weights

    def filter_contours(self, contours, area_thresh):
        filtered = []
        for contour in contours:
            if cv2.contourArea(contour) > area_thresh:
                filtered.append(contour)

        return filtered


class ShapeDetector:
    def detect_shape(self, contour, num_sides):
        """Returns True if shape with provided number of sides is detected with supplied closed contour and is convex, False otherwise."""
        # must be closed contour
        perimeter = cv2.arcLength(contour, True)
        approx = cv2.approxPolyDP(contour, 0.005 * perimeter, True)

        if len(approx) == num_sides and cv2.isContourConvex(approx):
            return True

        return False

    def get_contours_of_shape(self, contours, num_sides):
        new_contours = []
        for contour in contours:
            if self.detect_shape(contour, num_sides):
                new_contours.append(contour)

        return new_contours


class DisplayUtils:
    def create_img_grid(self, img_matrix):
        grid = []
        for row in img_matrix:
            new_row = []
            for img in row:
                if len(img.shape) != 3:
                    img = cv2.cvtColor(img, cv2.COLOR_GRAY2BGR)
                new_row.append(img)
            grid.append(np.hstack(new_row))

        return np.vstack(grid)