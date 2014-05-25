/*    */ package lido.common;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Cursor;
/*    */ import java.awt.Event;
/*    */ import java.awt.Graphics;
/*    */ import java.awt.Image;
/*    */ import java.awt.Panel;
/*    */ import java.awt.Rectangle;
/*    */ 
/*    */ class ImageLogoPanel extends Panel
/*    */ {
/* 12 */   private Image m_image = null;
/*    */   ControlBar m_parent;
/*    */   private static final Cursor s_HAND_CURSOR;
/*    */   private static final Cursor s_NORMAL_CURSOR;
/* 10 */   static Color s_menuBarColor = new Color(224, 224, 192);
/*    */ 
/*    */   static {
/* 13 */     s_HAND_CURSOR = new Cursor(12);
/*    */ 
/* 15 */     s_NORMAL_CURSOR = new Cursor(0);
/*    */   }
/*    */ 
/*    */   public ImageLogoPanel(Image p_image, ControlBar p_bar) {
/* 19 */     this.m_parent = p_bar;
/* 20 */     this.m_image = p_image;
/* 21 */     int l_start = this.m_parent.m_bounds.width - 45;
/* 22 */     this.m_parent.add(this);
/* 23 */     setBounds(l_start, 0, 45, 20);
/* 24 */     setBackground(s_menuBarColor);
/*    */   }
/*    */ 
/*    */   public boolean handleEvent(Event p_e)
/*    */   {
/* 34 */     if ((p_e.id == 504) || (p_e.id == 503) || (p_e.id == 506))
/*    */     {
/* 37 */       setCursor(s_HAND_CURSOR);
/* 38 */     }if (p_e.id == 505)
/* 39 */       setCursor(s_NORMAL_CURSOR);
/* 40 */     if (p_e.id == 502)
/* 41 */       this.m_parent.f_buttonClicked(100);
/* 42 */     return true;
/*    */   }
/*    */ 
/*    */   public void paint(Graphics p_g)
/*    */   {
/* 29 */     p_g.drawImage(this.m_image, 0, 0, this);
/*    */   }
/*    */ }

/* Location:           F:\stock research\kline\
 * Qualified Name:     lido.common.ImageLogoPanel
 * JD-Core Version:    0.6.0
 */