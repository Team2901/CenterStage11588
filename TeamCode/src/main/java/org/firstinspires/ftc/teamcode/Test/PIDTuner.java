package org.firstinspires.ftc.teamcode.Test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.hardware.RI3W.RI3WHardware;
import org.firstinspires.ftc.teamcode.hardware.controller.ImprovedGamepad;
import org.firstinspires.ftc.teamcode.hardware.qual.QualHardware;
import org.firstinspires.ftc.teamcode.teleop.qual.QualTeleop;


@TeleOp(name="PIDTuner", group="Test")
public class PIDTuner extends OpMode {
    public QualHardware robot = new QualHardware();
    public ImprovedGamepad gamepad;
    double turningPower = 0;
    ElapsedTime PIDTimer = new ElapsedTime();
    public enum Height{INTAKE, MAX}
    Height currentLiftHeight = Height.INTAKE;
    int liftTarget = 80;
    Height lastLiftHeight = currentLiftHeight;
    int intakeLiftPosition = (int) QualHardware.INTAKE_ENCODER_VALUE;
    int maxLiftPosition = (int) QualHardware.MAX_HEIGHT_ENCODER_VALUE;

    double error = 0.0;
    int reference = 0;
    int lastReference = reference;
    double total = 0.0;
    double kg = robot.KG;
    double kp = robot.KP;
    double ki = robot.KI;
    double kd = robot.KD;
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
        robot.init(this.hardwareMap, telemetry);
    }

    @Override
    public void loop() {
        gamepad.update();

        if(gamepad.dpad_down.isInitialPress()) {
            //Sets Lift to intake level
            liftTarget = intakeLiftPosition;
            currentLiftHeight = Height.INTAKE;
        } else if (gamepad.dpad_up.isInitialPress()) {
            liftTarget = maxLiftPosition;
            currentLiftHeight = Height.MAX;
        }

        if (gamepad.dpad_left.isPressed()) {
            robot.lift.setPower(robot.liftSpeed);
        } else if (gamepad.dpad_right.isPressed()) {
            robot.lift.setPower(-robot.liftSpeed);
        } else {
            robot.lift.setPower(0);
        }

        robot.lift.setPower(liftPower(liftTarget));

        if(gamepad.y.isInitialPress()){
            kp += .001;
        } else if(gamepad.a.isInitialPress()){
            kp -= .001;
        }
        telemetry.addData("P Gain", kp);
        if(gamepad.x.isInitialPress()){
            ki += .001;
        } else if(gamepad.b.isInitialPress()){
            ki -= .001;
        }
        telemetry.addData("I Gain", ki);
        if(gamepad.start.isInitialPress()){
            kd += .001;
        } else if(gamepad.back.isInitialPress()){
            kd -= .001;
        }
        telemetry.addData("D Gain", kd);
        if(gamepad.right_bumper.isInitialPress()){
            kg += .001;
        } else if(gamepad.left_bumper.isInitialPress()){
            kg -= .001;
        }
        telemetry.addData("KG", kg);

        robot.lift.setPower(liftPower(liftTarget));

        telemetry.addData("Right", gamepad.right_stick_y.getValue());
        telemetry.addData("Left", gamepad.left_stick_y.getValue());
        telemetry.addData("Lift Height", robot.lift.getCurrentPosition());
        telemetry.addData("Current Target Height", currentLiftHeight);
        telemetry.addData("Intake Position", intakeLiftPosition);
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
