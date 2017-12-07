package sample.com.jobin.msi.projectgarbage;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UploadActivity extends AppCompatActivity {

    private EditText name,description,title;
    private TextInputLayout edtest1,edtest2,edtest3;
    private ImageView imageView,cameraPick;
    private String photoPath;
    private Uri fileUri; // file url to store image/video
    private Button btnChoose, btnUpload;
    String str_name,str_title,str_description;
    public static String url = "http://searchdeal.online/webtemplate/garbagecollector/garbage_collection_upload.php?";
    public static  final int PICK_IMAGE_REQUEST = 1;
        public static  final int CAMERA_REQUEST = 200;
    Bitmap bitmap;
    Uri picuri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        initViews();
    }

    private void initViews() {
        btnChoose = (Button) findViewById(R.id.button_choose);
        btnUpload = (Button) findViewById(R.id.button_upload);
        imageView = (ImageView) findViewById(R.id.imageView);
        name = (EditText) findViewById(R.id.name);
        description = (EditText) findViewById(R.id.description);
        title = (EditText) findViewById(R.id.titleed);
        edtest1 = (TextInputLayout) findViewById(R.id.edtest1);
        edtest2 = (TextInputLayout) findViewById(R.id.edtest2);
        edtest3 = (TextInputLayout) findViewById(R.id.edtest3);
        cameraPick = (ImageView) findViewById(R.id.camerPick);
        
        cameraPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    captureImage();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageBrowse();
            }
        });
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bitmap!= null){
                    imageUpload(bitmap);
                }
                else{
                    Toast.makeText(UploadActivity.this, "no image selected", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void captureImage() throws IOException {
        Toast.makeText(this, "camera started or not", Toast.LENGTH_SHORT).show();
        CameraPhoto cameraPhoto = new CameraPhoto(getApplicationContext());
        Intent in = cameraPhoto.takePhotoIntent();
        startActivityForResult(in, CAMERA_REQUEST);
    }


    @SuppressLint("NewApi")
    private void imageBrowse() {
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
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent,PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == PICK_IMAGE_REQUEST){
                picuri = data.getData();
                try {

                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), picuri);
                    imageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //displaying selected image to imageview
//                imageView.setImageBitmap(bitmap);
//                filepath = getPath(picuri);
//                imageView.setImageURI(picuri);
            }
        }

    }

    private String getPath(Uri picuri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(getApplicationContext(),    picuri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return  result;
    }
    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
    private void imageUpload(final Bitmap bitmap) {
        NetworkInfo netInfo;
        ConnectivityManager conMgr = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        netInfo = conMgr.getActiveNetworkInfo();




        if (netInfo == null){
            Toast.makeText(getApplicationContext(), "no internet connection", Toast.LENGTH_SHORT).show();
        }
        else{



            str_name = name.getText().toString();
            str_description = description.getText().toString();
            str_title = title.getText().toString();
            boolean validationerror = false;
            if (TextUtils.isEmpty(str_name)) {

                edtest1.setError("Please Enter Name");
                name.setError("Please Enter Name");
                name.requestFocus();
                validationerror = true;

            } else {

                edtest1.setError(null);
                name.setError(null);
                validationerror = false;
            }

            if (TextUtils.isEmpty(str_title)) {

                edtest2.setError("Please Enter Title");
                title.setError("Please Enter Title");
                title.requestFocus();
                validationerror = true;

            } else {

                edtest2.setError(null);
                edtest2.setError(null);
                validationerror = false;
            }

            if (TextUtils.isEmpty(str_description)) {

                edtest3.setError("Please Enter Description");
                description.setError("Please Enter Description");
                description.requestFocus();
                validationerror = true;

            } else {

                edtest3.setError(null);
                edtest3.setError(null);
                validationerror = false;
            }
            //our custom volley request
            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url + "name=" + str_name + "&title=" + str_title +"&description=" + str_description,
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
                    params.put("pic", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                    return params;
                }
            };

            Volley.newRequestQueue(this).add(volleyMultipartRequest);

        }

    }



}
