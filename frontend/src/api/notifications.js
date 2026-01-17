import api from './client'

export async function fetchNotifications(userId, limit = 20) {
  const { data } = await api.get('/api/notifications', { params: { userId, limit } })
  return data
}

export async function markNotificationRead(id) {
  const { data } = await api.patch(`/api/notifications/${id}/read`)
  return data
}
