import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { createBrowserRouter, RouterProvider, Navigate } from "react-router-dom"
import './styles/index.css'
import  AuthProvider  from './context/AuthProvider'
import ProtectedRoute from './components/layout/ProtectedRoute'


import LoginPage from './pages/LoginPage'
import RegisterPage from './pages/RegisterPage'
// import DashboardPage from './pages/DashboardPage'


const router = createBrowserRouter([
  {
    path: "/login",
    element: <LoginPage />
  },
  {
    path: "/register",
    element: <RegisterPage />
  },
  // {
  //   path: "/dashboard",
  //   element: (
  //     <ProtectedRoute>
  //       <DashboardPage />
  //     </ProtectedRoute>
  //   )
  // },
  // {
  //   path: "/home",
  //   element: <HomePage />
  // }
])

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <AuthProvider>
      <RouterProvider router={router} />
    </AuthProvider>
  </StrictMode>,
)