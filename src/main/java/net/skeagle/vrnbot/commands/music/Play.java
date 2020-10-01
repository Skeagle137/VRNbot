package net.skeagle.vrnbot.commands.music;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchResult;
import net.skeagle.vrnbot.settings.Config;
import net.skeagle.vrnbot.handlers.Command;
import net.skeagle.vrnbot.handlers.lavaplayer.PlayerManager;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class Play extends Command {
    private YouTube yt;

    public Play() {
        super("play|p");
        setDesc("Plays music from a URL or using keywords.");
        setUsage("<url|keywords>");
        setCategory(Category.MUSIC);
        YouTube yt = null;

        try {
            yt = new YouTube.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    JacksonFactory.getDefaultInstance(),
                    null)
                    .setApplicationName("VRN-bot").build();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        this.yt = yt;
    }

    @Override
    public void runCMD() {
        if (args.length > 0) {
            PlayerManager manager = PlayerManager.getInstance();

            String input = String.join(" ", args);

            if (!joinVoice(e.getMember(), g)) return;
            joinVoice(e.getMember(), g);

            if (!isUrl(input)) {
                String s = ytSearch(input);

                if (s == null) {
                    send("The search returned no results.");
                    return;
                }

                input = s;
            }

            manager.loadAndPlay(channel, input);

            manager.getGuildMusicManager(g).player.setVolume(60);
            return;
        }
        send("You must include a URL or keywords in the command.");
    }

    private String ytSearch(String s) {
        try {
            List<SearchResult> searchresults = yt.search()
                    .list("id,snippet")
                    .setQ(s)
                    .setMaxResults(1L)
                    .setType("video")
                    .setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)")
                    .setKey(Config.YTKEY).execute().getItems();
            if (!searchresults.isEmpty()) {
                String id = searchresults.get(0).getId().getVideoId();

                return "https://www.youtube.com/watch?v=" + id;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private boolean isUrl(String input) {
        try {
            new URL(input);

            return true;
        } catch (MalformedURLException ignored) {
            return false;
        }
    }
}