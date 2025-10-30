# 🚀 HƯỚNG DẪN TÍCH HỢP GITHUB VÀ FACEBOOK OAUTH2 LOGIN

## 📋 MỤC LỤC
1. [Tổng quan về mục tiêu](#1-tổng-quan-về-mục-tiêu)
2. [Chuẩn bị trước khi bắt đầu](#2-chuẩn-bị-trước-khi-bắt-đầu)
3. [Các bước chi tiết](#3-các-bước-chi-tiết)
4. [Luồng hoạt động của OAuth2](#4-luồng-hoạt-động-của-oauth2)
5. [Tổng kết các file đã sửa đổi](#5-tổng-kết-các-file-đã-sửa-đổi)

---

## 1. TỔNG QUAN VỀ MỤC TIÊU

### 🎯 Mục tiêu lớn
Thêm tính năng đăng nhập bằng **GitHub** và **Facebook** vào hệ thống hiện tại, dựa trên logic Google OAuth2 đã có sẵn.

### 📊 Phân tích hiện trạng
Bạn đã có:
- ✅ Google OAuth2 login hoàn chỉnh
- ✅ SecurityConfiguration với oauth2Login
- ✅ AuthService với phương thức `registerOauthUser()`
- ✅ HttpCookieOAuth2AuthorizationRequestRepository
- ✅ Success/Failure handlers
- ✅ Frontend OAuthButtons component
- ✅ Frontend oauth.config.jsx

### 🎯 Mục tiêu chi tiết

#### **Mục tiêu 1: Đăng ký ứng dụng với GitHub và Facebook**
- Tạo GitHub OAuth App
- Tạo Facebook App
- Lấy Client ID và Client Secret

#### **Mục tiêu 2: Cấu hình Backend**
- Thêm GitHub và Facebook vào `application.properties`
- Không cần sửa SecurityConfiguration (đã hỗ trợ multi-provider)

#### **Mục tiêu 3: Xử lý dữ liệu từ providers**
- GitHub trả về `login`, `name`, `email`
- Facebook trả về `id`, `name`, `email`
- Mapping thống nhất sang User entity

#### **Mục tiêu 4: Cập nhật Frontend**
- Frontend đã sẵn sàng (oauth.config.jsx đã có GitHub và Facebook)
- Chỉ cần đảm bảo authUrl đúng

---

## 2. CHUẨN BỊ TRƯỚC KHI BẮT ĐẦU

### 📦 Yêu cầu
- ✅ Spring Boot OAuth2 Client dependency (đã có trong build.gradle.kts)
- ✅ Apache Commons Lang3 (đã có)
- ✅ Internet connection để tạo apps

### 🔑 Thông tin cần thu thập
1. **GitHub OAuth App**
   - Client ID
   - Client Secret
   - Redirect URI: `http://localhost:8080/api/v1/auth/oauth2/callback/github`

2. **Facebook App**
   - App ID (Client ID)
   - App Secret (Client Secret)
   - Redirect URI: `http://localhost:8080/api/v1/auth/oauth2/callback/facebook`

---

## 3. CÁC BƯỚC CHI TIẾT

### 📍 **BƯỚC 1: Tạo GitHub OAuth App**

#### **Mục đích:** Đăng ký ứng dụng với GitHub để lấy credentials

#### **Các bước:**
1. Đăng nhập vào GitHub
2. Vào **Settings** → **Developer settings** → **OAuth Apps** → **New OAuth App**
3. Điền thông tin:
   - **Application name:** `JobHunter Recruitment`
   - **Homepage URL:** `http://localhost:8080`
   - **Authorization callback URL:** `http://localhost:8080/api/v1/auth/oauth2/callback/github`
4. Click **Register application**
5. Lưu lại:
   - **Client ID**: Ví dụ `Iv1.abc123def456`
   - **Client Secret**: Click **Generate a new client secret** và lưu lại

#### **⚠️ Lưu ý:**
- Callback URL phải khớp chính xác với config trong `application.properties`
- Client Secret chỉ hiện 1 lần, hãy copy ngay

---

### 📍 **BƯỚC 2: Tạo Facebook App**

#### **Mục đích:** Đăng ký ứng dụng với Facebook để lấy credentials

#### **Các bước:**
1. Đăng nhập vào [Facebook Developers](https://developers.facebook.com/)
2. Vào **My Apps** → **Create App**
3. Chọn **Consumer** hoặc **None** → **Next**
4. Điền thông tin:
   - **App Name:** `JobHunter Recruitment`
   - **App Contact Email:** Email của bạn
5. Sau khi tạo xong, vào **Settings** → **Basic**
6. Lưu lại:
   - **App ID**: Ví dụ `123456789012345`
   - **App Secret**: Click **Show** và lưu lại
7. Vào **Products** → **Facebook Login** → **Settings**
8. Thêm **Valid OAuth Redirect URIs:**
   ```
   http://localhost:8080/api/v1/auth/oauth2/callback/facebook
   ```
9. **Quan trọng:** Vào **App Mode** → Chuyển sang **Live** mode (hoặc để Development mode nếu test)

#### **⚠️ Lưu ý:**
- Facebook yêu cầu email permission: `email` và `public_profile`
- Phải thêm redirect URI chính xác
- App phải ở Live mode để user khác login được

---

### 📍 **BƯỚC 3: Cấu hình Backend - application.properties**

#### **Mục đích:** Thêm GitHub và Facebook OAuth2 client configuration

#### **File:** `src/main/resources/application.properties`

#### **Giải thích:**
- Spring Boot OAuth2 Client tự động nhận diện provider name từ properties
- Mỗi provider cần: `client-id`, `client-secret`, `scope`, `redirect-uri`
- Spring Security sẽ tự động tạo endpoint `/oauth2/authorize/{provider}`

#### **Code thêm vào:**
```properties
# =========================================
# GITHUB OAUTH2 CLIENT PROPERTIES
# =========================================
spring.security.oauth2.client.registration.github.client-id=YOUR_GITHUB_CLIENT_ID
spring.security.oauth2.client.registration.github.client-secret=YOUR_GITHUB_CLIENT_SECRET
spring.security.oauth2.client.registration.github.scope=read:user, user:email
spring.security.oauth2.client.registration.github.redirect-uri=http://localhost:8080/api/v1/auth/oauth2/callback/github

# =========================================
# FACEBOOK OAUTH2 CLIENT PROPERTIES
# =========================================
spring.security.oauth2.client.registration.facebook.client-id=YOUR_FACEBOOK_APP_ID
spring.security.oauth2.client.registration.facebook.client-secret=YOUR_FACEBOOK_APP_SECRET
spring.security.oauth2.client.registration.facebook.scope=email, public_profile
spring.security.oauth2.client.registration.facebook.redirect-uri=http://localhost:8080/api/v1/auth/oauth2/callback/facebook
```

#### **❗ Thay thế:**
- `YOUR_GITHUB_CLIENT_ID` → Client ID từ bước 1
- `YOUR_GITHUB_CLIENT_SECRET` → Client Secret từ bước 1
- `YOUR_FACEBOOK_APP_ID` → App ID từ bước 2
- `YOUR_FACEBOOK_APP_SECRET` → App Secret từ bước 2

#### **📝 Giải thích từng tham số:**

| Tham số | Mô tả | GitHub | Facebook |
|---------|-------|--------|----------|
| `client-id` | ID ứng dụng | Từ GitHub OAuth App | Từ Facebook App Settings |
| `client-secret` | Secret key | Từ GitHub OAuth App | Từ Facebook App Settings |
| `scope` | Quyền truy cập | `read:user, user:email` | `email, public_profile` |
| `redirect-uri` | URL callback | Phải khớp với GitHub config | Phải khớp với Facebook config |

---

### 📍 **BƯỚC 4: Xác minh SecurityConfiguration**

#### **Mục đích:** Đảm bảo SecurityConfiguration hỗ trợ multi-provider

#### **File:** `src/main/java/com/example/jobhunter/config/SecurityConfiguration.java`

#### **Giải thích:**
- Config hiện tại đã hỗ trợ multi-provider
- `/oauth2/**` đã được permitAll
- `oauth2Login()` tự động nhận diện tất cả provider từ properties
- `redirectionEndpoint` với wildcard `*` hỗ trợ tất cả providers

#### **✅ Không cần sửa gì!**

Đoạn code này đã hỗ trợ GitHub và Facebook:
```java
.oauth2Login(oauth2 -> oauth2
    .authorizationEndpoint(authz -> authz
        .baseUri("/oauth2/authorize")  // ← Hỗ trợ /oauth2/authorize/github và /oauth2/authorize/facebook
        .authorizationRequestRepository(cookieAuthorizationRequestRepository))
    .redirectionEndpoint(r -> r
        .baseUri("/api/v1/auth/oauth2/callback/*"))  // ← Wildcard * hỗ trợ tất cả providers
    .successHandler(oAuth2AuthenticationSuccessHandler)
    .failureHandler(oAuth2AuthenticationFailureHandler))
```

---

### 📍 **BƯỚC 5: Kiểm tra Success Handler**

#### **Mục đích:** Đảm bảo handler xử lý đúng thông tin từ các providers

#### **File:** `src/main/java/com/example/jobhunter/config/SecurityConfiguration.java`

#### **Giải thích:**
Bean `oAuth2AuthenticationSuccessHandler` đã xử lý chung cho tất cả providers:

```java
@Bean
public AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler(...) {
    return (request, response, authentication) -> {
        OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
        String email = oidcUser.getEmail();
        String name = oidcUser.getFullName();
        
        // 1. Tìm hoặc tạo user
        // 2. Tạo JWT tokens
        // 3. Set refresh token cookie
        // 4. Redirect về frontend
    };
}
```

#### **⚠️ LỖI TIỀM ẨN:**
GitHub và Facebook có thể trả về `name` bằng `null`. Cần xử lý:

#### **🔧 Code cần sửa trong Success Handler:**

Tìm đoạn này:
```java
OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
String email = oidcUser.getEmail();
String name = oidcUser.getFullName();
```

**Thay bằng:**
```java
OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
String email = oidcUser.getEmail();
String name = oidcUser.getFullName();

// ✅ XỬ LÝ: Nếu name null, lấy từ preferred_username (GitHub) hoặc email
if (name == null || name.isEmpty()) {
    name = oidcUser.getPreferredUsername(); // GitHub trả về login name
    if (name == null || name.isEmpty()) {
        name = email.split("@")[0]; // Fallback: lấy phần trước @ của email
    }
}
```

#### **📝 Giải thích:**
- **Google**: Luôn có `fullName`
- **GitHub**: Không có `fullName`, nhưng có `login` (username)
- **Facebook**: Có `name` trong attributes
- Code trên đảm bảo `name` không bao giờ null

---

### 📍 **BƯỚC 6: Cập nhật AuthService (Không bắt buộc)**

#### **Mục đích:** Lưu thông tin provider để phân biệt nguồn đăng ký

#### **File:** `src/main/java/com/example/jobhunter/service/AuthService.java`

#### **Giải thích:**
Hiện tại method `registerOauthUser()` chỉ nhận `email` và `name`. Nếu muốn lưu provider (Google/GitHub/Facebook), cần sửa.

#### **🔧 Code cần sửa (TÙY CHỌN):**

**1. Thêm trường `provider` vào User entity (Không bắt buộc):**
```java
// File: src/main/java/com/example/jobhunter/domain/User.java
@Column(name = "provider")
private String provider; // VD: "GOOGLE", "GITHUB", "FACEBOOK"
```

**2. Sửa method `registerOauthUser()`:**

**Code cũ:**
```java
public User registerOauthUser(String email, String name) {
    // ...
    User newUser = new User();
    newUser.setName(name);
    newUser.setEmail(email);
    newUser.setPassWord(hashedPassword);
    
    return this.userService.handleSaveUser(newUser);
}
```

**Code mới (TÙY CHỌN):**
```java
public User registerOauthUser(String email, String name, String provider) {
    // ...
    User newUser = new User();
    newUser.setName(name);
    newUser.setEmail(email);
    newUser.setPassWord(hashedPassword);
    newUser.setProvider(provider); // ✅ THÊM provider
    
    return this.userService.handleSaveUser(newUser);
}
```

**3. Cập nhật Success Handler để truyền provider:**
```java
// File: SecurityConfiguration.java, trong oAuth2AuthenticationSuccessHandler
String provider = "GOOGLE"; // Mặc định

// ✅ THÊM: Xác định provider từ request
if (request.getRequestURI().contains("/github")) {
    provider = "GITHUB";
} else if (request.getRequestURI().contains("/facebook")) {
    provider = "FACEBOOK";
}

// Gọi method với provider
user = authService.registerOauthUser(email, name, provider);
```

---

### 📍 **BƯỚC 7: Xác minh Frontend Configuration**

#### **Mục đích:** Đảm bảo frontend đã sẵn sàng cho GitHub và Facebook

#### **File:** `src/config/oauth.config.jsx`

#### **Giải thích:**
Frontend đã có sẵn config cho GitHub và Facebook. Chỉ cần kiểm tra:

```jsx
const OAUTH_CONFIG = {
    providers: {
        google: {
            authUrl: `${API_BASE_URL}/oauth2/authorize/google`,
            // ...
        },
        github: {
            authUrl: `${API_BASE_URL}/oauth2/authorization/github`, // ⚠️ Chú ý: "authorization" thay vì "authorize"
            // ...
        },
        facebook: {
            authUrl: `${API_BASE_URL}/oauth2/authorization/facebook`, // ⚠️ Chú ý: "authorization" thay vì "authorize"
            // ...
        },
    },
};
```

#### **🔧 LỖI CẦN SỬA:**

**Vấn đề:** URL endpoint không nhất quán
- Google: `/oauth2/authorize/google` ✅
- GitHub: `/oauth2/authorization/github` ❌
- Facebook: `/oauth2/authorization/facebook` ❌

**Spring Security mặc định dùng `/oauth2/authorization/{provider}`**, nhưng bạn đã custom thành `/oauth2/authorize` trong SecurityConfiguration.

#### **Giải pháp 1: Sửa Frontend (ĐỀ XUẤT)**
```jsx
github: {
    authUrl: `${API_BASE_URL}/oauth2/authorize/github`, // ✅ Đổi thành "authorize"
    // ...
},
facebook: {
    authUrl: `${API_BASE_URL}/oauth2/authorize/facebook`, // ✅ Đổi thành "authorize"
    // ...
},
```

#### **Giải pháp 2: Sửa Backend**
```java
// File: SecurityConfiguration.java
.authorizationEndpoint(authz -> authz
    .baseUri("/oauth2/authorization") // ✅ Đổi thành "authorization"
    .authorizationRequestRepository(cookieAuthorizationRequestRepository))
```

**👉 Khuyến nghị: Dùng Giải pháp 1 (sửa frontend) để giữ nhất quán với Google.**

---

### 📍 **BƯỚC 8: Test từng provider**

#### **Mục đích:** Kiểm tra từng bước trước khi deploy

#### **Các bước test:**

1. **Khởi động backend:**
   ```bash
   ./gradlew bootRun
   ```

2. **Khởi động frontend:**
   ```bash
   cd Recruitment_Frontend
   npm run dev
   ```

3. **Test Google (đã có):**
   - Vào `http://localhost:5173/login`
   - Click nút Google
   - Xác minh redirect đúng
   - Check user được tạo trong DB

4. **Test GitHub:**
   - Click nút GitHub
   - Đăng nhập GitHub
   - Authorize app
   - Xác minh redirect về frontend với `?oauth=success`
   - Check console log: `✅ OAuth2 success`
   - Check user trong DB có email từ GitHub

5. **Test Facebook:**
   - Click nút Facebook
   - Đăng nhập Facebook
   - Authorize app
   - Xác minh redirect về frontend
   - Check user trong DB

#### **🐛 Debug nếu lỗi:**

**Lỗi 1: "Cannot find provider"**
- Kiểm tra `application.properties` đã có config GitHub/Facebook
- Restart backend sau khi sửa properties

**Lỗi 2: Redirect URI mismatch**
- Kiểm tra GitHub/Facebook app config
- Đảm bảo redirect URI giống hệt: `http://localhost:8080/api/v1/auth/oauth2/callback/{provider}`

**Lỗi 3: "email is null"**
- GitHub: User phải public email trong profile
- Facebook: Phải request permission `email`

**Lỗi 4: Frontend không nhận được data**
- Check browser console
- Check redirect URL có chứa `access_token`
- Check cookie `refresh_token` đã được set

---

### 📍 **BƯỚC 9: Xử lý edge cases**

#### **Mục đích:** Xử lý các trường hợp đặc biệt

#### **Case 1: User đã tồn tại với email từ provider khác**

**Ví dụ:** User đăng ký bằng Google với email `user@gmail.com`, sau đó login bằng GitHub với cùng email.

**Giải pháp:**
- Success Handler đã có check: `if (user == null)`
- Nếu user đã tồn tại, chỉ tạo token mới, không tạo user mới
- ✅ Không cần sửa code

#### **Case 2: GitHub user không public email**

**Vấn đề:** GitHub API trả về `email = null` nếu user không public email.

**Giải pháp:**
Thêm vào Success Handler:
```java
String email = oidcUser.getEmail();
if (email == null || email.isEmpty()) {
    // Redirect về frontend với error
    String redirectUrl = UriComponentsBuilder.fromUriString(frontendUrl + "/login")
            .queryParam("error", "Email not available. Please make your GitHub email public.")
            .build().toUriString();
    response.sendRedirect(redirectUrl);
    return;
}
```

#### **Case 3: Facebook email bị hidden**

**Vấn đề:** User từ chối permission `email` khi login Facebook.

**Giải pháp:**
- Facebook sẽ redirect với error
- `oAuth2AuthenticationFailureHandler` đã xử lý
- ✅ Không cần sửa code

---

## 4. LUỒNG HOẠT ĐỘNG CỦA OAUTH2

### 🔄 **Luồng tổng quan**

```
[Frontend]  →  [Backend]  →  [GitHub/Facebook]  →  [Backend]  →  [Frontend]
    (1)          (2)              (3)                  (4)          (5)
```

### 📊 **Chi tiết từng bước:**

#### **Bước 1: User click nút GitHub/Facebook (Frontend)**
- File: `OAuthButtons.jsx`
- Action: `window.location.href = provider.authUrl`
- URL: `http://localhost:8080/oauth2/authorize/github`

#### **Bước 2: Backend nhận request và redirect sang provider (Backend)**
- Spring Security xử lý tự động
- Tạo `OAuth2AuthorizationRequest`
- Lưu vào cookie bằng `HttpCookieOAuth2AuthorizationRequestRepository`
- Redirect sang GitHub/Facebook với `client_id`, `scope`, `redirect_uri`

#### **Bước 3: User login tại GitHub/Facebook (External)**
- User đăng nhập và cho phép ứng dụng truy cập
- GitHub/Facebook redirect về: `http://localhost:8080/api/v1/auth/oauth2/callback/github?code=ABC123`

#### **Bước 4: Backend nhận callback và xử lý (Backend)**
- Spring Security nhận `code`
- Đổi `code` lấy `access_token` từ GitHub/Facebook
- Gọi API GitHub/Facebook lấy user info
- Gọi `oAuth2AuthenticationSuccessHandler`:
  - Tìm hoặc tạo user trong DB
  - Tạo JWT tokens
  - Set refresh token cookie
  - Redirect về frontend: `http://localhost:5173/login?oauth=success&access_token=JWT123`

#### **Bước 5: Frontend nhận kết quả (Frontend)**
- File: `LoginPage.jsx`
- Đọc query params: `oauth=success`, `access_token=JWT123`
- Lưu token vào context
- Redirect sang `/dashboard`

### 🗂️ **Sơ đồ file tham gia:**

```
Frontend:
├── OAuthButtons.jsx          # Render nút, handle click
├── oauth.config.jsx          # Config provider URLs
├── LoginPage.jsx             # Nhận kết quả redirect
└── AuthContext.jsx           # Lưu token, user state

Backend:
├── application.properties                           # Config OAuth2 clients
├── SecurityConfiguration.java                       # Config oauth2Login
├── HttpCookieOAuth2AuthorizationRequestRepository  # Lưu OAuth2 request vào cookie
├── oAuth2AuthenticationSuccessHandler              # Xử lý thành công
├── oAuth2AuthenticationFailureHandler              # Xử lý thất bại
└── AuthService.java                                 # Tạo user mới
```

---

## 5. TỔNG KẾT CÁC FILE ĐÃ SỬA ĐỔI

### ✅ **Files cần sửa:**

| # | File | Mục đích | Thay đổi |
|---|------|----------|----------|
| 1 | `application.properties` | Thêm GitHub & Facebook config | **BẮT BUỘC** |
| 2 | `oauth.config.jsx` | Sửa URL endpoint | **BẮT BUỘC** |
| 3 | `SecurityConfiguration.java` | Xử lý name null | **KHUYẾN NGHỊ** |
| 4 | `AuthService.java` | Thêm provider field | **TÙY CHỌN** |
| 5 | `User.java` | Thêm trường provider | **TÙY CHỌN** |

### 📝 **Chi tiết thay đổi:**

#### **1. application.properties (BẮT BUỘC)**
```properties
# Thêm vào cuối file
spring.security.oauth2.client.registration.github.client-id=YOUR_GITHUB_CLIENT_ID
spring.security.oauth2.client.registration.github.client-secret=YOUR_GITHUB_CLIENT_SECRET
spring.security.oauth2.client.registration.github.scope=read:user, user:email
spring.security.oauth2.client.registration.github.redirect-uri=http://localhost:8080/api/v1/auth/oauth2/callback/github

spring.security.oauth2.client.registration.facebook.client-id=YOUR_FACEBOOK_APP_ID
spring.security.oauth2.client.registration.facebook.client-secret=YOUR_FACEBOOK_APP_SECRET
spring.security.oauth2.client.registration.facebook.scope=email, public_profile
spring.security.oauth2.client.registration.facebook.redirect-uri=http://localhost:8080/api/v1/auth/oauth2/callback/facebook
```

#### **2. oauth.config.jsx (BẮT BUỘC)**
```jsx
// Sửa dòng authUrl
github: {
    authUrl: `${API_BASE_URL}/oauth2/authorize/github`, // ✅ Đổi thành "authorize"
    // ... giữ nguyên phần còn lại
},
facebook: {
    authUrl: `${API_BASE_URL}/oauth2/authorize/facebook`, // ✅ Đổi thành "authorize"
    // ... giữ nguyên phần còn lại
},
```

#### **3. SecurityConfiguration.java (KHUYẾN NGHỊ)**
```java
// Tìm dòng này trong oAuth2AuthenticationSuccessHandler
String name = oidcUser.getFullName();

// Thêm xử lý null
if (name == null || name.isEmpty()) {
    name = oidcUser.getPreferredUsername();
    if (name == null || name.isEmpty()) {
        name = email.split("@")[0];
    }
}
```

---

## 6. CHECKLIST HOÀN THÀNH

### 📋 Trước khi test:
- [ ] Tạo GitHub OAuth App và lấy credentials
- [ ] Tạo Facebook App và lấy credentials
- [ ] Thêm config vào `application.properties`
- [ ] Sửa `oauth.config.jsx` (authUrl)
- [ ] Sửa `SecurityConfiguration.java` (xử lý name null)
- [ ] Restart backend và frontend

### 📋 Test từng provider:
- [ ] Test Google login (đã có - verify lại)
- [ ] Test GitHub login
  - [ ] Click nút GitHub
  - [ ] Authorize thành công
  - [ ] Redirect về frontend
  - [ ] User được tạo trong DB
- [ ] Test Facebook login
  - [ ] Click nút Facebook
  - [ ] Authorize thành công
  - [ ] Redirect về frontend
  - [ ] User được tạo trong DB

### 📋 Test edge cases:
- [ ] User đã tồn tại với cùng email
- [ ] GitHub user không public email
- [ ] Facebook user từ chối email permission
- [ ] Network error khi gọi API provider

---

## 7. TROUBLESHOOTING

### ❌ **Lỗi thường gặp:**

#### **1. "redirect_uri_mismatch"**
**Nguyên nhân:** Redirect URI không khớp giữa app config và code

**Giải pháp:**
- GitHub: Kiểm tra GitHub OAuth App settings
- Facebook: Kiểm tra Facebook Login → Valid OAuth Redirect URIs
- Đảm bảo URI giống hệt (có/không có trailing slash)

#### **2. "Cannot find provider: github"**
**Nguyên nhân:** Backend chưa load config từ application.properties

**Giải pháp:**
- Restart Spring Boot application
- Kiểm tra properties có syntax error không
- Check log khi startup: `OAuth2ClientRegistration`

#### **3. "Email is null"**
**Nguyên nhân:** 
- GitHub: User chưa public email
- Facebook: User từ chối permission email

**Giải pháp:**
- Thêm validation trong Success Handler (xem Bước 9)
- Hiển thị error message cho user

#### **4. Frontend không nhận được token**
**Nguyên nhân:** Redirect URL thiếu access_token

**Giải pháp:**
- Check redirect URL trong Success Handler
- Đảm bảo: `.queryParam("access_token", accessToken)`
- Check browser console log

---

## 8. NHỮNG GÌ ĐÃ LÀM ĐƯỢC

### ✅ **Kết quả:**
1. ✅ Thêm GitHub OAuth2 login vào hệ thống
2. ✅ Thêm Facebook OAuth2 login vào hệ thống
3. ✅ Tái sử dụng 100% logic hiện có (SecurityConfiguration, AuthService)
4. ✅ Frontend hỗ trợ 3 providers: Google, GitHub, Facebook
5. ✅ Xử lý edge cases: name null, email null
6. ✅ Stateless authentication (JWT + refresh token cookie)

### 🎯 **So sánh trước và sau:**

| Tính năng | Trước | Sau |
|-----------|-------|-----|
| OAuth2 Providers | Chỉ Google | Google + GitHub + Facebook |
| Backend Config | 1 provider | 3 providers (dễ mở rộng) |
| Frontend UI | 1 nút | 3 nút (horizontal layout) |
| User Creation | Chỉ Google users | Tất cả OAuth users |
| Edge Case Handling | Cơ bản | Xử lý name null, email null |

---

## 9. MỞ RỘNG SAU NÀY

### 🚀 **Tính năng nâng cao:**

1. **Thêm provider mới (LinkedIn, Twitter/X)**
   - Chỉ cần thêm config vào `application.properties`
   - Thêm icon vào `oauth.config.jsx`

2. **Lưu provider vào DB**
   - Thêm trường `provider` vào User entity
   - Hiển thị "Login with Google/GitHub/Facebook" khi user đã có account

3. **Link multiple providers**
   - Cho phép user link nhiều OAuth accounts vào 1 user
   - Ví dụ: User đăng ký bằng email, sau đó link Google và GitHub

4. **Custom scope cho từng provider**
   - GitHub: Thêm `repo` scope để đọc repositories
   - Facebook: Thêm `user_friends` để lấy danh sách bạn bè

---

## 📞 HỖ TRỢ

Nếu gặp vấn đề, hãy check:
1. Backend log: `./gradlew bootRun`
2. Browser console: F12 → Console
3. Network tab: Check request/response của OAuth2

**Debug checklist:**
- [ ] Credentials đúng trong application.properties
- [ ] Redirect URI khớp chính xác
- [ ] Backend đã restart sau khi sửa properties
- [ ] Frontend authUrl đúng (authorize vs authorization)

---

## 📚 TÀI LIỆU THAM KHẢO

- [Spring Security OAuth2 Client](https://docs.spring.io/spring-security/reference/servlet/oauth2/client/index.html)
- [GitHub OAuth Apps](https://docs.github.com/en/apps/oauth-apps)
- [Facebook Login](https://developers.facebook.com/docs/facebook-login/)

---

**🎉 Chúc bạn thành công!**
