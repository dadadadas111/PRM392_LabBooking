# 🧪 LAB Booking - Android Application

## 📌 Objective

Build an Android application that allows users to book **seats and tables** in a laboratory (LAB) environment. The app is developed by a team of 4–5 beginner Android developers and focuses on demonstrating key Android development skills through working, functional features.

---

## 🧭 Scope

The app enables users to:

- Reserve **seat packages** (1–6 seats).
- Reserve **table packages**:
  - 4 seats – 1 table
  - 6 seats – 1 table
  - 12 seats – 2 tables
- Choose additional **facilities**:
  - Whiteboard
  - Television
  - Network (for telemeetings)

**Note:** Backend functionality can be mocked using Firebase Emulator, MockAPI, or local SQLite. Focus is placed on **client-side mobile development**.

---

## ✅ Functional Requirements

### 🔐 Authentication Screen
- Sign up with email/password
- Login with existing credentials (email/password or Google)
- Password reset option
- Validate input (email format, password rules)
- Store user data securely (Firebase)

### 📋 Package List Screen
- Display available **seat** and **table** packages
- Show details (name, capacity)
- Tap to view more info

### 📦 Package Details Screen
- Show package information
- Let users select additional facilities
- Add to cart

### 🛒 Cart Screen
- Show selected packages + facilities
- Display price/summary
- Allow item removal or proceed to checkout

### 💳 Billing/Checkout Screen
- Display booking summary
- Input payment/confirmation details (mocked)
- Confirm booking and show result

### 🔔 Notification Feature
- On app launch: if cart is not empty, show alert via `Toast` or `Snackbar`

### 🗺️ Map Screen
- Display LAB location with a marker
- Support zoom and pan
- Fixed coordinates using Google Maps API

### 💬 Chat Screen
- Simple user-to-salesman messaging interface
- Simulated replies (via Firebase or local logic)
- Real-time updates

---

## 🧱 Tech Stack

| Layer           | Tech Used                          |
|----------------|-------------------------------------|
| Language        | Java                               |
| UI Components   | Android Views, RecyclerView, XML   |
| State Mgmt      | ViewModel, LiveData (optional)     |
| Data Storage    | Firebase / SQLite                  |
| Network         | Retrofit (for mocked APIs)         |
| Maps            | Google Maps API                    |
| Notifications   | Toast, Snackbar                    |

---

## 📦 Deliverables

- 📁 Android Studio Project Source Code
- 📱 APK file (debug or release)
- 📄 User Interface and Analysis Document
- 📘 README + Notes

---

## 🧪 Development Notes

- Authentication and chat can use **Firebase**.
- Backend API calls should be mocked via **MockAPI**, **Postman mock server**, or static JSON.
- Project emphasizes **clean architecture**, modular design, and separation of concerns.

---

