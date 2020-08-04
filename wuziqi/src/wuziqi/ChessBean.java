package wuziqi;
//����
public class ChessBean implements Comparable<ChessBean>{
	
	
	//���ӵ����꣨0��0��-��14��14��
	private int X;
	private int Y;
	
	//���������ĸ����  0:�գ�û�������ӣ� 1������  2������
	private int player;
	//����˳��
	private int orderNumber;
	
	private int offense;
	private int defense;
	private int sum;
	
	private StringBuffer buffer=new StringBuffer();
	
	public ChessBean(){
		
	}
	//����ǿղ�������������Ҫ�ȶ���һ���޲εĹ�����
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
