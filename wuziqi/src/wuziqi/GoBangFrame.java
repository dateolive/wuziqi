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

//��Ϸ������
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
	//������Ϸ����
	public void start(){
		
		panel=new GoBangPanel();
		add(panel,BorderLayout.WEST);
		
		
		JPanel rightPanel=new JPanel();
		//���ô�ֱ����
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		
		//�����ı���
		panel1=new JPanel();
		panel1.setBorder(new TitledBorder("�ڼ����ϵ�������Ҽ����鿴������ֵ"));
		panel1.setLayout(new BorderLayout());
		textArea=new JTextArea();
		textArea.setEditable(false);
		panel1.add(new JScrollPane(textArea));
		rightPanel.add(panel1);
		
		
		
		//ģʽ
		panel2=new JPanel();
		panel2.setBorder(new TitledBorder("ģʽ"));
		//��ѡ��
		model=new JRadioButton("���˶�ս");
		model.setSelected(true);
		robot=new JRadioButton("�˻���ս");
		//��ѡ�򻥳�
		buttonGroup1=new ButtonGroup();
		buttonGroup1.add(model);
		buttonGroup1.add(robot);
		panel2.add(model);
		panel2.add(robot);
		rightPanel.add(panel2);
		
		
		
		//����
		panel3=new JPanel();
		panel3.setBorder(new TitledBorder("����"));
		//��ѡ��
		intel=new JRadioButton("��ֵ����");
		intel.setSelected(true);
		radioButton4=new JRadioButton("��ֵ����+������");
		//��ѡ�򻥳�
		buttonGroup2=new ButtonGroup();
		buttonGroup2.add(intel);
		buttonGroup2.add(radioButton4);
		panel3.add(intel);
		panel3.add(radioButton4);
		rightPanel.add(panel3);
		
		//������
		panel4=new JPanel();
		panel4.setBorder(new TitledBorder("������"));
		label1=new JLabel("�������");
		depth=new JComboBox<>(new Integer[] {1,2,3});
		label2=new JLabel("ÿ��ڵ�");
		nodeCount=new JComboBox<>(new Integer[] {1,2,3});
		
		panel4.add(label1);
		panel4.add(depth);
		panel4.add(label2);
		panel4.add(nodeCount);
		rightPanel.add(panel4);
		
		//����
		panel5=new JPanel();
		panel5.setBorder(new TitledBorder("����"));
		showorder=new JCheckBox("��ʾ˳��");
		nudo=new JButton("����");
		newGame=new JButton("����Ϸ");
		showorder.addMouseListener(mouseListener);
		nudo.addMouseListener(mouseListener);
		newGame.addMouseListener(mouseListener);
		panel5.add(showorder);
		panel5.add(nudo);
		panel5.add(newGame);
		rightPanel.add(panel5);
		
		
		//�˻�ģʽ
		panel6=new JPanel();
		panel6.setBorder(new TitledBorder("�˻�ģʽ"));
		//��ѡ��
		first=new JRadioButton("��������");
		first.setSelected(true);
		radioButton6=new JRadioButton("��������");
		//��ѡ�򻥳�
		buttonGroup3=new ButtonGroup();
		buttonGroup3.add(first);
		buttonGroup3.add(radioButton6);
		panel6.add(first);
		panel6.add(radioButton6);
		rightPanel.add(panel6);
		
		
		
		add(rightPanel);
		
		//��Ϸ��������
		setSize(GoBangContantes.GAME_WIDTH,GoBangContantes.GAME_HEIGHT);
		setLocation(200, 200);
		setTitle("������");	
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	//������
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
				//��ʾ״̬����panel�����ж�
				panel.showOrder(showorder.isSelected());
			}else if(object==newGame){
				panel.newGame(model.isSelected()?true:false,intel.isSelected()?true:false,
						(int)depth.getSelectedItem(),(int)nodeCount.getSelectedItem(),
						first.isSelected()?true:false,showorder.isSelected(),textArea);
				
			}
			
		}
	};
	
	
	
	
}
