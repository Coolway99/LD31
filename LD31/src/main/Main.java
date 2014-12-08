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
	public static updateTask updateTask = mainWindow.new updateTask();
	public static final MainMenu menu = mainWindow.new MainMenu("Main Menu");
	private static Sequences sequence = mainWindow.new Sequences();
	
	public Main(String s){
		super(s);
	}
	public static void main(String Args[]){
		SoundManager.init();
		Level.init();
		mainWindow.addKeyListener(inputListener);
		mainWindow.addMouseListener(inputListener);
		mainWindow.addMouseMotionListener(inputListener);
		mainWindow.add(mainPanel);
		mainWindow.setSize(805, 630);
		mainWindow.setVisible(true);
		mainWindow.setLocationRelativeTo(null);
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.setResizable(false);
		menu.start();
	}
	public static void update(){
		sequence.update();
		mainPanel.update(inputListener.keycode, inputListener.mospos);
		mainWindow.repaint();
	}
	class Sequences {;
		private boolean[] teirList = new boolean[2];
		private int ticks = 0;
		private int cooldown = 0;
		private int lastTeir = 0;
		private int lastLevel = 0;
		public void update(){
			ticks++;
			if(cooldown > 0){
				cooldown--;
			}
			if(ticks > 60 && !teirList[0]){
				teirList[0] = true;
			}
			if(ticks > 2000 && !teirList[1]){
				teirList[1] = true;
			}
			if(cooldown == 0){
				for(int x = teirList.length-1; x > -1; x--){
					if(teirList[x]){
						try{
							int y = 0;
							if(x == 0){
								y = Main.ran.nextInt(((Level.Teir0) (Level.teirs[x])).levels.length+2);
								if(y < ((Level.Teir0) (Level.teirs[x])).levels.length){
									if(lastTeir == 0){
										((Level.Teir0)Level.teirs[lastTeir]).levels[lastLevel].remove();
									} else {
										((Level.Teir1)Level.teirs[lastTeir]).levels[lastLevel].remove();
									}
									lastTeir = x;
									lastLevel = y;
									((Level.Teir0) (Level.teirs[x])).levels[y].execute();
									cooldown = ((Level.Teir0) (Level.teirs[x])).levels[y].holdTime();
								}
							} else {
								y = Main.ran.nextInt(((Level.Teir1) (Level.teirs[x])).levels.length+2);
								if(y < ((Level.Teir1) (Level.teirs[x])).levels.length){
									if(lastTeir == 0){
										((Level.Teir0)Level.teirs[lastTeir]).levels[lastLevel].remove();
									} else {
										((Level.Teir1)Level.teirs[lastTeir]).levels[lastLevel].remove();
									}
									lastTeir = x;
									lastLevel = y;
									((Level.Teir1) (Level.teirs[x])).levels[y].execute();
									cooldown = ((Level.Teir1) (Level.teirs[x])).levels[y].holdTime();
								}
							}
						} catch(ArrayIndexOutOfBoundsException e){
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
	class MainMenu extends Thread{
		public boolean Menu = true;
		public boolean instructions = false;
		public int selection = 0;
		public final int amountOfSelections = 1 /*one less than total number*/;
		public int cooldown = 10;
		public int instrucCooldown = 10;
		
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
				if(!instructions){
					if(inputListener.keycode[0] && cooldown == 0){
						if(--selection < 0){
							selection = amountOfSelections;
						}
						cooldown = 10;
						SoundManager.menu.playClip();
					}
					if(cooldown > 0){
						cooldown--;
					}
					if(inputListener.keycode[1] && cooldown == 0){
						if(++selection > amountOfSelections){
							selection = 0;
						}
						cooldown = 10;
						SoundManager.menu.playClip();
					}
				}
				if(instrucCooldown > 0){
					instrucCooldown--;
				}
				if(inputListener.keycode[6] && (selection == 1 && instrucCooldown == 0)){
					selection = -1;
					instructions = true;
					instrucCooldown = 10;
					SoundManager.menu.playClip();
				} else if(instructions && (inputListener.keycode[6]) && instrucCooldown == 0){
					selection = 1;
					instructions = false;
					instrucCooldown = 10;
					SoundManager.menu.playClip();
				} else if(inputListener.keycode[6] && selection == 0){
					mainPanel.update(inputListener.keycode, inputListener.mospos);
				}
				mainWindow.repaint();
				try {
					Thread.sleep(33);
				} catch (InterruptedException e) {}
			}
		};
	}
	public class updateTask extends TimerTask{
		@Override 
		public void run(){
			Main.update();
		}
		@Override
		public boolean cancel(){
			Main.menu.Menu = true;
			Main.sequence = mainWindow.new Sequences();
			return super.cancel();
		}
	}
}

