import { useState } from 'react'
import { createOrder } from '../api/orders'
import { useCart } from '../state/CartContext'
import { useAuth } from '../auth/AuthContext'

export function CartPage() {
  const cart = useCart()
  const auth = useAuth()
  const [result, setResult] = useState(null)
  const [error, setError] = useState('')

  const total = cart.items.reduce((sum, item) => sum + Number(item.price) * Number(item.quantity), 0)

  const handleCheckout = async () => {
    setError('')
    try {
      const buyerId = auth?.user?.userId
      if (!buyerId) {
        throw new Error('Нет пользователя')
      }
      const payload = {
        buyerId,
        items: cart.items.map((item) => ({
          productId: item.productId,
          sellerId: item.sellerId,
          quantity: Number(item.quantity || 1),
          price: Number(item.price),
        })),
      }
      const order = await createOrder(payload)
      setResult(order)
      cart.clear()
    } catch (err) {
      setError(err?.response?.data?.message || err.message || 'Ошибка оформления')
    }
  }

  return (
    <section className="page">
      <header className="page-header">
        <h1>Корзина</h1>
        <p className="muted">Готовы оформить заказ?</p>
      </header>
      {error && <div className="alert">{error}</div>}
      {cart.items.length === 0 && <div className="muted">Корзина пуста</div>}
      <div className="list">
        {cart.items.map((item, idx) => (
          <div key={`${item.productId}-${idx}`} className="list-row">
            <div>
              <strong>{item.title || item.productId}</strong>
              <div className="muted">Кол-во: {item.quantity}</div>
            </div>
            <div className="list-row-right">
              <span>{item.price} ₽</span>
              <button className="btn ghost" onClick={() => cart.remove(idx)}>
                Удалить
              </button>
            </div>
          </div>
        ))}
      </div>
      <div className="cart-footer">
        <div className="total">Итого: {total.toFixed(2)} ₽</div>
        <button className="btn primary" onClick={handleCheckout} disabled={!cart.items.length}>
          Оформить
        </button>
      </div>
      {result && (
        <div className="result-box">
          Заказ создан: <strong>{result.id}</strong>
        </div>
      )}
    </section>
  )
}
