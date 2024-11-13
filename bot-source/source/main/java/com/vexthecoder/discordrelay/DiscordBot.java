import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import java.util.Timer;
import java.util.TimerTask;

public class DiscordBot extends ListenerAdapter {

    private final TextChannel mcChatChannel;
    private final String adminRoleId;
    private final long rstartCooldown = 1800 * 1000;
    private long lastRstartRequest = 0;

    public DiscordBot(String token, String mcChatChannelId, String adminRoleId) {
        this.mcChatChannel = JDABuilder.createDefault(token).build().awaitReady().getTextChannelById(mcChatChannelId);
        this.adminRoleId = adminRoleId;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (!event.getChannel().equals(mcChatChannel)) return;

        String message = event.getMessage().getContentRaw();
        if (message.startsWith("/start") && event.getMember().getRoles().contains(adminRoleId)) {
            startMinecraftServer(event);
        } else if (message.startsWith("/stop") && event.getMember().getRoles().contains(adminRoleId)) {
            stopMinecraftServer(event);
        } else if (message.startsWith("/rstart")) {
            requestServerStart(event);
        }
    }

    private void startMinecraftServer(MessageReceivedEvent event) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "start");
        mcChatChannel.sendMessage("Server is starting...").queue();
    }

    private void stopMinecraftServer(MessageReceivedEvent event) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop");
        mcChatChannel.sendMessage("Server is stopping...").queue();
    }

    private void requestServerStart(MessageReceivedEvent event) {
        long now = System.currentTimeMillis();
        if (now - lastRstartRequest > rstartCooldown) {
            lastRstartRequest = now;
            mcChatChannel.sendMessage("<@&" + adminRoleId + "> " + event.getAuthor().getAsMention() + " has requested the server to start.").queue();
        } else {
            long minutesRemaining = (rstartCooldown - (now - lastRstartRequest)) / 60000;
            event.getChannel().sendMessage("The /rstart command is on cooldown. Please wait " + minutesRemaining + " minutes.").queue();
        }
    }
}
