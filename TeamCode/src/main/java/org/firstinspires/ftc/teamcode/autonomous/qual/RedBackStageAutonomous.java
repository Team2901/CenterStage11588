package org.firstinspires.ftc.teamcode.autonomous.qual;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.hardware.vision.ComputerVisionProcessor;

@Autonomous(name="Red Back Stage", group="11588")
public class RedBackStageAutonomous extends AbstractAutonomous {
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

        // Move based on the detected prop position
        if (robot.propDetectionProcessor.propPosition == ComputerVisionProcessor.PropPosition.LEFT) {
            moveXY(24, 0);
            moveXY(0, 22);
        } else if (robot.propDetectionProcessor.propPosition == ComputerVisionProcessor.PropPosition.RIGHT){
            moveXY(34, 0);
            turnToAngle(-90);
            moveXY(3, 0);
            turnToAngle(360);
            moveXY(-10, 0);
            moveXY(0, 16);
            turnToAngle(360);
        } else if (robot.propDetectionProcessor.propPosition == ComputerVisionProcessor.PropPosition.MIDDLE) {
            moveXY(25, 0);
            moveXY(0, 21);
        } else {
            throw new RuntimeException("Prop position was not found");
        }
        backStagePath();

        while (!isStopRequested()) {
            telemetry.update();
        }
    }
}
