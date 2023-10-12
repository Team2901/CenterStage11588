package org.firstinspires.ftc.teamcode.autonomous.qual;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name="frontLeft", group="11588")
public class testFrontRightMotor extends AbstractAutonomous {

    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap, telemetry);

        robot.frontRight.setTargetPosition(1000);
        robot.frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.frontRight.setPower(0.5);
    }

}
