package uni.robot.game.robotinstruction;

import uni.robot.base.Sprite;
import uni.robot.base.SpritePlayer;
import uni.robot.game.ConeObject;
import uni.robot.game.Direction;
import uni.robot.game.RobotObject;

/**
 * Estado de poner un cono.
 * 
 * @author Fabio Kita
 *
 */
public class PutConeState extends RobotState{
	private static final int IMAGE_SCALE = 2;
	private static final double ANIM_SPEED = 0.25;
	
	private static final int PLACE_DOWN_FRAME = 2;
	
	private SpritePlayer spritePlayer;
	
	//Boolean value just in case
	private boolean placedCone = false;
	
	public PutConeState(RobotObject robot) {
		super(robot);
		
		//Set animation
		char dirChar = Direction.getDirectionChar(getRobot().getDirection());
		Sprite sprite = robot.getSprite("Interact" + dirChar);
		spritePlayer = new SpritePlayer(sprite, ANIM_SPEED);
	}

	@Override
	public RobotState handleUpdate() {
		try {
			//If is fast forward, skip animation
			if(isFastForward()) {
				getRobot().redraw();
				handlePutCone();
				finishInstruction();
				return new IdleState(getRobot());
			}
			
			spritePlayer.update();
			
			if(spritePlayer.frameChanged()) {
				getRobot().redraw();
				
				int currentFrame = spritePlayer.getCurrentFrameIndex();
				int lastFrame = spritePlayer.getSprite().getFrameCount()-1;
				
				if(currentFrame == PLACE_DOWN_FRAME) {
					handlePutCone();
				}else if(currentFrame >= lastFrame) {
					finishInstruction();
					return new IdleState(getRobot());
				}
			}
			
			return this;
			
		}catch(NoConeLeftException e) {
			finishInstruction("No hay conos en la bolsita.");
			return new IdleState(getRobot());
		}
	}
	
	@Override
	public void handleDraw() {
		int x = getRobot().getColumnX();
		int y = getRobot().getRowY();
		getRobot().drawSprite(spritePlayer.getSprite(), spritePlayer.getCurrentFrameIndex(), 
				x, y, IMAGE_SCALE, IMAGE_SCALE);
	}
	
	private void handlePutCone() throws NoConeLeftException {
		//If already placed, return;
		if(placedCone) return;
		
		//Throw exception if there's no cone left
		if(getRobot().getConeCount() <= 0) {
			throw new NoConeLeftException();
		}
		
		//Other wise put 
		ConeObject cone = new ConeObject(getRobot().getRow(), getRobot().getColumn());
		getRobot().getWorld().addObject(cone);
		getRobot().setConeCount(getRobot().getConeCount() - 1);
		placedCone = true;
	}
	
	//Local exceptions
	private class NoConeLeftException extends Exception{
		private static final long serialVersionUID = 1L;
	}
}