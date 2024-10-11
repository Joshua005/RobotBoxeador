package uni.robot.game.speedpanel;

import uni.robot.game.RobotLoop;

/**
 * EL boton que representa el modo avance normal
 * @author Fabio Kita
 *
 */
class PlayButton extends BaseButton{

	@Override
	public void onCreate() {
		this.setParameters(RobotLoop.ASSETS_PATH + "buttons/PlayButton.png", RobotLoop.NORMAL);
	}

}
