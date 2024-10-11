package uni.robot.game;

import java.util.LinkedList;
import java.util.List;

import uni.robot.base.GameObject;
import uni.robot.base.Sprite;

/**
 * Objeto que representa las cuadriculas de un {@link World}
 * 
 * @author Fabio Kita
 *
 */
public class WorldMap extends GameObject{
	private static final int TILE_SIZE = 48;
	private static final int PADDING_X = 32;
	private static final int PADDING_Y = 32;
	
	private GridCell[][] grid;
	private Sprite tileSprites;
	private Sprite numberSprites;
	
	public WorldMap(int rowCount, int columnCount) {
		grid = new GridCell[rowCount][columnCount];
		for(int i = 0; i < grid.length; i++) {
			for(int j = 0; j < grid[i].length; j++) {
				grid[i][j] = new GridCell();
			}
		}
	}
	
	@Override
	public void onCreate() {
		//Grid images
		final String GRID_IMAGE_PATH = RobotLoop.ASSETS_PATH + "WorldTile.png";
		final int GRID_IMAGE_SUBSPRITE_SIZE = 24;
		
		tileSprites = new Sprite(
				getResourceManager().loadImage(GRID_IMAGE_PATH), 
				GRID_IMAGE_SUBSPRITE_SIZE, 
				GRID_IMAGE_SUBSPRITE_SIZE
			);
		
		//Number Images
		final String NUMBER_IMAGE_PATH = RobotLoop.ASSETS_PATH + "NumberSprite.png";
		final int NUMBER_IMAGE_SUBSPRITE_SIZE = 6;
		
		numberSprites = new Sprite(
				getResourceManager().loadImage(NUMBER_IMAGE_PATH),
				NUMBER_IMAGE_SUBSPRITE_SIZE,
				NUMBER_IMAGE_SUBSPRITE_SIZE
			);
		numberSprites.setOrigin(
				NUMBER_IMAGE_SUBSPRITE_SIZE/2, 
				NUMBER_IMAGE_SUBSPRITE_SIZE/2
			);
	}
	
	@Override
	public void onDraw() {
		//Draw Grid
		final int GRID_IMAGE_DRAW_OFFSET = 1;
		
		for(int i = 0; i < grid.length; i++) {
			for(int j = 0; j < grid[i].length; j++) {
				int x = j*TILE_SIZE + PADDING_X + GRID_IMAGE_DRAW_OFFSET;
				int y = i*TILE_SIZE + PADDING_Y + GRID_IMAGE_DRAW_OFFSET;
				double scaleX = 2;
				double scaleY = 2;
				this.drawSprite(tileSprites, getTileIndex(i, j), x, y, scaleX, scaleY);
			}
		}
		
		//Draw Numbers
		final int NUMBER_PADDING = 16;
		
		//Draw column index
		for(int i = 0; i < getColumnCount(); i++) {
			drawNumber(i, columnToX(i), rowToY(0)-NUMBER_PADDING);
		}
		
		//Draw row index
		for(int i = 0; i < getRowCount(); i++) {
			drawNumber(i, columnToX(0)-NUMBER_PADDING, rowToY(i));
		}
	}

	@Override
	public void onUpdate() { }
	
	@Override
	public void onDestroy() { }
	
	//OBJECT METHODS
	/**
	 * Agrega un nuevo objeto dentro de una celda
	 * 
	 * @param row la fila 
	 * @param column la columna
	 * @param object el objeto a guardar
	 */
	public void addObject(int row, int column, GridObject object) {
		grid[row][column].addObject(object);
	}
	
	/**
	 * Remueve un objeto de una celda
	 * 
	 * @param row la fila
	 * @param column la columna
	 * @param object el objeto a remover
	 */
	public void removeObject(int row, int column, GridObject object) {
		grid[row][column].removeObject(object);
	}
	
	/**
	 * Retorna la lista de objetos que estan en una celda
	 * 
	 * @param row la fila 
	 * @param column la columna
	 * @return la lista de objetos
	 */
	public List<GridObject> getObjects(int row, int column) {
		return grid[row][column].getObjects();
	}
	
	//GRID METHODS
	/**
	 * Retorna la cantidad de filas.
	 * @return la cantidad de filas
	 */
	public int getRowCount() {
		return grid.length;
	}
	
	/**
	 * Retorna la cantidad de columnas.
	 * @return la cantidad de columnas
	 */
	public int getColumnCount() {
		return grid[0].length;
	}
	
	/**
	 * Calcula la posicion x de una columna.
	 * @param column la columna
	 * @return la posicion x de la columna, en pixel
	 */
	public int columnToX(int column) {
		return PADDING_X + TILE_SIZE*column + TILE_SIZE/2;
	}
	
	/**
	 * Calcula la posicion y de una fila.
	 * @param row la fila
	 * @return la posicion y de la fila, en pixel
	 */
	public int rowToY(int row) {
		return PADDING_Y + TILE_SIZE*row + TILE_SIZE/2;
	}
	
	/**
	 * Retorna el tamanho de cada cuadricula
	 * 
	 * @return el tamanho de cada cuadricula
	 */
	public int getTileSize() {
		return TILE_SIZE;
	}
	
	/**
	 * Retorna el margen x de la cuadricula con respecto al borde de la ventana
	 * @return el margen x
	 */
	public int getPaddingX() {
		return PADDING_X;
	}
	
	/**
	 * Retorna el margen y de la cuadricula con respecto al borde de la ventana
	 * @return el margen y
	 */
	public int getPaddingY() {
		return PADDING_Y;
	}
	
	/**
	 * Retorna si una posicion dada es valida dentro de la cuadricula.
	 * 
	 * @param row la fila 
	 * @param column la columna
	 * @return si la posicion es valida
	 */
	public boolean isValidPosition(int row, int column) {
		return !(row < 0 || row >= getRowCount()) && 
				!(column < 0 || column >= getColumnCount());
	}
	
	//PRIVATE METHODS
	//Numbers
	private void drawNumber(int number, int x, int y) {
		final int WIDTH_OFFSET = 2;
		final int NUMBER_WIDTH = numberSprites.getFrame(0).getWidth()*2-WIDTH_OFFSET;
		String numberString = String.valueOf(number);
		for(int i = 0; i < numberString.length(); i++) {
			//transformation of the formula = ((1-n)/2 + i) * width;
			int offsetX = (1-numberString.length())*NUMBER_WIDTH/2+i*NUMBER_WIDTH;
			String numberToDraw = numberString.substring(i, i+1);
			drawSprite(numberSprites, Integer.parseInt(numberToDraw), x+offsetX, y, 2, 2);
		}
	}
	
	//Tile indexes function
	private int getTileIndex(int row, int column) {
		final int[][] NORMAL_INDEX_MAP = {{0, 1, 2},{4, 5, 6},{8, 9, 10}};
		final int[] VERTICAL_INDEX_MAP = {3, 7, 11};
		final int[] HORIZONTAL_INDEX_MAP = {12, 13, 14};
		final int POINT_INDEX = 15;
		
		int rowCount = grid.length;
		int columnCount = grid[row].length;
		
		//Point grid
		if(rowCount == 1 && columnCount == 1) return POINT_INDEX;
		
		//Vertical grid
		int rowPos = toPositionIndex(row, 0, rowCount-1);
		if(columnCount == 1) return VERTICAL_INDEX_MAP[rowPos];
		
		//Horizontal grid
		int columnPos = toPositionIndex(column, 0, columnCount-1);
		if(rowCount == 1) return HORIZONTAL_INDEX_MAP[columnPos];
		
		//Normal grid
		return NORMAL_INDEX_MAP[rowPos][columnPos];
	}
	
	private int toPositionIndex(int number, int min, int max) {
		if(number == min) return 0;
		if(number == max) return 2;
		return 1;
	}
	
	//LOCAL CLASS
	private class GridCell{
		private List<GridObject> list = new LinkedList<>();
		
		public synchronized void addObject(GridObject gridObject) {
			list.add(gridObject);
		}
		
		public synchronized void removeObject(GridObject gridObject) {
			list.remove(gridObject);
		}
		
		public synchronized List<GridObject> getObjects(){
			return new LinkedList<>(list);
		}
	}
}
