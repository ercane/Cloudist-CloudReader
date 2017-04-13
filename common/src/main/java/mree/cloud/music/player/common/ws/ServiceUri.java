/*

 *
 * Project: mcys-server-common
 * Date Created: 16 Nis 2015
 * Created By: ykurt
 */
package mree.cloud.music.player.common.ws;

/**
 * @author ykurt
 */
public abstract class ServiceUri {

    public static final String PARAM = "/{param}";
    public static final String ID_PARAM = "/{id}";
    public static final String NAME_PARAM = "/{name}";
    public static final String MSG_PARAM = "/{msg}";
    public static final String OLD_PARAM = "/{pOld}";
    public static final String NEW_PARAM = "/{pNew}";
    public static final String TYPE_PARAM = "/{type}";
    public static final String ADD = "/add";
    public static final String UPDATE = "/update";
    public static final String GROUP = "/group";
    public static final String LIST = "/list";
    public static final String LIST_ACTIVE = "/list/a";
    public static final String LIST_ID = LIST + ID_PARAM;

    public static final String LIST_SELECTED = LIST + MSG_PARAM;

    public static final String DELETE = "/delete";
    public static final String CANCEL = "/cancel";
    public static final String RENEW = "/renew";
    public static final String SEARCH = "/search";
    public static final String JOIN = "/join";
    public static final String LEAVE = "/leave";

    public static final String DELETE_ID = DELETE + ID_PARAM;
    public static final String CANCEL_ID = CANCEL + ID_PARAM;
    public static final String RENEW_ID = RENEW + ID_PARAM;

    public static final String TEST = "/test";
    public static final String BULK = "/bulk";
    public static final String SYNC = "/sync";
    public static final String RESET = "/reset";
    public static final String AUTH = "/auth";
    public static final String LOGIN = "/login";
    public static final String LOGOUT = "/logout";
    public static final String CHANGE = "/change";
    public static final String VIEW = "/view";
    public static final String DIST = "/dist";
    public static final String MSG = "/msg";
    public static final String STAT = "/stat";
    public static final String TYPE = "/type";
    public static final String CHECK = "/check";
    public static final String AGREE = "/agree";
    public static final String PUSH = "/push";
    public static final String REGISTER = "/reg";
    public static final String BINARY = "/bin";
    public static final String INFO = "/info";
    public static final String INSTALL = "/install";
    public static final String UNINSTALL = "/uninstall";
    public static final String DOWNLOAD = "/download";
    public static final String MONITOR = "/monitor";
    public static final String PROCESS = "/process";
    public static final String CA = "/ca";
    public static final String BASH = "/bash";
    public static final String FILTER = "/filter";
    public static final String ACCOUNT = "/acc";

    public static final String INFO_ID = INFO + ID_PARAM;
    public static final String STATUS_ID_MSG_PARAM = STAT + ID_PARAM + MSG_PARAM;
    public static final String STAT_MSG_PARAM = STAT + MSG_PARAM;
    public static final String LIST_STAT_MSG_PARAM = LIST + STAT_MSG_PARAM;

    public static final String TYPE_LIST = TYPE + LIST;
    public static final String TYPE_MSG_PARAM = TYPE + MSG_PARAM;
    public static final String LIST_TYPE_MSG_PARAM = LIST + TYPE_MSG_PARAM;

    public static final String BATCH_MSG_PARAM = BASH + MSG_PARAM;
    public static final String BATCH_ID_PARAM = BASH + ID_PARAM;

}
