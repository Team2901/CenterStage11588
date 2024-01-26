package org.firstinspires.ftc.teamcode.autonomous.qual;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.hardware.vision.ComputerVisionProcessor;

@Autonomous(name="Back Stage Red", group="11588")
public class BackStageAutonomousRed extends AbstractAutonomous {
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

        robot.camera.stopStreaming();
        // prop detection should have already occurred, but just in case
        // init is over and we have started, loop here


        // Move based on the detected prop position
        if (robot.propDetectionProcessor.propPosition == ComputerVisionProcessor.PropPosition.LEFT) {
            moveXY(-25, 0);
            moveXY(0, -12);
            moveXY(-2, 0);
            dropPurplePixel();
            moveXY(3, 0);
            moveXY(0, 12);
            moveXY(-1, 0);
            robot.purplePixelDropper.setPosition(robot.PURPLE_PIXEL_DROPPER_START_POSITION);
        } else if (robot.propDetectionProcessor.propPosition == ComputerVisionProcessor.PropPosition.RIGHT){
            moveXY(-25, 0);
            moveXY(0, 12);
            moveXY(-2, 0);
            dropPurplePixel();
            moveXY(3, 0);
            moveXY(0, -12);
            moveXY(-1, 0);
            robot.purplePixelDropper.setPosition(robot.PURPLE_PIXEL_DROPPER_START_POSITION);
        } else{
            moveXY(-30, 0);
            dropPurplePixel();
            moveXY(11, 0);
            robot.purplePixelDropper.setPosition(robot.PURPLE_PIXEL_DROPPER_START_POSITION);
        }
        leftPathExtra();
        if(robot.propDetectionProcessor.propPosition == ComputerVisionProcessor.PropPosition.LEFT){
            moveXY(0, 6);
            moveXY(0, 17);
        }
        else if(robot.propDetectionProcessor.propPosition == ComputerVisionProcessor.PropPosition.RIGHT){
            moveXY(0, -6);
            moveXY(0, 29);
        }

        while (!isStopRequested()) {
            telemetry.update();
        }
    }
}

