package wuziqi;
//棋子
public class ChessBean implements Comparable<ChessBean>{
	
	
	//棋子的坐标（0，0）-（14，14）
	private int X;
	private int Y;
	
	//棋子属于哪个玩家  0:空（没有下棋子） 1：黑子  2：白子
	private int player;
	//落子顺序
	private int orderNumber;
	
	private int offense;
	private int defense;
	private int sum;
	
	private StringBuffer buffer=new StringBuffer();
	
	public ChessBean(){
		
	}
	//定义非空参数构造器，需要先定义一个无参的构造器
	public ChessBean(int x,int y,int player,int orderNumber){
		this.X=x;
		this.Y=y;
		this.player=player;
		this.orderNumber=orderNumber;
	}
	public int getX() {
		return X;
	}
	public void setX(int x) {
		X = x;
	}
	public int getY() {
		return Y;
	}
	public void setY(int y) {
		Y = y;
	}
	public int getPlayer() {
		return player;
	}
	public void setPlayer(int player) {
		this.player = player;
	}
	public int getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}
	public int getOffense() {
		return offense;
	}
	public void setOffense(int offense) {
		this.offense = offense;
	}
	public int getDefense() {
		return defense;
	}
	public void setDefense(int defense) {
		this.defense = defense;
	}
	public int getSum() {
		return sum;
	}
	public void setSum(int sum) {
		this.sum = sum;
	}
	public StringBuffer getBuffer() {
		return buffer;
	}
	public void setBuffer(StringBuffer buffer) {
		this.buffer = buffer;
	}
	@Override
	public int compareTo(ChessBean o) {
		if(this.getSum()>o.getSum()){
			return -1;
		}else {
			if(this.getSum()<o.getSum())
				return 1;
		}
		return 0;
	}
	
}
