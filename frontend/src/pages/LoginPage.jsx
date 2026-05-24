import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/authContext'
import { getErrorMessage } from '../services/apiService'

const LoginPage = () => {
  const navigate = useNavigate()
  const { login } = useAuth()
  const [form, setForm] = useState({ username: '', password: '' })
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)

  const submit = async (event) => {
    event.preventDefault()
    setError('')
    setLoading(true)
    try {
      await login(form)
      navigate('/dashboard')
    } catch (err) {
      setError(getErrorMessage(err) || 'Неверный логин или пароль')
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
              <h1 className="h3 text-center mb-4">Вход в систему</h1>
              {error && <div className="alert alert-danger py-2">{error}</div>}
              <form onSubmit={submit}>
                <div className="mb-3">
                  <label className="form-label">Логин</label>
                  <input className="form-control" value={form.username} onChange={(e) => setForm({ ...form, username: e.target.value })} required />
                </div>
                <div className="mb-3">
                  <label className="form-label">Пароль</label>
                  <input type="password" className="form-control" value={form.password} onChange={(e) => setForm({ ...form, password: e.target.value })} required />
                </div>
                <button className="btn btn-primary w-100" disabled={loading}>{loading ? 'Вход...' : 'Войти'}</button>
              </form>
              <div className="text-center mt-3">
                <span className="text-muted">Нет аккаунта? </span>
                <Link to="/register">Зарегистрироваться</Link>
              </div>
              <hr />
              <div className="small text-muted">
                <div><b>Админ:</b> admin / admin123</div>
                <div><b>Пользователь:</b> user / user123</div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}

export default LoginPage
