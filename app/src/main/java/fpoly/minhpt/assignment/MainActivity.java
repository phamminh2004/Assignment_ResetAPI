package fpoly.minhpt.assignment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import fpoly.minhpt.assignment.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ActivityMainBinding binding;
    DrawerLayout drawer;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        drawer = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);
        NavigationView nv = findViewById(R.id.nvView);
        nv.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        if (savedInstanceState == null){
            setTitle("Trang chủ");
            getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, new HomeFragment()).addToBackStack(null).commit();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            startActivity(new Intent(MainActivity.this, Login.class));
            finish();
        } else if (item.getItemId() == R.id.changePassword) {
            setTitle("Đổi mật khẩu");
            getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, new ChangePasswordFragment()).addToBackStack(null).commit();
        } else if (item.getItemId() == R.id.addAccount) {
            setTitle("Thêm tài khoản");
            getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, new AddAccountFragment()).addToBackStack(null).commit();
        } else if (item.getItemId() == R.id.home) {
            setTitle("Trang chủ");
            getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, new HomeFragment()).addToBackStack(null).commit();
        } else if (item.getItemId() == R.id.order) {
            setTitle("Đơn hàng");
            getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, new BillFragment()).addToBackStack(null).commit();
        }
        drawer.close();
        return false;
    }
}
