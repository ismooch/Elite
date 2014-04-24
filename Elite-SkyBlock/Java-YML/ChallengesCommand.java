/*   1:    */ package us.talabrek.ultimateskyblock;
/*   2:    */ 
/*   3:    */ import java.util.HashMap;
/*   4:    */ import net.milkbowl.vault.economy.Economy;
/*   5:    */ import org.bukkit.ChatColor;
/*   6:    */ import org.bukkit.Server;
/*   7:    */ import org.bukkit.World;
/*   8:    */ import org.bukkit.command.Command;
/*   9:    */ import org.bukkit.command.CommandExecutor;
/*  10:    */ import org.bukkit.command.CommandSender;
/*  11:    */ import org.bukkit.configuration.file.FileConfiguration;
/*  12:    */ import org.bukkit.entity.Player;
/*  13:    */ 
/*  14:    */ public class ChallengesCommand
/*  15:    */   implements CommandExecutor
/*  16:    */ {
/*  17:    */   public boolean onCommand(CommandSender sender, Command command, String label, String[] split)
/*  18:    */   {
/*  19: 17 */     if (!(sender instanceof Player)) {
/*  20: 18 */       return false;
/*  21:    */     }
/*  22: 21 */     Player player = sender.getServer().getPlayer(sender.getName());
/*  23: 22 */     if (!Settings.challenges_allowChallenges) {
/*  24: 24 */       return true;
/*  25:    */     }
/*  26: 26 */     if ((!VaultHandler.checkPerk(player.getName(), "usb.island.challenges", player.getWorld())) && (!player.isOp()))
/*  27:    */     {
/*  28: 27 */       player.sendMessage(ChatColor.RED + "You don't have access to this command!");
/*  29: 28 */       return true;
/*  30:    */     }
/*  31: 30 */     if (!player.getWorld().getName().equalsIgnoreCase(Settings.general_worldName))
/*  32:    */     {
/*  33: 31 */       player.sendMessage(ChatColor.RED + "You can only submit challenges in the skyblock world!");
/*  34: 32 */       return true;
/*  35:    */     }
/*  36: 35 */     if (split.length == 0) {
/*  37: 36 */       player.openInventory(uSkyBlock.getInstance().displayChallengeGUI(player));
/*  38: 52 */     } else if (split.length == 1)
/*  39:    */     {
/*  40: 54 */       if ((split[0].equalsIgnoreCase("help")) || (split[0].equalsIgnoreCase("complete")) || (split[0].equalsIgnoreCase("c")))
/*  41:    */       {
/*  42: 56 */         sender.sendMessage(ChatColor.YELLOW + "Use /c <name> to view information about a challenge.");
/*  43: 57 */         sender.sendMessage(ChatColor.YELLOW + "Use /c complete <name> to attempt to complete that challenge.");
/*  44: 58 */         sender.sendMessage(ChatColor.YELLOW + "Challenges will have different colors depending on if they are:");
/*  45: 59 */         sender.sendMessage(Settings.challenges_challengeColor.replace('&', 'Â§') + "Incomplete " + Settings.challenges_finishedColor.replace('&', 'Â§') + "Completed(not repeatable) " + Settings.challenges_repeatableColor.replace('&', 'Â§') + "Completed(repeatable) ");
/*  46:    */       }
/*  47: 60 */       else if (uSkyBlock.getInstance().isRankAvailable(player, uSkyBlock.getInstance().getConfig().getString("options.challenges.challengeList." + split[0].toLowerCase() + ".rankLevel")))
/*  48:    */       {
/*  49: 62 */         sender.sendMessage(ChatColor.YELLOW + "Challenge Name: " + ChatColor.WHITE + split[0].toLowerCase());
/*  50: 63 */         sender.sendMessage(ChatColor.YELLOW + uSkyBlock.getInstance().getConfig().getString(new StringBuilder("options.challenges.challengeList.").append(split[0].toLowerCase()).append(".description").toString()));
/*  51: 64 */         if (uSkyBlock.getInstance().getConfig().getString("options.challenges.challengeList." + split[0].toLowerCase() + ".type").equalsIgnoreCase("onPlayer"))
/*  52:    */         {
/*  53: 66 */           if (uSkyBlock.getInstance().getConfig().getBoolean("options.challenges.challengeList." + split[0].toLowerCase() + ".takeItems")) {
/*  54: 68 */             sender.sendMessage(ChatColor.RED + "You will lose all required items when you complete this challenge!");
/*  55:    */           }
/*  56:    */         }
/*  57: 70 */         else if (uSkyBlock.getInstance().getConfig().getString("options.challenges.challengeList." + split[0].toLowerCase() + ".type").equalsIgnoreCase("onIsland")) {
/*  58: 72 */           sender.sendMessage(ChatColor.RED + "All required items must be placed on your island!");
/*  59:    */         }
/*  60: 75 */         if (Settings.challenges_ranks.length > 1) {
/*  61: 77 */           sender.sendMessage(ChatColor.YELLOW + "Rank: " + ChatColor.WHITE + uSkyBlock.getInstance().getConfig().getString(new StringBuilder("options.challenges.challengeList.").append(split[0].toLowerCase()).append(".rankLevel").toString()));
/*  62:    */         }
/*  63: 79 */         if ((((PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(player.getName())).checkChallenge(split[0].toLowerCase()) > 0) && ((!uSkyBlock.getInstance().getConfig().getString("options.challenges.challengeList." + split[0].toLowerCase() + ".type").equalsIgnoreCase("onPlayer")) || (!uSkyBlock.getInstance().getConfig().getBoolean("options.challenges.challengeList." + split[0].toLowerCase() + ".repeatable"))))
/*  64:    */         {
/*  65: 81 */           sender.sendMessage(ChatColor.RED + "This Challenge is not repeatable!");
/*  66: 82 */           return true;
/*  67:    */         }
/*  68: 84 */         if ((Settings.challenges_enableEconomyPlugin) && (VaultHandler.econ != null))
/*  69:    */         {
/*  70: 86 */           if (((PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(player.getName())).checkChallenge(split[0].toLowerCase()) > 0)
/*  71:    */           {
/*  72: 88 */             sender.sendMessage(ChatColor.YELLOW + "Repeat reward(s): " + ChatColor.WHITE + uSkyBlock.getInstance().getConfig().getString(new StringBuilder("options.challenges.challengeList.").append(split[0].toLowerCase()).append(".repeatRewardText").toString()).replace('&', 'Â§'));
/*  73: 89 */             player.sendMessage(ChatColor.YELLOW + "Repeat exp reward: " + ChatColor.WHITE + uSkyBlock.getInstance().getConfig().getInt(new StringBuilder("options.challenges.challengeList.").append(split[0].toLowerCase()).append(".repeatXpReward").toString()));
/*  74: 90 */             sender.sendMessage(ChatColor.YELLOW + "Repeat currency reward: " + ChatColor.WHITE + uSkyBlock.getInstance().getConfig().getInt(new StringBuilder("options.challenges.challengeList.").append(split[0].toLowerCase()).append(".repeatCurrencyReward").toString()) + " " + VaultHandler.econ.currencyNamePlural());
/*  75:    */           }
/*  76:    */           else
/*  77:    */           {
/*  78: 93 */             sender.sendMessage(ChatColor.YELLOW + "Reward(s): " + ChatColor.WHITE + uSkyBlock.getInstance().getConfig().getString(new StringBuilder("options.challenges.challengeList.").append(split[0].toLowerCase()).append(".rewardText").toString()).replace('&', 'Â§'));
/*  79: 94 */             player.sendMessage(ChatColor.YELLOW + "Exp reward: " + ChatColor.WHITE + uSkyBlock.getInstance().getConfig().getInt(new StringBuilder("options.challenges.challengeList.").append(split[0].toLowerCase()).append(".xpReward").toString()));
/*  80: 95 */             sender.sendMessage(ChatColor.YELLOW + "Currency reward: " + ChatColor.WHITE + uSkyBlock.getInstance().getConfig().getInt(new StringBuilder("options.challenges.challengeList.").append(split[0].toLowerCase()).append(".currencyReward").toString()) + " " + VaultHandler.econ.currencyNamePlural());
/*  81:    */           }
/*  82:    */         }
/*  83: 99 */         else if (((PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(player.getName())).checkChallenge(split[0].toLowerCase()) > 0)
/*  84:    */         {
/*  85:101 */           sender.sendMessage(ChatColor.YELLOW + "Repeat reward(s): " + ChatColor.WHITE + uSkyBlock.getInstance().getConfig().getString(new StringBuilder("options.challenges.challengeList.").append(split[0].toLowerCase()).append(".repeatRewardText").toString()).replace('&', 'Â§'));
/*  86:102 */           player.sendMessage(ChatColor.YELLOW + "Repeat exp reward: " + ChatColor.WHITE + uSkyBlock.getInstance().getConfig().getInt(new StringBuilder("options.challenges.challengeList.").append(split[0].toLowerCase()).append(".repeatXpReward").toString()));
/*  87:    */         }
/*  88:    */         else
/*  89:    */         {
/*  90:105 */           sender.sendMessage(ChatColor.YELLOW + "Reward(s): " + ChatColor.WHITE + uSkyBlock.getInstance().getConfig().getString(new StringBuilder("options.challenges.challengeList.").append(split[0].toLowerCase()).append(".rewardText").toString()).replace('&', 'Â§'));
/*  91:106 */           player.sendMessage(ChatColor.YELLOW + "Exp reward: " + ChatColor.WHITE + uSkyBlock.getInstance().getConfig().getInt(new StringBuilder("options.challenges.challengeList.").append(split[0].toLowerCase()).append(".xpReward").toString()));
/*  92:    */         }
/*  93:109 */         sender.sendMessage(ChatColor.YELLOW + "To complete this challenge, use " + ChatColor.WHITE + "/c c " + split[0].toLowerCase());
/*  94:    */       }
/*  95:    */       else
/*  96:    */       {
/*  97:112 */         sender.sendMessage(ChatColor.RED + "Invalid challenge name! Use /c help for more information");
/*  98:    */       }
/*  99:    */     }
/* 100:114 */     else if (split.length == 2) {
/* 101:116 */       if ((split[0].equalsIgnoreCase("complete")) || (split[0].equalsIgnoreCase("c"))) {
/* 102:118 */         if (uSkyBlock.getInstance().checkIfCanCompleteChallenge(player, split[1].toLowerCase())) {
/* 103:120 */           uSkyBlock.getInstance().giveReward(player, split[1].toLowerCase());
/* 104:    */         }
/* 105:    */       }
/* 106:    */     }
/* 107:124 */     return true;
/* 108:    */   }
/* 109:    */ }
