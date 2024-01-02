package org.firstinspires.ftc.teamcode.teleop.qual;



import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.hardware.qual.QualHardware;
import org.firstinspires.ftc.teamcode.hardware.controller.ImprovedGamepad;


@TeleOp(name="Qual Teleop", group="11588 Quals")
public class QualTeleop extends OpMode {
    public QualHardware robot = new QualHardware();
    public ImprovedGamepad gamepad;
    double turningPower = 0;



    @Override
    public void init() {
        gamepad = new ImprovedGamepad(gamepad1, new ElapsedTime(), "Gamepad");
        robot.init(this.hardwareMap, telemetry);
    }

    @Override
    public void loop() {
        gamepad.update();
        if (gamepad.right_trigger.getValue() > 0) {
            turningPower = .3 * gamepad.right_trigger.getValue();
        } else if (gamepad.left_trigger.getValue() > 0) {
            turningPower = -.3 * gamepad.left_trigger.getValue();
        } else {
            turningPower = .75 * gamepad.right_stick_x.getValue();
        }

        if(gamepad.left_bumper.isInitialPress()) {
            if (robot.draggerPosition == QualHardware.DraggerPosition.UP) {
                robot.draggerPosition = QualHardware.DraggerPosition.DOWN;
                robot.dragger.setPosition(QualHardware.DRAGGER_DOWN_POSITION);
            } else if (robot.draggerPosition == QualHardware.DraggerPosition.DOWN) {
                robot.draggerPosition = QualHardware.DraggerPosition.UP;

                robot.dragger.setPosition(QualHardware.DRAGGER_UP_POSITION);
            }
        }


        if (gamepad.left_bumper.isPressed()) {
            robot.lift.setPower(robot.speed);
        } else if (gamepad.right_bumper.isPressed()) {
            robot.lift.setPower(-robot.speed);
        } else {
            robot.lift.setPower(0);
        }

        double y = 1 * gamepad.left_stick_y.getValue();
        double x = 1 * gamepad.left_stick_x.getValue();
        double rx = turningPower;

        robot.frontLeft.setPower(y + x + rx);
        robot.frontRight.setPower(y - x - rx);
        robot.backLeft.setPower(y - x + rx);
        robot.backRight.setPower(y + x - rx);


        telemetry.addData("Right", gamepad.right_stick_y.getValue());
        telemetry.addData("Left", gamepad.left_stick_y.getValue());
        telemetry.update();

    }

}