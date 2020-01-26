package tk.borroot.quo.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import static androidx.room.OnConflictStrategy.IGNORE;

/**
 * This class describes the DAO (data access object) for the
 * symbol table. So the queries which can be executed on the
 * table are defined here.
 *
 * @author Bram Pulles
 */
@Dao
public interface SymbolDao {

    @Insert(onConflict = IGNORE)
    void insert(Symbol symbol);

    @Delete
    void delete(Symbol symbol);

    @Query("SELECT * FROM symbol_table")
    List<Symbol> getSymbols();
}
