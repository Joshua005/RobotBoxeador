package uni.robot.base;

import java.awt.image.BufferedImage;

/**
 * Objeto responsable de facilitar la implementacion de animacion de {@link Sprite}.
 * 
 * @author Fabio Kita
 */
public class SpritePlayer {
	private Sprite sprite;
	private int currentFrameIndex;
	private int previousFrameIndex;
	private double speed;
	private double frameAcc;
	
	/**
	 * Se crea un SpritePlayer con el sprite, velocidad y indice incial especificado.
	 * 
	 * @param sprite el objeto {@link Sprite}
	 * @param speed la velocidad de la animacion, en cuadras por ciclo Update.
	 * @param initialIndex el indice de la imagen inicial
	 */
	public SpritePlayer(Sprite sprite, double speed, int initialIndex) {
		this.sprite = sprite;
		this.speed = speed;
		this.currentFrameIndex = initialIndex;
		this.previousFrameIndex = initialIndex;
		this.frameAcc = initialIndex;
	}
	
	/**
	 * Se crea un SpritePlayer con el sprite y velocidad especificado.
	 * 
	 * @param sprite el objeto {@link Sprite}
	 * @param speed la velocidad de la animacion, en cuadras por ciclo Update.
	 */
	public SpritePlayer(Sprite sprite, double speed) {
		this(sprite, speed, 0);
	}
	
	/**
	 * Se crea un SpritePlayer con el sprite especificado.
	 * 
	 * @param sprite el objeto {@link Sprite}
	 */
	public SpritePlayer(Sprite sprite) {
		this(sprite, 1);
	}
	
	/**
	 * Se crea un SpritePlayer sin ningun sprite.
	 */
	public SpritePlayer() {
		this(null);
	}

	//GETTERS/SETTERS
	/**
	 * Consigue el sprite de este SpritePlayer.
	 * 
	 * @return el sprite de este SpritePlayer.
	 */
	public Sprite getSprite() {
		return sprite;
	}

	/**
	 * Setea el sprite a animar, seteando el indice frame actual a 0.
	 * 
	 * @param sprite el sprite nuevo
	 */
	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
		this.setCurrentFrameIndex(0);
		this.previousFrameIndex = -1;
	}

	/**
	 * Consigue el indice del frame actual del sprite.
	 * 
	 * @return  el frame actual.
	 */
	public int getCurrentFrameIndex() {
		return currentFrameIndex;
	}

	/**
	 * Setea el indice actual del sprite.
	 * 
	 * @param currentFrameIndex el nuevo indice actual.
	 */
	public void setCurrentFrameIndex(int currentFrameIndex) {
		this.currentFrameIndex = currentFrameIndex;
		this.frameAcc = currentFrameIndex;
	}

	/**
	 * Consigue la velocidad de animacion.
	 * 
	 * @return La velocidad de animacion.
	 */
	public double getSpeed() {
		return speed;
	}

	/**
	 * Setea la velocidad actual de animacion. Una velocidad de 1, hace que la imagen cambie por cada ciclo 
	 * Update. Un valor de 0 hace que la animacion pause. Un valor mayor de 1 hace que la animacion saltee
	 * algunos frames. Un valor negativo hace que se anime en reversa.
	 * 
	 * 
	 * @param speed La nueva velocidad de animacion.
	 */
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	
	//PUBLIC METHODS
	/**
	 * Consigue el frame acutal.
	 * 
	 * @return el {@link BufferedImage} del frame actual.
 	 */
	public BufferedImage getCurrentFrame() {
		if(sprite == null) return null;
		return sprite.getFrame(currentFrameIndex);
	}
	
	/**
	 * Si en este ciclo Update el la imagen cambio, comparando con la anterior.
	 * @return si la imagen cambio
	 */
	public boolean frameChanged() {
		return previousFrameIndex != currentFrameIndex;
	}
	
	/**
	 * Metodo que tiene que ser llamado durante el ciclo Update, preferiblemente dentro del metodo onUpdate()
	 * de {@link GameObject}, para que la logica de animacion funcione.
	 */
	public void update() {
		if(sprite == null) return;
		
		previousFrameIndex = currentFrameIndex;
		
		frameAcc += speed;
		if(frameAcc >= sprite.getFrameCount()) 
			frameAcc -= sprite.getFrameCount();
		else if(frameAcc < 0) 
			frameAcc += sprite.getFrameCount();
		
		currentFrameIndex = (int) Math.floor(frameAcc);
	}
}
