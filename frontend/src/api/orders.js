import api from './client'

export async function createOrder(payload) {
  const { data } = await api.post('/api/orders', payload)
  return data
}

export async function payOrder(orderId) {
  const { data } = await api.patch(`/api/orders/${orderId}/payment`)
  return data
}

export async function updateDeliveryStatus(orderId, status) {
  const { data } = await api.patch(`/api/orders/${orderId}/delivery-status`, { status })
  return data
}
