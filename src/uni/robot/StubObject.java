package uni.robot;

import uni.robot.game.GridObject;

/**
 * Objeto base de los objetos como {@link Robot}, {@link Pared} o {@link Cono}. 
 * 
 * @author Fabio Kita
 *
 */
public abstract class StubObject {
	
	/**
	 * Crea un nuevo {@link GridObject} dentro del {@link Mundo} especificado.
	 * 
	 * @param m el mundo donde crear.
	 * @param object el objeto a crear.
	 */
	protected void createNewObject(Mundo m, GridObject object) {
		m.getWorld().createNewObject(object);
	}
}
