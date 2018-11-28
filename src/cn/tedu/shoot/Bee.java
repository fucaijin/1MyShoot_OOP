package cn.tedu.shoot;

import java.awt.image.BufferedImage;
import java.util.Random;

public class Bee extends FlyingObject implements Award{
	private static BufferedImage[] images;
	static{
		images = new BufferedImage[5];
		for (int i = 0; i < images.length; i++) {
			images[i] = readImage("bee" + i + ".png");
		}
	}
	
	private int xSpeed, ySpeed, awardType;

	public Bee() {
		super(60,50);
		xSpeed = 1;
		ySpeed = 2;
		Random rand = new Random();
		awardType = rand.nextInt(2);
	}
	
	public void step(){
//		y+=ySpeed;
//		if (x<=0) {
//			x+=xSpeed;
//		}else if(x>=World.WIDTH-width){
//			x-=xSpeed;
//		}
		
		x += xSpeed;
		y += ySpeed;
		if (x<=0 || x>=World.WIDTH-width) {
			xSpeed*=-1;
		}
	}
	
	int index = 1;
	public BufferedImage getImage() {
		if (isLife()) {
			return images[0];
		} else if (isDead()) {
			BufferedImage image = images[index++];
			if(index == images.length){
				state = REMOVE;
			}
			return image;
		}		
		return null;
	}

	public int getAwardType() {
		return awardType;
	}
}
