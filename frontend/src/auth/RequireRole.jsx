import { Navigate } from 'react-router-dom'
import { useAuth } from './AuthContext'

export function RequireRole({ roles, children }) {
  const auth = useAuth()
  if (!auth?.user) {
    return <Navigate to="/login" replace />
  }
  const hasRole = roles.some((role) => auth.user.roles.includes(role))
  if (!hasRole) {
    return <Navigate to="/forbidden" replace />
  }
  return children
}
