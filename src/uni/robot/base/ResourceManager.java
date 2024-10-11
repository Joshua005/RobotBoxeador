package uni.robot.base;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;


/**
 * Objeto que compone a {@link GameLoop}, responsable de cargar y almacenar recursos (ej. imagenes).
 * 
 * @author Fabio Kita
 *
 */
public class ResourceManager {
	private Map<String, Object> resourceMap;
	
	public ResourceManager() {
		this.resourceMap = new HashMap<>();
	}
	
	/**
	 * Carga y retorna un archivo de imagen como un BufferedImage. Si la imagen fue cargado previamente, 
	 * este retorna la imagen cacheada, en lugar de recargarla.
	 * 
	 * @param filename El nombre de la imagen.
	 * @return un objeto {@link BufferedImage}
	 */
	public BufferedImage loadImage(String filename) {
		//FORMAT FILENAME
		String path = formatFileName(filename);
		
		//IF ALREADY LOADED, RETURN.
		if(resourceMap.containsKey(path)) 
			return (BufferedImage) resourceMap.get(filename);
		
		try {
			//FIND, LOAD AND RETURN THE RESOURCE
			URL urlpath = getResourceURL(path);
			BufferedImage image = ImageIO.read(urlpath);
			resourceMap.put(path, image);
			return image;
		} catch (IOException e) {
			throw new RuntimeException(String.format("Could not load the resource '%s'.", path));
		}
	}
	
	/**
	 * Formatea el nombre de archivo para que sean consistentes.
	 * 
	 * @param filename un nombre de archivo
	 * 
	 * @return el nombre de archivo formateado
	 */
	private String formatFileName(String filename) {
		return filename.trim().replace('\\', '/');
	}
	
	/**
	 * Consigue un objeto {@link URL} que senhala al archivo especificado.
	 * 
	 * @param path el nombre del archivo
	 * 
	 * @return un objeto {@link URL}
	 */
	private URL getResourceURL(String path) {
		URL urlpath = getClass().getClassLoader().getResource(path);
		if(urlpath == null) 
			throw new RuntimeException(String.format("The resource '%s' does not exists.", path));
		return urlpath;
	}
}

