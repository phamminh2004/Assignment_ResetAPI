package fpoly.minhpt.assignment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import fpoly.minhpt.assignment.databinding.FragmentCartBinding;
import fpoly.minhpt.assignment.model.Cart;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CartFragment extends Fragment {
    FragmentCartBinding binding;
    List<Cart> list = new ArrayList<>();
    CartAdapter adapter;
    DecimalFormat decimalFormat = new DecimalFormat("#,###");
    int price = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCartBinding.inflate(inflater, container, false);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.rvProduct.setLayoutManager(manager);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIService.DOMAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService apiService = retrofit.create(APIService.class);
        Call<List<Cart>> call = apiService.getCart();
        call.enqueue(new Callback<List<Cart>>() {
            @Override
            public void onResponse(Call<List<Cart>> call, Response<List<Cart>> response) {
                if (response.isSuccessful()) {
                    list = response.body();
                    adapter = new CartAdapter(getContext(), list);
                    binding.rvProduct.setAdapter(adapter);
                    for (Cart cart : list) {
                        price += cart.Price;
                    }
                    binding.tvPrice.setText("đ" + decimalFormat.format(price));
                }
            }

            @Override
            public void onFailure(Call<List<Cart>> call, Throwable t) {

            }
        });
        binding.btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, new OrderFragment()).addToBackStack(null).commit();
            }
        });
        return binding.getRoot();
    }
}