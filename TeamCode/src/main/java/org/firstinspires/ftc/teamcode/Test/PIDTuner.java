package org.firstinspires.ftc.teamcode.Test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.hardware.RI3W.RI3WHardware;
import org.firstinspires.ftc.teamcode.hardware.controller.ImprovedGamepad;
import org.firstinspires.ftc.teamcode.hardware.qual.QualHardware;
import org.firstinspires.ftc.teamcode.teleop.qual.QualTeleop;


@TeleOp(name="PIDTuner", group="Test")
public class PIDTuner extends OpMode {
    public QualHardware robot = new QualHardware();
    public ImprovedGamepad gamepad;
    int liftTarget = 80;
    int intakeLiftPosition = (int) QualHardware.INTAKE_ENCODER_VALUE;
    int maxLiftPosition = (int) QualHardware.MAX_HEIGHT_ENCODER_VALUE;



    @Override
    public void init() {
        gamepad = new ImprovedGamepad(gamepad1, new ElapsedTime(), "Gamepad");
        robot.init(this.hardwareMap, telemetry);
    }

    @Override
    public void loop() {
        gamepad.update();
        robot.PIDLoop();



        if (gamepad.dpad_down.isPressed()) {
            robot.goalPosition = robot.GROUND_POSITION;
        } else if (gamepad.dpad_left.isPressed()) {
            robot.goalPosition = robot.FIRST_POSITION;
        } else if (gamepad.dpad_right.isPressed()) {
            robot.goalPosition = robot.SECOND_POSITION;
        } else if (gamepad.dpad_up.isPressed()) {
            robot.goalPosition = robot.THIRD_POSITION;
        }

        if (gamepad.dpad_left.isPressed()) {
            robot.lift.setPower(robot.liftSpeed);
        } else if (gamepad.dpad_right.isPressed()) {
            robot.lift.setPower(-robot.liftSpeed);
        } else {
            robot.lift.setPower(0);
        }


        if(gamepad.b.isInitialPress()){
            robot.KP += .001;
        } else if(gamepad.a.isInitialPress()){
            robot.KP -= .001;
        }


        if(gamepad.y.isInitialPress()){
            robot.KG += .001;
        } else if(gamepad.x.isInitialPress()){
            robot.KG -= .001;
        }
        telemetry.addData("KG", robot.KG);
        telemetry.addData("KP", robot.KP);
        telemetry.addData("Lift Height", robot.lift.getCurrentPosition());
        telemetry.addData("Current Target Height", robot.goalPosition);
        telemetry.update();

    }
}
