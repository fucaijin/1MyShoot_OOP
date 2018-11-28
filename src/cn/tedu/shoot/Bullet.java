package cn.tedu.shoot;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Bullet extends FlyingObject {
	private static BufferedImage image;
	static {
		image = readImage("bullet.png");
	}

	private int speed;

	public Bullet(int x, int y) {
		super(8, 14, x, y);
		speed = 2;
	}

	public void step() {
		y-=speed;
	}

	public boolean outOfBounds() {
		return y <= -height;
	}
	
	public BufferedImage getImage() {
		if (isLife()) {
			return image;
		} else if (isDead()) {
			state = REMOVE;
		}
		
		return null;
	}
	
}
