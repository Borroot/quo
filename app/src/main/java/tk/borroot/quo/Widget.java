package tk.borroot.quo;

import android.annotation.SuppressLint;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Implementation of App Widget functionality.
 */
public class Widget extends AppWidgetProvider {

    private static final String TAG = Widget.class.getSimpleName();
    private static final String[] symbols = {"L", "R"};

    private String updateSymbol(Context context) {
        // Create the shared pref object.
        SharedPreferences sharedPref = context.getSharedPreferences(
                String.valueOf(R.string.shared_prefs), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        // Get the current index for the symbol from the shared pref.
        int current = sharedPref.getInt(String.valueOf(R.string.saved_symbol_index), 0);
        current = (current >= symbols.length - 1) ? 0 : current + 1;

        // Update the index for the symbol.
        editor.putInt(String.valueOf(R.string.saved_symbol_index), current);
        editor.apply();

        // Take the new symbol from the array.
        return symbols[current % symbols.length];
    }

    @SuppressLint("SimpleDateFormat")
    private String updateDate() {
        return new SimpleDateFormat("HH:mm dd-MM").format(new Date());
    }

    private void updateWidget(Context context, AppWidgetManager widgetManager, int widgetId) {

        String symbol = updateSymbol(context);
        String date = updateDate();

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
        views.setTextViewText(R.id.app_widget_symbol, symbol);
        views.setTextViewText(R.id.app_widget_date, date);

        // Instruct the widget manager to update the widget
        widgetManager.updateAppWidget(widgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager widgetManager, int[] widgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int widgetId : widgetIds) {
            updateWidget(context, widgetManager, widgetId);
        }
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

    }
}

