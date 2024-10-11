package uni.robot.game;

import uni.robot.base.GameLoop;
import uni.robot.base.SyncInstructionManager;
import uni.robot.game.speedpanel.SpeedPanel;

/**
 * Repesenta un ciclo de juego, especifico para el juego robot.
 * 
 * @author Fabio Kita
 *
 */
public class RobotLoop extends GameLoop{
	public static final int PAUSED = 0;
	public static final int NORMAL = 1;
	public static final int FAST_FORWARD = 2;
	public static final int FAST_FAST_FORWARD = 3;
	
	public static String ASSETS_PATH;
	
	private final SyncInstructionManager instructionManager = new SyncInstructionManager();
	private final int[] playModeUpdateCounts;
	private final boolean showSpeedPanel;
	
	private int playMode;
	private int updateCount;
	
	public RobotLoop(String rootAssetsPath, boolean showSpeedPanel, int initialPlayMode, 
			int[] playModeUpdateCounts, int ups, int fps) {
		if(playModeUpdateCounts.length != 4)
			throw new IllegalArgumentException("Play mode update counts must be an array of size 4.");
		
		ASSETS_PATH = rootAssetsPath;
		if(!ASSETS_PATH.endsWith("/")) ASSETS_PATH.concat("/");
		
		this.playModeUpdateCounts = playModeUpdateCounts;
		this.showSpeedPanel = showSpeedPanel;
		this.setPlayMode(initialPlayMode);
		
		this.setUpdatePerSecond(ups);
		this.setDrawPerSecond(fps);
	}
	
	@Override
	public void onCreate() {
		if(showSpeedPanel) this.addWindow(new SpeedPanel());
	}

	@Override
	public void onUpdate() {
		World world = (World) instructionManager.readInstruction();
		if(world == null) return;
		
		this.addWindow(world);
		instructionManager.finishInstruction();
	}
	
	@Override
	public void update() {
		for(int i = 0; i < updateCount; i++) {
			super.update();
		}
	}
	
	//PUBLIC METHODS
	/**
	 * Consigue el modo de animacion actual
	 * 
	 * @return modo de animacion
	 */
	public int getPlayMode() {
		return playMode;
	}

	/**
	 * Setea el modo de animacion actual
	 * 
	 * @param playModeIndex el modo de animacion nuevo
	 */
	public void setPlayMode(int playModeIndex) {
		if(playMode >= playModeUpdateCounts.length || playMode < 0)
			throw new IllegalArgumentException("Invalid playMode index");
		
		this.playMode = playModeIndex;
		updateCount = playModeUpdateCounts[playModeIndex];
	}
	
	//Producer method
	/**
	 * Metodo exclusivo para un hilo productor, manda una instruccion para agregar un nuevo {@link World} 
	 * a este objeto.
	 * 
	 * @param newWorld un objeto {@link World}
	 */
	public void createNewWorld(World newWorld) {
		instructionManager.sendInstruction(newWorld);
	}
}
