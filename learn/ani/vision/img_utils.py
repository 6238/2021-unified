from pathlib import Path
import cv2
import numpy as np
import matplotlib.pyplot as plt
import imutils


class GeneralUtils:
    def display_img(self, img, window_name="img", wait_key=-1):
        cv2.imshow(window_name, img)
        cv2.waitKey(wait_key)

    def detect_draw_contours(self, src, dest=None):
        """Draws contours from given src on given dst (drawn on src if no dst specified) and returns said contours."""
        contours = cv2.findContours(src, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
        contours = imutils.grab_contours(contours)
        for contour in contours:
            if dest is not None:
                cv2.drawContours(dest, [contour], -1, (255, 0, 0), 3)
            else:
                cv2.drawContours(src, [contour], -1, (255, 0, 0), 3)
        
        return contours
        

class ShapeDetector:
    def detect_shape(self, contour, num_sides):
        """Returns True if shape with provided number of sides is detected with supplied closed contour, False otherwise."""
        # must be closed contour
        perimeter = cv2.arcLength(contour, True)
        sides_approx = cv2.approxPolyDP(contour, 0.04 * perimeter, True)

        if len(sides_approx) == num_sides:
            return True

        return False

    def detect_draw_contours(self, src, dest, num_sides):
        """Draws contours from given src on given dst (drawn on src if no dst specified) and returns said contours
        if detected contours have given number of sides."""
        contours = cv2.findContours(src, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
        contours = imutils.grab_contours(contours)
        for contour in contours:
            if shape_detector.detect_shape(contour, num_sides):
                if dest is not None:
                    cv2.drawContours(dest, [contour], -1, (255, 0, 0), 3)
                else:
                    cv2.drawContours(src, [contour], -1, (255, 0, 0), 3)
    
        return contours


if __name__ == '__main__':
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