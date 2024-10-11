package uni.robot.base;

import java.util.ArrayList;
import java.util.List;

/**
 * Objeto que compone a {@link GameLoop}, responsable de almacenar y manejar ventanas.
 * 
 * @author Fabio Kita
 *
 */
class WindowManager {
	private List<GameWindow> windowList;
	
	public WindowManager() {
		this.windowList = new ArrayList<>();
	}
	
	/**
	 * Agrega una ventana nueva
	 * 
	 * @param window la ventana a agregar
	 */
	public void addWindow(GameWindow window) {
		this.windowList.add(window);
	}
	
	/**
	 * Consigue una ventana segun el indice.
	 * 
	 * @param index el indice de la ventana
	 * @return la ventana
	 */
	public GameWindow getGameWindow(int index) {
		return this.windowList.get(index);
	}
	
	/**
	 * Ejecuta el metodo update() de todos los {@link GameWindow}
	 */
	public void updateWindows() {
		for(GameWindow window:windowList) {
			window.update();
		}
	}
	
	/**
	 * Ejecuta el metodo draw() de todos los {@link GameWindow}
	 */
	public void drawWindows() {
		for(GameWindow window:windowList) {
			window.draw();
		}
	}
}
