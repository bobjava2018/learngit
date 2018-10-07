package cn.baibo.tetris;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JPanel;




/**
 * @author bob
 *
 */
public class World extends JPanel{
	private static BufferedImage bg;
	private static BufferedImage pause;
	private static BufferedImage gameover;
	private Timer timer;
	private Shape shape=nextOne();
	/**
	 * ��һ��ͼ��
	 */
	private Shape shapenext=nextOne();
	private static List<Cell> cellLists= new ArrayList<Cell>();
	/**
	 * �����������飬ֵΪtrueʱ���Ӵ���ͼ�Σ�Ϊfalseʱ���Ӵ���ͼ�Σ���ʼ��Ĭ��Ϊfalse
	 */
	private static boolean[][] wall=new boolean[21][12];
	private static final int SHAPE_STOP=0;
	private static final int SHAPE_MOVE=1;
	private static final int RUNNING=0;
	private static final int PAUSE=1;
	private static final int GAME_OVER=2;

	private int score=0;
	private int line=0;
	private int level=1;
	int dropIndex=0;
	int shape_state=SHAPE_MOVE;
	int game_state=RUNNING;
	int interval=10;
	static {
		bg=Shape.getImage("tetris.png");
		gameover=Shape.getImage("game-over.png");
		pause=Shape.getImage("pause.png");
		bg=Shape.getImage("tetris.png");
		for(int i=0;i<20;i++){
			for(int j=0;j<10;j++){
				wall[i][j]=false;
			}
		}
	}
	public void action(){
		KeyListener l= new KeyAdapter(){
			public void keyPressed(KeyEvent e ){
				int key=e.getKeyCode();
				RunningKeyAdapter(key);
				repaint();
			}
		};
		this.addKeyListener(l);
		this.setFocusable(true);
		this.requestFocus();
		timer=new Timer();
		timer.schedule(new TimerTask(){

			@Override
			public void run() {
				switch(game_state){
				case RUNNING:
					if(dropIndex++%80==0){
						if(shape_state==SHAPE_MOVE){	
							dropAction();	
						}
					}
					shapeIsStop();
					hitAction();
					lineDisapearAction();
					checkGameOver();
					break;
				}  
				repaint();
			}
		}, interval,interval);
	}
	/**
	 * ����״̬���̼����¼�
	 * @param key
	 */
	public void RunningKeyAdapter(int key){
		switch(key){
		case KeyEvent.VK_UP:
			rollAction();
			break;
		case KeyEvent.VK_DOWN:
			dropAction();
			break;
		case KeyEvent.VK_LEFT:
			leftAction();
			break;
		case KeyEvent.VK_RIGHT:
			rightAction();
			break;
		case KeyEvent.VK_S:
			if(game_state==GAME_OVER){
				restartAction();
			}
			break;
		case KeyEvent.VK_P:  
			game_state=PAUSE;
			break;
		case KeyEvent.VK_C:
			if(game_state==PAUSE){	
				game_state=RUNNING;
			}
			break;
			
		case KeyEvent.VK_SPACE://�����½�ʱÿһ�ζ���Ҫ�ж��Ƿ�ֹͣ
			if(game_state==RUNNING){
				for(int i=0;i<22;i++){
					shapeIsStop();
					hitAction();
					dropAction();
				}
			}
			break;
		case KeyEvent.VK_Q:  
			System.exit(0);
			break;
		}
	}
	/**
	 * ������һ��ͼ��
	 * @return
	 */
	public Shape nextOne(){
		int i=(int) (Math.random()*7);
		switch(i){
		case 0:
			return new I();

		case 1:
			return new J();
		case 2:
			return new L();
		case 3:
			return new O();
		case 4:
			return new S();
		case 5:
			return new Z();
		case 6:
			return new I();
		}
		return null;
	}
	/**
	 * �ж�ͼ���Ƿ�ֹͣ���ƶ�
	 */
	public void shapeIsStop(){
		Cell[] cells=shape.getCells();
		for(Cell cell:cells){
			if(cell.getRow()>=20){
				shape_state=SHAPE_STOP;

			}

		}

		changeShapeState();
	}
	/**
	 * �½�����
	 */
	public void dropAction(){
		if(shape_state==SHAPE_MOVE&&game_state==RUNNING){
			shape.drop();
		}

	}
	/**
	 * ��ת����
	 */
	public void rollAction(){
		if(shape_state==SHAPE_MOVE){
			shape.RightRoll();
			Cell[] cells=shape.getCells();
			for(Cell cell:cells){
				if(cell.getRow()<0||cell.getCol()<0||cell.getRow()>20||cell.getCol()>=11){
					shape.LeftRoll();
					break;
				}
			}
		}
	}
	/**
	 * ���ƶ���
	 */
	public void leftAction(){
		if(shape_state==SHAPE_MOVE){
			Cell []cells=shape.getCells();
			for(Cell cell:cells){
				if(cell.getCol()<=0){//�����߽�ʱ���� �ƶ�
					return;
				}
				int col=cell.getCol()-1;
				int row=cell.getRow();
				if(wall[row][col]==true){//����߸��ӱ�ռʱ�����ƶ�
					return;
				}
			}
			shape.left();
		}
	}
	/**
	 * ���ƶ���
	 */
	public void rightAction(){
		if(shape_state==SHAPE_MOVE){
			Cell []cells=shape.getCells();
			for(Cell cell:cells){//���ҽ�߽�ʱ���� �ƶ�
				if(cell.getCol()>=10){
					return;
				}
				int col=cell.getCol()+1;
				int row=cell.getRow();
				if(wall[row][col]==true){//���ұ߸��ӱ�ռʱ�����ƶ�
					return;
				}
			}
			shape.right();
		}
	}
	/**
	 * ���ĳ���Ƿ�������Ҫ��ʧ
	 */
	public void lineDisapearAction(){
		int index=0;
		for(int i=20;i>=0;i--){//���м���Ƿ�ͼռ��������������ϼ�⣬�Ա�����һ����ʧ�������Ķ��½�һ��
			for(int j=0;j<=10;j++){
				if(wall[i][j]==true){
					index++;
				}	
			}
			if(index%11==0&&index!=0){///ÿһ����11����ʼ��ʧ
				for(int k=0;k<=10;k++){
					Cell cell=new Cell(i,k,null);
					Iterator<Cell> it=cellLists.iterator();
					while(it.hasNext()){
						Cell ce=it.next();
						if(ce.equals(cell)){
							it.remove();//��cellLists�������Ƴ���ʧ�ĸ���
						}
					}

				}
				score+=10;
				line+=1;
				if(line%11==0){
					level+=1;
				}
				Iterator<Cell> it=cellLists.iterator();
				while(it.hasNext()){
					Cell c=it.next();
					c.drop();//��ʣ���ÿ�������½�1��
				}
				updateCellListsAndWall();//���±�������ǽ
			}
			index=0;
		}
	}
	/**
	 * ���¿���ǽ��״̬
	 */
	public void updateCellListsAndWall(){
		for(int i=0;i<=20;i++){
			for(int j=0;j<=10;j++){
				wall[i][j]=false;
			}
		}
		for(Cell cell:cellLists){
			int row=cell.getRow();
			int col=cell.getCol();
			wall[row][col]=true;
		}

	}
	/**
	 * ��ײ���,��������������ǽ�еĸ���ֵΪtrue,���ô� ���и���
	 */
	public void hitAction(){
		Cell cells[]=shape.getCells();
		for(Cell cell:cells){
			int row=cell.getRow();
			int col=cell.getCol();
			if(row!=20){
				if(wall[row+1][col]==true){
					shape_state=SHAPE_STOP;
					changeShapeState();
					break;
				}
			}

		}
	}
	/**
	 * ��ǰͼ��ֹͣ�ƶ��󣬽���ǰͼ�δ��뼯�ϣ�������Ӧ�ı������ӵ�ֵ��Ϊtrue��һ��ͼ���볡
	 */
	public void changeShapeState(){
		Cell[] cells=shape.getCells();
		if(shape_state==SHAPE_STOP){
			//��ֹͣ��shapeͼ�ε�ÿ�����Ӵ����ϣ��ػ�
			for(Cell cell:cells){
				int row=cell.getRow();
				int col=cell.getCol();
				wall[row][col]=true;
				cellLists.add(cell);
			}
			shape=shapenext;
			shapenext=nextOne();
			shape_state=SHAPE_MOVE;
		}
	}
	/**
	 *��ͼ����
	 */
	public void paint(Graphics g){
		g.drawImage(bg, 0,0, null);
		Cell[]cs=shape.getCells();
		for(Cell cell:cs){
			cell.paint(g);
		}
		for(Cell cell:cellLists){
			cell.paint(g);
		}
		Cell[]cells=shapenext.getCells();
		for(Cell cell:cells){
			int col=cell.getCol();
			int row=cell.getRow();
			cell.setCol(col+10);
			cell.setRow(row+2);
			cell.paint(g);//���ұ���ʾ����һ������
			cell.setCol(col);//����֮����뽫col��Ϊԭλ�� ����Ȼÿ�ε���paint��������ʹ��col���ӣ�һֱ����
			cell.setRow(row);
		}
		g.setFont(new Font(Font.SERIF,Font.BOLD,30));
		g.setColor(new Color(0xAD745A));
		g.drawString("score:"+score,400, 200);
		g.drawString("line:"+line,400, 257);
		g.drawString("level:"+level,400, 320);
		if(game_state==PAUSE){
			g.drawImage(pause, 0,0,null);
		}
		if(game_state==GAME_OVER){
			g.drawImage(gameover, 0,0,null);
		}

	}
	/**
	 * �����Ϸ�Ƿ����
	 */
	public void checkGameOver(){
			for(int j=0;j<10;j++){
				if(wall[0][j]==true){
					
					game_state=GAME_OVER;
				}
			}
		
	}
	public void restartAction(){
		score=0;
		line=1;
		level=0;
		cellLists= new ArrayList<Cell>();
		wall=new boolean[21][12];
		game_state=RUNNING;
	}
	public static void main(String[] args){

		JFrame frame =new JFrame();
		World world =new World();
		frame.add(world);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(608,664);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		world.action();
	}
}
