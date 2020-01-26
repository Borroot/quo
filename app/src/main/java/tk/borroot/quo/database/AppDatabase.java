package tk.borroot.quo.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * This class provides a singleton implementation of the database. This
 * database contains all of the tables which are defined.
 *
 * @author Bram Pulles
 */
@Database(entities = {Symbol.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract SymbolDao symbolDao();
    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        synchronized (AppDatabase.class) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, "database").allowMainThreadQueries().build();
            }
        }
        return INSTANCE;
    }
}
