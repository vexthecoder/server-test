package com.example.discordrelay;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

import javax.security.auth.login.LoginException;

public class DiscordRelay extends JavaPlugin implements Listener {

    private JDA jda;
    private String botToken;
    private String mcChatChannelId;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadConfig();

        try {
            jda = JDABuilder.createDefault(botToken).build();
            jda.awaitReady();
        } catch (LoginException | InterruptedException e) {
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        if (jda != null) {
            jda.shutdown();
        }
    }

    private void loadConfig() {
        botToken = getConfig().getString("bot-token");
        mcChatChannelId = getConfig().getString("mc-chat-channel-id");
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        String message = event.getPlayer().getName() + ": " + event.getMessage();
        sendMessageToDiscord(mcChatChannelId, message);
    }

    @EventHandler
    public void onPlayerAdvancement(PlayerAdvancementDoneEvent event) {
        String message = event.getPlayer().getName() + " has made an advancement!";
        sendMessageToDiscord(mcChatChannelId, message);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        String message = event.getDeathMessage();
        sendMessageToDiscord(mcChatChannelId, message);
    }

    private void sendMessageToDiscord(String channelId, String message) {
        TextChannel channel = jda.getTextChannelById(channelId);
        if (channel != null) {
            channel.sendMessage(message).queue();
        }
    }

    public void startServer() {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "start");
    }

    public void stopServer() {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop");
    }
}
