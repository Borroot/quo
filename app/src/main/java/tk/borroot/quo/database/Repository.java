package tk.borroot.quo.database;

import android.content.Context;

import java.util.List;

/**
 * This class provides a bridge between the controller and
 * the database.
 *
 * @author Bram Pulles
 */
public class Repository {

    private SymbolDao symbolDao;

    public Repository(Context context) {
        AppDatabase db = AppDatabase.getDatabase(context);
        symbolDao = db.symbolDao();
    }

    public List<Symbol> getSymbols() {
        return symbolDao.getSymbols();
    }

    public void insert(Symbol symbol) {
        symbolDao.insert(symbol);
    }

    public void delete(Symbol symbol) {
        symbolDao.delete(symbol);
    }
}
