package org.firstinspires.ftc.teamcode.autonomous.qual;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.hardware.vision.ComputerVisionProcessor;

@Autonomous(name="testRedPath V8", group="11588")
public class testRedPath extends AbstractAutonomous {

    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap, telemetry, ComputerVisionProcessor.AllianceColor.BLUE);
        waitForStart();
        robot.speed = 0.5;
        startToDropPurplePixel(PropPosition.MIDDLE);
        robot.speed = 0.5;
        purplePixelToWhitePixelPickupFrontStage();
        robot.speed = 0.5;
        whitePixelsToBackstagePathFrontStage();
        while (!gamepad1.a) {idle();}
        robot.speed = 0.5;
        //backstageToParkPathFrontStage();
        if (robot.propDetectionProcessor.allianceColor == ComputerVisionProcessor.AllianceColor.RED) {
            telemetry.addLine("red");
            telemetry.update();
            while (!gamepad1.a) {idle();}
            moveXY(0, 30);
        } else {
            telemetry.addLine("blue");
            telemetry.update();
            while (!gamepad1.a) {idle();}
            moveXY(0, -100);

        }

        while (opModeIsActive() && robot.backLeft.isBusy())  {
            ;
        }
    }

}
