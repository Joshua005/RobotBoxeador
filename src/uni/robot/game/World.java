package uni.robot.game;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import uni.robot.Mundo;
import uni.robot.base.GameObject;
import uni.robot.base.GameWindow;
import uni.robot.base.SyncInstructionManager;

/**
 * Representa el mundo donde los {@link RobotObject} y otros {@link GridObject} existen.
 * 
 * @author Fabio Kita
 *
 */
public class World extends GameWindow implements Serializable{
	private static final long serialVersionUID = 1L;
	
	transient private SyncInstructionManager instructionManager;
	transient private WorldMap worldMap;
	
	private final Mundo mundoStub;
	
	public World(Mundo mundo, String title, int rowCount, int columnCount){
		if(rowCount <= 0 || columnCount <= 0)
			throw new RuntimeException("El mundo no puede tener 0 filas o columnas.");
		
		this.mundoStub = mundo;
		
		initWorldParameters(title, rowCount, columnCount);
	}

	@Override
	public void onCreate() {
		this.instructionManager = new SyncInstructionManager();
		this.setDimension(
				this.getColumnCount()*getTileSize() + 2*getPaddingX(), 
				this.getRowCount()*getTileSize() + 2*getPaddingY()
			);
	}

	@Override
	public void onUpdate() {
		Object[] args = (Object[]) instructionManager.readInstruction();
		if(args == null) return;
		
		String instruction = (String) args[0];
		switch(instruction) {
			case "CREATE":
				GridObject object = (GridObject) args[1];
				if(worldMap.isValidPosition(object.getRow(), object.getColumn())) {
					this.addObject(object);
					instructionManager.finishInstruction();
				}else {
					instructionManager.finishInstruction("Posicion invalida.");
				}
				return;
			case "GET_ROBOTS":
				RobotObject[] robots = getObjects().stream()
					.filter(o->o instanceof RobotObject)
					.map(o->(RobotObject) o)
					.toArray(RobotObject[]::new);
				instructionManager.finishInstruction(robots);
				return;
		}
	}
	
	//PRODUCER METHOD
	/**
	 * Metodo exlusivo para los hilos productores, manda una instruccion para agregar un {@link GridObject} 
	 * a este mundo.
	 * 
	 * @param gridObject
	 */
	public void createNewObject(GridObject gridObject) {
		Object[] args = {"CREATE", gridObject};
		String error = (String) instructionManager.sendInstruction(args);
		if(error != null) throw new IndexOutOfBoundsException(error);
	}
	
	/**
	 * Metodo exlusivo para los hilos productores, manda una instruccion para conseguir un arreglo de todos los 
	 * robot que existen dentro de este mundo.
	 * 
	 * @return Una lista de todos los {@link RobotObject}
	 */
	public RobotObject[] getAllRobotObjects(){
		Object[] args = {"GET_ROBOTS"};
		return (RobotObject[]) instructionManager.sendInstruction(args);
	}
	
	//PUBLIC METHODS
	/**
	 * Retorna la cantidad de filas de este mundo.
	 * 
	 * @return la cantidad de filas
	 */
	public int getRowCount() {
		return this.worldMap.getRowCount();
	}
	
	/**
	 * Retorna la cantidad de columnas de este mundo.
	 * 
	 * @return la cantidad de columnas
	 */
	public int getColumnCount() {
		return this.worldMap.getColumnCount();
	}
	
	/**
	 * Consigue el objeto stub {@link Mundo} 
	 * 
	 * @return el objeto stub {@link Mundo}.
	 */
	public Mundo getMundo() {
		return this.mundoStub;
	}
	
	@Override
	public void addObject(GameObject object) {
		if(object instanceof GridObject) {
			GridObject gridObject = (GridObject) object;
			this.worldMap.addObject(gridObject.getRow(), gridObject.getColumn(), gridObject);
		}
		super.addObject(object);
	}
	
	@Override
	public void removeObject(GameObject object) {
		if(object instanceof GridObject) {
			GridObject gridObject = (GridObject) object;
			this.worldMap.removeObject(gridObject.getRow(), gridObject.getColumn(), gridObject);
		}
		super.removeObject(object);
	}
	
	/**
	 * Retorna una lista de los {@link GridObject} que se encuentran en la posicion especificada.
	 * 
	 * @param row la fila a controlar
	 * @param column la columna a controlar
	 * 
	 * @return la lista de {@link GridObject}
	 */
	public List<GridObject> getObjectsInPosition(int row, int column){
		return this.worldMap.getObjects(row, column);
	}
	
	/**
	 * Transforma un una columna a su equivalente posicion x, en pixeles
	 * 
	 * @param column el numero de columna
	 * @return su posicion x en el mundo, en pixeles
	 */
	public int columnToX(int column) {
		return this.worldMap.columnToX(column);
	}
	
	/**
	 * Transforma un una fila a su equivalente posicion y, en pixeles
	 * 
	 * @param row el numero de fila
	 * @return su posicion y en el mundo, en pixeles
	 */
	public int rowToY(int row) {
		return this.worldMap.rowToY(row);
	}
	
	/**
	 * Retorna el tamanho en pixeles de cada cuadricula
	 * 
	 * @return el tamanho en pixeles de cada cuadricula
	 */
	public int getTileSize() {
		return worldMap.getTileSize();
	}
	
	/**
	 * Retorna el margen que hay entre las grillas con los bordes horizontales de la ventana.
	 * 
	 * @return el margen horizontal de las grillas
	 */
	public int getPaddingX() {
		return worldMap.getPaddingX();
	}
	
	/**
	 * Retorna el margen que hay entre las grillas con los bordes verticales de la ventana.
	 * 
	 * @return el margen vertical de las grillas
	 */
	public int getPaddingY() {
		return worldMap.getPaddingY();
	}
	
	/**
	 * Retorna si la posicion pasada es una posicion valida, es decir, si la posicion se encuentra 
	 * dentro del mundo.
	 * 
	 * @param row La fila a controlar
	 * @param column La fila a controlar
	 * @return Si la posicion es valida o no.
	 */
	public boolean isValidPosition(int row, int column) {
		if(row < 0 || row >= this.getRowCount()) return false;
		if(column < 0 || column >= this.getColumnCount()) return false;
		return true;
	}
	
	//DEFAULT METHODS
	/**
	 * Cambia la posicion de un objeto dentro del mundo. 
	 * 
	 * @param object el objeto a cambiar
	 * @param fromRow la fila en donde se encuentra
	 * @param fromColumn la columna en donde se encuentra
	 * @param toRow la nueva fila
	 * @param toColumn la nueva columna
	 */
	void changeObjectPosition(GridObject object, int fromRow, int fromColumn, int toRow, int toColumn) {
		this.worldMap.removeObject(fromRow, fromColumn, object);
		this.worldMap.addObject(toRow, toColumn, object);
	}
	
	//PRIVATE METHODS
	/**
	 * Setea ls parametros basicos del mundo
	 * 
	 * @param title el titulo de este mundo
	 * @param rowCount la cantidad de filas de este mundo
	 * @param columnCount la cantidad de columnas de este mundo
	 */
	private void initWorldParameters(String title, int rowCount, int columnCount) {
		this.setTitle(title);
		this.worldMap = new WorldMap(rowCount, columnCount);
		this.addObject(worldMap);
	}
	
	private void readObject(ObjectInputStream in) 
			throws ClassNotFoundException, IOException{
		in.defaultReadObject();
		
		String title = in.readUTF();
		int rowCount = in.readInt();
		int columnCount = in.readInt();
		
		initWorldParameters(title, rowCount, columnCount);
		
		//Read objects
		int objectCount = in.readInt();
		for(int i = 0; i < objectCount; i++) {
			this.addObject((GameObject) in.readObject());
		}
	}

	private void writeObject(ObjectOutputStream out) 
			throws IOException{
		out.defaultWriteObject();
		
		out.writeUTF(getTitle());
		out.writeInt(getRowCount());
		out.writeInt(getColumnCount());
		
		//Write all GridObjects
		List<GridObject> objects = getObjects().stream()
				.filter(o->o instanceof GridObject)
				.map(o->(GridObject) o)
				.collect(Collectors.toList());
		
		out.writeInt(objects.size());
		for(GridObject object:objects) {
			out.writeObject(object);
		}
	}
	
	//STATIC METHODS
	/**
	 * Metodo estatico que guarda el estado del mundo, y los objetos, dentro de un archivo.
	 * 
	 * @param world el mundo a guardas
	 * @param path el nombre del archivo donde guardar
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void saveWorld(World world, String path) 
			throws FileNotFoundException, IOException {
		try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path))){
			out.writeObject(world);
		}
	}
	
	/**
	 * Metodo estatico que carga y crea un nuevo mundo desde un archivo previamente guardado.
	 * 
	 * @param path el nombre del archivo
	 * 
	 * @return un objeto World con los Objetos
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static World loadWorld(String path) 
			throws FileNotFoundException, IOException, ClassNotFoundException {
		try(ObjectInputStream in = new ObjectInputStream(new FileInputStream(path))){
			return (World) in.readObject();
		}
	}
}