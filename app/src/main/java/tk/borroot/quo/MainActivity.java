package tk.borroot.quo;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/**
 * The main activity, here the symbols with their notes
 * can be seen, added and removed.
 *
 * @author Bram Pulles
 */
public class MainActivity extends AppCompatActivity {

    private static Controller controller = Controller.init();

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

            char symbol = view_symbol.getText().charAt(0);
            String note = String.valueOf(view_note.getText());
            controller.addSymbol(symbol, note);
        });
        alert.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the layout of the view.
        setContentView(R.layout.activity_main);
        setSupportActionBar(findViewById(R.id.toolbar));

        // Set the onclick listener for the plus button.
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener((View v) -> onAdd());
    }
}
