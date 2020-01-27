package tk.borroot.quo.controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

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
     * Look up the current symbol shown by the widget,
     * increase the index (or set to 0) and return the new symbol.
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

        // Get the the current symbol from the shared pref. If '?' is returned
        // then the index will be -1 which becomes 0 aka the first symbol in the list.
        String tmp = sharedPref.getString(String.valueOf(R.string.saved_symbol_current), "?");
        char current = tmp.charAt(0);

        int index = symbols.indexOf(new Symbol(current, ""));
        index = (index >= symbols.size() - 1) ? 0 : index + 1;

        // Set the next symbol.
        char nextSymbol = symbols.get(index).getSymbol();

        // Update the current symbol to the new one.
        editor.putString(String.valueOf(R.string.saved_symbol_current), nextSymbol + "");
        editor.apply();

        return nextSymbol;
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
     * Delete a symbol from the repository.
     *
     * @param symbol to be deleted
     */
    public void deleteSymbol(Symbol symbol) {
        repository.delete(symbol);
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
