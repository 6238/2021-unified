from pathlib import Path
import json

import cv2
import numpy as np
import numpy.polynomial.polynomial as poly
import pandas as pd
import matplotlib.pyplot as plt


class DepthPredModel:
    def __init__(self, known_irl_length=None, dists=None, pixel_lengths=None):
        self.known_length = known_irl_length
        if (
            known_irl_length is not None
            and dists is not None
            and pixel_lengths is not None
        ):
            self.fit(dists, pixel_lengths, known_irl_length=self.known_length)
        else:
            self.coefs = None
            self.poly_func = None

    def load_from_json(self, meta_path, points_path):
        with open(meta_path, "r") as f:
            meta = json.load(f)
        df = pd.read_json(points_path)

        self.known_length = meta["known_length"]

        dists = []
        for col_name in df:
            dists.append(meta[col_name])

        pixel_lengths = []
        for col_name in df:
            col = df[col_name].tolist()
            # can change metric later if needed
            pixel_len = DepthPredModel.find_pixel_lengths(col).mean()
            pixel_lengths.append(pixel_len)

        self.fit(dists, pixel_lengths)

    def fit(self, dists, pixel_lengths, known_irl_length=None):
        if known_irl_length is not None:
            self.known_length = known_irl_length
        if self.known_length is None:
            raise ValueError("Must supply known_irl_length")

        focii = []
        for dist, pixel_len in zip(dists, pixel_lengths):
            focii.append(pixel_len * dist / self.known_length)

        self.coefs = poly.polyfit(dists, focii, 3)
        self.poly_func = poly.Polynomial(self.coefs)

    def predict(self, pixel_lengths):
        if self.coefs is None or self.poly_func is None:
            raise ValueError("Must fit before predicting")

        return self.poly_func(pixel_lengths)

    def predict_contours(self, contours):
        if self.coefs is None or self.poly_func is None:
            raise ValueError("Must fit before predicting")

        return np.array([self.predict_contour(contour) for contour in contours])

    def predict_contour(self, contour):
        if self.coefs is None or self.poly_func is None:
            raise ValueError("Must fit before predicting")

        if isinstance(contour, list) or (
            isinstance(contour, np.ndarray)
            and len(contour.shape) == 2
            and contour.shape[1] == 2
        ):
            return self.poly_func(self.find_pixel_lengths(contour).mean())

        if (
            isinstance(contour, np.ndarray)
            and len(contour.shape) == 3
            and contour.shape[1:] == (1, 2)
        ):
            return self.poly_func(self.find_pixel_lengths(contour[:, 0, :]).mean())

        raise ValueError("Unknown contour shape")

    def plot_func(self, x):
        if self.coefs is None or self.poly_func is None:
            raise ValueError("Must fit before predicting")
        fig, ax = plt.subplots()
        ax.plot(x, self.poly_func(x))
        return fig, ax

    @classmethod
    def find_pixel_lengths(cls, points):
        dists = []
        x0, y0 = points[0]
        x1, y1 = points[-1]
        dists.append(np.sqrt((x1 - x0) ** 2 + (y1 - y0) ** 2))

        for idx in range(len(points) - 1):
            x0, y0 = points[idx]
            x1, y1 = points[idx + 1]
            dists.append(np.sqrt((x1 - x0) ** 2 + (y1 - y0) ** 2))

        return np.array(dists, dtype=np.float32)