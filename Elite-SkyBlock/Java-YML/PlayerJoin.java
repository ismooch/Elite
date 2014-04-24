/*   1:    */ package us.talabrek.ultimateskyblock;
/*   2:    */ 
/*   3:    */ import java.io.File;
/*   4:    */ import java.io.PrintStream;
/*   5:    */ import java.util.HashMap;
/*   6:    */ import java.util.Iterator;
/*   7:    */ import java.util.List;
/*   8:    */ import org.bukkit.Bukkit;
/*   9:    */ import org.bukkit.ChatColor;
/*  10:    */ import org.bukkit.Material;
/*  11:    */ import org.bukkit.Server;
/*  12:    */ import org.bukkit.World;
/*  13:    */ import org.bukkit.block.Block;
/*  14:    */ import org.bukkit.configuration.file.FileConfiguration;
/*  15:    */ import org.bukkit.entity.Arrow;
/*  16:    */ import org.bukkit.entity.Creeper;
/*  17:    */ import org.bukkit.entity.Entity;
/*  18:    */ import org.bukkit.entity.EntityType;
/*  19:    */ import org.bukkit.entity.Fireball;
/*  20:    */ import org.bukkit.entity.HumanEntity;
/*  21:    */ import org.bukkit.entity.Player;
/*  22:    */ import org.bukkit.entity.SmallFireball;
/*  23:    */ import org.bukkit.entity.Snowball;
/*  24:    */ import org.bukkit.event.EventHandler;
/*  25:    */ import org.bukkit.event.EventPriority;
/*  26:    */ import org.bukkit.event.Listener;
/*  27:    */ import org.bukkit.event.block.Action;
/*  28:    */ import org.bukkit.event.entity.EntityDamageEvent;
/*  29:    */ import org.bukkit.event.entity.FoodLevelChangeEvent;
/*  30:    */ import org.bukkit.event.inventory.InventoryClickEvent;
/*  31:    */ import org.bukkit.event.inventory.InventoryDragEvent;
/*  32:    */ import org.bukkit.event.player.PlayerInteractEvent;
/*  33:    */ import org.bukkit.event.player.PlayerJoinEvent;
/*  34:    */ import org.bukkit.event.player.PlayerQuitEvent;
/*  35:    */ import org.bukkit.inventory.Inventory;
/*  36:    */ import org.bukkit.inventory.ItemStack;
/*  37:    */ import org.bukkit.inventory.PlayerInventory;
/*  38:    */ import org.bukkit.inventory.meta.ItemMeta;
/*  39:    */ import org.bukkit.inventory.meta.SkullMeta;
/*  40:    */ import org.bukkit.plugin.PluginManager;
/*  41:    */ 
/*  42:    */ public class PlayerJoin
/*  43:    */   implements Listener
/*  44:    */ {
/*  45: 34 */   private Player hungerman = null;
/*  46: 35 */   private SkullMeta meta = null;
/*  47: 37 */   int randomNum = 0;
/*  48: 39 */   Player p = null;
/*  49:    */   String[] playerPerm;
/*  50:    */   
/*  51:    */   @EventHandler(priority=EventPriority.NORMAL)
/*  52:    */   public void onPlayerJoin(PlayerJoinEvent event)
/*  53:    */   {
/*  54: 46 */     File f = new File(uSkyBlock.getInstance().directoryPlayers, event.getPlayer().getName());
/*  55: 47 */     PlayerInfo pi = new PlayerInfo(event.getPlayer().getName());
/*  56: 48 */     if (f.exists())
/*  57:    */     {
/*  58: 50 */       PlayerInfo pi2 = uSkyBlock.getInstance().readPlayerFile(event.getPlayer().getName());
/*  59: 51 */       if (pi2 != null)
/*  60:    */       {
/*  61: 53 */         pi.setIslandLocation(pi2.getIslandLocation());
/*  62: 54 */         pi.setHomeLocation(pi2.getHomeLocation());
/*  63: 55 */         pi.setHasIsland(pi2.getHasIsland());
/*  64: 56 */         if (uSkyBlock.getInstance().getIslandConfig(pi.locationForParty()) == null) {
/*  65: 58 */           uSkyBlock.getInstance().createIslandConfig(pi.locationForParty(), event.getPlayer().getName());
/*  66:    */         }
/*  67: 60 */         uSkyBlock.getInstance().clearIslandConfig(pi.locationForParty(), event.getPlayer().getName());
/*  68: 61 */         if ((Settings.island_protectWithWorldGuard) && (Bukkit.getServer().getPluginManager().isPluginEnabled("WorldGuard"))) {
/*  69: 62 */           WorldGuardHandler.protectIsland(event.getPlayer(), event.getPlayer().getName(), pi);
/*  70:    */         }
/*  71:    */       }
/*  72: 64 */       f.delete();
/*  73:    */     }
/*  74: 72 */     uSkyBlock.getInstance().addActivePlayer(event.getPlayer().getName(), pi);
/*  75: 73 */     if ((pi.getHasIsland()) && (!uSkyBlock.getInstance().getTempIslandConfig(pi.locationForParty()).contains("general.level")))
/*  76:    */     {
/*  77: 75 */       uSkyBlock.getInstance().createIslandConfig(pi.locationForParty(), event.getPlayer().getName());
/*  78: 76 */       System.out.println("Creating new Config File");
/*  79:    */     }
/*  80: 79 */     uSkyBlock.getInstance().getIslandConfig(pi.locationForParty());
/*  81: 80 */     System.out.print("Loaded player file for " + event.getPlayer().getName());
/*  82:    */   }
/*  83:    */   
/*  84:    */   @EventHandler(priority=EventPriority.NORMAL)
/*  85:    */   public void onPlayerQuit(PlayerQuitEvent event)
/*  86:    */   {
/*  87: 90 */     if (uSkyBlock.getInstance().hasIsland(event.getPlayer().getName())) {
/*  88: 92 */       if (!uSkyBlock.getInstance().checkForOnlineMembers(event.getPlayer()))
/*  89:    */       {
/*  90: 94 */         System.out.print("Removing island config from memory.");
/*  91: 95 */         uSkyBlock.getInstance().removeIslandConfig(((PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(event.getPlayer().getName())).locationForParty());
/*  92:    */       }
/*  93:    */     }
/*  94: 98 */     uSkyBlock.getInstance().removeActivePlayer(event.getPlayer().getName());
/*  95:    */   }
/*  96:    */   
/*  97:    */   @EventHandler(priority=EventPriority.NORMAL)
/*  98:    */   public void onPlayerFoodChange(FoodLevelChangeEvent event)
/*  99:    */   {
/* 100:106 */     if ((event.getEntity() instanceof Player))
/* 101:    */     {
/* 102:108 */       this.hungerman = ((Player)event.getEntity());
/* 103:109 */       if (this.hungerman.getWorld().getName().equalsIgnoreCase(Settings.general_worldName)) {
/* 104:111 */         if (this.hungerman.getFoodLevel() > event.getFoodLevel()) {
/* 105:113 */           if (uSkyBlock.getInstance().playerIsOnIsland(this.hungerman))
/* 106:    */           {
/* 107:115 */             if (VaultHandler.checkPerk(this.hungerman.getName(), "usb.extra.hunger4", this.hungerman.getWorld()))
/* 108:    */             {
/* 109:120 */               event.setCancelled(true);
/* 110:121 */               return;
/* 111:    */             }
/* 112:124 */             if (VaultHandler.checkPerk(this.hungerman.getName(), "usb.extra.hunger3", this.hungerman.getWorld()))
/* 113:    */             {
/* 114:126 */               this.randomNum = (1 + (int)(Math.random() * 100.0D));
/* 115:127 */               if (this.randomNum <= 75)
/* 116:    */               {
/* 117:129 */                 event.setCancelled(true);
/* 118:130 */                 return;
/* 119:    */               }
/* 120:    */             }
/* 121:133 */             if (VaultHandler.checkPerk(this.hungerman.getName(), "usb.extra.hunger2", this.hungerman.getWorld()))
/* 122:    */             {
/* 123:135 */               this.randomNum = (1 + (int)(Math.random() * 100.0D));
/* 124:136 */               if (this.randomNum <= 50)
/* 125:    */               {
/* 126:138 */                 event.setCancelled(true);
/* 127:139 */                 return;
/* 128:    */               }
/* 129:    */             }
/* 130:142 */             if (VaultHandler.checkPerk(this.hungerman.getName(), "usb.extra.hunger", this.hungerman.getWorld()))
/* 131:    */             {
/* 132:144 */               this.randomNum = (1 + (int)(Math.random() * 100.0D));
/* 133:145 */               if (this.randomNum <= 25)
/* 134:    */               {
/* 135:147 */                 event.setCancelled(true);
/* 136:148 */                 return;
/* 137:    */               }
/* 138:    */             }
/* 139:    */           }
/* 140:    */         }
/* 141:    */       }
/* 142:    */     }
/* 143:    */   }
/* 144:    */   
/* 145:    */   @EventHandler(priority=EventPriority.NORMAL)
/* 146:    */   public void onPlayerInteract(PlayerInteractEvent event)
/* 147:    */   {
/* 148:160 */     if ((Settings.extras_obsidianToLava) && (uSkyBlock.getInstance().playerIsOnIsland(event.getPlayer())) && (event.getPlayer().getWorld().getName().equalsIgnoreCase(Settings.general_worldName))) {
/* 149:162 */       if ((event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) && (event.getPlayer().getItemInHand().getTypeId() == 325) && 
/* 150:163 */         (event.getClickedBlock().getType() == Material.OBSIDIAN)) {
/* 151:165 */         if (!uSkyBlock.getInstance().testForObsidian(event.getClickedBlock()))
/* 152:    */         {
/* 153:167 */           event.getPlayer().sendMessage(ChatColor.YELLOW + "Changing your obsidian back into lava. Be careful!");
/* 154:168 */           event.getClickedBlock().setType(Material.AIR);
/* 155:169 */           event.getPlayer().getInventory().removeItem(new ItemStack[] { new ItemStack(325, 1) });
/* 156:170 */           event.getPlayer().getInventory().addItem(new ItemStack[] { new ItemStack(327, 1) });
/* 157:    */         }
/* 158:    */       }
/* 159:    */     }
/* 160:    */   }
/* 161:    */   
/* 162:    */   @EventHandler(ignoreCancelled=true, priority=EventPriority.NORMAL)
/* 163:    */   public void onEntityDamage(EntityDamageEvent event)
/* 164:    */   {
/* 165:180 */     if (event.getEntity().getWorld().getName().equalsIgnoreCase(Settings.general_worldName)) {
/* 166:182 */       if (event.getEntity().getType().equals(EntityType.ITEM_FRAME))
/* 167:    */       {
/* 168:184 */         Iterator<Entity> closePlayers = event.getEntity().getNearbyEntities(3.0D, 3.0D, 3.0D).iterator();
/* 169:185 */         while (closePlayers.hasNext())
/* 170:    */         {
/* 171:187 */           Entity temp = (Entity)closePlayers.next();
/* 172:188 */           if ((temp instanceof Player))
/* 173:    */           {
/* 174:190 */             Player p = (Player)temp;
/* 175:191 */             if (!uSkyBlock.getInstance().locationIsOnIsland(p, event.getEntity().getLocation())) {
/* 176:193 */               event.setCancelled(true);
/* 177:    */             }
/* 178:    */           }
/* 179:    */           else
/* 180:    */           {
/* 181:197 */             if ((temp instanceof Arrow))
/* 182:    */             {
/* 183:199 */               event.setCancelled(true);
/* 184:200 */               return;
/* 185:    */             }
/* 186:203 */             if ((temp instanceof Snowball))
/* 187:    */             {
/* 188:205 */               event.setCancelled(true);
/* 189:206 */               return;
/* 190:    */             }
/* 191:209 */             if ((temp instanceof SmallFireball))
/* 192:    */             {
/* 193:211 */               event.setCancelled(true);
/* 194:212 */               return;
/* 195:    */             }
/* 196:215 */             if ((temp instanceof Creeper))
/* 197:    */             {
/* 198:217 */               event.setCancelled(true);
/* 199:218 */               return;
/* 200:    */             }
/* 201:221 */             if ((temp instanceof Fireball))
/* 202:    */             {
/* 203:223 */               event.setCancelled(true);
/* 204:224 */               return;
/* 205:    */             }
/* 206:    */           }
/* 207:    */         }
/* 208:    */       }
/* 209:    */     }
/* 210:    */   }
/* 211:    */   
/* 212:    */   @EventHandler(priority=EventPriority.MONITOR)
/* 213:    */   public void guiClick(InventoryClickEvent event)
/* 214:    */   {
/* 215:236 */     if (event.getInventory().getName().equalsIgnoreCase("Â§9Island Group Members"))
/* 216:    */     {
/* 217:238 */       event.setCancelled(true);
/* 218:239 */       if ((event.getSlot() < 0) || (event.getSlot() > 35)) {
/* 219:240 */         return;
/* 220:    */       }
/* 221:241 */       if (event.getCurrentItem().getTypeId() == 397) {
/* 222:242 */         this.meta = ((SkullMeta)event.getCurrentItem().getItemMeta());
/* 223:    */       }
/* 224:243 */       this.p = ((Player)event.getWhoClicked());
/* 225:244 */       if ((this.meta == null) || (event.getCurrentItem().getType() == Material.SIGN))
/* 226:    */       {
/* 227:247 */         this.p.closeInventory();
/* 228:248 */         this.p.openInventory(uSkyBlock.getInstance().displayIslandGUI(this.p));
/* 229:    */       }
/* 230:249 */       else if (this.meta.getLore().contains("Â§aÂ§lLeader"))
/* 231:    */       {
/* 232:252 */         this.p.closeInventory();
/* 233:253 */         this.p.openInventory(uSkyBlock.getInstance().displayPartyGUI(this.p));
/* 234:    */       }
/* 235:256 */       else if (!uSkyBlock.getInstance().isPartyLeader(this.p))
/* 236:    */       {
/* 237:259 */         this.p.closeInventory();
/* 238:260 */         this.p.openInventory(uSkyBlock.getInstance().displayPartyGUI(this.p));
/* 239:    */       }
/* 240:    */       else
/* 241:    */       {
/* 242:264 */         this.p.closeInventory();
/* 243:265 */         this.p.openInventory(uSkyBlock.getInstance().displayPartyPlayerGUI(this.p, this.meta.getOwner()));
/* 244:266 */         this.meta = null;
/* 245:    */       }
/* 246:    */     }
/* 247:271 */     else if (event.getInventory().getName().contains("Permissions"))
/* 248:    */     {
/* 249:273 */       event.setCancelled(true);
/* 250:274 */       if ((event.getSlot() < 0) || (event.getSlot() > 35)) {
/* 251:275 */         return;
/* 252:    */       }
/* 253:276 */       this.p = ((Player)event.getWhoClicked());
/* 254:277 */       this.playerPerm = event.getInventory().getName().split(" ");
/* 255:278 */       if (event.getCurrentItem().getTypeId() == 6)
/* 256:    */       {
/* 257:281 */         this.p.closeInventory();
/* 258:282 */         uSkyBlock.getInstance().changePlayerPermission(this.p, this.playerPerm[0], "canChangeBiome");
/* 259:283 */         this.p.openInventory(uSkyBlock.getInstance().displayPartyPlayerGUI(this.p, this.playerPerm[0]));
/* 260:    */       }
/* 261:284 */       else if (event.getCurrentItem().getTypeId() == 101)
/* 262:    */       {
/* 263:287 */         this.p.closeInventory();
/* 264:288 */         uSkyBlock.getInstance().changePlayerPermission(this.p, this.playerPerm[0], "canToggleLock");
/* 265:289 */         this.p.openInventory(uSkyBlock.getInstance().displayPartyPlayerGUI(this.p, this.playerPerm[0]));
/* 266:    */       }
/* 267:290 */       else if (event.getCurrentItem().getTypeId() == 90)
/* 268:    */       {
/* 269:293 */         this.p.closeInventory();
/* 270:294 */         uSkyBlock.getInstance().changePlayerPermission(this.p, this.playerPerm[0], "canChangeWarp");
/* 271:295 */         this.p.openInventory(uSkyBlock.getInstance().displayPartyPlayerGUI(this.p, this.playerPerm[0]));
/* 272:    */       }
/* 273:296 */       else if (event.getCurrentItem().getTypeId() == 69)
/* 274:    */       {
/* 275:299 */         this.p.closeInventory();
/* 276:300 */         uSkyBlock.getInstance().changePlayerPermission(this.p, this.playerPerm[0], "canToggleWarp");
/* 277:301 */         this.p.openInventory(uSkyBlock.getInstance().displayPartyPlayerGUI(this.p, this.playerPerm[0]));
/* 278:    */       }
/* 279:302 */       else if (event.getCurrentItem().getTypeId() == 398)
/* 280:    */       {
/* 281:305 */         this.p.closeInventory();
/* 282:306 */         uSkyBlock.getInstance().changePlayerPermission(this.p, this.playerPerm[0], "canInviteOthers");
/* 283:307 */         this.p.openInventory(uSkyBlock.getInstance().displayPartyPlayerGUI(this.p, this.playerPerm[0]));
/* 284:    */       }
/* 285:308 */       else if (event.getCurrentItem().getTypeId() == 301)
/* 286:    */       {
/* 287:311 */         this.p.closeInventory();
/* 288:312 */         uSkyBlock.getInstance().changePlayerPermission(this.p, this.playerPerm[0], "canKickOthers");
/* 289:313 */         this.p.openInventory(uSkyBlock.getInstance().displayPartyPlayerGUI(this.p, this.playerPerm[0]));
/* 290:    */       }
/* 291:314 */       else if (event.getCurrentItem().getTypeId() == 323)
/* 292:    */       {
/* 293:317 */         this.p.closeInventory();
/* 294:318 */         this.p.openInventory(uSkyBlock.getInstance().displayPartyGUI(this.p));
/* 295:    */       }
/* 296:    */       else
/* 297:    */       {
/* 298:322 */         this.p.closeInventory();
/* 299:323 */         this.p.openInventory(uSkyBlock.getInstance().displayPartyPlayerGUI(this.p, this.playerPerm[0]));
/* 300:    */       }
/* 301:    */     }
/* 302:327 */     else if (event.getInventory().getName().contains("Island Biome"))
/* 303:    */     {
/* 304:329 */       event.setCancelled(true);
/* 305:330 */       if ((event.getSlot() < 0) || (event.getSlot() > 35)) {
/* 306:331 */         return;
/* 307:    */       }
/* 308:332 */       this.p = ((Player)event.getWhoClicked());
/* 309:334 */       if ((event.getCurrentItem().getType() == Material.SAPLING) && (event.getCurrentItem().getDurability() == 3))
/* 310:    */       {
/* 311:337 */         this.p.closeInventory();
/* 312:338 */         this.p.performCommand("island biome jungle");
/* 313:339 */         this.p.openInventory(uSkyBlock.getInstance().displayIslandGUI(this.p));
/* 314:    */       }
/* 315:340 */       else if ((event.getCurrentItem().getType() == Material.SAPLING) && (event.getCurrentItem().getDurability() == 1))
/* 316:    */       {
/* 317:343 */         this.p.closeInventory();
/* 318:344 */         this.p.performCommand("island biome forest");
/* 319:345 */         this.p.openInventory(uSkyBlock.getInstance().displayIslandGUI(this.p));
/* 320:    */       }
/* 321:346 */       else if (event.getCurrentItem().getType() == Material.SAND)
/* 322:    */       {
/* 323:349 */         this.p.closeInventory();
/* 324:350 */         this.p.performCommand("island biome desert");
/* 325:351 */         this.p.openInventory(uSkyBlock.getInstance().displayIslandGUI(this.p));
/* 326:    */       }
/* 327:352 */       else if (event.getCurrentItem().getType() == Material.SNOW)
/* 328:    */       {
/* 329:355 */         this.p.closeInventory();
/* 330:356 */         this.p.performCommand("island biome taiga");
/* 331:357 */         this.p.openInventory(uSkyBlock.getInstance().displayIslandGUI(this.p));
/* 332:    */       }
/* 333:358 */       else if (event.getCurrentItem().getType() == Material.EYE_OF_ENDER)
/* 334:    */       {
/* 335:361 */         this.p.closeInventory();
/* 336:362 */         this.p.performCommand("island biome sky");
/* 337:363 */         this.p.openInventory(uSkyBlock.getInstance().displayIslandGUI(this.p));
/* 338:    */       }
/* 339:364 */       else if (event.getCurrentItem().getType() == Material.WATER_LILY)
/* 340:    */       {
/* 341:367 */         this.p.closeInventory();
/* 342:368 */         this.p.performCommand("island biome swampland");
/* 343:369 */         this.p.openInventory(uSkyBlock.getInstance().displayIslandGUI(this.p));
/* 344:    */       }
/* 345:370 */       else if (event.getCurrentItem().getType() == Material.FIRE)
/* 346:    */       {
/* 347:373 */         this.p.closeInventory();
/* 348:374 */         this.p.performCommand("island biome hell");
/* 349:375 */         this.p.openInventory(uSkyBlock.getInstance().displayIslandGUI(this.p));
/* 350:    */       }
/* 351:376 */       else if (event.getCurrentItem().getType() == Material.RED_MUSHROOM)
/* 352:    */       {
/* 353:379 */         this.p.closeInventory();
/* 354:380 */         this.p.performCommand("island biome mushroom");
/* 355:381 */         this.p.openInventory(uSkyBlock.getInstance().displayIslandGUI(this.p));
/* 356:    */       }
/* 357:382 */       else if (event.getCurrentItem().getType() == Material.WATER)
/* 358:    */       {
/* 359:385 */         this.p.closeInventory();
/* 360:386 */         this.p.performCommand("island biome ocean");
/* 361:387 */         this.p.openInventory(uSkyBlock.getInstance().displayIslandGUI(this.p));
/* 362:    */       }
/* 363:    */       else
/* 364:    */       {
/* 365:391 */         this.p.closeInventory();
/* 366:392 */         this.p.openInventory(uSkyBlock.getInstance().displayIslandGUI(this.p));
/* 367:    */       }
/* 368:    */     }
/* 369:395 */     else if (event.getInventory().getName().contains("Challenge Menu"))
/* 370:    */     {
/* 371:397 */       event.setCancelled(true);
/* 372:398 */       if ((event.getSlot() < 0) || (event.getSlot() > 35)) {
/* 373:399 */         return;
/* 374:    */       }
/* 375:400 */       if ((event.getCurrentItem().getType() != Material.DIRT) && (event.getCurrentItem().getType() != Material.IRON_BLOCK) && (event.getCurrentItem().getType() != Material.GOLD_BLOCK) && (event.getCurrentItem().getType() != Material.DIAMOND_BLOCK))
/* 376:    */       {
/* 377:402 */         this.p = ((Player)event.getWhoClicked());
/* 378:    */         
/* 379:404 */         this.p.closeInventory();
/* 380:405 */         if (event.getCurrentItem().getItemMeta() != null) {
/* 381:406 */           this.p.performCommand("c c " + event.getCurrentItem().getItemMeta().getDisplayName().replace("Â§e", "").replace("Â§8", "").replace("Â§a", "").replace("Â§2", "").replace("Â§l", ""));
/* 382:    */         }
/* 383:407 */         this.p.openInventory(uSkyBlock.getInstance().displayChallengeGUI(this.p));
/* 384:    */       }
/* 385:    */       else
/* 386:    */       {
/* 387:411 */         this.p.closeInventory();
/* 388:412 */         this.p.openInventory(uSkyBlock.getInstance().displayIslandGUI(this.p));
/* 389:    */       }
/* 390:    */     }
/* 391:414 */     else if (event.getInventory().getName().contains("Island Log"))
/* 392:    */     {
/* 393:416 */       event.setCancelled(true);
/* 394:417 */       if ((event.getSlot() < 0) || (event.getSlot() > 35)) {
/* 395:418 */         return;
/* 396:    */       }
/* 397:420 */       this.p.closeInventory();
/* 398:421 */       this.p.openInventory(uSkyBlock.getInstance().displayIslandGUI(this.p));
/* 399:    */     }
/* 400:423 */     else if (event.getInventory().getName().contains("Island Menu"))
/* 401:    */     {
/* 402:425 */       event.setCancelled(true);
/* 403:426 */       if ((event.getSlot() < 0) || (event.getSlot() > 35)) {
/* 404:427 */         return;
/* 405:    */       }
/* 406:428 */       this.p = ((Player)event.getWhoClicked());
/* 407:430 */       if ((event.getCurrentItem().getType() == Material.SAPLING) && (event.getCurrentItem().getDurability() == 3))
/* 408:    */       {
/* 409:433 */         this.p.closeInventory();
/* 410:434 */         this.p.performCommand("island biome");
/* 411:    */       }
/* 412:436 */       else if (event.getCurrentItem().getType() == Material.SKULL_ITEM)
/* 413:    */       {
/* 414:439 */         this.p.closeInventory();
/* 415:440 */         this.p.performCommand("island party");
/* 416:    */       }
/* 417:442 */       else if (event.getCurrentItem().getType() == Material.BED)
/* 418:    */       {
/* 419:445 */         this.p.closeInventory();
/* 420:446 */         this.p.performCommand("island sethome");
/* 421:447 */         this.p.openInventory(uSkyBlock.getInstance().displayIslandGUI(this.p));
/* 422:    */       }
/* 423:449 */       else if (event.getCurrentItem().getType() == Material.HOPPER)
/* 424:    */       {
/* 425:452 */         this.p.closeInventory();
/* 426:453 */         this.p.performCommand("island setwarp");
/* 427:454 */         this.p.openInventory(uSkyBlock.getInstance().displayIslandGUI(this.p));
/* 428:    */       }
/* 429:456 */       else if (event.getCurrentItem().getType() == Material.BOOK_AND_QUILL)
/* 430:    */       {
/* 431:459 */         this.p.closeInventory();
/* 432:460 */         this.p.performCommand("island log");
/* 433:    */       }
/* 434:462 */       else if (event.getCurrentItem().getType() == Material.ENDER_PORTAL)
/* 435:    */       {
/* 436:465 */         this.p.closeInventory();
/* 437:466 */         this.p.performCommand("island home");
/* 438:    */       }
/* 439:468 */       else if (event.getCurrentItem().getType() == Material.GRASS)
/* 440:    */       {
/* 441:471 */         this.p.closeInventory();
/* 442:472 */         this.p.performCommand("island create");
/* 443:    */       }
/* 444:474 */       else if (event.getCurrentItem().getType() == Material.CHEST)
/* 445:    */       {
/* 446:477 */         this.p.closeInventory();
/* 447:478 */         this.p.performCommand("chc open perks");
/* 448:    */       }
/* 449:480 */       else if (event.getCurrentItem().getType() == Material.ENDER_CHEST)
/* 450:    */       {
/* 451:483 */         this.p.closeInventory();
/* 452:484 */         if (VaultHandler.checkPerk(this.p.getName(), "group.donor", this.p.getWorld())) {
/* 453:486 */           this.p.performCommand("chc open donor");
/* 454:    */         } else {
/* 455:489 */           this.p.performCommand("donate");
/* 456:    */         }
/* 457:    */       }
/* 458:492 */       else if (event.getCurrentItem().getType() == Material.EXP_BOTTLE)
/* 459:    */       {
/* 460:495 */         this.p.closeInventory();
/* 461:496 */         this.p.performCommand("island level");
/* 462:    */       }
/* 463:499 */       else if (event.getCurrentItem().getType() == Material.DIAMOND_ORE)
/* 464:    */       {
/* 465:502 */         this.p.closeInventory();
/* 466:503 */         this.p.performCommand("c");
/* 467:    */       }
/* 468:506 */       else if ((event.getCurrentItem().getType() == Material.ENDER_STONE) || (event.getCurrentItem().getType() == Material.PORTAL))
/* 469:    */       {
/* 470:509 */         this.p.closeInventory();
/* 471:510 */         this.p.performCommand("island togglewarp");
/* 472:511 */         this.p.openInventory(uSkyBlock.getInstance().displayIslandGUI(this.p));
/* 473:    */       }
/* 474:513 */       else if ((event.getCurrentItem().getType() == Material.IRON_FENCE) && (uSkyBlock.getInstance().getIslandConfig(((PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(event.getWhoClicked().getName())).locationForParty()).getBoolean("general.locked")))
/* 475:    */       {
/* 476:516 */         this.p.closeInventory();
/* 477:517 */         this.p.performCommand("island unlock");
/* 478:518 */         this.p.openInventory(uSkyBlock.getInstance().displayIslandGUI(this.p));
/* 479:    */       }
/* 480:520 */       else if ((event.getCurrentItem().getType() == Material.IRON_FENCE) && (!uSkyBlock.getInstance().getIslandConfig(((PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(event.getWhoClicked().getName())).locationForParty()).getBoolean("general.locked")))
/* 481:    */       {
/* 482:523 */         this.p.closeInventory();
/* 483:524 */         this.p.performCommand("island lock");
/* 484:525 */         this.p.openInventory(uSkyBlock.getInstance().displayIslandGUI(this.p));
/* 485:    */       }
/* 486:    */       else
/* 487:    */       {
/* 488:530 */         this.p.closeInventory();
/* 489:531 */         this.p.openInventory(uSkyBlock.getInstance().displayIslandGUI(this.p));
/* 490:    */       }
/* 491:    */     }
/* 492:    */   }
/* 493:    */   
/* 494:    */   @EventHandler(priority=EventPriority.MONITOR)
/* 495:    */   public void onInventoryDrag(InventoryDragEvent event)
/* 496:    */   {
/* 497:541 */     if (event.getInventory().getName().equalsIgnoreCase("Â§9SB Island Group Members"))
/* 498:    */     {
/* 499:543 */       event.setCancelled(true);
/* 500:544 */       this.meta = ((SkullMeta)event.getCursor().getItemMeta());
/* 501:545 */       this.p = ((Player)event.getWhoClicked());
/* 502:546 */       if (this.meta.getOwner() == null)
/* 503:    */       {
/* 504:548 */         this.p.updateInventory();
/* 505:549 */         this.p.closeInventory();
/* 506:550 */         this.p.openInventory(uSkyBlock.getInstance().displayPartyGUI(this.p));
/* 507:    */       }
/* 508:    */       else
/* 509:    */       {
/* 510:553 */         this.p.updateInventory();
/* 511:554 */         this.p.closeInventory();
/* 512:555 */         this.p.openInventory(uSkyBlock.getInstance().displayPartyPlayerGUI(this.p, this.meta.getOwner()));
/* 513:    */       }
/* 514:    */     }
/* 515:    */   }
/* 516:    */ }
