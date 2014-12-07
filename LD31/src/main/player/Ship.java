package main.player;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;

public class Ship implements ImageObserver{
	private Image i;
	private Rectangle bounds;
	public Ship(Image i){
		this.i = i;
		this.bounds = new Rectangle(i.getWidth(this), i.getHeight(this));
	}
	public Ship(Image i, Rectangle bounds){
		this.i = i;
		this.bounds = bounds;
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
	public Ship clone(){return new Ship(this.i, this.bounds);}
	@Override
	public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height){
		if(width != -1 && height != -1){
			this.setBounds(new Rectangle(width, height));
			return true;
		}
		return false;
	}
}
