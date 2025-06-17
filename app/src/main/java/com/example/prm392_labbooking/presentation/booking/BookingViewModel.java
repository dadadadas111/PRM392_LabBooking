package com.example.prm392_labbooking.presentation.booking;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.prm392_labbooking.data.remote.dto.BookingDto;
import com.example.prm392_labbooking.domain.usecase.booking.GetBookingListUseCase;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingViewModel extends ViewModel {
    private final GetBookingListUseCase getBookingListUseCase;
    private final MutableLiveData<List<BookingDto>> bookings = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> error = new MutableLiveData<>(null);

    public BookingViewModel(GetBookingListUseCase getBookingListUseCase) {
        this.getBookingListUseCase = getBookingListUseCase;
    }

    public LiveData<List<BookingDto>> getBookings() { return bookings; }
    public LiveData<Boolean> isLoading() { return loading; }
    public LiveData<String> getError() { return error; }

    public void fetchBookings() {
        loading.setValue(true);
        getBookingListUseCase.execute().enqueue(new Callback<List<BookingDto>>() {
            @Override
            public void onResponse(Call<List<BookingDto>> call, Response<List<BookingDto>> response) {
                loading.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    bookings.setValue(response.body());
                } else {
                    error.setValue("Lỗi tải danh sách đặt chỗ");
                }
            }
            @Override
            public void onFailure(Call<List<BookingDto>> call, Throwable t) {
                loading.setValue(false);
                error.setValue(t.getMessage());
            }
        });
    }
}
