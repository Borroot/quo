package tk.borroot.quo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Controller {

    public static Controller controller;
    private static final char[] SYMBOLS = {'L', 'M', 'R'};

    private Controller() {
    }

    public static Controller init() {
        if (controller == null) {
            controller = new Controller();
        }
        return controller;
    }

    public void addSymbol(char symbol, String note) {

    }

    /**
     * Look up the current index of the symbol shown by the widget,
     * increase this value (or set to 0) and return the new symbol.
     * The symbol will always be exactly one character.
     *
     * @param context the current context
     * @return the next symbol from the SYMBOLS array
     */
    public char nextSymbol(Context context, int widgetId) {
        // Create the shared pref object.
        SharedPreferences sharedPref = context.getSharedPreferences(
                String.valueOf(R.string.shared_prefs) + widgetId, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        // Get the current index for the symbol from the shared pref.
        int index = sharedPref.getInt(String.valueOf(R.string.saved_symbol_index), 0);
        index = (index >= SYMBOLS.length - 1) ? 0 : index + 1;

        // Update the index for the symbol.
        editor.putInt(String.valueOf(R.string.saved_symbol_index), index);
        editor.apply();

        // Take the new symbol from the array.
        return SYMBOLS[index % SYMBOLS.length];
    }

    /**
     * Show the current time.
     *
     * @return the time in HH:mm dd-MM format
     */
    @SuppressLint("SimpleDateFormat")
    public String currentDate() {
        return new SimpleDateFormat("HH:mm dd-MM").format(new Date());
    }
}
