package fpoly.minhpt.assignment;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;

import fpoly.minhpt.assignment.databinding.ItemCartBinding;
import fpoly.minhpt.assignment.model.Cart;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    Context context;
    List<Cart> list;
    DecimalFormat decimalFormat = new DecimalFormat("#,###");
    int price;
    int quantity;

    public CartAdapter(Context context, List<Cart> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCartBinding binding = ItemCartBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cart cart = list.get(position);
        String id = cart._id;
        holder.binding.tvName.setText(cart.ProductName);
        holder.binding.tvPrice.setText("đ" + decimalFormat.format(cart.Price));
        holder.binding.tvQuantity.setText(cart.Quantity + "");
        String img = cart.Image;
        try {
            Picasso.get().load(img).placeholder(R.drawable.ic_camera).error(R.drawable.ic_camera)
                    .into(holder.binding.ivImg);
        } catch (Exception e) {
            Log.e("PicassoError", "Error loading image: " + e.getMessage());
        }

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

        holder.binding.ivMinus.setOnClickListener(v -> {
            quantity = cart.Quantity;
            if (quantity > 1) {
                int rate = cart.Price / quantity;
                quantity--;
                cart.Price -= rate;
                cart.Quantity = quantity;
                apiService.updateCart(id, cart).enqueue(new Callback<Cart>() {
                    @Override
                    public void onResponse(Call<Cart> call, Response<Cart> response) {
                        if (response.isSuccessful()) {
                            holder.binding.tvQuantity.setText(quantity + "");
                            holder.binding.tvPrice.setText("đ"+ decimalFormat.format(cart.Price));
                        }
                    }

                    @Override
                    public void onFailure(Call<Cart> call, Throwable t) {

                    }
                });
            }
        });
        holder.binding.ivPlus.setOnClickListener(v -> {
            quantity = cart.Quantity;
            int rate = cart.Price / quantity;
            quantity++;
            cart.Price += rate;
            cart.Quantity = quantity;
            apiService.updateCart(id, cart).enqueue(new Callback<Cart>() {
                @Override
                public void onResponse(Call<Cart> call, Response<Cart> response) {
                    if (response.isSuccessful()) {
                        holder.binding.tvQuantity.setText(quantity + "");
                        holder.binding.tvPrice.setText("đ"+ decimalFormat.format(cart.Price));
                    }
                }

                @Override
                public void onFailure(Call<Cart> call, Throwable t) {

                }
            });
        });
        holder.binding.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                apiService.deleteCart(id).enqueue(new Callback<Cart>() {
                    @Override
                    public void onResponse(Call<Cart> call, Response<Cart> response) {
                        if (response.isSuccessful()){
                            list.remove(position);
                            notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<Cart> call, Throwable t) {

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemCartBinding binding;

        public ViewHolder(@NonNull ItemCartBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
