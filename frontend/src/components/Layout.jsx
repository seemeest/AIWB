import { NavLink, Outlet } from 'react-router-dom'
import { useAuth } from '../auth/AuthContext'

const roleLabels = {
  BUYER: 'Покупатель',
  SELLER: 'Продавец',
  MODERATOR: 'Модератор',
}

function NavItem({ to, label }) {
  return (
    <NavLink
      to={to}
      className={({ isActive }) => `nav-item${isActive ? ' active' : ''}`}
    >
      {label}
    </NavLink>
  )
}

export function Layout() {
  const auth = useAuth()
  const roles = auth?.user?.roles || []
  const isSeller = roles.includes('SELLER')
  const isModerator = roles.includes('MODERATOR')

  return (
    <div className="app-shell">
      <aside className="sidebar">
        <div className="brand">
          <div className="brand-mark">AIWB</div>
          <div className="brand-subtitle">Marketplace</div>
        </div>
        <nav className="nav">
          <NavItem to="/catalog" label="Каталог" />
          <NavItem to="/cart" label="Корзина" />
          <NavItem to="/orders" label="Заказы" />
          {isSeller && <NavItem to="/seller" label="Кабинет продавца" />}
          {isSeller && <NavItem to="/metrics" label="Метрики" />}
          {isModerator && <NavItem to="/moderation" label="Модерация" />}
          <NavItem to="/notifications" label="Уведомления" />
        </nav>
        <div className="sidebar-footer">
          {auth?.user ? (
            <>
              <div className="user-pill">
                <div>{auth.user.email}</div>
                <div className="user-role">
                  {roles.map((role) => roleLabels[role] || role).join(', ')}
                </div>
              </div>
              <button className="btn ghost" onClick={auth.logout}>
                Выйти
              </button>
            </>
          ) : (
            <div className="auth-links">
              <NavLink to="/login" className="btn">
                Войти
              </NavLink>
              <NavLink to="/register" className="btn ghost">
                Регистрация
              </NavLink>
            </div>
          )}
        </div>
      </aside>
      <main className="main">
        <Outlet />
      </main>
    </div>
  )
}
