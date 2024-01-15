package org.firstinspires.ftc.teamcode.autonomous.qual.Test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.autonomous.qual.AbstractAutonomous;

@Autonomous(name="Test backpath", group="Test")
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
