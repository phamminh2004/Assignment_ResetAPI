package fpoly.minhpt.assignment;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import fpoly.minhpt.assignment.databinding.ItemOrderBinding;
import fpoly.minhpt.assignment.model.Cart;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private final Context context;
    private final List<Cart> list;
    DecimalFormat decimalFormat = new DecimalFormat("#,###");

    public OrderAdapter(Context context, List<Cart> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOrderBinding binding = ItemOrderBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cart cart = list.get(position);
        holder.binding.tvName.setText(cart.ProductName);
        holder.binding.tvPrice.setText("đ" + decimalFormat.format(cart.Price));
        String img = cart.Image;
        try {
            Picasso.get().load(img).placeholder(R.drawable.ic_camera).error(R.drawable.ic_camera)
                    .into(holder.binding.ivImg);
        } catch (Exception e) {
            Log.e("PicassoError", "Error loading image: " + e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemOrderBinding binding;

        public ViewHolder(@NonNull ItemOrderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
