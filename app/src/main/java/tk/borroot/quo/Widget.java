package tk.borroot.quo;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
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
 *
 * @author Bram Pulles
 */
public class Widget extends AppWidgetProvider {

    private static final String TAG = Widget.class.getSimpleName();

    private static final String ACTION_WIDGET = "tk.borroot.quo.action.APPWIDGET_CLICK";
    private static final String[] SYMBOLS = {"L", "R"};

    /**
     * Look up the current index of the symbol shown by the widget,
     * increase this value (or set to 0) and return the new symbol.
     *
     * @param context the current context
     * @return the next symbol from the SYMBOLS array
     */
    private String updateSymbol(Context context) {
        // Create the shared pref object. // TODO: give all widgets their own shared pref
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

    /**
     * Show the current time.
     *
     * @return the time in HH:mm dd-MM format
     */
    @SuppressLint("SimpleDateFormat")
    private String updateDate() {
        return new SimpleDateFormat("HH:mm dd-MM").format(new Date());
    }

    /**
     * Cycle to the next symbol on the widget and update the time.
     *
     * @param context the current context
     * @param widgetManager the widget manager
     * @param widgetId the current widget
     */
    private void updateWidget(Context context, AppWidgetManager widgetManager, int widgetId) {
        // Update the symbol and time variable.
        String symbol = updateSymbol(context);
        String date = updateDate();

        // Set the variables in the remote view.
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
        views.setTextViewText(R.id.widget_symbol, symbol);
        views.setTextViewText(R.id.widget_date, date);

        // Set the onclick event for the widget. The action is the action string plus the widgetId.
        Intent intent = new Intent(context, getClass()).setAction(ACTION_WIDGET + widgetId);
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

    /**
     * Deduce the AppWidgetManager and the widgetIds from the context
     * and call onUpdate() with all three arguments.
     *
     * @param context the current context
     * @param action first the action string followed by the widgetId in string form
     */
    private void onUpdate(Context context, String action) {
        AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);
        int widgetId = Integer.parseInt(action.substring(ACTION_WIDGET.length()));
        updateWidget(context, widgetManager, widgetId);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (intent != null) {
            String action = intent.getAction();

            if (action != null && action.startsWith(ACTION_WIDGET)) {
                onUpdate(context, action);
            }
        }
    }
}

