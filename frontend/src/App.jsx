import { Navigate, Route, Routes } from 'react-router-dom'

import AppNavbar from './components/AppNavbar'
import ProtectedRoute from './components/ProtectedRoute'

import ClientsPage from './pages/ClientsPage'
import DashboardPage from './pages/DashboardPage'
import EmployeesPage from './pages/EmployeesPage'
import LoginPage from './pages/LoginPage'
import OrdersPage from './pages/OrdersPage'
import RegisterPage from './pages/RegisterPage'
import ServicesPage from './pages/ServicesPage'
import UsersPage from './pages/UsersPage'

const App = () => (
  <>
    <AppNavbar />
    <Routes>
      <Route path="/" element={<Navigate to="/dashboard" replace />} />
      <Route path="/login" element={<LoginPage />} />
      <Route path="/register" element={<RegisterPage />} />
      <Route path="/dashboard" element={<ProtectedRoute><DashboardPage /></ProtectedRoute>} />
      <Route path="/clients" element={<ProtectedRoute><ClientsPage /></ProtectedRoute>} />
      <Route path="/employees" element={<ProtectedRoute><EmployeesPage /></ProtectedRoute>} />
      <Route path="/services" element={<ProtectedRoute><ServicesPage /></ProtectedRoute>} />
      <Route path="/orders" element={<ProtectedRoute><OrdersPage /></ProtectedRoute>} />
      <Route path="/users" element={<ProtectedRoute><UsersPage /></ProtectedRoute>} />
      <Route path="*" element={<Navigate to="/dashboard" replace />} />
    </Routes>
  </>
)

export default App
