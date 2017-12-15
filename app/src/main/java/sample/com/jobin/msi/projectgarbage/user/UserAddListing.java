package sample.com.jobin.msi.projectgarbage.user;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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

import sample.com.jobin.msi.projectgarbage.AddListingActivity;
import sample.com.jobin.msi.projectgarbage.AppController;
import sample.com.jobin.msi.projectgarbage.Home.UserHome;
import sample.com.jobin.msi.projectgarbage.Login.UserLogin;
import sample.com.jobin.msi.projectgarbage.R;
import sample.com.jobin.msi.projectgarbage.UserDetailActivity;

import static android.content.Context.MODE_PRIVATE;

public class UserAddListing extends Fragment {
    RecyclerView recyclerView;
    public MoviesAdapter adapter2;
    String email;
    public static ProgressBar progressBar;
    TextView txt_addlistings;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    public static NetworkInfo networkInfo;
    ConnectivityManager connectivityManager;
    public static ArrayList<Data> MovieList = new ArrayList<>();
    public static String url = "http://searchdeal.online/webtemplate/garbagecollector/garbage_vulisting.php?";
    Button logout;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_useraddlistings, container, false);


        recyclerView = (RecyclerView)view.findViewById(R.id.recycler);

        pref = getActivity().getSharedPreferences("Mypref",MODE_PRIVATE);
        editor = pref.edit();
        editor.apply();
        email = pref.getString("email",null);
        logout = (Button) view.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                editor.remove("isLoggedin").apply();
                Toast.makeText(getActivity(), "succesfully logged out", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), UserLogin.class);
                startActivity(intent);
                getActivity().finish();
            }

        });

        txt_addlistings = (TextView) view.findViewById(R.id.txt_addlistings);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),AddListingActivity.class);
                startActivity(intent);
            }
        });
        connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        fetchData();
        TextView desc = (TextView) view.findViewById(R.id.desc);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        adapter2 = new MoviesAdapter(MovieList);
        recyclerView.setAdapter(adapter2);
        return view;
    }

    private void fetchData() {

        if (networkInfo == null){
            MovieList.clear();
            recyclerView.setVisibility(View.GONE);
            Toast.makeText(getActivity(), "no network connection", Toast.LENGTH_SHORT).show();
        }
        else {

            JsonArrayRequest request = new JsonArrayRequest(url + "email=" + email,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            MovieList.clear();
                            progressBar.setVisibility(View.GONE);

                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject obj = response.getJSONObject(i);
                                    Data data = new Data();
                                    data.setDate(obj.getString("date"));
                                    data.setDescription(obj.getString("description"));
                                    data.setImg_url(obj.getString("image"));
                                    data.setPriority(obj.getString("priority"));
                                    data.setPostid(obj.getString("postid"));
                                    System.out.println(obj.getString("postid"));
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
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.grid_list_category, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {

            final Data movie = moviesList.get(position);
            holder.desc.setText(movie.getDescription());
            holder.date.setText(movie.getDate());
            holder.thumbnail.setImageUrl(movie.getImg_url(), imageLoader);
            holder.thumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), UserDetailActivity.class);
                    intent.putExtra("date",movie.getDate());
                    intent.putExtra("description",movie.getDescription());
                    intent.putExtra("image",movie.getImg_url());
                    intent.putExtra("priority",movie.getPriority());
                    intent.putExtra("postid",movie.getPostid());
                    startActivity(intent);
                }
            });
            holder.more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), UserDetailActivity.class);
                    intent.putExtra("date",movie.getDate());
                    intent.putExtra("description",movie.getDescription());
                    intent.putExtra("image",movie.getImg_url());
                    intent.putExtra("priority",movie.getPriority());
                    intent.putExtra("postid",movie.getPostid());
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return moviesList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            private TextView desc,date;
            private NetworkImageView thumbnail;
            private  Button more;


            public MyViewHolder(View view) {
                super(view);
                desc = (TextView) view.findViewById(R.id.description);
                thumbnail = (NetworkImageView) view.findViewById(R.id.thumbnail1);
                date = (TextView) view.findViewById(R.id.date);
                more = (Button) view.findViewById(R.id.more);

            }
        }
    }

    /**
     * Created by msi on 12/1/2017.
     */
    
    public static class Data {
        public  String img_url,description,date,priority,postid;
        public Data(){

        }
        public Data(String img_url,String description,String date,String priority, String postid){
            this.postid = postid;
            this.date = date;
            this.description = description;
            this.img_url = img_url;
            this.priority = priority;
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
