package org.firstinspires.ftc.teamcode.autonomous.qual;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.hardware.vision.ComputerVisionProcessor;

@Autonomous(name="testRedPath V8", group="11588")
public class testRedPath extends AbstractAutonomous {

    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap, telemetry, ComputerVisionProcessor.AllianceColor.RED);
        waitForStart();
        telemetry.clear();
        telemetry.addLine("push a button");
        telemetry.update();
        while (!gamepad1.a) {idle();}
        robot.speed = 0.5;
        startToDropPurplePixel(PropPosition.MIDDLE);
        telemetry.clear();
        telemetry.addLine("push b button");
        telemetry.update();
        while (!gamepad1.b) {idle();}
        robot.speed = 0.5;
        purplePixelToWhitePixelPickupFrontStage();
        telemetry.clear();
        telemetry.addLine("push x button");
        telemetry.update();
        while (!gamepad1.x) {idle();}
        robot.speed = 0.5;
        whitePixelsToBackstagePathFrontStage();
        telemetry.clear();
        telemetry.addLine("push y button");
        telemetry.update();
        while (!gamepad1.y) {idle();}
        robot.speed = 0.5;
        backstageToParkPathFrontStage();
        telemetry.clear();
        telemetry.addLine("push a button");
        telemetry.update();


        while (opModeIsActive() && robot.backLeft.isBusy())  {
            ;
        }
    }

}
