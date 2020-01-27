package tk.borroot.quo.view;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import tk.borroot.quo.controller.Controller;
import tk.borroot.quo.R;
import tk.borroot.quo.database.Symbol;

/**
 * The main activity, here the symbols with their notes
 * can be seen, added and removed.
 *
 * @author Bram Pulles
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private Controller controller;

    /**
     * Add a new symbol. The symbol and the optional note are
     * retrieved by using an alert dialog.
     */
    private void onAdd() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);

        final LayoutInflater inflater = this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_add, null);
        alert.setView(view);

        alert.setTitle(R.string.dialog_add_title);
        alert.setNegativeButton(android.R.string.cancel, null);
        alert.setPositiveButton(android.R.string.ok, (arg0, arg1) -> {
            EditText view_symbol = view.findViewById(R.id.dialog_add_symbol);
            EditText view_note = view.findViewById(R.id.dialog_add_note);

            // Take the symbol and the note from the fields with user input.
            String text = String.valueOf(view_symbol.getText());
            if (text.equals("")) return;
            char ch = text.charAt(0);
            String note = String.valueOf(view_note.getText());
            Symbol symbol = new Symbol(ch, note);

            // Add the symbol to the database and draw it on the screen.
            controller.addSymbol(symbol);
            onDraw();
        });
        alert.show();
    }

    /**
     * Draw all of the entries from the database.
     */
    private void onDraw() {
        List<Symbol> symbols = controller.getSymbols();

        // Create the layout where all of the entries will be shown and clear it.
        LinearLayout layout = findViewById(R.id.main_content);
        LayoutInflater inflater = this.getLayoutInflater();
        layout.removeAllViews();

        for (Symbol symbol : symbols) {
            // Create the view for one entry.
            View view = inflater.inflate(R.layout.main_entry, layout, false);
            TextView viewSymbol = view.findViewById(R.id.entry_symbol);
            TextView viewNote = view.findViewById(R.id.entry_note);
            ImageButton buttonDelete = view.findViewById(R.id.entry_button_delete);

            // Set the text in the view.
            viewSymbol.setText(String.format("%s", symbol.getSymbol()));
            viewNote.setText(symbol.getNote());

            // Set the onclick for deleting an item.
            buttonDelete.setOnClickListener((View v) -> {
                controller.deleteSymbol(symbol);
                onDraw();
            });

            // Add the view to the main content view.
            layout.addView(view);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        controller = Controller.getController(this);

        // Set the layout of the view.
        setContentView(R.layout.main_activity);
        setSupportActionBar(findViewById(R.id.toolbar));

        // Set the onclick listener for the plus button.
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener((View v) -> onAdd());

        // Draw all of the symbols with their notes.
        onDraw();
    }
}
