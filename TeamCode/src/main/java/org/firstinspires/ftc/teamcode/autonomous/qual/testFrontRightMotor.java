package org.firstinspires.ftc.teamcode.autonomous.qual;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name="frontRight V3", group="11588")
public class testFrontRightMotor extends AbstractAutonomous {

    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap, telemetry);
        robot.frontRight.setTargetPosition(10000);
        robot.frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.frontRight.setTargetPosition(10000);
        robot.frontRight.setPower(0.5);
        while (opModeIsActive() && robot.frontRight.isBusy())  {
            ;
        }

    }

}
