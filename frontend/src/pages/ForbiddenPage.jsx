import { Link } from 'react-router-dom'

export function ForbiddenPage() {
  return (
    <div className="empty-state">
      <h1>Доступ запрещен</h1>
      <p className="muted">У вас нет прав для просмотра этой страницы.</p>
      <Link to="/catalog" className="btn primary">
        Вернуться
      </Link>
    </div>
  )
}
