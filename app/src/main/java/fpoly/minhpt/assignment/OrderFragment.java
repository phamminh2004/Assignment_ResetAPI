package fpoly.minhpt.assignment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import fpoly.minhpt.assignment.databinding.FragmentOrderBinding;
import fpoly.minhpt.assignment.model.Bill;
import fpoly.minhpt.assignment.model.Cart;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class OrderFragment extends Fragment {
    private final String REGEX_PHONE_NUMBER = "^[0-9\\-\\+]{9,15}$";
    DecimalFormat decimalFormat = new DecimalFormat("#,###");
    FragmentOrderBinding binding;
    OrderAdapter adapter;
    List<Cart> list;
    int price, totalPrice;
    String nameCustomer, address, phone;
    Date currentDate;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM");


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOrderBinding.inflate(inflater, container, false);
        list = new ArrayList<>();
        currentDate = new Date();
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(RecyclerView.VERTICAL);
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
                list = response.body();
                for (Cart cart : list) {
                    price += cart.Price;
                }
                adapter = new OrderAdapter(getContext(), list);
                binding.rvProduct.setAdapter(adapter);
                binding.tvPriceShoe.setText("đ" + decimalFormat.format(price));
                int totalPrice = price + 30000;
                binding.tvPriceTotal.setText("đ" + decimalFormat.format(totalPrice));
                binding.tvTotal.setText("đ" + decimalFormat.format(totalPrice));
            }

            @Override
            public void onFailure(Call<List<Cart>> call, Throwable t) {

            }
        });
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, 3);
        Date dateAfterThreeDays = calendar.getTime();
        binding.tvDate.setText("Nhận hàng vào " + sdf.format(dateAfterThreeDays));

        binding.tvSubmit.setOnClickListener(v -> {
            nameCustomer = binding.edtNameCustomer.getText().toString();
            address = binding.edtAddress.getText().toString();
            phone = binding.edtPhone.getText().toString();
            totalPrice = price + 30000;
            if (TextUtils.isEmpty(nameCustomer) || TextUtils.isEmpty(address) || TextUtils.isEmpty(phone)) {
                Toast.makeText(getContext(), "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
            } else if (!phone.matches(REGEX_PHONE_NUMBER)) {
                Toast.makeText(getContext(), "Số điện thoại sai định dạng", Toast.LENGTH_SHORT).show();
            } else {
                Bill bill = new Bill();
                bill.nameCustomer = nameCustomer;
                bill.address = address;
                bill.phone = phone;
                bill.price = totalPrice;
                bill.date = new Date();
                bill.status = false;
                apiService.postBill(bill).enqueue(new Callback<Bill>() {
                    @Override
                    public void onResponse(Call<Bill> call, Response<Bill> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getContext(), "Thành công", Toast.LENGTH_SHORT).show();
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, new BillFragment()).addToBackStack(null).commit();
                        }
                    }

                    @Override
                    public void onFailure(Call<Bill> call, Throwable t) {

                    }
                });
            }
            for (Cart cart : list) {
                apiService.deleteCart(cart._id).enqueue(new Callback<Cart>() {
                    @Override
                    public void onResponse(Call<Cart> call, Response<Cart> response) {

                    }

                    @Override
                    public void onFailure(Call<Cart> call, Throwable t) {

                    }
                });
            }
        });
        return binding.getRoot();
    }
}