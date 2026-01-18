import { useState } from 'react'
import { createProduct, updateProduct, uploadProductImage } from '../api/products'
import { useAuth } from '../auth/AuthContext'

export function SellerPage() {
  const auth = useAuth()
  const [created, setCreated] = useState(null)
  const [error, setError] = useState('')
  const [imageFile, setImageFile] = useState(null)
  const [updateId, setUpdateId] = useState('')
  const [updateFields, setUpdateFields] = useState({
    title: '',
    description: '',
    price: '',
    quantity: '',
  })
  const [newProduct, setNewProduct] = useState({
    title: '',
    description: '',
    price: '',
    quantity: '',
  })

  const sellerId = auth?.user?.userId

  const handleCreate = async () => {
    setError('')
    try {
      const payload = {
        sellerId,
        title: newProduct.title,
        description: newProduct.description,
        price: Number(newProduct.price),
        quantity: Number(newProduct.quantity),
      }
      const product = await createProduct(payload)
      setCreated(product)
    } catch (err) {
      setError(err?.response?.data?.message || 'Ошибка создания товара')
    }
  }

  const handleUpdate = async () => {
    setError('')
    try {
      const payload = {
        sellerId,
        title: updateFields.title || null,
        description: updateFields.description || null,
        price: updateFields.price ? Number(updateFields.price) : null,
        quantity: updateFields.quantity ? Number(updateFields.quantity) : null,
      }
      const product = await updateProduct(updateId, payload)
      setCreated(product)
    } catch (err) {
      setError(err?.response?.data?.message || 'Ошибка обновления товара')
    }
  }

  const handleUpload = async () => {
    setError('')
    try {
      if (!created?.id || !imageFile) {
        setError('Выберите товар и файл')
        return
      }
      const product = await uploadProductImage(created.id, sellerId, imageFile)
      setCreated(product)
    } catch (err) {
      setError(err?.response?.data?.message || 'Ошибка загрузки изображения')
    }
  }

  return (
    <section className="page">
      <header className="page-header">
        <h1>Кабинет продавца</h1>
        <p className="muted">Создавайте и редактируйте товары</p>
      </header>
      {error && <div className="alert">{error}</div>}
      <div className="split">
        <div className="panel">
          <h2>Новый товар</h2>
          <div className="form-grid">
            <label>
              Название
              <input value={newProduct.title} onChange={(e) => setNewProduct({ ...newProduct, title: e.target.value })} />
            </label>
            <label>
              Описание
              <textarea value={newProduct.description} onChange={(e) => setNewProduct({ ...newProduct, description: e.target.value })} />
            </label>
            <label>
              Цена
              <input value={newProduct.price} onChange={(e) => setNewProduct({ ...newProduct, price: e.target.value })} />
            </label>
            <label>
              Количество
              <input value={newProduct.quantity} onChange={(e) => setNewProduct({ ...newProduct, quantity: e.target.value })} />
            </label>
          </div>
          <button className="btn primary" onClick={handleCreate} disabled={!sellerId}>
            Создать
          </button>
        </div>
        <div className="panel">
          <h2>Редактирование</h2>
          <label>
            ID товара
            <input value={updateId} onChange={(e) => setUpdateId(e.target.value)} />
          </label>
          <div className="form-grid">
            <label>
              Название
              <input value={updateFields.title} onChange={(e) => setUpdateFields({ ...updateFields, title: e.target.value })} />
            </label>
            <label>
              Описание
              <textarea value={updateFields.description} onChange={(e) => setUpdateFields({ ...updateFields, description: e.target.value })} />
            </label>
            <label>
              Цена
              <input value={updateFields.price} onChange={(e) => setUpdateFields({ ...updateFields, price: e.target.value })} />
            </label>
            <label>
              Количество
              <input value={updateFields.quantity} onChange={(e) => setUpdateFields({ ...updateFields, quantity: e.target.value })} />
            </label>
          </div>
          <button className="btn ghost" onClick={handleUpdate} disabled={!updateId || !sellerId}>
            Обновить
          </button>
        </div>
      </div>
      <div className="panel">
        <h2>Фотографии</h2>
        <div className="row-actions">
          <input type="file" onChange={(e) => setImageFile(e.target.files[0])} />
          <button className="btn primary" onClick={handleUpload} disabled={!imageFile}>
            Загрузить изображение
          </button>
        </div>
      </div>
      {created && (
        <div className="result-box">
          Товар сохранен: <strong>{created.id}</strong>
        </div>
      )}
    </section>
  )
}
