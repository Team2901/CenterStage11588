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

public class ComputerVisionProcessor implements VisionProcessor {
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
    CameraSubMat rightMat = new CameraSubMat(new Rect(875, 570, 150, 150));
    CameraSubMat middleMat = new CameraSubMat(new Rect(300, 570, 150, 150));

    public ComputerVisionProcessor(Telemetry telemetry, AllianceColor allianceColor) {
        this.allianceColor = allianceColor;
        this.telemetry = telemetry;
    }
    public ComputerVisionProcessor(Telemetry telemetry) {
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
        middleMat.update(inputFrameRGB);
        framesProcessed++;
        detectPropLocation();

        cameraTelemetry();

        return inputFrameRGB; // This return value is userContext in call to onDrawFrame()
    }

    public void detectPropLocation() {
        // If we have found the prop position before, do nothing
        // If we have already looked at X frames, assume prop
        // is on the right
         if (framesProcessed > 50 && propPosition == null) {
            propPosition = PropPosition.LEFT;
        }
        else if (allianceColor == AllianceColor.BLUE) {
            if (rightMat.blueAmount > middleMat.blueAmount && rightMat.blueAmount > 800) {
                propPosition = PropPosition.RIGHT;
                return;
            } else if (middleMat.blueAmount > rightMat.blueAmount && middleMat.blueAmount > 800) {
                propPosition = PropPosition.MIDDLE;
                return;
            }
        }
        else if (allianceColor == AllianceColor.RED){
            if (rightMat.redAmount > middleMat.redAmount && rightMat.redAmount > 800) {
                propPosition = PropPosition.RIGHT;
                return;
            }
            else if (middleMat.redAmount > rightMat.redAmount && middleMat.redAmount > 800) {
                propPosition = PropPosition.MIDDLE;
                return;
            }
        }
        else {
            throw new RuntimeException("There should be a team color defined at this point");
        }
    }

    @Override
    public void onDrawFrame(Canvas canvas, int onscreenWidth, int onscreenHeight, float scaleBmpPxToCanvasPx, float scaleCanvasDensity, Object userContext) {
        double scaleFactor = Math.min(
                canvas.getWidth() / targetSize.width,
                canvas.getHeight() / targetSize.height);
        android.graphics.Rect leftRect = rightMat.createAndroidRect(scaleFactor);
        android.graphics.Rect middleRect = middleMat.createAndroidRect(scaleFactor);

        canvas.drawRect(leftRect, new Paint());
        canvas.drawRect(middleRect, new Paint());
    }

    public void cameraTelemetry() {
        telemetry.addData("Blue amount Left", rightMat.blueAmount);
        telemetry.addData("Blue amount Middle", middleMat.blueAmount);
        telemetry.addData("Red amount Left", rightMat.redAmount);
        telemetry.addData("Red amount Middle", middleMat.redAmount);
        telemetry.addData("Frames Processed", framesProcessed);
        telemetry.addData("Prop Found", propFound);
        telemetry.addData("Prop Position", propPosition);
        telemetry.update();
    }
}
