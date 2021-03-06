/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import frc.robot.RobotMap.*;
import frc.robot.util.ShuffleDash;
import frc.robot.commands.DriveDistance;
import frc.robot.commands.FullForward;
import frc.robot.commands.MoveOnPath;
/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
  private ShuffleDash shuffleDash;
  
  private Joystick rightJoystick, leftJoystick, gamepad;

  private JoystickButton left10, left7, right10;

  public OI() {
    leftJoystick = new Joystick(DS_USB.LEFT_STICK);
    rightJoystick = new Joystick(DS_USB.RIGHT_STICK);
    gamepad = new Joystick(DS_USB.GAMEPAD);

    shuffleDash = new ShuffleDash();

    left7 = new JoystickButton(leftJoystick, JOYSTICK_BUTTONS.BTN7);
    left10 = new JoystickButton(leftJoystick, JOYSTICK_BUTTONS.BTN10);
    right10 = new JoystickButton(rightJoystick, JOYSTICK_BUTTONS.BTN10);


    left10.whenPressed(new DriveDistance(100, .5));
    left7.whenPressed(new FullForward());

    right10.whenPressed(new MoveOnPath(""));
  }

  //COMPLETE:
  public double getX(int port) {
    //return 0;
    switch(port) {
      case DS_USB.LEFT_STICK:
        return leftJoystick.getX();
      case DS_USB.RIGHT_STICK:
        return rightJoystick.getX();
      default:
        return 0;
    }
  }

  //COMPLETE:
  public double getY(int port) {
    //return 0;
    switch(port) {
      case DS_USB.LEFT_STICK:
        return leftJoystick.getY();
      case DS_USB.RIGHT_STICK:
        return rightJoystick.getY();
      default:
        return 0;
    }
  }
}
