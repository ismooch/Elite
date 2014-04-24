/*    1:     */ package us.talabrek.ultimateskyblock;
/*    2:     */ 
/*    3:     */ import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
/*    4:     */ import com.sk89q.worldguard.protection.managers.RegionManager;
/*    5:     */ import java.io.PrintStream;
/*    6:     */ import java.util.HashMap;
/*    7:     */ import java.util.Iterator;
/*    8:     */ import java.util.List;
/*    9:     */ import java.util.Map;
/*   10:     */ import java.util.Map.Entry;
/*   11:     */ import java.util.Set;
/*   12:     */ import org.bukkit.Bukkit;
/*   13:     */ import org.bukkit.ChatColor;
/*   14:     */ import org.bukkit.Location;
/*   15:     */ import org.bukkit.OfflinePlayer;
/*   16:     */ import org.bukkit.Server;
/*   17:     */ import org.bukkit.World;
/*   18:     */ import org.bukkit.command.Command;
/*   19:     */ import org.bukkit.command.CommandExecutor;
/*   20:     */ import org.bukkit.command.CommandSender;
/*   21:     */ import org.bukkit.configuration.ConfigurationSection;
/*   22:     */ import org.bukkit.configuration.file.FileConfiguration;
/*   23:     */ import org.bukkit.entity.Player;
/*   24:     */ import org.bukkit.inventory.EntityEquipment;
/*   25:     */ import org.bukkit.inventory.PlayerInventory;
/*   26:     */ import org.bukkit.plugin.PluginManager;
/*   27:     */ import org.bukkit.scheduler.BukkitScheduler;
/*   28:     */ 
/*   29:     */ public class IslandCommand
/*   30:     */   implements CommandExecutor
/*   31:     */ {
/*   32:     */   public Location Islandlocation;
/*   33:     */   private List<String> banList;
/*   34:     */   private String tempTargetPlayer;
/*   35:  29 */   public boolean allowInfo = true;
/*   36:  30 */   Set<String> memberList = null;
/*   37:  31 */   private HashMap<String, String> inviteList = new HashMap();
/*   38:     */   String tPlayer;
/*   39:     */   
/*   40:     */   public IslandCommand()
/*   41:     */   {
/*   42:  35 */     this.inviteList.put("NoInvited", "NoInviter");
/*   43:     */   }
/*   44:     */   
/*   45:     */   public boolean onCommand(CommandSender sender, Command command, String label, String[] split)
/*   46:     */   {
/*   47:  39 */     if (!(sender instanceof Player)) {
/*   48:  40 */       return false;
/*   49:     */     }
/*   50:  42 */     Player player = (Player)sender;
/*   51:     */     
/*   52:     */ 
/*   53:     */ 
/*   54:     */ 
/*   55:     */ 
/*   56:     */ 
/*   57:     */ 
/*   58:  50 */     PlayerInfo pi = (PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(player.getName());
/*   59:  51 */     if (pi == null)
/*   60:     */     {
/*   61:  53 */       player.sendMessage(ChatColor.RED + "Error: Couldn't read your player data!");
/*   62:  54 */       return true;
/*   63:     */     }
/*   64:  56 */     String iName = "";
/*   65:  57 */     if (pi.getIslandLocation() != null) {
/*   66:  58 */       iName = pi.locationForParty();
/*   67:     */     }
/*   68:  59 */     if (split.length == 0)
/*   69:     */     {
/*   70:  61 */       uSkyBlock.getInstance().updatePartyNumber(player);
/*   71:  62 */       player.openInventory(uSkyBlock.getInstance().displayIslandGUI(player));
/*   72:     */       
/*   73:     */ 
/*   74:     */ 
/*   75:     */ 
/*   76:     */ 
/*   77:     */ 
/*   78:  69 */       return true;
/*   79:     */     }
/*   80:  70 */     if (split.length == 1)
/*   81:     */     {
/*   82:  72 */       if (((split[0].equals("restart")) || (split[0].equals("reset"))) && (pi.getIslandLocation() != null))
/*   83:     */       {
/*   84:  74 */         if (uSkyBlock.getInstance().getIslandConfig(iName).getInt("party.currentSize") > 1)
/*   85:     */         {
/*   86:  76 */           if (!uSkyBlock.getInstance().getIslandConfig(iName).getString("party.leader").equalsIgnoreCase(player.getName())) {
/*   87:  77 */             player.sendMessage(ChatColor.RED + "Only the owner may restart this island. Leave this island in order to start your own (/island leave).");
/*   88:     */           } else {
/*   89:  79 */             player.sendMessage(ChatColor.YELLOW + "You must remove all players from your island before you can restart it (/island kick <player>). See a list of players currently part of your island using /island party.");
/*   90:     */           }
/*   91:  80 */           return true;
/*   92:     */         }
/*   93:  82 */         if ((!uSkyBlock.getInstance().onRestartCooldown(player)) || (Settings.general_cooldownRestart == 0))
/*   94:     */         {
/*   95:  86 */           uSkyBlock.getInstance().restartPlayerIsland(player, pi.getIslandLocation());
/*   96:  87 */           uSkyBlock.getInstance().setRestartCooldown(player);
/*   97:  88 */           return true;
/*   98:     */         }
/*   99:  91 */         player.sendMessage(ChatColor.YELLOW + "You can restart your island in " + uSkyBlock.getInstance().getRestartCooldownTime(player) / 1000L + " seconds.");
/*  100:  92 */         return true;
/*  101:     */       }
/*  102:  94 */       if (((split[0].equals("sethome")) || (split[0].equals("tpset"))) && (pi.getIslandLocation() != null) && (VaultHandler.checkPerk(player.getName(), "usb.island.sethome", player.getWorld())))
/*  103:     */       {
/*  104:  95 */         uSkyBlock.getInstance().homeSet(player);
/*  105:  96 */         return true;
/*  106:     */       }
/*  107:  97 */       if (((split[0].equals("log")) || (split[0].equals("l"))) && (pi.getIslandLocation() != null) && (VaultHandler.checkPerk(player.getName(), "usb.island.create", player.getWorld())))
/*  108:     */       {
/*  109:  98 */         player.openInventory(uSkyBlock.getInstance().displayLogGUI(player));
/*  110:  99 */         return true;
/*  111:     */       }
/*  112: 100 */       if (((split[0].equals("create")) || (split[0].equals("c"))) && (VaultHandler.checkPerk(player.getName(), "usb.island.create", player.getWorld())))
/*  113:     */       {
/*  114: 101 */         if (pi.getIslandLocation() == null)
/*  115:     */         {
/*  116: 103 */           uSkyBlock.getInstance().createIsland(sender, pi);
/*  117: 104 */           return true;
/*  118:     */         }
/*  119: 106 */         if ((pi.getIslandLocation().getBlockX() == 0) && (pi.getIslandLocation().getBlockY() == 0) && (pi.getIslandLocation().getBlockZ() == 0))
/*  120:     */         {
/*  121: 108 */           uSkyBlock.getInstance().createIsland(sender, pi);
/*  122: 109 */           return true;
/*  123:     */         }
/*  124: 111 */         return true;
/*  125:     */       }
/*  126: 112 */       if (((split[0].equals("home")) || (split[0].equals("h"))) && (pi.getIslandLocation() != null) && (VaultHandler.checkPerk(player.getName(), "usb.island.sethome", player.getWorld())))
/*  127:     */       {
/*  128: 113 */         if (pi.getHomeLocation() == null) {
/*  129: 115 */           ((PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(player.getName())).setHomeLocation(pi.getIslandLocation());
/*  130:     */         }
/*  131: 117 */         uSkyBlock.getInstance().homeTeleport(player);
/*  132: 118 */         return true;
/*  133:     */       }
/*  134: 119 */       if (((split[0].equals("setwarp")) || (split[0].equals("warpset"))) && (pi.getIslandLocation() != null) && (VaultHandler.checkPerk(player.getName(), "usb.extra.addwarp", player.getWorld())))
/*  135:     */       {
/*  136: 120 */         if (uSkyBlock.getInstance().getIslandConfig(iName).getBoolean("party.members." + player.getName() + ".canChangeWarp"))
/*  137:     */         {
/*  138: 122 */           uSkyBlock.getInstance().sendMessageToIslandGroup(iName, player.getName() + " changed the island warp location.");
/*  139: 123 */           uSkyBlock.getInstance().warpSet(player);
/*  140:     */         }
/*  141:     */         else
/*  142:     */         {
/*  143: 126 */           player.sendMessage("Â§cYou do not have permission to set your island's warp point!");
/*  144:     */         }
/*  145: 128 */         return true;
/*  146:     */       }
/*  147: 129 */       if ((split[0].equals("warp")) || (split[0].equals("w")))
/*  148:     */       {
/*  149: 131 */         if (VaultHandler.checkPerk(player.getName(), "usb.extra.addwarp", player.getWorld()))
/*  150:     */         {
/*  151: 133 */           if (uSkyBlock.getInstance().getIslandConfig(iName).getBoolean("general.warpActive")) {
/*  152: 135 */             player.sendMessage(ChatColor.GREEN + "Your incoming warp is active, players may warp to your island.");
/*  153:     */           } else {
/*  154: 138 */             player.sendMessage(ChatColor.RED + "Your incoming warp is inactive, players may not warp to your island.");
/*  155:     */           }
/*  156: 140 */           player.sendMessage(ChatColor.WHITE + "Set incoming warp to your current location using " + ChatColor.YELLOW + "/island setwarp");
/*  157: 141 */           player.sendMessage(ChatColor.WHITE + "Toggle your warp on/off using " + ChatColor.YELLOW + "/island togglewarp");
/*  158:     */         }
/*  159:     */         else
/*  160:     */         {
/*  161: 144 */           player.sendMessage(ChatColor.RED + "You do not have permission to create a warp on your island!");
/*  162:     */         }
/*  163: 146 */         if (VaultHandler.checkPerk(player.getName(), "usb.island.warp", player.getWorld())) {
/*  164: 148 */           player.sendMessage(ChatColor.WHITE + "Warp to another island using " + ChatColor.YELLOW + "/island warp <player>");
/*  165:     */         } else {
/*  166: 151 */           player.sendMessage(ChatColor.RED + "You do not have permission to warp to other islands!");
/*  167:     */         }
/*  168: 153 */         return true;
/*  169:     */       }
/*  170: 154 */       if (((split[0].equals("togglewarp")) || (split[0].equals("tw"))) && (pi.getIslandLocation() != null))
/*  171:     */       {
/*  172: 156 */         if (VaultHandler.checkPerk(player.getName(), "usb.extra.addwarp", player.getWorld()))
/*  173:     */         {
/*  174: 158 */           if (uSkyBlock.getInstance().getIslandConfig(iName).getBoolean("party.members." + player.getName() + ".canToggleWarp"))
/*  175:     */           {
/*  176: 160 */             if (!uSkyBlock.getInstance().getIslandConfig(iName).getBoolean("general.warpActive"))
/*  177:     */             {
/*  178: 162 */               if (uSkyBlock.getInstance().getIslandConfig(iName).getBoolean("general.locked"))
/*  179:     */               {
/*  180: 164 */                 player.sendMessage(ChatColor.RED + "Your island is locked. You must unlock it before enabling your warp.");
/*  181: 165 */                 return true;
/*  182:     */               }
/*  183: 167 */               uSkyBlock.getInstance().sendMessageToIslandGroup(iName, player.getName() + " activated the island warp.");
/*  184: 168 */               uSkyBlock.getInstance().getIslandConfig(iName).set("general.warpActive", Boolean.valueOf(true));
/*  185:     */             }
/*  186:     */             else
/*  187:     */             {
/*  188: 172 */               uSkyBlock.getInstance().sendMessageToIslandGroup(iName, player.getName() + " deactivated the island warp.");
/*  189: 173 */               uSkyBlock.getInstance().getIslandConfig(iName).set("general.warpActive", Boolean.valueOf(false));
/*  190:     */             }
/*  191:     */           }
/*  192:     */           else {
/*  193: 178 */             player.sendMessage("Â§cYou do not have permission to enable/disable your island's warp!");
/*  194:     */           }
/*  195:     */         }
/*  196:     */         else {
/*  197: 183 */           player.sendMessage(ChatColor.RED + "You do not have permission to create a warp on your island!");
/*  198:     */         }
/*  199: 185 */         uSkyBlock.getInstance().getActivePlayers().put(player.getName(), pi);
/*  200: 186 */         return true;
/*  201:     */       }
/*  202: 187 */       if (((split[0].equals("ban")) || (split[0].equals("banned")) || (split[0].equals("banlist")) || (split[0].equals("b"))) && (pi.getIslandLocation() != null))
/*  203:     */       {
/*  204: 189 */         if (VaultHandler.checkPerk(player.getName(), "usb.island.ban", player.getWorld()))
/*  205:     */         {
/*  206: 191 */           player.sendMessage(ChatColor.YELLOW + "The following players are banned from warping to your island:");
/*  207: 192 */           player.sendMessage(ChatColor.RED + getBanList(player));
/*  208: 193 */           player.sendMessage(ChatColor.YELLOW + "To ban/unban from your island, use /island ban <player>");
/*  209:     */         }
/*  210:     */         else
/*  211:     */         {
/*  212: 196 */           player.sendMessage(ChatColor.RED + "You do not have permission to ban players from your island!");
/*  213:     */         }
/*  214: 198 */         return true;
/*  215:     */       }
/*  216: 199 */       if ((split[0].equals("lock")) && (pi.getIslandLocation() != null))
/*  217:     */       {
/*  218: 200 */         if ((Settings.island_allowIslandLock) && (VaultHandler.checkPerk(player.getName(), "usb.lock", player.getWorld())))
/*  219:     */         {
/*  220: 202 */           if (uSkyBlock.getInstance().getIslandConfig(iName).getBoolean("party.members." + player.getName() + ".canToggleLock"))
/*  221:     */           {
/*  222: 204 */             WorldGuardHandler.islandLock(sender, uSkyBlock.getInstance().getIslandConfig(iName).getString("party.leader"));
/*  223: 205 */             uSkyBlock.getInstance().getIslandConfig(iName).set("general.locked", Boolean.valueOf(true));
/*  224: 206 */             uSkyBlock.getInstance().sendMessageToIslandGroup(iName, player.getName() + " locked the island.");
/*  225: 207 */             if (uSkyBlock.getInstance().getIslandConfig(iName).getBoolean("general.warpActive"))
/*  226:     */             {
/*  227: 209 */               uSkyBlock.getInstance().getIslandConfig(iName).set("general.warpActive", Boolean.valueOf(false));
/*  228: 210 */               player.sendMessage(ChatColor.RED + "Since your island is locked, your incoming warp has been deactivated.");
/*  229: 211 */               uSkyBlock.getInstance().sendMessageToIslandGroup(iName, player.getName() + " deactivated the island warp.");
/*  230:     */             }
/*  231: 213 */             uSkyBlock.getInstance().getActivePlayers().put(player.getName(), pi);
/*  232: 214 */             uSkyBlock.getInstance().saveIslandConfig(iName);
/*  233:     */           }
/*  234:     */           else
/*  235:     */           {
/*  236: 217 */             player.sendMessage(ChatColor.RED + "You do not have permission to lock your island!");
/*  237:     */           }
/*  238:     */         }
/*  239:     */         else {
/*  240: 221 */           player.sendMessage(ChatColor.RED + "You don't have access to this command!");
/*  241:     */         }
/*  242: 222 */         return true;
/*  243:     */       }
/*  244: 223 */       if ((split[0].equals("unlock")) && (pi.getIslandLocation() != null))
/*  245:     */       {
/*  246: 224 */         if ((Settings.island_allowIslandLock) && (VaultHandler.checkPerk(player.getName(), "usb.lock", player.getWorld())))
/*  247:     */         {
/*  248: 225 */           if (uSkyBlock.getInstance().getIslandConfig(iName).getBoolean("party.members." + player.getName() + ".canToggleLock"))
/*  249:     */           {
/*  250: 227 */             WorldGuardHandler.islandUnlock(sender, uSkyBlock.getInstance().getIslandConfig(iName).getString("party.leader"));
/*  251: 228 */             uSkyBlock.getInstance().getIslandConfig(iName).set("general.locked", Boolean.valueOf(false));
/*  252: 229 */             uSkyBlock.getInstance().sendMessageToIslandGroup(iName, player.getName() + " unlocked the island.");
/*  253: 230 */             uSkyBlock.getInstance().saveIslandConfig(iName);
/*  254:     */           }
/*  255:     */           else
/*  256:     */           {
/*  257: 233 */             player.sendMessage(ChatColor.RED + "You do not have permission to unlock your island!");
/*  258:     */           }
/*  259:     */         }
/*  260:     */         else {
/*  261: 236 */           player.sendMessage(ChatColor.RED + "You don't have access to this command!");
/*  262:     */         }
/*  263: 237 */         return true;
/*  264:     */       }
/*  265: 238 */       if (split[0].equals("help"))
/*  266:     */       {
/*  267: 239 */         player.sendMessage(ChatColor.GREEN + "[SkyBlock command usage]");
/*  268:     */         
/*  269: 241 */         player.sendMessage(ChatColor.YELLOW + "/island :" + ChatColor.WHITE + " start your island, or teleport back to one you have.");
/*  270: 242 */         player.sendMessage(ChatColor.YELLOW + "/island restart :" + ChatColor.WHITE + " delete your island and start a new one.");
/*  271: 243 */         player.sendMessage(ChatColor.YELLOW + "/island sethome :" + ChatColor.WHITE + " set your island teleport point.");
/*  272: 244 */         if (Settings.island_useIslandLevel)
/*  273:     */         {
/*  274: 246 */           player.sendMessage(ChatColor.YELLOW + "/island level :" + ChatColor.WHITE + " check your island level");
/*  275: 247 */           player.sendMessage(ChatColor.YELLOW + "/island level <player> :" + ChatColor.WHITE + " check another player's island level.");
/*  276:     */         }
/*  277: 249 */         if (VaultHandler.checkPerk(player.getName(), "usb.party.create", player.getWorld()))
/*  278:     */         {
/*  279: 251 */           player.sendMessage(ChatColor.YELLOW + "/island party :" + ChatColor.WHITE + " view your party information.");
/*  280: 252 */           player.sendMessage(ChatColor.YELLOW + "/island invite <player>:" + ChatColor.WHITE + " invite a player to join your island.");
/*  281: 253 */           player.sendMessage(ChatColor.YELLOW + "/island leave :" + ChatColor.WHITE + " leave another player's island.");
/*  282:     */         }
/*  283: 255 */         if (VaultHandler.checkPerk(player.getName(), "usb.party.kick", player.getWorld())) {
/*  284: 257 */           player.sendMessage(ChatColor.YELLOW + "/island kick <player>:" + ChatColor.WHITE + " remove a player from your island.");
/*  285:     */         }
/*  286: 259 */         if (VaultHandler.checkPerk(player.getName(), "usb.party.join", player.getWorld())) {
/*  287: 261 */           player.sendMessage(ChatColor.YELLOW + "/island [accept/reject]:" + ChatColor.WHITE + " accept/reject an invitation.");
/*  288:     */         }
/*  289: 263 */         if (VaultHandler.checkPerk(player.getName(), "usb.party.makeleader", player.getWorld())) {
/*  290: 265 */           player.sendMessage(ChatColor.YELLOW + "/island makeleader <player>:" + ChatColor.WHITE + " transfer the island to <player>.");
/*  291:     */         }
/*  292: 267 */         if (VaultHandler.checkPerk(player.getName(), "usb.island.warp", player.getWorld())) {
/*  293: 269 */           player.sendMessage(ChatColor.YELLOW + "/island warp <player> :" + ChatColor.WHITE + " warp to another player's island.");
/*  294:     */         }
/*  295: 271 */         if (VaultHandler.checkPerk(player.getName(), "usb.extra.addwarp", player.getWorld()))
/*  296:     */         {
/*  297: 273 */           player.sendMessage(ChatColor.YELLOW + "/island setwarp :" + ChatColor.WHITE + " set your island's warp location.");
/*  298: 274 */           player.sendMessage(ChatColor.YELLOW + "/island togglewarp :" + ChatColor.WHITE + " enable/disable warping to your island.");
/*  299:     */         }
/*  300: 276 */         if (VaultHandler.checkPerk(player.getName(), "usb.island.ban", player.getWorld())) {
/*  301: 278 */           player.sendMessage(ChatColor.YELLOW + "/island ban <player> :" + ChatColor.WHITE + " ban/unban a player from your island.");
/*  302:     */         }
/*  303: 280 */         player.sendMessage(ChatColor.YELLOW + "/island top :" + ChatColor.WHITE + " see the top ranked islands.");
/*  304: 281 */         if (Settings.island_allowIslandLock) {
/*  305: 283 */           if (!VaultHandler.checkPerk(player.getName(), "usb.lock", player.getWorld()))
/*  306:     */           {
/*  307: 285 */             player.sendMessage(ChatColor.DARK_GRAY + "/island lock :" + ChatColor.GRAY + " non-group members can't enter your island.");
/*  308: 286 */             player.sendMessage(ChatColor.DARK_GRAY + "/island unlock :" + ChatColor.GRAY + " allow anyone to enter your island.");
/*  309:     */           }
/*  310:     */           else
/*  311:     */           {
/*  312: 289 */             player.sendMessage(ChatColor.YELLOW + "/island lock :" + ChatColor.WHITE + " non-group members can't enter your island.");
/*  313: 290 */             player.sendMessage(ChatColor.YELLOW + "/island unlock :" + ChatColor.WHITE + " allow anyone to enter your island.");
/*  314:     */           }
/*  315:     */         }
/*  316: 295 */         if (Bukkit.getServer().getServerId().equalsIgnoreCase("UltimateSkyblock"))
/*  317:     */         {
/*  318: 297 */           player.sendMessage(ChatColor.YELLOW + "/dungeon :" + ChatColor.WHITE + " to warp to the dungeon world.");
/*  319: 298 */           player.sendMessage(ChatColor.YELLOW + "/fun :" + ChatColor.WHITE + " to warp to the Mini-Game/Fun world.");
/*  320: 299 */           player.sendMessage(ChatColor.YELLOW + "/pvp :" + ChatColor.WHITE + " join a pvp match.");
/*  321: 300 */           player.sendMessage(ChatColor.YELLOW + "/spleef :" + ChatColor.WHITE + " join spleef match.");
/*  322: 301 */           player.sendMessage(ChatColor.YELLOW + "/hub :" + ChatColor.WHITE + " warp to the world hub Sanconia.");
/*  323:     */         }
/*  324: 303 */         return true;
/*  325:     */       }
/*  326: 304 */       if ((split[0].equals("top")) && (VaultHandler.checkPerk(player.getName(), "usb.island.topten", player.getWorld())))
/*  327:     */       {
/*  328: 305 */         uSkyBlock.getInstance().displayTopTen(player);
/*  329: 306 */         return true;
/*  330:     */       }
/*  331: 307 */       if (((split[0].equals("biome")) || (split[0].equals("b"))) && (pi.getIslandLocation() != null))
/*  332:     */       {
/*  333: 310 */         player.openInventory(uSkyBlock.getInstance().displayBiomeGUI(player));
/*  334: 311 */         if (!uSkyBlock.getInstance().getIslandConfig(iName).getBoolean("party.members." + player.getName() + ".canToggleLock")) {
/*  335: 313 */           player.sendMessage("Â§cYou do not have permission to change the biome of your current island.");
/*  336:     */         }
/*  337: 315 */         return true;
/*  338:     */       }
/*  339: 317 */       if (((split[0].equals("info")) || (split[0].equals("level"))) && (pi.getIslandLocation() != null) && (VaultHandler.checkPerk(player.getName(), "usb.island.info", player.getWorld())) && (Settings.island_useIslandLevel))
/*  340:     */       {
/*  341: 319 */         if (uSkyBlock.getInstance().playerIsOnIsland(player))
/*  342:     */         {
/*  343: 321 */           if ((!uSkyBlock.getInstance().onInfoCooldown(player)) || (Settings.general_cooldownInfo == 0))
/*  344:     */           {
/*  345: 323 */             uSkyBlock.getInstance().setInfoCooldown(player);
/*  346: 324 */             if ((!pi.getHasParty()) && (!pi.getHasIsland()))
/*  347:     */             {
/*  348: 326 */               player.sendMessage(ChatColor.RED + "You do not have an island!");
/*  349:     */             }
/*  350:     */             else
/*  351:     */             {
/*  352: 329 */               for (int x = Settings.island_protectionRange / 2 * -1 - 16; x <= Settings.island_protectionRange / 2 + 16; x += 16) {
/*  353: 330 */                 for (int z = Settings.island_protectionRange / 2 * -1 - 16; z <= Settings.island_protectionRange / 2 + 16; z += 16) {
/*  354: 331 */                   uSkyBlock.getSkyBlockWorld().loadChunk((pi.getIslandLocation().getBlockX() + x) / 16, (pi.getIslandLocation().getBlockZ() + z) / 16);
/*  355:     */                 }
/*  356:     */               }
/*  357: 334 */               getIslandLevel(player, player.getName());
/*  358:     */             }
/*  359: 336 */             return true;
/*  360:     */           }
/*  361: 339 */           player.sendMessage(ChatColor.YELLOW + "You can use that command again in " + uSkyBlock.getInstance().getInfoCooldownTime(player) / 1000L + " seconds.");
/*  362: 340 */           return true;
/*  363:     */         }
/*  364: 344 */         player.sendMessage(ChatColor.YELLOW + "You must be on your island to use this command.");
/*  365: 345 */         return true;
/*  366:     */       }
/*  367: 347 */       if ((split[0].equals("invite")) && (pi.getIslandLocation() != null) && (VaultHandler.checkPerk(player.getName(), "usb.party.create", player.getWorld())))
/*  368:     */       {
/*  369: 349 */         player.sendMessage(ChatColor.YELLOW + "Use" + ChatColor.WHITE + " /island invite <playername>" + ChatColor.YELLOW + " to invite a player to your island.");
/*  370: 350 */         if (uSkyBlock.getInstance().hasParty(player.getName()))
/*  371:     */         {
/*  372: 352 */           if ((uSkyBlock.getInstance().getIslandConfig(pi.locationForParty()).getString("party.leader").equalsIgnoreCase(player.getName())) || (uSkyBlock.getInstance().getIslandConfig(pi.locationForParty()).getBoolean("party.members." + player.getName() + ".canInviteOthers")))
/*  373:     */           {
/*  374: 354 */             if (VaultHandler.checkPerk(player.getName(), "usb.extra.partysize", player.getWorld()))
/*  375:     */             {
/*  376: 356 */               if (uSkyBlock.getInstance().getIslandConfig(pi.locationForParty()).getInt("party.currentSize") < Settings.general_maxPartySize * 2) {
/*  377: 358 */                 player.sendMessage(ChatColor.GREEN + "You can invite " + (Settings.general_maxPartySize * 2 - uSkyBlock.getInstance().getIslandConfig(pi.locationForParty()).getInt("party.currentSize")) + " more players.");
/*  378:     */               } else {
/*  379: 360 */                 player.sendMessage(ChatColor.RED + "You can't invite any more players.");
/*  380:     */               }
/*  381: 361 */               return true;
/*  382:     */             }
/*  383: 364 */             if (uSkyBlock.getInstance().getIslandConfig(pi.locationForParty()).getInt("party.currentSize") < Settings.general_maxPartySize) {
/*  384: 366 */               player.sendMessage(ChatColor.GREEN + "You can invite " + (Settings.general_maxPartySize - uSkyBlock.getInstance().getIslandConfig(pi.locationForParty()).getInt("party.currentSize")) + " more players.");
/*  385:     */             } else {
/*  386: 368 */               player.sendMessage(ChatColor.RED + "You can't invite any more players.");
/*  387:     */             }
/*  388: 369 */             return true;
/*  389:     */           }
/*  390: 373 */           player.sendMessage(ChatColor.RED + "Only the island's owner can invite!");
/*  391: 374 */           return true;
/*  392:     */         }
/*  393: 377 */         return true;
/*  394:     */       }
/*  395: 378 */       if ((split[0].equals("accept")) && (VaultHandler.checkPerk(player.getName(), "usb.party.join", player.getWorld())))
/*  396:     */       {
/*  397: 381 */         if ((uSkyBlock.getInstance().onRestartCooldown(player)) && (Settings.general_cooldownRestart > 0))
/*  398:     */         {
/*  399: 383 */           player.sendMessage(ChatColor.YELLOW + "You can't join an island for another " + uSkyBlock.getInstance().getRestartCooldownTime(player) / 1000L + " seconds.");
/*  400: 384 */           return true;
/*  401:     */         }
/*  402: 386 */         if ((!uSkyBlock.getInstance().hasParty(player.getName())) && (this.inviteList.containsKey(player.getName())))
/*  403:     */         {
/*  404: 388 */           if (pi.getHasIsland()) {
/*  405: 390 */             uSkyBlock.getInstance().deletePlayerIsland(player.getName());
/*  406:     */           }
/*  407: 391 */           player.sendMessage(ChatColor.GREEN + "You have joined an island! Use /island party to see the other members.");
/*  408: 392 */           addPlayertoParty(player.getName(), (String)this.inviteList.get(player.getName()));
/*  409: 393 */           if (Bukkit.getPlayer((String)this.inviteList.get(player.getName())) != null)
/*  410:     */           {
/*  411: 395 */             Bukkit.getPlayer((String)this.inviteList.get(player.getName())).sendMessage(ChatColor.GREEN + player.getName() + " has joined your island!");
/*  412:     */           }
/*  413:     */           else
/*  414:     */           {
/*  415: 399 */             player.sendMessage(ChatColor.RED + "You couldn't join the island, maybe it's full.");
/*  416:     */             
/*  417: 401 */             return true;
/*  418:     */           }
/*  419: 403 */           uSkyBlock.getInstance().setRestartCooldown(player);
/*  420:     */           
/*  421: 405 */           uSkyBlock.getInstance().homeTeleport(player);
/*  422:     */           
/*  423: 407 */           player.getInventory().clear();
/*  424: 408 */           player.getEquipment().clear();
/*  425: 412 */           if ((Settings.island_protectWithWorldGuard) && (Bukkit.getServer().getPluginManager().isPluginEnabled("WorldGuard"))) {
/*  426: 414 */             if (WorldGuardHandler.getWorldGuard().getRegionManager(uSkyBlock.getSkyBlockWorld()).hasRegion(uSkyBlock.getInstance().getIslandConfig(((PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(this.inviteList.get(player.getName()))).locationForParty()).getString("party.leader") + "Island")) {
/*  427: 416 */               WorldGuardHandler.addPlayerToOldRegion(uSkyBlock.getInstance().getIslandConfig(((PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(this.inviteList.get(player.getName()))).locationForParty()).getString("party.leader"), player.getName());
/*  428:     */             }
/*  429:     */           }
/*  430: 419 */           this.inviteList.remove(player.getName());
/*  431: 420 */           return true;
/*  432:     */         }
/*  433: 423 */         player.sendMessage(ChatColor.RED + "You can't use that command right now.");
/*  434: 424 */         return true;
/*  435:     */       }
/*  436: 427 */       if (split[0].equals("reject"))
/*  437:     */       {
/*  438: 429 */         if (this.inviteList.containsKey(player.getName()))
/*  439:     */         {
/*  440: 431 */           player.sendMessage(ChatColor.YELLOW + "You have rejected the invitation to join an island.");
/*  441: 432 */           if (Bukkit.getPlayer((String)this.inviteList.get(player.getName())) != null) {
/*  442: 434 */             Bukkit.getPlayer((String)this.inviteList.get(player.getName())).sendMessage(ChatColor.RED + player.getName() + " has rejected your island invite!");
/*  443:     */           }
/*  444: 435 */           this.inviteList.remove(player.getName());
/*  445:     */         }
/*  446:     */         else
/*  447:     */         {
/*  448: 437 */           player.sendMessage(ChatColor.RED + "You haven't been invited.");
/*  449:     */         }
/*  450: 438 */         return true;
/*  451:     */       }
/*  452: 443 */       if (split[0].equalsIgnoreCase("partypurge"))
/*  453:     */       {
/*  454: 445 */         if (VaultHandler.checkPerk(player.getName(), "usb.mod.party", player.getWorld())) {
/*  455: 447 */           player.sendMessage(ChatColor.RED + "This command no longer functions!");
/*  456:     */         } else {
/*  457: 449 */           player.sendMessage(ChatColor.RED + "You can't access that command!");
/*  458:     */         }
/*  459: 450 */         return true;
/*  460:     */       }
/*  461: 451 */       if (split[0].equalsIgnoreCase("partyclean"))
/*  462:     */       {
/*  463: 453 */         if (VaultHandler.checkPerk(player.getName(), "usb.mod.party", player.getWorld())) {
/*  464: 455 */           player.sendMessage(ChatColor.RED + "This command no longer functions!");
/*  465:     */         } else {
/*  466: 457 */           player.sendMessage(ChatColor.RED + "You can't access that command!");
/*  467:     */         }
/*  468: 458 */         return true;
/*  469:     */       }
/*  470: 459 */       if (split[0].equalsIgnoreCase("purgeinvites"))
/*  471:     */       {
/*  472: 461 */         if (VaultHandler.checkPerk(player.getName(), "usb.mod.party", player.getWorld()))
/*  473:     */         {
/*  474: 463 */           player.sendMessage(ChatColor.RED + "Deleting all invites!");
/*  475: 464 */           invitePurge();
/*  476:     */         }
/*  477:     */         else
/*  478:     */         {
/*  479: 466 */           player.sendMessage(ChatColor.RED + "You can't access that command!");
/*  480:     */         }
/*  481: 467 */         return true;
/*  482:     */       }
/*  483: 468 */       if (split[0].equalsIgnoreCase("partylist"))
/*  484:     */       {
/*  485: 470 */         if (VaultHandler.checkPerk(player.getName(), "usb.mod.party", player.getWorld())) {
/*  486: 472 */           player.sendMessage(ChatColor.RED + "This command is currently not active.");
/*  487:     */         } else {
/*  488: 475 */           player.sendMessage(ChatColor.RED + "You can't access that command!");
/*  489:     */         }
/*  490: 476 */         return true;
/*  491:     */       }
/*  492: 477 */       if (split[0].equalsIgnoreCase("invitelist"))
/*  493:     */       {
/*  494: 479 */         if (VaultHandler.checkPerk(player.getName(), "usb.mod.party", player.getWorld()))
/*  495:     */         {
/*  496: 481 */           player.sendMessage(ChatColor.RED + "Checking Invites.");
/*  497: 482 */           inviteDebug(player);
/*  498:     */         }
/*  499:     */         else
/*  500:     */         {
/*  501: 484 */           player.sendMessage(ChatColor.RED + "You can't access that command!");
/*  502:     */         }
/*  503: 485 */         return true;
/*  504:     */       }
/*  505: 489 */       if ((split[0].equals("leave")) && (pi.getIslandLocation() != null) && (VaultHandler.checkPerk(player.getName(), "usb.party.join", player.getWorld())))
/*  506:     */       {
/*  507: 491 */         if (player.getWorld().getName().equalsIgnoreCase(uSkyBlock.getSkyBlockWorld().getName()))
/*  508:     */         {
/*  509: 493 */           if (uSkyBlock.getInstance().hasParty(player.getName()))
/*  510:     */           {
/*  511: 495 */             if (uSkyBlock.getInstance().getIslandConfig(iName).getString("party.leader").equalsIgnoreCase(player.getName()))
/*  512:     */             {
/*  513: 497 */               player.sendMessage(ChatColor.YELLOW + "You own this island, use /island remove <player> instead.");
/*  514: 498 */               return true;
/*  515:     */             }
/*  516: 503 */             player.getInventory().clear();
/*  517: 504 */             player.getEquipment().clear();
/*  518: 505 */             if (Settings.extras_sendToSpawn) {
/*  519: 506 */               player.performCommand("spawn");
/*  520:     */             } else {
/*  521: 508 */               player.teleport(uSkyBlock.getSkyBlockWorld().getSpawnLocation());
/*  522:     */             }
/*  523: 510 */             if ((Settings.island_protectWithWorldGuard) && (Bukkit.getServer().getPluginManager().isPluginEnabled("WorldGuard"))) {
/*  524: 512 */               WorldGuardHandler.removePlayerFromRegion(uSkyBlock.getInstance().getIslandConfig(pi.locationForParty()).getString("party.leader"), player.getName());
/*  525:     */             }
/*  526: 514 */             removePlayerFromParty(player.getName(), uSkyBlock.getInstance().getIslandConfig(pi.locationForParty()).getString("party.leader"), pi.locationForParty());
/*  527:     */             
/*  528: 516 */             player.sendMessage(ChatColor.YELLOW + "You have left the island and returned to the player spawn.");
/*  529: 517 */             if (Bukkit.getPlayer(uSkyBlock.getInstance().getIslandConfig(pi.locationForParty()).getString("party.leader")) != null) {
/*  530: 518 */               Bukkit.getPlayer(uSkyBlock.getInstance().getIslandConfig(pi.locationForParty()).getString("party.leader")).sendMessage(ChatColor.RED + player.getName() + " has left your island!");
/*  531:     */             }
/*  532:     */           }
/*  533:     */           else
/*  534:     */           {
/*  535: 523 */             player.sendMessage(ChatColor.RED + "You can't leave your island if you are the only person. Try using /island restart if you want a new one!");
/*  536: 524 */             return true;
/*  537:     */           }
/*  538:     */         }
/*  539:     */         else {
/*  540: 528 */           player.sendMessage(ChatColor.RED + "You must be in the skyblock world to leave your party!");
/*  541:     */         }
/*  542: 530 */         return true;
/*  543:     */       }
/*  544: 531 */       if ((split[0].equals("party")) && (pi.getIslandLocation() != null))
/*  545:     */       {
/*  546: 533 */         if (VaultHandler.checkPerk(player.getName(), "usb.party.create", player.getWorld())) {
/*  547: 534 */           player.openInventory(uSkyBlock.getInstance().displayPartyGUI(player));
/*  548:     */         }
/*  549: 561 */         player.sendMessage(ChatColor.YELLOW + "Listing your island members:");
/*  550: 562 */         String total = "";
/*  551: 563 */         this.memberList = uSkyBlock.getInstance().getIslandConfig(pi.locationForParty()).getConfigurationSection("party.members").getKeys(false);
/*  552: 564 */         total = total + "Â§a<" + uSkyBlock.getInstance().getIslandConfig(pi.locationForParty()).getString("party.leader") + "> ";
/*  553: 565 */         Iterator<String> tempIt = this.memberList.iterator();
/*  554: 566 */         while (tempIt.hasNext())
/*  555:     */         {
/*  556: 568 */           String temp = (String)tempIt.next();
/*  557: 569 */           if (!temp.equalsIgnoreCase(uSkyBlock.getInstance().getIslandConfig(pi.locationForParty()).getString("party.leader"))) {
/*  558: 571 */             total = total + "Â§e[" + temp + "]";
/*  559:     */           }
/*  560:     */         }
/*  561: 574 */         player.sendMessage(total);
/*  562:     */         
/*  563:     */ 
/*  564: 577 */         return true;
/*  565:     */       }
/*  566:     */     }
/*  567: 579 */     else if (split.length == 2)
/*  568:     */     {
/*  569: 580 */       if (((split[0].equals("info")) || (split[0].equals("level"))) && (pi.getIslandLocation() != null) && (VaultHandler.checkPerk(player.getName(), "usb.island.info", player.getWorld())) && (Settings.island_useIslandLevel))
/*  570:     */       {
/*  571: 582 */         if ((!uSkyBlock.getInstance().onInfoCooldown(player)) || (Settings.general_cooldownInfo == 0))
/*  572:     */         {
/*  573: 584 */           uSkyBlock.getInstance().setInfoCooldown(player);
/*  574: 585 */           if ((!pi.getHasParty()) && (!pi.getHasIsland())) {
/*  575: 587 */             player.sendMessage(ChatColor.RED + "You do not have an island!");
/*  576:     */           } else {
/*  577: 589 */             getIslandLevel(player, split[1]);
/*  578:     */           }
/*  579: 590 */           return true;
/*  580:     */         }
/*  581: 593 */         player.sendMessage(ChatColor.YELLOW + "You can use that command again in " + uSkyBlock.getInstance().getInfoCooldownTime(player) / 1000L + " seconds.");
/*  582: 594 */         return true;
/*  583:     */       }
/*  584: 596 */       if ((split[0].equals("warp")) || (split[0].equals("w")))
/*  585:     */       {
/*  586: 598 */         if (VaultHandler.checkPerk(player.getName(), "usb.island.warp", player.getWorld()))
/*  587:     */         {
/*  588: 600 */           PlayerInfo wPi = null;
/*  589: 601 */           if (!uSkyBlock.getInstance().getActivePlayers().containsKey(Bukkit.getPlayer(split[1])))
/*  590:     */           {
/*  591: 603 */             if (!uSkyBlock.getInstance().getActivePlayers().containsKey(split[1]))
/*  592:     */             {
/*  593: 605 */               wPi = new PlayerInfo(split[1]);
/*  594: 606 */               if (!wPi.getHasIsland())
/*  595:     */               {
/*  596: 607 */                 player.sendMessage(ChatColor.RED + "That player does not exist!");
/*  597: 608 */                 return true;
/*  598:     */               }
/*  599:     */             }
/*  600:     */             else
/*  601:     */             {
/*  602: 612 */               wPi = (PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(split[1]);
/*  603:     */             }
/*  604:     */           }
/*  605:     */           else {
/*  606: 616 */             wPi = (PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(Bukkit.getPlayer(split[1]));
/*  607:     */           }
/*  608: 618 */           if (!wPi.getHasIsland()) {
/*  609: 619 */             return true;
/*  610:     */           }
/*  611: 620 */           if (uSkyBlock.getInstance().getIslandConfig(wPi.locationForParty()).getBoolean("general.warpActive"))
/*  612:     */           {
/*  613: 622 */             if (!uSkyBlock.getInstance().getIslandConfig(wPi.locationForParty()).contains("banned.list." + player.getName())) {
/*  614: 624 */               uSkyBlock.getInstance().warpTeleport(player, wPi);
/*  615:     */             } else {
/*  616: 627 */               player.sendMessage(ChatColor.RED + "That player has forbidden you from warping to their island.");
/*  617:     */             }
/*  618:     */           }
/*  619:     */           else
/*  620:     */           {
/*  621: 631 */             player.sendMessage(ChatColor.RED + "That player does not have an active warp.");
/*  622: 632 */             return true;
/*  623:     */           }
/*  624:     */         }
/*  625:     */         else
/*  626:     */         {
/*  627: 636 */           player.sendMessage(ChatColor.RED + "You do not have permission to warp to other islands!");
/*  628:     */         }
/*  629: 638 */         return true;
/*  630:     */       }
/*  631: 639 */       if ((split[0].equals("ban")) && (pi.getIslandLocation() != null))
/*  632:     */       {
/*  633: 641 */         if (VaultHandler.checkPerk(player.getName(), "usb.island.ban", player.getWorld()))
/*  634:     */         {
/*  635: 643 */           if (!uSkyBlock.getInstance().getIslandConfig(pi.locationForParty()).contains("banned.list." + player.getName()))
/*  636:     */           {
/*  637: 645 */             this.banList = uSkyBlock.getInstance().getIslandConfig(pi.locationForParty()).getStringList("banned.list");
/*  638: 646 */             this.banList.add(split[1]);
/*  639: 647 */             uSkyBlock.getInstance().getIslandConfig(pi.locationForParty()).set("banned.list", this.banList);
/*  640: 648 */             player.sendMessage(ChatColor.YELLOW + "You have banned " + ChatColor.RED + split[1] + ChatColor.YELLOW + " from warping to your island.");
/*  641:     */           }
/*  642:     */           else
/*  643:     */           {
/*  644: 651 */             this.banList = uSkyBlock.getInstance().getIslandConfig(pi.locationForParty()).getStringList("banned.list");
/*  645: 652 */             this.banList.remove(split[1]);
/*  646: 653 */             uSkyBlock.getInstance().getIslandConfig(pi.locationForParty()).set("banned.list", this.banList);
/*  647: 654 */             player.sendMessage(ChatColor.YELLOW + "You have unbanned " + ChatColor.GREEN + split[1] + ChatColor.YELLOW + " from warping to your island.");
/*  648:     */           }
/*  649:     */         }
/*  650:     */         else {
/*  651: 658 */           player.sendMessage(ChatColor.RED + "You do not have permission to ban players from this island!");
/*  652:     */         }
/*  653: 660 */         uSkyBlock.getInstance().getActivePlayers().put(player.getName(), pi);
/*  654: 661 */         return true;
/*  655:     */       }
/*  656: 662 */       if (((split[0].equals("biome")) || (split[0].equals("b"))) && (pi.getIslandLocation() != null))
/*  657:     */       {
/*  658: 664 */         if (uSkyBlock.getInstance().getIslandConfig(iName).getBoolean("party.members." + player.getName() + ".canChangeBiome"))
/*  659:     */         {
/*  660: 666 */           if ((!uSkyBlock.getInstance().onBiomeCooldown(player)) || (Settings.general_biomeChange == 0))
/*  661:     */           {
/*  662: 668 */             if (uSkyBlock.getInstance().playerIsOnIsland(player))
/*  663:     */             {
/*  664: 670 */               if (uSkyBlock.getInstance().changePlayerBiome(player, split[1]))
/*  665:     */               {
/*  666: 672 */                 player.sendMessage(ChatColor.GREEN + "You have changed your island's biome to " + split[1].toUpperCase());
/*  667: 673 */                 player.sendMessage(ChatColor.GREEN + "You may need to relog to see the changes.");
/*  668: 674 */                 uSkyBlock.getInstance().sendMessageToIslandGroup(iName, player.getName() + " changed the island biome to " + split[1].toUpperCase());
/*  669: 675 */                 uSkyBlock.getInstance().setBiomeCooldown(player);
/*  670:     */               }
/*  671:     */               else
/*  672:     */               {
/*  673: 678 */                 player.sendMessage(ChatColor.GREEN + "Unknown biome name, changing your biome to OCEAN");
/*  674: 679 */                 player.sendMessage(ChatColor.GREEN + "You may need to relog to see the changes.");
/*  675: 680 */                 uSkyBlock.getInstance().sendMessageToIslandGroup(iName, player.getName() + " changed the island biome to OCEAN");
/*  676:     */               }
/*  677:     */             }
/*  678:     */             else {
/*  679: 684 */               player.sendMessage(ChatColor.YELLOW + "You must be on your island to change the biome!");
/*  680:     */             }
/*  681:     */           }
/*  682:     */           else
/*  683:     */           {
/*  684: 687 */             player.sendMessage(ChatColor.YELLOW + "You can change your biome again in " + uSkyBlock.getInstance().getBiomeCooldownTime(player) / 1000L / 60L + " minutes.");
/*  685: 688 */             return true;
/*  686:     */           }
/*  687:     */         }
/*  688:     */         else {
/*  689: 692 */           player.sendMessage(ChatColor.RED + "You do not have permission to change the biome of this island!");
/*  690:     */         }
/*  691: 695 */         return true;
/*  692:     */       }
/*  693: 696 */       if ((split[0].equalsIgnoreCase("invite")) && (pi.getIslandLocation() != null) && (VaultHandler.checkPerk(player.getName(), "usb.party.create", player.getWorld())))
/*  694:     */       {
/*  695: 703 */         if (!uSkyBlock.getInstance().getIslandConfig(iName).getBoolean("party.members." + player.getName() + ".canInviteOthers"))
/*  696:     */         {
/*  697: 705 */           player.sendMessage(ChatColor.RED + "You do not have permission to invite others to this island!");
/*  698: 706 */           return true;
/*  699:     */         }
/*  700: 708 */         if (Bukkit.getPlayer(split[1]) == null)
/*  701:     */         {
/*  702: 710 */           player.sendMessage(ChatColor.RED + "That player is offline or doesn't exist.");
/*  703: 711 */           return true;
/*  704:     */         }
/*  705: 713 */         if (!Bukkit.getPlayer(split[1]).isOnline())
/*  706:     */         {
/*  707: 715 */           player.sendMessage(ChatColor.RED + "That player is offline or doesn't exist.");
/*  708: 716 */           return true;
/*  709:     */         }
/*  710: 718 */         if (!uSkyBlock.getInstance().hasIsland(player.getName()))
/*  711:     */         {
/*  712: 720 */           player.sendMessage(ChatColor.RED + "You must have an island in order to invite people to it!");
/*  713: 721 */           return true;
/*  714:     */         }
/*  715: 723 */         if (player.getName().equalsIgnoreCase(Bukkit.getPlayer(split[1]).getName()))
/*  716:     */         {
/*  717: 725 */           player.sendMessage(ChatColor.RED + "You can't invite yourself!");
/*  718: 726 */           return true;
/*  719:     */         }
/*  720: 728 */         if (uSkyBlock.getInstance().hasParty(player.getName()))
/*  721:     */         {
/*  722: 730 */           if (!uSkyBlock.getInstance().getIslandConfig(pi.locationForParty()).getString("party.leader").equalsIgnoreCase(Bukkit.getPlayer(split[1]).getName()))
/*  723:     */           {
/*  724: 732 */             if (!uSkyBlock.getInstance().hasParty(Bukkit.getPlayer(split[1]).getName()))
/*  725:     */             {
/*  726: 734 */               if (uSkyBlock.getInstance().getIslandConfig(pi.locationForParty()).getInt("party.currentSize") < uSkyBlock.getInstance().getIslandConfig(pi.locationForParty()).getInt("party.maxSize"))
/*  727:     */               {
/*  728: 736 */                 if (this.inviteList.containsValue(player.getName()))
/*  729:     */                 {
/*  730: 738 */                   this.inviteList.remove(getKeyByValue(this.inviteList, player.getName()));
/*  731: 739 */                   player.sendMessage(ChatColor.YELLOW + "Removing your previous invite.");
/*  732:     */                 }
/*  733: 741 */                 this.inviteList.put(Bukkit.getPlayer(split[1]).getName(), player.getName());
/*  734: 742 */                 player.sendMessage(ChatColor.GREEN + "Invite sent to " + Bukkit.getPlayer(split[1]).getName());
/*  735:     */                 
/*  736: 744 */                 Bukkit.getPlayer(split[1]).sendMessage(player.getName() + " has invited you to join their island!");
/*  737: 745 */                 Bukkit.getPlayer(split[1]).sendMessage(ChatColor.WHITE + "/island [accept/reject]" + ChatColor.YELLOW + " to accept or reject the invite.");
/*  738: 746 */                 Bukkit.getPlayer(split[1]).sendMessage(ChatColor.RED + "WARNING: You will lose your current island if you accept!");
/*  739: 747 */                 uSkyBlock.getInstance().sendMessageToIslandGroup(iName, player.getName() + " invited " + Bukkit.getPlayer(split[1]).getName() + " to the island group.");
/*  740:     */               }
/*  741:     */               else
/*  742:     */               {
/*  743: 749 */                 player.sendMessage(ChatColor.RED + "Your island is full, you can't invite anyone else.");
/*  744:     */               }
/*  745:     */             }
/*  746:     */             else {
/*  747: 751 */               player.sendMessage(ChatColor.RED + "That player is already with a group on an island.");
/*  748:     */             }
/*  749:     */           }
/*  750:     */           else {
/*  751: 753 */             player.sendMessage(ChatColor.RED + "That player is the leader of your island!");
/*  752:     */           }
/*  753:     */         }
/*  754:     */         else
/*  755:     */         {
/*  756: 754 */           if (!uSkyBlock.getInstance().hasParty(player.getName()))
/*  757:     */           {
/*  758: 756 */             if (!uSkyBlock.getInstance().hasParty(Bukkit.getPlayer(split[1]).getName()))
/*  759:     */             {
/*  760: 758 */               if (this.inviteList.containsValue(player.getName()))
/*  761:     */               {
/*  762: 760 */                 this.inviteList.remove(getKeyByValue(this.inviteList, player.getName()));
/*  763: 761 */                 player.sendMessage(ChatColor.YELLOW + "Removing your previous invite.");
/*  764:     */               }
/*  765: 763 */               this.inviteList.put(Bukkit.getPlayer(split[1]).getName(), player.getName());
/*  766:     */               
/*  767:     */ 
/*  768: 766 */               player.sendMessage(ChatColor.GREEN + "Invite sent to " + Bukkit.getPlayer(split[1]).getName());
/*  769: 767 */               Bukkit.getPlayer(split[1]).sendMessage(player.getName() + " has invited you to join their island!");
/*  770: 768 */               Bukkit.getPlayer(split[1]).sendMessage(ChatColor.WHITE + "/island [accept/reject]" + ChatColor.YELLOW + " to accept or reject the invite.");
/*  771: 769 */               Bukkit.getPlayer(split[1]).sendMessage(ChatColor.RED + "WARNING: You will lose your current island if you accept!");
/*  772:     */             }
/*  773:     */             else
/*  774:     */             {
/*  775: 771 */               player.sendMessage(ChatColor.RED + "That player is already with a group on an island.");
/*  776:     */             }
/*  777: 772 */             return true;
/*  778:     */           }
/*  779: 775 */           player.sendMessage(ChatColor.RED + "Only the island's owner may invite new players!");
/*  780: 776 */           return true;
/*  781:     */         }
/*  782: 778 */         return true;
/*  783:     */       }
/*  784: 779 */       if (((split[0].equalsIgnoreCase("remove")) || (split[0].equalsIgnoreCase("kick"))) && (pi.getIslandLocation() != null) && (VaultHandler.checkPerk(player.getName(), "usb.party.kick", player.getWorld())))
/*  785:     */       {
/*  786: 782 */         if (!uSkyBlock.getInstance().getIslandConfig(iName).getBoolean("party.members." + player.getName() + ".canKickOthers"))
/*  787:     */         {
/*  788: 784 */           player.sendMessage(ChatColor.RED + "You do not have permission to kick others from this island!");
/*  789: 785 */           return true;
/*  790:     */         }
/*  791: 787 */         if ((Bukkit.getPlayer(split[1]) == null) && (Bukkit.getOfflinePlayer(split[1]) == null))
/*  792:     */         {
/*  793: 789 */           player.sendMessage(ChatColor.RED + "That player doesn't exist.");
/*  794: 790 */           return true;
/*  795:     */         }
/*  796: 792 */         if (Bukkit.getPlayer(split[1]) == null) {
/*  797: 794 */           this.tempTargetPlayer = Bukkit.getOfflinePlayer(split[1]).getName();
/*  798:     */         } else {
/*  799: 797 */           this.tempTargetPlayer = Bukkit.getPlayer(split[1]).getName();
/*  800:     */         }
/*  801: 799 */         if (uSkyBlock.getInstance().getIslandConfig(pi.locationForParty()).contains("party.members." + split[1])) {
/*  802: 801 */           this.tempTargetPlayer = split[1];
/*  803:     */         }
/*  804: 803 */         if (uSkyBlock.getInstance().hasParty(player.getName()))
/*  805:     */         {
/*  806: 805 */           if (!uSkyBlock.getInstance().getIslandConfig(pi.locationForParty()).getString("party.leader").equalsIgnoreCase(this.tempTargetPlayer))
/*  807:     */           {
/*  808: 807 */             if (uSkyBlock.getInstance().getIslandConfig(pi.locationForParty()).contains("party.members." + this.tempTargetPlayer))
/*  809:     */             {
/*  810: 809 */               if (player.getName().equalsIgnoreCase(this.tempTargetPlayer))
/*  811:     */               {
/*  812: 811 */                 player.sendMessage(ChatColor.RED + "Stop kickin' yourself!");
/*  813: 812 */                 return true;
/*  814:     */               }
/*  815: 814 */               if (Bukkit.getPlayer(split[1]) != null)
/*  816:     */               {
/*  817: 816 */                 if (Bukkit.getPlayer(split[1]).getWorld().getName().equalsIgnoreCase(uSkyBlock.getSkyBlockWorld().getName()))
/*  818:     */                 {
/*  819: 818 */                   Bukkit.getPlayer(split[1]).getInventory().clear();
/*  820: 819 */                   Bukkit.getPlayer(split[1]).getEquipment().clear();
/*  821: 820 */                   Bukkit.getPlayer(split[1]).sendMessage(ChatColor.RED + player.getName() + " has removed you from their island!");
/*  822:     */                 }
/*  823: 822 */                 if (Settings.extras_sendToSpawn) {
/*  824: 823 */                   Bukkit.getPlayer(split[1]).performCommand("spawn");
/*  825:     */                 } else {
/*  826: 825 */                   Bukkit.getPlayer(split[1]).teleport(uSkyBlock.getSkyBlockWorld().getSpawnLocation());
/*  827:     */                 }
/*  828:     */               }
/*  829: 830 */               if (Bukkit.getPlayer(uSkyBlock.getInstance().getIslandConfig(pi.locationForParty()).getString("party.leader")) != null) {
/*  830: 831 */                 Bukkit.getPlayer(uSkyBlock.getInstance().getIslandConfig(pi.locationForParty()).getString("party.leader")).sendMessage(ChatColor.RED + this.tempTargetPlayer + " has been removed from the island.");
/*  831:     */               }
/*  832: 832 */               removePlayerFromParty(this.tempTargetPlayer, uSkyBlock.getInstance().getIslandConfig(pi.locationForParty()).getString("party.leader"), pi.locationForParty());
/*  833: 836 */               if ((Settings.island_protectWithWorldGuard) && (Bukkit.getServer().getPluginManager().isPluginEnabled("WorldGuard")))
/*  834:     */               {
/*  835: 838 */                 System.out.println("Removing from " + uSkyBlock.getInstance().getIslandConfig(((PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(player.getName())).locationForParty()).getString("party.leader") + "'s Island");
/*  836: 839 */                 WorldGuardHandler.removePlayerFromRegion(uSkyBlock.getInstance().getIslandConfig(((PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(player.getName())).locationForParty()).getString("party.leader"), this.tempTargetPlayer);
/*  837:     */               }
/*  838:     */             }
/*  839:     */             else
/*  840:     */             {
/*  841: 843 */               System.out.print("Player " + player.getName() + " failed to remove " + this.tempTargetPlayer);
/*  842: 844 */               player.sendMessage(ChatColor.RED + "That player is not part of your island group!");
/*  843:     */             }
/*  844:     */           }
/*  845:     */           else {
/*  846: 847 */             player.sendMessage(ChatColor.RED + "You can't remove the leader from the Island!");
/*  847:     */           }
/*  848:     */         }
/*  849:     */         else {
/*  850: 849 */           player.sendMessage(ChatColor.RED + "No one else is on your island, are you seeing things?");
/*  851:     */         }
/*  852: 850 */         return true;
/*  853:     */       }
/*  854:     */     }
/*  855: 955 */     return true;
/*  856:     */   }
/*  857:     */   
/*  858:     */   private void inviteDebug(Player player)
/*  859:     */   {
/*  860: 978 */     player.sendMessage(this.inviteList.toString());
/*  861:     */   }
/*  862:     */   
/*  863:     */   private void invitePurge()
/*  864:     */   {
/*  865: 983 */     this.inviteList.clear();
/*  866: 984 */     this.inviteList.put("NoInviter", "NoInvited");
/*  867:     */   }
/*  868:     */   
/*  869:     */   public boolean addPlayertoParty(String playername, String partyleader)
/*  870:     */   {
/*  871:1006 */     if (!uSkyBlock.getInstance().getActivePlayers().containsKey(playername))
/*  872:     */     {
/*  873:1008 */       System.out.print("Failed to add player to party! (" + playername + ")");
/*  874:1009 */       return false;
/*  875:     */     }
/*  876:1011 */     if (!uSkyBlock.getInstance().getActivePlayers().containsKey(partyleader))
/*  877:     */     {
/*  878:1013 */       System.out.print("Failed to add player to party! (" + playername + ")");
/*  879:1014 */       return false;
/*  880:     */     }
/*  881:1017 */     ((PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(playername)).setJoinParty(((PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(partyleader)).getIslandLocation());
/*  882:1018 */     if (!playername.equalsIgnoreCase(partyleader))
/*  883:     */     {
/*  884:1020 */       if (((PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(partyleader)).getHomeLocation() != null) {
/*  885:1021 */         ((PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(playername)).setHomeLocation(((PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(partyleader)).getHomeLocation());
/*  886:     */       } else {
/*  887:1024 */         ((PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(playername)).setHomeLocation(((PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(partyleader)).getIslandLocation());
/*  888:     */       }
/*  889:1027 */       uSkyBlock.getInstance().setupPartyMember(((PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(partyleader)).locationForParty(), playername);
/*  890:     */     }
/*  891:1030 */     ((PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(playername)).savePlayerConfig(playername);
/*  892:1031 */     uSkyBlock.getInstance().sendMessageToIslandGroup(((PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(partyleader)).locationForParty(), playername + " has joined your island group.");
/*  893:     */     
/*  894:     */ 
/*  895:     */ 
/*  896:1035 */     return true;
/*  897:     */   }
/*  898:     */   
/*  899:     */   public void removePlayerFromParty(String playername, String partyleader, String location)
/*  900:     */   {
/*  901:1042 */     if (uSkyBlock.getInstance().getActivePlayers().containsKey(playername))
/*  902:     */     {
/*  903:1044 */       uSkyBlock.getInstance().getIslandConfig(location).set("party.members." + playername, null);
/*  904:1045 */       uSkyBlock.getInstance().getIslandConfig(location).set("party.currentSize", Integer.valueOf(uSkyBlock.getInstance().getIslandConfig(location).getInt("party.currentSize") - 1));
/*  905:1046 */       uSkyBlock.getInstance().saveIslandConfig(location);
/*  906:1047 */       uSkyBlock.getInstance().sendMessageToIslandGroup(location, playername + " has been removed from the island group.");
/*  907:1048 */       ((PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(playername)).setHomeLocation(null);
/*  908:1049 */       ((PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(playername)).setLeaveParty();
/*  909:1050 */       ((PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(playername)).savePlayerConfig(playername);
/*  910:     */     }
/*  911:     */     else
/*  912:     */     {
/*  913:1053 */       PlayerInfo pi = new PlayerInfo(playername);
/*  914:     */       
/*  915:1055 */       uSkyBlock.getInstance().getIslandConfig(location).set("party.members." + playername, null);
/*  916:1056 */       uSkyBlock.getInstance().getIslandConfig(location).set("party.currentSize", Integer.valueOf(uSkyBlock.getInstance().getIslandConfig(location).getInt("party.currentSize") - 1));
/*  917:1057 */       uSkyBlock.getInstance().saveIslandConfig(location);
/*  918:1058 */       uSkyBlock.getInstance().sendMessageToIslandGroup(location, playername + " has been removed from the island group.");
/*  919:1059 */       pi.setHomeLocation(null);
/*  920:1060 */       pi.setLeaveParty();
/*  921:1061 */       pi.savePlayerConfig(playername);
/*  922:     */     }
/*  923:     */   }
/*  924:     */   
/*  925:     */   public <T, E> T getKeyByValue(Map<T, E> map, E value)
/*  926:     */   {
/*  927:1066 */     for (Map.Entry<T, E> entry : map.entrySet()) {
/*  928:1067 */       if (value.equals(entry.getValue())) {
/*  929:1068 */         return entry.getKey();
/*  930:     */       }
/*  931:     */     }
/*  932:1071 */     return null;
/*  933:     */   }
/*  934:     */   
/*  935:     */   public boolean getIslandLevel(Player player, String islandPlayer)
/*  936:     */   {
/*  937:1076 */     if (this.allowInfo)
/*  938:     */     {
/*  939:1079 */       this.allowInfo = false;
/*  940:1080 */       final String playerx = player.getName();
/*  941:1081 */       final String islandPlayerx = islandPlayer;
/*  942:1082 */       if ((!uSkyBlock.getInstance().hasIsland(islandPlayer)) && (!uSkyBlock.getInstance().hasParty(islandPlayer)))
/*  943:     */       {
/*  944:1084 */         player.sendMessage(ChatColor.RED + "That player is invalid or does not have an island!");
/*  945:1085 */         this.allowInfo = true;
/*  946:1086 */         return false;
/*  947:     */       }
/*  948:1088 */       uSkyBlock.getInstance().getServer().getScheduler().runTaskAsynchronously(uSkyBlock.getInstance(), new Runnable()
/*  949:     */       {
/*  950:     */         public void run()
/*  951:     */         {
/*  952:     */           try
/*  953:     */           {
/*  954:1095 */             int[] values = new int[256];
/*  955:1096 */             String player = playerx;
/*  956:1097 */             String islandPlayer = islandPlayerx;
/*  957:     */             
/*  958:     */ 
/*  959:     */ 
/*  960:     */ 
/*  961:     */ 
/*  962:     */ 
/*  963:1104 */             Location l = ((PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(player)).getIslandLocation();
/*  964:1105 */             int blockcount = 0;
/*  965:1106 */             if (player.equalsIgnoreCase(islandPlayer))
/*  966:     */             {
/*  967:1108 */               int px = l.getBlockX();
/*  968:1109 */               int py = l.getBlockY();
/*  969:1110 */               int pz = l.getBlockZ();
/*  970:1111 */               World w = l.getWorld();
/*  971:1112 */               for (int x = Settings.island_protectionRange / 2 * -1; x <= Settings.island_protectionRange / 2; x++) {
/*  972:1114 */                 for (int y = 0; y <= 255; y++) {
/*  973:1115 */                   for (int z = Settings.island_protectionRange / 2 * -1; z <= Settings.island_protectionRange / 2; z++) {
/*  974:1118 */                     values[w.getBlockAt(px + x, py + y, pz + z).getTypeId()] += 1;
/*  975:     */                   }
/*  976:     */                 }
/*  977:     */               }
/*  978:1122 */               for (int i = 1; i <= 255; i++)
/*  979:     */               {
/*  980:1124 */                 if ((values[i] > Settings.limitList[i]) && (Settings.limitList[i] >= 0)) {
/*  981:1125 */                   values[i] = Settings.limitList[i];
/*  982:     */                 }
/*  983:1126 */                 if (Settings.diminishingReturnsList[i] > 0) {
/*  984:1127 */                   values[i] = ((int)Math.round(uSkyBlock.getInstance().dReturns(values[i], Settings.diminishingReturnsList[i])));
/*  985:     */                 }
/*  986:1128 */                 values[i] *= Settings.blockList[i];
/*  987:1129 */                 blockcount += values[i];
/*  988:     */               }
/*  989:     */             }
/*  990:1132 */             if (player.equalsIgnoreCase(islandPlayer))
/*  991:     */             {
/*  992:1134 */               uSkyBlock.getInstance().getIslandConfig(((PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(player)).locationForParty()).set("general.level", Integer.valueOf(blockcount / uSkyBlock.getInstance().getLevelConfig().getInt("general.pointsPerLevel")));
/*  993:1135 */               ((PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(player)).savePlayerConfig(player);
/*  994:     */             }
/*  995:     */           }
/*  996:     */           catch (Exception e)
/*  997:     */           {
/*  998:1138 */             System.out.print("Error while calculating Island Level: " + e);
/*  999:1139 */             IslandCommand.this.allowInfo = true;
/* 1000:     */           }
/* 1001:1144 */           uSkyBlock.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(uSkyBlock.getInstance(), new Runnable()
/* 1002:     */           {
/* 1003:     */             public void run()
/* 1004:     */             {
/* 1005:1149 */               IslandCommand.this.allowInfo = true;
/* 1006:1151 */               if (Bukkit.getPlayer(this.val$playerx) != null)
/* 1007:     */               {
/* 1008:1153 */                 Bukkit.getPlayer(this.val$playerx).sendMessage(ChatColor.YELLOW + "Information about " + this.val$islandPlayerx + "'s Island:");
/* 1009:1154 */                 if (this.val$playerx.equalsIgnoreCase(this.val$islandPlayerx))
/* 1010:     */                 {
/* 1011:1156 */                   Bukkit.getPlayer(this.val$playerx).sendMessage(ChatColor.GREEN + "Island level is " + uSkyBlock.getInstance().getIslandConfig(((PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(this.val$playerx)).locationForParty()).getInt("general.level"));
/* 1012:1157 */                   uSkyBlock.getInstance().saveIslandConfig(((PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(this.val$playerx)).locationForParty());
/* 1013:     */                 }
/* 1014:     */                 else
/* 1015:     */                 {
/* 1016:1160 */                   PlayerInfo pi = new PlayerInfo(this.val$islandPlayerx);
/* 1017:1161 */                   if (!pi.getHasIsland()) {
/* 1018:1162 */                     Bukkit.getPlayer(this.val$playerx).sendMessage(ChatColor.GREEN + "Island level is " + ChatColor.WHITE + uSkyBlock.getInstance().getIslandConfig(pi.locationForParty()).getInt("general.level"));
/* 1019:     */                   } else {
/* 1020:1164 */                     Bukkit.getPlayer(this.val$playerx).sendMessage(ChatColor.RED + "Error: Invalid Player");
/* 1021:     */                   }
/* 1022:     */                 }
/* 1023:     */               }
/* 1024:     */             }
/* 1025:1169 */           }, 0L);
/* 1026:     */         }
/* 1027:     */       });
/* 1028:     */     }
/* 1029:     */     else
/* 1030:     */     {
/* 1031:1174 */       player.sendMessage(ChatColor.RED + "Can't use that command right now! Try again in a few seconds.");
/* 1032:1175 */       System.out.print(player.getName() + " tried to use /island info but someone else used it first!");
/* 1033:1176 */       return false;
/* 1034:     */     }
/* 1035:1178 */     return true;
/* 1036:     */   }
/* 1037:     */   
/* 1038:     */   public String getBanList(Player player)
/* 1039:     */   {
/* 1040:1184 */     return null;
/* 1041:     */   }
/* 1042:     */ }
