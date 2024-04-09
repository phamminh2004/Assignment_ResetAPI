package fpoly.minhpt.assignment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;

import fpoly.minhpt.assignment.databinding.FragmentUserBinding;
import fpoly.minhpt.assignment.model.Bill;
import fpoly.minhpt.assignment.model.User;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class UserFragment extends Fragment {
    FragmentUserBinding binding;
    FirebaseAuth auth;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    String id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUserBinding.inflate(inflater, container, false);
        auth = FirebaseAuth.getInstance();
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
        Call<List<User>> call = apiService.getUser();
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                    List<User> list = response.body();
                    for (User u : list) {
                        if (u.email.equals(auth.getCurrentUser().getEmail())) {
                            id = u._id;
                            binding.edtEmail.setText(u.email);
                            binding.edtName.setText(u.name);
                            if (u.date != null) {
                                binding.edtDate.setText(sdf.format(u.date));
                            } else {
                            }
                            binding.edtAddress.setText(u.address);
                            binding.edtPhone.setText(u.phone);
                            if (u.gender != null) {
                                if (u.gender == true) {
                                    binding.rdMale.setChecked(true);
                                } else {
                                    binding.rdFemale.setChecked(true);
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {

            }
        });
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    User user = new User();
                    user.name = binding.edtName.getText().toString().trim();
                    user.date = sdf.parse(binding.edtDate.getText().toString().trim());
                    user.address = binding.edtAddress.getText().toString().trim();
                    user.phone = binding.edtPhone.getText().toString().trim();
                    if (binding.rdMale.isChecked()) {
                        user.gender = true;
                    } else if (binding.rdFemale.isChecked()) {
                        user.gender = false;
                    }
                    apiService.updateUser(id, user).enqueue(new Callback<Bill>() {
                        @Override
                        public void onResponse(Call<Bill> call, Response<Bill> response) {
                            Toast.makeText(getContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, new HomeFragment()).addToBackStack(null).commit();
                        }

                        @Override
                        public void onFailure(Call<Bill> call, Throwable t) {

                        }
                    });
                } catch (Exception e) {

                }

            }
        });

        return binding.getRoot();
    }
}