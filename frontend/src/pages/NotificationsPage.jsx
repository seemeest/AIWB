import { useEffect, useState } from 'react'
import { fetchNotifications, markNotificationRead } from '../api/notifications'
import { useAuth } from '../auth/AuthContext'

export function NotificationsPage() {
  const auth = useAuth()
  const [items, setItems] = useState([])
  const [error, setError] = useState('')

  useEffect(() => {
    const load = async () => {
      if (!auth?.user?.userId) return
      try {
        const data = await fetchNotifications(auth.user.userId, 30)
        setItems(data || [])
      } catch (err) {
        setError(err?.response?.data?.message || 'Ошибка загрузки')
      }
    }
    load()
  }, [auth?.user?.userId])

  const handleRead = async (id) => {
    try {
      const updated = await markNotificationRead(id)
      setItems((prev) => prev.map((item) => (item.id === id ? updated : item)))
    } catch (err) {
      setError(err?.response?.data?.message || 'Ошибка обновления')
    }
  }

  return (
    <section className="page">
      <header className="page-header">
        <h1>Уведомления</h1>
        <p className="muted">Системные события и статусы</p>
      </header>
      {error && <div className="alert">{error}</div>}
      <div className="list">
        {items.map((item) => (
          <div key={item.id} className="list-row">
            <div>
              <strong>{item.title}</strong>
              <div className="muted">{item.message}</div>
            </div>
            <div className="list-row-right">
              <span className="tag">{item.status}</span>
              {item.status !== 'READ' && (
                <button className="btn ghost" onClick={() => handleRead(item.id)}>
                  Прочитать
                </button>
              )}
            </div>
          </div>
        ))}
      </div>
    </section>
  )
}
