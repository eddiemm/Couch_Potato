package com.example.couchpotato;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MovieProfileFragment extends Fragment {

    private MovieSingleton movieSingleton = MovieSingleton.getInstance();
    private ImageView moviePic;
    private TextView movieTitle;
    private TextView movieDescription;
    private TextView movieRate;
    private RecyclerView recyclerView;
    private String similarMoviesUrl;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.movie_profile_fragment, container, false);

        moviePic = v.findViewById(R.id.contentPosterImageView);
        movieTitle = v.findViewById(R.id.contentTitleTextView);
        movieDescription = v.findViewById(R.id.contentSummaryTextView);
        movieRate = v.findViewById(R.id.reviewScoreTextView);
        recyclerView = v.findViewById(R.id.contentRecyclerView);

        String movieId = movieSingleton.getMovieId();
        MovieModelClass movieModelClass = movieSingleton.getMovieModelClass();

        Log.d("MovieProfileFrag", " Title: " + movieModelClass.getTitle());

        String title = movieModelClass.getTitle() + "";

        movieTitle.setText(title);
        movieDescription.setText(movieModelClass.getDescription() + "");
        movieRate.setText(movieModelClass.getReviewScore() + "");

        Glide.with(this)
                .load("https://image.tmdb.org/t/p/w500" + movieModelClass.getImg2())
                .into(moviePic);


        similarMoviesUrl = "https://api.themoviedb.org/3/movie/" + movieId + "/similar?api_key=4517228c3cc695f9dfa1dcb4c4979152&language=en-US&page=1";
        GetData getData = new GetData();
        getData.execute();
        return v;
    }

    public class GetData extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {

            String current = "";

            try {
                URL url;
                HttpURLConnection urlConnection = null;

                try {
                    url = new URL(similarMoviesUrl);
                    urlConnection = (HttpURLConnection) url.openConnection();

                    InputStream is = urlConnection.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is);

                    int data = isr.read();

                    while (data != -1) {
                        current += (char) data;
                        data = isr.read();

                    }
                    return current;

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if(urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return current;
        }

        @Override
        protected void onPostExecute(String s) {

            try {
                JSONObject jsonObject = new JSONObject(s);

                JSONArray jsonArray = jsonObject.getJSONArray("results");

                List<MovieModelClass> movieList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                    MovieModelClass model = new MovieModelClass();


                    model.setId(jsonObject1.getString("id"));
                    model.setTitle(jsonObject1.getString("title"));
                    model.setImg(jsonObject1.getString("poster_path"));
                    model.setImg2(jsonObject1.getString("backdrop_path"));
                    model.setReviewScore(jsonObject1.getString("vote_average"));
                    model.setDescription(jsonObject1.getString("overview"));


                    movieList.add(model);
                }


                PutDataIntoRecyclerView(movieList);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void PutDataIntoRecyclerView(List<MovieModelClass> movieList) {
        Adaptery adaptery = new Adaptery(this.getContext(), movieList);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this.getContext(), 3);

        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.setAdapter(adaptery);

    }
}