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


    @Override
    public void init() {
        gamepad = new ImprovedGamepad(gamepad1, new ElapsedTime(), "Gamepad");
        robot.init(this.hardwareMap, telemetry);
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
            liftTarget = RI3WHardware.INTAKE_ENCODER_VALUE;
            currentLiftHeight = Height.INTAKE;
        } else if (gamepad.dpad_down.isInitialPress()) {
            liftTarget = RI3WHardware.LOW_POLE_ENCODER_VALUE;
            currentLiftHeight = Height.LOW;
        } else if (gamepad.dpad_right.isInitialPress()) {
            liftTarget = RI3WHardware.MID_POLE_ENCODER_VALUE;
            currentLiftHeight = Height.MID;
        } else if (gamepad.dpad_up.isInitialPress()) {
            liftTarget = RI3WHardware.HIGH_POLE_ENCODER_VALUE;
            currentLiftHeight = Height.HIGH;
        } else if (gamepad.x.isInitialPress()) {
            liftTarget = RI3WHardware.MAX_HEIGHT_ENCODER_VALUE;
            currentLiftHeight = Height.MAX;
        }

        robot.lift.setPower(liftPower(liftTarget));

        switch (currentClawPosition) {
            case Open:
                robot.claw.setPosition(RI3WHardware.OPENED_POSITION);
                if (gamepad.b.isInitialPress()) {
                    currentClawPosition = ClawPosition.Closed;
                }
                break;
            case Closed:
                robot.claw.setPosition(robot.CLOSED_POSITION);
                if (gamepad.b.isInitialPress()) {
                    currentClawPosition = ClawPosition.Open;
                }
        }

        if(gamepad.y.isInitialPress()) {
            liftTarget += 10;
        } else if(gamepad.a.isInitialPress()) {
            liftTarget -= 10;
        }

        double y = .75 * gamepad.left_stick_y.getValue();
        double x = .75 * gamepad.left_stick_x.getValue();
        double rx = turningPower;

        robot.frontLeft.setPower(y + x + rx);
        robot.frontRight.setPower(y - x - rx);
        robot.backLeft.setPower(y - x + rx);
        robot.backRight.setPower(y + x - rx);
        robot.lift.setPower(liftPower(liftTarget));

        telemetry.addData("Right", gamepad.right_stick_y.getValue());
        telemetry.addData("Lrft", gamepad.left_stick_y.getValue());
        telemetry.addData("Claw", robot.claw.getPosition());
        telemetry.addData("Claw State", currentClawPosition);
        telemetry.addData("Lift Height", robot.lift.getCurrentPosition());
        telemetry.addData("Current Target Height", currentLiftHeight);
        telemetry.addData("Lift Target", liftTarget);
        telemetry.addData("Intake Position", RI3WHardware.INTAKE_ENCODER_VALUE);
        robot.telemetry(telemetry);
        telemetry.update();

    }

    public double liftPower(int target){
        robot.error = target - robot.lift.getCurrentPosition();
        robot.dLift = (robot.error - robot.pLift) / PIDTimer.seconds();
        robot.iLift = robot.iLift + (robot.error * PIDTimer.seconds());
        robot.pLift = robot.error;
        robot.total = ((robot.pLift * RI3WHardware.KP) + (robot.iLift * RI3WHardware.KI) + (robot.dLift * RI3WHardware.KD))/100;
        PIDTimer.reset();


        if(currentLiftHeight != lastLiftHeight){
            robot.iLift = 0;
        }
        if(robot.iLift > robot.iLiftMax){
            robot.iLift = robot.iLiftMax;
        }else if(robot.iLift < -robot.iLiftMax){
            robot.iLift = -robot.iLiftMax;
        }
        if(robot.total > .5){
            robot.total = .5;
        }

        robot.total = ((robot.pLift * RI3WHardware.KP) + (robot.iLift * RI3WHardware.KI) + (robot.dLift * RI3WHardware.KD))/100 +RI3WHardware.KG;

        lastLiftHeight = currentLiftHeight;

        return robot.total;
    }

}
