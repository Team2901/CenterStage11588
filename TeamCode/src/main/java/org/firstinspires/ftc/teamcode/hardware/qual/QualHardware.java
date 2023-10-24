package org.firstinspires.ftc.teamcode.hardware.Qual;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

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
    private ComputerVisionProcessor propDetectionProcessor;
    public AprilTagProcessor aprilTag;
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json";
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

    public static final double FX = 1442.66;
    public static final double FY = 1442.66;
    public static final double CX =  777.52;
    public static final double CY = 162.257;
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);
    }

    public void init(HardwareMap hardwareMap, Telemetry telemetry, ComputerVisionProcessor.AllianceColor allianceColor) {
        aprilTag = new AprilTagProcessor.Builder()
                .setLensIntrinsics(FX, FY, CX, CY)
                .setOutputUnits(DistanceUnit.INCH, AngleUnit.DEGREES)
                .build();
        propDetectionProcessor = new ComputerVisionProcessor(telemetry, allianceColor);
        visionPortal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                .addProcessors(aprilTag, propDetectionProcessor)
                .setCameraResolution(new Size(1280, 720))
                .build();

    public double getAngle(){
        Orientation orientation = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        return AngleUnit.normalizeDegrees(orientation.firstAngle);

    }

    public void telemetry(Telemetry telemetry) {
        //telemetry.addData("PID Total", total);
        //telemetry.addData("P Arm", pLift);
        //telemetry.addData("I Arm", iLift);
        //telemetry.addData("D Arm", dLift);
        //telemetry.addData("Proportional Stuff", pLift * KP);
        //telemetry.addData("Integral Stuff", iLift * KI);
        //telemetry.addData("Derivative Stuff", dLift * KD);
    }

    /*
    @Override
    public void onOpened() {

    }

    @Override
    public void onError(int errorCode) {

    }
}
