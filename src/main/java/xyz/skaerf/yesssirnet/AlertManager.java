package xyz.skaerf.yesssirnet;

import com.google.common.io.ByteArrayDataInput;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AlertManager {

    private final Set<Player> alertsEnabled = new HashSet<>();

    private final String prefix = "&4&lVulcan &8»";

    public void toggleAlerts(Player player) {
        if (this.alertsEnabled.contains(player)) {
            player.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&4&lVulcan &8» &7Vulcan Velocity alerts &cdisabled&7!"));
            this.alertsEnabled.remove(player);
        }
        else {
            player.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&4&lVulcan &8» &7Vulcan Velocity alerts &aenabled&7!"));
            this.alertsEnabled.add(player);
        }
    }

    public void handleAlert(ServerConnection connection, ByteArrayDataInput input) {
        String[] components = input.readUTF().split("#VULCAN#");
        String checkName = components[0];
        String checkType = components[1];
        String vl = components[2];
        String player = components[3];
        String maxVl = components[4];
        String clientVersion = components[5];
        String tps = components[6];
        String ping = components[7];
        String description = components[8];
        String info = components[9];
        String dev = components[10];
        String severity = components[11];
        String serverName = connection.getServerInfo().getName();
        
        String alertsFormat = "&4&lVulcan &7(%server%) &8» &f%player% &7failed &f%check% &7(&fType %type%&7) [&4%vl%&7/&4%max-vl%&7]";
        List<String> alertsHoverFormat = addAllHovers();
        String alertClickCommand = "server %server%";

        
        TextComponent alertMessage = LegacyComponentSerializer.legacyAmpersand().deserialize(alertsFormat.replace("%check%", checkName).replace("%type%", checkType).replace("%vl%", vl).replace("%player%", player).replace("%max-vl%", maxVl).replace("%client-version%", clientVersion).replace("%tps%", tps).replace("%ping%", ping).replace("%info%", info).replace("%description%", description).replace("%server%", serverName).replace("%prefix", prefix)).clickEvent(ClickEvent.runCommand("/" + alertClickCommand.replace("%player%", player).replace("%server%", serverName)));
        TextComponent.Builder hoverMessage = Component.text();
        int size = alertsHoverFormat.size();
        int i = 1;
        for (String line : alertsHoverFormat) {
            line = line.replace("%check%", checkName).replace("%type%", checkType).replace("%vl%", vl).replace("%player%", player).replace("%max-vl%", maxVl).replace("%client-version%", clientVersion).replace("%tps%", tps).replace("%ping%", ping).replace("%info%", info).replace("%description%", description).replace("%server%", serverName).replace("%dev%", dev).replace("%severity%", severity).replace("%prefix", prefix);
            hoverMessage.append(LegacyComponentSerializer.legacyAmpersand().deserialize(line));
            if (i != size)
                hoverMessage.append(Component.newline());
            i++;
        }
        alertMessage = alertMessage.hoverEvent(hoverMessage.build().asHoverEvent());
        for (Player staff : this.alertsEnabled) {
            if (staff.getCurrentServer().isPresent() && staff.getCurrentServer().get().getServerInfo().getName().equals(serverName))
                continue;
            staff.sendMessage(alertMessage);
        }
    }

    public void handlePunishment(ServerConnection connection, ByteArrayDataInput input) {
        String punishmentFormat = "&4&lVulcan &7(%server%) &8» &f%player% &7was punished for &f%check% &7(&fType %type%&7) [&4%max-vl%&7/&4%max-vl%&7]";

        String s = input.readUTF();
        Yesssirnet.getLogger().info(s);
        String[] components = s.split("#VULCAN#");
        String command = components[0];
        Yesssirnet.getServer().getCommandManager().executeAsync(Yesssirnet.getServer().getConsoleCommandSource(), command);
        String checkName = components[1];
        String checkType = components[2];
        String vl = components[3];
        String player = components[4];
        String maxVl = components[5];
        String serverName = connection.getServerInfo().getName();
        TextComponent punishmentMessage = LegacyComponentSerializer.legacyAmpersand().deserialize(punishmentFormat.replace("%check%", checkName).replace("%type%", checkType).replace("%vl%", vl).replace("%player%", player).replace("%max-vl%", maxVl).replace("%server%", serverName).replace("%prefix%", prefix));
        for (Player staff : this.alertsEnabled) {
            if (staff.getCurrentServer().isPresent() && staff.getCurrentServer().get().getServerInfo().getName().equals(serverName))
                continue;
            staff.sendMessage(punishmentMessage);
        }
    }

    public Set<Player> getAlertsEnabled() {
        return this.alertsEnabled;
    }

    private List<String> addAllHovers() {
        List<String> alertsHoverFormat = new ArrayList<>();

        alertsHoverFormat.add("&7Ping: &c%ping% &8| &7TPS: &c%tps% &8| &7Version: &c%client-version%");
        alertsHoverFormat.add("");
        alertsHoverFormat.add("&7Description:");
        alertsHoverFormat.add("&7%description%");
        alertsHoverFormat.add("");
        alertsHoverFormat.add("&7Information:");
        alertsHoverFormat.add("&7%info%");
        alertsHoverFormat.add("");
        alertsHoverFormat.add("&7Click to go to &b%server%&7.");

        return alertsHoverFormat;
    }
}
