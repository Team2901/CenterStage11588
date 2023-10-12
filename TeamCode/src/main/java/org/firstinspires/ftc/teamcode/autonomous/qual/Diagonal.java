package org.firstinspires.ftc.teamcode.autonomous.qual;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Diagonal", group="11588")
public class Diagonal extends AbstractAutonomous{
    public void runOpMode() throws InterruptedException {
        // Go diagonal
        moveXY(24, 12);
    }
}
