package uni.robot.game.speedpanel;

import uni.robot.base.GameWindow;

/**
 * Representa la ventana del panel de control
 * 
 * @author Fabio Kita
 *
 */
public class SpeedPanel extends GameWindow{
	@Override
	public void onCreate() {
		final int PAD_X = 32;
		final int PAD_Y = 10;
		
		final int WIDTH = 96;
		final int HEIGHT = 96;
		
		final BaseButton[] buttons = {
				new PauseButton(),
				new PlayButton(),
				new FFButton(),
				new FFFButton()
			};
		
		//Set Window parameter
		setTitle("Panel de Velocidad");
		setDimension(PAD_X*2 + WIDTH*buttons.length, PAD_Y*2+HEIGHT);
		this.getFrame().setLocation(0, 0);
		
		//Set Buttons
		for(int i = 0; i < buttons.length; i++) {
			BaseButton button = buttons[i];
			this.addObject(button);
			button.setPosition(PAD_X + WIDTH*i, PAD_Y);
			button.setDimension(WIDTH, HEIGHT);
		}
	}

	@Override
	public void onUpdate() {}
	
	
}
