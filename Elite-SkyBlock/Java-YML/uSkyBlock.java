/*    1:     */ package us.talabrek.ultimateskyblock;
/*    2:     */ 
/*    3:     */ import com.sk89q.worldedit.MaxChangedBlocksException;
/*    4:     */ import com.sk89q.worldedit.data.DataException;
/*    5:     */ import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
/*    6:     */ import com.sk89q.worldguard.protection.managers.RegionManager;
/*    7:     */ import java.io.File;
/*    8:     */ import java.io.FileInputStream;
/*    9:     */ import java.io.FileOutputStream;
/*   10:     */ import java.io.IOException;
/*   11:     */ import java.io.InputStream;
/*   12:     */ import java.io.ObjectInputStream;
/*   13:     */ import java.io.ObjectOutputStream;
/*   14:     */ import java.io.PrintStream;
/*   15:     */ import java.text.DateFormat;
/*   16:     */ import java.text.DecimalFormat;
/*   17:     */ import java.util.ArrayList;
/*   18:     */ import java.util.Calendar;
/*   19:     */ import java.util.Collections;
/*   20:     */ import java.util.Date;
/*   21:     */ import java.util.HashMap;
/*   22:     */ import java.util.Iterator;
/*   23:     */ import java.util.LinkedHashMap;
/*   24:     */ import java.util.List;
/*   25:     */ import java.util.Set;
/*   26:     */ import java.util.Stack;
/*   27:     */ import java.util.logging.Level;
/*   28:     */ import java.util.logging.Logger;
/*   29:     */ import net.milkbowl.vault.economy.Economy;
/*   30:     */ import org.bukkit.Bukkit;
/*   31:     */ import org.bukkit.ChatColor;
/*   32:     */ import org.bukkit.Chunk;
/*   33:     */ import org.bukkit.Location;
/*   34:     */ import org.bukkit.Material;
/*   35:     */ import org.bukkit.OfflinePlayer;
/*   36:     */ import org.bukkit.Server;
/*   37:     */ import org.bukkit.World;
/*   38:     */ import org.bukkit.World.Environment;
/*   39:     */ import org.bukkit.WorldCreator;
/*   40:     */ import org.bukkit.WorldType;
/*   41:     */ import org.bukkit.block.Biome;
/*   42:     */ import org.bukkit.block.Block;
/*   43:     */ import org.bukkit.block.BlockFace;
/*   44:     */ import org.bukkit.block.Chest;
/*   45:     */ import org.bukkit.block.Dispenser;
/*   46:     */ import org.bukkit.block.Furnace;
/*   47:     */ import org.bukkit.command.CommandSender;
/*   48:     */ import org.bukkit.command.PluginCommand;
/*   49:     */ import org.bukkit.configuration.ConfigurationSection;
/*   50:     */ import org.bukkit.configuration.file.FileConfiguration;
/*   51:     */ import org.bukkit.configuration.file.YamlConfiguration;
/*   52:     */ import org.bukkit.entity.Entity;
/*   53:     */ import org.bukkit.entity.EntityType;
/*   54:     */ import org.bukkit.entity.Player;
/*   55:     */ import org.bukkit.generator.ChunkGenerator;
/*   56:     */ import org.bukkit.inventory.EntityEquipment;
/*   57:     */ import org.bukkit.inventory.FurnaceInventory;
/*   58:     */ import org.bukkit.inventory.Inventory;
/*   59:     */ import org.bukkit.inventory.ItemStack;
/*   60:     */ import org.bukkit.inventory.PlayerInventory;
/*   61:     */ import org.bukkit.inventory.meta.ItemMeta;
/*   62:     */ import org.bukkit.inventory.meta.SkullMeta;
/*   63:     */ import org.bukkit.plugin.PluginDescriptionFile;
/*   64:     */ import org.bukkit.plugin.PluginManager;
/*   65:     */ import org.bukkit.plugin.java.JavaPlugin;
/*   66:     */ import org.bukkit.scheduler.BukkitScheduler;
/*   67:     */ 
/*   68:     */ public class uSkyBlock
/*   69:     */   extends JavaPlugin
/*   70:     */ {
/*   71:     */   public PluginDescriptionFile pluginFile;
/*   72:     */   public Logger log;
/*   73:     */   Date date;
/*   74:  68 */   public DecimalFormat df = new DecimalFormat(".#");
/*   75:  69 */   private FileConfiguration levelConfig = null;
/*   76:  70 */   private FileConfiguration lastIslandConfig = null;
/*   77:  71 */   private FileConfiguration orphans = null;
/*   78:  72 */   private FileConfiguration tempIsland = null;
/*   79:  73 */   private FileConfiguration tempPlayer = null;
/*   80:  74 */   private HashMap<String, FileConfiguration> islands = new HashMap();
/*   81:  77 */   private File levelConfigFile = null;
/*   82:  78 */   private File orphanFile = null;
/*   83:  79 */   private File lastIslandConfigFile = null;
/*   84:  80 */   private File islandConfigFile = null;
/*   85:  81 */   private File tempIslandFile = null;
/*   86:  82 */   private File tempPlayerFile = null;
/*   87:  86 */   public static World skyBlockWorld = null;
/*   88:     */   private static uSkyBlock instance;
/*   89:  88 */   public List<String> removeList = new ArrayList();
/*   90:     */   List<String> rankDisplay;
/*   91:     */   public FileConfiguration configPlugin;
/*   92:     */   public File filePlugin;
/*   93:     */   private Location lastIsland;
/*   94:  93 */   private Stack<Location> orphaned = new Stack();
/*   95:  94 */   private Stack<Location> tempOrphaned = new Stack();
/*   96:  95 */   private Stack<Location> reverseOrphaned = new Stack();
/*   97:     */   public File directoryPlayers;
/*   98:     */   public File directoryIslands;
/*   99:     */   private File directorySchematics;
/*  100:     */   public File[] schemFile;
/*  101:     */   public String pName;
/*  102: 105 */   public Location islandTestLocation = null;
/*  103:     */   LinkedHashMap<String, Double> topTen;
/*  104: 107 */   HashMap<String, Long> infoCooldown = new HashMap();
/*  105: 108 */   HashMap<String, Long> restartCooldown = new HashMap();
/*  106: 109 */   HashMap<String, Long> biomeCooldown = new HashMap();
/*  107: 110 */   HashMap<String, PlayerInfo> activePlayers = new HashMap();
/*  108: 111 */   LinkedHashMap<String, List<String>> challenges = new LinkedHashMap();
/*  109: 112 */   HashMap<Integer, Integer> requiredList = new HashMap();
/*  110: 113 */   public boolean purgeActive = false;
/*  111: 114 */   private FileConfiguration skyblockData = null;
/*  112: 115 */   private File skyblockDataFile = null;
/*  113: 117 */   public Inventory GUIparty = null;
/*  114: 118 */   public Inventory GUIpartyPlayer = null;
/*  115: 119 */   public Inventory GUIisland = null;
/*  116: 120 */   public Inventory GUIchallenge = null;
/*  117: 121 */   public Inventory GUIbiome = null;
/*  118: 122 */   public Inventory GUIlog = null;
/*  119: 124 */   ItemStack pHead = new ItemStack(397, 1, (short)3);
/*  120: 126 */   ItemStack sign = new ItemStack(323, 1);
/*  121: 128 */   ItemStack biome = new ItemStack(6, 1, (short)3);
/*  122: 130 */   ItemStack lock = new ItemStack(101, 1);
/*  123: 132 */   ItemStack warpset = new ItemStack(90, 1);
/*  124: 134 */   ItemStack warptoggle = new ItemStack(69, 1);
/*  125: 136 */   ItemStack invite = new ItemStack(398, 1);
/*  126: 138 */   ItemStack kick = new ItemStack(301, 1);
/*  127: 139 */   ItemStack currentBiomeItem = null;
/*  128: 140 */   ItemStack currentIslandItem = null;
/*  129: 141 */   ItemStack currentChallengeItem = null;
/*  130: 142 */   ItemStack currentLogItem = null;
/*  131: 143 */   List<String> lores = new ArrayList();
/*  132:     */   Iterator<String> tempIt;
/*  133:     */   private ArrayList<File> sfiles;
/*  134:     */   
/*  135:     */   public void onDisable()
/*  136:     */   {
/*  137:     */     try
/*  138:     */     {
/*  139: 149 */       unloadPlayerFiles();
/*  140: 151 */       if (this.lastIsland != null) {
/*  141: 153 */         setLastIsland(this.lastIsland);
/*  142:     */       }
/*  143:     */     }
/*  144:     */     catch (Exception e)
/*  145:     */     {
/*  146: 161 */       System.out.println("Something went wrong saving the island and/or party data!");
/*  147: 162 */       e.printStackTrace();
/*  148:     */     }
/*  149: 164 */     this.log.info(this.pluginFile.getName() + " v" + this.pluginFile.getVersion() + " disabled.");
/*  150:     */   }
/*  151:     */   
/*  152:     */   public void onEnable()
/*  153:     */   {
/*  154: 169 */     instance = this;
/*  155: 170 */     saveDefaultConfig();
/*  156: 171 */     saveDefaultLevelConfig();
/*  157: 172 */     saveDefaultOrphans();
/*  158: 173 */     this.pluginFile = getDescription();
/*  159: 174 */     this.log = getLogger();
/*  160: 175 */     this.pName = (ChatColor.WHITE + "[" + ChatColor.GREEN + this.pluginFile.getName() + ChatColor.WHITE + "] ");
/*  161:     */     
/*  162:     */ 
/*  163:     */ 
/*  164:     */ 
/*  165:     */ 
/*  166:     */ 
/*  167:     */ 
/*  168: 183 */     VaultHandler.setupEconomy();
/*  169: 184 */     if (!getDataFolder().exists()) {
/*  170: 185 */       getDataFolder().mkdir();
/*  171:     */     }
/*  172: 187 */     this.configPlugin = getConfig();
/*  173: 188 */     this.filePlugin = new File(getDataFolder(), "config.yml");
/*  174: 189 */     loadPluginConfig();
/*  175: 190 */     loadLevelConfig();
/*  176: 191 */     registerEvents();
/*  177: 192 */     this.directoryPlayers = new File(getDataFolder() + File.separator + "players");
/*  178: 193 */     this.directoryIslands = new File(getDataFolder() + File.separator + "islands");
/*  179: 194 */     if (!this.directoryPlayers.exists())
/*  180:     */     {
/*  181: 195 */       this.directoryPlayers.mkdir();
/*  182: 196 */       loadPlayerFiles();
/*  183:     */     }
/*  184:     */     else
/*  185:     */     {
/*  186: 198 */       loadPlayerFiles();
/*  187:     */     }
/*  188: 201 */     if (!this.directoryIslands.exists()) {
/*  189: 202 */       this.directoryIslands.mkdir();
/*  190:     */     }
/*  191: 204 */     this.directorySchematics = new File(getDataFolder() + File.separator + "schematics");
/*  192: 205 */     if (!this.directorySchematics.exists()) {
/*  193: 206 */       this.directorySchematics.mkdir();
/*  194:     */     }
/*  195: 208 */     this.schemFile = this.directorySchematics.listFiles();
/*  196: 209 */     if (this.schemFile == null) {
/*  197: 211 */       System.out.print("[uSkyBlock] No schematic file loaded.");
/*  198:     */     } else {
/*  199: 213 */       System.out.print("[uSkyBlock] " + this.schemFile.length + " schematics loaded.");
/*  200:     */     }
/*  201: 256 */     getCommand("island").setExecutor(new IslandCommand());
/*  202: 257 */     getCommand("challenges").setExecutor(new ChallengesCommand());
/*  203: 258 */     getCommand("dev").setExecutor(new DevCommand());
/*  204: 265 */     if (Settings.island_useTopTen) {
/*  205: 266 */       getInstance().updateTopTen(getInstance().generateTopTen());
/*  206:     */     }
/*  207: 267 */     populateChallengeList();
/*  208: 268 */     this.log.info(this.pluginFile.getName() + " v." + this.pluginFile.getVersion() + " enabled.");
/*  209: 269 */     getInstance().getServer().getScheduler().runTaskLater(getInstance(), new Runnable()
/*  210:     */     {
/*  211:     */       public void run()
/*  212:     */       {
/*  213: 274 */         if (Bukkit.getServer().getPluginManager().isPluginEnabled("Vault"))
/*  214:     */         {
/*  215: 276 */           System.out.print("[uSkyBlock] Using vault for permissions");
/*  216: 277 */           VaultHandler.setupPermissions();
/*  217:     */           try
/*  218:     */           {
/*  219: 279 */             if ((!uSkyBlock.this.getLastIslandConfig().contains("options.general.lastIslandX")) && (uSkyBlock.this.getConfig().contains("options.general.lastIslandX")))
/*  220:     */             {
/*  221: 281 */               uSkyBlock.this.getLastIslandConfig();FileConfiguration.createPath(uSkyBlock.this.getLastIslandConfig().getConfigurationSection("options.general"), "lastIslandX");
/*  222: 282 */               uSkyBlock.this.getLastIslandConfig();FileConfiguration.createPath(uSkyBlock.this.getLastIslandConfig().getConfigurationSection("options.general"), "lastIslandZ");
/*  223: 283 */               uSkyBlock.this.getLastIslandConfig().set("options.general.lastIslandX", Integer.valueOf(uSkyBlock.this.getConfig().getInt("options.general.lastIslandX")));
/*  224: 284 */               uSkyBlock.this.getLastIslandConfig().set("options.general.lastIslandZ", Integer.valueOf(uSkyBlock.this.getConfig().getInt("options.general.lastIslandZ")));
/*  225: 285 */               uSkyBlock.this.saveLastIslandConfig();
/*  226:     */             }
/*  227: 287 */             uSkyBlock.this.lastIsland = new Location(uSkyBlock.getSkyBlockWorld(), uSkyBlock.this.getLastIslandConfig().getInt("options.general.lastIslandX"), Settings.island_height, uSkyBlock.this.getLastIslandConfig().getInt("options.general.lastIslandZ"));
/*  228:     */           }
/*  229:     */           catch (Exception e)
/*  230:     */           {
/*  231: 290 */             uSkyBlock.this.lastIsland = new Location(uSkyBlock.getSkyBlockWorld(), uSkyBlock.this.getConfig().getInt("options.general.lastIslandX"), Settings.island_height, uSkyBlock.this.getConfig().getInt("options.general.lastIslandZ"));
/*  232:     */           }
/*  233: 292 */           if (uSkyBlock.this.lastIsland == null) {
/*  234: 294 */             uSkyBlock.this.lastIsland = new Location(uSkyBlock.getSkyBlockWorld(), 0.0D, Settings.island_height, 0.0D);
/*  235:     */           }
/*  236: 297 */           if ((Settings.island_protectWithWorldGuard) && (!Bukkit.getServer().getPluginManager().isPluginEnabled("WorldGuard")))
/*  237:     */           {
/*  238: 299 */             PluginManager manager = uSkyBlock.getInstance().getServer().getPluginManager();
/*  239: 300 */             System.out.print("[uSkyBlock] WorldGuard not loaded! Using built in protection.");
/*  240: 301 */             manager.registerEvents(new ProtectionEvents(), uSkyBlock.getInstance());
/*  241:     */           }
/*  242: 303 */           uSkyBlock.getInstance().setupOrphans();
/*  243:     */         }
/*  244:     */       }
/*  245: 307 */     }, 0L);
/*  246:     */   }
/*  247:     */   
/*  248:     */   public static uSkyBlock getInstance()
/*  249:     */   {
/*  250: 311 */     return instance;
/*  251:     */   }
/*  252:     */   
/*  253:     */   public void loadPlayerFiles()
/*  254:     */   {
/*  255: 316 */     int onlinePlayerCount = 0;
/*  256: 317 */     onlinePlayerCount = Bukkit.getServer().getOnlinePlayers().length;
/*  257: 318 */     Player[] onlinePlayers = Bukkit.getServer().getOnlinePlayers();
/*  258: 319 */     for (int i = 0; i < onlinePlayerCount; i++) {
/*  259: 321 */       if (onlinePlayers[i].isOnline())
/*  260:     */       {
/*  261: 323 */         File f = new File(getInstance().directoryPlayers, onlinePlayers[i].getName());
/*  262: 324 */         PlayerInfo pi = new PlayerInfo(onlinePlayers[i].getName());
/*  263: 325 */         if (f.exists())
/*  264:     */         {
/*  265: 327 */           PlayerInfo pi2 = getInstance().readPlayerFile(onlinePlayers[i].getName());
/*  266: 328 */           if (pi2 != null)
/*  267:     */           {
/*  268: 330 */             pi.setIslandLocation(pi2.getIslandLocation());
/*  269: 331 */             pi.setHomeLocation(pi2.getHomeLocation());
/*  270: 332 */             pi.setHasIsland(pi2.getHasIsland());
/*  271: 333 */             if (getInstance().getIslandConfig(pi.locationForParty()) == null) {
/*  272: 335 */               getInstance().createIslandConfig(pi.locationForParty(), onlinePlayers[i].getName());
/*  273:     */             }
/*  274: 337 */             getInstance().clearIslandConfig(pi.locationForParty(), onlinePlayers[i].getName());
/*  275: 338 */             if ((Settings.island_protectWithWorldGuard) && (Bukkit.getServer().getPluginManager().isPluginEnabled("WorldGuard"))) {
/*  276: 339 */               WorldGuardHandler.protectIsland(onlinePlayers[i], onlinePlayers[i].getName(), pi);
/*  277:     */             }
/*  278:     */           }
/*  279: 341 */           f.delete();
/*  280:     */         }
/*  281: 343 */         getInstance().addActivePlayer(onlinePlayers[i].getName(), pi);
/*  282: 344 */         if ((pi.getHasIsland()) && (getInstance().getTempIslandConfig(pi.locationForParty()) == null))
/*  283:     */         {
/*  284: 346 */           getInstance().createIslandConfig(pi.locationForParty(), onlinePlayers[i].getName());
/*  285: 347 */           System.out.println("Creating new Config File");
/*  286:     */         }
/*  287: 349 */         getInstance().getIslandConfig(pi.locationForParty());
/*  288:     */       }
/*  289:     */     }
/*  290: 353 */     System.out.print("Island Configs Loaded:");
/*  291: 354 */     getInstance().displayIslandConfigs();
/*  292:     */   }
/*  293:     */   
/*  294:     */   public void unloadPlayerFiles()
/*  295:     */   {
/*  296: 361 */     for (int i = 0; i < Bukkit.getServer().getOnlinePlayers().length; i++)
/*  297:     */     {
/*  298: 363 */       Player[] removedPlayers = Bukkit.getServer().getOnlinePlayers();
/*  299: 364 */       if (getActivePlayers().containsKey(removedPlayers[i].getName())) {
/*  300: 365 */         removeActivePlayer(removedPlayers[i].getName());
/*  301:     */       }
/*  302:     */     }
/*  303:     */   }
/*  304:     */   
/*  305:     */   public void registerEvents()
/*  306:     */   {
/*  307: 374 */     PluginManager manager = getServer().getPluginManager();
/*  308:     */     
/*  309:     */ 
/*  310: 377 */     manager.registerEvents(new PlayerJoin(), this);
/*  311: 378 */     if (!Settings.island_protectWithWorldGuard)
/*  312:     */     {
/*  313: 380 */       System.out.print("[uSkyBlock] Using built in protection.");
/*  314: 381 */       manager.registerEvents(new ProtectionEvents(), getInstance());
/*  315:     */     }
/*  316:     */     else
/*  317:     */     {
/*  318: 384 */       System.out.print("[uSkyBlock] Using WorldGuard protection.");
/*  319:     */     }
/*  320:     */   }
/*  321:     */   
/*  322:     */   public void loadPluginConfig()
/*  323:     */   {
/*  324:     */     try
/*  325:     */     {
/*  326: 398 */       getConfig();
/*  327:     */     }
/*  328:     */     catch (Exception e)
/*  329:     */     {
/*  330: 400 */       e.printStackTrace();
/*  331:     */     }
/*  332:     */     try
/*  333:     */     {
/*  334: 405 */       Settings.general_maxPartySize = getConfig().getInt("options.general.maxPartySize");
/*  335: 406 */       if (Settings.general_maxPartySize < 0) {
/*  336: 407 */         Settings.general_maxPartySize = 0;
/*  337:     */       }
/*  338:     */     }
/*  339:     */     catch (Exception e)
/*  340:     */     {
/*  341: 410 */       Settings.general_maxPartySize = 4;
/*  342:     */     }
/*  343:     */     try
/*  344:     */     {
/*  345: 413 */       Settings.island_distance = getConfig().getInt("options.island.distance");
/*  346: 414 */       if (Settings.island_distance < 50) {
/*  347: 415 */         Settings.island_distance = 50;
/*  348:     */       }
/*  349:     */     }
/*  350:     */     catch (Exception e)
/*  351:     */     {
/*  352: 418 */       Settings.island_distance = 110;
/*  353:     */     }
/*  354:     */     try
/*  355:     */     {
/*  356: 421 */       Settings.island_protectionRange = getConfig().getInt("options.island.protectionRange");
/*  357: 422 */       if (Settings.island_protectionRange > Settings.island_distance) {
/*  358: 423 */         Settings.island_protectionRange = Settings.island_distance;
/*  359:     */       }
/*  360:     */     }
/*  361:     */     catch (Exception e)
/*  362:     */     {
/*  363: 426 */       Settings.island_protectionRange = 100;
/*  364:     */     }
/*  365:     */     try
/*  366:     */     {
/*  367: 429 */       Settings.general_cooldownInfo = getConfig().getInt("options.general.cooldownInfo");
/*  368: 430 */       if (Settings.general_cooldownInfo < 0) {
/*  369: 431 */         Settings.general_cooldownInfo = 0;
/*  370:     */       }
/*  371:     */     }
/*  372:     */     catch (Exception e)
/*  373:     */     {
/*  374: 434 */       Settings.general_cooldownInfo = 60;
/*  375:     */     }
/*  376:     */     try
/*  377:     */     {
/*  378: 437 */       Settings.general_biomeChange = getConfig().getInt("options.general.biomeChange");
/*  379: 438 */       if (Settings.general_biomeChange < 0) {
/*  380: 439 */         Settings.general_biomeChange = 0;
/*  381:     */       }
/*  382:     */     }
/*  383:     */     catch (Exception e)
/*  384:     */     {
/*  385: 442 */       Settings.general_biomeChange = 3600;
/*  386:     */     }
/*  387:     */     try
/*  388:     */     {
/*  389: 445 */       Settings.general_cooldownRestart = getConfig().getInt("options.general.cooldownRestart");
/*  390: 446 */       if (Settings.general_cooldownRestart < 0) {
/*  391: 447 */         Settings.general_cooldownRestart = 0;
/*  392:     */       }
/*  393:     */     }
/*  394:     */     catch (Exception e)
/*  395:     */     {
/*  396: 450 */       Settings.general_cooldownRestart = 60;
/*  397:     */     }
/*  398:     */     try
/*  399:     */     {
/*  400: 453 */       Settings.island_height = getConfig().getInt("options.island.height");
/*  401: 454 */       if (Settings.island_height < 20) {
/*  402: 455 */         Settings.island_height = 20;
/*  403:     */       }
/*  404:     */     }
/*  405:     */     catch (Exception e)
/*  406:     */     {
/*  407: 458 */       Settings.island_height = 120;
/*  408:     */     }
/*  409:     */     try
/*  410:     */     {
/*  411: 461 */       Settings.challenges_rankLeeway = getConfig().getInt("options.challenges.rankLeeway");
/*  412: 462 */       if (Settings.challenges_rankLeeway < 0) {
/*  413: 463 */         Settings.challenges_rankLeeway = 0;
/*  414:     */       }
/*  415:     */     }
/*  416:     */     catch (Exception e)
/*  417:     */     {
/*  418: 466 */       Settings.challenges_rankLeeway = 0;
/*  419:     */     }
/*  420: 469 */     if (!getConfig().contains("options.extras.obsidianToLava"))
/*  421:     */     {
/*  422: 471 */       getConfig().set("options.extras.obsidianToLava", Boolean.valueOf(true));
/*  423: 472 */       saveConfig();
/*  424:     */     }
/*  425: 474 */     if (!getConfig().contains("options.general.spawnSize"))
/*  426:     */     {
/*  427: 476 */       getConfig().set("options.general.spawnSize", Integer.valueOf(50));
/*  428: 477 */       saveConfig();
/*  429:     */     }
/*  430:     */     try
/*  431:     */     {
/*  432: 480 */       Settings.general_spawnSize = getConfig().getInt("options.general.spawnSize");
/*  433: 481 */       if (Settings.general_spawnSize < 50) {
/*  434: 482 */         Settings.general_spawnSize = 50;
/*  435:     */       }
/*  436:     */     }
/*  437:     */     catch (Exception e)
/*  438:     */     {
/*  439: 485 */       Settings.general_spawnSize = 50;
/*  440:     */     }
/*  441: 490 */     String[] chestItemString = getConfig().getString("options.island.chestItems").split(" ");
/*  442: 491 */     ItemStack[] tempChest = new ItemStack[chestItemString.length];
/*  443: 492 */     String[] amountdata = new String[2];
/*  444: 493 */     for (int i = 0; i < tempChest.length; i++)
/*  445:     */     {
/*  446: 495 */       amountdata = chestItemString[i].split(":");
/*  447: 496 */       tempChest[i] = new ItemStack(Integer.parseInt(amountdata[0]), Integer.parseInt(amountdata[1]));
/*  448:     */     }
/*  449: 498 */     Settings.island_chestItems = tempChest;
/*  450: 499 */     Settings.island_allowPvP = getConfig().getString("options.island.allowPvP");
/*  451: 500 */     Settings.island_schematicName = getConfig().getString("options.island.schematicName");
/*  452: 501 */     if (!Settings.island_allowPvP.equalsIgnoreCase("allow")) {
/*  453: 502 */       Settings.island_allowPvP = "deny";
/*  454:     */     }
/*  455: 503 */     Set<String> permissionList = getConfig().getConfigurationSection("options.island.extraPermissions").getKeys(true);
/*  456: 504 */     Settings.island_addExtraItems = getConfig().getBoolean("options.island.addExtraItems");
/*  457: 505 */     Settings.extras_obsidianToLava = getConfig().getBoolean("options.extras.obsidianToLava");
/*  458: 506 */     Settings.island_useIslandLevel = getConfig().getBoolean("options.island.useIslandLevel");
/*  459: 507 */     Settings.island_extraPermissions = (String[])permissionList.toArray(new String[0]);
/*  460: 508 */     Settings.island_protectWithWorldGuard = getConfig().getBoolean("options.island.protectWithWorldGuard");
/*  461: 509 */     Settings.extras_sendToSpawn = getConfig().getBoolean("options.extras.sendToSpawn");
/*  462: 510 */     Settings.island_useTopTen = getConfig().getBoolean("options.island.useTopTen");
/*  463:     */     
/*  464: 512 */     Settings.general_worldName = getConfig().getString("options.general.worldName");
/*  465: 513 */     Settings.island_removeCreaturesByTeleport = getConfig().getBoolean("options.island.removeCreaturesByTeleport");
/*  466: 514 */     Settings.island_allowIslandLock = getConfig().getBoolean("options.island.allowIslandLock");
/*  467: 515 */     Settings.island_useOldIslands = getConfig().getBoolean("options.island.useOldIslands");
/*  468:     */     
/*  469: 517 */     Set<String> challengeList = getConfig().getConfigurationSection("options.challenges.challengeList").getKeys(false);
/*  470: 518 */     Settings.challenges_challengeList = challengeList;
/*  471: 519 */     Settings.challenges_broadcastCompletion = getConfig().getBoolean("options.challenges.broadcastCompletion");
/*  472: 520 */     Settings.challenges_broadcastText = getConfig().getString("options.challenges.broadcastText");
/*  473: 521 */     Settings.challenges_challengeColor = getConfig().getString("options.challenges.challengeColor");
/*  474: 522 */     Settings.challenges_enableEconomyPlugin = getConfig().getBoolean("options.challenges.enableEconomyPlugin");
/*  475: 523 */     Settings.challenges_finishedColor = getConfig().getString("options.challenges.finishedColor");
/*  476: 524 */     Settings.challenges_repeatableColor = getConfig().getString("options.challenges.repeatableColor");
/*  477: 525 */     Settings.challenges_requirePreviousRank = getConfig().getBoolean("options.challenges.requirePreviousRank");
/*  478: 526 */     Settings.challenges_allowChallenges = getConfig().getBoolean("options.challenges.allowChallenges");
/*  479: 527 */     String[] rankListString = getConfig().getString("options.challenges.ranks").split(" ");
/*  480: 528 */     Settings.challenges_ranks = rankListString;
/*  481:     */   }
/*  482:     */   
/*  483:     */   public List<Party> readPartyFile()
/*  484:     */   {
/*  485: 533 */     File f = new File(getDataFolder(), "partylist.bin");
/*  486: 534 */     if (!f.exists()) {
/*  487: 535 */       return null;
/*  488:     */     }
/*  489:     */     try
/*  490:     */     {
/*  491: 539 */       FileInputStream fileIn = new FileInputStream(f);
/*  492: 540 */       ObjectInputStream in = new ObjectInputStream(fileIn);
/*  493:     */       
/*  494: 542 */       List<Party> p = (List)in.readObject();
/*  495: 543 */       in.close();
/*  496: 544 */       fileIn.close();
/*  497: 545 */       return p;
/*  498:     */     }
/*  499:     */     catch (Exception e)
/*  500:     */     {
/*  501: 547 */       e.printStackTrace();
/*  502:     */     }
/*  503: 549 */     return null;
/*  504:     */   }
/*  505:     */   
/*  506:     */   public void writePartyFile(List<Party> pi)
/*  507:     */   {
/*  508: 554 */     File f = new File(getDataFolder(), "partylist.bin");
/*  509:     */     try
/*  510:     */     {
/*  511: 557 */       FileOutputStream fileOut = new FileOutputStream(f);
/*  512: 558 */       ObjectOutputStream out = new ObjectOutputStream(fileOut);
/*  513: 559 */       out.writeObject(pi);
/*  514: 560 */       out.flush();
/*  515: 561 */       out.close();
/*  516: 562 */       fileOut.close();
/*  517:     */     }
/*  518:     */     catch (Exception e)
/*  519:     */     {
/*  520: 564 */       e.printStackTrace();
/*  521:     */     }
/*  522:     */   }
/*  523:     */   
/*  524:     */   public PlayerInfo readPlayerFile(String playerName)
/*  525:     */   {
/*  526: 570 */     File f = new File(this.directoryPlayers, playerName);
/*  527: 571 */     if (!f.exists()) {
/*  528: 572 */       return null;
/*  529:     */     }
/*  530:     */     try
/*  531:     */     {
/*  532: 576 */       FileInputStream fileIn = new FileInputStream(f);
/*  533: 577 */       ObjectInputStream in = new ObjectInputStream(fileIn);
/*  534: 578 */       PlayerInfo p = (PlayerInfo)in.readObject();
/*  535: 579 */       in.close();
/*  536: 580 */       fileIn.close();
/*  537: 581 */       return p;
/*  538:     */     }
/*  539:     */     catch (Exception e)
/*  540:     */     {
/*  541: 583 */       e.printStackTrace();
/*  542:     */     }
/*  543: 585 */     return null;
/*  544:     */   }
/*  545:     */   
/*  546:     */   public boolean displayTopTen(Player player)
/*  547:     */   {
/*  548: 606 */     int i = 1;
/*  549: 607 */     int playerrank = 0;
/*  550: 608 */     player.sendMessage(ChatColor.YELLOW + "Displaying the top 10 islands:");
/*  551: 609 */     if (this.topTen == null)
/*  552:     */     {
/*  553: 611 */       player.sendMessage(ChatColor.RED + "Top ten list not generated yet!");
/*  554: 612 */       return false;
/*  555:     */     }
/*  556: 618 */     for (String playerName : this.topTen.keySet())
/*  557:     */     {
/*  558: 620 */       if (i <= 10) {
/*  559: 622 */         player.sendMessage(ChatColor.GREEN + "#" + i + ": " + playerName + " - Island level " + ((Double)this.topTen.get(playerName)).intValue());
/*  560:     */       }
/*  561: 624 */       if (playerName.equalsIgnoreCase(player.getName())) {
/*  562: 626 */         playerrank = i;
/*  563:     */       }
/*  564: 628 */       i++;
/*  565:     */     }
/*  566: 630 */     player.sendMessage(ChatColor.YELLOW + "Your rank is: " + ChatColor.WHITE + playerrank);
/*  567: 631 */     return true;
/*  568:     */   }
/*  569:     */   
/*  570:     */   public void updateTopTen(LinkedHashMap<String, Double> map)
/*  571:     */   {
/*  572: 636 */     this.topTen = map;
/*  573:     */   }
/*  574:     */   
/*  575:     */   public Location getLocationString(String s)
/*  576:     */   {
/*  577: 646 */     if ((s == null) || (s.trim() == "")) {
/*  578: 647 */       return null;
/*  579:     */     }
/*  580: 649 */     String[] parts = s.split(":");
/*  581: 650 */     if (parts.length == 4)
/*  582:     */     {
/*  583: 651 */       World w = getServer().getWorld(parts[0]);
/*  584: 652 */       int x = Integer.parseInt(parts[1]);
/*  585: 653 */       int y = Integer.parseInt(parts[2]);
/*  586: 654 */       int z = Integer.parseInt(parts[3]);
/*  587: 655 */       return new Location(w, x, y, z);
/*  588:     */     }
/*  589: 657 */     return null;
/*  590:     */   }
/*  591:     */   
/*  592:     */   public String getStringLocation(Location l)
/*  593:     */   {
/*  594: 667 */     if (l == null) {
/*  595: 668 */       return "";
/*  596:     */     }
/*  597: 670 */     return l.getWorld().getName() + ":" + l.getBlockX() + ":" + l.getBlockY() + ":" + l.getBlockZ();
/*  598:     */   }
/*  599:     */   
/*  600:     */   public void setStringbyPath(FileConfiguration fc, File f, String path, Object value)
/*  601:     */   {
/*  602: 683 */     fc.set(path, value.toString());
/*  603:     */     try
/*  604:     */     {
/*  605: 685 */       fc.save(f);
/*  606:     */     }
/*  607:     */     catch (IOException e)
/*  608:     */     {
/*  609: 687 */       e.printStackTrace();
/*  610:     */     }
/*  611:     */   }
/*  612:     */   
/*  613:     */   public String getStringbyPath(FileConfiguration fc, File file, String path, Object stdValue, boolean addMissing)
/*  614:     */   {
/*  615: 701 */     if (!fc.contains(path))
/*  616:     */     {
/*  617: 702 */       if (addMissing) {
/*  618: 703 */         setStringbyPath(fc, file, path, stdValue);
/*  619:     */       }
/*  620: 705 */       return stdValue.toString();
/*  621:     */     }
/*  622: 707 */     return fc.getString(path);
/*  623:     */   }
/*  624:     */   
/*  625:     */   public static World getSkyBlockWorld()
/*  626:     */   {
/*  627: 716 */     if (skyBlockWorld == null)
/*  628:     */     {
/*  629: 717 */       skyBlockWorld = WorldCreator.name(Settings.general_worldName).type(WorldType.FLAT).environment(World.Environment.NORMAL).generator(new SkyBlockChunkGenerator()).createWorld();
/*  630: 718 */       if (Bukkit.getServer().getPluginManager().isPluginEnabled("Multiverse-Core")) {
/*  631: 720 */         Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "mv import " + Settings.general_worldName + " normal -g uSkyBlock");
/*  632:     */       }
/*  633:     */     }
/*  634: 724 */     return skyBlockWorld;
/*  635:     */   }
/*  636:     */   
/*  637:     */   public void clearOrphanedIsland()
/*  638:     */   {
/*  639: 728 */     while (hasOrphanedIsland()) {
/*  640: 729 */       this.orphaned.pop();
/*  641:     */     }
/*  642:     */   }
/*  643:     */   
/*  644:     */   public void clearArmorContents(Player player)
/*  645:     */   {
/*  646: 738 */     player.getInventory().setArmorContents(new ItemStack[player.getInventory().getArmorContents().length]);
/*  647:     */   }
/*  648:     */   
/*  649:     */   public void getAllFiles(String path)
/*  650:     */   {
/*  651: 750 */     File dirpath = new File(path);
/*  652: 751 */     if (!dirpath.exists()) {
/*  653: 752 */       return;
/*  654:     */     }
/*  655: 755 */     for (File f : dirpath.listFiles()) {
/*  656:     */       try
/*  657:     */       {
/*  658: 757 */         if (!f.isDirectory()) {
/*  659: 758 */           this.sfiles.add(f);
/*  660:     */         } else {
/*  661: 760 */           getAllFiles(f.getAbsolutePath());
/*  662:     */         }
/*  663:     */       }
/*  664:     */       catch (Exception ex)
/*  665:     */       {
/*  666: 763 */         this.log.warning(ex.getMessage());
/*  667:     */       }
/*  668:     */     }
/*  669:     */   }
/*  670:     */   
/*  671:     */   public Location getYLocation(Location l)
/*  672:     */   {
/*  673: 775 */     for (int y = 0; y < 254; y++)
/*  674:     */     {
/*  675: 776 */       int px = l.getBlockX();
/*  676: 777 */       int py = y;
/*  677: 778 */       int pz = l.getBlockZ();
/*  678: 779 */       Block b1 = new Location(l.getWorld(), px, py, pz).getBlock();
/*  679: 780 */       Block b2 = new Location(l.getWorld(), px, py + 1, pz).getBlock();
/*  680: 781 */       Block b3 = new Location(l.getWorld(), px, py + 2, pz).getBlock();
/*  681: 782 */       if ((!b1.getType().equals(Material.AIR)) && (b2.getType().equals(Material.AIR)) && (b3.getType().equals(Material.AIR))) {
/*  682: 783 */         return b2.getLocation();
/*  683:     */       }
/*  684:     */     }
/*  685: 786 */     return l;
/*  686:     */   }
/*  687:     */   
/*  688:     */   public Location getSafeHomeLocation(PlayerInfo p)
/*  689:     */   {
/*  690: 791 */     Location home = null;
/*  691: 792 */     if (p.getHomeLocation() == null)
/*  692:     */     {
/*  693: 793 */       if (p.getIslandLocation() != null) {
/*  694: 794 */         home = p.getIslandLocation();
/*  695:     */       }
/*  696:     */     }
/*  697:     */     else {
/*  698: 796 */       home = p.getHomeLocation();
/*  699:     */     }
/*  700: 799 */     if (isSafeLocation(home)) {
/*  701: 800 */       return home;
/*  702:     */     }
/*  703: 803 */     for (int y = home.getBlockY() + 25; y > 0; y--)
/*  704:     */     {
/*  705: 804 */       Location n = new Location(home.getWorld(), home.getBlockX(), y, home.getBlockZ());
/*  706: 805 */       if (isSafeLocation(n)) {
/*  707: 806 */         return n;
/*  708:     */       }
/*  709:     */     }
/*  710: 809 */     for (int y = home.getBlockY(); y < 255; y++)
/*  711:     */     {
/*  712: 810 */       Location n = new Location(home.getWorld(), home.getBlockX(), y, home.getBlockZ());
/*  713: 811 */       if (isSafeLocation(n)) {
/*  714: 812 */         return n;
/*  715:     */       }
/*  716:     */     }
/*  717: 816 */     Location island = p.getIslandLocation();
/*  718: 817 */     if (isSafeLocation(island)) {
/*  719: 818 */       return island;
/*  720:     */     }
/*  721: 821 */     for (int y = island.getBlockY() + 25; y > 0; y--)
/*  722:     */     {
/*  723: 822 */       Location n = new Location(island.getWorld(), island.getBlockX(), y, island.getBlockZ());
/*  724: 823 */       if (isSafeLocation(n)) {
/*  725: 824 */         return n;
/*  726:     */       }
/*  727:     */     }
/*  728: 827 */     for (int y = island.getBlockY(); y < 255; y++)
/*  729:     */     {
/*  730: 828 */       Location n = new Location(island.getWorld(), island.getBlockX(), y, island.getBlockZ());
/*  731: 829 */       if (isSafeLocation(n)) {
/*  732: 830 */         return n;
/*  733:     */       }
/*  734:     */     }
/*  735: 833 */     return p.getHomeLocation();
/*  736:     */   }
/*  737:     */   
/*  738:     */   public Location getSafeWarpLocation(PlayerInfo p)
/*  739:     */   {
/*  740: 838 */     Location warp = null;
/*  741: 839 */     getTempIslandConfig(p.locationForParty());
/*  742: 840 */     if (this.tempIsland.getInt("general.warpLocationX") == 0)
/*  743:     */     {
/*  744: 841 */       if (p.getHomeLocation() == null)
/*  745:     */       {
/*  746: 843 */         if (p.getIslandLocation() != null) {
/*  747: 844 */           warp = p.getIslandLocation();
/*  748:     */         }
/*  749:     */       }
/*  750:     */       else {
/*  751: 846 */         warp = p.getHomeLocation();
/*  752:     */       }
/*  753:     */     }
/*  754:     */     else {
/*  755: 849 */       warp = new Location(skyBlockWorld, this.tempIsland.getInt("general.warpLocationX"), this.tempIsland.getInt("general.warpLocationY"), this.tempIsland.getInt("general.warpLocationZ"));
/*  756:     */     }
/*  757: 852 */     if (warp == null)
/*  758:     */     {
/*  759: 854 */       System.out.print("Error warping player to " + p.getPlayerName() + "'s island.");
/*  760: 855 */       return null;
/*  761:     */     }
/*  762: 858 */     if (isSafeLocation(warp)) {
/*  763: 859 */       return warp;
/*  764:     */     }
/*  765: 862 */     for (int y = warp.getBlockY() + 25; y > 0; y--)
/*  766:     */     {
/*  767: 863 */       Location n = new Location(warp.getWorld(), warp.getBlockX(), y, warp.getBlockZ());
/*  768: 864 */       if (isSafeLocation(n)) {
/*  769: 865 */         return n;
/*  770:     */       }
/*  771:     */     }
/*  772: 868 */     for (int y = warp.getBlockY(); y < 255; y++)
/*  773:     */     {
/*  774: 869 */       Location n = new Location(warp.getWorld(), warp.getBlockX(), y, warp.getBlockZ());
/*  775: 870 */       if (isSafeLocation(n)) {
/*  776: 871 */         return n;
/*  777:     */       }
/*  778:     */     }
/*  779: 874 */     return null;
/*  780:     */   }
/*  781:     */   
/*  782:     */   public boolean isSafeLocation(Location l)
/*  783:     */   {
/*  784: 878 */     if (l == null) {
/*  785: 879 */       return false;
/*  786:     */     }
/*  787: 882 */     Block ground = l.getBlock().getRelative(BlockFace.DOWN);
/*  788: 883 */     Block air1 = l.getBlock();
/*  789: 884 */     Block air2 = l.getBlock().getRelative(BlockFace.UP);
/*  790: 885 */     if (ground.getType().equals(Material.AIR)) {
/*  791: 886 */       return false;
/*  792:     */     }
/*  793: 887 */     if (ground.getType().equals(Material.LAVA)) {
/*  794: 888 */       return false;
/*  795:     */     }
/*  796: 889 */     if (ground.getType().equals(Material.STATIONARY_LAVA)) {
/*  797: 890 */       return false;
/*  798:     */     }
/*  799: 891 */     if (ground.getType().equals(Material.CACTUS)) {
/*  800: 892 */       return false;
/*  801:     */     }
/*  802: 893 */     if (((air1.getType().equals(Material.AIR)) || (air1.getType().equals(Material.CROPS)) || (air1.getType().equals(Material.LONG_GRASS)) || (air1.getType().equals(Material.RED_ROSE)) || (air1.getType().equals(Material.YELLOW_FLOWER)) || (air1.getType().equals(Material.DEAD_BUSH)) || (air1.getType().equals(Material.SIGN_POST)) || (air1.getType().equals(Material.SIGN))) && (air2.getType().equals(Material.AIR))) {
/*  803: 894 */       return true;
/*  804:     */     }
/*  805: 895 */     return false;
/*  806:     */   }
/*  807:     */   
/*  808:     */   public void removeCreatures(Location l)
/*  809:     */   {
/*  810: 904 */     if ((!Settings.island_removeCreaturesByTeleport) || (l == null)) {
/*  811: 905 */       return;
/*  812:     */     }
/*  813: 908 */     int px = l.getBlockX();
/*  814: 909 */     int py = l.getBlockY();
/*  815: 910 */     int pz = l.getBlockZ();
/*  816: 911 */     for (int x = -1; x <= 1; x++) {
/*  817: 912 */       for (int z = -1; z <= 1; z++)
/*  818:     */       {
/*  819: 913 */         Chunk c = l.getWorld().getChunkAt(new Location(l.getWorld(), px + x * 16, py, pz + z * 16));
/*  820: 914 */         for (Entity e : c.getEntities()) {
/*  821: 915 */           if ((e.getType() == EntityType.SPIDER) || (e.getType() == EntityType.CREEPER) || (e.getType() == EntityType.ENDERMAN) || (e.getType() == EntityType.SKELETON) || (e.getType() == EntityType.ZOMBIE)) {
/*  822: 916 */             e.remove();
/*  823:     */           }
/*  824:     */         }
/*  825:     */       }
/*  826:     */     }
/*  827:     */   }
/*  828:     */   
/*  829:     */   public void deletePlayerIsland(String player)
/*  830:     */   {
/*  831: 924 */     if (!getActivePlayers().containsKey(player))
/*  832:     */     {
/*  833: 926 */       PlayerInfo pi = new PlayerInfo(player);
/*  834: 927 */       if ((Settings.island_protectWithWorldGuard) && (Bukkit.getServer().getPluginManager().isPluginEnabled("WorldGuard"))) {
/*  835: 929 */         if (WorldGuardHandler.getWorldGuard().getRegionManager(getSkyBlockWorld()).hasRegion(player + "Island")) {
/*  836: 930 */           WorldGuardHandler.getWorldGuard().getRegionManager(getSkyBlockWorld()).removeRegion(player + "Island");
/*  837:     */         }
/*  838:     */       }
/*  839: 932 */       this.orphaned.push(pi.getIslandLocation());
/*  840: 933 */       removeIsland(pi.getIslandLocation());
/*  841: 934 */       deleteIslandConfig(pi.locationForParty());
/*  842: 935 */       pi.removeFromIsland();
/*  843: 936 */       saveOrphans();
/*  844: 937 */       pi.savePlayerConfig(player);
/*  845:     */     }
/*  846:     */     else
/*  847:     */     {
/*  848: 940 */       if ((Settings.island_protectWithWorldGuard) && (Bukkit.getServer().getPluginManager().isPluginEnabled("WorldGuard"))) {
/*  849: 942 */         if (WorldGuardHandler.getWorldGuard().getRegionManager(getSkyBlockWorld()).hasRegion(player + "Island")) {
/*  850: 943 */           WorldGuardHandler.getWorldGuard().getRegionManager(getSkyBlockWorld()).removeRegion(player + "Island");
/*  851:     */         }
/*  852:     */       }
/*  853: 945 */       this.orphaned.push(((PlayerInfo)getActivePlayers().get(player)).getIslandLocation());
/*  854: 946 */       removeIsland(((PlayerInfo)getActivePlayers().get(player)).getIslandLocation());
/*  855: 947 */       deleteIslandConfig(((PlayerInfo)getActivePlayers().get(player)).locationForParty());
/*  856: 948 */       PlayerInfo pi = new PlayerInfo(player);
/*  857: 949 */       pi.removeFromIsland();
/*  858:     */       
/*  859: 951 */       addActivePlayer(player, pi);
/*  860: 952 */       saveOrphans();
/*  861:     */     }
/*  862:     */   }
/*  863:     */   
/*  864:     */   public void restartPlayerIsland(Player player, Location next)
/*  865:     */   {
/*  866: 959 */     boolean hasIslandNow = false;
/*  867: 960 */     if ((next.getBlockX() == 0) && (next.getBlockZ() == 0)) {
/*  868: 962 */       return;
/*  869:     */     }
/*  870: 964 */     removeIsland(next);
/*  871: 965 */     if ((getInstance().getSchemFile().length > 0) && (Bukkit.getServer().getPluginManager().isPluginEnabled("WorldEdit")))
/*  872:     */     {
/*  873: 967 */       String cSchem = "";
/*  874: 968 */       for (int i = 0; i < getInstance().getSchemFile().length; i++) {
/*  875: 970 */         if (!hasIslandNow)
/*  876:     */         {
/*  877: 972 */           if (getInstance().getSchemFile()[i].getName().lastIndexOf('.') > 0) {
/*  878: 974 */             cSchem = getInstance().getSchemFile()[i].getName().substring(0, getInstance().getSchemFile()[i].getName().lastIndexOf('.'));
/*  879:     */           } else {
/*  880: 976 */             cSchem = getInstance().getSchemFile()[i].getName();
/*  881:     */           }
/*  882: 978 */           if (VaultHandler.checkPerk(player.getName(), "usb.schematic." + cSchem, getSkyBlockWorld())) {
/*  883:     */             try
/*  884:     */             {
/*  885: 981 */               if (WorldEditHandler.loadIslandSchematic(getSkyBlockWorld(), getInstance().getSchemFile()[i], next))
/*  886:     */               {
/*  887: 983 */                 setChest(next, player);
/*  888: 984 */                 hasIslandNow = true;
/*  889:     */               }
/*  890:     */             }
/*  891:     */             catch (MaxChangedBlocksException e)
/*  892:     */             {
/*  893: 987 */               e.printStackTrace();
/*  894:     */             }
/*  895:     */             catch (DataException e)
/*  896:     */             {
/*  897: 989 */               e.printStackTrace();
/*  898:     */             }
/*  899:     */             catch (IOException e)
/*  900:     */             {
/*  901: 991 */               e.printStackTrace();
/*  902:     */             }
/*  903:     */           }
/*  904:     */         }
/*  905:     */       }
/*  906: 996 */       if (!hasIslandNow) {
/*  907: 998 */         for (int i = 0; i < getInstance().getSchemFile().length; i++)
/*  908:     */         {
/*  909:1000 */           if (getInstance().getSchemFile()[i].getName().lastIndexOf('.') > 0) {
/*  910:1002 */             cSchem = getInstance().getSchemFile()[i].getName().substring(0, getInstance().getSchemFile()[i].getName().lastIndexOf('.'));
/*  911:     */           } else {
/*  912:1004 */             cSchem = getInstance().getSchemFile()[i].getName();
/*  913:     */           }
/*  914:1005 */           if (cSchem.equalsIgnoreCase(Settings.island_schematicName)) {
/*  915:     */             try
/*  916:     */             {
/*  917:1008 */               if (WorldEditHandler.loadIslandSchematic(getSkyBlockWorld(), getInstance().getSchemFile()[i], next))
/*  918:     */               {
/*  919:1010 */                 setChest(next, player);
/*  920:1011 */                 hasIslandNow = true;
/*  921:     */               }
/*  922:     */             }
/*  923:     */             catch (MaxChangedBlocksException e)
/*  924:     */             {
/*  925:1014 */               e.printStackTrace();
/*  926:     */             }
/*  927:     */             catch (DataException e)
/*  928:     */             {
/*  929:1016 */               e.printStackTrace();
/*  930:     */             }
/*  931:     */             catch (IOException e)
/*  932:     */             {
/*  933:1018 */               e.printStackTrace();
/*  934:     */             }
/*  935:     */           }
/*  936:     */         }
/*  937:     */       }
/*  938:     */     }
/*  939:1024 */     if (!hasIslandNow) {
/*  940:1026 */       if (!Settings.island_useOldIslands) {
/*  941:1027 */         generateIslandBlocks(next.getBlockX(), next.getBlockZ(), player, getSkyBlockWorld());
/*  942:     */       } else {
/*  943:1029 */         oldGenerateIslandBlocks(next.getBlockX(), next.getBlockZ(), player, getSkyBlockWorld());
/*  944:     */       }
/*  945:     */     }
/*  946:1031 */     next.setY(Settings.island_height);
/*  947:1032 */     System.out.println(next.getBlockY());
/*  948:     */     
/*  949:1034 */     setNewPlayerIsland(player, next);
/*  950:1035 */     player.getInventory().clear();
/*  951:1036 */     player.getEquipment().clear();
/*  952:1037 */     getInstance().changePlayerBiome(player, "OCEAN");
/*  953:1038 */     for (int x = Settings.island_protectionRange / 2 * -1 - 16; x <= Settings.island_protectionRange / 2 + 16; x += 16) {
/*  954:1039 */       for (int z = Settings.island_protectionRange / 2 * -1 - 16; z <= Settings.island_protectionRange / 2 + 16; z += 16) {
/*  955:1040 */         getSkyBlockWorld().refreshChunk((next.getBlockX() + x) / 16, (next.getBlockZ() + z) / 16);
/*  956:     */       }
/*  957:     */     }
/*  958:1043 */     Iterator<Entity> ents = player.getNearbyEntities(Settings.island_protectionRange / 2, 250.0D, Settings.island_protectionRange / 2).iterator();
/*  959:1044 */     while (ents.hasNext())
/*  960:     */     {
/*  961:1046 */       Entity tempent = (Entity)ents.next();
/*  962:1047 */       if (!(tempent instanceof Player)) {
/*  963:1052 */         tempent.remove();
/*  964:     */       }
/*  965:     */     }
/*  966:     */   }
/*  967:     */   
/*  968:     */   public void devDeletePlayerIsland(String player)
/*  969:     */   {
/*  970:1058 */     if (!getActivePlayers().containsKey(player))
/*  971:     */     {
/*  972:1060 */       PlayerInfo pi = new PlayerInfo(player);
/*  973:1061 */       if ((Settings.island_protectWithWorldGuard) && (Bukkit.getServer().getPluginManager().isPluginEnabled("WorldGuard"))) {
/*  974:1063 */         if (WorldGuardHandler.getWorldGuard().getRegionManager(getSkyBlockWorld()).hasRegion(player + "Island")) {
/*  975:1064 */           WorldGuardHandler.getWorldGuard().getRegionManager(getSkyBlockWorld()).removeRegion(player + "Island");
/*  976:     */         }
/*  977:     */       }
/*  978:1067 */       pi = new PlayerInfo(player);
/*  979:1068 */       pi.savePlayerConfig(player);
/*  980:     */     }
/*  981:     */     else
/*  982:     */     {
/*  983:1071 */       if ((Settings.island_protectWithWorldGuard) && (Bukkit.getServer().getPluginManager().isPluginEnabled("WorldGuard"))) {
/*  984:1073 */         if (WorldGuardHandler.getWorldGuard().getRegionManager(getSkyBlockWorld()).hasRegion(player + "Island")) {
/*  985:1074 */           WorldGuardHandler.getWorldGuard().getRegionManager(getSkyBlockWorld()).removeRegion(player + "Island");
/*  986:     */         }
/*  987:     */       }
/*  988:1076 */       PlayerInfo pi = new PlayerInfo(player);
/*  989:1077 */       removeActivePlayer(player);
/*  990:1078 */       addActivePlayer(player, pi);
/*  991:     */     }
/*  992:     */   }
/*  993:     */   
/*  994:     */   public boolean devSetPlayerIsland(Player sender, Location l, String player)
/*  995:     */   {
/*  996:1085 */     if (!getActivePlayers().containsKey(player))
/*  997:     */     {
/*  998:1087 */       PlayerInfo pi = new PlayerInfo(player);
/*  999:1088 */       int px = l.getBlockX();
/* 1000:1089 */       int py = l.getBlockY();
/* 1001:1090 */       int pz = l.getBlockZ();
/* 1002:1091 */       for (int x = -10; x <= 10; x++) {
/* 1003:1092 */         for (int y = -10; y <= 10; y++) {
/* 1004:1093 */           for (int z = -10; z <= 10; z++)
/* 1005:     */           {
/* 1006:1094 */             Block b = new Location(l.getWorld(), px + x, py + y, pz + z).getBlock();
/* 1007:1095 */             if (b.getTypeId() == 7)
/* 1008:     */             {
/* 1009:1097 */               pi.setHomeLocation(new Location(l.getWorld(), px + x, py + y + 3, pz + z));
/* 1010:1098 */               pi.setHasIsland(true);
/* 1011:1099 */               pi.setIslandLocation(b.getLocation());
/* 1012:1100 */               pi.savePlayerConfig(player);
/* 1013:1101 */               getInstance().createIslandConfig(pi.locationForParty(), player);
/* 1014:1102 */               getInstance().clearIslandConfig(pi.locationForParty(), player);
/* 1015:1103 */               if ((Settings.island_protectWithWorldGuard) && (Bukkit.getServer().getPluginManager().isPluginEnabled("WorldGuard"))) {
/* 1016:1104 */                 WorldGuardHandler.protectIsland(sender, player, pi);
/* 1017:     */               }
/* 1018:1105 */               getInstance().getIslandConfig(pi.locationForParty());
/* 1019:1106 */               return true;
/* 1020:     */             }
/* 1021:     */           }
/* 1022:     */         }
/* 1023:     */       }
/* 1024:     */     }
/* 1025:     */     else
/* 1026:     */     {
/* 1027:1113 */       int px = l.getBlockX();
/* 1028:1114 */       int py = l.getBlockY();
/* 1029:1115 */       int pz = l.getBlockZ();
/* 1030:1116 */       for (int x = -10; x <= 10; x++) {
/* 1031:1117 */         for (int y = -10; y <= 10; y++) {
/* 1032:1118 */           for (int z = -10; z <= 10; z++)
/* 1033:     */           {
/* 1034:1119 */             Block b = new Location(l.getWorld(), px + x, py + y, pz + z).getBlock();
/* 1035:1120 */             if (b.getTypeId() == 7)
/* 1036:     */             {
/* 1037:1122 */               ((PlayerInfo)getActivePlayers().get(player)).setHomeLocation(new Location(l.getWorld(), px + x, py + y + 3, pz + z));
/* 1038:1123 */               ((PlayerInfo)getActivePlayers().get(player)).setHasIsland(true);
/* 1039:1124 */               ((PlayerInfo)getActivePlayers().get(player)).setIslandLocation(b.getLocation());
/* 1040:1125 */               PlayerInfo pi = (PlayerInfo)getActivePlayers().get(player);
/* 1041:1126 */               removeActivePlayer(player);
/* 1042:1127 */               addActivePlayer(player, pi);
/* 1043:1128 */               if ((Settings.island_protectWithWorldGuard) && (Bukkit.getServer().getPluginManager().isPluginEnabled("WorldGuard"))) {
/* 1044:1129 */                 WorldGuardHandler.protectIsland(sender, player, pi);
/* 1045:     */               }
/* 1046:1130 */               return true;
/* 1047:     */             }
/* 1048:     */           }
/* 1049:     */         }
/* 1050:     */       }
/* 1051:     */     }
/* 1052:1136 */     return false;
/* 1053:     */   }
/* 1054:     */   
/* 1055:     */   public int orphanCount()
/* 1056:     */   {
/* 1057:1141 */     return this.orphaned.size();
/* 1058:     */   }
/* 1059:     */   
/* 1060:     */   public void removeIsland(Location loc)
/* 1061:     */   {
/* 1062:1150 */     if (loc != null)
/* 1063:     */     {
/* 1064:1151 */       Location l = loc;
/* 1065:1152 */       int px = l.getBlockX();
/* 1066:1153 */       int py = l.getBlockY();
/* 1067:1154 */       int pz = l.getBlockZ();
/* 1068:1155 */       for (int x = Settings.island_protectionRange / 2 * -1; x <= Settings.island_protectionRange / 2; x++) {
/* 1069:1156 */         for (int y = 0; y <= 255; y++) {
/* 1070:1157 */           for (int z = Settings.island_protectionRange / 2 * -1; z <= Settings.island_protectionRange / 2; z++)
/* 1071:     */           {
/* 1072:1158 */             Block b = new Location(l.getWorld(), px + x, py + y, pz + z).getBlock();
/* 1073:1159 */             if (!b.getType().equals(Material.AIR))
/* 1074:     */             {
/* 1075:1160 */               if (b.getType().equals(Material.CHEST))
/* 1076:     */               {
/* 1077:1161 */                 Chest c = (Chest)b.getState();
/* 1078:1162 */                 ItemStack[] items = new ItemStack[c.getInventory().getContents().length];
/* 1079:1163 */                 c.getInventory().setContents(items);
/* 1080:     */               }
/* 1081:1164 */               else if (b.getType().equals(Material.FURNACE))
/* 1082:     */               {
/* 1083:1165 */                 Furnace f = (Furnace)b.getState();
/* 1084:1166 */                 ItemStack[] items = new ItemStack[f.getInventory().getContents().length];
/* 1085:1167 */                 f.getInventory().setContents(items);
/* 1086:     */               }
/* 1087:1168 */               else if (b.getType().equals(Material.DISPENSER))
/* 1088:     */               {
/* 1089:1169 */                 Dispenser d = (Dispenser)b.getState();
/* 1090:1170 */                 ItemStack[] items = new ItemStack[d.getInventory().getContents().length];
/* 1091:1171 */                 d.getInventory().setContents(items);
/* 1092:     */               }
/* 1093:1173 */               b.setType(Material.AIR);
/* 1094:     */             }
/* 1095:     */           }
/* 1096:     */         }
/* 1097:     */       }
/* 1098:     */     }
/* 1099:     */   }
/* 1100:     */   
/* 1101:     */   public void removeIslandBlocks(Location loc)
/* 1102:     */   {
/* 1103:1182 */     if (loc != null)
/* 1104:     */     {
/* 1105:1183 */       System.out.print("Removing blocks from an abandoned island.");
/* 1106:1184 */       Location l = loc;
/* 1107:1185 */       int px = l.getBlockX();
/* 1108:1186 */       int py = l.getBlockY();
/* 1109:1187 */       int pz = l.getBlockZ();
/* 1110:1188 */       for (int x = -20; x <= 20; x++) {
/* 1111:1189 */         for (int y = -20; y <= 20; y++) {
/* 1112:1190 */           for (int z = -20; z <= 20; z++)
/* 1113:     */           {
/* 1114:1191 */             Block b = new Location(l.getWorld(), px + x, py + y, pz + z).getBlock();
/* 1115:1192 */             if (!b.getType().equals(Material.AIR))
/* 1116:     */             {
/* 1117:1193 */               if (b.getType().equals(Material.CHEST))
/* 1118:     */               {
/* 1119:1194 */                 Chest c = (Chest)b.getState();
/* 1120:1195 */                 ItemStack[] items = new ItemStack[c.getInventory().getContents().length];
/* 1121:1196 */                 c.getInventory().setContents(items);
/* 1122:     */               }
/* 1123:1197 */               else if (b.getType().equals(Material.FURNACE))
/* 1124:     */               {
/* 1125:1198 */                 Furnace f = (Furnace)b.getState();
/* 1126:1199 */                 ItemStack[] items = new ItemStack[f.getInventory().getContents().length];
/* 1127:1200 */                 f.getInventory().setContents(items);
/* 1128:     */               }
/* 1129:1201 */               else if (b.getType().equals(Material.DISPENSER))
/* 1130:     */               {
/* 1131:1202 */                 Dispenser d = (Dispenser)b.getState();
/* 1132:1203 */                 ItemStack[] items = new ItemStack[d.getInventory().getContents().length];
/* 1133:1204 */                 d.getInventory().setContents(items);
/* 1134:     */               }
/* 1135:1206 */               b.setType(Material.AIR);
/* 1136:     */             }
/* 1137:     */           }
/* 1138:     */         }
/* 1139:     */       }
/* 1140:     */     }
/* 1141:     */   }
/* 1142:     */   
/* 1143:     */   public boolean hasParty(String playername)
/* 1144:     */   {
/* 1145:1230 */     if (getActivePlayers().containsKey(playername))
/* 1146:     */     {
/* 1147:1232 */       if (getIslandConfig(((PlayerInfo)getActivePlayers().get(playername)).locationForParty()).getInt("party.currentSize") > 1) {
/* 1148:1233 */         return true;
/* 1149:     */       }
/* 1150:1235 */       return false;
/* 1151:     */     }
/* 1152:1238 */     PlayerInfo pi = new PlayerInfo(playername);
/* 1153:1239 */     if (!pi.getHasIsland()) {
/* 1154:1240 */       return false;
/* 1155:     */     }
/* 1156:1241 */     if (getTempIslandConfig(pi.locationForParty()).getInt("party.currentSize") > 1) {
/* 1157:1242 */       return true;
/* 1158:     */     }
/* 1159:1244 */     return false;
/* 1160:     */   }
/* 1161:     */   
/* 1162:     */   public Location getLastIsland()
/* 1163:     */   {
/* 1164:1260 */     if (this.lastIsland.getWorld().getName().equalsIgnoreCase(Settings.general_worldName)) {
/* 1165:1261 */       return this.lastIsland;
/* 1166:     */     }
/* 1167:1264 */     setLastIsland(new Location(getSkyBlockWorld(), 0.0D, Settings.island_height, 0.0D));
/* 1168:1265 */     return new Location(getSkyBlockWorld(), 0.0D, Settings.island_height, 0.0D);
/* 1169:     */   }
/* 1170:     */   
/* 1171:     */   public void setLastIsland(Location island)
/* 1172:     */   {
/* 1173:1273 */     getLastIslandConfig().set("options.general.lastIslandX", Integer.valueOf(island.getBlockX()));
/* 1174:1274 */     getLastIslandConfig().set("options.general.lastIslandZ", Integer.valueOf(island.getBlockZ()));
/* 1175:1275 */     saveLastIslandConfig();
/* 1176:1276 */     this.lastIsland = island;
/* 1177:     */   }
/* 1178:     */   
/* 1179:     */   public boolean hasOrphanedIsland()
/* 1180:     */   {
/* 1181:1285 */     return !this.orphaned.empty();
/* 1182:     */   }
/* 1183:     */   
/* 1184:     */   public Location checkOrphan()
/* 1185:     */   {
/* 1186:1290 */     return (Location)this.orphaned.peek();
/* 1187:     */   }
/* 1188:     */   
/* 1189:     */   public Location getOrphanedIsland()
/* 1190:     */   {
/* 1191:1294 */     if (hasOrphanedIsland()) {
/* 1192:1295 */       return (Location)this.orphaned.pop();
/* 1193:     */     }
/* 1194:1298 */     return null;
/* 1195:     */   }
/* 1196:     */   
/* 1197:     */   public void addOrphan(Location island)
/* 1198:     */   {
/* 1199:1303 */     this.orphaned.push(island);
/* 1200:     */   }
/* 1201:     */   
/* 1202:     */   public void removeNextOrphan()
/* 1203:     */   {
/* 1204:1308 */     this.orphaned.pop();
/* 1205:     */   }
/* 1206:     */   
/* 1207:     */   public void saveOrphans()
/* 1208:     */   {
/* 1209:1315 */     String fullOrphan = "";
/* 1210:1316 */     this.tempOrphaned = ((Stack)this.orphaned.clone());
/* 1211:1319 */     while (!this.tempOrphaned.isEmpty()) {
/* 1212:1321 */       this.reverseOrphaned.push((Location)this.tempOrphaned.pop());
/* 1213:     */     }
/* 1214:1323 */     while (!this.reverseOrphaned.isEmpty())
/* 1215:     */     {
/* 1216:1325 */       Location tempLoc = (Location)this.reverseOrphaned.pop();
/* 1217:1326 */       fullOrphan = fullOrphan + tempLoc.getBlockX() + "," + tempLoc.getBlockZ() + ";";
/* 1218:     */     }
/* 1219:1329 */     getOrphans().set("orphans.list", fullOrphan);
/* 1220:1330 */     saveOrphansFile();
/* 1221:     */   }
/* 1222:     */   
/* 1223:     */   public void setupOrphans()
/* 1224:     */   {
/* 1225:1343 */     if (getOrphans().contains("orphans.list"))
/* 1226:     */     {
/* 1227:1345 */       String fullOrphan = getOrphans().getString("orphans.list");
/* 1228:1346 */       if (!fullOrphan.isEmpty())
/* 1229:     */       {
/* 1230:1348 */         String[] orphanArray = fullOrphan.split(";");
/* 1231:     */         
/* 1232:     */ 
/* 1233:1351 */         this.orphaned = new Stack();
/* 1234:1352 */         for (int i = 0; i < orphanArray.length; i++)
/* 1235:     */         {
/* 1236:1354 */           String[] orphanXY = orphanArray[i].split(",");
/* 1237:1355 */           Location tempLoc = new Location(getSkyBlockWorld(), Integer.parseInt(orphanXY[0]), Settings.island_height, Integer.parseInt(orphanXY[1]));
/* 1238:1356 */           this.orphaned.push(tempLoc);
/* 1239:     */         }
/* 1240:     */       }
/* 1241:     */     }
/* 1242:     */   }
/* 1243:     */   
/* 1244:     */   public boolean homeTeleport(Player player)
/* 1245:     */   {
/* 1246:1376 */     Location homeSweetHome = null;
/* 1247:1377 */     if (getActivePlayers().containsKey(player.getName())) {
/* 1248:1379 */       homeSweetHome = getInstance().getSafeHomeLocation((PlayerInfo)getActivePlayers().get(player.getName()));
/* 1249:     */     }
/* 1250:1383 */     if (homeSweetHome == null)
/* 1251:     */     {
/* 1252:1384 */       player.performCommand("spawn");
/* 1253:1385 */       player.sendMessage(ChatColor.RED + "You are not part of an island. Returning you the spawn area!");
/* 1254:1386 */       return true;
/* 1255:     */     }
/* 1256:1389 */     getInstance().removeCreatures(homeSweetHome);
/* 1257:1390 */     player.teleport(homeSweetHome);
/* 1258:1391 */     player.sendMessage(ChatColor.GREEN + "Teleporting you to your island.");
/* 1259:1392 */     return true;
/* 1260:     */   }
/* 1261:     */   
/* 1262:     */   public boolean warpTeleport(Player player, PlayerInfo pi)
/* 1263:     */   {
/* 1264:1396 */     Location warpSweetWarp = null;
/* 1265:1397 */     if (pi == null)
/* 1266:     */     {
/* 1267:1399 */       player.sendMessage(ChatColor.RED + "That player does not exist!");
/* 1268:1400 */       return true;
/* 1269:     */     }
/* 1270:1402 */     warpSweetWarp = getInstance().getSafeWarpLocation(pi);
/* 1271:1406 */     if (warpSweetWarp == null)
/* 1272:     */     {
/* 1273:1407 */       player.sendMessage(ChatColor.RED + "Unable to warp you to that player's island!");
/* 1274:1408 */       return true;
/* 1275:     */     }
/* 1276:1412 */     player.teleport(warpSweetWarp);
/* 1277:1413 */     player.sendMessage(ChatColor.GREEN + "Teleporting you to " + pi.getPlayerName() + "'s island.");
/* 1278:1414 */     return true;
/* 1279:     */   }
/* 1280:     */   
/* 1281:     */   public boolean homeSet(Player player)
/* 1282:     */   {
/* 1283:1424 */     if (!player.getWorld().getName().equalsIgnoreCase(getSkyBlockWorld().getName()))
/* 1284:     */     {
/* 1285:1425 */       player.sendMessage(ChatColor.RED + "You must be closer to your island to set your skyblock home!");
/* 1286:1426 */       return true;
/* 1287:     */     }
/* 1288:1428 */     if (playerIsOnIsland(player))
/* 1289:     */     {
/* 1290:1430 */       if (getActivePlayers().containsKey(player.getName())) {
/* 1291:1432 */         ((PlayerInfo)getActivePlayers().get(player.getName())).setHomeLocation(player.getLocation());
/* 1292:     */       }
/* 1293:1435 */       player.sendMessage(ChatColor.GREEN + "Your skyblock home has been set to your current location.");
/* 1294:1436 */       return true;
/* 1295:     */     }
/* 1296:1438 */     player.sendMessage(ChatColor.RED + "You must be closer to your island to set your skyblock home!");
/* 1297:1439 */     return true;
/* 1298:     */   }
/* 1299:     */   
/* 1300:     */   public boolean warpSet(Player player)
/* 1301:     */   {
/* 1302:1444 */     if (!player.getWorld().getName().equalsIgnoreCase(getSkyBlockWorld().getName()))
/* 1303:     */     {
/* 1304:1445 */       player.sendMessage(ChatColor.RED + "You must be closer to your island to set your warp!");
/* 1305:1446 */       return true;
/* 1306:     */     }
/* 1307:1448 */     if (playerIsOnIsland(player))
/* 1308:     */     {
/* 1309:1450 */       if (getActivePlayers().containsKey(player.getName())) {
/* 1310:1452 */         setWarpLocation(((PlayerInfo)getActivePlayers().get(player.getName())).locationForParty(), player.getLocation());
/* 1311:     */       }
/* 1312:1455 */       player.sendMessage(ChatColor.GREEN + "Your skyblock incoming warp has been set to your current location.");
/* 1313:1456 */       return true;
/* 1314:     */     }
/* 1315:1458 */     player.sendMessage(ChatColor.RED + "You must be closer to your island to set your warp!");
/* 1316:1459 */     return true;
/* 1317:     */   }
/* 1318:     */   
/* 1319:     */   public boolean homeSet(String player, Location loc)
/* 1320:     */   {
/* 1321:1464 */     if (getActivePlayers().containsKey(player))
/* 1322:     */     {
/* 1323:1466 */       ((PlayerInfo)getActivePlayers().get(player)).setHomeLocation(loc);
/* 1324:     */     }
/* 1325:     */     else
/* 1326:     */     {
/* 1327:1469 */       PlayerInfo pi = new PlayerInfo(player);
/* 1328:1470 */       pi.setHomeLocation(loc);
/* 1329:1471 */       pi.savePlayerConfig(player);
/* 1330:     */     }
/* 1331:1474 */     return true;
/* 1332:     */   }
/* 1333:     */   
/* 1334:     */   public boolean playerIsOnIsland(Player player)
/* 1335:     */   {
/* 1336:1479 */     if (getActivePlayers().containsKey(player.getName()))
/* 1337:     */     {
/* 1338:1481 */       if (((PlayerInfo)getActivePlayers().get(player.getName())).getHasIsland()) {
/* 1339:1483 */         this.islandTestLocation = ((PlayerInfo)getActivePlayers().get(player.getName())).getIslandLocation();
/* 1340:     */       }
/* 1341:1485 */       if (this.islandTestLocation == null) {
/* 1342:1486 */         return false;
/* 1343:     */       }
/* 1344:1487 */       if ((player.getLocation().getX() > this.islandTestLocation.getX() - Settings.island_protectionRange / 2) && (player.getLocation().getX() < this.islandTestLocation.getX() + Settings.island_protectionRange / 2) && 
/* 1345:1488 */         (player.getLocation().getZ() > this.islandTestLocation.getZ() - Settings.island_protectionRange / 2) && (player.getLocation().getZ() < this.islandTestLocation.getZ() + Settings.island_protectionRange / 2)) {
/* 1346:1489 */         return true;
/* 1347:     */       }
/* 1348:     */     }
/* 1349:1491 */     return false;
/* 1350:     */   }
/* 1351:     */   
/* 1352:     */   public boolean locationIsOnIsland(Player player, Location loc)
/* 1353:     */   {
/* 1354:1496 */     if (getActivePlayers().containsKey(player.getName()))
/* 1355:     */     {
/* 1356:1498 */       if (((PlayerInfo)getActivePlayers().get(player.getName())).getHasIsland()) {
/* 1357:1500 */         this.islandTestLocation = ((PlayerInfo)getActivePlayers().get(player.getName())).getIslandLocation();
/* 1358:     */       }
/* 1359:1502 */       if (this.islandTestLocation == null) {
/* 1360:1503 */         return false;
/* 1361:     */       }
/* 1362:1504 */       if ((loc.getX() > this.islandTestLocation.getX() - Settings.island_protectionRange / 2) && (loc.getX() < this.islandTestLocation.getX() + Settings.island_protectionRange / 2) && 
/* 1363:1505 */         (loc.getZ() > this.islandTestLocation.getZ() - Settings.island_protectionRange / 2) && (loc.getZ() < this.islandTestLocation.getZ() + Settings.island_protectionRange / 2)) {
/* 1364:1506 */         return true;
/* 1365:     */       }
/* 1366:     */     }
/* 1367:1508 */     return false;
/* 1368:     */   }
/* 1369:     */   
/* 1370:     */   public boolean playerIsInSpawn(Player player)
/* 1371:     */   {
/* 1372:1513 */     if ((player.getLocation().getX() > Settings.general_spawnSize * -1) && (player.getLocation().getX() < Settings.general_spawnSize) && (player.getLocation().getZ() > Settings.general_spawnSize * -1) && (player.getLocation().getZ() < Settings.general_spawnSize)) {
/* 1373:1514 */       return true;
/* 1374:     */     }
/* 1375:1515 */     return false;
/* 1376:     */   }
/* 1377:     */   
/* 1378:     */   public boolean hasIsland(String playername)
/* 1379:     */   {
/* 1380:1520 */     if (getActivePlayers().containsKey(playername)) {
/* 1381:1522 */       return ((PlayerInfo)getActivePlayers().get(playername)).getHasIsland();
/* 1382:     */     }
/* 1383:1525 */     PlayerInfo pi = new PlayerInfo(playername);
/* 1384:1526 */     return pi.getHasIsland();
/* 1385:     */   }
/* 1386:     */   
/* 1387:     */   public Location getPlayerIsland(String playername)
/* 1388:     */   {
/* 1389:1532 */     if (getActivePlayers().containsKey(playername)) {
/* 1390:1534 */       return ((PlayerInfo)getActivePlayers().get(playername)).getIslandLocation();
/* 1391:     */     }
/* 1392:1537 */     PlayerInfo pi = new PlayerInfo(playername);
/* 1393:1538 */     if (!pi.getHasIsland()) {
/* 1394:1539 */       return null;
/* 1395:     */     }
/* 1396:1540 */     return pi.getIslandLocation();
/* 1397:     */   }
/* 1398:     */   
/* 1399:     */   public boolean islandAtLocation(Location loc)
/* 1400:     */   {
/* 1401:1570 */     if (loc == null) {
/* 1402:1572 */       return true;
/* 1403:     */     }
/* 1404:1574 */     int px = loc.getBlockX();
/* 1405:1575 */     int py = loc.getBlockY();
/* 1406:1576 */     int pz = loc.getBlockZ();
/* 1407:1577 */     for (int x = -2; x <= 2; x++) {
/* 1408:1578 */       for (int y = -50; y <= 50; y++) {
/* 1409:1579 */         for (int z = -2; z <= 2; z++)
/* 1410:     */         {
/* 1411:1580 */           Block b = new Location(loc.getWorld(), px + x, py + y, pz + z).getBlock();
/* 1412:1581 */           if (b.getTypeId() != 0) {
/* 1413:1582 */             return true;
/* 1414:     */           }
/* 1415:     */         }
/* 1416:     */       }
/* 1417:     */     }
/* 1418:1586 */     return false;
/* 1419:     */   }
/* 1420:     */   
/* 1421:     */   public boolean islandInSpawn(Location loc)
/* 1422:     */   {
/* 1423:1591 */     if (loc == null) {
/* 1424:1593 */       return true;
/* 1425:     */     }
/* 1426:1595 */     if ((loc.getX() > -50.0D) && (loc.getX() < 50.0D) && (loc.getZ() > -50.0D) && (loc.getZ() < 50.0D)) {
/* 1427:1596 */       return true;
/* 1428:     */     }
/* 1429:1597 */     return false;
/* 1430:     */   }
/* 1431:     */   
/* 1432:     */   public ChunkGenerator getDefaultWorldGenerator(String worldName, String id)
/* 1433:     */   {
/* 1434:1602 */     return new SkyBlockChunkGenerator();
/* 1435:     */   }
/* 1436:     */   
/* 1437:     */   public Stack<SerializableLocation> changeStackToFile(Stack<Location> stack)
/* 1438:     */   {
/* 1439:1607 */     Stack<SerializableLocation> finishStack = new Stack();
/* 1440:1608 */     Stack<Location> tempStack = new Stack();
/* 1441:1609 */     while (!stack.isEmpty()) {
/* 1442:1610 */       tempStack.push((Location)stack.pop());
/* 1443:     */     }
/* 1444:1611 */     while (!tempStack.isEmpty()) {
/* 1445:1613 */       if (tempStack.peek() != null) {
/* 1446:1615 */         finishStack.push(new SerializableLocation((Location)tempStack.pop()));
/* 1447:     */       } else {
/* 1448:1617 */         tempStack.pop();
/* 1449:     */       }
/* 1450:     */     }
/* 1451:1619 */     return finishStack;
/* 1452:     */   }
/* 1453:     */   
/* 1454:     */   public Stack<Location> changestackfromfile(Stack<SerializableLocation> stack)
/* 1455:     */   {
/* 1456:1623 */     Stack<SerializableLocation> tempStack = new Stack();
/* 1457:1624 */     Stack<Location> finishStack = new Stack();
/* 1458:1625 */     while (!stack.isEmpty()) {
/* 1459:1626 */       tempStack.push((SerializableLocation)stack.pop());
/* 1460:     */     }
/* 1461:1627 */     while (!tempStack.isEmpty()) {
/* 1462:1629 */       if (tempStack.peek() != null) {
/* 1463:1630 */         finishStack.push(((SerializableLocation)tempStack.pop()).getLocation());
/* 1464:     */       } else {
/* 1465:1632 */         tempStack.pop();
/* 1466:     */       }
/* 1467:     */     }
/* 1468:1634 */     return finishStack;
/* 1469:     */   }
/* 1470:     */   
/* 1471:     */   public boolean largeIsland(Location l)
/* 1472:     */   {
/* 1473:1640 */     int blockcount = 0;
/* 1474:1641 */     int px = l.getBlockX();
/* 1475:1642 */     int py = l.getBlockY();
/* 1476:1643 */     int pz = l.getBlockZ();
/* 1477:1644 */     for (int x = -30; x <= 30; x++) {
/* 1478:1645 */       for (int y = -30; y <= 30; y++) {
/* 1479:1646 */         for (int z = -30; z <= 30; z++)
/* 1480:     */         {
/* 1481:1647 */           Block b = new Location(l.getWorld(), px + x, py + y, pz + z).getBlock();
/* 1482:1648 */           if ((b.getTypeId() != 0) && (b.getTypeId() != 8) && (b.getTypeId() != 10)) {
/* 1483:1650 */             if (blockcount > 200) {
/* 1484:1652 */               return true;
/* 1485:     */             }
/* 1486:     */           }
/* 1487:     */         }
/* 1488:     */       }
/* 1489:     */     }
/* 1490:1658 */     if (blockcount > 200) {
/* 1491:1660 */       return true;
/* 1492:     */     }
/* 1493:1662 */     return false;
/* 1494:     */   }
/* 1495:     */   
/* 1496:     */   public boolean clearAbandoned()
/* 1497:     */   {
/* 1498:1670 */     int numOffline = 0;
/* 1499:1671 */     OfflinePlayer[] oplayers = Bukkit.getServer().getOfflinePlayers();
/* 1500:1672 */     System.out.print("Attemping to add more orphans");
/* 1501:1673 */     for (int i = 0; i < oplayers.length; i++)
/* 1502:     */     {
/* 1503:1675 */       long offlineTime = oplayers[i].getLastPlayed();
/* 1504:1676 */       offlineTime = (System.currentTimeMillis() - offlineTime) / 3600000L;
/* 1505:1677 */       if ((offlineTime > 250L) && (getInstance().hasIsland(oplayers[i].getName())) && (offlineTime < 50000L))
/* 1506:     */       {
/* 1507:1679 */         PlayerInfo pi = new PlayerInfo(oplayers[i].getName());
/* 1508:1680 */         Location l = pi.getIslandLocation();
/* 1509:1681 */         int blockcount = 0;
/* 1510:1682 */         int px = l.getBlockX();
/* 1511:1683 */         int py = l.getBlockY();
/* 1512:1684 */         int pz = l.getBlockZ();
/* 1513:1685 */         for (int x = -30; x <= 30; x++) {
/* 1514:1686 */           for (int y = -30; y <= 30; y++) {
/* 1515:1687 */             for (int z = -30; z <= 30; z++)
/* 1516:     */             {
/* 1517:1688 */               Block b = new Location(l.getWorld(), px + x, py + y, pz + z).getBlock();
/* 1518:1689 */               if ((b.getTypeId() != 0) && (b.getTypeId() != 8) && (b.getTypeId() != 10)) {
/* 1519:1691 */                 blockcount++;
/* 1520:     */               }
/* 1521:     */             }
/* 1522:     */           }
/* 1523:     */         }
/* 1524:1696 */         if (blockcount < 200)
/* 1525:     */         {
/* 1526:1699 */           numOffline++;
/* 1527:1700 */           WorldGuardHandler.getWorldGuard().getRegionManager(getSkyBlockWorld()).removeRegion(oplayers[i].getName() + "Island");
/* 1528:1701 */           this.orphaned.push(pi.getIslandLocation());
/* 1529:     */           
/* 1530:1703 */           pi.setHomeLocation(null);
/* 1531:1704 */           pi.setHasIsland(false);
/* 1532:1705 */           pi.setIslandLocation(null);
/* 1533:1706 */           pi.savePlayerConfig(pi.getPlayerName());
/* 1534:     */         }
/* 1535:     */       }
/* 1536:     */     }
/* 1537:1711 */     if (numOffline > 0)
/* 1538:     */     {
/* 1539:1713 */       System.out.print("Added " + numOffline + " new orphans.");
/* 1540:1714 */       saveOrphans();
/* 1541:1715 */       return true;
/* 1542:     */     }
/* 1543:1717 */     System.out.print("No new orphans to add!");
/* 1544:1718 */     return false;
/* 1545:     */   }
/* 1546:     */   
/* 1547:     */   public LinkedHashMap<String, Double> generateTopTen()
/* 1548:     */   {
/* 1549:1723 */     HashMap<String, Double> tempMap = new LinkedHashMap();
/* 1550:1724 */     File folder = this.directoryIslands;
/* 1551:1725 */     File[] listOfFiles = folder.listFiles();
/* 1552:1728 */     for (int i = 0; i < listOfFiles.length; i++) {
/* 1553:1730 */       if (getTempIslandConfig(listOfFiles[i].getName().replaceAll(".yml", "")) != null) {
/* 1554:1732 */         if (getTempIslandConfig(listOfFiles[i].getName().replaceAll(".yml", "")).getInt("general.level") > 0) {
/* 1555:1735 */           tempMap.put(getTempIslandConfig(listOfFiles[i].getName().replaceAll(".yml", "")).getString("party.leader"), Double.valueOf(getTempIslandConfig(listOfFiles[i].getName().replaceAll(".yml", "")).getInt("general.level")));
/* 1556:     */         }
/* 1557:     */       }
/* 1558:     */     }
/* 1559:1744 */     LinkedHashMap<String, Double> sortedMap = sortHashMapByValuesD(tempMap);
/* 1560:1745 */     return sortedMap;
/* 1561:     */   }
/* 1562:     */   
/* 1563:     */   public LinkedHashMap<String, Double> sortHashMapByValuesD(HashMap<String, Double> passedMap)
/* 1564:     */   {
/* 1565:1750 */     List<String> mapKeys = new ArrayList(passedMap.keySet());
/* 1566:1751 */     List<Double> mapValues = new ArrayList(passedMap.values());
/* 1567:1752 */     Collections.sort(mapValues);
/* 1568:1753 */     Collections.reverse(mapValues);
/* 1569:1754 */     Collections.sort(mapKeys);
/* 1570:1755 */     Collections.reverse(mapKeys);
/* 1571:     */     
/* 1572:1757 */     LinkedHashMap<String, Double> sortedMap = 
/* 1573:1758 */       new LinkedHashMap();
/* 1574:     */     
/* 1575:1760 */     Iterator<Double> valueIt = mapValues.iterator();
/* 1576:1761 */     while (valueIt.hasNext())
/* 1577:     */     {
/* 1578:1762 */       Double val = (Double)valueIt.next();
/* 1579:1763 */       Iterator<String> keyIt = mapKeys.iterator();
/* 1580:1765 */       while (keyIt.hasNext())
/* 1581:     */       {
/* 1582:1766 */         String key = (String)keyIt.next();
/* 1583:1767 */         String comp1 = ((Double)passedMap.get(key)).toString();
/* 1584:1768 */         String comp2 = val.toString();
/* 1585:1770 */         if (comp1.equals(comp2))
/* 1586:     */         {
/* 1587:1771 */           passedMap.remove(key);
/* 1588:1772 */           mapKeys.remove(key);
/* 1589:1773 */           sortedMap.put(key, val);
/* 1590:1774 */           break;
/* 1591:     */         }
/* 1592:     */       }
/* 1593:     */     }
/* 1594:1780 */     return sortedMap;
/* 1595:     */   }
/* 1596:     */   
/* 1597:     */   public boolean onInfoCooldown(Player player)
/* 1598:     */   {
/* 1599:1785 */     if (this.infoCooldown.containsKey(player.getName()))
/* 1600:     */     {
/* 1601:1787 */       if (((Long)this.infoCooldown.get(player.getName())).longValue() > Calendar.getInstance().getTimeInMillis()) {
/* 1602:1788 */         return true;
/* 1603:     */       }
/* 1604:1791 */       return false;
/* 1605:     */     }
/* 1606:1794 */     return false;
/* 1607:     */   }
/* 1608:     */   
/* 1609:     */   public boolean onBiomeCooldown(Player player)
/* 1610:     */   {
/* 1611:1799 */     if (this.biomeCooldown.containsKey(player.getName()))
/* 1612:     */     {
/* 1613:1801 */       if (((Long)this.biomeCooldown.get(player.getName())).longValue() > Calendar.getInstance().getTimeInMillis()) {
/* 1614:1802 */         return true;
/* 1615:     */       }
/* 1616:1805 */       return false;
/* 1617:     */     }
/* 1618:1808 */     return false;
/* 1619:     */   }
/* 1620:     */   
/* 1621:     */   public boolean onRestartCooldown(Player player)
/* 1622:     */   {
/* 1623:1813 */     if (this.restartCooldown.containsKey(player.getName()))
/* 1624:     */     {
/* 1625:1815 */       if (((Long)this.restartCooldown.get(player.getName())).longValue() > Calendar.getInstance().getTimeInMillis()) {
/* 1626:1816 */         return true;
/* 1627:     */       }
/* 1628:1819 */       return false;
/* 1629:     */     }
/* 1630:1822 */     return false;
/* 1631:     */   }
/* 1632:     */   
/* 1633:     */   public long getInfoCooldownTime(Player player)
/* 1634:     */   {
/* 1635:1827 */     if (this.infoCooldown.containsKey(player.getName()))
/* 1636:     */     {
/* 1637:1829 */       if (((Long)this.infoCooldown.get(player.getName())).longValue() > Calendar.getInstance().getTimeInMillis()) {
/* 1638:1830 */         return ((Long)this.infoCooldown.get(player.getName())).longValue() - Calendar.getInstance().getTimeInMillis();
/* 1639:     */       }
/* 1640:1833 */       return 0L;
/* 1641:     */     }
/* 1642:1836 */     return 0L;
/* 1643:     */   }
/* 1644:     */   
/* 1645:     */   public long getBiomeCooldownTime(Player player)
/* 1646:     */   {
/* 1647:1841 */     if (this.biomeCooldown.containsKey(player.getName()))
/* 1648:     */     {
/* 1649:1843 */       if (((Long)this.biomeCooldown.get(player.getName())).longValue() > Calendar.getInstance().getTimeInMillis()) {
/* 1650:1844 */         return ((Long)this.biomeCooldown.get(player.getName())).longValue() - Calendar.getInstance().getTimeInMillis();
/* 1651:     */       }
/* 1652:1847 */       return 0L;
/* 1653:     */     }
/* 1654:1850 */     return 0L;
/* 1655:     */   }
/* 1656:     */   
/* 1657:     */   public long getRestartCooldownTime(Player player)
/* 1658:     */   {
/* 1659:1855 */     if (this.restartCooldown.containsKey(player.getName()))
/* 1660:     */     {
/* 1661:1857 */       if (((Long)this.restartCooldown.get(player.getName())).longValue() > Calendar.getInstance().getTimeInMillis()) {
/* 1662:1858 */         return ((Long)this.restartCooldown.get(player.getName())).longValue() - Calendar.getInstance().getTimeInMillis();
/* 1663:     */       }
/* 1664:1861 */       return 0L;
/* 1665:     */     }
/* 1666:1864 */     return 0L;
/* 1667:     */   }
/* 1668:     */   
/* 1669:     */   public void setInfoCooldown(Player player)
/* 1670:     */   {
/* 1671:1869 */     this.infoCooldown.put(player.getName(), Long.valueOf(Calendar.getInstance().getTimeInMillis() + Settings.general_cooldownInfo * 1000));
/* 1672:     */   }
/* 1673:     */   
/* 1674:     */   public void setBiomeCooldown(Player player)
/* 1675:     */   {
/* 1676:1874 */     this.biomeCooldown.put(player.getName(), Long.valueOf(Calendar.getInstance().getTimeInMillis() + Settings.general_biomeChange * 1000));
/* 1677:     */   }
/* 1678:     */   
/* 1679:     */   public void setRestartCooldown(Player player)
/* 1680:     */   {
/* 1681:1879 */     this.restartCooldown.put(player.getName(), Long.valueOf(Calendar.getInstance().getTimeInMillis() + Settings.general_cooldownRestart * 1000));
/* 1682:     */   }
/* 1683:     */   
/* 1684:     */   public File[] getSchemFile()
/* 1685:     */   {
/* 1686:1884 */     return this.schemFile;
/* 1687:     */   }
/* 1688:     */   
/* 1689:     */   public boolean testForObsidian(Block block)
/* 1690:     */   {
/* 1691:1890 */     for (int x = -3; x <= 3; x++) {
/* 1692:1891 */       for (int y = -3; y <= 3; y++) {
/* 1693:1892 */         for (int z = -3; z <= 3; z++)
/* 1694:     */         {
/* 1695:1894 */           Block testBlock = getSkyBlockWorld().getBlockAt(block.getX() + x, block.getY() + y, block.getZ() + z);
/* 1696:1895 */           if (((x != 0) || (y != 0) || (z != 0)) && (testBlock.getType() == Material.OBSIDIAN)) {
/* 1697:1897 */             return true;
/* 1698:     */           }
/* 1699:     */         }
/* 1700:     */       }
/* 1701:     */     }
/* 1702:1900 */     return false;
/* 1703:     */   }
/* 1704:     */   
/* 1705:     */   public void removeInactive(List<String> removePlayerList)
/* 1706:     */   {
/* 1707:1905 */     getInstance().getServer().getScheduler().scheduleSyncRepeatingTask(getInstance(), new Runnable()
/* 1708:     */     {
/* 1709:     */       public void run()
/* 1710:     */       {
/* 1711:1908 */         if ((uSkyBlock.getInstance().getRemoveList().size() > 0) && (!uSkyBlock.getInstance().isPurgeActive()))
/* 1712:     */         {
/* 1713:1910 */           uSkyBlock.getInstance().deletePlayerIsland((String)uSkyBlock.getInstance().getRemoveList().get(0));
/* 1714:1911 */           System.out.print("[uSkyBlock] Purge: Removing " + (String)uSkyBlock.getInstance().getRemoveList().get(0) + "'s island");
/* 1715:1912 */           uSkyBlock.getInstance().deleteFromRemoveList();
/* 1716:     */         }
/* 1717:     */       }
/* 1718:1915 */     }, 0L, 200L);
/* 1719:     */   }
/* 1720:     */   
/* 1721:     */   public List<String> getRemoveList()
/* 1722:     */   {
/* 1723:1920 */     return this.removeList;
/* 1724:     */   }
/* 1725:     */   
/* 1726:     */   public void addToRemoveList(String string)
/* 1727:     */   {
/* 1728:1925 */     this.removeList.add(string);
/* 1729:     */   }
/* 1730:     */   
/* 1731:     */   public void deleteFromRemoveList()
/* 1732:     */   {
/* 1733:1930 */     this.removeList.remove(0);
/* 1734:     */   }
/* 1735:     */   
/* 1736:     */   public boolean isPurgeActive()
/* 1737:     */   {
/* 1738:1935 */     return this.purgeActive;
/* 1739:     */   }
/* 1740:     */   
/* 1741:     */   public void activatePurge()
/* 1742:     */   {
/* 1743:1940 */     this.purgeActive = true;
/* 1744:     */   }
/* 1745:     */   
/* 1746:     */   public void deactivatePurge()
/* 1747:     */   {
/* 1748:1945 */     this.purgeActive = false;
/* 1749:     */   }
/* 1750:     */   
/* 1751:     */   public HashMap<String, PlayerInfo> getActivePlayers()
/* 1752:     */   {
/* 1753:1950 */     return this.activePlayers;
/* 1754:     */   }
/* 1755:     */   
/* 1756:     */   public void addActivePlayer(String player, PlayerInfo pi)
/* 1757:     */   {
/* 1758:1955 */     this.activePlayers.put(player, pi);
/* 1759:     */   }
/* 1760:     */   
/* 1761:     */   public void removeActivePlayer(String player)
/* 1762:     */   {
/* 1763:1960 */     if (this.activePlayers.containsKey(player))
/* 1764:     */     {
/* 1765:1963 */       ((PlayerInfo)this.activePlayers.get(player)).savePlayerConfig(player);
/* 1766:     */       
/* 1767:1965 */       this.activePlayers.remove(player);
/* 1768:1966 */       System.out.print("Removing player from memory: " + player);
/* 1769:     */     }
/* 1770:     */   }
/* 1771:     */   
/* 1772:     */   public void populateChallengeList()
/* 1773:     */   {
/* 1774:1972 */     List<String> templist = new ArrayList();
/* 1775:1973 */     for (int i = 0; i < Settings.challenges_ranks.length; i++)
/* 1776:     */     {
/* 1777:1975 */       this.challenges.put(Settings.challenges_ranks[i], templist);
/* 1778:1976 */       templist = new ArrayList();
/* 1779:     */     }
/* 1780:1978 */     Iterator<String> itr = Settings.challenges_challengeList.iterator();
/* 1781:1979 */     while (itr.hasNext())
/* 1782:     */     {
/* 1783:1981 */       String tempString = (String)itr.next();
/* 1784:1982 */       if (this.challenges.containsKey(getConfig().getString("options.challenges.challengeList." + tempString + ".rankLevel"))) {
/* 1785:1984 */         ((List)this.challenges.get(getConfig().getString("options.challenges.challengeList." + tempString + ".rankLevel"))).add(tempString);
/* 1786:     */       }
/* 1787:     */     }
/* 1788:     */   }
/* 1789:     */   
/* 1790:     */   public String getChallengesFromRank(Player player, String rank)
/* 1791:     */   {
/* 1792:1993 */     this.rankDisplay = ((List)this.challenges.get(rank));
/* 1793:1994 */     String fullString = "";
/* 1794:1995 */     PlayerInfo pi = (PlayerInfo)getActivePlayers().get(player.getName());
/* 1795:1996 */     Iterator<String> itr = this.rankDisplay.iterator();
/* 1796:1997 */     while (itr.hasNext())
/* 1797:     */     {
/* 1798:1999 */       String tempString = (String)itr.next();
/* 1799:2000 */       if (pi.checkChallenge(tempString) > 0)
/* 1800:     */       {
/* 1801:2002 */         if (getConfig().getBoolean("options.challenges.challengeList." + tempString + ".repeatable")) {
/* 1802:2004 */           fullString = fullString + Settings.challenges_repeatableColor.replace('&', 'Â§') + tempString + ChatColor.DARK_GRAY + " - ";
/* 1803:     */         } else {
/* 1804:2006 */           fullString = fullString + Settings.challenges_finishedColor.replace('&', 'Â§') + tempString + ChatColor.DARK_GRAY + " - ";
/* 1805:     */         }
/* 1806:     */       }
/* 1807:     */       else {
/* 1808:2009 */         fullString = fullString + Settings.challenges_challengeColor.replace('&', 'Â§') + tempString + ChatColor.DARK_GRAY + " - ";
/* 1809:     */       }
/* 1810:     */     }
/* 1811:2012 */     if (fullString.length() > 4) {
/* 1812:2013 */       fullString = fullString.substring(0, fullString.length() - 3);
/* 1813:     */     }
/* 1814:2014 */     return fullString;
/* 1815:     */   }
/* 1816:     */   
/* 1817:     */   public int checkRankCompletion(Player player, String rank)
/* 1818:     */   {
/* 1819:2019 */     if (!Settings.challenges_requirePreviousRank) {
/* 1820:2020 */       return 0;
/* 1821:     */     }
/* 1822:2021 */     this.rankDisplay = ((List)this.challenges.get(rank));
/* 1823:2022 */     int ranksCompleted = 0;
/* 1824:2023 */     PlayerInfo pi = (PlayerInfo)getActivePlayers().get(player.getName());
/* 1825:2024 */     Iterator<String> itr = this.rankDisplay.iterator();
/* 1826:2025 */     while (itr.hasNext())
/* 1827:     */     {
/* 1828:2027 */       String tempString = (String)itr.next();
/* 1829:2028 */       if (pi.checkChallenge(tempString) > 0) {
/* 1830:2030 */         ranksCompleted++;
/* 1831:     */       }
/* 1832:     */     }
/* 1833:2036 */     return this.rankDisplay.size() - Settings.challenges_rankLeeway - ranksCompleted;
/* 1834:     */   }
/* 1835:     */   
/* 1836:     */   public boolean isRankAvailable(Player player, String rank)
/* 1837:     */   {
/* 1838:2041 */     if (this.challenges.size() < 2) {
/* 1839:2043 */       return true;
/* 1840:     */     }
/* 1841:2047 */     for (int i = 0; i < Settings.challenges_ranks.length; i++) {
/* 1842:2049 */       if (Settings.challenges_ranks[i].equalsIgnoreCase(rank))
/* 1843:     */       {
/* 1844:2051 */         if (i == 0) {
/* 1845:2052 */           return true;
/* 1846:     */         }
/* 1847:2055 */         if (checkRankCompletion(player, Settings.challenges_ranks[(i - 1)]) <= 0) {
/* 1848:2056 */           return true;
/* 1849:     */         }
/* 1850:     */       }
/* 1851:     */     }
/* 1852:2062 */     return false;
/* 1853:     */   }
/* 1854:     */   
/* 1855:     */   public boolean checkIfCanCompleteChallenge(Player player, String challenge)
/* 1856:     */   {
/* 1857:2067 */     PlayerInfo pi = (PlayerInfo)getActivePlayers().get(player.getName());
/* 1858:2071 */     if (!isRankAvailable(player, getConfig().getString("options.challenges.challengeList." + challenge + ".rankLevel")))
/* 1859:     */     {
/* 1860:2073 */       player.sendMessage(ChatColor.RED + "You have not unlocked this challenge yet!");
/* 1861:2074 */       return false;
/* 1862:     */     }
/* 1863:2076 */     if (!pi.challengeExists(challenge))
/* 1864:     */     {
/* 1865:2078 */       player.sendMessage(ChatColor.RED + "Unknown challenge name (check spelling)!");
/* 1866:2079 */       return false;
/* 1867:     */     }
/* 1868:2081 */     if ((pi.checkChallenge(challenge) > 0) && (!getConfig().getBoolean("options.challenges.challengeList." + challenge + ".repeatable")))
/* 1869:     */     {
/* 1870:2083 */       player.sendMessage(ChatColor.RED + "This challenge is not repeatable!");
/* 1871:2084 */       return false;
/* 1872:     */     }
/* 1873:2091 */     if ((pi.checkChallenge(challenge) > 0) && ((getConfig().getString("options.challenges.challengeList." + challenge + ".type").equalsIgnoreCase("onIsland")) || (getConfig().getString("options.challenges.challengeList." + challenge + ".type").equalsIgnoreCase("onIsland"))))
/* 1874:     */     {
/* 1875:2093 */       player.sendMessage(ChatColor.RED + "This challenge is not repeatable!");
/* 1876:2094 */       return false;
/* 1877:     */     }
/* 1878:2096 */     if (getConfig().getString("options.challenges.challengeList." + challenge + ".type").equalsIgnoreCase("onPlayer"))
/* 1879:     */     {
/* 1880:2098 */       if (!hasRequired(player, challenge, "onPlayer"))
/* 1881:     */       {
/* 1882:2100 */         player.sendMessage(ChatColor.RED + getConfig().getString(new StringBuilder("options.challenges.challengeList.").append(challenge).append(".description").toString()));
/* 1883:2101 */         player.sendMessage(ChatColor.RED + "You don't have enough of the required item(s)!");
/* 1884:2102 */         return false;
/* 1885:     */       }
/* 1886:2104 */       return true;
/* 1887:     */     }
/* 1888:2105 */     if (getConfig().getString("options.challenges.challengeList." + challenge + ".type").equalsIgnoreCase("onIsland"))
/* 1889:     */     {
/* 1890:2107 */       if (!playerIsOnIsland(player)) {
/* 1891:2109 */         player.sendMessage(ChatColor.RED + "You must be on your island to do that!");
/* 1892:     */       }
/* 1893:2111 */       if (!hasRequired(player, challenge, "onIsland"))
/* 1894:     */       {
/* 1895:2113 */         player.sendMessage(ChatColor.RED + getConfig().getString(new StringBuilder("options.challenges.challengeList.").append(challenge).append(".description").toString()));
/* 1896:     */         
/* 1897:2115 */         player.sendMessage(ChatColor.RED + "You must be standing within 10 blocks of all required items.");
/* 1898:2116 */         return false;
/* 1899:     */       }
/* 1900:2118 */       return true;
/* 1901:     */     }
/* 1902:2119 */     if (getConfig().getString("options.challenges.challengeList." + challenge + ".type").equalsIgnoreCase("islandLevel"))
/* 1903:     */     {
/* 1904:2121 */       if (getInstance().getIslandConfig(((PlayerInfo)getInstance().getActivePlayers().get(player.getName())).locationForParty()).getInt("general.level") >= getConfig().getInt("options.challenges.challengeList." + challenge + ".requiredItems")) {
/* 1905:2123 */         return true;
/* 1906:     */       }
/* 1907:2126 */       player.sendMessage(ChatColor.RED + "Your island must be level " + getConfig().getInt(new StringBuilder("options.challenges.challengeList.").append(challenge).append(".requiredItems").toString()) + " to complete this challenge!");
/* 1908:2127 */       return false;
/* 1909:     */     }
/* 1910:2130 */     return false;
/* 1911:     */   }
/* 1912:     */   
/* 1913:     */   public boolean takeRequired(Player player, String challenge, String type)
/* 1914:     */   {
/* 1915:2136 */     if (type.equalsIgnoreCase("onPlayer"))
/* 1916:     */     {
/* 1917:2138 */       String[] reqList = getConfig().getString("options.challenges.challengeList." + challenge + ".requiredItems").split(" ");
/* 1918:     */       
/* 1919:     */ 
/* 1920:2141 */       int reqItem = 0;
/* 1921:2142 */       int reqAmount = 0;
/* 1922:2143 */       int reqMod = -1;
/* 1923:2144 */       for (String s : reqList)
/* 1924:     */       {
/* 1925:2146 */         String[] sPart = s.split(":");
/* 1926:2147 */         if (sPart.length == 2)
/* 1927:     */         {
/* 1928:2149 */           reqItem = Integer.parseInt(sPart[0]);
/* 1929:2150 */           String[] sScale = sPart[1].split(";");
/* 1930:2151 */           if (sScale.length == 1) {
/* 1931:2152 */             reqAmount = Integer.parseInt(sPart[1]);
/* 1932:2153 */           } else if (sScale.length == 2) {
/* 1933:2155 */             if (sScale[1].charAt(0) == '+') {
/* 1934:2157 */               reqAmount = Integer.parseInt(sScale[0]) + Integer.parseInt(sScale[1].substring(1)) * ((PlayerInfo)getInstance().getActivePlayers().get(player.getName())).checkChallengeSinceTimer(challenge);
/* 1935:2158 */             } else if (sScale[1].charAt(0) == '*') {
/* 1936:2160 */               reqAmount = Integer.parseInt(sScale[0]) * (Integer.parseInt(sScale[1].substring(1)) * ((PlayerInfo)getInstance().getActivePlayers().get(player.getName())).checkChallengeSinceTimer(challenge));
/* 1937:2161 */             } else if (sScale[1].charAt(0) == '-') {
/* 1938:2163 */               reqAmount = Integer.parseInt(sScale[0]) - Integer.parseInt(sScale[1].substring(1)) * ((PlayerInfo)getInstance().getActivePlayers().get(player.getName())).checkChallengeSinceTimer(challenge);
/* 1939:2164 */             } else if (sScale[1].charAt(0) == '/') {
/* 1940:2166 */               reqAmount = Integer.parseInt(sScale[0]) / (Integer.parseInt(sScale[1].substring(1)) * ((PlayerInfo)getInstance().getActivePlayers().get(player.getName())).checkChallengeSinceTimer(challenge));
/* 1941:     */             }
/* 1942:     */           }
/* 1943:2169 */           if (!player.getInventory().contains(reqItem, reqAmount)) {
/* 1944:2170 */             return false;
/* 1945:     */           }
/* 1946:2173 */           player.getInventory().removeItem(new ItemStack[] { new ItemStack(reqItem, reqAmount) });
/* 1947:     */         }
/* 1948:2175 */         else if (sPart.length == 3)
/* 1949:     */         {
/* 1950:2177 */           reqItem = Integer.parseInt(sPart[0]);
/* 1951:2178 */           String[] sScale = sPart[2].split(";");
/* 1952:2179 */           if (sScale.length == 1) {
/* 1953:2180 */             reqAmount = Integer.parseInt(sPart[2]);
/* 1954:2181 */           } else if (sScale.length == 2) {
/* 1955:2183 */             if (sScale[1].charAt(0) == '+') {
/* 1956:2185 */               reqAmount = Integer.parseInt(sScale[0]) + Integer.parseInt(sScale[1].substring(1)) * ((PlayerInfo)getInstance().getActivePlayers().get(player.getName())).checkChallengeSinceTimer(challenge);
/* 1957:2186 */             } else if (sScale[1].charAt(0) == '*') {
/* 1958:2188 */               reqAmount = Integer.parseInt(sScale[0]) * (Integer.parseInt(sScale[1].substring(1)) * ((PlayerInfo)getInstance().getActivePlayers().get(player.getName())).checkChallengeSinceTimer(challenge));
/* 1959:2189 */             } else if (sScale[1].charAt(0) == '-') {
/* 1960:2191 */               reqAmount = Integer.parseInt(sScale[0]) - Integer.parseInt(sScale[1].substring(1)) * ((PlayerInfo)getInstance().getActivePlayers().get(player.getName())).checkChallengeSinceTimer(challenge);
/* 1961:2192 */             } else if (sScale[1].charAt(0) == '/') {
/* 1962:2194 */               reqAmount = Integer.parseInt(sScale[0]) / (Integer.parseInt(sScale[1].substring(1)) * ((PlayerInfo)getInstance().getActivePlayers().get(player.getName())).checkChallengeSinceTimer(challenge));
/* 1963:     */             }
/* 1964:     */           }
/* 1965:2197 */           reqMod = Integer.parseInt(sPart[1]);
/* 1966:2198 */           if (!player.getInventory().containsAtLeast(new ItemStack(reqItem, reqAmount, (short)reqMod), reqAmount)) {
/* 1967:2199 */             return false;
/* 1968:     */           }
/* 1969:2201 */           player.getInventory().removeItem(new ItemStack[] { new ItemStack(reqItem, reqAmount, (short)reqMod) });
/* 1970:     */         }
/* 1971:     */       }
/* 1972:2204 */       return true;
/* 1973:     */     }
/* 1974:2205 */     if (type.equalsIgnoreCase("onIsland")) {
/* 1975:2207 */       return true;
/* 1976:     */     }
/* 1977:2208 */     if (type.equalsIgnoreCase("islandLevel")) {
/* 1978:2210 */       return true;
/* 1979:     */     }
/* 1980:2212 */     return false;
/* 1981:     */   }
/* 1982:     */   
/* 1983:     */   public boolean hasRequired(Player player, String challenge, String type)
/* 1984:     */   {
/* 1985:2218 */     String[] reqList = getConfig().getString("options.challenges.challengeList." + challenge + ".requiredItems").split(" ");
/* 1986:2221 */     if (type.equalsIgnoreCase("onPlayer"))
/* 1987:     */     {
/* 1988:2223 */       int reqItem = 0;
/* 1989:2224 */       int reqAmount = 0;
/* 1990:2225 */       int reqMod = -1;
/* 1991:2226 */       for (String s : reqList)
/* 1992:     */       {
/* 1993:2228 */         String[] sPart = s.split(":");
/* 1994:2229 */         if (sPart.length == 2)
/* 1995:     */         {
/* 1996:2231 */           reqItem = Integer.parseInt(sPart[0]);
/* 1997:2232 */           String[] sScale = sPart[1].split(";");
/* 1998:2233 */           if (sScale.length == 1) {
/* 1999:2234 */             reqAmount = Integer.parseInt(sPart[1]);
/* 2000:2235 */           } else if (sScale.length == 2) {
/* 2001:2237 */             if (sScale[1].charAt(0) == '+') {
/* 2002:2239 */               reqAmount = Integer.parseInt(sScale[0]) + Integer.parseInt(sScale[1].substring(1)) * ((PlayerInfo)getInstance().getActivePlayers().get(player.getName())).checkChallengeSinceTimer(challenge);
/* 2003:2240 */             } else if (sScale[1].charAt(0) == '*') {
/* 2004:2242 */               reqAmount = Integer.parseInt(sScale[0]) * (Integer.parseInt(sScale[1].substring(1)) * ((PlayerInfo)getInstance().getActivePlayers().get(player.getName())).checkChallengeSinceTimer(challenge));
/* 2005:2243 */             } else if (sScale[1].charAt(0) == '-') {
/* 2006:2245 */               reqAmount = Integer.parseInt(sScale[0]) - Integer.parseInt(sScale[1].substring(1)) * ((PlayerInfo)getInstance().getActivePlayers().get(player.getName())).checkChallengeSinceTimer(challenge);
/* 2007:2246 */             } else if (sScale[1].charAt(0) == '/') {
/* 2008:2248 */               reqAmount = Integer.parseInt(sScale[0]) / (Integer.parseInt(sScale[1].substring(1)) * ((PlayerInfo)getInstance().getActivePlayers().get(player.getName())).checkChallengeSinceTimer(challenge));
/* 2009:     */             }
/* 2010:     */           }
/* 2011:2252 */           if (!player.getInventory().containsAtLeast(new ItemStack(reqItem, reqAmount, (short)0), reqAmount)) {
/* 2012:2253 */             return false;
/* 2013:     */           }
/* 2014:     */         }
/* 2015:2254 */         else if (sPart.length == 3)
/* 2016:     */         {
/* 2017:2256 */           reqItem = Integer.parseInt(sPart[0]);
/* 2018:2257 */           String[] sScale = sPart[2].split(";");
/* 2019:2258 */           if (sScale.length == 1) {
/* 2020:2259 */             reqAmount = Integer.parseInt(sPart[2]);
/* 2021:2260 */           } else if (sScale.length == 2) {
/* 2022:2262 */             if (sScale[1].charAt(0) == '+') {
/* 2023:2264 */               reqAmount = Integer.parseInt(sScale[0]) + Integer.parseInt(sScale[1].substring(1)) * ((PlayerInfo)getInstance().getActivePlayers().get(player.getName())).checkChallengeSinceTimer(challenge);
/* 2024:2265 */             } else if (sScale[1].charAt(0) == '*') {
/* 2025:2267 */               reqAmount = Integer.parseInt(sScale[0]) * (Integer.parseInt(sScale[1].substring(1)) * ((PlayerInfo)getInstance().getActivePlayers().get(player.getName())).checkChallengeSinceTimer(challenge));
/* 2026:2268 */             } else if (sScale[1].charAt(0) == '-') {
/* 2027:2270 */               reqAmount = Integer.parseInt(sScale[0]) - Integer.parseInt(sScale[1].substring(1)) * ((PlayerInfo)getInstance().getActivePlayers().get(player.getName())).checkChallengeSinceTimer(challenge);
/* 2028:2271 */             } else if (sScale[1].charAt(0) == '/') {
/* 2029:2273 */               reqAmount = Integer.parseInt(sScale[0]) / (Integer.parseInt(sScale[1].substring(1)) * ((PlayerInfo)getInstance().getActivePlayers().get(player.getName())).checkChallengeSinceTimer(challenge));
/* 2030:     */             }
/* 2031:     */           }
/* 2032:2276 */           reqMod = Integer.parseInt(sPart[1]);
/* 2033:2277 */           if (!player.getInventory().containsAtLeast(new ItemStack(reqItem, reqAmount, (short)reqMod), reqAmount)) {
/* 2034:2278 */             return false;
/* 2035:     */           }
/* 2036:     */         }
/* 2037:     */       }
/* 2038:2281 */       if (getConfig().getBoolean("options.challenges.challengeList." + challenge + ".takeItems")) {
/* 2039:2282 */         takeRequired(player, challenge, type);
/* 2040:     */       }
/* 2041:2283 */       return true;
/* 2042:     */     }
/* 2043:2284 */     if (type.equalsIgnoreCase("onIsland"))
/* 2044:     */     {
/* 2045:2286 */       int[][] neededItem = new int[reqList.length][2];
/* 2046:2287 */       for (int i = 0; i < reqList.length; i++)
/* 2047:     */       {
/* 2048:2289 */         String[] sPart = reqList[i].split(":");
/* 2049:2290 */         neededItem[i][0] = Integer.parseInt(sPart[0]);
/* 2050:2291 */         neededItem[i][1] = Integer.parseInt(sPart[1]);
/* 2051:     */       }
/* 2052:2293 */       Location l = player.getLocation();
/* 2053:2294 */       int px = l.getBlockX();
/* 2054:2295 */       int py = l.getBlockY();
/* 2055:2296 */       int pz = l.getBlockZ();
/* 2056:2297 */       for (int x = -10; x <= 10; x++) {
/* 2057:2298 */         for (int y = -3; y <= 10; y++) {
/* 2058:2299 */           for (int z = -10; z <= 10; z++)
/* 2059:     */           {
/* 2060:2300 */             Block b = new Location(l.getWorld(), px + x, py + y, pz + z).getBlock();
/* 2061:2301 */             for (int i = 0; i < neededItem.length; i++) {
/* 2062:2304 */               if (b.getTypeId() == neededItem[i][0]) {
/* 2063:2307 */                 neededItem[i][1] -= 1;
/* 2064:     */               }
/* 2065:     */             }
/* 2066:     */           }
/* 2067:     */         }
/* 2068:     */       }
/* 2069:2313 */       for (int i = 0; i < neededItem.length; i++) {
/* 2070:2315 */         if (neededItem[i][1] > 0) {
/* 2071:2317 */           return false;
/* 2072:     */         }
/* 2073:     */       }
/* 2074:2320 */       return true;
/* 2075:     */     }
/* 2076:2325 */     return true;
/* 2077:     */   }
/* 2078:     */   
/* 2079:     */   public boolean giveReward(Player player, String challenge)
/* 2080:     */   {
/* 2081:2332 */     String[] permList = getConfig().getString("options.challenges.challengeList." + challenge.toLowerCase() + ".permissionReward").split(" ");
/* 2082:2333 */     double rewCurrency = 0.0D;
/* 2083:2334 */     player.sendMessage(ChatColor.GREEN + "You have completed the " + challenge + " challenge!");
/* 2084:     */     String[] rewList;
/* 2085:2335 */     if (((PlayerInfo)getInstance().getActivePlayers().get(player.getName())).checkChallenge(challenge) == 0)
/* 2086:     */     {
/* 2087:2337 */       String[] rewList = getConfig().getString("options.challenges.challengeList." + challenge.toLowerCase() + ".itemReward").split(" ");
/* 2088:2338 */       if ((Settings.challenges_enableEconomyPlugin) && (VaultHandler.econ != null)) {
/* 2089:2339 */         rewCurrency = getConfig().getInt("options.challenges.challengeList." + challenge.toLowerCase() + ".currencyReward");
/* 2090:     */       }
/* 2091:     */     }
/* 2092:     */     else
/* 2093:     */     {
/* 2094:2343 */       rewList = getConfig().getString("options.challenges.challengeList." + challenge.toLowerCase() + ".repeatItemReward").split(" ");
/* 2095:2344 */       if ((Settings.challenges_enableEconomyPlugin) && (VaultHandler.econ != null)) {
/* 2096:2345 */         rewCurrency = getConfig().getInt("options.challenges.challengeList." + challenge.toLowerCase() + ".repeatCurrencyReward");
/* 2097:     */       }
/* 2098:     */     }
/* 2099:2348 */     int rewItem = 0;
/* 2100:2349 */     int rewAmount = 0;
/* 2101:2350 */     double rewBonus = 1.0D;
/* 2102:2351 */     int rewMod = -1;
/* 2103:2352 */     if ((Settings.challenges_enableEconomyPlugin) && (VaultHandler.econ != null))
/* 2104:     */     {
/* 2105:2354 */       if (VaultHandler.checkPerk(player.getName(), "group.memberplus", getSkyBlockWorld())) {
/* 2106:2356 */         rewBonus += 0.05D;
/* 2107:     */       }
/* 2108:2358 */       if (VaultHandler.checkPerk(player.getName(), "usb.donor.all", getSkyBlockWorld())) {
/* 2109:2360 */         rewBonus += 0.05D;
/* 2110:     */       }
/* 2111:2362 */       if (VaultHandler.checkPerk(player.getName(), "usb.donor.25", getSkyBlockWorld())) {
/* 2112:2364 */         rewBonus += 0.05D;
/* 2113:     */       }
/* 2114:2366 */       if (VaultHandler.checkPerk(player.getName(), "usb.donor.50", getSkyBlockWorld())) {
/* 2115:2368 */         rewBonus += 0.05D;
/* 2116:     */       }
/* 2117:2370 */       if (VaultHandler.checkPerk(player.getName(), "usb.donor.75", getSkyBlockWorld())) {
/* 2118:2372 */         rewBonus += 0.1D;
/* 2119:     */       }
/* 2120:2374 */       if (VaultHandler.checkPerk(player.getName(), "usb.donor.100", getSkyBlockWorld())) {
/* 2121:2376 */         rewBonus += 0.2D;
/* 2122:     */       }
/* 2123:2378 */       VaultHandler.econ.depositPlayer(player.getName(), rewCurrency * rewBonus);
/* 2124:2379 */       if (((PlayerInfo)getInstance().getActivePlayers().get(player.getName())).checkChallenge(challenge) > 0)
/* 2125:     */       {
/* 2126:2381 */         player.giveExp(getInstance().getConfig().getInt("options.challenges.challengeList." + challenge + ".repeatXpReward"));
/* 2127:2382 */         player.sendMessage(ChatColor.YELLOW + "Repeat reward(s): " + ChatColor.WHITE + getInstance().getConfig().getString(new StringBuilder("options.challenges.challengeList.").append(challenge).append(".repeatRewardText").toString()).replace('&', 'Â§'));
/* 2128:2383 */         player.sendMessage(ChatColor.YELLOW + "Repeat exp reward: " + ChatColor.WHITE + getInstance().getConfig().getInt(new StringBuilder("options.challenges.challengeList.").append(challenge).append(".repeatXpReward").toString()));
/* 2129:2384 */         player.sendMessage(ChatColor.YELLOW + "Repeat currency reward: " + ChatColor.WHITE + this.df.format(getInstance().getConfig().getInt(new StringBuilder("options.challenges.challengeList.").append(challenge).append(".repeatCurrencyReward").toString()) * rewBonus) + " " + VaultHandler.econ.currencyNamePlural() + "Â§a(+" + this.df.format((rewBonus - 1.0D) * 100.0D) + "%)");
/* 2130:     */       }
/* 2131:     */       else
/* 2132:     */       {
/* 2133:2387 */         if (Settings.challenges_broadcastCompletion) {
/* 2134:2388 */           Bukkit.getServer().broadcastMessage(Settings.challenges_broadcastText.replace('&', 'Â§') + player.getName() + " has completed the " + challenge + " challenge!");
/* 2135:     */         }
/* 2136:2389 */         player.giveExp(getInstance().getConfig().getInt("options.challenges.challengeList." + challenge + ".xpReward"));
/* 2137:2390 */         player.sendMessage(ChatColor.YELLOW + "Reward(s): " + ChatColor.WHITE + getInstance().getConfig().getString(new StringBuilder("options.challenges.challengeList.").append(challenge).append(".rewardText").toString()).replace('&', 'Â§'));
/* 2138:2391 */         player.sendMessage(ChatColor.YELLOW + "Exp reward: " + ChatColor.WHITE + getInstance().getConfig().getInt(new StringBuilder("options.challenges.challengeList.").append(challenge).append(".xpReward").toString()));
/* 2139:2392 */         player.sendMessage(ChatColor.YELLOW + "Currency reward: " + ChatColor.WHITE + this.df.format(getInstance().getConfig().getInt(new StringBuilder("options.challenges.challengeList.").append(challenge).append(".currencyReward").toString()) * rewBonus) + " " + VaultHandler.econ.currencyNamePlural() + "Â§a(+" + this.df.format((rewBonus - 1.0D) * 100.0D) + "%)");
/* 2140:     */       }
/* 2141:     */     }
/* 2142:2396 */     else if (((PlayerInfo)getInstance().getActivePlayers().get(player.getName())).checkChallenge(challenge) > 0)
/* 2143:     */     {
/* 2144:2398 */       player.giveExp(getInstance().getConfig().getInt("options.challenges.challengeList." + challenge + ".repeatXpReward"));
/* 2145:2399 */       player.sendMessage(ChatColor.YELLOW + "Repeat reward(s): " + ChatColor.WHITE + getInstance().getConfig().getString(new StringBuilder("options.challenges.challengeList.").append(challenge).append(".repeatRewardText").toString()).replace('&', 'Â§'));
/* 2146:2400 */       player.sendMessage(ChatColor.YELLOW + "Repeat exp reward: " + ChatColor.WHITE + getInstance().getConfig().getInt(new StringBuilder("options.challenges.challengeList.").append(challenge).append(".repeatXpReward").toString()));
/* 2147:     */     }
/* 2148:     */     else
/* 2149:     */     {
/* 2150:2403 */       if (Settings.challenges_broadcastCompletion) {
/* 2151:2404 */         Bukkit.getServer().broadcastMessage(Settings.challenges_broadcastText.replace('&', 'Â§') + player.getName() + " has completed the " + challenge + " challenge!");
/* 2152:     */       }
/* 2153:2405 */       player.giveExp(getInstance().getConfig().getInt("options.challenges.challengeList." + challenge + ".xpReward"));
/* 2154:2406 */       player.sendMessage(ChatColor.YELLOW + "Reward(s): " + ChatColor.WHITE + getInstance().getConfig().getString(new StringBuilder("options.challenges.challengeList.").append(challenge).append(".rewardText").toString()).replace('&', 'Â§'));
/* 2155:2407 */       player.sendMessage(ChatColor.YELLOW + "Exp reward: " + ChatColor.WHITE + getInstance().getConfig().getInt(new StringBuilder("options.challenges.challengeList.").append(challenge).append(".xpReward").toString()));
/* 2156:     */     }
/* 2157:2410 */     for (String s : permList) {
/* 2158:2412 */       if (!s.equalsIgnoreCase("none")) {
/* 2159:2414 */         if (!VaultHandler.checkPerk(player.getName(), s, player.getWorld())) {
/* 2160:2416 */           VaultHandler.addPerk(player, s);
/* 2161:     */         }
/* 2162:     */       }
/* 2163:     */     }
/* 2164:2420 */     for (String s : rewList)
/* 2165:     */     {
/* 2166:2422 */       String[] sPart = s.split(":");
/* 2167:2423 */       if (sPart.length == 2)
/* 2168:     */       {
/* 2169:2425 */         rewItem = Integer.parseInt(sPart[0]);
/* 2170:2426 */         rewAmount = Integer.parseInt(sPart[1]);
/* 2171:2427 */         player.getInventory().addItem(new ItemStack[] { new ItemStack(rewItem, rewAmount) });
/* 2172:     */       }
/* 2173:2428 */       else if (sPart.length == 3)
/* 2174:     */       {
/* 2175:2430 */         rewItem = Integer.parseInt(sPart[0]);
/* 2176:2431 */         rewAmount = Integer.parseInt(sPart[2]);
/* 2177:2432 */         rewMod = Integer.parseInt(sPart[1]);
/* 2178:2433 */         player.getInventory().addItem(new ItemStack[] { new ItemStack(rewItem, rewAmount, (short)rewMod) });
/* 2179:     */       }
/* 2180:     */     }
/* 2181:2436 */     ((PlayerInfo)getInstance().getActivePlayers().get(player.getName())).completeChallenge(challenge);
/* 2182:     */     
/* 2183:     */ 
/* 2184:2439 */     return true;
/* 2185:     */   }
/* 2186:     */   
/* 2187:     */   public void reloadData()
/* 2188:     */   {
/* 2189:2443 */     if (this.skyblockDataFile == null) {
/* 2190:2444 */       this.skyblockDataFile = new File(getDataFolder(), "skyblockData.yml");
/* 2191:     */     }
/* 2192:2446 */     this.skyblockData = YamlConfiguration.loadConfiguration(this.skyblockDataFile);
/* 2193:     */     
/* 2194:     */ 
/* 2195:2449 */     InputStream defConfigStream = getResource("skyblockData.yml");
/* 2196:2450 */     if (defConfigStream != null)
/* 2197:     */     {
/* 2198:2451 */       YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
/* 2199:2452 */       this.skyblockData.setDefaults(defConfig);
/* 2200:     */     }
/* 2201:     */   }
/* 2202:     */   
/* 2203:     */   public FileConfiguration getData()
/* 2204:     */   {
/* 2205:2458 */     if (this.skyblockData == null) {
/* 2206:2459 */       reloadData();
/* 2207:     */     }
/* 2208:2461 */     return this.skyblockData;
/* 2209:     */   }
/* 2210:     */   
/* 2211:     */   double dReturns(double val, double scale)
/* 2212:     */   {
/* 2213:2466 */     if (val < 0.0D) {
/* 2214:2467 */       return -dReturns(-val, scale);
/* 2215:     */     }
/* 2216:2468 */     double mult = val / scale;
/* 2217:2469 */     double trinum = (Math.sqrt(8.0D * mult + 1.0D) - 1.0D) / 2.0D;
/* 2218:2470 */     return trinum * scale;
/* 2219:     */   }
/* 2220:     */   
/* 2221:     */   public void reloadLevelConfig()
/* 2222:     */   {
/* 2223:2475 */     if (this.levelConfigFile == null) {
/* 2224:2476 */       this.levelConfigFile = new File(getDataFolder(), "levelConfig.yml");
/* 2225:     */     }
/* 2226:2478 */     this.levelConfig = YamlConfiguration.loadConfiguration(this.levelConfigFile);
/* 2227:     */     
/* 2228:     */ 
/* 2229:2481 */     InputStream defConfigStream = getResource("levelConfig.yml");
/* 2230:2482 */     if (defConfigStream != null)
/* 2231:     */     {
/* 2232:2483 */       YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
/* 2233:2484 */       this.levelConfig.setDefaults(defConfig);
/* 2234:     */     }
/* 2235:     */   }
/* 2236:     */   
/* 2237:     */   public FileConfiguration getLevelConfig()
/* 2238:     */   {
/* 2239:2489 */     if (this.levelConfig == null) {
/* 2240:2490 */       reloadLevelConfig();
/* 2241:     */     }
/* 2242:2492 */     return this.levelConfig;
/* 2243:     */   }
/* 2244:     */   
/* 2245:     */   public void saveLevelConfig()
/* 2246:     */   {
/* 2247:2496 */     if ((this.levelConfig == null) || (this.levelConfigFile == null)) {
/* 2248:2497 */       return;
/* 2249:     */     }
/* 2250:     */     try
/* 2251:     */     {
/* 2252:2500 */       getLevelConfig().save(this.levelConfigFile);
/* 2253:     */     }
/* 2254:     */     catch (IOException ex)
/* 2255:     */     {
/* 2256:2502 */       getLogger().log(Level.SEVERE, "Could not save config to " + this.levelConfigFile, ex);
/* 2257:     */     }
/* 2258:     */   }
/* 2259:     */   
/* 2260:     */   public void saveDefaultLevelConfig()
/* 2261:     */   {
/* 2262:2507 */     if (this.levelConfigFile == null) {
/* 2263:2508 */       this.levelConfigFile = new File(getDataFolder(), "levelConfig.yml");
/* 2264:     */     }
/* 2265:2510 */     if (!this.levelConfigFile.exists()) {
/* 2266:2511 */       getInstance().saveResource("levelConfig.yml", false);
/* 2267:     */     }
/* 2268:     */   }
/* 2269:     */   
/* 2270:     */   public void loadLevelConfig()
/* 2271:     */   {
/* 2272:     */     try
/* 2273:     */     {
/* 2274:2518 */       getLevelConfig();
/* 2275:     */     }
/* 2276:     */     catch (Exception e)
/* 2277:     */     {
/* 2278:2520 */       e.printStackTrace();
/* 2279:     */     }
/* 2280:2523 */     for (int i = 1; i <= 255; i++)
/* 2281:     */     {
/* 2282:2525 */       if (getLevelConfig().contains("blockValues." + i)) {
/* 2283:2526 */         Settings.blockList[i] = getLevelConfig().getInt("blockValues." + i);
/* 2284:     */       } else {
/* 2285:2528 */         Settings.blockList[i] = getLevelConfig().getInt("general.default");
/* 2286:     */       }
/* 2287:2529 */       if (getLevelConfig().contains("blockLimits." + i)) {
/* 2288:2530 */         Settings.limitList[i] = getLevelConfig().getInt("blockLimits." + i);
/* 2289:     */       } else {
/* 2290:2532 */         Settings.limitList[i] = -1;
/* 2291:     */       }
/* 2292:2533 */       if (getLevelConfig().contains("diminishingReturns." + i)) {
/* 2293:2534 */         Settings.diminishingReturnsList[i] = getLevelConfig().getInt("diminishingReturns." + i);
/* 2294:2537 */       } else if (getLevelConfig().getBoolean("general.useDiminishingReturns")) {
/* 2295:2538 */         Settings.diminishingReturnsList[i] = getLevelConfig().getInt("general.defaultScale");
/* 2296:     */       } else {
/* 2297:2540 */         Settings.diminishingReturnsList[i] = -1;
/* 2298:     */       }
/* 2299:     */     }
/* 2300:2543 */     System.out.print(Settings.blockList[57]);
/* 2301:2544 */     System.out.print(Settings.diminishingReturnsList[57]);
/* 2302:2545 */     System.out.print(Settings.limitList[57]);
/* 2303:     */   }
/* 2304:     */   
/* 2305:     */   public void clearIslandConfig(String location, String leader)
/* 2306:     */   {
/* 2307:2550 */     getIslandConfig(location).set("general.level", Integer.valueOf(0));
/* 2308:2551 */     getIslandConfig(location).set("general.warpLocationX", Integer.valueOf(0));
/* 2309:2552 */     getIslandConfig(location).set("general.warpLocationY", Integer.valueOf(0));
/* 2310:2553 */     getIslandConfig(location).set("general.warpLocationZ", Integer.valueOf(0));
/* 2311:2554 */     getIslandConfig(location).set("general.warpActive", Boolean.valueOf(false));
/* 2312:2555 */     getIslandConfig(location).set("log.logPos", Integer.valueOf(1));
/* 2313:2556 */     getIslandConfig(location).set("log.1", "Â§d[skyblock] The island has been created.");
/* 2314:2557 */     setupPartyLeader(location, leader);
/* 2315:     */   }
/* 2316:     */   
/* 2317:     */   public void setupPartyLeader(String location, String leader)
/* 2318:     */   {
/* 2319:2563 */     getIslandConfig(location).createSection("party.members." + leader);
/* 2320:2564 */     getIslandConfig(location);FileConfiguration.createPath(getIslandConfig(location).getConfigurationSection("party.members." + leader), "canChangeBiome");
/* 2321:2565 */     getIslandConfig(location);FileConfiguration.createPath(getIslandConfig(location).getConfigurationSection("party.members." + leader), "canToggleLock");
/* 2322:2566 */     getIslandConfig(location);FileConfiguration.createPath(getIslandConfig(location).getConfigurationSection("party.members." + leader), "canChangeWarp");
/* 2323:2567 */     getIslandConfig(location);FileConfiguration.createPath(getIslandConfig(location).getConfigurationSection("party.members." + leader), "canToggleWarp");
/* 2324:2568 */     getIslandConfig(location);FileConfiguration.createPath(getIslandConfig(location).getConfigurationSection("party.members." + leader), "canInviteOthers");
/* 2325:2569 */     getIslandConfig(location);FileConfiguration.createPath(getIslandConfig(location).getConfigurationSection("party.members." + leader), "canKickOthers");
/* 2326:2570 */     getIslandConfig(location).set("party.leader", leader);
/* 2327:2571 */     getIslandConfig(location).set("party.members." + leader + ".canChangeBiome", Boolean.valueOf(true));
/* 2328:2572 */     getIslandConfig(location).set("party.members." + leader + ".canToggleLock", Boolean.valueOf(true));
/* 2329:2573 */     getIslandConfig(location).set("party.members." + leader + ".canChangeWarp", Boolean.valueOf(true));
/* 2330:2574 */     getIslandConfig(location).set("party.members." + leader + ".canToggleWarp", Boolean.valueOf(true));
/* 2331:2575 */     getIslandConfig(location).set("party.members." + leader + ".canInviteOthers", Boolean.valueOf(true));
/* 2332:2576 */     getIslandConfig(location).set("party.members." + leader + ".canKickOthers", Boolean.valueOf(true));
/* 2333:2577 */     saveIslandConfig(location);
/* 2334:     */   }
/* 2335:     */   
/* 2336:     */   public void setupPartyMember(String location, String member)
/* 2337:     */   {
/* 2338:2583 */     getIslandConfig(location).createSection("party.members." + member);
/* 2339:2584 */     getIslandConfig(location);FileConfiguration.createPath(getIslandConfig(location).getConfigurationSection("party.members." + member), "canChangeBiome");
/* 2340:2585 */     getIslandConfig(location);FileConfiguration.createPath(getIslandConfig(location).getConfigurationSection("party.members." + member), "canToggleLock");
/* 2341:2586 */     getIslandConfig(location);FileConfiguration.createPath(getIslandConfig(location).getConfigurationSection("party.members." + member), "canChangeWarp");
/* 2342:2587 */     getIslandConfig(location);FileConfiguration.createPath(getIslandConfig(location).getConfigurationSection("party.members." + member), "canToggleWarp");
/* 2343:2588 */     getIslandConfig(location);FileConfiguration.createPath(getIslandConfig(location).getConfigurationSection("party.members." + member), "canInviteOthers");
/* 2344:2589 */     getIslandConfig(location);FileConfiguration.createPath(getIslandConfig(location).getConfigurationSection("party.members." + member), "canKickOthers");
/* 2345:2590 */     getIslandConfig(location).set("party.members." + member + ".canChangeBiome", Boolean.valueOf(false));
/* 2346:2591 */     getIslandConfig(location).set("party.currentSize", Integer.valueOf(getIslandConfig(location).getInt("party.currentSize") + 1));
/* 2347:2592 */     getIslandConfig(location).set("party.members." + member + ".canToggleLock", Boolean.valueOf(false));
/* 2348:2593 */     getIslandConfig(location).set("party.members." + member + ".canChangeWarp", Boolean.valueOf(false));
/* 2349:2594 */     getIslandConfig(location).set("party.members." + member + ".canToggleWarp", Boolean.valueOf(false));
/* 2350:2595 */     getIslandConfig(location).set("party.members." + member + ".canInviteOthers", Boolean.valueOf(false));
/* 2351:2596 */     getIslandConfig(location).set("party.members." + member + ".canKickOthers", Boolean.valueOf(false));
/* 2352:2597 */     getIslandConfig(location).set("party.members." + member + ".canBanOthers", Boolean.valueOf(false));
/* 2353:2598 */     saveIslandConfig(location);
/* 2354:     */   }
/* 2355:     */   
/* 2356:     */   public void reloadIslandConfig(String location)
/* 2357:     */   {
/* 2358:2603 */     this.islandConfigFile = new File(this.directoryIslands, location + ".yml");
/* 2359:2604 */     this.islands.put(location, YamlConfiguration.loadConfiguration(this.islandConfigFile));
/* 2360:2605 */     InputStream defConfigStream = getResource("island.yml");
/* 2361:2606 */     if (defConfigStream != null)
/* 2362:     */     {
/* 2363:2607 */       YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
/* 2364:2608 */       ((FileConfiguration)this.islands.get(location)).setDefaults(defConfig);
/* 2365:     */     }
/* 2366:2610 */     saveIslandConfig(location);
/* 2367:     */   }
/* 2368:     */   
/* 2369:     */   public FileConfiguration getTempIslandConfig(String location)
/* 2370:     */   {
/* 2371:2615 */     this.tempIslandFile = new File(this.directoryIslands, location + ".yml");
/* 2372:2616 */     this.tempIsland = YamlConfiguration.loadConfiguration(this.tempIslandFile);
/* 2373:2617 */     return this.tempIsland;
/* 2374:     */   }
/* 2375:     */   
/* 2376:     */   public FileConfiguration getCurrentPlayerConfig(String player)
/* 2377:     */   {
/* 2378:2622 */     this.tempPlayerFile = new File(this.directoryPlayers, player + ".yml");
/* 2379:2623 */     this.tempPlayer = YamlConfiguration.loadConfiguration(this.tempPlayerFile);
/* 2380:2624 */     return this.tempPlayer;
/* 2381:     */   }
/* 2382:     */   
/* 2383:     */   public void createIslandConfig(String location, String leader)
/* 2384:     */   {
/* 2385:2629 */     saveDefaultIslandsConfig(location);
/* 2386:2630 */     this.islandConfigFile = new File(this.directoryIslands, location + ".yml");
/* 2387:     */     
/* 2388:     */ 
/* 2389:     */ 
/* 2390:2634 */     InputStream defConfigStream = getResource("island.yml");
/* 2391:2635 */     if (defConfigStream != null)
/* 2392:     */     {
/* 2393:2637 */       this.islands.put(location, YamlConfiguration.loadConfiguration(defConfigStream));
/* 2394:2638 */       getIslandConfig(location);
/* 2395:2639 */       setupPartyLeader(location, leader);
/* 2396:     */     }
/* 2397:     */   }
/* 2398:     */   
/* 2399:     */   public FileConfiguration getIslandConfig(String location)
/* 2400:     */   {
/* 2401:2644 */     if (this.islands.get(location) == null) {
/* 2402:2645 */       reloadIslandConfig(location);
/* 2403:     */     }
/* 2404:2647 */     return (FileConfiguration)this.islands.get(location);
/* 2405:     */   }
/* 2406:     */   
/* 2407:     */   public void saveIslandConfig(String location)
/* 2408:     */   {
/* 2409:2655 */     if (this.islands.get(location) == null) {
/* 2410:2656 */       return;
/* 2411:     */     }
/* 2412:     */     try
/* 2413:     */     {
/* 2414:2659 */       this.islandConfigFile = new File(this.directoryIslands, location + ".yml");
/* 2415:2660 */       getIslandConfig(location).save(this.islandConfigFile);
/* 2416:     */     }
/* 2417:     */     catch (IOException ex)
/* 2418:     */     {
/* 2419:2662 */       getLogger().log(Level.SEVERE, "Could not save config to " + this.islandConfigFile, ex);
/* 2420:     */     }
/* 2421:     */   }
/* 2422:     */   
/* 2423:     */   public void deleteIslandConfig(String location)
/* 2424:     */   {
/* 2425:2667 */     this.islandConfigFile = new File(this.directoryIslands, location + ".yml");
/* 2426:2668 */     this.islandConfigFile.delete();
/* 2427:     */   }
/* 2428:     */   
/* 2429:     */   public void saveDefaultIslandsConfig(String location)
/* 2430:     */   {
/* 2431:     */     try
/* 2432:     */     {
/* 2433:2673 */       if (this.islandConfigFile == null)
/* 2434:     */       {
/* 2435:2674 */         this.islandConfigFile = new File(this.directoryIslands, location + ".yml");
/* 2436:2675 */         getIslandConfig(location).save(this.islandConfigFile);
/* 2437:     */       }
/* 2438:     */     }
/* 2439:     */     catch (IOException ex)
/* 2440:     */     {
/* 2441:2678 */       getLogger().log(Level.SEVERE, "Could not save config to " + this.islandConfigFile, ex);
/* 2442:     */     }
/* 2443:     */   }
/* 2444:     */   
/* 2445:     */   public void reloadLastIslandConfig()
/* 2446:     */   {
/* 2447:2684 */     if (this.lastIslandConfigFile == null) {
/* 2448:2685 */       this.lastIslandConfigFile = new File(getDataFolder(), "lastIslandConfig.yml");
/* 2449:     */     }
/* 2450:2687 */     this.lastIslandConfig = YamlConfiguration.loadConfiguration(this.lastIslandConfigFile);
/* 2451:     */     
/* 2452:     */ 
/* 2453:2690 */     InputStream defConfigStream = getResource("lastIslandConfig.yml");
/* 2454:2691 */     if (defConfigStream != null)
/* 2455:     */     {
/* 2456:2692 */       YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
/* 2457:2693 */       this.lastIslandConfig.setDefaults(defConfig);
/* 2458:     */     }
/* 2459:     */   }
/* 2460:     */   
/* 2461:     */   public FileConfiguration getLastIslandConfig()
/* 2462:     */   {
/* 2463:2698 */     if (this.lastIslandConfig == null) {
/* 2464:2699 */       reloadLastIslandConfig();
/* 2465:     */     }
/* 2466:2701 */     return this.lastIslandConfig;
/* 2467:     */   }
/* 2468:     */   
/* 2469:     */   public void saveLastIslandConfig()
/* 2470:     */   {
/* 2471:2705 */     if ((this.lastIslandConfig == null) || (this.lastIslandConfigFile == null)) {
/* 2472:2706 */       return;
/* 2473:     */     }
/* 2474:     */     try
/* 2475:     */     {
/* 2476:2709 */       getLastIslandConfig().save(this.lastIslandConfigFile);
/* 2477:     */     }
/* 2478:     */     catch (IOException ex)
/* 2479:     */     {
/* 2480:2711 */       getLogger().log(Level.SEVERE, "Could not save config to " + this.lastIslandConfigFile, ex);
/* 2481:     */     }
/* 2482:     */   }
/* 2483:     */   
/* 2484:     */   public void saveDefaultLastIslandConfig()
/* 2485:     */   {
/* 2486:2716 */     if (this.lastIslandConfigFile == null) {
/* 2487:2717 */       this.lastIslandConfigFile = new File(getDataFolder(), "lastIslandConfig.yml");
/* 2488:     */     }
/* 2489:2719 */     if (!this.lastIslandConfigFile.exists()) {
/* 2490:2720 */       getInstance().saveResource("lastIslandConfig.yml", false);
/* 2491:     */     }
/* 2492:     */   }
/* 2493:     */   
/* 2494:     */   public void reloadOrphans()
/* 2495:     */   {
/* 2496:2728 */     if (this.orphanFile == null) {
/* 2497:2729 */       this.orphanFile = new File(getDataFolder(), "orphans.yml");
/* 2498:     */     }
/* 2499:2731 */     this.orphans = YamlConfiguration.loadConfiguration(this.orphanFile);
/* 2500:     */     
/* 2501:     */ 
/* 2502:2734 */     InputStream defConfigStream = getResource("orphans.yml");
/* 2503:2735 */     if (defConfigStream != null)
/* 2504:     */     {
/* 2505:2736 */       YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
/* 2506:2737 */       this.orphans.setDefaults(defConfig);
/* 2507:     */     }
/* 2508:     */   }
/* 2509:     */   
/* 2510:     */   public FileConfiguration getOrphans()
/* 2511:     */   {
/* 2512:2742 */     if (this.orphans == null) {
/* 2513:2743 */       reloadOrphans();
/* 2514:     */     }
/* 2515:2745 */     return this.orphans;
/* 2516:     */   }
/* 2517:     */   
/* 2518:     */   public void saveOrphansFile()
/* 2519:     */   {
/* 2520:2749 */     if ((this.orphans == null) || (this.orphanFile == null)) {
/* 2521:2750 */       return;
/* 2522:     */     }
/* 2523:     */     try
/* 2524:     */     {
/* 2525:2753 */       getOrphans().save(this.orphanFile);
/* 2526:     */     }
/* 2527:     */     catch (IOException ex)
/* 2528:     */     {
/* 2529:2755 */       getLogger().log(Level.SEVERE, "Could not save config to " + this.orphanFile, ex);
/* 2530:     */     }
/* 2531:     */   }
/* 2532:     */   
/* 2533:     */   public void saveDefaultOrphans()
/* 2534:     */   {
/* 2535:2760 */     if (this.orphanFile == null) {
/* 2536:2761 */       this.orphanFile = new File(getDataFolder(), "orphans.yml");
/* 2537:     */     }
/* 2538:2763 */     if (!this.orphanFile.exists()) {
/* 2539:2764 */       getInstance().saveResource("orphans.yml", false);
/* 2540:     */     }
/* 2541:     */   }
/* 2542:     */   
/* 2543:     */   public boolean setBiome(Location loc, String bName)
/* 2544:     */   {
/* 2545:2769 */     int px = loc.getBlockX();
/* 2546:2770 */     int pz = loc.getBlockZ();
/* 2547:2771 */     Biome bType = Biome.OCEAN;
/* 2548:2772 */     if (bName.equalsIgnoreCase("jungle")) {
/* 2549:2774 */       bType = Biome.JUNGLE;
/* 2550:2775 */     } else if (bName.equalsIgnoreCase("hell")) {
/* 2551:2777 */       bType = Biome.HELL;
/* 2552:2778 */     } else if (bName.equalsIgnoreCase("sky")) {
/* 2553:2780 */       bType = Biome.SKY;
/* 2554:2781 */     } else if (bName.equalsIgnoreCase("mushroom")) {
/* 2555:2783 */       bType = Biome.MUSHROOM_ISLAND;
/* 2556:2784 */     } else if (bName.equalsIgnoreCase("ocean")) {
/* 2557:2786 */       bType = Biome.OCEAN;
/* 2558:2787 */     } else if (bName.equalsIgnoreCase("swampland")) {
/* 2559:2789 */       bType = Biome.SWAMPLAND;
/* 2560:2790 */     } else if (bName.equalsIgnoreCase("taiga")) {
/* 2561:2792 */       bType = Biome.TAIGA;
/* 2562:2793 */     } else if (bName.equalsIgnoreCase("desert")) {
/* 2563:2795 */       bType = Biome.DESERT;
/* 2564:2796 */     } else if (bName.equalsIgnoreCase("forest")) {
/* 2565:2798 */       bType = Biome.FOREST;
/* 2566:     */     } else {
/* 2567:2801 */       bType = Biome.OCEAN;
/* 2568:     */     }
/* 2569:2803 */     for (int x = Settings.island_protectionRange / 2 * -1 - 16; x <= Settings.island_protectionRange / 2 + 16; x += 16) {
/* 2570:2804 */       for (int z = Settings.island_protectionRange / 2 * -1 - 16; z <= Settings.island_protectionRange / 2 + 16; z += 16) {
/* 2571:2805 */         getSkyBlockWorld().loadChunk((px + x) / 16, (pz + z) / 16);
/* 2572:     */       }
/* 2573:     */     }
/* 2574:2808 */     for (int x = Settings.island_protectionRange / 2 * -1; x <= Settings.island_protectionRange / 2; x++) {
/* 2575:2809 */       for (int z = Settings.island_protectionRange / 2 * -1; z <= Settings.island_protectionRange / 2; z++) {
/* 2576:2811 */         getSkyBlockWorld().setBiome(px + x, pz + z, bType);
/* 2577:     */       }
/* 2578:     */     }
/* 2579:2814 */     for (int x = Settings.island_protectionRange / 2 * -1 - 16; x <= Settings.island_protectionRange / 2 + 16; x += 16) {
/* 2580:2815 */       for (int z = Settings.island_protectionRange / 2 * -1 - 16; z <= Settings.island_protectionRange / 2 + 16; z += 16) {
/* 2581:2816 */         getSkyBlockWorld().refreshChunk((px + x) / 16, (pz + z) / 16);
/* 2582:     */       }
/* 2583:     */     }
/* 2584:2821 */     if (bType == Biome.OCEAN) {
/* 2585:2822 */       return false;
/* 2586:     */     }
/* 2587:2824 */     return true;
/* 2588:     */   }
/* 2589:     */   
/* 2590:     */   public boolean changePlayerBiome(Player player, String bName)
/* 2591:     */   {
/* 2592:2829 */     if (VaultHandler.checkPerk(player.getName(), "usb.biome." + bName, player.getWorld()))
/* 2593:     */     {
/* 2594:2831 */       if (getInstance().getIslandConfig(((PlayerInfo)getInstance().getActivePlayers().get(player.getName())).locationForParty()).getBoolean("party.members." + player.getName() + ".canChangeBiome"))
/* 2595:     */       {
/* 2596:2833 */         setBiome(((PlayerInfo)getInstance().getActivePlayers().get(player.getName())).getIslandLocation(), bName);
/* 2597:2834 */         setConfigBiome(player, bName);
/* 2598:2835 */         return true;
/* 2599:     */       }
/* 2600:2837 */       return false;
/* 2601:     */     }
/* 2602:2839 */     return false;
/* 2603:     */   }
/* 2604:     */   
/* 2605:     */   public void listBiomes(Player player)
/* 2606:     */   {
/* 2607:2844 */     String biomeList = ", ";
/* 2608:2845 */     if (VaultHandler.checkPerk(player.getName(), "usb.biome.ocean", getSkyBlockWorld())) {
/* 2609:2847 */       biomeList = "OCEAN, ";
/* 2610:     */     }
/* 2611:2849 */     if (VaultHandler.checkPerk(player.getName(), "usb.biome.forest", getSkyBlockWorld())) {
/* 2612:2851 */       biomeList = biomeList + "FOREST, ";
/* 2613:     */     }
/* 2614:2853 */     if (VaultHandler.checkPerk(player.getName(), "usb.biome.jungle", getSkyBlockWorld())) {
/* 2615:2855 */       biomeList = biomeList + "JUNGLE, ";
/* 2616:     */     }
/* 2617:2857 */     if (VaultHandler.checkPerk(player.getName(), "usb.biome.desert", getSkyBlockWorld())) {
/* 2618:2859 */       biomeList = biomeList + "DESERT, ";
/* 2619:     */     }
/* 2620:2861 */     if (VaultHandler.checkPerk(player.getName(), "usb.biome.taiga", getSkyBlockWorld())) {
/* 2621:2863 */       biomeList = biomeList + "TAIGA, ";
/* 2622:     */     }
/* 2623:2865 */     if (VaultHandler.checkPerk(player.getName(), "usb.biome.swampland", getSkyBlockWorld())) {
/* 2624:2867 */       biomeList = biomeList + "SWAMPLAND, ";
/* 2625:     */     }
/* 2626:2869 */     if (VaultHandler.checkPerk(player.getName(), "usb.biome.mushroom", getSkyBlockWorld())) {
/* 2627:2871 */       biomeList = biomeList + "MUSHROOM, ";
/* 2628:     */     }
/* 2629:2873 */     if (VaultHandler.checkPerk(player.getName(), "usb.biome.hell", getSkyBlockWorld())) {
/* 2630:2875 */       biomeList = biomeList + "HELL, ";
/* 2631:     */     }
/* 2632:2877 */     if (VaultHandler.checkPerk(player.getName(), "usb.biome.sky", getSkyBlockWorld())) {
/* 2633:2879 */       biomeList = biomeList + "SKY, ";
/* 2634:     */     }
/* 2635:2881 */     player.sendMessage(ChatColor.YELLOW + "You have access to the following Biomes:");
/* 2636:2882 */     player.sendMessage(ChatColor.GREEN + biomeList.substring(0, biomeList.length() - 2));
/* 2637:2883 */     player.sendMessage(ChatColor.YELLOW + "Use /island biome <biomename> to change your biome. You must wait " + Settings.general_biomeChange / 60 + " minutes between each biome change.");
/* 2638:     */   }
/* 2639:     */   
/* 2640:     */   public boolean createIsland(CommandSender sender, PlayerInfo pi)
/* 2641:     */   {
/* 2642:2887 */     System.out.println("Creating player island...");
/* 2643:2888 */     Player player = (Player)sender;
/* 2644:2889 */     Location last = getInstance().getLastIsland();
/* 2645:2890 */     last.setY(Settings.island_height);
/* 2646:     */     try
/* 2647:     */     {
/* 2648:     */       do
/* 2649:     */       {
/* 2650:2895 */         getInstance().removeNextOrphan();
/* 2651:2893 */         if (!getInstance().hasOrphanedIsland()) {
/* 2652:     */           break;
/* 2653:     */         }
/* 2654:2893 */       } while (getInstance().islandAtLocation(getInstance().checkOrphan()));
/* 2655:2898 */       while ((getInstance().hasOrphanedIsland()) && (!getInstance().checkOrphan().getWorld().getName().equalsIgnoreCase(Settings.general_worldName))) {
/* 2656:2900 */         getInstance().removeNextOrphan();
/* 2657:     */       }
/* 2658:     */       Location next;
/* 2659:2903 */       if ((getInstance().hasOrphanedIsland()) && (!getInstance().islandAtLocation(getInstance().checkOrphan())))
/* 2660:     */       {
/* 2661:2904 */         Location next = getInstance().getOrphanedIsland();
/* 2662:2905 */         getInstance().saveOrphans();
/* 2663:     */       }
/* 2664:     */       else
/* 2665:     */       {
/* 2666:2909 */         next = nextIslandLocation(last);
/* 2667:2910 */         getInstance().setLastIsland(next);
/* 2668:2912 */         while (getInstance().islandAtLocation(next)) {
/* 2669:2915 */           next = nextIslandLocation(next);
/* 2670:     */         }
/* 2671:2918 */         while (getInstance().islandInSpawn(next)) {
/* 2672:2921 */           next = nextIslandLocation(next);
/* 2673:     */         }
/* 2674:2924 */         getInstance().setLastIsland(next);
/* 2675:     */       }
/* 2676:2926 */       boolean hasIslandNow = false;
/* 2677:2928 */       if ((getInstance().getSchemFile().length > 0) && (Bukkit.getServer().getPluginManager().isPluginEnabled("WorldEdit")))
/* 2678:     */       {
/* 2679:2930 */         String cSchem = "";
/* 2680:2931 */         for (int i = 0; i < getInstance().getSchemFile().length; i++) {
/* 2681:2933 */           if (!hasIslandNow)
/* 2682:     */           {
/* 2683:2935 */             if (getInstance().getSchemFile()[i].getName().lastIndexOf('.') > 0) {
/* 2684:2937 */               cSchem = getInstance().getSchemFile()[i].getName().substring(0, getInstance().getSchemFile()[i].getName().lastIndexOf('.'));
/* 2685:     */             } else {
/* 2686:2939 */               cSchem = getInstance().getSchemFile()[i].getName();
/* 2687:     */             }
/* 2688:2941 */             if (VaultHandler.checkPerk(player.getName(), "usb.schematic." + cSchem, getSkyBlockWorld())) {
/* 2689:2943 */               if (WorldEditHandler.loadIslandSchematic(getSkyBlockWorld(), getInstance().getSchemFile()[i], next))
/* 2690:     */               {
/* 2691:2945 */                 setChest(next, player);
/* 2692:2946 */                 hasIslandNow = true;
/* 2693:     */               }
/* 2694:     */             }
/* 2695:     */           }
/* 2696:     */         }
/* 2697:2951 */         if (!hasIslandNow) {
/* 2698:2953 */           for (int i = 0; i < getInstance().getSchemFile().length; i++)
/* 2699:     */           {
/* 2700:2955 */             if (getInstance().getSchemFile()[i].getName().lastIndexOf('.') > 0) {
/* 2701:2957 */               cSchem = getInstance().getSchemFile()[i].getName().substring(0, getInstance().getSchemFile()[i].getName().lastIndexOf('.'));
/* 2702:     */             } else {
/* 2703:2959 */               cSchem = getInstance().getSchemFile()[i].getName();
/* 2704:     */             }
/* 2705:2960 */             if (cSchem.equalsIgnoreCase(Settings.island_schematicName)) {
/* 2706:2962 */               if (WorldEditHandler.loadIslandSchematic(getSkyBlockWorld(), getInstance().getSchemFile()[i], next))
/* 2707:     */               {
/* 2708:2964 */                 setChest(next, player);
/* 2709:2965 */                 hasIslandNow = true;
/* 2710:     */               }
/* 2711:     */             }
/* 2712:     */           }
/* 2713:     */         }
/* 2714:     */       }
/* 2715:2971 */       if (!hasIslandNow) {
/* 2716:2973 */         if (!Settings.island_useOldIslands) {
/* 2717:2974 */           generateIslandBlocks(next.getBlockX(), next.getBlockZ(), player, getSkyBlockWorld());
/* 2718:     */         } else {
/* 2719:2976 */           oldGenerateIslandBlocks(next.getBlockX(), next.getBlockZ(), player, getSkyBlockWorld());
/* 2720:     */         }
/* 2721:     */       }
/* 2722:2978 */       next.setY(Settings.island_height);
/* 2723:2979 */       System.out.println(next.getBlockY());
/* 2724:     */       
/* 2725:2981 */       System.out.println("Preparing to set new player information...");
/* 2726:2982 */       setNewPlayerIsland(player, next);
/* 2727:2983 */       System.out.println("Finished setting new player information.");
/* 2728:     */       
/* 2729:2985 */       player.getInventory().clear();
/* 2730:2986 */       player.getEquipment().clear();
/* 2731:2987 */       System.out.println("Preparing to set initial player biome...");
/* 2732:2988 */       getInstance().changePlayerBiome(player, "OCEAN");
/* 2733:2989 */       System.out.println("Finished setting initial player biome.");
/* 2734:2990 */       for (int x = Settings.island_protectionRange / 2 * -1 - 16; x <= Settings.island_protectionRange / 2 + 16; x += 16) {
/* 2735:2991 */         for (int z = Settings.island_protectionRange / 2 * -1 - 16; z <= Settings.island_protectionRange / 2 + 16; z += 16) {
/* 2736:2992 */           getSkyBlockWorld().refreshChunk((next.getBlockX() + x) / 16, (next.getBlockZ() + z) / 16);
/* 2737:     */         }
/* 2738:     */       }
/* 2739:2995 */       Iterator<Entity> ents = player.getNearbyEntities(50.0D, 250.0D, 50.0D).iterator();
/* 2740:2996 */       while (ents.hasNext())
/* 2741:     */       {
/* 2742:2998 */         Entity tempent = (Entity)ents.next();
/* 2743:2999 */         if (!(tempent instanceof Player)) {
/* 2744:3004 */           tempent.remove();
/* 2745:     */         }
/* 2746:     */       }
/* 2747:3006 */       if ((Settings.island_protectWithWorldGuard) && (Bukkit.getServer().getPluginManager().isPluginEnabled("WorldGuard"))) {
/* 2748:3007 */         WorldGuardHandler.protectIsland(player, sender.getName(), pi);
/* 2749:     */       }
/* 2750:     */     }
/* 2751:     */     catch (Exception ex)
/* 2752:     */     {
/* 2753:3010 */       player.sendMessage("Could not create your Island. Pleace contact a server moderator.");
/* 2754:3011 */       ex.printStackTrace();
/* 2755:3012 */       return false;
/* 2756:     */     }
/* 2757:3014 */     System.out.println("Finished creating player island.");
/* 2758:3015 */     return true;
/* 2759:     */   }
/* 2760:     */   
/* 2761:     */   public void generateIslandBlocks(int x, int z, Player player, World world)
/* 2762:     */   {
/* 2763:3021 */     int y = Settings.island_height;
/* 2764:3022 */     Block blockToChange = world.getBlockAt(x, y, z);
/* 2765:3023 */     blockToChange.setTypeId(7);
/* 2766:3024 */     islandLayer1(x, z, player, world);
/* 2767:3025 */     islandLayer2(x, z, player, world);
/* 2768:3026 */     islandLayer3(x, z, player, world);
/* 2769:3027 */     islandLayer4(x, z, player, world);
/* 2770:3028 */     islandExtras(x, z, player, world);
/* 2771:     */   }
/* 2772:     */   
/* 2773:     */   public void oldGenerateIslandBlocks(int x, int z, Player player, World world)
/* 2774:     */   {
/* 2775:3033 */     int y = Settings.island_height;
/* 2776:3035 */     for (int x_operate = x; x_operate < x + 3; x_operate++) {
/* 2777:3036 */       for (int y_operate = y; y_operate < y + 3; y_operate++) {
/* 2778:3037 */         for (int z_operate = z; z_operate < z + 6; z_operate++)
/* 2779:     */         {
/* 2780:3038 */           Block blockToChange = world.getBlockAt(x_operate, y_operate, z_operate);
/* 2781:3039 */           blockToChange.setTypeId(2);
/* 2782:     */         }
/* 2783:     */       }
/* 2784:     */     }
/* 2785:3044 */     for (int x_operate = x + 3; x_operate < x + 6; x_operate++) {
/* 2786:3045 */       for (int y_operate = y; y_operate < y + 3; y_operate++) {
/* 2787:3046 */         for (int z_operate = z + 3; z_operate < z + 6; z_operate++)
/* 2788:     */         {
/* 2789:3047 */           Block blockToChange = world.getBlockAt(x_operate, y_operate, z_operate);
/* 2790:3048 */           blockToChange.setTypeId(2);
/* 2791:     */         }
/* 2792:     */       }
/* 2793:     */     }
/* 2794:3054 */     for (int x_operate = x + 3; x_operate < x + 7; x_operate++) {
/* 2795:3055 */       for (int y_operate = y + 7; y_operate < y + 10; y_operate++) {
/* 2796:3056 */         for (int z_operate = z + 3; z_operate < z + 7; z_operate++)
/* 2797:     */         {
/* 2798:3057 */           Block blockToChange = world.getBlockAt(x_operate, y_operate, z_operate);
/* 2799:3058 */           blockToChange.setTypeId(18);
/* 2800:     */         }
/* 2801:     */       }
/* 2802:     */     }
/* 2803:3064 */     for (int y_operate = y + 3; y_operate < y + 9; y_operate++)
/* 2804:     */     {
/* 2805:3065 */       Block blockToChange = world.getBlockAt(x + 5, y_operate, z + 5);
/* 2806:3066 */       blockToChange.setTypeId(17);
/* 2807:     */     }
/* 2808:3071 */     Block blockToChange = world.getBlockAt(x + 1, y + 3, z + 1);
/* 2809:3072 */     blockToChange.setTypeId(54);
/* 2810:3073 */     Chest chest = (Chest)blockToChange.getState();
/* 2811:3074 */     Inventory inventory = chest.getInventory();
/* 2812:3075 */     inventory.clear();
/* 2813:3076 */     inventory.setContents(Settings.island_chestItems);
/* 2814:3077 */     if (Settings.island_addExtraItems) {
/* 2815:3079 */       for (int i = 0; i < Settings.island_extraPermissions.length; i++) {
/* 2816:3081 */         if (VaultHandler.checkPerk(player.getName(), "usb." + Settings.island_extraPermissions[i], player.getWorld()))
/* 2817:     */         {
/* 2818:3083 */           String[] chestItemString = getInstance().getConfig().getString("options.island.extraPermissions." + Settings.island_extraPermissions[i]).split(" ");
/* 2819:3084 */           ItemStack[] tempChest = new ItemStack[chestItemString.length];
/* 2820:3085 */           String[] amountdata = new String[2];
/* 2821:3086 */           for (int j = 0; j < chestItemString.length; j++)
/* 2822:     */           {
/* 2823:3088 */             amountdata = chestItemString[j].split(":");
/* 2824:3089 */             tempChest[j] = new ItemStack(Integer.parseInt(amountdata[0]), Integer.parseInt(amountdata[1]));
/* 2825:3090 */             inventory.addItem(new ItemStack[] { tempChest[j] });
/* 2826:     */           }
/* 2827:     */         }
/* 2828:     */       }
/* 2829:     */     }
/* 2830:3096 */     blockToChange = world.getBlockAt(x, y, z);
/* 2831:3097 */     blockToChange.setTypeId(7);
/* 2832:     */     
/* 2833:     */ 
/* 2834:3100 */     blockToChange = world.getBlockAt(x + 2, y + 1, z + 1);
/* 2835:3101 */     blockToChange.setTypeId(12);
/* 2836:3102 */     blockToChange = world.getBlockAt(x + 2, y + 1, z + 2);
/* 2837:3103 */     blockToChange.setTypeId(12);
/* 2838:3104 */     blockToChange = world.getBlockAt(x + 2, y + 1, z + 3);
/* 2839:3105 */     blockToChange.setTypeId(12);
/* 2840:     */   }
/* 2841:     */   
/* 2842:     */   private Location nextIslandLocation(Location lastIsland)
/* 2843:     */   {
/* 2844:3112 */     int x = (int)lastIsland.getX();
/* 2845:3113 */     int z = (int)lastIsland.getZ();
/* 2846:3114 */     Location nextPos = lastIsland;
/* 2847:3115 */     if (x < z)
/* 2848:     */     {
/* 2849:3117 */       if (-1 * x < z)
/* 2850:     */       {
/* 2851:3119 */         nextPos.setX(nextPos.getX() + Settings.island_distance);
/* 2852:3120 */         return nextPos;
/* 2853:     */       }
/* 2854:3122 */       nextPos.setZ(nextPos.getZ() + Settings.island_distance);
/* 2855:3123 */       return nextPos;
/* 2856:     */     }
/* 2857:3125 */     if (x > z)
/* 2858:     */     {
/* 2859:3127 */       if (-1 * x >= z)
/* 2860:     */       {
/* 2861:3129 */         nextPos.setX(nextPos.getX() - Settings.island_distance);
/* 2862:3130 */         return nextPos;
/* 2863:     */       }
/* 2864:3132 */       nextPos.setZ(nextPos.getZ() - Settings.island_distance);
/* 2865:3133 */       return nextPos;
/* 2866:     */     }
/* 2867:3135 */     if (x <= 0)
/* 2868:     */     {
/* 2869:3137 */       nextPos.setZ(nextPos.getZ() + Settings.island_distance);
/* 2870:3138 */       return nextPos;
/* 2871:     */     }
/* 2872:3140 */     nextPos.setZ(nextPos.getZ() - Settings.island_distance);
/* 2873:3141 */     return nextPos;
/* 2874:     */   }
/* 2875:     */   
/* 2876:     */   private void islandLayer1(int x, int z, Player player, World world)
/* 2877:     */   {
/* 2878:3147 */     int y = Settings.island_height;
/* 2879:3148 */     y = Settings.island_height + 4;
/* 2880:3149 */     for (int x_operate = x - 3; x_operate <= x + 3; x_operate++) {
/* 2881:3151 */       for (int z_operate = z - 3; z_operate <= z + 3; z_operate++)
/* 2882:     */       {
/* 2883:3153 */         Block blockToChange = world.getBlockAt(x_operate, y, z_operate);
/* 2884:3154 */         blockToChange.setTypeId(2);
/* 2885:     */       }
/* 2886:     */     }
/* 2887:3157 */     Block blockToChange = world.getBlockAt(x - 3, y, z + 3);
/* 2888:3158 */     blockToChange.setTypeId(0);
/* 2889:3159 */     blockToChange = world.getBlockAt(x - 3, y, z - 3);
/* 2890:3160 */     blockToChange.setTypeId(0);
/* 2891:3161 */     blockToChange = world.getBlockAt(x + 3, y, z - 3);
/* 2892:3162 */     blockToChange.setTypeId(0);
/* 2893:3163 */     blockToChange = world.getBlockAt(x + 3, y, z + 3);
/* 2894:3164 */     blockToChange.setTypeId(0);
/* 2895:     */   }
/* 2896:     */   
/* 2897:     */   private void islandLayer2(int x, int z, Player player, World world)
/* 2898:     */   {
/* 2899:3169 */     int y = Settings.island_height;
/* 2900:3170 */     y = Settings.island_height + 3;
/* 2901:3171 */     for (int x_operate = x - 2; x_operate <= x + 2; x_operate++) {
/* 2902:3173 */       for (int z_operate = z - 2; z_operate <= z + 2; z_operate++)
/* 2903:     */       {
/* 2904:3175 */         Block blockToChange = world.getBlockAt(x_operate, y, z_operate);
/* 2905:3176 */         blockToChange.setTypeId(3);
/* 2906:     */       }
/* 2907:     */     }
/* 2908:3179 */     Block blockToChange = world.getBlockAt(x - 3, y, z);
/* 2909:3180 */     blockToChange.setTypeId(3);
/* 2910:3181 */     blockToChange = world.getBlockAt(x + 3, y, z);
/* 2911:3182 */     blockToChange.setTypeId(3);
/* 2912:3183 */     blockToChange = world.getBlockAt(x, y, z - 3);
/* 2913:3184 */     blockToChange.setTypeId(3);
/* 2914:3185 */     blockToChange = world.getBlockAt(x, y, z + 3);
/* 2915:3186 */     blockToChange.setTypeId(3);
/* 2916:3187 */     blockToChange = world.getBlockAt(x, y, z);
/* 2917:3188 */     blockToChange.setTypeId(12);
/* 2918:     */   }
/* 2919:     */   
/* 2920:     */   private void islandLayer3(int x, int z, Player player, World world)
/* 2921:     */   {
/* 2922:3194 */     int y = Settings.island_height;
/* 2923:3195 */     y = Settings.island_height + 2;
/* 2924:3196 */     for (int x_operate = x - 1; x_operate <= x + 1; x_operate++) {
/* 2925:3198 */       for (int z_operate = z - 1; z_operate <= z + 1; z_operate++)
/* 2926:     */       {
/* 2927:3200 */         Block blockToChange = world.getBlockAt(x_operate, y, z_operate);
/* 2928:3201 */         blockToChange.setTypeId(3);
/* 2929:     */       }
/* 2930:     */     }
/* 2931:3204 */     Block blockToChange = world.getBlockAt(x - 2, y, z);
/* 2932:3205 */     blockToChange.setTypeId(3);
/* 2933:3206 */     blockToChange = world.getBlockAt(x + 2, y, z);
/* 2934:3207 */     blockToChange.setTypeId(3);
/* 2935:3208 */     blockToChange = world.getBlockAt(x, y, z - 2);
/* 2936:3209 */     blockToChange.setTypeId(3);
/* 2937:3210 */     blockToChange = world.getBlockAt(x, y, z + 2);
/* 2938:3211 */     blockToChange.setTypeId(3);
/* 2939:3212 */     blockToChange = world.getBlockAt(x, y, z);
/* 2940:3213 */     blockToChange.setTypeId(12);
/* 2941:     */   }
/* 2942:     */   
/* 2943:     */   private void islandLayer4(int x, int z, Player player, World world)
/* 2944:     */   {
/* 2945:3219 */     int y = Settings.island_height;
/* 2946:3220 */     y = Settings.island_height + 1;
/* 2947:3221 */     Block blockToChange = world.getBlockAt(x - 1, y, z);
/* 2948:3222 */     blockToChange.setTypeId(3);
/* 2949:3223 */     blockToChange = world.getBlockAt(x + 1, y, z);
/* 2950:3224 */     blockToChange.setTypeId(3);
/* 2951:3225 */     blockToChange = world.getBlockAt(x, y, z - 1);
/* 2952:3226 */     blockToChange.setTypeId(3);
/* 2953:3227 */     blockToChange = world.getBlockAt(x, y, z + 1);
/* 2954:3228 */     blockToChange.setTypeId(3);
/* 2955:3229 */     blockToChange = world.getBlockAt(x, y, z);
/* 2956:3230 */     blockToChange.setTypeId(12);
/* 2957:     */   }
/* 2958:     */   
/* 2959:     */   private void islandExtras(int x, int z, Player player, World world)
/* 2960:     */   {
/* 2961:3236 */     int y = Settings.island_height;
/* 2962:     */     
/* 2963:3238 */     Block blockToChange = world.getBlockAt(x, y + 5, z);
/* 2964:3239 */     blockToChange.setTypeId(17);
/* 2965:3240 */     blockToChange = world.getBlockAt(x, y + 6, z);
/* 2966:3241 */     blockToChange.setTypeId(17);
/* 2967:3242 */     blockToChange = world.getBlockAt(x, y + 7, z);
/* 2968:3243 */     blockToChange.setTypeId(17);
/* 2969:3244 */     y = Settings.island_height + 8;
/* 2970:3245 */     for (int x_operate = x - 2; x_operate <= x + 2; x_operate++) {
/* 2971:3247 */       for (int z_operate = z - 2; z_operate <= z + 2; z_operate++)
/* 2972:     */       {
/* 2973:3249 */         blockToChange = world.getBlockAt(x_operate, y, z_operate);
/* 2974:3250 */         blockToChange.setTypeId(18);
/* 2975:     */       }
/* 2976:     */     }
/* 2977:3253 */     blockToChange = world.getBlockAt(x + 2, y, z + 2);
/* 2978:3254 */     blockToChange.setTypeId(0);
/* 2979:3255 */     blockToChange = world.getBlockAt(x + 2, y, z - 2);
/* 2980:3256 */     blockToChange.setTypeId(0);
/* 2981:3257 */     blockToChange = world.getBlockAt(x - 2, y, z + 2);
/* 2982:3258 */     blockToChange.setTypeId(0);
/* 2983:3259 */     blockToChange = world.getBlockAt(x - 2, y, z - 2);
/* 2984:3260 */     blockToChange.setTypeId(0);
/* 2985:3261 */     blockToChange = world.getBlockAt(x, y, z);
/* 2986:3262 */     blockToChange.setTypeId(17);
/* 2987:3263 */     y = Settings.island_height + 9;
/* 2988:3264 */     for (int x_operate = x - 1; x_operate <= x + 1; x_operate++) {
/* 2989:3266 */       for (int z_operate = z - 1; z_operate <= z + 1; z_operate++)
/* 2990:     */       {
/* 2991:3268 */         blockToChange = world.getBlockAt(x_operate, y, z_operate);
/* 2992:3269 */         blockToChange.setTypeId(18);
/* 2993:     */       }
/* 2994:     */     }
/* 2995:3272 */     blockToChange = world.getBlockAt(x - 2, y, z);
/* 2996:3273 */     blockToChange.setTypeId(18);
/* 2997:3274 */     blockToChange = world.getBlockAt(x + 2, y, z);
/* 2998:3275 */     blockToChange.setTypeId(18);
/* 2999:3276 */     blockToChange = world.getBlockAt(x, y, z - 2);
/* 3000:3277 */     blockToChange.setTypeId(18);
/* 3001:3278 */     blockToChange = world.getBlockAt(x, y, z + 2);
/* 3002:3279 */     blockToChange.setTypeId(18);
/* 3003:3280 */     blockToChange = world.getBlockAt(x, y, z);
/* 3004:3281 */     blockToChange.setTypeId(17);
/* 3005:3282 */     y = Settings.island_height + 10;
/* 3006:3283 */     blockToChange = world.getBlockAt(x - 1, y, z);
/* 3007:3284 */     blockToChange.setTypeId(18);
/* 3008:3285 */     blockToChange = world.getBlockAt(x + 1, y, z);
/* 3009:3286 */     blockToChange.setTypeId(18);
/* 3010:3287 */     blockToChange = world.getBlockAt(x, y, z - 1);
/* 3011:3288 */     blockToChange.setTypeId(18);
/* 3012:3289 */     blockToChange = world.getBlockAt(x, y, z + 1);
/* 3013:3290 */     blockToChange.setTypeId(18);
/* 3014:3291 */     blockToChange = world.getBlockAt(x, y, z);
/* 3015:3292 */     blockToChange.setTypeId(17);
/* 3016:3293 */     blockToChange = world.getBlockAt(x, y + 1, z);
/* 3017:3294 */     blockToChange.setTypeId(18);
/* 3018:     */     
/* 3019:3296 */     blockToChange = world.getBlockAt(x, Settings.island_height + 5, z + 1);
/* 3020:3297 */     blockToChange.setTypeId(54);
/* 3021:3298 */     Chest chest = (Chest)blockToChange.getState();
/* 3022:3299 */     Inventory inventory = chest.getInventory();
/* 3023:3300 */     inventory.clear();
/* 3024:3301 */     inventory.setContents(Settings.island_chestItems);
/* 3025:3302 */     if (Settings.island_addExtraItems) {
/* 3026:3304 */       for (int i = 0; i < Settings.island_extraPermissions.length; i++) {
/* 3027:3306 */         if (VaultHandler.checkPerk(player.getName(), "usb." + Settings.island_extraPermissions[i], player.getWorld()))
/* 3028:     */         {
/* 3029:3308 */           String[] chestItemString = getInstance().getConfig().getString("options.island.extraPermissions." + Settings.island_extraPermissions[i]).split(" ");
/* 3030:3309 */           ItemStack[] tempChest = new ItemStack[chestItemString.length];
/* 3031:3310 */           String[] amountdata = new String[2];
/* 3032:3311 */           for (int j = 0; j < chestItemString.length; j++)
/* 3033:     */           {
/* 3034:3313 */             amountdata = chestItemString[j].split(":");
/* 3035:3314 */             tempChest[j] = new ItemStack(Integer.parseInt(amountdata[0]), Integer.parseInt(amountdata[1]));
/* 3036:3315 */             inventory.addItem(new ItemStack[] { tempChest[j] });
/* 3037:     */           }
/* 3038:     */         }
/* 3039:     */       }
/* 3040:     */     }
/* 3041:     */   }
/* 3042:     */   
/* 3043:     */   public void setChest(Location loc, Player player)
/* 3044:     */   {
/* 3045:3325 */     for (int x = -15; x <= 15; x++) {
/* 3046:3326 */       for (int y = -15; y <= 15; y++) {
/* 3047:3327 */         for (int z = -15; z <= 15; z++) {
/* 3048:3328 */           if (getSkyBlockWorld().getBlockAt(loc.getBlockX() + x, loc.getBlockY() + y, loc.getBlockZ() + z).getTypeId() == 54)
/* 3049:     */           {
/* 3050:3330 */             Block blockToChange = getSkyBlockWorld().getBlockAt(loc.getBlockX() + x, loc.getBlockY() + y, loc.getBlockZ() + z);
/* 3051:3331 */             Chest chest = (Chest)blockToChange.getState();
/* 3052:3332 */             Inventory inventory = chest.getInventory();
/* 3053:3333 */             inventory.clear();
/* 3054:3334 */             inventory.setContents(Settings.island_chestItems);
/* 3055:3335 */             if (Settings.island_addExtraItems) {
/* 3056:3337 */               for (int i = 0; i < Settings.island_extraPermissions.length; i++) {
/* 3057:3339 */                 if (VaultHandler.checkPerk(player.getName(), "usb." + Settings.island_extraPermissions[i], player.getWorld()))
/* 3058:     */                 {
/* 3059:3341 */                   String[] chestItemString = getInstance().getConfig().getString("options.island.extraPermissions." + Settings.island_extraPermissions[i]).split(" ");
/* 3060:3342 */                   ItemStack[] tempChest = new ItemStack[chestItemString.length];
/* 3061:3343 */                   String[] amountdata = new String[2];
/* 3062:3344 */                   for (int j = 0; j < chestItemString.length; j++)
/* 3063:     */                   {
/* 3064:3346 */                     amountdata = chestItemString[j].split(":");
/* 3065:3347 */                     tempChest[j] = new ItemStack(Integer.parseInt(amountdata[0]), Integer.parseInt(amountdata[1]));
/* 3066:3348 */                     inventory.addItem(new ItemStack[] { tempChest[j] });
/* 3067:     */                   }
/* 3068:     */                 }
/* 3069:     */               }
/* 3070:     */             }
/* 3071:     */           }
/* 3072:     */         }
/* 3073:     */       }
/* 3074:     */     }
/* 3075:     */   }
/* 3076:     */   
/* 3077:     */   public Location getChestSpawnLoc(Location loc, Player player)
/* 3078:     */   {
/* 3079:3363 */     for (int x = -15; x <= 15; x++) {
/* 3080:3364 */       for (int y = -15; y <= 15; y++) {
/* 3081:3365 */         for (int z = -15; z <= 15; z++) {
/* 3082:3366 */           if (getSkyBlockWorld().getBlockAt(loc.getBlockX() + x, loc.getBlockY() + y, loc.getBlockZ() + z).getTypeId() == 54)
/* 3083:     */           {
/* 3084:3368 */             if ((getSkyBlockWorld().getBlockAt(loc.getBlockX() + x, loc.getBlockY() + y, loc.getBlockZ() + (z + 1)).getTypeId() == 0) && 
/* 3085:3369 */               (getSkyBlockWorld().getBlockAt(loc.getBlockX() + x, loc.getBlockY() + (y - 1), loc.getBlockZ() + (z + 1)).getTypeId() != 0)) {
/* 3086:3370 */               return new Location(getSkyBlockWorld(), loc.getBlockX() + x, loc.getBlockY() + (y + 1), loc.getBlockZ() + (z + 1));
/* 3087:     */             }
/* 3088:3371 */             if ((getSkyBlockWorld().getBlockAt(loc.getBlockX() + x, loc.getBlockY() + y, loc.getBlockZ() + (z - 1)).getTypeId() == 0) && 
/* 3089:3372 */               (getSkyBlockWorld().getBlockAt(loc.getBlockX() + x, loc.getBlockY() + (y - 1), loc.getBlockZ() + (z - 1)).getTypeId() != 0)) {
/* 3090:3373 */               return new Location(getSkyBlockWorld(), loc.getBlockX() + x, loc.getBlockY() + (y + 1), loc.getBlockZ() + (z + 1));
/* 3091:     */             }
/* 3092:3374 */             if ((getSkyBlockWorld().getBlockAt(loc.getBlockX() + (x + 1), loc.getBlockY() + y, loc.getBlockZ() + z).getTypeId() == 0) && 
/* 3093:3375 */               (getSkyBlockWorld().getBlockAt(loc.getBlockX() + (x + 1), loc.getBlockY() + (y - 1), loc.getBlockZ() + z).getTypeId() != 0)) {
/* 3094:3376 */               return new Location(getSkyBlockWorld(), loc.getBlockX() + x, loc.getBlockY() + (y + 1), loc.getBlockZ() + (z + 1));
/* 3095:     */             }
/* 3096:3377 */             if ((getSkyBlockWorld().getBlockAt(loc.getBlockX() + (x - 1), loc.getBlockY() + y, loc.getBlockZ() + z).getTypeId() == 0) && 
/* 3097:3378 */               (getSkyBlockWorld().getBlockAt(loc.getBlockX() + (x - 1), loc.getBlockY() + (y - 1), loc.getBlockZ() + z).getTypeId() != 0)) {
/* 3098:3379 */               return new Location(getSkyBlockWorld(), loc.getBlockX() + x, loc.getBlockY() + (y + 1), loc.getBlockZ() + (z + 1));
/* 3099:     */             }
/* 3100:3380 */             loc.setY(loc.getY() + 1.0D);
/* 3101:3381 */             return loc;
/* 3102:     */           }
/* 3103:     */         }
/* 3104:     */       }
/* 3105:     */     }
/* 3106:3386 */     return loc;
/* 3107:     */   }
/* 3108:     */   
/* 3109:     */   private void setNewPlayerIsland(Player player, Location loc)
/* 3110:     */   {
/* 3111:3391 */     ((PlayerInfo)getInstance().getActivePlayers().get(player.getName())).startNewIsland(loc);
/* 3112:3392 */     player.teleport(getChestSpawnLoc(loc, player));
/* 3113:3393 */     if (getIslandConfig(((PlayerInfo)getInstance().getActivePlayers().get(player.getName())).locationForParty()) == null) {
/* 3114:3395 */       createIslandConfig(((PlayerInfo)getInstance().getActivePlayers().get(player.getName())).locationForParty(), player.getName());
/* 3115:     */     }
/* 3116:3397 */     clearIslandConfig(((PlayerInfo)getInstance().getActivePlayers().get(player.getName())).locationForParty(), player.getName());
/* 3117:3398 */     getInstance().updatePartyNumber(player);
/* 3118:3399 */     getInstance().homeSet(player);
/* 3119:3400 */     ((PlayerInfo)getInstance().getActivePlayers().get(player.getName())).savePlayerConfig(player.getName());
/* 3120:     */   }
/* 3121:     */   
/* 3122:     */   public void setWarpLocation(String location, Location loc)
/* 3123:     */   {
/* 3124:3405 */     getIslandConfig(location).set("general.warpLocationX", Integer.valueOf(loc.getBlockX()));
/* 3125:3406 */     getIslandConfig(location).set("general.warpLocationY", Integer.valueOf(loc.getBlockY()));
/* 3126:3407 */     getIslandConfig(location).set("general.warpLocationZ", Integer.valueOf(loc.getBlockZ()));
/* 3127:3408 */     getIslandConfig(location).set("general.warpActive", Boolean.valueOf(true));
/* 3128:3409 */     saveIslandConfig(location);
/* 3129:     */   }
/* 3130:     */   
/* 3131:     */   public void buildIslandList()
/* 3132:     */   {
/* 3133:3414 */     File folder = getInstance().directoryPlayers;
/* 3134:3415 */     File[] listOfFiles = folder.listFiles();
/* 3135:     */     
/* 3136:3417 */     System.out.print(ChatColor.YELLOW + "[uSkyBlock] Building a new island list...");
/* 3137:3418 */     for (int i = 0; i < listOfFiles.length; i++)
/* 3138:     */     {
/* 3139:3420 */       PlayerInfo pi = new PlayerInfo(listOfFiles[i].getName());
/* 3140:3421 */       if (pi.getHasIsland())
/* 3141:     */       {
/* 3142:3423 */         System.out.print("Creating new island file for " + pi.getPlayerName());
/* 3143:3424 */         createIslandConfig(pi.locationForParty(), pi.getPlayerName());
/* 3144:3425 */         saveIslandConfig(pi.locationForParty());
/* 3145:     */       }
/* 3146:     */     }
/* 3147:3428 */     for (int i = 0; i < listOfFiles.length; i++)
/* 3148:     */     {
/* 3149:3430 */       PlayerInfo pi = new PlayerInfo(listOfFiles[i].getName());
/* 3150:3431 */       if (!pi.getHasIsland()) {
/* 3151:3433 */         if (pi.getPartyIslandLocation() != null) {
/* 3152:3435 */           if (getTempIslandConfig(pi.locationForPartyOld()) != null) {
/* 3153:3437 */             if (!getTempIslandConfig(pi.locationForPartyOld()).contains("party.members." + pi.getPlayerName()))
/* 3154:     */             {
/* 3155:3439 */               setupPartyMember(pi.locationForPartyOld(), pi.getPlayerName());
/* 3156:3440 */               saveIslandConfig(pi.locationForParty());
/* 3157:     */             }
/* 3158:     */           }
/* 3159:     */         }
/* 3160:     */       }
/* 3161:     */     }
/* 3162:3446 */     System.out.print(ChatColor.YELLOW + "[uSkyBlock] Party list completed.");
/* 3163:     */   }
/* 3164:     */   
/* 3165:     */   public void removeIslandConfig(String location)
/* 3166:     */   {
/* 3167:3451 */     this.islands.remove(location);
/* 3168:     */   }
/* 3169:     */   
/* 3170:     */   public void displayIslandConfigs()
/* 3171:     */   {
/* 3172:3456 */     Iterator<String> islandList = this.islands.keySet().iterator();
/* 3173:3457 */     while (islandList.hasNext()) {
/* 3174:3459 */       System.out.print((String)islandList.next());
/* 3175:     */     }
/* 3176:     */   }
/* 3177:     */   
/* 3178:     */   public void updatePartyNumber(Player player)
/* 3179:     */   {
/* 3180:3465 */     if (getInstance().getIslandConfig(((PlayerInfo)getInstance().getActivePlayers().get(player.getName())).locationForParty()).getInt("party.maxSize") < 8) {
/* 3181:3467 */       if (VaultHandler.checkPerk(player.getName(), "usb.extra.partysize", player.getWorld()))
/* 3182:     */       {
/* 3183:3469 */         getInstance().getIslandConfig(((PlayerInfo)getInstance().getActivePlayers().get(player.getName())).locationForParty()).set("party.maxSize", Integer.valueOf(8));
/* 3184:3470 */         getInstance().saveIslandConfig(((PlayerInfo)getInstance().getActivePlayers().get(player.getName())).locationForParty());
/* 3185:3471 */         return;
/* 3186:     */       }
/* 3187:     */     }
/* 3188:3474 */     if (getInstance().getIslandConfig(((PlayerInfo)getInstance().getActivePlayers().get(player.getName())).locationForParty()).getInt("party.maxSize") < 7) {
/* 3189:3476 */       if (VaultHandler.checkPerk(player.getName(), "usb.extra.party3", player.getWorld()))
/* 3190:     */       {
/* 3191:3478 */         getInstance().getIslandConfig(((PlayerInfo)getInstance().getActivePlayers().get(player.getName())).locationForParty()).set("party.maxSize", Integer.valueOf(7));
/* 3192:3479 */         getInstance().saveIslandConfig(((PlayerInfo)getInstance().getActivePlayers().get(player.getName())).locationForParty());
/* 3193:3480 */         return;
/* 3194:     */       }
/* 3195:     */     }
/* 3196:3483 */     if (getInstance().getIslandConfig(((PlayerInfo)getInstance().getActivePlayers().get(player.getName())).locationForParty()).getInt("party.maxSize") < 6) {
/* 3197:3485 */       if (VaultHandler.checkPerk(player.getName(), "usb.extra.party2", player.getWorld()))
/* 3198:     */       {
/* 3199:3487 */         getInstance().getIslandConfig(((PlayerInfo)getInstance().getActivePlayers().get(player.getName())).locationForParty()).set("party.maxSize", Integer.valueOf(6));
/* 3200:3488 */         getInstance().saveIslandConfig(((PlayerInfo)getInstance().getActivePlayers().get(player.getName())).locationForParty());
/* 3201:3489 */         return;
/* 3202:     */       }
/* 3203:     */     }
/* 3204:3492 */     if (getInstance().getIslandConfig(((PlayerInfo)getInstance().getActivePlayers().get(player.getName())).locationForParty()).getInt("party.maxSize") < 5) {
/* 3205:3494 */       if (VaultHandler.checkPerk(player.getName(), "usb.extra.party1", player.getWorld()))
/* 3206:     */       {
/* 3207:3496 */         getInstance().getIslandConfig(((PlayerInfo)getInstance().getActivePlayers().get(player.getName())).locationForParty()).set("party.maxSize", Integer.valueOf(5));
/* 3208:3497 */         getInstance().saveIslandConfig(((PlayerInfo)getInstance().getActivePlayers().get(player.getName())).locationForParty());
/* 3209:3498 */         return;
/* 3210:     */       }
/* 3211:     */     }
/* 3212:     */   }
/* 3213:     */   
/* 3214:     */   public void changePlayerPermission(Player player, String playername, String perm)
/* 3215:     */   {
/* 3216:3505 */     if (!getInstance().getIslandConfig(((PlayerInfo)getInstance().getActivePlayers().get(player.getName())).locationForParty()).contains("party.members." + playername + "." + perm)) {
/* 3217:3506 */       return;
/* 3218:     */     }
/* 3219:3507 */     if (getInstance().getIslandConfig(((PlayerInfo)getInstance().getActivePlayers().get(player.getName())).locationForParty()).getBoolean("party.members." + playername + "." + perm)) {
/* 3220:3508 */       getInstance().getIslandConfig(((PlayerInfo)getInstance().getActivePlayers().get(player.getName())).locationForParty()).set("party.members." + playername + "." + perm, Boolean.valueOf(false));
/* 3221:     */     } else {
/* 3222:3510 */       getInstance().getIslandConfig(((PlayerInfo)getInstance().getActivePlayers().get(player.getName())).locationForParty()).set("party.members." + playername + "." + perm, Boolean.valueOf(true));
/* 3223:     */     }
/* 3224:3511 */     getInstance().saveIslandConfig(((PlayerInfo)getInstance().getActivePlayers().get(player.getName())).locationForParty());
/* 3225:     */   }
/* 3226:     */   
/* 3227:     */   public boolean checkForOnlineMembers(Player p)
/* 3228:     */   {
/* 3229:3516 */     Iterator<String> temp = getInstance().getIslandConfig(((PlayerInfo)getInstance().getActivePlayers().get(p.getName())).locationForParty()).getConfigurationSection("party.members").getKeys(false).iterator();
/* 3230:3517 */     while (temp.hasNext())
/* 3231:     */     {
/* 3232:3519 */       String tString = (String)temp.next();
/* 3233:3520 */       if ((Bukkit.getPlayer(tString) != null) && (!Bukkit.getPlayer(tString).getName().equalsIgnoreCase(p.getName()))) {
/* 3234:3522 */         return true;
/* 3235:     */       }
/* 3236:     */     }
/* 3237:3525 */     return false;
/* 3238:     */   }
/* 3239:     */   
/* 3240:     */   public boolean checkCurrentBiome(Player p, String biome)
/* 3241:     */   {
/* 3242:3530 */     if (getInstance().getIslandConfig(((PlayerInfo)getInstance().getActivePlayers().get(p.getName())).locationForParty()).getString("general.biome").equalsIgnoreCase(biome)) {
/* 3243:3532 */       return true;
/* 3244:     */     }
/* 3245:3534 */     return false;
/* 3246:     */   }
/* 3247:     */   
/* 3248:     */   public void setConfigBiome(Player p, String biome)
/* 3249:     */   {
/* 3250:3539 */     getInstance().getIslandConfig(((PlayerInfo)getInstance().getActivePlayers().get(p.getName())).locationForParty()).set("general.biome", biome);
/* 3251:3540 */     getInstance().saveIslandConfig(((PlayerInfo)getInstance().getActivePlayers().get(p.getName())).locationForParty());
/* 3252:     */   }
/* 3253:     */   
/* 3254:     */   public Inventory displayPartyPlayerGUI(Player player, String pname)
/* 3255:     */   {
/* 3256:3546 */     this.GUIpartyPlayer = Bukkit.createInventory(null, 9, pname + " <Permissions>");
/* 3257:3547 */     ItemStack pHead = new ItemStack(397, 1, (short)3);
/* 3258:3548 */     SkullMeta meta3 = (SkullMeta)pHead.getItemMeta();
/* 3259:3549 */     ItemMeta meta4 = this.sign.getItemMeta();
/* 3260:3550 */     meta4.setDisplayName("Â§hPlayer Permissions");
/* 3261:3551 */     this.lores.add("Â§eClick here to return to");
/* 3262:3552 */     this.lores.add("Â§eyour island group's info.");
/* 3263:3553 */     meta4.setLore(this.lores);
/* 3264:3554 */     this.sign.setItemMeta(meta4);
/* 3265:3555 */     this.GUIpartyPlayer.addItem(new ItemStack[] { this.sign });
/* 3266:3556 */     this.lores.clear();
/* 3267:3557 */     meta3.setDisplayName(pname + "'s Permissions");
/* 3268:3558 */     this.lores.add("Â§eHover over an icon to view");
/* 3269:3559 */     this.lores.add("Â§ea permission. Change the");
/* 3270:3560 */     this.lores.add("Â§epermission by clicking it.");
/* 3271:3561 */     meta3.setLore(this.lores);
/* 3272:3562 */     pHead.setItemMeta(meta3);
/* 3273:3563 */     this.GUIpartyPlayer.addItem(new ItemStack[] { pHead });
/* 3274:3564 */     this.lores.clear();
/* 3275:     */     
/* 3276:3566 */     meta4 = this.biome.getItemMeta();
/* 3277:3567 */     if (getInstance().getIslandConfig(((PlayerInfo)getInstance().getActivePlayers().get(player.getName())).locationForParty()).getBoolean("party.members." + pname + ".canChangeBiome"))
/* 3278:     */     {
/* 3279:3569 */       meta4.setDisplayName("Â§aChange Biome");
/* 3280:3570 */       this.lores.add("Â§fThis player Â§acanÂ§f change the");
/* 3281:3571 */       this.lores.add("Â§fisland's biome. Click here");
/* 3282:3572 */       this.lores.add("Â§fto remove this permission.");
/* 3283:     */     }
/* 3284:     */     else
/* 3285:     */     {
/* 3286:3575 */       meta4.setDisplayName("Â§cChange Biome");
/* 3287:3576 */       this.lores.add("Â§fThis player Â§ccannotÂ§f change the");
/* 3288:3577 */       this.lores.add("Â§fisland's biome. Click here");
/* 3289:3578 */       this.lores.add("Â§fto grant this permission.");
/* 3290:     */     }
/* 3291:3580 */     meta4.setLore(this.lores);
/* 3292:3581 */     this.biome.setItemMeta(meta4);
/* 3293:3582 */     this.GUIpartyPlayer.addItem(new ItemStack[] { this.biome });
/* 3294:3583 */     this.lores.clear();
/* 3295:     */     
/* 3296:3585 */     meta4 = this.lock.getItemMeta();
/* 3297:3586 */     if (getInstance().getIslandConfig(((PlayerInfo)getInstance().getActivePlayers().get(player.getName())).locationForParty()).getBoolean("party.members." + pname + ".canToggleLock"))
/* 3298:     */     {
/* 3299:3588 */       meta4.setDisplayName("Â§aToggle Island Lock");
/* 3300:3589 */       this.lores.add("Â§fThis player Â§acanÂ§f toggle the");
/* 3301:3590 */       this.lores.add("Â§fisland's lock, which prevents");
/* 3302:3591 */       this.lores.add("Â§fnon-group members from entering.");
/* 3303:3592 */       this.lores.add("Â§fClick here to remove this permission.");
/* 3304:     */     }
/* 3305:     */     else
/* 3306:     */     {
/* 3307:3595 */       meta4.setDisplayName("Â§cToggle Island Lock");
/* 3308:3596 */       this.lores.add("Â§fThis player Â§ccannotÂ§f toggle the");
/* 3309:3597 */       this.lores.add("Â§fisland's lock, which prevents");
/* 3310:3598 */       this.lores.add("Â§fnon-group members from entering.");
/* 3311:3599 */       this.lores.add("Â§fClick here to add this permission");
/* 3312:     */     }
/* 3313:3601 */     meta4.setLore(this.lores);
/* 3314:3602 */     this.lock.setItemMeta(meta4);
/* 3315:3603 */     this.GUIpartyPlayer.addItem(new ItemStack[] { this.lock });
/* 3316:3604 */     this.lores.clear();
/* 3317:     */     
/* 3318:3606 */     meta4 = this.warpset.getItemMeta();
/* 3319:3607 */     if (getInstance().getIslandConfig(((PlayerInfo)getInstance().getActivePlayers().get(player.getName())).locationForParty()).getBoolean("party.members." + pname + ".canChangeWarp"))
/* 3320:     */     {
/* 3321:3609 */       meta4.setDisplayName("Â§aSet Island Warp");
/* 3322:3610 */       this.lores.add("Â§fThis player Â§acanÂ§f set the");
/* 3323:3611 */       this.lores.add("Â§fisland's warp, which allows");
/* 3324:3612 */       this.lores.add("Â§fnon-group members to teleport");
/* 3325:3613 */       this.lores.add("Â§fto the island. Click here to");
/* 3326:3614 */       this.lores.add("Â§fremove this permission.");
/* 3327:     */     }
/* 3328:     */     else
/* 3329:     */     {
/* 3330:3617 */       meta4.setDisplayName("Â§cSet Island Warp");
/* 3331:3618 */       this.lores.add("Â§fThis player Â§ccannotÂ§f set the");
/* 3332:3619 */       this.lores.add("Â§fisland's warp, which allows");
/* 3333:3620 */       this.lores.add("Â§fnon-group members to teleport");
/* 3334:3621 */       this.lores.add("Â§fto the island. Click here to");
/* 3335:3622 */       this.lores.add("Â§fadd this permission.");
/* 3336:     */     }
/* 3337:3624 */     meta4.setLore(this.lores);
/* 3338:3625 */     this.warpset.setItemMeta(meta4);
/* 3339:3626 */     this.GUIpartyPlayer.addItem(new ItemStack[] { this.warpset });
/* 3340:3627 */     this.lores.clear();
/* 3341:     */     
/* 3342:3629 */     meta4 = this.warptoggle.getItemMeta();
/* 3343:3630 */     if (getInstance().getIslandConfig(((PlayerInfo)getInstance().getActivePlayers().get(player.getName())).locationForParty()).getBoolean("party.members." + pname + ".canToggleWarp"))
/* 3344:     */     {
/* 3345:3632 */       meta4.setDisplayName("Â§aToggle Island Warp");
/* 3346:3633 */       this.lores.add("Â§fThis player Â§acanÂ§f toggle the");
/* 3347:3634 */       this.lores.add("Â§fisland's warp, allowing them");
/* 3348:3635 */       this.lores.add("Â§fto turn it on or off at anytime.");
/* 3349:3636 */       this.lores.add("Â§fbut not set the location. Click");
/* 3350:3637 */       this.lores.add("Â§fhere to remove this permission.");
/* 3351:     */     }
/* 3352:     */     else
/* 3353:     */     {
/* 3354:3640 */       meta4.setDisplayName("Â§cToggle Island Warp");
/* 3355:3641 */       this.lores.add("Â§fThis player Â§ccannotÂ§f toggle the");
/* 3356:3642 */       this.lores.add("Â§fisland's warp, allowing them");
/* 3357:3643 */       this.lores.add("Â§fto turn it on or off at anytime,");
/* 3358:3644 */       this.lores.add("Â§fbut not set the location. Click");
/* 3359:3645 */       this.lores.add("Â§fhere to add this permission.");
/* 3360:     */     }
/* 3361:3647 */     meta4.setLore(this.lores);
/* 3362:3648 */     this.warptoggle.setItemMeta(meta4);
/* 3363:3649 */     this.GUIpartyPlayer.addItem(new ItemStack[] { this.warptoggle });
/* 3364:3650 */     this.lores.clear();
/* 3365:     */     
/* 3366:3652 */     meta4 = this.invite.getItemMeta();
/* 3367:3653 */     if (getInstance().getIslandConfig(((PlayerInfo)getInstance().getActivePlayers().get(player.getName())).locationForParty()).getBoolean("party.members." + pname + ".canInviteOthers"))
/* 3368:     */     {
/* 3369:3655 */       meta4.setDisplayName("Â§aInvite Players");
/* 3370:3656 */       this.lores.add("Â§fThis player Â§acanÂ§f invite");
/* 3371:3657 */       this.lores.add("Â§fother players to the island if");
/* 3372:3658 */       this.lores.add("Â§fthere is enough room for more");
/* 3373:3659 */       this.lores.add("Â§fmembers. Click here to remove");
/* 3374:3660 */       this.lores.add("Â§fthis permission.");
/* 3375:     */     }
/* 3376:     */     else
/* 3377:     */     {
/* 3378:3663 */       meta4.setDisplayName("Â§cInvite Players");
/* 3379:3664 */       this.lores.add("Â§fThis player Â§ccannotÂ§f invite");
/* 3380:3665 */       this.lores.add("Â§fother players to the island.");
/* 3381:3666 */       this.lores.add("Â§fClick here to add this permission.");
/* 3382:     */     }
/* 3383:3668 */     meta4.setLore(this.lores);
/* 3384:3669 */     this.invite.setItemMeta(meta4);
/* 3385:3670 */     this.GUIpartyPlayer.addItem(new ItemStack[] { this.invite });
/* 3386:3671 */     this.lores.clear();
/* 3387:     */     
/* 3388:3673 */     meta4 = this.kick.getItemMeta();
/* 3389:3674 */     if (getInstance().getIslandConfig(((PlayerInfo)getInstance().getActivePlayers().get(player.getName())).locationForParty()).getBoolean("party.members." + pname + ".canKickOthers"))
/* 3390:     */     {
/* 3391:3676 */       meta4.setDisplayName("Â§aKick Players");
/* 3392:3677 */       this.lores.add("Â§fThis player Â§acanÂ§f kick");
/* 3393:3678 */       this.lores.add("Â§fother players from the island,");
/* 3394:3679 */       this.lores.add("Â§fbut they are unable to kick");
/* 3395:3680 */       this.lores.add("Â§fthe island leader. Click here");
/* 3396:3681 */       this.lores.add("Â§fto remove this permission.");
/* 3397:     */     }
/* 3398:     */     else
/* 3399:     */     {
/* 3400:3684 */       meta4.setDisplayName("Â§cKick Players");
/* 3401:3685 */       this.lores.add("Â§fThis player Â§ccannotÂ§f kick");
/* 3402:3686 */       this.lores.add("Â§fother players from the island.");
/* 3403:3687 */       this.lores.add("Â§fClick here to add this permission.");
/* 3404:     */     }
/* 3405:3689 */     meta4.setLore(this.lores);
/* 3406:3690 */     this.kick.setItemMeta(meta4);
/* 3407:3691 */     this.GUIpartyPlayer.addItem(new ItemStack[] { this.kick });
/* 3408:3692 */     this.lores.clear();
/* 3409:3693 */     return this.GUIpartyPlayer;
/* 3410:     */   }
/* 3411:     */   
/* 3412:     */   public Inventory displayPartyGUI(Player player)
/* 3413:     */   {
/* 3414:3697 */     this.GUIparty = Bukkit.createInventory(null, 18, "Â§9Island Group Members");
/* 3415:     */     
/* 3416:3699 */     Set<String> memberList = getInstance().getIslandConfig(((PlayerInfo)getInstance().getActivePlayers().get(player.getName())).locationForParty()).getConfigurationSection("party.members").getKeys(false);
/* 3417:3700 */     this.tempIt = memberList.iterator();
/* 3418:3701 */     SkullMeta meta3 = (SkullMeta)this.pHead.getItemMeta();
/* 3419:3702 */     ItemMeta meta4 = this.sign.getItemMeta();
/* 3420:3703 */     meta4.setDisplayName("Â§aGroup Info");
/* 3421:3704 */     this.lores.add("Group Members: Â§2" + getInstance().getIslandConfig(((PlayerInfo)getInstance().getActivePlayers().get(player.getName())).locationForParty()).getInt("party.currentSize") + "Â§7/Â§e" + getInstance().getIslandConfig(((PlayerInfo)getInstance().getActivePlayers().get(player.getName())).locationForParty()).getInt("party.maxSize"));
/* 3422:3705 */     if (getInstance().getIslandConfig(((PlayerInfo)getInstance().getActivePlayers().get(player.getName())).locationForParty()).getInt("party.currentSize") < getInstance().getIslandConfig(((PlayerInfo)getInstance().getActivePlayers().get(player.getName())).locationForParty()).getInt("party.maxSize")) {
/* 3423:3706 */       this.lores.add("Â§aMore players can be invited to this island.");
/* 3424:     */     } else {
/* 3425:3708 */       this.lores.add("Â§cThis island is full.");
/* 3426:     */     }
/* 3427:3709 */     this.lores.add("Â§eHover over a player's icon to");
/* 3428:3710 */     this.lores.add("Â§eview their permissions. The");
/* 3429:3711 */     this.lores.add("Â§eleader can change permissions");
/* 3430:3712 */     this.lores.add("Â§eby clicking a player's icon.");
/* 3431:3713 */     meta4.setLore(this.lores);
/* 3432:3714 */     this.sign.setItemMeta(meta4);
/* 3433:3715 */     this.GUIparty.addItem(new ItemStack[] { this.sign });
/* 3434:3716 */     this.lores.clear();
/* 3435:3717 */     while (this.tempIt.hasNext())
/* 3436:     */     {
/* 3437:3719 */       String temp = (String)this.tempIt.next();
/* 3438:3720 */       if (temp.equalsIgnoreCase(getInstance().getIslandConfig(((PlayerInfo)getInstance().getActivePlayers().get(player.getName())).locationForParty()).getString("party.leader")))
/* 3439:     */       {
/* 3440:3722 */         meta3.setDisplayName("Â§f" + temp);
/* 3441:3723 */         this.lores.add("Â§aÂ§lLeader");
/* 3442:3724 */         this.lores.add("Â§aCan Â§fchange the island's biome.");
/* 3443:3725 */         this.lores.add("Â§aCan Â§flock/unlock the island.");
/* 3444:3726 */         this.lores.add("Â§aCan Â§fset the island's warp.");
/* 3445:3727 */         this.lores.add("Â§aCan Â§ftoggle the island's warp.");
/* 3446:3728 */         this.lores.add("Â§aCan Â§finvite others to the island.");
/* 3447:3729 */         this.lores.add("Â§aCan Â§fkick others from the island.");
/* 3448:3730 */         meta3.setLore(this.lores);
/* 3449:3731 */         this.lores.clear();
/* 3450:     */       }
/* 3451:     */       else
/* 3452:     */       {
/* 3453:3734 */         meta3.setDisplayName("Â§f" + temp);
/* 3454:3735 */         this.lores.add("Â§eÂ§lMember");
/* 3455:3736 */         if (getInstance().getIslandConfig(((PlayerInfo)getInstance().getActivePlayers().get(player.getName())).locationForParty()).getBoolean("party.members." + temp + ".canChangeBiome")) {
/* 3456:3737 */           this.lores.add("Â§aCan Â§fchange the island's biome.");
/* 3457:     */         } else {
/* 3458:3739 */           this.lores.add("Â§cCannot Â§fchange the island's biome.");
/* 3459:     */         }
/* 3460:3740 */         if (getInstance().getIslandConfig(((PlayerInfo)getInstance().getActivePlayers().get(player.getName())).locationForParty()).getBoolean("party.members." + temp + ".canToggleLock")) {
/* 3461:3741 */           this.lores.add("Â§aCan Â§flock/unlock the island.");
/* 3462:     */         } else {
/* 3463:3743 */           this.lores.add("Â§cCannot Â§flock/unlock the island.");
/* 3464:     */         }
/* 3465:3744 */         if (getInstance().getIslandConfig(((PlayerInfo)getInstance().getActivePlayers().get(player.getName())).locationForParty()).getBoolean("party.members." + temp + ".canChangeWarp")) {
/* 3466:3745 */           this.lores.add("Â§aCan Â§fset the island's warp.");
/* 3467:     */         } else {
/* 3468:3747 */           this.lores.add("Â§cCannot Â§fset the island's warp.");
/* 3469:     */         }
/* 3470:3748 */         if (getInstance().getIslandConfig(((PlayerInfo)getInstance().getActivePlayers().get(player.getName())).locationForParty()).getBoolean("party.members." + temp + ".canToggleWarp")) {
/* 3471:3749 */           this.lores.add("Â§aCan Â§ftoggle the island's warp.");
/* 3472:     */         } else {
/* 3473:3751 */           this.lores.add("Â§cCannot Â§ftoggle the island's warp.");
/* 3474:     */         }
/* 3475:3752 */         if (getInstance().getIslandConfig(((PlayerInfo)getInstance().getActivePlayers().get(player.getName())).locationForParty()).getBoolean("party.members." + temp + ".canInviteOthers")) {
/* 3476:3753 */           this.lores.add("Â§aCan Â§finvite others to the island.");
/* 3477:     */         } else {
/* 3478:3755 */           this.lores.add("Â§cCannot Â§finvite others to the island.");
/* 3479:     */         }
/* 3480:3756 */         if (getInstance().getIslandConfig(((PlayerInfo)getInstance().getActivePlayers().get(player.getName())).locationForParty()).getBoolean("party.members." + temp + ".canKickOthers")) {
/* 3481:3757 */           this.lores.add("Â§aCan Â§fkick others from the island.");
/* 3482:     */         } else {
/* 3483:3759 */           this.lores.add("Â§cCannot Â§fkick others from the island.");
/* 3484:     */         }
/* 3485:3760 */         if (player.getName().equalsIgnoreCase(getInstance().getIslandConfig(((PlayerInfo)getInstance().getActivePlayers().get(player.getName())).locationForParty()).getString("party.leader"))) {
/* 3486:3762 */           this.lores.add("Â§e<Click to change this player's permissions>");
/* 3487:     */         }
/* 3488:3764 */         meta3.setLore(this.lores);
/* 3489:3765 */         this.lores.clear();
/* 3490:     */       }
/* 3491:3767 */       meta3.setOwner(temp);
/* 3492:3768 */       this.pHead.setItemMeta(meta3);
/* 3493:3769 */       this.GUIparty.addItem(new ItemStack[] { this.pHead });
/* 3494:     */     }
/* 3495:3771 */     return this.GUIparty;
/* 3496:     */   }
/* 3497:     */   
/* 3498:     */   public Inventory displayLogGUI(Player player)
/* 3499:     */   {
/* 3500:3776 */     this.GUIlog = Bukkit.createInventory(null, 9, "Â§9Island Log");
/* 3501:3777 */     ItemMeta meta4 = this.sign.getItemMeta();
/* 3502:3778 */     meta4.setDisplayName("Â§lIsland Log");
/* 3503:3779 */     this.lores.add("Â§eClick here to return to");
/* 3504:3780 */     this.lores.add("Â§ethe main island screen.");
/* 3505:3781 */     meta4.setLore(this.lores);
/* 3506:3782 */     this.sign.setItemMeta(meta4);
/* 3507:3783 */     this.GUIlog.addItem(new ItemStack[] { this.sign });
/* 3508:3784 */     this.lores.clear();
/* 3509:3785 */     this.currentLogItem = new ItemStack(Material.BOOK_AND_QUILL, 1);
/* 3510:3786 */     meta4 = this.currentLogItem.getItemMeta();
/* 3511:3787 */     meta4.setDisplayName("Â§eÂ§lIsland Log");
/* 3512:3788 */     for (int i = 1; i <= 10; i++) {
/* 3513:3790 */       if (getInstance().getIslandConfig(((PlayerInfo)getActivePlayers().get(player.getName())).locationForParty()).contains("log." + i)) {
/* 3514:3791 */         this.lores.add(getInstance().getIslandConfig(((PlayerInfo)getActivePlayers().get(player.getName())).locationForParty()).getString("log." + i));
/* 3515:     */       }
/* 3516:     */     }
/* 3517:3793 */     meta4.setLore(this.lores);
/* 3518:3794 */     this.currentLogItem.setItemMeta(meta4);
/* 3519:3795 */     this.GUIlog.setItem(8, this.currentLogItem);
/* 3520:3796 */     this.lores.clear();
/* 3521:3797 */     return this.GUIlog;
/* 3522:     */   }
/* 3523:     */   
/* 3524:     */   public Inventory displayBiomeGUI(Player player)
/* 3525:     */   {
/* 3526:3802 */     this.GUIbiome = Bukkit.createInventory(null, 18, "Â§9Island Biome");
/* 3527:     */     
/* 3528:3804 */     ItemMeta meta4 = this.sign.getItemMeta();
/* 3529:3805 */     meta4.setDisplayName("Â§hIsland Biome");
/* 3530:3806 */     this.lores.add("Â§eClick here to return to");
/* 3531:3807 */     this.lores.add("Â§ethe main island screen.");
/* 3532:3808 */     meta4.setLore(this.lores);
/* 3533:3809 */     this.sign.setItemMeta(meta4);
/* 3534:3810 */     this.GUIbiome.addItem(new ItemStack[] { this.sign });
/* 3535:3811 */     this.lores.clear();
/* 3536:     */     
/* 3537:3813 */     this.currentBiomeItem = new ItemStack(Material.WATER, 1);
/* 3538:3814 */     meta4 = this.currentBiomeItem.getItemMeta();
/* 3539:3815 */     if (VaultHandler.checkPerk(player.getName(), "usb.biome.ocean", player.getWorld()))
/* 3540:     */     {
/* 3541:3817 */       meta4.setDisplayName("Â§aBiome: Ocean");
/* 3542:3818 */       this.lores.add("Â§fThe ocean biome is the basic");
/* 3543:3819 */       this.lores.add("Â§fstarting biome for all islands.");
/* 3544:3820 */       this.lores.add("Â§fpassive mobs like animals will");
/* 3545:3821 */       this.lores.add("Â§fnot spawn. Hostile mobs will");
/* 3546:3822 */       this.lores.add("Â§fspawn normally.");
/* 3547:3823 */       if (checkCurrentBiome(player, "OCEAN")) {
/* 3548:3825 */         this.lores.add("Â§2Â§lThis is your current biome.");
/* 3549:     */       } else {
/* 3550:3828 */         this.lores.add("Â§eÂ§lClick to change to this biome.");
/* 3551:     */       }
/* 3552:     */     }
/* 3553:     */     else
/* 3554:     */     {
/* 3555:3832 */       meta4.setDisplayName("Â§8Biome: Ocean");
/* 3556:3833 */       this.lores.add("Â§cYou cannot use this biome.");
/* 3557:3834 */       this.lores.add("Â§7The ocean biome is the basic");
/* 3558:3835 */       this.lores.add("Â§7starting biome for all islands.");
/* 3559:3836 */       this.lores.add("Â§7passive mobs like animals will");
/* 3560:3837 */       this.lores.add("Â§7not spawn. Hostile mobs will");
/* 3561:3838 */       this.lores.add("Â§7spawn normally.");
/* 3562:     */     }
/* 3563:3840 */     meta4.setLore(this.lores);
/* 3564:3841 */     this.currentBiomeItem.setItemMeta(meta4);
/* 3565:3842 */     this.GUIbiome.addItem(new ItemStack[] { this.currentBiomeItem });
/* 3566:3843 */     this.lores.clear();
/* 3567:     */     
/* 3568:3845 */     this.currentBiomeItem = new ItemStack(Material.SAPLING, 1, (short)1);
/* 3569:3846 */     meta4 = this.currentBiomeItem.getItemMeta();
/* 3570:3847 */     if (VaultHandler.checkPerk(player.getName(), "usb.biome.forst", player.getWorld()))
/* 3571:     */     {
/* 3572:3849 */       meta4.setDisplayName("Â§aBiome: Forest");
/* 3573:3850 */       this.lores.add("Â§fThe forest biome will allow");
/* 3574:3851 */       this.lores.add("Â§fyour island to spawn passive.");
/* 3575:3852 */       this.lores.add("Â§fmobs like animals (including");
/* 3576:3853 */       this.lores.add("Â§fwolves). Hostile mobs will");
/* 3577:3854 */       this.lores.add("Â§fspawn normally.");
/* 3578:3855 */       if (checkCurrentBiome(player, "FOREST")) {
/* 3579:3857 */         this.lores.add("Â§2Â§lThis is your current biome.");
/* 3580:     */       } else {
/* 3581:3860 */         this.lores.add("Â§eÂ§lClick to change to this biome.");
/* 3582:     */       }
/* 3583:     */     }
/* 3584:     */     else
/* 3585:     */     {
/* 3586:3864 */       meta4.setDisplayName("Â§8Biome: Forest");
/* 3587:3865 */       this.lores.add("Â§cYou cannot use this biome.");
/* 3588:3866 */       this.lores.add("Â§7The forest biome will allow");
/* 3589:3867 */       this.lores.add("Â§7your island to spawn passive.");
/* 3590:3868 */       this.lores.add("Â§7mobs like animals (including");
/* 3591:3869 */       this.lores.add("Â§7wolves). Hostile mobs will");
/* 3592:3870 */       this.lores.add("Â§7spawn normally.");
/* 3593:     */     }
/* 3594:3872 */     meta4.setLore(this.lores);
/* 3595:3873 */     this.currentBiomeItem.setItemMeta(meta4);
/* 3596:3874 */     this.GUIbiome.addItem(new ItemStack[] { this.currentBiomeItem });
/* 3597:3875 */     this.lores.clear();
/* 3598:     */     
/* 3599:3877 */     this.currentBiomeItem = new ItemStack(Material.SAND, 1);
/* 3600:3878 */     meta4 = this.currentBiomeItem.getItemMeta();
/* 3601:3879 */     if (VaultHandler.checkPerk(player.getName(), "usb.biome.desert", player.getWorld()))
/* 3602:     */     {
/* 3603:3881 */       meta4.setDisplayName("Â§aBiome: Desert");
/* 3604:3882 */       this.lores.add("Â§fThe desert biome makes it so");
/* 3605:3883 */       this.lores.add("Â§fthat there is no rain or snow");
/* 3606:3884 */       this.lores.add("Â§fon your island. Passive mobs");
/* 3607:3885 */       this.lores.add("Â§fwon't spawn. Hostile mobs will");
/* 3608:3886 */       this.lores.add("Â§fspawn normally.");
/* 3609:3887 */       if (checkCurrentBiome(player, "DESERT")) {
/* 3610:3889 */         this.lores.add("Â§2Â§lThis is your current biome.");
/* 3611:     */       } else {
/* 3612:3892 */         this.lores.add("Â§eÂ§lClick to change to this biome.");
/* 3613:     */       }
/* 3614:     */     }
/* 3615:     */     else
/* 3616:     */     {
/* 3617:3896 */       meta4.setDisplayName("Â§8Biome: Desert");
/* 3618:3897 */       this.lores.add("Â§cYou cannot use this biome.");
/* 3619:3898 */       this.lores.add("Â§7The desert biome makes it so");
/* 3620:3899 */       this.lores.add("Â§7that there is no rain or snow");
/* 3621:3900 */       this.lores.add("Â§7on your island. Passive mobs");
/* 3622:3901 */       this.lores.add("Â§7won't spawn. Hostile mobs will");
/* 3623:3902 */       this.lores.add("Â§7spawn normally.");
/* 3624:     */     }
/* 3625:3904 */     meta4.setLore(this.lores);
/* 3626:3905 */     this.currentBiomeItem.setItemMeta(meta4);
/* 3627:3906 */     this.GUIbiome.addItem(new ItemStack[] { this.currentBiomeItem });
/* 3628:3907 */     this.lores.clear();
/* 3629:     */     
/* 3630:3909 */     this.currentBiomeItem = new ItemStack(Material.SAPLING, 1, (short)3);
/* 3631:3910 */     meta4 = this.currentBiomeItem.getItemMeta();
/* 3632:3911 */     if (VaultHandler.checkPerk(player.getName(), "usb.biome.jungle", player.getWorld()))
/* 3633:     */     {
/* 3634:3913 */       meta4.setDisplayName("Â§aBiome: Jungle");
/* 3635:3914 */       this.lores.add("Â§fThe jungle biome is bright");
/* 3636:3915 */       this.lores.add("Â§fand colorful. Passive mobs");
/* 3637:3916 */       this.lores.add("Â§f(including ocelots) will");
/* 3638:3917 */       this.lores.add("Â§fspawn. Hostile mobs will");
/* 3639:3918 */       this.lores.add("Â§fspawn normally.");
/* 3640:3919 */       if (checkCurrentBiome(player, "JUNGLE")) {
/* 3641:3921 */         this.lores.add("Â§2Â§lThis is your current biome.");
/* 3642:     */       } else {
/* 3643:3924 */         this.lores.add("Â§eÂ§lClick to change to this biome.");
/* 3644:     */       }
/* 3645:     */     }
/* 3646:     */     else
/* 3647:     */     {
/* 3648:3928 */       meta4.setDisplayName("Â§8Biome: Jungle");
/* 3649:3929 */       this.lores.add("Â§cYou cannot use this biome.");
/* 3650:3930 */       this.lores.add("Â§7The jungle biome is bright");
/* 3651:3931 */       this.lores.add("Â§7and colorful. Passive mobs");
/* 3652:3932 */       this.lores.add("Â§7(including ocelots) will");
/* 3653:3933 */       this.lores.add("Â§7spawn. Hostile mobs will");
/* 3654:3934 */       this.lores.add("Â§7spawn normally.");
/* 3655:     */     }
/* 3656:3936 */     meta4.setLore(this.lores);
/* 3657:3937 */     this.currentBiomeItem.setItemMeta(meta4);
/* 3658:3938 */     this.GUIbiome.addItem(new ItemStack[] { this.currentBiomeItem });
/* 3659:3939 */     this.lores.clear();
/* 3660:     */     
/* 3661:3941 */     this.currentBiomeItem = new ItemStack(Material.WATER_LILY, 1);
/* 3662:3942 */     meta4 = this.currentBiomeItem.getItemMeta();
/* 3663:3943 */     if (VaultHandler.checkPerk(player.getName(), "usb.biome.swampland", player.getWorld()))
/* 3664:     */     {
/* 3665:3945 */       meta4.setDisplayName("Â§aBiome: Swampland");
/* 3666:3946 */       this.lores.add("Â§fThe swamp biome is dark");
/* 3667:3947 */       this.lores.add("Â§fand dull. Passive mobs");
/* 3668:3948 */       this.lores.add("Â§fwill spawn normally and");
/* 3669:3949 */       this.lores.add("Â§fslimes have a small chance");
/* 3670:3950 */       this.lores.add("Â§fto spawn at night depending");
/* 3671:3951 */       this.lores.add("Â§fon the moon phase.");
/* 3672:3952 */       if (checkCurrentBiome(player, "SWAMPLAND")) {
/* 3673:3954 */         this.lores.add("Â§2Â§lThis is your current biome.");
/* 3674:     */       } else {
/* 3675:3957 */         this.lores.add("Â§eÂ§lClick to change to this biome.");
/* 3676:     */       }
/* 3677:     */     }
/* 3678:     */     else
/* 3679:     */     {
/* 3680:3961 */       meta4.setDisplayName("Â§8Biome: Swampland");
/* 3681:3962 */       this.lores.add("Â§cYou cannot use this biome.");
/* 3682:3963 */       this.lores.add("Â§7The swamp biome is dark");
/* 3683:3964 */       this.lores.add("Â§7and dull. Passive mobs");
/* 3684:3965 */       this.lores.add("Â§7will spawn normally and");
/* 3685:3966 */       this.lores.add("Â§7slimes have a small chance");
/* 3686:3967 */       this.lores.add("Â§7to spawn at night depending");
/* 3687:3968 */       this.lores.add("Â§7on the moon phase.");
/* 3688:     */     }
/* 3689:3970 */     meta4.setLore(this.lores);
/* 3690:3971 */     this.currentBiomeItem.setItemMeta(meta4);
/* 3691:3972 */     this.GUIbiome.addItem(new ItemStack[] { this.currentBiomeItem });
/* 3692:3973 */     this.lores.clear();
/* 3693:     */     
/* 3694:3975 */     this.currentBiomeItem = new ItemStack(Material.SNOW, 1);
/* 3695:3976 */     meta4 = this.currentBiomeItem.getItemMeta();
/* 3696:3977 */     if (VaultHandler.checkPerk(player.getName(), "usb.biome.taiga", player.getWorld()))
/* 3697:     */     {
/* 3698:3979 */       meta4.setDisplayName("Â§aBiome: Taiga");
/* 3699:3980 */       this.lores.add("Â§fThe taiga biome has snow");
/* 3700:3981 */       this.lores.add("Â§finstead of rain. Passive");
/* 3701:3982 */       this.lores.add("Â§fmobs will spawn normally");
/* 3702:3983 */       this.lores.add("Â§f(including wolves) and");
/* 3703:3984 */       this.lores.add("Â§fhostile mobs will spawn.");
/* 3704:3985 */       if (checkCurrentBiome(player, "TAIGA")) {
/* 3705:3987 */         this.lores.add("Â§2Â§lThis is your current biome.");
/* 3706:     */       } else {
/* 3707:3990 */         this.lores.add("Â§eÂ§lClick to change to this biome.");
/* 3708:     */       }
/* 3709:     */     }
/* 3710:     */     else
/* 3711:     */     {
/* 3712:3994 */       meta4.setDisplayName("Â§8Biome: Taiga");
/* 3713:3995 */       this.lores.add("Â§cYou cannot use this biome.");
/* 3714:3996 */       this.lores.add("Â§7The taiga biome has snow");
/* 3715:3997 */       this.lores.add("Â§7instead of rain. Passive");
/* 3716:3998 */       this.lores.add("Â§7mobs will spawn normally");
/* 3717:3999 */       this.lores.add("Â§7(including wolves) and");
/* 3718:4000 */       this.lores.add("Â§7hostile mobs will spawn.");
/* 3719:     */     }
/* 3720:4002 */     meta4.setLore(this.lores);
/* 3721:4003 */     this.currentBiomeItem.setItemMeta(meta4);
/* 3722:4004 */     this.GUIbiome.addItem(new ItemStack[] { this.currentBiomeItem });
/* 3723:4005 */     this.lores.clear();
/* 3724:     */     
/* 3725:4007 */     this.currentBiomeItem = new ItemStack(Material.RED_MUSHROOM, 1);
/* 3726:4008 */     meta4 = this.currentBiomeItem.getItemMeta();
/* 3727:4009 */     if (VaultHandler.checkPerk(player.getName(), "usb.biome.mushroom", player.getWorld()))
/* 3728:     */     {
/* 3729:4011 */       meta4.setDisplayName("Â§aBiome: Mushroom");
/* 3730:4012 */       this.lores.add("Â§fThe mushroom biome is");
/* 3731:4013 */       this.lores.add("Â§fbright and colorful.");
/* 3732:4014 */       this.lores.add("Â§fMooshrooms are the only");
/* 3733:4015 */       this.lores.add("Â§fmobs that will spawn.");
/* 3734:4016 */       this.lores.add("Â§fNo other passive or");
/* 3735:4017 */       this.lores.add("Â§fhostile mobs will spawn.");
/* 3736:4018 */       if (checkCurrentBiome(player, "MUSHROOM")) {
/* 3737:4020 */         this.lores.add("Â§2Â§lThis is your current biome.");
/* 3738:     */       } else {
/* 3739:4023 */         this.lores.add("Â§eÂ§lClick to change to this biome.");
/* 3740:     */       }
/* 3741:     */     }
/* 3742:     */     else
/* 3743:     */     {
/* 3744:4027 */       meta4.setDisplayName("Â§8Biome: Mushroom");
/* 3745:4028 */       this.lores.add("Â§cYou cannot use this biome.");
/* 3746:4029 */       this.lores.add("Â§7The mushroom biome is");
/* 3747:4030 */       this.lores.add("Â§7bright and colorful.");
/* 3748:4031 */       this.lores.add("Â§7Mooshrooms are the only");
/* 3749:4032 */       this.lores.add("Â§7mobs that will spawn.");
/* 3750:4033 */       this.lores.add("Â§7No other passive or");
/* 3751:4034 */       this.lores.add("Â§7hostile mobs will spawn.");
/* 3752:     */     }
/* 3753:4036 */     meta4.setLore(this.lores);
/* 3754:4037 */     this.currentBiomeItem.setItemMeta(meta4);
/* 3755:4038 */     this.GUIbiome.addItem(new ItemStack[] { this.currentBiomeItem });
/* 3756:4039 */     this.lores.clear();
/* 3757:     */     
/* 3758:4041 */     this.currentBiomeItem = new ItemStack(Material.FIRE, 1);
/* 3759:4042 */     meta4 = this.currentBiomeItem.getItemMeta();
/* 3760:4043 */     if (VaultHandler.checkPerk(player.getName(), "usb.biome.hell", player.getWorld()))
/* 3761:     */     {
/* 3762:4045 */       meta4.setDisplayName("Â§aBiome: Hell(Nether)");
/* 3763:4046 */       this.lores.add("Â§fThe hell biome looks");
/* 3764:4047 */       this.lores.add("Â§fdark and dead. Some");
/* 3765:4048 */       this.lores.add("Â§fmobs from the nether will");
/* 3766:4049 */       this.lores.add("Â§fspawn in this biome");
/* 3767:4050 */       this.lores.add("Â§f(excluding ghasts and");
/* 3768:4051 */       this.lores.add("Â§fblazes).");
/* 3769:4052 */       if (checkCurrentBiome(player, "HELL")) {
/* 3770:4054 */         this.lores.add("Â§2Â§lThis is your current biome.");
/* 3771:     */       } else {
/* 3772:4057 */         this.lores.add("Â§eÂ§lClick to change to this biome.");
/* 3773:     */       }
/* 3774:     */     }
/* 3775:     */     else
/* 3776:     */     {
/* 3777:4061 */       meta4.setDisplayName("Â§8Biome: Hell(Nether)");
/* 3778:4062 */       this.lores.add("Â§cYou cannot use this biome.");
/* 3779:4063 */       this.lores.add("Â§7The hell biome looks");
/* 3780:4064 */       this.lores.add("Â§7dark and dead. Some");
/* 3781:4065 */       this.lores.add("Â§7mobs from the nether will");
/* 3782:4066 */       this.lores.add("Â§7spawn in this biome");
/* 3783:4067 */       this.lores.add("Â§7(excluding ghasts and");
/* 3784:4068 */       this.lores.add("Â§7blazes).");
/* 3785:     */     }
/* 3786:4070 */     meta4.setLore(this.lores);
/* 3787:4071 */     this.currentBiomeItem.setItemMeta(meta4);
/* 3788:4072 */     this.GUIbiome.addItem(new ItemStack[] { this.currentBiomeItem });
/* 3789:4073 */     this.lores.clear();
/* 3790:     */     
/* 3791:4075 */     this.currentBiomeItem = new ItemStack(Material.EYE_OF_ENDER, 1);
/* 3792:4076 */     meta4 = this.currentBiomeItem.getItemMeta();
/* 3793:4077 */     if (VaultHandler.checkPerk(player.getName(), "usb.biome.sky", player.getWorld()))
/* 3794:     */     {
/* 3795:4079 */       meta4.setDisplayName("Â§aBiome: Sky(End)");
/* 3796:4080 */       this.lores.add("Â§fThe sky biome gives your");
/* 3797:4081 */       this.lores.add("Â§fisland a special dark sky.");
/* 3798:4082 */       this.lores.add("Â§fOnly endermen will spawn");
/* 3799:4083 */       this.lores.add("Â§fin this biome.");
/* 3800:4084 */       if (checkCurrentBiome(player, "SKY")) {
/* 3801:4086 */         this.lores.add("Â§2Â§lThis is your current biome.");
/* 3802:     */       } else {
/* 3803:4089 */         this.lores.add("Â§eÂ§lClick to change to this biome.");
/* 3804:     */       }
/* 3805:     */     }
/* 3806:     */     else
/* 3807:     */     {
/* 3808:4093 */       meta4.setDisplayName("Â§8Biome: Sky(End)");
/* 3809:4094 */       this.lores.add("Â§cYou cannot use this biome.");
/* 3810:4095 */       this.lores.add("Â§7The sky biome gives your");
/* 3811:4096 */       this.lores.add("Â§7island a special dark sky.");
/* 3812:4097 */       this.lores.add("Â§7Only endermen will spawn");
/* 3813:4098 */       this.lores.add("Â§7in this biome.");
/* 3814:     */     }
/* 3815:4100 */     meta4.setLore(this.lores);
/* 3816:4101 */     this.currentBiomeItem.setItemMeta(meta4);
/* 3817:4102 */     this.GUIbiome.addItem(new ItemStack[] { this.currentBiomeItem });
/* 3818:4103 */     this.lores.clear();
/* 3819:4104 */     return this.GUIbiome;
/* 3820:     */   }
/* 3821:     */   
/* 3822:     */   public Inventory displayChallengeGUI(Player player)
/* 3823:     */   {
/* 3824:4109 */     this.GUIchallenge = Bukkit.createInventory(null, 36, "Â§9Challenge Menu");
/* 3825:4110 */     PlayerInfo pi = (PlayerInfo)getInstance().getActivePlayers().get(player.getName());
/* 3826:4111 */     populateChallengeRank(player, 0, Material.DIRT, 0, pi);
/* 3827:4112 */     populateChallengeRank(player, 1, Material.IRON_BLOCK, 9, pi);
/* 3828:4113 */     populateChallengeRank(player, 2, Material.GOLD_BLOCK, 18, pi);
/* 3829:4114 */     populateChallengeRank(player, 3, Material.DIAMOND_BLOCK, 27, pi);
/* 3830:4115 */     return this.GUIchallenge;
/* 3831:     */   }
/* 3832:     */   
/* 3833:     */   public Inventory displayIslandGUI(Player player)
/* 3834:     */   {
/* 3835:4119 */     this.GUIisland = Bukkit.createInventory(null, 18, "Â§9Island Menu");
/* 3836:4120 */     if (hasIsland(player.getName()))
/* 3837:     */     {
/* 3838:4123 */       this.currentIslandItem = new ItemStack(Material.ENDER_PORTAL, 1);
/* 3839:4124 */       ItemMeta meta4 = this.currentIslandItem.getItemMeta();
/* 3840:4125 */       meta4.setDisplayName("Â§aÂ§lReturn Home");
/* 3841:4126 */       this.lores.add("Â§fReturn to your island's home");
/* 3842:4127 */       this.lores.add("Â§fpoint. You can change your home");
/* 3843:4128 */       this.lores.add("Â§fpoint to any location on your");
/* 3844:4129 */       this.lores.add("Â§fisland using Â§b/island sethome");
/* 3845:4130 */       this.lores.add("Â§eÂ§lClick here to return home.");
/* 3846:4131 */       meta4.setLore(this.lores);
/* 3847:4132 */       this.currentIslandItem.setItemMeta(meta4);
/* 3848:4133 */       this.GUIisland.addItem(new ItemStack[] { this.currentIslandItem });
/* 3849:4134 */       this.lores.clear();
/* 3850:     */       
/* 3851:4136 */       this.currentIslandItem = new ItemStack(Material.DIAMOND_ORE, 1);
/* 3852:4137 */       meta4 = this.currentIslandItem.getItemMeta();
/* 3853:4138 */       meta4.setDisplayName("Â§aÂ§lChallenges");
/* 3854:4139 */       this.lores.add("Â§fView a list of challenges that");
/* 3855:4140 */       this.lores.add("Â§fyou can complete on your island");
/* 3856:4141 */       this.lores.add("Â§fto earn skybucks, items, perks,");
/* 3857:4142 */       this.lores.add("Â§fand titles.");
/* 3858:4143 */       this.lores.add("Â§eÂ§lClick here to view challenges.");
/* 3859:4144 */       meta4.setLore(this.lores);
/* 3860:4145 */       this.currentIslandItem.setItemMeta(meta4);
/* 3861:4146 */       this.GUIisland.addItem(new ItemStack[] { this.currentIslandItem });
/* 3862:4147 */       this.lores.clear();
/* 3863:     */       
/* 3864:4149 */       this.currentIslandItem = new ItemStack(Material.EXP_BOTTLE, 1);
/* 3865:4150 */       meta4 = this.currentIslandItem.getItemMeta();
/* 3866:4151 */       meta4.setDisplayName("Â§aÂ§lIsland Level");
/* 3867:4152 */       this.lores.add("Â§eCurrent Level: Â§a" + showIslandLevel(player));
/* 3868:4153 */       this.lores.add("Â§fGain island levels by expanding");
/* 3869:4154 */       this.lores.add("Â§fyour skyblock and completing");
/* 3870:4155 */       this.lores.add("Â§fcertain challenges. Rarer blocks");
/* 3871:4156 */       this.lores.add("Â§fwill add more to your level.");
/* 3872:4157 */       this.lores.add("Â§eÂ§lClick here to refresh.");
/* 3873:4158 */       this.lores.add("Â§eÂ§l(must be on island)");
/* 3874:4159 */       meta4.setLore(this.lores);
/* 3875:4160 */       this.currentIslandItem.setItemMeta(meta4);
/* 3876:4161 */       this.GUIisland.addItem(new ItemStack[] { this.currentIslandItem });
/* 3877:4162 */       this.lores.clear();
/* 3878:     */       
/* 3879:4164 */       this.currentIslandItem = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
/* 3880:4165 */       SkullMeta meta3 = (SkullMeta)this.currentIslandItem.getItemMeta();
/* 3881:4166 */       meta3.setDisplayName("Â§aÂ§lIsland Group");
/* 3882:4167 */       this.lores.add("Â§eMembers: Â§2" + showCurrentMembers(player) + "/" + showMaxMembers(player));
/* 3883:4168 */       this.lores.add("Â§fView the members of your island");
/* 3884:4169 */       this.lores.add("Â§fgroup and their permissions. If");
/* 3885:4170 */       this.lores.add("Â§fyou are the island leader, you");
/* 3886:4171 */       this.lores.add("Â§fcan change the member permissions.");
/* 3887:4172 */       this.lores.add("Â§eÂ§lClick here to view or change.");
/* 3888:4173 */       meta3.setLore(this.lores);
/* 3889:4174 */       this.currentIslandItem.setItemMeta(meta3);
/* 3890:4175 */       this.GUIisland.addItem(new ItemStack[] { this.currentIslandItem });
/* 3891:4176 */       this.lores.clear();
/* 3892:     */       
/* 3893:4178 */       this.currentIslandItem = new ItemStack(Material.SAPLING, 1, (short)3);
/* 3894:4179 */       meta4 = this.currentIslandItem.getItemMeta();
/* 3895:4180 */       meta4.setDisplayName("Â§aÂ§lChange Island Biome");
/* 3896:4181 */       this.lores.add("Â§eCurrent Biome: Â§b" + getCurrentBiome(player).toUpperCase());
/* 3897:4182 */       this.lores.add("Â§fThe island biome affects things");
/* 3898:4183 */       this.lores.add("Â§flike grass color and spawning");
/* 3899:4184 */       this.lores.add("Â§fof both animals and monsters.");
/* 3900:4185 */       if (checkIslandPermission(player, "canChangeBiome")) {
/* 3901:4186 */         this.lores.add("Â§eÂ§lClick here to change biomes.");
/* 3902:     */       } else {
/* 3903:4188 */         this.lores.add("Â§cÂ§lYou can't change the biome.");
/* 3904:     */       }
/* 3905:4189 */       meta4.setLore(this.lores);
/* 3906:4190 */       this.currentIslandItem.setItemMeta(meta4);
/* 3907:4191 */       this.GUIisland.addItem(new ItemStack[] { this.currentIslandItem });
/* 3908:4192 */       this.lores.clear();
/* 3909:     */       
/* 3910:4194 */       this.currentIslandItem = new ItemStack(Material.IRON_FENCE, 1);
/* 3911:4195 */       meta4 = this.currentIslandItem.getItemMeta();
/* 3912:4196 */       meta4.setDisplayName("Â§aÂ§lIsland Lock");
/* 3913:4197 */       if (getInstance().getIslandConfig(((PlayerInfo)getInstance().getActivePlayers().get(player.getName())).locationForParty()).getBoolean("general.locked"))
/* 3914:     */       {
/* 3915:4199 */         this.lores.add("Â§eLock Status: Â§aActive");
/* 3916:4200 */         this.lores.add("Â§fYour island is currently Â§clocked.");
/* 3917:4201 */         this.lores.add("Â§fPlayers outside of your group");
/* 3918:4202 */         this.lores.add("Â§fare unable to enter your island.");
/* 3919:4203 */         if (checkIslandPermission(player, "canToggleLock")) {
/* 3920:4204 */           this.lores.add("Â§eÂ§lClick here to unlock your island.");
/* 3921:     */         } else {
/* 3922:4206 */           this.lores.add("Â§cÂ§lYou can't change the lock.");
/* 3923:     */         }
/* 3924:     */       }
/* 3925:     */       else
/* 3926:     */       {
/* 3927:4210 */         this.lores.add("Â§eLock Status: Â§8Inactive");
/* 3928:4211 */         this.lores.add("Â§fYour island is currently Â§aunlocked.");
/* 3929:4212 */         this.lores.add("Â§fAll players are able to enter your");
/* 3930:4213 */         this.lores.add("Â§fisland, but only you and your group");
/* 3931:4214 */         this.lores.add("Â§fmembers may build there.");
/* 3932:4215 */         if (checkIslandPermission(player, "canToggleLock")) {
/* 3933:4216 */           this.lores.add("Â§eÂ§lClick here to lock your island.");
/* 3934:     */         } else {
/* 3935:4218 */           this.lores.add("Â§cÂ§lYou can't change the lock.");
/* 3936:     */         }
/* 3937:     */       }
/* 3938:4220 */       meta4.setLore(this.lores);
/* 3939:4221 */       this.currentIslandItem.setItemMeta(meta4);
/* 3940:4222 */       this.GUIisland.addItem(new ItemStack[] { this.currentIslandItem });
/* 3941:4223 */       this.lores.clear();
/* 3942:4226 */       if (getInstance().getIslandConfig(((PlayerInfo)getInstance().getActivePlayers().get(player.getName())).locationForParty()).getBoolean("general.warpActive"))
/* 3943:     */       {
/* 3944:4228 */         this.currentIslandItem = new ItemStack(Material.PORTAL, 1);
/* 3945:4229 */         meta4 = this.currentIslandItem.getItemMeta();
/* 3946:4230 */         meta4.setDisplayName("Â§aÂ§lIsland Warp");
/* 3947:4231 */         this.lores.add("Â§eWarp Status: Â§aActive");
/* 3948:4232 */         this.lores.add("Â§fOther players may warp to your");
/* 3949:4233 */         this.lores.add("Â§fisland at anytime to the point");
/* 3950:4234 */         this.lores.add("Â§fyou set using Â§d/island setwarp.");
/* 3951:4235 */         if ((checkIslandPermission(player, "canToggleWarp")) && (VaultHandler.checkPerk(player.getName(), "usb.extra.addwarp", getSkyBlockWorld()))) {
/* 3952:4236 */           this.lores.add("Â§eÂ§lClick here to deactivate.");
/* 3953:     */         } else {
/* 3954:4238 */           this.lores.add("Â§cÂ§lYou can't change the warp.");
/* 3955:     */         }
/* 3956:     */       }
/* 3957:     */       else
/* 3958:     */       {
/* 3959:4242 */         this.currentIslandItem = new ItemStack(Material.ENDER_STONE, 1);
/* 3960:4243 */         meta4 = this.currentIslandItem.getItemMeta();
/* 3961:4244 */         meta4.setDisplayName("Â§aÂ§lIsland Warp");
/* 3962:4245 */         this.lores.add("Â§eWarp Status: Â§8Inactive");
/* 3963:4246 */         this.lores.add("Â§fOther players can't warp to your");
/* 3964:4247 */         this.lores.add("Â§fisland. Set a warp point using");
/* 3965:4248 */         this.lores.add("Â§d/island setwarp Â§fbefore activating.");
/* 3966:4249 */         if ((checkIslandPermission(player, "canToggleWarp")) && (VaultHandler.checkPerk(player.getName(), "usb.extra.addwarp", getSkyBlockWorld()))) {
/* 3967:4250 */           this.lores.add("Â§eÂ§lClick here to activate.");
/* 3968:     */         } else {
/* 3969:4252 */           this.lores.add("Â§cÂ§lYou can't change the warp.");
/* 3970:     */         }
/* 3971:     */       }
/* 3972:4254 */       meta4.setLore(this.lores);
/* 3973:4255 */       this.currentIslandItem.setItemMeta(meta4);
/* 3974:4256 */       this.GUIisland.addItem(new ItemStack[] { this.currentIslandItem });
/* 3975:4257 */       this.lores.clear();
/* 3976:     */       
/* 3977:4259 */       this.currentIslandItem = new ItemStack(Material.CHEST, 1);
/* 3978:4260 */       meta4 = this.currentIslandItem.getItemMeta();
/* 3979:4261 */       meta4.setDisplayName("Â§aÂ§lBuy Perks");
/* 3980:4262 */       this.lores.add("Â§fVisit the perk shop to buy");
/* 3981:4263 */       this.lores.add("Â§fspecial abilities for your");
/* 3982:4264 */       this.lores.add("Â§fisland and character, as well");
/* 3983:4265 */       this.lores.add("Â§fas titles and more.");
/* 3984:4266 */       this.lores.add("Â§eÂ§lClick here to open the shop!");
/* 3985:4267 */       meta4.setLore(this.lores);
/* 3986:4268 */       this.currentIslandItem.setItemMeta(meta4);
/* 3987:4269 */       this.GUIisland.addItem(new ItemStack[] { this.currentIslandItem });
/* 3988:4270 */       this.lores.clear();
/* 3989:     */       
/* 3990:4272 */       this.currentIslandItem = new ItemStack(Material.ENDER_CHEST, 1);
/* 3991:4273 */       meta4 = this.currentIslandItem.getItemMeta();
/* 3992:4274 */       meta4.setDisplayName("Â§aÂ§lBuy Donor Perks");
/* 3993:4275 */       this.lores.add("Â§fThis special perk shop is");
/* 3994:4276 */       this.lores.add("Â§fonly available to donors!");
/* 3995:4277 */       if (VaultHandler.checkPerk(player.getName(), "group.donor", player.getWorld())) {
/* 3996:4279 */         this.lores.add("Â§eÂ§lClick here to open the shop!");
/* 3997:     */       } else {
/* 3998:4282 */         this.lores.add("Â§aÂ§lClick here to become a donor!");
/* 3999:     */       }
/* 4000:4284 */       meta4.setLore(this.lores);
/* 4001:4285 */       this.currentIslandItem.setItemMeta(meta4);
/* 4002:4286 */       this.GUIisland.setItem(16, this.currentIslandItem);
/* 4003:4287 */       this.lores.clear();
/* 4004:     */       
/* 4005:4289 */       this.currentIslandItem = new ItemStack(Material.BOOK_AND_QUILL, 1);
/* 4006:4290 */       meta4 = this.currentIslandItem.getItemMeta();
/* 4007:4291 */       meta4.setDisplayName("Â§aÂ§lIsland Log");
/* 4008:4292 */       this.lores.add("Â§fView a log of events from");
/* 4009:4293 */       this.lores.add("Â§fyour island such as member,");
/* 4010:4294 */       this.lores.add("Â§fbiome, and warp changes.");
/* 4011:4295 */       this.lores.add("Â§eÂ§lClick to view the log.");
/* 4012:4296 */       meta4.setLore(this.lores);
/* 4013:4297 */       this.currentIslandItem.setItemMeta(meta4);
/* 4014:4298 */       this.GUIisland.addItem(new ItemStack[] { this.currentIslandItem });
/* 4015:4299 */       this.lores.clear();
/* 4016:     */       
/* 4017:4301 */       this.currentIslandItem = new ItemStack(Material.BED, 1);
/* 4018:4302 */       meta4 = this.currentIslandItem.getItemMeta();
/* 4019:4303 */       meta4.setDisplayName("Â§aÂ§lChange Home Location");
/* 4020:4304 */       this.lores.add("Â§fWhen you teleport to your");
/* 4021:4305 */       this.lores.add("Â§fisland you will be taken to");
/* 4022:4306 */       this.lores.add("Â§fthis location.");
/* 4023:4307 */       this.lores.add("Â§eÂ§lClick here to change.");
/* 4024:4308 */       meta4.setLore(this.lores);
/* 4025:4309 */       this.currentIslandItem.setItemMeta(meta4);
/* 4026:4310 */       this.GUIisland.addItem(new ItemStack[] { this.currentIslandItem });
/* 4027:4311 */       this.lores.clear();
/* 4028:     */       
/* 4029:4313 */       this.currentIslandItem = new ItemStack(Material.HOPPER, 1);
/* 4030:4314 */       meta4 = this.currentIslandItem.getItemMeta();
/* 4031:4315 */       meta4.setDisplayName("Â§aÂ§lChange Warp Location");
/* 4032:4316 */       this.lores.add("Â§fWhen your warp is activated,");
/* 4033:4317 */       this.lores.add("Â§fother players will be taken to");
/* 4034:4318 */       this.lores.add("Â§fthis point when they teleport");
/* 4035:4319 */       this.lores.add("Â§fto your island.");
/* 4036:4320 */       this.lores.add("Â§eÂ§lClick here to change.");
/* 4037:4321 */       meta4.setLore(this.lores);
/* 4038:4322 */       this.currentIslandItem.setItemMeta(meta4);
/* 4039:4323 */       this.GUIisland.setItem(15, this.currentIslandItem);
/* 4040:4324 */       this.lores.clear();
/* 4041:     */     }
/* 4042:4327 */     else if (VaultHandler.checkPerk(player.getName(), "group.member", getSkyBlockWorld()))
/* 4043:     */     {
/* 4044:4330 */       this.currentIslandItem = new ItemStack(Material.GRASS, 1);
/* 4045:4331 */       ItemMeta meta4 = this.currentIslandItem.getItemMeta();
/* 4046:4332 */       meta4.setDisplayName("Â§aÂ§lStart an Island");
/* 4047:4333 */       this.lores.add("Â§fStart your skyblock journey");
/* 4048:4334 */       this.lores.add("Â§fby starting your own island.");
/* 4049:4335 */       this.lores.add("Â§fComplete challenges to earn");
/* 4050:4336 */       this.lores.add("Â§fitems and skybucks to help");
/* 4051:4337 */       this.lores.add("Â§fexpand your skyblock. You can");
/* 4052:4338 */       this.lores.add("Â§finvite others to join in");
/* 4053:4339 */       this.lores.add("Â§fbuilding your island empire!");
/* 4054:4340 */       this.lores.add("Â§eÂ§lClick here to start!");
/* 4055:4341 */       meta4.setLore(this.lores);
/* 4056:4342 */       this.currentIslandItem.setItemMeta(meta4);
/* 4057:4343 */       this.GUIisland.addItem(new ItemStack[] { this.currentIslandItem });
/* 4058:4344 */       this.lores.clear();
/* 4059:     */       
/* 4060:4346 */       this.currentIslandItem = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
/* 4061:4347 */       SkullMeta meta3 = (SkullMeta)this.currentIslandItem.getItemMeta();
/* 4062:4348 */       meta3.setDisplayName("Â§aÂ§lJoin an Island");
/* 4063:4349 */       this.lores.add("Â§fWant to join another player's");
/* 4064:4350 */       this.lores.add("Â§fisland instead of starting");
/* 4065:4351 */       this.lores.add("Â§fyour own? If another player");
/* 4066:4352 */       this.lores.add("Â§finvites you to their island");
/* 4067:4353 */       this.lores.add("Â§fyou can click here or use");
/* 4068:4354 */       this.lores.add("Â§e/island accept Â§fto join them.");
/* 4069:4355 */       this.lores.add("Â§eÂ§lClick here to accept an invite!");
/* 4070:4356 */       this.lores.add("Â§eÂ§l(You must be invited first)");
/* 4071:4357 */       meta3.setLore(this.lores);
/* 4072:4358 */       this.currentIslandItem.setItemMeta(meta3);
/* 4073:4359 */       this.GUIisland.setItem(4, this.currentIslandItem);
/* 4074:4360 */       this.lores.clear();
/* 4075:     */       
/* 4076:4362 */       this.currentIslandItem = new ItemStack(Material.SIGN, 1);
/* 4077:4363 */       meta4 = this.currentIslandItem.getItemMeta();
/* 4078:4364 */       meta4.setDisplayName("Â§aÂ§lIsland Help");
/* 4079:4365 */       this.lores.add("Â§fNeed help with skyblock");
/* 4080:4366 */       this.lores.add("Â§fconcepts or commands? View");
/* 4081:4367 */       this.lores.add("Â§fdetails about them here.");
/* 4082:4368 */       this.lores.add("Â§eÂ§lClick here for help!");
/* 4083:4369 */       meta4.setLore(this.lores);
/* 4084:4370 */       this.currentIslandItem.setItemMeta(meta4);
/* 4085:4371 */       this.GUIisland.setItem(8, this.currentIslandItem);
/* 4086:4372 */       this.lores.clear();
/* 4087:     */     }
/* 4088:     */     else
/* 4089:     */     {
/* 4090:4375 */       this.currentIslandItem = new ItemStack(Material.BOOK, 1);
/* 4091:4376 */       ItemMeta meta4 = this.currentIslandItem.getItemMeta();
/* 4092:4377 */       meta4.setDisplayName("Â§aÂ§lWelcome to the Server!");
/* 4093:4378 */       this.lores.add("Â§fPlease read and accept the");
/* 4094:4379 */       this.lores.add("Â§fserver rules to become a");
/* 4095:4380 */       this.lores.add("Â§fmember and start your skyblock.");
/* 4096:4381 */       this.lores.add("Â§eÂ§lClick here to read!");
/* 4097:4382 */       meta4.setLore(this.lores);
/* 4098:4383 */       this.currentIslandItem.setItemMeta(meta4);
/* 4099:4384 */       this.GUIisland.addItem(new ItemStack[] { this.currentIslandItem });
/* 4100:4385 */       this.lores.clear();
/* 4101:     */     }
/* 4102:4388 */     return this.GUIisland;
/* 4103:     */   }
/* 4104:     */   
/* 4105:     */   public boolean isPartyLeader(Player player)
/* 4106:     */   {
/* 4107:4393 */     if (getInstance().getIslandConfig(((PlayerInfo)getInstance().getActivePlayers().get(player.getName())).locationForParty()).getString("party.leader").equalsIgnoreCase(player.getName())) {
/* 4108:4394 */       return true;
/* 4109:     */     }
/* 4110:4395 */     return false;
/* 4111:     */   }
/* 4112:     */   
/* 4113:     */   public boolean checkIslandPermission(Player player, String permission)
/* 4114:     */   {
/* 4115:4400 */     return getInstance().getIslandConfig(((PlayerInfo)getInstance().getActivePlayers().get(player.getName())).locationForParty()).getBoolean("party.members." + player.getName() + "." + permission);
/* 4116:     */   }
/* 4117:     */   
/* 4118:     */   public String getCurrentBiome(Player player)
/* 4119:     */   {
/* 4120:4405 */     return getInstance().getIslandConfig(((PlayerInfo)getInstance().getActivePlayers().get(player.getName())).locationForParty()).getString("general.biome");
/* 4121:     */   }
/* 4122:     */   
/* 4123:     */   public int showIslandLevel(Player player)
/* 4124:     */   {
/* 4125:4410 */     return getInstance().getIslandConfig(((PlayerInfo)getInstance().getActivePlayers().get(player.getName())).locationForParty()).getInt("general.level");
/* 4126:     */   }
/* 4127:     */   
/* 4128:     */   public int showCurrentMembers(Player player)
/* 4129:     */   {
/* 4130:4415 */     return getInstance().getIslandConfig(((PlayerInfo)getInstance().getActivePlayers().get(player.getName())).locationForParty()).getInt("party.currentSize");
/* 4131:     */   }
/* 4132:     */   
/* 4133:     */   public int showMaxMembers(Player player)
/* 4134:     */   {
/* 4135:4420 */     return getInstance().getIslandConfig(((PlayerInfo)getInstance().getActivePlayers().get(player.getName())).locationForParty()).getInt("party.maxSize");
/* 4136:     */   }
/* 4137:     */   
/* 4138:     */   public void populateChallengeRank(Player player, int rankIndex, Material mat, int location, PlayerInfo pi)
/* 4139:     */   {
/* 4140:4426 */     int rankComplete = 0;
/* 4141:4427 */     this.currentChallengeItem = new ItemStack(mat, 1);
/* 4142:4428 */     ItemMeta meta4 = this.currentChallengeItem.getItemMeta();
/* 4143:4429 */     meta4.setDisplayName("Â§eÂ§lRank: " + Settings.challenges_ranks[rankIndex]);
/* 4144:4430 */     this.lores.add("Â§fComplete most challenges in");
/* 4145:4431 */     this.lores.add("Â§fthis rank to unlock the next rank.");
/* 4146:4432 */     meta4.setLore(this.lores);
/* 4147:4433 */     this.currentChallengeItem.setItemMeta(meta4);
/* 4148:4434 */     this.GUIchallenge.setItem(location, this.currentChallengeItem);
/* 4149:4435 */     this.lores.clear();
/* 4150:4436 */     String[] challengeList = getChallengesFromRank(player, Settings.challenges_ranks[rankIndex]).split(" - ");
/* 4151:4437 */     for (int i = 0; i < challengeList.length; i++)
/* 4152:     */     {
/* 4153:4439 */       if (rankIndex > 0)
/* 4154:     */       {
/* 4155:4441 */         rankComplete = getInstance().checkRankCompletion(player, Settings.challenges_ranks[(rankIndex - 1)]);
/* 4156:4442 */         if (rankComplete > 0)
/* 4157:     */         {
/* 4158:4444 */           this.currentChallengeItem = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)14);
/* 4159:4445 */           meta4 = this.currentChallengeItem.getItemMeta();
/* 4160:4446 */           meta4.setDisplayName("Â§4Â§lLocked Challenge");
/* 4161:4447 */           this.lores.add("Â§7Complete " + rankComplete + " more " + Settings.challenges_ranks[(rankIndex - 1)] + " challenges");
/* 4162:4448 */           this.lores.add("Â§7to unlock this rank.");
/* 4163:4449 */           meta4.setLore(this.lores);
/* 4164:4450 */           this.currentChallengeItem.setItemMeta(meta4);
/* 4165:4451 */           this.GUIchallenge.setItem(++location, this.currentChallengeItem);
/* 4166:4452 */           this.lores.clear();
/* 4167:4453 */           continue;
/* 4168:     */         }
/* 4169:     */       }
/* 4170:4456 */       if (challengeList[i].charAt(1) == 'e')
/* 4171:     */       {
/* 4172:4458 */         this.currentChallengeItem = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)4);
/* 4173:4459 */         meta4 = this.currentChallengeItem.getItemMeta();
/* 4174:4460 */         meta4.setDisplayName(challengeList[i].replace("Â§e", "Â§eÂ§l"));
/* 4175:4461 */         challengeList[i] = challengeList[i].replace("Â§e", "");
/* 4176:4462 */         challengeList[i] = challengeList[i].replace("Â§8", "");
/* 4177:     */       }
/* 4178:4464 */       else if (challengeList[i].charAt(1) == 'a')
/* 4179:     */       {
/* 4180:4466 */         if (!getInstance().getConfig().contains("options.challenges.challengeList." + challengeList[i].replace("Â§a", "").replace("Â§2", "").replace("Â§e", "").replace("Â§8", "").toLowerCase() + ".displayItem")) {
/* 4181:4467 */           this.currentChallengeItem = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)5);
/* 4182:     */         } else {
/* 4183:4469 */           this.currentChallengeItem = new ItemStack(Material.getMaterial(getInstance().getConfig().getInt("options.challenges.challengeList." + challengeList[i].replace("Â§a", "").replace("Â§2", "").replace("Â§e", "").replace("Â§8", "").toLowerCase() + ".displayItem")), 1);
/* 4184:     */         }
/* 4185:4470 */         meta4 = this.currentChallengeItem.getItemMeta();
/* 4186:4471 */         meta4.setDisplayName(challengeList[i].replace("Â§a", "Â§aÂ§l"));
/* 4187:4472 */         challengeList[i] = challengeList[i].replace("Â§a", "");
/* 4188:4473 */         challengeList[i] = challengeList[i].replace("Â§8", "");
/* 4189:     */       }
/* 4190:4475 */       else if (challengeList[i].charAt(1) == '2')
/* 4191:     */       {
/* 4192:4477 */         this.currentChallengeItem = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)13);
/* 4193:4478 */         meta4 = this.currentChallengeItem.getItemMeta();
/* 4194:4479 */         meta4.setDisplayName(challengeList[i].replace("Â§2", "Â§2Â§l"));
/* 4195:4480 */         challengeList[i] = challengeList[i].replace("Â§2", "");
/* 4196:4481 */         challengeList[i] = challengeList[i].replace("Â§8", "");
/* 4197:     */       }
/* 4198:     */       else
/* 4199:     */       {
/* 4200:4485 */         this.currentChallengeItem = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)4);
/* 4201:4486 */         meta4 = this.currentChallengeItem.getItemMeta();
/* 4202:4487 */         meta4.setDisplayName(challengeList[i].replace("Â§e", "Â§eÂ§l"));
/* 4203:4488 */         challengeList[i] = challengeList[i].replace("Â§e", "");
/* 4204:4489 */         challengeList[i] = challengeList[i].replace("Â§8", "");
/* 4205:     */       }
/* 4206:4492 */       this.lores.add("Â§7" + getInstance().getConfig().getString(new StringBuilder("options.challenges.challengeList.").append(challengeList[i].toLowerCase()).append(".description").toString()));
/* 4207:4493 */       this.lores.add("Â§eThis challenge requires the following:");
/* 4208:4494 */       String[] reqList = getConfig().getString("options.challenges.challengeList." + challengeList[i].toLowerCase() + ".requiredItems").split(" ");
/* 4209:     */       
/* 4210:     */ 
/* 4211:4497 */       int reqItem = 0;
/* 4212:4498 */       int reqAmount = 0;
/* 4213:4499 */       int reqMod = -1;
/* 4214:4500 */       for (String s : reqList)
/* 4215:     */       {
/* 4216:4502 */         String[] sPart = s.split(":");
/* 4217:4503 */         if (sPart.length == 2)
/* 4218:     */         {
/* 4219:4505 */           reqItem = Integer.parseInt(sPart[0]);
/* 4220:4506 */           String[] sScale = sPart[1].split(";");
/* 4221:4507 */           if (sScale.length == 1) {
/* 4222:4508 */             reqAmount = Integer.parseInt(sPart[1]);
/* 4223:4509 */           } else if (sScale.length == 2) {
/* 4224:4511 */             if (sScale[1].charAt(0) == '+') {
/* 4225:4513 */               reqAmount = Integer.parseInt(sScale[0]) + Integer.parseInt(sScale[1].substring(1)) * ((PlayerInfo)getInstance().getActivePlayers().get(player.getName())).checkChallengeSinceTimer(challengeList[i].toLowerCase());
/* 4226:4514 */             } else if (sScale[1].charAt(0) == '*') {
/* 4227:4516 */               reqAmount = Integer.parseInt(sScale[0]) * (Integer.parseInt(sScale[1].substring(1)) * ((PlayerInfo)getInstance().getActivePlayers().get(player.getName())).checkChallengeSinceTimer(challengeList[i].toLowerCase()));
/* 4228:4517 */             } else if (sScale[1].charAt(0) == '-') {
/* 4229:4519 */               reqAmount = Integer.parseInt(sScale[0]) - Integer.parseInt(sScale[1].substring(1)) * ((PlayerInfo)getInstance().getActivePlayers().get(player.getName())).checkChallengeSinceTimer(challengeList[i].toLowerCase());
/* 4230:4520 */             } else if (sScale[1].charAt(0) == '/') {
/* 4231:4522 */               reqAmount = Integer.parseInt(sScale[0]) / (Integer.parseInt(sScale[1].substring(1)) * ((PlayerInfo)getInstance().getActivePlayers().get(player.getName())).checkChallengeSinceTimer(challengeList[i].toLowerCase()));
/* 4232:     */             }
/* 4233:     */           }
/* 4234:     */         }
/* 4235:4525 */         else if (sPart.length == 3)
/* 4236:     */         {
/* 4237:4527 */           reqItem = Integer.parseInt(sPart[0]);
/* 4238:4528 */           String[] sScale = sPart[2].split(";");
/* 4239:4529 */           if (sScale.length == 1) {
/* 4240:4530 */             reqAmount = Integer.parseInt(sPart[2]);
/* 4241:4531 */           } else if (sScale.length == 2) {
/* 4242:4533 */             if (sScale[1].charAt(0) == '+') {
/* 4243:4535 */               reqAmount = Integer.parseInt(sScale[0]) + Integer.parseInt(sScale[1].substring(1)) * ((PlayerInfo)getInstance().getActivePlayers().get(player.getName())).checkChallengeSinceTimer(challengeList[i].toLowerCase());
/* 4244:4536 */             } else if (sScale[1].charAt(0) == '*') {
/* 4245:4538 */               reqAmount = Integer.parseInt(sScale[0]) * (Integer.parseInt(sScale[1].substring(1)) * ((PlayerInfo)getInstance().getActivePlayers().get(player.getName())).checkChallengeSinceTimer(challengeList[i].toLowerCase()));
/* 4246:4539 */             } else if (sScale[1].charAt(0) == '-') {
/* 4247:4541 */               reqAmount = Integer.parseInt(sScale[0]) - Integer.parseInt(sScale[1].substring(1)) * ((PlayerInfo)getInstance().getActivePlayers().get(player.getName())).checkChallengeSinceTimer(challengeList[i].toLowerCase());
/* 4248:4542 */             } else if (sScale[1].charAt(0) == '/') {
/* 4249:4544 */               reqAmount = Integer.parseInt(sScale[0]) / (Integer.parseInt(sScale[1].substring(1)) * ((PlayerInfo)getInstance().getActivePlayers().get(player.getName())).checkChallengeSinceTimer(challengeList[i].toLowerCase()));
/* 4250:     */             }
/* 4251:     */           }
/* 4252:4547 */           reqMod = Integer.parseInt(sPart[1]);
/* 4253:     */         }
/* 4254:4549 */         ItemStack newItem = new ItemStack(reqItem, reqAmount, (short)reqMod);
/* 4255:4550 */         this.lores.add("Â§f" + newItem.getAmount() + " " + newItem.getType().toString());
/* 4256:     */       }
/* 4257:4553 */       if ((pi.checkChallenge(challengeList[i].toLowerCase()) > 0) && (getInstance().getConfig().getBoolean("options.challenges.challengeList." + challengeList[i].toLowerCase() + ".repeatable")))
/* 4258:     */       {
/* 4259:4556 */         if (pi.onChallengeCooldown(challengeList[i].toLowerCase())) {
/* 4260:4558 */           if (pi.getChallengeCooldownTime(challengeList[i].toLowerCase()) / 86400000L >= 1L)
/* 4261:     */           {
/* 4262:4560 */             int days = (int)pi.getChallengeCooldownTime(challengeList[i].toLowerCase()) / 86400000;
/* 4263:4561 */             this.lores.add("Â§4Requirements will reset in " + days + " days.");
/* 4264:     */           }
/* 4265:     */           else
/* 4266:     */           {
/* 4267:4564 */             int hours = (int)pi.getChallengeCooldownTime(challengeList[i].toLowerCase()) / 3600000;
/* 4268:4565 */             this.lores.add("Â§4Requirements will reset in " + hours + " hours.");
/* 4269:     */           }
/* 4270:     */         }
/* 4271:4568 */         this.lores.add("Â§6Item Reward: Â§a" + getInstance().getConfig().getString(new StringBuilder("options.challenges.challengeList.").append(challengeList[i].toLowerCase()).append(".repeatRewardText").toString()));
/* 4272:4569 */         this.lores.add("Â§6Currency Reward: Â§a" + getInstance().getConfig().getInt(new StringBuilder("options.challenges.challengeList.").append(challengeList[i].toLowerCase()).append(".repeatCurrencyReward").toString()));
/* 4273:4570 */         this.lores.add("Â§6Exp Reward: Â§a" + getInstance().getConfig().getInt(new StringBuilder("options.challenges.challengeList.").append(challengeList[i].toLowerCase()).append(".repeatXpReward").toString()));
/* 4274:4571 */         this.lores.add("Â§dTotal times completed: Â§f" + pi.getChallenge(challengeList[i].toLowerCase()).getTimesCompleted());
/* 4275:4572 */         this.lores.add("Â§eÂ§lClick to complete this challenge.");
/* 4276:     */       }
/* 4277:     */       else
/* 4278:     */       {
/* 4279:4575 */         this.lores.add("Â§6Item Reward: Â§a" + getInstance().getConfig().getString(new StringBuilder("options.challenges.challengeList.").append(challengeList[i].toLowerCase()).append(".rewardText").toString()));
/* 4280:4576 */         this.lores.add("Â§6Currency Reward: Â§a" + getInstance().getConfig().getInt(new StringBuilder("options.challenges.challengeList.").append(challengeList[i].toLowerCase()).append(".currencyReward").toString()));
/* 4281:4577 */         this.lores.add("Â§6Exp Reward: Â§a" + getInstance().getConfig().getInt(new StringBuilder("options.challenges.challengeList.").append(challengeList[i].toLowerCase()).append(".xpReward").toString()));
/* 4282:4578 */         if (getInstance().getConfig().getBoolean("options.challenges.challengeList." + challengeList[i].toLowerCase() + ".repeatable")) {
/* 4283:4579 */           this.lores.add("Â§eÂ§lClick to complete this challenge.");
/* 4284:     */         } else {
/* 4285:4581 */           this.lores.add("Â§4Â§lYou can't repeat this challenge.");
/* 4286:     */         }
/* 4287:     */       }
/* 4288:4583 */       meta4.setLore(this.lores);
/* 4289:4584 */       this.currentChallengeItem.setItemMeta(meta4);
/* 4290:4585 */       this.GUIchallenge.setItem(++location, this.currentChallengeItem);
/* 4291:4586 */       this.lores.clear();
/* 4292:     */     }
/* 4293:     */   }
/* 4294:     */   
/* 4295:     */   public void sendMessageToIslandGroup(String location, String message)
/* 4296:     */   {
/* 4297:4592 */     Iterator<String> temp = getInstance().getIslandConfig(location).getConfigurationSection("party.members").getKeys(false).iterator();
/* 4298:     */     
/* 4299:4594 */     this.date = new Date();
/* 4300:4595 */     String myDateString = DateFormat.getDateInstance(3).format(this.date).toString();
/* 4301:4596 */     String dateTxt = myDateString;
/* 4302:4597 */     int currentLogPos = getInstance().getIslandConfig(location).getInt("log.logPos");
/* 4303:4598 */     while (temp.hasNext())
/* 4304:     */     {
/* 4305:4600 */       String player = (String)temp.next();
/* 4306:4601 */       if (Bukkit.getPlayer(player) != null) {
/* 4307:4603 */         Bukkit.getPlayer(player).sendMessage("Â§d[skyblock] " + message);
/* 4308:     */       }
/* 4309:     */     }
/* 4310:4606 */     getInstance().getIslandConfig(location).set("log." + ++currentLogPos, "Â§d[" + dateTxt + "] " + message);
/* 4311:4607 */     if (currentLogPos < 10) {
/* 4312:4608 */       getInstance().getIslandConfig(location).set("log.logPos", Integer.valueOf(currentLogPos));
/* 4313:     */     } else {
/* 4314:4610 */       getInstance().getIslandConfig(location).set("log.logPos", Integer.valueOf(0));
/* 4315:     */     }
/* 4316:     */   }
/* 4317:     */ }
