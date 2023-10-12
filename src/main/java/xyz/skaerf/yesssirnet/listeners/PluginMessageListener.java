package xyz.skaerf.yesssirnet.listeners;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.ServerConnection;
import xyz.skaerf.yesssirnet.Yesssirnet;

public class PluginMessageListener {

    @Subscribe
    public void onPluginMessage(PluginMessageEvent event) {
        if (!event.getIdentifier().getId().equals("vulcan:bungee") || !(event.getSource() instanceof ServerConnection))
            return;
        event.setResult(PluginMessageEvent.ForwardResult.handled());
        ByteArrayDataInput input = ByteStreams.newDataInput(event.getData());
        String subCommand = input.readUTF();
        switch (subCommand) {
            case "alert":
                Yesssirnet.getAlertManager().handleAlert((ServerConnection)event.getSource(), input);
                break;
            case "punishment":
                Yesssirnet.getAlertManager().handlePunishment((ServerConnection)event.getSource(), input);
                break;
        }
    }
}
