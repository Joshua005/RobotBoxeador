package uni.robot;

import uni.robot.game.ConeObject;

/**
 * Objeto que representa un Cono. Es un cono de trafico hecho de plastico, 
 * normal, comun y corriente.
 * 
 * @author Fabio Kita
 *
 */
public class Cono extends StubObject{
	public Cono(Mundo m, int fila, int columna) {
		createNewObject(m, new ConeObject(fila, columna));
	}
}
