package cn.tedu.shoot;

import java.awt.image.BufferedImage;

public class Hero extends FlyingObject {
	private static BufferedImage[] images;
	static {
		images = new BufferedImage[2];
		for (int i = 0; i < images.length; i++) {
			images[i] = readImage("hero" + i + ".png");
		}
	}

	public void subtracatLife(){
		life--;
	}
	
	public void clearDoubleFire() {
		doubleFire = 0;
	}
	
	private int life, doubleFire;

	public Hero() {
		super(97, 124, 140, 400);
		life = 3;
		doubleFire = 0;
	}
	
	public void addLife(){
		life++;
	}
	
	public int getLife(){
		return life;
	}
	
	public void addDoubleFire(){
		doubleFire+=40;
	}

	public void moveTo(int x, int y) {
		this.x = x - this.width/2;
		this.y = y - this.height/2;
	}

	// 切换图片
	public void step() {
	}

	int index = 0;

	public BufferedImage getImage() {
		return images[index++ % images.length]; // 返回来回切换的图片
		// 注意： 先计算index++%images.length，再计算index++
		// index = 0;
		// 10ms images[0] index = 1;
		// 20ms images[1] index = 2;
		// 30ms images[0] index = 3;
		// 40ms images[1] index = 4;
		// 50ms images[0] index = 5;
		// 60ms images[1] index = 6;
	}

	public Bullet[] shoot() {
		int xStep = this.width/4;
		int yStep = 20;
		Bullet[] bs;
		if (doubleFire > 0) {
			bs = new Bullet[2];
			bs[0] = new Bullet(x+1*xStep, y-yStep);
			bs[1] = new Bullet(x+3*xStep, y-yStep);
			doubleFire-=2;
		} else {
			bs = new Bullet[1];
			bs[0] = new Bullet(x+2*xStep, y-yStep);
		}
		return bs;
	}
}
