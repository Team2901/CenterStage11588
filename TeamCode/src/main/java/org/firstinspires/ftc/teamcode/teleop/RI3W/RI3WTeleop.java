package org.firstinspires.ftc.teamcode.teleop.RI3W;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.hardware.RI3W.RI3WHardware;
import org.firstinspires.ftc.teamcode.hardware.controller.ImprovedGamepad;


@TeleOp(name="RI3W Teleop", group="11588")
public class RI3WTeleop extends OpMode {
    public RI3WHardware robot = new RI3WHardware();
    public ImprovedGamepad gamepad;
    double turningPower = 0;
    public enum Height{INTAKE, LOW, MID,HIGH}
    Height currentLiftHeight = Height.INTAKE;
    int liftTarget = 10;
    Height lastLiftHeight = currentLiftHeight;
    int intakeLiftPosition = 50;
    int lowLiftPosition = 1557;
    int midLiftPosition = 2757;
    int highLiftPosition = 3939;
    @Override
    public void init() {
        gamepad = new ImprovedGamepad(gamepad1, new ElapsedTime(), "Gamepad");
        robot.init(this.hardwareMap);
    }

    @Override
    public void loop() {
        gamepad.update();
        if(gamepad.right_trigger.getValue() > 0){
            turningPower = .3 * gamepad.right_trigger.getValue();
        }else if(gamepad.left_trigger.getValue() > 0){
            turningPower = -.3 * gamepad.left_trigger.getValue();
        }else{
            turningPower = .75 * gamepad.right_stick_x.getValue();
        }

        if(gamepad.dpad_left.isInitialPress()) {
            robot.lift.setTargetPosition(intakeLiftPosition);
        } else if (gamepad.dpad_down.isInitialPress()) {
            robot.lift.setTargetPosition(lowLiftPosition);
        } else if (gamepad.dpad_right.isInitialPress()) {
            robot.lift.setTargetPosition(midLiftPosition);
        } else if (gamepad.dpad_up.isInitialPress()) {
            robot.lift.setTargetPosition(highLiftPosition);
        }
        robot.lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        double y = .75 * gamepad.left_stick_y.getValue();
        double x = .75 * gamepad.left_stick_x.getValue();
        double rx = turningPower;

        robot.frontLeft.setPower(y + x + rx);
        robot.frontRight.setPower(y - x - rx);
        robot.backLeft.setPower(y - x + rx);
        robot.backRight.setPower(y + x - rx);

        telemetry.addData("Right", gamepad.right_stick_y.getValue());
        telemetry.addData("Lrft", gamepad.left_stick_y.getValue());
        telemetry.addData("Lift Motor", robot.lift.getCurrentPosition());
        telemetry.addData("Target Lift Motor", robot.lift.getTargetPosition());
        telemetry.update();
    }
}
