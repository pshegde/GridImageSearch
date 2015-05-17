package gridimagesearch.codepath.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import gridimagesearch.codepath.models.ImageResult;
import gridimagesearch.codepath.models.R;

/**
 * Created by Prajakta on 5/14/2015.
 */
public class ImageResultsAdapter extends ArrayAdapter<ImageResult> {
    public ImageResultsAdapter(Context context, List<ImageResult> images) {
          super(context, android.R.layout.simple_list_item_1, images);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageResult imageInfo = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_image_result,parent,false);
        }
        ImageView ivImage = (ImageView) convertView.findViewById(R.id.ivImage);
        //TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        //clear out the image
        ivImage.setImageResource(0);

       // tvTitle.setText(Html.fromHtml(imageInfo.getTitle()));
        Picasso.with(getContext()).load(imageInfo.getThumbUrl()).placeholder(R.drawable.placeholder).into(ivImage);
        //return the completed view to be displayed
        return convertView;
    }
}
