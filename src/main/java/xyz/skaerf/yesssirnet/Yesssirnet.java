package xyz.skaerf.yesssirnet;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import org.slf4j.Logger;
import xyz.skaerf.yesssirnet.cmds.HubCommand;
import xyz.skaerf.yesssirnet.listeners.PlayerListener;
import xyz.skaerf.yesssirnet.listeners.PluginMessageListener;

import java.nio.file.Path;

@Plugin(
        id = "yesssirnet",
        name = "yesssirnet",
        version = "1.0",
        authors = {"skaerf"},
        description = "yesssirnet package for Velocity"
)
public class Yesssirnet {

    @Inject
    private static Logger logger;
    private static ProxyServer server;
    private Path folder;

    private static final AlertManager alertManager = new AlertManager();

    @Inject
    public Yesssirnet(ProxyServer server, Logger logger, CommandManager commandManager, @DataDirectory Path folder) {
        Yesssirnet.server = server;
        Yesssirnet.logger = logger;
        this.folder = folder;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        // hub
        CommandMeta hubMeta = server.getCommandManager().metaBuilder("hub").build();
        server.getCommandManager().register(hubMeta, new HubCommand(this, server));

        // vulcan messages
        server.getChannelRegistrar().register(MinecraftChannelIdentifier.from("vulcan:bungee"));
        server.getEventManager().register(this, new PlayerListener());
        server.getEventManager().register(this, new PluginMessageListener());
        getLogger().info("Launched successfully");
    }

    public static Logger getLogger() {
        return logger;
    }

    public static AlertManager getAlertManager() {
        return alertManager;
    }

    public static ProxyServer getServer() {
        return server;
    }
}
