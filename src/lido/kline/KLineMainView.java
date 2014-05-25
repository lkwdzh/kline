/*     */ package lido.kline;
/*     */ 
/*     */ import java.applet.AppletContext;
/*     */ import java.awt.Color;
/*     */ import java.awt.Event;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Panel;
/*     */ import java.awt.Rectangle;
/*     */ import java.util.Date;
/*     */ import java.util.Observable;
/*     */ import java.util.Observer;
/*     */ import lido.common.CommEnv;
/*     */ import lido.common.ControlBar;
/*     */ 
/*     */ public class KLineMainView extends Panel
/*     */   implements Observer
/*     */ {
/*     */   private int m_brulerCalcBegPixel;
/*     */   private int m_brulerCalcEndPixel;
/*  36 */   private int m_brulerFinRec = -1;
/*     */ 
/*  35 */   private int m_brulerOrgRec = -1;
/*     */   private Rectangle m_kbottomRulerSize;
/*     */   private KLineDoc m_kdoc;
/*     */   private Rectangle m_klineMainViewSize;
/*     */   public KIndexChild m_klineView;
/*     */   private Rectangle m_klineViewSize;
/*     */   private Rectangle m_klineVolMoneyViewSize;
/*     */   private KMiniFrame m_kminiFrame;
/*     */   public KIndexChild m_kvolmoneyView;
/*     */   private int m_maxRecsInRuler;
/*     */   private KLineApplet m_parent;
/*     */   private double m_pixelsPerRecord;
/*  40 */   public int m_preRecPointedByMouse = -1;
/*     */ 
/*  39 */   public int m_recPointedByMouse = -1;
/*     */   private static final int s_defaultPixelsPerRecord = 7;
/*     */   private static final int s_maxPixelsPerRecord = 20;
/*     */   private static final int s_minPixelsPerRecord = 3;
/*  22 */   public static final Rectangle s_miniFrameSize = new Rectangle(0, 14, 60, 280);
/*     */ 
/*     */   public KLineMainView(KLineDoc p_kdoc, KLineApplet p_applet, Rectangle p_klineMainViewSize, Rectangle p_klineviewSize, Rectangle p_klinevolmoneyviewSize, Rectangle p_kbottomRulerSize)
/*     */   {
/*  51 */     setBackground(CommEnv.s_sysBkColor);
/*  52 */     setFont(CommEnv.s_smallFont);
/*  53 */     this.m_klineMainViewSize = p_klineMainViewSize;
/*  54 */     this.m_kbottomRulerSize = p_kbottomRulerSize;
/*     */ 
/*  56 */     setBounds(this.m_klineMainViewSize);
/*     */ 
/*  58 */     this.m_kdoc = p_kdoc;
/*  59 */     this.m_kdoc.addObserver(this);
/*  60 */     this.m_parent = p_applet;
/*     */ 
/*  62 */     this.m_brulerCalcBegPixel = 61;
/*     */ 
/*  64 */     this.m_brulerCalcEndPixel = (this.m_kbottomRulerSize.width - 15 - 2);
/*  65 */     this.m_maxRecsInRuler = ((this.m_brulerCalcEndPixel - this.m_brulerCalcBegPixel) / 7);
/*     */ 
/*  67 */     this.m_pixelsPerRecord = ((this.m_brulerCalcEndPixel - this.m_brulerCalcBegPixel) / this.m_maxRecsInRuler);
/*     */ 
/*  69 */     int l_recCount = this.m_kdoc.f_getRecCount();
/*  70 */     int l_validRecPos = this.m_kdoc.f_getValidRecPos();
/*  71 */     if ((l_recCount > 0) && (l_validRecPos <= l_recCount - 1))
/*     */     {
/*  73 */       this.m_brulerFinRec = (l_recCount - 1);
/*  74 */       this.m_brulerOrgRec = l_validRecPos;
/*  75 */       if (this.m_brulerFinRec - this.m_brulerOrgRec + 1 > this.m_maxRecsInRuler)
/*  76 */         this.m_brulerOrgRec = (this.m_brulerFinRec - this.m_maxRecsInRuler + 1);
/*     */     }
/*  78 */     this.m_klineViewSize = p_klineviewSize;
/*  79 */     this.m_klineVolMoneyViewSize = p_klinevolmoneyviewSize;
/*     */ 
/*  81 */     setLayout(null);
/*     */ 
/*  94 */     this.m_klineView = new KIndexChild(this, this.m_kdoc, this.m_klineViewSize, 0, 0);
/*     */ 
/*  98 */     this.m_kvolmoneyView = new KIndexChild(this, this.m_kdoc, this.m_klineVolMoneyViewSize, 3, 10);
/*     */ 
/* 102 */     this.m_kminiFrame = new KMiniFrame(this, s_miniFrameSize, this.m_kdoc);
/* 103 */     add(this.m_kminiFrame);
/* 104 */     this.m_kminiFrame.hide();
/* 105 */     add(this.m_klineView);
/* 106 */     add(this.m_kvolmoneyView);
/*     */   }
/*     */ 
/*     */   public boolean f_canDecrease()
/*     */   {
/* 225 */     if ((this.m_brulerOrgRec < 0) || (this.m_brulerFinRec < 0))
/*     */     {
/* 228 */       return false;
/*     */     }
/*     */ 
/* 233 */     return this.m_pixelsPerRecord > 3.0D;
/*     */   }
/*     */ 
/*     */   public boolean f_canIncrease()
/*     */   {
/* 173 */     if ((this.m_brulerOrgRec < 0) || (this.m_brulerFinRec < 0))
/*     */     {
/* 176 */       return false;
/*     */     }
/*     */ 
/* 181 */     return this.m_pixelsPerRecord < 20.0D;
/*     */   }
/*     */ 
/*     */   public boolean f_canNextLine()
/*     */   {
/* 314 */     if ((this.m_brulerOrgRec < 0) || (this.m_brulerFinRec < 0)) {
/* 315 */       return false;
/*     */     }
/* 317 */     return this.m_brulerFinRec < this.m_kdoc.f_getRecCount() - 1;
/*     */   }
/*     */ 
/*     */   public boolean f_canNextPage()
/*     */   {
/* 379 */     if ((this.m_brulerOrgRec < 0) || (this.m_brulerFinRec < 0)) {
/* 380 */       return false;
/*     */     }
/* 382 */     return this.m_brulerFinRec < this.m_kdoc.f_getRecCount() - 1;
/*     */   }
/*     */ 
/*     */   public boolean f_canPrevLine()
/*     */   {
/* 284 */     if ((this.m_brulerOrgRec < 0) || (this.m_brulerFinRec < 0)) {
/* 285 */       return false;
/*     */     }
/* 287 */     return this.m_brulerOrgRec > this.m_kdoc.f_getValidRecPos();
/*     */   }
/*     */ 
/*     */   public boolean f_canPrevPage()
/*     */   {
/* 342 */     if ((this.m_brulerOrgRec < 0) || (this.m_brulerFinRec < 0))
/*     */     {
/* 345 */       return false;
/*     */     }
/*     */ 
/* 351 */     return this.m_brulerOrgRec > this.m_kdoc.f_getValidRecPos();
/*     */   }
/*     */ 
/*     */   public void f_decrease()
/*     */   {
/* 239 */     if (!f_canDecrease())
/* 240 */       return;
/*     */     while (true)
/*     */     {
/* 243 */       this.m_pixelsPerRecord -= 1.0D;
/* 244 */       int l_maxRecsInRuler = (int)((this.m_brulerCalcEndPixel - this.m_brulerCalcBegPixel) / this.m_pixelsPerRecord);
/* 245 */       if (l_maxRecsInRuler > this.m_maxRecsInRuler)
/*     */       {
/* 247 */         this.m_maxRecsInRuler = l_maxRecsInRuler;
/* 248 */         break;
/*     */       }
/*     */     }
/* 251 */     this.m_pixelsPerRecord = ((this.m_brulerCalcEndPixel - this.m_brulerCalcBegPixel) / this.m_maxRecsInRuler);
/*     */ 
/* 253 */     this.m_brulerOrgRec = (this.m_brulerFinRec + 1 - this.m_maxRecsInRuler);
/* 254 */     if (this.m_brulerOrgRec < 0)
/* 255 */       this.m_brulerFinRec = this.m_maxRecsInRuler;
/* 256 */     if (this.m_brulerFinRec >= this.m_kdoc.f_getRecCount())
/*     */     {
/* 258 */       this.m_brulerFinRec = (this.m_kdoc.f_getRecCount() - 1);
/* 259 */       this.m_brulerOrgRec = 0;
/*     */     }
/*     */     else
/*     */     {
/* 263 */       int l_validRecPos = this.m_kdoc.f_getValidRecPos();
/* 264 */       if (this.m_brulerOrgRec < l_validRecPos) {
/* 265 */         this.m_brulerOrgRec = l_validRecPos;
/*     */       }
/*     */     }
/* 268 */     if (this.m_recPointedByMouse != -1) {
/* 269 */       if (this.m_recPointedByMouse > this.m_brulerFinRec) {
/* 270 */         this.m_recPointedByMouse = (this.m_preRecPointedByMouse = this.m_brulerFinRec);
/*     */       }
/*     */ 
/* 273 */       if (this.m_recPointedByMouse < this.m_brulerOrgRec) {
/* 274 */         this.m_recPointedByMouse = (this.m_preRecPointedByMouse = this.m_brulerOrgRec);
/*     */       }
/*     */ 
/* 277 */       this.m_kminiFrame.repaint();
/*     */     }
/* 279 */     f_notify(new Integer(-931), true);
/*     */   }
/*     */ 
/*     */   public void f_drawCursorLine(KIndexChild p_child, int p_rec_no)
/*     */   {
/* 112 */     Graphics l_child_g = p_child.getGraphics();
/* 113 */     l_child_g.setColor(Color.black);
/* 114 */     l_child_g.setXORMode(CommEnv.s_cursorLineColor);
/* 115 */     l_child_g.drawLine(f_rec2pos(p_rec_no), p_child.m_frameSize.y, f_rec2pos(p_rec_no), p_child.m_frameSize.y + p_child.m_frameSize.height);
/*     */ 
/* 120 */     l_child_g.setPaintMode();
/*     */   }
/*     */ 
/*     */   public int f_getCursorRec()
/*     */   {
/* 164 */     return this.m_recPointedByMouse;
/*     */   }
/*     */ 
/*     */   public int f_getFinRec()
/*     */   {
/* 160 */     return this.m_brulerFinRec;
/*     */   }
/*     */ 
/*     */   public int f_getOrgRec()
/*     */   {
/* 155 */     return this.m_brulerOrgRec;
/*     */   }
/*     */ 
/*     */   public double f_getPixelsPerRecord()
/*     */   {
/* 168 */     return this.m_pixelsPerRecord;
/*     */   }
/*     */ 
/*     */   public void f_increase()
/*     */   {
/* 187 */     if (!f_canIncrease())
/* 188 */       return;
/*     */     while (true)
/*     */     {
/* 191 */       this.m_pixelsPerRecord += 1.0D;
/* 192 */       int l_maxRecsInRuler = (int)((this.m_brulerCalcEndPixel - this.m_brulerCalcBegPixel) / this.m_pixelsPerRecord);
/* 193 */       if (l_maxRecsInRuler < this.m_maxRecsInRuler)
/*     */       {
/* 195 */         this.m_maxRecsInRuler = l_maxRecsInRuler;
/* 196 */         break;
/*     */       }
/*     */     }
/* 199 */     this.m_pixelsPerRecord = ((this.m_brulerCalcEndPixel - this.m_brulerCalcBegPixel) / this.m_maxRecsInRuler);
/*     */ 
/* 201 */     int l_fin = this.m_maxRecsInRuler + this.m_brulerOrgRec - 1;
/* 202 */     if (l_fin < this.m_brulerFinRec)
/*     */     {
/* 204 */       this.m_brulerOrgRec = (this.m_brulerFinRec - this.m_maxRecsInRuler + 1);
/* 205 */       int l_validRec = this.m_kdoc.f_getValidRecPos();
/* 206 */       if (this.m_brulerOrgRec < l_validRec)
/* 207 */         this.m_brulerOrgRec = l_validRec;
/*     */     }
/* 209 */     if (this.m_recPointedByMouse != -1) {
/* 210 */       if (this.m_recPointedByMouse > this.m_brulerFinRec) {
/* 211 */         this.m_recPointedByMouse = (this.m_preRecPointedByMouse = this.m_brulerFinRec);
/*     */       }
/*     */ 
/* 214 */       if (this.m_recPointedByMouse < this.m_brulerOrgRec) {
/* 215 */         this.m_recPointedByMouse = (this.m_preRecPointedByMouse = this.m_brulerOrgRec);
/*     */       }
/*     */ 
/* 218 */       this.m_kminiFrame.repaint();
/*     */     }
/* 220 */     f_notify(new Integer(-931), true);
/*     */   }
/*     */ 
/*     */   public void f_nextLine()
/*     */   {
/* 322 */     if (!f_canNextLine())
/* 323 */       return;
/* 324 */     this.m_brulerOrgRec += 1;
/* 325 */     this.m_brulerFinRec += 1;
/* 326 */     if (this.m_recPointedByMouse != -1) {
/* 327 */       if (this.m_recPointedByMouse > this.m_brulerFinRec) {
/* 328 */         this.m_recPointedByMouse = (this.m_preRecPointedByMouse = this.m_brulerFinRec);
/*     */       }
/*     */ 
/* 331 */       if (this.m_recPointedByMouse < this.m_brulerOrgRec) {
/* 332 */         this.m_recPointedByMouse = (this.m_preRecPointedByMouse = this.m_brulerOrgRec);
/*     */       }
/*     */ 
/* 335 */       this.m_kminiFrame.repaint();
/*     */     }
/* 337 */     f_notify(new Integer(-931), true);
/*     */   }
/*     */ 
/*     */   public void f_nextPage()
/*     */   {
/* 387 */     if (!f_canNextPage())
/* 388 */       return;
/* 389 */     int l_delta = Math.min(this.m_maxRecsInRuler, this.m_kdoc.f_getRecCount() - 1 - this.m_brulerFinRec);
/*     */ 
/* 391 */     this.m_brulerOrgRec += l_delta;
/* 392 */     this.m_brulerFinRec += l_delta;
/* 393 */     if (this.m_recPointedByMouse != -1) {
/* 394 */       if (this.m_recPointedByMouse > this.m_brulerFinRec) {
/* 395 */         this.m_recPointedByMouse = (this.m_preRecPointedByMouse = this.m_brulerFinRec);
/*     */       }
/*     */ 
/* 398 */       if (this.m_recPointedByMouse < this.m_brulerOrgRec) {
/* 399 */         this.m_recPointedByMouse = (this.m_preRecPointedByMouse = this.m_brulerOrgRec);
/*     */       }
/*     */ 
/* 402 */       this.m_kminiFrame.repaint();
/*     */     }
/* 404 */     f_notify(new Integer(-931), true);
/*     */   }
/*     */ 
/*     */   private void f_notify(Object p_arg, boolean p_drawBottomRuler)
/*     */   {
/* 581 */     if (p_drawBottomRuler)
/*     */     {
/* 587 */       repaint(this.m_kbottomRulerSize.x, this.m_kbottomRulerSize.y, this.m_kbottomRulerSize.width, this.m_kbottomRulerSize.height);
/*     */     }
/*     */ 
/* 593 */     this.m_klineView.f_update(this, p_arg);
/* 594 */     this.m_kvolmoneyView.f_update(this, p_arg);
/*     */   }
/*     */ 
/*     */   public int f_pos2rec(int p_pos)
/*     */   {
/* 126 */     if ((p_pos < this.m_brulerCalcBegPixel) || (p_pos > this.m_brulerCalcEndPixel))
/*     */     {
/* 128 */       return -1;
/*     */     }
/*     */ 
/* 133 */     double l_return = (p_pos - 60) / this.m_pixelsPerRecord + this.m_brulerOrgRec;
/*     */ 
/* 135 */     if (l_return > this.m_brulerFinRec)
/* 136 */       l_return = this.m_brulerFinRec;
/* 137 */     if (l_return < this.m_brulerOrgRec) {
/* 138 */       l_return = this.m_brulerOrgRec;
/*     */     }
/* 140 */     return (int)l_return;
/*     */   }
/*     */ 
/*     */   public void f_prevLine()
/*     */   {
/* 292 */     if (!f_canPrevLine())
/* 293 */       return;
/* 294 */     this.m_brulerOrgRec -= 1;
/* 295 */     this.m_brulerFinRec -= 1;
/*     */ 
/* 298 */     if (this.m_recPointedByMouse != -1) {
/* 299 */       if (this.m_recPointedByMouse > this.m_brulerFinRec) {
/* 300 */         this.m_recPointedByMouse = (this.m_preRecPointedByMouse = this.m_brulerFinRec);
/*     */       }
/*     */ 
/* 303 */       if (this.m_recPointedByMouse < this.m_brulerOrgRec) {
/* 304 */         this.m_recPointedByMouse = (this.m_preRecPointedByMouse = this.m_brulerOrgRec);
/*     */       }
/*     */ 
/* 307 */       this.m_kminiFrame.repaint();
/*     */     }
/* 309 */     f_notify(new Integer(-931), true);
/*     */   }
/*     */ 
/*     */   public void f_prevPage()
/*     */   {
/* 357 */     if (!f_canPrevPage())
/* 358 */       return;
/* 359 */     int l_delta = Math.min(this.m_maxRecsInRuler, this.m_brulerOrgRec - this.m_kdoc.f_getValidRecPos());
/*     */ 
/* 361 */     this.m_brulerOrgRec -= l_delta;
/* 362 */     this.m_brulerFinRec -= l_delta;
/* 363 */     if (this.m_recPointedByMouse != -1) {
/* 364 */       if (this.m_recPointedByMouse > this.m_brulerFinRec) {
/* 365 */         this.m_recPointedByMouse = (this.m_preRecPointedByMouse = this.m_brulerFinRec);
/*     */       }
/*     */ 
/* 368 */       if (this.m_recPointedByMouse < this.m_brulerOrgRec) {
/* 369 */         this.m_recPointedByMouse = (this.m_preRecPointedByMouse = this.m_brulerOrgRec);
/*     */       }
/*     */ 
/* 372 */       this.m_kminiFrame.repaint();
/*     */     }
/* 374 */     f_notify(new Integer(-931), true);
/*     */   }
/*     */ 
/*     */   public int f_rec2pos(int p_rec)
/*     */   {
/* 145 */     return (int)(this.m_brulerCalcBegPixel + (p_rec - this.m_brulerOrgRec) * this.m_pixelsPerRecord + this.m_pixelsPerRecord / 2.0D + 0.5D);
/*     */   }
/*     */ 
/*     */   private void f_removeCursorLine()
/*     */   {
/* 409 */     if (this.m_preRecPointedByMouse != -1)
/*     */     {
/* 411 */       f_drawCursorLine(this.m_klineView, this.m_preRecPointedByMouse);
/* 412 */       f_drawCursorLine(this.m_kvolmoneyView, this.m_preRecPointedByMouse);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean handleEvent(Event p_e)
/*     */   {
/* 419 */     if ((p_e.id == 501) || (p_e.id == 506)) {
/* 420 */       if (this.m_parent.m_controlBar.f_someMenuShows()) {
/* 421 */         this.m_parent.m_controlBar.f_hideAllMenuPanels();
/* 422 */         AppletContext l_appletContext1 = this.m_parent.getAppletContext();
/* 423 */         l_appletContext1.showStatus("K 线 分 析");
/* 424 */         return false;
/*     */       }
/*     */ 
/* 427 */       int l_previousRec = this.m_recPointedByMouse;
/* 428 */       this.m_recPointedByMouse = f_pos2rec(p_e.x);
/*     */ 
/* 430 */       if (p_e.metaDown()) {
/* 431 */         if (this.m_preRecPointedByMouse == -1) {
/* 432 */           return super.handleEvent(p_e);
/*     */         }
/* 434 */         this.m_kminiFrame.hide();
/*     */ 
/* 446 */         f_removeCursorLine();
/* 447 */         this.m_preRecPointedByMouse = -1;
/* 448 */         this.m_recPointedByMouse = -1;
/*     */ 
/* 451 */         this.m_klineView.f_paintIndexTitle(this.m_klineView.getGraphics());
/* 452 */         this.m_kvolmoneyView.f_paintIndexTitle(this.m_kvolmoneyView.getGraphics());
/*     */       }
/*     */       else
/*     */       {
/* 466 */         if (this.m_recPointedByMouse == -1)
/*     */         {
/* 468 */           this.m_recPointedByMouse = this.m_preRecPointedByMouse;
/* 469 */           return false;
/*     */         }
/*     */ 
/* 472 */         if (this.m_kminiFrame.isShowing()) {
/* 473 */           if (this.m_recPointedByMouse != this.m_preRecPointedByMouse)
/* 474 */             this.m_kminiFrame.repaint();
/*     */         }
/* 476 */         else this.m_kminiFrame.show();
/*     */ 
/* 479 */         if (this.m_recPointedByMouse == this.m_preRecPointedByMouse) {
/* 480 */           return false;
/*     */         }
/*     */ 
/* 483 */         f_removeCursorLine();
/*     */ 
/* 492 */         f_drawCursorLine(this.m_klineView, this.m_recPointedByMouse);
/* 493 */         f_drawCursorLine(this.m_kvolmoneyView, this.m_recPointedByMouse);
/*     */ 
/* 496 */         this.m_klineView.f_paintIndexTitle(this.m_klineView.getGraphics());
/* 497 */         this.m_kvolmoneyView.f_paintIndexTitle(this.m_kvolmoneyView.getGraphics());
/*     */ 
/* 500 */         this.m_preRecPointedByMouse = this.m_recPointedByMouse;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 506 */     return super.handleEvent(p_e);
/*     */   }
/*     */ 
/*     */   public void paint(Graphics p_g)
/*     */   {
/* 603 */     p_g.clearRect(this.m_kbottomRulerSize.x, this.m_kbottomRulerSize.y, this.m_kbottomRulerSize.width, this.m_kbottomRulerSize.height);
/*     */ 
/* 607 */     String l_stockCode = this.m_kdoc.f_getStockCode();
/* 608 */     String l_message = "“";
/* 609 */     if (l_stockCode == null) {
/* 610 */       l_message = "K 线 分 析";
/*     */     }
/*     */     else {
/* 613 */       l_message = l_message + l_stockCode;
/* 614 */       l_message = l_message + "”";
/* 615 */       l_message = l_message + "K线分析";
/*     */     }
/* 617 */     AppletContext l_appletContext = this.m_parent.getAppletContext();
/* 618 */     l_appletContext.showStatus(l_message);
/* 619 */     if (this.m_brulerOrgRec >= 0)
/*     */     {
/* 622 */       String l_show = null;
/* 623 */       FontMetrics l_fm = p_g.getFontMetrics();
/*     */ 
/* 626 */       int l_ypos = this.m_kbottomRulerSize.y + l_fm.getAscent() + (this.m_kbottomRulerSize.height - (l_fm.getAscent() + l_fm.getDescent())) / 2;
/*     */ 
/* 628 */       int l_cycle = this.m_kdoc.f_getCycleType();
/* 629 */       KLineRec l_rec = this.m_kdoc.f_getRec(this.m_brulerOrgRec);
/* 630 */       if (l_cycle == 0)
/*     */       {
/* 632 */         l_show = Integer.toString(l_rec.m_tradeDate.getYear() + 1900);
/* 633 */         p_g.setColor(CommEnv.s_rulerColor);
/*     */ 
/* 635 */         p_g.drawString(l_show, this.m_kbottomRulerSize.x + 2, l_ypos);
/*     */       }
/*     */ 
/* 639 */       int l_minXPos = 2147483647;
/* 640 */       boolean l_visible = false;
/* 641 */       for (int l_i = this.m_brulerOrgRec + 1; l_i <= this.m_brulerFinRec; l_i++)
/*     */       {
/* 643 */         KLineRec l_curRec = this.m_kdoc.f_getRec(l_i);
/* 644 */         l_visible = false;
/* 645 */         if (l_cycle == 0)
/*     */         {
/* 647 */           int l_mon = l_curRec.m_tradeDate.getMonth();
/* 648 */           if (l_mon != l_rec.m_tradeDate.getMonth())
/*     */           {
/* 650 */             l_mon++;
/* 651 */             l_show = l_mon < 10 ? "0" : "";
/* 652 */             l_show = l_show + Integer.toString(l_mon);
/* 653 */             l_visible = true;
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 658 */           int l_year = l_curRec.m_tradeDate.getYear();
/* 659 */           if (l_year != l_rec.m_tradeDate.getYear())
/*     */           {
/* 661 */             if (l_year >= 100)
/* 662 */               l_year -= 100;
/* 663 */             l_show = l_year < 10 ? "0" : "";
/* 664 */             l_show = l_show + Integer.toString(l_year);
/* 665 */             l_visible = true;
/*     */           }
/*     */         }
/*     */ 
/* 669 */         if (l_visible)
/*     */         {
/* 671 */           int l_xpos = f_rec2pos(l_i) - l_fm.stringWidth(l_show) / 2;
/* 672 */           if (l_minXPos > l_xpos)
/* 673 */             l_minXPos = l_xpos;
/* 674 */           p_g.drawString(l_show, l_xpos, l_ypos);
/*     */         }
/* 676 */         l_rec = l_curRec;
/*     */       }
/*     */ 
/* 679 */       l_rec = this.m_kdoc.f_getRec(this.m_brulerOrgRec);
/* 680 */       if (l_cycle == 0)
/*     */       {
/* 682 */         int l_mon = l_rec.m_tradeDate.getMonth() + 1;
/* 683 */         l_show = l_mon < 10 ? "0" : "";
/* 684 */         l_show = l_show + Integer.toString(l_mon);
/* 685 */         l_visible = true;
/*     */       }
/*     */       else
/*     */       {
/* 689 */         int l_year = l_rec.m_tradeDate.getYear();
/* 690 */         if (l_year >= 100)
/* 691 */           l_year -= 100;
/* 692 */         l_show = l_year < 10 ? "0" : "";
/* 693 */         l_show = l_show + Integer.toString(l_year);
/* 694 */         l_visible = true;
/*     */       }
/* 696 */       if (l_visible)
/*     */       {
/* 698 */         int l_offset = l_fm.stringWidth(l_show) / 2;
/* 699 */         int l_xpos = f_rec2pos(this.m_brulerOrgRec);
/* 700 */         if (l_xpos + l_offset < l_minXPos)
/* 701 */           p_g.drawString(l_show, l_xpos - l_offset, l_ypos);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void update(Observable p_obs, Object p_arg)
/*     */   {
/* 512 */     int l_recCount = this.m_kdoc.f_getRecCount();
/* 513 */     int l_validRecPos = this.m_kdoc.f_getValidRecPos();
/* 514 */     switch (((Integer)p_arg).intValue())
/*     */     {
/*     */     case -912:
/*     */     case -911:
/* 518 */       this.m_brulerOrgRec = -1;
/* 519 */       this.m_brulerFinRec = -1;
/* 520 */       if (this.m_kminiFrame.isShowing()) {
/* 521 */         this.m_kminiFrame.hide();
/* 522 */         f_removeCursorLine();
/*     */       }
/* 524 */       this.m_recPointedByMouse = (this.m_preRecPointedByMouse = -1);
/*     */ 
/* 526 */       f_notify(p_arg, true);
/* 527 */       break;
/*     */     case -913:
/* 529 */       boolean l_drawBottomRuler = false;
/* 530 */       int l_brulerFinRec = this.m_brulerOrgRec == -1 ? l_recCount - 1 : this.m_brulerFinRec;
/*     */ 
/* 533 */       int l_brulerOrgRec = l_validRecPos;
/* 534 */       if (l_brulerFinRec - l_brulerOrgRec + 1 > this.m_maxRecsInRuler) {
/* 535 */         l_brulerOrgRec = l_brulerFinRec - this.m_maxRecsInRuler + 1;
/*     */       }
/* 537 */       if ((l_brulerOrgRec != this.m_brulerOrgRec) || (l_brulerFinRec != this.m_brulerFinRec))
/*     */       {
/* 540 */         this.m_brulerOrgRec = l_brulerOrgRec;
/* 541 */         this.m_brulerFinRec = l_brulerFinRec;
/* 542 */         l_drawBottomRuler = true;
/*     */       }
/* 544 */       f_notify(p_arg, l_drawBottomRuler);
/* 545 */       break;
/*     */     case -914:
/* 553 */       if ((this.m_brulerFinRec == l_recCount - 2) || (this.m_brulerFinRec == -1))
/*     */       {
/* 557 */         this.m_brulerFinRec = (l_recCount - 1);
/* 558 */         this.m_brulerOrgRec = l_validRecPos;
/* 559 */         if (this.m_brulerFinRec - this.m_brulerOrgRec + 1 > this.m_maxRecsInRuler)
/* 560 */           this.m_brulerOrgRec = (this.m_brulerFinRec - this.m_maxRecsInRuler + 1);
/* 561 */         f_notify(new Integer(-932), true);
/*     */       }
/* 563 */       if (!this.m_kminiFrame.isShowing()) break;
/* 564 */       this.m_kminiFrame.repaint();
/* 565 */       break;
/*     */     case -915:
/* 570 */       if (this.m_brulerFinRec == l_recCount - 1)
/* 571 */         f_notify(new Integer(-933), false);
/* 572 */       if (!this.m_kminiFrame.isShowing()) break;
/* 573 */       this.m_kminiFrame.repaint();
/*     */     }
/*     */   }
/*     */ }

/* Location:           F:\stock research\kline\
 * Qualified Name:     lido.kline.KLineMainView
 * JD-Core Version:    0.6.0
 */