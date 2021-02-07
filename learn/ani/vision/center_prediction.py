from pathlib import Path

import numpy as np
import numpy.polynomial.polynomial as poly
import matplotlib.pyplot as plt


class CenterPredModel:
    def __init__(self, xs=None, ys=None, ts=None, degree=None):
        if degree is None:
            self.degree = 1
        else:
            self.degree = degree

        if xs is None and ys is None:
            self.poly_x = poly.Polynomial(np.zeros((self.degree + 1)))
            self.poly_y = poly.Polynomial(np.zeros((self.degree + 1)))
        else:
            self.fit(xs, ys, ts)

    def fit(self, xs, ys, ts=None):
        assert len(xs) == len(ys)
        if ts is None:
            ts = np.arange(len(xs))
        coefs_x = poly.polyfit(ts, xs, self.degree)
        coefs_y = poly.polyfit(ts, ys, self.degree)
        self.poly_x = poly.Polynomial(coefs_x)
        self.poly_y = poly.Polynomial(coefs_y)

    def fit_frames(self, frames):
        frames = np.array(frames)
        ts = None
        if frames.shape[-1] == 2:
            xs = frames[:, 0]
            ys = frames[:, 1]
        elif frames.shape[-1] == 3:
            ts = frames[:, 0]
            xs = frames[:, 1]
            ys = frames[:, 2]
        else:
            raise ValueError("Too many dims in input frames!")
        self.fit(xs, ys, ts=ts)

    def predict_ndarray(self, ts, dtype=float):
        pred_x = self.poly_x(ts).astype(dtype)
        pred_y = self.poly_y(ts).astype(dtype)
        return [pred_x, pred_y]

    def predict_single_scalar(self, t, dtype=int):
        # assert type(t) == int
        pred_x = dtype(self.poly_x(t))
        pred_y = dtype(self.poly_y(t))
        return [pred_x, pred_y]

    @staticmethod
    def graph_truth_pred_history(truth_history, pred_history):
        fig, axes = plt.subplots(2, 1, sharex=True)

        axes[0].scatter(truth_history[:, 0], truth_history[:, 1], color="b")
        axes[1].scatter(truth_history[:, 0], truth_history[:, 2], color="b")

        # preds
        axes[0].scatter(
            pred_history[:, 0],
            pred_history[:, 1],
            marker="o",
            s=20,
            alpha=1,
            color="red",
        )
        axes[1].scatter(
            pred_history[:, 0],
            pred_history[:, 2],
            marker="o",
            s=20,
            alpha=1,
            color="red",
        )

        return fig


if __name__ == "__main__":

    deg1 = 1
    frames1 = 3
    constant_thresh1 = 6
    truth_path = Path(
        f"centroid-pred-data/truth-centroid-history-deg{deg1}-frames{frames1}-constantthresh{constant_thresh1}.npy"
    )
    truth = np.load(truth_path)
    pred_path = Path(
        f"centroid-pred-data/pred-centroid-history-deg{deg1}-frames{frames1}-constantthresh{constant_thresh1}.npy"
    )
    pred = np.load(pred_path)
    fig1 = CenterPredModel.graph_truth_pred_history(truth, pred)
    fig1.suptitle(
        f"degree: {deg1}, frames: {frames1}, constant_thresh: {constant_thresh1}"
    )
    fig1.show()

    deg2 = 1
    frames2 = 3
    constant_thresh2 = 3
    truth_path = Path(
        f"centroid-pred-data/truth-centroid-history-deg{deg2}-frames{frames2}-constantthresh{constant_thresh2}.npy"
    )
    truth = np.load(truth_path)
    pred_path = Path(
        f"centroid-pred-data/pred-centroid-history-deg{deg2}-frames{frames2}-constantthresh{constant_thresh2}.npy"
    )
    pred = np.load(pred_path)
    fig2 = CenterPredModel.graph_truth_pred_history(truth, pred)
    fig2.suptitle(
        f"degree: {deg2}, frames: {frames2}, constant_thresh: {constant_thresh2}"
    )
    fig2.show()

    plt.show()

    # data_path = Path("truth_centroid_history.npy")
    # data = np.load(data_path)
    # print(data.shape)

    # NUM_FRAMES = 15
    # DEGREE = 10

    # last_frames = data[-NUM_FRAMES:, :]
    # last_frames_train = last_frames[:-1]

    # model = CenterPredModel()
    # model.fit_frames(last_frames_train, DEGREE)

    # pred_x, pred_y = model.predict_single_scalar(last_frames[-2, 0] + 1)
    # print(last_frames[-2, 0] + 1, pred_x, pred_y)

    # fig, axes = plt.subplots(2, 1, sharex=True)
    # axes[0].plot(last_frames[:, 0], last_frames[:, 1], "b")
    # axes[1].plot(last_frames[:, 0], last_frames[:, 2], "b")

    # axes[0].plot(last_frames_train[:, 0], model.poly_x(last_frames_train[:, 0]), "r")
    # axes[1].plot(last_frames_train[:, 0], model.poly_y(last_frames_train[:, 0]), "r")

    # # plotting preds
    # axes[0].plot(
    #     [last_frames[-2, 0] + 1], [pred_x], marker="o", markersize=3, color="red"
    # )
    # axes[1].plot(
    #     [last_frames[-2, 0] + 1], [pred_y], marker="o", markersize=3, color="red"
    # )

    # plt.show()