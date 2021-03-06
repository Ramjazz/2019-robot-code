package frc.robot.util;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMax.ExternalFollower;
import frc.robot.subsystems.DriveTrain;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.revrobotics.CANSparkMax.IdleMode;

import frc.robot.util.interfaces.IMercMotorController;

public class MercSparkMax implements IMercMotorController {
    private CANSparkMax sparkmax;
    private int port;
    private double setPos;

    public MercSparkMax(int port) {
        sparkmax = new CANSparkMax(port, CANSparkMaxLowLevel.MotorType.kBrushless);
        sparkmax.setSmartCurrentLimit(20);
        this.port = port;
    }

    @Override
    public void setSpeed(double speed) {
        sparkmax.set(speed);
    }

    @Override
    public void setPosition(double ticks) {
        setPos = MercMath.encoderTicksToRevs(ticks);
        sparkmax.getPIDController().setReference(setPos, ControlType.kPosition);
    }

    @Override
    public double getSpeed() {
        return sparkmax.get();
    }
    
    @Override
    public void setInverted(boolean invert) {
        sparkmax.setInverted(invert);
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public void follow(IMercMotorController leader) {
        if (leader instanceof MercSparkMax)
            sparkmax.follow(((MercSparkMax)leader).get());
        else
            sparkmax.follow((ExternalFollower)leader, leader.getPort());    //Def the wrong way to do this please check
    }

    @Override
    public void stop() {
        sparkmax.stopMotor();
    }

    @Override
    public double getEncPos() {
        return MercMath.revsToEncoderTicks(sparkmax.getEncoder().getPosition());
    }

    @Override
    public double getEncVelo() {
        return sparkmax.getEncoder().getVelocity();
    }

    @Override
    public double getClosedLoopError() {
        return setPos - sparkmax.getEncoder().getPosition();
    }

    @Override
    public void configPID(double p, double i, double d, double f) {
        sparkmax.getPIDController().setP(p, DriveTrain.SLOT_0);
        sparkmax.getPIDController().setI(i, DriveTrain.SLOT_0);
        sparkmax.getPIDController().setD(d, DriveTrain.SLOT_0);
        sparkmax.getPIDController().setFF(f, DriveTrain.SLOT_0);
    }

    @Override
    public void configVoltage(double nominalOutput, double peakOutput) {
        sparkmax.getPIDController().setOutputRange(nominalOutput, peakOutput);
    }

    @Override
    public void setNeutralMode(NeutralMode neutralMode) {
        CANSparkMax.IdleMode mode;
        if (neutralMode == NeutralMode.Brake)
            mode = IdleMode.kBrake;
        else
            mode = IdleMode.kCoast;
        sparkmax.setIdleMode(mode);
    }

//_________________________________________________________________________________
    /**
     * Get the Spark Max tied to this class
     * @return the Spark
     */
    public CANSparkMax get() {
        return sparkmax;
    }
}