from pathlib import Path
import cv2
import numpy as np

# 0 to convert to grayscale
template_img_path = Path("./target-imgs/target-template.jpg")
template_img = cv2.imread(str(template_img_path))
template_img = cv2.cvtColor(template_img, cv2.COLOR_BGR2GRAY)
template_img = cv2.resize(
    template_img, (template_img.shape[1] // 2, template_img.shape[0] // 2)
)

img_path = Path("./target-imgs/mid-target.jpg")
img = cv2.imread(str(img_path))
img = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
img = cv2.resize(img, (img.shape[1] // 2, img.shape[0] // 2))

orb = cv2.ORB_create()
kp1, des1 = orb.detectAndCompute(template_img, None)
kp2, des2 = orb.detectAndCompute(img, None)

bf = cv2.BFMatcher(cv2.NORM_HAMMING, crossCheck=True)
matches = bf.match(des1, des2)
matches = sorted(matches, key=lambda x: x.distance)

matches_display = cv2.drawMatches(template_img, kp1, img, kp2, matches[:10], None)
cv2.imshow("matches", matches_display)
cv2.waitKey(-1)