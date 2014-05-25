/*     */ package lido.common;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Image;
/*     */ import java.awt.Panel;
/*     */ import java.awt.Rectangle;
/*     */ 
/*     */ public class ControlBar extends Panel
/*     */ {
/*     */   Color m_background_color;
/*  41 */   public BlinkMenu[] m_blinkMenu = new BlinkMenu[20];
/*     */   Rectangle m_bounds;
/*     */   public ImageLogoPanel[] m_imageLogoPanel;
/*  29 */   int m_menuCount = 0;
/*     */ 
/*  40 */   MenuPanel[] m_menuPanel = new MenuPanel[20];
/*     */ 
/*  36 */   public int[] m_menuPanelIndex = new int[20];
/*  37 */   int m_menuPanelNum = 0;
/*     */   Menuable m_parent;
/*  52 */   int m_progress = -1;
/*     */ 
/*  44 */   int[] m_startPixel = new int[20];
/*     */ 
/*  53 */   private int m_step_size = 12;
/*     */   private static final int s_max_available = 20;
/*  45 */   public static double s_pixel_per_uni = 12.0D;
/*     */   public static double s_start_pos;
/*  46 */   public static double s_triangle_size = 8.0D;
/*     */ 
/*  47 */   static { s_start_pos = 15.0D;
/*     */   }
/*     */ 
/*     */   public ControlBar(Menuable p_applet, Rectangle p_mounds, Color p_back_color)
/*     */   {
/*  66 */     this.m_parent = p_applet;
/*  67 */     setLayout(null);
/*  68 */     this.m_background_color = p_back_color;
/*  69 */     this.m_bounds = p_mounds;
/*  70 */     setBackground(this.m_background_color);
/*  71 */     setBounds(this.m_bounds);
/*  72 */     this.m_startPixel[0] = (int)s_start_pos;
/*     */   }
/*     */ 
/*     */   public synchronized boolean f_addBlinkMenu(String p_name, boolean p_is_menu)
/*     */   {
/* 157 */     if (this.m_menuCount == 20) return false;
/* 158 */     this.m_menuCount += 1;
/*     */ 
/* 160 */     this.m_startPixel[this.m_menuCount] = (this.m_startPixel[(this.m_menuCount - 1)] + (int)(s_pixel_per_uni * p_name.length()) + this.m_step_size);
/*     */ 
/* 162 */     if (p_is_menu) this.m_startPixel[this.m_menuCount] += (int)s_triangle_size;
/* 163 */     this.m_blinkMenu[(this.m_menuCount - 1)] = new BlinkMenu(p_name, this, -1, this.m_menuCount - 1, p_is_menu);
/* 164 */     return true;
/*     */   }
/*     */ 
/*     */   public synchronized boolean f_addImageMenu(Image[] p_image)
/*     */   {
/* 170 */     if (this.m_menuCount == 20) return false;
/* 171 */     this.m_menuCount += 1;
/* 172 */     this.m_startPixel[this.m_menuCount] = (this.m_startPixel[(this.m_menuCount - 1)] + 15 + this.m_step_size);
/*     */ 
/* 174 */     this.m_blinkMenu[(this.m_menuCount - 1)] = new BlinkMenu(p_image, this, this.m_menuCount - 1);
/* 175 */     return true;
/*     */   }
/*     */ 
/*     */   public synchronized boolean f_addLogoPanel(Image[] p_image)
/*     */   {
/* 180 */     this.m_imageLogoPanel = new ImageLogoPanel[2];
/* 181 */     this.m_imageLogoPanel[0] = new ImageLogoPanel(p_image[0], this);
/* 182 */     this.m_imageLogoPanel[1] = new ImageLogoPanel(p_image[1], this);
/* 183 */     return true;
/*     */   }
/*     */ 
/*     */   public synchronized boolean f_addMenuPanel(int p_menu_index, String[] p_itemNames, int p_num, int p_select_default, boolean p_multiMode)
/*     */   {
/* 207 */     if ((p_menu_index < 0) || (p_menu_index >= this.m_menuCount))
/* 208 */       return false;
/* 209 */     if (!this.m_blinkMenu[p_menu_index].m_isMenu)
/* 210 */       return false;
/* 211 */     this.m_menuPanel[this.m_menuPanelNum] = new MenuPanel(this, p_menu_index);
/* 212 */     this.m_menuPanel[this.m_menuPanelNum].f_setMenu(p_itemNames, p_num, p_select_default, p_multiMode);
/* 213 */     this.m_parent.f_addMenuPanel(this.m_menuPanel[this.m_menuPanelNum]);
/* 214 */     this.m_menuPanelIndex[this.m_menuPanelNum] = p_menu_index;
/* 215 */     this.m_menuPanelNum += 1;
/* 216 */     return true;
/*     */   }
/*     */ 
/*     */   public synchronized boolean f_addMenuPanel(int p_menu_index, String[] p_itemNames, int p_num, boolean p_multiMode)
/*     */   {
/* 194 */     if ((p_menu_index < 0) || (p_menu_index >= this.m_menuCount))
/* 195 */       return false;
/* 196 */     if (!this.m_blinkMenu[p_menu_index].m_isMenu)
/* 197 */       return false;
/* 198 */     this.m_menuPanel[this.m_menuPanelNum] = new MenuPanel(this, p_menu_index);
/* 199 */     this.m_menuPanel[this.m_menuPanelNum].f_setMenu(p_itemNames, p_num, p_multiMode);
/* 200 */     this.m_parent.f_addMenuPanel(this.m_menuPanel[this.m_menuPanelNum]);
/* 201 */     this.m_menuPanelIndex[this.m_menuPanelNum] = p_menu_index;
/* 202 */     this.m_menuPanelNum += 1;
/* 203 */     return true;
/*     */   }
/*     */ 
/*     */   public synchronized boolean f_addMultiMenuPanel(int p_menu_index, String[] p_itemNames, int p_num, int p_select_default, int p_sepNum, int[] p_sepLocation, boolean p_multiMode)
/*     */   {
/* 228 */     if ((p_menu_index < 0) || (p_menu_index >= this.m_menuCount))
/* 229 */       return false;
/* 230 */     if (!this.m_blinkMenu[p_menu_index].m_isMenu)
/* 231 */       return false;
/* 232 */     this.m_menuPanel[this.m_menuPanelNum] = new MenuPanel(this, p_menu_index);
/* 233 */     this.m_menuPanel[this.m_menuPanelNum].f_setMultiMenu(p_itemNames, p_num, p_select_default, p_sepNum, p_sepLocation, p_multiMode);
/*     */ 
/* 235 */     this.m_parent.f_addMenuPanel(this.m_menuPanel[this.m_menuPanelNum]);
/* 236 */     this.m_menuPanelIndex[this.m_menuPanelNum] = p_menu_index;
/* 237 */     this.m_menuPanelNum += 1;
/* 238 */     return true;
/*     */   }
/*     */ 
/*     */   public void f_blinkMenuClicked(int p_menu_index)
/*     */   {
/* 112 */     if ((p_menu_index < 0) || (p_menu_index >= this.m_menuCount))
/* 113 */       return;
/* 114 */     if (this.m_blinkMenu[p_menu_index].m_isMenu) {
/* 115 */       MenuPanel l_popup = f_getMenuPanel(p_menu_index);
/* 116 */       if (l_popup == null) {
/* 117 */         return;
/*     */       }
/* 119 */       if (l_popup.isShowing())
/* 120 */         return;
/* 121 */       f_hideAllMenuPanels();
/*     */ 
/* 123 */       l_popup.setVisible(true);
/* 124 */       l_popup.repaint();
/*     */     }
/*     */     else
/*     */     {
/* 128 */       f_hideAllMenuPanels();
/* 129 */       this.m_parent.f_buttonClicked(p_menu_index);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void f_buttonClicked(int p_num)
/*     */   {
/*  93 */     this.m_parent.f_buttonClicked(p_num);
/*     */   }
/*     */ 
/*     */   public void f_destroy()
/*     */   {
/* 242 */     for (int l_i = 0; l_i < this.m_menuPanelNum; l_i++)
/* 243 */       if (this.m_menuPanel[l_i] != null)
/* 244 */         this.m_menuPanel[l_i].f_destroy();
/*     */   }
/*     */ 
/*     */   private MenuPanel f_getMenuPanel(int p_menu_index)
/*     */   {
/*  97 */     if ((p_menu_index < 0) || (p_menu_index >= this.m_menuCount)) {
/*  98 */       return null;
/*     */     }
/* 100 */     for (int l_i = 0; l_i < this.m_menuPanelNum; l_i++)
/* 101 */       if (this.m_menuPanelIndex[l_i] == p_menu_index)
/* 102 */         return this.m_menuPanel[l_i];
/* 103 */     return null;
/*     */   }
/*     */ 
/*     */   public void f_hideAllMenuPanels()
/*     */   {
/*  80 */     for (int l_i = 0; l_i < this.m_menuPanelNum; l_i++)
/*  81 */       this.m_menuPanel[l_i].hide();
/*     */   }
/*     */ 
/*     */   public void f_imageNameParse(int p_menuIndex)
/*     */   {
/* 108 */     this.m_parent.f_imageNameParse(p_menuIndex);
/*     */   }
/*     */ 
/*     */   public void f_menuItemClicked(int p_menu_index, int p_item_index, boolean p_select)
/*     */   {
/* 133 */     f_hideAllMenuPanels();
/* 134 */     this.m_parent.f_menuItemClicked(p_menu_index, p_item_index, p_select);
/*     */   }
/*     */ 
/*     */   public void f_selectMenuItem(int p_menu_index, int p_item_index) {
/* 138 */     if ((p_menu_index < 0) || (p_menu_index >= this.m_menuCount))
/* 139 */       return;
/* 140 */     MenuPanel l_menu = f_getMenuPanel(p_menu_index);
/* 141 */     if (l_menu == null)
/* 142 */       return;
/* 143 */     l_menu.f_selectItem(p_item_index);
/*     */   }
/*     */ 
/*     */   public void f_setBlink(int p_index, boolean p_blink)
/*     */   {
/*  76 */     this.m_blinkMenu[p_index].f_setBlink(p_blink);
/*     */   }
/*     */ 
/*     */   public void f_setLogoShowing(int p_logoNum, boolean p_showing)
/*     */   {
/* 188 */     if (this.m_imageLogoPanel != null)
/* 189 */       this.m_imageLogoPanel[p_logoNum].show(p_showing);
/*     */   }
/*     */ 
/*     */   public void f_setProgress(int l_progress)
/*     */   {
/*  61 */     this.m_progress = l_progress;
/*  62 */     repaint(this.m_bounds.width - 100, 0, 100, this.m_bounds.height);
/*     */   }
/*     */ 
/*     */   public void f_setStepSize(int p_step_size)
/*     */   {
/*  57 */     this.m_step_size = p_step_size;
/*     */   }
/*     */ 
/*     */   public boolean f_someMenuShows()
/*     */   {
/*  85 */     for (int l_i = 0; l_i < this.m_menuPanelNum; l_i++)
/*  86 */       if (this.m_menuPanel[l_i].isShowing())
/*  87 */         return true;
/*  88 */     return false;
/*     */   }
/*     */ 
/*     */   public void f_unselectMenuItem(int p_menu_index, int p_item_index)
/*     */   {
/* 148 */     if ((p_menu_index < 0) || (p_menu_index >= this.m_menuCount))
/* 149 */       return;
/* 150 */     MenuPanel l_menu = f_getMenuPanel(p_menu_index);
/* 151 */     if (l_menu == null)
/* 152 */       return;
/* 153 */     l_menu.f_unselectItem(p_item_index);
/*     */   }
/*     */ }

/* Location:           F:\stock research\kline\
 * Qualified Name:     lido.common.ControlBar
 * JD-Core Version:    0.6.0
 */