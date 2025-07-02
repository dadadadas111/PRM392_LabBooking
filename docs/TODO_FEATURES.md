# 📋 PRM392 Lab Booking - Feature Implementation Plan
**Date Created:** June 25, 2025  
**Implementation Target:** Tomorrow Morning

---

## 🚀 **PRIORITY FEATURES TO IMPLEMENT**

### 1. 💬 **Customer Support Chat System**
**Status:** 🔄 Planned  
**Priority:** High  

#### **📋 Requirements:**
- [ ] **Dual Chat Storage:** Save messages to Firebase (like current AI chat)
- [ ] **MQTT Integration:** Send/receive messages via MQTT endpoint (keep secret)
- [ ] **Real-time UI Updates:** Update chat UI when receiving MQTT messages
- [ ] **Firebase Sync:** Ensure MQTT and Firebase are synchronized
- [ ] **Demo Backend Simulation:** MQTT serves as fake backend for demo

#### **🔧 Technical Implementation:**
- [ ] **Enhance existing `ChatActivity`** - Add MQTT to the existing support tab
- [ ] Implement MQTT client service
- [ ] Add MQTT message models
- [ ] Enhance existing Firebase chat collections for support
- [ ] Add real-time MQTT message listeners to support tab
- [ ] Implement message encryption/security
- [ ] Add offline message queuing
- [ ] Update `SupportChatRepository` to use MQTT + Firebase dual storage

#### **📁 Files to Create/Modify:**
```
- presentation/chat/ChatActivity.java (enhance existing support tab functionality)
- services/MqttService.java (new background service)
- domain/model/SupportMessage.java (new model, similar to ChatMessage)
- data/repository/SupportChatRepositoryImpl.java (new, follows ChatRepositoryImpl pattern)
- domain/repository/SupportChatRepository.java (new interface)
- utils/MqttUtils.java (connection management utilities)
- data/firebase/SupportChatServiceImpl.java (new, separate from existing AI chat)
- data/firebase/SupportChatService.java (new interface)
```

---

### 2. 🏠 **Home Fragment - Product List**
**Status:** 🔄 Planned  
**Priority:** High  

#### **📋 Requirements:**
- [ ] **Code Merge:** Integrate teammate's product list code
- [ ] **UI Improvements:** Polish and fix design issues
- [ ] **Architecture Integration:** Ensure follows current MVVM pattern
- [ ] **Firebase Integration:** Connect to product data
- [ ] **Performance Optimization:** Implement caching and preloading

#### **🔧 Technical Implementation:**
- [ ] **Merge product list code** into existing `HomeFragment` 
- [ ] Create product models and repositories with Firebase integration
- [ ] Implement RecyclerView with ViewBinding
- [ ] Add product image loading (Glide)
- [ ] Implement search and filtering
- [ ] Add product categories with filtering
- [ ] Implement pull-to-refresh
- [ ] **Create ViewModel** following existing MVVM pattern
- [ ] **Add Firebase Collections** for products (name: "lab_products")
- [ ] **Connect to PreloadService** for better performance

#### **📁 Files to Create/Modify:**
```
- presentation/home/HomeFragment.java (major enhancement - currently minimal)
- presentation/home/HomeViewModel.java (new, follows existing MVVM pattern)
- presentation/home/ProductListAdapter.java (new RecyclerView adapter)
- domain/model/Product.java (new model for lab packages/products)
- domain/repository/ProductRepository.java (new interface)
- data/repository/ProductRepositoryImpl.java (follows existing repository patterns)
- data/firebase/ProductServiceImpl.java (new Firebase service)
- data/firebase/ProductService.java (new interface)
- domain/usecase/product/GetProductListUseCase.java (follows existing use case patterns)
- res/layout/fragment_home.xml (major redesign - currently basic)
- res/layout/item_product.xml (new for RecyclerView items)
```

---

### 3. 🔌 **Offline Support & Network Handling**
**Status:** 🔄 Planned  
**Priority:** Critical  

#### **📋 Requirements:**
- [ ] **Network Detection:** Monitor internet connectivity
- [ ] **Graceful Degradation:** Show appropriate offline UI
- [ ] **Crash Prevention:** Handle network failures without crashes
- [ ] **Offline Data:** Cache essential data locally
- [ ] **Sync on Reconnect:** Update data when back online

#### **🔧 Technical Implementation:**
- [ ] Create `NetworkMonitor` service
- [ ] Implement offline state management
- [ ] Add offline indicators in UI
- [ ] Create offline-friendly repositories
- [ ] Implement local data caching
- [ ] Add retry mechanisms
- [ ] Create offline placeholders

#### **📁 Files to Create/Modify:**
```
- services/NetworkMonitor.java (new service for connectivity monitoring)
- utils/NetworkUtils.java (connectivity helper methods)
- utils/OfflineManager.java (manages offline state and caching)
- presentation/base/BaseFragment.java (enhance with offline handling capabilities)
- presentation/base/BaseActivity.java (enhance with network state management)
- All Repository classes (add offline-first approach and caching)
- res/layout/offline_placeholder.xml (UI for offline state)
- res/layout/network_error_layout.xml (error state UI)
```

---

## 🛠 **TECHNICAL ARCHITECTURE UPDATES**

### **PreloadService Enhancements:**
- [ ] Add MQTT client preloading to existing `PreloadService.java`
- [ ] Add offline data preloading (essential product data)
- [ ] Add network status monitoring
- [ ] **Integrate with existing** `PreloadManager.java` and `PreloadUtils.java`
- [ ] Update `LabBookingApp.java` to include new preload services

### **Repository Pattern Enhancements:**
- [ ] Add offline-first approach to existing repositories
- [ ] Implement data synchronization between Firebase and local storage
- [ ] Add caching strategies using Room database or SharedPreferences
- [ ] **Maintain consistency** with existing `AuthRepositoryImpl` and `ChatRepositoryImpl`

### **Error Handling:**
- [ ] Network error handling across all activities
- [ ] MQTT connection failures with retry mechanisms
- [ ] Firebase offline scenarios
- [ ] **Integrate with existing** `BaseActivity` loading system

---

## 📦 **DEPENDENCIES TO ADD**

### **MQTT Client:**
```gradle
implementation 'org.eclipse.paho:org.eclipse.paho.client.mqttv3:1.2.5'
implementation 'org.eclipse.paho:org.eclipse.paho.android.service:1.1.1'
```

### **Network Monitoring:**
```gradle
implementation 'androidx.work:work-runtime:2.8.1'
implementation 'androidx.lifecycle:lifecycle-service:2.6.2'
```

### **Image Loading (if not already added):**
```gradle
implementation 'com.github.bumptech.glide:glide:4.15.1'
annotationProcessor 'com.github.bumptech.glide:compiler:4.15.1'
```

### **Local Database (for offline support):**
```gradle
implementation 'androidx.room:room-runtime:2.5.0'
annotationProcessor 'androidx.room:room-compiler:2.5.0'
implementation 'androidx.room:room-ktx:2.5.0'
```

### **Additional Permissions Needed:**
```xml
<!-- Add to AndroidManifest.xml -->
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.WAKE_LOCK" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />

<!-- Services -->
<service android:name="org.eclipse.paho.android.service.MqttService"/>
```

---

## 🔧 **IMPLEMENTATION ROADMAP**

### **Phase 1: Customer Support Chat (Priority 1)**
1. **Day 1 Morning:**
   - Create MQTT service and connection utilities
   - Set up SupportMessage model and Firebase service
   - Enhance ChatActivity support tab with MQTT integration
   
2. **Day 1 Afternoon:**
   - Implement dual storage (Firebase + MQTT) 
   - Add offline message queuing
   - Test real-time message synchronization

### **Phase 2: Product List Implementation (Priority 2)**
1. **Day 1 Evening:**
   - Create Product models and Firebase collections
   - Implement ProductRepository and UseCase classes
   - Design and implement HomeFragment UI
   
2. **Day 2 Morning:**
   - Add product list adapter with image loading
   - Implement search and filtering functionality
   - Connect to PreloadService for better performance

### **Phase 3: Offline Support (Priority 3)**
1. **Day 2 Afternoon:**
   - Implement NetworkMonitor service
   - Add offline state management to base classes
   - Create offline UI components and error handling
   
2. **Day 2 Evening:**
   - Add local caching with Room database
   - Implement sync mechanisms for online/offline transitions
   - Final testing and integration verification

---

## 🔒 **SECURITY CONSIDERATIONS**

- [ ] **MQTT Credentials:** Store in encrypted SharedPreferences or BuildConfig
- [ ] **Message Encryption:** Encrypt sensitive chat messages
- [ ] **Network Security:** Use SSL/TLS for MQTT connections
- [ ] **Firebase Rules:** Update security rules for new collections

---

## 🧪 **TESTING CHECKLIST**

### **Customer Support Chat:**
- [ ] Message sending/receiving
- [ ] MQTT connection stability
- [ ] Firebase synchronization
- [ ] Offline message queuing

### **Product List:**
- [ ] Data loading performance
- [ ] Image loading efficiency
- [ ] Search functionality
- [ ] Offline behavior

### **Network Handling:**
- [ ] Airplane mode scenarios
- [ ] Poor network conditions
- [ ] Connection recovery
- [ ] UI state management

---

## 📊 **IMPLEMENTATION ORDER**

### **Phase 1: Foundation (Morning)**
1. Network monitoring setup
2. Offline handling framework
3. Update PreloadService

### **Phase 2: Features (Afternoon)**
1. Customer support chat
2. MQTT integration
3. Home fragment updates

### **Phase 3: Polish (Evening)**
1. UI improvements
2. Error handling
3. Testing and debugging

---

## 📝 **NOTES & REMINDERS**

### **Architecture Consistency:**
- **Keep MQTT endpoint secret** - Store securely in BuildConfig or encrypted SharedPreferences (similar to existing Gemini API key handling in `SecretLoader`)
- **Maintain current architecture** - Follow existing MVVM pattern seen in auth, chat, and booking modules
- **Integrate with existing services** - Enhance current `PreloadService.java`, `PreloadManager.java`, and `PreloadUtils.java`
- **Follow current patterns** - Use existing `BaseActivity`, `AuthRequiredActivity`, and `BaseFragment` inheritance patterns
- **Maintain dual storage approach** - Similar to current chat (Firebase + Gemini AI), implement Firebase + MQTT for support
- **Consistency with navigation** - Use existing `NavigationManager` patterns for fragment transitions and activity launches

### **Current Code Integration:**
- **ChatActivity enhancement** - Support tab already exists; enhance with MQTT functionality while keeping AI chatbot tab unchanged
- **HomeFragment transformation** - Currently minimal implementation (only inflates layout); perfect for complete product list implementation
- **Repository consistency** - Follow patterns from existing `ChatRepositoryImpl` and `AuthRepositoryImpl` for consistency
- **Firebase structure** - Add new collections ("support_chat_histories", "lab_products") alongside existing "chat_histories"
- **Service integration** - Enhance existing `PreloadService` with MQTT and offline data preloading capabilities
- **Base class usage** - Leverage existing `BaseActivity` and `BaseFragment` for consistent loading states and error handling

### **Technical Implementation Details:**

#### **MQTT Integration Pattern:**
```java
// Follow existing SecretLoader pattern for API keys
public class SecretLoader {
    public static String getMqttEndpoint(Context context) { ... }
    public static String getMqttUsername(Context context) { ... }
    public static String getMqttPassword(Context context) { ... }
}

// Similar to existing ChatRepositoryImpl pattern
public class SupportChatRepositoryImpl implements SupportChatRepository {
    private final SupportChatService firebaseService;
    private final MqttService mqttService;
    // Dual storage implementation like current chat
}
```

#### **PreloadService Enhancement:**
```java
// Add to existing PreloadService.java
private void preloadMqttConnection() {
    // Initialize MQTT client connection
}

private void preloadOfflineData() {
    // Cache essential product data
}
```

#### **Network Monitoring Integration:**
```java
// Integrate with existing BaseActivity pattern
public abstract class BaseActivity extends AppCompatActivity {
    private NetworkMonitor networkMonitor;
    // Add network state management to existing loading system
}
```

---

**📅 Target Completion:** Tomorrow Evening  
**🎯 Goal:** Fully functional demo with real-time chat, product list, and offline support

### **Testing Priorities:**
- **Test offline scenarios thoroughly** - Critical for user experience
- **Document MQTT protocol** - For future maintenance  
- **Backup current code** - Before major merges
- **Test integration** - Ensure new features don't break existing chat/auth/map functionality
- **Test preloading performance** - Verify PreloadService enhancements don't impact startup time
- **Test dual storage sync** - Ensure Firebase and MQTT stay synchronized
- **Test network transitions** - Verify smooth offline-to-online transitions
