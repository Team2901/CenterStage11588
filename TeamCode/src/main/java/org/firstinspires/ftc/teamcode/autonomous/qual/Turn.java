package org.firstinspires.ftc.teamcode.autonomous.qual;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Turn", group="11588")
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