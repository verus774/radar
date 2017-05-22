package by.verus.radar;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.AppWidgetTarget;

import java.util.Random;

public class RadarWidget extends AppWidgetProvider {
    private AppWidgetTarget appWidgetTarget;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        int number = (new Random().nextInt(100));

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.radar_widget);
        views.setTextViewText(R.id.appwidget_text, String.valueOf(widgetText));

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        /*for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }*/

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.radar_widget);
        appWidgetTarget = new AppWidgetTarget(context, R.id.iv, views, appWidgetIds);

        loadRadarImg(context, Radar.RADAR_IMG_URL, appWidgetTarget);

        // start MainActivity clicking by widget
        Intent configIntent = new Intent(context, MainActivity.class);
        PendingIntent configPendingIntent = PendingIntent.getActivity(context, 0, configIntent, 0);
        views.setOnClickPendingIntent(R.id.iv, configPendingIntent);

        appWidgetManager.updateAppWidget(appWidgetIds, views);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            ComponentName widget = new ComponentName(context, RadarWidget.class);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.radar_widget);
            int[] appWidgetIds = AppWidgetManager.getInstance(context).getAppWidgetIds(widget);
            appWidgetTarget = new AppWidgetTarget(context, R.id.iv, views, appWidgetIds);

            loadRadarImg(context, Radar.RADAR_IMG_URL, appWidgetTarget);

            (AppWidgetManager.getInstance(context)).updateAppWidget(widget, views);
        }
    }

    private void loadRadarImg(Context context, String imgUrl, AppWidgetTarget appWidgetTarget) {
        RequestOptions options = new RequestOptions()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE);
        Glide.with(context)
                .asBitmap()
                .load(imgUrl)
                .apply(options)
                .into(appWidgetTarget);
    }

}

