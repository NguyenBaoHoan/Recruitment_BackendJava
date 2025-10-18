import { createContext, useContext, useState } from 'react';

/**
 * NotificationContext - Quản lý notifications toàn cục
 */

const NotificationContext = createContext();

export const useNotification = () => {
  const context = useContext(NotificationContext);
  if (!context) {
    throw new Error('useNotification must be used within NotificationProvider');
  }
  return context;
};

export const NotificationProvider = ({ children }) => {
  const [notifications, setNotifications] = useState([]);

  const showNotification = (type, message, options = {}) => {
    const id = Date.now() + Math.random();
    const notification = {
      id,
      type,
      message,
      autoHide: options.autoHide !== false,
      duration: options.duration || 3000,
      ...options
    };

    setNotifications(prev => [...prev, notification]);

    // Auto remove nếu autoHide = true
    if (notification.autoHide) {
      setTimeout(() => {
        removeNotification(id);
      }, notification.duration);
    }

    return id;
  };

  const removeNotification = (id) => {
    setNotifications(prev => prev.filter(notif => notif.id !== id));
  };

  const clearAllNotifications = () => {
    setNotifications([]);
  };

  // Helper methods
  const showSuccess = (message, options) => showNotification('success', message, options);
  const showError = (message, options) => showNotification('error', message, options);
  const showInfo = (message, options) => showNotification('info', message, options);
  const showWarning = (message, options) => showNotification('warning', message, options);

  const value = {
    notifications,
    showNotification,
    removeNotification,
    clearAllNotifications,
    showSuccess,
    showError,
    showInfo,
    showWarning
  };

  return (
    <NotificationContext.Provider value={value}>
      {children}
    </NotificationContext.Provider>
  );
};