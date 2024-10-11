package uni.robot.game;

/**
 * Clase con metodos y constantes estaticas para calculos con direcciones.
 * @author Fabio Kita
 *
 */
public class Direction {
	public static final int NORTH = 0;
	public static final int WEST = 1;
	public static final int SOUTH = 2;
	public static final int EAST = 3;
	
	/**
	 * Retorna el valor del vector x segun la direccion pasada
	 * <p>
	 * Si se pasa WEST, retorna -1.
	 * <p>
	 * Si se pasa EAST, retorna 1.
	 * <p>
	 * Si no, retorna 0.
	 * 
	 * 
	 * @param direction la direccion
	 * 
	 * @return un valor entre -1 a 1, segun la direccion pasada
	 */
	public static int getVectorX(int direction) {
		switch(direction) {
			case WEST: return -1;
			case EAST: return 1;
			default: return 0;
		}
	}
	
	/**
	 * Retorna el valor del vector y segun la direccion pasada
	 * <p>
	 * Si se pasa NORTH, retorna -1.
	 * <p>
	 * Si se pasa SOUTH, retorna 1.
	 * <p>
	 * Si no, retorna 0.
	 * 
	 * @param direction la direccion
	 * 
	 * @return un valor entre -1 a 1, segun la direccion pasada
	 */
	public static int getVectorY(int direction) {
		switch(direction) {
			case NORTH: return -1;
			case SOUTH: return 1;
			default: return 0;
		}
	}
	
	/**
	 * Retorna una direccion valida segun el vector pasado.
	 * <p>
	 * Si el componente x no es 0, retorna WEST o EAST acordemente.
	 * <p>
	 * Si el componente y no es 0, retorna NORTH o SOUTH acordemente.
	 * <p>
	 * Si no, lanza un {@link RuntimeException}.
	 * 
	 * @param x el componente x
	 * @param y el componente y
	 * @return una direccion
	 */
	public static int getFromVector(int x, int y) {
		if(x < 0) return WEST;
		if(x > 0) return EAST;
		if(y < 0) return NORTH;
		if(y > 0) return SOUTH;
		throw new IllegalArgumentException("Components x and y cannot be 0");
	}
	
	/**
	 * Metodo retorna una direccion girada a la izquierda.
	 * 
	 * @param direction la direccion a girar
	 * @return la direccion girada
	 */
	public static int rotateLeft(int direction) {
		int dirX = getVectorX(direction);
		int dirY = getVectorY(direction);
		return getFromVector(dirY, -dirX);
	}
	
	/**
	 * Metodo retorna una direccion girada a la derecha.
	 * 
	 * @param direction la direccion a girar
	 * @return la direccion girada
	 */
	public static int rotateRight(int direction) {
		int dirX = getVectorX(direction);
		int dirY = getVectorY(direction);
		return getFromVector(-dirY, dirX);
	}
	
	/**
	 * Metodo que retorna la direccion opuesta a la pasada
	 * 
	 * @param direction la direccion de referencia
	 * @return la direccion opuesta
	 */
	public static int getOpposite(int direction) {
		switch(direction) {
			case NORTH: return SOUTH;
			case SOUTH: return NORTH;
			case WEST: return EAST;
			case EAST: return WEST;
		}
		throw new IllegalArgumentException("Invalid direction");
	}
	
	/**
	 * Metodo que transforma una direccion a su caracter correspondiente.
	 * <p>
	 * NORTH: 'N'
	 * <p>
	 * SOUTH: 'S'
	 * <p>
	 * EAST: 'E'
	 * <p>
	 * WEST: 'W'
	 * 
	 * @param direction
	 * @return
	 */
	public static char getDirectionChar(int direction) {
		final char[] characters = {'N', 'W', 'S', 'E'};
		return characters[direction];
	}
}
