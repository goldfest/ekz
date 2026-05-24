import { Link, NavLink, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/authContext'

const AppNavbar = () => {
  const navigate = useNavigate()
  const { isAuthenticated, username, role, isAdmin, logout } = useAuth()

  const handleLogout = () => {
    logout()
    navigate('/login')
  }

  return (
    <nav className="navbar navbar-expand-lg navbar-dark bg-primary shadow-sm">
      <div className="container">
        <Link className="navbar-brand fw-semibold" to="/dashboard">
          <i className="bi bi-tools me-2" />
          Service Center
        </Link>

        <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
          <span className="navbar-toggler-icon" />
        </button>

        <div className="collapse navbar-collapse" id="navbarNav">
          {isAuthenticated && (
            <ul className="navbar-nav me-auto mb-2 mb-lg-0">
              <li className="nav-item"><NavLink className="nav-link" to="/clients">Клиенты</NavLink></li>
              <li className="nav-item"><NavLink className="nav-link" to="/employees">Сотрудники</NavLink></li>
              <li className="nav-item"><NavLink className="nav-link" to="/services">Услуги</NavLink></li>
              <li className="nav-item"><NavLink className="nav-link" to="/orders">Заказы</NavLink></li>
              {isAdmin && <li className="nav-item"><NavLink className="nav-link" to="/users">Пользователи</NavLink></li>}
            </ul>
          )}

          <div className="d-flex align-items-center gap-2 text-white">
            {isAuthenticated ? (
              <>
                <span className="small d-none d-md-inline">{username} · {role}</span>
                <button className="btn btn-outline-light btn-sm" onClick={handleLogout}>Выйти</button>
              </>
            ) : (
              <>
                <Link className="btn btn-outline-light btn-sm" to="/login">Вход</Link>
                <Link className="btn btn-light btn-sm" to="/register">Регистрация</Link>
              </>
            )}
          </div>
        </div>
      </div>
    </nav>
  )
}

export default AppNavbar
