import { useEffect, useState } from 'react'
import { Navigate } from 'react-router-dom'
import Loading from '../components/Loading'
import Pagination from '../components/Pagination'
import { useAuth } from '../context/authContext'
import { getErrorMessage, usersApi } from '../services/apiService'

const UsersPage = () => {
  const { isAdmin, username } = useAuth()
  const [items, setItems] = useState([])
  const [pageData, setPageData] = useState({ number: 0, totalPages: 0, totalElements: 0 })
  const [page, setPage] = useState(0)
  const [search, setSearch] = useState('')
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')


  const load = async () => {
    setLoading(true)
    setError('')
    try {
      const data = await usersApi.list({ search, page, size: 8, sort: 'id,asc' })
      setItems(data.content || [])
      setPageData(data)
    } catch (err) { setError(getErrorMessage(err)) }
    finally { setLoading(false) }
  }

  useEffect(() => { if (isAdmin) load() }, [page, isAdmin])

  if (!isAdmin) return <Navigate to="/dashboard" replace />

  const applySearch = (event) => { event.preventDefault(); page === 0 ? load() : setPage(0) }
  const changeRole = async (id, role) => { try { await usersApi.updateRole(id, role); load() } catch (err) { setError(getErrorMessage(err)) } }
  const setEnabled = async (id, enabled) => { try { await usersApi.setEnabled(id, enabled); load() } catch (err) { setError(getErrorMessage(err)) } }
  const remove = async (id) => { if (!confirm('Удалить пользователя?')) return; try { await usersApi.remove(id); load() } catch (err) { setError(getErrorMessage(err)) } }

  return (
    <div className="container py-4">
      <div className="mb-3">
        <h1 className="h3 mb-1">Пользователи</h1>
        <p className="text-muted mb-0">Админская часть: роли и блокировка пользователей.</p>
      </div>

      <form className="card page-card mb-3" onSubmit={applySearch}>
        <div className="card-body row g-2 align-items-end">
          <div className="col-12 col-md-10"><label className="form-label">Поиск</label><input className="form-control" placeholder="Логин" value={search} onChange={(e) => setSearch(e.target.value)} /></div>
          <div className="col-12 col-md-2"><button className="btn btn-outline-primary w-100">Найти</button></div>
        </div>
      </form>

      {error && <div className="alert alert-danger">{error}</div>}
      {loading ? <Loading /> : <>
        <div className="card page-card desktop-table"><div className="table-responsive"><table className="table table-hover mb-0"><thead><tr><th>ID</th><th>Логин</th><th>Роль</th><th>Статус</th><th /></tr></thead><tbody>{items.map((item) => <tr key={item.id}><td>{item.id}</td><td>{item.username}</td><td><select className="form-select form-select-sm" value={item.role} onChange={(e) => changeRole(item.id, e.target.value)} disabled={item.username === username}><option value="ADMIN">ADMIN</option><option value="USER">USER</option></select></td><td>{item.enabled ? <span className="badge text-bg-success">Активен</span> : <span className="badge text-bg-secondary">Заблокирован</span>}</td><td className="text-end"><button className="btn btn-sm btn-outline-warning me-1" disabled={item.username === username} onClick={() => setEnabled(item.id, !item.enabled)}>{item.enabled ? 'Блок.' : 'Разбл.'}</button><button className="btn btn-sm btn-outline-danger" disabled={item.username === username} onClick={() => remove(item.id)}>Удал.</button></td></tr>)}</tbody></table></div></div>
        <div className="mobile-cards row g-3">{items.map((item) => <div className="col-12" key={item.id}><div className="card entity-card"><div className="card-body"><div className="d-flex justify-content-between"><h2 className="h5">{item.username}</h2><span className="badge badge-soft">#{item.id}</span></div><p className="mb-2">Роль: <b>{item.role}</b></p><span className={`badge ${item.enabled ? 'text-bg-success' : 'text-bg-secondary'}`}>{item.enabled ? 'Активен' : 'Заблокирован'}</span><div className="mt-3 d-flex gap-2"><button className="btn btn-sm btn-outline-primary" disabled={item.username === username} onClick={() => changeRole(item.id, item.role === 'ADMIN' ? 'USER' : 'ADMIN')}>Сменить роль</button><button className="btn btn-sm btn-outline-danger" disabled={item.username === username} onClick={() => remove(item.id)}>Удалить</button></div></div></div></div>)}</div>
        <Pagination page={pageData.number || 0} totalPages={pageData.totalPages || 0} totalElements={pageData.totalElements || 0} onPageChange={setPage} />
      </>}
    </div>
  )
}

export default UsersPage
