package by.pavka.memento;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import by.pavka.memento.util.Displayer;

public class MementoActivity extends AppCompatActivity {

    private long backPressed;
    private boolean backOverridden;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_help) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.help);
            builder.setMessage(R.string.helptext);
            AlertDialog dialog = builder.create();
            dialog.show();
            ((TextView)dialog.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    protected void setBackOverridden() {
        backOverridden = true;
    }

    @Override
    public void onBackPressed() {
        if (backOverridden) {
            if (backPressed + 2000 > System.currentTimeMillis()) {
                super.onBackPressed();
                finishAffinity();
            } else {
                Displayer.showSnackbar(R.string.back, findViewById(android.R.id.content));
            }
            backPressed = System.currentTimeMillis();
        }
        else {
            super.onBackPressed();
        }
    }

}
