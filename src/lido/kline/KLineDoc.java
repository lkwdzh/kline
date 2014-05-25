/*     */ package lido.kline;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.Date;
/*     */ import java.util.Observable;
/*     */ import java.util.Observer;
/*     */ import lido.common.CommEnv;
/*     */ import lido.common.DynamicHQDaemon;
/*     */ import lido.common.SmallMXPLineView;
/*     */ 
/*     */ class KLineDoc extends Observable
/*     */   implements Observer
/*     */ {
/*     */   private int m_cycleType;
/*  37 */   private KLineRec m_dynkRec = new KLineRec();
/*     */   private HistoryKLineDaemon m_hiskDaemon;
/*  48 */   boolean m_hiskDaemonFinished = false;
/*     */   private Date m_hiskEndDate;
/*     */   private KLineRec[] m_hiskRecBuffer;
/*     */   private int m_hiskRecCount;
/*     */   private String m_historyKLineUrlHeader;
/*     */   private DynamicHQDaemon m_hqDaemon;
/*  47 */   private boolean m_isCode = false;
/*     */   boolean m_launchHqDaemon;
/*     */   private int m_mergeMethod;
/*     */   SmallMXPLineView m_smallMXPLineView;
/*     */   private String m_stockCode;
/*     */   private int m_validRecPos;
/*     */   private static final int s_APPEND = 1;
/*     */   private static final int s_DISCARD = 0;
/*     */   private static final int s_MERGE = 2;
/*  20 */   public static final String[] s_cycle = { "day", "week", "mon" };
/*     */   public static final int s_dayCycle = 0;
/*     */   public static final int s_defaultCycleType = 0;
/*     */   public static final int s_monCycle = 2;
/*     */   private static final String s_queryKLineInfo = "41";
/*     */   private static final int s_retKLineInfoOK = 4100;
/*     */   private static final int s_retNoKLineRec = 4101;
/*     */   public static final int s_weekCycle = 1;
/*     */ 
/*     */   public KLineDoc(DynamicHQDaemon p_hqDaemon, String p_stockCode, int p_cycleType, String p_historyKLineUrlHeader)
/*     */   {
/*  53 */     this.m_stockCode = p_stockCode;
/*  54 */     this.m_cycleType = p_cycleType;
/*     */ 
/*  56 */     this.m_historyKLineUrlHeader = p_historyKLineUrlHeader;
/*     */ 
/*  58 */     this.m_hqDaemon = p_hqDaemon;
/*  59 */     this.m_hiskDaemon = new HistoryKLineDaemon(this.m_historyKLineUrlHeader);
/*  60 */     this.m_hiskDaemon.addObserver(this);
/*     */   }
/*     */ 
/*     */   public void f_destroy()
/*     */   {
/*  84 */     this.m_hiskDaemon.f_stop();
/*     */   }
/*     */ 
/*     */   private synchronized void f_fillTodayKRec()
/*     */   {
/* 243 */     this.m_dynkRec.m_tradeDate = this.m_hqDaemon.m_Trade_Time;
/* 244 */     this.m_dynkRec.m_open = this.m_hqDaemon.m_P_Open;
/* 245 */     this.m_dynkRec.m_close = this.m_hqDaemon.m_P_Newest;
/* 246 */     this.m_dynkRec.m_high = this.m_hqDaemon.m_P_Highest;
/* 247 */     this.m_dynkRec.m_low = this.m_hqDaemon.m_P_Lowest;
/* 248 */     this.m_dynkRec.m_volume = this.m_hqDaemon.m_V_Total;
/* 249 */     this.m_dynkRec.m_money = this.m_hqDaemon.m_M_Total;
/*     */   }
/*     */ 
/*     */   public int f_getCycleType()
/*     */   {
/* 140 */     return this.m_cycleType;
/*     */   }
/*     */ 
/*     */   public boolean f_getHistoryDaemonStatus()
/*     */   {
/*  76 */     if ((this.m_hiskDaemon != null) && (!this.m_isCode)) {
/*  77 */       return this.m_hiskDaemon.m_initHistoryKline;
/*     */     }
/*  79 */     return true;
/*     */   }
/*     */ 
/*     */   public KLineRec f_getRec(int p_pos)
/*     */   {
/*     */     try
/*     */     {
/* 158 */       if (p_pos == f_getRecCount() - 1)
/*     */       {
/* 160 */         if (this.m_mergeMethod == 1)
/* 161 */           return this.m_dynkRec;
/* 162 */         KLineRec l_hisk = this.m_hiskRecBuffer[p_pos];
/* 163 */         if (this.m_mergeMethod == 0)
/* 164 */           return l_hisk;
/* 165 */         if (this.m_mergeMethod == 2)
/*     */         {
/* 167 */           KLineRec l_last = new KLineRec();
/* 168 */           l_last.m_tradeDate = this.m_dynkRec.m_tradeDate;
/* 169 */           l_last.m_open = l_hisk.m_open;
/* 170 */           l_last.m_close = this.m_dynkRec.m_close;
/* 171 */           l_last.m_high = Math.max(l_hisk.m_high, this.m_dynkRec.m_high);
/* 172 */           l_last.m_low = Math.min(l_hisk.m_low, this.m_dynkRec.m_low);
/* 173 */           l_hisk.m_volume += this.m_dynkRec.m_volume;
/* 174 */           l_hisk.m_money += this.m_dynkRec.m_money;
/* 175 */           return l_last;
/*     */         }
/*     */       }
/* 178 */       return this.m_hiskRecBuffer[p_pos];
/*     */     } catch (Exception l_e) {
/*     */     }
/* 181 */     return this.m_hiskRecBuffer[(this.m_hiskRecBuffer.length - 1)];
/*     */   }
/*     */ 
/*     */   public int f_getRecCount()
/*     */   {
/* 144 */     return this.m_mergeMethod == 1 ? this.m_hiskRecCount + 1 : this.m_hiskRecCount;
/*     */   }
/*     */ 
/*     */   public String f_getStockCode()
/*     */   {
/*  66 */     return this.m_stockCode;
/*     */   }
/*     */ 
/*     */   public synchronized int f_getValidRecPos()
/*     */   {
/* 149 */     return this.m_validRecPos;
/*     */   }
/*     */ 
/*     */   public boolean f_isBGu()
/*     */   {
/*  95 */     return this.m_hqDaemon.f_isBGu();
/*     */   }
/*     */ 
/*     */   public boolean f_isJiJin()
/*     */   {
/* 109 */     return this.m_hqDaemon.f_isJiJin();
/*     */   }
/*     */ 
/*     */   public boolean f_isQuanzheng()
/*     */   {
/* 114 */     return this.m_hqDaemon.f_isQuanZheng();
/*     */   }
/*     */ 
/*     */   public static boolean f_isSameDay(Date p_d1, Date p_d2)
/*     */   {
/* 119 */     return (p_d1.getYear() == p_d2.getYear()) && (p_d1.getMonth() == p_d2.getMonth()) && (p_d1.getDate() == p_d2.getDate());
/*     */   }
/*     */ 
/*     */   public static boolean f_isSameMonth(Date p_d1, Date p_d2)
/*     */   {
/* 134 */     return (p_d1.getYear() == p_d2.getYear()) && (p_d1.getMonth() == p_d2.getMonth());
/*     */   }
/*     */ 
/*     */   public static boolean f_isSameWeek(Date p_d1, Date p_d2)
/*     */   {
/* 125 */     long l_d1 = p_d1.getTime();
/* 126 */     int l_week = p_d1.getDay();
/* 127 */     long l_min = l_d1 - l_week * 24L * 60L * 60L * 1000L;
/* 128 */     long l_max = l_d1 + (6 - l_week) * 24L * 60L * 60L * 1000L;
/* 129 */     long l_d2 = p_d2.getTime();
/* 130 */     return (l_d2 >= l_min) && (l_d2 <= l_max);
/*     */   }
/*     */ 
/*     */   public boolean f_isZhaiQuan()
/*     */   {
/*  99 */     return this.m_hqDaemon.f_isZhaiQuan();
/*     */   }
/*     */ 
/*     */   public boolean f_isZhiShu()
/*     */   {
/* 104 */     return this.m_hqDaemon.f_isZhiShu();
/*     */   }
/*     */ 
/*     */   private void f_recalcMergeMethod()
/*     */   {
/* 209 */     Date l_today = this.m_hqDaemon.m_Trade_Time;
/* 210 */     if ((l_today == null) || (this.m_hqDaemon.m_P_Newest <= 0.0D)) {
/* 211 */       this.m_mergeMethod = 0;
/*     */     }
/* 213 */     else if (this.m_hiskRecCount <= 0) {
/* 214 */       this.m_mergeMethod = 1;
/*     */     }
/*     */     else
/* 217 */       switch (this.m_cycleType)
/*     */       {
/*     */       case 0:
/* 220 */         this.m_mergeMethod = (!f_isSameDay(l_today, this.m_hiskEndDate) ? 1 : 0);
/*     */ 
/* 224 */         break;
/*     */       case 1:
/* 226 */         this.m_mergeMethod = (!f_isSameDay(l_today, this.m_hiskEndDate) ? 2 : !f_isSameWeek(l_today, this.m_hiskEndDate) ? 1 : 0);
/*     */ 
/* 230 */         break;
/*     */       case 2:
/* 232 */         this.m_mergeMethod = (!f_isSameDay(l_today, this.m_hiskEndDate) ? 2 : !f_isSameMonth(l_today, this.m_hiskEndDate) ? 1 : 0);
/*     */       }
/*     */   }
/*     */ 
/*     */   public boolean f_refresh(Object p_arg, DataInputStream p_in)
/*     */   {
/* 255 */     this.m_isCode = false;
/* 256 */     this.m_hqDaemon.deleteObserver(this);
/* 257 */     this.m_hiskDaemon.f_stop();
/* 258 */     System.gc();
/* 259 */     boolean l_launchHiskDaemon = false;
/*     */ 
/* 262 */     this.m_hiskRecCount = 0;
/* 263 */     this.m_validRecPos = 0;
/* 264 */     this.m_hiskEndDate = null;
/* 265 */     this.m_mergeMethod = 0;
/*     */ 
/* 267 */     String l_connStr = new String(this.m_historyKLineUrlHeader + "?query_type=" + "41" + "&code=" + this.m_stockCode + "&cycle=" + s_cycle[this.m_cycleType]);
/*     */ 
/* 273 */     DataInputStream l_in = null;
/* 274 */     if (p_in == null)
/* 275 */       l_in = CommEnv.f_urlConn(l_connStr);
/*     */     else {
/* 277 */       l_in = p_in;
/*     */     }
/* 279 */     if (l_in == null)
/*     */     {
/* 281 */       this.m_isCode = true;
/* 282 */       return false;
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 287 */       int l_result = l_in.readInt();
/* 288 */       if (l_result == 4100)
/*     */       {
/* 290 */         this.m_hiskRecCount = l_in.readInt();
/* 291 */         this.m_hiskEndDate = new Date(l_in.readLong());
/*     */       }
/* 293 */       if (p_in == null)
/* 294 */         l_in.close();
/*     */     }
/*     */     catch (IOException l_ioe) {
/* 297 */       l_ioe.printStackTrace();
/*     */     }
/*     */ 
/* 300 */     this.m_hiskRecBuffer = new KLineRec[this.m_hiskRecCount];
/* 301 */     for (int l_i = 0; l_i < this.m_hiskRecCount; l_i++)
/* 302 */       this.m_hiskRecBuffer[l_i] = new KLineRec();
/* 303 */     if (this.m_hiskRecCount > 0)
/*     */     {
/* 305 */       this.m_validRecPos = this.m_hiskRecCount;
/*     */     }
/* 307 */     l_launchHiskDaemon = true;
/* 308 */     f_recalcMergeMethod();
/* 309 */     if ((this.m_mergeMethod == 1) || (this.m_mergeMethod == 2))
/*     */     {
/* 311 */       f_fillTodayKRec();
/* 312 */       this.m_launchHqDaemon = true;
/*     */     }
/* 314 */     if (p_arg != null)
/*     */     {
/* 316 */       setChanged();
/* 317 */       notifyObservers(p_arg);
/*     */     }
/* 319 */     else if ((this.m_hiskRecCount == 0) && (this.m_mergeMethod == 1))
/*     */     {
/* 321 */       setChanged();
/* 322 */       notifyObservers(new Integer(-914));
/*     */     }
/* 324 */     if (l_launchHiskDaemon) {
/* 325 */       this.m_hiskDaemon.f_start(this.m_stockCode, this.m_cycleType, this.m_validRecPos, new Date(this.m_hiskEndDate.getTime() + 86400000L), p_in);
/*     */     }
/*     */ 
/* 328 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean f_setCycleType(int p_cycleType)
/*     */   {
/* 200 */     if (this.m_cycleType == p_cycleType)
/* 201 */       return false;
/* 202 */     this.m_cycleType = p_cycleType;
/* 203 */     return f_refresh(new Integer(-912), null);
/*     */   }
/*     */ 
/*     */   public void f_setEndOfRecs(int p_endOfRecs)
/*     */   {
/*  90 */     this.m_hiskDaemon.f_setEndOfRecs(p_endOfRecs);
/*     */   }
/*     */ 
/*     */   public void f_setHistoryDaemonStatus(boolean p_initFlag)
/*     */   {
/*  71 */     this.m_hiskDaemon.m_initHistoryKline = p_initFlag;
/*     */   }
/*     */ 
/*     */   public void f_setMXView(SmallMXPLineView p_smallMXPLineView)
/*     */   {
/* 189 */     this.m_smallMXPLineView = p_smallMXPLineView;
/*     */   }
/*     */ 
/*     */   public boolean f_setStockCode(String p_stockCode, DataInputStream p_in)
/*     */   {
/* 194 */     this.m_stockCode = p_stockCode;
/* 195 */     return f_refresh(new Integer(-911), p_in);
/*     */   }
/*     */ 
/*     */   public void update(Observable p_obs, Object p_arg)
/*     */   {
/* 335 */     if ((p_obs instanceof HistoryKLineDaemon))
/*     */     {
/* 337 */       int l_state = this.m_hiskDaemon.f_getState();
/* 338 */       switch (l_state)
/*     */       {
/*     */       case 1:
/*     */       case 2:
/* 342 */         KLineRec[] l_trans = (KLineRec[])p_arg;
/* 343 */         synchronized (this) {
/* 344 */           for (int l_i = 0; l_i < l_trans.length; l_i++)
/*     */           {
/* 346 */             KLineRec l_to = this.m_hiskRecBuffer[(this.m_validRecPos - 1 - l_i)];
/* 347 */             KLineRec l_from = l_trans[l_i];
/* 348 */             l_to.m_tradeDate = new Date(l_from.m_tradeDate.getTime());
/* 349 */             l_to.m_open = l_from.m_open;
/* 350 */             l_to.m_high = l_from.m_high;
/* 351 */             l_to.m_low = l_from.m_low;
/* 352 */             l_to.m_close = l_from.m_close;
/* 353 */             l_to.m_volume = l_from.m_volume;
/* 354 */             l_to.m_money = l_from.m_money;
/*     */           }
/* 356 */           this.m_validRecPos -= l_trans.length;
/*     */         }
/* 358 */         setChanged();
/* 359 */         notifyObservers(new Integer(-913));
/* 360 */         break;
/*     */       case -1:
/*     */       case 0:
/*     */       default:
/* 365 */         boolean l_status = f_getHistoryDaemonStatus();
/* 366 */         if (!l_status)
/* 367 */           return;
/* 368 */         this.m_hiskDaemon.f_stop();
/*     */       }
/*     */ 
/* 372 */       if (!this.m_hiskDaemonFinished)
/*     */       {
/* 374 */         this.m_hqDaemon.addObserver(this);
/* 375 */         this.m_hiskDaemonFinished = true;
/*     */       }
/*     */ 
/*     */     }
/* 380 */     else if ((p_obs instanceof DynamicHQDaemon))
/*     */     {
/*     */       Object l_arg;
/* 383 */       switch (((Integer)p_arg).intValue())
/*     */       {
/*     */       case -916:
/* 386 */         synchronized (this) {
/* 387 */           KLineRec[] l_temp = this.m_hiskRecBuffer;
/* 388 */           this.m_hiskRecCount += 1;
/* 389 */           this.m_hiskEndDate = this.m_dynkRec.m_tradeDate;
/* 390 */           this.m_hiskRecBuffer = new KLineRec[this.m_hiskRecCount];
/* 391 */           for (int l_i = 0; l_i < this.m_hiskRecCount; l_i++)
/*     */           {
/* 393 */             if (l_i == this.m_hiskRecCount - 1)
/*     */             {
/* 395 */               this.m_hiskRecBuffer[l_i] = this.m_dynkRec;
/* 396 */               this.m_dynkRec = new KLineRec();
/*     */             }
/*     */             else {
/* 399 */               this.m_hiskRecBuffer[l_i] = l_temp[l_i];
/*     */             }
/*     */           }
/* 401 */           f_fillTodayKRec();
/* 402 */           this.m_mergeMethod = 1;
/*     */         }
/* 404 */         l_arg = new Integer(-914);
/* 405 */         break;
/*     */       case -901:
/*     */       default:
/* 408 */         f_fillTodayKRec();
/* 409 */         int l_oldRecCount = f_getRecCount();
/* 410 */         f_recalcMergeMethod();
/* 411 */         int l_newRecCount = f_getRecCount();
/* 412 */         l_arg = l_newRecCount > l_oldRecCount ? new Integer(-914) : new Integer(-915);
/*     */       }
/*     */ 
/* 418 */       setChanged();
/* 419 */       notifyObservers(l_arg);
/*     */     }
/*     */   }
/*     */ }

/* Location:           F:\stock research\kline\
 * Qualified Name:     lido.kline.KLineDoc
 * JD-Core Version:    0.6.0
 */