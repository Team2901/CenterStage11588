package org.firstinspires.ftc.teamcode.teleop.RI3W;

import android.os.Environment;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.hardware.RI3W.RI3WHardware;
import org.firstinspires.ftc.teamcode.hardware.controller.ImprovedGamepad;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

@TeleOp(name="Recording Teleop", group="11588")
public class RecordingTeleop extends OpMode {
    public RI3WHardware robot = new RI3WHardware();
    public ElapsedTime timer = new ElapsedTime();
    public boolean isOpen = true;
    FileOutputStream fileOutputStream = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + "/test.log", false);
    DataOutputStream writeFile = new DataOutputStream(fileOutputStream);
    public ImprovedGamepad gamepad;
    ElapsedTime PIDTimer = new ElapsedTime();
    public enum ClawPosition{Open, Closed}
    public enum Height{INTAKE, LOW, MID, HIGH, MAX}
    RI3WTeleop.Height currentLiftHeight = RI3WTeleop.Height.INTAKE;
    int liftTarget = 80;
    RI3WTeleop.Height lastLiftHeight = currentLiftHeight;

    double turningPower = 0;

    public RecordingTeleop() throws FileNotFoundException {
    }

    @Override
    public void init() {
        gamepad = new ImprovedGamepad(gamepad1, new ElapsedTime(), "Gamepad");
        robot.init(this.hardwareMap);
        timer.reset();
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
            liftTarget = RI3WHardware.INTAKE_ENCODER_VALUE;
            currentLiftHeight = RI3WTeleop.Height.INTAKE;
        } else if (gamepad.dpad_down.isInitialPress()) {
            liftTarget = RI3WHardware.LOW_POLE_ENCODER_VALUE;
            currentLiftHeight = RI3WTeleop.Height.LOW;
        } else if (gamepad.dpad_right.isInitialPress()) {
            liftTarget = RI3WHardware.MID_POLE_ENCODER_VALUE;
            currentLiftHeight = RI3WTeleop.Height.MID;
        } else if (gamepad.dpad_up.isInitialPress()) {
            liftTarget = RI3WHardware.HIGH_POLE_ENCODER_VALUE;
            currentLiftHeight = RI3WTeleop.Height.HIGH;
        } else if (gamepad.x.isInitialPress()) {
            liftTarget = RI3WHardware.MAX_HEIGHT_ENCODER_VALUE;
            currentLiftHeight = RI3WTeleop.Height.MAX;
        }

        robot.lift.setPower(liftPower(liftTarget));


        if(gamepad.y.isInitialPress()) {
            liftTarget += 10;
        } else if(gamepad.a.isInitialPress()) {
            liftTarget -= 10;
        }

        if (gamepad.left_bumper.isPressed()) {
            try {
                writeFile.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


        double y = .75 * gamepad.left_stick_y.getValue();
        double x = .75 * gamepad.left_stick_x.getValue();
        double rx = turningPower;

        robot.frontLeft.setPower(y + x + rx);
        robot.frontRight.setPower(y - x - rx);
        robot.backLeft.setPower(y - x + rx);
        robot.backRight.setPower(y + x - rx);
        robot.lift.setPower(liftPower(liftTarget));

        telemetry.addData("Right", gamepad.right_stick_y.getValue());
        telemetry.addData("Lrft", gamepad.left_stick_y.getValue());
        telemetry.addData("Claw", robot.hopper.getPosition());
        telemetry.addData("Lift Height", robot.lift.getCurrentPosition());
        telemetry.addData("Current Target Height", currentLiftHeight);
        telemetry.addData("Lift Target", liftTarget);
        telemetry.addData("Intake Position", RI3WHardware.INTAKE_ENCODER_VALUE);
        robot.telemetry(telemetry);
        telemetry.update();
        try {
            recordInput();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public double liftPower(int target){
        robot.error = target - robot.lift.getCurrentPosition();
        robot.dLift = (robot.error - robot.pLift) / PIDTimer.seconds();
        robot.iLift = robot.iLift + (robot.error * PIDTimer.seconds());
        robot.pLift = robot.error;
        robot.total = ((robot.pLift * RI3WHardware.KP) + (robot.iLift * RI3WHardware.KI) + (robot.dLift * RI3WHardware.KD))/100;
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

        robot.total = ((robot.pLift * RI3WHardware.KP) + (robot.iLift * RI3WHardware.KI) + (robot.dLift * RI3WHardware.KD))/100 +RI3WHardware.KG;

        lastLiftHeight = currentLiftHeight;

        return robot.total;
    }

    public void recordInput() throws IOException {
            // rx ry lx ly a b x y up down left right r_bumper l_bumper r_trigger l_trigger
            writeFile.writeDouble(timer.milliseconds());
            writeFile.writeFloat(gamepad.right_stick_x.getValue());
            writeFile.writeFloat(gamepad.right_stick_y.getValue());
            writeFile.writeFloat(gamepad.left_stick_x.getValue());
            writeFile.writeFloat(gamepad.left_stick_y.getValue());
            writeFile.writeBoolean(gamepad.a.isPressed());
            writeFile.writeBoolean(gamepad.b.isPressed());
            writeFile.writeBoolean(gamepad.x.isPressed());
            writeFile.writeBoolean(gamepad.y.isPressed());
            writeFile.writeBoolean(gamepad.dpad_up.isPressed());
            writeFile.writeBoolean(gamepad.dpad_down.isPressed());
            writeFile.writeBoolean(gamepad.dpad_left.isPressed());
            writeFile.writeBoolean(gamepad.dpad_right.isPressed());
            writeFile.writeBoolean(gamepad.right_bumper.isPressed());
            writeFile.writeBoolean(gamepad.left_bumper.isPressed());
            writeFile.writeFloat(gamepad.right_trigger.getValue());
            writeFile.writeFloat(gamepad.left_trigger.getValue());
            telemetry.addData("Data was written", true);
            writeFile.flush();
    }


}