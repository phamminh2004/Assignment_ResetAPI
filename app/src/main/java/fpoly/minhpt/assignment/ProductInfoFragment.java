package fpoly.minhpt.assignment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;

import fpoly.minhpt.assignment.databinding.FragmentProductInfoBinding;
import fpoly.minhpt.assignment.model.Cart;
import fpoly.minhpt.assignment.model.Product;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProductInfoFragment extends Fragment {
    DecimalFormat decimalFormat = new DecimalFormat("#,###");
    FragmentProductInfoBinding binding;
    Bundle bundle;
    String id, name, color;
    int quantity = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProductInfoBinding.inflate(inflater, container, false);
        bundle = getArguments();
        String id = bundle.getString("prdID");
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
                    List<Product> list = response.body();
                    for (Product product : list) {
                        if (product._id.equals(id)) {
                            Picasso.get().load(product.Image).placeholder(R.drawable.ic_camera).error(R.drawable.ic_camera)
                                    .into(binding.ivImg);
                            binding.tvName.setText(product.ProductName);
                            binding.tvDiscription.setText(product.Description);
                            binding.tvQuantity.setText(quantity + "");
                            binding.tvPrice.setText("đ" + decimalFormat.format(product.Price * quantity));
                            binding.ivPlus.setOnClickListener(view -> {
                                quantity++;
                                binding.tvQuantity.setText(quantity + "");
                                binding.tvPrice.setText("đ" + decimalFormat.format(product.Price * quantity));
                            });
                            binding.ivMinus.setOnClickListener(view -> {
                                if (quantity > 1) {
                                    quantity--;
                                    binding.tvQuantity.setText(quantity + "");
                                    binding.tvPrice.setText("đ" + decimalFormat.format(product.Price * quantity));
                                }
                            });
                            binding.btnAdd.setOnClickListener(view -> {
                                Retrofit retrofit = new Retrofit.Builder()
                                        .baseUrl(APIService.DOMAIN)
                                        .addConverterFactory(GsonConverterFactory.create())
                                        .client(new OkHttpClient.Builder()
                                                .connectTimeout(30, TimeUnit.SECONDS)
                                                .writeTimeout(30, TimeUnit.SECONDS)
                                                .readTimeout(30, TimeUnit.SECONDS)
                                                .build())
                                        .build();


                                APIService apiService = retrofit.create(APIService.class);
                                Cart cart = new Cart();
                                cart.ProductID = id;
                                cart.ProductName = product.ProductName;
                                cart.Price = product.Price * quantity;
                                cart.Quantity = quantity;
                                cart.Image = product.Image;

                                Call<List<Cart>> call1 = apiService.getCart();
                                call1.enqueue(new Callback<List<Cart>>() {
                                    @Override
                                    public void onResponse(Call<List<Cart>> call, Response<List<Cart>> response) {
                                        Boolean flag = false;
                                        if (response.isSuccessful()) {
                                            List<Cart> list = response.body();
                                            for (Cart cart1 : list) {
                                                if (cart1.ProductID.equals(cart.ProductID)) {
                                                    flag = true;
                                                }
                                            }
                                            if (flag == true) {
                                                Toast.makeText(getContext(), "Sản phầm đã có trong giỏ hàng", Toast.LENGTH_SHORT).show();
                                            } else {
                                                apiService.postCart(cart).enqueue(new Callback<Cart>() {
                                                    @Override
                                                    public void onResponse(Call<Cart> call, Response<Cart> response) {
                                                        if (response.isSuccessful()) {
                                                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, new CartFragment()).addToBackStack(null).commit();
                                                            Toast.makeText(getContext(), "Thêm vào giỏ hàng thành công", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<Cart> call, Throwable t) {

                                                    }
                                                });
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<List<Cart>> call, Throwable t) {

                                    }
                                });
                            });
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {

            }
        });
        return binding.getRoot();
    }

}