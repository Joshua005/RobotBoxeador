package uni.robot.game.robotinstruction;

import uni.robot.base.Sprite;
import uni.robot.base.SpritePlayer;
import uni.robot.game.Direction;
import uni.robot.game.RobotObject;

/**
 * Estado de giro derecha.
 * 
 * @author Fabio Kita
 *
 */
public class TurnRightState extends RobotState{
	private static final int IMAGE_SCALE = 2;
	private static final double ANIM_SPEED = -0.25;
	
	private SpritePlayer spritePlayer;
	
	public TurnRightState(RobotObject robot) {
		super(robot);
		
		int fromDir = getRobot().getDirection();
		int toDir = Direction.rotateRight(fromDir);
		
		char fromDirChar = Direction.getDirectionChar(fromDir);
		char toDirChar = Direction.getDirectionChar(toDir);
		
		Sprite sprite = robot.getSprite("Turn"+toDirChar+fromDirChar);
		spritePlayer = new SpritePlayer(sprite, ANIM_SPEED, sprite.getFrameCount()-1);
	}

	@Override
	public RobotState handleUpdate() {
		//If is fast forward, skip animation
		if(isFastForward()) {
			getRobot().redraw();
			return endInstruction();
		}
		
		spritePlayer.update();
		
		if(spritePlayer.frameChanged()) {
			getRobot().redraw();
			
			int currentFrame = spritePlayer.getCurrentFrameIndex();
			int lastFrame = 0;
			if(currentFrame <= lastFrame) {
				return endInstruction();
			}
		}
		
		return this;
	}

	@Override
	public void handleDraw() {
		int x = getRobot().getColumnX();
		int y = getRobot().getRowY();
		getRobot().drawSprite(spritePlayer.getSprite(), spritePlayer.getCurrentFrameIndex(), 
				x, y, IMAGE_SCALE, IMAGE_SCALE);
	}
	
	private RobotState endInstruction() {
		getRobot().setDirection(Direction.rotateRight(getRobot().getDirection()));
		finishInstruction();
		return new IdleState(getRobot());
	}
}
