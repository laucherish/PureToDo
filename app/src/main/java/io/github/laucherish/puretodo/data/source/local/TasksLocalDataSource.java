package io.github.laucherish.puretodo.data.source.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.github.laucherish.puretodo.data.Task;
import io.github.laucherish.puretodo.data.source.TasksDataSource;
import io.github.laucherish.puretodo.data.source.local.TasksPersistenceContract.TaskEntry;

/**
 * @author laucherish
 * @date 16/4/14
 */
public class TasksLocalDataSource implements TasksDataSource {

    private static final String TAG = "TasksLocalDataSource";

    private static TasksLocalDataSource INTANCE;

    private TasksDbHelper mDbHelper;

    private TasksLocalDataSource(Context context) {
        mDbHelper = new TasksDbHelper(context);
    }

    public static TasksLocalDataSource getInstance(Context context) {
        if (INTANCE == null) {
            INTANCE = new TasksLocalDataSource(context);
        }
        return INTANCE;
    }

    @Override
    public void saveTask(Task task) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TaskEntry.COLUMN_NAME_ENTRY_ID, task.getId());
        values.put(TaskEntry.COLUMN_NAME_TITLE, task.getTitle());
        values.put(TaskEntry.COLUMN_NAME_DESCRIPTION, task.getDescription());
        values.put(TaskEntry.COLUMN_NAME_COMPLETED, task.isCompleted());
        values.put(TaskEntry.COLUMN_NAME_TIME, task.getTime());

        long flag = db.insert(TaskEntry.TABLE_NAME, null, values);
        Log.d(TAG, "saveTask: " + flag + task.getTitle());

        db.close();
    }

    @Override
    public void deleteTask(String taskId) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        String selection = TaskEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
        String[] selectionArgs = {taskId};

        db.delete(TaskEntry.TABLE_NAME, selection, selectionArgs);

        db.close();
    }

    @Override
    public void deleteAllTasks() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        db.delete(TaskEntry.TABLE_NAME, null, null);

        db.close();
    }

    @Override
    public void getTask(String taskId, GetTaskCallback callback) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                TaskEntry.COLUMN_NAME_ENTRY_ID,
                TaskEntry.COLUMN_NAME_TITLE,
                TaskEntry.COLUMN_NAME_DESCRIPTION,
                TaskEntry.COLUMN_NAME_COMPLETED,
                TaskEntry.COLUMN_NAME_TIME
        };

        String selection = TaskEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
        String[] selectionArgs = {taskId};

        Cursor cursor = db.query(TaskEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        Task task = null;

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            String itemId = cursor.getString(cursor.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_ENTRY_ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_TITLE));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_DESCRIPTION));
            boolean completed = cursor.getInt(cursor.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_COMPLETED)) == 1;
            long time = cursor.getInt(cursor.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_TIME));
            task = new Task(itemId, title, description, completed, time);
        }
        if (cursor != null) {
            cursor.close();
        }

        db.close();

        if (task != null) {
            callback.onTaskLoaded(task);
        } else {
            callback.onDataNotAvailable();
        }
    }

    @Override
    public void getAllTasks(LoadTasksCallback callback) {
        List<Task> tasks = new ArrayList<>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                TaskEntry.COLUMN_NAME_ENTRY_ID,
                TaskEntry.COLUMN_NAME_TITLE,
                TaskEntry.COLUMN_NAME_DESCRIPTION,
                TaskEntry.COLUMN_NAME_COMPLETED,
                TaskEntry.COLUMN_NAME_TIME
        };

        String orderBy = TaskEntry.COLUMN_NAME_COMPLETED + "," + TaskEntry.COLUMN_NAME_TIME + " DESC";
        Cursor cursor = db.query(
                TaskEntry.TABLE_NAME, projection, null, null, null, null, orderBy);

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String itemId = cursor.getString(cursor.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_ENTRY_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_TITLE));
                String description =
                        cursor.getString(cursor.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_DESCRIPTION));
                boolean completed =
                        cursor.getInt(cursor.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_COMPLETED)) == 1;
                long time = cursor.getInt(cursor.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_TIME));
                Task task = new Task(itemId, title, description, completed, time);
                tasks.add(task);
            }
        }
        if (cursor != null) {
            cursor.close();
        }

        db.close();

        if (tasks.isEmpty()) {
            callback.onDataNotAvailable();
        } else {
            callback.onTasksLoaded(tasks);
        }
    }

    public List<Task> getAllTasksSync() {
        List<Task> tasks = new ArrayList<>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                TaskEntry.COLUMN_NAME_ENTRY_ID,
                TaskEntry.COLUMN_NAME_TITLE,
                TaskEntry.COLUMN_NAME_DESCRIPTION,
                TaskEntry.COLUMN_NAME_COMPLETED,
                TaskEntry.COLUMN_NAME_TIME
        };

        String orderBy = TaskEntry.COLUMN_NAME_COMPLETED + "," + TaskEntry.COLUMN_NAME_TIME + " DESC";
        Cursor cursor = db.query(
                TaskEntry.TABLE_NAME, projection, null, null, null, null, orderBy);

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String itemId = cursor.getString(cursor.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_ENTRY_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_TITLE));
                String description =
                        cursor.getString(cursor.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_DESCRIPTION));
                boolean completed =
                        cursor.getInt(cursor.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_COMPLETED)) == 1;
                long time = cursor.getInt(cursor.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_TIME));
                Task task = new Task(itemId, title, description, completed, time);
                if (!completed) {
                    tasks.add(task);
                }
            }
        }
        if (cursor != null) {
            cursor.close();
        }

        db.close();

        return tasks;
    }

    @Override
    public void completeTask(String taskId) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TaskEntry.COLUMN_NAME_COMPLETED, true);

        String seletion = TaskEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
        String[] seletionArgs = {taskId};

        db.update(TaskEntry.TABLE_NAME, values, seletion, seletionArgs);

        db.close();
    }

    @Override
    public void activateTask(String taskId) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TaskEntry.COLUMN_NAME_COMPLETED, false);

        String seletion = TaskEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
        String[] seletionArgs = {taskId};

        db.update(TaskEntry.TABLE_NAME, values, seletion, seletionArgs);

        db.close();
    }

    @Override
    public void updateTask(Task task) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TaskEntry.COLUMN_NAME_ENTRY_ID, task.getId());
        values.put(TaskEntry.COLUMN_NAME_TITLE, task.getTitle());
        values.put(TaskEntry.COLUMN_NAME_DESCRIPTION, task.getDescription());
        values.put(TaskEntry.COLUMN_NAME_COMPLETED, task.isCompleted());
        values.put(TaskEntry.COLUMN_NAME_TIME, task.getTime());

        String seletion = TaskEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
        String[] seletionArgs = {task.getId()};

        db.update(TaskEntry.TABLE_NAME, values, seletion, seletionArgs);

        db.close();
    }

    @Override
    public void clearCompletedTasks() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        String selection = TaskEntry.COLUMN_NAME_COMPLETED + " LIKE ?";
        String[] selectionArgs = {"1"};

        db.delete(TaskEntry.TABLE_NAME, selection, selectionArgs);

        db.close();
    }

    @Override
    public void refreshTasks() {

    }
}
