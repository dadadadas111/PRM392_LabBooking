package com.example.prm392_labbooking.domain.repository;

import com.example.prm392_labbooking.data.remote.dto.BookingDto;
import java.util.List;
import retrofit2.Call;

public interface BookingRepository {
    Call<List<BookingDto>> getBookings();
}
