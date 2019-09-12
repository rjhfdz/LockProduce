package com.boray.listener;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.boray.Data.Data;
import com.boray.ui.NewJTable;
import com.boray.ui.mainUI;

public class PopupListener implements ActionListener{
	private int selected = 0;
	public void actionPerformed(ActionEvent e) {
		if ("　删除　　　　　".equals(e.getActionCommand())) {
			Object[] options = { "否", "是" };
			int yes = JOptionPane.showOptionDialog((JFrame)mainUI.map.get("frame"), "将删除第"+(selected+1)+"行数据，是否要继续？", "警告",
			JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
			null, options, options[1]);
			if (yes == 1) {
				NewJTable table = (NewJTable)mainUI.map.get("Table");
				String id = table.getValueAt(selected, 0).toString();
				Data.lockIdTreeSet.remove(id);
				removeExcelData(table.getRowCount()-selected);
				
				DefaultTableModel model = (DefaultTableModel)table.getModel();
				model.removeRow(selected);
				JLabel recordLabel = (JLabel)mainUI.map.get("recordLabel");
				recordLabel.setText(Data.lockIdTreeSet.size()+"");
			} else {
				
			}
		}
	}
	private void removeExcelData(int selected){
		try {
			if (Data.projectFile != null) {
				FileInputStream fs=new FileInputStream(Data.projectFile);
		        POIFSFileSystem ps=new POIFSFileSystem(fs);
		        HSSFWorkbook wb = new HSSFWorkbook(ps);
				HSSFSheet sheet = wb.getSheetAt(0);
				int rows = sheet.getLastRowNum()+1;
				int dte = rows-selected;
				
				if (dte == rows-1) {
					sheet.removeRow(sheet.getRow(sheet.getLastRowNum()));
				} else {
					sheet.shiftRows(dte+1, sheet.getLastRowNum(), -1, true, false); 
					sheet.removeRow(sheet.getRow(sheet.getLastRowNum() + 1)); 
					for (int i = 1; i < rows-1; i++) {
						HSSFRow row = sheet.getRow(i);
						row.getCell(0).setCellValue(i+"");
					}
				}
				FileOutputStream fos = new FileOutputStream(Data.projectFile);
				wb.write(fos);
				wb.close();
				fos.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void setSelected(int select){
		selected = select;
	} 
}
