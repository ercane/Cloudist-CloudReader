package mree.cloud.music.player.app.report;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;

import mree.cloud.music.player.common.ref.SourceType;

/**
 * Created by eercan on 24.02.2017.
 */

public class AnswersImpl {


    public static void addAccount(String id, SourceType type) {
        CustomEvent event = new CustomEvent("Add Account").putCustomAttribute("Account Type",
                type.getDesc());
        Answers.getInstance().logCustom(event);
    }

    public static void nameAccount(String name, SourceType type) {
        CustomEvent event = new CustomEvent("Account name").
                putCustomAttribute("Account Name", name).
                putCustomAttribute("Account Type", type.getDesc());
        Answers.getInstance().logCustom(event);
    }


    public static void authAccount(String id, SourceType type) {
        CustomEvent event = new CustomEvent("Auth Account").putCustomAttribute("Account Type",
                type.getDesc());
        Answers.getInstance().logCustom(event);
    }


    public static void scanAccountStart(String id, SourceType type) {
        CustomEvent event = new CustomEvent("Scan Account Started").putCustomAttribute("Account " +
                "Type", type.getDesc());
        Answers.getInstance().logCustom(event);
    }


    public static void scanAccountFinish(String id, SourceType type) {
        CustomEvent event = new CustomEvent("Scan Account Finished").putCustomAttribute("Account " +
                "Type", type.getDesc());
        Answers.getInstance().logCustom(event);
    }


    public static void audioPlayRequest(String audioName, SourceType type) {
        CustomEvent event = new CustomEvent("Song played").
                putCustomAttribute("Audio Name", audioName).
                putCustomAttribute("Account Type", type.getDesc());
        Answers.getInstance().logCustom(event);
    }
}
