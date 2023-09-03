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

    public DcMotorEx lift;

    private ElapsedTime PIDTimer = new ElapsedTime();
    public enum Height{INTAKE, LOW, MID, HIGH, MAX}
    RI3WTeleop.Height currentLiftHeight = RI3WTeleop.Height.INTAKE;
    int liftTarget = 80;
    RI3WTeleop.Height lastLiftHeight = currentLiftHeight;
    double liftSpeed = -0.2;
    int intakeLiftPosition = (int) RI3WHardware.INTAKE_ENCODER_VALUE;
    int lowLiftPosition = (int) RI3WHardware.LOW_POLE_ENCODER_VALUE;
    int midLiftPosition = (int) RI3WHardware.MID_POLE_ENCODER_VALUE;
    int highLiftPosition = (int) RI3WHardware.HIGH_POLE_ENCODER_VALUE;
    int maxLiftPosition = (int) RI3WHardware.MAX_HEIGHT_ENCODER_VALUE;
    int target;

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

        lift = hardwareMap.get(DcMotorEx.class, "lift");
        lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        //tuneMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        lift.setPower(0);
    }
    @Override
    public void loop() {
        previousGamepad1.copy(currentGamepad1);
        previousGamepad2.copy(currentGamepad2);

        currentGamepad1.copy(gamepad1);
        currentGamepad2.copy(gamepad2);

        if(gamepad1.dpad_left) {
            //Sets Lift to intake level
            liftTarget = intakeLiftPosition;
            currentLiftHeight = RI3WTeleop.Height.INTAKE;
        } else if (gamepad1.dpad_down) {
            //Sets Lift to Low level
            liftTarget = lowLiftPosition;
            currentLiftHeight = RI3WTeleop.Height.LOW;
        } else if (gamepad1.dpad_right) {
            //Sets Lift to Mid level
            liftTarget = midLiftPosition;
            currentLiftHeight = RI3WTeleop.Height.MID;
        } else if (gamepad1.dpad_up) {
            //Sets Lift to High level
            liftTarget = highLiftPosition;
            currentLiftHeight = RI3WTeleop.Height.HIGH;
        }

        liftTarget += gamepad1.right_stick_y*10;


        if(gamepad1.right_trigger > .5){
            robot.lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }

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
        telemetry.addData("Total", liftPower());
        telemetry.addData("pArm", pLift);
        telemetry.addData("P Value", pLift * kp);
        telemetry.addData("iArm", iLift);
        telemetry.addData("I Value", iLift * ki);
        telemetry.addData("dArm", dLift);
        telemetry.addData("D Value", dLift * kd);
        telemetry.addData("cosArm", cosLift);
        telemetry.addData("Cos Value", cosLift * kCos);
    }
    public double liftPower(){
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
