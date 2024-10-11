package uni.robot.base;

/**
 * Objeto que controla el ciclo de juego, es decir, es responsable de actualizar estados (ciclo Update) y 
 * repintar las ventanas (ciclo Draw) periodicamente.
 * 
 * @author Fabio Kita
 *
 */
public abstract class GameLoop implements Runnable{
	private final static int THREAD_PRIORITY = 4; //This thread priority
	
	private static final double FPS_TO_INTERVAL = 1000.0;
	
	private int updatePerSecond = 60;
	private int drawPerSecond = 60;
	
	private double updateInterval = FPS_TO_INTERVAL/updatePerSecond;
	private double drawInterval = FPS_TO_INTERVAL/drawPerSecond;
	
	/**
	 * Comienza el ciclo de juego en un hilo nuevo.
	 */
	public void startGameLoop() {
		Thread t = new Thread(this);
		t.setPriority(THREAD_PRIORITY);
		t.start();
	}
	
	@Override
	public void run() {
		onCreate();
		handleLoop();
	}
	
	//WINDOW MANAGER
	private final WindowManager windowManager = new WindowManager();
	
	/**
	 * Agrega una nueva ventana al ciclo de juego.
	 * 
	 * @param window la ventana {@link GameWindow}
	 */
	public void addWindow(GameWindow window) {
		this.windowManager.addWindow(window);
		window.addToGameLoop(this);
	}
	
	//RESOURCE MANAGER
	private final ResourceManager resourceManager = new ResourceManager();
	
	/**
	 * Retorna el manejador de recursos.
	 * 
	 * @return el objeto {@link ResourceManager}.
	 * 
	 * @see ResourceManager
	 */
	public ResourceManager getResourceManager() {
		return this.resourceManager;
	}
	
	//ABSTRACT METHODS
	/**
	 * Metodo que se ejecuta cuando comienza el ciclo de juego.
	 */
	public abstract void onCreate();
	
	/**
	 * Metodo que se ejecuta periodicamente al comienzo de cada ciclo Update.
	 */
	public abstract void onUpdate();
	
	//PUBLIC METHODS
	/**
	 * Retorna la cantidad de ciclo Update que realiza por segundo.
	 * 
	 * @return la cantidad de ciclo Update por segundo
	 */
	public int getUpdatePerSecond() {
		return updatePerSecond;
	}

	/**
	 * Setea la cantidad de ciclo Update que realiza por segundo.
	 * 
	 * @param updatePerSecond el valor nuevo, en segundos
	 */
	public void setUpdatePerSecond(int updatePerSecond) {
		this.updatePerSecond = updatePerSecond;
		this.updateInterval = FPS_TO_INTERVAL / this.updatePerSecond;
	}

	/**
	 * Retorna la cantidad de ciclo Draw que realiza por segundo.
	 * 
	 * @return la cantidad de ciclo Draw por segundo
	 */
	public int getDrawPerSecond() {
		return drawPerSecond;
	}
	
	/**
	 * Setea la cantidad de ciclo Draw que realiza por segundo.
	 * 
	 * @param drawPerSecond el valor nuevo, en segundos
	 */
	public void setDrawPerSecond(int drawPerSecond) {
		this.drawPerSecond = drawPerSecond;
		this.drawInterval = FPS_TO_INTERVAL / this.drawPerSecond;
	}
	
	/**
	 * Metodo usado por el hilo creado en startGameLoop para correr el ciclo de juego.
	 */
	public void handleLoop() {
		double nextUpdate = System.currentTimeMillis() + updateInterval;
		double nextDraw = System.currentTimeMillis() + drawInterval;
		
		for(;;) {
			double current = System.currentTimeMillis();
			
			if(Math.round(nextUpdate - current) <= 0) {
				this.update();
				while(Math.round(nextUpdate - current) <= 0) {
					nextUpdate += updateInterval;
				}
			}
			
			if(Math.round(nextDraw - current) <= 0) {
				this.draw();
				while(Math.round(nextDraw - current) <= 0) {
					nextDraw +=  drawInterval;
				}
			}
			
			sleepMillis(Math.min(nextUpdate, nextDraw) - current);
		}
	}
	
	/**
	 * Metodo usado dentro de handleLoop para ejecutar un ciclo Update.
	 */
	public void update() {
		onUpdate();
		this.windowManager.updateWindows();
		Thread.yield();
	}

	/**
	 * Metodo usado dentro de handleLoop para ejecutar un ciclo Draw.
	 */
	public void draw() {
		this.windowManager.drawWindows();
		Thread.yield();
	}
	
	//PRIVATE METHODS
	/**
	 * Pausa el hilo durante el tiempo especificado.
	 * 
	 * @param millis el tiempo a pausar, en milisegundos
	 */
	private void sleepMillis(double millis) {
		try {
			Thread.sleep(Math.round(millis));
		}catch(InterruptedException e) {};
	}
}
