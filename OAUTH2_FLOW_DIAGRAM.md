# 🔄 LUỒNG HOẠT ĐỘNG OAUTH2 - VISUAL GUIDE

## 📖 MỤC LỤC
1. [Tổng quan về OAuth2 Flow](#1-tổng-quan-về-oauth2-flow)
2. [Chi tiết từng bước](#2-chi-tiết-từng-bước)
3. [Sơ đồ các file tham gia](#3-sơ-đồ-các-file-tham-gia)
4. [Luồng data trong hệ thống](#4-luồng-data-trong-hệ-thống)
5. [So sánh 3 providers](#5-so-sánh-3-providers)

---

## 1. TỔNG QUAN VỀ OAUTH2 FLOW

### 🌊 Luồng tổng quan (High-level)

```
┌──────────────────────────────────────────────────────────────────────────┐
│                         OAUTH2 AUTHORIZATION FLOW                         │
└──────────────────────────────────────────────────────────────────────────┘

    User                Frontend            Backend              Provider
     │                     │                   │                     │
     │  (1) Click nút      │                   │                     │
     │  GitHub login       │                   │                     │
     ├────────────────────►│                   │                     │
     │                     │  (2) Redirect to  │                     │
     │                     │  backend OAuth    │                     │
     │                     │  endpoint         │                     │
     │                     ├──────────────────►│                     │
     │                     │                   │  (3) Redirect to    │
     │                     │                   │  GitHub with        │
     │                     │                   │  client_id, scope   │
     │                     │                   ├────────────────────►│
     │                     │                   │                     │
     │  (4) Login GitHub   │                   │                     │
     │  & Authorize app    │                   │                     │
     ├─────────────────────┴───────────────────┴────────────────────►│
     │                                                                │
     │  (5) GitHub redirect with authorization code                  │
     │◄──────────────────────────────────────────────────────────────┤
     │                     │                   │                     │
     │                     │                   │◄────────────────────┤
     │                     │                   │  (6) Exchange code  │
     │                     │                   │  for access_token   │
     │                     │                   ├────────────────────►│
     │                     │                   │◄────────────────────┤
     │                     │                   │  access_token       │
     │                     │                   │                     │
     │                     │                   │  (7) Get user info  │
     │                     │                   │  from GitHub API    │
     │                     │                   ├────────────────────►│
     │                     │                   │◄────────────────────┤
     │                     │                   │  User data          │
     │                     │                   │  (email, name)      │
     │                     │                   │                     │
     │                     │  (8) Redirect to  │                     │
     │                     │  frontend with    │                     │
     │                     │  JWT tokens       │                     │
     │                     │◄──────────────────┤                     │
     │  (9) Show Dashboard │                   │                     │
     │◄────────────────────┤                   │                     │
     │                     │                   │                     │
```

---

## 2. CHI TIẾT TỪNG BƯỚC

### 📍 **Bước 1: User click nút OAuth login**

**Location:** Frontend - `OAuthButtons.jsx`

```jsx
const handleOAuthLogin = (providerName) => {
    const provider = OAUTH_CONFIG.providers[providerName]; // 'github'
    console.log(`🚀 Đăng nhập với ${provider.displayName}`);
    
    // Redirect sang backend
    window.location.href = provider.authUrl;
    // URL: http://localhost:8080/oauth2/authorize/github
};
```

**Output:**
- Browser redirect sang backend endpoint
- URL: `http://localhost:8080/oauth2/authorize/github`

---

### 📍 **Bước 2: Backend nhận request và chuẩn bị OAuth2 Authorization Request**

**Location:** Backend - Spring Security xử lý tự động

**Process:**
1. Spring Security intercept request tại `/oauth2/authorize/github`
2. Load config từ `application.properties`:
   ```properties
   spring.security.oauth2.client.registration.github.client-id=Iv1.abc123
   spring.security.oauth2.client.registration.github.scope=read:user, user:email
   spring.security.oauth2.client.registration.github.redirect-uri=...
   ```
3. Tạo `OAuth2AuthorizationRequest` object:
   ```json
   {
     "authorizationUri": "https://github.com/login/oauth/authorize",
     "clientId": "Iv1.abc123",
     "scope": ["read:user", "user:email"],
     "state": "random-string-123",
     "redirectUri": "http://localhost:8080/api/v1/auth/oauth2/callback/github"
   }
   ```
4. Lưu request vào cookie (bằng `HttpCookieOAuth2AuthorizationRequestRepository`)
5. Build redirect URL sang GitHub

**Output:**
- Cookie `oauth2_auth_request` được set
- Browser redirect sang GitHub

---

### 📍 **Bước 3: Backend redirect User sang GitHub**

**URL được tạo:**
```
https://github.com/login/oauth/authorize
  ?client_id=Iv1.abc123def456
  &redirect_uri=http://localhost:8080/api/v1/auth/oauth2/callback/github
  &scope=read:user user:email
  &state=random-string-123
  &response_type=code
```

**Query params giải thích:**

| Param | Giá trị | Ý nghĩa |
|-------|---------|---------|
| `client_id` | `Iv1.abc123` | ID của OAuth App (từ application.properties) |
| `redirect_uri` | `http://localhost:8080/api/v1/auth/oauth2/callback/github` | URL mà GitHub sẽ gọi về sau khi user authorize |
| `scope` | `read:user user:email` | Quyền truy cập cần xin phép |
| `state` | `random-string-123` | Random token để chống CSRF attack |
| `response_type` | `code` | Yêu cầu GitHub trả về authorization code |

**Output:**
- User thấy trang login GitHub (nếu chưa login)
- User thấy trang "Authorize JobHunter Recruitment"

---

### 📍 **Bước 4: User login GitHub và authorize app**

**GitHub UI hiển thị:**
```
┌─────────────────────────────────────────┐
│  Authorize JobHunter Recruitment        │
├─────────────────────────────────────────┤
│  This app would like to:                │
│    ✓ Read your user profile info        │
│    ✓ Access your email addresses        │
│                                         │
│  [Cancel]       [Authorize app]         │
└─────────────────────────────────────────┘
```

**User actions:**
1. Đăng nhập GitHub (username + password)
2. Review permissions request
3. Click **Authorize app**

**Output:**
- GitHub tạo authorization code (VD: `abc123xyz789`)
- GitHub redirect browser về `redirect_uri`

---

### 📍 **Bước 5: GitHub redirect về Backend với authorization code**

**URL callback:**
```
http://localhost:8080/api/v1/auth/oauth2/callback/github
  ?code=abc123xyz789
  &state=random-string-123
```

**Backend nhận:**
- `code`: Authorization code (1 lần dùng, expires sau 10 phút)
- `state`: Verify khớp với state đã gửi ở Bước 3 (chống CSRF)

---

### 📍 **Bước 6: Backend đổi code lấy access_token từ GitHub**

**Location:** Spring Security OAuth2 Client tự động xử lý

**HTTP Request gửi đến GitHub:**
```http
POST https://github.com/login/oauth/access_token
Content-Type: application/x-www-form-urlencoded

client_id=Iv1.abc123
&client_secret=1234567890abcdef
&code=abc123xyz789
&redirect_uri=http://localhost:8080/api/v1/auth/oauth2/callback/github
```

**GitHub Response:**
```json
{
  "access_token": "gho_16C7e42F292c6912E7710c838347Ae178B4a",
  "token_type": "bearer",
  "scope": "read:user,user:email"
}
```

**Output:**
- Backend có `access_token` để gọi GitHub API

---

### 📍 **Bước 7: Backend gọi GitHub API lấy user info**

**HTTP Request:**
```http
GET https://api.github.com/user
Authorization: Bearer gho_16C7e42F292c6912E7710c838347Ae178B4a
```

**GitHub Response:**
```json
{
  "login": "johndoe",
  "id": 12345678,
  "name": "John Doe",
  "email": "john@example.com",
  "avatar_url": "https://avatars.githubusercontent.com/u/12345678",
  "bio": "Software developer",
  "location": "Vietnam"
}
```

**Backend mapping:**
- `email` → User.email
- `name` hoặc `login` → User.name (handle null trong SecurityConfiguration)
- Avatar, bio → Có thể lưu vào DB nếu muốn

---

### 📍 **Bước 8: Backend xử lý trong Success Handler**

**Location:** `SecurityConfiguration.java` - `oAuth2AuthenticationSuccessHandler`

**Process:**

```java
@Bean
public AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler(...) {
    return (request, response, authentication) -> {
        // 1. Lấy user info từ authentication
        OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
        String email = oidcUser.getEmail();      // "john@example.com"
        String name = oidcUser.getFullName();    // null (GitHub không có)
        
        // 2. Xử lý name null
        if (name == null) {
            name = oidcUser.getPreferredUsername(); // "johndoe"
        }
        
        // 3. Tìm hoặc tạo user trong database
        User user = userService.handleGetUserByEmail(email);
        if (user == null) {
            user = authService.registerOauthUser(email, name);
            // Tạo user mới với email, name, và random password
        }
        
        // 4. Tạo JWT access token
        String accessToken = securityUtil.createAccessToken(email, ...);
        // accessToken: "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
        
        // 5. Tạo refresh token
        String refreshToken = securityUtil.createRefreshToken(email, ...);
        // refreshToken: "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
        
        // 6. Lưu refresh token vào database
        userService.updateUserToken(refreshToken, email);
        
        // 7. Set refresh token cookie
        ResponseCookie cookie = ResponseCookie
            .from("refresh_token", refreshToken)
            .httpOnly(true)
            .secure(true)
            .path("/")
            .maxAge(8640000) // 100 days
            .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        
        // 8. Build redirect URL về frontend
        String redirectUrl = UriComponentsBuilder
            .fromUriString(frontendUrl + "/login")
            .queryParam("oauth", "success")
            .queryParam("provider", "github")
            .queryParam("email", email)
            .queryParam("name", name)
            .queryParam("access_token", accessToken)
            .build().toUriString();
        
        // 9. Redirect
        response.sendRedirect(redirectUrl);
    };
}
```

**Output:**
- User được tạo/cập nhật trong database
- Cookie `refresh_token` được set
- Browser redirect về frontend với JWT token

---

### 📍 **Bước 9: Frontend nhận kết quả và hiển thị dashboard**

**Location:** `LoginPage.jsx`

**URL nhận được:**
```
http://localhost:5173/login
  ?oauth=success
  &provider=github
  &email=john@example.com
  &name=johndoe
  &access_token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**Process:**

```jsx
useEffect(() => {
    const params = new URLSearchParams(location.search);
    const oauthStatus = params.get('oauth');       // "success"
    const accessToken = params.get('access_token'); // JWT token
    const userEmail = params.get('email');          // "john@example.com"
    const userName = params.get('name');            // "johndoe"
    
    if (oauthStatus === 'success') {
        console.log('✅ OAuth2 success:', { userName, userEmail });
        
        // Lưu token vào AuthContext (state management)
        // AuthContext sẽ lưu vào memory và verify token
        
        // Xóa query params khỏi URL
        window.history.replaceState({}, document.title, location.pathname);
        
        // Redirect sang dashboard
        navigate('/dashboard');
    }
}, [location]);
```

**Output:**
- User thấy dashboard
- Token được lưu trong context
- Cookie `refresh_token` được lưu trong browser

---

## 3. SƠ ĐỒ CÁC FILE THAM GIA

### 📁 **Frontend Files**

```
Recruitment_Frontend/
│
├─ src/
│  ├─ config/
│  │  └─ oauth.config.jsx ─────────┐
│  │     ├─ Define authUrl          │ (1) Config
│  │     └─ Define provider icons   │
│  │                                 │
│  ├─ components/auth/               │
│  │  └─ OAuthButtons.jsx ──────────┤
│  │     ├─ Render 3 nút            │ (2) UI Component
│  │     └─ Handle click            │
│  │                                 │
│  ├─ pages/                         │
│  │  └─ LoginPage.jsx ─────────────┤
│  │     ├─ Nhận OAuth callback     │ (3) Page Handler
│  │     └─ Parse query params      │
│  │                                 │
│  └─ context/                       │
│     └─ AuthContext.jsx ────────────┘
│        ├─ Lưu access_token        (4) State Management
│        └─ Lưu user info
```

### 📁 **Backend Files**

```
Recruitment_Backend/
│
├─ src/main/resources/
│  └─ application.properties ───────┐
│     ├─ GitHub OAuth2 config       │ (1) Configuration
│     └─ Facebook OAuth2 config     │
│                                    │
├─ src/main/java/.../config/        │
│  └─ SecurityConfiguration.java ───┤
│     ├─ oauth2Login() config       │ (2) Security Config
│     ├─ Success Handler            │
│     └─ Failure Handler            │
│                                    │
├─ src/main/java/.../service/       │
│  └─ AuthService.java ─────────────┤
│     └─ registerOauthUser()        │ (3) Business Logic
│                                    │
├─ src/main/java/.../util/          │
│  ├─ HttpCookieOAuth2...java ──────┤
│  │  └─ Lưu OAuth2 request         │ (4) Utility
│  │                                 │
│  ├─ CookieUtil.java ───────────────┤
│  │  └─ Cookie helpers             │ (5) Utility
│  │                                 │
│  └─ SecurityUtil.java ─────────────┘
│     ├─ createAccessToken()        (6) JWT Helper
│     └─ createRefreshToken()
```

---

## 4. LUỒNG DATA TRONG HỆ THỐNG

### 🔄 **Data Flow Diagram**

```
┌─────────────────────────────────────────────────────────────────────────┐
│                          DATA TRANSFORMATION                             │
└─────────────────────────────────────────────────────────────────────────┘

[User Click]
     │
     ├─► providerName: "github"
     │
     └─► authUrl: "http://localhost:8080/oauth2/authorize/github"
            │
            │ [Frontend → Backend]
            ▼
      OAuth2AuthorizationRequest
        ├─ clientId: "Iv1.abc123"
        ├─ scope: ["read:user", "user:email"]
        ├─ redirectUri: "http://localhost:8080/api/v1/auth/oauth2/callback/github"
        └─ state: "random-string"
            │
            │ [Backend → GitHub]
            ▼
      GitHub Authorization URL
        URL: https://github.com/login/oauth/authorize?client_id=...
            │
            │ [User Login & Authorize]
            ▼
      Authorization Code
        code: "abc123xyz789"
        state: "random-string"
            │
            │ [GitHub → Backend]
            ▼
      Access Token Exchange
        Request: POST /login/oauth/access_token
        Response: { access_token: "gho_16C7e42F..." }
            │
            │ [Backend → GitHub API]
            ▼
      User Info
        GET /user
        Response: {
          "login": "johndoe",
          "email": "john@example.com",
          "name": null
        }
            │
            │ [GitHub API → Backend Handler]
            ▼
      OidcUser Object (Spring Security)
        ├─ email: "john@example.com"
        ├─ fullName: null
        ├─ preferredUsername: "johndoe"
        └─ attributes: { id: 12345, avatar_url: "...", ... }
            │
            │ [Handler Processing]
            ▼
      User Entity (Database)
        INSERT INTO user (email, name, password)
        VALUES ('john@example.com', 'johndoe', '$2a$10$hashed...')
            │
            │ [Create Tokens]
            ▼
      JWT Tokens
        ├─ accessToken: "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
        │   ├─ Payload: { sub: "john@example.com", permission: [...], exp: ... }
        │   └─ Expires: 100 days
        │
        └─ refreshToken: "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
            ├─ Payload: { sub: "john@example.com", user: {...}, exp: ... }
            └─ Expires: 100 days
            │
            │ [Set Cookie & Redirect]
            ▼
      HTTP Response
        Status: 302 Found
        Set-Cookie: refresh_token=eyJhbGci...; HttpOnly; Secure; Path=/
        Location: http://localhost:5173/login?oauth=success&access_token=eyJhbGci...
            │
            │ [Backend → Frontend]
            ▼
      Frontend State
        AuthContext:
          ├─ user: { id: 1, email: "john@example.com", name: "johndoe" }
          ├─ token: "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
          └─ isAuthenticated: true
            │
            │ [Redirect]
            ▼
      Dashboard Page
        User logged in successfully! 🎉
```

---

## 5. SO SÁNH 3 PROVIDERS

### 📊 **Google vs GitHub vs Facebook**

| Khía cạnh | Google | GitHub | Facebook |
|-----------|--------|--------|----------|
| **Authorization URL** | `https://accounts.google.com/o/oauth2/v2/auth` | `https://github.com/login/oauth/authorize` | `https://www.facebook.com/v10.0/dialog/oauth` |
| **Token URL** | `https://oauth2.googleapis.com/token` | `https://github.com/login/oauth/access_token` | `https://graph.facebook.com/v10.0/oauth/access_token` |
| **User Info API** | `https://www.googleapis.com/oauth2/v3/userinfo` | `https://api.github.com/user` | `https://graph.facebook.com/me` |
| **Scope** | `openid, profile, email` | `read:user, user:email` | `email, public_profile` |
| **User Data** | ✅ email, name, picture | ⚠️ email (nếu public), login | ✅ email, name, id |
| **fullName field** | ✅ Có | ❌ Không (dùng login) | ✅ Có |
| **Email availability** | ✅ Luôn có | ⚠️ Phụ thuộc user public | ✅ Luôn có (nếu permission OK) |
| **Setup complexity** | 🟢 Dễ | 🟢 Dễ | 🟡 Trung bình |
| **App Review** | ❌ Không cần | ❌ Không cần | ⚠️ Cần (nếu dùng permissions ngoài basic) |
| **Redirect URI** | `/oauth2/callback/google` | `/oauth2/callback/github` | `/oauth2/callback/facebook` |

### 🔍 **Chi tiết User Info Response**

#### **Google:**
```json
{
  "sub": "123456789",
  "name": "John Doe",
  "given_name": "John",
  "family_name": "Doe",
  "picture": "https://lh3.googleusercontent.com/...",
  "email": "john@gmail.com",
  "email_verified": true,
  "locale": "en"
}
```
✅ **Mapping:** `name` → User.name, `email` → User.email

#### **GitHub:**
```json
{
  "login": "johndoe",
  "id": 12345678,
  "name": null,
  "email": "john@example.com",
  "avatar_url": "https://avatars.githubusercontent.com/u/12345678",
  "bio": "Software developer",
  "location": "Vietnam",
  "company": "ABC Corp"
}
```
⚠️ **Lưu ý:** `name` có thể null → Dùng `login` thay thế

#### **Facebook:**
```json
{
  "id": "123456789012345",
  "name": "John Doe",
  "email": "john@example.com",
  "picture": {
    "data": {
      "url": "https://platform-lookaside.fbsbx.com/..."
    }
  }
}
```
✅ **Mapping:** `name` → User.name, `email` → User.email

---

## 6. LUỒNG LỖI (ERROR FLOW)

### ❌ **Khi có lỗi xảy ra**

```
[User authorize]
     │
     ├─► ❌ User từ chối permission
     │   └─► GitHub redirect: ?error=access_denied
     │       └─► Backend: oAuth2AuthenticationFailureHandler
     │           └─► Frontend: ?error=OAuth2 login failed
     │               └─► LoginPage: Hiển thị error message
     │
     ├─► ❌ Email không available (GitHub hidden email)
     │   └─► Backend: Success Handler check email null
     │       └─► Frontend: ?error=Email not available
     │           └─► LoginPage: Hiển thị error message
     │
     ├─► ❌ Redirect URI mismatch
     │   └─► Provider: error page "redirect_uri_mismatch"
     │       └─► User stuck → Không redirect về app
     │
     └─► ❌ Backend error (DB connection, JWT creation)
         └─► Backend: Exception thrown
             └─► Failure Handler: Catch exception
                 └─► Frontend: ?error=Internal server error
                     └─► LoginPage: Hiển thị generic error
```

---

## 📚 TÓM TẮT

### ✅ **Key Takeaways:**

1. **OAuth2 Flow có 9 bước chính:**
   - User click → Backend authorize → Provider login → Code callback → Token exchange → User info → JWT creation → Redirect → Dashboard

2. **Spring Security xử lý tự động:**
   - Bước 2-7 hoàn toàn tự động
   - Chỉ cần config properties và implement handlers

3. **3 providers tương tự nhưng có khác biệt:**
   - Google: Đơn giản nhất, luôn có đủ data
   - GitHub: Cần xử lý name null, email có thể hidden
   - Facebook: Cần Live mode, có thể cần App Review

4. **Security được đảm bảo:**
   - State token chống CSRF
   - JWT token cho stateless auth
   - HttpOnly cookie cho refresh token
   - HTTPS trong production

5. **Dễ dàng mở rộng:**
   - Thêm provider mới chỉ cần config properties
   - Logic xử lý dùng chung
   - Frontend component dynamic

---

**🎯 Hiểu rõ luồng này giúp bạn debug và mở rộng dễ dàng hơn!**
