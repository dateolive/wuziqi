package wuziqi;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.security.GuardedObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

//�������
public class GoBangPanel extends JPanel{
	private int currentPlayer=GoBangContantes.BLACK;
	//trueΪ��Ϸ����
	private boolean isGameOver=false;
	//��¼���ӵĲ���
	private int count=0;
	//ģʽtrueΪ���˶�ս falseΪ�˻���ս
	private boolean model;
	//true����ֵ���� false����ֵ����+������
	private boolean intel;
	//������Ⱥͽڵ�����
	private int depth;
	private int nodeCount;
	//trueΪ�������֣�falseΪ��������
	private boolean first;
	//
	private JTextArea textArea;
	//��ʾ״̬
	private boolean showOrder;
	
	private ChessBean chessBeanBytree;
	
	private int x,y;
	ChessBean[][] chessBeans=new ChessBean[GoBangContantes.LINE_NUMBER][GoBangContantes.LINE_NUMBER];
	//��������
	public GoBangPanel(){
		//setSize(650,700);
		setPreferredSize(new Dimension(GoBangContantes.PANEL_WIDTH, GoBangContantes.PANEL_HEIGHT));
		setBackground(Color.ORANGE);
		//���̰�����ƶ��¼�
		addMouseMotionListener(mouseMotionListener);
		//���̰�������¼�
		addMouseListener(mouseListener);
		//�������ϵ����ӳ�ʼ��
		for(int i=0;i<chessBeans.length;i++){
			for(int j=0;j<chessBeans[i].length;j++)
			{
				
				chessBeans[i][j]=new ChessBean(i,j,0,0);
				//���� b-w-b-w
				//chessBeans[i][j]=new ChessBean(i,j,currentPlayer,i*chessBeans.length+j);
				//currentPlayer=3-currentPlayer;
			}
		}
		
	}
	//���¼�
	MouseMotionListener mouseMotionListener=new MouseMotionListener() {
		
		@Override
		public void mouseMoved(MouseEvent arg0) {
			//����ƶ�����ȡ����
			int x_mv=arg0.getX();
			int y_mv=arg0.getY();
			
			//�ж�Խ��
			if(x_mv>=GoBangContantes.OFFSET&&x_mv<=GoBangContantes.OFFSET+(GoBangContantes.LINE_NUMBER-1)*GoBangContantes.LINE_SIZE
					&&y_mv>=GoBangContantes.OFFSET&&y_mv<=GoBangContantes.OFFSET+(GoBangContantes.LINE_NUMBER-1)*GoBangContantes.LINE_SIZE)
			{
				x=(x_mv-GoBangContantes.OFFSET/2)/GoBangContantes.LINE_SIZE;
				y=(y_mv-GoBangContantes.OFFSET/2)/GoBangContantes.LINE_SIZE;
				repaint();
			}
		}
		
		@Override
		public void mouseDragged(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
	};
	
	//��������
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2d=(Graphics2D) g;
		g2d.setStroke(new BasicStroke(2));//�Ӵ���
		drawLine(g2d);//������
		drawStar(g2d);//����Ԫ����
		drawTrips(g2d);//�����ѿ�	
		drawNumber(g2d);//������
		drawChess(g2d);//��������
		drawOrderNumber(g2d);//��������˳��
	}

	




	//��������
	private void drawLine(Graphics2D g2d){
		//����
		for(int i=0;i<GoBangContantes.LINE_NUMBER;i++){
			g2d.drawLine(GoBangContantes.OFFSET, GoBangContantes.OFFSET+i*GoBangContantes.LINE_SIZE,
					GoBangContantes.OFFSET+(15-1)*GoBangContantes.LINE_SIZE, GoBangContantes.OFFSET+i*GoBangContantes.LINE_SIZE);
		}
		//����
		for(int i=0;i<GoBangContantes.LINE_NUMBER;i++){
			g2d.drawLine(GoBangContantes.OFFSET+i*GoBangContantes.LINE_SIZE,GoBangContantes.OFFSET, 
					GoBangContantes.OFFSET+i*GoBangContantes.LINE_SIZE,GoBangContantes.OFFSET+(15-1)*GoBangContantes.LINE_SIZE);
		}
	}
	//����Ԫ����
	private void drawStar(Graphics2D g2d){
		int center=GoBangContantes.LINE_NUMBER/2;
		int quarter=GoBangContantes.LINE_NUMBER/4;
		//��Ԫ
		g2d.fillOval(center*GoBangContantes.LINE_SIZE+GoBangContantes.OFFSET-GoBangContantes.STAR/2,center*GoBangContantes.LINE_SIZE+GoBangContantes.OFFSET-GoBangContantes.STAR/2,
				GoBangContantes.STAR,GoBangContantes.STAR);
		//���Ͻ�����
		g2d.fillOval(quarter*GoBangContantes.LINE_SIZE+GoBangContantes.OFFSET-GoBangContantes.STAR/2,quarter*GoBangContantes.LINE_SIZE+GoBangContantes.OFFSET-GoBangContantes.STAR/2,
				GoBangContantes.STAR,GoBangContantes.STAR);
		//���½�����
		g2d.fillOval(quarter*GoBangContantes.LINE_SIZE+GoBangContantes.OFFSET-GoBangContantes.STAR/2,(GoBangContantes.LINE_NUMBER-quarter-1)*GoBangContantes.LINE_SIZE+GoBangContantes.OFFSET-GoBangContantes.STAR/2,
				GoBangContantes.STAR,GoBangContantes.STAR);
		//���Ͻ�����
		g2d.fillOval((GoBangContantes.LINE_NUMBER-quarter-1)*GoBangContantes.LINE_SIZE+GoBangContantes.OFFSET-GoBangContantes.STAR/2,quarter*GoBangContantes.LINE_SIZE+GoBangContantes.OFFSET-GoBangContantes.STAR/2,
				GoBangContantes.STAR,GoBangContantes.STAR);
		//���½�����
		g2d.fillOval((GoBangContantes.LINE_NUMBER-quarter-1)*GoBangContantes.LINE_SIZE+GoBangContantes.OFFSET-GoBangContantes.STAR/2,(GoBangContantes.LINE_NUMBER-quarter-1)*GoBangContantes.LINE_SIZE+GoBangContantes.OFFSET-GoBangContantes.STAR/2,
				GoBangContantes.STAR,GoBangContantes.STAR);
	}
	//�������ѿ�
	private void drawTrips(Graphics2D g2d) {
		g2d.setColor(Color.red);
		//����������
		int x_tmp=GoBangContantes.OFFSET+x*GoBangContantes.LINE_SIZE;
		int y_tmp=GoBangContantes.OFFSET+y*GoBangContantes.LINE_SIZE;
		
		int half=GoBangContantes.LINE_SIZE/2;
		int quarter=GoBangContantes.LINE_SIZE/4;
		//���Ͻ�
		g2d.drawLine(x_tmp-half,y_tmp-half, x_tmp-half+quarter,y_tmp-half);
		g2d.drawLine(x_tmp-half,y_tmp-half, x_tmp-half,y_tmp-half+quarter);
		
		//���½�
		g2d.drawLine(x_tmp-half,y_tmp+half, x_tmp-half+quarter,y_tmp+half);
		g2d.drawLine(x_tmp-half,y_tmp+half, x_tmp-half,y_tmp+half-quarter);
		
		//���Ͻ�
		g2d.drawLine(x_tmp+half,y_tmp-half, x_tmp+half-quarter,y_tmp-half);
		g2d.drawLine(x_tmp+half,y_tmp-half, x_tmp+half,y_tmp-half+quarter);
				
		//���½�
		g2d.drawLine(x_tmp+half,y_tmp+half, x_tmp+half-quarter,y_tmp+half);
		g2d.drawLine(x_tmp+half,y_tmp+half, x_tmp+half,y_tmp+half-quarter);
				
	}
	//������
	private void drawNumber(Graphics2D g2d) {

		g2d.setColor(Color.BLACK);
		for(int i=GoBangContantes.LINE_NUMBER;i>0;i--)
		{
			FontMetrics fn=g2d.getFontMetrics();
			int height=fn.getAscent();
			g2d.drawString(16-i+"",10,i*GoBangContantes.LINE_SIZE+height/2);
			
			int width=fn.stringWidth(((char)(64+i))+"");
			g2d.drawString(((char)(64+i))+"",GoBangContantes.LINE_SIZE*i-width/2, 
					GoBangContantes.OFFSET+GoBangContantes.LINE_NUMBER*GoBangContantes.LINE_SIZE);
			
		}
		
	}
	
	//��������
	private void drawChess(Graphics2D g2d) {
		for(int i=0;i<chessBeans.length;i++){
			for(int j=0;j<chessBeans[i].length;j++)
			{
					ChessBean bean=chessBeans[i][j];
					//�п�
					if(bean.getPlayer()!=GoBangContantes.EMPTY)
					{
						//����
						if(bean.getPlayer()==GoBangContantes.BLACK){
							g2d.setColor(Color.black);
						
						}//����
						else if(bean.getPlayer()==GoBangContantes.WHITE){
							g2d.setColor(Color.white);
						}
					
					//��������
					int x_tmp=GoBangContantes.OFFSET+bean.getX()*GoBangContantes.LINE_SIZE;
					int y_tmp=GoBangContantes.OFFSET+bean.getY()*GoBangContantes.LINE_SIZE;
					
					int width=GoBangContantes.LINE_SIZE/5*3;
					g2d.fillOval(x_tmp-width/2, y_tmp-width/2, width, width);
					}
					
				}
			}
			if(!showOrder){
				//��ȡ���һ������
				ChessBean bean=getLastBean();
				if(bean!=null){
					//������ʾ��Ҫô�����֣�Ҫô���һ�������Ǻ��
					g2d.setColor(Color.red);
					int width=GoBangContantes.LINE_SIZE/5;
					
					//��������
					int x_tmp=GoBangContantes.OFFSET+bean.getX()*GoBangContantes.LINE_SIZE;
					int y_tmp=GoBangContantes.OFFSET+bean.getY()*GoBangContantes.LINE_SIZE;
					g2d.fillRect(x_tmp-width/2,y_tmp-width/2, width, width);
				}
				
			}
	
			
		}

		//��ѯ���һ������
	private ChessBean getLastBean() {
			ChessBean bean=null;
			for(int i=0;i<chessBeans.length;i++){
				for(int j=0;j<chessBeans[i].length;j++)
				{
					ChessBean tmp=chessBeans[i][j];
					if(tmp.getPlayer()!=GoBangContantes.EMPTY)
					{
						if(bean==null)
							bean=tmp;
						else
						{
							if(tmp.getOrderNumber()>bean.getOrderNumber())
								bean=tmp;
						}
					}
					
				}
			}
			return bean;
		}
	
	//������������˳��
	private void drawOrderNumber(Graphics2D g2d) {
		if(showOrder){
			g2d.setColor(Color.red);
			for(int i=0;i<chessBeans.length;i++){
				for(int j=0;j<chessBeans[i].length;j++)
				{
					ChessBean bean=chessBeans[i][j];
					//�п�
					if(bean.getPlayer()!=GoBangContantes.EMPTY)
					{
						int number=bean.getOrderNumber();
						FontMetrics fontMetrics=g2d.getFontMetrics();
						int width=fontMetrics.stringWidth(number+"");
						int height=fontMetrics.getAscent();
						
						//��������
						int x_tmp=GoBangContantes.OFFSET+bean.getX()*GoBangContantes.LINE_SIZE;
						int y_tmp=GoBangContantes.OFFSET+bean.getY()*GoBangContantes.LINE_SIZE;
						
						g2d.drawString(number+"", x_tmp-width/2, y_tmp+height/2);
					}
				}
			}
		}

		
	}



	//��ʾ״̬�ж�
	public void showOrder(boolean selected) {
		this.showOrder=selected;
		//�ػ�����
		repaint();
		
	}



	//����Ϸ
	public void newGame(boolean model, boolean intel, int depth,int nodeCount, boolean first, boolean showorder, JTextArea textArea) 
	{
		this.model=model;
		this.intel=intel;
		this.depth=depth;
		this.nodeCount=nodeCount;
		this.first=first;
		this.showOrder=showorder;
		this.textArea=textArea;
		this.textArea.setText("");
		//��ֹ�������Ϸ����һ���Ǻ���
		currentPlayer=GoBangContantes.BLACK;
		//��ʼ��
		for(int i=0;i<chessBeans.length;i++){
			for(int j=0;j<chessBeans[i].length;j++)
			{
				
				chessBeans[i][j]=new ChessBean(i,j,0,0);
			}
		}
		isGameOver=false;
		count=0;
		JOptionPane.showMessageDialog(GoBangPanel.this, "����Ϸ��ʼ��");
		
		if(!model&&!first)
		{
			//�˻���ս����������
			int center=GoBangContantes.LINE_NUMBER/2;
			chessBeans[center][center].setPlayer(currentPlayer);
			chessBeans[center][center].setOrderNumber(count);
			count++;
			currentPlayer=3-currentPlayer;
		}
		
		repaint();
	}
	
	
	MouseListener mouseListener =new MouseListener() {
		
		@Override
		public void mouseClicked(MouseEvent e) {
			if(isGameOver){
				JOptionPane.showMessageDialog(GoBangPanel.this, "��Ϸ�Ѿ�������");
				return;
			}
			//��ȡ����
			//����ƶ�����ȡ����
			int x_mv=e.getX();
			int y_mv=e.getY();
			
			
			//�ж�Խ��
			if(x_mv>=GoBangContantes.OFFSET&&x_mv<=GoBangContantes.OFFSET+(GoBangContantes.LINE_NUMBER-1)*GoBangContantes.LINE_SIZE
					&&y_mv>=GoBangContantes.OFFSET&&y_mv<=GoBangContantes.OFFSET+(GoBangContantes.LINE_NUMBER-1)*GoBangContantes.LINE_SIZE)
			{
				x=(x_mv-GoBangContantes.OFFSET/2)/GoBangContantes.LINE_SIZE;
				y=(y_mv-GoBangContantes.OFFSET/2)/GoBangContantes.LINE_SIZE;
				if(e.getButton()==MouseEvent.BUTTON1){
					//���������
					if(model){
						//���˶�ս
						if(x>=0&&x<GoBangContantes.LINE_NUMBER&&y>=0&&y<GoBangContantes.LINE_NUMBER)
						{
							//����
							if(chessBeans[x][y].getPlayer()==GoBangContantes.EMPTY)
							{
								//Ϊ������
								chessBeans[x][y]=new ChessBean(x,y,currentPlayer,count);
								count++;
								currentPlayer=3-currentPlayer;
								checkWin(chessBeans[x][y]);
								repaint();
							}
						}
					
					}else{
						//�˻���ս
						if(intel){
							//��ֵ����
							if(x>=0&&x<GoBangContantes.LINE_NUMBER&&y>=0&&y<GoBangContantes.LINE_NUMBER)
							{
								//����
								if(chessBeans[x][y].getPlayer()==GoBangContantes.EMPTY)
								{
									//Ϊ������
									chessBeans[x][y]=new ChessBean(x,y,currentPlayer,count);
									count++;
									currentPlayer=3-currentPlayer;
									boolean win=checkWin(chessBeans[x][y]);
									repaint();
									
									//��������
									if(!win){
										
										List<ChessBean> list=getSortedBean(currentPlayer);
										if(list.size()>0){
											//��ȡ���ķ�ֵ
											ChessBean bean=list.get(0);
											bean.setPlayer(currentPlayer);
											bean.setOrderNumber(count);
											count++;
											currentPlayer=3-currentPlayer;
											chessBeans[bean.getX()][bean.getY()]=bean;
											checkWin(chessBeans[bean.getX()][bean.getY()]);
											repaint();
										}
									}
									
								}
							}
						
							
							
							
						}else{
							//��ֵ����+������
							if(x>=0&&x<GoBangContantes.LINE_NUMBER&&y>=0&&y<GoBangContantes.LINE_NUMBER)
							{
								//����
								if(chessBeans[x][y].getPlayer()==GoBangContantes.EMPTY)
								{
									//Ϊ������
									chessBeans[x][y]=new ChessBean(x,y,currentPlayer,count);
									count++;
									currentPlayer=3-currentPlayer;
									boolean win=checkWin(chessBeans[x][y]);
									repaint();
									
								
									//��������
									if(!win){
										
										getByTree2(0,currentPlayer,chessBeans,-Integer.MAX_VALUE,Integer.MAX_VALUE);
										ChessBean bean=chessBeanBytree;
										bean.setPlayer(currentPlayer);
										bean.setOrderNumber(count);
										count++;
										currentPlayer=3-currentPlayer;
										chessBeans[bean.getX()][bean.getY()]=bean;
										checkWin(chessBeans[bean.getX()][bean.getY()]);
										repaint();
										
										
										/*ChessBean bean=getByTree(0,currentPlayer,chessBeans);
										if(bean!=null)
										{
											bean.setPlayer(currentPlayer);
											bean.setOrderNumber(count);
											count++;
											currentPlayer=3-currentPlayer;
											chessBeans[bean.getX()][bean.getY()]=bean;
											checkWin(chessBeans[bean.getX()][bean.getY()]);
											repaint();
										}*/
										
										
									}
									
								}
							}
							
						}
					}
					
					
				}else if(e.getButton()==MouseEvent.BUTTON3){
					//�������Ҽ�
					ChessBean bean=chessBeans[x][y];
					int offense=getValue(bean,currentPlayer);
					int defense=getValue(bean,3-currentPlayer);
					int sum=offense+defense;
					chessBeans[x][y].getBuffer().append("��(" + x + "," + y + ")��" + "����:" + offense + " "
							+ "����:" + defense + " " + "�ܺ�:" + (sum) + "\n\n");
					textArea.append(chessBeans[x][y].getBuffer().toString());

				}
				
				
				
			}
		}





		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
		
	};
	//�������������в�ѯ
	protected ChessBean getByTree(int depth, int currentPlayer,ChessBean[][] chessBeans) {
		//ģ�����壬��¡����
		ChessBean[][] chessBeans2=clone(chessBeans);
		//�����λ�õĵ÷�
		List<ChessBean> list=getSortedBean(currentPlayer,chessBeans2);
		if(this.depth==depth){
			//������ָ�����
			return list.get(0);
		}
		for(int i=0;i<nodeCount;i++){
			ChessBean chessBean=list.get(i);
			if(chessBean.getSum()>Level.ALIVE_4.score){
				return chessBean;
			}else{
				//ģ�����壬�����ݹ�
				chessBeans2[chessBean.getX()][chessBean.getY()].setPlayer(currentPlayer);
				return getByTree(depth+1,3-currentPlayer , chessBeans2);
			}
		}
		return null;
	}

//����Сֵ����
	protected int getByTree2(int depth, int currentPlayer,ChessBean[][] chessBeans, int alpha, int beta) {
		//ģ�����壬��¡����
		ChessBean[][] chessBeans2=clone(chessBeans);
		//�����λ�õĵ÷�
		List<ChessBean> list=getSortedBean(currentPlayer,chessBeans2);
		if(this.depth==depth){
			//������ָ�����
			return list.get(0).getSum();
		}
		for(int i=0;i<nodeCount;i++){
			ChessBean chessBean=list.get(i);
			int score;
			if(chessBean.getSum()>Level.ALIVE_4.score){
				score=chessBean.getSum();
			}else{
				//ģ�����壬�����ݹ�
				chessBeans2[chessBean.getX()][chessBean.getY()].setPlayer(currentPlayer);
				score=getByTree2(depth+1,3-currentPlayer , chessBeans2,alpha,beta);
				
			}
			if (depth % 2 == 0) {
				// �Լ��������ֵ
				if (score > alpha) {
					alpha = score;
					if (depth == 0) {
						// ���
						chessBeanBytree=chessBean;
						// System.out.println(chessBeansForTree);
					}
				}
				if (alpha >= beta) {
					// ��֦
					score = alpha;
					return score;
				}
			} else {
				if (score < beta) {
					beta = score;
				}
				if (alpha >= beta) {
					// ��֦
					score = beta;
					return score;
				}
			}
		}
		return depth % 2 == 0 ? alpha : beta;
	}

		
	






	private List<ChessBean> getSortedBean(int currentPlayer, ChessBean[][] chessBeans) {
		
		List<ChessBean> list=new ArrayList<ChessBean>();
		//��ʼ��
		for(int i=0;i<chessBeans.length;i++){
			for(int j=0;j<chessBeans[i].length;j++)
			{
				//���ӳ�ʼ��
				ChessBean bean=chessBeans[i][j];
				if(bean.getPlayer()==GoBangContantes.EMPTY)
				{
					//��λ��Ϊ�գ�����÷�
					int offense=getValue(bean,currentPlayer);
					int defense=getValue(bean,3-currentPlayer);
					
					bean.setOffense(offense);
					bean.setDefense(defense);
					bean.setSum(offense+defense);
					list.add(bean);
				}
			}
		}
		//�����ܵ÷ֽ��д�С����
		Collections.sort(list);
		return list;
	}


	//�����¡
	private ChessBean[][] clone(ChessBean[][] chessBeans) {
		ChessBean[][] result=new ChessBean[GoBangContantes.LINE_NUMBER][GoBangContantes.LINE_NUMBER];
		for(int i=0;i<result.length;i++)
			for(int j=0;j<result[i].length;j++)
			{
				result[i][j]=new ChessBean(chessBeans[i][j].getX(),chessBeans[i][j].getY(),
						chessBeans[i][j].getPlayer(),chessBeans[i][j].getOrderNumber());
			}
		return result;
	}
	//�ж��Ƿ���Ӯ
	private boolean checkWin(ChessBean chessBean) {
		boolean result=false;
		//�ж��ĸ��������ӵ�״̬
		//��
		if(checkChessCount(chessBean,-1,0)+checkChessCount(chessBean,1,0)>=4)
		{
			result=true;
		}
		if(checkChessCount(chessBean,0,-1)+checkChessCount(chessBean,0,1)>=4)
		{
			result=true;
		}
		if(checkChessCount(chessBean,1,1)+checkChessCount(chessBean,-1,-1)>=4)
		{
			result=true;
		}
		if(checkChessCount(chessBean,1,-1)+checkChessCount(chessBean,-1,1)>=4)
		{
			result=true;
		}
		if(result){
			//��Ϸ����
			JOptionPane.showMessageDialog(GoBangPanel.this, "��Ϸ����");
			isGameOver=true;
		}
		return result;
	}






	protected List<ChessBean> getSortedBean(int currentPlayer) {
		List<ChessBean> list=new ArrayList<ChessBean>();
		//��ʼ��
		for(int i=0;i<chessBeans.length;i++){
			for(int j=0;j<chessBeans[i].length;j++)
			{
				//���ӳ�ʼ��
				ChessBean bean=chessBeans[i][j];
				if(bean.getPlayer()==GoBangContantes.EMPTY)
				{
					//��λ��Ϊ�գ�����÷�
					int offense=getValue(bean,currentPlayer);
					int defense=getValue(bean,3-currentPlayer);
					
					bean.setOffense(offense);
					bean.setDefense(defense);
					bean.setSum(offense+defense);
					list.add(bean);
				}
			}
		}
		//�����ܵ÷ֽ��д�С����
		Collections.sort(list);
		return list;
	}





	//���ĸ�������мƷ�
	private int getValue(ChessBean bean, int currentPlayer) {
		//����4�����������level
		Level level1=getLevel(bean,currentPlayer,Direction.HENG);
		Level level2=getLevel(bean,currentPlayer,Direction.SHU);
		Level level3=getLevel(bean,currentPlayer,Direction.PIE);
		Level level4=getLevel(bean,currentPlayer,Direction.NA);
		return getValueByLevel(level1,level2,level3,level4)+position[bean.getX()][bean.getY()];
	}




	//���4������������Ƿ��ظ�
	private int getValueByLevel(Level level1, Level level2, Level level3,Level level4) {
		int[] levelCount=new int[Level.values().length];
		for(int i=0;i<levelCount.length;i++){
			levelCount[i]=0;
		}
		levelCount[level1.index]++;
		levelCount[level2.index]++;
		levelCount[level3.index]++;
		levelCount[level4.index]++;
		
		int score = 0;
		if (levelCount[Level.GO_4.index] >= 2
				|| levelCount[Level.GO_4.index] >= 1 && levelCount[Level.ALIVE_3.index] >= 1)// ˫��4����4����
			score = 10000;
		else if (levelCount[Level.ALIVE_3.index] >= 2)// ˫��3
			score = 5000;
		else if (levelCount[Level.SLEEP_3.index] >= 1 && levelCount[Level.ALIVE_3.index] >= 1)// ��3��3
			score = 1000;
		else if (levelCount[Level.ALIVE_2.index] >= 2)// ˫��2
			score = 100;
		else if (levelCount[Level.SLEEP_2.index] >= 1 && levelCount[Level.ALIVE_2.index] >= 1)// ��2��2
			score = 10;
		score = Math.max(score, Math.max(Math.max(level1.score, level2.score), Math.max(level3.score, level4.score)));
		return score;
	}






	//����ĳ�����������
	private Level getLevel(ChessBean bean, int currentPlayer, Direction dire) {
		String left="";
		String right="";
		if(dire==Direction.HENG){
			left=getStringByDire(bean,currentPlayer,-1,0);
			right=getStringByDire(bean,currentPlayer,1,0);
		}else if(dire==Direction.SHU){
			left=getStringByDire(bean,currentPlayer,0,1);
			right=getStringByDire(bean,currentPlayer,0,-1);
		}else if(dire==Direction.PIE){
			left=getStringByDire(bean,currentPlayer,1,1);
			right=getStringByDire(bean,currentPlayer,-1,-1);
		}else if(dire==Direction.NA){
			left=getStringByDire(bean,currentPlayer,-1,1);
			right=getStringByDire(bean,currentPlayer,1,-1);
		}
		
		//������
		String str=left+currentPlayer+right;
		
		//chessBeans[bean.getX()][bean.getY()].getBuffer().append("(" + (bean.getX() + 1) + "," + (bean.getY()- 1) + ")" + dire + "\t" + str + "\t");
		//����
		String strres=new StringBuffer(str).reverse().toString();
		
		for(Level level:Level.values()){
			//����������ʽ���бȽ�
			Pattern pattern=Pattern.compile(level.regex[currentPlayer-1]);
			
			Matcher matcher=pattern.matcher(str);
			//ΪtrueΪ1
			boolean b1=matcher.find();
			
			Matcher matcher2=pattern.matcher(strres);
			//ΪtrueΪ1
			boolean b2=matcher2.find();
			
			if(b1||b2){
				//ƥ��ɹ�
				return level;
			}
		}
		
		return Level.NULL;
	}






	private String getStringByDire(ChessBean bean, int currentPlayer2, int x,int y) {
		boolean res=false;
		if(y>0||(y==0&&x<0))
		{
			//����ƴ��
			res=true;
		}
		int x_tmp=bean.getX();
		int y_tmp=bean.getY();
		String str="";
		for(int i=0;i<5;i++){
			x_tmp+=x;
			y_tmp+=y;
			if(x_tmp>=0&&x_tmp<GoBangContantes.LINE_NUMBER&&y_tmp>=0&&y_tmp<GoBangContantes.LINE_NUMBER)
			{
				if(res){
					//����ƴ��
					str=chessBeans[x_tmp][y_tmp].getPlayer()+str;
				}else{
					str+=chessBeans[x_tmp][y_tmp].getPlayer();
				}
			}
		}
		
		return str;
	}






	private int checkChessCount(ChessBean chessBean, int x, int y) {
		//�ж�ĳ������4������
		int sum=0;
		int x_tmp=chessBean.getX();
		int y_tmp=chessBean.getY();
		for(int i=0;i<4;i++)
		{
			x_tmp+=x;
			y_tmp+=y;
			if(x_tmp>=0&&x_tmp<GoBangContantes.LINE_NUMBER&&y_tmp>=0&&y_tmp<GoBangContantes.LINE_NUMBER)
			{
				if(chessBeans[x_tmp][y_tmp].getPlayer()==chessBean.getPlayer())
				{
					sum++;
				}
				else
				{
					break;
				}
			}
		}
		
		return sum;
	}






	public void huiqi() {
		if(isGameOver){
			JOptionPane.showMessageDialog(GoBangPanel.this, "���ȿ�ʼ��Ϸ");
			return;
		}else{
			if(count>0){
				ChessBean chessBean=getLastBean();
				chessBeans[chessBean.getX()][chessBean.getY()].setOrderNumber(0);
				chessBeans[chessBean.getX()][chessBean.getY()].setPlayer(GoBangContantes.EMPTY);
				count--;
				repaint();
				
			}
			else{
				JOptionPane.showMessageDialog(GoBangPanel.this, "��������");
			}
		}
		
	}
	
	public void huiqi2() { 
		if (isGameOver) {
			JOptionPane.showMessageDialog(GoBangPanel.this, "���ȿ�ʼ����Ϸ��");
		} else {
			if (count > 2) {
				for (int i = 0; i < 2; i++) {
					ChessBean tempBean =getLastBean();
					currentPlayer = tempBean.getPlayer();
					chessBeans[tempBean.getX()][tempBean.getY()].setPlayer(GoBangContantes.EMPTY); //
					chessBeans[tempBean.getX()][tempBean.getY()].setOrderNumber(0);
					count--;
					repaint();
				}
			} else {
				JOptionPane.showMessageDialog(GoBangPanel.this, "�㻹û�����أ�");
			}
		}
	}

	// ������Ϣ
	public static enum Level {
		CON_5("����", 0, new String[] { "11111", "22222" }, 100000),
		ALIVE_4("����", 1, new String[] { "011110", "022220" }, 10000),
		GO_4("����", 2, new String[] { "011112|0101110|0110110", "022221|0202220|0220220" }, 500),
		DEAD_4("����", 3, new String[] { "211112", "122221" }, -5),
		ALIVE_3("����", 4, new String[] { "01110|010110", "02220|020220" }, 200),
		SLEEP_3("����", 5,
				new String[] { "001112|010112|011012|10011|10101|2011102", "002221|020221|022021|20022|20202|1022201" },
				50),
		DEAD_3("����", 6, new String[] { "21112", "12221" }, -5),
		ALIVE_2("���", 7, new String[] { "00110|01010|010010", "00220|02020|020020" }, 5),
		SLEEP_2("�߶�", 8,
				new String[] { "000112|001012|010012|10001|2010102|2011002",
						"000221|002021|020021|20002|1020201|1022001" },
				3),
		DEAD_2("����", 9, new String[] { "2112", "1221" }, -5), NULL("null", 10, new String[] { "", "" }, 0);
		private String name;
		private int index;
		private String[] regex;// ������ʽ
		int score;// ��ֵ

		// ���췽��
		private Level(String name, int index, String[] regex, int score) {
			this.name = name;
			this.index = index;
			this.regex = regex;
			this.score = score;
		}

		// ���Ƿ���
		@Override
		public String toString() {
			return this.name;
		}
	};
	
	
	// ����
	private static enum Direction {
		HENG, SHU, PIE, NA
	};

	// λ�÷�
	private static int[][] position = { 
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0 },
			{ 0, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0 },
			{ 0, 1, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 2, 1, 0 },
			{ 0, 1, 2, 3, 4, 4, 4, 4, 4, 4, 4, 3, 2, 1, 0 },
			{ 0, 1, 2, 3, 4, 5, 5, 5, 5, 5, 4, 3, 2, 1, 0 }, 
			{ 0, 1, 2, 3, 4, 5, 6, 6, 6, 5, 4, 3, 2, 1, 0 },
			{ 0, 1, 2, 3, 4, 5, 6, 7, 6, 5, 4, 3, 2, 1, 0 }, 
			{ 0, 1, 2, 3, 4, 5, 6, 6, 6, 5, 4, 3, 2, 1, 0 },
			{ 0, 1, 2, 3, 4, 5, 5, 5, 5, 5, 4, 3, 2, 1, 0 }, 
			{ 0, 1, 2, 3, 4, 4, 4, 4, 4, 4, 4, 3, 2, 1, 0 },
			{ 0, 1, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 2, 1, 0 }, 
			{ 0, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0 },
			{ 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0 }, 
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } };


	

}
