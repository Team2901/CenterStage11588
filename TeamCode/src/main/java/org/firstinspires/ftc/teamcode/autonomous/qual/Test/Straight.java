package org.firstinspires.ftc.teamcode.autonomous.qual.Test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.autonomous.qual.AbstractAutonomous;

@Autonomous(name="Test Straight", group="Test")
public class Straight extends AbstractAutonomous {

    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap,telemetry);
        telemetry.addData("angle",robot.getAngle());
        telemetry.update();
        waitForStart();
        robot.speed = 0.5;
        moveXY(24, 0);
    }
}