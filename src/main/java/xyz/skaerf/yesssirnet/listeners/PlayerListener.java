package xyz.skaerf.yesssirnet.listeners;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.proxy.Player;
import xyz.skaerf.yesssirnet.Yesssirnet;

public class PlayerListener {

    @Subscribe
    public void onLogin(PostLoginEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("vulcanbungee.alerts"))
            Yesssirnet.getAlertManager().toggleAlerts(player);
    }

    @Subscribe
    public void onDisconnect(DisconnectEvent event) {
        Yesssirnet.getAlertManager().getAlertsEnabled().remove(event.getPlayer());
    }
}
