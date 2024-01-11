package org.firstinspires.ftc.teamcode.Test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.hardware.qual.QualHardware;
import org.firstinspires.ftc.teamcode.hardware.controller.ImprovedGamepad;


@TeleOp(name="Lift Testing Teleop", group="Test")
public class LiftTest extends OpMode {
    public QualHardware robot = new QualHardware();
    public ImprovedGamepad gamepad;

    @Override
    public void init() {
        gamepad = new ImprovedGamepad(gamepad1, new ElapsedTime(), "Gamepad");
        robot.init(this.hardwareMap, telemetry);
    }


    @Override
    public void loop() {
        gamepad.update();

        if (gamepad.left_bumper.isPressed()) {
            robot.lift.setPower(robot.liftSpeed);
        } else if (gamepad.right_bumper.isPressed()) {
            robot.lift.setPower(-robot.liftSpeed);
        } else {
            robot.lift.setPower(0);
        }

        if (gamepad.y.isInitialPress()) {
            robot.liftSpeed += 0.1;
        } else if (gamepad.a.isInitialPress()) {
            robot.liftSpeed -= 0.1;
        }

        if (gamepad.x.isPressed()) {
            robot.armRight.setPosition(+0.2);
            robot.armLeft.setPosition(+0.2);
        } else if (gamepad.b.isPressed()) {
            robot.armRight.setPosition(-0.2);
            robot.armLeft.setPosition(-0.2);
        }

        telemetry.addData("Lift Speed", robot.liftSpeed);
        telemetry.addData("Lift Position", robot.lift.getCurrentPosition());
        telemetry.addData("Right Arm Servo", robot.armRight.getPosition());
        telemetry.addData("Left Arm Servo", robot.armLeft.getPosition());
        telemetry.update();


    }
}