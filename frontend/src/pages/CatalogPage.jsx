import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { searchProducts } from '../api/search'

export function CatalogPage() {
  const [query, setQuery] = useState('кроссовки')
  const [results, setResults] = useState([])
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')

  const runSearch = async () => {
    if (!query) return
    setLoading(true)
    setError('')
    try {
      const data = await searchProducts(query, 24)
      setResults(data.items || [])
    } catch (err) {
      setError(err?.response?.data?.message || 'Ошибка поиска')
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    runSearch()
  }, [])

  return (
    <section className="page">
      <header className="page-header">
        <div>
          <h1>Каталог</h1>
          <p className="muted">Поиск с опечатками и быстрые подсказки</p>
        </div>
        <div className="search-row">
          <input
            value={query}
            onChange={(e) => setQuery(e.target.value)}
            placeholder="Введите запрос"
          />
          <button className="btn primary" onClick={runSearch} disabled={loading}>
            {loading ? 'Ищем...' : 'Найти'}
          </button>
        </div>
      </header>
      {error && <div className="alert">{error}</div>}
      <div className="grid">
        {results.map((item) => (
          <Link key={item.id} to={`/products/${item.id}`} className="card">
            <div className="card-body">
              <div className="card-title">{item.title}</div>
              <div className="card-subtitle">{item.description || 'Описание отсутствует'}</div>
              <div className="card-meta">
                <span>{item.price} ₽</span>
                <span className="tag">{item.status}</span>
              </div>
            </div>
          </Link>
        ))}
      </div>
    </section>
  )
}
