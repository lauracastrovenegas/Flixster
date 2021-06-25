package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextClock;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.adapters.CastMemberAdapter;
import com.example.flixster.models.CastMember;
import com.example.flixster.models.Movie;

import org.json.JSONArray;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.Headers;

public class MovieDetailsActivity extends AppCompatActivity {

    private final String API_URL = "https://api.themoviedb.org/3";
    private final String API_KEY = "69c6760b3c91ff75f8ed944213810297";
    private final String TAG = "MovieDetailsActivity";

    Movie movie;
    int movieId;

    TextView tvTitle;
    TextView tvOverview;
    TextView tvDetails;
    TextView tvTagline;
    RatingBar rbVoteAverage;
    ImageView ivBackdrop;

    int runtime;
    String releaseDate;
    String tagline;

    List<CastMember> cast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvOverview = (TextView) findViewById(R.id.tvOverview);
        tvDetails = (TextView) findViewById(R.id.tvDetails);
        tvTagline = (TextView) findViewById(R.id.tvTagline);
        rbVoteAverage = (RatingBar) findViewById(R.id.rbVoteAverage);
        ivBackdrop = (ImageView) findViewById(R.id.ivBackdrop);

        // unwrap movie passed in via Intent, use simple name as key
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));

        movieId = movie.getMovieId();

        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());

        float voteAverage = movie.getVoteAverage().floatValue();
        rbVoteAverage.setRating(voteAverage/2.0f);

        String imageUrl;
        int placeholderUrl = 0;

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            imageUrl = movie.getPosterPath();
            placeholderUrl = R.drawable.flicks_movie_placeholder;
        } else {
            imageUrl = movie.getBackdropPath();
            placeholderUrl = R.drawable.flicks_backdrop_placeholder;
        }

        Glide.with(this)
                .load(imageUrl)
                .placeholder(placeholderUrl)
                .error(R.drawable.flicks_movie_placeholder)
                .into(ivBackdrop);

        // set recycler view
        RecyclerView rvCast = findViewById(R.id.rvCast);
        cast = new ArrayList<>();

        // Create an adapter
        CastMemberAdapter castMemberAdapter = new CastMemberAdapter(this, cast);

        // set the adapter on the recycler view
        rvCast.setAdapter(castMemberAdapter);

        // set layout manager on the recycler view
        rvCast.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        AsyncHttpClient client = new AsyncHttpClient();
        // Fetch extra details for movie

        String MOVIE_DETAILS_URL = API_URL + "/movie/" + movieId + "?api_key=" + API_KEY;
        client.get(MOVIE_DETAILS_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
                JSONObject results = json.jsonObject;
                try {
                    Log.i(TAG, "Results:" + results.toString());
                    runtime = results.getInt("runtime");
                    releaseDate = (String) new SimpleDateFormat("MMM d, yyyy").format(new SimpleDateFormat("yyyy-mm-dd").parse(results.getString("release_date")));
                    if (!results.getString("tagline").equals("")){
                        tagline = String.format("\"%s\"", results.getString("tagline"));
                    } else {
                        tagline = "";
                    }
                    tvDetails.setText(String.format("%s min | %s", runtime, releaseDate));
                    tvTagline.setText(tagline);
                } catch (Exception e) {
                    Log.e(TAG, "JSON Exception", e);
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String s, Throwable throwable) {
                Log.d(TAG, "onFailure");
            }
        });

        // Fetch list of cast and crew
        String CREDITS_URL = API_URL + "/movie/" + movieId + "/credits?api_key=" + API_KEY;
        client.get(CREDITS_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray results = jsonObject.getJSONArray("cast");
                    Log.i(TAG, "Cast:" + results.toString());
                    cast.addAll(CastMember.fromJsonArray(results));
                    castMemberAdapter.notifyDataSetChanged();
                    Log.i(TAG, "Movies:" + cast.size());
                } catch (Exception e) {
                    Log.e(TAG, "JSON Exception", e);
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String s, Throwable throwable) {
                Log.d(TAG, "onFailure");
            }
        });
    }
}