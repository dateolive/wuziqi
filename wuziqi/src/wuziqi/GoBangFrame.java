package wuziqi;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

//游戏窗口类
public class GoBangFrame extends JFrame{
	
	private GoBangPanel panel;
	private JCheckBox showorder;
	private JButton nudo;
	private JButton newGame;
	private JLabel label1,label2;
	private JPanel panel1,panel2,panel3,panel4,panel5,panel6;
	private JTextArea textArea;
	private JRadioButton model,robot,intel,radioButton4,first,radioButton6;
	private ButtonGroup buttonGroup1,buttonGroup2,buttonGroup3;
	private JComboBox<Integer> depth,nodeCount;
	//启动游戏窗口
	public void start(){
		
		panel=new GoBangPanel();
		add(panel,BorderLayout.WEST);
		
		
		JPanel rightPanel=new JPanel();
		//设置垂直布局
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		
		//多行文本框
		panel1=new JPanel();
		panel1.setBorder(new TitledBorder("在键盘上单击鼠标右键，查看各个估值"));
		panel1.setLayout(new BorderLayout());
		textArea=new JTextArea();
		textArea.setEditable(false);
		panel1.add(new JScrollPane(textArea));
		rightPanel.add(panel1);
		
		
		
		//模式
		panel2=new JPanel();
		panel2.setBorder(new TitledBorder("模式"));
		//单选框
		model=new JRadioButton("人人对战");
		model.setSelected(true);
		robot=new JRadioButton("人机对战");
		//单选框互斥
		buttonGroup1=new ButtonGroup();
		buttonGroup1.add(model);
		buttonGroup1.add(robot);
		panel2.add(model);
		panel2.add(robot);
		rightPanel.add(panel2);
		
		
		
		//智能
		panel3=new JPanel();
		panel3.setBorder(new TitledBorder("智能"));
		//单选框
		intel=new JRadioButton("估值函数");
		intel.setSelected(true);
		radioButton4=new JRadioButton("估值函数+搜索树");
		//单选框互斥
		buttonGroup2=new ButtonGroup();
		buttonGroup2.add(intel);
		buttonGroup2.add(radioButton4);
		panel3.add(intel);
		panel3.add(radioButton4);
		rightPanel.add(panel3);
		
		//搜索树
		panel4=new JPanel();
		panel4.setBorder(new TitledBorder("搜索树"));
		label1=new JLabel("搜索深度");
		depth=new JComboBox<>(new Integer[] {1,2,3});
		label2=new JLabel("每层节点");
		nodeCount=new JComboBox<>(new Integer[] {1,2,3});
		
		panel4.add(label1);
		panel4.add(depth);
		panel4.add(label2);
		panel4.add(nodeCount);
		rightPanel.add(panel4);
		
		//其他
		panel5=new JPanel();
		panel5.setBorder(new TitledBorder("其他"));
		showorder=new JCheckBox("显示顺序");
		nudo=new JButton("悔棋");
		newGame=new JButton("新游戏");
		showorder.addMouseListener(mouseListener);
		nudo.addMouseListener(mouseListener);
		newGame.addMouseListener(mouseListener);
		panel5.add(showorder);
		panel5.add(nudo);
		panel5.add(newGame);
		rightPanel.add(panel5);
		
		
		//人机模式
		panel6=new JPanel();
		panel6.setBorder(new TitledBorder("人机模式"));
		//单选框
		first=new JRadioButton("人类先手");
		first.setSelected(true);
		radioButton6=new JRadioButton("机器先手");
		//单选框互斥
		buttonGroup3=new ButtonGroup();
		buttonGroup3.add(first);
		buttonGroup3.add(radioButton6);
		panel6.add(first);
		panel6.add(radioButton6);
		rightPanel.add(panel6);
		
		
		
		add(rightPanel);
		
		//游戏窗口设置
		setSize(GoBangContantes.GAME_WIDTH,GoBangContantes.GAME_HEIGHT);
		setLocation(200, 200);
		setTitle("五子棋");	
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	//监听器
	private MouseListener mouseListener=new MouseListener() {
		
		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {
			Object object=e.getSource();
			if(object==nudo){
				if(model.isSelected())
				panel.huiqi();
				else if(robot.isSelected())
				panel.huiqi2();
			}else if(object==showorder){
				//显示状态传到panel进行判断
				panel.showOrder(showorder.isSelected());
			}else if(object==newGame){
				panel.newGame(model.isSelected()?true:false,intel.isSelected()?true:false,
						(int)depth.getSelectedItem(),(int)nodeCount.getSelectedItem(),
						first.isSelected()?true:false,showorder.isSelected(),textArea);
				
			}
			
		}
	};
	
	
	
	
}
