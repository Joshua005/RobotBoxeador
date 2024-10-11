package uni.robot.base;

import java.awt.image.BufferedImage;

/**
 * Objeto que representa un Sprite, es decir, un conjunto de imagenes indexadas que representa una 
 * animacion por cuadra.
 * 
 * @author Fabio Kita
 *
 */
public class Sprite {
	private BufferedImage[] frames;
	private int originX = 0;
	private int originY = 0;
	
	/**
	 * Crea un sprite con la lista de imagenes pasadas.
	 * 
	 * @param frames la lista de {@link BufferedImage}
	 */
	public Sprite(BufferedImage[] frames) {
		this.frames = frames;
	}
	
	/**
	 * Crea un sprite con una sola imagen.
	 * 
	 * @param image un {@link BufferedImage}
	 */
	public Sprite(BufferedImage image) {
		this(image, image.getWidth(), image.getHeight());
	}
	
	/**
	 * Crea un Sprite a partir de una imagen, dividiendo dicha imagen segun grillas de dimension especificada.
	 * 
	 * @param spriteSheet Una imagen que representa un sprite sheet
	 * @param frameWidth el tamanho horizontal de la grilla
	 * @param frameHeight el tamanho vertical de la grilla
	 */
	public Sprite(BufferedImage spriteSheet, int frameWidth, int frameHeight) {
		this(spriteSheet, frameWidth, frameHeight, 0, -1);
	}
	
	/**
	 * Crea un Sprite a partir de una imagen, dividiendo dicha imagen segun grillas de dimension especificada.
	 * Luego solo guarda las sub-imagenes que se encuantran en el rango de indices epecificados, contando de 
	 * arriba para abajo, de izquierda a derecha.
	 * 
	 * @param spriteSheet Una imagen que representa un sprite sheet
	 * @param frameWidth el tamanho horizontal de la grilla
	 * @param frameHeight el tamanho vertical de la grilla
	 * @param startingFrame el indice de la sub-imagen inicial
	 * @param frameCount la cantidad de sub-imagenes sucesivas a guardar
	 */
	public Sprite(BufferedImage spriteSheet, int frameWidth, int frameHeight, int startingFrame, int frameCount) {
		//Calculate division
		int xDiv = spriteSheet.getWidth() / frameWidth;
		if(xDiv == 0) 
			throw new RuntimeException("The frame width cannot be bigger than the sprite sheet's width.");
		
		int yDiv = spriteSheet.getHeight() / frameHeight;
		if(yDiv == 0) 
			throw new RuntimeException("The frame height cannot be bigger than the sprite sheet's height.");
		
		//Set the frameCount as maximum if its an invalid number
		if(frameCount <= 0)
			frameCount = xDiv * yDiv - startingFrame;
		
		//Subdivide the sprite sheet image
		frames = new BufferedImage[frameCount];
		for(int i = 0; i < frameCount; i++) {
			int x = (i%xDiv)*frameWidth;
			int y = (i/xDiv)*frameHeight;
			frames[i] = spriteSheet.getSubimage(x, y, frameWidth, frameHeight);
		}
	}
	
	//PUBLIC METHODS
	/**
	 * Retorna la cantidad de imagenes que conforma este Sprite.
	 * 
	 * @return la cantidad de imagenes
	 */
	public int getFrameCount() {
		return this.frames.length;
	}
	
	/**
	 * Retorna la imagen con el indice especificado.
	 * 
	 * @param frameIndex el indice de la imagen.
	 * 
	 * @return una imagen {@link BufferedImage}
	 */
	public BufferedImage getFrame(int frameIndex) {
		return this.frames[frameIndex];
	}
	
	/**
	 * Setea el origen del sprite, es decir, el punto del sprite que se considerara como la posicion del
	 * sprite, a la hora de ser dibujado por un {@link GameObject} con el metodo drawSprite().
	 * 
	 * @param x la posicion x del punto origen, en pixeles
	 * @param y la posicion y del punto origen, en pixeles
	 */
	public void setOrigin(int x, int y) {
		this.originX = x;
		this.originY = y;
	}
	
	/**
	 * Setea el centro del sprite como el origen. Si las sub-imagenes tiene diferente tamanho, utiliza como
	 * referencia la primera sub-imagen.
	 */
	public void setCenterAsOrigin() {
		setOrigin(getWidth()/2, getHeight()/2);
	}
	
	/**
	 * Retorna La posicion x del punto de origen.
	 * 
	 * @return La posicion x del punto de origen.
	 */
	public int getOriginX() {
		return originX;
	}
	
	/**
	 * Retorna La posicion y del punto de origen.
	 * 
	 * @return La posicion y del punto de origen.
	 */
	public int getOriginY() {
		return originY;
	}
	
	/**
	 * Consigue el ancho del sprite.  Si las sub-imagenes tiene diferente tamanho, utiliza como
	 * referencia la primera sub-imagen.
	 * @return el ancho del sprite
	 */
	public int getWidth() {
		return this.getFrame(0).getWidth();
	}
	
	/**
	 * Consigue el alto del sprite.  Si las sub-imagenes tiene diferente tamanho, utiliza como
	 * referencia la primera sub-imagen.
	 * @return el alto del sprite
	 */
	public int getHeight() {
		return this.getFrame(0).getHeight();
	}
}
