package xyz.skaerf.yesssirnet.cmds;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.kyori.adventure.text.minimessage.MiniMessage;
import xyz.skaerf.yesssirnet.Yesssirnet;

import java.util.Optional;
public class HubCommand implements SimpleCommand {

    private Yesssirnet yesssirnet;
    private ProxyServer server;

    public HubCommand(Yesssirnet yesssirnet, ProxyServer server) {
        this.server = server;
        this.yesssirnet = yesssirnet;
    }

    @Override
    public void execute(Invocation invocation) {
        if (!(invocation.source() instanceof Player)) {
            yesssirnet.getLogger().info("Console is not able to go to the hub. Console is not a person. Console has no soul.");
            return;
        }
        Player player = (Player) invocation.source();
        if (server.getServer("hub").isPresent()) {
            if (server.getServer("hub").get().getPlayersConnected().contains(player)) {
                player.sendMessage(MiniMessage.miniMessage().deserialize("<red>You are already connected to the hub!"));
                return;
            }
            player.sendMessage(MiniMessage.miniMessage().deserialize("<green>Attempting to connect you to the hub.."));
            Optional<RegisteredServer> hubServer = this.server.getServer("hub");
            player.createConnectionRequest(hubServer.get()).fireAndForget();
        }
        else {
            yesssirnet.getLogger().warn(player.getUsername()+" requested to connect to Hub, but the server was not present");
            player.sendMessage(MiniMessage.miniMessage().deserialize("<red>Sorry, the hub is not available!"));
        }
    }
}
