package by.pavka.memento;

import android.content.Intent;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import by.pavka.memento.setting.SettingsActivity;
import by.pavka.memento.track.HabitActivity;
import by.pavka.memento.profile.MainActivity;
import by.pavka.memento.util.Displayer;
import by.pavka.memento.weight.MeasureActivity;

public class MementoActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.profile:
                intent = new Intent(this, MainActivity.class);
                break;
            case R.id.habit:
                intent = new Intent(this, HabitActivity.class);
                break;
            case R.id.weights:
                intent = new Intent(this, MeasureActivity.class);
                break;
            default:
        }
        if (intent == null) {
            return false;
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
        return true;
    }
}
