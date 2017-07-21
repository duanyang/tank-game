package testGame1;

//myTank only can fire consecutive 5 shots at a time.
//destroy enemy tank when it is hit, show explosion

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;



public class MyTankGame1 extends JFrame implements ActionListener {

	MyPanel panel;
	MyStartPanel msp = null;
	JMenuBar jmb = null;
	JMenu jml = null;
	JMenuItem jml1 = null;
	JMenuItem jml2 = null;
	JMenuItem jml3 = null;
	JMenuItem jml4 = null;

	public static void main(String[] args) {
		MyTankGame1 game = new MyTankGame1();
	}

	public MyTankGame1() {
		// panel = new MyPanel();
		// Thread t = new Thread(panel);
		// t.start();
		// this.add(panel);
		// this.addKeyListener(panel);
		jmb = new JMenuBar();
		jml = new JMenu("游戏(G)");
		jml.setMnemonic('G');
		jml1 = new JMenuItem("开始新游戏(N)");
		jml2 = new JMenuItem("退出游戏(E)");
		jml3 = new JMenuItem("存盘退出游戏(C)");
		jml4 = new JMenuItem("继续上局游戏(S)");

		jml2.setMnemonic('E');
		jml3.setMnemonic('C');
		jml1.addActionListener(this);
		jml2.addActionListener(this);
		jml3.addActionListener(this);
		jml4.addActionListener(this);
		jml1.setActionCommand("New Tank Game");
		jml2.setActionCommand("Exit the game");
		jml3.setActionCommand("saveExit");

		jml4.setActionCommand("conGame");
		jml.add(jml1);
		jml.add(jml2);
		jml.add(jml3);
		jml.add(jml4);
		jmb.add(jml);

		msp = new MyStartPanel();
		Thread t = new Thread(msp);
		t.start();
		this.setJMenuBar(jmb);
		this.add(msp);
		this.setSize(600, 500);
		this.setVisible(true);

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		if (arg0.getActionCommand().equals("New Tank Game")) {
			panel = new MyPanel("newGame");
			Thread t = new Thread(panel);
			t.start();
			this.remove(msp);
			this.add(panel);
			this.addKeyListener(panel);
			this.setVisible(true);
		} else if (arg0.getActionCommand().equals("Exit the game")) {
			Recorder.keepRecording();
			System.exit(0);
		} else if (arg0.getActionCommand().equals("saveExit")) {
			Recorder r = new Recorder();
			r.setEts(panel.ets);
			r.keepRecAndEnemyTank();
			System.exit(0);
		} else if (arg0.getActionCommand().equals("conGame")) {
			panel=new MyPanel("continue");
			
			Thread t=new Thread(panel);
			t.start();
			this.remove(msp);
			this.add(panel);
			this.addKeyListener(panel);
			this.setVisible(true);
			
			
		}
	}

}

class MyStartPanel extends JPanel implements Runnable {

	int times = 0;

	public void paint(Graphics g) {
		super.paint(g);
		g.fillRect(0, 0, 400, 300);

		if (times % 2 == 0) {
			g.setColor(Color.yellow);
			Font myFont = new Font("华文新魏", Font.BOLD, 30);
			g.setFont(myFont);
			g.drawString("stage: 1", 200, 200);
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

		while (true) {

			try {
				Thread.sleep(100);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			times++;
			this.repaint();
		}
	}
}

class MyPanel extends JPanel implements KeyListener, Runnable {

	MyTank myTank = null;

	// decide whether it is a new game or process with previous game


	Vector<EnemyTank> ets = new Vector<EnemyTank>();
	Vector<Node>  nodes=new Vector<Node>();
	
	Vector<Bomb> bombs = new Vector<Bomb>();
	int enemySize = 3;

	Image image1 = null;
	Image image2 = null;
	Image image3 = null;

	public MyPanel(String flag) {

		Recorder.getRecording();
		myTank = new MyTank(10, 10);
		if (flag.equals("newGame")) {
			for (int i = 0; i < enemySize; i++) {
				EnemyTank tank = new EnemyTank((i + 1) * 50, 0);
				tank.setColor(1);
				tank.setDirection(2);
				tank.setEts(ets);
				Thread t = new Thread(tank);
				t.start();
				Shot shot = new Shot(tank.getX() + 10, tank.getY() + 30,
						tank.getDirection());
				tank.getShots().add(shot);
				Thread t2 = new Thread(shot);
				t2.start();
				ets.add(tank);
			}
		}
		else{
			nodes=new Recorder().getNodesAndEnNums();
			for (int i = 0; i < nodes.size(); i++) {
				Node node=nodes.get(i);
				EnemyTank tank = new EnemyTank(node.x, node.y);
				tank.setColor(1);
				tank.setDirection(node.direction);
				tank.setEts(ets);
				Thread t = new Thread(tank);
				t.start();
				Shot shot = new Shot(tank.getX() + 10, tank.getY() + 30,
						tank.getDirection());
				tank.getShots().add(shot);
				Thread t2 = new Thread(shot);
				t2.start();
				ets.add(tank);
			}
		}
		try {
			image1 = ImageIO.read(new File("bomb_1.gif"));
			image2 = ImageIO.read(new File("bomb_2.gif"));
			image3 = ImageIO.read(new File("bomb_3.gif"));
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		AePlayWave wv=new AePlayWave("C:\\sound\\111.wav");
		wv.start();
	}

	public void showInfo(Graphics g) {
		this.drawTank(80, 330, g, 0, 1);
		g.setColor(Color.BLACK);
		g.drawString(Integer.toString(Recorder.getEnNum()), 110, 350);
		this.drawTank(150, 330, g, 0, 0);
		g.setColor(Color.BLACK);
		g.drawString(Integer.toString(Recorder.getMyLife()), 190, 350);
		g.setColor(Color.BLACK);
		Font f = new Font("宋体", Font.BOLD, 20);
		g.setFont(f);
		g.drawString("您的总成绩", 420, 30);

		this.drawTank(420, 60, g, 0, 0);

		g.setColor(Color.black);
		g.drawString(Recorder.getAllEnNum() + "", 460, 80);
	}

	public void paint(Graphics g) {

		super.paint(g);
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 400, 300);

		// show info
		this.showInfo(g);

		// draw my tank
		if (myTank.isAlive()) {
			this.drawTank(myTank.getX(), myTank.getY(), g,
					myTank.getDirection(), 0);
		}

		// draw my tank shot
		for (int i = 0; i < myTank.getShots().size(); i++) {
			Shot myShot = myTank.getShots().get(i);
			if (myShot != null && myShot.getAlive() == true) {
				g.draw3DRect(myShot.getX(), myShot.getY(), 5, 5, false);
			}
			if (myShot.getAlive() == false) {
				myTank.getShots().remove(myShot);
			}
		}

		// draw enemy tanks
		for (int i = 0; i < ets.size(); i++) {
			EnemyTank tank = ets.get(i);
			if (tank.isAlive()) {

				this.drawTank(tank.getX(), tank.getY(), g, tank.getDirection(),
						tank.getColor());

				// draw enemy shot

				for (int j = 0; j < tank.getShots().size(); j++) {
					Shot enemyShot = tank.getShots().get(j);
					if (enemyShot.isAlive()) {
						g.draw3DRect(enemyShot.getX(), enemyShot.getY(), 5, 5,
								false);
					} else {
						tank.getShots().remove(enemyShot);
					}
				}
			}
		}

		// draw bomb

		for (int i = 0; i < bombs.size(); i++) {
			System.out.println(bombs.size());
			Bomb b = bombs.get(i);
			if (b.getLife() > 6) {
				g.drawImage(image1, b.getX(), b.getY(), 30, 30, this);
			} else if (b.getLife() > 3) {
				g.drawImage(image2, b.getX(), b.getY(), 30, 30, this);
			} else {
				g.drawImage(image3, b.getX(), b.getY(), 30, 30, this);
			}
			b.lifeDown();

			if (b.getLife() == 0) {
				bombs.remove(b);
			}
		}
	}

	// whether my tank is hit by the enemy tank
	public void hitMe() {
		for (int i = 0; i < ets.size(); i++) {
			EnemyTank tank = ets.get(i);
			for (int j = 0; j < tank.getShots().size(); j++) {
				Shot enemyShot = tank.getShots().get(j);
				if (myTank.isAlive())
					if (this.hitTank(enemyShot, myTank)) {
						Recorder.reduceMyLife();
						if (Recorder.getMyLife() != 0)
							myTank.setAlive(true);
					}
			}
		}
	}

	// whether the enemy tank is hit
	public void hitEnemyTank() {
		for (Shot shot : myTank.getShots()) {
			if (shot.isAlive()) {
				for (EnemyTank tank : ets) {
					if (tank.isAlive()) {
						if (this.hitTank(shot, tank)) {
							Recorder.reduceEnNum();
							Recorder.addEnNumRec();
						}
					}
				}
			}
		}
	}

	public boolean hitTank(Shot shot, Tank tank) {
		boolean b2 = false;
		switch (tank.getDirection()) {
		case 0:
		case 2:
			if (shot.getX() > tank.getX() && shot.getX() < (tank.getX() + 20)
					&& shot.getY() > tank.getY()
					&& shot.getY() < (tank.getY() + 30)) {
				shot.setAlive(false);
				tank.setAlive(false);
				b2 = true;
				Bomb bomb = new Bomb(tank.getX(), tank.getY());
				bombs.add(bomb);

			}
			break;
		case 1:
		case 3:
			if (shot.getX() > tank.getX() && shot.getX() < (tank.getX() + 30)
					&& shot.getY() > shot.getY()
					&& shot.getY() < (tank.getY() + 20)) {
				shot.setAlive(false);
				tank.setAlive(false);
				b2 = true;
				Bomb bomb = new Bomb(tank.getX(), tank.getY());
				bombs.add(bomb);

			}
			break;
		}
		return b2;
	}

	public void drawTank(int x, int y, Graphics g, int direction, int type) {

		// tank type
		switch (type) {
		case 0:
			g.setColor(Color.CYAN);
			break;
		case 1:
			g.setColor(Color.RED);
			break;
		}

		// tank direction

		switch (direction) {
		// up direction
		case 0:
			g.fill3DRect(x, y, 5, 30, false);
			g.fill3DRect(x + 15, y, 5, 30, false);
			g.fill3DRect(x + 5, y + 5, 10, 20, false);
			g.fillOval(x + 5, y + 10, 10, 10);
			g.drawLine(x + 10, y + 15, x + 10, y);
			break;
		// right direction
		case 1:
			g.fill3DRect(x, y, 30, 5, false);
			g.fill3DRect(x, y + 15, 30, 5, false);
			g.fill3DRect(x + 5, y + 5, 20, 10, false);
			g.fillOval(x + 10, y + 5, 10, 10);
			g.drawLine(x + 15, y + 10, x + 30, y + 10);
			break;
		// down direction
		case 2:
			g.fill3DRect(x, y, 5, 30, false);
			g.fill3DRect(x + 15, y, 5, 30, false);
			g.fill3DRect(x + 5, y + 5, 10, 20, false);
			g.fillOval(x + 5, y + 10, 10, 10);
			g.drawLine(x + 10, y + 15, x + 10, y + 30);
			break;
		// left direction
		case 3:
			g.fill3DRect(x, y, 30, 5, false);
			g.fill3DRect(x, y + 15, 30, 5, false);
			g.fill3DRect(x + 5, y + 5, 20, 10, false);
			g.fillOval(x + 10, y + 5, 10, 10);
			g.drawLine(x + 15, y + 10, x, y + 10);
			break;
		}

	}

	@Override
	public void keyPressed(KeyEvent arg0) {

		if (arg0.getKeyCode() == KeyEvent.VK_W) {
			myTank.setDirection(0);
			myTank.moveUp();

		} else if (arg0.getKeyCode() == KeyEvent.VK_D) {
			myTank.setDirection(1);
			myTank.moveRight();
		} else if (arg0.getKeyCode() == KeyEvent.VK_S) {
			myTank.setDirection(2);
			myTank.moveDown();
		} else if (arg0.getKeyCode() == KeyEvent.VK_A) {
			myTank.setDirection(3);
			myTank.moveLeft();
		}

		else if (arg0.getKeyCode() == KeyEvent.VK_J) {
			if (myTank.getShots().size() <= 4) {
				myTank.shotEnemy();
			}
		}

		this.repaint();
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			try {
				Thread.sleep(100);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			this.hitEnemyTank();
			this.hitMe();
			this.repaint();
		}
	}
}
