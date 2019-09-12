package com.boray.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.comm.CommPortIdentifier;
import javax.comm.SerialPort;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.boray.Data.Data;
import com.boray.ui.mainUI;

public class OpenScanGunComActionListener implements ActionListener{
	private JComboBox box;
	private Thread thread;
	public OpenScanGunComActionListener(JComboBox box) {
		this.box = box;
	}

	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		JButton btn = (JButton)e.getSource();
		if ("打开串口".equals(command)) {
			btn.setText("关闭串口");
			String comsString = box.getSelectedItem().toString();
			try {
				CommPortIdentifier cpid = CommPortIdentifier.getPortIdentifier(comsString);
				Data.scanGunSerialPort = (SerialPort)cpid.open(comsString, 5);
				Data.scanGunSerialPort.setSerialPortParams(115200, 8, 1, 0);
				thread = new Thread(new ScanGunComReturnListener());
				thread.start();
			} catch (Exception e2) {
				btn.setText("打开串口");
				JFrame frame = (JFrame)mainUI.map.get("frame");
				JOptionPane.showMessageDialog(frame, "串口已被占用!", "提示", JOptionPane.ERROR_MESSAGE);
			}
			//Data.scanGunCom = comsString;
		} else if ("关闭串口".equals(command)) {
			btn.setText("打开串口");
			thread.interrupted();
			Data.scanGunSerialPort.close();
		}
	}
}
