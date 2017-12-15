package sample.com.jobin.msi.projectgarbage;

import android.content.Context;
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

import sample.com.jobin.msi.projectgarbage.user.UserAddListing;

public class UserDetailActivity extends AppCompatActivity {
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    public static NetworkInfo networkInfo;
    ConnectivityManager connectivityManager;
    public static ArrayList<Data> MovieList = new ArrayList<>();
    public static String url = "http://searchdeal.online/webtemplate/garbagecollector/garbage_vulistingclick.php?";
    private SharedPreferences pref;
    private Button back2;
    public MoviesAdapter adapter2;
    TextView date,description,priority;
    private SharedPreferences.Editor editor;
    private String str_date,str_description,image,str_priority,postid,email;
    private NetworkImageView networkImageView;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        Bundle extras = getIntent().getExtras();
        str_date = extras.getString("date",null);
        str_description = extras.getString("description",null);
        image = extras.getString("image",null);
        str_priority = extras.getString("priority",null);
        postid = extras.getString("postid",null);

        pref = getApplicationContext().getSharedPreferences("Mypref",MODE_PRIVATE);
        editor = pref.edit();
        editor.apply();
        email = pref.getString("email",null);
        initViews();
        fetchData();
        
    }

    private void initViews() {
        back2 = findViewById(R.id.back2);
        back2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();
        recyclerView = (RecyclerView)findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager;
        mLayoutManager = new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recyclerView.setLayoutManager(mLayoutManager);
        adapter2 = new MoviesAdapter(MovieList);
        recyclerView.setAdapter(adapter2);
        date = findViewById(R.id.date);
        priority = findViewById(R.id.priority);
        description = findViewById(R.id.description);
        networkImageView = findViewById(R.id.imgurl);
        networkImageView.setImageUrl(image,imageLoader);
        date.setText(str_date);
        description.setText(str_description);
        priority.setText(str_priority);

    }

    private void fetchData() {

        if (networkInfo == null){
            MovieList.clear();
            recyclerView.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "no network connection", Toast.LENGTH_SHORT).show();
        }
        else {

            JsonArrayRequest request = new JsonArrayRequest(url + "postid=" + postid,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            MovieList.clear();
//                            progressBar.setVisibility(View.GONE);

                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject obj = response.getJSONObject(i);
                                    Data data = new Data();
                                    data.setDate(obj.getString("date"));
                                    data.setDescription(obj.getString("description"));
                                    data.setCname(obj.getString("cname"));
                                    data.setBid(obj.getString("bid"));
                                    MovieList.add(data);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                adapter2.notifyDataSetChanged();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            AppController.getInstance().addToRequestQueue(request);

        }

    }

    public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder> {

        private List<Data> moviesList;

        public MoviesAdapter(List<Data> moviesList) {
            this.moviesList = moviesList;
        }

        @Override
        public MoviesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.bid_list, parent, false);

            return new MoviesAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MoviesAdapter.MyViewHolder holder, int position) {

            final Data movie = moviesList.get(position);
            holder.bid.setText(movie.getBid());
            holder.col_name.setText(movie.getCname());
//            holder.thumbnail.setImageUrl(movie.getImg_url(), imageLoader);
        }

        @Override
        public int getItemCount() {
            return moviesList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            private TextView col_name,bid;
//            private NetworkImageView thumbnail;


            public MyViewHolder(View view) {
                super(view);
                col_name = (TextView) view.findViewById(R.id.col_name);
                bid = (TextView) view.findViewById(R.id.bid);

            }
        }
    }

    public static class Data {
        public  String img_url,description,date,priority,postid,cname,bid;
        public Data(){

        }
        public Data(String img_url,String description,String date,String priority, String postid,String cname,String bid){
            this.bid = bid;
            this.cname = cname;
            this.postid = postid;
            this.date = date;
            this.description = description;
            this.img_url = img_url;
            this.priority = priority;
        }

        public String getCname() {

            return cname;
        }

        public void setCname(String cname) {
            this.cname = cname;
        }

        public String getBid() {
            return bid;
        }

        public void setBid(String bid) {
            this.bid = bid;
        }

        public String getPriority() {
            return priority;
        }

        public void setPriority(String priority) {
            this.priority = priority;
        }

        public String getPostid() {
            return postid;
        }

        public void setPostid(String postid) {
            this.postid = postid;
        }

        public String getImg_url() {
            return img_url;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }
}
