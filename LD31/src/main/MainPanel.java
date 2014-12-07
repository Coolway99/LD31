package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.HashMap;

import javax.swing.JPanel;

import main.player.Ship;

public class MainPanel extends JPanel{
	private static final long serialVersionUID = -892554275836568837L;
	
	private Image background, bullet, enemy;
	private Ship player;
	private int playerSpeed = 5;
	private HashMap<Integer, Ship> enemylist = new HashMap<>(25);
	private HashMap<Integer, Bullet> bulletlist = new HashMap<>(200);
	private HashMap<Integer, Integer> bulletRemove = new HashMap<>(20);
	private HashMap<Integer, Integer> enemyRemove = new HashMap<>(5);
	//Front 20 are vertical, last 15 are horizontal
	private Laser[] laserList = new Laser[35];
	public MainPanel(){
		super();
		Toolkit tk = Toolkit.getDefaultToolkit();
		background = tk.createImage(Main.class.getClassLoader().getResource("assets/grid.png"));
		player = new Ship(tk.createImage(Main.class.getClassLoader().getResource("assets/ship.png")));
		bullet = tk.createImage(Main.class.getClassLoader().getResource("assets/bullet.png"));
		enemy = tk.createImage(Main.class.getClassLoader().getResource("assets/enemy.png"));
		for(int x = 0; x < 20; x++){
			laserList[x] = new Laser(new Color(0xAAFF0000, true), new Color(0xAA00FF00, true),
				new Rectangle(x*(805/20), -20, (805/20), 700));
		}
		for(int x = 20; x < 35; x++){
			laserList[x] = new Laser(new Color(0xAAFF0000, true), new Color(0, true),
					new Rectangle(-20, (x-20)*(600/15), 900, (600/15)));
		}
	}
	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		Ship p;
		
		try{
			synchronized(player){
				p = player.clone();
			}
		} catch(NullPointerException e){
			p = null;
		}
		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(background, 0, 0, null);
		for(int x = 0; x < 35; x++){
			laserList[x].draw(g2);
		}
		if(p != null){
			p.draw(g2);
		}
		for(Ship enemy : enemylist.values()){
			if(enemy != null){
				enemy.draw(g2);
			}
		}
		for(Bullet x : bulletlist.values()){
			x.draw(g2);
		}
	}
	/**
	 * UP:0 DOWN:1 LEFT:2 RIGHT:3 LEFTCLICK:4 RIGHTCLICK:5
	 * @param keyCode The keycode entered
	 */
	public void update(boolean[] keycode, Point mospos){
		synchronized(player){
			if(keycode[0] && player.getY() - playerSpeed >= -20){
				player.setY(player.getY() - playerSpeed);
			} else if(keycode[0]){
				player.setY(600);
			}
			if(keycode[1] && player.getY() + playerSpeed <= 620){
				player.setY(player.getY() + playerSpeed);
			} else if(keycode[1]){
				player.setY(0);
			}
			if(keycode[2] && player.getX() - playerSpeed >= -20){
				player.setX(player.getX() - playerSpeed);
			} else if(keycode[2]){
				player.setX(800);
			}
			if(keycode[3] && player.getX() + playerSpeed <= 805){
				player.setX(player.getX() + playerSpeed);
			} else if(keycode[3]){
				player.setX(0);
			}
			if(keycode[4]){
				int key = Main.ran.nextInt();
				while(bulletlist.containsKey(key)){
					key = Main.ran.nextInt();
				}
				double radAngle = Math.atan2(player.getY() - mospos.getY(), player.getX() - mospos.getX());
				bulletlist.put(bulletlist.size(), new Bullet(bullet, player.getX(), player.getY(),
						(int)(-8*Math.cos(-radAngle)), (int)(-8*Math.sin(radAngle)), 5));
			}
			for(Integer y : bulletlist.keySet()){
				Bullet x = bulletlist.get(y);
				x.move();
				try{
					if(player.getBounds().intersects(x.getBounds())){
						if(x.Grace <= 0){
							bulletRemove.put(bulletRemove.size(), y);
							player.kill();
							player = null;
						}
					}
				} catch (NullPointerException e){
					Main.update.cancel();
				}
				for(Integer y2 : enemylist.keySet()){
					Ship enemy = enemylist.get(y2);
					try{
						if(enemy.getBounds().intersects(x.getBounds())){
							if(x.Grace <= 0){
								bulletRemove.put(bulletRemove.size(), y);
								enemy.kill();
							}
						}
					} catch(NullPointerException e){
						enemyRemove.put(enemyRemove.size(), y2);
					}
				}
				if(!x.getBounds().intersects(0, 0, 805, 630)){
					bulletRemove.put(bulletRemove.size(), y);
				}
			}
			for(int x = 0; x < 35; x++){
				try{
					if(laserList[x].update().intersects(player.getBounds())){
						player.kill();
						player = null;
					}
				} catch(NullPointerException e){
					if(player == null){
						Main.update.cancel();
					}
				}
			}
			for(Integer y : bulletRemove.keySet()){
				bulletlist.remove(bulletRemove.get(y));
				bulletRemove.remove(y);
			}
			for(Integer y: enemyRemove.keySet()){
				enemylist.remove(enemyRemove.get(y));
				enemyRemove.remove(y);
			}
		}
	}
	@Override
	public Dimension getPreferredSize(){
		return new Dimension(800, 600);
	}
	public void spawnEnemy(int x, int y){
		int key = Main.ran.nextInt();
		while(enemylist.containsKey(key)){
			key = Main.ran.nextInt();
		}
		enemylist.put(key, new Ship(enemy, new Rectangle(x, y, enemy.getWidth(null), enemy.getHeight(null))));
	}
}
class Bullet{
	private Image i;
	private Rectangle bounds;
	private int xSpeed;
	private int ySpeed;
	public int Grace;
	public Bullet(Image i, int startX, int startY, int xSpeed, int ySpeed, int gracePeriod){
		this.i = i;
		this.bounds = new Rectangle(startX, startY, i.getWidth(null), i.getHeight(null));
		this.xSpeed = xSpeed;
		this.ySpeed = ySpeed;
		this.Grace = gracePeriod;
	}
	public Rectangle getBounds(){return this.bounds;}
	public void move(){
		bounds.setLocation(bounds.x+xSpeed, bounds.y+ySpeed);
		if(Grace > 0){
			Grace--;
		}
	}
	public void draw(Graphics2D g){
		g.drawImage(i, bounds.x, bounds.y, null);
	}
}
class Laser{
	private Color activated, deactivated;
	private double transition = 0, transAmount = 0;
	private Rectangle bounds;
	private int ticks;
	public Laser(Color on, Color off, Rectangle bounds){
		this.activated = on;
		this.deactivated = off;
		this.bounds = bounds;
	}
	public void draw(Graphics2D g2){
		Color c = g2.getColor();
		int r, g, b, a;
		r = (int)((activated.getRed() * transition) + (deactivated.getRed() * (1-transition)));
		g = (int)((activated.getGreen() * transition) + (deactivated.getGreen() * (1-transition)));
		b = (int)((activated.getBlue() * transition) + (deactivated.getBlue() * (1-transition)));
		a = (int)((activated.getAlpha() * transition) + (deactivated.getAlpha() * (1-transition)));
		g2.setColor(new Color(((a<<24)+(r<<16)+(g<<8)+b), true));
		g2.fill(this.bounds);
		g2.setColor(c);
	}
	public void startTransition(boolean on, int ticks){
		if(on){
			transAmount = (1-transition)/ticks;
		} else {
			transAmount = transition/ticks*-1;
		}
		this.ticks = ticks;
	}
	public Rectangle update(){
		if(ticks > 0){
			transition += transAmount;
			ticks--;
		}
		if(transition > 0.9){
			return bounds;
		}
		return null;
	}
}