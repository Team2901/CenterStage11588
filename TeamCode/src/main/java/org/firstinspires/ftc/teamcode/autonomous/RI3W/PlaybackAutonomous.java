package org.firstinspires.ftc.teamcode.autonomous.RI3W;

import android.os.Environment;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.hardware.RI3W.RI3WHardware;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@Autonomous(name="Playback Auto", group="11588")
public class PlaybackAutonomous extends LinearOpMode {
    RI3WHardware robot = new RI3WHardware();
    Boolean checkedTimer = false;
    ElapsedTime timer = new ElapsedTime();
    FileInputStream fileInputStream = new FileInputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + "/test.log");
    DataInputStream readFile = new DataInputStream(fileInputStream);
    int lastKnownMilisecond = 0;
    double turningPower = 0;

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
        double fileMiliseconds = 0;
        double rightStickY = 0;
        double rightStickX = 0;
        double leftStickY = 0;
        double leftStickX = 0;
        boolean aButton;
        boolean bButton;
        boolean xButton;
        boolean yButton;
        boolean dpadUp;
        boolean dpadDown;
        boolean dpadLeft;
        boolean dpadRight;
        boolean rightBumper;
        boolean leftBumper;
        float rightTrigger = 0;
        float leftTrigger = 0;

        if (checkedTimer == false) {
           fileMiliseconds = readFile.readDouble();
           checkedTimer = true;
        }
        /*I wrote the above statement to keep it from reading the next byte before the time for those
        instructions has been executed. I don't want it to keep reading the next double before it
        executes the instructions that come after that double, because in sequence the next instruction
        is not a double, but a float(Because it should be the joystick value at that ms)
         */
        while (currentMiliseconds < fileMiliseconds) {
            currentMiliseconds = timer.milliseconds();
        }
            //fileMiliseconds means the double value read for the ms that all the joystick and button-
            //values were recorded at
            rightStickY = readFile.readFloat();
            rightStickX = readFile.readFloat();
            leftStickY = readFile.readFloat();
            leftStickX = readFile.readFloat();
            aButton = readFile.readBoolean();
            bButton = readFile.readBoolean();
            xButton = readFile.readBoolean();
            yButton = readFile.readBoolean();
            dpadUp = readFile.readBoolean();
            dpadDown = readFile.readBoolean();
            dpadLeft = readFile.readBoolean();
            dpadRight = readFile.readBoolean();
            rightBumper = readFile.readBoolean();
            leftBumper = readFile.readBoolean();
            rightTrigger = readFile.readFloat();
            leftTrigger = readFile.readFloat();
            turningPower = .75 * rightStickX;
            double y = .75 * leftStickY;
            double x = .75 * leftStickX;
            double rx = turningPower;

            robot.frontLeft.setPower(y + x + rx);
            robot.frontRight.setPower(y - x - rx);
            robot.backLeft.setPower(y - x + rx);
            robot.backRight.setPower(y + x - rx);
            checkedTimer = false;
    }

}
