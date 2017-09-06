/**
 * Created by mikni on 21.08.2017.
 */

package ua.cn.sandi.screenturn;

import java.util.Arrays;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.Settings;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.Toast;


public class ScreenTurnProvider extends AppWidgetProvider {

    public static String ACTION_CHANGE = "ua.cn.samdi.screenturn.button_change";

    final String LOG_TAG = "myLogs";

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Log.d(LOG_TAG, "onEnabled");
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        Log.d(LOG_TAG, "onUpdate " + Arrays.toString(appWidgetIds));

        for (int i : appWidgetIds) {
            updateWidget(context, appWidgetManager, i);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        Log.d(LOG_TAG, "onDeleted " + Arrays.toString(appWidgetIds));
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        Log.d(LOG_TAG, "onDisabled");
    }

    public void updateWidget(Context context, AppWidgetManager appWidgetManager, int widgetID) {

        Log.d(LOG_TAG, "updateWidget started ");

        // here code

        RemoteViews widgetView = new RemoteViews(context.getPackageName(), R.layout.st_layout);

        switch (android.provider.Settings.System.getInt(context.getContentResolver(), android.provider.Settings.System.ACCELEROMETER_ROTATION, -1)) {
            case 0: {
                widgetView.setInt(R.id.button_change, "setBackgroundColor", Color.RED);
                widgetView.setInt(R.id.button_change, "setTextColor", Color.BLUE);

                break;
            }
            case 1: {
                widgetView.setInt(R.id.button_change, "setBackgroundColor", Color.GREEN);
                widgetView.setInt(R.id.button_change, "setTextColor", Color.YELLOW);


                break;
            }
        }


        Intent countIntent = new Intent(context, ScreenTurnProvider.class);
        countIntent.setAction(ACTION_CHANGE);
        countIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);
        PendingIntent pIntent = PendingIntent.getBroadcast(context,
                widgetID, countIntent, 0);
        widgetView.setOnClickPendingIntent(R.id.button_change, pIntent);

        // updateAppWidget
        appWidgetManager.updateAppWidget(widgetID, widgetView);
    }

    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.d(LOG_TAG, "onReceive started");

        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        final int int_ori = display.getRotation();


        // Проверяем, что это intent от нажатия
        if (intent.getAction().equalsIgnoreCase(ACTION_CHANGE)) {
            // извлекаем ID экземпляра
            int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
            Bundle extras = intent.getExtras();
            if (extras != null) {
                mAppWidgetId = extras.getInt(
                        AppWidgetManager.EXTRA_APPWIDGET_ID,
                        AppWidgetManager.INVALID_APPWIDGET_ID);
            }

            if (mAppWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
                Log.d(LOG_TAG, "onReceive alert code started");
                //code here  --------------- R.layout.st_layout
                switch (android.provider.Settings.System.getInt(context.getContentResolver(), android.provider.Settings.System.ACCELEROMETER_ROTATION, -1)) {
                    case 1: {
                        Settings.System.putInt(context.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0);
                        Toast toast = Toast.makeText(context, "Auto-Rotate disabled " + int_ori, Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        break;
                    }
                    case 0: {
                        Settings.System.putInt(context.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 1);
                        Toast toast = Toast.makeText(context, "Auto-Rotate enabled " + int_ori, Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        break;
                    }
                }
                //code here end ---------------
                Log.d(LOG_TAG, "onReceive alert code ended ");
                //updateWidget
                updateWidget(context, AppWidgetManager.getInstance(context),mAppWidgetId);
            }
        }
    }
}
