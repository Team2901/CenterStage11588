package org.firstinspires.ftc.teamcode.hardware.RI3W.vision;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;

public class CameraSubMat {
    Mat subMat;
    Mat blueMask = new Mat();
    Mat redMask = new Mat();
    Rect rect;
    double redAmount = 0;
    double blueAmount = 0;


    public CameraSubMat(Rect rect) {
        this.rect = rect;
    }
    public Telemetry telemetry;

    public void update(Mat lastImage) {
        if (lastImage == null) {
            return;
        }
        subMat = lastImage.submat(rect);
        //Core.inRange(subMat, new Scalar(100, 50, 50), new Scalar(255, 100, 155), redMask);
        //Core.inRange(subMat, new Scalar(0, 0, 0), new Scalar(255, 255, 255), blueMask);
        Core.inRange(subMat, new Scalar(100, 50, 50), new Scalar(255, 100, 155), redMask);
        Core.inRange(subMat, new Scalar(0, 0, 80), new Scalar(70, 70, 255), blueMask);
        blueAmount = Core.countNonZero(blueMask);
        redAmount = Core.countNonZero(redMask);
    }

    public android.graphics.Rect createAndroidRect(double scaleFactor) {
        int right = (int)((rect.width + rect.x) * scaleFactor);
        int left = (int)(rect.x * scaleFactor);
        int bottom = (int)((rect.height + rect.y) * scaleFactor);
        int top = (int)(rect.y * scaleFactor);
        android.graphics.Rect returnRect = new android.graphics.Rect(left, top, right, bottom);

        return returnRect;
    }
}
