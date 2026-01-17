import { BrowserRouter, Navigate, Route, Routes } from 'react-router-dom'
import './App.css'
import { AuthProvider } from './auth/AuthContext'
import { RequireRole } from './auth/RequireRole'
import { Layout } from './components/Layout'
import { CatalogPage } from './pages/CatalogPage'
import { ProductPage } from './pages/ProductPage'
import { CartPage } from './pages/CartPage'
import { OrdersPage } from './pages/OrdersPage'
import { SellerPage } from './pages/SellerPage'
import { ModerationPage } from './pages/ModerationPage'
import { NotificationsPage } from './pages/NotificationsPage'
import { MetricsPage } from './pages/MetricsPage'
import { LoginPage } from './pages/LoginPage'
import { RegisterPage } from './pages/RegisterPage'
import { ResetRequestPage } from './pages/ResetRequestPage'
import { ResetConfirmPage } from './pages/ResetConfirmPage'
import { NotFoundPage } from './pages/NotFoundPage'
import { ForbiddenPage } from './pages/ForbiddenPage'
import { CartProvider } from './state/CartContext'

function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <CartProvider>
          <Routes>
            <Route element={<Layout />}>
              <Route index element={<Navigate to="/catalog" replace />} />
              <Route path="/catalog" element={<CatalogPage />} />
              <Route path="/products/:id" element={<ProductPage />} />
              <Route
                path="/cart"
                element={
                  <RequireRole roles={['BUYER', 'SELLER', 'MODERATOR']}>
                    <CartPage />
                  </RequireRole>
                }
              />
              <Route
                path="/orders"
                element={
                  <RequireRole roles={['BUYER', 'SELLER', 'MODERATOR']}>
                    <OrdersPage />
                  </RequireRole>
                }
              />
              <Route
                path="/seller"
                element={
                  <RequireRole roles={['SELLER']}>
                    <SellerPage />
                  </RequireRole>
                }
              />
              <Route
                path="/moderation"
                element={
                  <RequireRole roles={['MODERATOR']}>
                    <ModerationPage />
                  </RequireRole>
                }
              />
              <Route
                path="/notifications"
                element={
                  <RequireRole roles={['BUYER', 'SELLER', 'MODERATOR']}>
                    <NotificationsPage />
                  </RequireRole>
                }
              />
              <Route
                path="/metrics"
                element={
                  <RequireRole roles={['SELLER']}>
                    <MetricsPage />
                  </RequireRole>
                }
              />
              <Route path="*" element={<NotFoundPage />} />
            </Route>
            <Route path="/login" element={<LoginPage />} />
            <Route path="/register" element={<RegisterPage />} />
            <Route path="/reset" element={<ResetRequestPage />} />
            <Route path="/reset/confirm" element={<ResetConfirmPage />} />
            <Route path="/forbidden" element={<ForbiddenPage />} />
          </Routes>
        </CartProvider>
      </AuthProvider>
    </BrowserRouter>
  )
}

export default App
