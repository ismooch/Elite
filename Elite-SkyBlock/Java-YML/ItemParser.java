/*  1:   */ package us.talabrek.ultimateskyblock;
/*  2:   */ 
/*  3:   */ import java.util.Map;
/*  4:   */ import java.util.logging.Logger;
/*  5:   */ import org.bukkit.enchantments.Enchantment;
/*  6:   */ import org.bukkit.inventory.ItemStack;
/*  7:   */ import org.bukkit.material.MaterialData;
/*  8:   */ 
/*  9:   */ public class ItemParser
/* 10:   */ {
/* 11:   */   public static String parseItemStackToString(ItemStack item)
/* 12:   */   {
/* 13:10 */     if (item == null) {
/* 14:11 */       return "";
/* 15:   */     }
/* 16:15 */     String s = "";
/* 17:16 */     s = s + "id:" + item.getTypeId() + ";";
/* 18:17 */     s = s + "amount:" + item.getAmount() + ";";
/* 19:18 */     s = s + "durab:" + item.getDurability() + ";";
/* 20:19 */     s = s + "data:" + item.getData().getData() + ";";
/* 21:21 */     if (item.getEnchantments().size() > 0)
/* 22:   */     {
/* 23:22 */       s = s + "ench:";
/* 24:23 */       for (Enchantment e : item.getEnchantments().keySet())
/* 25:   */       {
/* 26:24 */         s = s + "eid#" + e.getId() + " ";
/* 27:25 */         s = s + "elevel#" + item.getEnchantments().get(e) + " ";
/* 28:   */       }
/* 29:   */     }
/* 30:28 */     return s.trim();
/* 31:   */   }
/* 32:   */   
/* 33:   */   public static ItemStack getItemStackfromString(String s)
/* 34:   */   {
/* 35:33 */     if (s.equalsIgnoreCase("")) {
/* 36:34 */       return null;
/* 37:   */     }
/* 38:36 */     ItemStack x = new ItemStack(1);
/* 39:38 */     for (String thing : s.split(";"))
/* 40:   */     {
/* 41:39 */       String[] sp = thing.split(":");
/* 42:40 */       if (sp.length != 2) {
/* 43:41 */         uSkyBlock.getInstance().log.warning("error, wrong type size");
/* 44:   */       }
/* 45:42 */       String name = sp[0];
/* 46:44 */       if (name.equals("id"))
/* 47:   */       {
/* 48:45 */         x.setTypeId(Integer.parseInt(sp[1]));
/* 49:   */       }
/* 50:46 */       else if (name.equals("amount"))
/* 51:   */       {
/* 52:47 */         x.setAmount(Integer.parseInt(sp[1]));
/* 53:   */       }
/* 54:48 */       else if (name.equals("durab"))
/* 55:   */       {
/* 56:49 */         x.setDurability((short)Integer.parseInt(sp[1]));
/* 57:   */       }
/* 58:50 */       else if (name.equals("data"))
/* 59:   */       {
/* 60:51 */         x.getData().setData((byte)Integer.parseInt(sp[1]));
/* 61:   */       }
/* 62:52 */       else if (name.equals("ench"))
/* 63:   */       {
/* 64:53 */         int enchId = 0;
/* 65:54 */         int level = 0;
/* 66:55 */         for (String enchantment : sp[1].split(" "))
/* 67:   */         {
/* 68:56 */           String[] prop = enchantment.split("#");
/* 69:57 */           if (prop.length != 2) {
/* 70:58 */             uSkyBlock.getInstance().log.warning("error, wrong enchantmenttype length");
/* 71:   */           }
/* 72:59 */           if (prop[0].equals("eid"))
/* 73:   */           {
/* 74:60 */             enchId = Integer.parseInt(prop[1]);
/* 75:   */           }
/* 76:61 */           else if (prop[0].equals("elevel"))
/* 77:   */           {
/* 78:62 */             level = Integer.parseInt(prop[1]);
/* 79:63 */             x.addUnsafeEnchantment(Enchantment.getById(enchId), level);
/* 80:   */           }
/* 81:   */         }
/* 82:   */       }
/* 83:   */       else
/* 84:   */       {
/* 85:68 */         uSkyBlock.getInstance().log.warning("error, unknown itemvalue");
/* 86:   */       }
/* 87:   */     }
/* 88:71 */     return x;
/* 89:   */   }
/* 90:   */ }
