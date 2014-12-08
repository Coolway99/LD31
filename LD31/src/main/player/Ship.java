package main.player;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;

import main.Bullet;
import main.SoundManager;

public class Ship implements ImageObserver{
	private Image i;
	private Rectangle bounds;
	private int cooldown;
	private int timeUntilNextBullet = 0;
	public Ship(Image i, int bulletCooldown){
		this.i = i;
		this.bounds = new Rectangle(i.getWidth(this), i.getHeight(this));
		this.cooldown = bulletCooldown;
		this.timeUntilNextBullet = this.cooldown;
	}
	public Ship(Image i, int bulletCooldown, int x, int y){
		this.i = i;
		this.bounds = new Rectangle(x, y, i.getWidth(this), i.getHeight(this));
		this.cooldown = bulletCooldown;
		this.timeUntilNextBullet = this.cooldown;
	}
	public Ship(Image i, Rectangle bounds, int bulletCooldown){
		this.i = i;
		this.bounds = bounds;
		this.cooldown = bulletCooldown;
		this.timeUntilNextBullet = this.cooldown;
	}
	public Image getImage(){return this.i;}
	public void kill(){
		this.setBounds(new Rectangle(0, 0, 0, 0));
		this.i = null;
	}
	public void draw(Graphics2D g){
		if(i != null){
			g.drawImage(i, bounds.x, bounds.y, null);
		}
	}
	public void update(){
		if(timeUntilNextBullet > 0){
			timeUntilNextBullet--;
		}
	}
	public Bullet fire(Point fireAt, Image bullet){
		if(timeUntilNextBullet == 0){
			double radAngle = Math.atan2(this.getY()-fireAt.getY(), this.getX()-fireAt.getX());
			timeUntilNextBullet = cooldown;
			SoundManager.bullet.playClip();
			return new Bullet(bullet, this.getX(), this.getY(),
					(int)(-8*Math.cos(-radAngle)), (int)(-8*Math.sin(radAngle)), 5);
			
		}
		return null;
		
	}
	/**
	 * NOTE: Bounds should be the width and height of the image to avoid stretching
	 */
	public void setBounds(Rectangle newBounds){
		this.bounds = newBounds;
	}
	public int getX(){return this.bounds.x;}
	public int getY(){return this.bounds.y;}
	public int getWidth(){return this.bounds.width;}
	public int getHeight(){return this.bounds.height;}
	public Rectangle getBounds(){return this.bounds;}
	
	public void setX(int x){this.bounds.x = x;}
	public void setY(int y){this.bounds.y = y;}
	public void setWidth(int width){this.bounds.width = width;}
	public void setHeight(int height){this.bounds.height = height;}
	@Override
	public Ship clone(){return new Ship(this.i, this.bounds, this.cooldown);}
	@Override
	public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height){
		if(width != -1 && height != -1){
			this.setBounds(new Rectangle(this.getX(), this.getY(), width, height));
			return true;
		}
		return false;
	}
}
