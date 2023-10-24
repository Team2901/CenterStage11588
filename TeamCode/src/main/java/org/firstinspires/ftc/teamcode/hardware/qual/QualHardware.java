package org.firstinspires.ftc.teamcode.hardware.qual;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.ImuOrientationOnRobot;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
//import org.firstinspires.ftc.teamcode.hardware.RI3W.vision.RI3WComputerVisionProcessor;
//import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.vision.VisionProcessor;
//import org.openftc.easyopencv.OpenCvCamera;
//import org.openftc.easyopencv.OpenCvCameraRotation;

public class QualHardware {
    //Math for drive motor encoders
    public static final double TICKS_PER_MOTOR_REV = 537.7;
    public static final double DRIVE_GEAR_RATIO = 1.0;
    public static final double TICKS_PER_DRIVE_REV = TICKS_PER_MOTOR_REV * DRIVE_GEAR_RATIO;
    public static final double WHEEL_CIRCUMFERENCE = Math.PI * 3.78;
    public static final double TICKS_PER_INCH = TICKS_PER_DRIVE_REV / WHEEL_CIRCUMFERENCE;
    /*public static final double OPENED_POSITION = 0.5;
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
     */
    public DcMotorEx frontLeft;
    public DcMotorEx frontRight;
    public DcMotorEx backLeft;
    public DcMotorEx backRight;
    public DcMotorEx lift;
    //public Servo claw;
    public double speed = .15;

    // public BNO055IMU imu;
    public IMU imu;             // Use new generic IMU class added to support both
                                // BHI260AP and BNO055 chip

    //public RI3WComputerVisionProcessor pipeline;

    //public OpenCvCamera camera;
    //public VisionPortal visionPortal;
    //private RI3WComputerVisionProcessor visionProcessor;

    /*public void init(HardwareMap hardwareMap, Telemetry telemetry) {
        init(hardwareMap, telemetry, RI3WComputerVisionProcessor.AllianceColor.BLUE);
    }
     */
    public void init(HardwareMap hardwareMap, Telemetry telemetry){
        frontLeft = hardwareMap.get(DcMotorEx.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotorEx.class, "frontRight");
        backLeft = hardwareMap.get(DcMotorEx.class, "backLeft");
        backRight = hardwareMap.get(DcMotorEx.class, "backRight");
        //lift = hardwareMap.get(DcMotorEx.class, "lift");
        //claw = hardwareMap.get(Servo.class, "claw");
//        visionProcessor = new RI3WComputerVisionProcessor(allianceColor, telemetry);
//
//
        //pipeline = new RI3WComputerVisionProcessor(telemetry);

        // Create the vision portal the easy way.
        /*VisionPortal visionPortal = VisionPortal.easyCreateWithDefaults(
                hardwareMap.get(WebcamName.class, "Webcam 1"), pipeline);
         */

        //Resetting encoders so they start at 0
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        //Running without encoders because it makes pid work better
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //lift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        //Making the drive motors break at 0 so they stop better
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // coachbot
        // Robot configuration
        //    0-frontRight  (GoBILDA 5202/3/4 series)  (reverse)
        //    1-backRight   (GoBILDA 5202/3/4 series)  (reverse)
        //    2-frontLeft   (GoBILDA 5202/3/4 series)
        //    3-backLeft    (GoBILDA 5202/3/4 series)

        // TODO: uncomment if you want to use coachbot
        // frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        //backRight.setDirection(DcMotorSimple.Direction.REVERSE);

        // teambot
        //Reversing the left motors so the robot goes straight
        //frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        //backLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        //lift.setDirection(DcMotorSimple.Direction.REVERSE);

        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);
        //claw.setPosition(CLOSED_POSITION);

        // Our Control Hub has the new IMU chip (BHI260AP). Use the new generic IMU class when
        // requesting a refernce to the IMU hardware. What chip you have can be determined by
        // using "program and manage" tab on driver station, then "manage" on the hamburger menu.
        imu = hardwareMap.get(IMU.class, "imu");

        // Use the new RevHubOrientationOnRobot classes to describe how the control hub is mounted on the robot.
        // For the coach bot its mounted Backward / usb cable on the right (as seen from back of robot)
        // Doc: https://github.com/FIRST-Tech-Challenge/FtcRobotController/wiki/Universal-IMU-Interface
        RevHubOrientationOnRobot.LogoFacingDirection logoDirection = RevHubOrientationOnRobot.LogoFacingDirection.BACKWARD;
        RevHubOrientationOnRobot.UsbFacingDirection  usbDirection  = RevHubOrientationOnRobot.UsbFacingDirection.RIGHT;
        RevHubOrientationOnRobot orientationOnRobot = new RevHubOrientationOnRobot(logoDirection, usbDirection);
        IMU.Parameters parameters = new IMU.Parameters(orientationOnRobot);

        imu.initialize(parameters);
    }

    public double getAngle(){
        YawPitchRollAngles angles = imu.getRobotYawPitchRollAngles();
        return AngleUnit.normalizeDegrees(angles.getYaw(AngleUnit.DEGREES));

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
        camera.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
    }
     */

    /*
    @Override
    public void onError(int errorCode) {
        throw new RuntimeException("Something with the camera went wrong - Nick");
    }
     */

    /*public enum Height {
        INTAKE,
        LOW,
        MID,
        HIGH,
        MAX
    }
     */
}
