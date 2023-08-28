package xyz.skaerf.yesssirnet;

import com.google.inject.Inject;
import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;
import xyz.skaerf.yesssirnet.cmds.HubCommand;

import java.awt.image.AffineTransformOp;
import java.io.IOException;
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
    private Logger logger;
    private ProxyServer server;
    private Path folder;

    @Inject
    public Yesssirnet(ProxyServer server, Logger logger, CommandManager commandManager, @DataDirectory Path folder) throws IOException {
        this.server = server;
        this.logger = logger;
        this.folder = folder;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        CommandMeta hubMeta = server.getCommandManager().metaBuilder("hub").build();
        server.getCommandManager().register(hubMeta, new HubCommand(this, this.server));
        getLogger().info("Launched successfully");
    }

    public Logger getLogger() {
        return this.logger;
    }
}
