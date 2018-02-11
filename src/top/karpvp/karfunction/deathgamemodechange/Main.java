package top.karpvp.karfunction.deathgamemodechange;

import org.bukkit.Bukkit;
import static org.bukkit.Bukkit.getServer;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Main extends JavaPlugin implements Listener{

    @Override
    public void onEnable() 
    {
        getLogger().info("成功载入插件DeathGameModeChange,请修改配置文件后重载此插件");
//        CommandListener cmd = new CommandListener();
        Bukkit.getPluginManager().registerEvents(this, this);
        saveDefaultConfig();
        reloadConfig();
    }
    
    @EventHandler
    public void onPlayerJoin (PlayerJoinEvent event){
        Player p = event.getPlayer();
        Location lc=p.getWorld().getSpawnLocation();
        p.teleport(lc);
        if (getConfig().getInt("GameModeChange")==2)
            p.setGameMode(GameMode.ADVENTURE);
        else if (getConfig().getInt("GameModeChange")==0)
            p.setGameMode(GameMode.SURVIVAL);
        else 
            p.setGameMode(GameMode.ADVENTURE);
    }
    
    @EventHandler
    public void onPlayerDeath (PlayerDeathEvent event){
            Player p = event.getEntity();
            p.setHealth(20.0);
            p.setFoodLevel(100);
            p.setGameMode(GameMode.SPECTATOR);
            new BukkitRunnable()
            {
                int t = getConfig().getInt("RespawnPeriod");
                @Override
                public void run() 
                {
                    if (t != getConfig().getInt("RespawnPeriod")&&t!=0){
                        TitleAPI.sendTitle(p, Integer.valueOf(0), Integer.valueOf(20), Integer.valueOf(0), "&4&l你死了", "&f你将在&l"+t+"&f秒后重生");
                        t--;
                    }else if(t == getConfig().getInt("RespawnPeriod")){
                        if (p.getKiller()!=null)
//                            p.sendTitle("§7你被§l§4"+p.getKiller().getName()+"§7杀死了",t+"秒后重生");
                            TitleAPI.sendTitle(p, Integer.valueOf(10), Integer.valueOf(10), Integer.valueOf(0), "&4&l你死了", "§7你被§l§4"+p.getKiller().getName()+"§7杀死了");
                        else
//                            p.sendTitle("§7你死了","");
                            TitleAPI.sendTitle(p, Integer.valueOf(10), Integer.valueOf(10), Integer.valueOf(0), "&4&l你死了", "");
                        t--;
                    }else if(t == 0){
                        if (getConfig().getInt("GameModeChange")==2)
                            p.setGameMode(GameMode.ADVENTURE);
                        else if (getConfig().getInt("GameModeChange")==0)
                            p.setGameMode(GameMode.SURVIVAL);
                        else 
                            p.setGameMode(GameMode.ADVENTURE);
                        Location lc = p.getWorld().getSpawnLocation();
                        p.teleport(lc);
//                        p.sendTitle("已重生", "");
                        TitleAPI.sendTitle(p, Integer.valueOf(0), Integer.valueOf(20), Integer.valueOf(10), "&9&l已重生", "");
//                        TitleActionBarAPI.sendFullTitle(evt.getPlayer(), 20, 30, 20, TITLE, SUBTITLE);
                        cancel();  // 终止线程
//                     return;
                    }else{cancel();}
                    
                }
            }.runTaskTimer((Plugin)this , /*(getConfig().getInt("RespawnDelay"))*1L*/0L ,20L );
            if (getConfig().getBoolean("Clear")){
                try{
                ItemStack air = new ItemStack(0);
                for (int x=0;x<40;x++){
                    p.getInventory().setItem(x,air);
                }
                }catch(Exception e){}
            }
            
//            new BukkitRunnable()
//            {
//                int x = 0;
//                @Override
//                public void run() 
//                {
//                    if(x == 0){
//                        p.sendMessage("3");
//                        x++;
//                    }
//                    if(x == 1){
//                        p.sendMessage("4");
//                        p.setGameMode(GameMode.ADVENTURE);
//                        Location lc = p.getWorld().getSpawnLocation();
//                        p.teleport(lc);
//                        cancel();  // 终止线程
////                     return;
//                    }
//
//                }
//            }.runTaskTimer((Plugin)this , /*(getConfig().getInt("RespawnDelay"))*1L*/1L ,(getConfig().getInt("RespawnPeriod"))*20L );
    }
}
