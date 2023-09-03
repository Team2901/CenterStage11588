package org.firstinspires.ftc.teamcode.teleop.RI3W;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "PID Tuner", group = "11588")
public class PIDTuner extends OpMode {
    public DcMotorEx tuneMotor;
    private ElapsedTime PIDTimer = new ElapsedTime();
    double error = 0.0;
    double total = 0.0;
    double kp = 0.0;
    double ki = 0.0;
    double kd = 0.0;
    double kCos = 0.0;
    double pLift = 0.0;
    double iLift = 0.0;
    double dLift = 0.0;
    double cosLift = 0.0;
    double iLiftMax = 0.0;
    double liftHeight = 0;

    @Override
    public void init() {
        tuneMotor = hardwareMap.get(DcMotorEx.class, "tune motor");
        tuneMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        tuneMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        tuneMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        //tuneMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        tuneMotor.setPower(0);
    }
}
