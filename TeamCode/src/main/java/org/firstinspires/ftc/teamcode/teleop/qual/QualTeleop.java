package org.firstinspires.ftc.teamcode.teleop.Qual;



import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.hardware.qual.QualHardware;
import org.firstinspires.ftc.teamcode.hardware.controller.ImprovedGamepad;


@TeleOp(name="Qual Bulldozer Teleop", group="11588 Quals")
public class QualTeleop extends OpMode {
    public static final double DROPPER_OPEN_POSITION = .5;
    public static final int ARM_SERVO_POSITION = 1;
    public QualHardware robot = new QualHardware();
    public ImprovedGamepad gamepad;
    double turningPower = 0;
    ElapsedTime PIDTimer = new ElapsedTime();
    public enum Height{GROUND, FIRST, SECOND, THIRD}
    Height currentLiftHeight = Height.GROUND;
    int liftTarget = 80;
    Height lastLiftHeight = currentLiftHeight;



    @Override
    public void init() {
        gamepad = new ImprovedGamepad(gamepad1, new ElapsedTime(), "Gamepad");
        robot.init(this.hardwareMap, telemetry);
    }

    @Override
    public void loop() {
        gamepad.update();
        if (gamepad.right_trigger.getValue() > 0) {
            turningPower = .3 * gamepad.right_trigger.getValue();
        } else if (gamepad.left_trigger.getValue() > 0) {
            turningPower = -.3 * gamepad.left_trigger.getValue();
        } else {
            turningPower = .75 * gamepad.right_stick_x.getValue();
        }

        if (gamepad.y.isPressed()) {
            robot.intake.setPower(.75);
        } else if (gamepad.x.isPressed()) {
            robot.intake.setPower(-.75);
        }

        if (gamepad.a.isInitialPress()) {
            robot.purplePixelDropper.setPosition(DROPPER_OPEN_POSITION);
        }

//        if (gamepad.b.isPressed() && robot.armRight.getPosition() == 0) {
//            robot.armRight.setPosition(ARM_SERVO_POSITION);
//            robot.armLeft.setPosition(ARM_SERVO_POSITION);
//        } else {
//            robot.armRight.setPosition(0);
//            robot.armLeft.setPosition(0);
//        }


        if(gamepad.left_bumper.isPressed() && robot.goalPosition > 5) {
            robot.goalPosition -= 5;
        } else if(gamepad.right_bumper.isPressed() && robot.goalPosition < robot.MAX_LIFT_HEIGHT) {
            robot.goalPosition += 5;
        }

        robot.PIDLoop();

        double y = 1 * gamepad.left_stick_y.getValue();
        double x = 1 * gamepad.left_stick_x.getValue();
        double rx = turningPower;

        robot.frontLeft.setPower(y + x + rx);
        robot.frontRight.setPower(y - x - rx);
        robot.backLeft.setPower(y - x + rx);
        robot.backRight.setPower(y + x - rx);


        telemetry.addData("Right", gamepad.right_stick_y.getValue());
        telemetry.addData("Left", gamepad.left_stick_y.getValue());
        telemetry.addData("Lift Speed", robot.liftSpeed);
        telemetry.addData("Lift Position", robot.lift.getCurrentPosition());
        telemetry.addData("Lift Target", robot.goalPosition);
//        telemetry.addData("Arm Left servo", robot.armLeft.getPosition());
//        telemetry.addData("Arm Right servo", robot.armRight.getPosition());
        telemetry.update();

    }

//    public double liftPower(int target){
//        robot.error = target - robot.lift.getCurrentPosition();
//        robot.dLift = (robot.error - robot.pLift) / PIDTimer.seconds();
//        robot.iLift = robot.iLift + (robot.error * PIDTimer.seconds());
//        robot.pLift = robot.error;
//        robot.total = ((robot.pLift * QualHardware.KP) + (robot.iLift * QualHardware.KI) + (robot.dLift * QualHardware.KD))/100;
//        PIDTimer.reset();
//
//
//        if(currentLiftHeight != lastLiftHeight){
//            robot.iLift = 0;
//        }
//        if(robot.iLift > robot.iLiftMax){
//            robot.iLift = robot.iLiftMax;
//        }else if(robot.iLift < -robot.iLiftMax){
//            robot.iLift = -robot.iLiftMax;
//        }
//        if(robot.total > .5){
//            robot.total = .5;
//        }
//
//        robot.total = ((robot.pLift * QualHardware.KP) + (robot.iLift * QualHardware.KI) + (robot.dLift * QualHardware.KD))/100 +QualHardware.KG;
//
//        lastLiftHeight = currentLiftHeight;
//
//        return robot.total;
//    }

}