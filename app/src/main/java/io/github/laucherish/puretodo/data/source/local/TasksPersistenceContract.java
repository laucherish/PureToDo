package io.github.laucherish.puretodo.data.source.local;

import android.provider.BaseColumns;

/**
 * @author laucherish
 * @date 16/4/14
 */
public final class TasksPersistenceContract {

    public TasksPersistenceContract(){}

    public static abstract class TaskEntry implements BaseColumns {
        public static final String TABLE_NAME = "task";
        public static final String COLUMN_NAME_ENTRY_ID = "entryid";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_COMPLETED = "completed";
    }
}
