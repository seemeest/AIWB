import { useState } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import { useAuth } from '../auth/AuthContext'
import { resolveErrorMessage } from '../utils/errorMessages'

export function LoginPage() {
  const auth = useAuth()
  const navigate = useNavigate()
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)

  const handleSubmit = async (event) => {
    event.preventDefault()
    setError('')
    setLoading(true)
    try {
      await auth.login(email, password)
      navigate('/catalog')
    } catch (err) {
      setError(resolveErrorMessage(err, 'Ошибка входа'))
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="auth-shell">
      <div className="auth-card">
        <h1>Вход</h1>
        <p className="muted">Войдите, чтобы управлять заказами и покупками</p>
        {error && <div className="alert">{error}</div>}
        <form onSubmit={handleSubmit} className="form">
          <label>
            Email
            <input value={email} onChange={(e) => setEmail(e.target.value)} type="email" required />
          </label>
          <label>
            Пароль
            <input value={password} onChange={(e) => setPassword(e.target.value)} type="password" required />
          </label>
          <button className="btn primary" type="submit" disabled={loading}>
            {loading ? 'Вход...' : 'Войти'}
          </button>
        </form>
        <div className="auth-links-row">
          <Link to="/register">Создать аккаунт</Link>
          <Link to="/reset">Забыли пароль?</Link>
        </div>
      </div>
    </div>
  )
}
