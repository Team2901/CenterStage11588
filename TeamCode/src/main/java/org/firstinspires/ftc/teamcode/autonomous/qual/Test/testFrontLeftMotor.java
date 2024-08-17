package org.firstinspires.ftc.teamcode.autonomous.qual.Test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.autonomous.qual.AbstractAutonomous;

@Autonomous(name="Test frontLeft", group="Test")
public class testFrontLeftMotor extends AbstractAutonomous {


    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap, telemetry);
        waitForStart();
        robot.frontLeft.setTargetPosition(1000);
        robot.frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.frontLeft.setPower(0.5);
        while (opModeIsActive() && robot.frontLeft.isBusy())  {
            ;
        }
    }

}
