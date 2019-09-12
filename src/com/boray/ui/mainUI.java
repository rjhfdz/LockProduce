package com.boray.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.comm.CommPortIdentifier;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.basic.BasicTableHeaderUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.boray.listener.MenuItemListener;
import com.boray.listener.OpenLockComActionListener;
import com.boray.listener.OpenScanGunComActionListener;
import com.boray.listener.PopupListener;

public class mainUI {
	public static Map map;
	private String[] s2;
	public static void main(String[] args) {
		new mainUI().show();
	}
	void show(){
		try {
	         //JDialog.setDefaultLookAndFeelDecorated(true); 
	         UIManager.setLookAndFeel(ch.randelshofer.quaqua.QuaquaManager.getLookAndFeel());
	         UIManager.put("FileChooser.cancelButtonText", "取消");
	         UIManager.put("FileChooser.saveButtonText", "保存");
	         UIManager.put("FileChooser.openButtonText", "确定");
	         UIManager.put("FileChooser.newFolderButtonText", "新建文件夹");
	         UIManager.put("OptionPane.yesButtonText", "是");
	         UIManager.put("OptionPane.noButtonText", "否"); 
			} 
		catch (Exception e) {
			e.printStackTrace();
		}
		map = new HashMap();
		final JFrame frame = new JFrame("智能锁生产录入V1.0");
		map.put("frame", frame);
		
		int screenWidth=((int)java.awt.Toolkit.getDefaultToolkit().getScreenSize().width);
		int screenHeight = ((int)java.awt.Toolkit.getDefaultToolkit().getScreenSize().height);
		int frameWidth = 640;
		int frameHeight = 454;
		frame.setSize(frameWidth, frameHeight);
		frame.setLocation(screenWidth/2-frameWidth/2, screenHeight/2-frameHeight/2);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		JPopupMenu.setDefaultLightWeightPopupEnabled(false);
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu(" 文件 ");
		JMenuItem item1 = new JMenuItem("新建工程");
		JMenuItem item2 = new JMenuItem("打开工程");
		//JMenuItem item3 = new JMenuItem("保存工程");
		//JMenuItem item4 = new JMenuItem("关闭工程");
		JMenuItem item5 = new JMenuItem("查看");
		MenuItemListener listener = new MenuItemListener();
		item1.addActionListener(listener);item2.addActionListener(listener);
		//item3.addActionListener(listener);//item4.addActionListener(listener);
		item5.addActionListener(listener);
		menu.add(item1);menu.add(item2);
		//menu.add(item3);//menu.add(item4);
		menu.add(item5);
		menuBar.add(menu);
		frame.setJMenuBar(menuBar);
		
		init(frame);
		frame.setIconImage(new ImageIcon(getClass().getResource("/icon/boray.png")).getImage());
		frame.setVisible(true);
	}
	private void init(JFrame frame) {
		Color color = new Color(196, 196, 196);
		JPanel p1 = new JPanel();
		FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
		flowLayout.setVgap(-6);
		//p1.setBorder(new LineBorder(Color.black));
		p1.setLayout(flowLayout);
		p1.setPreferredSize(new Dimension(630,50));
		
		JPanel p1_1 = new JPanel();
		p1_1.setLayout(flowLayout);
		p1_1.setPreferredSize(new Dimension(300,56));
		TitledBorder tb = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "智能锁", TitledBorder.LEFT, TitledBorder.TOP,new Font(Font.SERIF, Font.BOLD, 12));
		p1_1.setBorder(tb);
		p1_1.add(new JLabel("串口:"));
		JComboBox box = new JComboBox();
		box.setPreferredSize(new Dimension(120,32));
		p1_1.add(box);
		CommPortIdentifier cpid;
		Enumeration enumeration = CommPortIdentifier.getPortIdentifiers();
		List list = new ArrayList();
		while (enumeration.hasMoreElements()) {
			cpid = (CommPortIdentifier) enumeration.nextElement();
			if (cpid.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				//box.addItem(cpid.getName());
				list.add(cpid.getName());
			}
		}
		for (int i = 0; i < list.size(); i++) {
			box.addItem(((String)(list.get(i))));
		}
		JButton btn = new JButton("打开串口");
		btn.addActionListener(new OpenLockComActionListener(box));
		p1_1.add(btn);
		
		JPanel p1_2 = new JPanel();
		p1_2.setLayout(flowLayout);
		p1_2.setPreferredSize(new Dimension(300,56));
		TitledBorder tb2 = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "扫码枪", TitledBorder.LEFT, TitledBorder.TOP,new Font(Font.SERIF, Font.BOLD, 12));
		p1_2.setBorder(tb2);
		p1_2.add(new JLabel("串口:"));
		JComboBox box2 = new JComboBox();
		box2.setPreferredSize(new Dimension(120,32));
		p1_2.add(box2);
		for (int i = 0; i < list.size(); i++) {
			box2.addItem(((String)(list.get(i))));
		}
		JButton btn2 = new JButton("打开串口");
		btn2.addActionListener(new OpenScanGunComActionListener(box2));
		p1_2.add(btn2);
		p1.add(p1_1);p1.add(p1_2);

		JPanel p2 = new JPanel();
		p2.setPreferredSize(new Dimension(700,38));
		p2.setLayout(new FlowLayout(FlowLayout.CENTER));
		p2.add(new JLabel("品牌："));
		JComboBox box1 = new JComboBox(new String[]{"0（保仕盾）","1（优果）"});
		p2.add(box1);
		map.put("BrandBox",box1);
		p2.add(new JLabel("型号："));
		final JComboBox box3 = new JComboBox(new String[]{"（Z1）","（Z1S）","（Z1PRO）","（Z1W）"});
		map.put("TypeBox", box3);
		p2.add(box3);

		box1.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if("0（保仕盾）".equals(e.getItem().toString())){
					box3.removeAllItems();
					box3.addItem("（Z1）");
					box3.addItem("（Z1S）");
					box3.addItem("（Z1PRO）");
					box3.addItem("（Z1W）");
				}else if("1（优果）".equals(e.getItem().toString())){
					box3.removeAllItems();
					box3.addItem("（H1）");
					box3.addItem("（H1S）");
					box3.addItem("（H1PRO）");
					box3.addItem("（H1W）");
				}
			}
		});

		JPanel seizePane = new JPanel();
		seizePane.setPreferredSize(new Dimension(120,32));
		p2.add(seizePane);
		p2.add(new JLabel("已录入："));
		JLabel label = new JLabel("0",JLabel.CENTER);
		map.put("recordLabel", label);
		label.setBackground(color);
		label.setOpaque(true);
		label.setPreferredSize(new Dimension(68,26));
		p2.add(label);
		p2.add(new JLabel("条"));
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setPreferredSize(new Dimension(630,206));
		addListTable(scrollPane);
		
		JPanel p4 = new JPanel();
		//p4.setBorder(new LineBorder(Color.black));
		p4.setPreferredSize(new Dimension(600,48));
		FlowLayout flowLayout2 = new FlowLayout(FlowLayout.CENTER);
		flowLayout2.setVgap(12);
		p4.setLayout(flowLayout2);
		p4.add(new JLabel("ＩＤ："));
		JLabel label2 = new JLabel("",JLabel.CENTER);
		label2.setText("");
		map.put("lockLabel", label2);
		label2.setPreferredSize(new Dimension(200,26));
		label2.setBackground(color);
		label2.setOpaque(true);
		p4.add(label2);
		
		p4.add(new JLabel("                    ＳＮ："));
		JLabel label3 = new JLabel("",JLabel.CENTER);
		map.put("scanGunLabel", label3);
		label3.setPreferredSize(new Dimension(210,26));
		label3.setBackground(color);
		label3.setOpaque(true);
		p4.add(label3);
		
		frame.add(p1);
		frame.add(p2);
		frame.add(scrollPane);
		frame.add(p4);
	}
	private void addListTable(JScrollPane scrollPane){
		Object[][] data = {};
		String[] s = {"锁的序列号（ＩＤ）","条形码（ＳＮ）"};
		DefaultTableModel model = new DefaultTableModel(data,s);
		final NewJTable table = new NewJTable(model,1);
		map.put("Table", table);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		/////////////////////////
		DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer(){
		public Component getTableCellRendererComponent(JTable table,  
                Object value, boolean isSelected, boolean hasFocus,  
                int row, int column) { 
				Component cell = super.getTableCellRendererComponent(table, value,
	                    isSelected, hasFocus, row, column);
				cell.setBackground(Color.white);
				cell.setForeground(Color.black);
				
	            return cell;
        	}
		};
		cellRenderer.setHorizontalAlignment(JLabel.CENTER);
		for (int i = 0; i < s.length; i++) {
			table.getColumn(table.getColumnName(i)).setCellRenderer(cellRenderer);
		}
		table.setSelectionBackground(new Color(56,117,215));
		/////////////////////////
		table.getTableHeader().setUI(new BasicTableHeaderUI());
		table.getTableHeader().setReorderingAllowed(false);
		table.setOpaque(false);
		//table.setFocusable(false);
		table.setFont(new Font(Font.SERIF, Font.BOLD, 14));
		//table.getColumnModel().getColumn(0).setPreferredWidth(30);
		//table.getColumnModel().getColumn(1).setPreferredWidth(102);
        table.setRowHeight(36);
        final JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem menuItem = new JMenuItem("　删除　　　　　");
		final PopupListener listener = new PopupListener();
		menuItem.addActionListener(listener);
		popupMenu.add(menuItem);
        table.addMouseListener(new MouseAdapter() {
        	public void mousePressed(MouseEvent e) {
        		if (e.getButton() == 3) {
        			int s = table.rowAtPoint(new Point(e.getX(), e.getY()));
        			listener.setSelected(s);
        			popupMenu.show(table, e.getX(), e.getY());
				}
        	}
		});
        //table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); 
        scrollPane.setViewportView(table);
	}
}
