//package org.firstinspires.ftc.teamcode.autonomous.utility;
//
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//
//import org.firstinspires.ftc.teamcode.hardware.Qual.QualHardware;
//import org.firstinspires.ftc.teamcode.hardware.vision.ComputerVisionProcessor;
//import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
//
//import java.util.List;
//
//@Autonomous(name="april tag Test Auto", group="11588")
//public class AprilTagTestingAutonomous extends LinearOpMode {
//    QualHardware robot = new QualHardware();
//    List<AprilTagDetection> currentDetections;
//    @Override
//    public void runOpMode() throws InterruptedException {
//        robot.init(hardwareMap, telemetry, ComputerVisionProcessor.AllianceColor.BLUE);
//        waitForStart();
//        autoLoop();
//    }
//
//    public void autoLoop() {
//        while (opModeIsActive()) {
//            currentDetections = robot.aprilTag.getDetections();
//            telemetry.addData("April tags detected", currentDetections.size());
//            if (currentDetections.get(0) != null) {
//                telemetry.addData("Tag", currentDetections.get(0).id);
//                telemetry.addData("Distance from tag", currentDetections.get(0).ftcPose.range);
//                telemetry.addData("X Distance", currentDetections.get(0).ftcPose.x);
//                telemetry.addData("Y Distance", currentDetections.get(0).ftcPose.y);
//            }
//            telemetry.update();
//        }
//    }
//
//}
