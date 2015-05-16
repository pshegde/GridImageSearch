package gridimagesearch.codepath.activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import gridimagesearch.codepath.models.ImageResult;
import gridimagesearch.codepath.models.R;

public class ImageDisplayActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_display);

        //pull out the url
        ImageResult image = (ImageResult) getIntent().getParcelableExtra("image_result");
        ImageView ivImageResult = (ImageView) findViewById(R.id.ivFullImage);
        Picasso.with(this).load(Uri.parse(image.getFullUrl())).placeholder(R.drawable.placeholder).into(ivImageResult);
        getSupportActionBar().setTitle("View Image");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_image_display, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
        int id = item.getItemId();
        if(id==R.id.miRequestDrink) {
            //launch a second age form activity
            Toast.makeText(this, "Clicked!", Toast.LENGTH_SHORT)
                    .show();
        }

        return super.onOptionsItemSelected(item);
    }
}
