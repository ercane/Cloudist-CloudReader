package mree.cloud.music.player.app.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by eercan on 20.01.2017.
 */

public class DbSpecialOperations extends DbEntryService {

    public static boolean addColumn(String tableName, String newColumn, String type, String
            defaultValue) {
        try {
            Cursor cursor = getDB().rawQuery("SELECT * FROM " + tableName, null);
            int cursorColumnIndex = cursor.getColumnIndex(newColumn);
            if (cursorColumnIndex < 0) {
                String query = "ALTER TABLE " + tableName + " ADD COLUMN " + newColumn + " " +
                        type + "";
                getDB().execSQL(query);

                if (defaultValue != null) {
                    ContentValues values = new ContentValues();
                    values.put(newColumn, defaultValue);
                    int update = getDB().update(tableName, values, newColumn + " is null", null);
                    if (update < 0)
                        return false;
                }
            }
            return true;
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage());
            return false;
        }
    }


    public static boolean recreateTableWithData(String tableName) {
        try {
            Cursor cursor = getDB().rawQuery("SELECT COUNT(*) FROM " + tableName, null);
            int count = -1;
            if (cursor.moveToFirst()) {
                do {
                    HashMap<String, String> map = new HashMap<String, String>();
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        count = cursor.getInt(i);
                    }
                } while (cursor.moveToNext());
            }
            String tempTable = "temp_" + tableName;
            String query = "ALTER TABLE " + tableName + " RENAME TO " + tempTable;
            getDB().execSQL(query);
            query = "CREATE TABLE " + tableName + " AS SELECT * FROM " + tempTable;
            getDB().execSQL(query);
            DbTableService.dropTable(tempTable);
            cursor = getDB().rawQuery("SELECT COUNT(*) FROM " + tableName, null);

            int count2 = -1;
            if (cursor.moveToFirst()) {
                do {
                    HashMap<String, String> map = new HashMap<String, String>();
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        count2 = cursor.getInt(i);
                    }
                } while (cursor.moveToNext());
            }

            return count >= 0 && count == count2;
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage());
            return false;
        }
    }

    public static boolean isFieldExist(String tableName, String fieldName) {
        boolean isExist = true;
        Cursor res = getDB().rawQuery("PRAGMA table_info(" + tableName + ")", null);
        int value = res.getColumnIndex(fieldName);

        if (value == -1) {
            isExist = false;
        }
        return isExist;
    }
}
