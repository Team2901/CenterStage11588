package org.firstinspires.ftc.teamcode.Test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.hardware.qual.QualHardware;
import org.firstinspires.ftc.teamcode.hardware.controller.ImprovedGamepad;


@TeleOp(name="Lift Testing Teleop", group="Test")
public class TestingTeleop2 extends OpMode {
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
            robot.arm.setPower(robot.liftSpeed);
            robot.arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        } else if (gamepad.right_bumper.isPressed()) {
            robot.arm.setPower(-robot.liftSpeed);
            robot.arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        } else {
            int ticks = robot.arm.getCurrentPosition();
            robot.arm.setTargetPosition(ticks);
            robot.arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }

        if (gamepad.y.isInitialPress()) {
            robot.liftSpeed += 0.1;
        } else if (gamepad.a.isInitialPress()) {
            robot.liftSpeed -= 0.1;
        }

//        if (gamepad.x.isPressed()) {
//            robot.armRight.setPosition(+0.2);
//            robot.armLeft.setPosition(-0.2);
//        } else if (gamepad.b.isPressed()) {
//            robot.armRight.setPosition(-0.2);
//            robot.armLeft.setPosition(+0.2);
//        }

        telemetry.addData("Lift Speed", robot.liftSpeed);
        telemetry.addData("Lift Position", robot.arm.getCurrentPosition());
//        telemetry.addData("Right Arm Servo", robot.armRight.getPosition());
//        telemetry.addData("Left Arm Servo", robot.armLeft.getPosition());
        telemetry.update();


    }
}