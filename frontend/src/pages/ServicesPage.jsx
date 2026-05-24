import { useEffect, useState } from 'react'
import AdminOnly from '../components/AdminOnly'
import Loading from '../components/Loading'
import Pagination from '../components/Pagination'
import { getErrorMessage, servicesApi } from '../services/apiService'

const emptyForm = { title: '', category: '', price: 0, active: true }

const ServicesPage = () => {
  const [items, setItems] = useState([])
  const [pageData, setPageData] = useState({ number: 0, totalPages: 0, totalElements: 0 })
  const [page, setPage] = useState(0)
  const [search, setSearch] = useState('')
  const [category, setCategory] = useState('')
  const [active, setActive] = useState('')
  const [form, setForm] = useState(emptyForm)
  const [editingId, setEditingId] = useState(null)
  const [showForm, setShowForm] = useState(false)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')

  const load = async () => {
    setLoading(true)
    setError('')
    try {
      const data = await servicesApi.list({ search, category, active, page, size: 6, sort: 'id,desc' })
      setItems(data.content || [])
      setPageData(data)
    } catch (err) { setError(getErrorMessage(err)) }
    finally { setLoading(false) }
  }

  useEffect(() => { load() }, [page, active, category])

  const resetForm = () => { setForm(emptyForm); setEditingId(null); setShowForm(false) }
  const submit = async (event) => {
    event.preventDefault()
    try { editingId ? await servicesApi.update(editingId, form) : await servicesApi.create(form); resetForm(); load() }
    catch (err) { setError(getErrorMessage(err)) }
  }
  const edit = (item) => { setForm({ title: item.title, category: item.category, price: item.price, active: item.active }); setEditingId(item.id); setShowForm(true) }
  const remove = async (id) => { if (!confirm('Удалить услугу?')) return; try { await servicesApi.remove(id); load() } catch (err) { setError(getErrorMessage(err)) } }
  const applySearch = (event) => { event.preventDefault(); page === 0 ? load() : setPage(0) }

  return (
    <div className="container py-4">
      <div className="d-flex justify-content-between align-items-center gap-2 mb-3 flex-wrap">
        <div><h1 className="h3 mb-1">Услуги</h1><p className="text-muted mb-0">Каталог услуг, который используется в many-to-many связи с заказами.</p></div>
        <AdminOnly><button className="btn btn-primary" onClick={() => { setShowForm(true); setEditingId(null); setForm(emptyForm) }}><i className="bi bi-plus-lg me-1" /> Добавить</button></AdminOnly>
      </div>

      <form className="card page-card mb-3" onSubmit={applySearch}>
        <div className="card-body row g-2 align-items-end">
          <div className="col-12 col-md-4"><label className="form-label">Поиск</label><input className="form-control" placeholder="Название или категория" value={search} onChange={(e) => setSearch(e.target.value)} /></div>
          <div className="col-12 col-md-3"><label className="form-label">Категория</label><input className="form-control" placeholder="Например: Ремонт" value={category} onChange={(e) => { setCategory(e.target.value); setPage(0) }} /></div>
          <div className="col-12 col-md-3"><label className="form-label">Активность</label><select className="form-select" value={active} onChange={(e) => { setActive(e.target.value); setPage(0) }}><option value="">Все</option><option value="true">Активные</option><option value="false">Неактивные</option></select></div>
          <div className="col-12 col-md-2"><button className="btn btn-outline-primary w-100">Найти</button></div>
        </div>
      </form>

      {showForm && <AdminOnly><form className="form-section p-3 mb-3" onSubmit={submit}><h2 className="h5">{editingId ? 'Редактирование услуги' : 'Новая услуга'}</h2><div className="row g-2"><div className="col-12 col-md-5"><input className="form-control" placeholder="Название" value={form.title} onChange={(e) => setForm({ ...form, title: e.target.value })} required /></div><div className="col-12 col-md-4"><input className="form-control" placeholder="Категория" value={form.category} onChange={(e) => setForm({ ...form, category: e.target.value })} required /></div><div className="col-12 col-md-3"><input className="form-control" type="number" step="0.01" min="0" placeholder="Цена" value={form.price} onChange={(e) => setForm({ ...form, price: Number(e.target.value) })} required /></div><div className="col-12"><div className="form-check"><input className="form-check-input" type="checkbox" checked={form.active} onChange={(e) => setForm({ ...form, active: e.target.checked })} id="serviceActive" /><label className="form-check-label" htmlFor="serviceActive">Активна</label></div></div><div className="col-12 d-flex gap-2"><button className="btn btn-success">Сохранить</button><button type="button" className="btn btn-outline-secondary" onClick={resetForm}>Отмена</button></div></div></form></AdminOnly>}

      {error && <div className="alert alert-danger">{error}</div>}
      {loading ? <Loading /> : <><div className="card page-card desktop-table"><div className="table-responsive"><table className="table table-hover mb-0"><thead><tr><th>ID</th><th>Название</th><th>Категория</th><th>Цена</th><th>Статус</th><th /></tr></thead><tbody>{items.map((item) => <tr key={item.id}><td>{item.id}</td><td>{item.title}</td><td>{item.category}</td><td>{Number(item.price).toLocaleString('ru-RU')} ₽</td><td>{item.active ? <span className="badge text-bg-success">Активна</span> : <span className="badge text-bg-secondary">Неактивна</span>}</td><td className="text-end"><AdminOnly><button className="btn btn-sm btn-outline-primary me-1" onClick={() => edit(item)}>Изм.</button><button className="btn btn-sm btn-outline-danger" onClick={() => remove(item.id)}>Удал.</button></AdminOnly></td></tr>)}</tbody></table></div></div><div className="mobile-cards row g-3">{items.map((item) => <div className="col-12" key={item.id}><div className="card entity-card"><div className="card-body"><div className="d-flex justify-content-between"><h2 className="h5">{item.title}</h2><span className="badge badge-soft">#{item.id}</span></div><p className="mb-1"><i className="bi bi-tag me-2" />{item.category}</p><p className="fw-semibold mb-2">{Number(item.price).toLocaleString('ru-RU')} ₽</p><span className={`badge ${item.active ? 'text-bg-success' : 'text-bg-secondary'}`}>{item.active ? 'Активна' : 'Неактивна'}</span><AdminOnly><div className="mt-3 d-flex gap-2"><button className="btn btn-sm btn-outline-primary" onClick={() => edit(item)}>Изменить</button><button className="btn btn-sm btn-outline-danger" onClick={() => remove(item.id)}>Удалить</button></div></AdminOnly></div></div></div>)}</div><Pagination page={pageData.number || 0} totalPages={pageData.totalPages || 0} totalElements={pageData.totalElements || 0} onPageChange={setPage} /></>}
    </div>
  )
}

export default ServicesPage
