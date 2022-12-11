package com.tartantransporttracker.ui.route;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.tartantransporttracker.R;
import com.tartantransporttracker.managers.BusStopManager;
import com.tartantransporttracker.managers.RouteManager;
import com.tartantransporttracker.models.BusStop;
import com.tartantransporttracker.models.Route;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CreateBusStopActivity extends AppCompatActivity {

    private List<Route> items;
    private AutoCompleteTextView spinner;
    private Button btnCreateBusStop;
    private EditText edt_address;
    private EditText edt_position;
    private BusStopManager busStopManager;
    private RouteManager routeManager;
    private Route route;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_bus_stop);

        busStopManager = new BusStopManager();
        routeManager = new RouteManager();

        edt_address = findViewById(R.id.edtAddress);
        edt_position = findViewById(R.id.edtPosition);
        btnCreateBusStop = findViewById(R.id.btn_add_bus_stop);
        spinner = findViewById(R.id.selectRouteSpinner);

        setUpRoutesSpinner();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinner.getAdapter().getItem(position) instanceof Route) {
                    Route selectedRoute = (Route) parent.getSelectedItem();
                    if (selectedRoute != null)
                    {
                        Log.e("CLEAR", selectedRoute.getName());
                        route = selectedRoute;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.e("SPINNER", "Nothing selected");
            }
        });

        btnCreateBusStop.setOnClickListener(v -> {
            String address = edt_address.getText().toString();

            Integer position = Integer.parseInt(edt_position.getText().toString());

            BusStop busStop = new BusStop(address, route, position);
            busStopManager.createBusStop(busStop);

            Toast.makeText(CreateBusStopActivity.this, "Bus stop created", Toast.LENGTH_SHORT).show();
        });
    }

    private void setUpRoutesSpinner()
    {
        items = new ArrayList<>();

        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, items);
        spinner.setAdapter(adapter);

        routeManager.findAllRoutes().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful())
                {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Route route = document.toObject(Route.class);
                        items.add(route);
                    }
                    adapter.notifyDataSetChanged();
                    spinner.setSelection(0);
                }
            }
        });
    }
}