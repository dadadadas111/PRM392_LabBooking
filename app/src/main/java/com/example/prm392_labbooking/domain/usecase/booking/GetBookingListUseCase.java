package com.example.prm392_labbooking.domain.usecase.booking;

import com.example.prm392_labbooking.domain.repository.BookingRepository;
import com.example.prm392_labbooking.data.remote.dto.BookingDto;
import java.util.List;
import retrofit2.Call;

public class GetBookingListUseCase {
    private final BookingRepository bookingRepository;

    public GetBookingListUseCase(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public Call<List<BookingDto>> execute() {
        return bookingRepository.getBookings();
    }
}
