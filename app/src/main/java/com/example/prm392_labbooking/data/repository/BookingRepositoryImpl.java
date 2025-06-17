package com.example.prm392_labbooking.data.repository;

import com.example.prm392_labbooking.data.remote.BookingApiService;
import com.example.prm392_labbooking.data.remote.dto.BookingDto;
import com.example.prm392_labbooking.domain.repository.BookingRepository;
import java.util.List;
import retrofit2.Call;

public class BookingRepositoryImpl implements BookingRepository {
    private final BookingApiService apiService;

    public BookingRepositoryImpl(BookingApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public Call<List<BookingDto>> getBookings() {
        return apiService.getBookings();
    }
}
