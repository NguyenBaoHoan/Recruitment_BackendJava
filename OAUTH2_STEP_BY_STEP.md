# 🎯 HƯỚNG DẪN TỪNG BƯỚC CHO GITHUB VÀ FACEBOOK OAUTH2

## 📖 CẤU TRÚC TÀI LIỆU

Tài liệu này được chia thành 3 phần:
1. **Bước 1-3:** Chuẩn bị (Lấy credentials từ GitHub/Facebook)
2. **Bước 4-6:** Cấu hình code (Backend và Frontend)
3. **Bước 7-9:** Test và debug

---

# PHẦN 1: CHUẨN BỊ CREDENTIALS

## 📍 BƯỚC 1: TẠO GITHUB OAUTH APP (5 phút)

### **A. Đăng nhập GitHub và tạo OAuth App**

1. Mở trình duyệt, đăng nhập GitHub
2. Click vào **avatar (góc phải)** → **Settings**
3. Scroll xuống dưới sidebar → Click **Developer settings**
4. Sidebar trái → Click **OAuth Apps**
5. Click nút **New OAuth App**

### **B. Điền form đăng ký**

Điền thông tin như sau:

| Field | Value |
|-------|-------|
| **Application name** | `JobHunter Recruitment` |
| **Homepage URL** | `http://localhost:8080` |
| **Application description** | `Recruitment platform with OAuth2 login` (optional) |
| **Authorization callback URL** | `http://localhost:8080/api/v1/auth/oauth2/callback/github` |

⚠️ **LƯU Ý:** Authorization callback URL phải chính xác, không có trailing slash `/`

6. Click **Register application**

### **C. Lấy credentials**

Sau khi tạo xong, bạn sẽ thấy trang:

1. **Client ID:** 
   - Hiển thị ngay trên màn hình
   - Ví dụ: `Iv1.abc123def456`
   - Copy và lưu lại

2. **Client Secret:**
   - Click nút **Generate a new client secret**
   - Secret sẽ hiển thị 1 lần duy nhất
   - Ví dụ: `1234567890abcdef1234567890abcdef12345678`
   - ⚠️ **QUAN TRỌNG:** Copy ngay và lưu vào file an toàn

### **D. Lưu credentials**

Tạo file tạm `github-credentials.txt` (hoặc note riêng):
```
GITHUB OAUTH CREDENTIALS
========================
Client ID: Iv1.abc123def456
Client Secret: 1234567890abcdef1234567890abcdef12345678
Callback URL: http://localhost:8080/api/v1/auth/oauth2/callback/github
```

✅ **HOÀN THÀNH BƯỚC 1**

---

## 📍 BƯỚC 2: TẠO FACEBOOK APP (10 phút)

### **A. Đăng nhập Facebook Developers**

1. Mở trình duyệt, vào https://developers.facebook.com/
2. Đăng nhập bằng tài khoản Facebook cá nhân
3. Click **My Apps** (góc trên bên phải)
4. Click **Create App**

### **B. Chọn loại App**

1. Chọn **Consumer** (Dành cho đăng nhập người dùng thông thường)
2. Click **Next**

### **C. Điền thông tin App**

| Field | Value |
|-------|-------|
| **App name** | `JobHunter Recruitment` |
| **App contact email** | Email của bạn (VD: `youremail@gmail.com`) |

3. Click **Create app**
4. Có thể yêu cầu xác thực Security Check (CAPTCHA) → Hoàn thành

### **D. Lấy App ID và App Secret**

1. Sau khi tạo xong, bạn sẽ vào Dashboard của app
2. Sidebar trái → Click **Settings** → **Basic**
3. Bạn sẽ thấy:
   - **App ID:** Hiển thị ngay (VD: `123456789012345`)
   - **App Secret:** Click **Show** → Nhập password Facebook → Copy secret

### **E. Thêm Facebook Login Product**

1. Sidebar trái → Click **Add Product** (hoặc **Dashboard** → **Add Product**)
2. Tìm **Facebook Login** → Click **Set Up**
3. Chọn platform: **Web**
4. **Site URL:** Nhập `http://localhost:8080` → **Save** → **Continue**

### **F. Cấu hình Valid OAuth Redirect URIs**

1. Sidebar trái → Click **Facebook Login** → **Settings**
2. Tìm field **Valid OAuth Redirect URIs**
3. Nhập:
   ```
   http://localhost:8080/api/v1/auth/oauth2/callback/facebook
   ```
4. Click **Save Changes**

### **G. Chuyển App Mode sang Live (QUAN TRỌNG)**

⚠️ Nếu không làm bước này, chỉ admin/testers mới login được!

1. Sidebar trái → Click **Settings** → **Basic**
2. Scroll lên trên cùng, bạn sẽ thấy toggle **App Mode**
3. Nếu đang là **Development**, click toggle để chuyển sang **Live**
4. Có thể yêu cầu thêm thông tin:
   - **Privacy Policy URL:** `http://localhost:8080/privacy` (tạm thời)
   - **Terms of Service URL:** `http://localhost:8080/terms` (tạm thời)
   - Hoặc skip nếu dev testing

⚠️ **LƯU Ý:** Với Live mode, Facebook có thể yêu cầu App Review cho permissions ngoài `email` và `public_profile`. Nhưng cho dev testing, 2 permissions này là đủ.

### **H. Lưu credentials**

Thêm vào file `facebook-credentials.txt`:
```
FACEBOOK OAUTH CREDENTIALS
==========================
App ID: 123456789012345
App Secret: abcdef1234567890abcdef1234567890
Valid OAuth Redirect URI: http://localhost:8080/api/v1/auth/oauth2/callback/facebook
App Mode: Live
```

✅ **HOÀN THÀNH BƯỚC 2**

---

# PHẦN 2: CẤU HÌNH CODE

## 📍 BƯỚC 3: CẬP NHẬT APPLICATION.PROPERTIES (Backend)

### **A. Mở file application.properties**

**Đường dẫn:** `Recruitment_Backend/src/main/resources/application.properties`

### **B. Giải thích cấu trúc**

File này đã có config cho Google OAuth2:
```properties
spring.security.oauth2.client.registration.google.client-id=...
spring.security.oauth2.client.registration.google.client-secret=...
spring.security.oauth2.client.registration.google.scope=...
spring.security.oauth2.client.registration.google.redirect-uri=...
```

Bạn cần thêm tương tự cho GitHub và Facebook.

### **C. Thay credentials**

Tìm đoạn code mới được thêm:

```properties
# GITHUB OAUTH2
spring.security.oauth2.client.registration.github.client-id=YOUR_GITHUB_CLIENT_ID
spring.security.oauth2.client.registration.github.client-secret=YOUR_GITHUB_CLIENT_SECRET
```

**Thay bằng credentials từ Bước 1:**
```properties
spring.security.oauth2.client.registration.github.client-id=Iv1.abc123def456
spring.security.oauth2.client.registration.github.client-secret=1234567890abcdef1234567890abcdef12345678
```

Tương tự cho Facebook:
```properties
# FACEBOOK OAUTH2
spring.security.oauth2.client.registration.facebook.client-id=123456789012345
spring.security.oauth2.client.registration.facebook.client-secret=abcdef1234567890abcdef1234567890
```

### **D. Giải thích các tham số**

| Tham số | Ý nghĩa | GitHub | Facebook |
|---------|---------|--------|----------|
| `client-id` | ID ứng dụng | Từ OAuth App | Từ App Settings |
| `client-secret` | Secret key (bảo mật) | Từ OAuth App | Từ App Settings |
| `scope` | Quyền truy cập user data | `read:user, user:email` | `email, public_profile` |
| `redirect-uri` | URL mà provider gọi về sau khi user authorize | Phải khớp với config ở Bước 1 | Phải khớp với config ở Bước 2 |

### **E. Giải thích scope**

**GitHub scope:**
- `read:user`: Đọc thông tin cơ bản của user (username, name)
- `user:email`: Đọc email của user

**Facebook scope:**
- `email`: Lấy email
- `public_profile`: Lấy ID, name, avatar

### **F. Verify file hoàn chỉnh**

File `application.properties` của bạn bây giờ phải có 3 providers:
```properties
# GOOGLE
spring.security.oauth2.client.registration.google.client-id=...
spring.security.oauth2.client.registration.google.client-secret=...
spring.security.oauth2.client.registration.google.scope=...
spring.security.oauth2.client.registration.google.redirect-uri=...

# GITHUB
spring.security.oauth2.client.registration.github.client-id=...
spring.security.oauth2.client.registration.github.client-secret=...
spring.security.oauth2.client.registration.github.scope=...
spring.security.oauth2.client.registration.github.redirect-uri=...

# FACEBOOK
spring.security.oauth2.client.registration.facebook.client-id=...
spring.security.oauth2.client.registration.facebook.client-secret=...
spring.security.oauth2.client.registration.facebook.scope=...
spring.security.oauth2.client.registration.facebook.redirect-uri=...
```

✅ **HOÀN THÀNH BƯỚC 3**

---

## 📍 BƯỚC 4: XÁC MINH OAUTH.CONFIG.JSX (Frontend)

### **A. Mở file oauth.config.jsx**

**Đường dẫn:** `Recruitment_Frontend/src/config/oauth.config.jsx`

### **B. Giải thích file này làm gì**

File này define config cho các provider OAuth2:
- **authUrl:** URL để bắt đầu flow OAuth2
- **icon:** SVG icon hiển thị trên nút
- **displayName:** Tên hiển thị
- **color:** Màu chủ đạo của provider

### **C. Verify authUrl đã được sửa**

Kiểm tra authUrl của GitHub và Facebook:

```jsx
github: {
    displayName: 'GitHub',
    authUrl: `${API_BASE_URL}/oauth2/authorize/github`, // ✅ Phải là "authorize"
    // ...
},
facebook: {
    displayName: 'Facebook',
    authUrl: `${API_BASE_URL}/oauth2/authorize/facebook`, // ✅ Phải là "authorize"
    // ...
},
```

⚠️ **QUAN TRỌNG:** Phải là `/oauth2/authorize/` (không phải `/oauth2/authorization/`)

**Lý do:** Backend SecurityConfiguration bạn đã config:
```java
.authorizationEndpoint(authz -> authz
    .baseUri("/oauth2/authorize") // ← Endpoint này
```

### **D. Verify API_BASE_URL**

Kiểm tra đầu file:
```jsx
const API_BASE_URL = 'http://localhost:8080';
```

✅ Đúng cho development. Production phải đổi thành domain thật.

✅ **HOÀN THÀNH BƯỚC 4**

---

## 📍 BƯỚC 5: XÁC MINH SECURITYCONFIGURATION.JAVA (Backend)

### **A. Mở file SecurityConfiguration.java**

**Đường dẫn:** `Recruitment_Backend/src/main/java/com/example/jobhunter/config/SecurityConfiguration.java`

### **B. Giải thích file này làm gì**

File này cấu hình Spring Security, bao gồm:
- OAuth2 login endpoints
- Success/Failure handlers
- JWT token creation
- Redirect về frontend

### **C. Verify code đã được cập nhật**

Tìm method `oAuth2AuthenticationSuccessHandler`, kiểm tra đã có đoạn xử lý name null:

```java
@Bean
public AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler(...) {
    return (request, response, authentication) -> {
        OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
        String email = oidcUser.getEmail();
        String name = oidcUser.getFullName();

        // ✅ ĐOẠN NÀY PHẢI CÓ:
        if (name == null || name.isEmpty()) {
            name = oidcUser.getPreferredUsername();
            if (name == null || name.isEmpty()) {
                name = email.split("@")[0];
            }
        }

        // ✅ ĐOẠN NÀY PHẢI CÓ:
        if (email == null || email.isEmpty()) {
            String redirectUrl = UriComponentsBuilder.fromUriString(frontendUrl + "/login")
                    .queryParam("error", "Email not available...")
                    .encode(StandardCharsets.UTF_8)
                    .build().toUriString();
            HttpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
            response.sendRedirect(redirectUrl);
            return;
        }

        // ... (phần còn lại)
    };
}
```

### **D. Giải thích tại sao cần xử lý null**

| Provider | fullName | preferredUsername | email |
|----------|----------|-------------------|-------|
| Google | ✅ Có | ❌ Không | ✅ Có |
| GitHub | ❌ Không | ✅ Có (login name) | ✅ Có (nếu public) |
| Facebook | ✅ Có | ❌ Không | ✅ Có |

**Vấn đề:** GitHub không có `fullName`, chỉ có `login` (username)

**Giải pháp:**
1. Thử lấy `preferredUsername` (GitHub sẽ trả về)
2. Nếu không có, fallback sang prefix của email (`user@gmail.com` → `user`)

### **E. Verify oauth2Login config**

Tìm đoạn config oauth2Login:
```java
.oauth2Login(oauth2 -> oauth2
    .authorizationEndpoint(authz -> authz
        .baseUri("/oauth2/authorize")  // ← Hỗ trợ /github và /facebook
        .authorizationRequestRepository(cookieAuthorizationRequestRepository))
    .redirectionEndpoint(r -> r
        .baseUri("/api/v1/auth/oauth2/callback/*"))  // ← Wildcard * hỗ trợ tất cả
    .successHandler(oAuth2AuthenticationSuccessHandler)
    .failureHandler(oAuth2AuthenticationFailureHandler))
```

✅ **Không cần sửa gì!** Config này đã hỗ trợ multi-provider.

✅ **HOÀN THÀNH BƯỚC 5**

---

## 📍 BƯỚC 6: VERIFY CÁC FILE KHÁC (Không cần sửa)

### **A. AuthService.java**

**Đường dẫn:** `Recruitment_Backend/src/main/java/com/example/jobhunter/service/AuthService.java`

**Kiểm tra:** Method `registerOauthUser()` đã tồn tại
```java
public User registerOauthUser(String email, String name) {
    // ... tạo user mới với random password
}
```

✅ **Không cần sửa gì!** Method này dùng chung cho cả 3 providers.

### **B. HttpCookieOAuth2AuthorizationRequestRepository.java**

**Đường dẫn:** `Recruitment_Backend/src/main/java/com/example/jobhunter/util/HttpCookieOAuth2AuthorizationRequestRepository.java`

**Kiểm tra:** Class này implement `AuthorizationRequestRepository<OAuth2AuthorizationRequest>`

✅ **Không cần sửa gì!** Class này lưu OAuth2 request vào cookie thay vì session (để hỗ trợ stateless JWT).

### **C. OAuthButtons.jsx**

**Đường dẫn:** `Recruitment_Frontend/src/components/auth/OAuthButtons.jsx`

**Kiểm tra:** Component này render 3 nút (Google, GitHub, Facebook)

```jsx
const handleOAuthLogin = (providerName) => {
    const provider = OAUTH_CONFIG.providers[providerName];
    window.location.href = provider.authUrl;
};
```

✅ **Không cần sửa gì!** Component đã hỗ trợ dynamic providers.

### **D. LoginPage.jsx**

**Đường dẫn:** `Recruitment_Frontend/src/pages/LoginPage.jsx`

**Kiểm tra:** Page này xử lý OAuth callback result

```jsx
useEffect(() => {
    const params = new URLSearchParams(location.search);
    const oauthStatus = params.get('oauth');
    const oauthError = params.get('error');
    
    if (oauthStatus === 'success') {
        console.log('✅ OAuth2 success');
    }
    
    if (oauthError) {
        setError(decodeURIComponent(oauthError));
    }
}, [location]);
```

✅ **Không cần sửa gì!** Logic đã xử lý cả success và error.

✅ **HOÀN THÀNH BƯỚC 6**

---

# PHẦN 3: TEST VÀ DEBUG

## 📍 BƯỚC 7: RESTART APPLICATIONS

### **A. Restart Backend**

1. Mở terminal tại `Recruitment_Backend`
2. Nếu đang chạy, dừng lại (Ctrl+C)
3. Chạy lại:
   ```bash
   ./gradlew bootRun
   ```
4. Đợi cho đến khi thấy:
   ```
   Started JobhunterApplication in X.XXX seconds
   ```

### **B. Verify Backend startup log**

Kiểm tra log có những dòng này:
```
... OAuth2ClientRegistration [google]
... OAuth2ClientRegistration [github]   ← ✅ Phải có
... OAuth2ClientRegistration [facebook] ← ✅ Phải có
```

Nếu KHÔNG thấy `github` hoặc `facebook`, có nghĩa là config trong `application.properties` sai → Quay lại Bước 3.

### **C. Restart Frontend**

1. Mở terminal khác tại `Recruitment_Frontend`
2. Nếu đang chạy, dừng lại (Ctrl+C)
3. Chạy lại:
   ```bash
   npm run dev
   ```
4. Đợi cho đến khi thấy:
   ```
   ➜  Local:   http://localhost:5173/
   ```

✅ **HOÀN THÀNH BƯỚC 7**

---

## 📍 BƯỚC 8: TEST TỪNG PROVIDER

### **A. Test Google (Verify hiện tại vẫn hoạt động)**

1. Mở trình duyệt: `http://localhost:5173/login`
2. Click nút **Google** (màu xanh)
3. **Kết quả mong đợi:**
   - Redirect sang trang login Google
   - Chọn account
   - Redirect về `http://localhost:5173/login?oauth=success&...`
   - Tự động redirect sang `/dashboard`

4. **Verify database:**
   - Mở MySQL Workbench
   - Query: `SELECT * FROM user WHERE email = 'your-google-email@gmail.com';`
   - User phải tồn tại

✅ Nếu Google work, chuyển sang test GitHub.

### **B. Test GitHub**

#### **B1. Test flow cơ bản**

1. Vào `http://localhost:5173/login`
2. Click nút **GitHub** (màu đen)
3. **Kết quả mong đợi:**
   - Redirect sang `https://github.com/login/oauth/authorize?client_id=...`
   - Trang GitHub login hiển thị
   - Đăng nhập GitHub (nếu chưa)
   - Trang "Authorize JobHunter Recruitment" hiển thị
   - Quyền request: `read:user`, `user:email`

4. Click **Authorize**
5. **Kết quả mong đợi:**
   - Redirect về `http://localhost:5173/login?oauth=success&provider=github&...`
   - Console log: `✅ OAuth2 success: { userName: 'your-name', userEmail: 'your@email.com' }`
   - Tự động redirect sang `/dashboard`

#### **B2. Verify database**

```sql
SELECT * FROM user WHERE email = 'your-github-email@email.com';
```

**Kiểm tra:**
- `email`: Phải là email từ GitHub profile
- `name`: Phải là username GitHub (hoặc full name nếu có)
- `password`: Phải là hash (không phải "123456")

#### **B3. Verify cookie**

1. Mở Developer Tools (F12)
2. Tab **Application** → **Cookies** → `http://localhost:5173`
3. Tìm cookie `refresh_token`:
   - Value: JWT string dài
   - HttpOnly: ✅
   - Secure: ✅

#### **B4. Test second login**

1. Logout (nếu có nút logout)
2. Vào `/login` lại
3. Click GitHub lần nữa
4. **Kết quả mong đợi:**
   - Không cần authorize lại (đã authorize rồi)
   - Redirect thẳng về `/dashboard`
   - Database: Không tạo user mới, dùng user cũ

### **C. Test Facebook**

#### **C1. Test flow cơ bản**

1. Vào `http://localhost:5173/login`
2. Click nút **Facebook** (màu xanh dương)
3. **Kết quả mong đợi:**
   - Redirect sang Facebook login
   - Đăng nhập Facebook
   - Popup "Continue as [Your Name]"
   - Quyền request: Email, Public Profile

4. Click **Continue**
5. **Kết quả mong đợi:**
   - Redirect về frontend với `oauth=success`
   - Console log: `✅ OAuth2 success`
   - Redirect sang `/dashboard`

#### **C2. Verify database**

```sql
SELECT * FROM user WHERE email = 'your-facebook-email@email.com';
```

#### **C3. Test với user đã tồn tại**

**Scenario:** User đã đăng ký bằng Google với email `user@gmail.com`, sau đó login bằng Facebook với cùng email.

**Kết quả mong đợi:**
- Không tạo user mới
- Dùng user hiện có
- Login thành công

✅ **HOÀN THÀNH BƯỚC 8**

---

## 📍 BƯỚC 9: DEBUG NẾU CÓ LỖI

### **Lỗi 1: "redirect_uri_mismatch" (GitHub)**

**Mô tả:** Sau khi click GitHub, báo lỗi:
```
The redirect_uri in the request (http://localhost:8080/api/v1/auth/oauth2/callback/github) 
does not match the redirect_uri configured for the OAuth application.
```

**Nguyên nhân:**
- Redirect URI trong GitHub OAuth App khác với code

**Giải pháp:**
1. Vào GitHub OAuth App settings
2. Kiểm tra **Authorization callback URL**
3. Phải chính xác: `http://localhost:8080/api/v1/auth/oauth2/callback/github`
4. Không có trailing slash `/`
5. Update và Save

---

### **Lỗi 2: "redirect_uri_mismatch" (Facebook)**

**Mô tả:** Tương tự GitHub, nhưng với Facebook

**Giải pháp:**
1. Vào Facebook App → Facebook Login → Settings
2. Kiểm tra **Valid OAuth Redirect URIs**
3. Phải chính xác: `http://localhost:8080/api/v1/auth/oauth2/callback/facebook`
4. Save Changes

---

### **Lỗi 3: Backend log "Cannot find provider: github"**

**Mô tả:** Backend startup log:
```
No provider github could be found
```

**Nguyên nhân:**
- Config trong `application.properties` sai hoặc chưa load

**Giải pháp:**
1. Kiểm tra `application.properties`:
   - Có `spring.security.oauth2.client.registration.github.client-id=...` không?
   - Syntax đúng không? (không có khoảng trắng thừa, = đúng chỗ)
2. Restart backend: Ctrl+C → `./gradlew bootRun`
3. Kiểm tra log lại

---

### **Lỗi 4: "Email not available" (GitHub)**

**Mô tả:** Sau khi authorize GitHub, redirect về frontend với error:
```
?error=Email not available. Please make your email public.
```

**Nguyên nhân:**
- GitHub user chưa set email thành public

**Giải pháp:**
1. Vào GitHub Settings → Emails
2. Tìm **Primary email address**
3. Bỏ check **Keep my email addresses private**
4. Hoặc check **Public email** ở phần Profile

---

### **Lỗi 5: Frontend không redirect sau khi login**

**Mô tả:** Sau khi authorize provider, stuck ở trang `/login`, không redirect sang `/dashboard`

**Debug steps:**

1. **Check URL có query params không:**
   ```
   http://localhost:5173/login?oauth=success&access_token=...
   ```
   - Nếu không có `oauth=success` → Backend không redirect đúng
   - Nếu không có `access_token` → JWT token không được tạo

2. **Check browser console:**
   - Có error gì không?
   - Có log `✅ OAuth2 success` không?

3. **Check Network tab (F12):**
   - Filter: `auth`
   - Xem có request `/api/v1/auth/account` không?
   - Response có user data không?

4. **Check Backend log:**
   - Có error trong `oAuth2AuthenticationSuccessHandler` không?
   - Token có được tạo không? (`createAccessToken`)

---

### **Lỗi 6: "Access Denied" (Facebook)**

**Mô tả:** Facebook báo "This app is not available"

**Nguyên nhân:**
- Facebook App đang ở Development mode
- User không phải admin/tester

**Giải pháp:**
1. Vào Facebook App → Settings → Basic
2. Toggle **App Mode** sang **Live**
3. Có thể yêu cầu thêm Privacy Policy URL → Nhập tạm `http://localhost:8080/privacy`

---

### **Lỗi 7: Database connection error**

**Mô tả:** Backend crash khi tạo user:
```
Could not open JPA EntityManager for transaction
```

**Giải pháp:**
1. Kiểm tra MySQL đang chạy:
   ```bash
   # Windows
   net start MySQL80
   ```
2. Kiểm tra config trong `application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/jobhunter
   spring.datasource.username=root
   spring.datasource.password=your_password
   ```
3. Test connection bằng MySQL Workbench

---

## 🎉 HOÀN THÀNH!

Nếu tất cả 3 providers (Google, GitHub, Facebook) đều test thành công:

✅ **Bạn đã tích hợp thành công GitHub và Facebook OAuth2!**

### Checklist cuối cùng:
- [ ] GitHub OAuth App đã tạo và lấy credentials
- [ ] Facebook App đã tạo và lấy credentials
- [ ] `application.properties` đã cập nhật đúng credentials
- [ ] `oauth.config.jsx` đã sửa authUrl
- [ ] `SecurityConfiguration.java` đã xử lý name null và email null
- [ ] Backend restart thành công, log có github và facebook
- [ ] Frontend restart thành công
- [ ] Test Google login: ✅ Pass
- [ ] Test GitHub login: ✅ Pass
- [ ] Test Facebook login: ✅ Pass
- [ ] Database có user từ cả 3 providers: ✅ Pass

---

## 📚 TÀI LIỆU THAM KHẢO

### Documentation:
- [Spring Security OAuth2 Client](https://docs.spring.io/spring-security/reference/servlet/oauth2/client/index.html)
- [GitHub OAuth Apps Guide](https://docs.github.com/en/apps/oauth-apps/building-oauth-apps)
- [Facebook Login Documentation](https://developers.facebook.com/docs/facebook-login/web)

### Troubleshooting:
- [GitHub OAuth Troubleshooting](https://docs.github.com/en/apps/oauth-apps/maintaining-oauth-apps/troubleshooting-oauth-apps)
- [Facebook Login Troubleshooting](https://developers.facebook.com/docs/facebook-login/web/troubleshooting)

---

**🎯 Chúc mừng bạn đã hoàn thành tích hợp OAuth2!**
