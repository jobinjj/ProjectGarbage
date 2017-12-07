package sample.com.jobin.msi.projectgarbage;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class KarangEditProfile extends AppCompatActivity {



    Spinner spinner;
    private ArrayList mSelectedItems;
    private CharSequence[] Jurong_east = {"hello","hello2","hello3","hello4","hello5","hello6","hello7"};
    private CharSequence[] Kampong = {"hello","hello2","hello3","hello4","hello5","hello6","hello7"};
    private CharSequence[] cities_array;
    private String title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_karang_edit_profile);



        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.cities_array, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner = findViewById(R.id.spinner);

        spinner.setAdapter(adapter);
    }
    public void pushData(View view) {
        String selected_city = spinner.getSelectedItem().toString();
        if (selected_city.equals("Jurong_east")){
            cities_array = Jurong_east;
            title = "Jurong_east";
        }
        else if (selected_city.equals("Kampong")){
            cities_array = Kampong;
            title = "Kampong";
        }
        mSelectedItems = new ArrayList();  // Where we track the selected items
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Set the dialog title
        builder.setTitle(title)
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setMultiChoiceItems(cities_array, null,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which,
                                                boolean isChecked) {
                                if (isChecked) {
                                    // If the user checked the item, add it to the selected items
                                    mSelectedItems.add(which);
                                } else if (mSelectedItems.contains(which)) {
                                    // Else, if the item is already in the array, remove it
                                    mSelectedItems.remove(Integer.valueOf(which));
                                }
                            }
                        })
//                // Set the action buttons
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK, so save the mSelectedItems results somewhere
                        // or return them to the component that opened the dialog

                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();

        }

}
