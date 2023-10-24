package org.firstinspires.ftc.teamcode.Test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.hardware.qual.QualHardware;
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

    public void help() {
        telemetry.addLine("Test Individual Motors");
        telemetry.addLine("Press A to turn FR");
        telemetry.addLine("Press Y to turn FL");
        telemetry.addLine("Press X to turn BR");
        telemetry.addLine("Press B to turn BL");

    }
    @Override
    public void loop() {
        gamepad.update();
        if (gamepad.a.isPressed()) {
            robot.frontRight.setPower(robot.speed);
        } else if (gamepad.dpad_down.isPressed()) {
            robot.frontRight.setPower(-robot.speed);
        } else {
            robot.frontRight.setPower(0);
        }
        if (gamepad.x.isPressed()) {
            robot.backRight.setPower(robot.speed);
        } else if (gamepad.dpad_right.isPressed()) {
            robot.backRight.setPower(-robot.speed);
        } else {
            robot.backRight.setPower(0);
        }
        if (gamepad.b.isPressed()) {
            robot.backLeft.setPower(robot.speed);
        } else if (gamepad.dpad_left.isPressed()) {
            robot.backLeft.setPower(-robot.speed);
        } else {
            robot.backLeft.setPower(0);
        }
        if (gamepad.y.isPressed()) {
            robot.frontLeft.setPower(robot.speed);
        } else if (gamepad.dpad_up.isPressed()) {
            robot.frontLeft.setPower(-robot.speed);
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
        help();
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
