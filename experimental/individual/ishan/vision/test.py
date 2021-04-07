import cv2
import numpy as np

img = np.random.randn(16, 16, 3)
n = 32
img = np.kron(img, np.ones((n, n, 1)))

cv2.imshow("test", img)
cv2.waitKey(-1)
