package fpoly.minhpt.assignment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;

import fpoly.minhpt.assignment.databinding.ItemSpBinding;
import fpoly.minhpt.assignment.model.Cart;
import fpoly.minhpt.assignment.model.Product;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    List<Product> productList;
    Context context;
    DecimalFormat decimalFormat = new DecimalFormat("#,###");


    public ProductAdapter(List<Product> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSpBinding binding = ItemSpBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String id = productList.get(position)._id;
        String img = productList.get(position).Image;

        try {
            Picasso.get().load(img).placeholder(R.drawable.ic_camera).error(R.drawable.ic_camera)
                    .into(holder.binding.ivImg);
        } catch (Exception e) {
            Log.e("PicassoError", "Error loading image: " + e.getMessage());
        }
        holder.binding.tvName.setText(String.valueOf(productList.get(position).ProductName));
        holder.binding.tvPrice.setText("đ" + decimalFormat.format(productList.get(position).Price) + "");
        holder.itemView.setOnClickListener(v -> {
            // Tạo một Bundle để đính kèm dữ liệu
            Bundle bundle = new Bundle();
            bundle.putString("prdID", id);

            // Tạo một fragment mới
            ProductInfoFragment productInfoFragment = new ProductInfoFragment();
            // Đặt dữ liệu vào fragment mới
            productInfoFragment.setArguments(bundle);

            // Thực hiện thay đổi fragment sử dụng FragmentManager
            FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.flContainer, productInfoFragment)
                    .addToBackStack(null) // (Optional) Cho phép người dùng quay lại fragment trước đó
                    .commit();
        });

        holder.binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                cart.ProductName = productList.get(position).ProductName;
                cart.Price = productList.get(position).Price;
                cart.Image = productList.get(position).Image;
                cart.Quantity = 1;
                cart.ProductID = productList.get(position)._id;
                Call<List<Cart>> call = apiService.getCart();
                call.enqueue(new Callback<List<Cart>>() {
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
                                Toast.makeText(context, "Sản phầm đã có trong giỏ hàng", Toast.LENGTH_SHORT).show();
                            } else {
                                apiService.postCart(cart).enqueue(new Callback<Cart>() {
                                    @Override
                                    public void onResponse(Call<Cart> call, Response<Cart> response) {
                                        if (response.isSuccessful()) {
                                            Toast.makeText(context, "Thêm vào giỏ hàng thành công", Toast.LENGTH_SHORT).show();
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

            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemSpBinding binding;

        public ViewHolder(@NonNull ItemSpBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
