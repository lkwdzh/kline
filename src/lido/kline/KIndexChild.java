/*      */ package lido.kline;
/*      */ 
/*      */ import java.awt.Color;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.FontMetrics;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.Panel;
/*      */ import java.awt.Rectangle;
/*      */ import java.util.Date;
/*      */ import lido.common.CommEnv;
/*      */ 
/*      */ public class KIndexChild extends Panel
/*      */ {
/*      */   private int m_calcFinRec;
/*      */   private int m_calcOrgRec;
/*      */   private int m_calcParamCount;
/*      */   private int[] m_calcParamValue;
/*      */   private int m_displayParamCount;
/*      */   private String[] m_displayParamName;
/*      */   public Rectangle m_frameSize;
/*      */   private String m_indexName;
/*      */   public Rectangle m_indexNameSize;
/*      */   public Rectangle m_indexTitleSize;
/*      */   private KLineDoc m_kdoc;
/*      */   public Rectangle m_leftRulerSize;
/*      */   private int m_lrulerStep;
/*      */   private int m_mainIndexId;
/*      */   private double m_maxValue;
/*      */   private double m_minValue;
/*      */   private KLineMainView m_parent;
/*      */   private double m_pixelsPerUnit;
/*      */   private double[][] m_storedValue;
/*      */   private int m_subIndexId;
/*      */   public static final int s_AMOUNT = 4;
/*      */   public static final int s_BAR = 1;
/*      */   public static final int s_BOLBN = 4;
/*      */   public static final int s_BRAR = 20;
/*      */   public static final int s_CCI = 13;
/*      */   public static final int s_CR = 21;
/*      */   public static final int s_DMI = 16;
/*      */   private static final int s_DMI_COMP_FROM = 50;
/*      */   public static final int s_EMA = 2;
/*      */   private static final int s_EMA_COMP_FROM = 100;
/*      */   public static final int s_KDJ = 11;
/*      */   public static final int s_KLINE = 0;
/*      */   public static final int s_MA = 10;
/*      */   public static final int s_MACD = 15;
/*      */   private static final int s_MACD_COMP_FROM = 50;
/*      */   public static final int s_MIKEM = 6;
/*      */   public static final int s_MIKES = 7;
/*      */   public static final int s_MIKEW = 5;
/*      */   public static final int s_NONE = 5;
/*      */   public static final int s_PMA = 0;
/*      */   public static final int s_PRICE = 2;
/*      */   public static final int s_ROC = 14;
/*      */   public static final int s_RSI = 17;
/*      */   private static final int s_RSI_COMP_FROM = 50;
/*      */   public static final int s_SAR = 8;
/*      */   private static final int s_SAR_COMP_FROM = 50;
/*      */   public static final int s_SLOWKD = 12;
/*      */   public static final int s_SSL = 9;
/*      */   public static final int s_TREND = 3;
/*      */   public static final int s_VOL = 3;
/*      */   public static final int s_VR = 22;
/*      */   public static final int s_VRSI = 18;
/*      */   public static final int s_WMA = 1;
/*      */   public static final int s_WMS = 19;
/*      */   private static final double s_kwidthDecreaseRatio = 0.8D;
/*   27 */   public static final String[][] s_mainIndexInfo = { { "", "Ｋ线　（烛线）图" }, { "", "美国线（条状）图" }, { "", "收盘线（折线）图" }, { "VOL", "成交量\tVOL " }, { "AMOUNT", "成交额\tAMOUNT " }, { "", "" } };
/*      */   public static final int s_mainIndexNum = 5;
/*   73 */   public static final String[][] s_subIndexInfo = { { "PMA", "移动平均线\tPMA", "7", "0", "10", "3", "5", "10", "20", "3", "MA~0", "MA~1", "MA~2" }, { "WMA", "加权移动平均线\tWMA", "7", "0", "10", "3", "5", "10", "20", "3", "MA~0", "MA~1", "MA~2" }, { "EMA", "指数移动平均线\tEMA ", "7", "0", "10", "3", "5", "10", "20", "3", "MA~0", "MA~1", "MA~2" }, { "TREND", "轨道线\tTREND ", "7", "0", "10", "3", "5", "10", "10", "3", "TREND~0", "UPPER~1", "LOWER~2" }, { "BOLBN", "布林线\tBOLBN ", "7", "0", "10", "1", "10", "4", "B1", "B2", "B3", "B4" }, { "MIKEW", "初级压力支撑\tMIKEW ", "7", "0", "10", "1", "12", "2", "R1", "S1" }, { "MIKEM", "中级压力支撑\tMIKEM ", "7", "0", "10", "1", "12", "2", "R2", "S2" }, { "MIKES", "强力压力支撑\tMIKES ", "7", "0", "10", "1", "12", "2", "R3", "S3" }, { "SAR", "抛物线转向\tSAR ", "7", "0", "10", "3", "4", "2", "20", "2", "Brush", "L~0" }, { "SSL", "支撑压力表\tSSL ", "7", "0", "10", "0", "0" }, { "", "移动平均线\tMA ", "2", "0", "200", "2", "5", "10", "2", "MA~0", "MA~1" }, { "KDJ", "随机指标\tKDJ ", "4", "0", "100", "4", "9", "3", "3", "1", "3", "%K~1", "%D~2", "%J" }, { "SLOWKD", "慢速随机指标\tSLOWKD ", "4", "0", "100", "4", "5", "3", "3", "5", "2", "%K~3", "%D~2" }, { "CCI", "商品管道\tCCI ", "4", "-100", "100", "2", "21", "15", "1", "CCI~0" }, { "ROC", "变动速率\tROC ", "4", "-10", "10", "2", "12", "5", "2", "ROC~0", "MA~1" }, { "MACD", "平滑异同均线\tMACD ", "4", "-1", "1", "3", "12", "26", "9", "3", "Bar", "DIF", "MACD" }, { "DMI", "趋向指标\tDMI ", "4", "0", "100", "3", "5", "5", "5", "4", "+DI", "-DI", "ADX", "ADXR" }, { "RSI", "强弱指标\tRSI ", "4", "0", "100", "2", "9", "14", "2", "RSI~0", "RSI~1" }, { "VRSI", "成交量强弱指标\tVRSI ", "4", "0", "100", "2", "5", "10", "2", "VRSI~0", "VRSI~1" }, { "WMS", "威廉指标\tWMS ", "4", "0", "100", "1", "10", "1", "WMS%R~0" }, { "BRAR", "意愿及人气指标\tBRAR ", "4", "0", "400", "2", "26", "26", "2", "AR~0", "BR~1" }, { "CR", "ＣＲ指标\tCR", "4", "0", "400", "4", "26", "10", "20", "40", "4", "CR~0", "MA~1", "MA~2", "MA~3" }, { "VR", "成交量指标\tVR ", "4", "0", "400", "2", "24", "5", "2", "VR~0", "MA~1" } };
/*      */   public static final int s_subIndexNum = 23;
/*      */ 
/*      */   public KIndexChild(KLineMainView p_parent, KLineDoc p_kdoc, Rectangle p_size, int p_mainIndexId, int p_subIndexId)
/*      */   {
/*  282 */     setBounds(p_size);
/*  283 */     this.m_parent = p_parent;
/*  284 */     this.m_kdoc = p_kdoc;
/*  285 */     this.m_mainIndexId = p_mainIndexId;
/*  286 */     this.m_subIndexId = p_subIndexId;
/*  287 */     this.m_frameSize = new Rectangle(60, 14, p_size.width - 60 - 1, p_size.height - 14 - 1);
/*      */ 
/*  292 */     this.m_leftRulerSize = new Rectangle(0, 14, 60, this.m_frameSize.height);
/*      */ 
/*  297 */     this.m_indexNameSize = new Rectangle(0, 0, 60, 14);
/*      */ 
/*  302 */     this.m_indexTitleSize = new Rectangle(60, 0, this.m_frameSize.width, 14);
/*      */ 
/*  307 */     f_refreshIndex(true);
/*      */   }
/*      */ 
/*      */   public void f_changeIndex(int p_mainIndexId, int p_subIndexId)
/*      */   {
/* 1290 */     if ((p_mainIndexId >= 0) && (this.m_mainIndexId != p_mainIndexId))
/*      */     {
/* 1292 */       this.m_mainIndexId = p_mainIndexId;
/* 1293 */       if ((p_mainIndexId == 3) || (p_mainIndexId == 4))
/*      */       {
/* 1295 */         f_refreshIndex(true);
/* 1296 */         repaint();
/*      */       }
/*      */       else {
/* 1299 */         repaint(this.m_frameSize.x, this.m_frameSize.y, this.m_frameSize.width, this.m_frameSize.height);
/*      */       }
/*      */     }
/* 1301 */     if ((p_subIndexId >= 0) && (this.m_subIndexId != p_subIndexId))
/*      */     {
/* 1303 */       this.m_subIndexId = p_subIndexId;
/* 1304 */       f_refreshIndex(true);
/* 1305 */       repaint();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void f_checkMinMax(double p_value)
/*      */   {
/* 1113 */     if (this.m_minValue > p_value)
/* 1114 */       this.m_minValue = p_value;
/* 1115 */     if (this.m_maxValue < p_value)
/* 1116 */       this.m_maxValue = p_value;
/*      */   }
/*      */ 
/*      */   private void f_drawBar(Graphics p_g, KLineRec p_rec, double p_width, int p_xpos)
/*      */   {
/* 1394 */     int l_xbeg = p_xpos - (int)(p_width / 2.0D + 0.5D);
/* 1395 */     int l_xend = p_xpos + (int)(p_width / 2.0D + 0.5D);
/* 1396 */     int l_close = f_unit2pos(p_rec.m_close);
/* 1397 */     int l_open = f_unit2pos(p_rec.m_open);
/* 1398 */     int l_low = f_unit2pos(p_rec.m_low);
/* 1399 */     int l_high = f_unit2pos(p_rec.m_high);
/* 1400 */     p_g.setColor(p_rec.m_open > p_rec.m_close ? CommEnv.s_klineDownColor : CommEnv.s_klineUpColor);
/*      */ 
/* 1403 */     p_g.drawLine(p_xpos, l_high, p_xpos, l_low + 1);
/* 1404 */     p_g.drawLine(l_xbeg, l_open, p_xpos, l_open);
/* 1405 */     p_g.drawLine(p_xpos, l_close, l_xend, l_close);
/*      */   }
/*      */ 
/*      */   private void f_drawCellLine(Graphics p_g, int p_x1, int p_y1, int p_x2, int p_y2)
/*      */   {
/* 1434 */     p_g.setColor(CommEnv.s_klineFrameColor);
/* 1435 */     if (p_x1 == p_x2)
/*      */     {
/* 1437 */       for (int l_y = p_y1; l_y <= p_y2; l_y += 4) {
/* 1438 */         p_g.drawLine(p_x1, l_y, p_x1, l_y);
/*      */       }
/*      */     }
/*      */     else
/* 1442 */       for (int l_x = p_x1; l_x <= p_x2; l_x += 4)
/* 1443 */         p_g.drawLine(l_x, p_y1, l_x, p_y1);
/*      */   }
/*      */ 
/*      */   private void f_drawKLine(Graphics p_g, KLineRec p_rec, double p_width, int p_xpos)
/*      */   {
/* 1369 */     int l_width = (int)(p_width + 0.5D);
/* 1370 */     if (l_width > 1) l_width--;
/* 1371 */     int l_xbeg = p_xpos - l_width / 2;
/* 1372 */     int l_close = f_unit2pos(p_rec.m_close);
/* 1373 */     int l_open = f_unit2pos(p_rec.m_open);
/* 1374 */     int l_low = f_unit2pos(p_rec.m_low);
/* 1375 */     int l_high = f_unit2pos(p_rec.m_high);
/* 1376 */     if (p_rec.m_open > p_rec.m_close)
/*      */     {
/* 1378 */       p_g.setColor(CommEnv.s_klineDownColor);
/* 1379 */       p_g.fillRect(l_xbeg, l_open, l_width, l_close - l_open + 1);
/* 1380 */       p_g.drawLine(p_xpos, l_high, p_xpos, l_open);
/* 1381 */       p_g.drawLine(p_xpos, l_close + 1, p_xpos, l_low + 1);
/*      */     }
/*      */     else
/*      */     {
/* 1385 */       p_g.setColor(CommEnv.s_klineUpColor);
/* 1386 */       p_g.drawRect(l_xbeg, l_close, l_width, l_open - l_close + 1);
/* 1387 */       p_g.drawLine(p_xpos, l_high, p_xpos, l_close);
/* 1388 */       p_g.drawLine(p_xpos, l_open + 1, p_xpos, l_low + 1);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void f_drawVolMoney(Graphics p_g, KLineRec p_rec, double p_width, int p_xpos)
/*      */   {
/* 1410 */     int l_dec = this.m_kdoc.f_isZhaiQuan() ? 10 : 100;
/* 1411 */     int l_width = (int)(p_width + 0.5D);
/* 1412 */     if (l_width > 1) l_width--;
/* 1413 */     int l_xbeg = p_xpos - l_width / 2;
/* 1414 */     int l_ypos = f_unit2pos(this.m_mainIndexId == 3 ? p_rec.m_volume / l_dec : p_rec.m_money / 10000.0D);
/*      */ 
/* 1416 */     if (l_ypos >= this.m_frameSize.height + this.m_frameSize.y - 2) {
/* 1417 */       l_ypos = this.m_frameSize.height + this.m_frameSize.y - 3;
/*      */     }
/*      */ 
/* 1420 */     if (p_rec.m_open > p_rec.m_close)
/*      */     {
/* 1422 */       p_g.setColor(CommEnv.s_klineDownColor);
/* 1423 */       p_g.fillRect(l_xbeg, l_ypos, l_width, this.m_frameSize.height + this.m_frameSize.y - l_ypos);
/*      */     }
/*      */     else
/*      */     {
/* 1427 */       p_g.setColor(CommEnv.s_klineUpColor);
/* 1428 */       p_g.drawRect(l_xbeg, l_ypos, l_width, this.m_frameSize.height + this.m_frameSize.y - l_ypos);
/*      */     }
/*      */   }
/*      */ 
/*      */   private String f_getDisplayParam(int p_no)
/*      */   {
/* 1600 */     String l_sepStr = "~";
/* 1601 */     int l_sepPos = this.m_displayParamName[p_no].indexOf(l_sepStr);
/* 1602 */     if (l_sepPos >= 0) {
/* 1603 */       return this.m_displayParamName[p_no].substring(0, l_sepPos) + Integer.toString(this.m_calcParamValue[Integer.parseInt(this.m_displayParamName[p_no].substring(l_sepPos + 1))]) + ":";
/*      */     }
/*      */ 
/* 1608 */     return this.m_displayParamName[p_no] + ":";
/*      */   }
/*      */ 
/*      */   private void f_paintIndexLine(Graphics p_g)
/*      */   {
/* 1450 */     int l_org = this.m_parent.f_getOrgRec();
/* 1451 */     int l_fin = this.m_parent.f_getFinRec();
/*      */ 
/* 1455 */     if (this.m_subIndexId == 9)
/*      */     {
/* 1457 */       int l_count = (this.m_frameSize.height - 2) / 12;
/* 1458 */       for (int l_i = 0; l_i < l_count; l_i++)
/*      */       {
/* 1460 */         p_g.setColor(CommEnv.s_sslBarColor);
/* 1461 */         p_g.fillRect(this.m_frameSize.x + 1, this.m_frameSize.y + this.m_frameSize.height - 2 - 12 * (l_i + 1) + 1, (int)Math.ceil(this.m_storedValue[1][l_i] * (this.m_frameSize.width - 2)), 11);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1468 */     if (this.m_mainIndexId == 2)
/*      */     {
/* 1470 */       int[] l_xpos = new int[l_fin - l_org + 1];
/* 1471 */       int[][] l_ypos = new int[2][l_fin - l_org + 1];
/* 1472 */       for (int l_i = l_org; l_i <= l_fin; l_i++)
/*      */       {
/* 1474 */         if (this.m_kdoc.f_getRec(l_i) == null)
/*      */           break;
/* 1476 */         KLineRec l_rec = this.m_kdoc.f_getRec(l_i);
/* 1477 */         l_xpos[(l_i - l_org)] = this.m_parent.f_rec2pos(l_i);
/* 1478 */         l_ypos[0][(l_i - l_org)] = f_unit2pos(l_rec.m_close);
/* 1479 */         l_ypos[1][(l_i - l_org)] = (l_ypos[0][(l_i - l_org)] + 1);
/*      */       }
/* 1481 */       p_g.setColor(CommEnv.s_defaultLineColor);
/* 1482 */       p_g.drawPolyline(l_xpos, l_ypos[0], l_fin - l_org + 1);
/* 1483 */       p_g.drawPolyline(l_xpos, l_ypos[1], l_fin - l_org + 1);
/*      */     }
/* 1485 */     else if (this.m_mainIndexId != 5)
/*      */     {
/* 1487 */       for (int l_i = l_org; l_i <= l_fin; l_i++)
/*      */       {
/* 1489 */         KLineRec l_rec = this.m_kdoc.f_getRec(l_i);
/* 1490 */         double l_width = this.m_parent.f_getPixelsPerRecord() * 0.8D;
/* 1491 */         int l_xpos = this.m_parent.f_rec2pos(l_i);
/* 1492 */         if (this.m_mainIndexId == 0)
/* 1493 */           f_drawKLine(p_g, l_rec, l_width, l_xpos);
/* 1494 */         else if (this.m_mainIndexId == 1)
/* 1495 */           f_drawBar(p_g, l_rec, l_width, l_xpos);
/*      */         else {
/* 1497 */           f_drawVolMoney(p_g, l_rec, l_width, l_xpos);
/*      */         }
/*      */       }
/*      */     }
/* 1501 */     switch (this.m_subIndexId)
/*      */     {
/*      */     case 8:
/* 1504 */       for (int l_i = l_org; l_i <= l_fin; l_i++)
/*      */       {
/* 1506 */         int l_x = this.m_parent.f_rec2pos(l_i);
/* 1507 */         int l_y = f_unit2pos(this.m_storedValue[0][(l_i - l_org)]);
/* 1508 */         switch ((int)this.m_storedValue[1][(l_i - l_org)]) {
/*      */         case 0:
/* 1510 */           p_g.setColor(CommEnv.s_hqUpColor); break;
/*      */         case 1:
/* 1511 */           p_g.setColor(CommEnv.s_hqDownColor); break;
/*      */         case 2:
/* 1512 */           p_g.setColor(CommEnv.s_hqEqualColor);
/*      */         }
/* 1514 */         p_g.drawOval(l_x - 1, l_y - 1, 2, 2);
/*      */       }
/* 1516 */       break;
/*      */     case 9:
/* 1518 */       int l_sslCount = (this.m_frameSize.height - 2) / 12;
/* 1519 */       for (int l_i = 0; l_i < l_sslCount; l_i++)
/*      */       {
/* 1521 */         double l_value = this.m_storedValue[0][l_i] / 100.0D;
/* 1522 */         if (l_value < 1.0D)
/*      */           continue;
/* 1524 */         p_g.setColor(CommEnv.s_sslVolColor);
/* 1525 */         p_g.drawString(CommEnv.f_scaleDouble(l_value, 0), this.m_frameSize.x + 1, this.m_frameSize.y + this.m_frameSize.height - 2 - 12 * (l_i + 1) + 1 + 10);
/*      */       }
/*      */ 
/* 1529 */       break;
/*      */     case 15:
/* 1531 */       for (int l_i = l_org; l_i <= l_fin; l_i++)
/*      */       {
/* 1533 */         if (l_i - this.m_calcParamValue[2] + 1 < this.m_calcOrgRec)
/*      */           continue;
/* 1535 */         double l_value = 2.0D * (this.m_storedValue[0][(l_i - l_org)] - this.m_storedValue[1][(l_i - l_org)]);
/* 1536 */         p_g.setColor(l_value >= 0.0D ? CommEnv.s_klineUpColor : CommEnv.s_klineDownColor);
/* 1537 */         int l_xpos = this.m_parent.f_rec2pos(l_i);
/* 1538 */         int l_ypos = f_unit2pos(l_value);
/* 1539 */         int l_zero = f_unit2pos(0.0D);
/* 1540 */         p_g.drawLine(l_xpos, l_zero, l_xpos, l_ypos);
/*      */       }
/* 1542 */       for (int l_k = 0; l_k < 2; l_k++)
/*      */       {
/* 1544 */         int[] l_xpos = new int[l_fin - l_org + 1];
/* 1545 */         int[] l_ypos = new int[l_fin - l_org + 1];
/* 1546 */         int l_count = 0;
/* 1547 */         for (int l_i = l_org; l_i <= l_fin; l_i++)
/*      */         {
/* 1549 */           if (l_i - this.m_calcParamValue[2] + 1 < this.m_calcOrgRec)
/*      */             continue;
/* 1551 */           l_xpos[l_count] = this.m_parent.f_rec2pos(l_i);
/* 1552 */           l_ypos[l_count] = f_unit2pos(this.m_storedValue[l_k][(l_i - l_org)]);
/* 1553 */           l_count++;
/*      */         }
/* 1555 */         p_g.setColor(CommEnv.s_klineLineColor[l_k]);
/* 1556 */         p_g.drawPolyline(l_xpos, l_ypos, l_count);
/*      */       }
/* 1558 */       break;
/*      */     default:
/* 1560 */       for (int l_k = 0; l_k < this.m_displayParamCount; l_k++)
/*      */       {
/* 1562 */         int[] l_xpos = new int[l_fin - l_org + 1];
/* 1563 */         int[] l_ypos = new int[l_fin - l_org + 1];
/* 1564 */         int l_count = 0;
/* 1565 */         for (int l_i = l_org; l_i <= l_fin; l_i++)
/*      */         {
/* 1567 */           int l_days = ((this.m_subIndexId == 21) || (this.m_subIndexId == 22)) && (l_k > 0) ? this.m_calcParamValue[l_k] + this.m_calcParamValue[0] : (this.m_subIndexId == 16) && (l_k >= 2) ? this.m_calcParamValue[(l_k - 1)] : ((this.m_subIndexId == 16) && (l_k < 2)) || (this.m_subIndexId == 11) || (this.m_subIndexId == 12) || (this.m_subIndexId == 3) || (this.m_subIndexId == 4) || (this.m_subIndexId == 5) || (this.m_subIndexId == 6) || (this.m_subIndexId == 7) ? this.m_calcParamValue[0] : (this.m_subIndexId == 14) && (l_k == 1) ? this.m_calcParamValue[0] + this.m_calcParamValue[1] : this.m_calcParamValue[l_k];
/*      */ 
/* 1577 */           if (l_i - l_days + 1 < this.m_calcOrgRec)
/*      */             continue;
/* 1579 */           l_xpos[l_count] = this.m_parent.f_rec2pos(l_i);
/* 1580 */           l_ypos[l_count] = f_unit2pos((this.m_subIndexId == 11) && (l_k == 2) ? 3.0D * this.m_storedValue[0][(l_i - l_org)] - 2.0D * this.m_storedValue[1][(l_i - l_org)] : this.m_subIndexId == 4 ? this.m_storedValue[0][(l_i - l_org)] - 2.0D * this.m_storedValue[1][(l_i - l_org)] : l_k == 2 ? this.m_storedValue[0][(l_i - l_org)] - this.m_storedValue[1][(l_i - l_org)] : l_k == 1 ? this.m_storedValue[0][(l_i - l_org)] + this.m_storedValue[1][(l_i - l_org)] : l_k == 0 ? this.m_storedValue[0][(l_i - l_org)] + 2.0D * this.m_storedValue[1][(l_i - l_org)] : this.m_storedValue[l_k][(l_i - l_org)]);
/*      */ 
/* 1589 */           l_count++;
/*      */         }
/* 1591 */         p_g.setColor(CommEnv.s_klineLineColor[l_k]);
/* 1592 */         p_g.drawPolyline(l_xpos, l_ypos, l_count);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void f_paintIndexTitle(Graphics p_g)
/*      */   {
/* 1613 */     p_g.clearRect(this.m_indexTitleSize.x, this.m_indexTitleSize.y, this.m_indexTitleSize.width, this.m_indexTitleSize.height);
/* 1614 */     FontMetrics l_fm = p_g.getFontMetrics();
/* 1615 */     int l_height = l_fm.getAscent() + (14 - (l_fm.getAscent() + l_fm.getDescent())) / 2;
/* 1616 */     int l_org = this.m_parent.f_getOrgRec();
/* 1617 */     int l_fin = this.m_parent.f_getFinRec();
/* 1618 */     int l_titleXPos = this.m_indexTitleSize.x + 2;
/* 1619 */     int l_cursor = this.m_parent.f_getCursorRec();
/* 1620 */     if (l_cursor < 0)
/* 1621 */       l_cursor = l_fin;
/* 1622 */     int l_scale = (this.m_kdoc.f_isBGu()) || (this.m_kdoc.f_isJiJin()) || (this.m_kdoc.f_isQuanzheng()) ? 3 : 2;
/* 1623 */     for (int l_k = 0; l_k < this.m_displayParamCount; l_k++)
/*      */     {
/* 1625 */       int l_days = this.m_subIndexId == 15 ? this.m_calcParamValue[2] : ((this.m_subIndexId == 21) || (this.m_subIndexId == 22)) && (l_k > 0) ? this.m_calcParamValue[l_k] + this.m_calcParamValue[0] : (this.m_subIndexId == 16) && (l_k >= 2) ? this.m_calcParamValue[(l_k - 1)] : ((this.m_subIndexId == 16) && (l_k < 2)) || (this.m_subIndexId == 8) || (this.m_subIndexId == 11) || (this.m_subIndexId == 12) || (this.m_subIndexId == 3) || (this.m_subIndexId == 4) || (this.m_subIndexId == 5) || (this.m_subIndexId == 6) || (this.m_subIndexId == 7) ? this.m_calcParamValue[0] : (this.m_subIndexId == 14) && (l_k == 1) ? this.m_calcParamValue[0] + this.m_calcParamValue[1] : this.m_calcParamValue[l_k];
/*      */ 
/* 1637 */       if (l_cursor - l_days + 1 < this.m_calcOrgRec)
/*      */         continue;
/* 1639 */       double l_value = (this.m_subIndexId == 15) && (l_k > 0) ? this.m_storedValue[(l_k - 1)][(l_cursor - l_org)] : (this.m_subIndexId == 15) && (l_k == 0) ? 2.0D * (this.m_storedValue[0][(l_cursor - l_org)] - this.m_storedValue[1][(l_cursor - l_org)]) : (this.m_subIndexId == 11) && (l_k == 2) ? 3.0D * this.m_storedValue[0][(l_cursor - l_org)] - 2.0D * this.m_storedValue[1][(l_cursor - l_org)] : this.m_subIndexId == 4 ? this.m_storedValue[0][(l_cursor - l_org)] - 2.0D * this.m_storedValue[1][(l_cursor - l_org)] : l_k == 2 ? this.m_storedValue[0][(l_cursor - l_org)] - this.m_storedValue[1][(l_cursor - l_org)] : l_k == 1 ? this.m_storedValue[0][(l_cursor - l_org)] + this.m_storedValue[1][(l_cursor - l_org)] : l_k == 0 ? this.m_storedValue[0][(l_cursor - l_org)] + 2.0D * this.m_storedValue[1][(l_cursor - l_org)] : this.m_storedValue[l_k][(l_cursor - l_org)];
/*      */ 
/* 1652 */       int l_dec_scale = (this.m_subIndexId == 11) || (this.m_subIndexId == 12) || (this.m_subIndexId == 15) || (this.m_subIndexId == 17) || (this.m_subIndexId == 18) || (this.m_subIndexId == 19) || (this.m_subIndexId == 13) || (this.m_subIndexId == 14) || (this.m_subIndexId == 16) || (this.m_subIndexId == 20) || (this.m_subIndexId == 21) || (this.m_subIndexId == 22) ? 2 : (this.m_mainIndexId == 3) || ((this.m_subIndexId == 8) && (l_k == 1)) ? 0 : l_scale;
/*      */ 
/* 1661 */       String l_show = f_getDisplayParam(l_k) + CommEnv.f_scaleDouble(l_value, l_dec_scale);
/* 1662 */       p_g.setColor((this.m_subIndexId == 15) && (l_k > 0) ? CommEnv.s_klineLineColor[(l_k - 1)] : (this.m_subIndexId == 15) && (l_k == 0) ? CommEnv.s_klineDownColor : l_value >= 0.0D ? CommEnv.s_klineUpColor : CommEnv.s_klineLineColor[l_k]);
/*      */ 
/* 1667 */       p_g.drawString(l_show, l_titleXPos, l_height);
/* 1668 */       l_titleXPos += l_fm.stringWidth(l_show) + 8;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void f_recalcIndex()
/*      */   {
/*  385 */     int l_org = this.m_parent.f_getOrgRec();
/*  386 */     int l_fin = this.m_parent.f_getFinRec();
/*  387 */     if ((l_org < 0) || (l_fin < 0))
/*  388 */       return;
/*  389 */     int l_dec = this.m_kdoc.f_isZhaiQuan() ? 10 : 100;
/*  390 */     int l_validRec = this.m_kdoc.f_getValidRecPos();
/*  391 */     switch (this.m_subIndexId)
/*      */     {
/*      */     case 0:
/*      */     case 1:
/*      */     case 10:
/*  396 */       this.m_storedValue = new double[this.m_calcParamCount][l_fin - l_org + 1];
/*  397 */       for (int l_k = 0; l_k < this.m_calcParamCount; l_k++)
/*      */       {
/*  399 */         for (int l_i = l_org; l_i <= l_fin; l_i++)
/*      */         {
/*  401 */           if (l_i - this.m_calcParamValue[l_k] + 1 < this.m_calcOrgRec)
/*      */             continue;
/*  403 */           for (int l_j = l_i; l_j >= l_i - this.m_calcParamValue[l_k] + 1; l_j--)
/*      */           {
/*  405 */             KLineRec l_rec = this.m_kdoc.f_getRec(l_j);
/*  406 */             this.m_storedValue[l_k][(l_i - l_org)] += (this.m_subIndexId == 1 ? l_rec.m_close * (l_j - l_i + this.m_calcParamValue[l_k]) : this.m_mainIndexId == 4 ? l_rec.m_money / 10000.0D : this.m_mainIndexId == 3 ? l_rec.m_volume / l_dec : l_rec.m_close);
/*      */           }
/*      */ 
/*  412 */           this.m_storedValue[l_k][(l_i - l_org)] /= this.m_calcParamValue[l_k];
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  417 */       break;
/*      */     case 2:
/*  419 */       this.m_storedValue = new double[this.m_calcParamCount][l_fin - l_org + 1];
/*  420 */       for (int l_k = 0; l_k < this.m_calcParamCount; l_k++)
/*      */       {
/*  422 */         int l_beg = Math.max(l_validRec, l_org - 100);
/*  423 */         double[] l_ema = new double[this.m_calcFinRec - l_beg + 1];
/*  424 */         KLineRec l_rec = this.m_kdoc.f_getRec(l_beg);
/*  425 */         l_ema[0] = l_rec.m_close;
/*  426 */         for (int l_i = l_beg + 1; l_i <= l_fin; l_i++)
/*      */         {
/*  428 */           l_rec = this.m_kdoc.f_getRec(l_i);
/*  429 */           l_ema[(l_i - l_beg)] = (l_ema[(l_i - l_beg - 1)] + (l_rec.m_close - l_ema[(l_i - l_beg - 1)]) * 2.0D / (this.m_calcParamValue[l_k] + 1));
/*      */         }
/*      */ 
/*  432 */         for (int l_i = l_org; l_i <= l_fin; l_i++)
/*  433 */           this.m_storedValue[l_k][(l_i - l_org)] = l_ema[(l_i - l_beg)];
/*      */       }
/*  435 */       break;
/*      */     case 8:
/*  437 */       this.m_storedValue = new double[2][l_fin - l_org + 1];
/*  438 */       int l_sarBeg = Math.max(l_validRec, l_org - 50);
/*  439 */       double l_af = this.m_calcParamValue[1] / 100.0D;
/*  440 */       double l_maxaf = this.m_calcParamValue[2] / 100.0D;
/*  441 */       double l_startaf = 0.04D;
/*  442 */       double[] l_sar = new double[this.m_calcFinRec - l_sarBeg + 1];
/*  443 */       double[] l_fall = new double[this.m_calcFinRec - l_sarBeg + 1];
/*  444 */       double l_avet = 0.0D; double l_avey = 0.0D;
/*  445 */       int l_days = this.m_calcParamValue[0] + 1;
/*  446 */       for (int l_i = 0; l_i < l_days; l_i++)
/*      */       {
/*  448 */         KLineRec l_rec = this.m_kdoc.f_getRec(Math.max(l_validRec, l_sarBeg - l_i));
/*  449 */         l_avet += l_rec.m_close;
/*  450 */         l_rec = this.m_kdoc.f_getRec(Math.max(l_validRec, l_sarBeg - l_i - 1));
/*  451 */         l_avey += l_rec.m_close;
/*      */       }
/*      */ 
/*  454 */       int l_isFall = l_avet >= l_avey ? 0 : 1;
/*  455 */       if (l_isFall > 0)
/*      */       {
/*  457 */         double l_high = 0.0D;
/*  458 */         for (int l_i = 0; l_i < l_days; l_i++)
/*      */         {
/*  460 */           KLineRec l_rec = this.m_kdoc.f_getRec(Math.max(l_validRec, l_sarBeg - l_i));
/*  461 */           if (l_rec.m_high > l_high)
/*  462 */             l_high = l_rec.m_high;
/*      */         }
/*  464 */         l_sar[0] = l_high;
/*  465 */         l_fall[0] = 1.0D;
/*      */       }
/*      */       else
/*      */       {
/*  469 */         double l_low = 1.7976931348623157E+308D;
/*  470 */         for (int l_i = 0; l_i < l_days; l_i++)
/*      */         {
/*  472 */           KLineRec l_rec = this.m_kdoc.f_getRec(Math.max(l_validRec, l_sarBeg - l_i));
/*  473 */           if (l_rec.m_low < l_low)
/*  474 */             l_low = l_rec.m_low;
/*      */         }
/*  476 */         l_sar[0] = l_low;
/*  477 */         l_fall[0] = 0.0D;
/*      */       }
/*  479 */       double l_afc = l_startaf;
/*  480 */       for (int l_i = 1; l_i < l_fin - l_sarBeg + 1; l_i++)
/*      */       {
/*      */         KLineRec l_rec;
/*  483 */         if (l_isFall > 0)
/*      */         {
/*  485 */           l_rec = this.m_kdoc.f_getRec(l_sarBeg + l_i);
/*  486 */           l_sar[l_i] = (l_sar[(l_i - 1)] - l_afc * (l_sar[(l_i - 1)] - l_rec.m_low));
/*      */ 
/*  488 */           if (l_sar[l_i] < l_rec.m_low)
/*      */           {
/*      */             int tmp1040_1039 = 0; l_isFall = tmp1040_1039; l_fall[l_i] = tmp1040_1039;
/*      */ 
/*  492 */             double l_low = 1.7976931348623157E+308D;
/*  493 */             for (int l_j = 0; l_j < l_days; l_j++)
/*      */             {
/*  495 */               l_rec = this.m_kdoc.f_getRec(Math.max(l_sarBeg + l_i - l_j, l_validRec));
/*  496 */               if (l_rec.m_low < l_low)
/*  497 */                 l_low = l_rec.m_low;
/*      */             }
/*  499 */             l_sar[l_i] = l_low;
/*  500 */             l_afc = l_startaf;
/*  501 */             continue;
/*      */           }
/*      */ 
/*  505 */           l_fall[l_i] = l_isFall;
/*      */ 
/*  507 */           l_rec = this.m_kdoc.f_getRec(l_sarBeg + l_i);
/*  508 */           if (l_sar[l_i] < l_rec.m_high) {
/*  509 */             l_fall[l_i] = 2.0D;
/*      */           }
/*      */ 
/*  512 */           boolean l_isLow = true;
/*  513 */           l_rec = this.m_kdoc.f_getRec(l_sarBeg + l_i);
/*  514 */           for (int l_j = 1; l_j < l_days - 1; l_j++)
/*      */           {
/*  516 */             KLineRec l_prev = this.m_kdoc.f_getRec(Math.max(l_sarBeg + l_i - l_j, l_validRec));
/*  517 */             if (l_prev.m_low > l_rec.m_low)
/*      */               continue;
/*  519 */             l_isLow = false;
/*  520 */             break;
/*      */           }
/*      */ 
/*  523 */           if (l_isLow)
/*  524 */             l_afc += l_af;
/*  525 */           if (l_afc > l_maxaf)
/*  526 */             l_afc = l_maxaf;
/*      */         }
/*      */         else
/*      */         {
/*  530 */           l_rec = this.m_kdoc.f_getRec(l_sarBeg + l_i - 1);
/*  531 */           l_sar[l_i] = (l_sar[(l_i - 1)] + l_afc * (l_rec.m_high - l_sar[(l_i - 1)]));
/*      */ 
/*  533 */           l_rec = this.m_kdoc.f_getRec(l_sarBeg + l_i);
/*  534 */           if (l_sar[l_i] > l_rec.m_high)
/*      */           {
/*  536 */             l_isFall = 1;
/*  537 */             l_fall[l_i] = l_isFall;
/*      */ 
/*  539 */             double l_high = 0.0D;
/*  540 */             for (int l_j = 0; l_j < l_days; l_j++)
/*      */             {
/*  542 */               l_rec = this.m_kdoc.f_getRec(Math.max(l_sarBeg + l_i - l_j, l_validRec));
/*  543 */               if (l_rec.m_high > l_high)
/*  544 */                 l_high = l_rec.m_high;
/*      */             }
/*  546 */             l_sar[l_i] = l_high;
/*  547 */             l_afc = l_startaf;
/*  548 */             continue;
/*      */           }
/*      */ 
/*  552 */           l_fall[l_i] = l_isFall;
/*  553 */           l_rec = this.m_kdoc.f_getRec(l_sarBeg + l_i);
/*  554 */           if (l_sar[l_i] > l_rec.m_low) {
/*  555 */             l_fall[l_i] = 2.0D;
/*      */           }
/*      */ 
/*  558 */           boolean l_isHigh = true;
/*  559 */           l_rec = this.m_kdoc.f_getRec(l_sarBeg + l_i);
/*  560 */           for (int l_j = 1; l_j < l_days - 1; l_j++)
/*      */           {
/*  562 */             KLineRec l_prev = this.m_kdoc.f_getRec(Math.max(l_sarBeg + l_i - l_j, l_validRec));
/*  563 */             if (l_prev.m_high < l_rec.m_high)
/*      */               continue;
/*  565 */             l_isHigh = false;
/*  566 */             break;
/*      */           }
/*      */ 
/*  569 */           if (l_isHigh)
/*  570 */             l_afc += l_af;
/*  571 */           if (l_afc > l_maxaf)
/*  572 */             l_afc = l_maxaf;
/*      */         }
/*      */       }
/*  575 */       for (int l_i = l_org; l_i <= l_fin; l_i++)
/*      */       {
/*  577 */         this.m_storedValue[0][(l_i - l_org)] = l_sar[(l_i - l_sarBeg)];
/*  578 */         this.m_storedValue[1][(l_i - l_org)] = l_fall[(l_i - l_sarBeg)];
/*      */       }
/*  580 */       break;
/*      */     case 4:
/*  582 */       this.m_storedValue = new double[2][l_fin - l_org + 1];
/*  583 */       for (int l_i = l_org; l_i <= l_fin; l_i++)
/*      */       {
/*  585 */         if (l_i - this.m_calcParamValue[0] + 1 < this.m_calcOrgRec)
/*      */           continue;
/*  587 */         for (int l_j = l_i; l_j >= l_i - this.m_calcParamValue[0] + 1; l_j--)
/*      */         {
/*  589 */           KLineRec l_rec = this.m_kdoc.f_getRec(l_j);
/*  590 */           this.m_storedValue[0][(l_i - l_org)] += l_rec.m_close;
/*      */         }
/*  592 */         this.m_storedValue[0][(l_i - l_org)] /= this.m_calcParamValue[0];
/*  593 */         for (int l_j = l_i; l_j >= l_i - this.m_calcParamValue[0] + 1; l_j--)
/*      */         {
/*  595 */           KLineRec l_rec = this.m_kdoc.f_getRec(l_j);
/*  596 */           this.m_storedValue[1][(l_i - l_org)] += (l_rec.m_close - this.m_storedValue[0][(l_i - l_org)]) * (l_rec.m_close - this.m_storedValue[0][(l_i - l_org)]);
/*      */         }
/*      */ 
/*  599 */         this.m_storedValue[1][(l_i - l_org)] /= this.m_calcParamValue[0];
/*  600 */         this.m_storedValue[1][(l_i - l_org)] = Math.sqrt(this.m_storedValue[1][(l_i - l_org)]);
/*      */       }
/*  602 */       break;
/*      */     case 3:
/*  604 */       this.m_storedValue = new double[this.m_displayParamCount][l_fin - l_org + 1];
/*  605 */       for (int l_i = l_org; l_i <= l_fin; l_i++)
/*      */       {
/*  607 */         if (l_i - this.m_calcParamValue[0] + 1 < this.m_calcOrgRec)
/*      */           continue;
/*  609 */         double l_value = 0.0D;
/*  610 */         for (int l_j = l_i; l_j >= l_i - this.m_calcParamValue[0] + 1; l_j--)
/*      */         {
/*  612 */           KLineRec l_rec = this.m_kdoc.f_getRec(l_j);
/*  613 */           l_value += l_rec.m_close;
/*      */         }
/*  615 */         this.m_storedValue[0][(l_i - l_org)] = (l_value / this.m_calcParamValue[0]);
/*  616 */         this.m_storedValue[1][(l_i - l_org)] = ((l_value + l_value * this.m_calcParamValue[1] / 100.0D) / this.m_calcParamValue[0]);
/*  617 */         this.m_storedValue[2][(l_i - l_org)] = ((l_value - l_value * this.m_calcParamValue[1] / 100.0D) / this.m_calcParamValue[0]);
/*      */       }
/*  619 */       break;
/*      */     case 5:
/*      */     case 6:
/*      */     case 7:
/*      */     case 19:
/*  624 */       this.m_storedValue = new double[this.m_displayParamCount][l_fin - l_org + 1];
/*  625 */       for (int l_i = l_org; l_i <= l_fin; l_i++)
/*      */       {
/*  627 */         if (l_i - this.m_calcParamValue[0] + 1 < this.m_calcOrgRec)
/*      */           continue;
/*  629 */         double l_high = 0.0D;
/*  630 */         double l_low = 1.7976931348623157E+308D;
/*      */ 
/*  632 */         for (int l_j = l_i; l_j >= l_i - this.m_calcParamValue[0] + 1; l_j--)
/*      */         {
/*  634 */          KLineRec l_rec = this.m_kdoc.f_getRec(l_j);
/*  635 */           if (l_rec.m_low < l_low)
/*  636 */             l_low = l_rec.m_low;
/*  637 */           if (l_rec.m_high > l_high)
/*  638 */             l_high = l_rec.m_high;
/*      */         }
/*  640 */         KLineRec l_rec = this.m_kdoc.f_getRec(l_i);
/*  641 */         switch (this.m_subIndexId)
/*      */         {
/*      */         case 5:
/*  644 */           this.m_storedValue[0][(l_i - l_org)] = (2.0D * (l_rec.m_high + l_rec.m_low + l_rec.m_close) / 3.0D - l_low);
/*  645 */           this.m_storedValue[1][(l_i - l_org)] = (2.0D * (l_rec.m_high + l_rec.m_low + l_rec.m_close) / 3.0D - l_high);
/*  646 */           break;
/*      */         case 6:
/*  648 */           this.m_storedValue[0][(l_i - l_org)] = ((l_rec.m_high + l_rec.m_low + l_rec.m_close) / 3.0D + (l_high - l_low));
/*  649 */           this.m_storedValue[1][(l_i - l_org)] = ((l_rec.m_high + l_rec.m_low + l_rec.m_close) / 3.0D - (l_high - l_low));
/*  650 */           break;
/*      */         case 7:
/*  652 */           this.m_storedValue[0][(l_i - l_org)] = (2.0D * l_high - l_low);
/*  653 */           this.m_storedValue[1][(l_i - l_org)] = (2.0D * l_low - l_high);
/*  654 */           break;
/*      */         case 19:
/*  656 */           if (l_high == l_low)
/*  657 */             l_high = l_low + 1.0D;
/*  658 */           this.m_storedValue[0][(l_i - l_org)] = (100.0D - (l_high - l_rec.m_close) / (l_high - l_low) * 100.0D);
/*      */         }
/*      */       }
/*      */ 
/*  662 */       break;
/*      */     case 9:
/*  664 */       int l_count = (this.m_frameSize.height - 2) / 12;
/*  665 */       this.m_storedValue = new double[2][l_count];
/*  666 */       double l_max = 0.0D;
/*  667 */       double l_min = 1.7976931348623157E+308D;
/*  668 */       for (int l_i = l_org; l_i <= l_fin; l_i++)
/*      */       {
/*  670 */         KLineRec l_rec = this.m_kdoc.f_getRec(l_i);
/*  671 */         if (l_rec.m_low < l_min)
/*  672 */           l_min = l_rec.m_low;
/*  673 */         if (l_rec.m_high > l_max)
/*  674 */           l_max = l_rec.m_high;
/*      */       }
/*  676 */       double l_delta = (l_max - l_min) / l_count;
/*  677 */       for (int l_i = l_org; l_i <= l_fin; l_i++)
/*      */       {
/*  679 */         KLineRec l_rec = this.m_kdoc.f_getRec(l_i);
/*  680 */         double l_ave = (l_rec.m_high + l_rec.m_low + l_rec.m_close) / 3.0D;
/*  681 */         int l_pos = (int)Math.floor((l_ave - l_min) / l_delta);
/*  682 */         if (l_pos > l_count)
/*  683 */           l_pos = l_count - 1;
/*  684 */         this.m_storedValue[0][l_pos] += l_rec.m_volume / l_dec;
/*      */       }
/*  686 */       double l_maxVol = 0.0D;
/*  687 */       for (int l_i = 0; l_i < l_count; l_i++)
/*      */       {
/*  689 */         if (this.m_storedValue[0][l_i] > l_maxVol)
/*  690 */           l_maxVol = this.m_storedValue[0][l_i];
/*      */       }
/*  692 */       for (int l_i = 0; l_i < l_count; l_i++)
/*  693 */         this.m_storedValue[1][l_i] = (this.m_storedValue[0][l_i] / l_maxVol);
/*  694 */       break;
/*      */     case 11:
/*      */     case 12:
/*  697 */       this.m_storedValue = new double[2][l_fin - l_org + 1];
/*  698 */       if (this.m_calcParamValue[1] < 3)
/*  699 */         this.m_calcParamValue[1] = 3;
/*  700 */       if (this.m_calcParamValue[2] < 3)
/*  701 */         this.m_calcParamValue[2] = 3;
/*  702 */       double ky = 50.0D; double dy = 50.0D;
/*  703 */       double[] l_kvalue = new double[this.m_calcFinRec - this.m_calcOrgRec + 1];
/*  704 */       double[] l_dvalue = new double[this.m_calcFinRec - this.m_calcOrgRec + 1];
/*  705 */       for (int l_i = this.m_calcOrgRec; l_i <= this.m_calcFinRec; l_i++)
/*      */       {
/*  708 */         double l_low = 1.7976931348623157E+308D;
/*  709 */         double l_high = 0.0D;
/*  710 */         for (int l_j = 0; l_j < this.m_calcParamValue[0]; l_j++)
/*      */         {
/*  712 */           if (l_i - l_j < l_validRec)
/*      */             continue;
/*  714 */          KLineRec  l_rec = this.m_kdoc.f_getRec(l_i - l_j);
/*  715 */           if (l_low > l_rec.m_low)
/*  716 */             l_low = l_rec.m_low;
/*  717 */           if (l_high < l_rec.m_high)
/*  718 */             l_high = l_rec.m_high;
/*      */         }
/*  720 */         if (l_low >= l_high)
/*  721 */           l_high = l_low + 1.0D;
/*  722 */         KLineRec l_rec = this.m_kdoc.f_getRec(l_i);
/*  723 */         double RSV = (l_rec.m_close - l_low) / (l_high - l_low) * 100.0D;
/*      */         double tmp3119_3118 = ((ky * (this.m_calcParamValue[1] - 1) + RSV) / this.m_calcParamValue[1]); ky = tmp3119_3118; l_kvalue[(l_i - this.m_calcOrgRec)] = tmp3119_3118;
/*      */         double tmp3155_3154 = ((dy * (this.m_calcParamValue[2] - 1) + ky) / this.m_calcParamValue[2]); dy = tmp3155_3154; l_dvalue[(l_i - this.m_calcOrgRec)] = tmp3155_3154;
/*  726 */         if (l_i - l_org < 0)
/*      */           continue;
/*  728 */         if (this.m_subIndexId == 11)
/*      */         {
/*  730 */           this.m_storedValue[0][(l_i - l_org)] = l_kvalue[(l_i - this.m_calcOrgRec)];
/*  731 */           this.m_storedValue[1][(l_i - l_org)] = l_dvalue[(l_i - this.m_calcOrgRec)];
/*      */         }
/*      */         else
/*      */         {
/*  735 */           this.m_storedValue[0][(l_i - l_org)] = l_dvalue[(l_i - this.m_calcOrgRec)];
/*  736 */           this.m_storedValue[1][(l_i - l_org)] = 0.0D;
/*  737 */           for (int l_j = 0; l_j < this.m_calcParamValue[3]; l_j++)
/*      */           {
/*  739 */             int l_pos = l_i - l_j;
/*  740 */             if (l_pos < this.m_calcOrgRec)
/*  741 */               l_pos = this.m_calcOrgRec;
/*  742 */             this.m_storedValue[1][(l_i - l_org)] += l_dvalue[(l_pos - this.m_calcOrgRec)];
/*      */           }
/*  744 */           this.m_storedValue[1][(l_i - l_org)] /= this.m_calcParamValue[3];
/*      */         }
/*      */       }
/*  747 */       break;
/*      */     case 15:
/*  749 */       this.m_storedValue = new double[2][l_fin - l_org + 1];
/*  750 */       int l_macdBeg = Math.max(l_validRec, l_org - 50 + 1);
/*  751 */       double[] l_dif = new double[this.m_calcFinRec - l_macdBeg + 1];
/*  752 */       double[] l_dest = new double[this.m_calcFinRec - l_macdBeg + 1];
/*  753 */       double l_fast = 2.0D / (1 + this.m_calcParamValue[0]);
/*  754 */       double l_slow = 2.0D / (1 + this.m_calcParamValue[1]);
/*  755 */       if (this.m_calcParamValue[2] <= 0)
/*  756 */         this.m_calcParamValue[2] = 5;
/*  757 */       int l_p = Math.max(l_validRec, l_macdBeg - this.m_calcParamValue[2] + 1);
/*  758 */       KLineRec l_krec = this.m_kdoc.f_getRec(l_p);
/*  759 */       double l_prevFast = l_krec.m_close;
/*  760 */       double l_prevSlow = l_krec.m_close;
/*  761 */       double l_macd = 0.0D;
/*  762 */       for (int l_i = l_p + 1; l_i <= l_macdBeg; l_i++)
/*      */       {
/*  764 */         l_krec = this.m_kdoc.f_getRec(l_i);
/*  765 */         l_prevFast += l_fast * (l_krec.m_close - l_prevFast);
/*  766 */         l_prevSlow += l_slow * (l_krec.m_close - l_prevSlow);
/*  767 */         l_macd += l_prevFast - l_prevSlow;
/*      */       }
/*  769 */       l_dif[0] = (l_prevFast - l_prevSlow);
/*  770 */       l_dest[0] = (l_macdBeg - l_p + 1 > 0 ? l_macd / (l_macdBeg - l_p + 1) : 0.0D);
/*  771 */       for (int l_i = 1; l_i < this.m_calcFinRec - l_macdBeg + 1; l_i++)
/*      */       {
/*  773 */         l_krec = this.m_kdoc.f_getRec(l_i + l_macdBeg);
/*  774 */         l_prevFast += l_fast * (l_krec.m_close - l_prevFast);
/*  775 */         l_prevSlow += l_slow * (l_krec.m_close - l_prevSlow);
/*  776 */         l_dif[l_i] = (l_prevFast - l_prevSlow);
/*  777 */         l_dest[l_i] = (l_dest[(l_i - 1)] + (l_dif[l_i] - l_dest[(l_i - 1)]) * 2.0D / (1 + this.m_calcParamValue[2]));
/*      */       }
/*  779 */       for (int l_i = l_org; l_i <= l_fin; l_i++)
/*      */       {
/*  781 */         this.m_storedValue[0][(l_i - l_org)] = l_dif[(l_i - l_macdBeg)];
/*  782 */         this.m_storedValue[1][(l_i - l_org)] = l_dest[(l_i - l_macdBeg)];
/*      */       }
/*  784 */       break;
/*      */     case 17:
/*      */     case 18:
/*  787 */       this.m_storedValue = new double[this.m_calcParamCount][l_fin - l_org + 1];
/*  788 */       int l_rsiBeg = Math.max(l_validRec, l_org - 50 + 1);
/*  789 */       int[] l_pos = new int[this.m_calcParamCount];
/*  790 */       double[] l_up = new double[this.m_calcParamCount];
/*  791 */       double[] l_down = new double[this.m_calcParamCount];
/*  792 */       for (int l_k = 0; l_k < this.m_calcParamCount; l_k++)
/*      */       {
/*  794 */         l_pos[l_k] = l_validRec;
/*  795 */         l_up[l_k] = 50.0D;
/*  796 */         l_down[l_k] = 50.0D;
/*  797 */         int l_cur = l_rsiBeg;
/*  798 */         int l_beg = Math.max(l_rsiBeg - this.m_calcParamValue[l_k], l_validRec);
/*  799 */         if (l_cur - l_beg < this.m_calcParamValue[l_k])
/*  800 */           l_cur = l_beg + this.m_calcParamValue[l_k];
/*  801 */         if (l_cur > this.m_calcFinRec)
/*      */           continue;
/*  803 */         l_pos[l_k] = l_cur;
/*      */         double tmp3954_3953 = 0.0D; l_down[l_k] = tmp3954_3953; l_up[l_k] = tmp3954_3953;
/*  805 */         for (int l_i = l_cur; l_i > l_beg; l_i--)
/*      */         {
/*  807 */           KLineRec l_yesterday = this.m_kdoc.f_getRec(Math.max(l_i - 1, l_validRec));
/*  808 */           KLineRec l_today = this.m_kdoc.f_getRec(l_i);
/*  809 */           if (this.m_subIndexId == 17)
/*      */           {
/*  811 */             if (l_today.m_close > l_yesterday.m_close)
/*  812 */               l_up[l_k] += l_today.m_close - l_yesterday.m_close;
/*  813 */             else if (l_today.m_close < l_yesterday.m_close) {
/*  814 */               l_down[l_k] += -l_today.m_close + l_yesterday.m_close;
/*      */             }
/*      */ 
/*      */           }
/*  818 */           else if (l_today.m_volume > l_yesterday.m_volume)
/*  819 */             l_up[l_k] += l_today.m_volume - l_yesterday.m_volume;
/*  820 */           else if (l_today.m_volume < l_yesterday.m_volume) {
/*  821 */             l_down[l_k] += -l_today.m_volume + l_yesterday.m_volume;
/*      */           }
/*      */         }
/*      */       }
/*  825 */       for (int l_i = l_org; l_i <= l_fin; l_i++)
/*      */       {
/*  828 */         for (int l_k = 0; l_k < this.m_calcParamCount; l_k++)
/*      */         {
/*  830 */           int l_npos = l_pos[l_k];
/*  831 */           for (int l_j = l_npos; l_j <= l_i; l_j++)
/*      */           {
/*  833 */             KLineRec l_yesterday = this.m_kdoc.f_getRec(Math.max(l_j - 1, l_validRec));
/*  834 */             KLineRec l_today = this.m_kdoc.f_getRec(l_j);
/*  835 */             double l_todayUp = this.m_subIndexId == 17 ? l_today.m_close - l_yesterday.m_close : l_today.m_volume - l_yesterday.m_volume;
/*      */ 
/*  838 */             double l_todayDown = -l_todayUp;
/*  839 */             if (l_todayUp < 0.0D)
/*  840 */               l_todayUp = 0.0D;
/*  841 */             if (l_todayDown < 0.0D)
/*  842 */               l_todayDown = 0.0D;
/*  843 */             l_up[l_k] = (l_up[l_k] * (this.m_calcParamValue[l_k] - 1) / this.m_calcParamValue[l_k] + l_todayUp);
/*  844 */             l_down[l_k] = (l_down[l_k] * (this.m_calcParamValue[l_k] - 1) / this.m_calcParamValue[l_k] + l_todayDown);
/*  845 */             l_pos[l_k] = l_i;
/*      */           }
/*  847 */           if (Math.abs(l_up[l_k] + l_down[l_k]) <= 1.0E-007D)
/*  848 */             this.m_storedValue[l_k][(l_i - l_org)] = 50.0D;
/*      */           else
/*  850 */             this.m_storedValue[l_k][(l_i - l_org)] = (100.0D * l_up[l_k] / (l_up[l_k] + l_down[l_k]));
/*      */         }
/*      */       }
/*  853 */       break;
/*      */     case 13:
/*  855 */       this.m_storedValue = new double[this.m_calcParamCount][l_fin - l_org + 1];
/*  856 */       for (int l_i = l_org; l_i <= l_fin; l_i++)
/*      */       {
/*  859 */         double l_ave = 0.0D;
/*  860 */         for (int l_j = 0; l_j < this.m_calcParamValue[0]; l_j++)
/*      */         {
/*  862 */           KLineRec l_rec = this.m_kdoc.f_getRec(Math.max(l_i - l_j, l_validRec));
/*  863 */           l_ave += (l_rec.m_high + l_rec.m_low + l_rec.m_close) / 3.0D;
/*      */         }
/*  865 */         l_ave /= this.m_calcParamValue[0];
/*  866 */         double l_md = 0.0D;
/*  867 */         for (int l_j = 0; l_j < this.m_calcParamValue[0]; l_j++)
/*      */         {
/*  869 */           KLineRec l_rec = this.m_kdoc.f_getRec(Math.max(l_i - l_j, l_validRec));
/*  870 */           l_md += Math.abs((l_rec.m_high + l_rec.m_low + l_rec.m_close) / 3.0D - l_ave);
/*      */         }
/*  872 */         l_md /= this.m_calcParamValue[0];
/*  873 */         KLineRec l_rec = this.m_kdoc.f_getRec(l_i);
/*  874 */         this.m_storedValue[0][(l_i - l_org)] = (l_md <= 1.0E-007D ? 0.0D : ((l_rec.m_high + l_rec.m_low + l_rec.m_close) / 3.0D - l_ave) / (this.m_calcParamValue[1] / 100.0D) / l_md * 10.0D);
/*      */       }
/*      */ 
/*  878 */       break;
/*      */     case 14:
/*  880 */       this.m_storedValue = new double[this.m_calcParamCount][l_fin - l_org + 1];
/*  881 */       int l_rocBeg = Math.max(l_validRec, l_org - this.m_calcParamValue[1] + 1);
/*  882 */       double[] l_roc = new double[l_fin - l_rocBeg + 1];
/*  883 */       for (int l_i = l_rocBeg; l_i <= l_fin; l_i++)
/*      */       {
/*  885 */         if (l_i - this.m_calcParamValue[0] + 1 < this.m_calcOrgRec)
/*      */           continue;
/*  887 */         KLineRec l_rec = this.m_kdoc.f_getRec(l_i);
/*  888 */         KLineRec l_prev = this.m_kdoc.f_getRec(l_i - this.m_calcParamValue[0] + 1);
/*  889 */         l_roc[(l_i - l_rocBeg)] = (Math.abs(l_prev.m_close) < 1.0E-007D ? 0.0D : (l_rec.m_close - l_prev.m_close) / l_prev.m_close * 100.0D);
/*      */       }
/*      */ 
/*  893 */       for (int l_i = l_org; l_i <= l_fin; l_i++)
/*      */       {
/*  895 */         this.m_storedValue[0][(l_i - l_org)] = l_roc[(l_i - l_rocBeg)];
/*  896 */         if (l_i - this.m_calcParamValue[1] + 1 < this.m_calcOrgRec)
/*      */           continue;
/*  898 */         for (int l_j = 0; l_j < this.m_calcParamValue[1]; l_j++)
/*  899 */           this.m_storedValue[1][(l_i - l_org)] += l_roc[(l_i - l_rocBeg - l_j)];
/*  900 */         this.m_storedValue[1][(l_i - l_org)] /= this.m_calcParamValue[1];
/*      */       }
/*  902 */       break;
/*      */     case 16:
/*  904 */       this.m_storedValue = new double[this.m_displayParamCount][l_fin - l_org + 1];
/*  905 */       int l_dmiBeg = Math.max(l_validRec, l_org - 50 + 1);
/*  906 */       double[] l_pdm = new double[l_fin - l_dmiBeg + 1];
/*  907 */       double[] l_ndm = new double[l_fin - l_dmiBeg + 1];
/*  908 */       double[] l_tr = new double[l_fin - l_dmiBeg + 1];
/*  909 */       double[] l_pdi = new double[l_fin - l_dmiBeg + 1];
/*  910 */       double[] l_ndi = new double[l_fin - l_dmiBeg + 1];
/*  911 */       double[] l_adx = new double[l_fin - l_dmiBeg + 1];
/*  912 */       double[] l_adxr = new double[l_fin - l_dmiBeg + 1];
/*      */ 
/*  914 */       for (int l_i = 0; l_i < l_fin - l_dmiBeg + 1; l_i++)
/*      */       {
/*      */         KLineRec l_today;
/*      */         KLineRec l_yesterday;
/*  917 */         if (l_i < this.m_calcParamValue[0])
/*      */         {
/*  920 */           for (int l_j = 1; l_j < this.m_calcParamValue[0]; l_j++)
/*      */           {
/*  922 */             l_today = this.m_kdoc.f_getRec(Math.max(l_dmiBeg + l_i - l_j, l_validRec));
/*  923 */             l_yesterday = this.m_kdoc.f_getRec(Math.max(l_dmiBeg + l_i - l_j - 1, l_validRec));
/*  924 */             if ((l_today.m_high > l_yesterday.m_high) && (l_today.m_high - l_yesterday.m_high > l_yesterday.m_low - l_today.m_low))
/*      */             {
/*  926 */               l_pdm[l_i] += l_today.m_high - l_yesterday.m_high;
/*  927 */             }if ((l_yesterday.m_low > l_today.m_low) && (l_yesterday.m_low - l_today.m_low > l_today.m_high - l_yesterday.m_high))
/*      */             {
/*  929 */               l_ndm[l_i] += l_yesterday.m_low - l_today.m_low;
/*  930 */             }double tr = l_today.m_high - l_today.m_low;
/*  931 */             if (tr < Math.abs(l_today.m_high - l_yesterday.m_close))
/*  932 */               tr = Math.abs(l_today.m_high - l_yesterday.m_close);
/*  933 */             if (tr < Math.abs(l_today.m_low - l_yesterday.m_close))
/*  934 */               tr = Math.abs(l_today.m_low - l_yesterday.m_close);
/*  935 */             l_tr[l_i] += tr;
/*      */           }
/*      */ 
/*  938 */           l_today = this.m_kdoc.f_getRec(Math.max(l_dmiBeg + l_i, l_validRec));
/*  939 */           l_yesterday = this.m_kdoc.f_getRec(Math.max(l_dmiBeg + l_i - 1, l_validRec));
/*  940 */           double l_vpdm = (l_today.m_high > l_yesterday.m_high) && (l_today.m_high - l_yesterday.m_high > l_yesterday.m_low - l_today.m_low) ? l_today.m_high - l_yesterday.m_high : 0.0D;
/*      */ 
/*  943 */           double l_vndm = (l_yesterday.m_low > l_today.m_low) && (l_yesterday.m_low - l_today.m_low > l_today.m_high - l_yesterday.m_high) ? l_yesterday.m_low - l_today.m_low : 0.0D;
/*      */ 
/*  946 */           double l_vtr = l_today.m_high - l_today.m_low;
/*  947 */           if (l_vtr < Math.abs(l_today.m_high - l_yesterday.m_close))
/*  948 */             l_vtr = Math.abs(l_today.m_high - l_yesterday.m_close);
/*  949 */           if (l_vtr < Math.abs(l_today.m_low - l_yesterday.m_close))
/*  950 */             l_vtr = Math.abs(l_today.m_low - l_yesterday.m_close);
/*  951 */           l_pdm[l_i] = (l_pdm[l_i] / (this.m_calcParamValue[0] - 1) + l_vpdm);
/*  952 */           l_ndm[l_i] = (l_ndm[l_i] / (this.m_calcParamValue[0] - 1) + l_vndm);
/*  953 */           l_tr[l_i] = (l_tr[l_i] / (this.m_calcParamValue[0] - 1) + l_vtr);
/*      */         }
/*      */         else
/*      */         {
/*  958 */           l_today = this.m_kdoc.f_getRec(Math.max(l_dmiBeg + l_i, l_validRec));
/*  959 */           l_yesterday = this.m_kdoc.f_getRec(Math.max(l_dmiBeg + l_i - 1, l_validRec));
/*  960 */           double l_vpdm = (l_today.m_high > l_yesterday.m_high) && (l_today.m_high - l_yesterday.m_high > l_yesterday.m_low - l_today.m_low) ? l_today.m_high - l_yesterday.m_high : 0.0D;
/*      */ 
/*  963 */           double l_vndm = (l_yesterday.m_low > l_today.m_low) && (l_yesterday.m_low - l_today.m_low > l_today.m_high - l_yesterday.m_high) ? l_yesterday.m_low - l_today.m_low : 0.0D;
/*      */ 
/*  966 */           double l_vtr = l_today.m_high - l_today.m_low;
/*  967 */           if (l_vtr < Math.abs(l_today.m_high - l_yesterday.m_close))
/*  968 */             l_vtr = Math.abs(l_today.m_high - l_yesterday.m_close);
/*  969 */           if (l_vtr < Math.abs(l_today.m_low - l_yesterday.m_close))
/*  970 */             l_vtr = Math.abs(l_today.m_low - l_yesterday.m_close);
/*  971 */           l_pdm[l_i] = (l_pdm[(l_i - 1)] * (this.m_calcParamValue[0] - 1) / this.m_calcParamValue[0] + l_vpdm);
/*  972 */           l_ndm[l_i] = (l_ndm[(l_i - 1)] * (this.m_calcParamValue[0] - 1) / this.m_calcParamValue[0] + l_vndm);
/*  973 */           l_tr[l_i] = (l_tr[(l_i - 1)] * (this.m_calcParamValue[0] - 1) / this.m_calcParamValue[0] + l_vtr);
/*      */         }
/*      */       }
/*      */ 
/*  977 */       for (int l_i = 0; l_i < l_fin - l_dmiBeg + 1; l_i++)
/*      */       {
/*  979 */         l_pdi[l_i] = (Math.abs(l_tr[l_i]) <= 1.0E-007D ? 50.0D : l_pdm[l_i] / l_tr[l_i] * 100.0D);
/*      */ 
/*  981 */         l_ndi[l_i] = (Math.abs(l_tr[l_i]) <= 1.0E-007D ? 50.0D : l_ndm[l_i] / l_tr[l_i] * 100.0D);
/*      */       }
/*      */ 
/*  985 */       if (this.m_calcParamValue[1] > 0)
/*      */       {
/*  987 */         for (int l_i = 0; l_i < l_fin - l_dmiBeg + 1; l_i++)
/*      */         {
/*  989 */           if (l_i < this.m_calcParamValue[1])
/*      */           {
/*  991 */             for (int l_j = 0; l_j < this.m_calcParamValue[1]; l_j++)
/*      */             {
/*  993 */               int l_k = l_i - l_j < 0 ? 0 : l_i - l_j;
/*  994 */               l_adx[l_i] += (Math.abs(l_pdi[l_k] + l_ndi[l_k]) <= 1.0E-007D ? 0.0D : Math.abs(l_pdi[l_k] - l_ndi[l_k]) / (l_pdi[l_k] + l_ndi[l_k]) * 100.0D);
/*      */             }
/*      */ 
/*  997 */             l_adx[l_i] /= this.m_calcParamValue[1];
/*      */           }
/*      */           else
/*      */           {
/* 1001 */             double l_vdx = Math.abs(l_pdi[l_i] + l_ndi[l_i]) <= 1.0E-007D ? 0.0D : Math.abs(l_pdi[l_i] - l_ndi[l_i]) / (l_pdi[l_i] + l_ndi[l_i]) * 100.0D;
/*      */ 
/* 1003 */             l_adx[l_i] = ((l_adx[(l_i - 1)] * (this.m_calcParamValue[1] - 1) + l_vdx) / this.m_calcParamValue[1]);
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/* 1008 */       if (this.m_calcParamValue[2] > 0)
/*      */       {
/* 1010 */         for (int l_i = 0; l_i < l_fin - l_dmiBeg + 1; l_i++)
/*      */         {
/* 1012 */           int l_k = l_i - this.m_calcParamValue[2] < 0 ? 0 : l_i - this.m_calcParamValue[2];
/* 1013 */           l_adxr[l_i] = ((l_adx[l_i] + l_adx[l_k]) / 2.0D);
/*      */         }
/*      */       }
/* 1016 */       for (int l_k = 0; l_k < this.m_displayParamCount; l_k++)
/*      */       {
/* 1018 */         for (int l_i = l_org; l_i <= l_fin; l_i++) {
/* 1019 */           this.m_storedValue[l_k][(l_i - l_org)] = (l_k == 2 ? l_adx[(l_i - l_dmiBeg)] : l_k == 1 ? l_ndi[(l_i - l_dmiBeg)] : l_k == 0 ? l_pdi[(l_i - l_dmiBeg)] : l_adxr[(l_i - l_dmiBeg)]);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1024 */       break;
/*      */     case 20:
/* 1026 */       this.m_storedValue = new double[this.m_displayParamCount][l_fin - l_org + 1];
/* 1027 */       for (int l_k = 0; l_k < this.m_displayParamCount; l_k++)
/*      */       {
/* 1029 */         for (int l_i = l_org; l_i <= l_fin; l_i++)
/*      */         {
/* 1031 */           if (l_i - this.m_calcParamValue[l_k] + 1 < this.m_calcOrgRec)
/*      */             continue;
/* 1033 */           double l_s1 = 0.0D; double l_s2 = 0.0D;
/* 1034 */           for (int l_j = l_i; l_j >= l_i - this.m_calcParamValue[l_k] + 1; l_j--)
/*      */           {
/* 1036 */             if (l_k == 0)
/*      */             {
/* 1038 */               KLineRec l_rec = this.m_kdoc.f_getRec(l_j);
/* 1039 */               l_s1 += l_rec.m_high - l_rec.m_open;
/* 1040 */               l_s2 += l_rec.m_open - l_rec.m_low;
/*      */             }
/*      */             else
/*      */             {
/* 1044 */               KLineRec l_today = this.m_kdoc.f_getRec(l_j);
/* 1045 */               KLineRec l_yesterday = this.m_kdoc.f_getRec(Math.max(l_j - 1, l_validRec));
/* 1046 */               l_s1 += l_today.m_high - l_yesterday.m_close;
/* 1047 */               l_s2 += l_yesterday.m_close - l_today.m_low;
/*      */             }
/*      */           }
/* 1050 */           this.m_storedValue[l_k][(l_i - l_org)] = (Math.abs(l_s2) <= 1.0E-007D ? 1.0D : l_s1 / l_s2 * 100.0D);
/*      */         }
/*      */       }
/* 1053 */       break;
/*      */     case 21:
/*      */     case 22:
/* 1056 */       this.m_storedValue = new double[this.m_calcParamCount][l_fin - l_org + 1];
/* 1057 */       int l_crBeg = Math.max(l_validRec, l_org - this.m_calcParamValue[(this.m_calcParamCount - 1)] + 1);
/* 1058 */       double[] l_cr = new double[l_fin - l_crBeg + 1];
/* 1059 */       for (int l_i = l_crBeg; l_i <= l_fin; l_i++)
/*      */       {
/* 1061 */         if (l_i - this.m_calcParamValue[0] + 1 < this.m_calcOrgRec)
/*      */           continue;
/* 1063 */         if (this.m_subIndexId == 21)
/*      */         {
/* 1065 */           double l_s1 = 0.0D; double l_s2 = 0.0D;
/* 1066 */           for (int l_j = l_i; l_j >= l_i - this.m_calcParamValue[0] + 1; l_j--)
/*      */           {
/* 1068 */             KLineRec l_today = this.m_kdoc.f_getRec(l_j);
/* 1069 */             KLineRec l_yesterday = this.m_kdoc.f_getRec(Math.max(l_j - 1, l_validRec));
/* 1070 */             double l_pm = (l_yesterday.m_high + l_yesterday.m_low + l_yesterday.m_close) / 3.0D;
/* 1071 */             l_s1 += l_today.m_high - l_pm;
/* 1072 */             l_s2 += l_pm - l_today.m_low;
/*      */           }
/* 1074 */           l_cr[(l_i - l_crBeg)] = (Math.abs(l_s2) <= 1.0E-007D ? 1.0D : l_s1 / l_s2 * 100.0D);
/*      */         }
/*      */         else
/*      */         {
/* 1078 */           double l_sup = 0.0D; double l_sdown = 0.0D; double l_sequal = 0.0D;
/* 1079 */           for (int l_j = l_i; l_j >= l_i - this.m_calcParamValue[0] + 1; l_j--)
/*      */           {
/* 1081 */             KLineRec l_today = this.m_kdoc.f_getRec(l_j);
/* 1082 */             KLineRec l_yesterday = this.m_kdoc.f_getRec(Math.max(l_j - 1, l_validRec));
/* 1083 */             if (l_today.m_close > l_yesterday.m_close)
/* 1084 */               l_sup += l_today.m_money;
/* 1085 */             else if (l_today.m_close < l_yesterday.m_close)
/* 1086 */               l_sdown += l_today.m_money;
/*      */             else
/* 1088 */               l_sequal += l_today.m_money;
/*      */           }
/* 1090 */           l_cr[(l_i - l_crBeg)] = (Math.abs(l_sdown + l_sequal / 2.0D) <= 1.0E-007D ? 1.0D : (l_sup + l_sequal / 2.0D) / (l_sdown + l_sequal / 2.0D) * 100.0D);
/*      */         }
/*      */       }
/*      */ 
/* 1094 */       for (int l_i = l_org; l_i <= l_fin; l_i++)
/* 1095 */         this.m_storedValue[0][(l_i - l_org)] = l_cr[(l_i - l_crBeg)];
/* 1096 */       for (int l_k = 1; l_k < this.m_calcParamCount; l_k++)
/*      */       {
/* 1098 */         for (int l_i = l_org; l_i <= l_fin; l_i++)
/*      */         {
/* 1100 */           if (l_i - this.m_calcParamValue[l_k] - this.m_calcParamValue[0] + 1 < this.m_calcOrgRec)
/*      */             continue;
/* 1102 */           for (int l_j = l_i; l_j >= l_i - this.m_calcParamValue[l_k] + 1; l_j--)
/* 1103 */             this.m_storedValue[l_k][(l_i - l_org)] += l_cr[(l_j - l_crBeg)];
/* 1104 */           this.m_storedValue[l_k][(l_i - l_org)] /= this.m_calcParamValue[l_k];
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private boolean f_recalcMinMax()
/*      */   {
/* 1122 */     double l_oldMinValue = this.m_minValue;
/* 1123 */     double l_oldMaxValue = this.m_maxValue;
/* 1124 */     int l_org = this.m_parent.f_getOrgRec();
/* 1125 */     int l_fin = this.m_parent.f_getFinRec();
/* 1126 */     int l_dec = this.m_kdoc.f_isZhaiQuan() ? 10 : 100;
/* 1127 */     if ((l_org < 0) || (l_fin < 0))
/*      */     {
/* 1129 */       this.m_minValue = Integer.parseInt(s_subIndexInfo[this.m_subIndexId][3]);
/* 1130 */       this.m_maxValue = Integer.parseInt(s_subIndexInfo[this.m_subIndexId][4]);
/*      */     }
/*      */     else
/*      */     {
/* 1134 */       switch (this.m_subIndexId)
/*      */       {
/*      */       case 0:
/*      */       case 1:
/*      */       case 2:
/*      */       case 3:
/*      */       case 4:
/*      */       case 5:
/*      */       case 6:
/*      */       case 7:
/*      */       case 8:
/*      */       case 9:
/* 1146 */         this.m_minValue = 1.7976931348623157E+308D;
/* 1147 */         this.m_maxValue = 0.0D;
/* 1148 */         for (int l_i = l_org; l_i <= l_fin; l_i++)
/*      */         {
/* 1150 */           KLineRec l_rec = this.m_kdoc.f_getRec(l_i);
/* 1151 */           f_checkMinMax(l_rec.m_low);
/* 1152 */           f_checkMinMax(l_rec.m_high);
/* 1153 */           switch (this.m_subIndexId)
/*      */           {
/*      */           case 4:
/* 1156 */             if (l_i - this.m_calcParamValue[0] + 1 < this.m_calcOrgRec)
/*      */               continue;
/* 1158 */             f_checkMinMax(this.m_storedValue[0][(l_i - l_org)] - 2.0D * this.m_storedValue[1][(l_i - l_org)]);
/* 1159 */             f_checkMinMax(this.m_storedValue[0][(l_i - l_org)] + 2.0D * this.m_storedValue[1][(l_i - l_org)]);
/* 1160 */             break;
/*      */           case 8:
/* 1162 */             f_checkMinMax(this.m_storedValue[0][(l_i - l_org)]);
/* 1163 */             break;
/*      */           case 0:
/*      */           case 1:
/*      */           case 2:
/*      */           case 3:
/*      */           case 5:
/*      */           case 6:
/*      */           case 7:
/* 1171 */             for (int l_k = 0; l_k < this.m_displayParamCount; l_k++)
/*      */             {
/* 1173 */               int l_days = (this.m_subIndexId == 0) || (this.m_subIndexId == 1) || (this.m_subIndexId == 2) ? this.m_calcParamValue[l_k] : this.m_calcParamValue[0];
/*      */ 
/* 1176 */               if (l_i - l_days + 1 < this.m_calcOrgRec)
/*      */                 continue;
/* 1178 */               f_checkMinMax(this.m_storedValue[l_k][(l_i - l_org)]);
/*      */             }
/*      */           }
/*      */         }
/*      */ 
/* 1183 */         break;
/*      */       case 10:
/* 1185 */         this.m_minValue = 0.0D;
/* 1186 */         this.m_maxValue = 0.0D;
/* 1187 */         for (int l_i = l_org; l_i <= l_fin; l_i++)
/*      */         {
/* 1189 */           KLineRec l_rec = this.m_kdoc.f_getRec(l_i);
/* 1190 */           if (this.m_mainIndexId == 3)
/*      */           {
/* 1192 */             if (this.m_maxValue < l_rec.m_volume / l_dec) {
/* 1193 */               this.m_maxValue = (l_rec.m_volume / l_dec);
/*      */             }
/*      */ 
/*      */           }
/* 1197 */           else if (this.m_maxValue < l_rec.m_money / 10000.0D) {
/* 1198 */             this.m_maxValue = (l_rec.m_money / 10000.0D);
/*      */           }
/*      */         }
/* 1201 */         break;
/*      */       case 11:
/*      */       case 12:
/*      */       case 16:
/*      */       case 17:
/*      */       case 18:
/*      */       case 19:
/* 1208 */         this.m_minValue = Integer.parseInt(s_subIndexInfo[this.m_subIndexId][3]);
/* 1209 */         this.m_maxValue = Integer.parseInt(s_subIndexInfo[this.m_subIndexId][4]);
/* 1210 */         break;
/*      */       case 15:
/* 1212 */         this.m_minValue = 1.7976931348623157E+308D;
/* 1213 */         this.m_maxValue = 4.9E-324D;
/* 1214 */         for (int l_i = l_org; l_i <= l_fin; l_i++)
/*      */         {
/* 1216 */           if (l_i - this.m_calcParamValue[2] + 1 < this.m_calcOrgRec)
/*      */             continue;
/* 1218 */           f_checkMinMax(this.m_storedValue[0][(l_i - l_org)]);
/* 1219 */           f_checkMinMax(this.m_storedValue[1][(l_i - l_org)]);
/* 1220 */           f_checkMinMax(2.0D * (this.m_storedValue[0][(l_i - l_org)] - this.m_storedValue[1][(l_i - l_org)]));
/*      */         }
/* 1222 */         break;
/*      */       case 13:
/*      */       case 20:
/*      */       case 21:
/*      */       case 22:
/* 1227 */         this.m_minValue = 1.7976931348623157E+308D;
/* 1228 */         this.m_maxValue = 4.9E-324D;
/* 1229 */         for (int l_k = 0; l_k < this.m_displayParamCount; l_k++)
/*      */         {
/* 1231 */           for (int l_i = l_org; l_i <= l_fin; l_i++)
/*      */           {
/* 1233 */             if (((this.m_subIndexId == 20) && (l_i - this.m_calcParamValue[l_k] + 1 < this.m_calcOrgRec)) || (((this.m_subIndexId == 21) || (this.m_subIndexId == 22)) && (((l_k == 0) && (l_i - this.m_calcParamValue[0] + 1 < this.m_calcOrgRec)) || (((this.m_subIndexId == 21) || (this.m_subIndexId == 22)) && (l_k > 0) && (l_i - this.m_calcParamValue[l_k] - this.m_calcParamValue[0] + 1 < this.m_calcOrgRec)))))
/*      */             {
/*      */               continue;
/*      */             }
/* 1237 */             f_checkMinMax(this.m_storedValue[l_k][(l_i - l_org)]);
/*      */           }
/*      */         }
/* 1240 */         break;
/*      */       case 14:
/* 1242 */         this.m_minValue = 1.7976931348623157E+308D;
/* 1243 */         this.m_maxValue = 4.9E-324D;
/* 1244 */         for (int l_i = l_org; l_i <= l_fin; l_i++)
/*      */         {
/* 1246 */           if (l_i - this.m_calcParamValue[0] + 1 < this.m_calcOrgRec)
/*      */             continue;
/* 1248 */           f_checkMinMax(this.m_storedValue[0][(l_i - l_org)]);
/* 1249 */           if (l_i - this.m_calcParamValue[0] - this.m_calcParamValue[1] + 1 < this.m_calcOrgRec)
/*      */             continue;
/* 1251 */           f_checkMinMax(this.m_storedValue[1][(l_i - l_org)]);
/*      */         }
/*      */       }
/*      */ 
/* 1255 */       if (this.m_minValue >= this.m_maxValue)
/*      */       {
/* 1257 */         this.m_minValue = Integer.parseInt(s_subIndexInfo[this.m_subIndexId][3]);
/* 1258 */         this.m_maxValue = Integer.parseInt(s_subIndexInfo[this.m_subIndexId][4]);
/*      */       }
/*      */     }
/* 1261 */     this.m_pixelsPerUnit = ((this.m_leftRulerSize.height - 3) / (this.m_maxValue - this.m_minValue));
/*      */ 
/* 1263 */     return (this.m_minValue != l_oldMinValue) || (this.m_maxValue != l_oldMaxValue);
/*      */   }
/*      */ 
/*      */   private boolean f_recalcOrgFin()
/*      */   {
/*  313 */     int l_oldCalcOrgRec = this.m_calcOrgRec;
/*  314 */     int l_oldCalcFinRec = this.m_calcFinRec;
/*  315 */     int l_org = this.m_parent.f_getOrgRec();
/*  316 */     int l_fin = this.m_parent.f_getFinRec();
/*  317 */     if ((l_org < 0) || (l_fin < 0))
/*      */     {
/*  319 */       this.m_calcOrgRec = -1;
/*  320 */       this.m_calcFinRec = -1;
/*      */     }
/*      */     else
/*      */     {
/*  324 */       this.m_calcOrgRec = l_org;
/*  325 */       switch (this.m_subIndexId)
/*      */       {
/*      */       case 0:
/*      */       case 1:
/*      */       case 10:
/*  330 */         this.m_calcOrgRec -= this.m_calcParamValue[(this.m_calcParamCount - 1)];
/*  331 */         break;
/*      */       case 17:
/*      */       case 18:
/*  334 */         this.m_calcOrgRec -= 50 + this.m_calcParamValue[(this.m_calcParamCount - 1)];
/*  335 */         break;
/*      */       case 2:
/*  337 */         this.m_calcOrgRec -= 100;
/*  338 */         break;
/*      */       case 8:
/*  340 */         this.m_calcOrgRec -= 50 + this.m_calcParamValue[0] + 1;
/*  341 */         break;
/*      */       case 3:
/*      */       case 4:
/*      */       case 5:
/*      */       case 6:
/*      */       case 7:
/*      */       case 11:
/*      */       case 12:
/*      */       case 13:
/*      */       case 19:
/*  351 */         this.m_calcOrgRec -= this.m_calcParamValue[0];
/*  352 */         break;
/*      */       case 14:
/*  354 */         this.m_calcOrgRec -= this.m_calcParamValue[0] + this.m_calcParamValue[1];
/*  355 */         break;
/*      */       case 15:
/*  357 */         this.m_calcOrgRec -= 50 + this.m_calcParamValue[2];
/*  358 */         break;
/*      */       case 16:
/*  360 */         this.m_calcOrgRec -= 50 + this.m_calcParamValue[0];
/*  361 */         break;
/*      */       case 20:
/*  363 */         this.m_calcOrgRec -= Math.max(this.m_calcParamValue[0], this.m_calcParamValue[1]);
/*  364 */         break;
/*      */       case 21:
/*      */       case 22:
/*  367 */         this.m_calcOrgRec -= this.m_calcParamValue[0] + this.m_calcParamValue[(this.m_calcParamCount - 1)];
/*      */       case 9:
/*      */       }
/*  370 */       if (this.m_calcOrgRec < 0)
/*  371 */         this.m_calcOrgRec = 0;
/*  372 */       this.m_kdoc.f_setEndOfRecs(this.m_calcOrgRec);
/*  373 */       int l_validRec = this.m_kdoc.f_getValidRecPos();
/*  374 */       if (this.m_calcOrgRec < l_validRec)
/*  375 */         this.m_calcOrgRec = l_validRec;
/*  376 */       this.m_calcFinRec = l_fin;
/*      */     }
/*  378 */     return (this.m_calcOrgRec != l_oldCalcOrgRec) || (this.m_calcFinRec != l_oldCalcFinRec);
/*      */   }
/*      */ 
/*      */   private void f_refreshIndex(boolean p_param)
/*      */   {
/* 1269 */     if (p_param)
/*      */     {
/* 1271 */       this.m_indexName = (s_mainIndexInfo[this.m_mainIndexId][0] + s_subIndexInfo[this.m_subIndexId][0]);
/*      */ 
/* 1273 */       this.m_lrulerStep = Integer.parseInt(s_subIndexInfo[this.m_subIndexId][2]);
/* 1274 */       this.m_calcParamCount = Integer.parseInt(s_subIndexInfo[this.m_subIndexId][5]);
/* 1275 */       this.m_calcParamValue = new int[this.m_calcParamCount];
/* 1276 */       for (int l_i = 0; l_i < this.m_calcParamCount; l_i++)
/* 1277 */         this.m_calcParamValue[l_i] = Integer.parseInt(s_subIndexInfo[this.m_subIndexId][(6 + l_i)]);
/* 1278 */       this.m_displayParamCount = Integer.parseInt(s_subIndexInfo[this.m_subIndexId][(6 + this.m_calcParamCount)]);
/* 1279 */       this.m_displayParamName = new String[this.m_displayParamCount];
/* 1280 */       for (int l_i = 0; l_i < this.m_displayParamCount; l_i++)
/* 1281 */         this.m_displayParamName[l_i] = s_subIndexInfo[this.m_subIndexId][(7 + this.m_calcParamCount + l_i)];
/*      */     }
/* 1283 */     f_recalcOrgFin();
/* 1284 */     f_recalcIndex();
/* 1285 */     f_recalcMinMax();
/*      */   }
/*      */ 
/*      */   private int f_unit2pos(double p_unit)
/*      */   {
/* 1363 */     int l_delta = (int)((p_unit - this.m_minValue) * this.m_pixelsPerUnit + 0.5D);
/* 1364 */     return this.m_leftRulerSize.y + 1 + (this.m_leftRulerSize.height - 3 - l_delta);
/*      */   }
/*      */ 
/*      */   public void f_update(Object p_obs, Object p_arg)
/*      */   {
/* 1313 */     switch (((Integer)p_arg).intValue())
/*      */     {
/*      */     case -911:
/* 1318 */       f_refreshIndex(true);
/* 1319 */       repaint();
/* 1320 */       break;
/*      */     case -931:
/*      */     case -912:
/* 1324 */       f_refreshIndex(false);
/* 1325 */       repaint();
/* 1326 */       break;
/*      */     case -932:
/*      */     case -913:
/* 1331 */       if (!f_recalcOrgFin())
/*      */         break;
/* 1333 */       f_recalcIndex();
/* 1334 */       f_recalcMinMax();
/* 1335 */       repaint();
/* 1336 */       break;
/*      */     case -933:
/* 1339 */       f_recalcIndex();
/* 1340 */       if (f_recalcMinMax()) {
/* 1341 */         repaint();
/*      */       }
/*      */       else {
/* 1344 */         Graphics l_g = getGraphics();
/* 1345 */         int l_fin = this.m_parent.f_getFinRec();
/* 1346 */         int l_cursor = this.m_parent.f_getCursorRec();
/* 1347 */         if ((l_cursor == -1) || (l_cursor == l_fin))
/*      */         {
/* 1349 */           l_g.setClip(this.m_indexTitleSize.x, this.m_indexTitleSize.y, this.m_indexTitleSize.width, this.m_indexTitleSize.height);
/* 1350 */           f_paintIndexTitle(l_g);
/*      */         }
/* 1352 */         int l_pos = this.m_parent.f_rec2pos(l_fin);
/* 1353 */         int l_width = (int)this.m_parent.f_getPixelsPerRecord() + 1;
/* 1354 */         int l_offset = (int)(this.m_parent.f_getPixelsPerRecord() / 2.0D) + 1;
/* 1355 */         repaint(l_pos - l_offset, this.m_frameSize.y, l_width, this.m_frameSize.height);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void paint(Graphics p_g)
/*      */   {
/* 1674 */     Rectangle l_system_clip = p_g.getClipBounds();
/*      */ 
/* 1677 */     Dimension l_dimension = getSize();
/* 1678 */     p_g.clipRect(0, 0, l_dimension.width, l_dimension.height);
/* 1679 */     p_g.setColor(CommEnv.s_klineFrameColor);
/* 1680 */     p_g.clearRect(this.m_frameSize.x, this.m_frameSize.y, this.m_frameSize.width, this.m_frameSize.height);
/*      */ 
/* 1682 */     p_g.setColor(KLineApplet.s_bgColor);
/* 1683 */     p_g.fillRect(this.m_frameSize.x, this.m_frameSize.y, this.m_frameSize.width, this.m_frameSize.height);
/* 1684 */     p_g.setColor(CommEnv.s_klineFrameColor);
/* 1685 */     p_g.drawRect(this.m_frameSize.x, this.m_frameSize.y, this.m_frameSize.width, this.m_frameSize.height);
/*      */ 
/* 1687 */     FontMetrics l_fm = p_g.getFontMetrics();
/* 1688 */     int l_height = l_fm.getAscent() + (14 - (l_fm.getAscent() + l_fm.getDescent())) / 2;
/* 1689 */     p_g.setColor(CommEnv.s_indexNameColor);
/* 1690 */     p_g.drawString(this.m_indexName, this.m_indexNameSize.width - 2 - l_fm.stringWidth(this.m_indexName), l_height + 1);
/*      */ 
/* 1695 */     int l_scale = (this.m_kdoc.f_isBGu()) || (this.m_kdoc.f_isJiJin()) || (this.m_kdoc.f_isQuanzheng()) ? 3 : 2;
/* 1696 */     boolean l_fixedRuler = (this.m_subIndexId == 11) || (this.m_subIndexId == 12) || (this.m_subIndexId == 17) || (this.m_subIndexId == 18) || (this.m_subIndexId == 19) || (this.m_subIndexId == 16);
/*      */ 
/* 1699 */     double[] l_fixedUnit = { 20.0D, 50.0D, 80.0D };
/* 1700 */     int l_rulerCount = l_fixedRuler ? 4 : this.m_lrulerStep;
/* 1701 */     int l_dec_scale = this.m_mainIndexId <= 2 ? l_scale : this.m_mainIndexId == 3 ? 0 : 2;
/*      */ 
/* 1703 */     for (int l_i = 1; l_i < l_rulerCount; l_i++)
/*      */     {
/* 1705 */       double l_unit = l_fixedRuler ? l_fixedUnit[(l_i - 1)] : (this.m_maxValue - this.m_minValue) * l_i / this.m_lrulerStep + this.m_minValue;
/*      */ 
/* 1707 */       int l_pos = f_unit2pos(l_unit);
/* 1708 */       f_drawCellLine(p_g, this.m_frameSize.x + 1, l_pos, this.m_frameSize.width + this.m_frameSize.x - 1, l_pos);
/*      */ 
/* 1710 */       p_g.setColor(CommEnv.s_rulerColor);
/* 1711 */       String l_show = CommEnv.f_scaleDouble(l_unit, l_dec_scale);
/* 1712 */       String l_multiTimes = "X";
/* 1713 */       boolean l_drawTimes = false;
/* 1714 */       int l_stringWidth = l_fm.stringWidth(l_show);
/* 1715 */       if (this.m_mainIndexId == 3)
/*      */       {
/* 1718 */         l_drawTimes = false;
/* 1719 */         int l_times = 1;
/* 1720 */         while (l_stringWidth > 57)
/*      */         {
/* 1722 */           l_times *= 10;
/* 1723 */           l_unit /= l_times;
/* 1724 */           l_show = CommEnv.f_scaleDouble(l_unit, 0);
/* 1725 */           l_stringWidth = l_fm.stringWidth(l_show);
/*      */         }
/* 1727 */         if (l_times > 1)
/* 1728 */           l_drawTimes = true;
/* 1729 */         l_multiTimes = l_multiTimes + Integer.toString(l_times);
/*      */       }
/*      */ 
/* 1733 */       if (this.m_mainIndexId == 4)
/*      */       {
/* 1736 */         l_drawTimes = false;
/* 1737 */         int l_time = 1;
/* 1738 */         while (l_stringWidth > 57)
/*      */         {
/* 1740 */           l_time *= 10;
/* 1741 */           l_unit /= l_time;
/* 1742 */           l_show = CommEnv.f_scaleDouble(l_unit, 1);
/* 1743 */           l_stringWidth = l_fm.stringWidth(l_show);
/*      */         }
/* 1745 */         if (l_time > 1)
/* 1746 */           l_drawTimes = true;
/* 1747 */         l_multiTimes = l_multiTimes + Integer.toString(l_time);
/*      */       }
/*      */ 
/* 1752 */       if (l_drawTimes) {
/* 1753 */         p_g.drawString(l_multiTimes, this.m_leftRulerSize.width - 2 - l_fm.stringWidth(l_multiTimes), l_pos + (l_fm.getAscent() + l_fm.getDescent()) + 5);
/*      */       }
/* 1755 */       p_g.drawString(l_show, this.m_leftRulerSize.width - 2 - l_fm.stringWidth(l_show), l_pos + (l_fm.getAscent() + l_fm.getDescent()) / 2 - 2);
/*      */     }
/*      */ 
/* 1761 */     int l_org = this.m_parent.f_getOrgRec();
/* 1762 */     int l_fin = this.m_parent.f_getFinRec();
/* 1763 */     if ((l_org < 0) || (l_fin < 0))
/* 1764 */       return;
/* 1765 */     int l_cycle = this.m_kdoc.f_getCycleType();
/* 1766 */     Date l_prevDate = null;
/* 1767 */     Date l_currDate = null;
/* 1768 */     KLineRec l_rec = null;
/* 1769 */     for (int l_i = l_org; l_i <= l_fin; l_i++)
/*      */     {
/* 1771 */       l_rec = this.m_kdoc.f_getRec(l_i);
/* 1772 */       l_currDate = l_rec.m_tradeDate;
/* 1773 */       boolean l_visible = l_currDate.getMonth() != l_prevDate.getMonth();
/*      */ 
/* 1778 */       if (l_visible)
/*      */       {
/* 1780 */         int l_xpos = this.m_parent.f_rec2pos(l_i);
/* 1781 */         f_drawCellLine(p_g, l_xpos, this.m_frameSize.y + 1, l_xpos, this.m_frameSize.height + this.m_frameSize.y - 1);
/*      */       }
/*      */ 
/* 1784 */       l_prevDate = l_currDate;
/*      */     }
/*      */ 
/* 1787 */     p_g.setClip(l_system_clip);
/* 1788 */     p_g.clipRect(this.m_frameSize.x, this.m_frameSize.y, this.m_frameSize.width, this.m_frameSize.height);
/* 1789 */     f_paintIndexLine(p_g);
/*      */ 
/* 1792 */     p_g.setClip(l_system_clip);
/* 1793 */     p_g.clipRect(this.m_indexTitleSize.x, this.m_indexTitleSize.y, this.m_indexTitleSize.width, this.m_indexTitleSize.height);
/* 1794 */     f_paintIndexTitle(p_g);
/*      */ 
/* 1797 */     p_g.setClip(l_system_clip);
/* 1798 */     p_g.clipRect(this.m_frameSize.x, this.m_frameSize.y, this.m_frameSize.width, this.m_frameSize.height);
/* 1799 */     if (this.m_parent.m_preRecPointedByMouse != -1)
/*      */     {
/* 1801 */       p_g.setColor(Color.black);
/*      */ 
/* 1803 */       p_g.setXORMode(CommEnv.s_cursorLineColor);
/* 1804 */       p_g.drawLine(this.m_parent.f_rec2pos(this.m_parent.m_preRecPointedByMouse), this.m_frameSize.y, this.m_parent.f_rec2pos(this.m_parent.m_preRecPointedByMouse), this.m_frameSize.y + this.m_frameSize.height);
/*      */ 
/* 1809 */       p_g.setPaintMode();
/*      */     }
/*      */   }
/*      */ }

/* Location:           F:\stock research\kline\
 * Qualified Name:     lido.kline.KIndexChild
 * JD-Core Version:    0.6.0
 */