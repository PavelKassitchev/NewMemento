package by.pavka.memento.habit;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import by.pavka.memento.R;

public class HabitRecyclerViewAdapter extends RecyclerView.Adapter<HabitRecyclerViewAdapter.HabitViewHolder> {
    private List<Habit> habits;
    private LayoutInflater inflater;
    private Context context;

    public HabitRecyclerViewAdapter(Context context, List<Habit> habits) {
        this.habits = habits;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public HabitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.card_item, parent, false);
        return new HabitViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull HabitViewHolder holder, int position) {
        Habit habit = habits.get(position);
        holder.habitName.setText(habit.getName());

//        Uri uri = Uri.parse("@android:drawable/btn_star");
        holder.imageView.setImageResource(R.drawable.ic_launcher_background);
    }

    @Override
    public int getItemCount() {
        return habits.size();
    }

    public class HabitViewHolder extends RecyclerView.ViewHolder {
        public int REQUEST_CODE = 1;
        private HabitRecyclerViewAdapter adapter;
        private ImageView imageView;
        private TextView habitName;
        private int position;

        public HabitViewHolder(@NonNull View itemView, HabitRecyclerViewAdapter adapter) {
            super(itemView);
            this.adapter = adapter;
            imageView = itemView.findViewById(R.id.card_image);
            habitName = itemView.findViewById(R.id.card_text);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HabitActivity context = (HabitActivity)(adapter.context);
                    Intent intent = new Intent(context,ActivizationActivity.class);
                    intent.putExtra("position", getLayoutPosition());
                    context.startActivityForResult(intent, REQUEST_CODE);
                }
            });
        }


    }
}
