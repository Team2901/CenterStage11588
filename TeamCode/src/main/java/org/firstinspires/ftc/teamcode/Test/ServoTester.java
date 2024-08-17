package org.firstinspires.ftc.teamcode.Test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.hardware.controller.ImprovedGamepad;
import org.firstinspires.ftc.teamcode.hardware.qual.QualHardware;

@TeleOp(name="Servo Testing Teleop", group="Test")
public class ServoTester extends OpMode {

    public ImprovedGamepad gamepad;
    QualHardware robot = new QualHardware();
    @Override
    public void init() {
        gamepad = new ImprovedGamepad(gamepad1, new ElapsedTime(), "Gamepad");
        robot.init(this.hardwareMap, telemetry);
    }

    @Override
    public void loop() {
        gamepad.update();
        robot.PIDLoop();
        if(gamepad.left_bumper.isInitialPress()) {
            robot.goalPosition -= 10;
        } else if(gamepad.right_bumper.isInitialPress()) {
            robot.goalPosition += 10;
        }

        if(gamepad.a.isInitialPress()) {
            robot.clawLeft.setPosition(robot.clawLeft.getPosition() + 0.1);
        } else if (gamepad.b.isInitialPress()) {
            robot.clawLeft.setPosition(robot.clawLeft.getPosition() - 0.1);
        }

        if(gamepad.x.isInitialPress()) {
            robot.clawRight.setPosition(robot.clawRight.getPosition() + 0.1);
        } else if (gamepad.y.isInitialPress()) {
            robot.clawRight.setPosition(robot.clawRight.getPosition() - 0.1);
        }


        telemetry.addData("Right", robot.clawRight.getPosition());
        telemetry.addData("Left", robot.clawLeft.getPosition());
        telemetry.update();

    }
}
