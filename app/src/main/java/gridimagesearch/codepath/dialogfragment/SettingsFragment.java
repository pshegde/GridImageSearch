package gridimagesearch.codepath.dialogfragment;

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
import android.widget.Spinner;
import android.widget.TextView;

import gridimagesearch.codepath.activities.SearchActivity;
import gridimagesearch.codepath.models.R;

/**
 * Created by Prajakta on 5/16/2015.
 */
public class SettingsFragment extends DialogFragment {
    private Spinner spSize;
    @Override
    public View onCreateView(
                LayoutInflater inflater,
                ViewGroup container,
                Bundle savedInstanceState) {
        //View v = inflater.inflate(R.layout.settings_fragment, container, false);
        String[] arraySpinner = new String[] {
                "any", "small", "medium", "large", "extra-large"
        };
//
        getDialog().setTitle("Advanced Filters");

        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view = inflater.inflate(R.layout.settings_fragment, null);

        builder.setView(view);

        // Show soft keyboard automatically



        spSize = (Spinner) view.findViewById(R.id.spSize);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, arraySpinner) {
            public View getView(int position, View convertView,ViewGroup parent) {

                View v = super.getView(position, convertView, parent);

                ((TextView) v).setTextSize(16);
                ((TextView) v).setGravity(Gravity.END);
                ((TextView) v).setWidth(50);
                return v;

            }

            public View getDropDownView(int position, View convertView,ViewGroup parent) {

                View v = super.getDropDownView(position, convertView,parent);
                ((TextView) v).setTextSize(16);
                ((TextView) v).setGravity(Gravity.END);
                ((TextView) v).setWidth(50);
                return v;

            }
        };

        spSize.setAdapter(adapter);
        spSize.requestFocus();

        Button btSave = (Button) view.findViewById(R.id.btSave);

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = spSize.getSelectedItem().toString();

                //dismiss the dialog and return to activity
                ((SearchActivity)getActivity()).setSize(text);
                dismiss();


            }
        });
        return view;
    }

}
