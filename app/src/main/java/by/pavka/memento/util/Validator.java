package by.pavka.memento.util;

import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

public class Validator {
    public static void showSnackbar(int id, View view) {
        Snackbar snack = Snackbar.make(view, id, Snackbar.LENGTH_SHORT);
        View v = snack.getView();
        TextView tv = v.findViewById(com.google.android.material.R.id.snackbar_text);
        //tv.setGravity(Gravity.CENTER_HORIZONTAL);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        } else {
            tv.setGravity(Gravity.CENTER_HORIZONTAL);
        }
        snack.show();
    }
}
