package com.example.ecommerce.Users;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.Admin.AdminApproveVendorActivity;
import com.example.ecommerce.Model.Products;
import com.example.ecommerce.Prevalent.Prevalent;
import com.example.ecommerce.R;
import com.example.ecommerce.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;


public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{
    private DatabaseReference ProductsRef;
    private RecyclerView recyclerView;
    LinearLayoutManager layoutManager;

    private String type = "";

//    private FloatingActionButton fab;

//    getSupportActionBar().hide();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null){
            type= getIntent().getExtras().get("Admin").toString();
        }

        ProductsRef = FirebaseDatabase.getInstance("https://ecommerce-87169-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("Products");

        Paper.init(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        TextView userNameTextView = headerView.findViewById(R.id.user_profile_name);
        CircleImageView profileImageView = headerView.findViewById(R.id.user_profile_image);

        if(!type.equals("Admin")){
            userNameTextView.setText(Prevalent.currentOnlineUser.getName());
            Picasso.get().load(Prevalent.currentOnlineUser.getImage()).placeholder(R.drawable.profile).into(profileImageView);
        }


        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(false);
        recyclerView.setLayoutManager(layoutManager);


        FloatingActionButton fabAscending = (FloatingActionButton) findViewById(R.id.fab_sort_ascending);
        FloatingActionButton fabDescending = (FloatingActionButton) findViewById(R.id.fab_sort_descending);
        fabAscending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabAscending.setVisibility(View.INVISIBLE);
                fabDescending.setVisibility(View.VISIBLE);

                layoutManager.setReverseLayout(false);
                layoutManager.setStackFromEnd(false);
                FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter = getAllProductsSorted();
                recyclerView.setAdapter(adapter);
                adapter.startListening();
            }
        });

        fabDescending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabAscending.setVisibility(View.VISIBLE);
                fabDescending.setVisibility(View.INVISIBLE);

                FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter = getAllProductsSorted();
                layoutManager.setReverseLayout(true);
                layoutManager.setStackFromEnd(true);
                recyclerView.setAdapter(adapter);
                adapter.startListening();
            }
        });
    }


    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter = getAllProducts();

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @NotNull
    private FirebaseRecyclerAdapter<Products, ProductViewHolder> getAllProducts() {
        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(ProductsRef.orderByChild("productState").equalTo("Approved"), Products.class)
                        .build();


        return new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Products model) {
                    holder.txtProductName.setText(model.getPname());
                    holder.txtProductDescription.setText(model.getDescription());
                    holder.txtProductPrice.setText("Price = Rp. " + model.getPrice().toString());
                    Picasso.get().load(model.getImage()).into(holder.imageView);


                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent;
                            if (type.equals("Admin")) {
                                intent = new Intent(HomeActivity.this, AdminApproveVendorActivity.class);
                            } else {
                                intent = new Intent(HomeActivity.this, ProductDetailsActivity.class);
                            }
                            intent.putExtra("pid", model.getPid());
                            startActivity(intent);

                        }
                    });

            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_layout, parent, false);
                ProductViewHolder holder = new ProductViewHolder(view);
                return holder;
            }
        };
    }

    private FirebaseRecyclerAdapter<Products, ProductViewHolder> getAllProductsSorted() {
        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(ProductsRef.orderByChild("price"), Products.class)
                        .build();


        return new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Products model)
            {
                if(model.getProductState().equalsIgnoreCase("Approved")) {
                    holder.txtProductName.setText(model.getPname());
                    holder.txtProductDescription.setText(model.getDescription());
                    holder.txtProductPrice.setText("Price = Rp. " + model.getPrice().toString());
                    Picasso.get().load(model.getImage()).into(holder.imageView);


                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent;
                            if (type.equals("Admin")) {
                                intent = new Intent(HomeActivity.this, AdminApproveVendorActivity.class);
                            } else {
                                intent = new Intent(HomeActivity.this, ProductDetailsActivity.class);
                            }
                            intent.putExtra("pid", model.getPid());
                            startActivity(intent);

                        }
                    });
                }
            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_layout, parent, false);
                ProductViewHolder holder = new ProductViewHolder(view);
                return holder;
            }
        };
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
//            super.onBackPressed();

            new AlertDialog.Builder(this)
                    .setTitle("Really Exit?")
                    .setMessage("Are you sure you want to exit?")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            Paper.book().destroy();

                            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                    }).create().show();


        }

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

//        if (id == R.id.action_settings)
//        {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

//        if (id == R.id.nav_cart)
//        {
//            if(!type.equals("Admin")){
//                Intent intent = new Intent(HomeActivity.this, CartActivity.class);
//                startActivity(intent);
//            }
//
//        }
//        if (id == R.id.nav_search)
//        {
//            if(!type.equals("Admin")){
//                Intent intent = new Intent(HomeActivity.this, SearchProductActivity.class);
//                startActivity(intent);
//            }
//
//        }
         if (id == R.id.nav_List)
        {
            Intent intent = new Intent(HomeActivity.this, WeddingToDoActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_setting)
        {
            if(!type.equals("Admin")){
                Intent intent = new Intent(HomeActivity.this, SettingActivity.class);
                startActivity(intent);
            }

        }
        else if (id == R.id.nav_logout)
        {
            if(!type.equals("Admin")){
                Paper.book().destroy();

                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}