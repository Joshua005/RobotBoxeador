package uni.robot.game.robotinstruction;

import uni.robot.game.RobotObject;

/**
 * Clase cuya funcion es manejar las instrucciones que recibe un {@link RobotObject}.
 * 
 * @author Fabio Kita
 *
 */
public abstract class RobotInstructionHandler {
	/**
	 * Metodo abstracto especializado en manejar instrucciones acciones. 
	 * 
	 * @param robot El robot que recibio la instruccion
	 * @param instruction Un String que se utiliza para determinar el tipo de instruccion que recibio
	 * @return Un {@link RobotState} que representa el estado nuevo que transicionara el robot acorde a la instruccion recibida. 
	 * O null si este handler no puede manejar la instruccion recibida. 
	 * 
	 * @see RobotState 
	 */
	public abstract RobotState handleActionInstruction(RobotObject robot, String instruction);
	
	/**
	 * Metodo abstracto especializado en manejar instrucciones sensores.
	 *  
	 * @param robot El robot que recibio la instruccion
	 * @param instruction Un String que se utiliza para determinar el tipo de instruccion que recibio
	 * @return Cualquier valor no-nulo que representa el valor retornado por el sensor, segun la instruccion recibida. 
	 * O null si este handler no puede manejar la instruccion recibida.
	 */
	public abstract Object handleSensorInstruction(RobotObject robot, String instruction);
}
