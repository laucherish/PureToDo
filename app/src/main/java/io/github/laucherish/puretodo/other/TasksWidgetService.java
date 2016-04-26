package io.github.laucherish.puretodo.other;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.List;

import io.github.laucherish.puretodo.R;
import io.github.laucherish.puretodo.data.Task;
import io.github.laucherish.puretodo.data.source.local.TasksLocalDataSource;

/**
 * @author laucherish
 * @date 16/4/25
 */
public class TasksWidgetService extends RemoteViewsService {

    private static final String TAG = "TasksWidgetService";

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new TasksWidgetFactory(this.getApplicationContext(), intent);
    }

    class TasksWidgetFactory implements RemoteViewsService.RemoteViewsFactory {

        List<String> mCollections = new ArrayList<String>();

        List<Task> mTasks = new ArrayList<>();

        Context mContext = null;

        public TasksWidgetFactory(Context context, Intent intent) {
            mContext = context;
        }

        @Override
        public int getCount() {
            return mTasks.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public RemoteViews getViewAt(int position) {
            Task task = mTasks.get(position);
            RemoteViews mView = new RemoteViews(mContext.getPackageName(),
                    R.layout.widget_item_tasks);
            mView.setTextViewText(R.id.tv_widget_title, task.getTitle());
            mView.setTextColor(R.id.tv_widget_title, Color.BLACK);

            final Intent doneIntent = new Intent();
//            doneIntent.setAction(TasksWidget.ACTION_DONE);
            doneIntent.putExtra(TasksWidget.EXTRA_STRING, task.getId());
            doneIntent.putExtra(TasksWidget.EXTRA_DO, TasksWidget.DO_DONE);
            mView.setOnClickFillInIntent(R.id.iv_widget_done, doneIntent);

            final Intent fillInIntent = new Intent();
//            fillInIntent.setAction(TasksWidget.ACTION_EDIT);
            fillInIntent.putExtra(TasksWidget.EXTRA_STRING, task.getId());
            fillInIntent.putExtra(TasksWidget.EXTRA_DO, TasksWidget.DO_EDIT);
            mView.setOnClickFillInIntent(R.id.tv_widget_title, fillInIntent);
            return mView;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public void onCreate() {
            Log.d(TAG, "onCreate: ");
            initData();
        }

        @Override
        public void onDataSetChanged() {
            Log.d(TAG, "onDataSetChanged: ");
            initData();
        }

        private void initData() {
            mTasks.clear();
            mTasks = TasksLocalDataSource.getInstance(mContext).getAllTasksSync();
        }

        @Override
        public void onDestroy() {

        }
    }
}
