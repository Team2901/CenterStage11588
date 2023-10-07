package org.firstinspires.ftc.teamcode.autonomous.utility;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.hardware.RI3W.RI3WHardware;
import org.firstinspires.ftc.teamcode.hardware.vision.ComputerVisionProcessor;


@Autonomous(name="Camera Test Auto", group="11588")
public class CameraTestingAutonomous extends LinearOpMode {
    RI3WHardware robot = new RI3WHardware();
    ComputerVisionProcessor.PropPosition propPosition;
    @Override
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap, telemetry, ComputerVisionProcessor.AllianceColor.RED);
        waitForStart();
        while (!isStopRequested()) {
            autoLoop();
        }
    }

    private void autoLoop() {
        robot.pipeline.cameraTelemetry();
    }
}
