package fpoly.minhpt.assignment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import fpoly.minhpt.assignment.databinding.FragmentHomeBinding;
import fpoly.minhpt.assignment.model.Product;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class HomeFragment extends Fragment {
    FragmentHomeBinding binding;
    ProductAdapter adapter;
    List<Product> list;
    ArrayList<String> listPhoto = new ArrayList<>();
    PhotoAdapter photoAdapter;
    Timer mTimer;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        listPhoto.add("https://shophoaqua.vn/public/media/file/files/slider/banner-4.png");
        listPhoto.add("https://dichoisaigon.com/wp-content/uploads/2019/10/anh-dai-dien-20.jpg");
        listPhoto.add("https://hoaquafuji.com/storage/app/uploads/public/fb9/03f/f84/thumb__1920_0_0_0_auto.jpg");
        autoImg();
        GridLayoutManager manager = new GridLayoutManager(getContext(), 2);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.rvPrd.setLayoutManager(manager);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIService.DOMAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService apiService = retrofit.create(APIService.class);
        Call<List<Product>> call = apiService.getProduct();
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    list = response.body();
                    Log.d("list", list.size() + "");
                    adapter = new ProductAdapter(list, getContext());
                    binding.rvPrd.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {

            }
        });
        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<Product> templist = new ArrayList<>();
                try {
                    if (s.toString().trim() != "") {
                        for (Product product : list) {
                            if (String.valueOf(product.ProductName).toLowerCase().contains(String.valueOf(s).toLowerCase())) {
                                templist.add(product);
                            }
                        }
                        adapter = new ProductAdapter(templist, getContext());
                        binding.rvPrd.setAdapter(adapter);
                    }
                } catch (Exception e) {
                    Log.e("TAG", "Lỗi tìm kiếm" + e.getMessage());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, new CartFragment()).addToBackStack(null).commit();
            }
        });
        binding.ivPerson.setOnClickListener(view -> {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, new UserFragment()).addToBackStack(null).commit();
        });
        return binding.getRoot();

    }

    private void autoImg() {
        photoAdapter = new PhotoAdapter(getContext(), listPhoto);
        binding.viewpager.setAdapter(photoAdapter);
        if (mTimer == null) {
            mTimer = new Timer();
        }
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        int currentItem = binding.viewpager.getCurrentItem();
                        int totalItem = listPhoto.size() - 1;
                        if (currentItem < totalItem) {
                            currentItem++;
                            binding.viewpager.setCurrentItem(currentItem);
                        } else {
                            binding.viewpager.setCurrentItem(0);
                        }
                    }
                });
            }
        }, 500, 3000);
    }
}