package tk.borroot.quo.controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import tk.borroot.quo.R;
import tk.borroot.quo.database.Repository;
import tk.borroot.quo.database.Symbol;

/**
 * This class provides a bridge between the UI and the logic
 * of the app.
 *
 * @author Bram Pulles
 */
public class Controller {

    private static final String TAG = Controller.class.getSimpleName();

    private static Repository repository;
    private static Controller INSTANCE;

    /**
     * Create the controller with a repository.
     *
     * @param context the context where the controller is made
     */
    private Controller(Context context) {
        repository = new Repository(context);
    }

    /**
     * Get the controller. If there is no controller yet then
     * create the controller.
     *
     * @param context the context where the controller is made
     * @return the instance of the controller
     */
    public static Controller getController(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new Controller(context);
        }
        return INSTANCE;
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

        // Extract the current list of symbols.
        List<Symbol> symbols = getSymbols();
        if (symbols.size() == 0) return '?';

        // Get the current index for the symbol from the shared pref.
        int index = sharedPref.getInt(String.valueOf(R.string.saved_symbol_index), 0);
        index = (index >= symbols.size() - 1) ? 0 : index + 1;

        // Update the index for the symbol.
        editor.putInt(String.valueOf(R.string.saved_symbol_index), index);
        editor.apply();

        // Take the new symbol from the array.
        return symbols.get(index).getSymbol();
    }

    /**
     * Show the current date and time.
     *
     * @return the time in HH:mm dd-MM format
     */
    @SuppressLint("SimpleDateFormat")
    public String currentDate() {
        return new SimpleDateFormat("HH:mm dd-MM").format(new Date());
    }

    /**
     * Add a symbol to the repository.
     *
     * @param symbol to be added
     */
    public void addSymbol(Symbol symbol) {
        repository.insert(symbol);
    }

    /**
     * Get all of the symbols from the repository.
     *
     * @return all of the symbols in the repository
     */
    public List<Symbol> getSymbols() {
        return repository.getSymbols();
    }
}
