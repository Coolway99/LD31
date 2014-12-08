package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
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
	
	private Image background, bullet, enemy, playerImage;
	private Ship player;
	private final int playerSpeed = 5;
	private final int enemySpeed = 3;
	private HashMap<Integer, Ship> enemylist = new HashMap<>(25);
	private HashMap<Integer, Bullet> bulletlist = new HashMap<>(200);
	//Front 20 are vertical, last 15 are horizontal
	private Laser[] laserList = new Laser[35];
	public MainPanel(){
		super();
		Toolkit tk = Toolkit.getDefaultToolkit();
		background = tk.createImage(Main.class.getClassLoader().getResource("assets/grid.png"));
		playerImage = tk.createImage(Main.class.getClassLoader().getResource("assets/ship.png"));
		bullet = tk.createImage(Main.class.getClassLoader().getResource("assets/bullet.png"));
		enemy = tk.createImage(Main.class.getClassLoader().getResource("assets/enemy.png"));
		for(int x = 0; x < 20; x++){
			laserList[x] = new Laser(new Color(0xAAFF0000, true), new Color(0xAA00FF00, true),
					new Rectangle(x*(805/20), -20, (805/20), 700));
		}
		for(int x = 20; x < 35; x++){
			laserList[x] = new Laser(new Color(0xCCFF0000, true), new Color(0, true),
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
		if(Main.menu.Menu){
			if(Main.menu.instructions){
				g2.setColor(Color.WHITE);
				g2.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
				g2.drawString("The objective of this game is to not die.", 50, 200);
				g2.drawString("As you play the lasers (columns and rows) will turn red and", 50, 220);
				g2.drawString("green. Green lasers you can pass through unharmed but red", 50, 240);
				g2.drawString("lasers will kill you. There are also enemies that will", 50, 260);
				g2.drawString("be spawning and shooting at you. Try not to die!", 50, 280);
				g2.drawString("Also, the walls can help you go from one side of the", 50, 300);
				g2.drawString("level to the other!", 50, 320);
				
				g2.drawString("Controls:WASD or Arrow keys. Click to shoot at the point you ", 50, 360);
				g2.drawString("are clicking", 50, 380);
			} else {
				g2.setColor(Color.WHITE);
				g2.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
				g2.drawString("Play", 300, 200);
				g2.drawString("Instructions", 300, 300);
				if(Main.menu.selection == 0){
					g2.fillRect(260, 190, 20, 20);
				} else {
					g2.fillRect(250, 290, 20, 20);
				}
			}
		}
	}
	/**
	 * UP:0 DOWN:1 LEFT:2 RIGHT:3 LEFTCLICK:4 RIGHTCLICK:5 ENTER:6
	 * @param keyCode The keycode entered
	 */
	public void update(boolean[] keycode, Point mospos){
		if(!Main.menu.Menu){
			synchronized(player){
				player.update();
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
					Bullet x = player.fire(mospos, bullet);
					if(x != null){
						bulletlist.put(key, x);
					}
				}
				
				for(Integer y : enemylist.keySet()){
					Ship enemy = enemylist.get(y);
					try{
						enemy.update();
						enemy.setX(enemy.getX()+(player.getX() > enemy.getX()? enemySpeed : -enemySpeed));
						enemy.setY(enemy.getY()+(player.getY() > enemy.getY()? enemySpeed : -enemySpeed));
						Bullet bul = enemy.fire(player.getBounds().getLocation(), bullet);
						if(bul != null){
							int key = Main.ran.nextInt();
							while(bulletlist.containsKey(key)){key = Main.ran.nextInt();}
							bulletlist.put(key, bul);
						}
					} catch(NullPointerException e){}
				}
				
				Integer[] bulletKeys = bulletlist.keySet().toArray(new Integer[0]);
				for(int z = 0; z < bulletKeys.length; z++){
					int y= bulletKeys[z];
					Bullet x = bulletlist.get(y);
					try{
						x.move();
						if(player.getBounds().intersects(x.getBounds())){
							if(0 >= x.Grace){
								bulletlist.remove(y);
								killPlayer();
							}
						}
					} catch (NullPointerException e){
						if(player == null){
							Main.updateTask.cancel();
						}
					}
					Integer[] w2 = enemylist.keySet().toArray(new Integer[0]);
					for(int z2 = 0; z2 < w2.length; z2++){
						int y2 = w2[z2];
						Ship enemy = enemylist.get(y2);
						try{
							if(enemy.getBounds().intersects(x.getBounds())){
								if(0 >= x.Grace){
									bulletlist.remove(y);
									killEnemy(y2);
								}
							}
							if(enemy.getBounds().intersects(player.getBounds())){
								killEnemy(y2);
								killPlayer();
							}
						} catch(NullPointerException e){
							if(enemy == null){
								enemylist.remove(y2);
							}
						}
					}
					try{
						if(!x.getBounds().intersects(0, 0, 805, 630)){
							bulletlist.remove(y);
						}
					} catch(NullPointerException e){}
				}
				for(int x = 0; x < 35; x++){
					try{
						if(laserList[x].update().intersects(player.getBounds())){
							killPlayer();
						}
					} catch(NullPointerException e){
						if(player == null){
							Main.updateTask.cancel();
						}
					}
				}
			}
		} else {
			SoundManager.menu.playClip();
			Main.menu.Menu = false;
			this.bulletlist = new HashMap<>(200);
			this.enemylist = new HashMap<>(25);
			for(int x = 0; x < 35; x++){
				this.laserList[x].startTransition(false, 1);
				this.laserList[x].update();
			}
			this.player = new Ship(playerImage, 10, 400, 310);
			Main.updateTask = Main.mainWindow.new updateTask();
			Main.update.scheduleAtFixedRate(Main.updateTask, 200, 33);
		}
	}
	private void killPlayer(){
		player.kill();
		player = null;
		SoundManager.explosion.playClip();
	}
	private void killEnemy(int id){
		Ship enemy = enemylist.get(id);
		enemy.kill();
		enemy = null;
		enemylist.remove(id);
		SoundManager.explosion.playClip();
	}
	public void startTransition(int id, int ticks, boolean on){
		laserList[id].startTransition(on, ticks);
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
		enemylist.put(key, new Ship(enemy, 50, x, y));
		SoundManager.enemy.playClip();
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