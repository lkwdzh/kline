/*    */ package lido.common;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Font;
/*    */ import java.awt.FontMetrics;
/*    */ import java.awt.Graphics;
/*    */ import java.awt.Image;
/*    */ import java.awt.Panel;
/*    */ 
/*    */ public class PromotingPanel extends Panel
/*    */ {
/*    */   private int m_height;
/*    */   private Image m_image;
/*    */   private int m_width;
/*    */   public static final Font s_SONG_TI_PLAIN_12;
/*  8 */   static Color s_ShowingColor = new Color(221, 221, 221);
/*    */ 
/*  9 */   static { s_SONG_TI_PLAIN_12 = new Font("宋体", 0, 12);
/*    */   }
/*    */ 
/*    */   public PromotingPanel(Image p_image, int p_xstart, int p_ystart)
/*    */   {
/* 15 */     this.m_width = 202;
/* 16 */     this.m_height = 48;
/* 17 */     this.m_image = p_image;
/* 18 */     setBounds(p_xstart, p_ystart, this.m_width, this.m_height);
/* 19 */     setBackground(Color.white);
/*    */   }
/*    */ 
/*    */   public void f_setImage(Image p_image)
/*    */   {
/* 24 */     this.m_image = p_image;
/*    */   }
/*    */ 
/*    */   public void paint(Graphics p_g)
/*    */   {
/* 29 */     p_g.setColor(s_ShowingColor);
/* 30 */     p_g.fillRect(0, 0, 198, 45);
/* 31 */     if (this.m_image != null)
/* 32 */       p_g.drawImage(this.m_image, 2, 12, this);
/* 33 */     p_g.setFont(s_SONG_TI_PLAIN_12);
/* 34 */     FontMetrics l_fm = p_g.getFontMetrics();
/* 35 */     String l_string = "数 据 传 输 中 ......";
/* 36 */     int l_ascent = l_fm.getAscent();
/* 37 */     int l_descent = l_fm.getDescent();
/* 38 */     int l_location = (45 - l_ascent - l_descent) / 2;
/* 39 */     p_g.setColor(Color.red);
/* 40 */     if (this.m_image != null)
/* 41 */       p_g.drawString(l_string, 50, 45 - l_location);
/*    */     else
/* 43 */       p_g.drawString(l_string, 10, 45 - l_location);
/* 44 */     p_g.setColor(new Color(165, 165, 165));
/* 45 */     p_g.fillRect(3, 45, 199, 3);
/* 46 */     p_g.fillRect(198, 4, 4, 44);
/*    */   }
/*    */ }

/* Location:           F:\stock research\kline\
 * Qualified Name:     lido.common.PromotingPanel
 * JD-Core Version:    0.6.0
 */