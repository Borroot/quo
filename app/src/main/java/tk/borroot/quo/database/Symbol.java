package tk.borroot.quo.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * This class represents the table which stores the symbols
 * together with their notes.
 *
 * @author Bram Pulles
 */
@Entity(tableName = "symbol_table")
public class Symbol {

    @PrimaryKey
    @NonNull
    private char symbol;
    private String note;

    public Symbol(char symbol, String note) {
        this.symbol = symbol;
        this.note = note;
    }

    public char getSymbol() {
        return symbol;
    }

    public String getNote() {
        return note;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Symbol) {
            return ((Symbol)o).getSymbol() == symbol;
        }
        return false;
    }

    @Override
    public String toString() {
        return symbol + ". " + note;
    }
}
