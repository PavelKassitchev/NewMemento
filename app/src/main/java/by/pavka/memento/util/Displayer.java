package by.pavka.memento.util;

import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import static com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_SHORT;

public class Displayer {

    private Displayer() {}

    public static void showSnackbar(int id, View view) {
        Snackbar snack = Snackbar.make(view, id, LENGTH_SHORT);
        View v = snack.getView();
        TextView tv = v.findViewById(com.google.android.material.R.id.snackbar_text);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        } else {
            tv.setGravity(Gravity.CENTER_HORIZONTAL);
        }
        snack.show();
    }
}
