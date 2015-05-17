package gridimagesearch.codepath.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import gridimagesearch.codepath.adapters.ImageResultsAdapter;
import gridimagesearch.codepath.dialogfragment.SettingsFragment;
import gridimagesearch.codepath.models.ImageResult;
import gridimagesearch.codepath.models.R;
import gridimagesearch.codepath.scrolllistener.EndlessScrollListener;

import static android.widget.AdapterView.OnItemClickListener;


public class SearchActivity extends ActionBarActivity {
    private EditText etQuery;
    private GridView gvResults;
    ArrayList<ImageResult> imageResults;
    private ImageResultsAdapter aImageAdapter;
    private String imageSize;
    String[] imageSizes = new String[] {
            "any", "small", "medium", "large", "xlarge"
    };

    public void setSize(String text){
        imageSize = text;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_search);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        etQuery = (EditText) findViewById(R.id.etQuery);
        gvResults = (GridView) findViewById(R.id.gvResults);
        imageResults = new ArrayList<ImageResult>();
        aImageAdapter = new ImageResultsAdapter(this,imageResults);
        //link the adapter to the adapterview(gridview)
        gvResults.setAdapter(aImageAdapter );
        gvResults.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(SearchActivity.this, ImageDisplayActivity.class);
                ImageResult result = imageResults.get(position);
                i.putExtra("image_result", result);   //either be serializable or parcelable
                startActivity(i);
            }
        });

       // Attach the listener to the AdapterView onCreate
        gvResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                customLoadMoreDataFromApi(page, totalItemsCount);
                // or customLoadMoreDataFromApi(totalItemsCount);
            }
        });
        imageSize = "any";
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //display popup to show advanced filters
            //Toast.makeText(this,"fff",Toast.LENGTH_SHORT).show();
            SettingsFragment myfrag = new SettingsFragment();
            myfrag.show(getFragmentManager(),"Diag");

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    //fire when search button is pressed
    public void onImageSearch(View view) {
        String query = etQuery.getText().toString();
        if (!isNetworkAvailable()) {
            Toast.makeText(this, "Please connect to the network and try again!", Toast.LENGTH_SHORT).show();
            return;
        }
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=" + query + "&rsz=8" ;
        if(imageSize!="" && imageSize != "any")
             url += "&imgsz=" + imageSize;
        client.get(url,null,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("DEBUG",response.toString());
                try {
                    JSONArray imageResultsJSON = response.getJSONObject("responseData").getJSONArray("results");
                    imageResults.clear(); //clear existing images from the array (in case its a new search)
                    //when u add to adapter it modifies underlying data
                    aImageAdapter.addAll(ImageResult.fromJSONArray(imageResultsJSON));
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getBaseContext(), "Error while connecting to server!", Toast.LENGTH_SHORT).show();
                }
                Log.d("INFO",imageResults.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }



    // Append more data into the adapter
    public void customLoadMoreDataFromApi(int page,int offset) {
        // This method probably sends out a network request and appends new data items to your adapter.
        // Use the offset value and add it as a parameter to your API request to retrieve paginated data.
        // Deserialize API response and then construct new objects to append to the adapter

        if(page==8){
            Toast.makeText(this,"Done with 8 pages",Toast.LENGTH_SHORT).show();
            return;
        }

        String query = etQuery.getText().toString();
        //Toast.makeText(this,query,Toast.LENGTH_SHORT).show();
        String url;
        url = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=" + query + "&rsz=8&start=" + offset  ;

        AsyncHttpClient client = new AsyncHttpClient();

        client.get(url,null,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("DEBUG",response.toString());
                try {
                    JSONArray imageResultsJSON = response.getJSONObject("responseData").getJSONArray("results");
                    //imageResults.clear(); //clear existing images from the array (in case its a new search)
                    //when u add to adapter it modifies underlying data
                    aImageAdapter.addAll(ImageResult.fromJSONArray(imageResultsJSON));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("INFO",imageResults.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }
}
