import { createContext, useContext, useEffect, useMemo, useState } from 'react'
import { login as apiLogin, refresh as apiRefresh } from '../api/auth'
import { setAccessToken } from '../api/client'

const AuthContext = createContext(null)

const STORAGE_KEY = 'aiwb_auth'

function parseJwt(token) {
  try {
    const payload = JSON.parse(atob(token.split('.')[1]))
    return {
      userId: payload.uid,
      roles: payload.roles || [],
      email: payload.sub || '',
    }
  } catch {
    return null
  }
}

export function AuthProvider({ children }) {
  const [auth, setAuth] = useState(() => {
    const stored = localStorage.getItem(STORAGE_KEY)
    return stored ? JSON.parse(stored) : null
  })

  useEffect(() => {
    if (auth?.accessToken) {
      setAccessToken(auth.accessToken)
      localStorage.setItem(STORAGE_KEY, JSON.stringify(auth))
    } else {
      setAccessToken(null)
      localStorage.removeItem(STORAGE_KEY)
    }
  }, [auth])

  const value = useMemo(() => {
    const user = auth?.accessToken ? parseJwt(auth.accessToken) : null
    return {
      accessToken: auth?.accessToken || null,
      refreshToken: auth?.refreshToken || null,
      user,
      async login(email, password) {
        const tokens = await apiLogin({ email, password })
        setAuth(tokens)
        return tokens
      },
      async refresh() {
        if (!auth?.refreshToken) {
          return null
        }
        const tokens = await apiRefresh(auth.refreshToken)
        setAuth(tokens)
        return tokens
      },
      logout() {
        setAuth(null)
      },
    }
  }, [auth])

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}

export function useAuth() {
  return useContext(AuthContext)
}
