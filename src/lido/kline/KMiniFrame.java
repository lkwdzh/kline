/*     */ package lido.kline;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Image;
/*     */ import java.awt.Panel;
/*     */ import java.awt.Rectangle;
/*     */ import java.util.Date;
/*     */ import lido.common.CommEnv;
/*     */ 
/*     */ public class KMiniFrame extends Panel
/*     */ {
/*     */   private Rectangle m_bounds;
/*     */   private KLineDoc m_hiskDoc;
/*     */   private KLineMainView m_parent;
/*     */ 
/*     */   public KMiniFrame(KLineMainView p_parent, Rectangle p_size, KLineDoc p_hiskDoc)
/*     */   {
/*  23 */     setBackground(KLineApplet.s_bgColor);
/*     */ 
/*  25 */     setFont(CommEnv.s_smallFont);
/*  26 */     setBounds(p_size);
/*  27 */     this.m_bounds = p_size;
/*  28 */     this.m_parent = p_parent;
/*  29 */     this.m_hiskDoc = p_hiskDoc;
/*     */   }
/*     */ 
/*     */   public void paint(Graphics p_g)
/*     */   {
/*  43 */     int l_dec = this.m_hiskDoc.f_isZhaiQuan() ? 10 : 100;
/*  44 */     int l_scale = (this.m_hiskDoc.f_isBGu()) || (this.m_hiskDoc.f_isJiJin()) || (this.m_hiskDoc.f_isQuanzheng()) ? 3 : 2;
/*  45 */     int l_y_pos = 16;
/*  46 */     int l_line_gap = 16;
/*     */ 
/*  48 */     KLineRec l_krec = this.m_hiskDoc.f_getRec(this.m_parent.m_recPointedByMouse);
/*     */     KLineRec l_kpreRec;
/*  50 */     if (this.m_parent.m_recPointedByMouse == 0)
/*  51 */       l_kpreRec = l_krec;
/*     */     else {
/*  53 */       l_kpreRec = this.m_hiskDoc.f_getRec(this.m_parent.m_recPointedByMouse - 1);
/*     */     }
/*     */ 
/*  56 */     FontMetrics l_fm = p_g.getFontMetrics();
/*  57 */     Color l_up_down_color = CommEnv.f_chooseColorByValue(l_krec.m_close, l_kpreRec.m_close);
/*     */ 
/*  59 */     p_g.setColor(CommEnv.s_textColor);
/*  60 */     String l_show = "日　　 期";
/*  61 */     p_g.drawString(l_show, (this.m_bounds.width - l_fm.stringWidth(l_show)) / 2, l_y_pos);
/*  62 */     l_y_pos += l_line_gap;
/*     */ 
/*  64 */     l_show = Integer.toString(l_krec.m_tradeDate.getYear() + 1900);
/*  65 */     if (l_krec.m_tradeDate.getMonth() + 1 < 10)
/*  66 */       l_show = l_show + "0";
/*  67 */     l_show = l_show + (l_krec.m_tradeDate.getMonth() + 1);
/*  68 */     if (l_krec.m_tradeDate.getDate() < 10)
/*  69 */       l_show = l_show + "0";
/*  70 */     l_show = l_show + l_krec.m_tradeDate.getDate();
/*  71 */     p_g.drawString(l_show, this.m_bounds.width - 1 - l_fm.stringWidth(l_show), l_y_pos);
/*  72 */     l_y_pos = (int)(l_y_pos + l_line_gap * 1.2D);
/*     */ 
/*  75 */     l_show = "涨 跌 幅";
/*  76 */     p_g.drawString(l_show, (this.m_bounds.width - l_fm.stringWidth(l_show)) / 2, l_y_pos);
/*  77 */     l_y_pos += l_line_gap;
/*     */     double l_up_down_value;
/*  79 */     if ((l_kpreRec.m_close == 0.0D) || (l_kpreRec == l_krec)) {
/*  80 */       l_up_down_value = 0.0D;
/*  81 */       l_show = CommEnv.f_changeIfNeeded(p_g, Double.toString(l_up_down_value));
/*     */     } else {
/*  83 */       l_up_down_value = (l_krec.m_close - l_kpreRec.m_close) / l_kpreRec.m_close;
/*  84 */       l_show = CommEnv.f_scaleDouble(l_up_down_value * 100.0D, 2) + "%";
/*     */     }
/*  86 */     p_g.setColor(l_up_down_color);
/*  87 */     p_g.drawString(l_show, this.m_bounds.width - 1 - l_fm.stringWidth(l_show) - 2, l_y_pos);
/*  88 */     l_y_pos = (int)(l_y_pos + l_line_gap * 1.2D);
/*     */ 
/*  92 */     p_g.setColor(CommEnv.s_textColor);
/*  93 */     l_show = "开　  盘";
/*  94 */     p_g.drawString(l_show, (this.m_bounds.width - l_fm.stringWidth(l_show)) / 2, l_y_pos);
/*  95 */     l_y_pos += l_line_gap;
/*  96 */     l_show = CommEnv.f_scaleDouble(l_krec.m_open, l_scale);
/*     */ 
/*  98 */     p_g.setColor(CommEnv.f_chooseColorByValue(l_krec.m_open, l_kpreRec.m_close));
/*  99 */     p_g.drawString(l_show, this.m_bounds.width - 1 - l_fm.stringWidth(l_show), l_y_pos);
/* 100 */     l_y_pos = (int)(l_y_pos + l_line_gap * 1.2D);
/*     */ 
/* 103 */     p_g.setColor(CommEnv.s_textColor);
/* 104 */     l_show = "收　  盘";
/* 105 */     p_g.drawString(l_show, (this.m_bounds.width - l_fm.stringWidth(l_show)) / 2, l_y_pos);
/* 106 */     l_y_pos += l_line_gap;
/* 107 */     l_show = CommEnv.f_scaleDouble(l_krec.m_close, l_scale);
/* 108 */     p_g.setColor(l_up_down_color);
/* 109 */     p_g.drawString(l_show, this.m_bounds.width - 1 - l_fm.stringWidth(l_show), l_y_pos);
/* 110 */     l_y_pos = (int)(l_y_pos + l_line_gap * 1.2D);
/*     */ 
/* 113 */     p_g.setColor(CommEnv.s_textColor);
/* 114 */     l_show = "最　  高";
/* 115 */     p_g.drawString(l_show, (this.m_bounds.width - l_fm.stringWidth(l_show)) / 2, l_y_pos);
/* 116 */     l_y_pos += l_line_gap;
/* 117 */     l_show = CommEnv.f_scaleDouble(l_krec.m_high, l_scale);
/* 118 */     p_g.setColor(CommEnv.f_chooseColorByValue(l_krec.m_high, l_kpreRec.m_close));
/* 119 */     p_g.drawString(l_show, this.m_bounds.width - 1 - l_fm.stringWidth(l_show), l_y_pos);
/* 120 */     l_y_pos = (int)(l_y_pos + l_line_gap * 1.2D);
/*     */ 
/* 123 */     p_g.setColor(CommEnv.s_textColor);
/* 124 */     l_show = "最  　低";
/* 125 */     p_g.drawString(l_show, (this.m_bounds.width - l_fm.stringWidth(l_show)) / 2, l_y_pos);
/* 126 */     l_y_pos += l_line_gap;
/* 127 */     l_show = CommEnv.f_scaleDouble(l_krec.m_low, l_scale);
/* 128 */     p_g.setColor(CommEnv.f_chooseColorByValue(l_krec.m_low, l_kpreRec.m_close));
/* 129 */     p_g.drawString(l_show, this.m_bounds.width - 1 - l_fm.stringWidth(l_show), l_y_pos);
/* 130 */     l_y_pos = (int)(l_y_pos + l_line_gap * 1.2D);
/*     */ 
/* 133 */     p_g.setColor(CommEnv.s_textColor);
/* 134 */     l_show = "成 交 量";
/* 135 */     p_g.drawString(l_show, (this.m_bounds.width - l_fm.stringWidth(l_show)) / 2, l_y_pos);
/* 136 */     l_y_pos += l_line_gap;
/* 137 */     l_show = CommEnv.f_scaleDouble(l_krec.m_volume / l_dec, 0);
/* 138 */     p_g.setColor(l_up_down_color);
/* 139 */     int l_stringWidth = l_fm.stringWidth(l_show);
/* 140 */     if (l_stringWidth < this.m_bounds.width - 2)
/* 141 */       p_g.drawString(l_show, this.m_bounds.width - 1 - l_fm.stringWidth(l_show), l_y_pos);
/*     */     else
/* 143 */       p_g.drawString(l_show, 4, l_y_pos);
/* 144 */     l_y_pos = (int)(l_y_pos + l_line_gap * 1.2D);
/*     */ 
/* 147 */     p_g.setColor(CommEnv.s_textColor);
/* 148 */     l_show = "成 交 额";
/* 149 */     p_g.drawString(l_show, (this.m_bounds.width - l_fm.stringWidth(l_show)) / 2, l_y_pos);
/* 150 */     l_y_pos += l_line_gap;
/* 151 */     if (this.m_hiskDoc.f_isZhiShu())
/* 152 */       l_show = CommEnv.f_scaleDouble(l_krec.m_money / 10000.0D, 0) + ".";
/*     */     else
/* 154 */       l_show = CommEnv.f_scaleDouble(l_krec.m_money / 10000.0D, 2);
/* 155 */     p_g.setColor(l_up_down_color);
/* 156 */     l_stringWidth = l_fm.stringWidth(l_show);
/* 157 */     if (l_stringWidth < this.m_bounds.width - 2)
/* 158 */       p_g.drawString(l_show, this.m_bounds.width - 1 - l_fm.stringWidth(l_show), l_y_pos);
/*     */     else
/* 160 */       p_g.drawString(l_show, 4, l_y_pos);
/*     */   }
/*     */ 
/*     */   public synchronized void update(Graphics p_g)
/*     */   {
/*  34 */     Image l_image = createImage(this.m_bounds.width, this.m_bounds.height);
/*  35 */     Graphics l_g = l_image.getGraphics();
/*     */ 
/*  37 */     paint(l_g);
/*  38 */     p_g.drawImage(l_image, 0, 0, this);
/*     */   }
/*     */ }

/* Location:           F:\stock research\kline\
 * Qualified Name:     lido.kline.KMiniFrame
 * JD-Core Version:    0.6.0
 */