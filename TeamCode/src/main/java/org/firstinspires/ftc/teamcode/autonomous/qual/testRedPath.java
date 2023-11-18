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
        purplePixelToWhitePixelPickup();
        robot.speed = 0.1;
        whitePixelsToBackstagePath();
        robot.speed = 0.5;
        BackstageToParkPath();

        while (opModeIsActive() && robot.backLeft.isBusy())  {
            ;
        }
    }

}
