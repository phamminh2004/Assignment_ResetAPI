package fpoly.minhpt.assignment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import fpoly.minhpt.assignment.databinding.ItemBillBinding;
import fpoly.minhpt.assignment.model.Bill;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.ViewHolder> {
    private final Context context;
    private final List<Bill> list;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    DecimalFormat decimalFormat = new DecimalFormat("#,###");
    Boolean status;

    public BillAdapter(Context context, List<Bill> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBillBinding binding = ItemBillBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Bill item = list.get(position);
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

        Date currentDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(item.date);
        cal.add(Calendar.DAY_OF_MONTH, 3);
        Date datePlus3 = cal.getTime();
        if (datePlus3.before(currentDate)) {
            holder.binding.tvStatus.setText("Đã thanh toán");
            holder.binding.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.green));
            item.status = true;
            apiService.updateBill(item._id, item).enqueue(new Callback<Bill>() {
                @Override
                public void onResponse(Call<Bill> call, Response<Bill> response) {

                }

                @Override
                public void onFailure(Call<Bill> call, Throwable t) {

                }
            });
        }

        holder.binding.tvId.setText("ID: " + item._id);
        holder.binding.tvPrice.setText("Giá: đ" + decimalFormat.format(item.price));
        holder.binding.tvNameCustomer.setText("Họ tên: " + item.nameCustomer);
        holder.binding.tvAddress.setText("Địa chỉ: " + item.address);
        holder.binding.tvPhone.setText("SĐT: " + item.phone);
        holder.binding.tvDate.setText("Ngày đặt hàng: " + sdf.format(item.date));
        holder.binding.tvStatus.setText("Chưa thanh toán");
        if (item.status == true) {
            holder.binding.tvStatus.setText("Đã thanh toán");
            holder.binding.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.green));
        } else {
            holder.binding.tvStatus.setText("Chưa thanh toán");
            holder.binding.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.red));
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemBillBinding binding;

        public ViewHolder(@NonNull ItemBillBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
