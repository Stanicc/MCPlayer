package stanic.mcplayer

import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import stanic.mcplayer.commands.PlayerCommand
import stanic.mcplayer.common.VideoManager
import stanic.mcplayer.common.extensions.send
import java.io.File

class MCPlayer : JavaPlugin() {

    private lateinit var videoManager: VideoManager

    override fun onEnable() {
        videoManager = VideoManager(this)

        getCommand("player")?.setExecutor(PlayerCommand(videoManager))

        Bukkit.getConsoleSender().send("&e[MCPlayer] &fEnabled!")
    }

    override fun onDisable() {
        //On disable logic
    }

}