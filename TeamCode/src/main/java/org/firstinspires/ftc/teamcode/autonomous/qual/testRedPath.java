package org.firstinspires.ftc.teamcode.autonomous.qual;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.hardware.vision.ComputerVisionProcessor;

@Autonomous(name="testRedPath V6", group="11588")
public class testRedPath extends AbstractAutonomous {

    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap, telemetry, ComputerVisionProcessor.AllianceColor.RED);
        //waitForStart();
        //robot.speed = 0.5;
        //startToDropPurplePixel(PropPosition.MIDDLE);
        //while (!gamepad1.a) idle();
        //robot.speed = 0.5;
        //purplePixelToWhitePixelPickupFrontStage();
        //while (!gamepad1.a) idle();
        //robot.speed = 0.5;
        //whitePixelsToBackstagePathFrontStage();
        //while (!gamepad1.a) idle();
        //robot.speed = 0.5;
        backstageToParkPathFrontStage();

        while (opModeIsActive() && robot.backLeft.isBusy())  {
            ;
        }
    }

}
