package application;

public class Player {
	//first class im making so that this project isnt just a bunch of scrambled code in a big file
	public double x;
    public double y;
    public double speed;
    
    public Player(double x, double y, double speed) {
        this.x = x;
        this.y = y;
        this.speed = speed;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }
    
    public void updateMovement(boolean goUp, boolean goDown, boolean goLeft, boolean goRight) {
        //Code for moving player
        if (goUp && goRight && goLeft && goDown){
        
        } else {
        	if (goUp && goDown && goLeft) {
        		x -= speed;
        	} else if (goUp && goDown && goRight) {
        		x += speed;
        	} else if (goUp && goLeft && goRight) {
        		y -= speed;
        	} else if (goDown && goLeft && goRight) {
        		y += speed;
        	} else if (goUp && goRight) {
                y -= speed/Math.sqrt(2);
                x += speed/Math.sqrt(2);
            } else if (goUp && goLeft) {
                y -= speed/Math.sqrt(2);
                x -= speed/Math.sqrt(2);
            } else if (goDown && goLeft) {
                y += speed/Math.sqrt(2);
                x -= speed/Math.sqrt(2);
            } else if (goDown && goRight) {
                y += speed/Math.sqrt(2);
                x += speed/Math.sqrt(2);
            } else {
                if (goUp) y -= speed;
                if (goDown) y += speed;
                if (goLeft) x -= speed;
                if (goRight) x += speed;
            }
        }
    }
    
}
