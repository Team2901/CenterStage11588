package org.firstinspires.ftc.teamcode.autonomous.qual;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="testRedPath V3", group="11588")
public class testRedPath extends AbstractAutonomous {

    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap, telemetry);
        waitForStart();
        robot.speed = 0.1;
        startToDropPurplePixel(PropPosition.MIDDLE);
        robot.speed = 0.5;
        purplePixelToWhitePixelPickupFrontStage();
        robot.speed = 0.1;
        whitePixelsToBackstagePathFrontStage();
        robot.speed = 0.5;
        backstageToParkPathFrontStage();

        while (opModeIsActive() && robot.backLeft.isBusy())  {
            ;
        }
    }

}
