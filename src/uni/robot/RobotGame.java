package uni.robot;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;

import uni.robot.game.RobotLoop;

/**
 * Clase estatica que contiene el {@link RobotLoop}.
 * 
 * @author Fabio Kita
 *
 */
public class RobotGame{
	private static String CONFIG_FILE = "config.properties";
	private static RobotLoop loop;
	
	/**
	 * Consigue el {@link RobotLoop}. Se crea uno nuevo si no existe.
	 * @return
	 */
	public static RobotLoop getRobotLoop() {
		if(loop == null) {
			loop = readPropertiesAndCreateNewLoop();
			loop.startGameLoop();
		}
		return loop;
	}
	
	/**
	 * Lee el archivo de configuracion y setea los parametros de {@link RobotLoop}.
	 * @return un nuevo RobotLoop con los parametros definidos.
	 */
	private static RobotLoop readPropertiesAndCreateNewLoop() {
		Properties props = new Properties();
		
		final String DEFAULT_UI_SCALING = "1.0";
		
		final String DEFAULT_ASSETS_PATH = "uni/assets/";
		
		final boolean DEFAULT_SHOW_SPEED_PANEL = true;
		final int DEFAULT_INITIAL_MODE = RobotLoop.PAUSED;
		
		final int DEFAULT_UPS = 60;
		final int DEFAULT_FPS = 60;
		
		final int DEFAULT_PLAY_MODE_UPDATE_COUNTS_PAUSED = 1;
		final int DEFAULT_PLAY_MODE_UPDATE_COUNTS_NORMAL = 1;
		final int DEFAULT_PLAY_MODE_UPDATE_COUNTS_FF = 20;
		final int DEFAULT_PLAY_MODE_UPDATE_COUNTS_FFF = 100;
		final int[] DEFAULT_PLAY_MODE_UPDATE_COUNTS = {
				DEFAULT_PLAY_MODE_UPDATE_COUNTS_PAUSED, 
				DEFAULT_PLAY_MODE_UPDATE_COUNTS_NORMAL, 
				DEFAULT_PLAY_MODE_UPDATE_COUNTS_FF, 
				DEFAULT_PLAY_MODE_UPDATE_COUNTS_FFF
			};
		
		try(FileInputStream in = new FileInputStream(CONFIG_FILE)){
			props.load(in);
			
			//Read properties
			String assetsPath = props.getProperty("root_assets_path", DEFAULT_ASSETS_PATH);
			
			String uiScaling = props.getProperty("image_scaling", DEFAULT_UI_SCALING);
			
			boolean showSpeedPanel = readBool(props, "show_speed_panel", DEFAULT_SHOW_SPEED_PANEL);
			
			int initialSpeed = readSpeed(props, "initial_speed", DEFAULT_INITIAL_MODE);
			if(initialSpeed == RobotLoop.PAUSED && showSpeedPanel == false) initialSpeed = RobotLoop.NORMAL;
			
			int ups = readInt(props, "ups", DEFAULT_UPS);
			int fps = readInt(props, "fps", DEFAULT_FPS);
			
			int[] playModeUpdateCounts = {
				readInt(props, "paused_update_count", DEFAULT_PLAY_MODE_UPDATE_COUNTS_PAUSED),
				readInt(props, "normal_update_count", DEFAULT_PLAY_MODE_UPDATE_COUNTS_NORMAL),
				readInt(props, "fast_forward_update_count", DEFAULT_PLAY_MODE_UPDATE_COUNTS_FF),
				readInt(props, "fast_fast_forward_update_count", DEFAULT_PLAY_MODE_UPDATE_COUNTS_FFF),
			};
			
			//Set values
			setUiScaling(uiScaling);
			return new RobotLoop(assetsPath, showSpeedPanel, initialSpeed, playModeUpdateCounts, ups, fps);
		} catch (FileNotFoundException e) {
			//RETURN DEFAULT VALUES
			setUiScaling(DEFAULT_UI_SCALING);
			return new RobotLoop(
					DEFAULT_ASSETS_PATH, 
					DEFAULT_SHOW_SPEED_PANEL, 
					DEFAULT_INITIAL_MODE, 
					DEFAULT_PLAY_MODE_UPDATE_COUNTS, 
					DEFAULT_UPS, 
					DEFAULT_FPS
				);
		} catch (Exception e) {
			System.err.println("Error loading " + CONFIG_FILE + " file." + 
					e.getMessage() + " Using default values.");
			
			//RETURN DEFAULT VALUES
			setUiScaling(DEFAULT_UI_SCALING);
			return new RobotLoop(
					DEFAULT_ASSETS_PATH, 
					DEFAULT_SHOW_SPEED_PANEL, 
					DEFAULT_INITIAL_MODE, 
					DEFAULT_PLAY_MODE_UPDATE_COUNTS, 
					DEFAULT_UPS, 
					DEFAULT_FPS
				);
		}
	}
	
	private static void setUiScaling(String value) {
		System.setProperty("sun.java2d.uiScale", value);
	}
	
	private static int readInt(Properties props, String key, int defaultValue) {
		try {
			String value = props.getProperty(key);
			if(value == null) return defaultValue;
			return Integer.parseInt(value);
		}catch(NumberFormatException e) {
			System.err.println("Invalid value for " + key + ", using default value.");
			return defaultValue;
		}
	}
	
	private static boolean readBool(Properties props, String key, boolean defaultValue) {
		String value = props.getProperty(key);
		if(value == null) return defaultValue;
		return Boolean.parseBoolean(value);
	}
	
	private static int readSpeed(Properties props, String key, int defaultValue) {
		String value = props.getProperty(key);
		if(value == null) return defaultValue;
		
		switch(value){
			case "PAUSED": return RobotLoop.PAUSED;
			case "NORMAL": return RobotLoop.NORMAL;
			case "FAST_FORWARD": return RobotLoop.FAST_FORWARD;
			case "FAST_FAST_FORWARD": return RobotLoop.FAST_FAST_FORWARD;
			default: {
				System.err.println("Invalid value for " + key + ", using default value.");
				return defaultValue;
			}
		}
	}
}
