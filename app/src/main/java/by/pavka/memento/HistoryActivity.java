package by.pavka.memento;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Calendar;

import by.pavka.memento.databinding.ActivityHistoryBinding;

public class HistoryActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Chronicler chronicler;
    private GraphView graphView;
    private DataPoint[] data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chronicler = ((MementoApplication)getApplication()).getUser().getChronicler();
        ActivityHistoryBinding binding = ActivityHistoryBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        Spinner period = binding.period;
        period.setOnItemSelectedListener(this);
        graphView = binding.graph;

        data = chronicler.getDataSeries();
        if (data.length > 0) {
            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(data);
            graphView.addSeries(series);
            graphView.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this));
            graphView.getGridLabelRenderer().setNumHorizontalLabels(3);
            graphView.getViewport().setMaxX(data[data.length - 1].getX());
            graphView.getViewport().setXAxisBoundsManual(true);
            graphView.getGridLabelRenderer().setHumanRounding(false);
            graphView.getGridLabelRenderer().setVerticalAxisTitle(getResources().getString(R.string.weight));
        }

        BottomNavigationView bottomNavigationView = binding.bottomNavigation.getRoot();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationListener(this));
        MenuItem item = bottomNavigationView.getMenu().findItem(R.id.weights);
        item.setChecked(true);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (data.length > 0) {
            int choice = parent.getSelectedItemPosition();
            int index = 0;
            Calendar start = Calendar.getInstance();
            switch (choice) {
                case 1:
                    start.add(Calendar.MONTH, -6);
                    index = chronicler.getTimeIndex(start);
                    break;
                case 2:
                    start.add(Calendar.MONTH, -3);
                    index = chronicler.getTimeIndex(start);
                    break;
                case 3:
                    start.add(Calendar.MONTH, -1);
                    index = chronicler.getTimeIndex(start);
                    break;
                default:
                    break;
            }
            graphView.getViewport().setMinX(data[index].getX());
            graphView.onDataChanged(false, false);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}