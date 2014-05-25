/*     */ package lido.kline;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.util.Date;
/*     */ import java.util.Observable;
/*     */ import lido.common.CommEnv;
/*     */ 
/*     */ class HistoryKLineDaemon extends Observable
/*     */   implements Runnable
/*     */ {
/*     */   private int m_cycleType;
/*     */   private Date m_endDate;
/*     */   private int m_endOfRecs;
/*     */   private String m_historyKLineUrlHeader;
/*     */   private DataInputStream m_in;
/*  41 */   public boolean m_initHistoryKline = false;
/*     */   private int m_remainRecs;
/*  39 */   private int m_state = 0;
/*     */   private String m_stockCode;
/*     */   private Thread m_thread;
/*     */   public static final int s_COMPLETE = 2;
/*     */   public static final int s_FAILED = -1;
/*     */   public static final int s_NORMAL = 0;
/*     */   public static final int s_SUCCESS = 1;
/*     */   private static final String s_commonKLineField = "trade_time+open+high+low+close+volume+money";
/*     */   private static final int s_commonKLineFieldNum = 7;
/*     */   private static final long s_klineSleepTime = 300L;
/*     */   private static final String s_queryKLine = "42";
/*     */   private static final int s_retKLineRecOK = 4200;
/*     */   private static final int s_transferRecordCount = 70;
/*     */ 
/*     */   public HistoryKLineDaemon(String p_historyKLineUrlHeader)
/*     */   {
/*  47 */     this.m_historyKLineUrlHeader = p_historyKLineUrlHeader;
/*     */   }
/*     */ 
/*     */   public int f_getState()
/*     */   {
/*  86 */     return this.m_state;
/*     */   }
/*     */ 
/*     */   public void f_setEndOfRecs(int p_endOfRecs)
/*     */   {
/*  66 */     if (p_endOfRecs < this.m_endOfRecs)
/*     */     {
/*  68 */       this.m_endOfRecs = p_endOfRecs;
/*  69 */       System.gc();
/*  70 */       this.m_thread.resume();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void f_start(String p_stockCode, int p_cycleType, int p_recCount, Date p_endDate, DataInputStream p_in)
/*     */   {
/*  53 */     this.m_stockCode = p_stockCode;
/*  54 */     this.m_cycleType = p_cycleType;
/*  55 */     this.m_remainRecs = p_recCount;
/*  56 */     this.m_endOfRecs = (p_recCount - 70);
/*  57 */     this.m_endDate = new Date(p_endDate.getTime());
/*  58 */     this.m_in = p_in;
/*  59 */     System.gc();
/*  60 */     this.m_thread = new Thread(this);
/*  61 */     this.m_thread.start();
/*     */   }
/*     */ 
/*     */   public void f_stop()
/*     */   {
/*  77 */     if ((this.m_thread != null) && (this.m_thread.isAlive()))
/*     */     {
/*  79 */       this.m_thread.stop();
/*  80 */       System.gc();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void run()
/*     */   {
/*  91 */     this.m_initHistoryKline = false;
/*     */ 
/*  93 */     this.m_state = 2;
/*  94 */     this.m_initHistoryKline = true;
/*  95 */     KLineRec[] l_trans1 = new KLineRec[0];
/*  96 */     if (this.m_in != null)
/*     */       try {
/*  98 */         this.m_in.close();
/*  99 */         this.m_in = null;
/*     */       }
/*     */       catch (Exception l_e) {
/*     */       }
/* 103 */     setChanged();
/* 104 */     notifyObservers(l_trans1);
/*     */ 
/* 106 */     while (this.m_remainRecs > 0)
/*     */     {
/* 108 */       if (this.m_remainRecs <= this.m_endOfRecs)
/*     */       {
/* 111 */         this.m_thread.suspend();
/*     */       }
/*     */ 
/* 115 */       String l_connStr = this.m_historyKLineUrlHeader + "?query_type=" + "42" + "&code=" + this.m_stockCode + "&cycle=" + KLineDoc.s_cycle[this.m_cycleType] + "&end_time=" + Long.toString(this.m_endDate.getTime()) + "&rec_num=" + Integer.toString(70) + "&field_num=" + Integer.toString(7) + "&field=" + "trade_time+open+high+low+close+volume+money";
/*     */ 
/* 126 */       DataInputStream l_in = null;
/* 127 */       if (this.m_in == null)
/* 128 */         l_in = CommEnv.f_urlConn(l_connStr);
/*     */       else
/* 130 */         l_in = this.m_in;
/* 131 */       Object l_arg = null;
/* 132 */       this.m_state = -1;
/*     */       try {
/* 134 */         if (l_in != null)
/*     */         {
/* 136 */           int l_result = l_in.readInt();
/* 137 */           if (l_result == 4200)
/*     */           {
/* 139 */             int l_count = l_in.readInt();
/* 140 */             if (l_count <= 0)
/* 141 */               return;
/* 142 */             KLineRec[] l_trans = new KLineRec[l_count];
/* 143 */             for (int l_i = 0; l_i < l_count; l_i++)
/*     */             {
/* 145 */               l_trans[l_i] = new KLineRec();
/* 146 */               l_trans[l_i].m_tradeDate = new Date(l_in.readLong());
/* 147 */               l_trans[l_i].m_open = l_in.readDouble();
/* 148 */               l_trans[l_i].m_high = l_in.readDouble();
/* 149 */               l_trans[l_i].m_low = l_in.readDouble();
/* 150 */               l_trans[l_i].m_close = l_in.readDouble();
/* 151 */               l_trans[l_i].m_volume = l_in.readDouble();
/* 152 */               l_trans[l_i].m_money = l_in.readDouble();
/*     */             }
/*     */ 
/* 157 */             l_arg = l_trans;
/* 158 */             this.m_endDate = new Date(l_trans[(l_count - 1)].m_tradeDate.getTime());
/*     */ 
/* 160 */             this.m_remainRecs -= l_count;
/* 161 */             this.m_state = (this.m_remainRecs <= 0 ? 2 : 1);
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 166 */         this.m_initHistoryKline = true;
/* 167 */         if (this.m_in != null) {
/* 168 */           this.m_in.close();
/* 169 */           this.m_in = null;
/*     */         }
/* 171 */         setChanged();
/* 172 */         notifyObservers(l_arg);
/*     */ 
/* 175 */         Thread.sleep(300L);
/*     */       }
/*     */       catch (Exception l_e)
/*     */       {
/* 187 */         l_e.printStackTrace();
/*     */       }
/*     */     }
/* 190 */     this.m_initHistoryKline = true;
/*     */   }
/*     */ }

/* Location:           F:\stock research\kline\
 * Qualified Name:     lido.kline.HistoryKLineDaemon
 * JD-Core Version:    0.6.0
 */