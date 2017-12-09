package sample.com.jobin.msi.projectgarbage;

import android.*;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class KarangEditProfile extends AppCompatActivity {



    Spinner spinner;
    private ArrayList<Integer> mSelectedItems;
    String[] listitems;
    boolean[] checkedItems;
    private String title,str_latitude,str_longitude;
    Uri picuri,picuri2;
    Bitmap bitmap,bitmap2;
    Button submit;
    ImageView id;
    public static  final int PICK_IMAGE_REQUEST = 1;
    public static  final int PICK_IMAGE_REQUEST2 = 2;
    public static  final int PLACE_PICKER_REQUEST = 3;
    String url1 = "http://searchdeal.online/webtemplate/garbagecollector/garbage_acprofile.php?";
    String url2 = "http://searchdeal.online/webtemplate/garbagecollector/garabge_colprofimage.php?";
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private String email;
    private String item;
    private ImageView back2;
    private EditText ed_address;
    private String str_address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_karang_edit_profile);

        ed_address = findViewById(R.id.ed_address);

        submit = findViewById(R.id.submit);
        back2 = findViewById(R.id.back2);
        back2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        pref = getApplicationContext().getSharedPreferences("Mypref",MODE_PRIVATE);
        editor = pref.edit();
        editor.apply();
        email = pref.getString("email",null);
        id = findViewById(R.id.id);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.cities_array, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner = findViewById(R.id.spinner);

        spinner.setAdapter(adapter);

        addListenerOnSpinnerItemSelection();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pushData();
                profileimgUpload(bitmap);
                doorimgUpload(bitmap2);
            }
        });
    }

    private void profileimgUpload(final Bitmap bitmap) {
        NetworkInfo netInfo;
        ConnectivityManager conMgr = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo == null){
            Toast.makeText(getApplicationContext(), "no internet connection", Toast.LENGTH_SHORT).show();
        }
        else{
            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url2 + "email=" + email,
                    new Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response) {

                            try {
                                JSONObject obj = new JSONObject(new String(response.data));

                                Toast.makeText(getApplicationContext(), obj.getString("status"), Toast.LENGTH_SHORT).show();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }) {

                @Override
                protected Map<String, DataPart> getByteData() {
                    Map<String, DataPart> params = new HashMap<>();
                    long imagename = System.currentTimeMillis();
                    params.put("picp", new DataPart(imagename + "img" + ".png", getFileDataFromDrawable(bitmap)));
                    return params;
                }


            };

            Volley.newRequestQueue(this).add(volleyMultipartRequest);
        }

    }

    private void doorimgUpload(final Bitmap bitmap2){
        NetworkInfo netInfo;
        ConnectivityManager conMgr = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo == null){
            Toast.makeText(getApplicationContext(), "no internet connection", Toast.LENGTH_SHORT).show();
        }
        else{
            //our custom volley request
            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url2 + "email=" + email,
                    new Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response) {

                            try {
                                JSONObject obj = new JSONObject(new String(response.data));

                                Toast.makeText(getApplicationContext(), obj.getString("status"), Toast.LENGTH_SHORT).show();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }) {

                @Override
                protected Map<String, DataPart> getByteData() {
                    Map<String, DataPart> params = new HashMap<>();
                    long imagename = System.currentTimeMillis();
                    params.put("picd", new DataPart(imagename + "img" + ".png", getFileDataFromDrawable(bitmap2)));
                    return params;
                }


            };

            Volley.newRequestQueue(this).add(volleyMultipartRequest);
        }

    }
    private void pushData() {

        NetworkInfo netInfo;
        ConnectivityManager conMgr = (ConnectivityManager) KarangEditProfile.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo == null){
            Toast.makeText(KarangEditProfile.this, "no internet connection", Toast.LENGTH_SHORT).show();
        }
        else
        {
            str_address = ed_address.getText().toString();
            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url1 +
                    "&email=" + email + "&street=" + item + "&latitude=" + "9.9559" + "&longitude=" + "76.2899" + "&address=" + str_address,
                    new Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response) {

                            try {
                                JSONObject obj = new JSONObject(new String(response.data));

                                Toast.makeText(getApplicationContext(), obj.getString("update"), Toast.LENGTH_SHORT).show();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }) {

//                @Override
//                protected Map<String, DataPart> getByteData() {
//                    Map<String, DataPart> params = new HashMap<>();
//                    long imagename = System.currentTimeMillis();
//                    params.put("pic", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
//                    return params;
//                }
            };

            Volley.newRequestQueue(KarangEditProfile.this).add(volleyMultipartRequest);


        }
    }

    private void addListenerOnSpinnerItemSelection() {
        spinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }



    public void imageBrowse(View view) {
        if (android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            // only for gingerbread and newer versions


        }
        else {

            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (shouldShowRequestPermissionRationale(
                        android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    // Explain to the user why we need to read the contacts
                }

                requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);

                // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
                // app-defined int constant that should be quite unique

                return;
            }
        }

        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent,PICK_IMAGE_REQUEST);
    }




    public void fetchProfile(View view) {
        if (android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            // only for gingerbread and newer versions


        }
        else {

            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (shouldShowRequestPermissionRationale(
                        android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    // Explain to the user why we need to read the contacts
                }

                requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);

                // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
                // app-defined int constant that should be quite unique

                return;
            }
        }

        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent,PICK_IMAGE_REQUEST2);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == PICK_IMAGE_REQUEST){
                picuri = data.getData();

                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), picuri);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
//                    id.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                }

//                displaying selected image to imageview
//                imageView.setImageBitmap(bitmap);
//                filepath = getPath(picuri);
//                imageView.setImageURI(picuri);

            }
            if (requestCode == PICK_IMAGE_REQUEST2){
                picuri2 = data.getData();
                try {
                    bitmap2 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), picuri2);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap2.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);


                } catch (IOException e) {
                    e.printStackTrace();
                }

//                displaying selected image to imageview
//                imageView.setImageBitmap(bitmap);
//                filepath = getPath(picuri);
//                imageView.setImageURI(picuri);
            }
            if (requestCode == PLACE_PICKER_REQUEST) {

                Place place = PlacePicker.getPlace(data, this);
                StringBuilder stBuilder = new StringBuilder();
                String placename = String.format("%s", place.getName());
                String latitude = String.valueOf(place.getLatLng().latitude);
                String longitude = String.valueOf(place.getLatLng().longitude);
                str_latitude= latitude;
                str_longitude= longitude;
                Toast.makeText(this, str_latitude + str_longitude, Toast.LENGTH_SHORT).show();
            }

        }
    }
    public  void shwDialog(String title){
        mSelectedItems = new ArrayList();  // Where we track the selected items
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Set the dialog title
        builder.setTitle(title)
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setMultiChoiceItems( listitems, checkedItems,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which,
                                                boolean isChecked) {
                                if (isChecked) {
                                    if(! mSelectedItems.contains(which)) {
                                        // If the user checked the item, add it to the selected items
                                        mSelectedItems.add(which);

                                    }

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
                        item = "";
                        for (int i = 0;i < mSelectedItems.size(); i++){
                            item = item + listitems[mSelectedItems.get(i)];

                            if(i !=mSelectedItems.size() - 1){
                                item = item + ", ";

                            }
                        }
                        Toast.makeText(KarangEditProfile.this, item, Toast.LENGTH_SHORT).show();

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

    private class CustomOnItemSelectedListener implements android.widget.AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {

            String selected_city = parent.getItemAtPosition(pos).toString();
            if (selected_city.equals("Jurong east")){
                listitems = getResources().getStringArray(R.array.Jurong_east);
                checkedItems = new boolean[listitems.length];
                title = "Jurong_east";
                shwDialog(title);


            }
            else if (selected_city.equals("Kampong")){
                listitems = getResources().getStringArray(R.array.Kampong);
                checkedItems = new boolean[listitems.length];
                title = "Kampong";
                shwDialog(title);
            }


        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }
    public void startUpdatesButtonHandler(View view) {

        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(KarangEditProfile.this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
//        if (!mRequestingLocationUpdates) {
//            mRequestingLocationUpdates = true;
//            setButtonsEnabledState();
//            startLocationUpdates();
//        }
    }
    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

}

