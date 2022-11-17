import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener{
	
	int segments = 6;
	int applesEaten;
	int appleX;
	int appleY;
	char direction = 'R';
	boolean gameRun = false;
	Timer timer;
	Random random;
	
	final int x[] = new int[GAME_UNITS];
	final int y[] = new int[GAME_UNITS];
	
	static final int MONITOR_WIDTH = 1300;
	static final int MONITOR_HEIGHT = 750;
	static final int UNIT_SIZE = 50;
	static final int GAME_UNITS = (MONITOR_WIDTH*MONITOR_HEIGHT)/(UNIT_SIZE*UNIT_SIZE);
	static final int DELAY = 175;

	
	GamePanel(){
		random = new Random();
		this.setPreferredSize(new Dimension(MONITOR_WIDTH,MONITOR_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
	}
	public void startGame() {
		newApple();
		gameRun = true;
		timer = new Timer(DELAY,this);
		timer.start();
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	public void draw(Graphics g) {
		
		if(gameRun) {
			g.setColor(Color.red);
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
		
			for(int i = 0; i< segments;i++) {
				if(i == 0) {
					g.setColor(Color.yellow);
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
				else {
					g.setColor(new Color(45,180,0));
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}			
			}
			g.setColor(Color.red);
			g.setFont( new Font("Sans Serif",Font.BOLD, 40));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score: "+applesEaten, (MONITOR_WIDTH - metrics.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize());
		}
		else {
			gameOver(g);
		}
		
	}
	public void newApple(){
		appleX = random.nextInt((int)(MONITOR_WIDTH/UNIT_SIZE))*UNIT_SIZE;
		appleY = random.nextInt((int)(MONITOR_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
	}
	public void move(){
		for(int i = segments;i>0;i--) {
			x[i] = x[i-1];
			y[i] = y[i-1];
		}
		
		switch(direction) {
		case 'U':
			y[0] = y[0] - UNIT_SIZE;
			break;
		case 'D':
			y[0] = y[0] + UNIT_SIZE;
			break;
		case 'L':
			x[0] = x[0] - UNIT_SIZE;
			break;
		case 'R':
			x[0] = x[0] + UNIT_SIZE;
			break;
		}
		
	}
	public void checkApple() {
		if((x[0] == appleX) && (y[0] == appleY)) {
			segments++;
			applesEaten++;
			newApple();
		}
	}
	public void checkCollisions() {
		//head collides with body
		for(int i = segments;i>0;i--) {
			if((x[0] == x[i])&& (y[0] == y[i])) {
				gameRun = false;
			}
		}
		//head touches left border
		if(x[0] < 0) {
			gameRun = false;
		}
		//head touches right border
		if(x[0] > MONITOR_WIDTH) {
			gameRun = false;
		}
		//head touches top border
		if(y[0] < 0) {
			gameRun = false;
		}
		//head touches bottom border
		if(y[0] > MONITOR_HEIGHT) {
			gameRun = false;
		}
		
		if(!gameRun) {
			timer.stop();
		}
	}
	public void gameOver(Graphics g) {

		g.setColor(Color.red);
		g.setFont( new Font("Sans Serif",Font.BOLD, 40));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("Score: "+applesEaten, (MONITOR_WIDTH - metrics1.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize());

		g.setColor(Color.red);
		g.setFont( new Font("Sans Serif",Font.BOLD, 75));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Game Over", (MONITOR_WIDTH - metrics2.stringWidth("Game Over"))/2, MONITOR_HEIGHT/2);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(gameRun) {
			move();
			checkApple();
			checkCollisions();
		}
		repaint();
	}
	
	public class MyKeyAdapter extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if(direction != 'R') {
					direction = 'L';
				}
				break;
			case KeyEvent.VK_RIGHT:
				if(direction != 'L') {
					direction = 'R';
				}
				break;
			case KeyEvent.VK_UP:
				if(direction != 'D') {
					direction = 'U';
				}
				break;
			case KeyEvent.VK_DOWN:
				if(direction != 'U') {
					direction = 'D';
				}
				break;
			}
		}
	}
}