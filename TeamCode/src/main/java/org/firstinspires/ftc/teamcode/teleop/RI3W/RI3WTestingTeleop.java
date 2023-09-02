package org.firstinspires.ftc.teamcode.teleop.RI3W;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.hardware.RI3W.RI3WHardware;
import org.firstinspires.ftc.teamcode.hardware.controller.ImprovedGamepad;


@TeleOp(name="RI3W Testing Teleop", group="11588")
public class RI3WTestingTeleop extends OpMode {
    public RI3WHardware robot = new RI3WHardware();
    public ImprovedGamepad gamepad;
    double turningPower = 0;
    @Override
    public void init() {
        gamepad = new ImprovedGamepad(gamepad1, new ElapsedTime(), "Gamepad");
        robot.init(this.hardwareMap);
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
        if (gamepad.b.isPressed()) {
            robot.backRight.setPower(1);
        } else if (gamepad.dpad_right.isPressed()) {
            robot.backRight.setPower(-1);
        } else {
            robot.backRight.setPower(0);
        }
        if (gamepad.x.isPressed()) {
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
            robot.lift.setPower(0);
        }
        telemetryStuff();
    }

    public void telemetryStuff() {
        telemetry.addData("Lift Motor", robot.lift.getCurrentPosition());
        telemetry.addData("Front Right Motor", robot.frontRight.getCurrentPosition());
        telemetry.addData("Back Right Motor", robot.backRight.getCurrentPosition());
        telemetry.addData("Front Left Motor", robot.frontLeft.getCurrentPosition());
        telemetry.addData("Back Left Motor", robot.backLeft.getCurrentPosition());
        telemetry.addData("Servo", robot.claw.getPosition());
        telemetry.update();
    }
}
