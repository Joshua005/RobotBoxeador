package uni.robot.game.robotinstruction;

import uni.robot.game.Direction;
import uni.robot.game.RobotLoop;
import uni.robot.game.RobotObject;

/**
 * Estado quieto, esperando una instruccion. 
 * <p>
 * Aqui es donde el  robot lee, procesa una instruccion y cambia de estado acordemente.
 * 
 * @author Fabio Kita
 *
 */
public class IdleState extends RobotState{
	private static final int IMAGE_SCALE = 2;
	
	public IdleState(RobotObject robot) {
		super(robot);
	}
	
	@Override
	public RobotState handleUpdate() {
		//RETURN THIS IF THE GAME IS PAUSED
		if(getRobot().getRobotLoop().getPlayMode() == RobotLoop.PAUSED) return this;
		
		//READ INSTRUCTION
		String message = (String) readInstruction();
		if(message == null) return this;
		
		//HANDLE INSTRUCTION
		for(RobotInstructionHandler instructionHandler:RobotObject.getInstructionHandlerList()) {
			//HANDLE ACTION
			RobotState newState = instructionHandler.handleActionInstruction(getRobot(), message);
			if(newState != null) return newState;
			
			//HANDLE SENSOR
			Object response = instructionHandler.handleSensorInstruction(getRobot(), message);
			if(response != null) {
				finishInstruction(response);
				return this;
			}
		}
		
		//If it couldn't handle the instruction
		System.err.println("Received Invalid Instruction. This robot will not act.");
		finishInstruction(null);
		return this;
	}

	@Override
	public void handleDraw() {
		char dirChar = Direction.getDirectionChar(getRobot().getDirection());
		String spriteName = "Walk" + dirChar;
		
		int x = getRobot().getColumnX();
		int y = getRobot().getRowY();
		
		getRobot().drawSprite(getRobot().getSprite(spriteName), 0, x, y, IMAGE_SCALE, IMAGE_SCALE);
	}
	
}
