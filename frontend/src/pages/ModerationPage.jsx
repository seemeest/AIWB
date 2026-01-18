import { useState } from 'react'
import { createAppeal, createComplaint, createDecision, decideAppeal } from '../api/moderation'

export function ModerationPage() {
  const [message, setMessage] = useState('')
  const [error, setError] = useState('')
  const [complaint, setComplaint] = useState({ authorId: '', productId: '', reason: '' })
  const [decision, setDecision] = useState({
    moderatorId: '',
    targetType: 'PRODUCT',
    targetId: '',
    action: '',
    reason: '',
    blockedUntil: '',
  })
  const [appeal, setAppeal] = useState({ blockId: '', authorId: '', reason: '' })
  const [appealDecision, setAppealDecision] = useState({ id: '', approved: false })

  const submit = async (fn, payload) => {
    setError('')
    setMessage('')
    try {
      const result = await fn(payload)
      setMessage(`Успешно: ${JSON.stringify(result)}`)
    } catch (err) {
      setError(err?.response?.data?.message || 'Ошибка запроса')
    }
  }

  return (
    <section className="page">
      <header className="page-header">
        <h1>Модерация</h1>
        <p className="muted">Жалобы, блокировки, апелляции</p>
      </header>
      {error && <div className="alert">{error}</div>}
      {message && <div className="success">{message}</div>}
      <div className="split">
        <div className="panel">
          <h2>Жалоба</h2>
          <label>
            Автор ID
            <input value={complaint.authorId} onChange={(e) => setComplaint({ ...complaint, authorId: e.target.value })} />
          </label>
          <label>
            Товар ID
            <input value={complaint.productId} onChange={(e) => setComplaint({ ...complaint, productId: e.target.value })} />
          </label>
          <label>
            Причина
            <textarea value={complaint.reason} onChange={(e) => setComplaint({ ...complaint, reason: e.target.value })} />
          </label>
          <button className="btn primary" onClick={() => submit(createComplaint, complaint)}>
            Отправить
          </button>
        </div>
        <div className="panel">
          <h2>Решение</h2>
          <label>
            Модератор ID
            <input value={decision.moderatorId} onChange={(e) => setDecision({ ...decision, moderatorId: e.target.value })} />
          </label>
          <label>
            Тип цели
            <select value={decision.targetType} onChange={(e) => setDecision({ ...decision, targetType: e.target.value })}>
              <option value="PRODUCT">PRODUCT</option>
              <option value="SELLER">SELLER</option>
              <option value="BUYER">BUYER</option>
            </select>
          </label>
          <label>
            Цель ID
            <input value={decision.targetId} onChange={(e) => setDecision({ ...decision, targetId: e.target.value })} />
          </label>
          <label>
            Действие
            <input value={decision.action} onChange={(e) => setDecision({ ...decision, action: e.target.value })} />
          </label>
          <label>
            Причина
            <textarea value={decision.reason} onChange={(e) => setDecision({ ...decision, reason: e.target.value })} />
          </label>
          <label>
            Блокировка до (ISO)
            <input value={decision.blockedUntil} onChange={(e) => setDecision({ ...decision, blockedUntil: e.target.value })} />
          </label>
          <button className="btn ghost" onClick={() => submit(createDecision, decision)}>
            Применить
          </button>
        </div>
      </div>
      <div className="split">
        <div className="panel">
          <h2>Апелляция</h2>
          <label>
            Блокировка ID
            <input value={appeal.blockId} onChange={(e) => setAppeal({ ...appeal, blockId: e.target.value })} />
          </label>
          <label>
            Автор ID
            <input value={appeal.authorId} onChange={(e) => setAppeal({ ...appeal, authorId: e.target.value })} />
          </label>
          <label>
            Причина
            <textarea value={appeal.reason} onChange={(e) => setAppeal({ ...appeal, reason: e.target.value })} />
          </label>
          <button className="btn primary" onClick={() => submit(createAppeal, appeal)}>
            Отправить
          </button>
        </div>
        <div className="panel">
          <h2>Решение по апелляции</h2>
          <label>
            Апелляция ID
            <input value={appealDecision.id} onChange={(e) => setAppealDecision({ ...appealDecision, id: e.target.value })} />
          </label>
          <label>
            Одобрено
            <select value={String(appealDecision.approved)} onChange={(e) => setAppealDecision({ ...appealDecision, approved: e.target.value === 'true' })}>
              <option value="true">true</option>
              <option value="false">false</option>
            </select>
          </label>
          <button className="btn ghost" onClick={() => submit((payload) => decideAppeal(payload.id, payload.approved), appealDecision)}>
            Применить
          </button>
        </div>
      </div>
    </section>
  )
}
