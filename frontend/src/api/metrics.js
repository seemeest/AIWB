import api from './client'

export async function fetchSellerMetrics(sellerId, from, to) {
  const params = {}
  if (from) params.from = from
  if (to) params.to = to
  const { data } = await api.get(`/api/metrics/sellers/${sellerId}`, { params })
  return data
}
