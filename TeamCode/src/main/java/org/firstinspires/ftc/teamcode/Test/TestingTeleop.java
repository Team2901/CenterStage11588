package org.firstinspires.ftc.teamcode.Test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.hardware.Qual.QualHardware;
import org.firstinspires.ftc.teamcode.hardware.RI3W.RI3WHardware;
import org.firstinspires.ftc.teamcode.hardware.controller.ImprovedGamepad;


@TeleOp(name="Qual Testing Teleop", group="Test")
public class TestingTeleop extends OpMode {
    public QualHardware robot = new QualHardware();
    public ImprovedGamepad gamepad;
    double turningPower = 0;
    public enum ClawPosition{Open, Closed}
    ClawPosition currentClawPosition = ClawPosition.Closed;
    @Override
    public void init() {
        gamepad = new ImprovedGamepad(gamepad1, new ElapsedTime(), "Gamepad");
        robot.init(this.hardwareMap, telemetry);
    }

    @Override
    public void loop() {
        gamepad.update();
        if (gamepad.a.isPressed()) {
            robot.frontRight.setPower(1);
        } else if (gamepad.dpad_down.isPressed()) {
            robot.frontRight.setPower(-1);
        } else {
            robot.frontRight.setPower(0);
        }
        if (gamepad.x.isPressed()) {
            robot.backRight.setPower(1);
        } else if (gamepad.dpad_right.isPressed()) {
            robot.backRight.setPower(-1);
        } else {
            robot.backRight.setPower(0);
        }
        if (gamepad.b.isPressed()) {
            robot.backLeft.setPower(1);
        } else if (gamepad.dpad_left.isPressed()) {
            robot.backLeft.setPower(-1);
        } else {
            robot.backLeft.setPower(0);
        }
        if (gamepad.y.isPressed()) {
            robot.frontLeft.setPower(1);
        } else if (gamepad.dpad_up.isPressed()) {
            robot.frontLeft.setPower(-1);
        } else {
            robot.frontLeft.setPower(0);
        }
        if (gamepad.right_bumper.isPressed()) {
            robot.lift.setPower(0.5);
        } else if (gamepad.left_bumper.isPressed()) {
            robot.lift.setPower(-0.5);
        } else {
            //robot.lift.setPower(0);
        }
//claw related stuff
        switch (currentClawPosition) {
            case Open:
                //robot.claw.setPosition(RI3WHardware.OPENED_POSITION);
                if (gamepad.right_trigger.isInitialPress()) {
                    currentClawPosition = ClawPosition.Closed;
                }
                break;
            case Closed:
                //robot.claw.setPosition(robot.CLOSED_POSITION);
                if (gamepad.left_trigger.isInitialPress()) {
                    currentClawPosition = ClawPosition.Open;
                }
        }
        telemetryStuff();
    }

    public void telemetryStuff() {
        //telemetry.addData("Lift Motor", robot.lift.getCurrentPosition());
        telemetry.addData("Front Right Motor", robot.frontRight.getCurrentPosition());
        telemetry.addData("Back Right Motor", robot.backRight.getCurrentPosition());
        telemetry.addData("Front Left Motor", robot.frontLeft.getCurrentPosition());
        telemetry.addData("Back Left Motor", robot.backLeft.getCurrentPosition());
        //telemetry.addData("Servo", robot.claw.getPosition());
        //telemetry.addData("Servo Direction", robot.claw.getDirection());
        //telemetry.addData("Claw", currentClawPosition);
        telemetry.update();
    }
}
