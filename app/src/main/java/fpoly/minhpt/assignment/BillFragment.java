package fpoly.minhpt.assignment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import fpoly.minhpt.assignment.databinding.FragmentBillBinding;
import fpoly.minhpt.assignment.model.Bill;
import fpoly.minhpt.assignment.model.Cart;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BillFragment extends Fragment {
    FragmentBillBinding binding;
    BillAdapter adapter;
    List<Bill> list;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBillBinding.inflate(inflater, container, false);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(RecyclerView.VERTICAL);
        binding.rvBill.setLayoutManager(manager);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIService.DOMAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService apiService = retrofit.create(APIService.class);
        Call<List<Bill>> call = apiService.getBill();
        call.enqueue(new Callback<List<Bill>>() {
            @Override
            public void onResponse(Call<List<Bill>> call, Response<List<Bill>> response) {
                list = response.body();
                adapter = new BillAdapter(getContext(), list);
                binding.rvBill.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Bill>> call, Throwable t) {

            }
        });
        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ArrayList<Bill> templist = new ArrayList<>();
                try {
                    if (s.toString().trim() != "") {
                        for (Bill bill : list) {
                            if (String.valueOf(bill._id).contains(String.valueOf(s))) {
                                templist.add(bill);
                            }
                        }
                        adapter = new BillAdapter(getContext(), templist);
                        binding.rvBill.setAdapter(adapter);
                    }
                } catch (Exception e) {
                    Log.e("TAG", "Lỗi tìm kiếm" + e.getMessage());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return binding.getRoot();
    }
}