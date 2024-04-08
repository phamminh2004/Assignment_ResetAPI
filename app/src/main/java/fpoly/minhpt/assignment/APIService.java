package fpoly.minhpt.assignment;

import java.util.List;

import fpoly.minhpt.assignment.model.Bill;
import fpoly.minhpt.assignment.model.Cart;
import fpoly.minhpt.assignment.model.Product;
import fpoly.minhpt.assignment.model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface APIService {
    String DOMAIN = "http://192.168.1.100:3000/";

    @GET("/api/product")
    Call<List<Product>> getProduct();

    @POST("/api/product")
    Call<Product> postProduct(@Body Product product);

    @PUT("/api/product/{id}")
    Call<Product> updateProduct(@Path("id") String prdId, @Body Product product);

    @DELETE("/api/product/{id}")
    Call<Product> deleteProduct(@Path("id") String id);


    @GET("api/cart")
    Call<List<Cart>> getCart();

    @POST("/api/cart")
    Call<Cart> postCart(@Body Cart cart);

    @PUT("/api/cart/{id}")
    Call<Cart> updateCart(@Path("id") String cartId, @Body Cart cart);

    @DELETE("/api/cart/{id}")
    Call<Cart> deleteCart(@Path("id") String id);


    @GET("/api/bill")
    Call<List<Bill>> getBill();

    @POST("/api/bill")
    Call<Bill> postBill(@Body Bill cart);

    @PUT("/api/bill/{id}")
    Call<Bill> updateBill(@Path("id") String billId, @Body Bill bill);

    @DELETE("/api/bill/{id}")
    Call<Bill> deleteBill(@Path("id") String id);

    @GET("/api/user")
    Call<List<User>> getUser();
    @POST("/api/user")
    Call<Bill> postUser(@Body User user);

    @PUT("/api/user/{id}")
    Call<Bill> updateUser(@Path("id") String userId, @Body User user);
}
