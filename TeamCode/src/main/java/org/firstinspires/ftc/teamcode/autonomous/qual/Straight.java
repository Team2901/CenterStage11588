package org.firstinspires.ftc.teamcode.autonomous.qual;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Straight", group="11588")
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