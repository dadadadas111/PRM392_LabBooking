# Project Structure Overview
This document outlines the recommended structure for your Android project, focusing on clean architecture principles. The goal is to separate concerns, making the codebase maintainable and scalable.

### 📁 `data/`

Handles data access — from remote APIs, Firebase, or local sources.

* 📁 `remote/` – Retrofit API clients, DTOs

  * `ApiClient.java` – Retrofit instance
  * `BookingApiService.java` – Endpoints interface
  * `BookingDto.java` – Remote model classes
* 📁 `firebase/` – Firebase-specific access

  * `FirebaseAuthService.java`
  * `FirebaseDatabaseService.java`
* 📁 `local/` – SQLite/Room DB access

  * `AppDatabase.java`
  * `CartDao.java`
* 📁 `repository/` – Mediates between data sources and domain layer

  * `BookingRepository.java`
  * `AuthRepository.java`

---

### 📁 `domain/`

Holds use cases/business logic and clean models.

* 📁 `model/`

  * `User.java`
  * `Booking.java`
  * `Product.java`
* 📁 `usecase/`

  * `LoginUseCase.java`
  * `ReserveSeatUseCase.java`
  * `GetProductListUseCase.java`

---

### 📁 `presentation/`

Everything UI-related.

* 📁 `auth/`

  * `LoginActivity.java`
  * `RegisterActivity.java`
  * `AuthViewModel.java`
* 📁 `booking/`

  * `BookingActivity.java`
  * `BookingViewModel.java`
* 📁 `cart/`

  * `CartActivity.java`
  * `CartViewModel.java`
* 📁 `map/`

  * `MapActivity.java`
  * `MapViewModel.java`
* 📁 `chat/`

  * `ChatActivity.java`
  * `ChatViewModel.java`
* 📁 `components/` – Reusable UI components (custom views, adapters)

  * `ProductAdapter.java`
  * `FacilityChip.java`

---

### 📁 `di/`

Dependency injection setup (if you're using Hilt/Dagger later).

* `AppModule.java`

---

### 📁 `utils/`

Helper classes

* `Validators.java`
* `Constants.java`
* `DateUtils.java`

---

### 📁 `navigation/`

Navigation logic (could be handled manually or via Jetpack Navigation).

* `Navigator.java`

---

### 🔹 Notes

* Use `ViewModel` for state management.
* Avoid business logic in Activities or Fragments.
* Keep Firebase APIs confined to `data/firebase`, and map them to domain models in `repository`.
* When you clarify backend-vs-Firebase roles, just add/remove layers in `data`.
