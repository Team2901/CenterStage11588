package org.firstinspires.ftc.teamcode.autonomous.qual;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name="frontLeft", group="11588")
public class testFrontRightMotor extends AbstractAutonomous {

    public void runOpMode() throws InterruptedException {
        robot.frontLeft.setTargetPosition(1000);
        robot.frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.frontLeft.setPower(0.5);
    }

}
