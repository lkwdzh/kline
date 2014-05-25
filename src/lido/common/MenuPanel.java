/*     */ package lido.common;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Event;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Image;
/*     */ import java.awt.Panel;
/*     */ import java.awt.Rectangle;
/*     */ 
/*     */ class MenuPanel extends Panel
/*     */   implements Runnable
/*     */ {
/*     */   int m_actualItemNum;
/*     */   Rectangle m_bounds;
/*     */   boolean[] m_fillFlag;
/*     */   int m_firstShowingIndex;
/*     */   int m_focusedIndex;
/*     */   boolean[] m_itemSelected;
/*     */   int m_menuIndex;
/*     */   String[] m_menuItem;
/*     */   int m_menuState;
/*     */   boolean m_multiMode;
/*  59 */   private boolean m_noSelected = false;
/*     */   ControlBar m_parent;
/*     */   int m_selectedIndex;
/*     */   int[] m_sepLocation;
/*  81 */   int m_sepNum = 0;
/*     */   int m_showingItemNum;
/*     */   Thread m_thread;
/*     */   int m_totalItemNum;
/*     */   boolean m_useExtent;
/*  24 */   Color s_background_color = new Color(0, 0, 0);
/*     */   public static final int s_extent = 10;
/*  23 */   Color s_item_color = new Color(255, 255, 255);
/*     */   public static final int s_item_height = 20;
/*  26 */   Color s_item_selected_color = new Color(255, 255, 255);
/*     */ 
/*  28 */   Color s_line_color = new Color(100, 100, 100);
/*     */   public static final int s_margin = 16;
/*     */   public static final int s_max_items = 12;
/*  33 */   Color s_menu_botton_color = new Color(255, 255, 200);
/*  34 */   Color s_menu_botton_selected_color = new Color(255, 255, 200);
/*     */   private static final int s_menu_button_down = -2;
/*     */   private static final int s_menu_button_up = -1;
/*  22 */   Color s_menu_frame_color = Color.white;
/*     */   private static final int s_menu_no_where = -3;
/*  43 */   public static double s_pixel_per_uni = 12.0D;
/*     */   public static final int s_scroll_backward = 2;
/*     */   public static final int s_scroll_forward = 1;
/*     */   public static final int s_scroll_halt = 0;
/*  25 */   Color s_selected_bg_color = new Color(100, 100, 100);
/*     */   public static final int s_sleep_gap = 150;
/*     */ 
/*     */   public MenuPanel(ControlBar p_bar, int p_menu_index)
/*     */   {
/*  86 */     this.m_parent = p_bar;
/*  87 */     this.m_menuIndex = p_menu_index;
/*  88 */     this.m_bounds = new Rectangle();
/*  89 */     this.m_multiMode = false;
/*  90 */     setBackground(this.s_background_color);
/*     */   }
/*     */ 
/*     */   public void f_destroy()
/*     */   {
/* 905 */     if (this.m_thread != null)
/*     */     {
/* 907 */       this.m_thread.stop();
/* 908 */       System.gc();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void f_drawItem(Color p_color, Graphics p_g, int p_index)
/*     */   {
/* 576 */     int l_start = 0;
/* 577 */     int l_sepNum = 0;
/* 578 */     if ((this.m_sepNum == 1) && 
/* 579 */       (p_index >= this.m_sepLocation[0]) && (this.m_sepLocation[0] >= this.m_firstShowingIndex) && (this.m_sepLocation[0] < this.m_firstShowingIndex + this.m_showingItemNum))
/*     */     {
/* 582 */       l_sepNum++;
/* 583 */     }if (this.m_sepNum > 1)
/*     */     {
/* 585 */       for (int l_i = 0; l_i < this.m_sepNum; l_i++) {
/* 586 */         if ((this.m_sepLocation[l_i] < this.m_firstShowingIndex) || (this.m_sepLocation[l_i] >= this.m_firstShowingIndex + this.m_showingItemNum) || (p_index < this.m_sepLocation[l_i])) {
/*     */           continue;
/*     */         }
/* 589 */         l_sepNum++;
/*     */       }
/*     */     }
/* 591 */     if (this.m_useExtent) {
/* 592 */       l_start = 10;
/*     */     }
/* 594 */     int l_drawRectHeight = 0;
/* 595 */     p_g.setColor(p_color);
/* 596 */     if (!this.m_fillFlag[p_index])
/*     */     {
/* 598 */       p_g.setColor(new Color(128, 128, 128));
/* 599 */       l_drawRectHeight = l_start + (p_index - this.m_firstShowingIndex - l_sepNum) * 20 + l_sepNum * 20 / 2;
/*     */ 
/* 602 */       if (this.m_useExtent)
/*     */       {
/* 604 */         if (l_drawRectHeight > this.m_bounds.height - 10 - 3 - 10) {
/* 605 */           return;
/*     */         }
/*     */       }
/* 608 */       else if (l_drawRectHeight > this.m_bounds.height - 1)
/* 609 */         return;
/* 610 */       p_g.drawLine(3, l_start - 4 + (p_index - this.m_firstShowingIndex - l_sepNum + 1) * 20 + l_sepNum * 20 / 2, 8 + this.m_bounds.width - 11, l_start - 4 + (p_index - this.m_firstShowingIndex - l_sepNum + 1) * 20 + l_sepNum * 20 / 2);
/*     */ 
/* 618 */       p_g.setColor(new Color(255, 255, 255));
/* 619 */       p_g.drawLine(3, l_start - 3 + (p_index - this.m_firstShowingIndex - l_sepNum + 1) * 20 + l_sepNum * 20 / 2, 8 + this.m_bounds.width - 11, l_start - 3 + (p_index - this.m_firstShowingIndex - l_sepNum + 1) * 20 + l_sepNum * 20 / 2);
/*     */     }
/*     */     else
/*     */     {
/* 630 */       if (this.m_multiMode) {
/* 631 */         l_drawRectHeight = l_start + (p_index - this.m_firstShowingIndex + 1 - l_sepNum) * 20 + l_sepNum * 20 / 2;
/*     */ 
/* 634 */         if (this.m_useExtent) {
/* 635 */           if (l_drawRectHeight > this.m_bounds.height - 10 - 1) {
/* 636 */             return;
/*     */           }
/* 638 */           if (l_drawRectHeight > this.m_bounds.height - 1)
/* 639 */             return; 
/*     */         }
/* 640 */         if (this.m_itemSelected[p_index]) {
/* 641 */           p_g.fillOval(8, l_start + (p_index - this.m_firstShowingIndex + 1 - l_sepNum) * 20 - 12 + l_sepNum * 20 / 2, 4, 4);
/*     */         }
/*     */ 
/*     */       }
/* 650 */       else if ((p_index == this.m_selectedIndex) && (!this.m_noSelected)) {
/* 651 */         p_g.fillOval(8, l_start + (p_index - this.m_firstShowingIndex + 1 - l_sepNum) * 20 - 12 + l_sepNum * 20 / 2, 4, 4);
/*     */       }
/*     */ 
/* 658 */       if (this.m_useExtent)
/*     */       {
/* 660 */         l_drawRectHeight = l_start + (p_index - this.m_firstShowingIndex - l_sepNum) * 20 + l_sepNum * 20 / 2;
/*     */ 
/* 663 */         if (l_drawRectHeight > this.m_bounds.height - 10 - 1) {
/* 664 */           return;
/*     */         }
/* 666 */         p_g.drawString(this.m_menuItem[p_index], 16, l_start + (p_index - this.m_firstShowingIndex + 1 - l_sepNum) * 20 + l_sepNum * 20 / 2 - 4);
/*     */       }
/*     */       else
/*     */       {
/* 673 */         if (l_drawRectHeight > this.m_bounds.height - 1) {
/* 674 */           return;
/*     */         }
/* 676 */         p_g.drawString(this.m_menuItem[p_index], 16, l_start + (p_index - this.m_firstShowingIndex + 1 - l_sepNum) * 20 + l_sepNum * 20 / 2 - 4);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void f_drawMenuBody(Graphics p_g)
/*     */   {
/* 685 */     if (this.m_useExtent)
/* 686 */       p_g.clearRect(1, 10, this.m_bounds.width - 2, this.m_bounds.height - 20);
/*     */     else
/* 688 */       p_g.clearRect(1, 1, this.m_bounds.width - 2, this.m_bounds.height - 2);
/* 689 */     int l_showingItemNum = 0;
/* 690 */     int l_y = 0;
/* 691 */     if (this.m_useExtent)
/*     */     {
/* 693 */       l_y = 10;
/* 694 */       for (int l_i = this.m_firstShowingIndex; l_i < this.m_totalItemNum; l_i++)
/*     */       {
/* 696 */         if (l_y >= this.m_bounds.height - 10 - 1)
/*     */           continue;
/* 698 */         l_showingItemNum++;
/* 699 */         if (this.m_fillFlag[l_i])
/* 700 */           l_y += 20;
/*     */         else {
/* 702 */           l_y += 10;
/*     */         }
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 708 */       for (int l_i = this.m_firstShowingIndex; l_i < this.m_totalItemNum; l_i++)
/*     */       {
/* 710 */         if (l_y >= this.m_bounds.height - 1)
/*     */           continue;
/* 712 */         l_showingItemNum++;
/* 713 */         if (this.m_fillFlag[l_i] )
/* 714 */           l_y += 20;
/*     */         else {
/* 716 */           l_y += 10;
/*     */         }
/*     */       }
/*     */     }
/* 720 */     this.m_actualItemNum = l_showingItemNum;
/* 721 */     for (int l_i = 0; l_i < l_showingItemNum; l_i++)
/* 722 */       f_drawItem(this.s_item_color, p_g, l_i + this.m_firstShowingIndex);
/*     */   }
/*     */ 
/*     */   private int f_locate(int p_y)
/*     */   {
/* 467 */     int l_height = 0;
/* 468 */     if (this.m_useExtent) {
/* 469 */       l_height = 10;
/* 470 */       if (p_y < 10)
/* 471 */         return -1;
/* 472 */       if (p_y >= this.m_bounds.height - 10 - 2)
/* 473 */         return -2;
/* 474 */       for (int l_i = 0; l_i < this.m_totalItemNum - 1; l_i++)
/*     */       {
/* 476 */         int l_end = l_height;
/* 477 */         if (!this.m_fillFlag[(l_i + this.m_firstShowingIndex)])
/* 478 */           l_end += 10;
/*     */         else
/* 480 */           l_end += 20;
/* 481 */         if ((p_y >= l_height) && (p_y < l_end))
/*     */         {
/* 483 */           if (l_i + this.m_firstShowingIndex >= this.m_totalItemNum) {
/* 484 */             return this.m_totalItemNum - 1;
/*     */           }
/* 486 */           return l_i + this.m_firstShowingIndex;
/*     */         }
/* 488 */         l_height = l_end;
/*     */       }
/* 490 */       return -3;
/*     */     }
/*     */ 
/* 494 */     for (int l_i = 0; l_i < this.m_totalItemNum; l_i++)
/*     */     {
/* 496 */       int l_end = l_height;
/* 497 */       if (!this.m_fillFlag[(l_i + this.m_firstShowingIndex)])
/* 498 */         l_end += 10;
/*     */       else
/* 500 */         l_end += 20;
/* 501 */       if ((p_y >= l_height) && (p_y < l_end))
/*     */       {
/* 503 */         if (l_i + this.m_firstShowingIndex >= this.m_totalItemNum) {
/* 504 */           return this.m_totalItemNum - 1;
/*     */         }
/* 506 */         return l_i + this.m_firstShowingIndex;
/*     */       }
/* 508 */       l_height = l_end;
/*     */     }
/*     */ 
/* 511 */     return 0;
/*     */   }
/*     */ 
/*     */   private void f_paintItemRect(Color p_rect, Graphics p_g, int p_index) {
/* 515 */     if ((p_index < 0) || (p_index > this.m_actualItemNum))
/* 516 */       return;
/* 517 */     if (!this.m_fillFlag[(this.m_firstShowingIndex + p_index)] ) {
/* 518 */       return;
/*     */     }
/* 520 */     int l_x = 1;
/* 521 */     int l_width = this.m_bounds.width - 2;
/*     */     int l_y;
/* 522 */     if (this.m_useExtent)
/*     */     {
/* 524 */       l_y = 11;
/* 525 */       for (int l_i = 0; l_i < p_index; l_i++)
/*     */       {
/* 527 */         if (this.m_fillFlag[(l_i + this.m_firstShowingIndex)])
/* 528 */           l_y += 20;
/*     */         else
/* 530 */           l_y += 10;
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 535 */       l_y = 1;
/* 536 */       for (int l_i = 0; l_i < p_index; l_i++)
/*     */       {
/* 538 */         if (this.m_fillFlag[(this.m_firstShowingIndex + l_i)] )
/* 539 */           l_y += 20;
/*     */         else
/* 541 */           l_y += 10;
/*     */       }
/*     */     }
/* 544 */     int l_height = 19;
/* 545 */     if (this.m_useExtent)
/*     */     {
/* 547 */       if (this.m_fillFlag[(this.m_firstShowingIndex + p_index)] )
/*     */       {
/* 549 */         if (l_height > this.m_bounds.height - 10 + 2 - 20) {
/* 550 */           return;
/*     */         }
/*     */ 
/*     */       }
/* 554 */       else if (l_height > this.m_bounds.height - 10 + 2 - 10) {
/* 555 */         return;
/*     */       }
/*     */ 
/*     */     }
/* 560 */     else if (this.m_fillFlag[(this.m_firstShowingIndex + p_index)])
/*     */     {
/* 562 */       if (l_height > this.m_bounds.height - 20 + 2) {
/* 563 */         return;
/*     */       }
/*     */ 
/*     */     }
/* 567 */     else if (l_height > this.m_bounds.height - 10 + 2) {
/* 568 */       return;
/*     */     }
/*     */ 
/* 571 */     p_g.setColor(p_rect);
/* 572 */     p_g.fillRect(l_x, l_y, l_width, l_height);
/*     */   }
/*     */ 
/*     */   private void f_paintMoveRect(Color p_color, Graphics p_g, int p_where, boolean p_3D)
/*     */   {
/* 748 */     if (p_where == -1)
/*     */     {
/* 750 */       p_g.clearRect(1, 0, this.m_bounds.width - 2, 10);
/*     */ 
/* 753 */       int[] l_x = new int[3];
/* 754 */       int[] l_y = new int[3];
/* 755 */       l_x[0] = (this.m_bounds.width / 2);
/* 756 */       l_x[1] = (this.m_bounds.width / 2 + 4);
/* 757 */       l_x[2] = (this.m_bounds.width / 2 - 3);
/* 758 */       l_y[0] = 3;
/* 759 */       l_y[2] = 7; l_y[1] = 7;
/*     */ 
/* 762 */       if (p_3D) {
/* 763 */         p_g.setColor(p_color);
/*     */ 
/* 765 */         p_g.draw3DRect(1, 1, this.m_bounds.width - 3, 8, false);
/* 766 */         p_g.setColor(this.s_menu_botton_selected_color);
/* 767 */         p_g.fillPolygon(l_x, l_y, 3);
/*     */       }
/*     */       else
/*     */       {
/* 771 */         p_g.setColor(this.s_line_color);
/* 772 */         p_g.fillPolygon(l_x, l_y, 3);
/*     */       }
/*     */     }
/* 775 */     if (p_where == -2) {
/* 776 */       p_g.clearRect(0, this.m_bounds.height - 10, this.m_bounds.width, 10);
/*     */ 
/* 778 */       p_g.setColor(this.s_line_color);
/*     */ 
/* 780 */       int[] l_x = new int[3];
/* 781 */       int[] l_y = new int[3];
/* 782 */       l_x[0] = (this.m_bounds.width / 2 - 2);
/* 783 */       l_x[1] = (this.m_bounds.width / 2 + 3);
/* 784 */       l_x[2] = (this.m_bounds.width / 2);
/*     */       int tmp267_266 = (this.m_bounds.height - 5 - 1); l_y[1] = tmp267_266; l_y[0] = tmp267_266;
/*     */ 
/* 787 */       l_y[2] = (this.m_bounds.height - 5 + 2);
/*     */ 
/* 789 */       if (p_3D) {
/* 790 */         p_g.setColor(p_color);
/* 791 */         p_g.draw3DRect(1, this.m_bounds.height - 10, this.m_bounds.width - 3, 8, false);
/* 792 */         p_g.setColor(this.s_menu_botton_selected_color);
/* 793 */         p_g.fillPolygon(l_x, l_y, 3);
/*     */       }
/*     */       else
/*     */       {
/* 797 */         p_g.setColor(this.s_line_color);
/* 798 */         p_g.fillPolygon(l_x, l_y, 3);
/*     */       }
/*     */     }
/*     */ 
/* 802 */     p_g.setColor(this.s_menu_frame_color);
/* 803 */     p_g.draw3DRect(0, 0, this.m_bounds.width - 1, this.m_bounds.height - 1, true);
/*     */   }
/*     */ 
/*     */   public void f_selectItem(int p_index)
/*     */   {
/* 376 */     if ((p_index < 0) || (p_index >= this.m_totalItemNum))
/* 377 */       return;
/* 378 */     if (!this.m_fillFlag[p_index] )
/* 379 */       return;
/* 380 */     if (!this.m_multiMode) {
/* 381 */       this.m_selectedIndex = p_index;
/*     */     }
/*     */     else
/*     */     {
/* 385 */       int l_begin = 0;
/* 386 */       int l_end = 0;
/* 387 */       if (p_index < this.m_sepLocation[0])
/*     */       {
/* 389 */         l_begin = 0;
/* 390 */         l_end = this.m_sepLocation[0];
/*     */       }
/* 392 */       else if (p_index > this.m_sepLocation[(this.m_sepNum - 1)])
/*     */       {
/* 394 */         l_begin = this.m_sepLocation[(this.m_sepNum - 1)];
/* 395 */         l_end = this.m_totalItemNum;
/*     */       }
/*     */       else
/*     */       {
/* 399 */         for (int l_i = 0; l_i < this.m_sepNum - 1; l_i++)
/*     */         {
/* 401 */           if ((p_index <= this.m_sepLocation[l_i]) || (p_index >= this.m_sepLocation[(l_i + 1)]))
/*     */             continue;
/* 403 */           l_begin = this.m_sepLocation[l_i] + 1;
/* 404 */           l_end = this.m_sepLocation[(l_i + 1)];
/*     */         }
/*     */       }
/*     */ 
/* 408 */       for (int l_i = l_begin; l_i < l_end; l_i++)
/* 409 */         this.m_itemSelected[l_i] = false;
/*     */     }
/* 411 */     this.m_itemSelected[p_index] = true;
/*     */   }
/*     */ 
/*     */   public void f_setMenu(String[] p_itemNames, int p_num, boolean p_multiMode)
/*     */   {
/* 179 */     int l_length = -2147483648;
/*     */ 
/* 181 */     this.m_menuItem = new String[p_num];
/* 182 */     this.m_fillFlag = new boolean[p_num];
/* 183 */     this.m_noSelected = true;
/* 184 */     for (int l_i = 0; l_i < p_num; l_i++)
/* 185 */       this.m_fillFlag[l_i] = true;
/* 186 */     int l_itemLen = 0; int l_briefLen = 0;
/* 187 */     String l_sep = "\t"; String l_space = "¡¡";
/* 188 */     for (int l_i = 0; l_i < p_num; l_i++)
/*     */     {
/* 190 */       int l_sepIndex = p_itemNames[l_i].indexOf(l_sep);
/* 191 */       if (l_sepIndex >= 0)
/*     */       {
/* 193 */         String l_item = p_itemNames[l_i].substring(0, l_sepIndex);
/* 194 */         String l_brief = p_itemNames[l_i].substring(l_sepIndex + 2);
/* 195 */         if (l_itemLen < l_item.length())
/* 196 */           l_itemLen = l_item.length();
/* 197 */         if (l_briefLen < l_brief.length()) {
/* 198 */           l_briefLen = l_brief.length();
/*     */         }
/*     */ 
/*     */       }
/* 202 */       else if (l_itemLen < p_itemNames[l_i].length()) {
/* 203 */         l_itemLen = p_itemNames[l_i].length();
/*     */       }
/*     */     }
/* 206 */     int l_width = l_briefLen > 0 ? (int)((l_itemLen + 1) * s_pixel_per_uni) + (int)(l_briefLen * s_pixel_per_uni / 2.0D) + 32 : (int)(l_itemLen * s_pixel_per_uni) + 32;
/*     */ 
/* 209 */     for (int l_i = 0; l_i < p_num; l_i++)
/*     */     {
/* 211 */       int l_sepIndex = p_itemNames[l_i].indexOf(l_sep);
/* 212 */       if (l_sepIndex >= 0)
/*     */       {
/* 214 */         String l_item = p_itemNames[l_i].substring(0, l_sepIndex);
/* 215 */         String l_brief = p_itemNames[l_i].substring(l_sepIndex + 1);
/* 216 */         this.m_menuItem[l_i] = l_item;
/* 217 */         for (int l_j = l_item.length(); l_j <= l_itemLen; l_j++)
/*     */         {
/*     */           int tmp304_302 = l_i;
/*     */           String[] tmp304_299 = this.m_menuItem; tmp304_299[tmp304_302] = (tmp304_299[tmp304_302] + l_space);
/*     */         }
/*     */         int tmp341_339 = l_i;
/*     */         String[] tmp341_336 = this.m_menuItem; tmp341_336[tmp341_339] = (tmp341_336[tmp341_339] + l_brief);
/*     */       }
/*     */       else {
/* 222 */         this.m_menuItem[l_i] = p_itemNames[l_i];
/*     */       }
/*     */     }
/* 224 */     int l_x = this.m_parent.m_startPixel[this.m_menuIndex];
/* 225 */     int l_y = this.m_parent.m_bounds.y + this.m_parent.m_bounds.height;
/* 226 */     if (l_x + l_width > this.m_parent.m_bounds.width - 2) {
/* 227 */       l_x = this.m_parent.m_bounds.width - 2 - l_width;
/*     */     }
/* 229 */     l_x += this.m_parent.m_bounds.x;
/* 230 */     this.m_firstShowingIndex = 0;
/* 231 */     this.m_focusedIndex = -3;
/* 232 */     this.m_totalItemNum = p_num;
/* 233 */     this.m_multiMode = p_multiMode;
/* 234 */     this.m_itemSelected = new boolean[this.m_totalItemNum];
/* 235 */     for (int l_i = 0; l_i < this.m_totalItemNum; l_i++) {
/* 236 */       this.m_itemSelected[l_i] = false;
/*     */     }
/* 238 */     if (p_num <= 12) {
/* 239 */       this.m_showingItemNum = p_num;
/* 240 */       this.m_useExtent = false;
/*     */     } else {
/* 242 */       this.m_showingItemNum = 12;
/* 243 */       this.m_useExtent = true;
/*     */     }
/*     */     int l_height;
/* 245 */     if (this.m_useExtent)
/* 246 */       l_height = this.m_showingItemNum * 20 + 20;
/*     */     else {
/* 248 */       l_height = this.m_showingItemNum * 20;
/*     */     }
/* 250 */     this.m_bounds = new Rectangle(l_x, l_y, l_width, l_height);
/* 251 */     setBounds(this.m_bounds);
/*     */ 
/* 253 */     hide();
/*     */ 
/* 255 */     if (this.m_useExtent) {
/* 256 */       System.gc();
/* 257 */       this.m_thread = new Thread(this);
/* 258 */       this.m_menuState = 0;
/* 259 */       this.m_thread.start();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void f_setMenu(String[] p_itemNames, int p_num, int p_select_default, boolean p_multiMode)
/*     */   {
/*  94 */     int l_length = -2147483648;
/*     */ 
/*  96 */     this.m_menuItem = new String[p_num];
/*  97 */     this.m_fillFlag = new boolean[p_num];
/*  98 */     for (int l_i = 0; l_i < p_num; l_i++)
/*  99 */       this.m_fillFlag[l_i] = true;
/* 100 */     int l_itemLen = 0; int l_briefLen = 0;
/* 101 */     String l_sep = "\t"; String l_space = "¡¡";
/* 102 */     for (int l_i = 0; l_i < p_num; l_i++)
/*     */     {
/* 104 */       int l_sepIndex = p_itemNames[l_i].indexOf(l_sep);
/* 105 */       if (l_sepIndex >= 0)
/*     */       {
/* 107 */         String l_item = p_itemNames[l_i].substring(0, l_sepIndex);
/* 108 */         String l_brief = p_itemNames[l_i].substring(l_sepIndex + 2);
/* 109 */         if (l_itemLen < l_item.length())
/* 110 */           l_itemLen = l_item.length();
/* 111 */         if (l_briefLen < l_brief.length()) {
/* 112 */           l_briefLen = l_brief.length();
/*     */         }
/*     */ 
/*     */       }
/* 116 */       else if (l_itemLen < p_itemNames[l_i].length()) {
/* 117 */         l_itemLen = p_itemNames[l_i].length();
/*     */       }
/*     */     }
/* 120 */     int l_width = l_briefLen > 0 ? (int)((l_itemLen + 1) * s_pixel_per_uni) + (int)(l_briefLen * s_pixel_per_uni / 2.0D) + 32 : (int)(l_itemLen * s_pixel_per_uni) + 32;
/*     */ 
/* 123 */     for (int l_i = 0; l_i < p_num; l_i++)
/*     */     {
/* 125 */       int l_sepIndex = p_itemNames[l_i].indexOf(l_sep);
/* 126 */       if (l_sepIndex >= 0)
/*     */       {
/* 128 */         String l_item = p_itemNames[l_i].substring(0, l_sepIndex);
/* 129 */         String l_brief = p_itemNames[l_i].substring(l_sepIndex + 1);
/* 130 */         this.m_menuItem[l_i] = l_item;
/* 131 */         for (int l_j = l_item.length(); l_j <= l_itemLen; l_j++)
/*     */         {
/*     */           int tmp299_297 = l_i;
/*     */           String[] tmp299_294 = this.m_menuItem; tmp299_294[tmp299_297] = (tmp299_294[tmp299_297] + l_space);
/*     */         }
/*     */         int tmp336_334 = l_i;
/*     */         String[] tmp336_331 = this.m_menuItem; tmp336_331[tmp336_334] = (tmp336_331[tmp336_334] + l_brief);
/*     */       }
/*     */       else {
/* 136 */         this.m_menuItem[l_i] = p_itemNames[l_i];
/*     */       }
/*     */     }
/* 138 */     int l_x = this.m_parent.m_startPixel[this.m_menuIndex];
/* 139 */     int l_y = this.m_parent.m_bounds.y + this.m_parent.m_bounds.height;
/* 140 */     if (l_x + l_width > this.m_parent.m_bounds.width - 2) {
/* 141 */       l_x = this.m_parent.m_bounds.width - 2 - l_width;
/*     */     }
/* 143 */     l_x += this.m_parent.m_bounds.x;
/* 144 */     this.m_firstShowingIndex = 0;
/* 145 */     this.m_selectedIndex = p_select_default;
/* 146 */     this.m_focusedIndex = -3;
/* 147 */     this.m_totalItemNum = p_num;
/* 148 */     this.m_multiMode = p_multiMode;
/* 149 */     this.m_itemSelected = new boolean[this.m_totalItemNum];
/* 150 */     for (int l_i = 0; l_i < this.m_totalItemNum; l_i++) {
/* 151 */       this.m_itemSelected[l_i] = false;
/*     */     }
/* 153 */     if (p_num <= 12) {
/* 154 */       this.m_showingItemNum = p_num;
/* 155 */       this.m_useExtent = false;
/*     */     } else {
/* 157 */       this.m_showingItemNum = 12;
/* 158 */       this.m_useExtent = true;
/*     */     }
/*     */     int l_height;
/* 160 */     if (this.m_useExtent)
/* 161 */       l_height = this.m_showingItemNum * 20 + 20;
/*     */     else {
/* 163 */       l_height = this.m_showingItemNum * 20;
/*     */     }
/* 165 */     this.m_bounds = new Rectangle(l_x, l_y, l_width, l_height);
/* 166 */     setBounds(this.m_bounds);
/*     */ 
/* 168 */     hide();
/*     */ 
/* 170 */     if (this.m_useExtent) {
/* 171 */       System.gc();
/* 172 */       this.m_thread = new Thread(this);
/* 173 */       this.m_menuState = 0;
/* 174 */       this.m_thread.start();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void f_setMultiMenu(String[] p_itemNames, int p_num, int p_select_default, int p_sepNum, int[] p_sepLocation, boolean p_multiMode)
/*     */   {
/* 265 */     int l_length = -2147483648;
/*     */ 
/* 268 */     this.m_menuItem = new String[p_num + p_sepNum];
/* 269 */     this.m_sepNum = p_sepNum;
/* 270 */     this.m_sepLocation = new int[this.m_sepNum];
/* 271 */     this.m_sepLocation = p_sepLocation;
/* 272 */     this.m_fillFlag = new boolean[p_num + this.m_sepNum];
/* 273 */     for (int l_i = 0; l_i < p_num + this.m_sepNum; l_i++)
/* 274 */       this.m_fillFlag[l_i] = true;
/* 275 */     int l_itemLen = 0; int l_briefLen = 0;
/* 276 */     String l_sep = "\t"; String l_space = "¡¡";
/* 277 */     for (int l_i = 0; l_i < p_num; l_i++)
/*     */     {
/* 279 */       int l_sepIndex = p_itemNames[l_i].indexOf(l_sep);
/* 280 */       if (l_sepIndex >= 0)
/*     */       {
/* 282 */         String l_item = p_itemNames[l_i].substring(0, l_sepIndex);
/* 283 */         String l_brief = p_itemNames[l_i].substring(l_sepIndex + 2);
/* 284 */         if (l_itemLen < l_item.length())
/* 285 */           l_itemLen = l_item.length();
/* 286 */         if (l_briefLen < l_brief.length()) {
/* 287 */           l_briefLen = l_brief.length();
/*     */         }
/*     */ 
/*     */       }
/* 291 */       else if (l_itemLen < p_itemNames[l_i].length()) {
/* 292 */         l_itemLen = p_itemNames[l_i].length();
/*     */       }
/*     */     }
/* 295 */     int l_width = l_briefLen > 0 ? (int)((l_itemLen + 1) * s_pixel_per_uni) + (int)(l_briefLen * s_pixel_per_uni / 2.0D) + 32 : (int)(l_itemLen * s_pixel_per_uni) + 32;
/*     */ 
/* 298 */     int l_sepNum = 0;
/* 299 */     for (int l_i = 0; l_i < p_num + this.m_sepNum; l_i++)
/*     */     {
/* 301 */       for (int l_j = l_sepNum; l_j < this.m_sepNum; l_j++)
/*     */       {
/* 303 */         if (l_i != this.m_sepLocation[l_j])
/*     */           continue;
/* 305 */         this.m_menuItem[l_i] = " ";
/* 306 */         l_sepNum++;
/* 307 */         this.m_fillFlag[l_i] = false;
/*     */       }
/*     */ 
/* 310 */       if (!this.m_fillFlag[l_i] )
/*     */         continue;
/* 312 */       int l_sepIndex = p_itemNames[(l_i - l_sepNum)].indexOf(l_sep);
/* 313 */       if (l_sepIndex >= 0)
/*     */       {
/* 315 */         String l_item = p_itemNames[(l_i - l_sepNum)].substring(0, l_sepIndex);
/* 316 */         String l_brief = p_itemNames[(l_i - l_sepNum)].substring(l_sepIndex + 1);
/* 317 */         this.m_menuItem[l_i] = l_item;
/* 318 */         for (int l_j = l_item.length(); l_j <= l_itemLen; l_j++)
/*     */         {
/*     */           int tmp409_407 = l_i;
/*     */           String[] tmp409_404 = this.m_menuItem; tmp409_404[tmp409_407] = (tmp409_404[tmp409_407] + l_space);
/*     */         }
/*     */         int tmp446_444 = l_i;
/*     */         String[] tmp446_441 = this.m_menuItem; tmp446_441[tmp446_444] = (tmp446_441[tmp446_444] + l_brief);
/*     */       }
/*     */       else {
/* 323 */         this.m_menuItem[l_i] = p_itemNames[(l_i - l_sepNum)];
/*     */       }
/*     */     }
/*     */ 
/* 327 */     int l_x = this.m_parent.m_startPixel[this.m_menuIndex];
/* 328 */     int l_y = this.m_parent.m_bounds.y + this.m_parent.m_bounds.height;
/* 329 */     if (l_x + l_width > this.m_parent.m_bounds.width - 2) {
/* 330 */       l_x = this.m_parent.m_bounds.width - 2 - l_width;
/*     */     }
/* 332 */     l_x += this.m_parent.m_bounds.x;
/* 333 */     this.m_firstShowingIndex = 0;
/* 334 */     this.m_selectedIndex = p_select_default;
/* 335 */     this.m_focusedIndex = -3;
/* 336 */     this.m_totalItemNum = (p_num + this.m_sepNum);
/* 337 */     this.m_multiMode = p_multiMode;
/*     */ 
/* 339 */     this.m_itemSelected = new boolean[this.m_totalItemNum];
/* 340 */     for (int l_i = 0; l_i < this.m_totalItemNum; l_i++)
/* 341 */       this.m_itemSelected[l_i] = false;
/* 342 */     this.m_itemSelected[0] = true;
/* 343 */     for (int l_i = 0; l_i < this.m_sepNum; l_i++) {
/* 344 */       this.m_itemSelected[(this.m_sepLocation[l_i] + 1)] = true;
/*     */     }
/* 346 */     if (this.m_totalItemNum <= 12) {
/* 347 */       this.m_showingItemNum = this.m_totalItemNum;
/* 348 */       this.m_useExtent = false;
/*     */     } else {
/* 350 */       this.m_showingItemNum = 12;
/* 351 */       this.m_useExtent = true;
/*     */     }
/* 353 */     int l_number = 0;
/* 354 */     for (int l_i = 0; l_i < this.m_sepNum; l_i++)
/* 355 */       if ((l_i >= this.m_firstShowingIndex) && (l_i <= this.m_firstShowingIndex + this.m_showingItemNum))
/* 356 */         l_number++;
/*     */     int l_height;
/* 357 */     if (this.m_useExtent) {
/* 358 */       l_height = this.m_showingItemNum * 20 + 20 + 2;
/*     */     }
/*     */     else {
/* 361 */       l_height = this.m_showingItemNum * 20 + 2;
/*     */     }
/*     */ 
/* 364 */     this.m_bounds = new Rectangle(l_x, l_y, l_width, l_height);
/* 365 */     setBounds(this.m_bounds);
/* 366 */     hide();
/* 367 */     if (this.m_useExtent) {
/* 368 */       System.gc();
/* 369 */       this.m_thread = new Thread(this);
/* 370 */       this.m_menuState = 0;
/* 371 */       this.m_thread.start();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void f_unselectItem(int p_index)
/*     */   {
/* 415 */     if ((p_index < 0) || (p_index >= this.m_totalItemNum))
/* 416 */       return;
/* 417 */     if ((!this.m_multiMode) || (!this.m_itemSelected[p_index]))
/* 418 */       return;
/* 419 */     this.m_itemSelected[p_index] = false;
/*     */   }
/*     */ 
/*     */   public boolean handleEvent(Event p_e)
/*     */   {
/* 808 */     if (p_e.id == 505) {
/* 809 */       Graphics l_g = getGraphics();
/* 810 */       if (this.m_focusedIndex >= 0) {
/* 811 */         f_paintItemRect(this.s_background_color, l_g, this.m_focusedIndex - this.m_firstShowingIndex);
/* 812 */         f_drawItem(this.s_item_color, l_g, this.m_focusedIndex);
/*     */       }
/*     */ 
/* 815 */       if (this.m_focusedIndex == -1) {
/* 816 */         f_paintMoveRect(this.s_background_color, l_g, -1, false);
/*     */       }
/* 818 */       if (this.m_focusedIndex == -2) {
/* 819 */         f_paintMoveRect(this.s_background_color, l_g, -2, false);
/*     */       }
/* 821 */       if (this.m_useExtent) {
/* 822 */         this.m_menuState = 0;
/* 823 */         this.m_thread.suspend();
/* 824 */         System.gc();
/*     */       }
/* 826 */       this.m_focusedIndex = -3;
/* 827 */       return true;
/*     */     }
/* 829 */     if (p_e.id == 503) {
/* 830 */       Graphics l_g = getGraphics();
/* 831 */       int l_index = f_locate(p_e.y);
/* 832 */       if ((this.m_focusedIndex >= 0) && (l_index != this.m_focusedIndex)) {
/* 833 */         f_paintItemRect(this.s_background_color, l_g, this.m_focusedIndex - this.m_firstShowingIndex);
/* 834 */         f_drawItem(this.s_item_color, l_g, this.m_focusedIndex);
/*     */       }
/* 836 */       if (l_index == -1) {
/* 837 */         if (l_index == this.m_focusedIndex)
/* 838 */           return false;
/* 839 */         f_paintMoveRect(this.s_background_color, l_g, -1, true);
/* 840 */         this.m_focusedIndex = -1;
/* 841 */         this.m_menuState = 2;
/* 842 */         System.gc();
/* 843 */         this.m_thread.resume();
/*     */ 
/* 845 */         return false;
/*     */       }
/* 847 */       if (l_index == -2) {
/* 848 */         if (l_index == this.m_focusedIndex)
/* 849 */           return false;
/* 850 */         f_paintMoveRect(this.s_background_color, l_g, -2, true);
/* 851 */         this.m_focusedIndex = -2;
/* 852 */         this.m_menuState = 1;
/* 853 */         System.gc();
/* 854 */         this.m_thread.resume();
/* 855 */         System.gc();
/* 856 */         return false;
/*     */       }
/* 858 */       if (this.m_useExtent) {
/* 859 */         this.m_thread.suspend();
/* 860 */         System.gc();
/* 861 */         this.m_menuState = 0;
/* 862 */         f_paintMoveRect(this.s_background_color, l_g, -1, false);
/* 863 */         f_paintMoveRect(this.s_background_color, l_g, -2, false);
/*     */       }
/* 865 */       if (l_index == this.m_focusedIndex)
/* 866 */         return false;
/* 867 */       f_paintItemRect(this.s_selected_bg_color, l_g, l_index - this.m_firstShowingIndex);
/* 868 */       f_drawItem(this.s_item_selected_color, l_g, l_index);
/* 869 */       this.m_focusedIndex = l_index;
/* 870 */       return true;
/*     */     }
/* 872 */     if (p_e.id == 502) {
/* 873 */       int l_index = f_locate(p_e.y);
/* 874 */       if ((l_index == -1) || (l_index == -2))
/*     */       {
/* 876 */         return true;
/*     */       }
/* 878 */       if ((l_index >= 0) && (l_index < this.m_totalItemNum))
/*     */       {
/* 881 */         hide();
/* 882 */         boolean l_selected = true;
/* 883 */         if (!this.m_multiMode) {
/* 884 */           this.m_selectedIndex = l_index;
/*     */         }
/* 886 */         else if (this.m_itemSelected[l_index]) {
/* 887 */           this.m_itemSelected[l_index] = false;
/* 888 */           l_selected = false;
/*     */         } else {
/* 890 */           this.m_itemSelected[l_index] = true;
/*     */         }
/*     */ 
/* 894 */         this.m_parent.f_menuItemClicked(this.m_menuIndex, l_index, l_selected);
/*     */ 
/* 897 */         return true;
/*     */       }
/*     */     }
/*     */ 
/* 901 */     return super.handleEvent(p_e);
/*     */   }
/*     */ 
/*     */   public void paint(Graphics p_g)
/*     */   {
/* 736 */     int l_start = 0;
/* 737 */     if (this.m_useExtent) {
/* 738 */       l_start = 10;
/* 739 */       f_paintMoveRect(this.s_background_color, p_g, -1, false);
/* 740 */       f_paintMoveRect(this.s_background_color, p_g, -2, false);
/*     */     }
/* 742 */     f_drawMenuBody(p_g);
/* 743 */     p_g.setColor(this.s_menu_frame_color);
/* 744 */     p_g.draw3DRect(0, 0, this.m_bounds.width, this.m_bounds.height - 1, true);
/*     */   }
/*     */ 
/*     */   public void run()
/*     */   {
/*     */     try
/*     */     {
/*     */       while (true)
/* 425 */         switch (this.m_menuState) {
/*     */         case 0:
/* 427 */           Thread.currentThread().suspend();
/* 428 */           System.gc();
/* 429 */           break;
/*     */         case 1:
/* 431 */           if (this.m_firstShowingIndex < this.m_totalItemNum - this.m_showingItemNum) {
/* 432 */             this.m_firstShowingIndex += 1;
/* 433 */             Graphics l_g = getGraphics();
/* 434 */             f_drawMenuBody(l_g);
/*     */             try {
/* 436 */               Thread.currentThread(); Thread.sleep(150L);
/* 437 */               System.gc();
/*     */             } catch (Exception l_e) {
/* 439 */               l_e.printStackTrace();
/*     */             }
/*     */           } else {
/* 442 */             this.m_menuState = 0;
/* 443 */           }break;
/*     */         case 2:
/* 445 */           if (this.m_firstShowingIndex > 0) {
/* 446 */             this.m_firstShowingIndex -= 1;
/* 447 */             Graphics l_g = getGraphics();
/* 448 */             f_drawMenuBody(l_g);
/*     */             try {
/* 450 */               Thread.currentThread(); Thread.sleep(150L);
/*     */             }
/*     */             catch (Exception l_e) {
/* 453 */               l_e.printStackTrace();
/*     */             }
/*     */           } else {
/* 456 */             this.m_menuState = 0;
/*     */           }
/*     */         }
/*     */     }
/*     */     catch (Exception l_ex)
/*     */     {
/* 462 */       l_ex.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized void update(Graphics p_g)
/*     */   {
/* 728 */     Image l_image = createImage(this.m_bounds.width, this.m_bounds.height);
/* 729 */     Graphics l_g = l_image.getGraphics();
/* 730 */     l_g.clearRect(0, 0, this.m_bounds.width, this.m_bounds.height);
/* 731 */     paint(l_g);
/* 732 */     p_g.drawImage(l_image, 0, 0, this);
/*     */   }
/*     */ }

/* Location:           F:\stock research\kline\
 * Qualified Name:     lido.common.MenuPanel
 * JD-Core Version:    0.6.0
 */