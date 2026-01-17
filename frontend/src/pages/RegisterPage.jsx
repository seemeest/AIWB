import { useState } from 'react'
import { Link } from 'react-router-dom'
import { register } from '../api/auth'

export function RegisterPage() {
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [role, setRole] = useState('BUYER')
  const [token, setToken] = useState('')
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)

  const handleSubmit = async (event) => {
    event.preventDefault()
    setError('')
    setLoading(true)
    try {
      const result = await register({ email, password, roles: [role] })
      setToken(result.verificationToken)
    } catch (err) {
      setError(err?.response?.data?.message || 'Ошибка регистрации')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="auth-shell">
      <div className="auth-card">
        <h1>Регистрация</h1>
        <p className="muted">Создайте аккаунт и подтвердите email</p>
        {error && <div className="alert">{error}</div>}
        {token && (
          <div className="token-box">
            Токен подтверждения: <strong>{token}</strong>
          </div>
        )}
        <form onSubmit={handleSubmit} className="form">
          <label>
            Email
            <input value={email} onChange={(e) => setEmail(e.target.value)} type="email" required />
          </label>
          <label>
            Пароль
            <input value={password} onChange={(e) => setPassword(e.target.value)} type="password" required />
          </label>
          <label>
            Роль
            <select value={role} onChange={(e) => setRole(e.target.value)}>
              <option value="BUYER">Покупатель</option>
              <option value="SELLER">Продавец</option>
            </select>
          </label>
          <button className="btn primary" type="submit" disabled={loading}>
            {loading ? 'Создание...' : 'Создать'}
          </button>
        </form>
        <div className="auth-links-row">
          <Link to="/login">Уже есть аккаунт</Link>
        </div>
      </div>
    </div>
  )
}
