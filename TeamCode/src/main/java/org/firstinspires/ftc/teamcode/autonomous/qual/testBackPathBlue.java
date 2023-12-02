package org.firstinspires.ftc.teamcode.autonomous.qual;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.hardware.vision.ComputerVisionProcessor;

@Autonomous(name="testBackPathBlue", group="11588")
public class testBackPathBlue extends AbstractAutonomous {

    public void runOpMode() throws InterruptedException{
        robot.init(hardwareMap, telemetry, ComputerVisionProcessor.AllianceColor.BLUE);
        waitForStart();
        robot.speed = 0.5;
        backStagePath();
    }

}
