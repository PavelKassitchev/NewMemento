package by.pavka.memento.habit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Map;

import by.pavka.memento.BottomNavigationListener;
import by.pavka.memento.MementoActivity;
import by.pavka.memento.MementoApplication;
import by.pavka.memento.R;
import by.pavka.memento.databinding.ActivityHabbitBinding;

public class  HabitActivity extends MementoActivity {

    private MementoApplication application;
    private RecyclerView recycler;
    private HabitRecyclerViewAdapter adapter;
    private UserHabitTracker tracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityHabbitBinding binding = ActivityHabbitBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        setBackOverridden();
        Toolbar toolbar = binding.toolbar.getRoot();
        setSupportActionBar(toolbar);
        application = (MementoApplication) getApplication();
        BottomNavigationView bottomNavigationView = binding.bottomNavigation.getRoot();
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        MenuItem item = bottomNavigationView.getMenu().findItem(R.id.habit);
        item.setChecked(true);
        tracker = application.getUser().getTracker();
        recycler = findViewById(R.id.recyclerView);
        adapter = new HabitRecyclerViewAdapter(this, tracker);
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new GridLayoutManager(this, getResources().getConfiguration().orientation * 2));
        Intent intent = getIntent();
        clearHabit(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        clearHabit(intent);
    }

    private void clearHabit(Intent intent) {
        if (intent != null && intent.getSerializableExtra("habit") != null) {
            Habit habit = (Habit)intent.getSerializableExtra("habit");
            tracker.clearHabitProgress(habit.getId());
            adapter.setTracker(tracker);
            adapter.notifyItemChanged(adapter.habits.indexOf(habit));
            application.saveHabits();
            if (intent.getBooleanExtra("expire", false)) {
                finish();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Habit habit = (Habit)data.getSerializableExtra("habit");
            application.launchNotification(habit.getId(), true);
            application.saveHabits();
            adapter.setTracker(application.getUser().getTracker());
            adapter.notifyItemChanged(adapter.habits.indexOf(habit));
            adapter.notifyDataSetChanged();
        }
    }
}