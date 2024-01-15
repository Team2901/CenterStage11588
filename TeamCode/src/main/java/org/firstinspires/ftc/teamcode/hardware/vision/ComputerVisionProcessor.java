package org.firstinspires.ftc.teamcode.hardware.vision;

import android.graphics.Canvas;
import android.graphics.Paint;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;


/*
    The Camera Resolution must be 1280x720 and the rects are hard coded to this size
 */

public class ComputerVisionProcessor implements VisionProcessor {

    public static final int PIXEL_THRESHOLD_CONSTANT = 200;

    public enum PropPosition {LEFT, MIDDLE, RIGHT}

    public PropPosition propPosition = null;

    public enum AllianceColor {BLUE, RED}

    public AllianceColor allianceColor;
    private Mat lastImage = null;

    public int framesProcessed = 0;
    private boolean init = false;
    Telemetry telemetry;
    Size targetSize;
    CameraSubMat rightMat = new CameraSubMat(new Rect(830, 350, 150, 150));
    CameraSubMat middleMat = new CameraSubMat(new Rect(400, 325, 150, 150));


    public ComputerVisionProcessor(Telemetry telemetry) {
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
        middleMat.update(inputFrameRGB);
        rightMat.update(inputFrameRGB);
        framesProcessed++;
        cameraTelemetry();
        detectPropLocation();


        return inputFrameRGB; // This return value is userContext in call to onDrawFrame()
    }

    public void detectPropLocation() {
        if (framesProcessed < 10) {
            return;
        }
        // If we have found the prop position before, do nothing
        // If we have already looked at X frames, assume prop
        // is on the right
        if (framesProcessed > 60 && propPosition == null) {
            propPosition = PropPosition.LEFT;
            telemetry.addLine("No prop detected in 50 frames: GUESSING LEFT");
            return;
        }

        if (allianceColor == AllianceColor.BLUE) {
            if (middleMat.blueAmount > PIXEL_THRESHOLD_CONSTANT && middleMat.blueAmount > rightMat.blueAmount) {
                propPosition = PropPosition.MIDDLE;
            } else if (rightMat.blueAmount > PIXEL_THRESHOLD_CONSTANT && rightMat.blueAmount > middleMat.blueAmount) {
                propPosition = PropPosition.RIGHT;
            }
        } else if (allianceColor == AllianceColor.RED) {
            if (middleMat.redAmount > PIXEL_THRESHOLD_CONSTANT && middleMat.redAmount > rightMat.redAmount) {
                propPosition = PropPosition.MIDDLE;
            } else if (rightMat.redAmount > PIXEL_THRESHOLD_CONSTANT && rightMat.redAmount > middleMat.redAmount) {
                propPosition = PropPosition.RIGHT;
            }
        } else {
            throw new RuntimeException("Alliance color must be defined");
        }
    }
        @Override
        public void onDrawFrame (Canvas canvas,int onscreenWidth, int onscreenHeight,
        float scaleBmpPxToCanvasPx, float scaleCanvasDensity, Object userContext){
            double scaleFactor = Math.min(
                    canvas.getWidth() / targetSize.width,
                    canvas.getHeight() / targetSize.height);
            android.graphics.Rect rightRect = rightMat.createAndroidRect(scaleFactor);
            android.graphics.Rect middleRect = middleMat.createAndroidRect(scaleFactor);

            Paint paint = new Paint();
            paint.setAlpha(100);
            canvas.drawRect(rightRect, paint);
            canvas.drawRect(middleRect, paint);
        }

        public void cameraTelemetry () {
            telemetry.addData("Blue amount right", rightMat.blueAmount);
            telemetry.addData("Blue amount Middle", middleMat.blueAmount);
            telemetry.addData("Red amount right", rightMat.redAmount);
            telemetry.addData("Red amount Middle", middleMat.redAmount);
            telemetry.addData("Red Middle Conditon threasgold", middleMat.redAmount > PIXEL_THRESHOLD_CONSTANT);
            telemetry.addData("Red Middle Conditon greater", middleMat.redAmount > rightMat.redAmount);
            telemetry.addData("Frames Processed", framesProcessed);
            telemetry.addData("Prop Position", propPosition);
            telemetry.addData("Alliance Color", allianceColor);
            telemetry.update();
        }
    }
