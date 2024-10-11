package uni.robot;

import java.io.Serializable;

import uni.robot.game.Direction;
import uni.robot.game.RobotObject;
import uni.robot.game.robotinstruction.BaseInstructionHandler;

/**
 * Esta clase representa un Robot abstracto. Estos robots existen en un {@link Mundo} relativamente vacio
 * (donde no existen autos ni casas) con el unico objeto de hacer tareas simples, pero interesantes.
 * <p>
 * Un Robot siempre se encuentra en una esquina mirando hacia una direccion (NORTE, SUR, ESTE u OESTE). El 
 * Robot es un Robot mobil, capaz de transladarse de una esquina a otra, pero tiene que tener cuidado, ya
 * que puede chocar con una pared. Para evitar collisionar con las paredes, el Robot tiene un visor de corta
 * distancia que le deja ver si en frente hay una pared o no.
 * <p>
 * El Robot tiene brazos y una bolsita de conos, lo que le deja poner conos en el mundo, y recoger conos y 
 * ponerlos en la bolsita. Pero nuevamente hay que tener cuidado, ya que el robot tiene una cantidad limitada 
 * de conos, y su bolsita tambien tiene espacio limitado.
 * <p>
 * Vos, como el dueño, tienes la responsabilidad de guiar a los Robot, usando sus sensores y acciones 
 * adecuadamente, para que realizen las tareas necesarias de forma correcta.
 * 
 * @author Fabio Kita
 *
 */
public class Robot extends StubObject implements Serializable{
	private static final long serialVersionUID = 1L;
	
	public final static int NORTE = Direction.NORTH;
	public final static int OESTE = Direction.WEST;
	public final static int SUR = Direction.SOUTH;
	public final static int ESTE = Direction.EAST;
	
	private final RobotObject robot;
	
	static {
		RobotObject.addInstructionHandler(new BaseInstructionHandler());
	}
	
	/**
	 * Crea un Robot en la esquina superior-izquierda del Mundo, mirando al sur, con 10 conos (de cantidad y 
	 * capacidad).
	 * @param m El mundo en donde crear el Robot.
	 */
	public Robot(Mundo m) {
		this(m, 0, 0, SUR, 10, 10);
	}
	
	/**
	 * Crea un Robot en un Mundo, con la posicion, direccion, cantidad y capacidad de conos especificadas.
	 * @param m El mundo en donde crear el Robot.
	 * @param fila La fila inicial del Robot.
	 * @param columna La columna inicial del Robot.
	 * @param dirInicial La direccion inicial del Robot.
	 * @param cantidadMaxima La capacidad de la bolsita.
	 * @param cantConos La cantidad de conos inicial.
	 */
	public Robot(Mundo m, int fila, int columna, int dirInicial, int cantidadMaxima, int cantConos) {
		this.robot = new RobotObject(this, fila, columna, dirInicial, cantidadMaxima, cantConos);
		createNewObject(m, robot);
	}
	
	//ACCIONES
	/**
	 * Hace que este Robot gire 90° hacia la izquierda.
	 */
	public void girarIzquierda() {
		sendInstruction("TURN_LEFT");
	}
	
	/**
	 * Hace que este Robot gire 90° hacia la derecha.
	 */
	public void girarDerecha() {
		sendInstruction("TURN_RIGHT");
	}
	
	/**
	 * Hace que el Robot avance al frente, hasta la siguiente esquina. Si choca con alguna pared, el Robot lanza
	 * un error.
	 */
	public void avanzar() {
		tirarErrorSiHubo(sendInstruction("WALK"));
	}
	
	/**
	 * Hace que el Robot ponga un cono en su posicion actual. Tira un error si al Robot no le quedan conos en 
	 * la bolsita.
	 */
	public void ponerCono() {
		tirarErrorSiHubo(sendInstruction("PUT_CONE"));
	}
	
	/**
	 * Hace que el Robot guarde un cono en la posicion actual. Tira un error si no hay ningun cono o si la 
	 * bolsita del Robot esta lleno.
	 */
	public void guardarCono() {
		tirarErrorSiHubo(sendInstruction("REMOVE_CONE"));
	}
	
	//SENSORES
	/**
	 * Devuelve el numero de fila donde este Robot se encuentra.
	 * 
	 * @return El numero de fila.
	 */
	public int getFila() {
		return (int) sendInstruction("GET_ROW");
	}
	
	/**
	 * Devuelve el numero de columna donde este Robot se encuentra.
	 * 
	 * @return El numero de columna.
	 */
	public int getColumna() {
		return (int) sendInstruction("GET_COLUMN");
	}
	
	/**
	 * Devuelve la direccion actual que este Robot esta mirando.
	 * 
	 * @return la direccion actual del Robot.
	 */
	public int getDireccion() {
		return (int) sendInstruction("GET_DIRECTION");
	}
	
	/**
	 * Controla si en la posicion actual del Robot hay por lo menos un cono.
	 * 
	 * @return Si hay un cono o no en la posicion actual.
	 */
	public boolean hayCono() {
		return (boolean) sendInstruction("IS_CONE");
	}
	
	/**
	 * Devuelve la cantidad de conos que el Robot tiene actualmente.
	 * 
	 * @return La cantidad de conos que tiene el Robot.
	 */
	public int getCantidadConos() {
		return (int) sendInstruction("GET_CONE_COUNT");
	}
	
	/**
	 * Devuelve la cantidad de conos que entran dentro de la bolsita de este Robot.
	 * 
	 * @return La capacidad de la bolsita de este Robot.
	 */
	public int getCapacidadConos() {
		return (int) sendInstruction("GET_CONE_CAPACITY");
	}
	
	/**
	 * Controla si el Robot puede avanzar, es decir, si no hay ninguna pared en frente del Robot.
	 * 
	 * @return Si el Robot puede avanzar.
	 */
	public boolean puedeAvanzar() {
		return (boolean) sendInstruction("CAN_MOVE_FORWARD");
	}
	
	/**
	 * Devuelve el Mundo donde el Robot existe actualmente.
	 * 
	 * @return el Mundo donde el Robot esta.
	 */
	public Mundo getMundo() {
		return (Mundo) sendInstruction("GET_WORLD");
	}
	
	//METODOS PROTEGIDOS
	/**
	 * Manda una instruccion al {@link RobotObject}
	 * @param message la instruccion a mandar.
	 * @return el resultado de ejecutar la instruccion.
	 */
	protected Object sendInstruction(Object message) {
		return this.robot.getInstructionManager().sendInstruction(message);
	}
	
	//METODOS PRIVADOS
	private void tirarErrorSiHubo(Object response) {
		String message = (String) response;
		if(message != null) throw new RuntimeException(message);
	}
}
