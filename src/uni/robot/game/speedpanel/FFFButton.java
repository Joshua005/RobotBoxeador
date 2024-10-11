package uni.robot.game.speedpanel;

import uni.robot.game.RobotLoop;

/**
 * Boton que representa el modo de avance mas rapido
 * @author Fabio Kita
 *
 */
class FFFButton extends BaseButton{
	
	@Override
	public void onCreate() {
		this.setParameters(RobotLoop.ASSETS_PATH + "buttons/FFFButton.png", RobotLoop.FAST_FAST_FORWARD);
	}

}
