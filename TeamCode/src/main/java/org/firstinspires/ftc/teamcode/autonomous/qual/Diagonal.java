package org.firstinspires.ftc.teamcode.autonomous.qual;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Diagonal", group="11588")
public class Diagonal extends AbstractAutonomous{
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap, telemetry);
        telemetry.addData("angle",robot.getAngle());
        telemetry.update();
        waitForStart();
        // Go diagonal
        moveXY(24, 12);
    }
}
