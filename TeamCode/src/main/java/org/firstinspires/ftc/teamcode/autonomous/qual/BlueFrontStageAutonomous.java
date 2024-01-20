package org.firstinspires.ftc.teamcode.autonomous.qual;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.hardware.vision.ComputerVisionProcessor;

@Autonomous(name="BLue Front Stage", group="11588")
public class BlueFrontStageAutonomous extends AbstractAutonomous {

    @Override
    public void runOpMode() throws InterruptedException {
        // Since we are updating telemetry from multiple places,
        // we want to prevent clearing the telemetry when update is called.
        // If needed, we can call telemetry.clear() too.
        telemetry.setAutoClear(false);

        robot.init(hardwareMap, telemetry, ComputerVisionProcessor.AllianceColor.BLUE);
        waitForStart();

        // prop detection should have already occurred, but just in case
        // init is over and we have started, loop here


        // TODO: Stop camera now that we have found the prop

        // Move based on the detected prop position
        robot.speed = 0.5;
        if (robot.propDetectionProcessor.propPosition == ComputerVisionProcessor.PropPosition.LEFT) {
            moveXY(-25, 0);
            moveXY(0, 12);
            dropPurplePixel();
            moveXY(0, -12);
            robot.purplePixelDropper.setPosition(robot.PURPLE_PIXEL_DROPPER_START_POSITION);
            turnToAngle(180);
        } else if (robot.propDetectionProcessor.propPosition == ComputerVisionProcessor.PropPosition.RIGHT){
            moveXY(-25, 0);
            moveXY(0, -12);
            dropPurplePixel();
            moveXY(0, 12);
            robot.purplePixelDropper.setPosition(robot.PURPLE_PIXEL_DROPPER_START_POSITION);
            turnToAngle(180);
        } else if (robot.propDetectionProcessor.propPosition == ComputerVisionProcessor.PropPosition.MIDDLE) {
            moveXY(-30, 0);
            dropPurplePixel();
            moveXY(11, 0);
            robot.purplePixelDropper.setPosition(0);
            turnToAngle(180);
        } else {
            throw new RuntimeException("Prop position was not found");
        }

        placePurplePixelGoToBackBoardPark();

        while (!isStopRequested()) {
            telemetry.update();
        }
    }
}
