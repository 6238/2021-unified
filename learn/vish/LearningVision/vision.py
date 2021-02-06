import cv2
import matplotlib.pyplot as plt


img_path = "./datasets/2012-retroreflective-targets.jpg"

print(cv2.__version__) 

if(5 == 4):
    print("hehee")

#Shows normal image
img = cv2.imread(img_path) #imread returns numpy array
# cv2.imshow("original image", img) #Shows image
# cv2.waitKey(-1) #keeps it up

#Lets you see specific pixel points
plt.imshow(img) 
# plt.show()

cropped_img = img[137:215, 185:300]
# print(cropped_img.shape)

edges = cv2.Canny(cropped_img, 400, 120)

alpha = 0.5 #Alpha is for contrast
beta = 10   #Beta is for brightness
new_img = cv2.addWeighted(cropped_img, alpha, cropped_img, alpha, beta)
print("heelle")
plt.imshow(new_img)
plt.show()


