package uni.robot.game.speedpanel;

import java.awt.Rectangle;

import uni.robot.base.GameObject;
import uni.robot.base.InputManager;
import uni.robot.base.Sprite;
import uni.robot.game.RobotLoop;

/**
 * Objeto que es base de los botones del panel de control
 * 
 * @author Fabio Kita
 *
 */
abstract class BaseButton extends GameObject {
	private static final int SUBSPRITE_SIZE = 48;
	
	private Sprite sprite;
	private int playMode;
	
	private int x;
	private int y;
	private int width;
	private int height;
	
	private static final int NORMAL = 0;
	private static final int HOVER = 1;
	private static final int ACTIVE = 2;
	private static final int PRESSED = 3;
	
	private int frameIndex = 0;

	@Override
	public void onUpdate() {
		if(!isPressed()) {
			if(isCursorOnButton()) {
				if(getInputManager().isMouseJustPressed(InputManager.MOUSE_LEFT)) {
					setFrameIndex(ACTIVE);
					this.getRobotLoop().setPlayMode(playMode);
				}else {
					setFrameIndex(HOVER);
				}
			}else {
				setFrameIndex(NORMAL);
			}
		}else {
			if(isCursorOnButton() && getInputManager().isMousePressed(InputManager.MOUSE_LEFT)) {
				setFrameIndex(ACTIVE);
			}else {
				setFrameIndex(PRESSED);
			}
		}
	}

	@Override
	public void onDraw() {
		this.drawSprite(sprite, frameIndex, x, y, getXScale(), getYScale());
	}

	@Override
	public void onDestroy() {}
	
	/**
	 * Setea la posicion dentro de la ventana
	 * 
	 * @param x posicion x, en pixeles
	 * @param y posicion y, en pixeles
	 */
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Setea la dimension del boton
	 * 
	 * @param w
	 * @param h
	 */
	public void setDimension(int w, int h) {
		this.width = w;
		this.height = h;
	}
	
	/**
	 * Setea los parametros necesarios del boton
	 * 
	 * @param path el nombre del archivo de la imagen que se usara
	 * @param playMode el modo de juego que representa el boton
	 */
	protected void setParameters(String path, int playMode) {
		this.sprite = new Sprite(getResourceManager().loadImage(path), 
				SUBSPRITE_SIZE, SUBSPRITE_SIZE);
		this.playMode = playMode;
	}
	
	/**
	 * Retorna si el boton es presionado, es decir el modo de juego actual es el modo de juego que el boton 
	 * representa
	 * 
	 * @return si el boton es presionado
	 */
	private boolean isPressed() {
		return getRobotLoop().getPlayMode() == playMode;
	}
	
	/**
	 * Retorna el {@link RobotLoop} que este objeto pertenece
	 * 
	 * @return el objeto {@link RobotLoop}
	 */
	private RobotLoop getRobotLoop() {
		return (RobotLoop) getWindow().getGameLoop();
	}
	
	/**
	 * Retorna si el mouse se encuentra ensima de este boton, 
	 * @return
	 */
	private boolean isCursorOnButton() {
		Rectangle rect = new Rectangle(x, y, 
				sprite.getWidth()*getXScale(), sprite.getHeight()*getYScale());
		int mouseX = getInputManager().getMouseX();
		int mouseY = getInputManager().getMouseY();
		return rect.contains(mouseX, mouseY);
	}
	
	/**
	 * Setea la imagen actual que se mostrara
	 * 
	 * @param frameIndex el indice de la imagen
	 */
	private void setFrameIndex(int frameIndex) {
		if(frameIndex != this.frameIndex) {
			this.frameIndex = frameIndex;
			this.redraw();
		}
	}
	
	/**
	 * Retorna el escalado en x, para que la imagen del boton respete su dimension
	 * @return el escalado en x
	 */
	private int getXScale() {
		return this.width/SUBSPRITE_SIZE;
	}
	
	/**
	 * Retorna el escalado en y, para que la imagen del boton respete su dimension
	 * @return el escalado en y
	 */
	private int getYScale() {
		return this.height/SUBSPRITE_SIZE;
	}
}
