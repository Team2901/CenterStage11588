package org.firstinspires.ftc.teamcode.autonomous.qual;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Straight", group="11588")
public class Straight extends AbstractAutonomous {

    public void runOpMode() throws InterruptedException {
        //Go straight and turn
        moveXY(24, 0);
        //turn right
        turnToAngle(90);

    }
}
