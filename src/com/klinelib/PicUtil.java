package com.klinelib;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

public class PicUtil {
	public final static int K_LINE_IMAGE_HEIGH = 333;
	public final static int K_LINE_IMAGE_WIDTH = 1059;
	public final static int K_LINE_SMALL_IMAGE_WIDTH = 120;
	public final static int STOCK_NUMBER_EACH_ROW = 10;
	public final static int PREPOS_STOCK_NUMBER = 10;
	public final static int SPACE_IMAGE_STOCK_NUMBER = 2;
	public final static int HEIGH_STOCK_NUMBER = 10;
	public final static int K_LINE_SMALL_IMAGE_COUNT_ALL = -1;

	public static void reduceImg(String imgdist, String flag,
			int stockCountInSmall, String today) {
		try {
			List stockNoList = null;
			if (stockCountInSmall == K_LINE_SMALL_IMAGE_COUNT_ALL) {
				stockNoList = StockIDReader.read(flag);
			} else {
				stockNoList = StockIDReader.read(flag, stockCountInSmall);
			}
			int row_count = stockNoList.size() / STOCK_NUMBER_EACH_ROW;
			if (stockNoList.size() % STOCK_NUMBER_EACH_ROW != 0) {
				row_count++;
			}

			double ratioHeightWidth = K_LINE_IMAGE_WIDTH / K_LINE_IMAGE_HEIGH;
			int heightdist = (int) (K_LINE_SMALL_IMAGE_WIDTH / ratioHeightWidth);
			BufferedImage tag = new BufferedImage(
					(int) K_LINE_SMALL_IMAGE_WIDTH * STOCK_NUMBER_EACH_ROW,
					(int) (heightdist + HEIGH_STOCK_NUMBER
							+ SPACE_IMAGE_STOCK_NUMBER + SPACE_IMAGE_STOCK_NUMBER)
							* row_count, BufferedImage.TYPE_INT_RGB);

			for (int i = 0; i < stockNoList.size(); i++) {
				String stockID = (String) stockNoList.get(i);
				String imgsrc = Constants.DATA_ROOT + today + "\\" + flag
						+ "\\" + stockID + ".bmp";
				File srcfile = new File(imgsrc);
				if (!srcfile.exists()) {
					return;
				}

				Image src = javax.imageio.ImageIO.read(srcfile);

				int rowNo = i / STOCK_NUMBER_EACH_ROW;
				int columnNo = i % STOCK_NUMBER_EACH_ROW;
				tag
						.getGraphics()
						.drawImage(
								src.getScaledInstance(K_LINE_SMALL_IMAGE_WIDTH,
										heightdist, Image.SCALE_SMOOTH),
								K_LINE_SMALL_IMAGE_WIDTH * columnNo,
								rowNo
										* (heightdist + HEIGH_STOCK_NUMBER
												+ SPACE_IMAGE_STOCK_NUMBER + SPACE_IMAGE_STOCK_NUMBER),
								null);
				tag
						.getGraphics()
						.drawString(
								stockID,
								K_LINE_SMALL_IMAGE_WIDTH * columnNo
										+ PREPOS_STOCK_NUMBER,
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
		String flag = Constants.FLAG;
		if (args.length > 0) {
			flag = args[0].toUpperCase();
		}
		int stockCountInSmall = K_LINE_SMALL_IMAGE_COUNT_ALL;
		if (args.length > 1) {
			String temp = args[1];
			stockCountInSmall = Integer.parseInt(temp);
		}

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
		String smallFileName = Constants.DATA_ROOT + today + "\\" + flag + "\\"
				+ flag + "_small.png";
		if (stockCountInSmall != K_LINE_SMALL_IMAGE_COUNT_ALL) {
			smallFileName = Constants.DATA_ROOT + today + "\\" + flag + "\\"
					+ flag + "_small_" + stockCountInSmall + ".png";
		}
		reduceImg(smallFileName, flag, stockCountInSmall, today);
	}
}
