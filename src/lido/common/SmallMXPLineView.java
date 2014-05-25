/*     */ package lido.common;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Panel;
/*     */ import java.awt.Rectangle;
/*     */ import java.io.DataInputStream;
/*     */ import java.util.Date;
/*     */ import java.util.Observable;
/*     */ import java.util.Observer;
/*     */ 
/*     */ public class SmallMXPLineView extends Panel
/*     */   implements Observer
/*     */ {
/*     */   private int[] m_MX_align;
/*  37 */   private double m_PreVTotal = 0.0D;
/*     */ 
/*  36 */   private double m_VTotal = 0.0D;
/*     */   private Rectangle m_basicHQViewSize;
/*     */   private int[] m_basic_hq_column_align;
/*     */   String[] m_cj_type;
/*     */   DynamicHQDaemon m_daemon;
/*     */   double[][] m_data;
/*     */   private boolean m_firstRecord;
/*     */   private String m_hostAndPath;
/*     */   private boolean m_initFinished;
/*     */   private int m_list;
/*     */   private int m_rowUsed;
/*     */   private Rectangle m_smallMXPLineViewSize;
/*     */   String m_stock_code;
/*     */   Date[] m_tradeDate;
/*     */ 
/*     */   public SmallMXPLineView(DynamicHQDaemon p_masterDaemon, Rectangle p_bounds, String p_stockCode, Rectangle p_basicHQViewSize, String p_hostAndPath)
/*     */   {
/*  47 */     setBounds(p_bounds);
/*  48 */     setBackground(CommEnv.s_sysBkColor);
/*  49 */     setFont(CommEnv.s_smallFont);
/*     */ 
/*  51 */     this.m_hostAndPath = p_hostAndPath;
/*  52 */     this.m_smallMXPLineViewSize = p_bounds;
/*  53 */     this.m_list = (this.m_smallMXPLineViewSize.height / 16 - 1);
/*  54 */     this.m_basicHQViewSize = p_basicHQViewSize;
/*  55 */     this.m_MX_align = new int[4];
/*  56 */     this.m_MX_align[0] = 2;
/*  57 */     this.m_MX_align[1] = (this.m_smallMXPLineViewSize.width / 2 + 15);
/*  58 */     this.m_MX_align[2] = (this.m_smallMXPLineViewSize.width - 10);
/*  59 */     this.m_MX_align[3] = (this.m_smallMXPLineViewSize.width - 1);
/*  60 */     this.m_basic_hq_column_align = new int[4];
/*  61 */     this.m_basic_hq_column_align[0] = 2;
/*  62 */     this.m_basic_hq_column_align[1] = (this.m_basicHQViewSize.width / 2 + 5);
/*  63 */     this.m_basic_hq_column_align[2] = (this.m_basicHQViewSize.width / 2 + 7);
/*  64 */     this.m_basic_hq_column_align[3] = (this.m_basicHQViewSize.width - 1);
/*  65 */     this.m_stock_code = p_stockCode;
/*     */ 
/*  69 */     this.m_tradeDate = new Date[this.m_list + 1];
/*  70 */     this.m_cj_type = new String[this.m_list + 1];
/*  71 */     this.m_data = new double[this.m_list + 1][3];
/*  72 */     this.m_rowUsed = 0;
/*  73 */     this.m_initFinished = false;
/*     */ 
/*  75 */     this.m_daemon = p_masterDaemon;
/*  76 */     this.m_daemon.addObserver(this);
/*     */ 
/*  78 */     this.m_VTotal = this.m_daemon.m_V_Total;
/*  79 */     update(this.m_daemon, new Integer(-901));
/*  80 */     this.m_firstRecord = false;
/*     */   }
/*     */ 
/*     */   public void f_destroy()
/*     */   {
/* 110 */     System.gc();
/*     */   }
/*     */ 
/*     */   public boolean f_getInitStatus()
/*     */   {
/*  88 */     return this.m_initFinished;
/*     */   }
/*     */ 
/*     */   private void f_setData(int l_used)
/*     */   {
/* 684 */     this.m_stock_code = this.m_daemon.m_Stock_Code;
/* 685 */     this.m_tradeDate[l_used] = this.m_daemon.m_Trade_Time;
/* 686 */     this.m_cj_type[l_used] = this.m_daemon.m_cj_type;
/*     */ 
/* 688 */     this.m_data[l_used][0] = this.m_daemon.m_P_Newest;
/*     */ 
/* 690 */     this.m_data[l_used][1] = this.m_daemon.m_V_Total;
/* 691 */     this.m_data[l_used][2] = this.m_daemon.m_P_Close;
/*     */   }
/*     */ 
/*     */   public void f_setStockCode(String p_stockCode, DataInputStream p_in)
/*     */   {
/* 116 */     this.m_stock_code = p_stockCode;
/* 117 */     String l_connStr = this.m_hostAndPath + "?query_type=43&code=" + this.m_stock_code + "&rec_num=" + (this.m_list + 1);
/*     */ 
/* 121 */     DataInputStream l_in = null;
/* 122 */     if (p_in == null)
/* 123 */       l_in = CommEnv.f_urlConn(l_connStr);
/*     */     else {
/* 125 */       l_in = p_in;
/*     */     }
/* 127 */     if (l_in == null)
/*     */     {
/* 130 */       this.m_initFinished = true;
/* 131 */       return;
/*     */     }
/*     */     try
/*     */     {
/* 135 */       int l_sign = l_in.readInt();
/*     */ 
/* 137 */       if (l_sign != 4300)
/*     */       {
/* 140 */         this.m_initFinished = true;
/* 141 */         return;
/*     */       }
/*     */ 
/* 145 */       int l_actualNum = l_in.readInt();
/*     */ 
/* 147 */       double[] l_price = new double[l_actualNum];
/* 148 */       double[] l_v_total = new double[l_actualNum];
/* 149 */       double[] l_p_close = new double[l_actualNum];
/* 150 */       Date[] l_tradeTime = new Date[l_actualNum];
/* 151 */       String[] l_cj_type = new String[l_actualNum];
/*     */ 
/* 153 */       for (int l_i = 0; l_i < l_actualNum; l_i++)
/*     */       {
/* 155 */         l_tradeTime[l_i] = new Date();
/* 156 */         l_cj_type[l_i] = "";
/*     */ 
/* 158 */         l_price[l_i] = l_in.readDouble();
/* 159 */         l_v_total[l_i] = l_in.readDouble();
/* 160 */         l_p_close[l_i] = l_in.readDouble();
/* 161 */         l_tradeTime[l_i] = new Date(l_in.readLong());
/* 162 */         l_cj_type[l_i] = l_in.readUTF().trim();
/*     */       }
/* 164 */       this.m_rowUsed = 0;
/*     */ 
/* 166 */       int l_currentLine = this.m_rowUsed;
/* 167 */       int l_endLine = this.m_rowUsed;
/*     */ 
/* 172 */       for (int l_i = 0; l_i < this.m_rowUsed; l_i++)
/*     */       {
/* 174 */         if (this.m_tradeDate[l_i].getTime() <= l_tradeTime[(l_actualNum - 1)].getTime())
/*     */           continue;
/* 176 */         l_currentLine = l_i;
/* 177 */         break;
/*     */       }
/*     */ 
/* 181 */       Date[] l_temp_tradeTime = new Date[this.m_rowUsed - l_currentLine];
/* 182 */       String[] l_temp_cj_type = new String[this.m_rowUsed - l_currentLine];
/* 183 */       double[] l_temp_price = new double[this.m_rowUsed - l_currentLine];
/* 184 */       double[] l_temp_v_total = new double[this.m_rowUsed - l_currentLine];
/* 185 */       double[] l_temp_p_close = new double[this.m_rowUsed - l_currentLine];
/*     */ 
/* 187 */       for (int l_i = l_currentLine; l_i < l_endLine; l_i++)
/*     */       {
/* 189 */         l_temp_tradeTime[(l_i - l_currentLine)] = this.m_tradeDate[l_i];
/* 190 */         l_temp_cj_type[(l_i - l_currentLine)] = this.m_cj_type[l_i];
/* 191 */         l_temp_price[(l_i - l_currentLine)] = this.m_data[l_i][0];
/* 192 */         l_temp_v_total[(l_i - l_currentLine)] = this.m_data[l_i][1];
/* 193 */         l_temp_p_close[(l_i - l_currentLine)] = this.m_data[l_i][2];
/*     */       }
/*     */ 
/* 196 */       int l_startLine = l_actualNum + (l_endLine - l_currentLine) - 10;
/* 197 */       if (l_startLine < 0)
/* 198 */         l_startLine = 0;
/* 199 */       synchronized (this)
/*     */       {
/* 201 */         this.m_rowUsed = 0;
/*     */ 
/* 203 */         if (l_actualNum < this.m_list)
/*     */         {
/* 205 */           for (int l_i = 0; l_i < l_actualNum; l_i++)
/*     */           {
/* 207 */             this.m_tradeDate[this.m_rowUsed] = l_tradeTime[l_i];
/* 208 */             this.m_cj_type[this.m_rowUsed] = l_cj_type[l_i];
/* 209 */             this.m_data[this.m_rowUsed][0] = l_price[l_i];
/* 210 */             this.m_data[this.m_rowUsed][1] = l_v_total[l_i];
/* 211 */             this.m_data[this.m_rowUsed][2] = l_p_close[l_i];
/* 212 */             this.m_rowUsed += 1;
/*     */           }
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/* 219 */           for (int l_i = l_actualNum - this.m_list; l_i < l_actualNum; l_i++)
/*     */           {
/* 221 */             this.m_tradeDate[this.m_rowUsed] = l_tradeTime[l_i];
/* 222 */             this.m_cj_type[this.m_rowUsed] = l_cj_type[l_i];
/* 223 */             this.m_data[this.m_rowUsed][0] = l_price[l_i];
/* 224 */             this.m_data[this.m_rowUsed][1] = l_v_total[l_i];
/* 225 */             this.m_data[this.m_rowUsed][2] = l_p_close[l_i];
/* 226 */             this.m_rowUsed += 1;
/*     */           }
/*     */         }
/* 229 */         for (int l_i = l_currentLine; l_i < l_endLine; l_i++)
/*     */         {
/* 232 */           this.m_tradeDate[this.m_rowUsed] = l_temp_tradeTime[l_i];
/* 233 */           this.m_cj_type[this.m_rowUsed] = l_temp_cj_type[l_i];
/* 234 */           this.m_data[this.m_rowUsed][0] = l_temp_price[l_i];
/* 235 */           this.m_data[this.m_rowUsed][1] = l_temp_v_total[l_i];
/* 236 */           this.m_data[this.m_rowUsed][2] = l_temp_p_close[l_i];
/* 237 */           this.m_rowUsed += 1;
/*     */         }
/*     */ 
/* 240 */         this.m_VTotal = this.m_data[(this.m_rowUsed - 1)][1];
/* 241 */         if (l_startLine > 0)
/*     */         {
/* 244 */           this.m_PreVTotal = l_v_total[(l_actualNum - this.m_list - 1)];
/*     */         }
/* 246 */         else if (l_startLine == 0)
/*     */         {
/* 248 */           if (l_actualNum > this.m_list)
/* 249 */             this.m_PreVTotal = l_v_total[(l_actualNum - this.m_list - 1)];
/*     */           else {
/* 251 */             this.m_PreVTotal = 0.0D;
/*     */           }
/*     */         }
/*     */         else {
/* 255 */           this.m_PreVTotal = 0.0D;
/*     */         }
/*     */ 
/* 258 */         this.m_initFinished = true;
/*     */       }
/*     */ 
/*     */     }
/*     */     catch (Exception l_e)
/*     */     {
/* 264 */       l_e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void f_start(DataInputStream p_in)
/*     */   {
/*  93 */     this.m_rowUsed = 0;
/*  94 */     this.m_PreVTotal = 0.0D;
/*  95 */     this.m_VTotal = 0.0D;
/*  96 */     this.m_stock_code = this.m_daemon.m_Stock_Code;
/*     */ 
/*  99 */     System.gc();
/* 100 */     f_setStockCode(this.m_stock_code, p_in);
/*     */   }
/*     */ 
/*     */   public void paint(Graphics p_g)
/*     */   {
/* 281 */     int l_dec = this.m_daemon.f_isZhaiQuan() ? 10 : 100;
/* 282 */     int l_scale = (this.m_daemon.f_isBGu()) || (this.m_daemon.f_isJiJin()) || (this.m_daemon.f_isQuanZheng()) ? 3 : 2;
/* 283 */     p_g.setFont(CommEnv.s_smallFont);
/* 284 */     FontMetrics l_fm = p_g.getFontMetrics();
/*     */ 
/* 287 */     p_g.setColor(CommEnv.s_textColor);
/*     */ 
/* 290 */     p_g.drawString("мБел", this.m_basic_hq_column_align[0], this.m_smallMXPLineViewSize.height / (this.m_list + 1) - 1);
/* 291 */     p_g.drawString("дзел", this.m_basic_hq_column_align[2] - 5, this.m_smallMXPLineViewSize.height / (this.m_list + 1) - 1);
/*     */ 
/* 293 */     Color[] l_ctemp = { CommEnv.s_hqUpColor, CommEnv.s_hqDownColor };
/* 294 */     double[] l_dl = { this.m_daemon.m_v_outer, this.m_daemon.m_v_inner };
/*     */     String l_temp;
/* 296 */     for (int l_i = 0; l_i < 2; l_i++)
/*     */     {
/* 298 */       p_g.setColor(l_ctemp[l_i]);
/* 299 */       l_temp = CommEnv.f_changeIfNeeded(p_g, CommEnv.f_scaleDouble(l_dl[l_i] / l_dec, 0));
/*     */ 
/* 301 */       if (l_i == 0)
/*     */       {
/* 303 */         p_g.drawString(l_temp, this.m_basic_hq_column_align[(l_i * 2 + 1)] - l_fm.stringWidth(l_temp) - 3, this.m_smallMXPLineViewSize.height / (this.m_list + 1) - 1);
/*     */       }
/*     */       else {
/* 306 */         p_g.drawString(l_temp, this.m_basic_hq_column_align[(l_i * 2 + 1)] - l_fm.stringWidth(l_temp), this.m_smallMXPLineViewSize.height / (this.m_list + 1) - 1);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 312 */     int l_adjust = 3;
/*     */ 
/* 314 */     if ((this.m_daemon.m_V_Total <= 0.0D) || (this.m_daemon.m_P_Newest <= 0.0D))
/*     */     {
/* 316 */       this.m_rowUsed = 0;
/*     */ 
/* 318 */       return;
/*     */     }
/*     */ 
/* 323 */     if (this.m_rowUsed == 0)
/*     */     {
/* 326 */       for (int l_j = 0; l_j < 3; l_j++)
/*     */       {
/* 328 */         if (l_j == 0)
/*     */         {
/* 330 */           p_g.setColor(CommEnv.s_digitalColor);
/* 331 */           int l_hour = this.m_daemon.m_Trade_Time.getHours();
/*     */ 
/* 333 */           l_temp = l_hour < 10 ? " " : "";
/* 334 */           l_temp = l_temp + Integer.toString(l_hour) + ":";
/* 335 */           int l_min = this.m_daemon.m_Trade_Time.getMinutes();
/* 336 */           l_temp = l_temp + (l_min < 10 ? "0" : "");
/* 337 */           l_temp = l_temp + Integer.toString(l_min) + ":";
/* 338 */           int l_sec = this.m_daemon.m_Trade_Time.getSeconds();
/* 339 */           l_temp = l_temp + (l_sec < 10 ? "0" : "");
/* 340 */           l_temp = l_temp + Integer.toString(l_sec);
/* 341 */           p_g.drawString(l_temp, this.m_MX_align[0], this.m_smallMXPLineViewSize.height / (this.m_list + 1) * 2 - 4 + l_adjust);
/*     */         }
/* 343 */         else if (l_j == 1)
/*     */         {
/* 345 */           p_g.setColor(CommEnv.f_chooseColorByValue(this.m_daemon.m_P_Newest, this.m_daemon.m_P_Close));
/* 346 */           l_temp = CommEnv.f_changeIfNeeded(p_g, CommEnv.f_scaleDouble(this.m_daemon.m_P_Newest, l_scale));
/* 347 */           p_g.drawString(l_temp, this.m_MX_align[1] - l_fm.stringWidth(l_temp), this.m_smallMXPLineViewSize.height / (this.m_list + 1) * 2 - 4 + l_adjust);
/*     */         }
/*     */         else
/*     */         {
/* 352 */           p_g.setColor(CommEnv.f_chooseColorByValue(this.m_daemon.m_P_Newest, this.m_daemon.m_P_Close));
/* 353 */           if (this.m_daemon.f_isZhaiQuan())
/* 354 */             l_temp = CommEnv.f_changeIfNeeded(p_g, CommEnv.f_scaleDouble(this.m_daemon.m_V_Now / 10.0D, 0));
/*     */           else {
/* 356 */             l_temp = CommEnv.f_changeIfNeeded(p_g, CommEnv.f_scaleDouble(this.m_daemon.m_V_Now / 100.0D, 0));
/*     */           }
/*     */ 
/* 359 */           p_g.drawString(l_temp, this.m_MX_align[2] - l_fm.stringWidth(l_temp), this.m_smallMXPLineViewSize.height / (this.m_list + 1) * 2 - 4 + l_adjust);
/*     */         }
/*     */       }
/* 362 */       p_g.setColor(CommEnv.f_chooseColorByValue(this.m_daemon.m_cj_type));
/*     */ 
/* 364 */       if (this.m_daemon.m_cj_type.equalsIgnoreCase("up"))
/*     */       {
/* 366 */         p_g.drawLine(this.m_MX_align[3] - 4, this.m_smallMXPLineViewSize.height / (this.m_list + 1) * 2 - 4 + l_adjust, this.m_MX_align[3] - 4, this.m_smallMXPLineViewSize.height / (this.m_list + 1) * 2 - 13 + l_adjust);
/*     */ 
/* 374 */         int[] l_xArray = { this.m_MX_align[3] - 4, this.m_MX_align[3] - 8, this.m_MX_align[3] };
/*     */ 
/* 379 */         int[] l_yArray = { this.m_smallMXPLineViewSize.height / (this.m_list + 1) * 2 - 14 + l_adjust, this.m_smallMXPLineViewSize.height / (this.m_list + 1) * 2 - 9 + l_adjust, this.m_smallMXPLineViewSize.height / (this.m_list + 1) * 2 - 9 + l_adjust };
/*     */ 
/* 384 */         p_g.fillPolygon(l_xArray, l_yArray, 3);
/*     */       }
/* 386 */       else if (this.m_daemon.m_cj_type.equalsIgnoreCase("dn"))
/*     */       {
/* 388 */         p_g.drawLine(this.m_MX_align[3] - 4, this.m_smallMXPLineViewSize.height / (this.m_list + 1) * 2 - 4 + l_adjust, this.m_MX_align[3] - 4, this.m_smallMXPLineViewSize.height / (this.m_list + 1) * 2 - 13 + l_adjust);
/*     */ 
/* 395 */         int[] l_xArray = { this.m_MX_align[3] - 4, this.m_MX_align[3] - 7, this.m_MX_align[3] };
/*     */ 
/* 400 */         int[] l_yArray = { this.m_smallMXPLineViewSize.height / (this.m_list + 1) * 2 - 3 + l_adjust, this.m_smallMXPLineViewSize.height / (this.m_list + 1) * 2 - 7 + l_adjust, this.m_smallMXPLineViewSize.height / (this.m_list + 1) * 2 - 7 + l_adjust };
/*     */ 
/* 405 */         p_g.fillPolygon(l_xArray, l_yArray, 3);
/*     */       }
/*     */       else
/*     */       {
/* 409 */         p_g.drawLine(this.m_MX_align[3] - 8, this.m_smallMXPLineViewSize.height / (this.m_list + 1) * 2 - 9 + l_adjust, this.m_MX_align[3] - 1, this.m_smallMXPLineViewSize.height / (this.m_list + 1) * 2 - 9 + l_adjust);
/*     */ 
/* 416 */         p_g.drawLine(this.m_MX_align[3] - 8, this.m_smallMXPLineViewSize.height / (this.m_list + 1) * 2 - 7 + l_adjust, this.m_MX_align[3] - 1, this.m_smallMXPLineViewSize.height / (this.m_list + 1) * 2 - 7 + l_adjust);
/*     */       }
/*     */ 
/* 424 */       this.m_firstRecord = true;
/*     */     }
/*     */     else
/*     */     {
/* 431 */       for (int l_i = this.m_rowUsed - 1; l_i >= 0; l_i--)
/*     */       {
/* 435 */         for (int l_j = 0; l_j < 3; l_j++)
/*     */         {
/* 437 */           if (l_j == 0)
/*     */           {
/* 439 */             p_g.setColor(CommEnv.s_digitalColor);
/* 440 */             int l_hour = this.m_tradeDate[l_i].getHours();
/* 441 */             l_temp = l_hour < 10 ? " " : "";
/* 442 */             l_temp = l_temp + Integer.toString(l_hour) + ":";
/* 443 */             int l_min = this.m_tradeDate[l_i].getMinutes();
/* 444 */             l_temp = l_temp + (l_min < 10 ? "0" : "");
/* 445 */             l_temp = l_temp + Integer.toString(l_min) + ":";
/* 446 */             int l_sec = this.m_tradeDate[l_i].getSeconds();
/* 447 */             l_temp = l_temp + (l_sec < 10 ? "0" : "");
/* 448 */             l_temp = l_temp + Integer.toString(l_sec);
/*     */ 
/* 452 */             p_g.drawString(l_temp, this.m_MX_align[0], this.m_smallMXPLineViewSize.height / (this.m_list + 1) * (this.m_rowUsed - l_i + 1) - 4 + l_adjust);
/*     */           }
/* 454 */           else if (l_j == 1)
/*     */           {
/* 456 */             p_g.setColor(CommEnv.f_chooseColorByValue(this.m_data[l_i][0], this.m_data[l_i][2]));
/* 457 */             l_temp = CommEnv.f_changeIfNeeded(p_g, CommEnv.f_scaleDouble(this.m_data[l_i][0], l_scale));
/*     */ 
/* 461 */             p_g.drawString(l_temp, this.m_MX_align[1] - l_fm.stringWidth(l_temp), this.m_smallMXPLineViewSize.height / (this.m_list + 1) * (this.m_rowUsed - l_i + 1) - 4 + l_adjust);
/*     */           }
/*     */           else
/*     */           {
/* 465 */             p_g.setColor(CommEnv.f_chooseColorByValue(this.m_data[l_i][0], this.m_data[l_i][2]));
/* 466 */             if (l_i == 0)
/*     */             {
/* 468 */               if (this.m_daemon.f_isZhaiQuan())
/* 469 */                 l_temp = CommEnv.f_changeIfNeeded(p_g, CommEnv.f_scaleDouble((this.m_data[l_i][1] - this.m_PreVTotal) / 10.0D, 0));
/*     */               else {
/* 471 */                 l_temp = CommEnv.f_changeIfNeeded(p_g, CommEnv.f_scaleDouble((this.m_data[l_i][1] - this.m_PreVTotal) / 100.0D, 0));
/*     */               }
/*     */ 
/*     */             }
/* 475 */             else if (this.m_daemon.f_isZhaiQuan())
/* 476 */               l_temp = CommEnv.f_changeIfNeeded(p_g, CommEnv.f_scaleDouble((this.m_data[l_i][1] - this.m_data[(l_i - 1)][1]) / 10.0D, 0));
/*     */             else {
/* 478 */               l_temp = CommEnv.f_changeIfNeeded(p_g, CommEnv.f_scaleDouble((this.m_data[l_i][1] - this.m_data[(l_i - 1)][1]) / 100.0D, 0));
/*     */             }
/*     */ 
/* 482 */             p_g.drawString(l_temp, this.m_MX_align[2] - l_fm.stringWidth(l_temp), this.m_smallMXPLineViewSize.height / (this.m_list + 1) * (this.m_rowUsed - l_i + 1) - 4 + l_adjust);
/*     */           }
/*     */ 
/* 486 */           p_g.setColor(CommEnv.f_chooseColorByValue(this.m_cj_type[l_i]));
/* 487 */           if (this.m_cj_type[l_i].equalsIgnoreCase("up"))
/*     */           {
/* 490 */             p_g.drawLine(this.m_MX_align[3] - 4, this.m_smallMXPLineViewSize.height / (this.m_list + 1) * (this.m_rowUsed - l_i + 1) - 4 + l_adjust, this.m_MX_align[3] - 4, this.m_smallMXPLineViewSize.height / (this.m_list + 1) * (this.m_rowUsed - l_i + 1) - 13 + l_adjust);
/*     */ 
/* 501 */             int[] l_xArray = { this.m_MX_align[3] - 4, this.m_MX_align[3] - 8, this.m_MX_align[3] };
/*     */ 
/* 509 */             int[] l_yArray = { this.m_smallMXPLineViewSize.height / (this.m_list + 1) * (this.m_rowUsed - l_i + 1) - 14 + l_adjust, this.m_smallMXPLineViewSize.height / (this.m_list + 1) * (this.m_rowUsed - l_i + 1) - 9 + l_adjust, this.m_smallMXPLineViewSize.height / (this.m_list + 1) * (this.m_rowUsed - l_i + 1) - 9 + l_adjust };
/*     */ 
/* 517 */             p_g.fillPolygon(l_xArray, l_yArray, 3);
/*     */           }
/* 519 */           else if (this.m_cj_type[l_i].equalsIgnoreCase("dn"))
/*     */           {
/* 521 */             p_g.drawLine(this.m_MX_align[3] - 4, this.m_smallMXPLineViewSize.height / (this.m_list + 1) * (this.m_rowUsed - l_i + 1) - 4 + l_adjust, this.m_MX_align[3] - 4, this.m_smallMXPLineViewSize.height / (this.m_list + 1) * (this.m_rowUsed - l_i + 1) - 13 + l_adjust);
/*     */ 
/* 533 */             int[] l_xArray = { this.m_MX_align[3] - 4, this.m_MX_align[3] - 7, this.m_MX_align[3] };
/*     */ 
/* 541 */             int[] l_yArray = { this.m_smallMXPLineViewSize.height / (this.m_list + 1) * (this.m_rowUsed - l_i + 1) - 3 + l_adjust, this.m_smallMXPLineViewSize.height / (this.m_list + 1) * (this.m_rowUsed - l_i + 1) - 7 + l_adjust, this.m_smallMXPLineViewSize.height / (this.m_list + 1) * (this.m_rowUsed - l_i + 1) - 7 + l_adjust };
/*     */ 
/* 549 */             p_g.fillPolygon(l_xArray, l_yArray, 3);
/*     */           }
/*     */           else
/*     */           {
/* 553 */             p_g.drawLine(this.m_MX_align[3] - 8, this.m_smallMXPLineViewSize.height / (this.m_list + 1) * (this.m_rowUsed - l_i + 1) - 9 + l_adjust, this.m_MX_align[3] - 1, this.m_smallMXPLineViewSize.height / (this.m_list + 1) * (this.m_rowUsed - l_i + 1) - 9 + l_adjust);
/*     */ 
/* 563 */             p_g.drawLine(this.m_MX_align[3] - 8, this.m_smallMXPLineViewSize.height / (this.m_list + 1) * (this.m_rowUsed - l_i + 1) - 7 + l_adjust, this.m_MX_align[3] - 1, this.m_smallMXPLineViewSize.height / (this.m_list + 1) * (this.m_rowUsed - l_i + 1) - 7 + l_adjust);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void resetRectangle(Rectangle p_bounds, Rectangle p_basicHQViewSize)
/*     */   {
/* 696 */     setBounds(p_bounds);
/* 697 */     setBackground(CommEnv.s_sysBkColor);
/* 698 */     setFont(CommEnv.s_smallFont);
/*     */ 
/* 700 */     this.m_smallMXPLineViewSize = p_bounds;
/* 701 */     this.m_list = (this.m_smallMXPLineViewSize.height / 16 - 1);
/* 702 */     this.m_basicHQViewSize = p_basicHQViewSize;
/* 703 */     this.m_MX_align[0] = 2;
/* 704 */     this.m_MX_align[1] = (this.m_smallMXPLineViewSize.width / 2 + 15);
/* 705 */     this.m_MX_align[2] = (this.m_smallMXPLineViewSize.width - 10);
/* 706 */     this.m_MX_align[3] = (this.m_smallMXPLineViewSize.width - 1);
/* 707 */     this.m_basic_hq_column_align[0] = 2;
/* 708 */     this.m_basic_hq_column_align[1] = (this.m_basicHQViewSize.width / 2 + 5);
/* 709 */     this.m_basic_hq_column_align[2] = (this.m_basicHQViewSize.width / 2 + 7);
/* 710 */     this.m_basic_hq_column_align[3] = (this.m_basicHQViewSize.width - 1);
/*     */ 
/* 712 */     this.m_list = (this.m_smallMXPLineViewSize.height / 16 - 1);
/* 713 */     this.m_tradeDate = new Date[this.m_list + 1];
/* 714 */     this.m_cj_type = new String[this.m_list + 1];
/* 715 */     this.m_data = new double[this.m_list + 1][3];
/*     */   }
/*     */ 
/*     */   public void update(Observable p_o, Object p_arg)
/*     */   {
/* 583 */     if (((Integer)p_arg).intValue() == -917)
/* 584 */       return;
/* 585 */     if (((Integer)p_arg).intValue() == -916)
/*     */     {
/* 587 */       this.m_rowUsed = 0;
/* 588 */       this.m_PreVTotal = 0.0D;
/* 589 */       repaint();
/*     */     }
/*     */ 
/* 600 */     if (this.m_stock_code.equalsIgnoreCase(this.m_daemon.m_Stock_Code))
/*     */     {
/* 610 */       if ((this.m_rowUsed > 0) && (this.m_rowUsed < this.m_list) && (this.m_VTotal < this.m_daemon.m_V_Total - 1.E-005D))
/*     */       {
/* 613 */         if (this.m_daemon.m_P_Newest != 0.0D)
/*     */         {
/* 615 */           this.m_VTotal = this.m_daemon.m_V_Total;
/* 616 */           f_setData(this.m_rowUsed);
/* 617 */           this.m_rowUsed += 1;
/*     */         }
/* 619 */         repaint();
/*     */       }
/* 621 */       if ((this.m_rowUsed == 0) && (this.m_daemon.m_V_Total > 0.0D))
/*     */       {
/* 624 */         if (this.m_daemon.m_P_Newest != 0.0D)
/*     */         {
/* 626 */           this.m_VTotal = this.m_daemon.m_V_Total;
/* 627 */           f_setData(this.m_rowUsed);
/* 628 */           this.m_rowUsed += 1;
/*     */         }
/*     */ 
/*     */       }
/* 634 */       else if (this.m_VTotal < this.m_daemon.m_V_Total - 1.E-005D)
/*     */       {
/* 637 */         if (this.m_daemon.m_P_Newest != 0.0D)
/*     */         {
/* 639 */           this.m_PreVTotal = this.m_data[0][1];
/* 640 */           this.m_VTotal = this.m_daemon.m_V_Total;
/*     */ 
/* 642 */           for (int l_i = 0; l_i < this.m_list - 1; l_i++)
/*     */           {
/* 644 */             this.m_tradeDate[l_i] = new Date(this.m_tradeDate[(l_i + 1)].getTime());
/* 645 */             this.m_cj_type[l_i] = new String(this.m_cj_type[(l_i + 1)]);
/*     */ 
/* 647 */             for (int l_j = 0; l_j < 3; l_j++)
/*     */             {
/* 649 */               this.m_data[l_i][l_j] = this.m_data[(l_i + 1)][l_j];
/*     */             }
/*     */           }
/* 652 */           f_setData(this.m_rowUsed - 1);
/*     */         }
/* 654 */         repaint();
/*     */       }
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 660 */       this.m_rowUsed = 0;
/* 661 */       this.m_PreVTotal = 0.0D;
/* 662 */       this.m_VTotal = 0.0D;
/* 663 */       repaint();
/* 664 */       this.m_stock_code = this.m_daemon.m_Stock_Code;
/* 665 */       if (this.m_daemon.m_P_Newest != 0.0D)
/*     */       {
/* 667 */         this.m_VTotal = this.m_daemon.m_V_Total;
/* 668 */         f_setData(this.m_rowUsed);
/* 669 */         this.m_rowUsed += 1;
/*     */       }
/* 671 */       repaint();
/* 672 */       System.gc();
/*     */ 
/* 675 */       boolean l_initStatus = f_getInitStatus();
/* 676 */       while (!l_initStatus)
/* 677 */         l_initStatus = f_getInitStatus();
/* 678 */       repaint();
/*     */     }
/*     */   }
/*     */ }

/* Location:           F:\stock research\kline\
 * Qualified Name:     lido.common.SmallMXPLineView
 * JD-Core Version:    0.6.0
 */