import { useState } from 'react'
import { fetchSellerMetrics } from '../api/metrics'
import { useAuth } from '../auth/AuthContext'

export function MetricsPage() {
  const auth = useAuth()
  const [from, setFrom] = useState('')
  const [to, setTo] = useState('')
  const [metrics, setMetrics] = useState(null)
  const [error, setError] = useState('')

  const handleLoad = async () => {
    setError('')
    try {
      const sellerId = auth?.user?.userId
      const result = await fetchSellerMetrics(sellerId, from || null, to || null)
      setMetrics(result)
    } catch (err) {
      setError(err?.response?.data?.message || 'Ошибка загрузки')
    }
  }

  return (
    <section className="page">
      <header className="page-header">
        <h1>Метрики продавца</h1>
        <p className="muted">Просмотры, позиция в поиске, конверсия</p>
      </header>
      {error && <div className="alert">{error}</div>}
      <div className="form-grid">
        <label>
          С (ISO)
          <input value={from} onChange={(e) => setFrom(e.target.value)} placeholder="2026-01-01T00:00:00Z" />
        </label>
        <label>
          По (ISO)
          <input value={to} onChange={(e) => setTo(e.target.value)} placeholder="2026-01-31T00:00:00Z" />
        </label>
      </div>
      <button className="btn primary" onClick={handleLoad}>
        Загрузить
      </button>
      {metrics && (
        <div className="metrics-grid">
          <div className="metric-card">
            <div className="metric-label">Уникальные просмотры карточек</div>
            <div className="metric-value">{metrics.uniqueViews}</div>
          </div>
          <div className="metric-card">
            <div className="metric-label">Средняя позиция в поиске</div>
            <div className="metric-value">{Number(metrics.averageSearchPosition || 0).toFixed(2)}</div>
          </div>
          <div className="metric-card">
            <div className="metric-label">Заказы</div>
            <div className="metric-value">{metrics.orderCount}</div>
          </div>
          <div className="metric-card">
            <div className="metric-label">Конверсия</div>
            <div className="metric-value">{Number(metrics.conversion || 0).toFixed(3)}</div>
          </div>
        </div>
      )}
    </section>
  )
}
