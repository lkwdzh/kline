/*     */ package lido.common;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Font;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Rectangle;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.math.BigDecimal;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.util.Date;
/*     */ 
/*     */ public final class CommEnv
/*     */ {
/*     */   private static final int l_miniFrameHeight = 160;
/*     */   public static final String s_AGU = "002";
/*     */   public static final String s_BGU = "004";
/*     */   public static final Color s_ChildBkColor;
/*     */   public static final String s_GUOZHAI = "016";
/*     */   public static final String s_HUIGOU = "128";
/*     */   public static final String s_JIJIN = "008";
/*     */   public static final String s_OTHER = "256";
/*     */   public static final String s_QIZHAI = "032";
/*     */   public static final String s_QUANZHENG = "203";
/*     */   public static final String s_ZHISHU = "001";
/*     */   public static final String s_ZHUANZHAI = "064";
/*     */   public static final int s_Zhishu_basicHQViewHeight = 94;
/*     */   public static final int s_Zhishu_basicHQViewWidth = 169;
/*     */   public static final int s_Zhishu_buySaleViewHeight = 101;
/*     */   public static final int s_Zhishu_buySaleViewWidth = 169;
/*     */   public static final int s_Zhishu_smallMXPLineViewWidth = 169;
/*     */   public static final int s_basicHQViewHeight = 92;
/*     */   public static final int s_basicHQViewWidth = 169;
/*     */   public static final int s_brulerAppend = -932;
/*     */   public static final int s_brulerRefreshLastOne = -933;
/*     */   public static final Color s_brwsrBKColor;
/*     */   public static final int s_buySaleViewHeight = 147;
/*     */   public static final int s_buySaleViewWidth = 169;
/*     */   private static final int s_connRetryCount = 3;
/*     */   public static final Color s_cursorLineColor;
/*     */   public static final int s_cycleTypeChanged = -912;
/*     */   public static final Date s_defaultAmClose;
/*  22 */   public static final Date s_defaultAmOpen = new Date(100, 0, 1, 9, 30, 0);
/*     */   public static final Color s_defaultLineColor;
/*     */   public static final Date s_defaultPmClose;
/*     */   public static final Date s_defaultPmOpen;
/*     */   public static final String s_defaultStockCode = "000001.SS";
/*     */   public static final Color s_digitalColor;
/*     */   public static final int s_drawVerticalLine = -907;
/*     */   public static final int s_dynkAppend = -914;
/*     */   public static final int s_dynkRefresh = -915;
/*     */   public static final int s_hiskDataRetrieved = -913;
/*     */   public static final int s_hispDataRetrieved = -921;
/*     */   public static final int s_hqDataRetrieved = -901;
/*     */   public static final Color s_hqDownColor;
/*     */   public static final Color s_hqEqualColor;
/*     */   public static final Color s_hqUpColor;
/*     */   public static final Color s_indexNameColor;
/*     */   public static final int s_indexTitleHeight = 14;
/*     */   public static final int s_initUpdate = -919;
/*     */   public static final int s_isNextDay = -916;
/*     */   public static final int s_kleftRulerHeight = 280;
/*     */   public static final int s_kleftRulerWidth = 70;
/*     */   public static final Color s_klineDownColor;
/*     */   public static final Color s_klineFrameColor;
/*     */   public static final Color[] s_klineLineColor;
/*     */   public static final int s_klineUICommand = -931;
/*     */   public static final Color s_klineUpColor;
/*     */   public static final Font s_largeFont;
/*     */   public static final int s_leftRulerWidth = 60;
/*     */   public static final int s_leftRulerWidth5 = 60;
/*     */   public static final Color s_menuBarColor;
/*     */   public static final Color s_miniFrameColor;
/*     */   public static final Rectangle s_miniPFrameSize;
/*     */   public static final int s_noUpdate = -917;
/*     */   public static final Font s_normalFont;
/*     */   public static final int s_pbottomRulerViewHeight = 14;
/*     */   public static final Color s_plineColor;
/*     */   public static final Color s_plineDownColor;
/*     */   public static final Color s_plineEqualColor;
/*     */   public static final Color s_plineFrameColor;
/*     */   public static final Color s_plineIndexColor;
/*     */   public static final Color s_plineUpColor;
/*     */   public static final Color s_plineVolColor;
/*     */   public static final int s_reservedAreaWidth = 15;
/*     */   public static final Color s_rulerColor;
/*     */   public static final Font s_selectedFont;
/*     */   public static final Font s_smallFont;
/*     */   public static final int s_smallMXPLineViewWidth = 169;
/*     */   public static final Color s_sslBarColor;
/*     */   public static final Color s_sslVolColor;
/*     */   public static final Color s_statusBarColor;
/*     */   public static final int s_stockCodeChanged = -911;
/*     */   public static final int s_stockInfoViewHeight = 216;
/*     */   public static final int s_stockInfoViewWidth = 169;
/*     */   public static final int s_stockNameViewHeight = 21;
/*     */   public static final int s_stockNameViewWidth = 169;
/*     */   public static final Color s_sysBkColor;
/*     */   public static final Color s_sysFrameColor;
/*     */   public static final Color s_textColor;
/*     */   public static final Color s_titleColor;
/*     */ 
/*     */   static
/*     */   {
/*  24 */     s_defaultAmClose = new Date(100, 0, 1, 11, 30, 0);
/*  25 */     s_defaultPmOpen = new Date(100, 0, 1, 13, 0, 0);
/*  26 */     s_defaultPmClose = new Date(100, 0, 1, 15, 0, 0);
/*     */ 
/*  78 */     s_miniPFrameSize = new Rectangle(0, 18, 60, 160);
/*     */ 
/*  81 */     s_miniFrameColor = new Color(255, 255, 222);
/*     */ 
/*  83 */     s_cursorLineColor = new Color(0, 128, 128);
/*     */ 
/*  86 */     s_largeFont = new Font("宋体", 1, 18);
/*     */ 
/*  88 */     s_normalFont = new Font("宋体", 0, 14);
/*  89 */     s_smallFont = new Font("宋体", 0, 12);
/*  90 */     s_selectedFont = new Font("宋体", 1, 13);
/*     */ 
/*  93 */     s_brwsrBKColor = Color.white;
/*     */ 
/*  96 */     s_menuBarColor = new Color(0, 0, 0);
/*     */ 
/*  98 */     s_sysBkColor = new Color(255, 255, 255);
/*  99 */     s_sysFrameColor = new Color(128, 128, 128);
/* 100 */     s_statusBarColor = new Color(192, 192, 192);
/* 101 */     s_hqUpColor = new Color(255, 0, 0);
/* 102 */     s_hqDownColor = new Color(0, 128, 0);
/* 103 */     s_hqEqualColor = new Color(0, 0, 0);
/* 104 */     s_titleColor = new Color(0, 128, 128);
/* 105 */     s_textColor = new Color(0, 0, 0);
/* 106 */     s_digitalColor = new Color(128, 128, 0);
/*     */ 
/* 109 */     s_klineFrameColor = new Color(51, 127, 178);
/*     */ 
/* 111 */     s_ChildBkColor = new Color(119, 237, 248);
/* 112 */     s_indexNameColor = new Color(0, 128, 128);
/* 113 */     s_rulerColor = new Color(0, 0, 0);
/* 114 */     s_klineUpColor = new Color(255, 0, 0);
/* 115 */     s_klineDownColor = new Color(0, 128, 0);
/* 116 */     s_sslBarColor = new Color(192, 224, 224);
/* 117 */     s_sslVolColor = new Color(50, 100, 192);
/* 118 */     s_defaultLineColor = new Color(0, 0, 222);
/* 119 */     s_plineFrameColor = s_klineFrameColor;
/* 120 */     s_plineColor = new Color(0, 0, 222);
/* 121 */     s_plineIndexColor = new Color(128, 0, 0);
/* 122 */     s_plineVolColor = s_plineColor;
/* 123 */     s_plineUpColor = s_hqUpColor;
/* 124 */     s_plineDownColor = s_hqDownColor;
/* 125 */     s_plineEqualColor = s_hqEqualColor;
/* 126 */     s_klineLineColor = new Color[] { new Color(128, 128, 0), new Color(0, 128, 128), new Color(128, 0, 128), new Color(0, 0, 128), new Color(128, 0, 0), new Color(0, 128, 0), new Color(128, 128, 128) };
/*     */   }
/*     */ 
/*     */   public static String f_changeIfNeeded(Graphics p_g, String p_s)
/*     */   {
/* 181 */     if (Math.abs(Double.valueOf(p_s).doubleValue()) > 9.899999999999999E-005D)
/* 182 */       return p_s;
/* 183 */     if ((Math.abs(Double.valueOf(p_s).doubleValue()) < 9.899999999999999E-005D) && (Math.abs(Double.valueOf(p_s).doubleValue()) > 0.0D))
/*     */     {
/* 185 */       return "0";
/*     */     }
/*     */ 
/* 189 */     p_g.setColor(s_textColor);
/* 190 */     p_s = "——";
/* 191 */     return p_s;
/*     */   }
/*     */ 
/*     */   public static Color f_chooseColorByValue(String p_s)
/*     */   {
/* 174 */     return p_s.equalsIgnoreCase("dn") ? s_hqDownColor : p_s.equalsIgnoreCase("up") ? s_hqUpColor : s_hqEqualColor;
/*     */   }
/*     */ 
/*     */   public static Color f_chooseColorByValue(double p_d1, double p_d2)
/*     */   {
/* 167 */     return p_d1 < p_d2 ? s_hqDownColor : p_d1 > p_d2 ? s_hqUpColor : s_hqEqualColor;
/*     */   }
/*     */ 
/*     */   public static String f_delMarkCode(String p_stockID)
/*     */   {
/* 227 */     String l_retStr = null;
/* 228 */     if (p_stockID != null) {
/* 229 */       int l_index = p_stockID.indexOf(".");
/* 230 */       if (l_index < 0)
/* 231 */         l_retStr = p_stockID;
/*     */       else
/* 233 */         l_retStr = p_stockID.substring(0, l_index);
/*     */     }
/* 235 */     return l_retStr;
/*     */   }
/*     */ 
/*     */   public static String f_scaleDouble(double p_value, int p_scale)
/*     */   {
/* 154 */     BigDecimal l_big = new BigDecimal(p_value);
/* 155 */     l_big = l_big.setScale(p_scale, 4);
/* 156 */     return l_big.toString();
/*     */   }
/*     */ 
/*     */   public static long f_toSeconds(long p_time)
/*     */   {
/* 161 */     Date l_temp = new Date(p_time);
/* 162 */     return l_temp.getSeconds() + 60 * l_temp.getMinutes() + 3600 * l_temp.getHours();
/*     */   }
/*     */ 
/*     */   public static DataInputStream f_urlConn(String p_connStr)
/*     */   {
/* 199 */     DataInputStream l_in = null;
/* 200 */     for (int i = 0; i < 3; i++)
/*     */     {
/*     */       try
/*     */       {
/* 204 */         URL l_url = new URL(p_connStr);
/* 205 */         l_in = new DataInputStream(new BufferedInputStream(l_url.openStream(), 20480));
/*     */       }
/*     */       catch (MalformedURLException l_urle)
/*     */       {
/* 210 */         l_in = null;
/*     */       }
/*     */       catch (IOException l_ioe)
/*     */       {
/* 215 */         l_in = null;
/*     */       }
/*     */       catch (Exception l_e) {
/* 218 */         l_e.printStackTrace();
/*     */       }
/* 220 */       if (l_in != null)
/*     */         break;
/*     */     }
/* 223 */     return l_in;
/*     */   }
/*     */ }

/* Location:           F:\stock research\kline\
 * Qualified Name:     lido.common.CommEnv
 * JD-Core Version:    0.6.0
 */