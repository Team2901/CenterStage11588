package org.firstinspires.ftc.teamcode.autonomous.qual.Emergency;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.autonomous.qual.AbstractAutonomous;
import org.firstinspires.ftc.teamcode.hardware.vision.ComputerVisionProcessor;

@Autonomous(name="Emergency Red Front", group="Emergency")
public class EmergencyRedFront extends AbstractAutonomous {
    @Override
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap, telemetry, ComputerVisionProcessor.AllianceColor.RED);
        waitForStart();
        emergencyPark();
    }
    public void emergencyPark() {
        robot.speed = 0.5;
        moveXY(-53, 0);
        moveXY(0, -93);
    }
}
