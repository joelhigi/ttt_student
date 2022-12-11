package com.tartantransporttracker.ui.route;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.android.material.navigation.NavigationView;
import com.tartantransporttracker.BaseActivity;
import com.tartantransporttracker.MainActivity;
import com.tartantransporttracker.R;
import com.tartantransporttracker.managers.RouteManager;
import com.tartantransporttracker.models.Route;

import java.util.ArrayList;
import java.util.List;

import com.tartantransporttracker.databinding.ActivityAdminViewRouteBinding;
import com.tartantransporttracker.databinding.ActivityMainBinding;


public class AdminViewRoute extends BaseActivity<ActivityAdminViewRouteBinding>{

    private AppBarConfiguration appBarConfiguration;

    Button deleteBtn;
    FloatingActionButton floatingActionButton;
    private RouteManager routeManager = RouteManager.getInstance();
    private List<Route> routes = new ArrayList<>();

    @Override
    public ActivityAdminViewRouteBinding getViewBinding() {
        return ActivityAdminViewRouteBinding.inflate(getLayoutInflater());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_route);

        deleteBtn = findViewById(R.id.deleteBtn);
        floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), CreateRouteActivity.class);
                startActivity(i);
            }
        });

        findAllRoute();
    }
    private void findAllRoute()
    {
        RecyclerView recyclerView = findViewById(R.id.listOfRoute);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        routeManager.findAllRoutes().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()){
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for(DocumentSnapshot doc:list){
                                Route route = doc.toObject(Route.class);
                                routes.add(route);
                                ViewRouteAdapter adapter = new ViewRouteAdapter(routes);
                                recyclerView.setAdapter(adapter);
                                adapter.notifyItemInserted(routes.size() -1);
                            }
                        }else{
                            Log.w("Message:","No data found in the database");
                        }
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.SecondFragment);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}