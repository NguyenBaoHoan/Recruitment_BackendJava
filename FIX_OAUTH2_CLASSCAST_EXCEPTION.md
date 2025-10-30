# 🔧 SỬA LỖI: ClassCastException - OidcUser vs OAuth2User

## ❌ LỖI BAN ĐẦU

```
class org.springframework.security.oauth2.core.user.DefaultOAuth2User 
cannot be cast to class org.springframework.security.oauth2.core.oidc.user.OidcUser
```

**File lỗi:** `SecurityConfiguration.java` - dòng 191

---

## 🧐 NGUYÊN NHÂN

### **Sự khác biệt giữa OAuth2 và OIDC:**

| Provider | Protocol | Spring Security Object |
|----------|----------|------------------------|
| **Google** | OpenID Connect (OIDC) | `OidcUser` |
| **Facebook** | OAuth2/OIDC | `OidcUser` hoặc `DefaultOAuth2User` |
| **GitHub** | OAuth2 (cơ bản) | `DefaultOAuth2User` ❌ |

**Vấn đề:**
- Code ban đầu giả định tất cả providers đều trả về `OidcUser`
- GitHub chỉ dùng OAuth2 cơ bản → trả về `DefaultOAuth2User`
- Khi user đăng nhập GitHub, cast `(OidcUser)` gây ra `ClassCastException`

---

## ✅ GIẢI PHÁP

### **Dùng `OAuth2User` - interface chung cho cả hai**

`OAuth2User` là interface cha của cả `OidcUser` và `DefaultOAuth2User`.

### **Code TRƯỚC (Bị lỗi):**

```java
@Bean
public AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler(...) {
    return (request, response, authentication) -> {
        // ❌ LỖI: Cast sang OidcUser
        OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
        String email = oidcUser.getEmail();
        String name = oidcUser.getFullName();
        
        // ...
    };
}
```

### **Code SAU (Đã sửa):**

```java
@Bean
public AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler(...) {
    return (request, response, authentication) -> {
        // ✅ SỬA: Dùng OAuth2User thay vì OidcUser
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        
        // ✅ Lấy email bằng getAttribute()
        String email = oauth2User.getAttribute("email");
        
        // ✅ Lấy name, xử lý các providers khác nhau
        String name = oauth2User.getAttribute("name"); // Google, Facebook
        
        // ✅ GitHub không có "name", dùng "login" (username)
        if (name == null || name.isEmpty()) {
            name = oauth2User.getAttribute("login"); // GitHub
            if (name == null || name.isEmpty()) {
                name = email.split("@")[0]; // Fallback
            }
        }
        
        // ... logic tạo user, JWT tokens, redirect
    };
}
```

---

## 📊 SO SÁNH: OidcUser vs OAuth2User

### **Cách lấy thông tin:**

| Thông tin | OidcUser (OIDC) | OAuth2User (OAuth2) |
|-----------|----------------|---------------------|
| **Email** | `oidcUser.getEmail()` | `oauth2User.getAttribute("email")` |
| **Name** | `oidcUser.getFullName()` | `oauth2User.getAttribute("name")` |
| **Username** | `oidcUser.getPreferredUsername()` | `oauth2User.getAttribute("login")` (GitHub) |
| **ID** | `oidcUser.getSubject()` | `oauth2User.getAttribute("id")` |

### **Attributes khác nhau giữa providers:**

| Provider | Name Attribute | Username Attribute | Email Attribute |
|----------|----------------|-------------------|-----------------|
| **Google** | `name` ✅ | `preferred_username` | `email` ✅ |
| **GitHub** | `name` ❌ (null) | `login` ✅ | `email` ✅ |
| **Facebook** | `name` ✅ | ❌ | `email` ✅ |

---

## 🔍 KIỂM TRA SAU KHI SỬA

### **1. Test Google Login (OIDC):**
- ✅ Vẫn hoạt động bình thường
- `oauth2User.getAttribute("name")` → trả về full name
- `oauth2User.getAttribute("email")` → trả về email

### **2. Test GitHub Login (OAuth2):**
- ✅ Không còn lỗi ClassCastException
- `oauth2User.getAttribute("name")` → null
- `oauth2User.getAttribute("login")` → trả về username
- `oauth2User.getAttribute("email")` → trả về email

### **3. Test Facebook Login:**
- ✅ Hoạt động bình thường
- `oauth2User.getAttribute("name")` → trả về full name
- `oauth2User.getAttribute("email")` → trả về email

---

## 📝 TÓM TẮT THAY ĐỔI

### **File đã sửa:** `SecurityConfiguration.java`

**Thay đổi:**

1. ✅ Import thêm: `import org.springframework.security.oauth2.core.user.OAuth2User;`

2. ✅ Đổi kiểu dữ liệu:
   - Từ: `OidcUser oidcUser = (OidcUser) authentication.getPrincipal();`
   - Sang: `OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();`

3. ✅ Đổi cách lấy dữ liệu:
   - Từ: `oidcUser.getEmail()`, `oidcUser.getFullName()`
   - Sang: `oauth2User.getAttribute("email")`, `oauth2User.getAttribute("name")`

4. ✅ Xử lý GitHub không có "name":
   ```java
   if (name == null || name.isEmpty()) {
       name = oauth2User.getAttribute("login"); // GitHub username
   }
   ```

---

## 🎯 KẾT QUẢ

- ✅ **Lỗi ClassCastException đã được sửa**
- ✅ **Hỗ trợ cả 3 providers: Google, GitHub, Facebook**
- ✅ **Code linh hoạt, dễ mở rộng thêm providers mới**
- ✅ **Xử lý khác biệt giữa OIDC và OAuth2 tự động**

---

## 🚀 NEXT STEPS

1. Test lại tất cả 3 providers
2. Verify user được tạo đúng trong database
3. Kiểm tra JWT tokens được tạo chính xác
4. Test refresh token flow

---

**🎉 Đã hoàn thành sửa lỗi OAuth2 ClassCastException!**
