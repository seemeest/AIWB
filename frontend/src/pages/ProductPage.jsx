import { useEffect, useState } from 'react'
import { Link, useParams } from 'react-router-dom'
import { getProduct, getSimilarProducts } from '../api/products'
import { useCart } from '../state/CartContext'
import { useAuth } from '../auth/AuthContext'

export function ProductPage() {
  const { id } = useParams()
  const cart = useCart()
  const auth = useAuth()
  const [product, setProduct] = useState(null)
  const [similar, setSimilar] = useState([])
  const [error, setError] = useState('')

  useEffect(() => {
    const load = async () => {
      try {
        const data = await getProduct(id, auth?.user?.userId, null)
        setProduct(data)
        const similarData = await getSimilarProducts(id, 8)
        setSimilar(similarData.items || [])
      } catch (err) {
        setError(err?.response?.data?.message || 'Ошибка загрузки товара')
      }
    }
    load()
  }, [id, auth?.user?.userId])

  if (error) {
    return <div className="alert">{error}</div>
  }
  if (!product) {
    return <div className="muted">Загрузка...</div>
  }

  return (
    <section className="page">
      <div className="product-hero">
        <div className="product-gallery">
          {product.images?.length ? (
            product.images.map((img, idx) => (
              <img key={img} src={img} alt={`product-${idx}`} />
            ))
          ) : (
            <div className="image-placeholder">Нет изображений</div>
          )}
        </div>
        <div className="product-info">
          <h1>{product.title}</h1>
          <p className="muted">{product.description || 'Описание отсутствует'}</p>
          <div className="price-row">
            <span className="price">{product.price} ₽</span>
            <span className="tag">{product.status}</span>
          </div>
          <div className="stock">Остаток: {product.quantity}</div>
          <button
            className="btn primary"
            onClick={() =>
              cart.add({
                productId: product.id,
                sellerId: product.sellerId,
                quantity: 1,
                price: product.price,
                title: product.title,
              })
            }
          >
            В корзину
          </button>
        </div>
      </div>

      <div className="section-divider">
        <h2>Похожие товары</h2>
      </div>
      <div className="grid">
        {similar.map((item) => (
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
