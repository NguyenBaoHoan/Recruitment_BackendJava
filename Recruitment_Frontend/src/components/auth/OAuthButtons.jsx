import { useState } from 'react';
import OAUTH_CONFIG from '../../config/oauth.config';

/**
 * OAuth Login Buttons Component
 * 
 * ⚠️ Với httpOnly cookie, flow đơn giản hơn:
 * 1. User click button
 * 2. Redirect đến backend OAuth endpoint
 * 3. Backend handle toàn bộ OAuth flow
 * 4. Backend set cookie & redirect về frontend
 * 
 * KHÔNG cần:
 * - State management
 * - Code exchange
 * - Token handling
 * 
 * Props:
 * - providers: Array of provider names
 */
const OAuthButtons = ({ providers = ['google', 'github', 'facebook'] }) => {
    const [loading, setLoading] = useState(null);

    /**
     * Handle OAuth login
     * 
     * Flow:
     * 1. Set loading state
     * 2. Redirect browser đến backend OAuth endpoint
     * 3. Backend sẽ redirect đến provider (Google/GitHub/Facebook)
     * 4. User login & consent
     * 5. Provider callback về backend
     * 6. Backend set cookie & redirect về frontend /dashboard
     */
    const handleOAuthLogin = (providerName) => {
        const provider = OAUTH_CONFIG.providers[providerName];

        if (!provider) {
            console.error(`Provider ${providerName} not configured`);
            return;
        }

        setLoading(providerName);

        // Redirect đến backend OAuth endpoint
        // Backend sẽ handle toàn bộ OAuth flow
        window.location.href = provider.authUrl;
    };

    return (
        <div className="space-y-3">
            {/* Divider */}
            <div className="relative my-6">
                <div className="absolute inset-0 flex items-center">
                    <div className="w-full border-t border-gray-300"></div>
                </div>
                <div className="relative flex justify-center text-sm">
                    <span className="px-2 bg-white text-gray-500">Or continue with</span>
                </div>
            </div>

            {/* OAuth Buttons - Horizontal Layout with Icons Only */}
            <div className="flex justify-center gap-4">
                {providers.map((providerName) => {
                    const provider = OAUTH_CONFIG.providers[providerName];

                    if (!provider) return null;

                    const isLoading = loading === providerName;

                    return (
                        <button
                            key={providerName}
                            onClick={() => handleOAuthLogin(providerName)}
                            disabled={isLoading}
                            className={`
                flex items-center justify-center w-12 h-12 
                border-2 rounded-full font-medium text-gray-700
                transition-all duration-200
                hover:bg-gray-50 hover:border-gray-400 hover:shadow-md hover:scale-110
                disabled:opacity-50 disabled:cursor-not-allowed
                focus:outline-none focus:ring-2 focus:ring-offset-2
                group relative
              `}
                            style={{
                                borderColor: isLoading ? provider.color : '#E5E7EB',
                                '--tw-ring-color': provider.color
                            }}
                            title={`Continue with ${provider.displayName}`}
                        >
                            {isLoading ? (
                                <div
                                    className="animate-spin rounded-full h-5 w-5 border-b-2"
                                    style={{ borderColor: provider.color }}
                                ></div>
                            ) : (
                                <span className="text-2xl">{provider.icon}</span>
                            )}
                            
                            {/* Tooltip */}
                            <div className="absolute -top-10 left-1/2 transform -translate-x-1/2 
                                        bg-gray-900 text-white text-xs px-2 py-1 rounded 
                                        opacity-0 group-hover:opacity-100 transition-opacity duration-200
                                        whitespace-nowrap pointer-events-none">
                                {provider.displayName}
                            </div>
                        </button>
                    );
                })}
            </div>

        </div>
    );
};

export default OAuthButtons;