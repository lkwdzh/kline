/*     */ package lido.common;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Image;
/*     */ import java.awt.Panel;
/*     */ import java.awt.Rectangle;
/*     */ import java.util.Observable;
/*     */ import java.util.Observer;
/*     */ 
/*     */ public class StockInfoView extends Panel
/*     */   implements Observer
/*     */ {
/*     */   private Rectangle m_basicHQViewSize;
/*     */   private int[] m_basic_hq_column_align;
/*     */   private Rectangle m_buySaleViewSize;
/*     */   private int[] m_buy_sale_column_align;
/*     */   DynamicHQDaemon m_daemon;
/*     */   private double[] m_dbArray;
/*     */   private double[] m_dbArray1;
/*  24 */   private boolean m_dispMarkCode = false;
/*     */ 
/*  22 */   private boolean m_drawNameAndCode = false;
/*     */ 
/*  20 */   private String m_stockCode = null;
/*     */   private Rectangle m_stockInfoViewSize;
/*  21 */   private String m_stockName = null;
/*     */   private Rectangle m_stockNameViewSize;
/*     */   private static String[] s_basic_hq_item;
/*     */   private static final int s_basic_hq_row_number = 6;
/*     */   private static final int s_buy_sale_col_number = 3;
/*     */   private static final String[] s_buy_sale_item_exp;
/*  38 */   private static final String[] s_buy_sale_item_sgn = { "卖五", "卖四", "卖三", "卖二", "卖一", "买一", "买二", "买三", "买四", "买五" };
/*     */   private static final int s_buy_sale_row_number = 11;
/*     */   private static final int s_zhishi_row_number = 7;
/*     */   private static final int s_zhishu_col_number = 3;
/*     */ 
/*     */   static
/*     */   {
/*  39 */     s_buy_sale_item_exp = new String[] { "卖一", "买一", "停牌", "上涨", "下跌", "平盘" };
/*     */ 
/*  43 */     s_basic_hq_item = new String[] { "最新", "均价", "涨跌", "前收", "涨幅", "开盘", "总额", "最高", "总量", "最低", "现手", "量比" };
/*     */   }
/*     */ 
/*     */   public StockInfoView(DynamicHQDaemon p_masterDaemon, Rectangle p_bounds, Rectangle p_buySaleViewSize, Rectangle p_basicHQViewSize, Rectangle p_stockNameViewSize, boolean p_dispMarkCode)
/*     */   {
/*  58 */     setBounds(p_bounds);
/*  59 */     setBackground(CommEnv.s_sysBkColor);
/*     */ 
/*  61 */     this.m_stockInfoViewSize = p_bounds;
/*     */ 
/*  63 */     this.m_stockNameViewSize = p_stockNameViewSize;
/*     */ 
/*  65 */     this.m_buySaleViewSize = p_buySaleViewSize;
/*     */ 
/*  67 */     this.m_basicHQViewSize = p_basicHQViewSize;
/*     */ 
/*  76 */     this.m_buy_sale_column_align = new int[3];
/*  77 */     this.m_buy_sale_column_align[0] = 2;
/*  78 */     this.m_buy_sale_column_align[1] = (this.m_buySaleViewSize.width / 3 * 2 - 14);
/*  79 */     this.m_buy_sale_column_align[2] = (this.m_buySaleViewSize.width / 3 * 3 - 14);
/*     */ 
/*  81 */     this.m_basic_hq_column_align = new int[4];
/*  82 */     this.m_basic_hq_column_align[0] = 2;
/*  83 */     this.m_basic_hq_column_align[1] = (this.m_basicHQViewSize.width / 2 + 5);
/*  84 */     this.m_basic_hq_column_align[2] = (this.m_basicHQViewSize.width / 2 + 7);
/*  85 */     this.m_basic_hq_column_align[3] = (this.m_basicHQViewSize.width - 1);
/*     */ 
/*  87 */     this.m_dbArray = new double[3];
/*  88 */     this.m_dbArray1 = new double[3];
/*     */ 
/*  91 */     this.m_daemon = p_masterDaemon;
/*  92 */     this.m_daemon.addObserver(this);
/*     */     double tmp220_217 = this.m_daemon.m_up_numb; this.m_dbArray1[0] = tmp220_217; this.m_dbArray[0] = tmp220_217;
/*     */     double tmp240_237 = this.m_daemon.m_down_numb; this.m_dbArray1[1] = tmp240_237; this.m_dbArray[1] = tmp240_237;
/*     */     double tmp260_257 = this.m_daemon.m_equal_numb; this.m_dbArray1[2] = tmp260_257; this.m_dbArray[2] = tmp260_257;
/*     */ 
/*  99 */     this.m_stockName = this.m_daemon.m_Stock_Name;
/* 100 */     this.m_stockCode = this.m_daemon.m_Stock_Code;
/* 101 */     this.m_dispMarkCode = p_dispMarkCode;
/*     */   }
/*     */ 
/*     */   public void f_getStockData() {
/* 105 */     this.m_stockName = this.m_daemon.m_Stock_Name;
/* 106 */     this.m_stockCode = this.m_daemon.m_Stock_Code;
/*     */   }
/*     */ 
/*     */   public void paint(Graphics p_g)
/*     */   {
/* 121 */     int col_number = 0;
/* 122 */     int row_number = 0;
/* 123 */     if (this.m_daemon.m_Variety_ID.equalsIgnoreCase("001")) {
/* 124 */       col_number = 3;
/* 125 */       row_number = 7;
/*     */     } else {
/* 127 */       col_number = 3;
/* 128 */       row_number = 11;
/*     */     }
/*     */ 
/* 131 */     int l_dec = this.m_daemon.f_isZhaiQuan() ? 10 : 100;
/* 132 */     int l_scale = (this.m_daemon.f_isBGu()) || (this.m_daemon.f_isJiJin()) || (this.m_daemon.f_isQuanZheng()) ? 3 : 2;
/*     */ 
/* 134 */     p_g.setColor(new Color(192, 192, 192));
/*     */ 
/* 137 */     p_g.drawLine(0, this.m_stockNameViewSize.height, this.m_stockInfoViewSize.width, this.m_stockNameViewSize.height);
/*     */ 
/* 141 */     p_g.drawLine(0, this.m_stockNameViewSize.height + this.m_buySaleViewSize.height, this.m_stockInfoViewSize.width, this.m_stockNameViewSize.height + this.m_buySaleViewSize.height);
/*     */ 
/* 151 */     p_g.setFont(CommEnv.s_largeFont);
/* 152 */     FontMetrics l_fm = p_g.getFontMetrics();
/* 153 */     p_g.setColor(CommEnv.s_titleColor);
/* 154 */     int l_height = l_fm.getAscent() + (this.m_stockNameViewSize.height - (l_fm.getAscent() + l_fm.getDescent())) / 2;
/*     */ 
/* 170 */     if (this.m_daemon.m_Stock_Code != null)
/*     */     {
/* 172 */       if (this.m_dispMarkCode)
/* 173 */         p_g.drawString(this.m_daemon.m_Stock_Code, 8, l_height);
/*     */       else {
/* 175 */         p_g.drawString(CommEnv.f_delMarkCode(this.m_daemon.m_Stock_Code), 8, l_height);
/*     */       }
/*     */     }
/* 178 */     if (this.m_daemon.m_Stock_Name != null)
/*     */     {
/* 180 */       int l_len = l_fm.stringWidth(this.m_daemon.m_Stock_Name);
/* 181 */       p_g.drawString(this.m_daemon.m_Stock_Name, this.m_stockNameViewSize.width - l_len - 8, l_height + 1);
/*     */     }
/*     */ 
/* 187 */     p_g.setFont(CommEnv.s_smallFont);
/* 188 */     l_fm = p_g.getFontMetrics();
/*     */ 
/* 191 */     p_g.setColor(CommEnv.s_textColor);
/*     */ 
/* 193 */     p_g.drawString("委比", this.m_buy_sale_column_align[0], this.m_buySaleViewSize.height / row_number + this.m_stockNameViewSize.height);
/*     */ 
/* 196 */     p_g.setColor(CommEnv.f_chooseColorByValue(this.m_daemon.m_Buy_Sale_Ratio, 0.0D));
/* 197 */     String l_temp = CommEnv.f_changeIfNeeded(p_g, CommEnv.f_scaleDouble(this.m_daemon.m_Buy_Sale_Ratio * 100.0D, 2));
/*     */ 
/* 199 */     p_g.drawString(l_temp, this.m_buy_sale_column_align[1] - l_fm.stringWidth(l_temp), this.m_buySaleViewSize.height / row_number + this.m_stockNameViewSize.height - 1);
/*     */ 
/* 202 */     p_g.setColor(CommEnv.f_chooseColorByValue(this.m_daemon.m_Buy_Sale_Ratio, 0.0D));
/*     */ 
/* 204 */     l_temp = CommEnv.f_changeIfNeeded(p_g, CommEnv.f_scaleDouble(this.m_daemon.m_Buy_Sale_Gap / l_dec, 0));
/*     */ 
/* 206 */     p_g.drawString(l_temp, this.m_buy_sale_column_align[2] - l_fm.stringWidth(l_temp), this.m_buySaleViewSize.height / row_number + this.m_stockNameViewSize.height - 1);
/*     */ 
/* 209 */     if (this.m_daemon.m_Variety_ID.equalsIgnoreCase("001"))
/*     */     {
/* 211 */       double[] l_data_p = { this.m_daemon.m_P_sale1, this.m_daemon.m_P_Buy1 };
/* 212 */       double[] l_data_v = { this.m_daemon.m_v_sale1, this.m_daemon.m_v_Buy1 };
/*     */ 
/* 215 */       for (int l_i = 0; l_i < 2; l_i++)
/*     */       {
/* 217 */         for (int l_j = 0; l_j < col_number; l_j++)
/*     */         {
/* 219 */           if (l_j == 0)
/*     */           {
/* 221 */             p_g.setColor(CommEnv.s_textColor);
/*     */ 
/* 223 */             p_g.drawString(s_buy_sale_item_exp[l_i], this.m_buy_sale_column_align[0], this.m_buySaleViewSize.height / row_number * (l_i + 2) + this.m_stockNameViewSize.height);
/*     */           }
/* 226 */           else if (l_j == 1)
/*     */           {
/* 228 */             p_g.setColor(CommEnv.f_chooseColorByValue(l_data_p[1], this.m_daemon.m_P_Close));
/* 229 */             if ((this.m_stockCode == "000003") || (this.m_stockCode == "9903") || (this.m_stockCode == "9913"))
/* 230 */               l_temp = CommEnv.f_changeIfNeeded(p_g, CommEnv.f_scaleDouble(l_data_p[l_i], 3));
/*     */             else {
/* 232 */               l_temp = CommEnv.f_changeIfNeeded(p_g, CommEnv.f_scaleDouble(l_data_p[l_i], 2));
/*     */             }
/* 234 */             p_g.drawString(l_temp, this.m_buy_sale_column_align[1] - l_fm.stringWidth(l_temp), this.m_buySaleViewSize.height / row_number * (l_i + 2) + this.m_stockNameViewSize.height - 1);
/*     */           }
/*     */           else
/*     */           {
/* 239 */             p_g.setColor(CommEnv.s_digitalColor);
/* 240 */             l_temp = CommEnv.f_changeIfNeeded(p_g, CommEnv.f_scaleDouble(l_data_v[l_i] / 100.0D, 0));
/*     */ 
/* 242 */             p_g.drawString(l_temp, this.m_buy_sale_column_align[2] - l_fm.stringWidth(l_temp), this.m_buySaleViewSize.height / row_number * (l_i + 2) + this.m_stockNameViewSize.height - 1);
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 249 */       Color[] l_colorArray = { CommEnv.s_digitalColor, CommEnv.s_hqUpColor, CommEnv.s_hqDownColor, CommEnv.s_hqEqualColor };
/*     */ 
/* 251 */       double[] l_dbArray = { this.m_daemon.m_up_numb, this.m_daemon.m_down_numb, this.m_daemon.m_equal_numb };
/*     */ 
/* 253 */       p_g.setColor(CommEnv.s_textColor);
/* 254 */       p_g.drawString(s_buy_sale_item_exp[2], this.m_buy_sale_column_align[0], this.m_buySaleViewSize.height / row_number * 4 + this.m_stockNameViewSize.height);
/*     */ 
/* 256 */       p_g.setColor(l_colorArray[0]);
/* 257 */       l_temp = CommEnv.f_changeIfNeeded(p_g, CommEnv.f_scaleDouble(this.m_daemon.m_break1_numb, 0));
/* 258 */       p_g.drawString(l_temp, this.m_buy_sale_column_align[1] - l_fm.stringWidth(l_temp), this.m_buySaleViewSize.height / row_number * 4 + this.m_stockNameViewSize.height - 1);
/*     */ 
/* 260 */       l_temp = CommEnv.f_changeIfNeeded(p_g, CommEnv.f_scaleDouble(this.m_daemon.m_break2_numb, 0));
/* 261 */       p_g.drawString(l_temp, this.m_buy_sale_column_align[2] - l_fm.stringWidth(l_temp), this.m_buySaleViewSize.height / row_number * 4 + this.m_stockNameViewSize.height - 1);
/*     */ 
/* 265 */       for (int l_i = 0; l_i < 3; l_i++)
/*     */       {
/* 267 */         p_g.setColor(CommEnv.s_textColor);
/*     */ 
/* 269 */         p_g.drawString(s_buy_sale_item_exp[(l_i + 3)], this.m_buy_sale_column_align[0], this.m_buySaleViewSize.height / row_number * (l_i + 5) + this.m_stockNameViewSize.height);
/*     */ 
/* 273 */         p_g.setColor(l_colorArray[(l_i + 1)]);
/* 274 */         if (l_dbArray[l_i] < 0.0D)
/* 275 */           l_dbArray[l_i] = 0.0D;
/*     */         else {
/* 277 */           l_temp = CommEnv.f_changeIfNeeded(p_g, CommEnv.f_scaleDouble(l_dbArray[l_i], 0));
/*     */         }
/* 279 */         p_g.drawString(l_temp, this.m_buy_sale_column_align[2] - l_fm.stringWidth(l_temp), this.m_buySaleViewSize.height / row_number * (l_i + 5) + this.m_stockNameViewSize.height - 1);
/*     */ 
/* 281 */         if (this.m_dbArray[l_i] < 0.0D)
/* 282 */           this.m_dbArray[l_i] = 0.0D;
/*     */         else
/* 284 */           l_temp = CommEnv.f_changeIfNeeded(p_g, CommEnv.f_scaleDouble(this.m_dbArray[l_i], 0));
/* 285 */         p_g.drawString(l_temp, this.m_buy_sale_column_align[1] - l_fm.stringWidth(l_temp), this.m_buySaleViewSize.height / row_number * (l_i + 5) + this.m_stockNameViewSize.height - 1);
/*     */ 
/* 287 */         this.m_dbArray1[l_i] = l_dbArray[l_i];
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 292 */       double[] l_data_p = { this.m_daemon.m_P_sale5, this.m_daemon.m_P_sale4, this.m_daemon.m_P_sale3, this.m_daemon.m_P_sale2, this.m_daemon.m_P_sale1, this.m_daemon.m_P_Buy1, this.m_daemon.m_P_Buy2, this.m_daemon.m_P_Buy3, this.m_daemon.m_P_Buy4, this.m_daemon.m_P_Buy5 };
/* 293 */       double[] l_data_v = { this.m_daemon.m_v_sale5, this.m_daemon.m_v_sale4, this.m_daemon.m_v_sale3, this.m_daemon.m_v_sale2, this.m_daemon.m_v_sale1, this.m_daemon.m_v_Buy1, this.m_daemon.m_v_Buy2, this.m_daemon.m_v_Buy3, this.m_daemon.m_v_Buy4, this.m_daemon.m_v_Buy5 };
/*     */ 
/* 296 */       for (int l_i = 0; l_i < row_number - 1; l_i++)
/*     */       {
/* 298 */         for (int l_j = 0; l_j < col_number; l_j++)
/*     */         {
/* 300 */           if (l_j == 0)
/*     */           {
/* 302 */             p_g.setColor(CommEnv.s_textColor);
/*     */ 
/* 304 */             p_g.drawString(s_buy_sale_item_sgn[l_i], this.m_buy_sale_column_align[0], this.m_buySaleViewSize.height / row_number * (l_i + 2) + this.m_stockNameViewSize.height);
/*     */           }
/* 307 */           else if (l_j == 1)
/*     */           {
/* 309 */             p_g.setColor(CommEnv.f_chooseColorByValue(l_data_p[l_i], this.m_daemon.m_P_Close));
/* 310 */             if ((this.m_daemon.f_isBGu()) || (this.m_daemon.f_isJiJin()) || (this.m_daemon.f_isQuanZheng()))
/* 311 */               l_temp = CommEnv.f_changeIfNeeded(p_g, CommEnv.f_scaleDouble(l_data_p[l_i], 3));
/*     */             else {
/* 313 */               l_temp = CommEnv.f_changeIfNeeded(p_g, CommEnv.f_scaleDouble(l_data_p[l_i], 2));
/*     */             }
/* 315 */             p_g.drawString(l_temp, this.m_buy_sale_column_align[1] - l_fm.stringWidth(l_temp), this.m_buySaleViewSize.height / row_number * (l_i + 2) + this.m_stockNameViewSize.height - 1);
/*     */           }
/*     */           else
/*     */           {
/* 320 */             p_g.setColor(CommEnv.s_digitalColor);
/* 321 */             l_temp = CommEnv.f_changeIfNeeded(p_g, CommEnv.f_scaleDouble(l_data_v[l_i] / l_dec, 0));
/*     */ 
/* 323 */             p_g.drawString(l_temp, this.m_buy_sale_column_align[2] - l_fm.stringWidth(l_temp), this.m_buySaleViewSize.height / row_number * (l_i + 2) + this.m_stockNameViewSize.height - 1);
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 333 */     p_g.setColor(CommEnv.s_textColor);
/*     */ 
/* 335 */     int l_delta = this.m_stockNameViewSize.height + this.m_buySaleViewSize.height;
/* 336 */     for (int l_i = 0; l_i < 6; l_i++)
/*     */     {
/* 340 */       p_g.drawString(s_basic_hq_item[(l_i * 2)], this.m_basic_hq_column_align[0], this.m_basicHQViewSize.height / 6 * (l_i + 1) + l_delta - 1);
/* 341 */       p_g.drawString(s_basic_hq_item[(l_i * 2 + 1)], this.m_basic_hq_column_align[2], this.m_basicHQViewSize.height / 6 * (l_i + 1) + l_delta - 1);
/*     */     }
/*     */ 
/* 346 */     p_g.setColor(CommEnv.f_chooseColorByValue(this.m_daemon.m_P_Newest, this.m_daemon.m_P_Close));
/* 347 */     l_temp = CommEnv.f_changeIfNeeded(p_g, CommEnv.f_scaleDouble(this.m_daemon.m_P_Newest, l_scale));
/*     */ 
/* 349 */     p_g.drawString(l_temp, this.m_basic_hq_column_align[1] - l_fm.stringWidth(l_temp), this.m_basicHQViewSize.height / 6 * 1 + l_delta - 2);
/*     */ 
/* 352 */     if (this.m_daemon.m_Variety_ID.equalsIgnoreCase("001"))
/* 353 */       p_g.setColor(CommEnv.f_chooseColorByValue(this.m_daemon.m_P_Newest, this.m_daemon.m_P_Close));
/*     */     else
/* 355 */       p_g.setColor(CommEnv.f_chooseColorByValue(this.m_daemon.m_Ave_Price, this.m_daemon.m_P_Close));
/* 356 */     l_temp = CommEnv.f_changeIfNeeded(p_g, CommEnv.f_scaleDouble(this.m_daemon.m_Ave_Price, l_scale));
/*     */ 
/* 358 */     p_g.drawString(l_temp, this.m_basic_hq_column_align[3] - l_fm.stringWidth(l_temp), this.m_basicHQViewSize.height / 6 * 1 + l_delta - 2);
/*     */ 
/* 361 */     p_g.setColor(CommEnv.f_chooseColorByValue(this.m_daemon.m_P_Newest, this.m_daemon.m_P_Close));
/* 362 */     l_temp = CommEnv.f_changeIfNeeded(p_g, CommEnv.f_scaleDouble(this.m_daemon.m_Up_Down_Val, l_scale));
/*     */ 
/* 364 */     p_g.drawString(l_temp, this.m_basic_hq_column_align[1] - l_fm.stringWidth(l_temp), this.m_basicHQViewSize.height / 6 * 2 + l_delta - 2);
/*     */ 
/* 367 */     p_g.setColor(CommEnv.f_chooseColorByValue(this.m_daemon.m_P_Close, this.m_daemon.m_P_Close));
/* 368 */     l_temp = CommEnv.f_changeIfNeeded(p_g, CommEnv.f_scaleDouble(this.m_daemon.m_P_Close, l_scale));
/*     */ 
/* 370 */     p_g.drawString(l_temp, this.m_basic_hq_column_align[3] - l_fm.stringWidth(l_temp), this.m_basicHQViewSize.height / 6 * 2 + l_delta - 2);
/*     */ 
/* 373 */     p_g.setColor(CommEnv.f_chooseColorByValue(this.m_daemon.m_P_Newest, this.m_daemon.m_P_Close));
/* 374 */     l_temp = CommEnv.f_changeIfNeeded(p_g, CommEnv.f_scaleDouble(this.m_daemon.m_Up_Down_Range * 100.0D, 2));
/* 375 */     l_temp = l_temp + "%";
/*     */ 
/* 377 */     p_g.drawString(l_temp, this.m_basic_hq_column_align[1] - l_fm.stringWidth(l_temp), this.m_basicHQViewSize.height / 6 * 3 + l_delta - 2);
/*     */ 
/* 380 */     p_g.setColor(CommEnv.f_chooseColorByValue(this.m_daemon.m_P_Open, this.m_daemon.m_P_Close));
/* 381 */     l_temp = CommEnv.f_changeIfNeeded(p_g, CommEnv.f_scaleDouble(this.m_daemon.m_P_Open, l_scale));
/*     */ 
/* 383 */     p_g.drawString(l_temp, this.m_basic_hq_column_align[3] - l_fm.stringWidth(l_temp), this.m_basicHQViewSize.height / 6 * 3 + l_delta - 2);
/*     */ 
/* 386 */     p_g.setColor(CommEnv.f_chooseColorByValue(this.m_daemon.m_P_Newest, this.m_daemon.m_P_Close));
/* 387 */     l_temp = CommEnv.f_changeIfNeeded(p_g, CommEnv.f_scaleDouble(this.m_daemon.m_M_Total / 10000.0D, this.m_daemon.m_Variety_ID.equalsIgnoreCase("001") ? 0 : 2));
/*     */ 
/* 389 */     p_g.drawString(l_temp, this.m_basic_hq_column_align[1] - l_fm.stringWidth(l_temp), this.m_basicHQViewSize.height / 6 * 4 + l_delta - 2);
/*     */ 
/* 392 */     p_g.setColor(CommEnv.f_chooseColorByValue(this.m_daemon.m_P_Highest, this.m_daemon.m_P_Close));
/* 393 */     l_temp = CommEnv.f_changeIfNeeded(p_g, CommEnv.f_scaleDouble(this.m_daemon.m_P_Highest, l_scale));
/*     */ 
/* 395 */     p_g.drawString(l_temp, this.m_basic_hq_column_align[3] - l_fm.stringWidth(l_temp), this.m_basicHQViewSize.height / 6 * 4 + l_delta - 2);
/*     */ 
/* 398 */     p_g.setColor(CommEnv.f_chooseColorByValue(this.m_daemon.m_P_Newest, this.m_daemon.m_P_Close));
/* 399 */     l_temp = CommEnv.f_changeIfNeeded(p_g, CommEnv.f_scaleDouble(this.m_daemon.m_V_Total / l_dec, 0));
/*     */ 
/* 401 */     p_g.drawString(l_temp, this.m_basic_hq_column_align[1] - l_fm.stringWidth(l_temp), this.m_basicHQViewSize.height / 6 * 5 + l_delta - 2);
/*     */ 
/* 404 */     p_g.setColor(CommEnv.f_chooseColorByValue(this.m_daemon.m_P_Lowest, this.m_daemon.m_P_Close));
/* 405 */     l_temp = CommEnv.f_changeIfNeeded(p_g, CommEnv.f_scaleDouble(this.m_daemon.m_P_Lowest, l_scale));
/*     */ 
/* 407 */     p_g.drawString(l_temp, this.m_basic_hq_column_align[3] - l_fm.stringWidth(l_temp), this.m_basicHQViewSize.height / 6 * 5 + l_delta - 2);
/*     */ 
/* 410 */     p_g.setColor(CommEnv.f_chooseColorByValue(this.m_daemon.m_cj_type));
/* 411 */     l_temp = CommEnv.f_changeIfNeeded(p_g, CommEnv.f_scaleDouble(this.m_daemon.m_V_Now / l_dec, 0));
/*     */ 
/* 413 */     p_g.drawString(l_temp, this.m_basic_hq_column_align[1] - l_fm.stringWidth(l_temp), this.m_basicHQViewSize.height / 6 * 6 + l_delta - 2);
/*     */ 
/* 416 */     p_g.setColor(CommEnv.f_chooseColorByValue(this.m_daemon.m_Vol_Ratio, 1.0D));
/* 417 */     l_temp = CommEnv.f_changeIfNeeded(p_g, CommEnv.f_scaleDouble(this.m_daemon.m_Vol_Ratio, 2));
/*     */ 
/* 419 */     p_g.drawString(l_temp, this.m_basic_hq_column_align[3] - l_fm.stringWidth(l_temp), this.m_basicHQViewSize.height / 6 * 6 + l_delta - 2);
/*     */   }
/*     */ 
/*     */   public void resetRectangle(Rectangle p_bounds, Rectangle p_buySaleViewSize, Rectangle p_basicHQViewSize, Rectangle p_stockNameViewSize)
/*     */   {
/* 497 */     setBounds(p_bounds);
/* 498 */     setBackground(CommEnv.s_sysBkColor);
/*     */ 
/* 500 */     this.m_stockInfoViewSize = p_bounds;
/* 501 */     this.m_stockNameViewSize = p_stockNameViewSize;
/* 502 */     this.m_buySaleViewSize = p_buySaleViewSize;
/* 503 */     this.m_basicHQViewSize = p_basicHQViewSize;
/*     */ 
/* 505 */     this.m_buy_sale_column_align[0] = 2;
/* 506 */     this.m_buy_sale_column_align[1] = (this.m_buySaleViewSize.width / 3 * 2 - 14);
/* 507 */     this.m_buy_sale_column_align[2] = (this.m_buySaleViewSize.width / 3 * 3 - 14);
/*     */ 
/* 509 */     this.m_basic_hq_column_align[0] = 2;
/* 510 */     this.m_basic_hq_column_align[1] = (this.m_basicHQViewSize.width / 2 + 5);
/* 511 */     this.m_basic_hq_column_align[2] = (this.m_basicHQViewSize.width / 2 + 7);
/* 512 */     this.m_basic_hq_column_align[3] = (this.m_basicHQViewSize.width - 1);
/*     */   }
/*     */ 
/*     */   public void update(Observable p_o, Object p_arg)
/*     */   {
/* 425 */     if (((Integer)p_arg).intValue() == -917)
/* 426 */       return;
/* 427 */     if (((Integer)p_arg).intValue() == -911) {
/* 428 */       this.m_stockName = this.m_daemon.m_Stock_Name;
/* 429 */       this.m_stockCode = this.m_daemon.m_Stock_Code;
/* 430 */       repaint();
/* 431 */       return;
/*     */     }
/*     */ 
/* 434 */     DynamicHQDaemon l_daemon = (DynamicHQDaemon)p_o;
/* 435 */     if (l_daemon.f_isZhiShu())
/*     */     {
/* 437 */       this.m_dbArray[0] = this.m_dbArray1[0];
/* 438 */       this.m_dbArray[1] = this.m_dbArray1[1];
/* 439 */       this.m_dbArray[2] = this.m_dbArray1[2];
/*     */     }
/* 441 */     if (l_daemon.m_Stock_Code.equalsIgnoreCase(this.m_stockCode))
/*     */     {
/* 443 */       int l_singleWordWidth = 14;
/*     */ 
/* 463 */       repaint(2 + l_singleWordWidth * 2, this.m_stockNameViewSize.height + 1, this.m_buySaleViewSize.width - 2 - (2 + l_singleWordWidth * 2), this.m_buySaleViewSize.height);
/*     */ 
/* 469 */       repaint(2 + l_singleWordWidth * 2, this.m_stockNameViewSize.height + this.m_buySaleViewSize.height + 1, this.m_basicHQViewSize.width / 2 + 5 - (2 + l_singleWordWidth * 2), this.m_basicHQViewSize.height);
/*     */ 
/* 475 */       repaint(this.m_basicHQViewSize.width / 2 + 7 + l_singleWordWidth * 2, this.m_stockNameViewSize.height + this.m_buySaleViewSize.height + 1, this.m_basicHQViewSize.width - 2 - (this.m_basicHQViewSize.width / 2 + 7 + l_singleWordWidth * 2), this.m_basicHQViewSize.height);
/*     */     }
/*     */     else
/*     */     {
/* 484 */       this.m_stockName = l_daemon.m_Stock_Name;
/* 485 */       this.m_stockCode = l_daemon.m_Stock_Code;
/*     */       double tmp324_321 = this.m_daemon.m_up_numb; this.m_dbArray1[0] = tmp324_321; this.m_dbArray[0] = tmp324_321;
/*     */       double tmp344_341 = this.m_daemon.m_down_numb; this.m_dbArray1[1] = tmp344_341; this.m_dbArray[1] = tmp344_341;
/*     */       double tmp364_361 = this.m_daemon.m_equal_numb; this.m_dbArray1[2] = tmp364_361; this.m_dbArray[2] = tmp364_361;
/* 489 */       repaint();
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized void update(Graphics p_g)
/*     */   {
/* 111 */     Rectangle l_bounds = getBounds();
/* 112 */     Image l_image = createImage(l_bounds.width, l_bounds.height);
/* 113 */     Graphics l_g = l_image.getGraphics();
/* 114 */     paint(l_g);
/* 115 */     p_g.drawImage(l_image, 0, 0, this);
/*     */   }
/*     */ }

/* Location:           F:\stock research\kline\
 * Qualified Name:     lido.common.StockInfoView
 * JD-Core Version:    0.6.0
 */