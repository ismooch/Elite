/*   1:    */ package us.talabrek.ultimateskyblock;
/*   2:    */ 
/*   3:    */ import org.bukkit.ChatColor;
/*   4:    */ import org.bukkit.World;
/*   5:    */ import org.bukkit.block.Block;
/*   6:    */ import org.bukkit.entity.Hanging;
/*   7:    */ import org.bukkit.entity.Player;
/*   8:    */ import org.bukkit.entity.Vehicle;
/*   9:    */ import org.bukkit.event.EventHandler;
/*  10:    */ import org.bukkit.event.EventPriority;
/*  11:    */ import org.bukkit.event.Listener;
/*  12:    */ import org.bukkit.event.block.BlockBreakEvent;
/*  13:    */ import org.bukkit.event.block.BlockPlaceEvent;
/*  14:    */ import org.bukkit.event.entity.EntityDamageByEntityEvent;
/*  15:    */ import org.bukkit.event.hanging.HangingBreakByEntityEvent;
/*  16:    */ import org.bukkit.event.player.PlayerBedEnterEvent;
/*  17:    */ import org.bukkit.event.player.PlayerBucketEmptyEvent;
/*  18:    */ import org.bukkit.event.player.PlayerBucketFillEvent;
/*  19:    */ import org.bukkit.event.player.PlayerInteractEntityEvent;
/*  20:    */ import org.bukkit.event.player.PlayerInteractEvent;
/*  21:    */ import org.bukkit.event.player.PlayerShearEntityEvent;
/*  22:    */ import org.bukkit.event.vehicle.VehicleDamageEvent;
/*  23:    */ 
/*  24:    */ public class ProtectionEvents
/*  25:    */   implements Listener
/*  26:    */ {
/*  27: 23 */   private Player attacker = null;
/*  28: 24 */   private Player breaker = null;
/*  29:    */   
/*  30:    */   @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
/*  31:    */   public void onPlayerBlockBreak(BlockBreakEvent event)
/*  32:    */   {
/*  33: 36 */     if (event.getPlayer().getWorld().getName().equalsIgnoreCase(Settings.general_worldName)) {
/*  34: 38 */       if ((!uSkyBlock.getInstance().locationIsOnIsland(event.getPlayer(), event.getBlock().getLocation())) && 
/*  35: 39 */         (!VaultHandler.checkPerk(event.getPlayer().getName(), "usb.mod.bypassprotection", event.getPlayer().getWorld())) && (!event.getPlayer().isOp())) {
/*  36: 41 */         event.setCancelled(true);
/*  37:    */       }
/*  38:    */     }
/*  39:    */   }
/*  40:    */   
/*  41:    */   @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
/*  42:    */   public void onPlayerBlockPlace(BlockPlaceEvent event)
/*  43:    */   {
/*  44: 49 */     if (event.getPlayer().getWorld().getName().equalsIgnoreCase(Settings.general_worldName)) {
/*  45: 51 */       if ((!uSkyBlock.getInstance().locationIsOnIsland(event.getPlayer(), event.getBlock().getLocation())) && 
/*  46: 52 */         (!VaultHandler.checkPerk(event.getPlayer().getName(), "usb.mod.bypassprotection", event.getPlayer().getWorld())) && (!event.getPlayer().isOp())) {
/*  47: 54 */         event.setCancelled(true);
/*  48:    */       }
/*  49:    */     }
/*  50:    */   }
/*  51:    */   
/*  52:    */   @EventHandler(ignoreCancelled=true, priority=EventPriority.NORMAL)
/*  53:    */   public void onPlayerInteract(PlayerInteractEvent event)
/*  54:    */   {
/*  55: 61 */     if (event.getPlayer().getWorld().getName().equalsIgnoreCase(Settings.general_worldName)) {
/*  56: 63 */       if ((!uSkyBlock.getInstance().playerIsOnIsland(event.getPlayer())) && (!uSkyBlock.getInstance().playerIsInSpawn(event.getPlayer())) && 
/*  57: 64 */         (!VaultHandler.checkPerk(event.getPlayer().getName(), "usb.mod.bypassprotection", event.getPlayer().getWorld())) && (!event.getPlayer().isOp())) {
/*  58: 66 */         event.setCancelled(true);
/*  59:    */       }
/*  60:    */     }
/*  61:    */   }
/*  62:    */   
/*  63:    */   @EventHandler(ignoreCancelled=true, priority=EventPriority.NORMAL)
/*  64:    */   public void onPlayerBedEnter(PlayerBedEnterEvent event)
/*  65:    */   {
/*  66: 73 */     if (event.getPlayer().getWorld().getName().equalsIgnoreCase(Settings.general_worldName)) {
/*  67: 75 */       if ((!uSkyBlock.getInstance().playerIsOnIsland(event.getPlayer())) && 
/*  68: 76 */         (!VaultHandler.checkPerk(event.getPlayer().getName(), "usb.mod.bypassprotection", event.getPlayer().getWorld())) && (!event.getPlayer().isOp())) {
/*  69: 78 */         event.setCancelled(true);
/*  70:    */       }
/*  71:    */     }
/*  72:    */   }
/*  73:    */   
/*  74:    */   @EventHandler(ignoreCancelled=true, priority=EventPriority.NORMAL)
/*  75:    */   public void onPlayerShearEntity(PlayerShearEntityEvent event)
/*  76:    */   {
/*  77: 86 */     if (event.getPlayer().getWorld().getName().equalsIgnoreCase(Settings.general_worldName)) {
/*  78: 88 */       if ((!uSkyBlock.getInstance().playerIsOnIsland(event.getPlayer())) && 
/*  79: 89 */         (!VaultHandler.checkPerk(event.getPlayer().getName(), "usb.mod.bypassprotection", event.getPlayer().getWorld())) && (!event.getPlayer().isOp()))
/*  80:    */       {
/*  81: 91 */         event.getPlayer().sendMessage(ChatColor.RED + "You can only do that on your island!");
/*  82: 92 */         event.setCancelled(true);
/*  83:    */       }
/*  84:    */     }
/*  85:    */   }
/*  86:    */   
/*  87:    */   @EventHandler(ignoreCancelled=true, priority=EventPriority.NORMAL)
/*  88:    */   public void onPlayerInteractEntity(PlayerInteractEntityEvent event)
/*  89:    */   {
/*  90: 99 */     if (event.getPlayer().getWorld().getName().equalsIgnoreCase(Settings.general_worldName)) {
/*  91:101 */       if ((!uSkyBlock.getInstance().playerIsOnIsland(event.getPlayer())) && 
/*  92:102 */         (!VaultHandler.checkPerk(event.getPlayer().getName(), "usb.mod.bypassprotection", event.getPlayer().getWorld())) && (!event.getPlayer().isOp()))
/*  93:    */       {
/*  94:104 */         event.getPlayer().sendMessage(ChatColor.RED + "You can only do that on your island!");
/*  95:105 */         event.setCancelled(true);
/*  96:    */       }
/*  97:    */     }
/*  98:    */   }
/*  99:    */   
/* 100:    */   @EventHandler(ignoreCancelled=true, priority=EventPriority.NORMAL)
/* 101:    */   public void onPlayerBucketFill(PlayerBucketFillEvent event)
/* 102:    */   {
/* 103:112 */     if (event.getPlayer().getWorld().getName().equalsIgnoreCase(Settings.general_worldName)) {
/* 104:114 */       if ((!uSkyBlock.getInstance().locationIsOnIsland(event.getPlayer(), event.getBlockClicked().getLocation())) && 
/* 105:115 */         (!VaultHandler.checkPerk(event.getPlayer().getName(), "usb.mod.bypassprotection", event.getPlayer().getWorld())) && (!event.getPlayer().isOp()))
/* 106:    */       {
/* 107:117 */         event.getPlayer().sendMessage(ChatColor.RED + "You can only do that on your island!");
/* 108:118 */         event.setCancelled(true);
/* 109:    */       }
/* 110:    */     }
/* 111:    */   }
/* 112:    */   
/* 113:    */   @EventHandler(ignoreCancelled=true, priority=EventPriority.NORMAL)
/* 114:    */   public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event)
/* 115:    */   {
/* 116:125 */     if (event.getPlayer().getWorld().getName().equalsIgnoreCase(Settings.general_worldName)) {
/* 117:127 */       if ((!uSkyBlock.getInstance().locationIsOnIsland(event.getPlayer(), event.getBlockClicked().getLocation())) && 
/* 118:128 */         (!VaultHandler.checkPerk(event.getPlayer().getName(), "usb.mod.bypassprotection", event.getPlayer().getWorld())) && (!event.getPlayer().isOp()))
/* 119:    */       {
/* 120:130 */         event.getPlayer().sendMessage(ChatColor.RED + "You can only do that on your island!");
/* 121:131 */         event.setCancelled(true);
/* 122:    */       }
/* 123:    */     }
/* 124:    */   }
/* 125:    */   
/* 126:    */   @EventHandler(ignoreCancelled=true, priority=EventPriority.NORMAL)
/* 127:    */   public void onPlayerBreakHanging(HangingBreakByEntityEvent event)
/* 128:    */   {
/* 129:138 */     if ((event.getRemover() instanceof Player))
/* 130:    */     {
/* 131:140 */       this.breaker = ((Player)event.getRemover());
/* 132:141 */       if (this.breaker.getWorld().getName().equalsIgnoreCase(Settings.general_worldName)) {
/* 133:143 */         if ((!uSkyBlock.getInstance().locationIsOnIsland(this.breaker, event.getEntity().getLocation())) && 
/* 134:144 */           (!VaultHandler.checkPerk(this.breaker.getName(), "usb.mod.bypassprotection", this.breaker.getWorld())) && (!this.breaker.isOp())) {
/* 135:146 */           event.setCancelled(true);
/* 136:    */         }
/* 137:    */       }
/* 138:    */     }
/* 139:    */   }
/* 140:    */   
/* 141:    */   @EventHandler(ignoreCancelled=true, priority=EventPriority.NORMAL)
/* 142:    */   public void onPlayerVehicleDamage(VehicleDamageEvent event)
/* 143:    */   {
/* 144:154 */     if ((event.getAttacker() instanceof Player))
/* 145:    */     {
/* 146:156 */       this.breaker = ((Player)event.getAttacker());
/* 147:157 */       if (this.breaker.getWorld().getName().equalsIgnoreCase(Settings.general_worldName)) {
/* 148:159 */         if ((!uSkyBlock.getInstance().locationIsOnIsland(this.breaker, event.getVehicle().getLocation())) && 
/* 149:160 */           (!VaultHandler.checkPerk(this.breaker.getName(), "usb.mod.bypassprotection", this.breaker.getWorld())) && (!this.breaker.isOp())) {
/* 150:162 */           event.setCancelled(true);
/* 151:    */         }
/* 152:    */       }
/* 153:    */     }
/* 154:    */   }
/* 155:    */   
/* 156:    */   @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
/* 157:    */   public void onPlayerAttack(EntityDamageByEntityEvent event)
/* 158:    */   {
/* 159:172 */     if ((event.getDamager() instanceof Player))
/* 160:    */     {
/* 161:173 */       this.attacker = ((Player)event.getDamager());
/* 162:174 */       if (this.attacker.getWorld().getName().equalsIgnoreCase(Settings.general_worldName)) {
/* 163:176 */         if (!(event.getEntity() instanceof Player)) {
/* 164:181 */           if ((!uSkyBlock.getInstance().playerIsOnIsland(this.attacker)) && 
/* 165:182 */             (!VaultHandler.checkPerk(this.attacker.getName(), "usb.mod.bypassprotection", this.attacker.getWorld())) && (!this.attacker.isOp())) {
/* 166:184 */             event.setCancelled(true);
/* 167:    */           }
/* 168:    */         }
/* 169:    */       }
/* 170:    */     }
/* 171:    */   }
/* 172:    */ }
