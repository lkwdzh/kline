import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class PicUtil {
	public final static int K_LINE_IMAGE_HEIGH = 333;
	public final static int K_LINE_IMAGE_WIDTH = 1059;
	public final static int K_LINE_SMALL_IMAGE_WIDTH = 120;
	public final static int STOCK_NUMBER_EACH_ROW = 10;
	public final static int PREPOS_STOCK_NUMBER = 10;
	public final static int SPACE_IMAGE_STOCK_NUMBER = 2;
	public final static int HEIGH_STOCK_NUMBER = 10;

	public static void reduceImg(String imgsrc, String imgdist) {
		try {
			File srcfile = new File(imgsrc);
			if (!srcfile.exists()) {
				return;
			}
			Image src = javax.imageio.ImageIO.read(srcfile);
			double ratioHeightWidth = K_LINE_IMAGE_WIDTH / K_LINE_IMAGE_HEIGH;
			int heightdist = (int) (K_LINE_SMALL_IMAGE_WIDTH / ratioHeightWidth);
			BufferedImage tag = new BufferedImage((int) K_LINE_SMALL_IMAGE_WIDTH * STOCK_NUMBER_EACH_ROW,
					(int) heightdist * 10, BufferedImage.TYPE_INT_RGB);

			for (int i = 0; i < 29; i++) {
				int rowNo = i / STOCK_NUMBER_EACH_ROW;
				int columnNo = i % STOCK_NUMBER_EACH_ROW;
				tag
						.getGraphics()
						.drawImage(
								src.getScaledInstance(K_LINE_SMALL_IMAGE_WIDTH, heightdist,
										Image.SCALE_SMOOTH),
										K_LINE_SMALL_IMAGE_WIDTH * columnNo,
								rowNo
										* (heightdist + HEIGH_STOCK_NUMBER
												+ SPACE_IMAGE_STOCK_NUMBER + SPACE_IMAGE_STOCK_NUMBER),
								null);
				tag
						.getGraphics()
						.drawString(
								"000001",
								K_LINE_SMALL_IMAGE_WIDTH * columnNo + PREPOS_STOCK_NUMBER,
								(rowNo
										* (heightdist + +HEIGH_STOCK_NUMBER
												+ SPACE_IMAGE_STOCK_NUMBER + SPACE_IMAGE_STOCK_NUMBER)
										+ heightdist + HEIGH_STOCK_NUMBER + SPACE_IMAGE_STOCK_NUMBER));
			}

			ImageIO.write(tag, "png", new File(imgdist));

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public static void main(String[] args) {
		reduceImg("I:\\股票研究\\SZ\\000001.bmp", "I:\\股票研究\\SZ\\000001s.bmp");
	}
}
