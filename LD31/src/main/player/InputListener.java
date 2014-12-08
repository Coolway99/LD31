package main.player;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;

import javax.swing.event.MouseInputListener;

public class InputListener implements KeyListener, MouseInputListener{

	public boolean[] keycode = new boolean[7];
	public Point mospos = new Point();
	@Override
	public void keyTyped(KeyEvent e){}
	
	@Override
	public void keyPressed(KeyEvent e){
		switch(e.getKeyCode()){
			case KeyEvent.VK_UP:
			case KeyEvent.VK_W:
				keycode[0] = true;
				break;
			case KeyEvent.VK_DOWN:
			case KeyEvent.VK_S:
				keycode[1] = true;
				break;
			case KeyEvent.VK_LEFT:
			case KeyEvent.VK_A:
				keycode[2] = true;
				break;
			case KeyEvent.VK_RIGHT:
			case KeyEvent.VK_D:
				keycode[3] = true;
				break;
			case KeyEvent.VK_ENTER:
				keycode[6] = true;
				break;
			default:break;
		}
	}
	@Override
	public void keyReleased(KeyEvent e){
		switch(e.getKeyCode()){
			case KeyEvent.VK_UP:
			case KeyEvent.VK_W:
				keycode[0] = false;
				break;
			case KeyEvent.VK_DOWN:
			case KeyEvent.VK_S:
				keycode[1] = false;
				break;
			case KeyEvent.VK_LEFT:
			case KeyEvent.VK_A:
				keycode[2] = false;
				break;
			case KeyEvent.VK_RIGHT:
			case KeyEvent.VK_D:
				keycode[3] = false;
				break;
			case KeyEvent.VK_ENTER:
				keycode[6] = false;
				break;
			default:break;
		}
	}
	@Override
	public void mouseClicked(MouseEvent e){}
	@Override
	public void mousePressed(MouseEvent e){
		switch(e.getButton()){
			case MouseEvent.BUTTON1:
				keycode[4] = true;
				break;
			case MouseEvent.BUTTON3:
				keycode[5] = true;
				break;
			default:break;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e){
		switch(e.getButton()){
			case MouseEvent.BUTTON1:
				keycode[4] = false;
				break;
			case MouseEvent.BUTTON3:
				keycode[5] = false;
				break;
			default:break;
		}
	}

	@Override
	public void mouseEntered(MouseEvent e){}
	@Override
	public void mouseExited(MouseEvent e){}
	@Override
	public void mouseDragged(MouseEvent e){
		mouseMoved(e);
	}
	@Override
	public void mouseMoved(MouseEvent e){
		this.mospos = e.getPoint();
	}
}
