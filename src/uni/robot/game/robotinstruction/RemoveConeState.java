package uni.robot.game.robotinstruction;

import java.util.List;

import uni.robot.base.Sprite;
import uni.robot.base.SpritePlayer;
import uni.robot.game.ConeObject;
import uni.robot.game.Direction;
import uni.robot.game.RobotObject;

/**
 * Estado de quitar un cono.
 * 
 * @author Fabio Kita
 *
 */
public class RemoveConeState extends RobotState{
	private static final int IMAGE_SCALE = 2;
	private static final double ANIM_SPEED = 0.25;
	
	private static final int REMOVE_FRAME = 2;
	
	private SpritePlayer spritePlayer;
	
	//Boolean value just in case
	private boolean removedCone = false;
	
	public RemoveConeState(RobotObject robot) {
		super(robot);

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
				handleRemoveCone();
				finishInstruction();
				return new IdleState(getRobot());
			}
			
			spritePlayer.update();
			
			if(spritePlayer.frameChanged()) {
				getRobot().redraw();
				
				int currentFrame = spritePlayer.getCurrentFrameIndex();
				if(currentFrame == REMOVE_FRAME && !removedCone) {
					handleRemoveCone();
				}else if(currentFrame >= spritePlayer.getSprite().getFrameCount()-1) {
					finishInstruction();
					return new IdleState(getRobot());
				}
			}
		}catch(NoConeInPositionException e) {
			finishInstruction("No hay ningun cono para guardar!");
			return new IdleState(getRobot());
		}catch(NoSpaceForConeException e) {
			finishInstruction("No hay mas espacio en la bolsita!");
			return new IdleState(getRobot());
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
	
	private void handleRemoveCone() throws NoConeInPositionException, NoSpaceForConeException {
		if(removedCone) return;
		
		List<ConeObject> coneList = getRobot().getConeInPosition();
		
		//Check for errors
		if(coneList.isEmpty()) {
			throw new NoConeInPositionException();
		}else if(getRobot().getConeCount() >= getRobot().getCapacity()) {
			throw new NoSpaceForConeException();
		}
		
		//Remove cone
		coneList.get(coneList.size()-1).destroy();
		getRobot().setConeCount(getRobot().getConeCount() + 1);
		removedCone = true;
	}
	
	//Local Exception
	private class NoConeInPositionException extends Exception{
		private static final long serialVersionUID = 1L;
	}
	
	private class NoSpaceForConeException extends Exception{
		private static final long serialVersionUID = 1L;
	}
}