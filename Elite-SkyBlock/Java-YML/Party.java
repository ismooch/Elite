/*  1:   */ package us.talabrek.ultimateskyblock;
/*  2:   */ 
/*  3:   */ public class Party
/*  4:   */ {
/*  5:   */   private String name;
/*  6:   */   private int firstCompleted;
/*  7:   */   private int timesCompleted;
/*  8:   */   
/*  9:   */   public Party(String name, int firstCompleted, int timesCompleted)
/* 10:   */   {
/* 11:12 */     this.name = name;
/* 12:13 */     this.firstCompleted = firstCompleted;
/* 13:14 */     this.timesCompleted = timesCompleted;
/* 14:   */   }
/* 15:   */   
/* 16:   */   public String getName()
/* 17:   */   {
/* 18:18 */     return this.name;
/* 19:   */   }
/* 20:   */   
/* 21:   */   public int getFirstCompleted()
/* 22:   */   {
/* 23:22 */     return this.firstCompleted;
/* 24:   */   }
/* 25:   */   
/* 26:   */   public int getTimesCompleted()
/* 27:   */   {
/* 28:26 */     return this.timesCompleted;
/* 29:   */   }
/* 30:   */   
/* 31:   */   public void setFirstCompleted(int newCompleted)
/* 32:   */   {
/* 33:30 */     this.firstCompleted = newCompleted;
/* 34:   */   }
/* 35:   */   
/* 36:   */   public void setTimesCompleted(int newCompleted)
/* 37:   */   {
/* 38:34 */     this.timesCompleted = newCompleted;
/* 39:   */   }
/* 40:   */   
/* 41:   */   public void addTimesCompleted()
/* 42:   */   {
/* 43:38 */     this.timesCompleted += 1;
/* 44:   */   }
/* 45:   */   
/* 46:   */   public void setName(String name)
/* 47:   */   {
/* 48:42 */     this.name = name;
/* 49:   */   }
/* 50:   */ }
