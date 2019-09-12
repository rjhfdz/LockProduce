package com.boray.listener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.TreeSet;

import javax.swing.JLabel;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class test {
	public static void main(String[] args) {
		TreeSet set = new TreeSet();
		set.add("BD0102111304040B03060185");
		set.add("BD0102111304040B03060186");
		set.add("BD0102111304040B03060187");
		set.add("BD0102111304040B03060185");
		System.out.println(set.size());
		System.out.println(set);
		/*try {
			String[] ss = {"2019-5-17 10:54",
					"BD0102111304040B03060185","BD0102111304040B03060185","0002"};
			creatExcel(ss);
		} catch (Exception e) {
			// TODO: handle exception
		}*/
	}
	public static void creatExcel(String[] ss) throws Exception{
		String path = "C:/Documents and Settings/Administrator/����/�½��ļ��� (4)/a.xls",mac = "abc";
		File file = new File(path);
		HSSFWorkbook wb;
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
			//���	��ǰʱ��	����	ID	�ͺ�
			String[] s = {"���","��ǰʱ��","����","ID","�ͺ�"};
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
			if (e.getMessage().contains("��һ����������ʹ�ô��ļ��������޷�����")) {
				//tips.setText("���������ļ��������޷�д�룬��ر��ļ���");
				System.out.println("���������ļ��������޷�д�룬��ر��ļ������²�����");
			}
		}
	}
}
