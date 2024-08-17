package org.firstinspires.ftc.teamcode.autonomous.qual.Test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.autonomous.qual.AbstractAutonomous;

@Autonomous(name="Test backLeft", group="Test")
public class testBackLeftMotor extends AbstractAutonomous {

    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap, telemetry);
        waitForStart();
        robot.backLeft.setTargetPosition(1000);
        robot.backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.backLeft.setPower(0.5);
        while (opModeIsActive() && robot.backLeft.isBusy())  {
            ;
        }
    }

}
