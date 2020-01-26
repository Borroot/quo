package tk.borroot.quo;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Implementation of App Widget functionality.
 */
public class Widget extends AppWidgetProvider {

    private static final String TAG = Widget.class.getSimpleName();

    private static final String ACTION_WIDGET = "tk.borroot.quo.action.APPWIDGET_CLICK";
    private static final String[] SYMBOLS = {"L", "R"};

    private String updateSymbol(Context context) {
        // Create the shared pref object.
        SharedPreferences sharedPref = context.getSharedPreferences(
                String.valueOf(R.string.shared_prefs), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        // Get the current index for the symbol from the shared pref.
        int current = sharedPref.getInt(String.valueOf(R.string.saved_symbol_index), 0);
        current = (current >= SYMBOLS.length - 1) ? 0 : current + 1;

        // Update the index for the symbol.
        editor.putInt(String.valueOf(R.string.saved_symbol_index), current);
        editor.apply();

        // Take the new symbol from the array.
        return SYMBOLS[current % SYMBOLS.length];
    }

    @SuppressLint("SimpleDateFormat")
    private String updateDate() {
        return new SimpleDateFormat("HH:mm dd-MM").format(new Date());
    }

    private void updateWidget(Context context, AppWidgetManager widgetManager, int widgetId) {
        // Update the symbol and time variable.
        String symbol = updateSymbol(context);
        String date = updateDate();

        // Set the variables in the remote view.
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
        views.setTextViewText(R.id.widget_symbol, symbol);
        views.setTextViewText(R.id.widget_date, date);

        // Set the onclick event for the widget.
        Intent intent = new Intent(context, getClass()).setAction(ACTION_WIDGET);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.widget_layout, pendingIntent);

        // Instruct the widget manager to update the widget.
        widgetManager.updateAppWidget(widgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager widgetManager, int[] widgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int widgetId : widgetIds) {
            updateWidget(context, widgetManager, widgetId);
        }
    }

    private void onUpdate(Context context) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName component = new ComponentName(context.getPackageName(), getClass().getName());
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(component);
        onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION_WIDGET)) {
            Log.d(TAG, "The button has been clicked by the widget action.");
            onUpdate(context);
        }
        super.onReceive(context, intent);
    }
}

