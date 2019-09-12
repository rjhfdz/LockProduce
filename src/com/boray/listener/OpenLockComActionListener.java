package com.boray.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.OutputStream;
import java.util.Timer;
import java.util.TimerTask;

import javax.comm.CommPortIdentifier;
import javax.comm.SerialPort;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.boray.Data.Data;
import com.boray.ui.mainUI;

public class OpenLockComActionListener implements ActionListener{
	private JComboBox box;
	private Thread thread;
	public OpenLockComActionListener(JComboBox box) {
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
				Data.lockSerialPort = (SerialPort)cpid.open(comsString, 5);
				Data.lockSerialPort.setSerialPortParams(9600, 8, 1, 0);
				thread = new Thread(new LockComReturnListener());
				thread.start();
				Data.lockTimer = new Timer();
				Data.lockTimer.schedule(new TimerTask() {
					public void run() {
						try {
							OutputStream os = Data.lockSerialPort.getOutputStream();
							byte[] b = new byte[7];
							//0x5353	0x07	0x02	0x00	0x09	0x45
							b[0] = 0x53;b[1] = 0x53;b[2] = 0x07;b[3] = (byte) 0x02;b[5] = 0x09;b[6] = 0x45;
							os.write(b);
							os.flush();
						} catch (Exception e2) {
							e2.printStackTrace();
						}
					}
				}, 500,500);
			} catch (Exception e2) {
				btn.setText("打开串口");
				JFrame frame = (JFrame)mainUI.map.get("frame");
				JOptionPane.showMessageDialog(frame, "串口已被占用!", "提示", JOptionPane.ERROR_MESSAGE);
			}
			//Data.lockCom = comsString;
		} else if ("关闭串口".equals(command)) {
			btn.setText("打开串口");
			Data.lockTimer.cancel();
			Data.lockTimer = null;
			thread.interrupt();
			thread = null;
			Data.lockSerialPort.close();
		}
	}
}
