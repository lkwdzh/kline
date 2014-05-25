/*     */ package lido.common;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Event;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Image;
/*     */ import java.awt.Panel;
/*     */ import java.awt.Rectangle;
/*     */ 
/*     */ class BlinkMenu extends Panel
/*     */ {
/*  23 */   public boolean m_blinkable = true;
/*     */   public String m_caption;
/*     */   boolean m_isImage;
/*  31 */   private boolean m_isLogo = false;
/*     */   boolean m_isMenu;
/*  30 */   private Image m_logoImage = null;
/*     */   public int m_menuID;
/*     */   Image[] m_menuImage;
/*     */   public int m_menuIndex;
/*     */   int m_menuWidth;
/*     */   ControlBar m_parent;
/*  29 */   private int m_style = 0;
/*     */   static Color s_blink_color;
/*     */   static Color s_disabled_color;
/*     */   static Color s_menuBarColor;
/*  10 */   static int s_menuHeight = 15;
/*     */ 
/*  12 */   static Color s_normal_color = Color.white;
/*     */ 
/*  14 */   static { s_disabled_color = new Color(200, 200, 200);
/*  15 */     s_blink_color = new Color(24, 119, 248);
/*     */ 
/*  17 */     s_menuBarColor = new Color(0, 0, 0);
/*     */   }
/*     */ 
/*     */   public BlinkMenu(Image p_image, ControlBar p_bar)
/*     */   {
/*  70 */     this.m_parent = p_bar;
/*  71 */     this.m_isMenu = false;
/*  72 */     this.m_logoImage = p_image;
/*  73 */     this.m_isImage = false;
/*  74 */     this.m_menuWidth = 45;
/*  75 */     this.m_isLogo = true;
/*  76 */     int l_start = this.m_parent.m_bounds.width - 45;
/*  77 */     this.m_parent.add(this);
/*  78 */     setBounds(l_start, 1, this.m_menuWidth, 18);
/*     */ 
/*  80 */     setBackground(s_menuBarColor);
/*     */   }
/*     */ 
/*     */   public BlinkMenu(Image[] p_image, ControlBar p_bar, int p_index)
/*     */   {
/*  55 */     this.m_parent = p_bar;
/*  56 */     this.m_isMenu = false;
/*  57 */     this.m_menuIndex = p_index;
/*  58 */     this.m_menuImage = new Image[3];
/*  59 */     this.m_menuImage = p_image;
/*  60 */     this.m_isImage = true;
/*  61 */     this.m_menuWidth = 15;
/*  62 */     this.m_parent.add(this);
/*  63 */     setBounds(this.m_parent.m_startPixel[p_index], 2, this.m_menuWidth, s_menuHeight);
/*     */ 
/*  65 */     setBackground(s_menuBarColor);
/*     */   }
/*     */ 
/*     */   public BlinkMenu(String p_name, ControlBar p_bar, int p_id, int p_index, boolean p_need)
/*     */   {
/*  39 */     this.m_caption = p_name;
/*  40 */     this.m_parent = p_bar;
/*  41 */     this.m_isMenu = p_need;
/*  42 */     this.m_menuID = p_id;
/*  43 */     this.m_menuIndex = p_index;
/*  44 */     this.m_isImage = false;
/*  45 */     this.m_parent.add(this);
/*  46 */     this.m_menuWidth = (int)(ControlBar.s_pixel_per_uni * this.m_caption.length());
/*  47 */     if (this.m_isMenu) this.m_menuWidth = (int)(this.m_menuWidth + ControlBar.s_triangle_size);
/*  48 */     setBounds(this.m_parent.m_startPixel[p_index], 2, this.m_menuWidth, s_menuHeight);
/*     */ 
/*  50 */     setBackground(s_menuBarColor);
/*     */   }
/*     */ 
/*     */   private void f_drawMenuName(Color p_color, boolean p_move)
/*     */   {
/* 153 */     Graphics l_g = getGraphics();
/* 154 */     FontMetrics l_fm = l_g.getFontMetrics();
/* 155 */     l_g.setColor(p_color);
/* 156 */     if (!p_move)
/* 157 */       l_g.drawString(this.m_caption, 0, s_menuHeight - 2);
/*     */     else
/* 159 */       l_g.drawString(this.m_caption, 1, s_menuHeight - 2 + 1);
/* 160 */     if (this.m_isMenu) {
/* 161 */       int[] l_x = new int[3];
/* 162 */       int[] l_y = new int[3];
/* 163 */       int l_x_start = this.m_menuWidth - 5;
/*     */       int tmp89_88 = (s_menuHeight / 2); l_y[2] = tmp89_88; l_y[0] = tmp89_88;
/* 166 */       l_y[1] = (s_menuHeight / 2 + 3);
/*     */ 
/* 168 */       if (p_move)
/* 169 */         for (int i = 0; i < 3; i++)
/* 170 */           l_y[i] += 1;
/* 171 */       l_x[0] = l_x_start;
/* 172 */       l_x[2] = (l_x_start + 5);
/* 173 */       l_x[1] = (l_x_start + 2);
/* 174 */       l_g.fillPolygon(l_x, l_y, 3);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void f_drawMenuName(int p_style)
/*     */   {
/* 143 */     this.m_style = p_style;
/* 144 */     Graphics l_g = getGraphics();
/* 145 */     l_g.setColor(s_menuBarColor);
/* 146 */     Image l_image = this.m_menuImage[p_style];
/* 147 */     l_g.fillRect(0, 1, this.m_menuWidth, 18);
/* 148 */     l_g.drawImage(l_image, 0, 1, this);
/*     */   }
/*     */ 
/*     */   private void f_drawMenuName()
/*     */   {
/* 133 */     Graphics l_g = getGraphics();
/*     */ 
/* 135 */     l_g.setColor(s_menuBarColor);
/* 136 */     l_g.fillRect(0, 0, 45, 20);
/* 137 */     boolean result = l_g.drawImage(this.m_logoImage, 0, 1, this);
/*     */   }
/*     */ 
/*     */   public void f_setBlink(boolean p_blink)
/*     */   {
/*  85 */     this.m_blinkable = p_blink;
/*  86 */     if (p_blink)
/*  87 */       this.m_style = 0;
/*     */     else
/*  89 */       this.m_style = 2;
/*  90 */     repaint();
/*     */   }
/*     */ 
/*     */   public boolean handleEvent(Event p_e)
/*     */   {
/* 179 */     if (!this.m_blinkable)
/* 180 */       return false;
/* 181 */     if (p_e.id == 501) {
/* 182 */       if (this.m_isMenu) {
/* 183 */         f_drawMenuName(Color.white, false);
/*     */       }
/* 186 */       else if (this.m_isImage)
/* 187 */         f_drawMenuName(2);
/*     */       else {
/* 189 */         f_drawMenuName(Color.white, false);
/*     */       }
/* 191 */       return true;
/*     */     }
/* 193 */     if (p_e.id == 504) {
/* 194 */       if (this.m_isMenu) {
/* 195 */         f_drawMenuName(s_blink_color, false);
/*     */       }
/* 198 */       else if (this.m_isImage)
/*     */       {
/* 200 */         f_drawMenuName(1);
/* 201 */         this.m_parent.f_imageNameParse(this.m_menuIndex);
/*     */       }
/*     */       else {
/* 204 */         f_drawMenuName(s_blink_color, false);
/*     */       }
/* 206 */       return true;
/*     */     }
/* 208 */     if (p_e.id == 505) {
/* 209 */       if (this.m_isMenu) {
/* 210 */         f_drawMenuName(s_normal_color, false);
/*     */       }
/* 213 */       else if (this.m_isImage)
/* 214 */         f_drawMenuName(0);
/*     */       else {
/* 216 */         f_drawMenuName(s_normal_color, false);
/*     */       }
/* 218 */       return true;
/*     */     }
/* 220 */     if (p_e.id == 502) {
/* 221 */       if ((p_e.x < 0) || (p_e.x > getBounds().width) || (p_e.y < 0) || (p_e.y > getBounds().height))
/*     */       {
/* 223 */         if (this.m_isMenu) {
/* 224 */           f_drawMenuName(s_normal_color, false);
/*     */         }
/* 227 */         else if (this.m_isImage)
/* 228 */           f_drawMenuName(0);
/*     */         else {
/* 230 */           f_drawMenuName(s_normal_color, false);
/*     */         }
/* 232 */         return true;
/*     */       }
/* 234 */       if (this.m_isMenu) {
/* 235 */         f_drawMenuName(s_blink_color, false);
/*     */       }
/* 238 */       else if (this.m_isImage)
/* 239 */         f_drawMenuName(1);
/*     */       else {
/* 241 */         f_drawMenuName(s_blink_color, false);
/*     */       }
/*     */ 
/* 244 */       if (p_e.metaDown())
/* 245 */         return false;
/* 246 */       this.m_parent.f_blinkMenuClicked(this.m_menuIndex);
/* 247 */       return true;
/*     */     }
/* 249 */     return super.handleEvent(p_e);
/*     */   }
/*     */ 
/*     */   public void paint(Graphics p_g)
/*     */   {
/*  94 */     if (this.m_isLogo) {
/*  95 */       f_drawMenuName();
/*     */     }
/*  98 */     else if (this.m_blinkable) {
/*  99 */       if (this.m_isMenu) {
/* 100 */         f_drawMenuName(s_normal_color, false);
/*     */       }
/* 103 */       else if (this.m_isImage)
/*     */       {
/* 105 */         f_drawMenuName(this.m_style);
/*     */       }
/*     */       else {
/* 108 */         f_drawMenuName(s_normal_color, false);
/*     */       }
/*     */ 
/*     */     }
/* 112 */     else if (this.m_isMenu)
/*     */     {
/* 114 */       f_drawMenuName(Color.white, true);
/* 115 */       f_drawMenuName(s_disabled_color, false);
/*     */     }
/* 119 */     else if (this.m_isImage) {
/* 120 */       f_drawMenuName(this.m_style);
/*     */     }
/*     */     else {
/* 123 */       f_drawMenuName(Color.white, true);
/* 124 */       f_drawMenuName(s_disabled_color, false);
/*     */     }
/*     */   }
/*     */ }

/* Location:           F:\stock research\kline\
 * Qualified Name:     lido.common.BlinkMenu
 * JD-Core Version:    0.6.0
 */