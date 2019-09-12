package com.boray.listener;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.boray.Data.Data;
import com.boray.ui.NewJTable;
import com.boray.ui.mainUI;

public class MenuItemListener implements ActionListener{
	public void actionPerformed(ActionEvent e) {
		String  s = e.getActionCommand();
		if ("新建工程".equals(s)) {
			newProject();
		} else if ("打开工程".equals(s)) {
			openProject();
		} else if ("保存工程".equals(s)) {
			System.out.println(s);
		} else if ("查看".equals(s)) {
			try {
				if (Data.projectFile != null) {
					String path = Data.projectFile.getParent();
					String fileName = Data.projectFile.getName();
					Runtime.getRuntime().exec("cmd /c cd /d "+path+"&start "+fileName);
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		} else if ("关闭工程".equals(s)) {
			Data.projectFile = null;
			Data.lockIdTreeSet.clear();
			NewJTable table = (NewJTable)mainUI.map.get("Table");
			DefaultTableModel model = (DefaultTableModel)table.getModel();
			for (int i = table.getRowCount()-1; i >= 0; i--) {
				model.removeRow(i);
			}
			JLabel recordLabel = (JLabel)mainUI.map.get("recordLabel");
			recordLabel.setText("0");
		}
	}
	public void newProject(){
		if (Data.projectFile != null) {
			Object[] options = { "否", "是" };
			int yes = JOptionPane.showOptionDialog((JFrame)mainUI.map.get("frame"), "已经打开着一个工程，是否继续！", "提示警告",
			JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
			null, options, options[1]);
			if (yes == 1) {
				Data.projectFile = null;
				Data.lockIdTreeSet.clear();
				NewJTable table = (NewJTable)mainUI.map.get("Table");
				DefaultTableModel model = (DefaultTableModel)table.getModel();
				for (int i = table.getRowCount()-1; i >= 0; i--) {
					model.removeRow(i);
				}
				JLabel recordLabel = (JLabel)mainUI.map.get("recordLabel");
				recordLabel.setText("0");
				
				newProjectDialog();
			} else {
				
			}
		} else {
			newProjectDialog();
		}
	}
	private void openProject(){
		if (Data.projectFile == null) {
			openPrj();
		} else {
			Object[] options = { "否", "是" };
			int yes = JOptionPane.showOptionDialog((JFrame)mainUI.map.get("frame"), "已经存在相同文件名，将覆盖之前的数据，是否要继续？", "警告",
			JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
			null, options, options[1]);
			if (yes == 1) {
				openPrj();
			} else {
				
			}
		}
	}
	private void openPrj(){
		JFileChooser fileChooser = new JFileChooser();
		if (!"".equals(Data.openProjectPath) && Data.openProjectPath != null) {
			fileChooser.setCurrentDirectory(new File(Data.openProjectPath));
		} else {
			String path = "";
			if (!"".equals(Data.newProjectPath) && Data.newProjectPath != null) {
				path = Data.newProjectPath;
			} else {
				path = getClass().getResource("/").getPath();
				try {
					path = URLDecoder.decode(path, "UTF-8");
				} catch (UnsupportedEncodingException e2) {
					e2.printStackTrace();
				}
				path = path.substring(1,path.length()-1);
			}
			fileChooser.setCurrentDirectory(new File(path));
		}
		String[] houZhui = {"xls",};
		FileNameExtensionFilter filter = new FileNameExtensionFilter("*.xls", houZhui);
		fileChooser.setFileFilter(filter);
		int returnVal = fileChooser.showOpenDialog((JFrame)mainUI.map.get("frame"));
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			File projectFile = fileChooser.getSelectedFile();
			Data.projectFile = projectFile;
			try {
				FileInputStream fs=new FileInputStream(projectFile);
		        POIFSFileSystem ps=new POIFSFileSystem(fs);
		        HSSFWorkbook wb = new HSSFWorkbook(ps);
				HSSFSheet sheet = wb.getSheetAt(0);
				int rows = sheet.getLastRowNum() + 1;
				String[][] tp = null;
				if (rows > 6) {
					tp = new String[5][2];
					for (int i = 5; i >= 1; i--) {
						HSSFRow row = sheet.getRow(rows-i);
						tp[5-i][0] = row.getCell(3).getStringCellValue();
						tp[5-i][1] = row.getCell(2).getStringCellValue();
					}
				} else {
					tp = new String[rows-1][2];
					for (int i = 1; i < rows; i++) {
						HSSFRow row = sheet.getRow(i);
						tp[i-1][0] = row.getCell(3).getStringCellValue();
						tp[i-1][1] = row.getCell(2).getStringCellValue();
					}
				}
				Data.lockIdTreeSet.clear();
				Data.SnTreeSet.clear();
				for (int i = 1; i < rows; i++) {
					HSSFRow row = sheet.getRow(i);
					HSSFCell cell = row.getCell(3);
					HSSFCell cell2 = row.getCell(2);
					Data.lockIdTreeSet.add(cell.getStringCellValue());
					Data.SnTreeSet.add(cell2.getStringCellValue());
				}
				JLabel recordLabel = (JLabel)mainUI.map.get("recordLabel");
				recordLabel.setText(Data.lockIdTreeSet.size()+"");
				NewJTable table = (NewJTable)mainUI.map.get("Table");
				DefaultTableModel model = (DefaultTableModel)table.getModel();
				for (int i = table.getRowCount()-1; i >= 0; i--) {
					model.removeRow(i);
				}
				for (int i = 0; i < tp.length; i++) {
					model.addRow(tp[i]);
				}
				wb.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	private void newProjectDialog(){
		JFrame f = (JFrame)mainUI.map.get("frame");
		final JDialog dialog = new JDialog(f,true);
		dialog.setResizable(false);
		dialog.setTitle("新建工程");
		int w = 520,h = 180;
		dialog.setSize(w, h);
		dialog.setLocation(f.getLocation().x+f.getSize().width/2-w/2,f.getLocation().y+f.getSize().height/2-h/2);
		dialog.setLayout(new FlowLayout(FlowLayout.CENTER));
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		JPanel p1 = new JPanel();
		p1.setPreferredSize(new Dimension(480,38));
		p1.setLayout(new FlowLayout(FlowLayout.LEFT));
		JPanel p2 = new JPanel();
		//p2.setBorder(new LineBorder(Color.gray));
		p2.setPreferredSize(new Dimension(480,38));
		p2.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		JLabel projectLabel = new JLabel("工程名:");
		final JTextField projectField = new JTextField(30);
		Date date = new Date(System.currentTimeMillis());
		//String name = (date.getYear()+1900)+"_"+(date.getMonth()+1)+"_"+date.getDate()+"_"+date.getHours();
		String name = (date.getYear()+1900)+""+(date.getMonth()+1)+""+date.getDate()+""+date.getHours()+""+date.getMinutes();
		projectField.setText("智能锁录入"+name);
		p1.add(projectLabel);p1.add(projectField);
		JLabel pathJLabel = new JLabel("路　径:");
		final JTextField pathField = new JTextField(30);
		pathField.setEditable(false);
		if (!"".equals(Data.newProjectPath) && Data.newProjectPath != null) {
			pathField.setText(Data.newProjectPath);
		} else {
//			String path = getClass().getResource("/").getPath();
			String path = new File("").getAbsolutePath();
			try {
				path = URLDecoder.decode(path, "UTF-8");
			} catch (UnsupportedEncodingException e2) {
				e2.printStackTrace();
			}
			pathField.setText(path);
		}
		JButton btn = new JButton("选择...");
		btn.setPreferredSize(new Dimension(68,30));
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				try {
					fileChooser.setSelectedFile(new File(pathField.getText()));
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				int returnVal = fileChooser.showOpenDialog(dialog);
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					pathField.setText(fileChooser.getSelectedFile().toString());
				}
			}
		});
		p2.add(pathJLabel);p2.add(pathField);p2.add(btn);
		ActionListener actionListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ("完成".equals(e.getActionCommand())) {
					Data.newProjectPath = pathField.getText();
					String name = projectField.getText();
					if (!name.matches("[^\\s\\\\/:\\*\\?\\\"<>\\|](\\x20|[^\\s\\\\/:\\*\\?\\\"<>\\|])*[^\\s\\\\/:\\*\\?\\\"<>\\|\\.]$")) {
						JFrame frame = (JFrame)mainUI.map.get("frame");
						JOptionPane.showMessageDialog(frame, "工程名含有非法字符!", "提示", JOptionPane.ERROR_MESSAGE);
						return;
					}
					Data.newProjectName = name;
					try {
						File file = new File(Data.newProjectPath+"/"+name+".xls");
						newProject(file);
						dialog.dispose();
					} catch (Exception e2) {
						JFrame frame = (JFrame)mainUI.map.get("frame");
						JOptionPane.showMessageDialog(frame, e2.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
						return;
					}
				} else {
					dialog.dispose();
				}
			}
		};
		JButton sure = new JButton("完成");
		sure.addActionListener(actionListener);
		sure.setPreferredSize(new Dimension(63,28));
		JButton cancel = new JButton("取消");
		cancel.setPreferredSize(new Dimension(63,28));
		cancel.addActionListener(actionListener);
		dialog.add(p1);dialog.add(p2);
		dialog.add(sure);dialog.add(cancel);
		dialog.setVisible(true);
	}
	public void newProject(File file) throws Exception{
		if (file.exists()) {
			Object[] options = { "否", "是" };
			int yes = JOptionPane.showOptionDialog((JFrame)mainUI.map.get("frame"), "已经存在相同文件名，将覆盖之前的数据，是否要继续？", "警告",
			JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
			null, options, options[1]);
			if (yes == 1) {
				file.delete();
				//file.createNewFile();
				createXLS(file);
			} else {
				Data.newProjectName = "";
				file = null;
			}
		} else {
			createXLS(file);
			//file.createNewFile();
		}
		Data.projectFile = file;
		/*HSSFWorkbook wb;
		if (file.exists()) {
			FileInputStream fs=new FileInputStream(file);
	        POIFSFileSystem ps=new POIFSFileSystem(fs);
			wb = new HSSFWorkbook(ps);
			HSSFSheet sheet = wb.getSheetAt(0);
			int rows = sheet.getLastRowNum();
			HSSFRow row = sheet.createRow((short)(rows+1));
			row.createCell(0).setCellValue(""+(rows+1));
			for (int i = 1; i < ss.length+1; i++) {
				row.createCell(i).setCellValue(ss[i-1]);
			}
			
		} else {
			wb = new HSSFWorkbook();
			HSSFSheet sheet = wb.createSheet();
			sheet.setColumnWidth(1, 8000);
			sheet.setColumnWidth(2, 8000);
			sheet.setColumnWidth(3, 8000);
			HSSFRow row = sheet.createRow(0);
			//序号	当前时间	条码	ID	型号
			String[] s = {"序号","当前时间","条码","ID","型号"};
			for (int i = 0; i < 5; i++) {
				row.createCell(i).setCellValue(s[i]);
			}
			row = sheet.createRow(1);
			row.createCell(0).setCellValue("1");
			for (int i = 1; i < ss.length+1; i++) {
				row.createCell(i).setCellValue(ss[i-1]);
			}
		}
		try {
			FileOutputStream fos = new FileOutputStream(path);
			wb.write(fos);
			wb.close();
			fos.close();
		} catch (Exception e) {
			if (e.getMessage().contains("另一个程序正在使用此文件，进程无法访问")) {
				System.out.println("你正打开着文件，数据无法写入，请关闭文件，重新操作！");
			}
		}*/
	}
	private void createXLS(File file) throws Exception{
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet();
		sheet.setColumnWidth(1, 8000);
		sheet.setColumnWidth(2, 8000);
		sheet.setColumnWidth(3, 8000);
		HSSFRow row = sheet.createRow(0);
		//序号	当前时间	条码	ID	型号
		String[] s = {"序号","当前时间","条码","ID","型号","品牌"};
		for (int i = 0; i < 6; i++) {
			row.createCell(i).setCellValue(s[i]);
		}
		FileOutputStream fos = new FileOutputStream(file);
		wb.write(fos);
		wb.close();
		fos.close();
	}
}
