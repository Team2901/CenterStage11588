package org.firstinspires.ftc.teamcode.autonomous.qual;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.hardware.vision.ComputerVisionProcessor;

@Autonomous(name="Emergency Blue Front", group="Emergency")
public class EmergencyBlueFront extends AbstractAutonomous {
    @Override
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap, telemetry, ComputerVisionProcessor.AllianceColor.BLUE);
        waitForStart();
    }
    public void emergencyPark() {
        moveXY(53, 0);
        if(robot.getAlliance() == ComputerVisionProcessor.AllianceColor.RED) {
            turnToAngle(-90);
        }else{
            turnToAngle(90);
        }
        moveXY(83, 0);
    }
}
