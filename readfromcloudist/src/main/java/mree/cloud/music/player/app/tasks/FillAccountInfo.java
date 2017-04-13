package mree.cloud.music.player.app.tasks;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.util.HashMap;
import java.util.List;

import mree.cloud.music.player.app.act.fragment.AudioFragment;
import mree.cloud.music.player.app.database.DbConstants;
import mree.cloud.music.player.app.database.DbEntryService;
import mree.cloud.music.player.common.model.SourceInfo;

/**
 * Created by eercan on 11.11.2016.
 */

public class FillAccountInfo implements Runnable {

    private Handler addHandler;
    private List<HashMap<String, String>> allAccounts;

    public FillAccountInfo(Handler addHandler, List<HashMap<String, String>> allAccounts) {
        this.addHandler = addHandler;
        this.allAccounts = allAccounts;
    }

    @Override
    public void run() {
        try {
            for (HashMap<String, String> acc : allAccounts) {
                SourceInfo ai = DbEntryService.getAccountInfo(acc.get(DbConstants.ACCOUNT_ID));
                Bundle b = new Bundle();
                b.putSerializable(AudioFragment.AUDIO_INFO, ai);
                Message m = new Message();
                m.setData(b);
                addHandler.sendMessage(m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}