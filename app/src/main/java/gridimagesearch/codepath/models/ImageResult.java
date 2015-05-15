package gridimagesearch.codepath.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Prajakta on 5/14/2015.
 */
public class ImageResult implements Parcelable{
    //private static final long serialVersionUID = 1;
    private String fullUrl;
    private String thumbUrl;
    private String title;

    public ImageResult(JSONObject json){
        try {
            this.fullUrl = json.getString("url");
            this.thumbUrl = json.getString("tbUrl");
            this.title = json.getString("title");
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    public static ArrayList<ImageResult> fromJSONArray(JSONArray array) {
        ArrayList<ImageResult> results = new ArrayList<>();
        for(int i=0;i<array.length();i++){
            try{
                results.add(new ImageResult(array.getJSONObject(i)));
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return results;
    }

    public String getFullUrl() {
        return fullUrl;
    }

    public void setFullUrl(String fullUrl) {
        this.fullUrl = fullUrl;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(fullUrl);
        out.writeString(thumbUrl);
        out.writeString(title);

    }

    public static final Parcelable.Creator<ImageResult> CREATOR
            = new Parcelable.Creator<ImageResult>() {
        public ImageResult createFromParcel(Parcel in) {
            return new ImageResult(in);
        }

        public ImageResult[] newArray(int size) {
            return new ImageResult[size];
        }
    };

    public ImageResult() {
        fullUrl = "";
        thumbUrl = "";
        title = "";
    }

    private ImageResult(Parcel in) {
        fullUrl = in.readString();
        thumbUrl = in.readString();
        title = in.readString();
    }
}
