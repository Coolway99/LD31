package main;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;

public class Bullet{
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
