package com.example.prm392_labbooking.data.remote;

import com.example.prm392_labbooking.data.remote.dto.BookingDto;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface BookingApiService {
    @GET("bookings")
    Call<List<BookingDto>> getBookings();
}
