package org.firstinspires.ftc.teamcode.autonomous.qual;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name="backRight", group="11588")
public class testBackRightMotor extends AbstractAutonomous {

    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap, telemetry);
        robot.backRight.setTargetPosition(1000);
        robot.backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.backRight.setPower(0.5);
    }

}
