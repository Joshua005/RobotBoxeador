package uni.robot.base;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;


/**
 * Objeto que compone a GameObject responsable de la logica y optimizacion de dibujo.
 * 
 * Este objeto realiza y optimiza el pintado de la siguiente forma:<p>
 * <p>
 * 	- Durante el ciclo Draw, si toRedraw es true:<p>
 *		- Llama el metodo onDraw() del {@link GameObject} padre.<p>
 *		- El metodo onDraw() llama a los metodos draw de este objeto (ej. drawImage), lo que resulta en la 
 *		  cacheacion de un objeto DrawCall en la lista drawCallList.<p>
 *		- Retorna un rectangulo que representa la region que seria afectado por la
 *		   llamada onDraw(). Este rectangulo es usado para calcular la region a repintar total.<p>
 *<p>
 *	- Durante la repintada, en el metodo paintCompontent():<p>
 *		- Itera por todas las llamadas cacheadas y solo pinta las llamadas que son afectadas por la region a 
 *		  repintar total.<p>
 * 
 * @author Fabio Kita
 *
 */
class ObjectDrawManager{
	private final GameObject object;
	
	private int zIndex = 0;
	private boolean toRedraw = true;
	
	private final List<DrawCall> drawCallList = new ArrayList<>();
	private Rectangle drawCallListRect = null;
	
	public ObjectDrawManager(GameObject object) {
		this.object = object;
	}
	
	/**
	 * Cachea en drawCallList una llamada de dibujo de imagen, para ser dibujado posteriormente.
	 * 
	 * @param image la imagen a dibujar
	 * @param x La posicion x de la imagen a dibujar, en pixel
	 * @param y La posicion y de la imagen a dibujar, en pixel
	 * @param scaleX El escalado x de la imagen
	 * @param scaleY El escalado y de la image
	 * 
	 * @see ImageDrawCall
	 */
	public void drawImage(BufferedImage image, int x, int y, double scaleX, double scaleY) {
		this.drawCallList.add(new ImageDrawCall(image, x, y, scaleX, scaleY));
	}
	
	/**
	 * Retorna el valor de toRedraw
	 * 
	 * @return El valor de toRedraw
	 */
	public boolean getToRedraw() {
		return this.toRedraw;
	}
	
	/**
	 * Setea el valor de toRedraw
	 * 
	 * @param toRedraw El nuevo valor
	 */
	public void setToRedraw(boolean toRedraw) {
		this.toRedraw = toRedraw;
	}
	
	/**
	 * Retorna el valor de zIndex
	 * 
	 * @return El valor de zIndex
	 */
	public int getZIndex() {
		return this.zIndex;
	}
	
	/**
	 * Setea el valor de zIndex
	 * 
	 * @param zIndex El valor nuevo
	 */
	public void setZIndex(int zIndex) {
		this.zIndex = zIndex;
	}
	
	/**
	 * Llama el metodo onDraw() del {@link GameObject} padre y cachea todas las llamadas de dibujo realizada 
	 * dentro de dicho metodo, luego calcula y cachea la region que sera que requeriria repintar.
	 */
	public void updateDrawCalls() {
		//EMPTY THE PREVIOUS DRAW CALLS
		drawCallList.clear();
		
		//GET ALL THE CURRENT DRAW CALLS
		object.onDraw();
		
		//GET/DETERMINE THE CURRENT DRAW RECT
		Rectangle newDrawCallListRect = null;
		for(DrawCall drawCall:drawCallList) {
			Rectangle drawCallRect = drawCall.getDrawRegion();
			if(drawCallRect != null) {
				if(newDrawCallListRect == null) newDrawCallListRect = drawCallRect;
				else newDrawCallListRect.add(drawCallRect);
			}
		}
		
		//SET THE NEW DRAW RECT AS THE CURRENT ONE.
		drawCallListRect = newDrawCallListRect;
	}
	
	/**
	 * Retorna un {@link Rectangle} que representa la region afectada por el metodo onDraw() del 
	 * {@link GameObject} padre.
	 * 
	 * @return un {@link Rectangle} que representa la region a repintar
	 */
	public Rectangle getDrawCallsRectangle() {
		if(this.drawCallListRect == null) return null;
		return (Rectangle) this.drawCallListRect.clone();
	}
	
	/**
	 * Dibuja las llamadas cacheadas usando el {@link Graphics2D} pasado.
	 * 
	 * @param g el {@link Graphics2D} que usara para dibujar
	 * @param redrawRect si no es null, solo dibuja las llamadas que intersectan con el rectangulo pasado
	 */
	public void draw(Graphics2D g, Rectangle redrawRect) {
		for(DrawCall drawCall:drawCallList) {
			Rectangle drawCallRect = drawCall.getDrawRegion();
			if(drawCallRect != null && drawCallRect.intersects(redrawRect)) {
				drawCall.draw(g);
			}
		}
	}
	
	//PRIVATE CLASSES
	/**
	 * Interfaz que representa una llamada de dibujo generica.
	 * 
	 * @author Fabio Kita
	 */
	private interface DrawCall{
		/**
		 * Retorna la region que seria afectada por la llamada de dibujo. 
		 * 
		 * @return La region que seria afectada por la llamada de dibujo. 
		 */
		public Rectangle getDrawRegion();
		
		/**
		 * Ejecuta la llamada de dibujo.
		 * 
		 * @param g el objeto {@link Graphics2D} que usara para dibujar.
		 */
		public void draw(Graphics2D g);
	};
	
	
	/**
	 * Clase que representa una llamada de dibujo de imagen.
	 *
	 * @author Fabio Kita
	 */
	private class ImageDrawCall implements DrawCall{
		private int x;
		private int y;
		private int width;
		private int height;
		private BufferedImage image;
		
		public ImageDrawCall(BufferedImage image, int x, int y, double scaleX, double scaleY) {
			this.image = image;
			this.x = x;
			this.y = y;
			this.width = (int) Math.round(scaleX * image.getWidth());
			this.height = (int) Math.round(scaleY * image.getHeight());
		}

		@Override
		public Rectangle getDrawRegion() {
			return new Rectangle(x, y, width+1, height+1);
		}

		@Override
		public void draw(Graphics2D g) {
			g.drawImage(image, x, y, width, height, null);
		}
		
	}
	
	//TODO IF NECESSARY: Add other draw call
}
