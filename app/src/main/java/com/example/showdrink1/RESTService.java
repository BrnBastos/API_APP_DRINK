package com.example.showdrink1;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RESTService {

    @GET("{strDrink}")
    Call<DrinkGit> consultaDrink(@Path("strDrink")String Drink);

}
