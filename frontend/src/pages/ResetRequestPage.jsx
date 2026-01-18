import { useState } from 'react'
import { Link } from 'react-router-dom'
import { requestPasswordReset } from '../api/auth'
import { resolveErrorMessage } from '../utils/errorMessages'

export function ResetRequestPage() {
  const [email, setEmail] = useState('')
  const [message, setMessage] = useState('')
  const [error, setError] = useState('')

  const handleSubmit = async (event) => {
    event.preventDefault()
    setError('')
    try {
      await requestPasswordReset(email)
      setMessage('Заявка отправлена. Проверьте email или спам.')
    } catch (err) {
      setError(resolveErrorMessage(err, 'Ошибка запроса'))
    }
  }

  return (
    <div className="auth-shell">
      <div className="auth-card">
        <h1>Восстановление пароля</h1>
        <p className="muted">Мы отправим токен сброса на email</p>
        {error && <div className="alert">{error}</div>}
        {message && <div className="success">{message}</div>}
        <form onSubmit={handleSubmit} className="form">
          <label>
            Email
            <input value={email} onChange={(e) => setEmail(e.target.value)} type="email" required />
          </label>
          <button className="btn primary" type="submit">
            Отправить
          </button>
        </form>
        <div className="auth-links-row">
          <Link to="/reset/confirm">Уже есть токен</Link>
        </div>
      </div>
    </div>
  )
}
