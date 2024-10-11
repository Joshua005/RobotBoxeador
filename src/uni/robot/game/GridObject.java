package uni.robot.game;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

import uni.robot.base.GameObject;

/**
 * Representa un {@link GameObject} en una posicion dentro de la cuadricula de un {@link World}.
 * 
 * @author Fabio Kita
 *
 */
public abstract class GridObject extends GameObject implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private AtomicInteger row;
	private AtomicInteger column;
	
	public GridObject(int row, int column) {
		this.row = new AtomicInteger(row);
		this.column = new AtomicInteger(column);
	}
	
	/**
	 * Retorna la fila en que se encuentra este objeto.
	 * 
	 * @return la fila 
	 */
	public int getRow() {
		return this.row.get();
	}

	/**
	 * Retorna la columna en que se encuentra este objeto.
	 * 
	 * @return la columna
	 */
	public int getColumn() {
		return this.column.get();
	}
	
	/**
	 * Retorna la posicion de la columna, en pixeles, que este objeto se encuentra.
	 * 
	 * @return Retorna la posicion x en pixeles de la columna
	 */
	public int getColumnX() {
		return this.getWorld().columnToX(getColumn());
	}
	
	/**
	 * Retorna la posicion de la fila, en pixeles, que este objeto se encuentra.
	 * 
	 * @return Retorna la posicion y en pixeles de la fila
	 */
	public int getRowY() {
		return this.getWorld().rowToY(getRow());
	}
	
	/**
	 * Setea la nueva posicion, fila y columna, de este objeto
	 * 
	 * @param row la nueva fila
	 * @param column la nueva columna
	 */
	public void setGridPosition(int row, int column) {
		this.getWorld().changeObjectPosition(this, getRow(), this.getColumn(), row, column);
		this.row.set(row);
		this.column.set(column);
	}
	
	/**
	 * Consigue el objeto {@link World} donde este obejto se encuentra.
	 * 
	 * @return el objeto {@link World}
	 */
	public World getWorld() {
		return (World) this.getWindow();
	}
	
	/**
	 * Consigue el objeto {@link RobotLoop} donde este objeto esta.
	 * 
	 * @return el objeto {@link RobotLoop}
	 */
	public RobotLoop getRobotLoop() {
		return (RobotLoop) this.getWindow().getGameLoop();
	}
}
