import { Link } from 'react-router-dom'

export function NotFoundPage() {
  return (
    <div className="empty-state">
      <h1>Страница не найдена</h1>
      <p className="muted">Проверьте адрес или вернитесь в каталог.</p>
      <Link to="/catalog" className="btn primary">
        В каталог
      </Link>
    </div>
  )
}
