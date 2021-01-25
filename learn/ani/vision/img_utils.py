from pathlib import Path
import cv2
import numpy as np
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
            if self.detect_shape(contour, num_sides):
                if dest is not None:
                    cv2.drawContours(dest, [contour], -1, (255, 0, 0), 3)
                else:
                    cv2.drawContours(src, [contour], -1, (255, 0, 0), 3)
    
        return contours