package sample.com.jobin.msi.projectgarbage;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
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
import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity {
    Bitmap bitmap,bitmap2;

    private RelativeLayout rl_profile,btn_doorimage,bt_chooseloc;
    Uri picuri,picuri2;
    private EditText city,age,Address,street,ed_from,ed_to,dayed_from,dayed_to;
    private String user_city,user_sex,user_age,user_address,user_street,from,to,str_dayed_to,str_dayed_from,email,str_latitude,str_longitude;
    private String url = "http://searchdeal.online/webtemplate/garbagecollector/garbage_vaprofile.php?";
    private String url3 = "http://searchdeal.online/webtemplate/garbagecollector/garbage_addprofimage.php?";
    private String url2 = "http://searchdeal.online/webtemplate/garbagecollector/garbage_adddoorimage.php?";
    public static  final int PICK_IMAGE_REQUEST = 1;
    public static  final int PICK_IMAGE_REQUEST2 = 2;
    public static  final int PLACE_PICKER_REQUEST = 3;
    Button submit;
    private ImageView img_profile,back;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        initViews();
    }

    public void startUpdatesButtonHandler(View view) {

        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(EditProfile.this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
//        if (!mRequestingLocationUpdates) {
//            mRequestingLocationUpdates = true;
//            setButtonsEnabledState();
//            startLocationUpdates();
//        }
    }


    private void initViews() {
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        back = findViewById(R.id.back2);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.radioLakiLaki) {
                    user_sex = "male";
                } else {
                    user_sex = "female";
                }
            }
        });


        pref = getApplicationContext().getSharedPreferences("Mypref",MODE_PRIVATE);
        editor = pref.edit();
        editor.apply();
        img_profile = (ImageView)findViewById(R.id.img_profile);

        email = pref.getString("email",null);
        rl_profile = findViewById(R.id.rl_profile);
        btn_doorimage = findViewById(R.id.btn_doorimage);
        bt_chooseloc = findViewById(R.id.bt_chooseloc);
        city = findViewById(R.id.city);
        age = findViewById(R.id.age);
        Address = findViewById(R.id.Address);
        street = findViewById(R.id.street);
        ed_from = findViewById(R.id.ed_from);
        ed_to = findViewById(R.id.ed_to);
        dayed_from = findViewById(R.id.dayed_from);
        dayed_to = findViewById(R.id.ed_to);
        submit = findViewById(R.id.btn_submit);
        rl_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileimageBrowse();
            }
        });

        btn_doorimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doorimageBrowse();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user_city = city.getText().toString();

                user_age = age.getText().toString();
                user_address = Address.getText().toString();
                user_street = street.getText().toString();
                from = ed_from.getText().toString();
                to = ed_to.getText().toString();
                str_dayed_from = dayed_from.getText().toString();
                str_dayed_to = dayed_to.getText().toString();

                pushData();
                profileimgUpload(bitmap);
                doorimgUpload(bitmap2);
            }

            private void pushData() {
                NetworkInfo netInfo;
                ConnectivityManager conMgr = (ConnectivityManager) EditProfile.this.getSystemService(Context.CONNECTIVITY_SERVICE);
                netInfo = conMgr.getActiveNetworkInfo();
                if (netInfo == null){
                    Toast.makeText(EditProfile.this, "no internet connection", Toast.LENGTH_SHORT).show();
                }
                else
                {

                    //our custom volley request
                    VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url + " age=" + user_age + "&sex=" + user_sex + "&address=" + user_address +
                            "&email=" + email + "&street=" + user_street + "&city=" + user_city + "&latitude=" + str_latitude + "&longitude=" + str_longitude + "&preferredday=" + str_dayed_from + " to " + str_dayed_to + "&preferredtime=" + from + " to " + to ,
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

                    Volley.newRequestQueue(EditProfile.this).add(volleyMultipartRequest);


                }

            }
        });
    }
    @SuppressLint("NewApi")
    private void doorimageBrowse() {
        if (android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            // only for gingerbread and newer versions


        }
        else {

            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (shouldShowRequestPermissionRationale(
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    // Explain to the user why we need to read the contacts
                }

                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);

                // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
                // app-defined int constant that should be quite unique

                return;
            }
        }

        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent,PICK_IMAGE_REQUEST2);
    }

    @SuppressLint("NewApi")
    private void profileimageBrowse() {
        if (android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            // only for gingerbread and newer versions


        }
        else {

            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (shouldShowRequestPermissionRationale(
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    // Explain to the user why we need to read the contacts
                }

                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);

                // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
                // app-defined int constant that should be quite unique

                return;
            }
        }

        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent,PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == PICK_IMAGE_REQUEST){
                picuri = data.getData();
                String str_picuri = picuri.toString();

                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), picuri);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);

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
            }
        }
    }



    private void doorimgUpload(final Bitmap bitmap2) {
        NetworkInfo netInfo;
        ConnectivityManager conMgr = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo == null){
            Toast.makeText(getApplicationContext(), "no internet connection", Toast.LENGTH_SHORT).show();
        }
        else{

            //our custom volley request
            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url3 + "email=" + email,
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





    private void profileimgUpload(final Bitmap bitmap) {
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
                    params.put("picp", new DataPart(imagename + "img" + ".png", getFileDataFromDrawable(bitmap)));
                    return params;
                }


            };

            Volley.newRequestQueue(this).add(volleyMultipartRequest);

        }

    }
    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }


}
