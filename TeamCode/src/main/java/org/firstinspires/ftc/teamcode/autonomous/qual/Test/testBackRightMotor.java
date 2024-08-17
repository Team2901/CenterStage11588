package org.firstinspires.ftc.teamcode.autonomous.qual.Test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.autonomous.qual.AbstractAutonomous;

@Autonomous(name="Test backRight", group="Test")
public class testBackRightMotor extends AbstractAutonomous {

    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap, telemetry);
        waitForStart();
        robot.backRight.setTargetPosition(1000);
        robot.backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.backRight.setPower(0.5);
        while (opModeIsActive() && robot.backRight.isBusy())  {
            ;
        }
    }

}
