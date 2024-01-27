package org.firstinspires.ftc.teamcode.teleop.Qual;



import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.hardware.qual.QualHardware;
import org.firstinspires.ftc.teamcode.hardware.controller.ImprovedGamepad;


@TeleOp(name="Qual Bulldozer Teleop", group="11588 Quals")
public class QualTeleop extends OpMode {
    public static final double DROPPER_OPEN_POSITION = .5;

    private boolean slidesDeployed = false;
    public static final int ARM_SERVO_POSITION = 1;
    public QualHardware robot = new QualHardware();
    public ImprovedGamepad gamepad;
    public ImprovedGamepad gamepadTwo;
    double turningPower = 0;
    ElapsedTime PIDTimer = new ElapsedTime();
    public enum Height{GROUND, FIRST, SECOND, THIRD}
    Height currentLiftHeight = Height.GROUND;
    int liftTarget = 80;
    Height lastLiftHeight = currentLiftHeight;
    boolean leftClawOpen = false;



    @Override
    public void init() {
        gamepad = new ImprovedGamepad(gamepad1, new ElapsedTime(), "Gamepad");
        gamepadTwo = new ImprovedGamepad(gamepad2, new ElapsedTime(), "Gamepad");
        robot.init(this.hardwareMap, telemetry, false);
        //robot.visionPortal.stopStreaming();
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

        if(gamepad.x.isInitialPress()) {
            if (robot.leftClawPositon == QualHardware.ClawPosition.CLOSED) {
                robot.clawLeft.setPosition(QualHardware.OPEN_CLAW_POSITION);
                robot.leftClawPositon = QualHardware.ClawPosition.OPEN;
                robot.clawRight.setPosition(QualHardware.OPEN_CLAW_POSITION);
                robot.rightClawPositon = QualHardware.ClawPosition.OPEN;
            } else {
                robot.clawLeft.setPosition(QualHardware.CLOSED_CLAW_POSITION);
                robot.leftClawPositon = QualHardware.ClawPosition.CLOSED;
                robot.clawRight.setPosition(QualHardware.CLOSED_CLAW_POSITION);
                robot.rightClawPositon = QualHardware.ClawPosition.CLOSED;
            }
        }

        if (gamepadTwo.a.isInitialPress() || gamepad.right_trigger.isInitialPress()) {
            robot.planeLauncher.setPosition(.5);
        }

        if (gamepad.right_bumper.isPressed() || gamepadTwo.right_bumper.isPressed()) {
            robot.lift.setPower(1);
        } else if (gamepad.left_bumper.isPressed() || gamepadTwo.right_bumper.isPressed()) {
            robot.lift.setPower(-1);
        } else {
            robot.lift.setPower(0);
        }

        if (gamepad.dpad_up.isPressed()) {
            robot.goalPosition += 1;
        } else if (gamepad.dpad_down.isPressed()) {
            robot.goalPosition -= 1;
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
        telemetry.addData("Lift Position", robot.arm.getCurrentPosition());
        telemetry.addData("Lift Target", robot.goalPosition);
        telemetry.addData("Arm Left servo", robot.clawLeft.getPosition());
        telemetry.addData("Arm Right servo", robot.clawRight.getPosition());
        telemetry.update();

    }

//    public void deploySlides() {
//        if (!slidesDeployed) {
//            robot.goalPosition = 150;
//            robot.clawRight.setPosition(1);
//            robot.clawLeft.setPosition(-1);
//            slidesDeployed = true;
//        }
//    }

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