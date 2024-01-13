package org.firstinspires.ftc.teamcode.autonomous.qual;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.hardware.vision.ComputerVisionProcessor;

@Autonomous(name="testRedFrontPath", group="Test")
public class testRedFrontPath extends AbstractAutonomous {

    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap, telemetry, ComputerVisionProcessor.AllianceColor.RED);
        waitForStart();
        robot.speed = 0.5;
        startToDropPurplePixel(PropPosition.MIDDLE);
        purplePixelToWhitePixelPickupFrontStage();
        whitePixelsToBackstagePathFrontStage();
        backstageToParkPathFrontStage();


        while (opModeIsActive() && robot.backLeft.isBusy())  {
            ;
        }
    }

}
