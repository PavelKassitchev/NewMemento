package by.pavka.memento.habit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import by.pavka.memento.BottomNavigationListener;
import by.pavka.memento.MementoApplication;
import by.pavka.memento.R;
import by.pavka.memento.databinding.ActivityHabbitBinding;

public class  HabitActivity extends AppCompatActivity {

    private MementoApplication application;
    private RecyclerView recycler;
    private HabitRecyclerViewAdapter adapter;

//    private List<Habit> habits;
    private UserHabitTracker tracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityHabbitBinding binding = ActivityHabbitBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        application = (MementoApplication) getApplication();
        BottomNavigationView bottomNavigationView = binding.bottomNavigation.getRoot();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationListener(this));
        MenuItem item = bottomNavigationView.getMenu().findItem(R.id.action_dial);
        item.setChecked(true);

        tracker = application.getUser().getTracker();
        recycler = findViewById(R.id.recyclerView);
        adapter = new HabitRecyclerViewAdapter(this, tracker);
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new GridLayoutManager(this, getResources().getConfiguration().orientation * 2));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            adapter.setTracker(application.getUser().getTracker());
            adapter.notifyDataSetChanged();
            application.saveHabits();
        }
    }
}