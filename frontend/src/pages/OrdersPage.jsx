import { useState } from 'react'
import { payOrder, updateDeliveryStatus } from '../api/orders'

const deliveryStatuses = [
  'CREATED',
  'PAID',
  'ACCEPTED',
  'ASSEMBLED',
  'SHIPPED',
  'IN_TRANSIT',
  'ARRIVED_AT_PVZ',
  'READY_FOR_PICKUP',
  'PICKED_UP',
  'COMPLETED',
  'CANCELLED',
  'RETURNED',
  'LOST',
  'EXPIRED',
]

export function OrdersPage() {
  const [orderId, setOrderId] = useState('')
  const [status, setStatus] = useState('PAID')
  const [message, setMessage] = useState('')
  const [error, setError] = useState('')

  const handlePay = async () => {
    setError('')
    try {
      const result = await payOrder(orderId)
      setMessage(`Заказ оплачен: ${result.status}`)
    } catch (err) {
      setError(err?.response?.data?.message || 'Ошибка оплаты')
    }
  }

  const handleStatus = async () => {
    setError('')
    try {
      const result = await updateDeliveryStatus(orderId, status)
      setMessage(`Статус доставки обновлен: ${result.status}`)
    } catch (err) {
      setError(err?.response?.data?.message || 'Ошибка обновления статуса')
    }
  }

  return (
    <section className="page">
      <header className="page-header">
        <h1>Заказы</h1>
        <p className="muted">Управляйте оплатой и статусом доставки</p>
      </header>
      {error && <div className="alert">{error}</div>}
      {message && <div className="success">{message}</div>}
      <div className="form-grid">
        <label>
          ID заказа
          <input value={orderId} onChange={(e) => setOrderId(e.target.value)} placeholder="UUID" />
        </label>
        <label>
          Статус доставки
          <select value={status} onChange={(e) => setStatus(e.target.value)}>
            {deliveryStatuses.map((item) => (
              <option key={item} value={item}>
                {item}
              </option>
            ))}
          </select>
        </label>
      </div>
      <div className="row-actions">
        <button className="btn primary" onClick={handlePay} disabled={!orderId}>
          Оплатить
        </button>
        <button className="btn ghost" onClick={handleStatus} disabled={!orderId}>
          Обновить статус
        </button>
      </div>
    </section>
  )
}
