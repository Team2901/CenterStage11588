package org.firstinspires.ftc.teamcode.hardware.RI3W.vision;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;

public class CameraSubMat {
    Mat subMat;
    Mat blueMask = new Mat();
    Mat redMask = new Mat();
    Rect rect;
    Mat lastImage;
    double redAmount = 0;
    double blueAmount = 0;

    public CameraSubMat(Rect rect, Mat lastImage) {
        this.rect = rect;
        this.lastImage = lastImage;
    }

    public void update() {
        subMat = lastImage.submat(rect);
        Core.inRange(subMat, new Scalar(100, 50, 50), new Scalar(255, 100, 155), redMask);
        Core.inRange(subMat, new Scalar(0, 0, 80), new Scalar(70, 70, 255), blueMask);

        blueAmount = Core.countNonZero(redMask);
        redAmount = Core.countNonZero(blueMask);
    }
}
