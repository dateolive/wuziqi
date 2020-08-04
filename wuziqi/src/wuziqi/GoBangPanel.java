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

//左侧棋盘
public class GoBangPanel extends JPanel{
	private int currentPlayer=GoBangContantes.BLACK;
	//true为游戏结束
	private boolean isGameOver=false;
	//记录棋子的步数
	private int count=0;
	//模式true为人人对战 false为人机对战
	private boolean model;
	//true：估值函数 false：估值函数+搜索树
	private boolean intel;
	//搜索深度和节点数量
	private int depth;
	private int nodeCount;
	//true为人类先手，false为机器先手
	private boolean first;
	//
	private JTextArea textArea;
	//显示状态
	private boolean showOrder;
	
	private ChessBean chessBeanBytree;
	
	private int x,y;
	ChessBean[][] chessBeans=new ChessBean[GoBangContantes.LINE_NUMBER][GoBangContantes.LINE_NUMBER];
	//创建棋盘
	public GoBangPanel(){
		//setSize(650,700);
		setPreferredSize(new Dimension(GoBangContantes.PANEL_WIDTH, GoBangContantes.PANEL_HEIGHT));
		setBackground(Color.ORANGE);
		//棋盘绑定鼠标移动事件
		addMouseMotionListener(mouseMotionListener);
		//棋盘绑定鼠标点击事件
		addMouseListener(mouseListener);
		//对棋盘上的棋子初始化
		for(int i=0;i<chessBeans.length;i++){
			for(int j=0;j<chessBeans[i].length;j++)
			{
				
				chessBeans[i][j]=new ChessBean(i,j,0,0);
				//测试 b-w-b-w
				//chessBeans[i][j]=new ChessBean(i,j,currentPlayer,i*chessBeans.length+j);
				//currentPlayer=3-currentPlayer;
			}
		}
		
	}
	//绑定事件
	MouseMotionListener mouseMotionListener=new MouseMotionListener() {
		
		@Override
		public void mouseMoved(MouseEvent arg0) {
			//鼠标移动，获取坐标
			int x_mv=arg0.getX();
			int y_mv=arg0.getY();
			
			//判断越界
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
	
	//绘制棋盘
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2d=(Graphics2D) g;
		g2d.setStroke(new BasicStroke(2));//加粗线
		drawLine(g2d);//画棋盘
		drawStar(g2d);//画天元和星
		drawTrips(g2d);//画提醒框	
		drawNumber(g2d);//画坐标
		drawChess(g2d);//绘制棋子
		drawOrderNumber(g2d);//绘制下棋顺序
	}

	




	//绘制棋盘
	private void drawLine(Graphics2D g2d){
		//横线
		for(int i=0;i<GoBangContantes.LINE_NUMBER;i++){
			g2d.drawLine(GoBangContantes.OFFSET, GoBangContantes.OFFSET+i*GoBangContantes.LINE_SIZE,
					GoBangContantes.OFFSET+(15-1)*GoBangContantes.LINE_SIZE, GoBangContantes.OFFSET+i*GoBangContantes.LINE_SIZE);
		}
		//竖线
		for(int i=0;i<GoBangContantes.LINE_NUMBER;i++){
			g2d.drawLine(GoBangContantes.OFFSET+i*GoBangContantes.LINE_SIZE,GoBangContantes.OFFSET, 
					GoBangContantes.OFFSET+i*GoBangContantes.LINE_SIZE,GoBangContantes.OFFSET+(15-1)*GoBangContantes.LINE_SIZE);
		}
	}
	//画天元和星
	private void drawStar(Graphics2D g2d){
		int center=GoBangContantes.LINE_NUMBER/2;
		int quarter=GoBangContantes.LINE_NUMBER/4;
		//天元
		g2d.fillOval(center*GoBangContantes.LINE_SIZE+GoBangContantes.OFFSET-GoBangContantes.STAR/2,center*GoBangContantes.LINE_SIZE+GoBangContantes.OFFSET-GoBangContantes.STAR/2,
				GoBangContantes.STAR,GoBangContantes.STAR);
		//左上角星星
		g2d.fillOval(quarter*GoBangContantes.LINE_SIZE+GoBangContantes.OFFSET-GoBangContantes.STAR/2,quarter*GoBangContantes.LINE_SIZE+GoBangContantes.OFFSET-GoBangContantes.STAR/2,
				GoBangContantes.STAR,GoBangContantes.STAR);
		//左下角星星
		g2d.fillOval(quarter*GoBangContantes.LINE_SIZE+GoBangContantes.OFFSET-GoBangContantes.STAR/2,(GoBangContantes.LINE_NUMBER-quarter-1)*GoBangContantes.LINE_SIZE+GoBangContantes.OFFSET-GoBangContantes.STAR/2,
				GoBangContantes.STAR,GoBangContantes.STAR);
		//右上角星星
		g2d.fillOval((GoBangContantes.LINE_NUMBER-quarter-1)*GoBangContantes.LINE_SIZE+GoBangContantes.OFFSET-GoBangContantes.STAR/2,quarter*GoBangContantes.LINE_SIZE+GoBangContantes.OFFSET-GoBangContantes.STAR/2,
				GoBangContantes.STAR,GoBangContantes.STAR);
		//右下角星星
		g2d.fillOval((GoBangContantes.LINE_NUMBER-quarter-1)*GoBangContantes.LINE_SIZE+GoBangContantes.OFFSET-GoBangContantes.STAR/2,(GoBangContantes.LINE_NUMBER-quarter-1)*GoBangContantes.LINE_SIZE+GoBangContantes.OFFSET-GoBangContantes.STAR/2,
				GoBangContantes.STAR,GoBangContantes.STAR);
	}
	//绘制提醒框
	private void drawTrips(Graphics2D g2d) {
		g2d.setColor(Color.red);
		//交叉点的坐标
		int x_tmp=GoBangContantes.OFFSET+x*GoBangContantes.LINE_SIZE;
		int y_tmp=GoBangContantes.OFFSET+y*GoBangContantes.LINE_SIZE;
		
		int half=GoBangContantes.LINE_SIZE/2;
		int quarter=GoBangContantes.LINE_SIZE/4;
		//左上角
		g2d.drawLine(x_tmp-half,y_tmp-half, x_tmp-half+quarter,y_tmp-half);
		g2d.drawLine(x_tmp-half,y_tmp-half, x_tmp-half,y_tmp-half+quarter);
		
		//左下角
		g2d.drawLine(x_tmp-half,y_tmp+half, x_tmp-half+quarter,y_tmp+half);
		g2d.drawLine(x_tmp-half,y_tmp+half, x_tmp-half,y_tmp+half-quarter);
		
		//右上角
		g2d.drawLine(x_tmp+half,y_tmp-half, x_tmp+half-quarter,y_tmp-half);
		g2d.drawLine(x_tmp+half,y_tmp-half, x_tmp+half,y_tmp-half+quarter);
				
		//右下角
		g2d.drawLine(x_tmp+half,y_tmp+half, x_tmp+half-quarter,y_tmp+half);
		g2d.drawLine(x_tmp+half,y_tmp+half, x_tmp+half,y_tmp+half-quarter);
				
	}
	//画坐标
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
	
	//绘制棋子
	private void drawChess(Graphics2D g2d) {
		for(int i=0;i<chessBeans.length;i++){
			for(int j=0;j<chessBeans[i].length;j++)
			{
					ChessBean bean=chessBeans[i][j];
					//判空
					if(bean.getPlayer()!=GoBangContantes.EMPTY)
					{
						//黑子
						if(bean.getPlayer()==GoBangContantes.BLACK){
							g2d.setColor(Color.black);
						
						}//白子
						else if(bean.getPlayer()==GoBangContantes.WHITE){
							g2d.setColor(Color.white);
						}
					
					//计算坐标
					int x_tmp=GoBangContantes.OFFSET+bean.getX()*GoBangContantes.LINE_SIZE;
					int y_tmp=GoBangContantes.OFFSET+bean.getY()*GoBangContantes.LINE_SIZE;
					
					int width=GoBangContantes.LINE_SIZE/5*3;
					g2d.fillOval(x_tmp-width/2, y_tmp-width/2, width, width);
					}
					
				}
			}
			if(!showOrder){
				//获取最后一个棋子
				ChessBean bean=getLastBean();
				if(bean!=null){
					//棋子显示，要么是数字，要么最后一个棋子是红框
					g2d.setColor(Color.red);
					int width=GoBangContantes.LINE_SIZE/5;
					
					//计算坐标
					int x_tmp=GoBangContantes.OFFSET+bean.getX()*GoBangContantes.LINE_SIZE;
					int y_tmp=GoBangContantes.OFFSET+bean.getY()*GoBangContantes.LINE_SIZE;
					g2d.fillRect(x_tmp-width/2,y_tmp-width/2, width, width);
				}
				
			}
	
			
		}

		//查询最后一个棋子
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
	
	//绘制下棋数字顺序
	private void drawOrderNumber(Graphics2D g2d) {
		if(showOrder){
			g2d.setColor(Color.red);
			for(int i=0;i<chessBeans.length;i++){
				for(int j=0;j<chessBeans[i].length;j++)
				{
					ChessBean bean=chessBeans[i][j];
					//判空
					if(bean.getPlayer()!=GoBangContantes.EMPTY)
					{
						int number=bean.getOrderNumber();
						FontMetrics fontMetrics=g2d.getFontMetrics();
						int width=fontMetrics.stringWidth(number+"");
						int height=fontMetrics.getAscent();
						
						//计算坐标
						int x_tmp=GoBangContantes.OFFSET+bean.getX()*GoBangContantes.LINE_SIZE;
						int y_tmp=GoBangContantes.OFFSET+bean.getY()*GoBangContantes.LINE_SIZE;
						
						g2d.drawString(number+"", x_tmp-width/2, y_tmp+height/2);
					}
				}
			}
		}

		
	}



	//显示状态判断
	public void showOrder(boolean selected) {
		this.showOrder=selected;
		//重绘棋盘
		repaint();
		
	}



	//新游戏
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
		//防止多次新游戏，第一个是黑子
		currentPlayer=GoBangContantes.BLACK;
		//初始化
		for(int i=0;i<chessBeans.length;i++){
			for(int j=0;j<chessBeans[i].length;j++)
			{
				
				chessBeans[i][j]=new ChessBean(i,j,0,0);
			}
		}
		isGameOver=false;
		count=0;
		JOptionPane.showMessageDialog(GoBangPanel.this, "新游戏开始！");
		
		if(!model&&!first)
		{
			//人机对战，机器先手
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
				JOptionPane.showMessageDialog(GoBangPanel.this, "游戏已经结束！");
				return;
			}
			//获取坐标
			//鼠标移动，获取坐标
			int x_mv=e.getX();
			int y_mv=e.getY();
			
			
			//判断越界
			if(x_mv>=GoBangContantes.OFFSET&&x_mv<=GoBangContantes.OFFSET+(GoBangContantes.LINE_NUMBER-1)*GoBangContantes.LINE_SIZE
					&&y_mv>=GoBangContantes.OFFSET&&y_mv<=GoBangContantes.OFFSET+(GoBangContantes.LINE_NUMBER-1)*GoBangContantes.LINE_SIZE)
			{
				x=(x_mv-GoBangContantes.OFFSET/2)/GoBangContantes.LINE_SIZE;
				y=(y_mv-GoBangContantes.OFFSET/2)/GoBangContantes.LINE_SIZE;
				if(e.getButton()==MouseEvent.BUTTON1){
					//点击鼠标左键
					if(model){
						//人人对战
						if(x>=0&&x<GoBangContantes.LINE_NUMBER&&y>=0&&y<GoBangContantes.LINE_NUMBER)
						{
							//落子
							if(chessBeans[x][y].getPlayer()==GoBangContantes.EMPTY)
							{
								//为空落子
								chessBeans[x][y]=new ChessBean(x,y,currentPlayer,count);
								count++;
								currentPlayer=3-currentPlayer;
								checkWin(chessBeans[x][y]);
								repaint();
							}
						}
					
					}else{
						//人机对战
						if(intel){
							//估值函数
							if(x>=0&&x<GoBangContantes.LINE_NUMBER&&y>=0&&y<GoBangContantes.LINE_NUMBER)
							{
								//落子
								if(chessBeans[x][y].getPlayer()==GoBangContantes.EMPTY)
								{
									//为空落子
									chessBeans[x][y]=new ChessBean(x,y,currentPlayer,count);
									count++;
									currentPlayer=3-currentPlayer;
									boolean win=checkWin(chessBeans[x][y]);
									repaint();
									
									//机器下棋
									if(!win){
										
										List<ChessBean> list=getSortedBean(currentPlayer);
										if(list.size()>0){
											//获取最大的分值
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
							//估值函数+搜索树
							if(x>=0&&x<GoBangContantes.LINE_NUMBER&&y>=0&&y<GoBangContantes.LINE_NUMBER)
							{
								//落子
								if(chessBeans[x][y].getPlayer()==GoBangContantes.EMPTY)
								{
									//为空落子
									chessBeans[x][y]=new ChessBean(x,y,currentPlayer,count);
									count++;
									currentPlayer=3-currentPlayer;
									boolean win=checkWin(chessBeans[x][y]);
									repaint();
									
								
									//机器下棋
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
					//点击鼠标右键
					ChessBean bean=chessBeans[x][y];
					int offense=getValue(bean,currentPlayer);
					int defense=getValue(bean,3-currentPlayer);
					int sum=offense+defense;
					chessBeans[x][y].getBuffer().append("点(" + x + "," + y + ")的" + "攻击:" + offense + " "
							+ "防御:" + defense + " " + "总和:" + (sum) + "\n\n");
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
	//根据搜索树进行查询
	protected ChessBean getByTree(int depth, int currentPlayer,ChessBean[][] chessBeans) {
		//模拟下棋，克隆棋盘
		ChessBean[][] chessBeans2=clone(chessBeans);
		//计算空位置的得分
		List<ChessBean> list=getSortedBean(currentPlayer,chessBeans2);
		if(this.depth==depth){
			//搜索到指定深度
			return list.get(0);
		}
		for(int i=0;i<nodeCount;i++){
			ChessBean chessBean=list.get(i);
			if(chessBean.getSum()>Level.ALIVE_4.score){
				return chessBean;
			}else{
				//模拟下棋，继续递归
				chessBeans2[chessBean.getX()][chessBean.getY()].setPlayer(currentPlayer);
				return getByTree(depth+1,3-currentPlayer , chessBeans2);
			}
		}
		return null;
	}

//极大极小值搜索
	protected int getByTree2(int depth, int currentPlayer,ChessBean[][] chessBeans, int alpha, int beta) {
		//模拟下棋，克隆棋盘
		ChessBean[][] chessBeans2=clone(chessBeans);
		//计算空位置的得分
		List<ChessBean> list=getSortedBean(currentPlayer,chessBeans2);
		if(this.depth==depth){
			//搜索到指定深度
			return list.get(0).getSum();
		}
		for(int i=0;i<nodeCount;i++){
			ChessBean chessBean=list.get(i);
			int score;
			if(chessBean.getSum()>Level.ALIVE_4.score){
				score=chessBean.getSum();
			}else{
				//模拟下棋，继续递归
				chessBeans2[chessBean.getX()][chessBean.getY()].setPlayer(currentPlayer);
				score=getByTree2(depth+1,3-currentPlayer , chessBeans2,alpha,beta);
				
			}
			if (depth % 2 == 0) {
				// 自己，找最大值
				if (score > alpha) {
					alpha = score;
					if (depth == 0) {
						// 结果
						chessBeanBytree=chessBean;
						// System.out.println(chessBeansForTree);
					}
				}
				if (alpha >= beta) {
					// 剪枝
					score = alpha;
					return score;
				}
			} else {
				if (score < beta) {
					beta = score;
				}
				if (alpha >= beta) {
					// 剪枝
					score = beta;
					return score;
				}
			}
		}
		return depth % 2 == 0 ? alpha : beta;
	}

		
	






	private List<ChessBean> getSortedBean(int currentPlayer, ChessBean[][] chessBeans) {
		
		List<ChessBean> list=new ArrayList<ChessBean>();
		//初始化
		for(int i=0;i<chessBeans.length;i++){
			for(int j=0;j<chessBeans[i].length;j++)
			{
				//棋子初始化
				ChessBean bean=chessBeans[i][j];
				if(bean.getPlayer()==GoBangContantes.EMPTY)
				{
					//该位置为空，计算得分
					int offense=getValue(bean,currentPlayer);
					int defense=getValue(bean,3-currentPlayer);
					
					bean.setOffense(offense);
					bean.setDefense(defense);
					bean.setSum(offense+defense);
					list.add(bean);
				}
			}
		}
		//根据总得分进行大小排序
		Collections.sort(list);
		return list;
	}


	//数组克隆
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
	//判断是否输赢
	private boolean checkWin(ChessBean chessBean) {
		boolean result=false;
		//判断四个方面棋子的状态
		//横
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
			//游戏结束
			JOptionPane.showMessageDialog(GoBangPanel.this, "游戏结束");
			isGameOver=true;
		}
		return result;
	}






	protected List<ChessBean> getSortedBean(int currentPlayer) {
		List<ChessBean> list=new ArrayList<ChessBean>();
		//初始化
		for(int i=0;i<chessBeans.length;i++){
			for(int j=0;j<chessBeans[i].length;j++)
			{
				//棋子初始化
				ChessBean bean=chessBeans[i][j];
				if(bean.getPlayer()==GoBangContantes.EMPTY)
				{
					//该位置为空，计算得分
					int offense=getValue(bean,currentPlayer);
					int defense=getValue(bean,3-currentPlayer);
					
					bean.setOffense(offense);
					bean.setDefense(defense);
					bean.setSum(offense+defense);
					list.add(bean);
				}
			}
		}
		//根据总得分进行大小排序
		Collections.sort(list);
		return list;
	}





	//对四个方向进行计分
	private int getValue(ChessBean bean, int currentPlayer) {
		//计算4个方向的棋形level
		Level level1=getLevel(bean,currentPlayer,Direction.HENG);
		Level level2=getLevel(bean,currentPlayer,Direction.SHU);
		Level level3=getLevel(bean,currentPlayer,Direction.PIE);
		Level level4=getLevel(bean,currentPlayer,Direction.NA);
		return getValueByLevel(level1,level2,level3,level4)+position[bean.getX()][bean.getY()];
	}




	//检测4个方向的棋形是否重复
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
				|| levelCount[Level.GO_4.index] >= 1 && levelCount[Level.ALIVE_3.index] >= 1)// 双活4，冲4活三
			score = 10000;
		else if (levelCount[Level.ALIVE_3.index] >= 2)// 双活3
			score = 5000;
		else if (levelCount[Level.SLEEP_3.index] >= 1 && levelCount[Level.ALIVE_3.index] >= 1)// 活3眠3
			score = 1000;
		else if (levelCount[Level.ALIVE_2.index] >= 2)// 双活2
			score = 100;
		else if (levelCount[Level.SLEEP_2.index] >= 1 && levelCount[Level.ALIVE_2.index] >= 1)// 活2眠2
			score = 10;
		score = Math.max(score, Math.max(Math.max(level1.score, level2.score), Math.max(level3.score, level4.score)));
		return score;
	}






	//计算某个方向的棋形
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
		
		//正方向
		String str=left+currentPlayer+right;
		
		//chessBeans[bean.getX()][bean.getY()].getBuffer().append("(" + (bean.getX() + 1) + "," + (bean.getY()- 1) + ")" + dire + "\t" + str + "\t");
		//反向
		String strres=new StringBuffer(str).reverse().toString();
		
		for(Level level:Level.values()){
			//根据正则表达式进行比较
			Pattern pattern=Pattern.compile(level.regex[currentPlayer-1]);
			
			Matcher matcher=pattern.matcher(str);
			//为true为1
			boolean b1=matcher.find();
			
			Matcher matcher2=pattern.matcher(strres);
			//为true为1
			boolean b2=matcher2.find();
			
			if(b1||b2){
				//匹配成功
				return level;
			}
		}
		
		return Level.NULL;
	}






	private String getStringByDire(ChessBean bean, int currentPlayer2, int x,int y) {
		boolean res=false;
		if(y>0||(y==0&&x<0))
		{
			//反向拼接
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
					//反向拼接
					str=chessBeans[x_tmp][y_tmp].getPlayer()+str;
				}else{
					str+=chessBeans[x_tmp][y_tmp].getPlayer();
				}
			}
		}
		
		return str;
	}






	private int checkChessCount(ChessBean chessBean, int x, int y) {
		//判断某个方向4个棋子
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
			JOptionPane.showMessageDialog(GoBangPanel.this, "请先开始游戏");
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
				JOptionPane.showMessageDialog(GoBangPanel.this, "请先下棋");
			}
		}
		
	}
	
	public void huiqi2() { 
		if (isGameOver) {
			JOptionPane.showMessageDialog(GoBangPanel.this, "请先开始新游戏！");
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
				JOptionPane.showMessageDialog(GoBangPanel.this, "你还没下棋呢！");
			}
		}
	}

	// 棋型信息
	public static enum Level {
		CON_5("长连", 0, new String[] { "11111", "22222" }, 100000),
		ALIVE_4("活四", 1, new String[] { "011110", "022220" }, 10000),
		GO_4("冲四", 2, new String[] { "011112|0101110|0110110", "022221|0202220|0220220" }, 500),
		DEAD_4("死四", 3, new String[] { "211112", "122221" }, -5),
		ALIVE_3("活三", 4, new String[] { "01110|010110", "02220|020220" }, 200),
		SLEEP_3("眠三", 5,
				new String[] { "001112|010112|011012|10011|10101|2011102", "002221|020221|022021|20022|20202|1022201" },
				50),
		DEAD_3("死三", 6, new String[] { "21112", "12221" }, -5),
		ALIVE_2("活二", 7, new String[] { "00110|01010|010010", "00220|02020|020020" }, 5),
		SLEEP_2("眠二", 8,
				new String[] { "000112|001012|010012|10001|2010102|2011002",
						"000221|002021|020021|20002|1020201|1022001" },
				3),
		DEAD_2("死二", 9, new String[] { "2112", "1221" }, -5), NULL("null", 10, new String[] { "", "" }, 0);
		private String name;
		private int index;
		private String[] regex;// 正则表达式
		int score;// 分值

		// 构造方法
		private Level(String name, int index, String[] regex, int score) {
			this.name = name;
			this.index = index;
			this.regex = regex;
			this.score = score;
		}

		// 覆盖方法
		@Override
		public String toString() {
			return this.name;
		}
	};
	
	
	// 方向
	private static enum Direction {
		HENG, SHU, PIE, NA
	};

	// 位置分
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
