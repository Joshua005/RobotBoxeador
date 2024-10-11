package uni.robot.game.speedpanel;

import uni.robot.game.RobotLoop;

/**
 * Boton que representa el modo pausado
 * 
 * @author Fabio Kita
 *
 */
class PauseButton extends BaseButton{
	
	@Override
	public void onCreate() {
		this.setParameters(RobotLoop.ASSETS_PATH + "buttons/PauseButton.png", RobotLoop.PAUSED);
	}

}
