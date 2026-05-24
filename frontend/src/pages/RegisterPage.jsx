import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/authContext'
import { getErrorMessage } from '../services/apiService'

const RegisterPage = () => {
  const navigate = useNavigate()
  const { register } = useAuth()
  const [form, setForm] = useState({ username: '', password: '' })
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)

  const submit = async (event) => {
    event.preventDefault()
    setError('')
    setLoading(true)
    try {
      await register(form)
      navigate('/dashboard')
    } catch (err) {
      setError(getErrorMessage(err))
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="container py-5">
      <div className="row justify-content-center">
        <div className="col-12 col-md-7 col-lg-5">
          <div className="card page-card">
            <div className="card-body p-4 p-md-5">
              <h1 className="h3 text-center mb-4">Регистрация</h1>
              {error && <div className="alert alert-danger py-2">{error}</div>}
              <form onSubmit={submit}>
                <div className="mb-3">
                  <label className="form-label">Логин</label>
                  <input className="form-control" value={form.username} onChange={(e) => setForm({ ...form, username: e.target.value })} minLength={3} required />
                </div>
                <div className="mb-3">
                  <label className="form-label">Пароль</label>
                  <input type="password" className="form-control" value={form.password} onChange={(e) => setForm({ ...form, password: e.target.value })} minLength={6} required />
                </div>
                <button className="btn btn-success w-100" disabled={loading}>{loading ? 'Регистрация...' : 'Зарегистрироваться'}</button>
              </form>
              <div className="text-center mt-3">
                <span className="text-muted">Уже есть аккаунт? </span>
                <Link to="/login">Войти</Link>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}

export default RegisterPage
