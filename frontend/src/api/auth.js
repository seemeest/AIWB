import api from './client'

export async function register(payload) {
  const { data } = await api.post('/api/auth/register', payload)
  return data
}

export async function verifyEmail(token) {
  const { data } = await api.post('/api/auth/verify-email', { token })
  return data
}

export async function login(payload) {
  const { data } = await api.post('/api/auth/login', payload)
  return data
}

export async function refresh(refreshToken) {
  const { data } = await api.post('/api/auth/refresh', { refreshToken })
  return data
}

export async function requestPasswordReset(email) {
  const { data } = await api.post('/api/auth/password/reset-request', { email })
  return data
}

export async function resetPassword(token, newPassword) {
  const { data } = await api.post('/api/auth/password/reset', { token, newPassword })
  return data
}

export async function changePassword(userId, currentPassword, newPassword) {
  const { data } = await api.post('/api/auth/password/change', {
    userId,
    currentPassword,
    newPassword,
  })
  return data
}
