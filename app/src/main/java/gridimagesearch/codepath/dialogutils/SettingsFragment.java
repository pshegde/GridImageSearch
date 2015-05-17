package gridimagesearch.codepath.dialogutils;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Arrays;

import gridimagesearch.codepath.activities.SearchActivity;
import gridimagesearch.codepath.models.R;

/**
 * Created by Prajakta on 5/16/2015.
 */
public class SettingsFragment extends DialogFragment {
    private Spinner spSize;
    private Spinner spType;
    private EditText etSite;
    private EditText etColor;
    private Button btSave;
    private Button btCancel;

    @Override
    public View onCreateView(
                LayoutInflater inflater,
                ViewGroup container,
                Bundle savedInstanceState) {
        //View v = inflater.inflate(R.layout.settings_fragment, container, false);
        String[] imageSizes = ((SearchActivity)getActivity()).getImageSizes();
        String[] imageTypes = ((SearchActivity)getActivity()).getImageTypes();
//
        getDialog().setTitle("Advanced Filters");
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = inflater.inflate(R.layout.settings_fragment, null);
        builder.setView(view);

        spSize = (Spinner) view.findViewById(R.id.spSize);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, imageSizes) {
            public View getView(int position, View convertView,ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                //((TextView) v).setTextSize(12);
                ((TextView) v).setGravity(Gravity.CENTER);
                ((TextView) v).setWidth(50);
                return v;
            }

            public View getDropDownView(int position, View convertView,ViewGroup parent) {
                View v = super.getDropDownView(position, convertView,parent);
                //((TextView) v).setTextSize(16);
                ((TextView) v).setGravity(Gravity.CENTER);
                ((TextView) v).setWidth(50);
                return v;
            }
        };
        spSize.setAdapter(adapter);
        spSize.requestFocus();

        //color
        etColor = (EditText) view.findViewById(R.id.etColor);

        //spinner type
        spType = (Spinner) view.findViewById(R.id.spType);
        ArrayAdapter<String> adapterType = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, imageTypes) {
            public View getView(int position, View convertView,ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                //((TextView) v).setTextSize(12);
                ((TextView) v).setGravity(Gravity.CENTER);
                ((TextView) v).setWidth(50);
                return v;
            }

            public View getDropDownView(int position, View convertView,ViewGroup parent) {
                View v = super.getDropDownView(position, convertView,parent);
                //((TextView) v).setTextSize(16);
                ((TextView) v).setGravity(Gravity.CENTER);
                ((TextView) v).setWidth(50);
                return v;
            }
        };
        spType.setAdapter(adapterType);
        spType.requestFocus();

        //site
        etSite = (EditText) view.findViewById(R.id.etSite);

        btSave = (Button) view.findViewById(R.id.btSave);
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = spSize.getSelectedItem().toString();
                ((SearchActivity)getActivity()).setSize(text);

                text = etColor.getText().toString();
                ((SearchActivity)getActivity()).setColor(text);

                text = spType.getSelectedItem().toString();
                ((SearchActivity)getActivity()).setType(text);

                text = etSite.getText().toString();
                ((SearchActivity)getActivity()).setSite(text);

                dismiss();


            }
        });

        btCancel = (Button) view.findViewById(R.id.btCancel);
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        //set old values in settings
        String oldSize = ((SearchActivity)getActivity()).getSize();
        spSize.setSelection(Arrays.asList(imageSizes).indexOf(oldSize));
        String oldColor = ((SearchActivity)getActivity()).getColor();
        etColor.setText(oldColor);
        String oldType = ((SearchActivity)getActivity()).getType();
        spType.setSelection(Arrays.asList(imageTypes).indexOf(oldType));

        String oldSite = ((SearchActivity)getActivity()).getSite();
        etSite.setText(oldSite);

        return view;
    }

}
