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

    public enum AllianceColor {BLUE, RED}

    public AllianceColor allianceColor;
    private Mat lastImage = null;

    public int framesProcessed = 0;
    private boolean propFound = false;
    private boolean init = false;
    Telemetry telemetry;
    Size targetSize;
    Mat inputFrameGray;
    CameraSubMat leftMat = new CameraSubMat(new Rect(10, 10, 50, 50));
    CameraSubMat rightMat = new CameraSubMat(new Rect(40, 10, 50, 50));
    CameraSubMat middleMat = new CameraSubMat(new Rect(100, 10, 50, 50));

    public RI3WComputerVisionProcessor(Telemetry telemetry) {
//        allianceColor = color;
        this.telemetry = telemetry;
        leftMat.telemetry = telemetry;
    }

    @Override
    public void init(int width, int height, CameraCalibration calibration) {
        telemetry.addLine("init...");
        targetSize = new Size(width, height);
        inputFrameGray = new Mat(targetSize, CvType.CV_8UC1);
        telemetry.update();
    }

    @Override
    public Object processFrame(Mat inputFrameRGB, long captureTimeNanos) {
        telemetry.clearAll();
        if (inputFrameRGB.type() == 24) {
            Imgproc.cvtColor(inputFrameRGB, inputFrameRGB, Imgproc.COLOR_RGBA2RGB);
        }
        leftMat.update(inputFrameRGB);
        rightMat.update(inputFrameRGB);
        middleMat.update(inputFrameRGB);
        framesProcessed++;
        telemetry.addData("Blue amount", leftMat.blueAmount);
        telemetry.addData("Red amount", leftMat.redAmount);
        telemetry.addData("Frames Processed", framesProcessed);
        telemetry.update();
        return inputFrameRGB; // Don't think this does anything
    }

    public PropPosition getPropLocation() {
        if (allianceColor == AllianceColor.BLUE) {
            if (leftMat.blueAmount > rightMat.blueAmount && leftMat.blueAmount > middleMat.blueAmount) {
                propFound = true;
                return PropPosition.LEFT;
            } else if (rightMat.blueAmount > leftMat.blueAmount && rightMat.blueAmount > middleMat.blueAmount) {
                propFound = true;
                return PropPosition.RIGHT;
            } else if (middleMat.blueAmount > rightMat.blueAmount && middleMat.blueAmount > leftMat.blueAmount) {
                propFound = true;
                return PropPosition.MIDDLE;
            } else {
                throw new RuntimeException("No prop placement could be determined");
            }
        } else if (allianceColor == AllianceColor.RED){
            if (leftMat.redAmount > rightMat.redAmount && leftMat.redAmount > middleMat.redAmount) {
                propFound = true;
                return PropPosition.LEFT;
            } else if (rightMat.redAmount > leftMat.redAmount && rightMat.redAmount > middleMat.redAmount) {
                propFound = true;
                return PropPosition.RIGHT;
            } else if (middleMat.redAmount > rightMat.redAmount && middleMat.redAmount > leftMat.redAmount) {
                propFound = true;
                return PropPosition.MIDDLE;
            } else {
                throw new RuntimeException("No prop placement could be determined");
            }
        } else {
            throw new RuntimeException("There should be a team color defined at this point");
        }
    }

    @Override
    public void onDrawFrame(Canvas canvas, int onscreenWidth, int onscreenHeight, float scaleBmpPxToCanvasPx, float scaleCanvasDensity, Object userContext) {
        //telemetry.addData("CanvasWidth", canvas.getWidth());
        //telemetry.addData("onscreenWidth", onscreenWidth);
        //telemetry.addData("matWidth", targetSize.width);
        //telemetry.update();
        double scaleFactor = Math.min(
                canvas.getWidth() / targetSize.width,
                canvas.getHeight() / targetSize.height);
        //telemetry.addData("scaleFactor", scaleFactor);
        android.graphics.Rect leftRect = leftMat.createAndroidRect(scaleFactor);
        android.graphics.Rect rightRect = rightMat.createAndroidRect(scaleFactor);
        android.graphics.Rect middleRect = middleMat.createAndroidRect(scaleFactor);

        canvas.drawRect(leftRect, new Paint());
        canvas.drawRect(middleRect, new Paint());
        canvas.drawRect(rightRect, new Paint());
    }

    public void cameraTelemetry() {
        telemetry.addData("Blue amount Left", leftMat.blueAmount);
        telemetry.addData("Blue amount Right", rightMat.blueAmount);
        telemetry.addData("Blue amount Middle", middleMat.blueAmount);
        telemetry.addData("Red amount Left", leftMat.blueAmount);
        telemetry.addData("Red amount Right", rightMat.blueAmount);
        telemetry.addData("Red amount Middle", middleMat.blueAmount);
        telemetry.addData("Frames Processed", framesProcessed);
    }
}
