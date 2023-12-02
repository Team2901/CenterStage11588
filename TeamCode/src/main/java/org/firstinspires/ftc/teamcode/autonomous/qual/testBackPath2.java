package org.firstinspires.ftc.teamcode.autonomous.qual;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="testBackPath", group="11588")
public class testBackPath2 extends AbstractAutonomous {

    public void runOpMode() throws InterruptedException{
        robot.init(hardwareMap, telemetry);
        waitForStart();
        backStagePath();
    }

}
