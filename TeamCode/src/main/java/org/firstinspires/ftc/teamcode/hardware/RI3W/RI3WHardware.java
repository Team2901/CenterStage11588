package org.firstinspires.ftc.teamcode.hardware.RI3W;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.hardware.vision.ComputerVisionProcessor;
import org.firstinspires.ftc.vision.VisionPortal;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraRotation;

public class RI3WHardware implements OpenCvCamera.AsyncCameraOpenListener {
    //Math for drive motor encoders
    public static final double TICKS_PER_MOTOR_REV = 537.7;
    public static final double DRIVE_GEAR_RATIO = 1.0/1.0;
    public static final double TICKS_PER_DRIVE_REV = TICKS_PER_MOTOR_REV * DRIVE_GEAR_RATIO;
    public static final double WHEEL_CIRCUMFERENCE = Math.PI * 3.78;
    public static final double TICKS_PER_INCH = TICKS_PER_DRIVE_REV / WHEEL_CIRCUMFERENCE;
    public static final double OPENED_POSITION = 0.5;
    public static final double CLOSED_POSITION = 0.15;
    public static final int INTAKE_ENCODER_VALUE = 80;
    public static final int LOW_POLE_ENCODER_VALUE = 1635;
    public static final int MID_POLE_ENCODER_VALUE = 2800;
    public static final int HIGH_POLE_ENCODER_VALUE = 3853;
    public static final int MAX_HEIGHT_ENCODER_VALUE = 4350;
    public static final double KG = 0.046;
    public static final double KP = 0.566;
    public static final double KI = 0.011;
    public static final double KD = 0.008;

    public static double error = 0.0;
    public static double total = 0.0;
    public static double pLift = 0.0;
    public static double iLift = 0.0;
    public static double dLift = 0.0;
    public double iLiftMax = 0.0;
    public DcMotorEx frontLeft;
    public DcMotorEx frontRight;
    public DcMotorEx backLeft;
    public DcMotorEx backRight;
    public DcMotorEx lift;
    public Servo claw;
    public BNO055IMU imu;
    public ComputerVisionProcessor pipeline;

    public OpenCvCamera camera;
    public VisionPortal visionPortal;
    private ComputerVisionProcessor visionProcessor;

    public void init(HardwareMap hardwareMap, Telemetry telemetry) {
        init(hardwareMap, telemetry, ComputerVisionProcessor.AllianceColor.BLUE);
    }
    public void init(HardwareMap hardwareMap, Telemetry telemetry, ComputerVisionProcessor.AllianceColor allianceColor){
        visionProcessor = new ComputerVisionProcessor(telemetry, allianceColor);

         pipeline = new ComputerVisionProcessor(telemetry);

        // Create the vision portal the easy way.
        VisionPortal visionPortal = VisionPortal.easyCreateWithDefaults(
                hardwareMap.get(WebcamName.class, "Webcam 1"), pipeline);


        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json";
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);
    }

    public double getAngle(){
        Orientation orientation = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        return AngleUnit.normalizeDegrees(orientation.firstAngle);

    }

    public void telemetry(Telemetry telemetry) {
        telemetry.addData("PID Total", total);
        telemetry.addData("P Arm", pLift);
        telemetry.addData("I Arm", iLift);
        telemetry.addData("D Arm", dLift);
        telemetry.addData("Proportional Stuff", pLift * KP);
        telemetry.addData("Integral Stuff", iLift * KI);
        telemetry.addData("Derivative Stuff", dLift * KD);
    }

    @Override
    public void onOpened() {
        camera.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
    }

    @Override
    public void onError(int errorCode) {
        throw new RuntimeException("Something with the camera went wrong - Nick");
    }

    public enum Height {
        INTAKE,
        LOW,
        MID,
        HIGH,
        MAX
    }
}