import { useState } from 'react'
import { Link } from 'react-router-dom'
import { register } from '../api/auth'
import { resolveErrorMessage } from '../utils/errorMessages'

export function RegisterPage() {
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [fullName, setFullName] = useState('')
  const [birthDate, setBirthDate] = useState('')
  const [role, setRole] = useState('BUYER')
  const [token, setToken] = useState('')
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)

  const handleSubmit = async (event) => {
    event.preventDefault()
    setError('')
    setLoading(true)
    try {
      const result = await register({
        email,
        password,
        fullName,
        birthDate,
        roles: [role],
      })
      setToken(result.verificationToken)
    } catch (err) {
      setError(resolveErrorMessage(err, 'Ошибка регистрации'))
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="auth-shell">
      <div className="auth-card">
        <h1>Регистрация</h1>
        <p className="muted">Укажите данные и подтвердите email</p>
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
            Имя и фамилия
            <input value={fullName} onChange={(e) => setFullName(e.target.value)} required />
          </label>
          <label>
            Дата рождения
            <input value={birthDate} onChange={(e) => setBirthDate(e.target.value)} type="date" required />
          </label>
          <label>
            Роль
            <select value={role} onChange={(e) => setRole(e.target.value)}>
              <option value="BUYER">Покупатель</option>
              <option value="SELLER">Продавец</option>
            </select>
          </label>
          <button className="btn primary" type="submit" disabled={loading}>
            {loading ? 'Регистрация...' : 'Зарегистрироваться'}
          </button>
        </form>
        <div className="auth-links-row">
          <Link to="/login">Уже есть аккаунт</Link>
        </div>
      </div>
    </div>
  )
}
