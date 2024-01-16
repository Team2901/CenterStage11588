package org.firstinspires.ftc.teamcode.autonomous.qual.Emergency;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.autonomous.qual.AbstractAutonomous;
import org.firstinspires.ftc.teamcode.hardware.vision.ComputerVisionProcessor;

@Autonomous(name="Emergency Blue Back", group="Emergency")
public class EmergencyBlueBack extends AbstractAutonomous {
    @Override
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap, telemetry, ComputerVisionProcessor.AllianceColor.BLUE);
        waitForStart();
        emergencyPark();
    }
    public void emergencyPark() {
        robot.speed = 0.5;
        moveXY(0,46);
    }
}
