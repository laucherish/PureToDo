package io.github.laucherish.puretodo.other;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.List;

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

        Context mContext = null;

        public TasksWidgetFactory(Context context, Intent intent) {
            mContext = context;
        }

        @Override
        public int getCount() {
            return mCollections.size();
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
            RemoteViews mView = new RemoteViews(mContext.getPackageName(),
                    android.R.layout.simple_list_item_1);
            mView.setTextViewText(android.R.id.text1, mCollections.get(position));
            mView.setTextColor(android.R.id.text1, Color.BLACK);

            final Intent fillInIntent = new Intent();
            fillInIntent.setAction(TasksWidget.ACTION_TOAST);
            final Bundle bundle = new Bundle();
            bundle.putString(TasksWidget.EXTRA_STRING,
                    mCollections.get(position));
            fillInIntent.putExtras(bundle);
            mView.setOnClickFillInIntent(android.R.id.text1, fillInIntent);
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
            initData();
        }

        @Override
        public void onDataSetChanged() {
            initData();
        }

        private void initData() {
            mCollections.clear();
            for (int i = 1; i <= 10; i++) {
                mCollections.add("ListView item " + i);
            }
        }

        @Override
        public void onDestroy() {

        }
    }
}
