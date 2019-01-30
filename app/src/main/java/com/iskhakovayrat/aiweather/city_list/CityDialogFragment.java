package com.iskhakovayrat.aiweather.city_list;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.iskhakovayrat.aiweather.R;

public class CityDialogFragment extends DialogFragment {

    public interface ChooseCityDialogListener{
        void onDialogPositiveClick(DialogFragment dialog, String cityName);
    }

    ChooseCityDialogListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(getActivity() instanceof ChooseCityDialogListener) {
            listener = (ChooseCityDialogListener) getActivity();
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_city, null);
        EditText editText = view.findViewById(R.id.dialogCityName);
        builder.setView(view)
                .setPositiveButton("OK", (dialogInterface, i) ->
                        listener.onDialogPositiveClick(CityDialogFragment.this, editText.getText().toString()))
                .setNegativeButton("Cancel", (dialogInterface, i) -> {

                });

        return builder.create();
    }
}
