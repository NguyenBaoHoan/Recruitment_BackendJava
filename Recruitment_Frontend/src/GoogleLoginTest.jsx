import React, { useState, useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';

// URL để bắt đầu luồng đăng nhập (trỏ đến backend)
const GOOGLE_AUTH_URL = 'http://localhost:8080/oauth2/authorize/google';

const GoogleLoginTest = () => {
  const [userInfo, setUserInfo] = useState(null);
  const [token, setToken] = useState(null);
  const [error, setError] = useState(null);

  const location = useLocation();
  const navigate = useNavigate();

  // 1. XỬ LÝ CALLBACK KHI TRANG ĐƯỢC TẢI
  useEffect(() => {
    // Lấy các tham số từ URL
    const params = new URLSearchParams(location.search);
    const oauthStatus = params.get('oauth');
    const accessToken = params.get('access_token');
    const userEmail = params.get('email');
    const userName = params.get('name');
    const oauthError = params.get('error');

    // ✅ TRƯỜNG HỢP THÀNH CÔNG
    if (oauthStatus === 'success' && accessToken) {
      console.log("✅ [TEST] Đăng nhập THÀNH CÔNG");
      console.log("   - Tên User:", userName);
      console.log("   - Email User:", userEmail);
      console.log("   - Access Token:", accessToken);

      // Lưu state để hiển thị
      setToken(accessToken);
      setUserInfo({ email: userEmail, name: userName });
      setError(null);

      // Xóa các tham số khỏi URL
      navigate('.', { replace: true });
    }
    
    // ❌ TRƯỜNG HỢP THẤT BẠI
    if (oauthError) {
      console.error("❌ [TEST] Đăng nhập THẤT BẠI");
      console.error("   - Lý do:", oauthError);

      // Lưu lỗi để hiển thị
      setError(oauthError);
      setToken(null);
      setUserInfo(null);

      // Xóa các tham số khỏi URL
      navigate('.', { replace: true });
    }

  }, [location, navigate]);

  // 2. HÀM KÍCH HOẠT ĐĂNG NHẬP
  const handleGoogleLogin = () => {
    console.log("🚀 [TEST] Bắt đầu đăng nhập, chuyển hướng đến Backend...");
    // Chuyển hướng đến backend, backend sẽ lo việc redirect sang Google
    window.location.href = GOOGLE_AUTH_URL;
  };

  return (
    <div className="w-full max-w-lg p-8 space-y-6 bg-white rounded-lg shadow-md">
      <h1 className="text-2xl font-bold text-center text-gray-900">
        Test Đăng nhập Google
      </h1>

      {/* Nút đăng nhập */}
      <button
        type="button"
        onClick={handleGoogleLogin}
        className="w-full flex justify-center items-center px-4 py-3 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50"
      >
        <svg className="w-5 h-5 mr-2" fill="currentColor" viewBox="0 0 20 20">
            {/* (Google icon SVG) */}
            <path d="M10 0C4.477 0 0 4.477 0 10s4.477 10 10 10 10-4.477 10-10S15.523 0 10 0zM10 18a8 8 0 1 1 0-16 8 8 0 0 1 0 16z" />
            <path d="M10 3.25c1.755 0 3.32.67 4.5 1.75l-1.5 1.5C12.5 5.5 11.375 5 10 5 8.125 5 6.5 6.125 6 7.75L4.5 6.5C5.33 4.5 7.5 3.25 10 3.25z" />
            <path d="M10 15c-1.625 0-3-1.125-3.5-2.75l-1.5 1.5C6 15.5 7.875 16.75 10 16.75c2.5 0 4.5-1.5 5.25-3.75l-1.5-1.5C13 13.875 11.625 15 10 15z" />
            <path d="M17.5 10a7.5 7.5 0 0 0-7.5-7.5c-1.06 0-2.06.25-2.94.69L8.5 4.5h3V6H9.25C9.12 6.5 9 7.125 9 7.75c0 .625.125 1.25.25 1.75H12v1.5h-2.5l-1.44 1.31C8.94 13.06 9.94 13.25 10 13.25a7.5 7.5 0 0 0 7.5-7.5z" />
        </svg>
        Đăng nhập với Google
      </button>

      {/* Khu vực hiển thị kết quả */}
      <div className="pt-6 mt-6 border-t">
        <h2 className="text-lg font-medium text-gray-800">Kết quả Test:</h2>
        
        {/* Trường hợp thành công */}
        {token && userInfo && (
          <div className="mt-4 p-4 bg-green-50 border border-green-300 rounded-md">
            <h3 className="font-bold text-green-800">✅ Đăng nhập thành công!</h3>
            <p className="mt-2 text-sm text-gray-700"><strong>Tên:</strong> {userInfo.name}</p>
            <p className="text-sm text-gray-700"><strong>Email:</strong> {userInfo.email}</p>
            <p className="mt-2 text-sm text-gray-700"><strong>Access Token:</strong></p>
            <textarea
              readOnly
              className="w-full p-2 mt-1 text-xs text-gray-600 border rounded bg-gray-50 h-28"
              value={token}
            />
          </div>
        )}

        {/* Trường hợp thất bại */}
        {error && (
          <div className="mt-4 p-4 bg-red-50 border border-red-300 rounded-md">
            <h3 className="font-bold text-red-800">❌ Đăng nhập thất bại!</h3>
            <p className="mt-2 text-sm text-red-700"><strong>Lý do:</strong> {error}</p>
          </div>
        )}

        {/* Trường hợp mặc định */}
        {!token && !error && (
          <p className="mt-4 text-gray-500">
            Chưa đăng nhập. Hãy nhấp nút ở trên để bắt đầu.
          </p>
        )}
      </div>
    </div>
  );
};

export default GoogleLoginTest;
