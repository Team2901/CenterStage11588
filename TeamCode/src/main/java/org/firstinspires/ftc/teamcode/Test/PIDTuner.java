package org.firstinspires.ftc.teamcode.Test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.hardware.controller.ImprovedGamepad;
import org.firstinspires.ftc.teamcode.hardware.qual.QualHardware;


@TeleOp(name="PIDTuner", group="Test")
public class PIDTuner extends OpMode {
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
        robot.PIDLoop();



        if(gamepad.left_bumper.isInitialPress()) {
            robot.goalPosition -= 10;
        } else if(gamepad.right_bumper.isInitialPress()) {
            robot.goalPosition += 10;
        }

        if(gamepad.b.isInitialPress()){
            robot.KP += .001;
        } else if(gamepad.a.isInitialPress()){
            robot.KP -= .001;
        }


        if(gamepad.y.isInitialPress()){
            robot.KG += .01;
        } else if(gamepad.x.isInitialPress()){
            robot.KG -= .01;
        }
        telemetry.addData("KG", robot.KG);
        telemetry.addData("KP", robot.KP);
        telemetry.addData("Lift Height", robot.lift.getCurrentPosition());
        telemetry.addData("Current Target Height", robot.goalPosition);
        telemetry.update();

    }
}
