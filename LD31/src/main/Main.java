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
		Timer update = new Timer("Update Thread", false);
		update.scheduleAtFixedRate(new TimerTask(){
			@Override
			public void run(){
				Main.update();
			}
		}, 33, 33);
	}
	public static void update(){
		mainPanel.update(inputListener.keycode, inputListener.mospos);
		mainWindow.repaint();
	}
}
