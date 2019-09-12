package com.boray.listener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;

import javax.swing.JComboBox;
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


public class ScanGunComReturnListener implements Runnable {
    public void run() {
        try {
            InputStream is = Data.scanGunSerialPort.getInputStream();
            byte[] temp = new byte[1024];
            int len = -1;
            JLabel scanGunLabel = (JLabel) mainUI.map.get("scanGunLabel");
            JLabel lockLabel = (JLabel) mainUI.map.get("lockLabel");
            JLabel recordLabel = (JLabel) mainUI.map.get("recordLabel");
            JComboBox TypeBox = (JComboBox) mainUI.map.get("TypeBox");
            JComboBox BrandBox = (JComboBox) mainUI.map.get("BrandBox");
            while (true) {
                len = is.read(temp);
                String lockId = lockLabel.getText();
                if (len > 2 && !"".equals(lockId)) {
                    if (Data.projectFile == null) {
                        new MenuItemListener().newProject();
                    }
                    if (Data.projectFile != null) {
                        int oldSize = Data.lockIdTreeSet.size();
                        Data.lockIdTreeSet.add(lockId);
                        if (Data.lockIdTreeSet.size() > oldSize) {
                            String snString = new String(temp, 0, len);
                            oldSize = Data.SnTreeSet.size();
                            Data.SnTreeSet.add(snString);
                            if (Data.SnTreeSet.size() > oldSize) {
                                scanGunLabel.setText(snString);
                                NewJTable table = (NewJTable) mainUI.map.get("Table");
                                DefaultTableModel model = (DefaultTableModel) table.getModel();
                                String[] objects = {lockId, scanGunLabel.getText()};
                                Date date = new Date(System.currentTimeMillis());
                                String s = TypeBox.getSelectedItem().toString();
                                String s1 = BrandBox.getSelectedItem().toString();
                                String[] tt = {date.toLocaleString(), objects[1], objects[0], s.substring(s.indexOf("（") + 1, s.indexOf("）")), s1.substring(s1.indexOf("（") + 1, s1.indexOf("）"))};
                                if (writeExcel(tt)) {
                                    recordLabel.setText(Data.lockIdTreeSet.size() + "");
                                    if (table.getRowCount() == 5) {
                                        model.removeRow(0);
                                        model.addRow(objects);
                                    } else {
                                        model.addRow(objects);
                                    }
                                    scanGunLabel.setText("");
                                    lockLabel.setText("");
                                }
                            } else {
                                JFrame frame = (JFrame) mainUI.map.get("frame");
                                JOptionPane.showMessageDialog(frame, "锁的SN已重复！", "提示", JOptionPane.WARNING_MESSAGE);
                            }
                        } else {
                            JFrame frame = (JFrame) mainUI.map.get("frame");
                            JOptionPane.showMessageDialog(frame, "锁的ID已重复！", "提示", JOptionPane.WARNING_MESSAGE);
                        }
                    }/* else {
						JFrame frame = (JFrame)mainUI.map.get("frame");
						JOptionPane.showMessageDialog(frame, "请先创建工程或打开一个工程，再操作！", "提示", JOptionPane.ERROR_MESSAGE);
					}*/
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean writeExcel(String[] ss) {
        try {
            if (Data.projectFile != null) {
                FileInputStream fs = new FileInputStream(Data.projectFile);
                POIFSFileSystem ps = new POIFSFileSystem(fs);
                HSSFWorkbook wb = new HSSFWorkbook(ps);
                HSSFSheet sheet = wb.getSheetAt(0);
                int rows = sheet.getLastRowNum();
                HSSFRow row = sheet.createRow((short) (rows + 1));
                row.createCell(0).setCellValue("" + (rows + 1));
                for (int i = 1; i < ss.length + 1; i++) {
                    row.createCell(i).setCellValue(ss[i - 1]);
                }
                FileOutputStream fos = new FileOutputStream(Data.projectFile);
                wb.write(fos);
                wb.close();
                fos.close();
            }
            return true;
        } catch (Exception e) {
            Data.lockIdTreeSet.remove(ss[2]);
            JFrame frame = (JFrame) mainUI.map.get("frame");
            JOptionPane.showMessageDialog(frame, "写入失败，原因：" + e.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}
