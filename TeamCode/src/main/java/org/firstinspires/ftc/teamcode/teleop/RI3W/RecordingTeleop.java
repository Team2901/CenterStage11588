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
import java.sql.Date;
import java.text.SimpleDateFormat;

@TeleOp(name="Recording Teleop", group="11588")
public class RecordingTeleop extends OpMode {
    public RI3WHardware robot = new RI3WHardware();
    public ElapsedTime timer = new ElapsedTime();
    FileOutputStream fileOutputStream = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + "/test" + new SimpleDateFormat("HH.mm.ss").format(new java.util.Date()) + ".log", false);
    DataOutputStream writeFile = new DataOutputStream(fileOutputStream);
    public ImprovedGamepad gamepad;


    public RecordingTeleop() throws FileNotFoundException {
    }

    @Override
    public void init() {
        gamepad = new ImprovedGamepad(gamepad1, new ElapsedTime(), "Gamepad");
        robot.init(this.hardwareMap, telemetry);
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