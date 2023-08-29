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
        } else {
            robot.frontRight.setPower(0);
        }
        if (gamepad.b.isPressed()) {
            robot.backRight.setPower(1);
        } else {
            robot.backRight.setPower(0);
        }
        if (gamepad.x.isPressed()) {
            robot.backLeft.setPower(1);
        } else {
            robot.backLeft.setPower(0);
        }
        if (gamepad.y.isPressed()) {
            robot.frontLeft.setPower(1);
        } else {
            robot.frontLeft.setPower(0);
        }
        telemetry.update();
    }
}
