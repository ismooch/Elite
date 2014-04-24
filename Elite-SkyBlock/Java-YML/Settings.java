/*  1:   */ package us.talabrek.ultimateskyblock;
/*  2:   */ 
/*  3:   */ import java.util.Set;
/*  4:   */ import org.bukkit.Material;
/*  5:   */ import org.bukkit.inventory.ItemStack;
/*  6:   */ 
/*  7:   */ public class Settings
/*  8:   */ {
/*  9:   */   public static int general_maxPartySize;
/* 10:   */   public static String general_worldName;
/* 11:   */   public static int island_distance;
/* 12:17 */   public static int[] blockList = new int[256];
/* 13:18 */   public static int[] limitList = new int[256];
/* 14:19 */   public static int[] diminishingReturnsList = new int[256];
/* 15:   */   public static int island_height;
/* 16:   */   public static int general_spawnSize;
/* 17:   */   public static boolean island_removeCreaturesByTeleport;
/* 18:   */   public static boolean island_protectWithWorldGuard;
/* 19:   */   public static int island_protectionRange;
/* 20:   */   public static String island_allowPvP;
/* 21:   */   public static ItemStack[] island_chestItems;
/* 22:   */   public static boolean island_addExtraItems;
/* 23:   */   public static String[] island_extraPermissions;
/* 24:   */   public static boolean island_useOldIslands;
/* 25:   */   public static boolean island_allowIslandLock;
/* 26:   */   public static boolean island_useIslandLevel;
/* 27:   */   public static boolean island_useTopTen;
/* 28:   */   public static int island_listTime;
/* 29:   */   public static int general_cooldownInfo;
/* 30:   */   public static int general_cooldownRestart;
/* 31:   */   public static int general_biomeChange;
/* 32:   */   public static boolean extras_sendToSpawn;
/* 33:   */   public static boolean extras_obsidianToLava;
/* 34:   */   public static String island_schematicName;
/* 35:   */   public static boolean challenges_broadcastCompletion;
/* 36:   */   public static String challenges_broadcastText;
/* 37:   */   public static String[] challenges_ranks;
/* 38:   */   public static boolean challenges_requirePreviousRank;
/* 39:   */   public static int challenges_rankLeeway;
/* 40:   */   public static String challenges_challengeColor;
/* 41:   */   public static String challenges_finishedColor;
/* 42:   */   public static String challenges_repeatableColor;
/* 43:   */   public static boolean challenges_enableEconomyPlugin;
/* 44:   */   public static boolean challenges_allowChallenges;
/* 45:   */   public static Set<String> challenges_challengeList;
/* 46:51 */   public static Material[] itemList = new Material[2000];
/* 47:   */ }
