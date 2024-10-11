package uni.robot.game;

import uni.robot.base.Sprite;

/**
 * Representa un objeto pared dentro de un {@link World}.
 * 
 * @author Fabio Kita
 *
 */
public class WallObject extends GridObject{
	private static final long serialVersionUID = 1L;
	private static final String IMAGE_PATH = RobotLoop.ASSETS_PATH + "WallSprite.png";
	private static final int SUBIMAGE_SIZE = 36;
	private static final int SPRITE_Y_ORIGIN = 21;
	
	transient private Sprite sprite;
	
	private final int direction;
	
	public WallObject(int row, int column, int direction) {
		super(row, column);
		this.direction = direction;
	}

	@Override
	public void onCreate() {	
		this.sprite = new Sprite(getResourceManager()
				.loadImage(IMAGE_PATH), SUBIMAGE_SIZE, SUBIMAGE_SIZE);
		
		sprite.setOrigin(
				sprite.getFrame(0).getWidth()/2,
				SPRITE_Y_ORIGIN
			);
		
		if(getXOffsetSign() == 0) {
			this.setZIndex(getRowY() + getYOffsetSign() * getHalfTileSize());
		}else {
			this.setZIndex(getRowY() - getHalfTileSize());
		}
	}

	@Override
	public void onUpdate() {}

	@Override
	public void onDraw() {
		int hts = getHalfTileSize();
		int offsetX = getXOffsetSign() * hts;
		int offsetY = getYOffsetSign() * hts;
		
		int frameIndex;
		if(offsetX == 0) frameIndex = 0;
		else frameIndex = 1;
		
		drawSprite(sprite, frameIndex, getColumnX() + offsetX, getRowY() + offsetY, 2, 2);
	}

	@Override
	public void onDestroy() {}
	
	/**
	 * Retorna la direccion que la pared esta mirando.
	 * 
	 * @return
	 */
	public int getDirection() {
		return direction;
	}
	
	
	//PRIVATE METHODS
	private int getXOffsetSign() {
		return Direction.getVectorX(getDirection());
	}
	
	private int getYOffsetSign() {
		return Direction.getVectorY(getDirection());
	}
	
	private int getHalfTileSize() {
		return getWorld().getTileSize() / 2;
	}
}
