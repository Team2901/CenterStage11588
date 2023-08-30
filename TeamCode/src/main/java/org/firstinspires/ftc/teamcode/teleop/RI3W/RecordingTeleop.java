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
    //test
    int lastKnownMilisecond;

    FileOutputStream fileOutputStream = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + "/test.log", false);
    DataOutputStream writeFile = new DataOutputStream(fileOutputStream);
    public ImprovedGamepad gamepad;
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
        try {
            recordInput();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        lastKnownMilisecond = (int) Math.floor(timer.milliseconds());
        if(gamepad.right_trigger.getValue() > 0){
            turningPower = .3 * gamepad.right_trigger.getValue();
        }else if(gamepad.left_trigger.getValue() > 0){
            turningPower = -.3 * gamepad.left_trigger.getValue();
        }else{
            turningPower = .75 * gamepad.right_stick_x.getValue();
            //turningPower = impGamepad1.right_stick_x.getValue();
        }
        double y = .75 * gamepad.left_stick_y.getValue();
        double x = .75 * gamepad.left_stick_x.getValue();
        //double y = impGamepad1.left_stick_y.getValue();
        //double x = impGamepad1.left_stick_x.getValue();
        double rx = turningPower;

        robot.frontLeft.setPower(y + x + rx);
        robot.frontRight.setPower(y - x - rx);
        robot.backLeft.setPower(y - x + rx);
        robot.backRight.setPower(y + x - rx);

        telemetry.addData("Right", gamepad.right_stick_y.getValue());
        telemetry.addData("Lrft", gamepad.left_stick_y.getValue());

        if (gamepad.a.isPressed()) {
            try {
                writeFile.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
//        telemetry.addData("Front Left Power", robot.frontLeft.getPower());
//        telemetry.addData("Front Right Power", robot.frontRight.getPower());
//        telemetry.addData("Back Left Power", robot.backLeft.getPower());
//        telemetry.addData("Back Right Power", robot.backRight.getPower());
//        telemetry.addData("Front Left Position", robot.frontLeft.getCurrentPosition());
//        telemetry.addData("Front Right Position", robot.frontRight.getCurrentPosition());
//        telemetry.addData("Back Left Position", robot.backLeft.getCurrentPosition());
//        telemetry.addData("Back Right Position", robot.backRight.getCurrentPosition());
//        telemetry.addData("Robot Angle", robot.getAngle());
        telemetry.update();
    }

    public void recordInput() throws IOException {
        int currentMiliseconds = (int) Math.floor(timer.milliseconds());
        if (currentMiliseconds > lastKnownMilisecond && isOpen) {
            // ry rx ly lx a b x y up down left right r_bumper l_bumper r_trigger l_trigger
            lastKnownMilisecond = currentMiliseconds;
            writeFile.writeFloat(gamepad.right_stick_y.getValue());
            writeFile.writeFloat(gamepad.right_stick_x.getValue());
            writeFile.writeFloat(gamepad.left_stick_y.getValue());
            writeFile.writeFloat(gamepad.left_stick_x.getValue());
            writeFile.writeBoolean(gamepad.a.isPressed());
            writeFile.writeBoolean(gamepad.b.isPressed());
            writeFile.writeBoolean(gamepad.x.isPressed());
            writeFile.writeBoolean(gamepad.y.isPressed());
            writeFile.writeBoolean(gamepad.dpad_up.isPressed());
            writeFile.writeBoolean(gamepad.dpad_down.isPressed());
            writeFile.writeBoolean(gamepad.dpad_left.isPressed());
            writeFile.writeBoolean(gamepad.dpad_left.isPressed());
            writeFile.writeBoolean(gamepad.right_bumper.isPressed());
            writeFile.writeBoolean(gamepad.left_bumper.isPressed());
            writeFile.writeFloat(gamepad.right_trigger.getValue());
            writeFile.writeFloat(gamepad.left_trigger.getValue());
            telemetry.addData("Data was written", true);
            writeFile.flush();
        }
    }
}