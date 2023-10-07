package org.firstinspires.ftc.teamcode.hardware.RI3W.vision;

import android.graphics.Canvas;
import android.graphics.Paint;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class RI3WComputerVisionProcessor implements VisionProcessor {
    public enum PropPosition { LEFT, MIDDLE, RIGHT }
    public PropPosition propPosition = null;

    public enum AllianceColor {BLUE, RED}

    public AllianceColor allianceColor;
    private Mat lastImage = null;

    public int framesProcessed = 0;
    public boolean propFound = false;
    private boolean init = false;
    Telemetry telemetry;
    Size targetSize;
    CameraSubMat leftMat = new CameraSubMat(new Rect(10, 300, 150, 150));
    CameraSubMat middleMat = new CameraSubMat(new Rect(300, 300, 150, 150));

    public RI3WComputerVisionProcessor(Telemetry telemetry, AllianceColor allianceColor) {
        this.allianceColor = allianceColor;
        this.telemetry = telemetry;
    }
    public RI3WComputerVisionProcessor(Telemetry telemetry) {
        this.allianceColor = AllianceColor.BLUE;
        this.telemetry = telemetry;
    }

    @Override
    public void init(int width, int height, CameraCalibration calibration) {
        telemetry.addLine("init...");
        targetSize = new Size(width, height);
        telemetry.update();
    }

    @Override
    public Object processFrame(Mat inputFrameRGB, long captureTimeNanos) {
        telemetry.clearAll();
        if (inputFrameRGB.type() == 24) {
            Imgproc.cvtColor(inputFrameRGB, inputFrameRGB, Imgproc.COLOR_RGBA2RGB);
        }
        leftMat.update(inputFrameRGB);
        middleMat.update(inputFrameRGB);
        framesProcessed++;
        setPropLocation();
        return inputFrameRGB; // Don't think this does anything
    }

    public void setPropLocation() {
        if (propPosition != null) {
            propFound = true;
            return;
        }
        if (framesProcessed > 50) {
            propPosition = PropPosition.RIGHT;
        }
        if (allianceColor == AllianceColor.BLUE) {
            if (leftMat.blueAmount > middleMat.blueAmount) {
                propPosition = PropPosition.LEFT;
            } else if (middleMat.blueAmount > leftMat.blueAmount) {
                propPosition = PropPosition.MIDDLE;
            }
        } else if (allianceColor == AllianceColor.RED){
            if (leftMat.redAmount > middleMat.redAmount) {
                propPosition = PropPosition.LEFT;
            } else if (middleMat.redAmount > leftMat.redAmount) {
                propPosition = PropPosition.MIDDLE;
            }
        } else {
            throw new RuntimeException("There should be a team color defined at this point");
        }
    }

    @Override
    public void onDrawFrame(Canvas canvas, int onscreenWidth, int onscreenHeight, float scaleBmpPxToCanvasPx, float scaleCanvasDensity, Object userContext) {
        double scaleFactor = Math.min(
                canvas.getWidth() / targetSize.width,
                canvas.getHeight() / targetSize.height);
        android.graphics.Rect leftRect = leftMat.createAndroidRect(scaleFactor);
        android.graphics.Rect middleRect = middleMat.createAndroidRect(scaleFactor);

        canvas.drawRect(leftRect, new Paint());
        canvas.drawRect(middleRect, new Paint());
    }

    public void cameraTelemetry() {
        telemetry.addData("Blue amount Left", leftMat.blueAmount);
        telemetry.addData("Blue amount Middle", middleMat.blueAmount);
        telemetry.addData("Red amount Left", leftMat.redAmount);
        telemetry.addData("Red amount Middle", middleMat.redAmount);
        telemetry.addData("Frames Processed", framesProcessed);
        telemetry.addData("Prop Position", propPosition);
        telemetry.update();
    }
}
