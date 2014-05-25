package com.klinelib;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

public class CaptureScreen {

	public static void main(String[] args) {
		String flag = Constants.FLAG;
		// String flag = "SSBK";
		if (args.length > 0) {
			flag = args[0].toUpperCase();
		}
		List stockSSID = StockIDReader.read(flag);
		// stockSSID.add("000001");
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String today = null;
			try {
				today = sdf.format(new Date());
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new RuntimeException(ex);
			}
			File file = new File(Constants.DATA_ROOT + today + "\\" + flag);
			if (!file.exists()) {
				file.mkdirs();
			}
			String runningID = "";
			String idFlag = flag;
			if (flag.equals("SSBK")) {
				idFlag = "SS";
			}
			if (flag.equals("SZBK")) {
				idFlag = "SZ";
			}

			for (int i = 0; i < stockSSID.size(); i++) {
				String previousID = null;
				if (i == 0) {
					previousID = "XXXXXX";
				} else {
					previousID = (String) stockSSID.get(i - 1) + "." + idFlag;
				}
				runningID = (String) stockSSID.get(i);
				String nextID = (String) stockSSID.get(i) + "." + idFlag;

				StringRpl.replace(previousID, nextID);
				Process p = Runtime.getRuntime().exec(
						"C:\\Program Files\\Java\\jdk1.6.0_16\\bin\\appletviewer "
								+ Constants.DATA_ROOT + "Applet.html");
				Thread.sleep(5000);
				captureScreen(Constants.DATA_ROOT + today + "\\" + flag + "\\"
						+ runningID + ".bmp");
				System.out.println("capture is okay");
				p.destroy();

				if (i == stockSSID.size() - 1) {
					previousID = nextID;
					nextID = "XXXXXX";
					StringRpl.replace(previousID, nextID);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void captureScreen(String fileName) throws Exception {
		Rectangle screenRectangle = new Rectangle(0, 0, 1270, 410);
		// Rectangle screenRectangle = new Rectangle(4, 70, 1060, 334);
		Robot robot = new Robot();
		BufferedImage image = robot.createScreenCapture(screenRectangle);
		ImageIO.write(image, "png", new File(fileName));
	}

}
