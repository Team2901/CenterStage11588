package org.firstinspires.ftc.teamcode.hardware.RI3W.vision;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.openftc.easyopencv.OpenCvPipeline;

public class RI3WComputerVisionPipeline extends OpenCvPipeline {



    public enum PropPosition { LEFT, MIDDLE, RIGHT }

    public enum AllianceColor {BLUE, RED}

    public AllianceColor allianceColor;
    private Mat lastImage = null;

    private CameraSubMat leftMat = new CameraSubMat(new Rect(1, 1, 10, 10), lastImage);
    private CameraSubMat middleMat = new CameraSubMat(new Rect(1, 1, 10, 10), lastImage);
    private CameraSubMat rightMat = new CameraSubMat(new Rect(1, 1, 10, 10), lastImage);
    public int framesProcessed = 0;
    private boolean propFound = false;

    public RI3WComputerVisionPipeline(AllianceColor color) {
        allianceColor = color;
    }
    @Override
    public Mat processFrame(Mat input) {
        if (propFound) {
            return lastImage;
        }
        framesProcessed++;
        if (input == null) {
            return lastImage;
        //Makes sure doesn't crash when the camera does nothing
        }
        leftMat.update();
        middleMat.update();
        rightMat.update();
        input.copyTo(lastImage);

        return lastImage;
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
}
