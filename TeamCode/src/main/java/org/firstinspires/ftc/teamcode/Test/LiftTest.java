package org.firstinspires.ftc.teamcode.Test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.hardware.qual.QualHardware;
import org.firstinspires.ftc.teamcode.hardware.controller.ImprovedGamepad;


@TeleOp(name="Lift Testing Teleop", group="Test")
public class TestingTeleop extends OpMode {
    public QualHardware robot = new QualHardware();
    public ImprovedGamepad gamepad;
    double LSpeed = robot.speed;

    @Override
    public void init() {
        gamepad = new ImprovedGamepad(gamepad1, new ElapsedTime(), "Gamepad");
        robot.init(this.hardwareMap, telemetry);
    }


    @Override
    public void loop() {
        gamepad.update();

        if (gamepad.left_bumper.isPressed()) {
            robot.lift.setPower(LSpeed);
        } else if (gamepad.right_bumper.isPressed()) {
            robot.lift.setPower(-LSpeed);
        } else {
            robot.lift.setPower(0);
        }

        if (gamepad.y.isInitialPress()) {
            LSpeed += 0.1;
        } else if (gamepad.a.isInitialPress()) {
            LSpeed -= 0.1;
        }
        telemetry.addData("Lift Speed", LSpeed);


    }
}