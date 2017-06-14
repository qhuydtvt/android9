package techkids.vn.tidypiano.noteplayers;


import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.rtp.AudioStream;

import java.util.HashMap;

/**
 * Created by huynq on 6/14/17.
 */

public class NotePlayer {
    // "c" => play sound_1.wav
    // "d" => play sound_3.wav

    public static SoundPool soundPool = new SoundPool(12, AudioManager.STREAM_MUSIC, 1);
    public static HashMap<String, Integer> noteMap = new HashMap<>();

    public static void loadSounds(Context context) {
        loadSound("c", "sound_1", context);
        loadSound("cs", "sound_2", context);
        loadSound("d", "sound_3", context);
        loadSound("ds", "sound_4", context);
    }

    public static void play(String note) {
        int soundId = noteMap.get(note);
        soundPool.play(soundId, 1, 1, 1, 0, 1);
    }

    private static int loadSound(String note, String fileName, Context context) {

        int id = context
                .getResources()
                .getIdentifier(fileName, "raw", context.getPackageName());


        int soundId = soundPool.load(context, id, 1);

        noteMap.put(note, soundId);

        return id;
    }
}
