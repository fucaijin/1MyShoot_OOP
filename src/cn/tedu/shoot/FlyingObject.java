package cn.tedu.shoot;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import java.awt.Graphics;

public abstract class FlyingObject {
	public static final int LIFE = 0;
	public static final int DEAD = 1;
	public static final int REMOVE = 2;
	protected int state = LIFE;

	// 从子类抽取的属性
	protected int x;
	protected int y;
	protected int width;
	protected int height;

	public FlyingObject(int width, int height, int x, int y) {
		this.width = width;
		this.height = height;
		this.x = x;
		this.y = y;
	}

	public FlyingObject(int width, int height) {
		this.width = width;
		this.height = height;
		x = (int) (Math.random() * (World.WIDTH - this.width));
		y = -this.height;
	}

	public abstract void step();

	public boolean outOfBounds() {
		return y >= World.HEIGHT;
	}
	
	public void goDead(){
		state = DEAD;
	}

	/**
	 * 读取图片
	 */
	public static BufferedImage readImage(String fileName) {
		try {
			BufferedImage img = ImageIO.read(FlyingObject.class.getResource(fileName));
			return img;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	/**
	 * 获取图片
	 */
	public abstract BufferedImage getImage();

	/**
	 * 判断是否活着
	 */
	public boolean isLife() {
		return state == LIFE;
	}

	/**
	 * 判断是否死了
	 */
	public boolean isDead() {
		return state == DEAD;
	}

	/**
	 * 判断是否删除
	 */
	public boolean isRemove() {
		return state == REMOVE;
	}

	public void paintObject(Graphics g) {
		g.drawImage(this.getImage(), x, y, null);
	}
	
	public boolean hit(FlyingObject other){
		int x1 = x - other.width;
		int x2 = x + width;
		int y1 = y - other.height;
		int y2 = y + height;
		
		int x = other.x;
		int y = other.y;
		
		return x>=x1 && x<=x2 && y<=y2 && y>=y1;
	}


}
