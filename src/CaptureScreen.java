import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CaptureScreen {
	private static List stockSSID = new ArrayList();
	private static String flag = "SS"; 
//	private static String flag = "SZ"; 
//	private static String flag = "BK"; 

	public static void main(String[] args) {
		read();

//		stockSSID.add("000001");
//		stockSSID.add("000002");
//		stockSSID.add("000004");
//		stockSSID.add("000005");
//		stockSSID.add("000006");
//		stockSSID.add("000007");
//		stockSSID.add("000008");
//		stockSSID.add("000930");
//		stockSSID.add("000010");

		try {
			for (int i = 0; i < stockSSID.size(); i++) {
				String previousID = null;
				if (i ==0){
					previousID = "XXXXXX";
				}else{
					previousID = (String)stockSSID.get(i - 1);
				}
				String nextID = (String)stockSSID.get(i);;
				StringRpl.replace(previousID, nextID);
				Process p = Runtime
						.getRuntime()
						.exec(
								"C:\\Program Files\\Java\\jdk1.6.0_16\\bin\\appletviewer I:\\股票研究\\Applet.html");
				Thread.sleep(10000);
				captureScreen("I:\\股票研究\\" + flag + "\\" + nextID + ".bmp");
				System.out.println("capture is okay");
				p.destroy();
				
				if(i == stockSSID.size() -1 ){
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
//		// Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//		Dimension klineScreen = new Dimension();
//		klineScreen.setSize(1270, 410);
//		Rectangle screenRectangle = new Rectangle(0,0,1270, 410);
		Rectangle screenRectangle = new Rectangle(4,70,1060, 334);
		Robot robot = new Robot();
		BufferedImage image = robot.createScreenCapture(screenRectangle);
		ImageIO.write(image, "png", new File(fileName));
	}

	public static void read() {
		
		String file = "I:\\股票研究\\" + flag + ".txt";
		String s = null;
		StringBuffer sb = new StringBuffer();
		File f = new File(file);
		if (f.exists()) {
			System.out.println("该文件存在");
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(
						new FileInputStream(f)));
				while ((s = br.readLine()) != null) {
					String id = s.substring(0, s.indexOf(" "));
					System.out.println(id);
					stockSSID.add(id);
				}
				System.out.println(sb);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("该文件不存在!");
		}
		System.out.println(sb);
	}

}
