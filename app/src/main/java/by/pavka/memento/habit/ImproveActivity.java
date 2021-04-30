package by.pavka.memento.habit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import by.pavka.memento.BottomNavigationListener;
import by.pavka.memento.MementoApplication;
import by.pavka.memento.databinding.ActivityImproveBinding;

public class ImproveActivity extends AppCompatActivity {

    private MementoApplication application;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityImproveBinding binding = ActivityImproveBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        application = (MementoApplication) getApplication();
        BottomNavigationView bottomNavigationView = binding.bottomNavigation.getRoot();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationListener(this));
    }
}