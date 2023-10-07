package org.firstinspires.ftc.teamcode.autonomous.utility;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.hardware.qual.QualHardware;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

import java.util.List;

@Autonomous(name="april tag Test Auto", group="11588")
public class AprilTagTestingAutonomous extends LinearOpMode {
    QualHardware robot = new QualHardware();
    List<AprilTagDetection> currentDetections;
    @Override
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap, telemetry);
        waitForStart();
        autoLoop();
    }

    public void autoLoop() {
        while (opModeIsActive()) {
            currentDetections = robot.aprilTag.getDetections();
            telemetry.addData("April tags detected", currentDetections.size());
            if (currentDetections.get(0) != null) {
                telemetry.addData("Tag", currentDetections.get(0).id);
                telemetry.addData("X comp", currentDetections.get(0).ftcPose.x);
                telemetry.addData("Theta", currentDetections.get(0).ftcPose.pitch);
                telemetry.addData("Distance x", Math.cos(Math.abs(currentDetections.get(0).ftcPose.pitch)) * currentDetections.get(0).ftcPose.x);

            }
            telemetry.update();
        }
    }

}
