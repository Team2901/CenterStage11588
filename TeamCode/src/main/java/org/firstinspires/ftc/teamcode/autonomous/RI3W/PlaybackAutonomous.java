package org.firstinspires.ftc.teamcode.autonomous.RI3W;

import android.os.Environment;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.hardware.RI3W.RI3WHardware;
import org.firstinspires.ftc.teamcode.hardware.controller.ImprovedGamepad;
import org.firstinspires.ftc.teamcode.teleop.RI3W.RI3WTeleop;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@Autonomous(name="Playback Auto", group="11588")
public class PlaybackAutonomous extends LinearOpMode {
    RI3WHardware robot = new RI3WHardware();
    ImprovedGamepad gamepad = new ImprovedGamepad(new Gamepad(), new ElapsedTime(), "Gamepad");
    Boolean checkedTimer = false;
    ElapsedTime timer = new ElapsedTime();

    double turningPower = 0;
    ElapsedTime PIDTimer = new ElapsedTime();
    public enum ClawPosition{Open, Closed}
    public enum Height{INTAKE, LOW, MID, HIGH, MAX}
    RI3WTeleop.Height currentLiftHeight = RI3WTeleop.Height.INTAKE;
    int liftTarget = 80;
    RI3WTeleop.Height lastLiftHeight = currentLiftHeight;

    FileInputStream fileInputStream = new FileInputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + "/test.log");
    DataInputStream readFile = new DataInputStream(fileInputStream);

    public PlaybackAutonomous() throws FileNotFoundException {
    }

    @Override
    public void runOpMode() throws InterruptedException {
        robot.init(this.hardwareMap);
        waitForStart();
        timer.reset();
        while (true) {
            try {
                autoLoop();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void autoLoop() throws IOException {
        double currentMiliseconds = timer.milliseconds();
        double fileMiliseconds = readFile.readDouble();

        while (currentMiliseconds < fileMiliseconds) {
            currentMiliseconds = timer.milliseconds();
        }
        gamepad.updateFromFile(readFile);
            //fileMiliseconds means the double value read for the ms that all the joystick and button-
            //values were recorded at
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

        double y = .75 * gamepad.left_stick_y.getValue();
        double x = .75 * gamepad.left_stick_x.getValue();
        double rx = turningPower;

        robot.frontLeft.setPower(y + x + rx);
        robot.frontRight.setPower(y - x - rx);
        robot.backLeft.setPower(y - x + rx);
        robot.backRight.setPower(y + x - rx);
        robot.lift.setPower(liftPower(liftTarget));
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

}
