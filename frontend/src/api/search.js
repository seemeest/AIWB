import api from './client'

export async function searchProducts(query, limit = 20) {
  const { data } = await api.get('/api/search', { params: { query, limit } })
  return data
}

export async function suggestProducts(prefix, limit = 10) {
  const { data } = await api.get('/api/search/suggest', { params: { prefix, limit } })
  return data
}
