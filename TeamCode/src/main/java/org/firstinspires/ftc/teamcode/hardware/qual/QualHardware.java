package org.firstinspires.ftc.teamcode.hardware.qual;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import android.util.Size;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.teamcode.Utilities.ConfigUtilities;
import org.firstinspires.ftc.teamcode.hardware.vision.ComputerVisionProcessor;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraRotation;
//import org.openftc.easyopencv.OpenCvCamera;
//import org.openftc.easyopencv.OpenCvCameraRotation;

public class QualHardware implements OpenCvCamera.AsyncCameraOpenListener {
    //Math for drive motor encoders
    public static final double TICKS_PER_MOTOR_REV = 537.7;
    public static final double DRIVE_GEAR_RATIO = 1.0;
    public static final double TICKS_PER_DRIVE_REV = TICKS_PER_MOTOR_REV * DRIVE_GEAR_RATIO;
    public static final double WHEEL_CIRCUMFERENCE = Math.PI * 3.78;
    public static final double TICKS_PER_INCH = TICKS_PER_DRIVE_REV / WHEEL_CIRCUMFERENCE;
    public static final double PURPLE_PIXEL_DROPPER_START_POSITION = 0.25;
    public OpenCvCamera camera;
    public VisionPortal visionPortal;
    public ComputerVisionProcessor propDetectionProcessor;
    public AprilTagProcessor aprilTag;
    public final int MAX_LIFT_HEIGHT = 0;
    public static double KG = 0.12;
    public static double KP = 0.041;

    //Leave KI and KD as be, we are sticking with a proportinal controller
    public static double KI = 0.0;
    public static double KD = 0.0;
    public static double error = 0.0;
    public static double total = 0.0;
    public DcMotorEx frontLeft;
    public DcMotorEx frontRight;
    public DcMotorEx backLeft;
    public DcMotorEx backRight;
    public DcMotorEx arm;
    public Servo clawRight;
    public Servo clawLeft;
    public Servo purplePixelDropper;
    public double speed = .15;
    public double liftSpeed = .35;
    public double turnTolerance = 0.5;
    int lowArmPosition = 0;
    int zeroAngleTicks = lowArmPosition;

    public int goalPosition = 0;
    ElapsedTime PIDTimer = new ElapsedTime();

    public double bestSpeed = 0.5;


    // public BNO055IMU imu;

    RevHubOrientationOnRobot.UsbFacingDirection usbFacingDirection;
    RevHubOrientationOnRobot.LogoFacingDirection logoDirection;
    public IMU imu;

    double kCos = 0.3;
    double pArm = 0.0;
    double iArm = 0.0;
    double dArm = 0.0;
    double cosArm = 0.0;
    double iArmMax = .25;
    double armAngle = 0;
    Height currentArmHeight = Height.RETRACTED;
    Height lastArmHeight = currentArmHeight;
    public static final double OPEN_CLAW_POSITION = 0;
    public static final double CLOSED_CLAW_POSITION = 0;
    public ClawPosition leftClawPositon = ClawPosition.CLOSED;
    public ClawPosition rightClawPositon = ClawPosition.CLOSED;

    public enum ClawPosition {
        OPEN,
        CLOSED
    }

    enum Height {
        EXTENDED,
        RETRACTED
    }

    public void init(HardwareMap hardwareMap, Telemetry telemetry) {
        init(hardwareMap, telemetry, ComputerVisionProcessor.AllianceColor.BLUE);
    }
    public void init(HardwareMap hardwareMap, Telemetry telemetry, ComputerVisionProcessor.AllianceColor teamColor){

        aprilTag = new AprilTagProcessor.Builder()
                .setOutputUnits(DistanceUnit.INCH, AngleUnit.DEGREES)
                .build();
        propDetectionProcessor = new ComputerVisionProcessor(telemetry);
        visionPortal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                .addProcessor(aprilTag)
                .addProcessor(propDetectionProcessor)
                .setCameraResolution(new Size(1280, 720))
                .build();
        frontLeft = hardwareMap.get(DcMotorEx.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotorEx.class, "frontRight");
        backLeft = hardwareMap.get(DcMotorEx.class, "backLeft");
        backRight = hardwareMap.get(DcMotorEx.class, "backRight");

        propDetectionProcessor.allianceColor = teamColor;
        arm = hardwareMap.get(DcMotorEx.class, "lift");
        purplePixelDropper = hardwareMap.get(Servo.class, "purplePixelDropper");
        //clawRight = hardwareMap.get(Servo.class, "armRight");
        //clawLeft = hardwareMap.get(Servo.class, "armLeft");


//        visionProcessor = new RI3WComputerVisionProcessor(allianceColor, telemetry);
//
//
        //pipeline = new RI3WComputerVisionProcessor(telemetry);

        // Create the vision portal the easy way.
        /*VisionPortal visionPortal = VisionPortal.easyCreateWithDefaults(
                hardwareMap.get(WebcamName.class, "Webcam 1"), pipeline);
         */

//        armRight.setPosition(ARM_SERVO_START_POSITION);
//        armLeft.setPosition(ARM_SERVO_START_POSITION);

        //Resetting encoders so they start at 0
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        //Running without encoders because it makes pid work better
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        //Making the drive motors break at 0 so they stop better
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // The following call to getRobotConfigurationName has been
        // copied over from prior years Utilities package. It allows
        // you to determine at runtime what the name of the robot configuration
        // is and initialize differently for different robots. In this case
        // the Team Robot and the Coach Robot have their drive motors
        // mounted differently. And therefore require different
        // forward/reverse motor choices at initialization.
        //
        // Switching which initialization is done at runtime
        // means you don't have to change the code every time you
        // switch between robots.
        //
        //
        String configurationName = ConfigUtilities.getRobotConfigurationName();
        if (configurationName.equals("coachbot")) {
            // Robot configuration: coachbot
            //    0-frontRight  (GoBILDA 5202/3/4 series)  (reverse)
            //    1-backRight   (GoBILDA 5202/3/4 series)  (reverse)
            //    2-frontLeft   (GoBILDA 5202/3/4 series)
            //    3-backLeft    (GoBILDA 5202/3/4 series)
            frontRight.setDirection(DcMotor.Direction.REVERSE);
            backRight.setDirection(DcMotor.Direction.REVERSE);

            // Use the new RevHubOrientationOnRobot classes to describe how the control hub is mounted on the robot.
            // For the coach bot its mounted Backward / usb cable on the right (as seen from back of robot)
            // Doc: https://github.com/FIRST-Tech-Challenge/FtcRobotController/wiki/Universal-IMU-Interface
            logoDirection = RevHubOrientationOnRobot.LogoFacingDirection.BACKWARD;
            usbFacingDirection  = RevHubOrientationOnRobot.UsbFacingDirection.LEFT;
            //bestSpeed = 0.25;
        }
        else {
            // teambot

            //Reversing the left motors so the robot goes straight
            frontLeft.setDirection(DcMotor.Direction.REVERSE);
            backLeft.setDirection(DcMotor.Direction.REVERSE);

            // Set the Rev Hub Orientation
            logoDirection = RevHubOrientationOnRobot.LogoFacingDirection.RIGHT;
            usbFacingDirection  = RevHubOrientationOnRobot.UsbFacingDirection.UP;
        }
        purplePixelDropper.setPosition(PURPLE_PIXEL_DROPPER_START_POSITION);
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);
        //claw.setPosition(CLOSED_POSITION);

        // Our Control Hub has the new IMU chip (BHI260AP). Use the new generic IMU class when
        // requesting a refernce to the IMU hardware. What chip you have can be determined by
        // using "program and manage" tab on dr iver station, then "manage" on the hamburger menu.
        imu = hardwareMap.get(IMU.class, "imu");

        // Use the new RevHubOrientationOnRobot classes to describe how the control hub is mounted on the robot.
        // For the coach bot its mounted Bgackward / usb cable on the right (as seen from back of robot)
        // Doc: https://github.com/FIRST-Tech-Challenge/FtcRobotController/wiki/Universal-IMU-Interface

        RevHubOrientationOnRobot orientationOnRobot = new RevHubOrientationOnRobot(logoDirection, usbFacingDirection);
        IMU.Parameters parameters = new IMU.Parameters(orientationOnRobot);

        boolean success = imu.initialize(parameters);
        if(success){
            telemetry.addLine("ERROR");
            telemetry.update();
        }
    }

    //Must be looped
    public void PIDLoop() {
        error = goalPosition - arm.getCurrentPosition();
        dArm = (error - pArm) / PIDTimer.seconds();
        iArm = iArm + (error * PIDTimer.seconds());
        pArm = error;
        armAngle = recalculateAngle();
        cosArm = Math.cos(Math.toRadians(armAngle));
        total = ((pArm * KP) + (iArm * KI) + (dArm * KD))/100 + (cosArm * kCos);
        PIDTimer.reset();

        if(currentArmHeight != lastArmHeight){
            iArm = 0;
        }

        if(iArm > iArmMax){
            iArm = iArmMax;
        }else if(iArm < -iArmMax){
            iArm = -iArmMax;
        }

        if(total > .5){
            total = .5;
        }
        if(recalculateAngle() > 60 && total < -.3){
            total = -.3;
        }else if(total < .005 && recalculateAngle() < 60){
            total = .005;
        }
        lastArmHeight = currentArmHeight;

        arm.setPower(total);
//        int encoderPosition = lift.getCurrentPosition();
//        error = goalPosition - encoderPosition;
//        double derivative = (error - lastError) / PIDTimer.seconds();
//
//        integralSum = integralSum + (error * PIDTimer.seconds());
//        double armPower;
//
//        armPower = (KP * error) + (KI * integralSum) + (KD * derivative) + KG;
//        lift.setPower(armPower);
//
//        lastError = error;
//
//        PIDTimer.reset();
    }

    public double recalculateAngle(){
        //Placeholder variables that will be deleted
        double rightAngleDiff = 800;
        double slope = 90/((zeroAngleTicks + rightAngleDiff) - zeroAngleTicks);
        double newAngle = slope * (arm.getCurrentPosition() - zeroAngleTicks);
        return newAngle;
    }

    public double getAngle(){
        YawPitchRollAngles angles = imu.getRobotYawPitchRollAngles();
        return AngleUnit.normalizeDegrees(angles.getYaw(AngleUnit.DEGREES));

    }

    public ComputerVisionProcessor.AllianceColor getAlliance(){
        return propDetectionProcessor.allianceColor;
    }

    public void telemetry(Telemetry telemetry) {
        telemetry.addData("PID Total", total);
        telemetry.addData("P Arm", pArm);
        telemetry.addData("I Arm", iArm);
        telemetry.addData("D Arm", dArm);
        telemetry.addData("Proportional Stuff", pArm * KP);
        telemetry.addData("Integral Stuff", iArm * KI);
        telemetry.addData("Derivative Stuff", dArm * KD);
    }

    @Override
    public void onOpened() {
        camera.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
    }

    @Override
    public void onError(int errorCode) {
        throw new RuntimeException("Something with the camera went wrong - Nick");
    }


}
