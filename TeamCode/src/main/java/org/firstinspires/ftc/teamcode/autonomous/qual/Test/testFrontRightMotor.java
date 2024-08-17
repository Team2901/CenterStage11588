package org.firstinspires.ftc.teamcode.autonomous.qual.Test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.autonomous.qual.AbstractAutonomous;

@Autonomous(name="Test frontRight V4", group="Test")
public class testFrontRightMotor extends AbstractAutonomous {

    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap, telemetry);
        waitForStart();
        robot.frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.frontRight.setTargetPosition(1000);
        robot.frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.frontRight.setPower(0.5);
        while (opModeIsActive() && robot.frontRight.isBusy())  {
            ;
        }

    }

}
