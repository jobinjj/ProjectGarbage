package sample.com.jobin.msi.projectgarbage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OrganicActivity extends AppCompatActivity {
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    public static NetworkInfo networkInfo;
    ConnectivityManager connectivityManager;
    ProgressBar progressBar2;
    public static ArrayList<Data> List = new ArrayList<>();
    public static String url = "http://searchdeal.online/webtemplate/garbagecollector/garbage_collocsearch.php?";
    private SharedPreferences pref;
    String email;
    private SharedPreferences.Editor editor;
    private RecyclerView recycler;
    private ImageView back2;
    private MoviesAdapter adapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organic);
        initViews();
        getData();
        recycler.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(mLayoutManager);
        adapter2 = new MoviesAdapter(List);
        recycler.setAdapter(adapter2);
    }

    private void getData() {
        if (networkInfo == null){
            Toast.makeText(this, "no network connection", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, email, Toast.LENGTH_SHORT).show();
            JsonArrayRequest request = new JsonArrayRequest(url + "email=" + email + "&category=" + "organic-waste",
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            List.clear();
                            progressBar2.setVisibility(View.GONE);

                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject obj = response.getJSONObject(i);
                                    Data data = new Data();
                                    data.setpriority(obj.getString("priority"));
                                    data.sete_title(obj.getString("description"));
                                    data.setE_street(obj.getString("street"));
                                    data.setCity(obj.getString("city"));
                                    data.setDate(obj.getString("date"));
                                    data.setName(obj.getString("name"));
                                    data.setAddress(obj.getString("address"));
                                    data.setImg_url(obj.getString("image"));
                                    data.setPostid(obj.getString("postid"));
                                    List.add(data);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                            adapter2.notifyDataSetChanged();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            AppController.getInstance().addToRequestQueue(request);

        }

    }
    private void initViews() {
        pref = getApplicationContext().getSharedPreferences("Mypref",MODE_PRIVATE);
        editor = pref.edit();
        editor.apply();
        email = pref.getString("email","");
        connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();
        progressBar2 = findViewById(R.id.progressBar2);
        recycler = findViewById(R.id.recycler);
        back2 = findViewById(R.id.back2);
        back2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder> {

        private java.util.List<Data> moviesList;

        public MoviesAdapter(List<Data> moviesList) {
            this.moviesList = moviesList;
        }

        @Override
        public MoviesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.grid_list_ewaste, parent, false);

            return new MoviesAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MoviesAdapter.MyViewHolder holder, int position) {

            final Data movie = moviesList.get(position);
            holder.e_title.setText(movie.gete_title());
            holder.priority.setText(movie.getpriority());
//            holder.e_street.setText(movie.getE_street());
            holder.thumbnail.setImageUrl(movie.getImg_url(), imageLoader);
            holder.more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(OrganicActivity.this,DetailActivity.class);
                    intent.putExtra("img_url",movie.getImg_url());
                    intent.putExtra("etitle",movie.gete_title());
                    intent.putExtra("priority",movie.getpriority());
                    intent.putExtra("name",movie.getName());
                    intent.putExtra("city",movie.getCity());
                    intent.putExtra("address",movie.getAddress());
                    intent.putExtra("street",movie.getE_street());
                    intent.putExtra("date",movie.getDate());
                    intent.putExtra("post",movie.getPostid());
                    startActivity(intent);


                }
            });
        }

        @Override
        public int getItemCount() {
            return moviesList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            private TextView e_title,priority,e_street;
            private NetworkImageView thumbnail;
            private Button more;


            public MyViewHolder(View view) {
                super(view);
                e_title = (TextView) view.findViewById(R.id.e_title);
                thumbnail = (NetworkImageView) view.findViewById(R.id.e_thumbnail);
                priority = (TextView) view.findViewById(R.id.priority);
                e_street = (TextView) view.findViewById(R.id.e_street);
                more = (Button) view.findViewById(R.id.more);
            }
        }
    }
    public static class Data {
        public  String img_url,e_title,priority,e_street,date,name,address,city,postid;
        public Data(){

        }
        public Data(String img_url, String e_title, String priority, String e_street, String date, String name, String address, String city,String postid)
        {
            this.postid = postid;
            this.city = city;
            this.address = address;
            this.priority = priority;
            this.e_title = e_title;
            this.img_url = img_url;
            this.e_street = e_street;
            this.date = date;
            this.name = name;
        }


        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getPostid() {
            return postid;
        }

        public void setPostid(String postid) {
            this.postid = postid;
        }

        public String getE_street() {
            return e_street;
        }

        public void setE_street(String e_street) {
            this.e_street = e_street;
        }

        public String getImg_url() {
            return img_url;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }

        public String gete_title() {
            return e_title;
        }

        public void sete_title(String e_title) {
            this.e_title = e_title;
        }

        public String getpriority() {
            return priority;
        }

        public void setpriority(String priority) {
            this.priority = priority;
        }
    }
}
