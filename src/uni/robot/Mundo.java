package uni.robot;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;

import uni.robot.game.RobotLoop;
import uni.robot.game.World;

/**
 * Esta clase representa un mundo abstracto en donde uno o mas {@link Robot} pueden existir.
 * <p>
 * El mundo esta compuesto de calles y avenidas (o filas y columnas) y esta rodeado de parades hecho de un 
 * material inpenetrable e indestructible llamado NEUTRONIUM. 
 * 
 * @author Fabio Kita
 *
 */
public class Mundo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private final World world;
	
	/**
     * Crea un mundo nuevo de tamaño 12x12.
     */
    public Mundo() {
        this("<<Comunidad de Unibots>>", 12, 12);
    }
	
	/**
	 * Crea un mundo nuevo, con un titulo y dimensiones especificadas.
	 * 
	 * @param titulo El titulo del mundo.
	 * @param cantFilas La cantidad de filas del mundo.
	 * @param cantColumnas La cantidad de columnas del mundo.
	 */
	public Mundo(String titulo, int cantFilas, int cantColumnas) {
		RobotLoop loop = RobotGame.getRobotLoop();
		this.world = new World(this, titulo, cantFilas, cantColumnas);
		loop.createNewWorld(this.getWorld());
	}
	
	/**
	 * Devuelve la cantidad de calles (o filas) que tiene este mundo.
	 * 
	 * @return La cantidad de filas.
	 */
	public int getCantidadFilas() {
		return this.world.getRowCount();
	}
	
	/**
	 * Devuelve la cantidad de avenidas (o columnas) que tiene este mundo.
	 * 
	 * @return La cantidad de columnas.
	 */
	public int getCantidadColumnas() {
		return this.world.getColumnCount();
	}
	
	/**
	 * Devuelve un arreglo de todos los {@link Robot} que estan dentro de este mundo.
	 * 
	 * @return Un arreglo de todos los {@link Robot}.
	 */
	public Robot[] getRobots() {
		return Arrays.stream(this.getWorld().getAllRobotObjects())
				.map(o->o.getRobot())
				.toArray(Robot[]::new);
	}
	
	/**
	 * Devuelve el objeto real {@link World} que este stub representa.
	 * 
	 * @return El objeto {@link World}
	 */
	protected World getWorld() {
		return world;
	}
	
	/**
	 * Metodo estatico que guarda el estado de este mundo, y sus objetos, dentro de un archivo.
	 * 
	 * @param mundo El mundo a guardar
	 * @param nombreArchivo El archivo donde guardar.
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void guardarMundo(Mundo mundo, String nombreArchivo) 
			throws FileNotFoundException, IOException {
		World.saveWorld(mundo.getWorld(), nombreArchivo);
	}
	
	/**
	 * Metodo estatico que carga y crea un Mundo, y sus objetos, desde un archivo.
	 * 
	 * @param nombreArchivo El archivo origen
	 * 
	 * @return Un Mundo nuevo con los objetos.
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Mundo cargarMundo(String nombreArchivo) 
			throws FileNotFoundException, IOException, ClassNotFoundException {
		RobotLoop loop = RobotGame.getRobotLoop();
		World world = World.loadWorld(nombreArchivo);
		loop.createNewWorld(world);
		return world.getMundo();
	}
}
