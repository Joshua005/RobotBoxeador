package uni.robot;

import uni.robot.game.Direction;
import uni.robot.game.WallObject;

/**
 * Objeto que representa una pared dentro de un {@link Mundo}. Esta pared esta hecho del mismo material que 
 * los bordes de un Mundo (NEUTRONIUM). Una pared se crea entre dos esquinas y bloquea el paso directo de un 
 * {@link Robot} entre esas esquinas.
 * 
 * @author Fabio Kita
 *
 */
public class Pared extends StubObject{
	public final static int NORTE = Direction.NORTH;
	public final static int OESTE = Direction.WEST;
	public final static int SUR = Direction.SOUTH;
	public final static int ESTE = Direction.EAST;
	
	/**
	 * Se crea una pared que bloquea el paso entre la posicion especificada hasta la posicion hacia la direccion
	 * especificada.
	 * 
	 * @param m El mundo en donde crear la pared.
	 * @param fila La fila que bloquea la pared
	 * @param columna La columna que bloquea la pared
	 * @param direccion La direccion en que bloquea la pared.
	 */
	public Pared(Mundo m, int fila, int columna, int direccion) {
		createNewObject(m, new WallObject(fila, columna, direccion));
	}
}
