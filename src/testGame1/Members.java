package testGame1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;


// play sound
class AePlayWave extends Thread {

	private String filename;

	public AePlayWave(String wavfile) {

		filename = wavfile;

	}

	public void run() {

		File soundFile = new File(filename);

		AudioInputStream audioInputStream = null;

		try {

			audioInputStream = AudioSystem.getAudioInputStream(soundFile);

		} catch (Exception e1) {

			e1.printStackTrace();

			return;

		}

		AudioFormat format = audioInputStream.getFormat();

		SourceDataLine auline = null;

		DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

		try {

			auline = (SourceDataLine) AudioSystem.getLine(info);

			auline.open(format);

		} catch (Exception e) {

			e.printStackTrace();

			return;

		}

		auline.start();

		int nBytesRead = 0;

		// 这是缓冲

		byte[] abData = new byte[512];

		try {

			while (nBytesRead != -1) {

				nBytesRead = audioInputStream.read(abData, 0, abData.length);

				if (nBytesRead >= 0)

					auline.write(abData, 0, nBytesRead);

			}

		} catch (IOException e) {

			e.printStackTrace();

			return;

		} finally {

			auline.drain();

			auline.close();

		}

	}

}

class Node {

	int x;
	int y;
	int direction;

	public Node(int x, int y, int direction) {
		this.x = x;
		this.y = y;
		this.direction = direction;
	}
}

class Recorder {
	private static int enNum = 5;
	private static int myLife = 3;
	private static int allEnNum = 0;
	private static FileWriter fw = null;
	private static BufferedWriter bw = null;
	private static FileReader fr = null;
	private static BufferedReader br = null;
	private Vector<EnemyTank> ets = new Vector<EnemyTank>();
	private Vector<Node> nodes = new Vector<Node>();

	public Vector<EnemyTank> getEts() {
		return ets;
	}

	public void setEts(Vector<EnemyTank> ets1) {
		ets = ets1;
	}

	public Vector<Node> getNodesAndEnNums() {
		try {
			fr = new FileReader(
					"C:\\Users\\xingh\\workspace\\tankgame\\myRecording.txt");
			br = new BufferedReader(fr);
			String n = "";
			// 先读取第一行
			n = br.readLine();
			allEnNum = Integer.parseInt(n);
			while ((n = br.readLine()) != null) {
				String[] xyz = n.split(" ");

				Node node = new Node(Integer.parseInt(xyz[0]),
						Integer.parseInt(xyz[1]), Integer.parseInt(xyz[2]));
				nodes.add(node);
			}

		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		} finally {

			try {
				// 后打开则先关闭
				br.close();
				fr.close();
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}
		}

		return nodes;

	}

	public void keepRecAndEnemyTank() {
		try {
			fw = new FileWriter(
					"C:\\Users\\xingh\\workspace\\tankgame\\myRecording.txt");
			bw = new BufferedWriter(fw);
			bw.write(allEnNum + "\r\n");

			for (int i = 0; i < ets.size(); i++) {

				EnemyTank et = ets.get(i);

				if (et.isAlive()) {
					// 活的就保存
					String recode = et.getX() + " " + et.getY() + " "
							+ et.getDirection();

					// 写入
					bw.write(recode + "\r\n");

				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				// 后开先关闭
				bw.close();
				fw.close();
			} catch (Exception e) {
				e.printStackTrace();

			}
		}
	}

	// retrieve number of enemy tank destroyed by my tank
	public static void getRecording() {
		try {
			fr = new FileReader(
					"C:\\Users\\xingh\\workspace\\tankgame\\myRecording.txt");
			br = new BufferedReader(fr);
			String n = br.readLine();
			allEnNum = Integer.parseInt(n);
		} catch (Exception e) {
			e.printStackTrace();

		} finally {

			try {

				br.close();
				fr.close();
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}
		}
	}

	// keep recorder of enemy tankd destroyed by my tank
	public static void keepRecording() {
		try {

			// 创建
			fw = new FileWriter(
					"C:\\Users\\xingh\\workspace\\tankgame\\myRecording.txt");
			bw = new BufferedWriter(fw);

			bw.write(allEnNum + "\r\n");

		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		} finally {

			// 关闭流
			try {
				// 后开先关闭
				bw.close();
				fw.close();
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}
		}
	}

	public static int getEnNum() {
		return enNum;
	}

	public static void setEnNum(int enNum) {
		Recorder.enNum = enNum;
	}

	public static int getMyLife() {
		return myLife;
	}

	public static void setMyLife(int myLife) {
		Recorder.myLife = myLife;
	}

	public static void reduceEnNum() {
		enNum--;
	}

	public static void reduceMyLife() {
		myLife--;
	}

	public static void addEnNumRec() {
		allEnNum++;
	}

	public static int getAllEnNum() {
		return allEnNum;
	}

	public static void setAllEnNum(int allEnNum) {
		Recorder.allEnNum = allEnNum;
	}
}

class Bomb {
	int x, y;
	int life = 9;
	boolean Alive = true;

	public boolean isAlive() {
		return Alive;
	}

	public void setAlive(boolean alive) {
		this.Alive = alive;
	}

	public Bomb(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void lifeDown() {
		if (life > 0)
			life--;
		else
			this.Alive = false;

	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}
}

class Shot implements Runnable {
	int x;
	int y;
	int direction;
	int speed = 4;
	boolean isAlive = true;

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public boolean isAlive() {
		return isAlive;
	}

	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}

	public boolean getAlive() {
		return isAlive;
	}

	public Shot(int x, int y, int direction) {
		this.x = x;
		this.y = y;
		this.direction = direction;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

		while (true) {

			try {
				Thread.sleep(50);
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			switch (direction) {
			case 0:
				y -= speed;
				break;
			case 1:
				x += speed;
				break;
			case 2:
				y += speed;
				break;
			case 3:
				x -= speed;
				break;
			}

			// decide whether shot is alive or not.

			if (x < 0 || x > 400 || y < 0 || y > 300) {
				this.isAlive = false;
				break;
			}
		}
	}
}

class Tank {
	int x = 0;
	int y = 0;
	int direction = 0; // 0: up, 1: right, 2:down, 3: left
	int speed = 3;
	int color;
	boolean Alive = true;
	Vector<Shot> shots = new Vector<Shot>();
	Shot shot = null;

	public Vector<Shot> getShots() {
		return shots;
	}

	public void setShots(Vector<Shot> shots) {
		this.shots = shots;
	}

	public Shot getShot() {
		return shot;
	}

	public void setShot(Shot shot) {
		this.shot = shot;
	}

	public boolean isAlive() {
		return Alive;
	}

	public void setAlive(boolean alive) {
		Alive = alive;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public Tank(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
}

class EnemyTank extends Tank implements Runnable {

	int times = 0;

	Vector<EnemyTank> ets = new Vector<EnemyTank>();

	public EnemyTank(int x, int y) {
		super(x, y);

	}

	public void setEts(Vector<EnemyTank> ets) {
		this.ets = ets;
	}

	public boolean isTouchOtherEnemy() {
		boolean b = false;

		switch (this.getDirection()) {
		case 0:
			for (int i = 0; i < ets.size(); i++) {
				EnemyTank et = ets.get(i);
				if (et != this) {

					// if enemy et direction is up or down
					if (et.getDirection() == 0 || et.getDirection() == 2) {

						if (this.getX() >= et.getX()
								&& this.getX() <= et.getX() + 20
								&& this.getY() >= et.y
								&& this.getY() <= et.y + 30) {
							return true;
						}
						if (this.getX() + 20 >= et.getX()
								&& this.getX() + 20 <= et.getX() + 20
								&& this.getY() >= et.y
								&& this.getY() <= et.y + 30) {
							return true;
						}
					}
					// if enemy et direction is left or right
					if (et.getDirection() == 3 || et.getDirection() == 1) {

						if (this.getX() >= et.getX()
								&& this.getX() <= et.getX() + 30
								&& this.getY() >= et.y
								&& this.getY() <= et.y + 20) {
							return true;
						}
						if (this.getX() + 20 >= et.getX()
								&& this.getX() + 20 <= et.getX() + 30
								&& this.getY() >= et.y
								&& this.getY() <= et.y + 20) {
							return true;
						}
					}
				}
			}
			break;
		case 1:
			for (int i = 0; i < ets.size(); i++) {
				EnemyTank et = ets.get(i);
				if (et != this) {

					// if enemy et direction is up or down
					if (et.getDirection() == 0 || et.getDirection() == 2) {

						if (this.getX() + 30 >= et.getX()
								&& this.getX() + 30 <= et.getX() + 20
								&& this.getY() >= et.y
								&& this.getY() <= et.y + 30) {
							return true;
						}

						if (this.getX() + 30 >= et.getX()
								&& this.getX() + 30 <= et.getX() + 20
								&& this.getY() + 20 >= et.y
								&& this.getY() + 20 <= et.y + 30) {
							return true;
						}
					}
					// if enemy et direction is left or right
					if (et.getDirection() == 3 || et.getDirection() == 1) {

						if (this.getX() + 30 >= et.getX()
								&& this.getX() + 30 <= et.getX() + 30
								&& this.getY() >= et.y
								&& this.getY() <= et.y + 20) {
							return true;
						}
						if (this.getX() + 30 >= et.getX()
								&& this.getX() + 30 <= et.getX() + 30
								&& this.getY() + 20 >= et.y
								&& this.getY() + 20 <= et.y + 20) {
							return true;
						}
					}
				}
			}
			break;
		case 2:
			for (int i = 0; i < ets.size(); i++) {
				EnemyTank et = ets.get(i);
				if (et != this) {

					// if enemy et direction is up or down
					if (et.getDirection() == 0 || et.getDirection() == 2) {

						if (this.getX() >= et.getX()
								&& this.getX() <= et.getX() + 20
								&& this.getY() + 30 >= et.y
								&& this.getY() + 30 <= et.y + 30) {
							return true;
						}
						// 我的右点
						if (this.getX() + 20 >= et.getX()
								&& this.getX() + 20 <= et.getX() + 20
								&& this.getY() + 30 >= et.y
								&& this.getY() + 30 <= et.y + 30) {
							return true;
						}
					}
					// if enemy et direction is left or right
					if (et.getDirection() == 3 || et.getDirection() == 1) {

						if (this.getX() >= et.getX()
								&& this.getX() <= et.getX() + 30
								&& this.getY() + 30 >= et.y
								&& this.getY() + 30 <= et.y + 20) {
							return true;
						}

						if (this.getX() + 20 >= et.getX()
								&& this.getX() + 20 <= et.getX() + 30
								&& this.getY() + 30 >= et.y
								&& this.getY() + 30 <= et.y + 20) {
							return true;
						}
					}
				}
			}
			break;
		case 3:
			for (int i = 0; i < ets.size(); i++) {
				EnemyTank et = ets.get(i);
				if (et != this) {

					// if enemy et direction is up or down
					if (et.getDirection() == 0 || et.getDirection() == 2) {

						if (this.getX() >= et.getX()
								&& this.getX() <= et.getX() + 20
								&& this.getY() >= et.y
								&& this.getY() <= et.y + 30) {
							return true;
						}
						// 下一点
						if (this.getX() >= et.getX()
								&& this.getX() <= et.getX() + 20
								&& this.getY() + 20 >= et.y
								&& this.getY() + 20 <= et.y + 30) {
							return true;
						}
					}
					// if enemy et direction is left or right
					if (et.getDirection() == 3 || et.getDirection() == 1) {

						if (this.getX() >= et.getX()
								&& this.getX() <= et.getX() + 30
								&& this.getY() >= et.y
								&& this.getY() <= et.y + 20) {
							return true;
						}
						if (this.getX() >= et.getX()
								&& this.getX() <= et.getX() + 30
								&& this.getY() + 20 >= et.getY()
								&& this.getY() + 20 <= et.y + 20) {
							return true;
						}
					}
				}
			}
			break;
		}
		return b;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

		while (true) {

			try {
				Thread.sleep(50);
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			switch (this.getDirection()) {
			case 0:
				for (int i = 0; i < 20; i++) {
					if (y > 0 && !this.isTouchOtherEnemy()) {
						y -= speed;
					}
					try {
						Thread.sleep(50);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
				break;
			case 1:
				for (int i = 0; i < 20; i++) {
					if (x < 300 && !this.isTouchOtherEnemy()) {
						x += speed;
					}
					try {
						Thread.sleep(20);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
				break;
			case 2:
				for (int i = 0; i < 20; i++) {
					if (y < 400 && !this.isTouchOtherEnemy()) {
						y += speed;
					}
					try {
						Thread.sleep(50);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
				break;
			case 3:
				for (int i = 0; i < 20; i++) {
					if (x > 0 && !this.isTouchOtherEnemy()) {
						x -= speed;
					}
					try {
						Thread.sleep(50);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
				break;
			}
			// add shot to enemy tank
			times++;
			if (times % 2 == 0) {
				if (this.isAlive()) {
					if (this.getShots().size() < 5) {
						Shot shot = null;
						switch (this.getDirection()) {
						case 0:
							shot = new Shot(this.getX() + 10, this.getY(), 0);
							this.getShots().add(shot);
							break;
						case 1:
							shot = new Shot(this.getX() + 30, this.getY() + 10,
									1);
							this.getShots().add(shot);
							break;
						case 2:
							shot = new Shot(this.getX() + 10, this.getY() + 30,
									2);
							this.getShots().add(shot);
							break;
						case 3:
							shot = new Shot(this.getX(), this.getY() + 10, 3);
							this.getShots().add(shot);
							break;
						}
						Thread t = new Thread(shot);
						t.start();
					}
				}

			}

			// set direction of Enemy tank
			this.setDirection((int) (Math.random() * 4));

			if (!this.isAlive()) {
				break;
			}
		}
	}
}

class MyTank extends Tank {

	public MyTank(int x, int y) {
		super(x, y);

	}

	public void moveUp() {
		y -= speed;
	}

	public void moveRight() {
		x += speed;
	}

	public void moveLeft() {
		x -= speed;
	}

	public void moveDown() {
		y += speed;
	}

	public void shotEnemy() {

		switch (this.direction) {
		case 0:
			shot = new Shot(x + 10, y, 0);
			shots.add(shot);
			break;
		case 1:
			shot = new Shot(x + 30, y + 10, 1);
			shots.add(shot);
			break;
		case 2:
			shot = new Shot(x + 10, y + 30, 2);
			shots.add(shot);
			break;
		case 3:
			shot = new Shot(x, y + 10, 3);
			shots.add(shot);
			break;
		}
		Thread t = new Thread(shot);
		t.start();
	}

}
