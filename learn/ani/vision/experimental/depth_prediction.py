from pathlib import Path

import cv2
import numpy as np
import numpy.polynomial.polynomial as poly
import pandas as pd
import matplotlib.pyplot as plt


# side length of hexagonal target: 30/sqrt(3) inches or 439.941 mm
SIDE_LENGTH = 439.941

FT_MM = {
    "5ft": 1524,
    "10ft": 3048,
    "15ft": 4572,
    "20ft": 6096,
    "25ft": 7620,
    "30ft": 9144,
}

imgs_dir = Path(
    r"E:\code\projects\frc-vision\datasets\target-dataset\vision-videos\distance-measuring"
)
pixel_to_dist_path = Path(
    r"E:\code\projects\frc-vision\datasets\target-dataset\vision-videos\distance-measuring\pixel_distances.json"
)

df = pd.read_json(pixel_to_dist_path)

for col_name in df:
    col = df[col_name]
    dists = []
    x0, y0 = col.iloc[0]
    x1, y1 = col.iloc[-1]
    dists.append(np.sqrt((x1 - x0) ** 2 + (y1 - y0) ** 2))
    for idx in range(len(col) - 1):
        x0, y0 = col[idx]
        x1, y1 = col[idx + 1]
        dists.append(np.sqrt((x1 - x0) ** 2 + (y1 - y0) ** 2))

    df[col_name] = dists

df = df.mean()

focii = []
for pixels in df:
    focii.append(pixels * FT_MM[col_name] / SIDE_LENGTH)

coefs = poly.polyfit(range(5, 35, 5), focii, 3)
poly_func = poly.Polynomial(coefs)

print(poly_func)

fig, ax = plt.subplots()

# ax.plot(df)
ax.plot(range(5, 35, 5), focii)
ax.plot(range(5, 30), poly_func(range(5, 30)))
plt.show()
