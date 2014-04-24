/*   1:    */ package us.talabrek.ultimateskyblock;
/*   2:    */ 
/*   3:    */ import java.io.File;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.io.PrintStream;
/*   6:    */ import java.io.Serializable;
/*   7:    */ import java.util.Calendar;
/*   8:    */ import java.util.HashMap;
/*   9:    */ import java.util.Iterator;
/*  10:    */ import java.util.Set;
/*  11:    */ import java.util.logging.Level;
/*  12:    */ import java.util.logging.Logger;
/*  13:    */ import org.bukkit.Bukkit;
/*  14:    */ import org.bukkit.Location;
/*  15:    */ import org.bukkit.Server;
/*  16:    */ import org.bukkit.World;
/*  17:    */ import org.bukkit.configuration.file.FileConfiguration;
/*  18:    */ import org.bukkit.configuration.file.YamlConfiguration;
/*  19:    */ import org.bukkit.entity.Player;
/*  20:    */ 
/*  21:    */ public class PlayerInfo
/*  22:    */   implements Serializable
/*  23:    */ {
/*  24:    */   private static final long serialVersionUID = 1L;
/*  25:    */   private String playerName;
/*  26:    */   private boolean hasIsland;
/*  27:    */   private boolean hasParty;
/*  28:    */   private String islandLocation;
/*  29:    */   private String homeLocation;
/*  30:    */   private HashMap<String, Challenge> challengeListNew;
/*  31:    */   private String partyIslandLocation;
/*  32:    */   private FileConfiguration playerData;
/*  33:    */   private File playerConfigFile;
/*  34:    */   
/*  35:    */   public PlayerInfo(String playerName)
/*  36:    */   {
/*  37: 50 */     this.playerName = playerName;
/*  38: 51 */     loadPlayer(playerName);
/*  39:    */   }
/*  40:    */   
/*  41:    */   public PlayerInfo(String playerName, boolean hasIsland, int iX, int iY, int iZ, int hX, int hY, int hZ)
/*  42:    */   {
/*  43: 56 */     this.playerName = playerName;
/*  44: 57 */     this.hasIsland = hasIsland;
/*  45: 58 */     if ((iX == 0) && (iY == 0) && (iZ == 0)) {
/*  46: 59 */       this.islandLocation = null;
/*  47:    */     } else {
/*  48: 61 */       this.islandLocation = getStringLocation(new Location(uSkyBlock.getSkyBlockWorld(), iX, iY, iZ));
/*  49:    */     }
/*  50: 62 */     if ((hX == 0) && (hY == 0) && (hZ == 0)) {
/*  51: 63 */       this.homeLocation = null;
/*  52:    */     } else {
/*  53: 65 */       this.homeLocation = getStringLocation(new Location(uSkyBlock.getSkyBlockWorld(), hX, hY, hZ));
/*  54:    */     }
/*  55: 66 */     this.challengeListNew = new HashMap();
/*  56: 67 */     buildChallengeList();
/*  57:    */   }
/*  58:    */   
/*  59:    */   public void startNewIsland(Location l)
/*  60:    */   {
/*  61: 82 */     this.hasIsland = true;
/*  62: 83 */     setIslandLocation(l);
/*  63: 84 */     this.hasParty = false;
/*  64: 85 */     this.homeLocation = null;
/*  65:    */   }
/*  66:    */   
/*  67:    */   public void removeFromIsland()
/*  68:    */   {
/*  69: 97 */     this.hasIsland = false;
/*  70: 98 */     setIslandLocation(null);
/*  71: 99 */     this.hasParty = false;
/*  72:100 */     this.homeLocation = null;
/*  73:    */   }
/*  74:    */   
/*  75:    */   public void setPlayerName(String s)
/*  76:    */   {
/*  77:111 */     this.playerName = s;
/*  78:    */   }
/*  79:    */   
/*  80:    */   public boolean getHasIsland()
/*  81:    */   {
/*  82:115 */     return this.hasIsland;
/*  83:    */   }
/*  84:    */   
/*  85:    */   public String locationForParty()
/*  86:    */   {
/*  87:119 */     return getPartyLocationString(this.islandLocation);
/*  88:    */   }
/*  89:    */   
/*  90:    */   public String locationForPartyOld()
/*  91:    */   {
/*  92:123 */     return getPartyLocationString(this.partyIslandLocation);
/*  93:    */   }
/*  94:    */   
/*  95:    */   public Player getPlayer()
/*  96:    */   {
/*  97:127 */     return Bukkit.getPlayer(this.playerName);
/*  98:    */   }
/*  99:    */   
/* 100:    */   public String getPlayerName()
/* 101:    */   {
/* 102:131 */     return this.playerName;
/* 103:    */   }
/* 104:    */   
/* 105:    */   public void setHasIsland(boolean b)
/* 106:    */   {
/* 107:135 */     this.hasIsland = b;
/* 108:    */   }
/* 109:    */   
/* 110:    */   public void setIslandLocation(Location l)
/* 111:    */   {
/* 112:139 */     this.islandLocation = getStringLocation(l);
/* 113:    */   }
/* 114:    */   
/* 115:    */   public Location getIslandLocation()
/* 116:    */   {
/* 117:143 */     return getLocationString(this.islandLocation);
/* 118:    */   }
/* 119:    */   
/* 120:    */   public void setHomeLocation(Location l)
/* 121:    */   {
/* 122:148 */     this.homeLocation = getStringLocation(l);
/* 123:    */   }
/* 124:    */   
/* 125:    */   public Location getHomeLocation()
/* 126:    */   {
/* 127:153 */     return getLocationString(this.homeLocation);
/* 128:    */   }
/* 129:    */   
/* 130:    */   public boolean getHasParty()
/* 131:    */   {
/* 132:158 */     return this.hasParty;
/* 133:    */   }
/* 134:    */   
/* 135:    */   public void setJoinParty(Location l)
/* 136:    */   {
/* 137:163 */     this.hasParty = true;
/* 138:164 */     this.islandLocation = getStringLocation(l);
/* 139:165 */     this.hasIsland = true;
/* 140:    */   }
/* 141:    */   
/* 142:    */   public void setLeaveParty()
/* 143:    */   {
/* 144:170 */     this.hasParty = false;
/* 145:171 */     this.islandLocation = null;
/* 146:172 */     this.hasIsland = false;
/* 147:173 */     if (Bukkit.getPlayer(this.playerName) == null) {
/* 148:174 */       getPlayerConfig(this.playerName).set("player.kickWarning", Boolean.valueOf(true));
/* 149:    */     }
/* 150:    */   }
/* 151:    */   
/* 152:    */   private Location getLocationString(String s)
/* 153:    */   {
/* 154:178 */     if ((s == null) || (s.trim() == "")) {
/* 155:179 */       return null;
/* 156:    */     }
/* 157:181 */     String[] parts = s.split(":");
/* 158:182 */     if (parts.length == 4)
/* 159:    */     {
/* 160:183 */       World w = Bukkit.getServer().getWorld(parts[0]);
/* 161:184 */       int x = Integer.parseInt(parts[1]);
/* 162:185 */       int y = Integer.parseInt(parts[2]);
/* 163:186 */       int z = Integer.parseInt(parts[3]);
/* 164:187 */       return new Location(w, x, y, z);
/* 165:    */     }
/* 166:189 */     return null;
/* 167:    */   }
/* 168:    */   
/* 169:    */   private String getPartyLocationString(String s)
/* 170:    */   {
/* 171:193 */     if ((s == null) || (s.trim() == "")) {
/* 172:194 */       return null;
/* 173:    */     }
/* 174:196 */     String[] parts = s.split(":");
/* 175:197 */     if (parts.length == 4) {
/* 176:198 */       return parts[1] + "," + parts[3];
/* 177:    */     }
/* 178:200 */     return null;
/* 179:    */   }
/* 180:    */   
/* 181:    */   public void completeChallenge(String challenge)
/* 182:    */   {
/* 183:205 */     if (this.challengeListNew.containsKey(challenge))
/* 184:    */     {
/* 185:208 */       if (!onChallengeCooldown(challenge)) {
/* 186:210 */         if (uSkyBlock.getInstance().getConfig().contains("options.challenges.challengeList." + challenge + ".resetInHours")) {
/* 187:212 */           ((Challenge)this.challengeListNew.get(challenge)).setFirstCompleted(Calendar.getInstance().getTimeInMillis() + uSkyBlock.getInstance().getConfig().getInt("options.challenges.challengeList." + challenge + ".resetInHours") * 3600000);
/* 188:214 */         } else if (uSkyBlock.getInstance().getConfig().contains("options.challenges.defaultResetInHours")) {
/* 189:216 */           ((Challenge)this.challengeListNew.get(challenge)).setFirstCompleted(Calendar.getInstance().getTimeInMillis() + uSkyBlock.getInstance().getConfig().getInt("options.challenges.defaultResetInHours") * 3600000);
/* 190:    */         } else {
/* 191:220 */           ((Challenge)this.challengeListNew.get(challenge)).setFirstCompleted(Calendar.getInstance().getTimeInMillis() + 518400000L);
/* 192:    */         }
/* 193:    */       }
/* 194:223 */       ((Challenge)this.challengeListNew.get(challenge)).addTimesCompleted();
/* 195:    */     }
/* 196:    */   }
/* 197:    */   
/* 198:    */   public long getChallengeCooldownTime(String challenge)
/* 199:    */   {
/* 200:229 */     if (getChallenge(challenge).getFirstCompleted() > 0L)
/* 201:    */     {
/* 202:231 */       if (getChallenge(challenge).getFirstCompleted() > Calendar.getInstance().getTimeInMillis()) {
/* 203:232 */         return getChallenge(challenge).getFirstCompleted() - Calendar.getInstance().getTimeInMillis();
/* 204:    */       }
/* 205:235 */       return 0L;
/* 206:    */     }
/* 207:238 */     return 0L;
/* 208:    */   }
/* 209:    */   
/* 210:    */   public boolean onChallengeCooldown(String challenge)
/* 211:    */   {
/* 212:243 */     if (getChallenge(challenge).getFirstCompleted() > 0L)
/* 213:    */     {
/* 214:245 */       if (getChallenge(challenge).getFirstCompleted() > Calendar.getInstance().getTimeInMillis()) {
/* 215:247 */         return true;
/* 216:    */       }
/* 217:250 */       return false;
/* 218:    */     }
/* 219:253 */     return false;
/* 220:    */   }
/* 221:    */   
/* 222:    */   public void resetChallenge(String challenge)
/* 223:    */   {
/* 224:258 */     if (this.challengeListNew.containsKey(challenge))
/* 225:    */     {
/* 226:261 */       ((Challenge)this.challengeListNew.get(challenge)).setTimesCompleted(0);
/* 227:262 */       ((Challenge)this.challengeListNew.get(challenge)).setFirstCompleted(0L);
/* 228:    */     }
/* 229:    */   }
/* 230:    */   
/* 231:    */   public int checkChallenge(String challenge)
/* 232:    */   {
/* 233:    */     try
/* 234:    */     {
/* 235:269 */       if (this.challengeListNew.containsKey(challenge.toLowerCase())) {
/* 236:271 */         return ((Challenge)this.challengeListNew.get(challenge.toLowerCase())).getTimesCompleted();
/* 237:    */       }
/* 238:    */     }
/* 239:    */     catch (ClassCastException localClassCastException) {}
/* 240:277 */     return 0;
/* 241:    */   }
/* 242:    */   
/* 243:    */   public int checkChallengeSinceTimer(String challenge)
/* 244:    */   {
/* 245:    */     try
/* 246:    */     {
/* 247:283 */       if (this.challengeListNew.containsKey(challenge.toLowerCase())) {
/* 248:285 */         return ((Challenge)this.challengeListNew.get(challenge.toLowerCase())).getTimesCompletedSinceTimer();
/* 249:    */       }
/* 250:    */     }
/* 251:    */     catch (ClassCastException localClassCastException) {}
/* 252:291 */     return 0;
/* 253:    */   }
/* 254:    */   
/* 255:    */   public Challenge getChallenge(String challenge)
/* 256:    */   {
/* 257:296 */     if (this.challengeListNew.containsKey(challenge.toLowerCase())) {
/* 258:298 */       return (Challenge)this.challengeListNew.get(challenge.toLowerCase());
/* 259:    */     }
/* 260:301 */     return null;
/* 261:    */   }
/* 262:    */   
/* 263:    */   public boolean challengeExists(String challenge)
/* 264:    */   {
/* 265:306 */     if (this.challengeListNew.containsKey(challenge.toLowerCase())) {
/* 266:308 */       return true;
/* 267:    */     }
/* 268:310 */     return false;
/* 269:    */   }
/* 270:    */   
/* 271:    */   public void resetAllChallenges()
/* 272:    */   {
/* 273:315 */     this.challengeListNew = null;
/* 274:316 */     buildChallengeList();
/* 275:    */   }
/* 276:    */   
/* 277:    */   public void displayData(String player)
/* 278:    */   {
/* 279:321 */     System.out.print(player + " has an island: " + getHasIsland());
/* 280:322 */     if (getIslandLocation() != null) {
/* 281:323 */       System.out.print(player + " island location: " + getIslandLocation().toString());
/* 282:    */     }
/* 283:324 */     if (getHomeLocation() != null) {
/* 284:325 */       System.out.print(player + " home location: " + getHomeLocation().toString());
/* 285:    */     }
/* 286:    */   }
/* 287:    */   
/* 288:    */   public void buildChallengeList()
/* 289:    */   {
/* 290:330 */     if (this.challengeListNew == null) {
/* 291:331 */       this.challengeListNew = new HashMap();
/* 292:    */     }
/* 293:332 */     Iterator<String> itr = Settings.challenges_challengeList.iterator();
/* 294:333 */     while (itr.hasNext())
/* 295:    */     {
/* 296:335 */       String current = (String)itr.next();
/* 297:336 */       if (!this.challengeListNew.containsKey(current.toLowerCase())) {
/* 298:338 */         this.challengeListNew.put(current.toLowerCase(), new Challenge(current.toLowerCase(), 0L, 0, 0));
/* 299:    */       }
/* 300:    */     }
/* 301:341 */     if (this.challengeListNew.size() > Settings.challenges_challengeList.size())
/* 302:    */     {
/* 303:343 */       Object[] challengeArray = this.challengeListNew.keySet().toArray();
/* 304:344 */       for (int i = 0; i < challengeArray.length; i++) {
/* 305:346 */         if (!Settings.challenges_challengeList.contains(challengeArray[i].toString())) {
/* 306:348 */           this.challengeListNew.remove(challengeArray[i].toString());
/* 307:    */         }
/* 308:    */       }
/* 309:    */     }
/* 310:    */   }
/* 311:    */   
/* 312:    */   public void displayChallengeList()
/* 313:    */   {
/* 314:356 */     Iterator<String> itr = this.challengeListNew.keySet().iterator();
/* 315:357 */     System.out.print("Displaying Challenge list for " + this.playerName);
/* 316:358 */     while (itr.hasNext())
/* 317:    */     {
/* 318:360 */       String current = (String)itr.next();
/* 319:361 */       System.out.print(current + ": " + this.challengeListNew.get(current));
/* 320:    */     }
/* 321:    */   }
/* 322:    */   
/* 323:    */   private String getStringLocation(Location l)
/* 324:    */   {
/* 325:366 */     if (l == null) {
/* 326:367 */       return "";
/* 327:    */     }
/* 328:369 */     return l.getWorld().getName() + ":" + l.getBlockX() + ":" + l.getBlockY() + ":" + l.getBlockZ();
/* 329:    */   }
/* 330:    */   
/* 331:    */   public Location getPartyIslandLocation()
/* 332:    */   {
/* 333:373 */     return getLocationString(this.partyIslandLocation);
/* 334:    */   }
/* 335:    */   
/* 336:    */   public void setupPlayer(String player)
/* 337:    */   {
/* 338:379 */     System.out.println("Creating player config Paths!");
/* 339:380 */     getPlayerConfig(player).createSection("player");
/* 340:381 */     getPlayerConfig(player);FileConfiguration.createPath(getPlayerConfig(player).getConfigurationSection("player"), "hasIsland");
/* 341:382 */     getPlayerConfig(player);FileConfiguration.createPath(getPlayerConfig(player).getConfigurationSection("player"), "islandX");
/* 342:383 */     getPlayerConfig(player);FileConfiguration.createPath(getPlayerConfig(player).getConfigurationSection("player"), "islandY");
/* 343:384 */     getPlayerConfig(player);FileConfiguration.createPath(getPlayerConfig(player).getConfigurationSection("player"), "islandZ");
/* 344:385 */     getPlayerConfig(player);FileConfiguration.createPath(getPlayerConfig(player).getConfigurationSection("player"), "homeX");
/* 345:386 */     getPlayerConfig(player);FileConfiguration.createPath(getPlayerConfig(player).getConfigurationSection("player"), "homeY");
/* 346:387 */     getPlayerConfig(player);FileConfiguration.createPath(getPlayerConfig(player).getConfigurationSection("player"), "homeZ");
/* 347:388 */     getPlayerConfig(player);FileConfiguration.createPath(getPlayerConfig(player).getConfigurationSection("player"), "challenges");
/* 348:389 */     getPlayerConfig(player).set("player.hasIsland", Boolean.valueOf(false));
/* 349:390 */     getPlayerConfig(player).set("player.islandX", Integer.valueOf(0));
/* 350:391 */     getPlayerConfig(player).set("player.islandY", Integer.valueOf(0));
/* 351:392 */     getPlayerConfig(player).set("player.islandZ", Integer.valueOf(0));
/* 352:393 */     getPlayerConfig(player).set("player.homeX", Integer.valueOf(0));
/* 353:394 */     getPlayerConfig(player).set("player.homeY", Integer.valueOf(0));
/* 354:395 */     getPlayerConfig(player).set("player.homeZ", Integer.valueOf(0));
/* 355:396 */     getPlayerConfig(player).set("player.kickWarning", Boolean.valueOf(false));
/* 356:397 */     Iterator<String> ent = this.challengeListNew.keySet().iterator();
/* 357:398 */     String currentChallenge = "";
/* 358:399 */     while (ent.hasNext())
/* 359:    */     {
/* 360:401 */       currentChallenge = (String)ent.next();
/* 361:402 */       getPlayerConfig(player).createSection("player.challenges." + currentChallenge);
/* 362:403 */       getPlayerConfig(player);FileConfiguration.createPath(getPlayerConfig(player).getConfigurationSection("player.challenges." + currentChallenge), "firstCompleted");
/* 363:404 */       getPlayerConfig(player);FileConfiguration.createPath(getPlayerConfig(player).getConfigurationSection("player.challenges." + currentChallenge), "timesCompleted");
/* 364:405 */       getPlayerConfig(player);FileConfiguration.createPath(getPlayerConfig(player).getConfigurationSection("player.challenges." + currentChallenge), "timesCompletedSinceTimer");
/* 365:406 */       getPlayerConfig(player).set("player.challenges." + currentChallenge + ".firstCompleted", Long.valueOf(((Challenge)this.challengeListNew.get(currentChallenge)).getFirstCompleted()));
/* 366:407 */       getPlayerConfig(player).set("player.challenges." + currentChallenge + ".timesCompleted", Integer.valueOf(((Challenge)this.challengeListNew.get(currentChallenge)).getTimesCompleted()));
/* 367:408 */       getPlayerConfig(player).set("player.challenges." + currentChallenge + ".timesCompletedSinceTimer", Integer.valueOf(((Challenge)this.challengeListNew.get(currentChallenge)).getTimesCompletedSinceTimer()));
/* 368:    */     }
/* 369:    */   }
/* 370:    */   
/* 371:    */   public PlayerInfo loadPlayer(String player)
/* 372:    */   {
/* 373:415 */     if (!getPlayerConfig(player).contains("player.hasIsland"))
/* 374:    */     {
/* 375:418 */       this.playerName = player;
/* 376:419 */       this.hasIsland = false;
/* 377:420 */       this.islandLocation = null;
/* 378:421 */       this.homeLocation = null;
/* 379:422 */       this.hasParty = false;
/* 380:423 */       buildChallengeList();
/* 381:424 */       createPlayerConfig(player);
/* 382:425 */       return this;
/* 383:    */     }
/* 384:    */     try
/* 385:    */     {
/* 386:431 */       this.hasIsland = getPlayerConfig(player).getBoolean("player.hasIsland");
/* 387:432 */       this.islandLocation = getStringLocation(new Location(uSkyBlock.getSkyBlockWorld(), getPlayerConfig(player).getInt("player.islandX"), getPlayerConfig(player).getInt("player.islandY"), getPlayerConfig(player).getInt("player.islandZ")));
/* 388:433 */       this.homeLocation = getStringLocation(new Location(uSkyBlock.getSkyBlockWorld(), getPlayerConfig(player).getInt("player.homeX"), getPlayerConfig(player).getInt("player.homeY"), getPlayerConfig(player).getInt("player.homeZ")));
/* 389:434 */       buildChallengeList();
/* 390:    */       
/* 391:436 */       Iterator<String> ent = Settings.challenges_challengeList.iterator();
/* 392:437 */       String currentChallenge = "";
/* 393:438 */       this.challengeListNew = new HashMap();
/* 394:439 */       while (ent.hasNext())
/* 395:    */       {
/* 396:441 */         currentChallenge = (String)ent.next();
/* 397:442 */         this.challengeListNew.put(currentChallenge, new Challenge(currentChallenge, getPlayerConfig(player).getLong("player.challenges." + currentChallenge + ".firstCompleted"), getPlayerConfig(player).getInt("player.challenges." + currentChallenge + ".timesCompleted"), getPlayerConfig(player).getInt("player.challenges." + currentChallenge + ".timesCompletedSinceTimer")));
/* 398:    */       }
/* 399:445 */       if (Bukkit.getPlayer(player) != null) {
/* 400:447 */         if (getPlayerConfig(player).getBoolean("player.kickWarning"))
/* 401:    */         {
/* 402:449 */           Bukkit.getPlayer(player).sendMessage("Â§cYou were removed from your island since the last time you played!");
/* 403:450 */           getPlayerConfig(player).set("player.kickWarning", Boolean.valueOf(false));
/* 404:    */         }
/* 405:    */       }
/* 406:453 */       return this;
/* 407:    */     }
/* 408:    */     catch (Exception e)
/* 409:    */     {
/* 410:454 */       e.printStackTrace();
/* 411:    */       
/* 412:456 */       System.out.println("Returning null while loading, not good!");
/* 413:    */     }
/* 414:457 */     return null;
/* 415:    */   }
/* 416:    */   
/* 417:    */   public void reloadPlayerConfig(String player)
/* 418:    */   {
/* 419:462 */     this.playerConfigFile = new File(uSkyBlock.getInstance().directoryPlayers, player + ".yml");
/* 420:463 */     this.playerData = YamlConfiguration.loadConfiguration(this.playerConfigFile);
/* 421:    */   }
/* 422:    */   
/* 423:    */   public void createPlayerConfig(String player)
/* 424:    */   {
/* 425:468 */     System.out.println("Creating new player config!");
/* 426:469 */     getPlayerConfig(player);
/* 427:470 */     setupPlayer(player);
/* 428:    */   }
/* 429:    */   
/* 430:    */   public FileConfiguration getPlayerConfig(String player)
/* 431:    */   {
/* 432:474 */     if (this.playerData == null)
/* 433:    */     {
/* 434:475 */       System.out.println("Reloading player data!");
/* 435:476 */       reloadPlayerConfig(player);
/* 436:    */     }
/* 437:478 */     return this.playerData;
/* 438:    */   }
/* 439:    */   
/* 440:    */   public void savePlayerConfig(String player)
/* 441:    */   {
/* 442:486 */     if (this.playerData == null)
/* 443:    */     {
/* 444:487 */       System.out.println("Can't save player data!");
/* 445:488 */       return;
/* 446:    */     }
/* 447:491 */     getPlayerConfig(player).set("player.hasIsland", Boolean.valueOf(getHasIsland()));
/* 448:492 */     if (getIslandLocation() != null)
/* 449:    */     {
/* 450:494 */       getPlayerConfig(player).set("player.islandX", Integer.valueOf(getIslandLocation().getBlockX()));
/* 451:495 */       getPlayerConfig(player).set("player.islandY", Integer.valueOf(getIslandLocation().getBlockY()));
/* 452:496 */       getPlayerConfig(player).set("player.islandZ", Integer.valueOf(getIslandLocation().getBlockZ()));
/* 453:    */     }
/* 454:    */     else
/* 455:    */     {
/* 456:499 */       getPlayerConfig(player).set("player.islandX", Integer.valueOf(0));
/* 457:500 */       getPlayerConfig(player).set("player.islandY", Integer.valueOf(0));
/* 458:501 */       getPlayerConfig(player).set("player.islandZ", Integer.valueOf(0));
/* 459:    */     }
/* 460:503 */     if (getHomeLocation() != null)
/* 461:    */     {
/* 462:505 */       getPlayerConfig(player).set("player.homeX", Integer.valueOf(getHomeLocation().getBlockX()));
/* 463:506 */       getPlayerConfig(player).set("player.homeY", Integer.valueOf(getHomeLocation().getBlockY()));
/* 464:507 */       getPlayerConfig(player).set("player.homeZ", Integer.valueOf(getHomeLocation().getBlockZ()));
/* 465:    */     }
/* 466:    */     else
/* 467:    */     {
/* 468:510 */       getPlayerConfig(player).set("player.homeX", Integer.valueOf(0));
/* 469:511 */       getPlayerConfig(player).set("player.homeY", Integer.valueOf(0));
/* 470:512 */       getPlayerConfig(player).set("player.homeZ", Integer.valueOf(0));
/* 471:    */     }
/* 472:514 */     Iterator<String> ent = this.challengeListNew.keySet().iterator();
/* 473:515 */     String currentChallenge = "";
/* 474:516 */     while (ent.hasNext())
/* 475:    */     {
/* 476:518 */       currentChallenge = (String)ent.next();
/* 477:519 */       getPlayerConfig(player).set("player.challenges." + currentChallenge + ".firstCompleted", Long.valueOf(((Challenge)this.challengeListNew.get(currentChallenge)).getFirstCompleted()));
/* 478:520 */       getPlayerConfig(player).set("player.challenges." + currentChallenge + ".timesCompleted", Integer.valueOf(((Challenge)this.challengeListNew.get(currentChallenge)).getTimesCompleted()));
/* 479:521 */       getPlayerConfig(player).set("player.challenges." + currentChallenge + ".timesCompletedSinceTimer", Integer.valueOf(((Challenge)this.challengeListNew.get(currentChallenge)).getTimesCompletedSinceTimer()));
/* 480:    */     }
/* 481:523 */     this.playerConfigFile = new File(uSkyBlock.getInstance().directoryPlayers, player + ".yml");
/* 482:    */     try
/* 483:    */     {
/* 484:526 */       getPlayerConfig(player).save(this.playerConfigFile);
/* 485:527 */       System.out.println("Player data saved!");
/* 486:    */     }
/* 487:    */     catch (IOException ex)
/* 488:    */     {
/* 489:529 */       uSkyBlock.getInstance().getLogger().log(Level.SEVERE, "Could not save config to " + this.playerConfigFile, ex);
/* 490:    */     }
/* 491:    */   }
/* 492:    */   
/* 493:    */   public void deleteIslandConfig(String player)
/* 494:    */   {
/* 495:534 */     this.playerConfigFile = new File(uSkyBlock.getInstance().directoryPlayers, player + ".yml");
/* 496:535 */     this.playerConfigFile.delete();
/* 497:    */   }
/* 498:    */ }
