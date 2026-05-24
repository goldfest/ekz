import { useEffect, useMemo, useState } from 'react'
import { authApi } from '../services/apiService'
import { AuthContext } from './authContext'

export const AuthProvider = ({ children }) => {
  const [token, setToken] = useState(() => localStorage.getItem('token'))
  const [username, setUsername] = useState(() => localStorage.getItem('username'))
  const [role, setRole] = useState(() => localStorage.getItem('role'))

  useEffect(() => {
    if (token) localStorage.setItem('token', token)
    else localStorage.removeItem('token')
  }, [token])

  const saveAuth = (data) => {
    setToken(data.token)
    setUsername(data.username)
    setRole(data.role)
    localStorage.setItem('username', data.username)
    localStorage.setItem('role', data.role)
  }

  const login = async (credentials) => saveAuth(await authApi.login(credentials))
  const register = async (payload) => saveAuth(await authApi.register(payload))

  const logout = () => {
    setToken(null)
    setUsername(null)
    setRole(null)
    localStorage.removeItem('token')
    localStorage.removeItem('username')
    localStorage.removeItem('role')
  }

  const value = useMemo(() => ({
    token,
    username,
    role,
    isAuthenticated: Boolean(token),
    isAdmin: role === 'ADMIN',
    login,
    register,
    logout,
  }), [token, username, role])

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}
