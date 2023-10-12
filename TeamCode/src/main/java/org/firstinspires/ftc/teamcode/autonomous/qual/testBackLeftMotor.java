package org.firstinspires.ftc.teamcode.autonomous.qual;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name="backLeft", group="11588")
public class testBackLeftMotor extends AbstractAutonomous {

    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap, telemetry);
        robot.backLeft.setTargetPosition(1000);
        robot.backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.backLeft.setPower(0.5);
    }

}
