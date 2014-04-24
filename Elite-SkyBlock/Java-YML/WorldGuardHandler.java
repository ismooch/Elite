/*   1:    */ package us.talabrek.ultimateskyblock;
/*   2:    */ 
/*   3:    */ import com.sk89q.worldedit.BlockVector;
/*   4:    */ import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
/*   5:    */ import com.sk89q.worldguard.domains.DefaultDomain;
/*   6:    */ import com.sk89q.worldguard.protection.ApplicableRegionSet;
/*   7:    */ import com.sk89q.worldguard.protection.flags.DefaultFlag;
/*   8:    */ import com.sk89q.worldguard.protection.flags.StateFlag;
/*   9:    */ import com.sk89q.worldguard.protection.flags.StringFlag;
/*  10:    */ import com.sk89q.worldguard.protection.managers.RegionManager;
/*  11:    */ import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
/*  12:    */ import com.sk89q.worldguard.protection.regions.ProtectedRegion;
/*  13:    */ import java.io.PrintStream;
/*  14:    */ import org.bukkit.Bukkit;
/*  15:    */ import org.bukkit.ChatColor;
/*  16:    */ import org.bukkit.Location;
/*  17:    */ import org.bukkit.Server;
/*  18:    */ import org.bukkit.command.CommandSender;
/*  19:    */ import org.bukkit.entity.Player;
/*  20:    */ import org.bukkit.plugin.Plugin;
/*  21:    */ import org.bukkit.plugin.PluginManager;
/*  22:    */ 
/*  23:    */ public class WorldGuardHandler
/*  24:    */ {
/*  25:    */   public static WorldGuardPlugin getWorldGuard()
/*  26:    */   {
/*  27: 29 */     Plugin plugin = uSkyBlock.getInstance().getServer().getPluginManager().getPlugin("WorldGuard");
/*  28: 32 */     if ((plugin == null) || (!(plugin instanceof WorldGuardPlugin))) {
/*  29: 33 */       return null;
/*  30:    */     }
/*  31: 36 */     return (WorldGuardPlugin)plugin;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public static void protectIsland(Player sender, String player, PlayerInfo pi)
/*  35:    */   {
/*  36:    */     try
/*  37:    */     {
/*  38: 43 */       if (Settings.island_protectWithWorldGuard) {
/*  39: 45 */         if ((pi.getIslandLocation() != null) && (!getWorldGuard().getRegionManager(uSkyBlock.getSkyBlockWorld()).hasRegion(player + "Island")))
/*  40:    */         {
/*  41: 47 */           ProtectedRegion region = null;
/*  42: 48 */           DefaultDomain owners = new DefaultDomain();
/*  43: 49 */           region = new ProtectedCuboidRegion(player + "Island", getProtectionVectorLeft(pi.getIslandLocation()), getProtectionVectorRight(pi.getIslandLocation()));
/*  44: 50 */           owners.addPlayer(player);
/*  45: 51 */           region.setOwners(owners);
/*  46: 52 */           region.setParent(getWorldGuard().getRegionManager(uSkyBlock.getSkyBlockWorld()).getRegion("__Global__"));
/*  47: 53 */           region.setPriority(100);
/*  48: 54 */           region.setFlag(DefaultFlag.GREET_MESSAGE, DefaultFlag.GREET_MESSAGE.parseInput(getWorldGuard(), sender, "Â§d** You are entering a protected island area. (" + player + ")"));
/*  49: 55 */           region.setFlag(DefaultFlag.FAREWELL_MESSAGE, DefaultFlag.FAREWELL_MESSAGE.parseInput(getWorldGuard(), sender, "Â§d** You are leaving a protected island area. (" + player + ")"));
/*  50: 56 */           region.setFlag(DefaultFlag.PVP, DefaultFlag.PVP.parseInput(getWorldGuard(), sender, Settings.island_allowPvP));
/*  51: 57 */           region.setFlag(DefaultFlag.CHEST_ACCESS, DefaultFlag.CHEST_ACCESS.parseInput(getWorldGuard(), sender, "deny"));
/*  52: 58 */           region.setFlag(DefaultFlag.USE, DefaultFlag.USE.parseInput(getWorldGuard(), sender, "deny"));
/*  53: 59 */           region.setFlag(DefaultFlag.DESTROY_VEHICLE, DefaultFlag.DESTROY_VEHICLE.parseInput(getWorldGuard(), sender, "deny"));
/*  54: 60 */           region.setFlag(DefaultFlag.ENTITY_ITEM_FRAME_DESTROY, DefaultFlag.ENTITY_ITEM_FRAME_DESTROY.parseInput(getWorldGuard(), sender, "deny"));
/*  55: 61 */           region.setFlag(DefaultFlag.ENTITY_PAINTING_DESTROY, DefaultFlag.ENTITY_PAINTING_DESTROY.parseInput(getWorldGuard(), sender, "deny"));
/*  56: 62 */           ApplicableRegionSet set = getWorldGuard().getRegionManager(uSkyBlock.getSkyBlockWorld()).getApplicableRegions(pi.getIslandLocation());
/*  57: 63 */           if (set.size() > 0) {
/*  58: 65 */             for (ProtectedRegion regions : set) {
/*  59: 66 */               if (!regions.getId().equalsIgnoreCase("__global__")) {
/*  60: 67 */                 getWorldGuard().getRegionManager(uSkyBlock.getSkyBlockWorld()).removeRegion(regions.getId());
/*  61:    */               }
/*  62:    */             }
/*  63:    */           }
/*  64: 70 */           getWorldGuard().getRegionManager(uSkyBlock.getSkyBlockWorld()).addRegion(region);
/*  65: 71 */           System.out.print("New protected region created for " + player + "'s Island by " + sender.getName());
/*  66: 72 */           getWorldGuard().getRegionManager(uSkyBlock.getSkyBlockWorld()).save();
/*  67:    */         }
/*  68:    */         else
/*  69:    */         {
/*  70: 75 */           sender.sendMessage("Player doesn't have an island or it's already protected!");
/*  71:    */         }
/*  72:    */       }
/*  73:    */     }
/*  74:    */     catch (Exception ex)
/*  75:    */     {
/*  76: 79 */       System.out.print("ERROR: Failed to protect " + player + "'s Island (" + sender.getName() + ")");
/*  77: 80 */       ex.printStackTrace();
/*  78:    */     }
/*  79:    */   }
/*  80:    */   
/*  81:    */   public static void islandLock(CommandSender sender, String player)
/*  82:    */   {
/*  83:    */     try
/*  84:    */     {
/*  85:208 */       if (getWorldGuard().getRegionManager(uSkyBlock.getSkyBlockWorld()).hasRegion(player + "Island"))
/*  86:    */       {
/*  87:210 */         getWorldGuard().getRegionManager(uSkyBlock.getSkyBlockWorld()).getRegion(player + "Island").setFlag(DefaultFlag.ENTRY, DefaultFlag.ENTRY.parseInput(getWorldGuard(), sender, "deny"));
/*  88:211 */         sender.sendMessage(ChatColor.YELLOW + "Your island is now locked. Only your party members may enter.");
/*  89:212 */         getWorldGuard().getRegionManager(uSkyBlock.getSkyBlockWorld()).save();
/*  90:    */       }
/*  91:    */       else
/*  92:    */       {
/*  93:215 */         sender.sendMessage(ChatColor.RED + "You must be the party leader to lock your island!");
/*  94:    */       }
/*  95:    */     }
/*  96:    */     catch (Exception ex)
/*  97:    */     {
/*  98:218 */       System.out.print("ERROR: Failed to lock " + player + "'s Island (" + sender.getName() + ")");
/*  99:219 */       ex.printStackTrace();
/* 100:    */     }
/* 101:    */   }
/* 102:    */   
/* 103:    */   public static void islandUnlock(CommandSender sender, String player)
/* 104:    */   {
/* 105:    */     try
/* 106:    */     {
/* 107:227 */       if (getWorldGuard().getRegionManager(uSkyBlock.getSkyBlockWorld()).hasRegion(player + "Island"))
/* 108:    */       {
/* 109:229 */         getWorldGuard().getRegionManager(uSkyBlock.getSkyBlockWorld()).getRegion(player + "Island").setFlag(DefaultFlag.ENTRY, DefaultFlag.ENTRY.parseInput(getWorldGuard(), sender, "allow"));
/* 110:230 */         sender.sendMessage(ChatColor.YELLOW + "Your island is unlocked and anyone may enter, however only you and your party members may build or remove blocks.");
/* 111:231 */         getWorldGuard().getRegionManager(uSkyBlock.getSkyBlockWorld()).save();
/* 112:    */       }
/* 113:    */       else
/* 114:    */       {
/* 115:234 */         sender.sendMessage(ChatColor.RED + "You must be the party leader to unlock your island!");
/* 116:    */       }
/* 117:    */     }
/* 118:    */     catch (Exception ex)
/* 119:    */     {
/* 120:237 */       System.out.print("ERROR: Failed to unlock " + player + "'s Island (" + sender.getName() + ")");
/* 121:238 */       ex.printStackTrace();
/* 122:    */     }
/* 123:    */   }
/* 124:    */   
/* 125:    */   public static BlockVector getProtectionVectorLeft(Location island)
/* 126:    */   {
/* 127:244 */     return new BlockVector(island.getX() + Settings.island_protectionRange / 2, 255.0D, island.getZ() + Settings.island_protectionRange / 2);
/* 128:    */   }
/* 129:    */   
/* 130:    */   public static BlockVector getProtectionVectorRight(Location island)
/* 131:    */   {
/* 132:249 */     return new BlockVector(island.getX() - Settings.island_protectionRange / 2, 0.0D, island.getZ() - Settings.island_protectionRange / 2);
/* 133:    */   }
/* 134:    */   
/* 135:    */   public static void removePlayerFromRegion(String owner, String player)
/* 136:    */   {
/* 137:254 */     if (getWorldGuard().getRegionManager(uSkyBlock.getSkyBlockWorld()).hasRegion(owner + "Island"))
/* 138:    */     {
/* 139:256 */       DefaultDomain owners = getWorldGuard().getRegionManager(uSkyBlock.getSkyBlockWorld()).getRegion(owner + "Island").getOwners();
/* 140:257 */       owners.removePlayer(player);
/* 141:258 */       getWorldGuard().getRegionManager(uSkyBlock.getSkyBlockWorld()).getRegion(owner + "Island").setOwners(owners);
/* 142:    */     }
/* 143:    */   }
/* 144:    */   
/* 145:    */   public static void addPlayerToOldRegion(String owner, String player)
/* 146:    */   {
/* 147:264 */     if (getWorldGuard().getRegionManager(uSkyBlock.getSkyBlockWorld()).hasRegion(owner + "Island"))
/* 148:    */     {
/* 149:266 */       DefaultDomain owners = getWorldGuard().getRegionManager(uSkyBlock.getSkyBlockWorld()).getRegion(owner + "Island").getOwners();
/* 150:267 */       owners.addPlayer(player);
/* 151:268 */       getWorldGuard().getRegionManager(uSkyBlock.getSkyBlockWorld()).getRegion(owner + "Island").setOwners(owners);
/* 152:    */     }
/* 153:    */   }
/* 154:    */   
/* 155:    */   public static void resetPlayerRegion(String owner)
/* 156:    */   {
/* 157:274 */     if (getWorldGuard().getRegionManager(uSkyBlock.getSkyBlockWorld()).hasRegion(owner + "Island"))
/* 158:    */     {
/* 159:276 */       DefaultDomain owners = new DefaultDomain();
/* 160:277 */       owners.addPlayer(owner);
/* 161:    */       
/* 162:279 */       getWorldGuard().getRegionManager(uSkyBlock.getSkyBlockWorld()).getRegion(owner + "Island").setOwners(owners);
/* 163:    */     }
/* 164:    */   }
/* 165:    */   
/* 166:    */   public static void transferRegion(String owner, String player, CommandSender sender)
/* 167:    */   {
/* 168:    */     try
/* 169:    */     {
/* 170:287 */       ProtectedRegion region2 = null;
/* 171:288 */       region2 = new ProtectedCuboidRegion(player + "Island", getWorldGuard().getRegionManager(Bukkit.getWorld("skyworld")).getRegion(owner + "Island").getMinimumPoint(), getWorldGuard().getRegionManager(Bukkit.getWorld(Settings.general_worldName)).getRegion(owner + "Island").getMaximumPoint());
/* 172:289 */       region2.setOwners(getWorldGuard().getRegionManager(uSkyBlock.getSkyBlockWorld()).getRegion(owner + "Island").getOwners());
/* 173:290 */       region2.setParent(getWorldGuard().getRegionManager(uSkyBlock.getSkyBlockWorld()).getRegion("__Global__"));
/* 174:291 */       region2.setFlag(DefaultFlag.GREET_MESSAGE, DefaultFlag.GREET_MESSAGE.parseInput(getWorldGuard(), sender, "Â§d** You are entering a protected island area. (" + player + ")"));
/* 175:292 */       region2.setFlag(DefaultFlag.FAREWELL_MESSAGE, DefaultFlag.FAREWELL_MESSAGE.parseInput(getWorldGuard(), sender, "Â§d** You are leaving a protected island area. (" + player + ")"));
/* 176:293 */       region2.setFlag(DefaultFlag.PVP, DefaultFlag.PVP.parseInput(getWorldGuard(), sender, "deny"));
/* 177:294 */       region2.setFlag(DefaultFlag.DESTROY_VEHICLE, DefaultFlag.DESTROY_VEHICLE.parseInput(getWorldGuard(), sender, "deny"));
/* 178:295 */       region2.setFlag(DefaultFlag.ENTITY_ITEM_FRAME_DESTROY, DefaultFlag.ENTITY_ITEM_FRAME_DESTROY.parseInput(getWorldGuard(), sender, "deny"));
/* 179:296 */       region2.setFlag(DefaultFlag.ENTITY_PAINTING_DESTROY, DefaultFlag.ENTITY_PAINTING_DESTROY.parseInput(getWorldGuard(), sender, "deny"));
/* 180:297 */       getWorldGuard().getRegionManager(uSkyBlock.getSkyBlockWorld()).removeRegion(owner + "Island");
/* 181:298 */       getWorldGuard().getRegionManager(uSkyBlock.getSkyBlockWorld()).addRegion(region2);
/* 182:    */     }
/* 183:    */     catch (Exception e)
/* 184:    */     {
/* 185:302 */       System.out.println("Error transferring WorldGuard Protected Region from (" + owner + ") to (" + player + ")");
/* 186:    */     }
/* 187:    */   }
/* 188:    */ }
