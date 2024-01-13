package org.firstinspires.ftc.teamcode.autonomous.qual;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.hardware.vision.ComputerVisionProcessor;

@Autonomous(name="testBackPathBlue", group="Test")
public class testBackPathBlue extends AbstractAutonomous {

    public void runOpMode() throws InterruptedException{
        robot.init(hardwareMap, telemetry, ComputerVisionProcessor.AllianceColor.BLUE);
        waitForStart();
        //robot.visionPortal.stopStreaming();
        robot.speed = 0.5;
        backStagePath();
    }

}
