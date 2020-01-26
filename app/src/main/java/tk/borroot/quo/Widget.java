package tk.borroot.quo;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

/**
 * Implementation of the Widget functionality.
 *
 * @author Bram Pulles
 */
public class Widget extends AppWidgetProvider {

    private static final Controller controller = Controller.init();
    private static final String ACTION_WIDGET = "tk.borroot.quo.action.APPWIDGET_CLICK";

    /**
     * Cycle to the next symbol on the widget and update the time on the widget.
     *
     * @param context the current context
     * @param widgetManager the widget manager
     * @param widgetId the current widget
     */
    private void updateWidget(Context context, AppWidgetManager widgetManager, int widgetId) {
        // Update the symbol and time variable.
        String symbol = "" + controller.nextSymbol(context, widgetId);
        String date = controller.currentDate();

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
     * Deduce the AppWidgetManager and the widgetId from the context
     * and call updateWidget() with all three arguments.
     *
     * @param context the current context
     * @param action first the action string followed by the widgetId in string form
     */
    private void onClick(Context context, String action) {
        AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);
        int widgetId = Integer.parseInt(action.substring(ACTION_WIDGET.length()));
        updateWidget(context, widgetManager, widgetId);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        String action = intent.getAction();

        if (action != null && action.startsWith(ACTION_WIDGET)) {
            onClick(context, action);
        }
    }
}

