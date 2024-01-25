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
        robot.camera.stopStreaming();
        while (robot.propDetectionProcessor.propPosition == null && opModeIsActive()) {
            idle();
        }

        // prop detection should have already occurred, but just in case
        // init is over and we have started, loop here


        // TODO: Stop camera now that we have found the prop

        purplePixelToWhitePixelPickupFrontStage();
        whitePixelsToBackstagePathFrontStage();
        if(robot.propDetectionProcessor.propPosition == ComputerVisionProcessor.PropPosition.LEFT){
            moveXY(0, 6);
            moveXY(0, 21);
        }
        else if(robot.propDetectionProcessor.propPosition == ComputerVisionProcessor.PropPosition.RIGHT){
            moveXY(0, -6);
            moveXY(0,  33);
        }
        backstageToParkPathFrontStage();


        while (!isStopRequested()) {
            telemetry.update();
        }
    }
}

