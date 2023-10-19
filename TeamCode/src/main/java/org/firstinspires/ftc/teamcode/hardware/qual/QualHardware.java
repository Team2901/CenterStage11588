package org.firstinspires.ftc.teamcode.hardware.qual;

import android.util.Size;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.hardware.vision.ComputerVisionProcessor;
import org.firstinspires.ftc.teamcode.hardware.vision.LensIntrinsics;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.openftc.easyopencv.OpenCvCamera;

public class QualHardware implements OpenCvCamera.AsyncCameraOpenListener{

    public OpenCvCamera camera;
    public VisionPortal visionPortal;
    private ComputerVisionProcessor propDetectionProcessor;
    public AprilTagProcessor aprilTag;


    public void init(HardwareMap hardwareMap, Telemetry telemetry, ComputerVisionProcessor.AllianceColor allianceColor) {
        aprilTag = new AprilTagProcessor.Builder()
                .setLensIntrinsics(LensIntrinsics.FX, LensIntrinsics.FY, LensIntrinsics.CX, LensIntrinsics.CY)
                .setOutputUnits(DistanceUnit.INCH, AngleUnit.DEGREES)
                .build();
        propDetectionProcessor = new ComputerVisionProcessor(telemetry, allianceColor);
        visionPortal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                .addProcessors(aprilTag, propDetectionProcessor)
                .setCameraResolution(new Size(1280, 720))
                .build();


    }
    @Override
    public void onOpened() {

    }

    @Override
    public void onError(int errorCode) {

    }
}
