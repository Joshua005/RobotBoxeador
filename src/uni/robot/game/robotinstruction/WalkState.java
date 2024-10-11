package uni.robot.game.robotinstruction;

import uni.robot.base.SpritePlayer;
import uni.robot.game.Direction;
import uni.robot.game.RobotObject;

/**
 * Estado de avanzar.
 * 
 * @author Fabio Kita
 *
 */
public class WalkState extends RobotState{
	private static final double MOVE_PERCENTAGE = 0.125;
	private static final double DISTANCE_SNAP = 1.5;
	
	private static final int IMAGE_SCALE = 2;
	private static final double ANIM_SPEED = 0.25;
	
	//The position to draw in the window
	private double x;
	private double y;
	
	//The objective position in window
	private int xTo;
	private int yTo;
	
	//The objective position in grid
	private int rowTo;
	private int columnTo;
	
	//Whether or not the action will fail
	private boolean fail = false;
	
	//Animation Player
	private SpritePlayer spritePlayer;
	
	public WalkState(RobotObject robot) {
		super(robot);
		
		x = getRobot().getColumnX();
		y = getRobot().getRowY();
		
		int direction = getRobot().getDirection();
		
		rowTo = getRobot().getRow() + Direction.getVectorY(direction);
		columnTo = getRobot().getColumn() + Direction.getVectorX(direction);
		
		xTo = getRobot().getWorld().columnToX(columnTo);
		yTo = getRobot().getWorld().rowToY(rowTo);
		
		if(!getRobot().canMoveForward()) fail = true;
		
		char dirChar = Direction.getDirectionChar(direction);
		String spriteName = "Walk" + dirChar;
		spritePlayer = new SpritePlayer(getRobot().getSprite(spriteName), ANIM_SPEED);
	}

	@Override
	public RobotState handleUpdate() {
		if(fail) {
			finishInstruction("Bonk!!");
			return new IdleState(getRobot());
		}
		
		if(isFastForward()) {
			x = xTo;
			y = yTo;
		}else {
			x = lerp(x, xTo, MOVE_PERCENTAGE);
			y = lerp(y, yTo, MOVE_PERCENTAGE);
		}
		
		getRobot().setZIndex((int) Math.round(y));
		spritePlayer.update();
		getRobot().redraw();
		
		if(Math.round(x) == xTo && Math.round(y) == yTo) {
			//Finish Walk
			getRobot().setGridPosition(rowTo, columnTo);
			finishInstruction();
			return new IdleState(getRobot());
		}
		
		return this;
	}

	@Override
	public void handleDraw() {
		int x = (int) Math.round(this.x);
		int y = (int) Math.round(this.y);
		getRobot().drawSprite(spritePlayer.getSprite(), spritePlayer.getCurrentFrameIndex(), 
				x, y, IMAGE_SCALE, IMAGE_SCALE);
	}
	
	private double lerp(double i, double f, double p) {
		if(Math.abs(f-i) < DISTANCE_SNAP) return f;
		return i + (f-i)*p;
	}
}