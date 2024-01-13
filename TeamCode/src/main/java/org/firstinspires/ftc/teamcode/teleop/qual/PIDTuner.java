package org.firstinspires.ftc.teamcode.teleop.qual;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.hardware.controller.ImprovedGamepad;
import org.firstinspires.ftc.teamcode.hardware.qual.QualHardware;

public class PIDTuner extends OpMode {
    ImprovedGamepad gamepad;
    QualHardware robot = new QualHardware();
    @Override
    public void init() {
        gamepad = new ImprovedGamepad(gamepad1, new ElapsedTime(), "Gamepad");
        robot.init(this.hardwareMap, telemetry);
    }

    @Override
    public void loop() {
        if (gamepad.a.isPressed()) {
            robot.KD += 0.05;
        }

        if (gamepad.b.isPressed()) {
            robot.KD -= 0.05;
        }

        robot.PIDLoop();
    }
}
