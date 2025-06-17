// package com.example.prm392_labbooking.navigation;

// import android.app.Activity;
// import android.content.Context;
// import android.content.Intent;

// import com.example.prm392_labbooking.presentation.auth.LoginActivity;
// import com.example.prm392_labbooking.presentation.auth.RegisterActivity;
// import com.example.prm392_labbooking.presentation.booking.BookingActivity;
// import com.example.prm392_labbooking.presentation.cart.CartActivity;
// import com.example.prm392_labbooking.presentation.chat.ChatActivity;
// import com.example.prm392_labbooking.presentation.map.MapActivity;

// /**
//  * Centralized navigation manager for app-wide navigation.
//  * Usage: NavigationManager.goToLogin(context);
//  */
// public class NavigationManager {
//     public static void goToLogin(Context context) {
//         context.startActivity(new Intent(context, LoginActivity.class));
//     }

//     public static void goToRegister(Context context) {
//         context.startActivity(new Intent(context, RegisterActivity.class));
//     }

//     public static void goToBooking(Context context) {
//         context.startActivity(new Intent(context, BookingActivity.class));
//     }

//     public static void goToCart(Context context) {
//         context.startActivity(new Intent(context, CartActivity.class));
//     }

//     public static void goToChat(Context context) {
//         context.startActivity(new Intent(context, ChatActivity.class));
//     }

//     public static void goToMap(Context context) {
//         context.startActivity(new Intent(context, MapActivity.class));
//     }

//     public static void goBack(Activity activity) {
//         activity.finish();
//     }
// }
