package mree.cloud.music.player.app.report;

import mree.cloud.music.player.common.ref.SourceType;

/**
 * Created by eercan on 24.02.2017.
 */

public interface IReport {
    void addAccount(String id, SourceType type);

    void authAccount(String id, SourceType type);

    void scanAccountStart(String id, SourceType type);

    void scanAccountFinish(String id, SourceType type);

    void audioPlayRequest(String audioName, SourceType type);


}
