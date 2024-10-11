package uni.robot.base;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

/**
 * Objeto que compone a {@link GameWindow}, encargado de almacenar los {@link GameObject} de una ventana.
 * @author Fabio Kita
 *
 */
class ObjectManager {
	private Map<Integer, GameObject> objectMap;
	
	public ObjectManager() {
		objectMap = new TreeMap<>();
	}
	
	/**
	 * Agrega un objeto
	 * 
	 * @param newObject El objeto a agregar
	 */
	public synchronized void addObject(GameObject newObject) {
		objectMap.put(newObject.getId(), newObject);
	}
	
	/**
	 * Remueve un objeto, segun id
	 * 
	 * @param id El id del obejto a remover
	 */
	public synchronized void removeObject(int id) {
		objectMap.remove(id);
	}
	
	/**
	 * Remueve un objeto
	 * 
	 * @param object el objeto a remover
	 */
	public synchronized void removeObject(GameObject object) {
		this.removeObject(object.getId());
	}
	
	/**
	 * Consigue un objeto segun su id.
	 * 
	 * @param id El id del Objeto
	 * @return El objeto con el id especificado
	 */
	public synchronized GameObject getObject(int id) {
		return objectMap.get(id);
	}
	
	/**
	 * Retorna una copia de la lista ordenada de objetos, segun id.
	 * @return Una lista de objetos
	 */
	public synchronized Collection<GameObject> getObjects(){
		return new LinkedList<>(objectMap.values());
	}
	
	/**
	 * Ejecuta el metodo update de todos los objetos.
	 * 
	 * @see GameObject
	 */
	public void updateObjects() {
		for(GameObject object:getObjects()) {
			object.update();
		}
	}
}
