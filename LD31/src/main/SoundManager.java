package main;


public class SoundManager {
	public static SoundThread enemy, bullet, explosion, menu, explosionRecover;
	public static void init(){
		enemy = new SoundThread("assets/enemySpawn.wav");
		bullet = new SoundThread("assets/bullet.wav");
		explosion = new SoundThread("assets/explosion.wav");
		menu = new SoundThread("assets/menu.wav");
		explosionRecover = new SoundThread("assets/explosionrecover.wav");
	}
}
