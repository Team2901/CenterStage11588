package org.firstinspires.ftc.teamcode.Test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.hardware.RI3W.RI3WHardware;
import org.firstinspires.ftc.teamcode.hardware.controller.ImprovedGamepad;


@TeleOp(name="PIDTunerV2", group="Test")
public class PIDTunerV2 extends OpMode {
    public RI3WHardware robot = new RI3WHardware();
    public ImprovedGamepad gamepad;
    double turningPower = 0;
    ElapsedTime PIDTimer = new ElapsedTime();
    public enum ClawPosition{Open, Closed}
    ClawPosition currentClawPosition = ClawPosition.Closed;
    public enum Height{INTAKE, LOW, MID, HIGH, MAX}
    Height currentLiftHeight = Height.INTAKE;
    int liftTarget = 80;
    Height lastLiftHeight = currentLiftHeight;
    double liftSpeed = 0.2;
    int intakeLiftPosition = (int) RI3WHardware.INTAKE_ENCODER_VALUE;
    int lowLiftPosition = (int) RI3WHardware.LOW_POLE_ENCODER_VALUE;
    int midLiftPosition = (int) RI3WHardware.MID_POLE_ENCODER_VALUE;
    int highLiftPosition = (int) RI3WHardware.HIGH_POLE_ENCODER_VALUE;
    int maxLiftPosition = (int) RI3WHardware.MAX_HEIGHT_ENCODER_VALUE;

    double error = 0.0;
    int reference = 0;
    int lastReference = reference;
    double total = 0.0;
    double kg = 0;
    double kp = 0.0;
    double ki = 0.0;
    double kd = 0.0;
    double kCos = 0.0;
    double pLift = 0.0;
    double iLift = 0.0;
    double dLift = 0.0;
    double cosLift = 0.0;
    double iLiftMax = 0.0;
    double liftHeight = 0;


    @Override
    public void init() {
        gamepad = new ImprovedGamepad(gamepad1, new ElapsedTime(), "Gamepad");
        robot.init(this.hardwareMap);
    }

    @Override
    public void loop() {
        gamepad.update();

        if(gamepad.dpad_left.isInitialPress()) {
            //Sets Lift to intake level
            liftTarget = intakeLiftPosition;
            currentLiftHeight = Height.INTAKE;
        } else if (gamepad.dpad_down.isInitialPress()) {
            //Sets Lift to Low level
            liftTarget = lowLiftPosition;
            currentLiftHeight = Height.LOW;
        } else if (gamepad.dpad_right.isInitialPress()) {
            //Sets Lift to Mid level
            liftTarget = midLiftPosition;
            currentLiftHeight = Height.MID;
        } else if (gamepad.dpad_up.isInitialPress()) {
            //Sets Lift to High level
            liftTarget = highLiftPosition;
            currentLiftHeight = Height.HIGH;
        }

        robot.lift.setPower(liftPower(liftTarget));

        if(gamepad.y.isInitialPress()){
            kp += .0001;
        } else if(gamepad.a.isInitialPress()){
            kp -= .0001;
        }
        telemetry.addData("P Gain", kp);
        if(gamepad.x.isInitialPress()){
            ki += .0001;
        } else if(gamepad.b.isInitialPress()){
            ki -= .0001;
        }
        telemetry.addData("I Gain", ki);
        if(gamepad.start.isInitialPress()){
            kd += .0001;
        } else if(gamepad.back.isInitialPress()){
            kd -= .0001;
        }
        telemetry.addData("D Gain", kd);
        if(gamepad.right_bumper.isInitialPress()){
            kg += .0000001;
        } else if(gamepad.left_bumper.isInitialPress()){
            kg -= .0000001;
        }
        telemetry.addData("KG", kg);

        //robot.lift.setPower(liftPower());

        telemetry.addData("Right", gamepad.right_stick_y.getValue());
        telemetry.addData("Lrft", gamepad.left_stick_y.getValue());
        telemetry.addData("Claw", robot.claw.getPosition());
        telemetry.addData("Claw State", currentClawPosition);
        telemetry.addData("Lift Height", robot.lift.getCurrentPosition());
        telemetry.addData("Current Target Height", currentLiftHeight);
        telemetry.addData("Intake Position", intakeLiftPosition);
        telemetry.addData("Low Position", lowLiftPosition);
        telemetry.addData("Medium Position", midLiftPosition);
        telemetry.addData("High Position", highLiftPosition);
        telemetry.addData("PID Total", total);
        telemetry.addData("P Arm", pLift);
        telemetry.addData("I Arm", iLift);
        telemetry.addData("D Arm", dLift);
        telemetry.addData("Cos Arm", cosLift);
        telemetry.addData("Proportional Stuff", pLift * kp);
        telemetry.addData("Integral Stuff", iLift * ki);
        telemetry.addData("Derivative Stuff", dLift * kd);
        telemetry.update();

    }

    public double liftPower(int target){
        error = target - robot.lift.getCurrentPosition();
        dLift = (error - pLift) / PIDTimer.seconds();
        iLift = iLift + (error * PIDTimer.seconds());
        pLift = error;
        total = ((pLift * kp) + (iLift * ki) + (dLift * kd))/100;
        PIDTimer.reset();


        if(currentLiftHeight != lastLiftHeight){
            iLift = 0;
        }
        if(iLift > iLiftMax){
            iLift = iLiftMax;
        }else if(iLift < -iLiftMax){
            iLift = -iLiftMax;
        }
        if(total > .5){
            total = .5;
        }

        total = ((pLift * kp) + (iLift * ki) + (dLift * kd))/100 +kg;

        lastLiftHeight = currentLiftHeight;

        return total;
    }

}
