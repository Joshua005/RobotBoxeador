package uni.robot.base;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Collection;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Objeto que representa una ventana, en donde un o mas {@link GameObject} puede existir.
 * <p>
 * Este objeto debe de ser agregado dentro de un {@link GameLoop} para que funcione.
 * 
 * @author Fabio Kita
 *
 */
public abstract class GameWindow{
	private final GameFrame frame;
	private final GamePanel panel;
	
	private final ObjectManager objectManager;
	private final InputManager inputManager;
	private final DrawManager drawManager;
	
	private GameLoop gameLoop;
	private boolean created = false;
	
	public GameWindow() {
		this("", 640, 640);
	}
	
	public GameWindow(String title, int width, int height) {
		this.frame = new GameFrame();
		this.panel = new GamePanel(this);
		this.frame.getContentPane().add(this.panel);
		
		this.setTitle(title);
		this.setDimension(width, height);
		
		this.objectManager = new ObjectManager();
		this.inputManager = new InputManager(panel);
		this.drawManager = new DrawManager();
	}
	
	//CALLBACKS
	/**
	 * Metodo ejecutado una vez, cuando es agregado dentro de un GameLoop.
	 */
	public abstract void onCreate();
	
	
	/**
	 * Metodo ejecutado periodicamente al comienzo de un ciclo Update.
	 */
	public abstract void onUpdate();
	
	//PUBLIC METHODS
	//Object manipulation
	/**
	 * Metodo usado para agregar un nuevo objeto {@link GameObject} dentro de esta ventana.
	 * 
	 * @param object El objeto {@link GameObject} nuevo
	 */
	public void addObject(GameObject object) {
		this.objectManager.addObject(object);
		object.addToWindow(this);
		if(this.gameLoop != null) object.createIfNotCreated();
	}
	
	/**
	 * Metodo usado para remover el objeto {@link GameObject} de esta ventana.
	 * 
	 * @param object El objeto {@link GameObject} a quitar
	 */
	public void removeObject(GameObject object) {
		this.objectManager.removeObject(object);
		object.removeFromWindow();
		this.drawManager.addDrawRegion(object.getDrawRect());
	}
	
	/**
	 * Retorna la lista de objetos {@link GameObject} que existe dentro de esta ventana.
	 * 
	 * @return La lista de {@link GameObject}
	 */
	public Collection<GameObject> getObjects() {
		return this.objectManager.getObjects();
	}
	
	//Input Manipulation
	/**
	 * Retorna el objeto {@link InputManager}.
	 * 
	 * @return el objeto {@link InputManager}
	 */
	public InputManager getInputManager() {
		return this.inputManager;
	}
	
	//GETTER SETTERS
	/**
	 * Retorna el objeto {@link GameLoop} en donde esta ventana se encuentra.
	 * 
	 * @return El objeto {@link GameLoop}
	 */
	public GameLoop getGameLoop() {
		return gameLoop;
	}
	
	/**
	 * Retorna el objeto {@link JFrame} de esta ventana.
	 * 
	 * @return El objeto {@link JFrame}
	 */
	public JFrame getFrame() {
		return this.frame;
	}
	
	/**
	 * Retorna el objeto {@link JPanel} de esta ventana.
	 * 
	 * @return El objeto {@link JPanel}
	 */
	public JPanel getPanel() {
		return this.panel;
	}
	
	/**
	 * Retorna el titulo de esta ventana.
	 * 
	 * @return El titulo de esta ventana.
	 */
	public String getTitle() {
		return this.frame.getTitle();
	}
	
	/**
	 * Setea el titulo de esta ventana.
	 * 
	 * @param title El nuevo titulo.
	 */
	public void setTitle(String title) {
		this.frame.setTitle(title);
	}
	
	/**
	 * Retorna el ancho de esta ventana.
	 * 
	 * @return El ancho de esta ventana, en pixel.
	 */
	public int getWidth() {
		return this.panel.getWidth();
	}
	
	/**
	 * Retorna el alto de esta ventana.
	 * 
	 * @return El alto de esta ventana, en pixel.
	 */
	public int getHeight() {
		return this.panel.getHeight();
	}
	
	/**
	 * Setea la dimension de esta ventana.
	 * 
	 * @param width El ancho, en pixel.
	 * @param height El alto, en pixel.
	 */
	public void setDimension(int width, int height) {
		this.panel.setPreferredSize(new Dimension(width, height));
		this.frame.pack();
		this.frame.setLocationRelativeTo(null);
	}
	
	//DEFAULT METHODS
	/**
	 * Metodo usado para ejecutar los metodos onDraw todos los {@link GameObject}, por medio de 
	 * {@link DrawManager}.
	 * 
	 * @param g El objeto {@link Graphics2D} usado para dibujar.
	 */
	void drawObjects(Graphics2D g) {
		if(!this.created) return;
		this.drawManager.drawObjects(g, this.objectManager.getObjects());
	}
	
	/**
	 * Metodo usado por {@link GameLoop} para agregar la ventana.
	 * 
	 * @param gameLoop El objeto GameLoop en donde se agregara.
	 */
	void addToGameLoop(GameLoop gameLoop) {
		this.gameLoop = gameLoop;
		if(!created) {
			this.onCreate();
			
			for(GameObject object:getObjects())
				object.createIfNotCreated();
			
			this.frame.setVisible(true);
			
			this.created = true;
		}
	}
	
	/**
	 * Ejecuta lod metodos onUpdate() de todos los {@link GameObject}.
	 */
	void update() {
		onUpdate();
		
		this.inputManager.updateInputs();
		this.objectManager.updateObjects();
	}
	
	/**
	 * Ejecuta lod metodos onDraw() de todos los {@link GameObject}.
	 */
	void draw() {
		Rectangle drawRect = drawManager.executeOnDraw(objectManager.getObjects());
		if(DrawManager.DEBUG_DRAW_RECT) {
			panel.repaint();
		}else {
			if(drawRect != null) panel.repaint(drawRect);
		}
	}
}

class GameFrame extends JFrame{
	private static final long serialVersionUID = 1L;

	public GameFrame() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
	}
}

class GamePanel extends JPanel{
	private static final long serialVersionUID = 1L;
	
	private final GameWindow window;
	
	public GamePanel(GameWindow window) {
		this.window = window;
		
		this.setBackground(Color.white);
		this.setDoubleBuffered(true);
		this.setPreferredSize(new Dimension(640, 640));
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2d = (Graphics2D) g.create();
		this.window.drawObjects(g2d);
		g2d.dispose();
	}
}