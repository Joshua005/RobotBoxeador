package uni.robot.base;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 * Objeto logico que existe dentro de un {@link GameWindow}.
 * <p>
 * Es resonsable de mantener estados y dibujar acordemente.
 * 
 * @author Fabio Kita
 *
 */
public abstract class GameObject {
	private static int nextId = 0;
	
	//El id del objeto, usado dentro de ObjectManager
	private final int id = nextId++;
	
	//El objeto ObjectDrawManager, responsable de los metodos de dibujos.
	private final ObjectDrawManager objectDrawManager = new ObjectDrawManager(this);
	
	//La ventana en donde se encuentra el objeto
	private GameWindow window = null;
	
	//Si el metodo onCreate fue ejecutado.
	private boolean created = false;
	
	//ABSTRACT METHODS
	/**
	 * Ejecutado una vez cuando es agregado dentro de una ventana. No se ejecuta de nuevo si es reagregado a otra
	 * o la misma ventana.
	 */
	public abstract void onCreate();
	
	/**
	 * Ejecutado cada ciclo Update. Solo se ejecuta si se encuetra dentro de una ventana.
	 */
	public abstract void onUpdate();
	
	/**
	 * Ejecutado cada ciclo Draw, solo si se cumplen las siguientes condiciones:
	 * <p>
	 * - El objeto se encuentra dentro de una ventana.
	 * <p>
	 * - El metodo redraw de este objeto fue ejecutado dentro del metodo onUpdate().
	 * <p>
	 * Usar exclusivamente para dibujar.
	 */
	public abstract void onDraw();
	
	/**
	 * Ejecutado una vez cuando el obejto es destruido con el metodo destroy().
	 */
	public abstract void onDestroy();
	
	
	//PUBLIC METHODS
	/**
	 * Retorna el objeto {@link GameWindow} donde el objeto se encuentra.
	 * @return El objeto {@link GameWindow}
	 */
	public GameWindow getWindow() {
		return window;
	}
	
	/**
	 * Retorna el objeto {@link InputManager} de la ventana actual.
	 * <p>
	 * Es lo mismo que hacer getWindow().getInputManager().
	 * 
	 * @return el obejto {@link InputManager}
	 * 
	 * @see InputManager
	 */
	public InputManager getInputManager() {
		return this.window.getInputManager();
	}
	
	/**
	 * Retorna el objeto {@link ResourceManager} del GameLoop actual.
	 * <p>
	 * Es lo mismo que hacer getWindow().getGameLoop().getResourceManager().
	 * 
	 * @return El objeto {@link ResourceManager}
	 * 
	 * @see ResourceManager
	 */
	public ResourceManager getResourceManager() {
		return this.window.getGameLoop().getResourceManager();
	}
	
	/**
	 * Obtiene el z-index del objeto. Cada vez mas alto el z-index, mas en frente se dibuja este objeto.
	 * @return El z-index de este objeto.
	 */
	public int getZIndex() {
		return objectDrawManager.getZIndex();
	}
	
	/**
	 * Setea el z-index de este objeto.
	 * @param zIndex el nuevo valor de z-index.
	 */
	public void setZIndex(int zIndex) {
		objectDrawManager.setZIndex(zIndex);
	}
	
	/**
	 * Destruye este objeto de la ventana, ejecutando el metodo onDestroy de este objeto.
	 */
	public void destroy() {
		if(this.window != null) {
			this.window.removeObject(this);
		}
		this.onDestroy();
	}
	
	/**
	 * Marca este objeto para redibujar en el siguiente ciclo Draw.
	 * <p>
	 * Se recomienda llamarlo dentro del metodo onUpdate(), cuando algun estado que afecta el 
	 * metodo onDraw().
	 * <p>
	 * Aunque se llame este metodo multiples veces antes del mismo ciclo, no se ejecutara 
	 * el metodo onDraw multiples veces.
	 */
	public void redraw() {
		this.objectDrawManager.setToRedraw(true);
	}
	
	/**
	 * Dibuja una imagen acorde a los parametros especificados.
	 * <p>
	 * Usar exclusivamente dentro del metodo onDraw().
	 * 
	 * @param image La imagen {@link BufferedImage} a dibujar.
	 * @param x La posicion x, en relacion a la esquina superior-izquierda de la ventana, en pixel. 
	 * @param y La posicion y, en relacion a la esquina superior-izquierda de la ventana, en pixel. 
	 * @param scaleX El escalado x de la imagen a dibujar. 
	 * @param scaleY El escalado y de la imagen a dibujar. 
	 */
	public void drawImage(BufferedImage image, int x, int y, double scaleX, double scaleY) {
		this.objectDrawManager.drawImage(image, x, y, scaleX, scaleY);
	}
	
	/**
	 * Dibuja una imagen acorde a los parametros especificados.
	 * <p>
	 * Usar exclusivamente dentro del metodo onDraw().
	 * 
	 * @param image La imagen {@link BufferedImage} a dibujar.
	 * @param x La posicion x, en relacion a la esquina superior-izquierda de la ventana, en pixel. 
	 * @param y La posicion y, en relacion a la esquina superior-izquierda de la ventana, en pixel.
	 */
	public void drawImage(BufferedImage image, int x, int y) {
		this.drawImage(image, x, y, 1.0, 1.0);
	}
	
	/**
	 * Dibuja el objeto {@link Sprite}, en el frame especificado.
	 * <p>
	 * Usar exclusivamente dentro del metodo onDraw().
	 * 
	 * @param sprite El objeto {@link Sprite} a dibujar
	 * @param frameIndex El indice de imagen a dibujar.
	 * @param x La posicion x, en relacion a la esquina superior-izquierda de la ventana, en pixel. 
	 * @param y La posicion y, en relacion a la esquina superior-izquierda de la ventana, en pixel. 
	 * @param scaleX El escalado x de la imagen a dibujar. 
	 * @param scaleY El escalado y de la imagen a dibujar. 
	 * 
	 * @see Sprite
	 */
	public void drawSprite(Sprite sprite, int frameIndex, int x, int y, double scaleX, double scaleY) {
		int xOffset = (int) Math.round(sprite.getOriginX()*scaleX);
		int yOffset = (int) Math.round(sprite.getOriginY()*scaleY);
		this.drawImage(sprite.getFrame(frameIndex), x - xOffset, y - yOffset, scaleX, scaleY);
	}
	
	/**
	 * Dibuja el objeto {@link Sprite}, en el frame especificado.
	 * <p>
	 * Usar exclusivamente dentro del metodo onDraw().
	 * 
	 * @param sprite El objeto {@link Sprite} a dibujar
	 * @param frameIndex El indice de imagen a dibujar.
	 * @param x La posicion x, en relacion a la esquina superior-izquierda de la ventana, en pixel. 
	 * @param y La posicion y, en relacion a la esquina superior-izquierda de la ventana, en pixel.
	 * 
	 * @see Sprite
	 */
	public void drawSprite(Sprite sprite, int frameIndex, int x, int y) {
		this.drawSprite(sprite, frameIndex, x, y, 1.0, 1.0);
	}
	
	//PROTECTED/DEFAULT METHODS
	/**
	 * Retorna el ID de este obejto, usado dentro del {@link ObjectManager}.
	 * <p>
	 * No es recomendado utilizar fuera de su uso original.
	 * 
	 * @return El ID de este objeto
	 * 
	 * @see ObjectManager
	 */
	protected int getId() {
		return id;
	}
	
	/**
	 * Metodo usado dentro de {@link GameWindow} para crear el objeto.
	 * <p>
	 * No es recomendado utilizar fuera de su uso original.
	 */
	protected void createIfNotCreated() {
		if(created) return;
		
		this.onCreate();
		created = true;
	}
	
	/**
	 * Metodo usado dentro de {@link GameWindow} para setear la variable de instancia window.
	 * <p>
	 * No es recomendado utilizar fuera de su uso original.
	 * 
	 * @param window the GameWindow object
	 */
	protected void addToWindow(GameWindow window) {
		this.window = window;
	}
	
	/**
	 * Metodo usado por GameWindow para resetear la variable de instancia window.
	 * <p>
	 * No es recomendado utilizar fuera de su uso original.
	 */
	protected void removeFromWindow() {
		this.window = null;
	}
	
	/**
	 * Metodo usado por GameWindow para conseguir la region dibujada por este objeto.
	 * <p>
	 * No es recomendado utilizar fuera de su uso original.
	 * 
	 * @return {@link Rectangle} que representa la region dibujada por este objeto.
	 */
	protected Rectangle getDrawRect() {
		return this.objectDrawManager.getDrawCallsRectangle();
	}
	
	/**
	 * Metodo usado por {@link ObjectManager} para ejecutar el metodo onUpdate().
	 * <p>
	 * No es recomendado utilizar fuera de su uso original.
	 */
	protected void update() {
		if(window == null) return;
		if(window.getGameLoop() == null) return;
		
		this.onUpdate();
	}
	
	/**
	 * Metodo usado por {@link DrawManager} para ejecutar el metodo onDraw().
	 * <p>
	 * El metodo retorna un {@link Rectangle} que representa una region a repintar, es decir, la combinacion de 
	 * la region afectada por por llamada onDraw() actual con la region afectada por la llamada onDraw() 
	 * anterior.
	 * <p>
	 * Este metodo retorna null si no requiere repintar. Esto puede ocurrir cuando este objeto no ejecuto el 
	 * metodo redraw(), o si el metodo onDraw() no dibujo nada.
	 * <p>
	 * No es recomendado utilizar fuera de su uso original.
	 * 
	 * @return el {@link Rectangle} que representa la region que requiere repintar. O null si no requiere 
	 * repintar.
	 */
	protected Rectangle executeOnDraw() {
		if(!this.objectDrawManager.getToRedraw()) return null;
		this.objectDrawManager.setToRedraw(false);
		
		Rectangle previousDrawCallRect = this.objectDrawManager.getDrawCallsRectangle();
		this.objectDrawManager.updateDrawCalls();
		Rectangle currentDrawCallRect = this.objectDrawManager.getDrawCallsRectangle();
		
		if(previousDrawCallRect == null) {
			return currentDrawCallRect;
		}else if(currentDrawCallRect == null) {
			return previousDrawCallRect;
		}else {
			return (Rectangle) previousDrawCallRect.createUnion(currentDrawCallRect);
		}
	}
	
	/**
	 * Metodo usado por {@link DrawManager} para controlar si la region intersecta el lo dibujado por el metodo 
	 * onDraw de este objeto. 
	 * <p>
	 * Este metodo solo funciona si el metodo executeOnDraw() fue ejecutado anteriormente. 
	 * <p>
	 * No es recomendado utilizar fuera de su uso original.
	 * 
	 * @param rect El {@link Rectangle} usado para el control.
	 * 
	 * @return Si este objeto intersecta con la region.
	 */
	protected boolean intersectsDrawRectangle(Rectangle rect) {
		Rectangle drawCallRect = this.objectDrawManager.getDrawCallsRectangle();
		if(drawCallRect == null) return false;
		return drawCallRect.intersects(rect);
	}
	
	/**
	 * Metodo usado por {@link DrawManager} para dibujar usando {@link Graphics2D}.
	 * <p>
	 * Si el parametro drawRect no es null, este solo repintara las cosas que intersecta con el rectangulo.
	 * <p>
	 * No es recomendado utilizar fuera de su uso original.
	 * 
	 * @param g El objeto {@link Graphics2D}.
	 * @param drawRect La region a repintar, o null si se requiere repintar todo el objeto;
	 */
	protected void draw(Graphics2D g, Rectangle drawRect) {
		this.objectDrawManager.draw(g, drawRect);
	}
}
