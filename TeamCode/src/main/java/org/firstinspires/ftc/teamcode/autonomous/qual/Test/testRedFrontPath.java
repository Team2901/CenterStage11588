package org.firstinspires.ftc.teamcode.autonomous.qual.Test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.autonomous.qual.AbstractAutonomous;
import org.firstinspires.ftc.teamcode.hardware.vision.ComputerVisionProcessor;

@Autonomous(name="testRedFrontPath", group="Test")
public class testRedFrontPath extends AbstractAutonomous {

    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap, telemetry, ComputerVisionProcessor.AllianceColor.RED);
        waitForStart();
        robot.speed = 0.5;
        startToDropPurplePixel(PropPosition.RIGHT);
        purplePixelToWhitePixelPickupFrontStage();
        whitePixelsToBackstagePathFrontStage();
        backstageToParkPathFrontStage();


        while (opModeIsActive() && robot.backLeft.isBusy())  {
            ;
        }
    }

}
