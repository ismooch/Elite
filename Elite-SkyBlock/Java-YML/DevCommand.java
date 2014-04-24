/*   1:    */ package us.talabrek.ultimateskyblock;
/*   2:    */ 
/*   3:    */ import java.io.File;
/*   4:    */ import java.io.PrintStream;
/*   5:    */ import java.util.HashMap;
/*   6:    */ import java.util.List;
/*   7:    */ import org.bukkit.Bukkit;
/*   8:    */ import org.bukkit.ChatColor;
/*   9:    */ import org.bukkit.OfflinePlayer;
/*  10:    */ import org.bukkit.Server;
/*  11:    */ import org.bukkit.command.Command;
/*  12:    */ import org.bukkit.command.CommandExecutor;
/*  13:    */ import org.bukkit.command.CommandSender;
/*  14:    */ import org.bukkit.configuration.file.FileConfiguration;
/*  15:    */ import org.bukkit.entity.Player;
/*  16:    */ import org.bukkit.scheduler.BukkitScheduler;
/*  17:    */ 
/*  18:    */ public class DevCommand
/*  19:    */   implements CommandExecutor
/*  20:    */ {
/*  21:    */   public boolean onCommand(CommandSender sender, Command command, String label, String[] split)
/*  22:    */   {
/*  23: 24 */     if (!(sender instanceof Player)) {
/*  24: 25 */       return false;
/*  25:    */     }
/*  26: 27 */     Player player = (Player)sender;
/*  27: 28 */     if (split.length == 0)
/*  28:    */     {
/*  29: 29 */       if ((VaultHandler.checkPerk(player.getName(), "usb.mod.protect", player.getWorld())) || (VaultHandler.checkPerk(player.getName(), "usb.mod.protectall", player.getWorld())) || 
/*  30: 30 */         (VaultHandler.checkPerk(player.getName(), "usb.mod.topten", player.getWorld())) || (VaultHandler.checkPerk(player.getName(), "usb.mod.orphan", player.getWorld())) || 
/*  31: 31 */         (VaultHandler.checkPerk(player.getName(), "usb.admin.delete", player.getWorld())) || (VaultHandler.checkPerk(player.getName(), "usb.admin.remove", player.getWorld())) || 
/*  32: 32 */         (VaultHandler.checkPerk(player.getName(), "usb.admin.register", player.getWorld())) || (player.isOp()))
/*  33:    */       {
/*  34: 34 */         player.sendMessage("[dev usage]");
/*  35: 35 */         if ((VaultHandler.checkPerk(player.getName(), "usb.mod.protect", player.getWorld())) || (player.isOp())) {
/*  36: 36 */           player.sendMessage(ChatColor.YELLOW + "/dev protect <player>:" + ChatColor.WHITE + " add protection to an island.");
/*  37:    */         }
/*  38: 37 */         if ((VaultHandler.checkPerk(player.getName(), "usb.admin.reload", player.getWorld())) || (player.isOp())) {
/*  39: 38 */           player.sendMessage(ChatColor.YELLOW + "/dev reload:" + ChatColor.WHITE + " reload configuration from file.");
/*  40:    */         }
/*  41: 39 */         if ((VaultHandler.checkPerk(player.getName(), "usb.mod.protectall", player.getWorld())) || (player.isOp())) {
/*  42: 40 */           player.sendMessage(ChatColor.YELLOW + "/dev protectall:" + ChatColor.WHITE + " add island protection to unprotected islands.");
/*  43:    */         }
/*  44: 41 */         if ((VaultHandler.checkPerk(player.getName(), "usb.mod.topten", player.getWorld())) || (player.isOp())) {
/*  45: 42 */           player.sendMessage(ChatColor.YELLOW + "/dev topten:" + ChatColor.WHITE + " manually update the top 10 list");
/*  46:    */         }
/*  47: 43 */         if ((VaultHandler.checkPerk(player.getName(), "usb.mod.orphan", player.getWorld())) || (player.isOp())) {
/*  48: 44 */           player.sendMessage(ChatColor.YELLOW + "/dev orphancount:" + ChatColor.WHITE + " unused island locations count");
/*  49:    */         }
/*  50: 45 */         if ((VaultHandler.checkPerk(player.getName(), "usb.mod.orphan", player.getWorld())) || (player.isOp())) {
/*  51: 46 */           player.sendMessage(ChatColor.YELLOW + "/dev clearorphan:" + ChatColor.WHITE + " remove any unused island locations.");
/*  52:    */         }
/*  53: 47 */         if ((VaultHandler.checkPerk(player.getName(), "usb.mod.orphan", player.getWorld())) || (player.isOp())) {
/*  54: 48 */           player.sendMessage(ChatColor.YELLOW + "/dev saveorphan:" + ChatColor.WHITE + " save the list of old (empty) island locations.");
/*  55:    */         }
/*  56: 49 */         if ((VaultHandler.checkPerk(player.getName(), "usb.admin.delete", player.getWorld())) || (player.isOp())) {
/*  57: 50 */           player.sendMessage(ChatColor.YELLOW + "/dev delete <player>:" + ChatColor.WHITE + " delete an island (removes blocks).");
/*  58:    */         }
/*  59: 51 */         if ((VaultHandler.checkPerk(player.getName(), "usb.admin.remove", player.getWorld())) || (player.isOp())) {
/*  60: 52 */           player.sendMessage(ChatColor.YELLOW + "/dev remove <player>:" + ChatColor.WHITE + " remove a player from an island.");
/*  61:    */         }
/*  62: 53 */         if ((VaultHandler.checkPerk(player.getName(), "usb.admin.register", player.getWorld())) || (player.isOp())) {
/*  63: 54 */           player.sendMessage(ChatColor.YELLOW + "/dev register <player>:" + ChatColor.WHITE + " set a player's island to your location");
/*  64:    */         }
/*  65: 55 */         if ((VaultHandler.checkPerk(player.getName(), "usb.mod.challenges", player.getWorld())) || (player.isOp())) {
/*  66: 56 */           player.sendMessage(ChatColor.YELLOW + "/dev completechallenge <challengename> <player>:" + ChatColor.WHITE + " marks a challenge as complete");
/*  67:    */         }
/*  68: 57 */         if ((VaultHandler.checkPerk(player.getName(), "usb.mod.challenges", player.getWorld())) || (player.isOp())) {
/*  69: 58 */           player.sendMessage(ChatColor.YELLOW + "/dev resetchallenge <challengename> <player>:" + ChatColor.WHITE + " marks a challenge as incomplete");
/*  70:    */         }
/*  71: 59 */         if ((VaultHandler.checkPerk(player.getName(), "usb.mod.challenges", player.getWorld())) || (player.isOp())) {
/*  72: 60 */           player.sendMessage(ChatColor.YELLOW + "/dev resetallchallenges <challengename>:" + ChatColor.WHITE + " resets all of the player's challenges");
/*  73:    */         }
/*  74: 61 */         if ((VaultHandler.checkPerk(player.getName(), "usb.admin.purge", player.getWorld())) || (player.isOp())) {
/*  75: 62 */           player.sendMessage(ChatColor.YELLOW + "/dev purge [TimeInDays]:" + ChatColor.WHITE + " delete inactive islands older than [TimeInDays].");
/*  76:    */         }
/*  77: 63 */         if ((VaultHandler.checkPerk(player.getName(), "usb.mod.party", player.getWorld())) || (player.isOp())) {
/*  78: 64 */           player.sendMessage(ChatColor.YELLOW + "/dev buildpartylist:" + ChatColor.WHITE + " build a new party list (use this if parties are broken).");
/*  79:    */         }
/*  80: 65 */         if ((VaultHandler.checkPerk(player.getName(), "usb.mod.party", player.getWorld())) || (player.isOp())) {
/*  81: 66 */           player.sendMessage(ChatColor.YELLOW + "/dev info <player>:" + ChatColor.WHITE + " check the party information for the given player.");
/*  82:    */         }
/*  83:    */       }
/*  84:    */       else
/*  85:    */       {
/*  86: 68 */         player.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
/*  87:    */       }
/*  88:    */     }
/*  89: 69 */     else if (split.length == 1)
/*  90:    */     {
/*  91: 70 */       if ((split[0].equals("clearorphan")) && ((VaultHandler.checkPerk(player.getName(), "usb.mod.orphan", player.getWorld())) || (player.isOp())))
/*  92:    */       {
/*  93: 72 */         player.sendMessage(ChatColor.YELLOW + "Clearing all old (empty) island locations.");
/*  94: 73 */         uSkyBlock.getInstance().clearOrphanedIsland();
/*  95:    */       }
/*  96: 74 */       else if ((split[0].equals("protectall")) && ((VaultHandler.checkPerk(player.getName(), "usb.mod.protectall", player.getWorld())) || (player.isOp())))
/*  97:    */       {
/*  98: 76 */         player.sendMessage(ChatColor.YELLOW + "This command is only available using WorldGuard.");
/*  99: 77 */         if (Settings.island_protectWithWorldGuard) {
/* 100: 79 */           player.sendMessage(ChatColor.YELLOW + "This command has been disabled.");
/* 101:    */         } else {
/* 102: 82 */           player.sendMessage(ChatColor.RED + "You must enable WorldGuard protection in the config.yml to use this!");
/* 103:    */         }
/* 104:    */       }
/* 105: 83 */       else if ((split[0].equals("buildislandlist")) && ((VaultHandler.checkPerk(player.getName(), "usb.mod.protectall", player.getWorld())) || (player.isOp())))
/* 106:    */       {
/* 107: 85 */         player.sendMessage(ChatColor.YELLOW + "Building island list..");
/* 108: 86 */         uSkyBlock.getInstance().buildIslandList();
/* 109: 87 */         player.sendMessage(ChatColor.YELLOW + "Finished building island list..");
/* 110:    */       }
/* 111: 88 */       else if ((split[0].equals("orphancount")) && ((VaultHandler.checkPerk(player.getName(), "usb.mod.orphan", player.getWorld())) || (player.isOp())))
/* 112:    */       {
/* 113: 90 */         player.sendMessage(ChatColor.YELLOW + uSkyBlock.getInstance().orphanCount() + " old island locations will be used before new ones.");
/* 114:    */       }
/* 115: 91 */       else if ((split[0].equals("reload")) && ((VaultHandler.checkPerk(player.getName(), "usb.admin.reload", player.getWorld())) || (player.isOp())))
/* 116:    */       {
/* 117: 93 */         uSkyBlock.getInstance().reloadConfig();
/* 118: 94 */         uSkyBlock.getInstance().loadPluginConfig();
/* 119: 95 */         uSkyBlock.getInstance().reloadLevelConfig();
/* 120: 96 */         uSkyBlock.getInstance().loadLevelConfig();
/* 121: 97 */         player.sendMessage(ChatColor.YELLOW + "Configuration reloaded from file.");
/* 122:    */       }
/* 123: 98 */       else if ((split[0].equals("saveorphan")) && ((VaultHandler.checkPerk(player.getName(), "usb.mod.orphan", player.getWorld())) || (player.isOp())))
/* 124:    */       {
/* 125:100 */         player.sendMessage(ChatColor.YELLOW + "Saving the orphan list.");
/* 126:101 */         uSkyBlock.getInstance().saveOrphans();
/* 127:    */       }
/* 128:102 */       else if ((split[0].equals("topten")) && ((VaultHandler.checkPerk(player.getName(), "usb.mod.topten", player.getWorld())) || (player.isOp())))
/* 129:    */       {
/* 130:104 */         player.sendMessage(ChatColor.YELLOW + "Generating the Top Ten list");
/* 131:105 */         uSkyBlock.getInstance().updateTopTen(uSkyBlock.getInstance().generateTopTen());
/* 132:106 */         player.sendMessage(ChatColor.YELLOW + "Finished generation of the Top Ten list");
/* 133:    */       }
/* 134:107 */       else if ((split[0].equals("purge")) && ((VaultHandler.checkPerk(player.getName(), "usb.admin.purge", player.getWorld())) || (player.isOp())))
/* 135:    */       {
/* 136:109 */         if (uSkyBlock.getInstance().isPurgeActive())
/* 137:    */         {
/* 138:111 */           player.sendMessage(ChatColor.RED + "A purge is already running, please wait for it to finish!");
/* 139:112 */           return true;
/* 140:    */         }
/* 141:114 */         player.sendMessage(ChatColor.YELLOW + "Usage: /dev purge [TimeInDays]");
/* 142:115 */         return true;
/* 143:    */       }
/* 144:    */     }
/* 145:117 */     else if (split.length == 2)
/* 146:    */     {
/* 147:118 */       if ((split[0].equals("purge")) && ((VaultHandler.checkPerk(player.getName(), "usb.admin.purge", player.getWorld())) || (player.isOp())))
/* 148:    */       {
/* 149:120 */         if (uSkyBlock.getInstance().isPurgeActive())
/* 150:    */         {
/* 151:122 */           player.sendMessage(ChatColor.RED + "A purge is already running, please wait for it to finish!");
/* 152:123 */           return true;
/* 153:    */         }
/* 154:125 */         uSkyBlock.getInstance().activatePurge();
/* 155:126 */         final int time = Integer.parseInt(split[1]) * 24;
/* 156:127 */         player.sendMessage(ChatColor.YELLOW + "Marking all islands inactive for more than " + split[1] + " days.");
/* 157:128 */         uSkyBlock.getInstance().getServer().getScheduler().runTaskAsynchronously(uSkyBlock.getInstance(), new Runnable()
/* 158:    */         {
/* 159:    */           public void run()
/* 160:    */           {
/* 161:132 */             File directoryPlayers = new File(uSkyBlock.getInstance().getDataFolder() + File.separator + "players");
/* 162:    */             
/* 163:134 */             long offlineTime = 0L;
/* 164:136 */             for (File child : directoryPlayers.listFiles()) {
/* 165:138 */               if ((Bukkit.getOfflinePlayer(child.getName()) != null) && (Bukkit.getPlayer(child.getName()) == null))
/* 166:    */               {
/* 167:140 */                 OfflinePlayer oplayer = Bukkit.getOfflinePlayer(child.getName());
/* 168:141 */                 offlineTime = oplayer.getLastPlayed();
/* 169:142 */                 offlineTime = (System.currentTimeMillis() - offlineTime) / 3600000L;
/* 170:143 */                 if ((offlineTime > time) && (uSkyBlock.getInstance().hasIsland(oplayer.getName())))
/* 171:    */                 {
/* 172:145 */                   PlayerInfo pi = new PlayerInfo(oplayer.getName());
/* 173:146 */                   if (pi.getHasIsland()) {
/* 174:148 */                     if (!pi.getHasParty()) {
/* 175:150 */                       if (uSkyBlock.getInstance().getTempIslandConfig(pi.locationForParty()) != null) {
/* 176:152 */                         if (uSkyBlock.getInstance().getTempIslandConfig(pi.locationForParty()).getInt("general.level") < 10) {
/* 177:154 */                           if (child.getName() != null) {
/* 178:155 */                             uSkyBlock.getInstance().addToRemoveList(child.getName());
/* 179:    */                           }
/* 180:    */                         }
/* 181:    */                       }
/* 182:    */                     }
/* 183:    */                   }
/* 184:    */                 }
/* 185:    */               }
/* 186:    */             }
/* 187:161 */             System.out.print("Removing " + uSkyBlock.getInstance().getRemoveList().size() + " inactive islands.");
/* 188:162 */             uSkyBlock.getInstance().getServer().getScheduler().scheduleSyncRepeatingTask(uSkyBlock.getInstance(), new Runnable()
/* 189:    */             {
/* 190:    */               public void run()
/* 191:    */               {
/* 192:165 */                 if ((uSkyBlock.getInstance().getRemoveList().size() > 0) && (uSkyBlock.getInstance().isPurgeActive()))
/* 193:    */                 {
/* 194:167 */                   uSkyBlock.getInstance().deletePlayerIsland((String)uSkyBlock.getInstance().getRemoveList().get(0));
/* 195:168 */                   System.out.print("[uSkyBlock] Purge: Removing " + (String)uSkyBlock.getInstance().getRemoveList().get(0) + "'s island");
/* 196:169 */                   uSkyBlock.getInstance().deleteFromRemoveList();
/* 197:    */                 }
/* 198:172 */                 if ((uSkyBlock.getInstance().getRemoveList().size() == 0) && (uSkyBlock.getInstance().isPurgeActive()))
/* 199:    */                 {
/* 200:174 */                   uSkyBlock.getInstance().deactivatePurge();
/* 201:175 */                   System.out.print("[uSkyBlock] Finished purging marked inactive islands.");
/* 202:    */                 }
/* 203:    */               }
/* 204:179 */             }, 0L, 20L);
/* 205:    */           }
/* 206:    */         });
/* 207:    */       }
/* 208:182 */       else if ((split[0].equals("goto")) && ((VaultHandler.checkPerk(player.getName(), "usb.mod.goto", player.getWorld())) || (player.isOp())))
/* 209:    */       {
/* 210:184 */         PlayerInfo pi = new PlayerInfo(split[1]);
/* 211:185 */         if (!pi.getHasIsland())
/* 212:    */         {
/* 213:186 */           player.sendMessage(ChatColor.RED + "Error: Invalid Player (check spelling)");
/* 214:    */         }
/* 215:    */         else
/* 216:    */         {
/* 217:189 */           if (pi.getHomeLocation() != null)
/* 218:    */           {
/* 219:191 */             player.sendMessage(ChatColor.GREEN + "Teleporting to " + split[1] + "'s island.");
/* 220:192 */             player.teleport(pi.getHomeLocation());
/* 221:193 */             return true;
/* 222:    */           }
/* 223:195 */           if (pi.getIslandLocation() != null)
/* 224:    */           {
/* 225:197 */             player.sendMessage(ChatColor.GREEN + "Teleporting to " + split[1] + "'s island.");
/* 226:198 */             player.teleport(pi.getIslandLocation());
/* 227:199 */             return true;
/* 228:    */           }
/* 229:201 */           player.sendMessage("Error: That player does not have an island!");
/* 230:    */         }
/* 231:    */       }
/* 232:203 */       else if ((split[0].equals("remove")) && ((VaultHandler.checkPerk(player.getName(), "usb.admin.remove", player.getWorld())) || (player.isOp())))
/* 233:    */       {
/* 234:205 */         PlayerInfo pi = new PlayerInfo(split[1]);
/* 235:206 */         if (!pi.getHasIsland())
/* 236:    */         {
/* 237:207 */           player.sendMessage(ChatColor.RED + "Error: Invalid Player (check spelling)");
/* 238:    */         }
/* 239:    */         else
/* 240:    */         {
/* 241:210 */           if (pi.getIslandLocation() != null)
/* 242:    */           {
/* 243:212 */             player.sendMessage(ChatColor.YELLOW + "Removing " + split[1] + "'s island.");
/* 244:213 */             uSkyBlock.getInstance().devDeletePlayerIsland(split[1]);
/* 245:214 */             return true;
/* 246:    */           }
/* 247:216 */           player.sendMessage("Error: That player does not have an island!");
/* 248:    */         }
/* 249:    */       }
/* 250:218 */       else if ((split[0].equals("delete")) && ((VaultHandler.checkPerk(player.getName(), "usb.admin.delete", player.getWorld())) || (player.isOp())))
/* 251:    */       {
/* 252:220 */         PlayerInfo pi = new PlayerInfo(split[1]);
/* 253:221 */         if (!pi.getHasIsland())
/* 254:    */         {
/* 255:222 */           player.sendMessage(ChatColor.RED + "Error: Invalid Player (check spelling)");
/* 256:    */         }
/* 257:    */         else
/* 258:    */         {
/* 259:225 */           if (pi.getIslandLocation() != null)
/* 260:    */           {
/* 261:227 */             player.sendMessage(ChatColor.YELLOW + "Removing " + split[1] + "'s island.");
/* 262:228 */             uSkyBlock.getInstance().deletePlayerIsland(split[1]);
/* 263:229 */             return true;
/* 264:    */           }
/* 265:231 */           player.sendMessage("Error: That player does not have an island!");
/* 266:    */         }
/* 267:    */       }
/* 268:233 */       else if ((split[0].equals("register")) && ((VaultHandler.checkPerk(player.getName(), "usb.admin.register", player.getWorld())) || (player.isOp())))
/* 269:    */       {
/* 270:235 */         PlayerInfo pi = new PlayerInfo(split[1]);
/* 271:236 */         if (pi.getHasIsland()) {
/* 272:237 */           uSkyBlock.getInstance().devDeletePlayerIsland(split[1]);
/* 273:    */         }
/* 274:238 */         if (uSkyBlock.getInstance().devSetPlayerIsland(player, player.getLocation(), split[1])) {
/* 275:240 */           player.sendMessage(ChatColor.GREEN + "Set " + split[1] + "'s island to the bedrock nearest you.");
/* 276:    */         } else {
/* 277:242 */           player.sendMessage(ChatColor.RED + "Bedrock not found: unable to set the island!");
/* 278:    */         }
/* 279:    */       }
/* 280:243 */       else if ((!split[0].equals("info")) || ((!VaultHandler.checkPerk(player.getName(), "usb.mod.party", player.getWorld())) && (!player.isOp())))
/* 281:    */       {
/* 282:244 */         if ((split[0].equals("resetallchallenges")) && ((VaultHandler.checkPerk(player.getName(), "usb.mod.challenges", player.getWorld())) || (player.isOp())))
/* 283:    */         {
/* 284:246 */           if (!uSkyBlock.getInstance().getActivePlayers().containsKey(split[1]))
/* 285:    */           {
/* 286:248 */             PlayerInfo pi = new PlayerInfo(split[1]);
/* 287:249 */             if (!pi.getHasIsland())
/* 288:    */             {
/* 289:250 */               player.sendMessage(ChatColor.RED + "Error: Invalid Player (check spelling)");
/* 290:251 */               return true;
/* 291:    */             }
/* 292:253 */             pi.resetAllChallenges();
/* 293:254 */             pi.savePlayerConfig(split[1]);
/* 294:255 */             player.sendMessage(ChatColor.YELLOW + split[1] + " has had all challenges reset.");
/* 295:    */           }
/* 296:    */           else
/* 297:    */           {
/* 298:258 */             ((PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(split[1])).resetAllChallenges();
/* 299:259 */             player.sendMessage(ChatColor.YELLOW + split[1] + " has had all challenges reset.");
/* 300:    */           }
/* 301:    */         }
/* 302:261 */         else if ((split[0].equals("setbiome")) && ((VaultHandler.checkPerk(player.getName(), "usb.mod.setbiome", player.getWorld())) || (player.isOp()))) {
/* 303:263 */           if (!uSkyBlock.getInstance().getActivePlayers().containsKey(split[1]))
/* 304:    */           {
/* 305:265 */             PlayerInfo pi = new PlayerInfo(split[1]);
/* 306:266 */             if (!pi.getHasIsland())
/* 307:    */             {
/* 308:267 */               player.sendMessage(ChatColor.RED + "Error: Invalid Player (check spelling)");
/* 309:268 */               return true;
/* 310:    */             }
/* 311:270 */             uSkyBlock.getInstance().setBiome(pi.getIslandLocation(), "OCEAN");
/* 312:271 */             pi.savePlayerConfig(split[1]);
/* 313:272 */             player.sendMessage(ChatColor.YELLOW + split[1] + " has had their biome changed to OCEAN.");
/* 314:    */           }
/* 315:    */           else
/* 316:    */           {
/* 317:275 */             uSkyBlock.getInstance().setBiome(((PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(split[1])).getIslandLocation(), "OCEAN");
/* 318:276 */             player.sendMessage(ChatColor.YELLOW + split[1] + " has had their biome changed to OCEAN.");
/* 319:    */           }
/* 320:    */         }
/* 321:    */       }
/* 322:    */     }
/* 323:279 */     else if (split.length == 3) {
/* 324:281 */       if ((split[0].equals("completechallenge")) && ((VaultHandler.checkPerk(player.getName(), "usb.mod.challenges", player.getWorld())) || (player.isOp())))
/* 325:    */       {
/* 326:283 */         if (!uSkyBlock.getInstance().getActivePlayers().containsKey(split[2]))
/* 327:    */         {
/* 328:285 */           PlayerInfo pi = new PlayerInfo(split[2]);
/* 329:286 */           if (!pi.getHasIsland())
/* 330:    */           {
/* 331:287 */             player.sendMessage(ChatColor.RED + "Error: Invalid Player (check spelling)");
/* 332:288 */             return true;
/* 333:    */           }
/* 334:290 */           if ((pi.checkChallenge(split[1].toLowerCase()) > 0) || (!pi.challengeExists(split[1].toLowerCase())))
/* 335:    */           {
/* 336:292 */             player.sendMessage(ChatColor.RED + "Challenge doesn't exist or is already completed");
/* 337:293 */             return true;
/* 338:    */           }
/* 339:295 */           pi.completeChallenge(split[1].toLowerCase());
/* 340:296 */           pi.savePlayerConfig(split[2]);
/* 341:297 */           player.sendMessage(ChatColor.YELLOW + "challange: " + split[1].toLowerCase() + " has been completed for " + split[2]);
/* 342:    */         }
/* 343:    */         else
/* 344:    */         {
/* 345:300 */           if ((((PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(split[2])).checkChallenge(split[1].toLowerCase()) > 0) || (!((PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(split[2])).challengeExists(split[1].toLowerCase())))
/* 346:    */           {
/* 347:302 */             player.sendMessage(ChatColor.RED + "Challenge doesn't exist or is already completed");
/* 348:303 */             return true;
/* 349:    */           }
/* 350:305 */           ((PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(split[2])).completeChallenge(split[1].toLowerCase());
/* 351:306 */           player.sendMessage(ChatColor.YELLOW + "challange: " + split[1].toLowerCase() + " has been completed for " + split[2]);
/* 352:    */         }
/* 353:    */       }
/* 354:308 */       else if ((split[0].equals("resetchallenge")) && ((VaultHandler.checkPerk(player.getName(), "usb.mod.challenges", player.getWorld())) || (player.isOp())))
/* 355:    */       {
/* 356:310 */         if (!uSkyBlock.getInstance().getActivePlayers().containsKey(split[2]))
/* 357:    */         {
/* 358:312 */           PlayerInfo pi = new PlayerInfo(split[2]);
/* 359:313 */           if (!pi.getHasIsland())
/* 360:    */           {
/* 361:314 */             player.sendMessage(ChatColor.RED + "Error: Invalid Player (check spelling)");
/* 362:315 */             return true;
/* 363:    */           }
/* 364:317 */           if ((pi.checkChallenge(split[1].toLowerCase()) == 0) || (!pi.challengeExists(split[1].toLowerCase())))
/* 365:    */           {
/* 366:319 */             player.sendMessage(ChatColor.RED + "Challenge doesn't exist or isn't yet completed");
/* 367:320 */             return true;
/* 368:    */           }
/* 369:322 */           pi.resetChallenge(split[1].toLowerCase());
/* 370:323 */           pi.savePlayerConfig(split[2]);
/* 371:324 */           player.sendMessage(ChatColor.YELLOW + "challange: " + split[1].toLowerCase() + " has been reset for " + split[2]);
/* 372:    */         }
/* 373:    */         else
/* 374:    */         {
/* 375:327 */           if ((((PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(split[2])).checkChallenge(split[1].toLowerCase()) == 0) || (!((PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(split[2])).challengeExists(split[1].toLowerCase())))
/* 376:    */           {
/* 377:329 */             player.sendMessage(ChatColor.RED + "Challenge doesn't exist or isn't yet completed");
/* 378:330 */             return true;
/* 379:    */           }
/* 380:332 */           ((PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(split[2])).resetChallenge(split[1].toLowerCase());
/* 381:333 */           player.sendMessage(ChatColor.YELLOW + "challange: " + split[1].toLowerCase() + " has been completed for " + split[2]);
/* 382:    */         }
/* 383:    */       }
/* 384:335 */       else if ((split[0].equals("setbiome")) && ((VaultHandler.checkPerk(player.getName(), "usb.mod.setbiome", player.getWorld())) || (player.isOp()))) {
/* 385:337 */         if (!uSkyBlock.getInstance().getActivePlayers().containsKey(split[1]))
/* 386:    */         {
/* 387:339 */           PlayerInfo pi = new PlayerInfo(split[1]);
/* 388:340 */           if (!pi.getHasIsland())
/* 389:    */           {
/* 390:341 */             player.sendMessage(ChatColor.RED + "Error: Invalid Player (check spelling)");
/* 391:342 */             return true;
/* 392:    */           }
/* 393:344 */           if (uSkyBlock.getInstance().setBiome(pi.getIslandLocation(), split[2])) {
/* 394:346 */             player.sendMessage(ChatColor.YELLOW + split[1] + " has had their biome changed to " + split[2].toUpperCase() + ".");
/* 395:    */           } else {
/* 396:348 */             player.sendMessage(ChatColor.YELLOW + split[1] + " has had their biome changed to OCEAN.");
/* 397:    */           }
/* 398:349 */           pi.savePlayerConfig(split[1]);
/* 399:    */         }
/* 400:352 */         else if (uSkyBlock.getInstance().setBiome(((PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(split[1])).getIslandLocation(), split[2]))
/* 401:    */         {
/* 402:354 */           player.sendMessage(ChatColor.YELLOW + split[1] + " has had their biome changed to " + split[2].toUpperCase() + ".");
/* 403:    */         }
/* 404:    */         else
/* 405:    */         {
/* 406:356 */           player.sendMessage(ChatColor.YELLOW + split[1] + " has had their biome changed to OCEAN.");
/* 407:    */         }
/* 408:    */       }
/* 409:    */     }
/* 410:360 */     return true;
/* 411:    */   }
/* 412:    */ }
