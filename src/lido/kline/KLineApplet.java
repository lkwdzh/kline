/*     */ package lido.kline;
/*     */ 
/*     */ import java.applet.Applet;
/*     */ import java.applet.AppletContext;
/*     */ import java.awt.Color;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Event;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Image;
/*     */ import java.awt.Panel;
/*     */ import java.awt.Rectangle;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.net.URL;
/*     */ import java.util.Observable;
/*     */ import java.util.Observer;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.TimeZone;
/*     */ import lido.common.CommEnv;
/*     */ import lido.common.ControlBar;
/*     */ import lido.common.DynamicHQDaemon;
/*     */ import lido.common.Menuable;
/*     */ import lido.common.SmallMXPLineView;
/*     */ import lido.common.StockInfoView;
/*     */ 
/*     */ public class KLineApplet extends Applet
/*     */   implements Observer, Menuable, Runnable
/*     */ {
/*     */   private int m_appletHeight;
/*     */   private int m_appletWidth;
/*     */   private Boolean m_bZhishu;
/*     */   private Rectangle m_basicHQViewSize;
/*     */   private Rectangle m_buySaleViewSize;
/*     */   public ControlBar m_controlBar;
/*  82 */   private int m_count = 0;
/*     */ 
/*  80 */   private int m_cycle = 0;
/*     */   private String m_defaultStockCode;
/*     */   private String m_dispLogo;
/*     */   private DynamicHQDaemon m_dynamicHQDaemon;
/*     */   private String m_dynamicURL;
/*     */   private String m_firstURL;
/*     */   private String m_imageURL;
/*  77 */   private boolean m_initFlag = false;
/*     */   Thread m_initThread;
/*     */   private Rectangle m_kbottomRulerViewSize;
/*     */   public KLineDoc m_kdoc;
/*     */   private KLineMainView m_klineMainView;
/*     */   private Rectangle m_klineMainViewSize;
/*     */   private Rectangle m_klineViewSize;
/*     */   private Rectangle m_klineVolMoneyViewSize;
/*     */   private String m_mxplineURL;
/*     */   private String m_pline5URL;
/*     */   private String m_plineURL;
/*     */   private SmallMXPLineView m_smallMXPLineView;
/*     */   private Rectangle m_smallMXPLineViewSize;
/*     */   private String m_staticURL;
/*     */   private String m_stockDescURL;
/*     */   private StockInfoView m_stockInfoView;
/*     */   private Rectangle m_stockInfoViewSize;
/*     */   private Rectangle m_stockNameViewSize;
/*     */   public static Color s_bgColor;
/*     */   private static final String[] s_blinkButton;
/*  22 */   private static final String[] s_blinkButtonName = { "放大", "缩小", "前屏", "前移", "后移", "后屏" };
/*     */   private static final String[] s_blinkMenuName;
/*     */   private static final String[] s_itemCycle;
/*     */   private static final String[] s_itemGraphics;
/*     */   private static final String[] s_itemIndex1;
/*     */   private static final String[] s_plineCycle;
/*     */ 
/*     */   static
/*     */   {
/*  25 */     s_blinkButton = new String[] { "fangda", "suoxiao", "qianping", "qianyi", "houyi", "houping" };
/*     */ 
/*  27 */     s_blinkMenuName = new String[] { "图形", "指标", "分时切换", "Ｋ线切换" };
/*     */ 
/*  29 */     s_itemCycle = new String[] { "日Ｋ线", "周Ｋ线", "月Ｋ线" };
/*     */ 
/*  31 */     s_plineCycle = new String[] { "当日走势", "近期走势" };
/*     */ 
/*  33 */     s_itemGraphics = new String[] { KIndexChild.s_mainIndexInfo[0][1], KIndexChild.s_mainIndexInfo[1][1], KIndexChild.s_mainIndexInfo[2][1] };
/*     */ 
/*  37 */     s_itemIndex1 = new String[] { KIndexChild.s_subIndexInfo[0][1], KIndexChild.s_subIndexInfo[1][1], KIndexChild.s_mainIndexInfo[3][1], KIndexChild.s_mainIndexInfo[4][1] };
/*     */ 
/*  74 */     s_bgColor = CommEnv.s_ChildBkColor;
/*     */   }
/*     */ 
/*     */   private void ResetWindowRectange(Boolean bZhishu)
/*     */   {
/* 653 */     if (bZhishu == null) {
/* 654 */       return;
/*     */     }
/* 656 */     Rectangle l_stockNameViewSize = new Rectangle(this.m_appletWidth - 2 - 169, 21, 169, 21);
/*     */     Rectangle l_buySaleViewSize;
/*     */     Rectangle l_basicHQViewSize;
/* 667 */     if (bZhishu.booleanValue()) {
/* 668 */       System.out.println("指数区域重新布置");
/*     */ 
/* 670 */       l_buySaleViewSize = new Rectangle(this.m_appletWidth - 2 - 169, 43, 169, 101);
/*     */ 
/* 677 */       l_basicHQViewSize = new Rectangle(this.m_appletWidth - 2 - 169, 145, 169, 94);
/*     */ 
/* 684 */       this.m_stockInfoViewSize = new Rectangle(this.m_appletWidth - 2 - 169, 21, 169, 216);
/*     */     }
/*     */     else
/*     */     {
/* 691 */       System.out.println("个股区域重新布置");
/*     */ 
/* 693 */       l_buySaleViewSize = new Rectangle(this.m_appletWidth - 2 - 169, 43, 169, 147);
/*     */ 
/* 700 */       l_basicHQViewSize = new Rectangle(this.m_appletWidth - 2 - 169, 145, 169, 92);
/*     */ 
/* 707 */       this.m_stockInfoViewSize = new Rectangle(this.m_appletWidth - 2 - 169, 21, 169, 260);
/*     */     }
/*     */ 
/* 717 */     this.m_smallMXPLineViewSize = new Rectangle(this.m_appletWidth - 2 - 169, 21 + this.m_stockInfoViewSize.height + 1, 169, this.m_klineMainViewSize.height - this.m_stockInfoViewSize.height - 1);
/*     */ 
/* 725 */     this.m_stockInfoView.resetRectangle(this.m_stockInfoViewSize, l_buySaleViewSize, l_basicHQViewSize, l_stockNameViewSize);
/*     */ 
/* 730 */     this.m_smallMXPLineView.resetRectangle(this.m_smallMXPLineViewSize, this.m_smallMXPLineViewSize);
/*     */   }
/*     */ 
/*     */   public void destroy()
/*     */   {
/* 329 */     if ((this.m_initThread != null) && (this.m_initThread.isAlive()))
/* 330 */       this.m_initThread.stop();
/* 331 */     this.m_controlBar.f_destroy();
/* 332 */     this.m_dynamicHQDaemon.f_destroy();
/* 333 */     this.m_kdoc.f_destroy();
/* 334 */     System.gc();
/*     */   }
/*     */ 
/*     */   public void f_addMenuPanel(Panel p_menu)
/*     */   {
/* 619 */     add(p_menu);
/*     */   }
/*     */ 
/*     */   public void f_buttonClicked(int p_button_index)
/*     */   {
/* 399 */     if (p_button_index != 100)
/*     */     {
/* 401 */       if (this.m_controlBar != null)
/*     */       {
/* 403 */         this.m_controlBar.f_setLogoShowing(0, false);
/* 404 */         this.m_controlBar.f_setLogoShowing(1, true);
/*     */       }
/*     */     }
/* 407 */     switch (p_button_index)
/*     */     {
/*     */     case 0:
/* 410 */       this.m_klineMainView.f_increase();
/* 411 */       this.m_controlBar.f_setBlink(1, true);
/* 412 */       if (this.m_klineMainView.f_canPrevPage())
/* 413 */         this.m_controlBar.f_setBlink(2, true);
/* 414 */       if (this.m_klineMainView.f_canIncrease()) break;
/* 415 */       this.m_controlBar.f_setBlink(0, false); break;
/*     */     case 1:
/* 418 */       this.m_klineMainView.f_decrease();
/* 419 */       this.m_controlBar.f_setBlink(0, true);
/* 420 */       if (this.m_klineMainView.f_canDecrease()) break;
/* 421 */       this.m_controlBar.f_setBlink(1, false); break;
/*     */     case 2:
/* 424 */       this.m_klineMainView.f_prevPage();
/* 425 */       this.m_controlBar.f_setBlink(4, true);
/* 426 */       this.m_controlBar.f_setBlink(5, true);
/* 427 */       if (this.m_klineMainView.f_canPrevPage())
/*     */         break;
/* 429 */       this.m_controlBar.f_setBlink(2, false);
/* 430 */       this.m_controlBar.f_setBlink(3, false);
/* 431 */       break;
/*     */     case 3:
/* 434 */       this.m_klineMainView.f_prevLine();
/* 435 */       this.m_controlBar.f_setBlink(4, true);
/* 436 */       this.m_controlBar.f_setBlink(5, true);
/* 437 */       if (this.m_klineMainView.f_canPrevLine())
/*     */         break;
/* 439 */       this.m_controlBar.f_setBlink(2, false);
/* 440 */       this.m_controlBar.f_setBlink(3, false);
/* 441 */       break;
/*     */     case 4:
/* 444 */       this.m_klineMainView.f_nextLine();
/* 445 */       this.m_controlBar.f_setBlink(2, true);
/* 446 */       this.m_controlBar.f_setBlink(3, true);
/* 447 */       if (this.m_klineMainView.f_canNextLine())
/*     */         break;
/* 449 */       this.m_controlBar.f_setBlink(4, false);
/* 450 */       this.m_controlBar.f_setBlink(5, false);
/* 451 */       break;
/*     */     case 5:
/* 454 */       this.m_klineMainView.f_nextPage();
/* 455 */       this.m_controlBar.f_setBlink(2, true);
/* 456 */       this.m_controlBar.f_setBlink(3, true);
/* 457 */       if (this.m_klineMainView.f_canNextPage())
/*     */         break;
/* 459 */       this.m_controlBar.f_setBlink(4, false);
/* 460 */       this.m_controlBar.f_setBlink(5, false);
/* 461 */       break;
/*     */     case 9:
/*     */       try
/*     */       {
/* 465 */         AppletContext l_ac = getAppletContext();
/* 466 */         l_ac.showDocument(new URL(this.m_stockDescURL + "?stock_code=" + this.m_dynamicHQDaemon.m_Stock_Code), "stock_desc");
/*     */       } catch (Exception l_e) {
/*     */       }
/* 469 */       break;
/*     */     case 100:
/* 471 */       AppletContext l_a = getAppletContext();
/*     */       try {
/* 473 */         l_a.showDocument(new URL("http://www.lidoo.com"), "_blank");
/*     */       }
/*     */       catch (Exception l_e) {
/* 476 */         l_e.printStackTrace();
/*     */       }
/*     */     }
/*     */ 
/* 480 */     if (this.m_controlBar != null)
/*     */     {
/* 482 */       this.m_controlBar.f_setLogoShowing(1, false);
/* 483 */       this.m_controlBar.f_setLogoShowing(0, true);
/*     */     }
/*     */   }
/*     */ 
/*     */   private int f_getCycleFromHtml()
/*     */   {
/* 109 */     String temp = getParameter("cycle");
/* 110 */   //String temp = "0";  
			if (temp == null) {
/* 111 */       return 0;
/*     */     }
/*     */ 
/* 114 */     int cycle = Integer.parseInt(temp);
/* 115 */     if ((cycle > 2) || (cycle < 0)) {
/* 116 */       return 0;
/*     */     }
/* 118 */     return cycle;
/*     */   }
/*     */ 
/*     */   private String f_getStkCodeFromHtml()
/*     */   {
/*  96 */     String temp = getParameter("stock_code");
			  //String temp = "600036.SS";
/*  97 */     if (temp == null) {
/*  98 */       return "000001.SS";
/*     */     }
/* 100 */     return temp;
/*     */   }
/*     */ 
/*     */   public void f_imageNameParse(int p_imageNumber)
/*     */   {
/* 359 */     AppletContext l_appletContext = getAppletContext();
/* 360 */     switch (p_imageNumber)
/*     */     {
/*     */     case 0:
/* 363 */       l_appletContext.showStatus(s_blinkButtonName[0]);
/* 364 */       break;
/*     */     case 1:
/* 366 */       l_appletContext.showStatus(s_blinkButtonName[1]);
/* 367 */       break;
/*     */     case 2:
/* 369 */       l_appletContext.showStatus(s_blinkButtonName[2]);
/* 370 */       break;
/*     */     case 3:
/* 372 */       l_appletContext.showStatus(s_blinkButtonName[3]);
/* 373 */       break;
/*     */     case 4:
/* 375 */       l_appletContext.showStatus(s_blinkButtonName[4]);
/* 376 */       break;
/*     */     case 5:
/* 378 */       l_appletContext.showStatus(s_blinkButtonName[5]);
/* 379 */       break;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void f_menuItemClicked(int p_menu_index, int p_item_index, boolean p_select)
/*     */   {
/* 489 */     this.m_controlBar.f_selectMenuItem(p_menu_index, p_item_index);
/* 490 */     switch (p_menu_index) {
/*     */     case 9:
/* 492 */       this.m_kdoc.f_setCycleType(p_item_index);
/* 493 */       break;
/*     */     case 6:
/* 495 */       this.m_klineMainView.m_klineView.f_changeIndex(p_item_index, -1);
/* 496 */       break;
/*     */     case 7:
/* 498 */       if (p_item_index < 2) {
/* 499 */         this.m_klineMainView.m_klineView.f_changeIndex(-1, p_item_index);
/*     */       }
/* 501 */       else if (p_item_index > 2)
/* 502 */         this.m_klineMainView.m_kvolmoneyView.f_changeIndex(p_item_index + 3 - 3, -1);
/* 503 */       break;
/*     */     case 8:
/* 506 */       AppletContext l_a = getAppletContext();
/* 507 */       if (p_item_index == 0) {
/*     */         try {
/* 509 */           String l_url_str = this.m_plineURL + "?code=" + this.m_dynamicHQDaemon.m_Stock_Code;
/*     */ 
/* 512 */           l_a.showDocument(new URL(l_url_str), "_self");
/*     */         }
/*     */         catch (Exception l_ex) {
/* 515 */           l_ex.printStackTrace();
/*     */         }
/*     */       }
/* 518 */       if (p_item_index == 1)
/*     */         try {
/* 520 */           String l_url_str = this.m_pline5URL + "?code=" + this.m_dynamicHQDaemon.m_Stock_Code;
/*     */ 
/* 524 */           l_a.showDocument(new URL(l_url_str), "_self");
/*     */         }
/*     */         catch (Exception l_ex) {
/* 527 */           l_ex.printStackTrace();
/*     */         }
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized void f_setStockCode(String p_code, boolean p_first)
/*     */   {
/* 540 */     String l_connStr = this.m_firstURL + "?query_type=4&code=" + p_code + "&rec_num=12";
/* 541 */     DataInputStream l_in = null;
/*     */     try {
/* 543 */       this.m_initFlag = false;
/*     */ 
/* 545 */       this.m_kdoc.f_setHistoryDaemonStatus(this.m_initFlag);
/* 546 */       if (p_first)
/* 547 */         l_in = CommEnv.f_urlConn(l_connStr);
/* 548 */       if (l_in == null) {
/* 549 */         System.out.println("\n---\nconnect to servlet failed!\tconnection String = \n" + l_connStr + "\n---");
/*     */       }
/* 551 */       this.m_dynamicHQDaemon.f_setStockCode(p_code, p_first, l_in);
/* 552 */       this.m_stockInfoView.f_getStockData();
/* 553 */       this.m_dynamicHQDaemon.f_daemonStart();
/*     */ 
/* 556 */       if (this.m_bZhishu == null) {
/* 557 */         System.out.println("bZhishu is null");
/*     */ 
/* 559 */         if (this.m_dynamicHQDaemon.m_Variety_ID.equalsIgnoreCase("001"))
/* 560 */           this.m_bZhishu = Boolean.TRUE;
/*     */         else {
/* 562 */           this.m_bZhishu = Boolean.FALSE;
/*     */         }
/*     */ 
/* 565 */         if (this.m_bZhishu.booleanValue())
/* 566 */           ResetWindowRectange(this.m_bZhishu);
/*     */       }
/*     */       else {
/* 569 */         Boolean newZhishu = new Boolean(this.m_dynamicHQDaemon.m_Variety_ID.equalsIgnoreCase("001"));
/* 570 */         if (!this.m_bZhishu.equals(newZhishu)) {
/* 571 */           this.m_bZhishu = newZhishu;
/* 572 */           ResetWindowRectange(this.m_bZhishu);
/*     */         }
/*     */       }
/*     */ 
/* 576 */       this.m_smallMXPLineView.f_start(l_in);
/* 577 */       this.m_kdoc.f_setStockCode(p_code, l_in);
/*     */ 
/* 579 */       this.m_stockInfoView.repaint();
/* 580 */       this.m_smallMXPLineView.repaint();
/* 581 */       this.m_initFlag = this.m_kdoc.f_getHistoryDaemonStatus();
/* 582 */       while (!this.m_initFlag) {
/* 583 */         this.m_initFlag = this.m_kdoc.f_getHistoryDaemonStatus();
/*     */       }
/* 585 */       if (l_in != null)
/* 586 */         l_in.close();
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 590 */       System.out.println("KLineApplet.f_setStockCode Exception:\n" + e);
/* 591 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   private Color f_str2Color(String p_strColor)
/*     */   {
/* 338 */     Color l_retColor = null;
/*     */     try {
/* 340 */       if (p_strColor != null) {
/* 341 */         StringTokenizer l_st = new StringTokenizer(p_strColor, ",");
/* 342 */         int l_i = l_st.countTokens();
/* 343 */         if (l_i == 3) {
/* 344 */           int[] l_bgr = new int[3];
/* 345 */           while (l_st.hasMoreTokens()) {
/* 346 */             l_bgr[(l_i - 1)] = Integer.parseInt(l_st.nextToken(",").trim());
/* 347 */             l_i--;
/*     */           }
/* 349 */           l_retColor = new Color(l_bgr[2], l_bgr[1], l_bgr[0]);
/*     */         }
/*     */       }
/*     */     } catch (Exception l_e) {
/*     */     }
/* 354 */     return l_retColor;
/*     */   }
/*     */ 
/*     */   public boolean handleEvent(Event p_e)
/*     */   {
/* 624 */     if (p_e.id == 502)
/*     */     {
/* 626 */       this.m_controlBar.f_hideAllMenuPanels();
/* 627 */       return true;
/*     */     }
/* 629 */     return false;
/*     */   }
/*     */ 
/*     */   public void init()
/*     */   {
/* 124 */     this.m_bZhishu = null;
/*     */ 
/* 126 */     TimeZone.setDefault(TimeZone.getTimeZone("CTT"));
/*     */ 
/* 128 */     setBackground(CommEnv.s_sysBkColor);
/* 129 */     setFont(CommEnv.s_smallFont);
/* 130 */     this.m_appletWidth = getSize().width;
/* 131 */     if (this.m_appletWidth < 380)
/* 132 */       this.m_appletWidth = 380;
/* 133 */     this.m_appletHeight = getSize().height;
/* 134 */     if (this.m_appletHeight < 280) {
/* 135 */       this.m_appletHeight = 280;
/*     */     }
			this.m_appletWidth = 660;
			this.m_appletWidth = 334;
			
/* 137 */     

//<PARAM NAME="dynamicURL" VALUE="http://222.73.229.19/lidoo/servlet/dynamic">
//<PARAM NAME="staticURL" VALUE="http://222.73.229.19/lidoo/servlet/static">
//<PARAM NAME="mxplineURL" VALUE="http://222.73.229.19/lidoo/servlet/mxpline">
//<PARAM NAME="firstURL" VALUE="http://222.73.229.19/lidoo/servlet/first">
//<PARAM NAME="imageURL" VALUE="http://222.73.229.19/lidoo/images/">
//<PARAM NAME="plineURL" VALUE="http://www.sse.com.cn/sseportal/ps/zhs/hqjt/hq2.jsp">
//<PARAM NAME="pline5URL" VALUE="http://www.sse.com.cn/sseportal/ps/zhs/hqjt/hq3.jsp">
//<PARAM NAME="bgColor" VALUE="219,237,248"><PARAM NAME="dispLogo" VALUE="0">
//<PARAM NAME="dispMarkCode" VALUE="0"><PARAM NAME="cycle" VALUE="0">
//<PARAM NAME="stock_code" VALUE="600036.SS">

//            this.m_firstURL = "http://222.73.229.19/lidoo/servlet/first";
//            this.m_dynamicURL = "http://222.73.229.19/lidoo/servlet/dynamic";
//            this.m_staticURL = "http://222.73.229.19/lidoo/servlet/static";
//            this.m_mxplineURL = "http://222.73.229.19/lidoo/servlet/mxpline";
//            this.m_plineURL = "http://www.sse.com.cn/sseportal/ps/zhs/hqjt/hq2.jsp";
//            this.m_pline5URL = "http://www.sse.com.cn/sseportal/ps/zhs/hqjt/hq3.jsp";
//            this.m_imageURL = "http://222.73.229.19/lidoo/images/";
//            this.m_dispLogo = "0";
//            this.m_stockDescURL = getParameter("stockDescURL");
//            String l_bgColor = "219,237,248";		  

            
//            <APPLET 
//            codeBase=http://222.73.229.19/lidoo/ height=334 
//            archive=encode/kline.jar width=1440
//            code=lido.kline.KLineApplet.class name=kline_applet>
//
//     <PARAM NAME="dynamicURL" VALUE="http://222.73.229.19/lidoo/servlet/dynamic">
//     <PARAM NAME="staticURL" VALUE="http://222.73.229.19/lidoo/servlet/static">
//     <PARAM NAME="mxplineURL" VALUE="http://222.73.229.19/lidoo/servlet/mxpline">
//     <PARAM NAME="firstURL" VALUE="http://222.73.229.19/lidoo/servlet/first">
//     <PARAM NAME="imageURL" VALUE="http://222.73.229.19/lidoo/images/">
//     <PARAM NAME="plineURL" VALUE="http://www.sse.com.cn/sseportal/ps/zhs/hqjt/hq2.jsp">
//     <PARAM NAME="pline5URL" VALUE="http://www.sse.com.cn/sseportal/ps/zhs/hqjt/hq3.jsp">
//     <PARAM NAME="bgColor" VALUE="219,237,248">
//     <PARAM NAME="dispLogo" VALUE="0">
//     <PARAM NAME="dispMarkCode" VALUE="0">
//     <PARAM NAME="cycle" VALUE="0">
//     <PARAM NAME="stock_code" VALUE="000002.SZ">
//     </APPLET>

            
              this.m_firstURL = getParameter("firstURL");
/* 138 */     this.m_dynamicURL = getParameter("dynamicURL");
/* 139 */     this.m_staticURL = getParameter("staticURL");
/* 140 */     this.m_mxplineURL = getParameter("mxplineURL");
/* 141 */     this.m_plineURL = getParameter("plineURL");
/* 142 */     this.m_pline5URL = getParameter("pline5URL");
/* 143 */     this.m_imageURL = getParameter("imageURL");
/* 144 */     this.m_dispLogo = getParameter("dispLogo");
/* 145 */     this.m_stockDescURL = getParameter("stockDescURL");
/* 146 */     String l_bgColor = getParameter("bgColor");
/* 147 */     s_bgColor = f_str2Color(l_bgColor) == null ? s_bgColor : f_str2Color(l_bgColor);
/*     */ 
/* 149 */     this.m_stockNameViewSize = new Rectangle(this.m_appletWidth - 2 - 169, 21, 169, 21);
/*     */ 
/* 155 */     this.m_buySaleViewSize = new Rectangle(this.m_appletWidth - 2 - 169, 43, 169, 147);
/*     */ 
/* 161 */     this.m_basicHQViewSize = new Rectangle(this.m_appletWidth - 2 - 169, 145, 169, 92);
/*     */ 
/* 167 */     this.m_kbottomRulerViewSize = new Rectangle(0, this.m_appletHeight - 22 - 14, this.m_appletWidth - 4 - 169, 14);
/*     */ 
/* 182 */     this.m_klineVolMoneyViewSize = new Rectangle(0, 260 * (this.m_appletHeight - 22 - 14) / 384, this.m_appletWidth - 19 - 169, 124 * (this.m_appletHeight - 22 - 14) / 384);
/*     */ 
/* 188 */     this.m_klineViewSize = new Rectangle(0, 0, this.m_appletWidth - 19 - 169, 260 * (this.m_appletHeight - 22 - 14) / 384);
/*     */ 
/* 195 */     this.m_klineMainViewSize = new Rectangle(1, 21, this.m_appletWidth - 4 - 169, this.m_appletHeight - 22);
/*     */ 
/* 201 */     this.m_stockInfoViewSize = new Rectangle(this.m_appletWidth - 2 - 169, 21, 169, 260);
/*     */ 
/* 209 */     this.m_smallMXPLineViewSize = new Rectangle(this.m_appletWidth - 2 - 169, 21 + this.m_stockInfoViewSize.height + 1, 169, this.m_klineMainViewSize.height - this.m_stockInfoViewSize.height - 1);
/*     */ 
/* 228 */     Rectangle l_klineToolbarSize = new Rectangle(0, 0, this.m_appletWidth - 1, 20);
/* 229 */     this.m_defaultStockCode = f_getStkCodeFromHtml();
/* 230 */     this.m_dynamicHQDaemon = new DynamicHQDaemon(this.m_dynamicURL);
/* 231 */     int l_cycle = f_getCycleFromHtml();
/* 232 */     this.m_cycle = l_cycle;
/* 233 */     this.m_kdoc = new KLineDoc(this.m_dynamicHQDaemon, this.m_defaultStockCode, l_cycle, this.m_staticURL);
/*     */ 
/* 237 */     this.m_kdoc.addObserver(this);
/* 238 */     setLayout(null);
/* 239 */     this.m_controlBar = new ControlBar(this, l_klineToolbarSize, CommEnv.s_menuBarColor);
/* 240 */     this.m_initThread = new Thread(this);
/* 241 */     this.m_initThread.start();
/*     */   }
/*     */ 
/*     */   public void paint(Graphics p_g)
/*     */   {
/* 634 */     if (!this.m_initFlag)
/* 635 */       return;
/* 636 */     p_g.setColor(CommEnv.s_sysFrameColor);
/* 637 */     p_g.drawRect(this.m_klineMainViewSize.x - 1, this.m_klineMainViewSize.y - 1, this.m_klineMainViewSize.width + 1, this.m_klineMainViewSize.height + 1);
/*     */ 
/* 641 */     p_g.drawRect(this.m_stockInfoViewSize.x - 1, this.m_stockInfoViewSize.y - 1, this.m_stockInfoViewSize.width + 1, this.m_stockInfoViewSize.height + 3);
/*     */ 
/* 645 */     p_g.drawRect(this.m_smallMXPLineViewSize.x - 1, this.m_smallMXPLineViewSize.y - 1, this.m_smallMXPLineViewSize.width + 1, this.m_smallMXPLineViewSize.height + 1);
/*     */   }
/*     */ 
/*     */   public void run()
/*     */   {
/* 246 */     if ((this.m_dispLogo != null) && (this.m_dispLogo.trim().endsWith("1"))) {
/* 247 */       Image[] l_logo = new Image[2];
/*     */       try {
/* 249 */         URL l_url = new URL(this.m_imageURL + "logo.gif");
/* 250 */         l_logo[0] = getImage(l_url);
/* 251 */         l_url = new URL(this.m_imageURL + "logo44.gif");
/* 252 */         l_logo[1] = getImage(l_url);
/*     */       } catch (Exception l_e) {
/*     */       }
/* 255 */       this.m_controlBar.f_addLogoPanel(l_logo);
/* 256 */       this.m_controlBar.f_setLogoShowing(0, true);
/* 257 */       this.m_controlBar.f_setLogoShowing(1, false);
/*     */     }
/* 259 */     this.m_dynamicHQDaemon.f_setControlBar(this.m_controlBar);
/* 260 */     int l_blinkButtonNum = 6;
/*     */ 
/* 265 */     String l_location = null;
/*     */     try {
/* 267 */       for (int l_i = 0; l_i < l_blinkButtonNum; l_i++)
/*     */       {
/* 269 */         Image[] l_image = new Image[3];
/* 270 */         l_location = this.m_imageURL;
/* 271 */         l_location = l_location + s_blinkButton[l_i] + "1.gif";
/* 272 */         URL l_url = new URL(l_location);
/* 273 */         l_image[0] = getImage(l_url);
/* 274 */         l_location = this.m_imageURL;
/* 275 */         l_location = l_location + s_blinkButton[l_i] + "2.gif";
/* 276 */         l_url = new URL(l_location);
/* 277 */         l_image[1] = getImage(l_url);
/* 278 */         l_location = this.m_imageURL;
/* 279 */         l_location = l_location + s_blinkButton[l_i] + "3.gif";
/* 280 */         l_url = new URL(l_location);
/* 281 */         l_image[2] = getImage(l_url);
/* 282 */         this.m_controlBar.f_addImageMenu(l_image);
/*     */       }
/*     */     } catch (Exception l_e) {
/*     */     }
/* 285 */     for (int i = 0; i < s_blinkMenuName.length; i++) {
/* 286 */       this.m_controlBar.f_addBlinkMenu(s_blinkMenuName[i], true);
/*     */     }
/* 288 */     this.m_controlBar.f_addMenuPanel(6, s_itemGraphics, s_itemGraphics.length, 0, false);
/* 289 */     int l_sepNum = 1;
/* 290 */     int[] l_sepLocation = new int[1];
/* 291 */     l_sepLocation[0] = 2;
/* 292 */     this.m_controlBar.f_addMultiMenuPanel(7, s_itemIndex1, s_itemIndex1.length, 0, l_sepNum, l_sepLocation, true);
/*     */ 
/* 294 */     this.m_controlBar.f_addMenuPanel(9, s_itemCycle, s_itemCycle.length, this.m_cycle, false);
/* 295 */     this.m_controlBar.f_addMenuPanel(8, s_plineCycle, s_plineCycle.length, false);
/*     */ 
/* 297 */     this.m_controlBar.setVisible(false);
/* 298 */     add(this.m_controlBar);
/* 299 */     this.m_controlBar.setVisible(true);
/* 300 */     this.m_smallMXPLineView = new SmallMXPLineView(this.m_dynamicHQDaemon, this.m_smallMXPLineViewSize, this.m_dynamicHQDaemon.m_Stock_Code, this.m_basicHQViewSize, this.m_mxplineURL);
/*     */ 
/* 306 */     this.m_kdoc.f_setMXView(this.m_smallMXPLineView);
/* 307 */     this.m_klineMainView = new KLineMainView(this.m_kdoc, this, this.m_klineMainViewSize, this.m_klineViewSize, this.m_klineVolMoneyViewSize, this.m_kbottomRulerViewSize);
/* 308 */     boolean l_dispMarkCode = false;
				String dispMarkCode = getParameter("dispMarkCode");
				//String dispMarkCode = "0";
				  
/* 309 */     if ((getParameter("dispMarkCode") != null) && (getParameter("dispMarkCode").equals("1")))
/* 310 */       l_dispMarkCode = true;
/* 311 */     this.m_stockInfoView = new StockInfoView(this.m_dynamicHQDaemon, this.m_stockInfoViewSize, this.m_buySaleViewSize, this.m_basicHQViewSize, this.m_stockNameViewSize, l_dispMarkCode);
/* 312 */     this.m_smallMXPLineView.setVisible(false);
/* 313 */     this.m_klineMainView.setVisible(false);
/* 314 */     this.m_stockInfoView.setVisible(false);
/* 315 */     add(this.m_smallMXPLineView);
/* 316 */     add(this.m_klineMainView);
/* 317 */     add(this.m_stockInfoView);
/* 318 */     this.m_smallMXPLineView.setVisible(true);
/* 319 */     this.m_stockInfoView.setVisible(true);
/* 320 */     this.m_klineMainView.setVisible(true);
/*     */ 
/* 322 */     f_setStockCode(this.m_defaultStockCode, true);
/* 323 */     this.m_initFlag = true;
/* 324 */     repaint();
/*     */   }
/*     */ 
/*     */   public synchronized void setStockCode(String p_code)
/*     */   {
/* 535 */     f_setStockCode(p_code, false);
/*     */   }
/*     */ 
/*     */   public void update(Observable p_obs, Object p_arg)
/*     */   {
/* 598 */     switch (((Integer)p_arg).intValue())
/*     */     {
/*     */     case -913:
/*     */     case -912:
/*     */     case -911:
/* 603 */       this.m_controlBar.f_setBlink(0, this.m_klineMainView.f_canIncrease());
/* 604 */       this.m_controlBar.f_setBlink(1, this.m_klineMainView.f_canDecrease());
/* 605 */       this.m_controlBar.f_setBlink(2, this.m_klineMainView.f_canPrevPage());
/* 606 */       this.m_controlBar.f_setBlink(3, this.m_klineMainView.f_canPrevLine());
/* 607 */       this.m_controlBar.f_setBlink(4, this.m_klineMainView.f_canNextLine());
/* 608 */       this.m_controlBar.f_setBlink(5, this.m_klineMainView.f_canNextPage());
/* 609 */       int l_recCount = this.m_kdoc.f_getRecCount();
/* 610 */       int l_validRecPos = this.m_kdoc.f_getValidRecPos();
/* 611 */       int l_progress = l_recCount > 0 ? (int)((l_recCount - l_validRecPos) / l_recCount * 100.0D + 0.5D) : 0;
/* 612 */       this.m_controlBar.f_setProgress(l_progress);
/*     */     }
/*     */   }
/*     */ }

/* Location:           F:\stock research\kline\
 * Qualified Name:     lido.kline.KLineApplet
 * JD-Core Version:    0.6.0
 */