package org.firstinspires.ftc.teamcode.autonomous.qual;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.hardware.qual.QualHardware;
import org.firstinspires.ftc.teamcode.hardware.vision.ComputerVisionProcessor;

public abstract class AbstractAutonomous extends LinearOpMode {
    public enum PropPosition { LEFT, MIDDLE, RIGHT }
    //public ComputerVisionProcessor.AllianceColor teamColor;
    public QualHardware robot = new QualHardware();
    public void moveDiagonal(double distanceInches, double thetaDegrees){
        double yComponent = Math.cos(Math.toRadians(thetaDegrees))*distanceInches;
        double xComponent = Math.sin(Math.toRadians(thetaDegrees))*distanceInches;
        telemetry.addData("Ycomp", yComponent);
        telemetry.addData("Xcomp", xComponent);
        moveXY(yComponent, xComponent);
    }
    public void moveXY(double yInches, double xInches){
        int ticksY = (int) (yInches * robot.TICKS_PER_INCH);
        int ticksX = (int) (xInches * robot.TICKS_PER_INCH);

        robot.frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
// TODO: Figure out what this is doing
        robot.frontLeft.setTargetPosition(ticksY + ticksX);
        robot.frontRight.setTargetPosition(ticksY - ticksX);
        robot.backLeft.setTargetPosition(ticksY - ticksX);
        robot.backRight.setTargetPosition(ticksY + ticksX);

        robot.frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        //robot.speed = 0.10;
        robot.frontLeft.setPower(robot.speed);
        robot.frontRight.setPower(robot.speed);
        robot.backLeft.setPower(robot.speed);
        robot.backRight.setPower(robot.speed);

        while (opModeIsActive() && (robot.frontLeft.isBusy() || robot.frontRight.isBusy() ||
                robot.backLeft.isBusy() || robot.backRight.isBusy())){
            telemetryLog();
        }

        robot.frontLeft.setPower(0);
        robot.frontRight.setPower(0);
        robot.backLeft.setPower(0);
        robot.backRight.setPower(0);
// TODO: Save the original mode, put it back before you leave
        robot.frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    // TODO: The telemetryLog method should be enhanced to print out the
    // target and current position of each motor.
    private void telemetryLog() {
        telemetry.addData("angle",robot.getAngle());
        telemetry.update();
    }
    public void startToDropPurplePixel(PropPosition location){
        //right path blue
        if(location == PropPosition.RIGHT){
        }
        else if (location == PropPosition.MIDDLE) {
            moveXY(37, 0);
        }
        else {
        }
    }

    public void purplePixelToWhitePixelPickupFrontStage() {
        moveXY(15, 0);
        if(robot.getAlliance() == ComputerVisionProcessor.AllianceColor.RED) {
            turnToAngle(90);
        }else{
            turnToAngle(-90);
        }
        //turns 180 instead of 90
    }
    public void whitePixelsToBackstagePathFrontStage() {
        moveXY(70, 0);
        if(robot.getAlliance() == ComputerVisionProcessor.AllianceColor.RED) {
            moveXY(0, 27);
        }else{
            moveXY(0, -27);
        }
        moveXY(32, 0);
    }

    public void backstageToParkPathFrontStage() {
        if(robot.propDetectionProcessor.allianceColor == ComputerVisionProcessor.AllianceColor.RED) {
            moveXY(0, -30);
        }else{
            moveXY(0, 30);
        }
        moveXY(10, 0);
    }
    //robot is going left, so change one of the first two measurements

    public void navigateToBackdropBackStage() {
        if(robot.getAlliance() == ComputerVisionProcessor.AllianceColor.RED) {
            turnToAngle(-90);
        }else{
            turnToAngle(90);
        }
        moveXY(32, 0);
    }

    public void navigateToFrontStageBackStage() {
        if(robot.getAlliance() == ComputerVisionProcessor.AllianceColor.RED) {
            turnToAngle(-270); //Turn to face the front stage
        }else {
            turnToAngle(270);
        }
        if(robot.getAlliance() == ComputerVisionProcessor.AllianceColor.RED) {
            moveXY(0, -2);
        }else{
            moveXY(0, 2);
        }
        moveXY(90, 0); //Drive under truss
    }

    public void navigateToBackStageBackStage() {
        if(robot.getAlliance() == ComputerVisionProcessor.AllianceColor.RED) {
            moveXY(0, 33);//Move to center stage door
        }else{
            moveXY(0, -33);
        }
        if(robot.getAlliance() == ComputerVisionProcessor.AllianceColor.RED) {
            turnToAngle(-90);// Turn to face backstage
        }else{
            turnToAngle(90);
        }
        moveXY(86, 0);//Move under stage door to backstage
        if(robot.getAlliance() == ComputerVisionProcessor.AllianceColor.RED) {
            moveXY(0, 26);//positioned in-front of canvas
        }else{
            moveXY(0, -26);
        }
    }

    public void parkBackStage() {
        if(robot.getAlliance() == ComputerVisionProcessor.AllianceColor.RED) {
            moveXY(0, 24);
        }else{
            moveXY(0, -24);
        }
    }

    public void turnToAngle(double turnAngle){

        //robot.getAngle is between -180 and 180, starting at 0
        double turnPower = 0;
        double targetAngle = AngleUnit.normalizeDegrees(turnAngle) + 180;
        double startAngle = robot.getAngle() + 180;
        double turnError = AngleUnit.normalizeDegrees(targetAngle - startAngle);
        while(opModeIsActive() && !(turnError < .5 && turnError > -.5)){
            if(turnError >= 0){
                turnPower = turnError/90;
                if(turnPower > robot.speed){
                    turnPower = robot.speed;
                }
            }else if(turnError < 0){
                turnPower = turnError/90;
                if(turnPower < -robot.speed){
                    turnPower = -robot.speed;
                }
            }
            robot.frontLeft.setPower(-turnPower);
            robot.frontRight.setPower(turnPower);
            robot.backLeft.setPower(-turnPower);
            robot.backRight.setPower(turnPower);

            double currentAngle = robot.getAngle() + 180;
            turnError = AngleUnit.normalizeDegrees(targetAngle - currentAngle);
            telemetryLog();
        }
        robot.frontLeft.setPower(0);
        robot.frontRight.setPower(0);
        robot.backRight.setPower(0);
        robot.backLeft.setPower(0);
    }

    public void backStagePath() {
        navigateToBackdropBackStage();
        navigateToFrontStageBackStage();
        navigateToBackStageBackStage();
        parkBackStage();
    }

}
