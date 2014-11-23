package com.example.TextMailnChat;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class Textmailwidget extends AppWidgetProvider{

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		
		final int N = appWidgetIds.length;
		for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];
            
            Intent intent = new Intent(context, Launchbyspeech.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widgetlayout);
            views.setOnClickPendingIntent(R.id.wbutton, pendingIntent);
            appWidgetManager.updateAppWidget(appWidgetId, views);
		}
		
	}
	

}
