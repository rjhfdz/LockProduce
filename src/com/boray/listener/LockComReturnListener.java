package com.boray.listener;

import io.netty.buffer.ByteBufUtil;
import java.io.InputStream;
import javax.swing.JLabel;
import com.boray.Data.Data;
import com.boray.EncryPwd.TeaUtil;
import com.boray.ui.mainUI;

public class LockComReturnListener implements Runnable{
	public void run() {
		try {
			InputStream is = Data.lockSerialPort.getInputStream();
			byte[] temp = new byte[1024];
			int len = -1;
			JLabel label = (JLabel)mainUI.map.get("lockLabel");
			while (true) {
				len = is.read(temp);
				if (len > 4 && Byte.toUnsignedInt(temp[3])==2) {
					String str = "";
					byte[] en = new byte[17];
					for (int i = 0; i < 17; i++) {
						en[i] = temp[4+i];
					}
					str = ByteBufUtil.hexDump(TeaUtil.decryptByHexKey("424F53444F4E4C6F636B4C6F6E767838", en)).toUpperCase();
					if (!label.getText().equals(str) && str.length() == 24) {
						label.setText(str);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
