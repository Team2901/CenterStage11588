package org.firstinspires.ftc.teamcode.autonomous.RI3W;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.hardware.RI3W.RI3WHardware;
import org.firstinspires.ftc.teamcode.hardware.RI3W.vision.RI3WComputerVisionProcessor;


@Autonomous(name="Camera Test Auto", group="11588")
public class CameraTestingAutonomous extends LinearOpMode {
    RI3WHardware robot = new RI3WHardware();
    @Override
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap, telemetry, RI3WComputerVisionProcessor.AllianceColor.BLUE);
        waitForStart();
        while (!isStopRequested()) {
            autoLoop();
        }
    }

    private void autoLoop() {

    }
}
