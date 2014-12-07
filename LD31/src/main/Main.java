package main;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;

import main.player.InputListener;

public class Main extends JFrame{
	private static final long serialVersionUID = -6198982814111068471L;
	public static final Main mainWindow = new Main("LD31 - Insert name here"); //TODO
	public static final Random ran = new Random();
	public static final MainPanel mainPanel = new MainPanel();
	public static final InputListener inputListener = new InputListener();
	public static final Timer update = new Timer("Update Thread", false);
	public static final MainMenu menu = new MainMenu("Main Menu");
	public static final TimerTask updateTask = new TimerTask(){
		@Override 
		public void run(){
			Main.update();
		}
		@Override
		public boolean cancel(){
			Main.menu.Menu = true;
			return super.cancel();
		}
	};

	public Main(String s){
		super(s);
	}
	public static void main(String Args[]){
		mainWindow.addKeyListener(inputListener);
		mainWindow.addMouseListener(inputListener);
		mainWindow.addMouseMotionListener(inputListener);
		mainWindow.add(mainPanel);
		mainWindow.setSize(805, 630);
		mainWindow.setVisible(true);
		mainWindow.setLocationRelativeTo(null);
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.setResizable(false);
		update.scheduleAtFixedRate(updateTask, 33, 33);
		}
	public static void update(){
		mainPanel.update(inputListener.keycode, inputListener.mospos);
		mainWindow.repaint();
	}
	class Sequences {
		private boolean[] teirList = new boolean [5];
		private long ticks = 0L;
		public void enableTeir(int teir){
			if(teir < teirList.length){
				teirList[teir] = true;
			}
		}
		public void update(){
			ticks++;
			
		}
	}
}
class MainMenu extends Thread{
	public boolean Menu = true;
	
	public MainMenu(String s){
		super(s);
	}
	@Override
	public void run() {
		while(true){
			while(!Menu){
				try{
					Thread.sleep(100);
				} catch(InterruptedException e){}
			}
			
		}
	};
}
