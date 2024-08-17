package org.firstinspires.ftc.teamcode.autonomous.qual.Test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.autonomous.qual.AbstractAutonomous;

@Autonomous(name="Test Diagonal", group="Test")
public class Diagonal extends AbstractAutonomous {
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap, telemetry);
        telemetry.addData("angle",robot.getAngle());
        telemetry.update();
        waitForStart();
        // Go diagonal
        moveDiagonal(24, 45);
        while (true) {

        }
    }
}
