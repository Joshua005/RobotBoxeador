package uni.robot.game.robotinstruction;

import uni.robot.game.RobotLoop;
import uni.robot.game.RobotObject;

/**
 * Estado generico para la maquina de estado de robot.
 * 
 * @author Fabio Kita
 *
 */
public abstract class RobotState{
	private RobotObject robot;
	
	public RobotState(RobotObject robot) {
		this.robot = robot;
	}
	
	/**
	 * Consigue el objeto {@link RobotObject}
	 * 
	 * @return el objeto {@link RobotObject}
	 */
	protected RobotObject getRobot() {
		return robot;
	}
	
	/**
	 * Retorna si el juego esta en modo avance rapido
	 * @return
	 */
	protected boolean isFastForward() {
		int currentPlayMode = getRobot().getRobotLoop().getPlayMode();
		return currentPlayMode == RobotLoop.FAST_FAST_FORWARD;
	}
	
	/**
	 * Lee primera instruccion mandada al robot. Es lo mismo que escribir:
	 * getRobot().getInstructionManager().readInstruccion(). 
	 * 
	 * @return el mensaje de la intruccion.
	 */
	protected Object readInstruction() {
		return robot.getInstructionManager().readInstruction();
	}
	
	/**
	 * Termina la instruccion mandada al robot. Es lo mismo que escribir:
	 * getRobot().getInstructionManager().finishInstruction(). 
	 * 
	 */
	protected void finishInstruction() {
		robot.getInstructionManager().finishInstruction();
	}
	

	/**
	 * Termina la instruccion mandada al robot. Es lo mismo que escribir:
	 * getRobot().getInstructionManager().finishInstruction(). 
	 * 
	 * @param response El mensaje de respuesta
	 */
	protected void finishInstruction(Object response) {
		robot.getInstructionManager().finishInstruction(response);
	}
	
	/**
	 * Metodo que se ejecuta periodicamente en cada ciclo Update, cada vez que se ejecuta el metodo 
	 * onUpdate() del objeto {@link RobotObject}.
	 * <p>
	 * Aqui es donde va toda la logica de un estado.
	 * <p>
	 * Este metodo debe retornar: un estado nuevo, si se quiere que el robot cambie de estado; o this, si se 
	 * quiere que el robot permanesca en el mismo estado hasta el siguiente ciclo Update.
	 * 
	 * @return 
	 */
	public abstract RobotState handleUpdate();
	
	/**
	 * Metodo que se ejecuta periodicamente en cada ciclo Draw, cada vez que se ejecuta el metodo 
	 * onDraw() del objeto {@link RobotObject}. 
	 *<p>
	 * Aqui es donde se pone toda la logica de dibujo de un estado.
	 */
	public abstract void handleDraw();
	
	/**
	 * Metodo que se ejecuta una vez cuando el robot cambia de un estado a este estado.
	 * 
	 * @param from el estado anterior a este
	 */
	public void onEnter(RobotState from) {}
	
	/**
	 * Metodo que se ejecuta una vez cuando el robot cambia de este estado a otro.
	 * 
	 * @param to el estado siguiente a este.
	 */
	public void onExit(RobotState to) {}
}
