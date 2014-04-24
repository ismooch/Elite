/*   1:    */ package us.talabrek.ultimateskyblock;
/*   2:    */ 
/*   3:    */ import java.io.PrintStream;
/*   4:    */ import java.io.Serializable;
/*   5:    */ import org.bukkit.Bukkit;
/*   6:    */ import org.bukkit.Location;
/*   7:    */ import org.bukkit.Server;
/*   8:    */ import org.bukkit.World;
/*   9:    */ import org.bukkit.entity.Player;
/*  10:    */ 
/*  11:    */ public class PlayerInfoOld
/*  12:    */   implements Serializable
/*  13:    */ {
/*  14:    */   private static final long serialVersionUID = 1L;
/*  15:    */   private String playerName;
/*  16:    */   private boolean hasIsland;
/*  17:    */   private boolean hasParty;
/*  18:    */   private String islandLocation;
/*  19:    */   private String homeLocation;
/*  20:    */   private String partyIslandLocation;
/*  21:    */   
/*  22:    */   public PlayerInfoOld(String playerName)
/*  23:    */   {
/*  24: 33 */     this.hasIsland = false;
/*  25: 34 */     this.hasParty = false;
/*  26: 35 */     this.islandLocation = "";
/*  27: 36 */     this.homeLocation = "";
/*  28:    */   }
/*  29:    */   
/*  30:    */   public PlayerInfoOld(String playerName, boolean hasIsland, int iX, int iY, int iZ, int hX, int hY, int hZ)
/*  31:    */   {
/*  32: 40 */     this.playerName = playerName;
/*  33: 41 */     this.hasIsland = hasIsland;
/*  34: 42 */     if ((iX == 0) && (iY == 0) && (iZ == 0)) {
/*  35: 43 */       this.islandLocation = null;
/*  36:    */     } else {
/*  37: 45 */       this.islandLocation = getStringLocation(new Location(uSkyBlock.getSkyBlockWorld(), iX, iY, iZ));
/*  38:    */     }
/*  39: 46 */     if ((hX == 0) && (hY == 0) && (hZ == 0)) {
/*  40: 47 */       this.homeLocation = null;
/*  41:    */     } else {
/*  42: 49 */       this.homeLocation = getStringLocation(new Location(uSkyBlock.getSkyBlockWorld(), hX, hY, hZ));
/*  43:    */     }
/*  44:    */   }
/*  45:    */   
/*  46:    */   public void startNewIsland(Location l)
/*  47:    */   {
/*  48: 64 */     this.hasIsland = true;
/*  49: 65 */     setIslandLocation(l);
/*  50: 66 */     this.hasParty = false;
/*  51: 67 */     this.homeLocation = null;
/*  52:    */   }
/*  53:    */   
/*  54:    */   public void removeFromIsland()
/*  55:    */   {
/*  56: 79 */     this.hasIsland = false;
/*  57: 80 */     setIslandLocation(null);
/*  58: 81 */     this.hasParty = false;
/*  59: 82 */     this.homeLocation = null;
/*  60:    */   }
/*  61:    */   
/*  62:    */   public void setPlayerName(String s)
/*  63:    */   {
/*  64: 93 */     this.playerName = s;
/*  65:    */   }
/*  66:    */   
/*  67:    */   public boolean getHasIsland()
/*  68:    */   {
/*  69: 97 */     return this.hasIsland;
/*  70:    */   }
/*  71:    */   
/*  72:    */   public String locationForParty()
/*  73:    */   {
/*  74:101 */     return getPartyLocationString(this.islandLocation);
/*  75:    */   }
/*  76:    */   
/*  77:    */   public String locationForPartyOld()
/*  78:    */   {
/*  79:105 */     return getPartyLocationString(this.partyIslandLocation);
/*  80:    */   }
/*  81:    */   
/*  82:    */   public Player getPlayer()
/*  83:    */   {
/*  84:109 */     return Bukkit.getPlayer(this.playerName);
/*  85:    */   }
/*  86:    */   
/*  87:    */   public String getPlayerName()
/*  88:    */   {
/*  89:113 */     return this.playerName;
/*  90:    */   }
/*  91:    */   
/*  92:    */   public void setHasIsland(boolean b)
/*  93:    */   {
/*  94:117 */     this.hasIsland = b;
/*  95:    */   }
/*  96:    */   
/*  97:    */   public void setIslandLocation(Location l)
/*  98:    */   {
/*  99:121 */     this.islandLocation = getStringLocation(l);
/* 100:    */   }
/* 101:    */   
/* 102:    */   public Location getIslandLocation()
/* 103:    */   {
/* 104:125 */     return getLocationString(this.islandLocation);
/* 105:    */   }
/* 106:    */   
/* 107:    */   public void setHomeLocation(Location l)
/* 108:    */   {
/* 109:130 */     this.homeLocation = getStringLocation(l);
/* 110:    */   }
/* 111:    */   
/* 112:    */   public Location getHomeLocation()
/* 113:    */   {
/* 114:135 */     return getLocationString(this.homeLocation);
/* 115:    */   }
/* 116:    */   
/* 117:    */   public boolean getHasParty()
/* 118:    */   {
/* 119:140 */     return this.hasParty;
/* 120:    */   }
/* 121:    */   
/* 122:    */   public void setJoinParty(Location l)
/* 123:    */   {
/* 124:145 */     this.hasParty = true;
/* 125:146 */     this.islandLocation = getStringLocation(l);
/* 126:147 */     this.hasIsland = true;
/* 127:    */   }
/* 128:    */   
/* 129:    */   public void setLeaveParty()
/* 130:    */   {
/* 131:152 */     this.hasParty = false;
/* 132:153 */     this.islandLocation = null;
/* 133:154 */     this.hasIsland = false;
/* 134:    */   }
/* 135:    */   
/* 136:    */   private Location getLocationString(String s)
/* 137:    */   {
/* 138:158 */     if ((s == null) || (s.trim() == "")) {
/* 139:159 */       return null;
/* 140:    */     }
/* 141:161 */     String[] parts = s.split(":");
/* 142:162 */     if (parts.length == 4)
/* 143:    */     {
/* 144:163 */       World w = Bukkit.getServer().getWorld(parts[0]);
/* 145:164 */       int x = Integer.parseInt(parts[1]);
/* 146:165 */       int y = Integer.parseInt(parts[2]);
/* 147:166 */       int z = Integer.parseInt(parts[3]);
/* 148:167 */       return new Location(w, x, y, z);
/* 149:    */     }
/* 150:169 */     return null;
/* 151:    */   }
/* 152:    */   
/* 153:    */   private String getPartyLocationString(String s)
/* 154:    */   {
/* 155:173 */     if ((s == null) || (s.trim() == "")) {
/* 156:174 */       return null;
/* 157:    */     }
/* 158:176 */     String[] parts = s.split(":");
/* 159:177 */     if (parts.length == 4) {
/* 160:178 */       return parts[1] + "," + parts[3];
/* 161:    */     }
/* 162:180 */     return null;
/* 163:    */   }
/* 164:    */   
/* 165:    */   public void displayData(String player)
/* 166:    */   {
/* 167:186 */     System.out.print(player + " has an island: " + getHasIsland());
/* 168:187 */     if (getIslandLocation() != null) {
/* 169:188 */       System.out.print(player + " island location: " + getIslandLocation().toString());
/* 170:    */     }
/* 171:189 */     if (getHomeLocation() != null) {
/* 172:190 */       System.out.print(player + " home location: " + getHomeLocation().toString());
/* 173:    */     }
/* 174:    */   }
/* 175:    */   
/* 176:    */   private String getStringLocation(Location l)
/* 177:    */   {
/* 178:194 */     if (l == null) {
/* 179:195 */       return "";
/* 180:    */     }
/* 181:197 */     return l.getWorld().getName() + ":" + l.getBlockX() + ":" + l.getBlockY() + ":" + l.getBlockZ();
/* 182:    */   }
/* 183:    */   
/* 184:    */   public Location getPartyIslandLocation()
/* 185:    */   {
/* 186:201 */     return getLocationString(this.partyIslandLocation);
/* 187:    */   }
/* 188:    */ }
