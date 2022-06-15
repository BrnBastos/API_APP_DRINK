package com.example.showdrink1;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class AbrirHistoricoWidget extends AppWidgetProvider {

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;

        // Realizado um loop para cada widget criada 
        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];

            // Faz um armazenamento para direcionar o usuário para a MainActivity
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            // Coloca o armazenamento no botão para realizar sua tarefa
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.abrir_historico_widget);
            views.setOnClickPendingIntent(R.id.appwidget_button, pendingIntent);

            // Faz a atualização dos dados da widget pela sua Id
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}
