package org.firstinspires.ftc.teamcode.autonomous.qual;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="backpath test", group="Test")
public class TestBackPath extends AbstractAutonomous {
    @Override
    public void runOpMode() throws InterruptedException {
        navigateToBackdropBackStage();
        robot.speed = 0.5;
        navigateToFrontStageBackStage();
        robot.speed = 0.5;
        navigateToBackStageBackStage();
        robot.speed = 0.5;
        parkBackStage();
        robot.speed = 0.5;

        while (opModeIsActive() && robot.backLeft.isBusy())  {
            ;
        }
    }
}
