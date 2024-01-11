package org.firstinspires.ftc.teamcode.teleop.qual;



import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.hardware.RI3W.RI3WHardware;
import org.firstinspires.ftc.teamcode.hardware.qual.QualHardware;
import org.firstinspires.ftc.teamcode.hardware.controller.ImprovedGamepad;
import org.firstinspires.ftc.teamcode.teleop.RI3W.RI3WTeleop;


@TeleOp(name="Qual Teleop", group="11588 Quals")
public class QualTeleop extends OpMode {
    public QualHardware robot = new QualHardware();
    public ImprovedGamepad gamepad;
    double turningPower = 0;
    ElapsedTime PIDTimer = new ElapsedTime();
    public enum Height{INTAKE, MAX}
    Height currentLiftHeight = Height.INTAKE;
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
/*
        if(gamepad.left_bumper.isInitialPress()) {
            if (robot.draggerPosition == QualHardware.DraggerPosition.UP) {
                robot.draggerPosition = QualHardware.DraggerPosition.DOWN;
                robot.dragger.setPosition(QualHardware.DRAGGER_DOWN_POSITION);
            } else if (robot.draggerPosition == QualHardware.DraggerPosition.DOWN) {
                robot.draggerPosition = QualHardware.DraggerPosition.UP;

                robot.dragger.setPosition(QualHardware.DRAGGER_UP_POSITION);
            }
        }
*/

        if (gamepad.dpad_down.isPressed()) {
            liftTarget = QualHardware.INTAKE_ENCODER_VALUE;
            currentLiftHeight = Height.INTAKE;
        } else if (gamepad.dpad_up.isInitialPress()) {
            liftTarget = QualHardware.MAX_HEIGHT_ENCODER_VALUE;
            currentLiftHeight = Height.MAX;
        }

        if(gamepad.left_bumper.isInitialPress()) {
            liftTarget -= 10;
        } else if(gamepad.right_bumper.isInitialPress()) {
            liftTarget += 10;
        }

        robot.lift.setPower(liftPower(liftTarget));

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
        telemetry.addData("Lift Target", liftTarget);
        telemetry.update();

    }

    public double liftPower(int target){
        robot.error = target - robot.lift.getCurrentPosition();
        robot.dLift = (robot.error - robot.pLift) / PIDTimer.seconds();
        robot.iLift = robot.iLift + (robot.error * PIDTimer.seconds());
        robot.pLift = robot.error;
        robot.total = ((robot.pLift * QualHardware.KP) + (robot.iLift * QualHardware.KI) + (robot.dLift * QualHardware.KD))/100;
        PIDTimer.reset();


        if(currentLiftHeight != lastLiftHeight){
            robot.iLift = 0;
        }
        if(robot.iLift > robot.iLiftMax){
            robot.iLift = robot.iLiftMax;
        }else if(robot.iLift < -robot.iLiftMax){
            robot.iLift = -robot.iLiftMax;
        }
        if(robot.total > .5){
            robot.total = .5;
        }

        robot.total = ((robot.pLift * QualHardware.KP) + (robot.iLift * QualHardware.KI) + (robot.dLift * QualHardware.KD))/100 +QualHardware.KG;

        lastLiftHeight = currentLiftHeight;

        return robot.total;
    }

}