import api from './client'

export async function createProduct(payload) {
  const { data } = await api.post('/api/products', payload)
  return data
}

export async function updateProduct(id, payload) {
  const { data } = await api.patch(`/api/products/${id}`, payload)
  return data
}

export async function uploadProductImage(id, sellerId, file) {
  const form = new FormData()
  form.append('sellerId', sellerId)
  form.append('file', file)
  const { data } = await api.post(`/api/products/${id}/images`, form, {
    headers: { 'Content-Type': 'multipart/form-data' },
  })
  return data
}

export async function getProduct(id, viewerId, sessionId) {
  const { data } = await api.get(`/api/products/${id}`, {
    params: { viewerId, sessionId },
  })
  return data
}

export async function getSimilarProducts(id, limit = 10) {
  const { data } = await api.get(`/api/products/${id}/similar`, { params: { limit } })
  return data
}
