package uni.robot.base;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Objeto responsable de dibujar y obtimizar dibujo dentro de {@link GameWindow}
 * 
 * @author Fabio Kita
 *
 */
class DrawManager {
	private static final int PADDING = 1;
	private static final int DRAW_GRID_SIZE = 32;
	
	//Acumulador de region de dinujos
	private Rectangle accDrawRect;
	
	//DEBUG
	public static final boolean DEBUG_DRAW_RECT = false;
	private Rectangle debugDrawRect;
	private Rectangle debugGridRect;
	
	public DrawManager() {
		this.accDrawRect = null;
	}
	
	/**
	 * Agrega a accDrawRect un rectangulo que representa la region dentro de la ventana a redibujar.
	 * 
	 * @param drawRect la region que requiere redibujar
	 */
	public void addDrawRegion(Rectangle drawRect) {
		if(drawRect == null) return;
		
		if(this.accDrawRect == null) this.accDrawRect = drawRect;
		else this.accDrawRect.add(drawRect);
	}
	
	/**
	 * Ejecuta los metodos onDraw() de todos los {@link GameObject} y agrega las regiones que requiere
	 * redibujo dentro de accDrawRect.
	 * <p>
	 * Luego, retorna accDrawRect y lo resetea a null.
	 * <p>
	 * @param objects La lista de {@link GameObject}
	 * @return la region dentro de la ventana que requiere redibujo
	 */
	public Rectangle executeOnDraw(Collection<GameObject> objects) {
		Rectangle retRect;
		
		//Add all draw region into accDrawRect;
		for(GameObject object:objects) {
			this.addDrawRegion(object.executeOnDraw());
		}
		
		//Save and reset accDrawRect's value
		retRect = accDrawRect;
		accDrawRect = null;
		
		//DEBUG
		if(DEBUG_DRAW_RECT) {
			debugDrawRect = null;
			debugGridRect = null;
		}
		
		//Return null if null;
		if(retRect == null) 
			return null;
		
		//Add padding to prevent weird artifacts
		retRect.grow(PADDING, PADDING);
		
		//DEBUG
		if(DEBUG_DRAW_RECT) debugDrawRect = (Rectangle) retRect.clone();
		
		//Fit the region into a grid to prevent weird artifacts
		int topLeftX = (retRect.x / DRAW_GRID_SIZE) * DRAW_GRID_SIZE;
		int topLeftY = (retRect.y / DRAW_GRID_SIZE) * DRAW_GRID_SIZE;
		int bottomRightX = ((retRect.x+retRect.width) / DRAW_GRID_SIZE + 1) * DRAW_GRID_SIZE;
		int bottomRightY = ((retRect.y+retRect.height) / DRAW_GRID_SIZE + 1) * DRAW_GRID_SIZE;
		
		retRect.setBounds(topLeftX, topLeftY, bottomRightX-topLeftX, bottomRightY-topLeftY);
		
		//DEBUG
		if(DEBUG_DRAW_RECT) debugGridRect = (Rectangle) retRect.clone();
		
		return retRect;
	}
	
	/**
	 * Metodo que usa un objeto {@link Graphics2D} para dibujar los objetos.
	 * 
	 * @param g El obejto {@link Graphics2D}
	 * @param objects la lista de objetos {@link GameObject}
	 */
	public void drawObjects(Graphics2D g, Collection<GameObject> objects) {
		//Get the drawing region
		Rectangle drawRect = g.getClipBounds();
		
		//DEBUG
		if(DEBUG_DRAW_RECT) drawRect = debugGridRect;
		
		if(drawRect == null) return;
		
		//Initialize the z-index:GameObject map
		Map<Integer, List<GameObject>> zIndexMap = new TreeMap<Integer, List<GameObject>>();
		
		//Add affected object to the map
		for(GameObject object:objects) {
			if(object.intersectsDrawRectangle(drawRect)) {
				int objectZIndex = object.getZIndex();
				if(!zIndexMap.containsKey(objectZIndex))
					zIndexMap.put(objectZIndex, new ArrayList<>());
				zIndexMap.get(objectZIndex).add(object);
			}
		}
		
		//Draw affected object in order of their z-index
		for(List<GameObject> objectBatch:zIndexMap.values()) {
			for(GameObject object:objectBatch) {
				object.draw(g, drawRect);
			}
		}
		
		//DEBUG
		if(DEBUG_DRAW_RECT) g.drawRect(debugDrawRect.x, debugDrawRect.y, debugDrawRect.width, debugDrawRect.height);
		if(DEBUG_DRAW_RECT) g.drawRect(debugGridRect.x, debugGridRect.y, debugGridRect.width, debugGridRect.height);
	}
	
}
