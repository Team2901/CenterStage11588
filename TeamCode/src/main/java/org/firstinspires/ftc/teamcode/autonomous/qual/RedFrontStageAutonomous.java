package org.firstinspires.ftc.teamcode.autonomous.qual;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.hardware.vision.ComputerVisionProcessor;

@Autonomous(name="Red Front Stage", group="11588")
public class RedFrontStageAutonomous extends AbstractAutonomous {

    @Override
    public void runOpMode() throws InterruptedException {
        // Since we are updating telemetry from multiple places,
        // we want to prevent clearing the telemetry when update is called.
        // If needed, we can call telemetry.clear() too.
        telemetry.setAutoClear(false);

        robot.init(hardwareMap, telemetry, ComputerVisionProcessor.AllianceColor.RED);
        waitForStart();
        while (robot.propDetectionProcessor.propPosition == null && opModeIsActive()) {
            idle();
        }

        robot.visionPortal.stopStreaming();
        // prop detection should have already occurred, but just in case
        // init is over and we have started, loop here



        purplePixelToWhitePixelPickupFrontStage();
        whitePixelsToBackstagePathFrontStage();
        placeOnBackdrop();
        backstageToParkPathFrontStage();


        while (!isStopRequested()) {
            telemetry.update();
        }
    }
}

