package mree.cloud.music.player.app.utils.lazylist;

import android.content.Context;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import mree.cloud.music.player.app.utils.RandomStringUtils;

public class FileCache {

    private File cacheDir;
    private Map<String, String> fileMap;

    public FileCache(Context context) {
        //Find the dir to save cached images

        cacheDir = context.getCacheDir();
        if (!cacheDir.exists())
            cacheDir.mkdirs();

        fileMap = new HashMap<>();
    }

    public File getFile(String url) {
        //I identify images by hashcode. Not a perfect solution, good for the demo.
        //String filename = String.valueOf(url.hashCode());
        //Another possible solution (thanks to grantland)
        String filename = RandomStringUtils.randomAlphanumeric(20);
        fileMap.put(url, filename);
        File f = new File(cacheDir, filename);
        return f;

    }

    public void clear() {
        File[] files = cacheDir.listFiles();
        if (files == null)
            return;
        for (File f : files)
            f.delete();
    }

    public String getFilename(String url) {
        return fileMap.get(url);
    }

}