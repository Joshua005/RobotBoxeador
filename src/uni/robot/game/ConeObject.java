package uni.robot.game;

import java.util.List;
import java.util.stream.Collectors;

import uni.robot.base.Sprite;

/**
 * Objeto que representa un cono en el mundo.
 * 
 * @author Fabio Kita
 *
 */
public class ConeObject extends GridObject {
	private static final long serialVersionUID = 1L;
	private static final String CONE_IMAGE_PATH = RobotLoop.ASSETS_PATH + "ConeSprite.png";
	private static final int CONE_BASE_OFFSET = 8;
	private static final int CONE_STACK_OFFSET = -4;
	
	transient private Sprite sprite;
	private int coneIndex;
	
	public ConeObject(int row, int column) {
		super(row, column);
	}
	
	@Override
	public void onCreate() {
		sprite = new Sprite(getResourceManager().loadImage(CONE_IMAGE_PATH));
		sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()-6);
		
		//get Index
		coneIndex = getConeIndex();
		
		//Calculate z index
		setZIndex(getRowY()+CONE_BASE_OFFSET+coneIndex);
	}
	
	@Override
	public void onUpdate() {}

	@Override
	public void onDraw() {
		final int SCALE = 2;
		drawSprite(sprite, 0, getColumnX(), getYPosition(), SCALE, SCALE);
	}

	@Override
	public void onDestroy() { }
	
	/**
	 * Retorna la posicion y que el cono debe de ser dibujado.
	 * 
	 * @return la posicion y 
	 */
	private int getYPosition() {
		//Added offset to make it draw a little lower
		return getRowY()+CONE_BASE_OFFSET+coneIndex*CONE_STACK_OFFSET;
	}

	/**
	 * Retorna el indice de este cono dentro de los conos en la misma posicion.
	 * Principalmente para calcular su posicion en y.
	 * 
	 * @return Indice de este cono
	 */
	private int getConeIndex() {
		List<ConeObject> cones = this.getWorld().getObjectsInPosition(getRow(), getColumn()).stream()
				.filter(o->o instanceof ConeObject)
				.map(c->(ConeObject) c)
				.collect(Collectors.toList());
		
		int index = cones.indexOf(this);
		if(index == -1) return cones.size();
		else return index;
	}
}
