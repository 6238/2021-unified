import cv2
import matplotlib.pyplot as plt
# print(cv2.__version__)

# loading image
img_path = "./2012-retroreflective-targets.jpg"
img = cv2.imread(img_path)
print(img.shape)

# displaying image
# plt.imshow(img)
# plt.show()
# cv2.imshow("original image", img)
cv2.waitKey(-1)

# crop image
cropped_img = img[200:350, 290:490, 0:3] # 0-2 was actually getting elements 0 and 1
print(cropped_img.shape)

print(f"pixel values are between: {cropped_img.max()} and {cropped_img.min()}")


edges = cv2.Canny(cropped_img, 400, 200)

# messing with brightness and contrast
# new_img = cropped_img * alpha + cropped_img * beta + gamma
# new_img = cv2.addWeighted(cropped_img, 0.2, cropped_img, 0, 50)
# new_img = cv2.convertScaleAbs(cropped_img, 1.5, 20)


# displaying cropped image
# plt.imshow(cropped_img)
# plt.show()
# cv2.imshow("edges", edges)
cv2.waitKey(-1)
