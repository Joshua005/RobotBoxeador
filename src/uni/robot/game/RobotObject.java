package uni.robot.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import uni.robot.Robot;
import uni.robot.base.Sprite;
import uni.robot.base.SyncInstructionManager;
import uni.robot.game.robotinstruction.*;

/**
 * Obejto que hereda de {@link GridObject}, que representa un Robot.
 * 
 * @author Fabio
 *
 */
public class RobotObject extends GridObject{
	private static final long serialVersionUID = 1L;
	
	private static final List<RobotInstructionHandler> instructionHandlerList = new ArrayList<>();
	
	transient private RobotState state;
	transient private Map<String, Sprite> spriteMap;
	transient private SyncInstructionManager instructionManager;
	
	private final Robot robotStub;
	private final int capacity;
	private int direction;
	private int coneCount;
	
	public RobotObject(Robot robot, int row, int column, int direction, int coneCapacity, int initialConeCount) {
		super(row, column);
		
		this.robotStub = robot;
		this.capacity = coneCapacity;
		
		this.direction = direction;
		this.coneCount = initialConeCount;
		
		if(getCapacity() < 0) throw new RuntimeException("No se puede tener capacidad negativa de conos.");
		if(getDirection() < 0) throw new RuntimeException("No se puede tener cantidad negativa de conos.");
		if(getConeCount() > getCapacity()) throw new RuntimeException("La cantidad de conos no puede superar la capacidad.");
	}
	
	//BASE METHODS
	@Override
	public void onCreate() {
		//Initialize sprites
		initSprites();
		
		//Initialize InstructionManager, done here for serialization reasons
		this.instructionManager = new SyncInstructionManager();
		
		//Set initial state to Idle
		this.state = new IdleState(this);
		
		setZIndex(getRowY());
	}

	@Override
	public void onUpdate() {
		RobotState newState = state.handleUpdate();
		if(newState != state) {
			state.onExit(newState);
			newState.onEnter(state);
			state = newState;
		}
	}

	@Override
	public void onDraw() {
		state.handleDraw();
	}

	@Override
	public void onDestroy() {}

	//GETTER/SETTERS
	/**
	 * Consigue la direccion que el robot esta mirando
	 * 
	 * @return la direccion que el robot esta mirando
	 */
	public int getDirection() {
		return direction;
	}

	/**
	 * Setea la direccion que el robot esta mirando
	 * 
	 * @param direction la nueva direccion
	 */
	public void setDirection(int direction) {
		this.direction = direction;
	}
	
	/**
	 * Consigue la cantidad de conos que el robot tiene
	 * 
	 * @return la cantidad de conos que el robot tiene
	 */
	public int getConeCount() {
		return coneCount;
	}

	/**
	 * Setea la cantidad de conos que el robot tiene
	 * 
	 * @param coneCount la nueva cantidad de conos
	 */
	public void setConeCount(int coneCount) {
		this.coneCount = coneCount;
	}

	/**
	 * Retorna la capacidad de conos del robot
	 * 
	 * @return la capacidad de conos del robot
	 */
	public int getCapacity() {
		return capacity;
	}
	
	/**
	 * Consigue el objeto {@link SyncInstructionManager} del robot.
	 * <p>
	 * Cualquier instruccion que se quierea
	 * 
	 * @return
	 */
	public SyncInstructionManager getInstructionManager() {
		return instructionManager;
	}
	
	/**
	 * Consigue el objeto stub {@link Robot}.
	 * 
	 * @return el objeto stub {@link Robot}
	 */
	public Robot getRobot() {
		return robotStub;
	}

	//HELPER METHODS
	/**
	 * Consigue todos los conos que se encuentran en la misma posicion que este objeto.
	 * 
	 * @return lista de conos en la misma posicion que el robot.
	 */
	public List<ConeObject> getConeInPosition(){
		return getWorld().getObjectsInPosition(getRow(), getColumn()).stream()
				.filter(o->o instanceof ConeObject)
				.map(o-> (ConeObject) o)
				.collect(Collectors.toList());
	}
	
	/**
	 * Retorna si el robot puede avanzar o no.
	 * 
	 * @return si el robot puede avanzar o no.
	 */
	public boolean canMoveForward() {
		int rowTo = getRow() + Direction.getVectorY(getDirection());
		int columnTo = getColumn() + Direction.getVectorX(getDirection());
		
		//Check borders
		if(!getWorld().isValidPosition(rowTo, columnTo)) {
			return false;
		}
		
		//Check for walls in current position
		//(Bonks if the wall has the same direction with the robot)
		List<WallObject> wallInCurrentPosition = getWallInPosition(getRow(), getColumn());
		for(WallObject wall:wallInCurrentPosition) {
			if(wall.getDirection() == getDirection()) return false;
		}
		
		//Check for walls in next position
		//(Bonks if the wall has the opposite direction with the robot)
		List<WallObject> wallInNextPosition = getWallInPosition(rowTo, columnTo);
		for(WallObject wall:wallInNextPosition) {
			if(wall.getDirection() == Direction.getOpposite(getDirection())) return false;
		}
		
		return true;
	}
	
	/**
	 * Consigue un sprite de robot a partir del nombre, segun definido dentro del metodo initSprites().
	 * 
	 * @param name el nombre del sprite
	 * @return
	 */
	public Sprite getSprite(String name) {
		return spriteMap.get(name);
	}
	
	//Private Methods
	/**
	 * Inicializa todos los sprites usados por el robot.
	 */
	private void initSprites() {
		spriteMap = new HashMap<>();
		
		final int DIRECTIONS_COUNT = 4;
		final int SIZE = 32;
		final int Y_OFFSET = 10;
		
		//Walk Sprites
		for(int direction = 0; direction < DIRECTIONS_COUNT; direction++) {
			char dirChar = Direction.getDirectionChar(direction);
			final String PATH = RobotLoop.ASSETS_PATH + "robot/walk/RobotWalk" + dirChar + ".png";
			
			Sprite sprite = new Sprite(getResourceManager().loadImage(PATH), SIZE, SIZE);
			sprite.setCenterAsOrigin();
			sprite.setOrigin(sprite.getOriginX(), sprite.getOriginY()+Y_OFFSET);
			spriteMap.put("Walk" + dirChar, sprite);
		}
		
		//Turn Sprites
		for(int direction = 0; direction < DIRECTIONS_COUNT; direction++) {
			int nextDirection = (direction+1)%4;
			char dirChar = Direction.getDirectionChar(direction);
			char nextDirChar = Direction.getDirectionChar(nextDirection);
			final String PATH = RobotLoop.ASSETS_PATH + "robot/turn/RobotTurn" + dirChar + nextDirChar + ".png";
			
			Sprite sprite = new Sprite(getResourceManager().loadImage(PATH), SIZE, SIZE);
			sprite.setCenterAsOrigin();
			sprite.setOrigin(sprite.getOriginX(), sprite.getOriginY()+Y_OFFSET);
			spriteMap.put("Turn"+dirChar+nextDirChar, sprite);
		}
		
		//Put/Remove Sprite
		for(int direction = 0; direction < DIRECTIONS_COUNT; direction++) {
			char dirChar = Direction.getDirectionChar(direction);
			final String PATH = RobotLoop.ASSETS_PATH + "robot/interact/RobotInteract" + dirChar + ".png";
			
			Sprite sprite = new Sprite(getResourceManager().loadImage(PATH), SIZE, SIZE);
			sprite.setCenterAsOrigin();
			sprite.setOrigin(sprite.getOriginX(), sprite.getOriginY()+Y_OFFSET);
			spriteMap.put("Interact" + dirChar, sprite);
		}
	}
	
	/**
	 * Consige una lista de {@link WallObject} que se encuentra en una posicion especifica.
	 * 
	 * @param row la fila a controlar
	 * @param column la columna a controlar
	 * @return la lista de {@link WallObject}
	 */
	private List<WallObject> getWallInPosition(int row, int column){
		return getWorld().getObjectsInPosition(row, column).stream()
				.filter(o->o instanceof WallObject)
				.map(o-> (WallObject) o)
				.collect(Collectors.toList());
	}

	//STATIC METHODS
	public static List<RobotInstructionHandler> getInstructionHandlerList(){
		return instructionHandlerList;
	}
	
	public static void addInstructionHandler(RobotInstructionHandler instructionHandler) {
		instructionHandlerList.add(instructionHandler);
	}
}