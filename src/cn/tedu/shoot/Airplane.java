package cn.tedu.shoot;

import java.awt.image.BufferedImage;

public class Airplane extends FlyingObject implements Enermy{
	private static BufferedImage[] images;
	static{
		images = new BufferedImage[5];
		for (int i = 0; i < images.length; i++) {
			images[i] = readImage("airplane" + i + ".png");
		}
	}
	
	private int speed;

	public Airplane() {
		super(49, 36);
		speed = 2;
	}
	
	public void step(){
		y+=speed;
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
		
		/*
		 * 					index = 1
		 * 		images[1]	index = 2	����images[1]
		 * 		images[2]	index = 3	����images[2]
		 * 		images[3]	index = 4	����images[3]
		 * 		images[4]	index = 5	if����state=REMOVE	����images[4]
		 * 		
		 */
	}

	public int getScore() {
		return 1;
	}
}
