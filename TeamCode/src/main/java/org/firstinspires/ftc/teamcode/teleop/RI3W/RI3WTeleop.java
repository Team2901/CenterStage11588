package org.firstinspires.ftc.teamcode.teleop.RI3W;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.hardware.RI3W.RI3WHardware;
import org.firstinspires.ftc.teamcode.hardware.controller.ImprovedGamepad;


@TeleOp(name="RI3W Teleop", group="11588")
public class RI3WTeleop extends OpMode {
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
    double liftSpeed = 0.5;
    int intakeLiftPosition = 80;
    int lowLiftPosition = 1557;
    int midLiftPosition = 2757;
    int highLiftPosition = 3939;
    int maxLiftPosition = 4350;

    double error = 0.0;
    double total = 0.0;
    double kp = 0.2;
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
        if(gamepad.right_trigger.getValue() > 0){
            turningPower = .3 * gamepad.right_trigger.getValue();
        }else if(gamepad.left_trigger.getValue() > 0){
            turningPower = -.3 * gamepad.left_trigger.getValue();
        }else{
            turningPower = .75 * gamepad.right_stick_x.getValue();
        }

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
        } else if (gamepad.b.isInitialPress()) {
            //Sets Lift to Max level
            liftTarget = maxLiftPosition;
            currentLiftHeight = Height.MAX;
        }

        robot.lift.setPower(liftPower(liftTarget));

        switch (currentClawPosition) {
            case Open:
                robot.claw.setPosition(RI3WHardware.OPENED_POSITION);
                if (gamepad.a.isInitialPress()) {
                    currentClawPosition = ClawPosition.Closed;
                }
                break;
            case Closed:
                robot.claw.setPosition(robot.CLOSED_POSITION);
                if (gamepad.y.isInitialPress()) {
                    currentClawPosition = ClawPosition.Open;
                }
        }

        double y = .75 * gamepad.left_stick_y.getValue();
        double x = .75 * gamepad.left_stick_x.getValue();
        double rx = turningPower;

        robot.frontLeft.setPower(y + x + rx);
        robot.frontRight.setPower(y - x - rx);
        robot.backLeft.setPower(y - x + rx);
        robot.backRight.setPower(y + x - rx);
        robot.lift.setPower(liftSpeed);

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

        lastLiftHeight = currentLiftHeight;

        return total;
    }

}
