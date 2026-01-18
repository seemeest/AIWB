import { useState } from 'react'
import { Link } from 'react-router-dom'
import { resetPassword } from '../api/auth'
import { resolveErrorMessage } from '../utils/errorMessages'

export function ResetConfirmPage() {
  const [token, setToken] = useState('')
  const [newPassword, setNewPassword] = useState('')
  const [message, setMessage] = useState('')
  const [error, setError] = useState('')

  const handleSubmit = async (event) => {
    event.preventDefault()
    setError('')
    try {
      await resetPassword(token, newPassword)
      setMessage('Пароль обновлен. Теперь можно войти.')
    } catch (err) {
      setError(resolveErrorMessage(err, 'Ошибка сброса'))
    }
  }

  return (
    <div className="auth-shell">
      <div className="auth-card">
        <h1>Подтвердить сброс</h1>
        <p className="muted">Введите токен и новый пароль</p>
        {error && <div className="alert">{error}</div>}
        {message && <div className="success">{message}</div>}
        <form onSubmit={handleSubmit} className="form">
          <label>
            Токен
            <input value={token} onChange={(e) => setToken(e.target.value)} required />
          </label>
          <label>
            Новый пароль
            <input value={newPassword} onChange={(e) => setNewPassword(e.target.value)} type="password" required />
          </label>
          <button className="btn primary" type="submit">
            Подтвердить
          </button>
        </form>
        <div className="auth-links-row">
          <Link to="/login">К входу</Link>
        </div>
      </div>
    </div>
  )
}
