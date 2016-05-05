package io.github.laucherish.puretodo.other;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import io.github.laucherish.puretodo.R;
import io.github.laucherish.puretodo.addedittask.AddEditTaskActivity;
import io.github.laucherish.puretodo.addedittask.AddEditTaskFragment;
import io.github.laucherish.puretodo.data.source.local.TasksLocalDataSource;
import io.github.laucherish.puretodo.tasks.TasksActivity;

/**
 * @author laucherish
 * @date 16/4/25
 */
public class TasksWidget extends AppWidgetProvider {

    private static final String TAG = "TasksWidget";

    public static final String ACTION_DONE = "io.github.laucherish.puretodo.ACTION_DONE";
    public static final String EXTRA_STRING = "io.github.laucherish.puretodo.EXTRA_STRING";
    public static final String EXTRA_DO = "io.github.laucherish.puretodo.EXTRA_DO";
    public static final int DO_DONE = 1;
    public static final int DO_EDIT = 2;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: " + intent.getAction());

        if (intent.getAction().equals(ACTION_DONE)) {
            String taskId = intent.getExtras().getString(EXTRA_STRING);
            int doInt = intent.getExtras().getInt(EXTRA_DO);

            switch (doInt) {
                case DO_DONE:
                    TasksLocalDataSource.getInstance(context).completeTask(taskId);
                    Toast.makeText(context, R.string.task_marked_complete, Toast.LENGTH_LONG).show();
                    break;
                case DO_EDIT:
                    Intent editIntent = new Intent(context, AddEditTaskActivity.class);
                    editIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    editIntent.putExtra(AddEditTaskFragment.ARGUMENT_EDIT_TASK_ID, taskId);
                    context.startActivity(editIntent);
                    break;
            }
        }

        super.onReceive(context, intent);

        // 更新 Widget
        final AppWidgetManager mgr = AppWidgetManager.getInstance(context);
        final ComponentName cn = new ComponentName(context, TasksWidget.class);
        mgr.notifyAppWidgetViewDataChanged(mgr.getAppWidgetIds(cn), R.id.lv_widget);


    }

    @SuppressLint("NewApi")
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        Log.d(TAG, "onUpdate: ");
        for (int widgetId : appWidgetIds) {
            RemoteViews mView = initViews(context, appWidgetManager, widgetId);

            // 设置 AppWidget Done 点击事件
            final Intent doneIntent = new Intent(context, TasksWidget.class);
            doneIntent.setAction(ACTION_DONE);
            doneIntent.setData(Uri.parse(doneIntent.toUri(Intent.URI_INTENT_SCHEME)));
            final PendingIntent donePendingIntent = PendingIntent
                    .getBroadcast(context, 0, doneIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            mView.setPendingIntentTemplate(R.id.lv_widget, donePendingIntent);

            // 设置 AppWidget Add 点击事件
            Intent addIntent = new Intent(context, AddEditTaskActivity.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(AddEditTaskActivity.class);
            stackBuilder.addNextIntent(addIntent);
            PendingIntent addStackIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            PendingIntent addPendingIntent = PendingIntent.getActivity(context, 0, addIntent, 0);
            mView.setOnClickPendingIntent(R.id.iv_widget_add, addStackIntent);

            // 设置 AppWidget label 点击事件
            Intent labelIntent = new Intent(context, TasksActivity.class);
            PendingIntent labelPendingIntent = PendingIntent.getActivity(context, 0, labelIntent, 0);
            mView.setOnClickPendingIntent(R.id.tv_widget_lable, labelPendingIntent);

            appWidgetManager.updateAppWidget(widgetId, mView);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    private RemoteViews initViews(Context context,
                                  AppWidgetManager widgetManager, int widgetId) {

        RemoteViews mView = new RemoteViews(context.getPackageName(),
                R.layout.widget_tasks42);

        Intent intent = new Intent(context, TasksWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);

        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        mView.setRemoteAdapter(widgetId, R.id.lv_widget, intent);

        return mView;
    }
}
