package org.firstinspires.ftc.teamcode.Test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.hardware.RI3W.RI3WHardware;
import org.firstinspires.ftc.teamcode.hardware.controller.ImprovedGamepad;
import org.firstinspires.ftc.teamcode.teleop.RI3W.RI3WTeleop;

@TeleOp(name ="PID Tuner", group ="Test")
public class PIDTuner extends OpMode {
    public RI3WHardware robot = new RI3WHardware();
    public ImprovedGamepad gamepad;
    private ElapsedTime PIDTimer = new ElapsedTime();
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
    double total = 0.0;
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
    Gamepad currentGamepad1 = new Gamepad();
    Gamepad currentGamepad2 = new Gamepad();

    Gamepad previousGamepad1 = new Gamepad();
    Gamepad previousGamepad2 = new Gamepad();

    //Rename the motor u want to tune
    @Override
    public void init() {
        gamepad = new ImprovedGamepad(gamepad1, new ElapsedTime(), "Gamepad");
        robot.init(this.hardwareMap);
    }
    @Override
    public void loop() {
        previousGamepad1.copy(currentGamepad1);
        previousGamepad2.copy(currentGamepad2);

        currentGamepad1.copy(gamepad1);
        currentGamepad2.copy(gamepad2);

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

        liftTarget += gamepad1.right_stick_y*10;


        robot.lift.setPower(liftSpeed);

        if(gamepad1.y && !previousGamepad1.y){
            kp += .01;
        } else if(gamepad1.a && !previousGamepad1.a){
            kp -= .01;
        }
        telemetry.addData("P Gain", kp);
        if(gamepad1.x && !previousGamepad1.x){
            ki += .01;
        } else if(gamepad1.b && !previousGamepad1.b){
            ki -= .01;
        }
        telemetry.addData("I Gain", ki);
        if(gamepad1.start && !previousGamepad1.start){
            kd += .01;
        } else if(gamepad1.back && !previousGamepad1.back){
            kd -= .01;
        }
        telemetry.addData("D Gain", kd);
        telemetry.addData("Cos Gain", kCos);
        telemetry.addData("Current Position", robot.lift.getCurrentPosition());
        telemetry.addData("Target Position", liftTarget);
        telemetry.addData("Motor Power", robot.lift.getPower());
        telemetry.addData("Total", total);
        telemetry.addData("pArm", pLift);
        telemetry.addData("P Value", pLift * kp);
        telemetry.addData("iArm", iLift);
        telemetry.addData("I Value", iLift * ki);
        telemetry.addData("dArm", dLift);
        telemetry.addData("D Value", dLift * kd);
        telemetry.addData("cosArm", cosLift);
        telemetry.addData("Cos Value", cosLift * kCos);
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

        lastLiftHeight = currentLiftHeight;

        return total;
    }
}
