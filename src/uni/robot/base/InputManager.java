package uni.robot.base;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JPanel;

/**
 * Objeto que compone a {@link GameWindow}, encargado de manejar input de una ventana.
 * 
 * @author Fabio Kita
 *
 */
public class InputManager{
	public static final int MOUSE_LEFT = MouseEvent.BUTTON1;
	public static final int MOUSE_MIDDLE = MouseEvent.BUTTON2;
	public static final int MOUSE_RIGHT = MouseEvent.BUTTON3;
	
	private MouseInputListener mouseInputListener;
	
	private Map<Integer, Input> inputMap;
	
	public InputManager(JPanel panel) {
		//Init Mouse Listener
		mouseInputListener = new MouseInputListener();
		
		panel.addMouseListener(mouseInputListener);
		panel.addMouseMotionListener(mouseInputListener);
		
		//Init InputMap
		inputMap = new HashMap<>();
		
		inputMap.put(MOUSE_LEFT, new Input());
		inputMap.put(MOUSE_MIDDLE, new Input());
		inputMap.put(MOUSE_RIGHT, new Input());
	}

	/**
	 * Actualiza los estados de los inputs
	 */
	public void updateInputs() {
		//Update Mouse Input Map
		for(int mouseCode:inputMap.keySet()) {
			Input mouseInput = this.inputMap.get(mouseCode);
			mouseInput.previous = mouseInput.current;
			mouseInput.current = this.mouseInputListener.getInputState(mouseCode);
		}
	}
	
	//MOUSE INPUT METHODS
	/**
	 * Retorna la posicion x del mouse, en relacion a la esquina superior-izquierda de la ventana.
	 * 
	 * @return La posicion x del mouse, en pixeles.
	 */
	public int getMouseX() {
		return this.mouseInputListener.getMouseX();
	}
	
	/**
	 * Retorna la posicion y del mouse, en relacion a la esquina superior-izquierda de la ventana.
	 * 
	 * @return La posicion y del mouse, en pixeles.
	 */
	public int getMouseY() {
		return this.mouseInputListener.getMouseY();
	}
	
	/**
	 * Retorna true si un boton del mouse esta siendo presionado.
	 * 
	 * @param mouseCode El indice de boton del mouse.
	 * @return true si el boton esta siendo presionado.
	 */
	public boolean isMousePressed(int mouseCode) {
		Input mouseInput = this.inputMap.get(mouseCode);
		return mouseInput.current;
	}
	
	/**
	 * Retorna true solo en el instante en donde el boton del mouse cambio de no presionado a presionado.
	 * 
	 * @param mouseCode El indice de boton del mouse.
	 * @return true en el instance que el boton fue presionado.
	 */
	public boolean isMouseJustPressed(int mouseCode) {
		Input mouseInput = this.inputMap.get(mouseCode);
		return mouseInput.current && !mouseInput.previous;
	}
	
	/**
	 * Retorna true si un boton del mouse no esta siendo presionado.
	 * 
	 * @param mouseCode El indice de boton del mouse.
	 * @return true si un boton del mouse no esta siendo presionado.
	 */
	public boolean isMouseReleased(int mouseCode) {
		return !isMousePressed(mouseCode);
	}
	
	/**
	 * Retorna true solo en el instante en donde el boton del mouse cambio de presionado a no presionado.
	 * 
	 * @param mouseCode El indice de boton del mouse.
	 * @return true en el instance que el boton fue soltado.
	 */
	public boolean isMouseJustReleased(int mouseCode) {
		Input mouseInput = this.inputMap.get(mouseCode);
		return !mouseInput.current && mouseInput.previous;
	}
	
	//PRIVATE CLASSES
	/**
	 * Objeto que representa el estado actual y anterior de un input.
	 * 
	 * @author Fabio Kita
	 *
	 */
	private class Input{
		boolean current = false;
		boolean previous = false;
	}
	
	/**
	 * Obejto que implemente los Listener de Mouse.
	 * 
	 * @author Fabio Kita
	 *
	 */
	private class MouseInputListener implements MouseListener, MouseMotionListener{
		//Mouse Motion Fields
		private boolean insideWindow = false;
		private AtomicInteger mouseX = new AtomicInteger();
		private AtomicInteger mouseY = new AtomicInteger();
		
		//Mouse Input Fields
		private Map<Integer, Boolean> mouseInputMap;
		
		//Public Methods
		public int getMouseX() {
			return this.mouseX.get();
		}
		
		public int getMouseY() {
			return this.mouseY.get();
		}
		
		public boolean getInputState(int mouseCode) {
			return this.mouseInputMap.get(mouseCode);
		}
		
		//Mouse Motion Events
		public MouseInputListener() {
			mouseInputMap = new ConcurrentHashMap<>();
			
			//Init Inputs
			mouseInputMap.put(MOUSE_LEFT, false);
			mouseInputMap.put(MOUSE_MIDDLE, false);
			mouseInputMap.put(MOUSE_RIGHT, false);
		}
		
		@Override
		public void mouseDragged(MouseEvent e) {
			if(insideWindow) {
				mouseX.set(e.getX());
				mouseY.set(e.getY());
			}
			e.consume();
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			if(insideWindow) {
				mouseX.set(e.getX());
				mouseY.set(e.getY());
			}
			e.consume();
		}
		
		@Override
		public void mouseEntered(MouseEvent e) {
			insideWindow = true;
			e.consume();
		}

		@Override
		public void mouseExited(MouseEvent e) {
			insideWindow = false;
			e.consume();
		}
		
		//Mouse Input Events
		@Override
		public void mousePressed(MouseEvent e) {
			mouseInputMap.put(e.getButton(), true);
			e.consume();
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			mouseInputMap.put(e.getButton(), false);
			e.consume();
		}
		
		
		//UNUSED
		@Override
		public void mouseClicked(MouseEvent e) { }
		
	}

	//TODO IF NECESSARY: Add Key Listener
}