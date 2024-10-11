package uni.robot.game.robotinstruction;

import uni.robot.game.RobotObject;

/**
 * El {@link RobotInstructionHandler} que maneja todas las instrucciones bases (Avanzar, girar, etc.). 
 * @author Fabio Kita
 *
 */
public class BaseInstructionHandler extends RobotInstructionHandler {
	@Override
	public RobotState handleActionInstruction(RobotObject robot, String instruction) {
		switch(instruction) {
			case "WALK": return new WalkState(robot);
			case "TURN_LEFT": return new TurnLeftState(robot);
			case "TURN_RIGHT": return new TurnRightState(robot);
			case "PUT_CONE": return new PutConeState(robot);
			case "REMOVE_CONE": return new RemoveConeState(robot);
		}
		return null;
	}
	

	@Override
	public Object handleSensorInstruction(RobotObject robot, String instruction) {
		//SENSORS
		switch(instruction) {
			case "GET_ROW": return robot.getRow();
			case "GET_COLUMN": return robot.getColumn();
			case "GET_DIRECTION": return robot.getDirection();
			case "IS_CONE": return !robot.getConeInPosition().isEmpty();
			case "GET_CONE_COUNT": return robot.getConeCount();
			case "GET_CONE_CAPACITY": return robot.getCapacity();
			case "CAN_MOVE_FORWARD": return robot.canMoveForward();
			case "GET_WORLD": return robot.getWorld().getMundo();
		}
		return null;
	}
}
