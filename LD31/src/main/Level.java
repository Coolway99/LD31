package main;

public class Level {
	public static Teir teirs[] = new Teir[2];
	public static void init(){
		teirs[0] = new Teir0().init();
		teirs[1] = new Teir1().init();
	}
	public static class Teir0 extends Teir{
		public LevelStructure[] levels = new LevelStructure[3];
		@Override
		public Teir init(){
			levels[0] = new LevelStructure(){
				@Override
				public void execute(){
					for(int x = 1; x < 5; x++){
						Main.mainPanel.startTransition(x-1, 20*x, true);
						Main.mainPanel.startTransition(20-x, 20*x, true);
					}
					for(int x = 1; x < 3; x++){
						Main.mainPanel.startTransition(19+x, 20*x, true);
						Main.mainPanel.startTransition(35-x, 20*x, true);
					}
					Main.mainPanel.spawnEnemy(0, 0);
					Main.mainPanel.spawnEnemy(805, 630);
					Main.mainPanel.spawnEnemy(805, 0);
					Main.mainPanel.spawnEnemy(0, 630);
				}
				@Override
				public void remove(){
					for(int x = 0; x < 4; x++){
						Main.mainPanel.startTransition(x, 80-(20*x), false);
						Main.mainPanel.startTransition(19-x, 80-(20*x), false);
					}
					for(int x = 0; x < 2; x++){
						Main.mainPanel.startTransition(20+x, 40-(20*x), false);
						Main.mainPanel.startTransition(34-x, 40-(20*x), false);
					}
				}
				@Override
				public int holdTime(){
					return 300;
				}
			};
			levels[1] = new LevelStructure(){
				@Override
				public void execute(){
					for(int x = 1; x < 3; x++){
						Main.mainPanel.startTransition(22-x, 20*x, true);
						Main.mainPanel.startTransition(32+x, 20*x, true);
					}
					Main.mainPanel.spawnEnemy(500, 400);
					Main.mainPanel.spawnEnemy(300, 200);
					Main.mainPanel.spawnEnemy(500, 200);
					Main.mainPanel.spawnEnemy(300, 400);
				}
				@Override
				public void remove(){
					for(int x = 1; x < 3; x++){
						Main.mainPanel.startTransition(22-x, 60-(20*x), false);
						Main.mainPanel.startTransition(32+x, 60-(20*x), false);
					}
				}
				@Override
				public int holdTime(){
					return 300;
				}
			};
			levels[2] = new LevelStructure(){
				@Override
				public void execute(){
					for(int x = 0; x < 19; x++){
						Main.mainPanel.startTransition(x, (x*10)+30, true);
					}
					for(int x = 0; x < 4; x++){
						Main.mainPanel.spawnEnemy(200*x, 630);
					}
				}
				@Override
				public void remove(){
					for(int x = 0; x < 19; x++){
						Main.mainPanel.startTransition(x, 20, false);
					}
				}
				@Override
				public int holdTime(){
					return 240;
				}
			};
			return this;
		}
	} 
	public static class Teir1 extends Teir{
		public LevelStructure[] levels = new LevelStructure[3];
		@Override
		public Teir init(){
			levels[0] = new LevelStructure(){
				@Override
				public void execute(){
					for(int x = 7; x < 14; x++){
						Main.mainPanel.startTransition(x+20, ((x-7)*20)+10, true);
					}
					for(int x = 6; x > -1; x--){
						Main.mainPanel.startTransition(x, ((6-x)*20)+20, true);
					}
					Main.mainPanel.spawnEnemy(0, 0);
					Main.mainPanel.spawnEnemy(805, 630);
					Main.mainPanel.spawnEnemy(805, 0);
					Main.mainPanel.spawnEnemy(0, 630);
				}
				@Override
				public void remove(){
					for(int x = 0; x < 14; x++){
						Main.mainPanel.startTransition(x+20, 20, false);
					}
				}
				@Override
				public int holdTime(){
					return 150;
				}
			};
			levels[1] = new LevelStructure(){
				@Override
				public void execute(){
					for(int x = 0; x < 8; x++){
						Main.mainPanel.startTransition(x, (20*x)+10, true);
						Main.mainPanel.startTransition(19-x, (20*x)+10, true);
					}
					Main.mainPanel.spawnEnemy(0, 0);
					Main.mainPanel.spawnEnemy(805, 630);
					Main.mainPanel.spawnEnemy(805, 0);
					Main.mainPanel.spawnEnemy(0, 630);
					Main.mainPanel.spawnEnemy(400, 630);
					Main.mainPanel.spawnEnemy(400, 0);
				}
				@Override
				public void remove(){
					for(int x = 0; x < 8; x++){
						Main.mainPanel.startTransition(x, 20, false);
						Main.mainPanel.startTransition(19-x, 20, false);
					}
				}
				@Override
				public int holdTime(){
					return 200;
				}
			};
			levels[2] = new LevelStructure(){
				@Override
				public void execute(){
					for(int x = 9; x > 0; x--){
						Main.mainPanel.startTransition(x, ((9-x)*20)+10, true);
						Main.mainPanel.spawnEnemy(0, 0);
						Main.mainPanel.spawnEnemy(805, 630);
						Main.mainPanel.spawnEnemy(805, 0);
						Main.mainPanel.spawnEnemy(0, 630);
					}
					for(int x = 10; x < 19; x++){
						Main.mainPanel.startTransition(x, (x*20)+10, true);
					}
				}
				@Override
				public void remove(){
					for(int x = 1; x < 20; x++){
						Main.mainPanel.startTransition(x, 20, false);
					}
				}
				@Override
				public int holdTime(){
					return 250;
				}
			};
			return this;
		}
	}
	public static class Teir2{
		
	}
	public static class Teir3{
		
	}
	public static abstract class Teir{
		public LevelStructure[] levels;
		public abstract Teir init();
	}
}
interface LevelStructure{
	public void execute();
	public void remove();
	public int holdTime();
}