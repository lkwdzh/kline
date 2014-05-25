/*     */ package lido.common;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.Date;
/*     */ import java.util.Observable;
/*     */ 
/*     */ public class DynamicHQDaemon extends Observable
/*     */   implements Runnable
/*     */ {
/*  83 */   public Date m_AM_Close = CommEnv.s_defaultAmClose;
/*     */ 
/*  82 */   public Date m_AM_Open = CommEnv.s_defaultAmOpen;
/*     */   public double m_Ave_Price;
/*     */   public double m_Buy_Sale_Gap;
/*     */   public double m_Buy_Sale_Ratio;
/*  78 */   public Date m_Current_Time = new Date();
/*     */   public double m_M_Total;
/*  85 */   public Date m_PM_Close = CommEnv.s_defaultPmClose;
/*     */ 
/*  84 */   public Date m_PM_Open = CommEnv.s_defaultPmOpen;
/*     */   public double m_P_Buy1;
/*     */   public double m_P_Buy2;
/*     */   public double m_P_Buy3;
/*     */   public double m_P_Buy4;
/*     */   public double m_P_Buy5;
/*     */   public double m_P_Close;
/*     */   public double m_P_Highest;
/*     */   public double m_P_Lowest;
/*     */   public double m_P_Newest;
/*     */   public double m_P_Open;
/*     */   public double m_P_sale1;
/*     */   public double m_P_sale2;
/*     */   public double m_P_sale3;
/*     */   public double m_P_sale4;
/*     */   public double m_P_sale5;
/*  72 */   public String m_Stock_Code = "！！";
/*     */ 
/*  74 */   public String m_Stock_Name = "！！";
/*     */   public Date m_Trade_Time;
/*     */   public double m_Up_Down_Range;
/*     */   public double m_Up_Down_Val;
/*     */   public double m_V_Now;
/*     */   public double m_V_Total;
/*  75 */   public String m_Variety_ID = "";
/*     */   public double m_Vol_Ratio;
/*     */   public double m_break1_numb;
/*     */   public double m_break2_numb;
/*     */   private boolean m_changeStockCode;
/*     */   public String m_cj_type;
/*  69 */   ControlBar m_controlBar = null;
/*     */   public double m_down_numb;
/*     */   private String m_dynamicHQUrlHeader;
/*     */   public double m_equal_numb;
/* 138 */   private Thread m_hqThread = null;
/*     */   public double m_up_numb;
/*     */   public double m_v_Buy1;
/*     */   public double m_v_Buy2;
/*     */   public double m_v_Buy3;
/*     */   public double m_v_Buy4;
/*     */   public double m_v_Buy5;
/*     */   public double m_v_inner;
/*     */   public double m_v_outer;
/*     */   public double m_v_sale1;
/*     */   public double m_v_sale2;
/*     */   public double m_v_sale3;
/*     */   public double m_v_sale4;
/*     */   public double m_v_sale5;
/*     */   public static final long s_MILLISECONDS_OF_ONE_DAY = 86400000L;
/*     */   public static final long s_NINE_TWENTY_IN_SECONDS = 33600L;
/*     */   public static final long s_SECONDS_OF_ONE_DAY = 86400L;
/*     */   public static final long s_SECONDS_OF_TEN_MINUTES = 600L;
/*     */   public static final long s_SLEEP_TIME_IN_TRADE = 5L;
/*     */   public static final long s_SLEEP_TIME_NEAR_TRADE = 5L;
/*     */   private static final long s_adjustTimeOffset = 600L;
/*     */   private static final int s_commonHQFieldNum_exp = 26;
/*     */   private static final int s_commonHQFieldNum_sgn = 37;
/*     */   private static final String s_commonHQField_exp = "cj_type+p_newest+ave_price+up_down_val+up_down_range+p_close+p_open+m_total+v_total+p_highest+p_lowest+v_now+vol_ratio+buy_sale_ratio+buy_sale_gap+v_buy1+p_buy1+v_sale1+p_sale1+v_inner+v_outer+zs_up_numb+zs_down_numb+zs_equal_numb+zs_break1_numb+zs_break2_numb";
/*     */   private static final String s_commonHQField_sgn = "cj_type+p_newest+ave_price+up_down_val+up_down_range+p_close+p_open+m_total+v_total+p_highest+p_lowest+v_now+vol_ratio+buy_sale_ratio+buy_sale_gap+v_buy1+p_buy1+v_sale1+p_sale1+v_buy2+p_buy2+v_sale2+p_sale2+v_buy3+p_buy3+v_sale3+p_sale3+v_buy4+p_buy4+v_sale4+p_sale4+v_buy5+p_buy5+v_sale5+p_sale5+v_inner+v_outer";
/*     */   public static final long s_hqSleepTime = 3000L;
/*     */   private static final String s_queryMarketInfo = "12";
/*     */   private static final String s_querySingleHQ = "21";
/*     */   private static final String s_queryStockInfo = "11";
/*     */   private static final int s_retInvalidStockCode = 1101;
/*     */   private static final int s_retMarketInfoOK = 1200;
/*     */   private static final int s_retNewSingleHQ = 2100;
/*     */   private static final int s_retNoSingleHQ = 2101;
/*     */   private static final int s_retValidStockCode = 1100;
/*     */ 
/*     */   public DynamicHQDaemon(String p_dynamicHQUrlHeader)
/*     */   {
/* 240 */     this.m_changeStockCode = false;
/* 241 */     f_initHQ();
/*     */ 
/* 243 */     this.m_dynamicHQUrlHeader = p_dynamicHQUrlHeader;
/*     */   }
/*     */ 
/*     */   public void f_SendNewHQMessage()
/*     */   {
/* 716 */     setChanged();
/* 717 */     notifyObservers(new Integer(-901));
/*     */   }
/*     */ 
/*     */   private long f_adjustSleepTime(long p_amOpenInSecondFormat, long p_amCloseInSecondFormat, long p_pmOpenInSecondFormat, long p_pmCloseInSecondFormat)
/*     */   {
/* 655 */     int l_day = this.m_Current_Time.getDay();
/*     */ 
/* 657 */     long l_current_in_second_format = CommEnv.f_toSeconds(this.m_Current_Time.getTime());
/*     */     long l_sleepTimeGap;
/* 658 */     if ((l_day == 0) || (l_day == 6))
/*     */     {
/* 660 */       l_sleepTimeGap = 86400000L - 1000L * l_current_in_second_format + 1000L * (p_amOpenInSecondFormat - 600L);
/*     */ 
/* 662 */       if (l_day == 0)
/*     */       {
/* 664 */         l_sleepTimeGap += 86400000L;
/*     */       }
/*     */     }
/* 666 */     if (l_current_in_second_format < p_amOpenInSecondFormat - 600L)
/*     */     {
/* 669 */       l_sleepTimeGap = 1000L * (p_amOpenInSecondFormat - 600L - l_current_in_second_format);
/*     */     }
/* 672 */     else if (l_current_in_second_format < p_amOpenInSecondFormat)
/*     */     {
/* 676 */       l_sleepTimeGap = 5000L;
/*     */     }
/* 679 */     else if (l_current_in_second_format < p_amCloseInSecondFormat + 600L)
/*     */     {
/* 682 */       l_sleepTimeGap = 5000L;
/*     */     }
/* 685 */     else if (l_current_in_second_format < p_pmOpenInSecondFormat - 600L)
/*     */     {
/* 689 */       l_sleepTimeGap = 1000L * (p_pmOpenInSecondFormat - 600L - l_current_in_second_format);
/*     */     }
/* 692 */     else if (l_current_in_second_format < p_pmOpenInSecondFormat)
/*     */     {
/* 695 */       l_sleepTimeGap = 5000L;
/*     */     }
/* 698 */     else if (l_current_in_second_format < p_pmCloseInSecondFormat + 600L)
/*     */     {
/* 701 */       l_sleepTimeGap = 5000L;
/*     */     }
/*     */     else
/*     */     {
/* 707 */       l_sleepTimeGap = 1000L * (86400L - l_current_in_second_format + 33600L);
/*     */     }
/*     */ 
/* 712 */     return l_sleepTimeGap;
/*     */   }
/*     */ 
/*     */   public void f_daemonStart()
/*     */   {
/* 721 */     this.m_hqThread = new Thread(this);
/* 722 */     this.m_hqThread.start();
/*     */   }
/*     */ 
/*     */   public void f_destroy()
/*     */   {
/* 170 */     if (this.m_hqThread != null)
/*     */     {
/* 172 */       if (this.m_hqThread.isAlive())
/* 173 */         this.m_hqThread.stop();
/*     */     }
/* 175 */     System.gc();
/*     */   }
/*     */ 
/*     */   private boolean f_getDynamicHQ(boolean bNotify, DataInputStream p_in)
/*     */   {
/* 346 */     if (this.m_controlBar != null)
/*     */     {
/* 348 */       this.m_controlBar.f_setLogoShowing(0, false);
/* 349 */       this.m_controlBar.f_setLogoShowing(1, true);
/*     */     }
/*     */ 
/* 352 */     String l_connStr = this.m_dynamicHQUrlHeader + "?query_type=" + "21" + "&code=" + this.m_Stock_Code + "&trade_time=" + this.m_Trade_Time.getTime();
/*     */ 
/* 356 */     Date d = new Date();
/* 357 */     l_connStr = l_connStr + "&nouse=" + Long.toString(d.getTime());
/* 358 */     if (this.m_Variety_ID.equalsIgnoreCase("001"))
/*     */     {
/* 360 */       l_connStr = l_connStr + "&field_num=26&field=cj_type+p_newest+ave_price+up_down_val+up_down_range+p_close+p_open+m_total+v_total+p_highest+p_lowest+v_now+vol_ratio+buy_sale_ratio+buy_sale_gap+v_buy1+p_buy1+v_sale1+p_sale1+v_inner+v_outer+zs_up_numb+zs_down_numb+zs_equal_numb+zs_break1_numb+zs_break2_numb";
/*     */     }
/*     */     else
/*     */     {
/* 365 */       l_connStr = l_connStr + "&field_num=37&field=cj_type+p_newest+ave_price+up_down_val+up_down_range+p_close+p_open+m_total+v_total+p_highest+p_lowest+v_now+vol_ratio+buy_sale_ratio+buy_sale_gap+v_buy1+p_buy1+v_sale1+p_sale1+v_buy2+p_buy2+v_sale2+p_sale2+v_buy3+p_buy3+v_sale3+p_sale3+v_buy4+p_buy4+v_sale4+p_sale4+v_buy5+p_buy5+v_sale5+p_sale5+v_inner+v_outer";
/*     */     }
/*     */     DataInputStream l_in;
/* 370 */     if (p_in == null)
/* 371 */       l_in = CommEnv.f_urlConn(l_connStr);
/*     */     else {
/* 373 */       l_in = p_in;
/*     */     }
/* 375 */     boolean l_isOk = false;
/*     */ 
/* 377 */     boolean l_isNextDay = false;
/*     */     try {
/* 379 */       if (l_in != null)
/*     */       {
/* 381 */         int l_result = l_in.readInt();
/*     */ 
/* 383 */         if (l_result == 2100)
/*     */         {
/* 385 */           this.m_Current_Time = new Date(l_in.readLong());
/*     */ 
/* 389 */           Date l_d = new Date(l_in.readLong());
/*     */ 
/* 391 */           if ((this.m_Trade_Time.getTime() != 0L) && (l_d.getDate() != this.m_Trade_Time.getDate()))
/*     */           {
/* 397 */             l_isNextDay = true;
/*     */           }
/* 399 */           this.m_Trade_Time = l_d;
/*     */ 
/* 402 */           this.m_cj_type = l_in.readUTF();
/* 403 */           this.m_P_Newest = l_in.readDouble();
/*     */ 
/* 405 */           this.m_Ave_Price = l_in.readDouble();
/* 406 */           this.m_Up_Down_Val = l_in.readDouble();
/* 407 */           this.m_Up_Down_Range = l_in.readDouble();
/* 408 */           this.m_P_Close = l_in.readDouble();
/* 409 */           this.m_P_Open = l_in.readDouble();
/* 410 */           this.m_M_Total = l_in.readDouble();
/* 411 */           this.m_V_Total = l_in.readDouble();
/* 412 */           this.m_P_Highest = l_in.readDouble();
/* 413 */           this.m_P_Lowest = l_in.readDouble();
/* 414 */           this.m_V_Now = l_in.readDouble();
/* 415 */           this.m_Vol_Ratio = l_in.readDouble();
/* 416 */           this.m_Buy_Sale_Ratio = l_in.readDouble();
/* 417 */           this.m_Buy_Sale_Gap = l_in.readDouble();
/* 418 */           this.m_v_Buy1 = l_in.readDouble();
/* 419 */           this.m_P_Buy1 = l_in.readDouble();
/* 420 */           this.m_v_sale1 = l_in.readDouble();
/* 421 */           this.m_P_sale1 = l_in.readDouble();
/* 422 */           if (p_in != null) {
/* 423 */             this.m_v_inner = l_in.readDouble();
/* 424 */             this.m_v_outer = l_in.readDouble();
/*     */           }
/* 426 */           if (!this.m_Variety_ID.equalsIgnoreCase("001"))
/*     */           {
/* 428 */             this.m_v_Buy2 = l_in.readDouble();
/* 429 */             this.m_P_Buy2 = l_in.readDouble();
/* 430 */             this.m_v_sale2 = l_in.readDouble();
/* 431 */             this.m_P_sale2 = l_in.readDouble();
/* 432 */             this.m_v_Buy3 = l_in.readDouble();
/* 433 */             this.m_P_Buy3 = l_in.readDouble();
/* 434 */             this.m_v_sale3 = l_in.readDouble();
/* 435 */             this.m_P_sale3 = l_in.readDouble();
/*     */ 
/* 437 */             this.m_v_Buy4 = l_in.readDouble();
/* 438 */             this.m_P_Buy4 = l_in.readDouble();
/* 439 */             this.m_v_sale4 = l_in.readDouble();
/* 440 */             this.m_P_sale4 = l_in.readDouble();
/* 441 */             this.m_v_Buy5 = l_in.readDouble();
/* 442 */             this.m_P_Buy5 = l_in.readDouble();
/* 443 */             this.m_v_sale5 = l_in.readDouble();
/* 444 */             this.m_P_sale5 = l_in.readDouble();
/*     */           }
/* 446 */           if (p_in == null) {
/* 447 */             this.m_v_inner = l_in.readDouble();
/* 448 */             this.m_v_outer = l_in.readDouble();
/*     */           }
/* 450 */           if (this.m_Variety_ID.equalsIgnoreCase("001"))
/*     */           {
/* 452 */             this.m_up_numb = l_in.readDouble();
/* 453 */             this.m_down_numb = l_in.readDouble();
/* 454 */             this.m_equal_numb = l_in.readDouble();
/* 455 */             this.m_break1_numb = l_in.readDouble();
/* 456 */             this.m_break2_numb = l_in.readDouble();
/*     */           }
/* 458 */           l_isOk = true;
/*     */         }
/* 461 */         else if (l_result == 2101) {
/* 462 */           this.m_Current_Time = new Date(l_in.readLong());
/*     */         }
/*     */ 
/* 467 */         if (p_in == null)
/* 468 */           l_in.close();
/*     */       }
/*     */     }
/*     */     catch (IOException l_ioe)
/*     */     {
/* 473 */       l_ioe.printStackTrace();
/*     */     }
/*     */ 
/* 476 */     if (l_isOk)
/*     */     {
/* 481 */       int l_currentYear = this.m_Current_Time.getYear();
/* 482 */       int l_currentMonth = this.m_Current_Time.getMonth();
/* 483 */       int l_currentDay = this.m_Current_Time.getDate();
/*     */ 
/* 485 */       int l_tradeYear = this.m_Trade_Time.getYear();
/* 486 */       int l_tradeMonth = this.m_Trade_Time.getMonth();
/* 487 */       int l_tradeDay = this.m_Trade_Time.getDate();
/*     */ 
/* 489 */       if (this.m_changeStockCode)
/*     */       {
/* 491 */         this.m_changeStockCode = false;
/*     */ 
/* 493 */         setChanged();
/* 494 */         if (bNotify) {
/* 495 */           notifyObservers(new Integer(-911));
/*     */         }
/*     */ 
/*     */       }
/* 500 */       else if ((l_currentYear != l_tradeYear) || (l_currentMonth != l_tradeMonth) || (l_currentDay != l_tradeDay))
/*     */       {
/* 502 */         if (this.m_controlBar != null)
/*     */         {
/* 504 */           this.m_controlBar.f_setLogoShowing(1, false);
/* 505 */           this.m_controlBar.f_setLogoShowing(0, true);
/*     */         }
/* 507 */         return false;
/*     */       }
/*     */ 
/* 510 */       setChanged();
/* 511 */       if (l_isNextDay == true)
/*     */       {
/* 513 */         if (bNotify)
/*     */         {
/* 517 */           notifyObservers(new Integer(-916));
/*     */         }
/* 519 */         l_isNextDay = false;
/*     */       }
/*     */       else
/*     */       {
/* 523 */         l_currentYear = this.m_Current_Time.getYear();
/* 524 */         l_currentMonth = this.m_Current_Time.getMonth();
/* 525 */         l_currentDay = this.m_Current_Time.getDate();
/* 526 */         l_tradeYear = this.m_Trade_Time.getYear();
/* 527 */         l_tradeMonth = this.m_Trade_Time.getMonth();
/* 528 */         l_tradeDay = this.m_Trade_Time.getDate();
/* 529 */         if (this.m_changeStockCode)
/*     */         {
/* 531 */           this.m_changeStockCode = false;
/*     */         }
/* 536 */         else if ((l_currentYear != l_tradeYear) || (l_currentMonth != l_tradeMonth) || (l_currentDay != l_tradeDay))
/*     */         {
/* 538 */           if (this.m_controlBar != null)
/*     */           {
/* 540 */             this.m_controlBar.f_setLogoShowing(1, false);
/* 541 */             this.m_controlBar.f_setLogoShowing(0, true);
/*     */           }
/* 543 */           return false;
/*     */         }
/*     */ 
/* 546 */         if (bNotify)
/*     */         {
/* 550 */           notifyObservers(new Integer(-901));
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 558 */       int l_currentYear = this.m_Current_Time.getYear();
/* 559 */       int l_currentMonth = this.m_Current_Time.getMonth();
/* 560 */       int l_currentDay = this.m_Current_Time.getDate();
/* 561 */       int l_tradeYear = this.m_Trade_Time.getYear();
/* 562 */       int l_tradeMonth = this.m_Trade_Time.getMonth();
/* 563 */       int l_tradeDay = this.m_Trade_Time.getDate();
/* 564 */       if (this.m_changeStockCode) {
/* 565 */         this.m_changeStockCode = false;
/*     */       }
/* 568 */       else if ((l_currentYear != l_tradeYear) || (l_currentMonth != l_tradeMonth) || (l_currentDay != l_tradeDay))
/*     */       {
/* 570 */         if (this.m_controlBar != null)
/*     */         {
/* 572 */           this.m_controlBar.f_setLogoShowing(1, false);
/* 573 */           this.m_controlBar.f_setLogoShowing(0, true);
/*     */         }
/* 575 */         return false;
/*     */       }
/*     */ 
/* 578 */       setChanged();
/* 579 */       if (bNotify)
/*     */       {
/* 583 */         notifyObservers(new Integer(-917));
/*     */       }
/*     */     }
/* 586 */     if (this.m_controlBar != null)
/*     */     {
/* 588 */       this.m_controlBar.f_setLogoShowing(1, false);
/* 589 */       this.m_controlBar.f_setLogoShowing(0, true);
/*     */     }
/* 591 */     return l_isOk;
/*     */   }
/*     */ 
/*     */   public boolean f_getMarketInfo(DataInputStream p_in)
/*     */   {
/* 249 */     String l_connStr = this.m_dynamicHQUrlHeader + "?query_type=" + "12";
/*     */ 
/* 251 */     DataInputStream l_in = null;
/* 252 */     if (p_in == null)
/* 253 */       l_in = CommEnv.f_urlConn(l_connStr);
/*     */     else {
/* 255 */       l_in = p_in;
/*     */     }
/* 257 */     boolean l_isOk = false;
/*     */     try {
/* 259 */       if (l_in != null)
/*     */       {
/* 261 */         int l_result = l_in.readInt();
/* 262 */         if (l_result == 1200)
/*     */         {
/* 264 */           this.m_Current_Time = new Date(l_in.readLong());
/* 265 */           this.m_AM_Open = new Date(l_in.readLong());
/* 266 */           this.m_AM_Close = new Date(l_in.readLong());
/* 267 */           this.m_PM_Open = new Date(l_in.readLong());
/* 268 */           this.m_PM_Close = new Date(l_in.readLong());
/* 269 */           l_isOk = true;
/* 270 */           if (p_in == null)
/* 271 */             l_in.close();
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (IOException l_ioe) {
/* 276 */       l_ioe.printStackTrace();
/*     */     }
/* 278 */     return l_isOk;
/*     */   }
/*     */ 
/*     */   private void f_initHQ()
/*     */   {
/* 188 */     this.m_Trade_Time = new Date(0L);
/* 189 */     this.m_cj_type = "eq";
/* 190 */     this.m_P_Newest = 0.0D;
/* 191 */     this.m_Ave_Price = 0.0D;
/* 192 */     this.m_Up_Down_Val = 0.0D;
/* 193 */     this.m_Up_Down_Range = 0.0D;
/* 194 */     this.m_P_Close = 0.0D;
/* 195 */     this.m_P_Open = 0.0D;
/* 196 */     this.m_M_Total = 0.0D;
/* 197 */     this.m_V_Total = 0.0D;
/* 198 */     this.m_P_Highest = 0.0D;
/* 199 */     this.m_P_Lowest = 0.0D;
/* 200 */     this.m_V_Now = 0.0D;
/* 201 */     this.m_Vol_Ratio = 0.0D;
/*     */ 
/* 203 */     this.m_Buy_Sale_Ratio = 0.0D;
/* 204 */     this.m_Buy_Sale_Gap = 0.0D;
/* 205 */     this.m_v_Buy1 = 0.0D;
/* 206 */     this.m_v_Buy2 = 0.0D;
/* 207 */     this.m_v_Buy3 = 0.0D;
/* 208 */     this.m_v_Buy4 = 0.0D;
/* 209 */     this.m_v_Buy5 = 0.0D;
/* 210 */     this.m_P_Buy1 = 0.0D;
/* 211 */     this.m_P_Buy2 = 0.0D;
/* 212 */     this.m_P_Buy3 = 0.0D;
/* 213 */     this.m_P_Buy4 = 0.0D;
/* 214 */     this.m_P_Buy5 = 0.0D;
/* 215 */     this.m_v_sale1 = 0.0D;
/* 216 */     this.m_v_sale2 = 0.0D;
/* 217 */     this.m_v_sale3 = 0.0D;
/* 218 */     this.m_v_sale4 = 0.0D;
/* 219 */     this.m_v_sale5 = 0.0D;
/* 220 */     this.m_P_sale1 = 0.0D;
/* 221 */     this.m_P_sale2 = 0.0D;
/* 222 */     this.m_P_sale3 = 0.0D;
/* 223 */     this.m_P_sale4 = 0.0D;
/* 224 */     this.m_P_sale5 = 0.0D;
/* 225 */     this.m_v_inner = 0.0D;
/* 226 */     this.m_v_outer = 0.0D;
/*     */ 
/* 228 */     this.m_up_numb = 0.0D;
/* 229 */     this.m_down_numb = 0.0D;
/* 230 */     this.m_equal_numb = 0.0D;
/* 231 */     this.m_break1_numb = 0.0D;
/* 232 */     this.m_break2_numb = 0.0D;
/*     */   }
/*     */ 
/*     */   public boolean f_isBGu()
/*     */   {
/* 142 */     return this.m_Variety_ID.equalsIgnoreCase("004");
/*     */   }
/*     */ 
/*     */   public boolean f_isJiJin()
/*     */   {
/* 160 */     return this.m_Variety_ID.equalsIgnoreCase("008");
/*     */   }
/*     */ 
/*     */   public boolean f_isQuanZheng()
/*     */   {
/* 165 */     return this.m_Variety_ID.equalsIgnoreCase("203");
/*     */   }
/*     */ 
/*     */   public boolean f_isZhaiQuan()
/*     */   {
/* 147 */     return (this.m_Variety_ID.equalsIgnoreCase("016")) || (this.m_Variety_ID.equalsIgnoreCase("032")) || (this.m_Variety_ID.equalsIgnoreCase("064")) || (this.m_Variety_ID.equalsIgnoreCase("128"));
/*     */   }
/*     */ 
/*     */   public boolean f_isZhiShu()
/*     */   {
/* 155 */     return this.m_Variety_ID.equalsIgnoreCase("001");
/*     */   }
/*     */ 
/*     */   public void f_setControlBar(ControlBar p_controlBar)
/*     */   {
/* 183 */     this.m_controlBar = p_controlBar;
/*     */   }
/*     */ 
/*     */   public synchronized boolean f_setStockCode(String p_Stock_Code, boolean p_isFirst, DataInputStream p_in)
/*     */   {
/* 288 */     boolean l_isOk = false;
/* 289 */     if (p_isFirst)
/*     */     {
/* 291 */       l_isOk = f_getMarketInfo(p_in);
/* 292 */       if (!l_isOk)
/* 293 */         return false;
/*     */     }
/* 295 */     String l_connStr = this.m_dynamicHQUrlHeader + "?query_type=" + "11" + "&code=" + p_Stock_Code;
/*     */     DataInputStream l_in;
/* 299 */     if (p_in == null)
/* 300 */       l_in = CommEnv.f_urlConn(l_connStr);
/*     */     else {
/* 302 */       l_in = p_in;
/*     */     }
/* 304 */     if (l_in == null) {
/* 305 */       return false;
/*     */     }
/* 307 */     l_isOk = false;
/*     */     try {
/* 309 */       if (l_in != null)
/*     */       {
/* 311 */         int l_result = l_in.readInt();
/* 312 */         if (l_result == 1100)
/*     */         {
/* 314 */           this.m_Stock_Code = p_Stock_Code;
/* 315 */           this.m_Stock_Name = l_in.readUTF();
/* 316 */           this.m_Variety_ID = l_in.readUTF();
/* 317 */           l_isOk = true;
/*     */         }
/* 319 */         if (p_in == null)
/* 320 */           l_in.close();
/*     */       }
/*     */     }
/*     */     catch (IOException l_ioe) {
/* 324 */       l_ioe.printStackTrace();
/*     */     }
/*     */ 
/* 327 */     if ((l_isOk) || (l_in == null))
/*     */     {
/* 330 */       if ((this.m_hqThread != null) && (this.m_hqThread.isAlive()))
/* 331 */         this.m_hqThread.stop();
/* 332 */       System.gc();
/* 333 */       this.m_changeStockCode = true;
/* 334 */       f_initHQ();
/* 335 */       if (p_in == null)
/* 336 */         f_getDynamicHQ(false, null);
/*     */       else
/* 338 */         f_getDynamicHQ(false, l_in);
/* 339 */       return true;
/*     */     }
/* 341 */     return false;
/*     */   }
/*     */ 
/*     */   public void run()
/*     */   {
/* 596 */     long l_adjustAmOpen = (this.m_AM_Open.getHours() * 60 + this.m_AM_Open.getMinutes()) * 60 + this.m_AM_Open.getSeconds() - 600L;
/* 597 */     long l_adjustAmClose = (this.m_AM_Close.getHours() * 60 + this.m_AM_Close.getMinutes()) * 60 + this.m_AM_Close.getSeconds() + 600L;
/* 598 */     long l_adjustPmOpen = (this.m_PM_Open.getHours() * 60 + this.m_PM_Open.getMinutes()) * 60 + this.m_PM_Open.getSeconds() - 600L;
/* 599 */     long l_adjustPmClose = (this.m_PM_Close.getHours() * 60 + this.m_PM_Close.getMinutes()) * 60 + this.m_PM_Close.getSeconds() + 600L;
/*     */     while (true)
/*     */     {
/*     */       try
/*     */       {
/* 604 */         long x = f_adjustSleepTime(l_adjustAmOpen, l_adjustAmClose, l_adjustPmOpen, l_adjustPmClose);
/*     */ 
/* 613 */         Thread.sleep(x);
/*     */ 
/* 615 */         System.gc();
/*     */       }
/*     */       catch (Exception l_e)
/*     */       {
/* 620 */         l_e.printStackTrace();
/*     */       }
/*     */ 
/* 640 */       f_getDynamicHQ(true, null);
/*     */     }
/*     */   }
/*     */ }

/* Location:           F:\stock research\kline\
 * Qualified Name:     lido.common.DynamicHQDaemon
 * JD-Core Version:    0.6.0
 */