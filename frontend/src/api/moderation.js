import api from './client'

export async function createComplaint(payload) {
  const { data } = await api.post('/api/moderation/complaints', payload)
  return data
}

export async function createDecision(payload) {
  const { data } = await api.post('/api/moderation/decisions', payload)
  return data
}

export async function createAppeal(payload) {
  const { data } = await api.post('/api/moderation/appeals', payload)
  return data
}

export async function decideAppeal(id, approved) {
  const { data } = await api.patch(`/api/moderation/appeals/${id}`, null, {
    params: { approved },
  })
  return data
}
