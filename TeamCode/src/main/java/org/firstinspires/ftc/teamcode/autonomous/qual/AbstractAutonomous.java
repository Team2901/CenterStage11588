package org.firstinspires.ftc.teamcode.autonomous.qual;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.hardware.Qual.QualHardware;
import org.firstinspires.ftc.teamcode.hardware.RI3W.RI3WHardware;
import org.firstinspires.ftc.teamcode.hardware.RI3W.vision.RI3WComputerVisionProcessor;

public class AbstractAutonomous extends LinearOpMode {
    public enum PropPosition { LEFT, MIDDLE, RIGHT }
    public RI3WComputerVisionProcessor.AllianceColor teamColor;
    public QualHardware robot = new QualHardware();
    public void moveDiagonal(double distanceInches, double thetaDegrees){
        double yComponent = Math.cos(Math.toRadians(thetaDegrees))*distanceInches;
        double xComponent = Math.sin(Math.toRadians(thetaDegrees))*distanceInches;
        telemetry.addData("Ycomp", yComponent);
        telemetry.addData("Xcomp", xComponent);
        moveXY(yComponent, xComponent);
    }
    @Override
    public void runOpMode() throws InterruptedException {

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

        robot.speed = 0.10;
        robot.frontLeft.setPower(robot.speed);
        robot.frontRight.setPower(robot.speed);
        robot.backLeft.setPower(robot.speed);
        robot.backRight.setPower(robot.speed);

        // TODO: change to and instead of or
        while (opModeIsActive() && (robot.frontLeft.isBusy() && robot.frontRight.isBusy() &&
                robot.backLeft.isBusy() && robot.backRight.isBusy())){
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

    private void telemetryLog() {
        telemetry.addData("angle",robot.getAngle());
        telemetry.update();
    }
    public void spikePixel(PropPosition location){
        //right path blue
        moveXY(20.5, 0);
        if(location == PropPosition.RIGHT){

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
                turnPower = turnError/50;
                if(turnPower > .75){
                    turnPower = .75;
                }
            }else if(turnError < 0){
                turnPower = turnError/50;
                if(turnPower < -.75){
                    turnPower = -.75;
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

}
