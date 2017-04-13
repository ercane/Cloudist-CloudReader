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
public class AuthServiceUri extends ServiceUri {

    public static final String USER = "/user";
    public static final String USER_LIST = USER + LIST;
    public static final String USER_LIST_ID = USER_LIST + ID_PARAM;

    public static final String BULK_ADD = BULK + ADD;
    public static final String BULK_ADD_ID = BULK_ADD + ID_PARAM;
    public static final String BULK_JOIN_ID = BULK + JOIN + ID_PARAM;
    public static final String BULK_LEAVE_ID = BULK + LEAVE + ID_PARAM;

    public static final String DIST_ID = DIST + ID_PARAM;

    public static final String SYNC_ID = SYNC + ID_PARAM;
    public static final String RESET_ID = RESET + ID_PARAM;
    public static final String CHANGE_ID = CHANGE + ID_PARAM + NEW_PARAM + OLD_PARAM;

    public static final String ACCOUNT = "/account";
    public static final String ACCOUNT_ADD = ACCOUNT + ADD;
    public static final String ACCOUNT_BULK_ADD = ACCOUNT + BULK_ADD;
    public static final String ACCOUNT_UPDATE = ACCOUNT + UPDATE;
    public static final String ACCOUNT_LIST = ACCOUNT + LIST;
    public static final String MODULE_LIST_ID = "/mod" + LIST_ID;
    public static final String ACCOUNT_LIST_BY_MODULE = ACCOUNT + MODULE_LIST_ID;
    public static final String ACCOUNT_NAME = ACCOUNT + NAME_PARAM;
    public static final String ACCOUNT_ID = ACCOUNT + ID_PARAM;
    public static final String ACCOUNT_DELETE = ACCOUNT_ID;
    public static final String ACCOUNT_LIST_ID = ACCOUNT_LIST + ID_PARAM;
    public static final String ACCOUNT_RESET = ACCOUNT + RESET_ID;
    public static final String ACCOUNT_CHANGE_PWD = ACCOUNT + CHANGE_ID;
    public static final String ACCOUNT_SET_STATUS = ACCOUNT + STATUS_ID_MSG_PARAM;
    public static final String ACCOUNT_SYNC = ACCOUNT + SYNC_ID;

    public static final String USER_GROUP = "/group";
    public static final String USER_GROUP_ADD = USER_GROUP + ADD;
    public static final String USER_GROUP_BULK_JOIN = USER_GROUP + BULK_JOIN_ID;
    public static final String USER_GROUP_BULK_LEAVE = USER_GROUP + BULK_LEAVE_ID;
    public static final String USER_GROUP_UPDATE = USER_GROUP + UPDATE;
    public static final String USER_GROUP_LIST = USER_GROUP + LIST;
    public static final String USER_GROUP_NAME = USER_GROUP + NAME_PARAM;
    public static final String USER_GROUP_ID = USER_GROUP + ID_PARAM;
    public static final String USER_GROUP_DELETE = USER_GROUP_ID;
    public static final String USER_GROUP_LIST_ID = USER_GROUP_LIST + ID_PARAM;
    public static final String USER_GROUP_USER_LIST_ID = USER_GROUP + USER_LIST_ID;
    public static final String USER_GROUP_RESET = USER_GROUP + RESET_ID;
    public static final String USER_GROUP_CHANGE_PWD = USER_GROUP + CHANGE_ID;
    public static final String USER_GROUP_SET_STATUS = USER_GROUP + STATUS_ID_MSG_PARAM;
    public static final String USER_GROUP_SYNC = USER_GROUP + SYNC_ID;

    public static final String REALM = "/realm";
    public static final String REALM_ADD = REALM + ADD;
    public static final String REALM_UPDATE = REALM + UPDATE;
    public static final String REALM_LIST = REALM + LIST;
    public static final String REALM_ID = REALM + ID_PARAM;
    public static final String REALM_DELETE = REALM + ID_PARAM;
    public static final String REALM_TEST = REALM + TEST;
    public static final String REALM_ACCOUNT_LIST = REALM + ACCOUNT_LIST_ID;
    public static final String REALM_USER_LIST = REALM + USER_LIST_ID;
    public static final String REALM_GROUP_LIST = REALM + USER_GROUP_LIST_ID;
    public static final String GROUP_USER_LIST = ID_PARAM + USER_GROUP_NAME + USER_LIST;
    public static final String REALM_GROUP_USER_LIST = REALM + GROUP_USER_LIST;

    public static final String ROLE = "/role";
    public static final String ROLE_ADD = ROLE + ADD;
    public static final String ROLE_UPDATE = ROLE + UPDATE;
    public static final String ROLE_STAT_ID = ROLE + STATUS_ID_MSG_PARAM;

    public static final String DEFAULT = "/def";
    public static final String DEFAULT_ID = DEFAULT + ID_PARAM;
    public static final String ROLE_SET_DEFAULT = ROLE + DEFAULT_ID;
    public static final String ROLE_GET_DEFAULT = ROLE + DEFAULT;

}
