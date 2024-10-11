package uni.robot.game.speedpanel;

import uni.robot.game.RobotLoop;

/**
 * Boton que represneta el modo avance rapido
 * @author Fabio Kita
 *
 */
class FFButton extends BaseButton{

	@Override
	public void onCreate() {
		this.setParameters(RobotLoop.ASSETS_PATH + "buttons/FFButton.png", RobotLoop.FAST_FORWARD);
	}

}
