package gridimagesearch.codepath.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
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
import gridimagesearch.codepath.dialogutils.SettingsFragment;
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
    private String imageColor;
    private String imageSite;
    private String imageType;
    String query;
    String[] imageSizes = new String[] {
            "any", "small", "medium", "large", "xlarge"
    };
    String[] imageTypes = new String[] {
            "any", "face", "photo", "clipart", "lineart"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_search);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setTitle("");

        //etQuery = (EditText) findViewById(R.id.etQuery);
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
        imageType = "any";
        imageColor= "";
        imageSite = "";
        query="";
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here
                onImageSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
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
    public void onImageSearch(String query) {
        //String query = etQuery.getText().toString();
        this.query = query;
        if(query.trim().isEmpty()){
            Toast.makeText(this, R.string.error_search_string, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isNetworkAvailable()) {
            Toast.makeText(this, R.string.error_network_string, Toast.LENGTH_SHORT).show();
            return;
        }
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=" + query + "&rsz=8" ;
        if(imageSize!="" && imageSize != "any")
             url += "&imgsz=" + imageSize;
        if(!imageColor.isEmpty())
            url += "&imgcolor=" + imageColor;
        if(!imageSite.isEmpty())
            url += "&as_sitesearch=" + imageSite;
        if(imageType!="" && imageType != "any")
            url += "&imgtype=" + imageType;
        //Toast.makeText(this, url, Toast.LENGTH_LONG).show();
        client.get(url,null,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("DEBUG",response.toString());
                try {
                    JSONArray imageResultsJSON = response.getJSONObject("responseData").getJSONArray("results");
                    imageResults.clear(); //clear existing images from the array (in case its a new search)
                    //when u add to adapter it modifies underlying data
                    if(imageResultsJSON.length() == 0) {
                        aImageAdapter.clear();
                        Toast.makeText(getBaseContext(), R.string.error_no_results_string, Toast.LENGTH_SHORT).show();
                    }else
                        aImageAdapter.addAll(ImageResult.fromJSONArray(imageResultsJSON));
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getBaseContext(), R.string.error_conn_server_string, Toast.LENGTH_SHORT).show();
                }
                Log.d("INFO",imageResults.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(getBaseContext(), R.string.error_req_failed_string, Toast.LENGTH_SHORT).show();
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
            //Toast.makeText(this,"Done with 8 pages",Toast.LENGTH_SHORT).show();
            return;
        }

        //String query = etQuery.getText().toString();
        //Toast.makeText(this,query,Toast.LENGTH_SHORT).show();
        String url= "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=" + query + "&rsz=8&start=" + offset ;
        if(imageSize!="" && imageSize != "any")
            url += "&imgsz=" + imageSize;
        if(imageColor!="" && imageColor != "")
            url += "&imgcolor=" + imageColor;
        if(imageSite!="" && imageSite != "")
            url += "&as_sitesearch=" + imageSite;
        if(imageType!="" && imageType != "any")
            url += "&imgtype=" + imageType;

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
                    Toast.makeText(getBaseContext(), R.string.error_conn_server_string, Toast.LENGTH_SHORT).show();
                }
                Log.d("INFO",imageResults.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(getBaseContext(), R.string.error_req_failed_string, Toast.LENGTH_SHORT).show();
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    public String[] getImageSizes() {
        return imageSizes;
    }

    public String getSize(){
        return imageSize;
    }
    public void setSize(String text){
        imageSize = text;
    }

    public String[] getImageTypes() {
        return imageTypes;
    }

    public String getType(){
        return imageType;
    }
    public void setType(String text){
        imageType = text;
    }

    public String getColor(){
        return imageColor;
    }

    public void setColor(String text){
        imageColor = text;
    }

    public String getSite(){
        return imageSite;
    }
    public void setSite(String text){
        imageSite = text;
    }

}
