package org.firstinspires.ftc.teamcode.autonomous.qual.Test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.autonomous.qual.AbstractAutonomous;

@Autonomous(name="Test Turn", group="Test")
public class Turn extends AbstractAutonomous {

    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap,telemetry);
        telemetry.addData("angle",robot.getAngle());
        telemetry.update();
        waitForStart();
        robot.speed = 0.5;
        turnToAngle(90);
    }
}