package frc.robot.util;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import frc.robot.util.interfaces.IMercMotorController;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;

/**
 * Helper class for various manual drive controls
 * using Controllers. This is to replace {@link DifferentialDrive},
 * which can interfere with Controller's in closed-loop mode.
 * 
 */
public class DriveAssist {
	private final Logger LOG = LogManager.getLogger(DriveAssist.class);
	private final DelayableLogger SLOW_LOG = new DelayableLogger(LOG, 10, TimeUnit.SECONDS);
	private final IMercMotorController LEFT_CONTROLLER, RIGHT_CONTROLLER;
	private double maxOutput = 1.0;
	private double deadzone = 0.08;
	
	/**
	 * Creates a drive train, assuming there is one Controller for the left side
	 * and another one for the right side.
	 * 
	 * @param left  Left-side Controller
	 * @param right Right-side Controller
	 */
	public DriveAssist(IMercMotorController left, IMercMotorController right) {
		LEFT_CONTROLLER = left;
		RIGHT_CONTROLLER = right;
	}
	
	/**
	 * Sets the max output that can be taken in by the Controller.
	 * The value is in change/100ms
	 * 
	 * @param max Max output value
	 */
	public void setMaxOutput(double max) {
		maxOutput = max;
	}

	/**
	 * I'm just trying to get the code to compile sorry idk
	 * @see DriveAssist#setMaxOutput(double)
	 */
	public double getMaxOutput() {
		return maxOutput;
	}

	/**
	 * Single stick driving. This is done by using one axis for forwards/backwards,
	 * and another for turning right/left. This method allows direct input from any joystick
	 * value. This assumes that the control mode for the back has been properly setClawState.
	 * 
	 * @param moveVal      Value for forwards/backwards
	 * @param rotateVal    Value for rotation right/left
	 * @param squareInputs If set, decreases sensitivity at lower speeds
	 */
	public void arcadeDrive(double moveVal, double rotateVal, boolean squareInputs) {
		double leftPercent, rightPercent;
		
		// Clamp moveVal and rotateVal.
		// Assume a deadzone is already being applied to these values.
		moveVal = MercMath.clamp(moveVal, -1.0, 1.0);
		rotateVal = MercMath.clamp(rotateVal, -1.0, 1.0);
		
		// Square inputs, but maintain their signs.
		// This allows for more precise control at lower speeds,
		// but permits full power.
		if (squareInputs) {
			moveVal = Math.copySign(moveVal * moveVal, moveVal);
			rotateVal = Math.copySign(rotateVal * rotateVal, rotateVal);
		}
		
		// Set left and right motor speeds.
		if (moveVal > 0) {
			if (rotateVal > 0) {
				rightPercent = moveVal - rotateVal;
				leftPercent = Math.max(moveVal, rotateVal);
			} else {
				rightPercent = Math.max(moveVal, -rotateVal);
				leftPercent = moveVal + rotateVal;
			}
		} else {
			if (rotateVal > 0) {
				rightPercent = -Math.max(-moveVal, rotateVal);
				leftPercent = moveVal + rotateVal;
			} else {
				rightPercent = moveVal - rotateVal;
				leftPercent = -Math.max(-moveVal, -rotateVal);
			}
		}
		
		// Clamp motor percents
		leftPercent = MercMath.clamp(leftPercent, -1.0, 1.0);
		rightPercent = MercMath.clamp(rightPercent, -1.0, 1.0);

		//deadzone
		leftPercent = Math.abs(leftPercent) <= deadzone ? 0 : leftPercent;
		rightPercent = Math.abs(rightPercent) <= deadzone ? 0 : rightPercent;
		
		// Apply speeds to motors.
		// This assumes that the Controllers have been setClawState properly.
		LEFT_CONTROLLER.setSpeed(leftPercent * maxOutput);
		RIGHT_CONTROLLER.setSpeed(rightPercent * maxOutput);
	}
	
	/**
	 * Double stick driving. Each joystick has their y-axes setClawState to control one side
	 * of the robot, like a tank. his method allows direct input from any joystick
	 * value. This assumes that the control mode for the back has been properly setClawState.
	 * 
	 * @param leftVal  Value for left forwards/backwards
	 * @param rightVal Value for right forwards/backwards
	 */
	public void tankDrive(double leftVal, double rightVal) {

		// Apply speeds to motors.
		// This assumes that the Controllers have been setClawState properly.
		LEFT_CONTROLLER.setSpeed(leftVal * maxOutput);
		RIGHT_CONTROLLER.setSpeed(rightVal * maxOutput);
	}
}
