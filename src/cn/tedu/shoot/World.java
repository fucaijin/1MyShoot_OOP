package cn.tedu.shoot;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Random;
import java.util.Arrays;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class World extends JPanel {
	// 窗口尺寸
	public static final int WIDTH = 400;
	public static final int HEIGHT = 700;

	private Sky sky = new Sky();
	private Hero hero = new Hero();
	private FlyingObject[] enermies = {};
	private Bullet[] bullets = {};

	public FlyingObject nextOne() {
		Random random = new Random();
		int type = random.nextInt(20);
		if (type < 5) {
			return new Bee();
		} else if (type < 12) {
			return new Airplane();
		} else {
			return new BigAirplane();
		}
	}

	int enterIndex = 0;

	public void enterAction() {
		enterIndex++;
		if (enterIndex % 40 == 0) {
			FlyingObject flying = nextOne();
			enermies = Arrays.copyOf(enermies, enermies.length + 1);
			enermies[enermies.length - 1] = flying;
		}
	}

	private int shootIndex = 0;
	public void shootAction() {
		shootIndex++;
		if (shootIndex % 30 == 0) {
			Bullet[] bs = hero.shoot();
			bullets = Arrays.copyOf(bullets, bullets.length+bs.length);
			System.arraycopy(bs, 0, bullets, bullets.length-bs.length, bs.length);
		}
	}

	public void stepAction() {
		sky.step();
		for (int i = 0; i < bullets.length; i++) {
			bullets[i].step();
		}
		
		for (int i = 0; i < enermies.length; i++) {
			enermies[i].step();
		}
	}
	
	public void outOfBoundsAction(){
		Bullet[] bulletLive = new Bullet[bullets.length];
		int index = 0;
		for (int i = 0; i < bullets.length; i++) {
			if (!bullets[i].outOfBounds() && !bullets[i].isRemove()) {
				bulletLive[index] = bullets[i];
				index++;
			}
		}
		bullets = Arrays.copyOf(bulletLive, index);
		
		FlyingObject[] enermiesLive = new FlyingObject[enermies.length];
		index = 0;
		for (int i = 0; i < enermies.length; i++) {
			if (!enermies[i].outOfBounds() && !enermies[i].isRemove()) {
					enermiesLive[index] = enermies[i];
					index++;
			}
		}
		enermies = Arrays.copyOf(enermiesLive, index);
		
	}
	
	int score = 0;
	public void bulletBangAction(){
		for (int i = 0; i < bullets.length; i++) {
			Bullet bullet = bullets[i];
			for (int j = 0; j < enermies.length; j++) {
				FlyingObject enermy = enermies[j];
				if (bullet.isLife() && enermy.isLife() && enermy.hit(bullet)) {
					bullet.goDead();
					enermy.goDead();
					
					if (enermy instanceof Enermy) {
						Enermy e = (Enermy)enermy;
						score += e.getScore();
					}
					
					if (enermy instanceof Award) {
						Award award = (Award) enermy;
						switch (award.getAwardType()) {
						case Award.LIFE:
							hero.addLife();
							break;
						case Award.DOUBLE_FIRE:
							hero.addDoubleFire();
							break;
						}
					}
				}
			}
		}
	}
	
	
	public void heroBangAction(){
		for (int i = 0; i < enermies.length; i++) {
			FlyingObject enermy = enermies[i];
			if (enermy.isLife() && hero.isLife() && enermy.hit(hero)) {
				enermy.goDead();
				hero.subtracatLife();
				hero.clearDoubleFire();
			}
		}
		
		if(hero.getLife() <=0 ){
			state = GAMEOVER;
		}
	}
	
	public void checkGameOverAction() {
		if (hero.getLife() <= 0) {
		}
	}
	
	public static final int START = 0;
	public static final int RUNNING = 1;
	public static final int PAUSE = 2;
	public static final int GAMEOVER = 3;
	
	private  int state = START;
	
	private static BufferedImage start;
	private static BufferedImage pause;
	private static BufferedImage gameover;
	
	static{
		start = FlyingObject.readImage("start.png");
		pause = FlyingObject.readImage("pause.png");
		gameover = FlyingObject.readImage("gameover.png");
	}
	
	public void action() {
		MouseAdapter mouseAdapter = new MouseAdapter(){
			public void mouseMoved(MouseEvent e) {
				if (state == RUNNING) {
					hero.moveTo(e.getX(), e.getY());
				}
			}
			
			public void mouseEntered(MouseEvent e) {
				if (state == PAUSE) {
					state = RUNNING;
				}
			}
			
			public void mouseExited(MouseEvent e) {
				if (state == RUNNING) {
					state = PAUSE;
				}
			}
			
			public void mouseClicked(MouseEvent e) {
				switch (state) {
				case START:
					state = RUNNING;
					break;
				case GAMEOVER:
					score = 0;
					sky = new Sky();
					hero = new Hero();
					enermies = new FlyingObject[0];
					bullets = new Bullet[0];
					state = START;
					break;
				}
			}
			
		};
		
		addMouseListener(mouseAdapter);
		addMouseMotionListener(mouseAdapter);
		
		Timer timer = new Timer();
		int interval = 10;	// 以毫秒为单位
		timer.schedule(new TimerTask() {
			public void run() {
				if (state == RUNNING) {
					enterAction();
					shootAction();
					stepAction();
					outOfBoundsAction();
					bulletBangAction();	// 子弹与敌人的碰撞
					heroBangAction();
					checkGameOverAction();
				}
				repaint(); // 重绘paint()
			}

		}, interval, interval);
	}
	

	public void paint(Graphics g) {
		sky.paintObject(g);
		hero.paintObject(g);
		for (int i = 0; i < enermies.length; i++) {
			enermies[i].paintObject(g);
		}

		for (int i = 0; i < bullets.length; i++) {
			bullets[i].paintObject(g);
		}
		
		g.drawString("LIFE: " + hero.getLife(), 10	,25 );
		g.drawString("SCORE: " + score, 10, 45);
		
		switch (state) {
		case START:
			g.drawImage(start, 0, 0, null);
			break;
		case PAUSE:
			g.drawImage(pause, 0, 0, null);
			break;
		case GAMEOVER:
			g.drawImage(gameover, 0, 0, null);
			break;
		}
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		World world = new World();
		frame.add(world);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(WIDTH, HEIGHT);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		world.action();
	}
	
	

}
