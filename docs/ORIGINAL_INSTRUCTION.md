# Android Booking Application

## Objective
Develop an Android application for booking seats and tables in a laboratory (LAB) environment. This project is designed for a team of 4-5 beginner Android developers. The application facilitates reservations of seat and table packages with additional facilities (whiteboard, television, network for telemeetings). Evaluation focuses on functionality, with specific screens demonstrating core Android development skills.

## Project Scope
The application enables users to:
- Reserve seat packages (1, 2, 3, 4, 5, or 6 seats).
- Reserve table setting packages (4 seats-1 table, 6 seats-1 table, 12 seats-2 tables).
- Select additional facilities (whiteboard, television, network).

Functionality is prioritized, with screens for user management, package selection, booking, payment, notifications, maps, and chat.

## Functional Requirements
The application must include the following screens and features:

### 1. Sign Up/Login Screen
**Purpose:** Manage user authentication.

**Features:**
- Sign up with email and password.
- Login with credentials.
- Securely store user data (e.g., SQLite, Firebase).
- Validate inputs (email format, password length).

**Knowledge:** EditText, Button, SQLite/Firebase, input validation.

---

### 2. List of Products Screen
**Purpose:** Display available packages.

**Features:**
- List seat (1-6 seats) and table packages (4-seat, 6-seat, 12-seat).
- Show basic details (name, capacity).
- Navigate to details on selection.

**Knowledge:** RecyclerView, Intents, data binding.

---

### 3. Product Details Screen
**Purpose:** Show package details.

**Features:**
- Display package info (seats/tables, facilities, duration).
- Select facilities (whiteboard, TV, network).
- Add to cart.

**Knowledge:** TextView, CheckBox, Intents, event handling.

---

### 4. Product Cart Screen
**Purpose:** Show selected packages.

**Features:**
- List selected packages and facilities.
- Show summary/total.
- Remove items or proceed to billing.

**Knowledge:** RecyclerView, SQLite, navigation.

---

### 5. Billing Screen
**Purpose:** Process booking/payment.

**Features:**
- Show booking summary.
- Collect payment/confirmation details.
- Confirm and save booking.
- Display success/failure.

**Knowledge:** TextView, EditText, SQLite/Firebase, validation.

---

### 6. Notification Feature
**Purpose:** Alert if cart has items on app launch.

**Features:**
- Check cart on launch.
- Show notification (Toast/Snackbar) if cart is not empty.

**Knowledge:** Notification Manager, Toast, lifecycle methods.

---

### 7. Map Screen
**Purpose:** Show LAB location.

**Features:**
- Display map with LAB marker (fixed coordinates).
- Support zoom/pan.

**Knowledge:** Google Maps API, permissions, MapView.

---

### 8. Chat Screen
**Purpose:** Enable customer-salesman chat.

**Features:**
- Simple chat interface for sending/receiving messages.
- Simulate salesman replies (Firebase or local).

**Knowledge:** RecyclerView, EditText, Firebase, real-time updates.

---

## Notes
- Focus on Mobile Client Development. The back-end part should be mocked by using tools such as Firebase Emulator Suite (Firestore + Auth), MockAPI, or just SQLite database, etc.

## Deliverable Package Includes
- Android Project Source Code
- Android Application Package (APK)
- Analysis and User Interface Design Document
- Others