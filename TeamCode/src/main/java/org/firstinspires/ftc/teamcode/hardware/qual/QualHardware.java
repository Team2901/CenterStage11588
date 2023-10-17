package org.firstinspires.ftc.teamcode.hardware.Qual;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.hardware.vision.ComputerVisionProcessor;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.openftc.easyopencv.OpenCvCamera;

public class QualHardware implements OpenCvCamera.AsyncCameraOpenListener{

    public OpenCvCamera camera;
    public VisionPortal visionPortal;
    private ComputerVisionProcessor visionProcessor;
    public AprilTagProcessor aprilTag;


    public void init(HardwareMap hardwareMap, Telemetry telemetry, ComputerVisionProcessor.AllianceColor allianceColor) {
        aprilTag = new AprilTagProcessor.Builder()
                .setLensIntrinsics(1430, 1430, 480, 620)
                .setOutputUnits(DistanceUnit.INCH, AngleUnit.DEGREES)
                .build();
        visionProcessor = new ComputerVisionProcessor(telemetry, allianceColor);
        visionPortal = VisionPortal.easyCreateWithDefaults(
                hardwareMap.get(WebcamName.class, "Webcam 1"), aprilTag, visionProcessor);

    }
    @Override
    public void onOpened() {

    }

    @Override
    public void onError(int errorCode) {

    }
}
