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

        // prop detection should have already occurred, but just in case
        // init is over and we have started, loop here


        // TODO: Stop camera now that we have found the prop


        robot.speed = robot.bestSpeed;
        if (robot.propDetectionProcessor.propPosition == ComputerVisionProcessor.PropPosition.LEFT) {
            moveXY(-25, 0);
            moveXY(0, 12);
            dropPurplePixel();
            moveXY(22, 0);
            robot.purplePixelDropper.setPosition(robot.PURPLE_PIXEL_DROPPER_START_POSITION);
            turnToAngle(-90);
        } else if (robot.propDetectionProcessor.propPosition == ComputerVisionProcessor.PropPosition.RIGHT){
            moveXY(-25, 0);
            moveXY(0, -12);
            dropPurplePixel();
            moveXY(1, 0);
            moveXY(0, 12);
            moveXY(19, 0);
            robot.purplePixelDropper.setPosition(robot.PURPLE_PIXEL_DROPPER_START_POSITION);
            turnToAngle(180);
        } else {
            moveXY(-30, 0);
            dropPurplePixel();
            moveXY(11, 0);
            robot.purplePixelDropper.setPosition(robot.PURPLE_PIXEL_DROPPER_START_POSITION);
            turnToAngle(180);
        }

        purplePixelToWhitePixelPickupFrontStage();
        whitePixelsToBackstagePathFrontStage();
        backstageToParkPathFrontStage();


        while (!isStopRequested()) {
            telemetry.update();
        }
    }
}
