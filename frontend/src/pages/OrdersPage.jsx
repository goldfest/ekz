import { useEffect, useState } from 'react'
import AdminOnly from '../components/AdminOnly'
import Loading from '../components/Loading'
import Pagination from '../components/Pagination'
import { clientsApi, employeesApi, getErrorMessage, ordersApi, servicesApi } from '../services/apiService'

const emptyForm = {
  title: '',
  description: '',
  status: 'NEW',
  clientId: '',
  employeeId: '',
  dueDate: '',
  serviceIds: [],
}

const statuses = [
  ['NEW', 'Новый'],
  ['IN_PROGRESS', 'В работе'],
  ['COMPLETED', 'Завершен'],
  ['CANCELLED', 'Отменен'],
]

const statusText = Object.fromEntries(statuses)

const OrdersPage = () => {
  const [items, setItems] = useState([])
  const [pageData, setPageData] = useState({ number: 0, totalPages: 0, totalElements: 0 })
  const [page, setPage] = useState(0)
  const [search, setSearch] = useState('')
  const [status, setStatus] = useState('')
  const [clientId, setClientId] = useState('')
  const [serviceId, setServiceId] = useState('')
  const [form, setForm] = useState(emptyForm)
  const [editingId, setEditingId] = useState(null)
  const [showForm, setShowForm] = useState(false)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')
  const [clients, setClients] = useState([])
  const [employees, setEmployees] = useState([])
  const [services, setServices] = useState([])

  const loadLookups = async () => {
    const [clientsPage, employeesPage, servicesPage] = await Promise.all([
      clientsApi.list({ size: 200, sort: 'fullName,asc' }),
      employeesApi.list({ size: 200, sort: 'fullName,asc' }),
      servicesApi.list({ size: 200, sort: 'title,asc' }),
    ])
    setClients(clientsPage.content || [])
    setEmployees(employeesPage.content || [])
    setServices(servicesPage.content || [])
  }

  const load = async () => {
    setLoading(true)
    setError('')
    try {
      const data = await ordersApi.list({ search, status, clientId, serviceId, page, size: 6, sort: 'id,desc' })
      setItems(data.content || [])
      setPageData(data)
    } catch (err) { setError(getErrorMessage(err)) }
    finally { setLoading(false) }
  }

  useEffect(() => { loadLookups().catch((err) => setError(getErrorMessage(err))) }, [])
  useEffect(() => { load() }, [page, status, clientId, serviceId])

  const resetForm = () => { setForm(emptyForm); setEditingId(null); setShowForm(false) }
  const submit = async (event) => {
    event.preventDefault()
    const payload = {
      ...form,
      clientId: Number(form.clientId),
      employeeId: form.employeeId ? Number(form.employeeId) : null,
      dueDate: form.dueDate || null,
      serviceIds: form.serviceIds.map(Number),
    }
    try { editingId ? await ordersApi.update(editingId, payload) : await ordersApi.create(payload); resetForm(); load() }
    catch (err) { setError(getErrorMessage(err)) }
  }
  const edit = (item) => { setForm({ title: item.title, description: item.description || '', status: item.status, clientId: item.clientId || '', employeeId: item.employeeId || '', dueDate: item.dueDate || '', serviceIds: item.serviceIds || [] }); setEditingId(item.id); setShowForm(true) }
  const remove = async (id) => { if (!confirm('Удалить заказ?')) return; try { await ordersApi.remove(id); load() } catch (err) { setError(getErrorMessage(err)) } }
  const applySearch = (event) => { event.preventDefault(); page === 0 ? load() : setPage(0) }
  const handleServices = (event) => {
    const selected = Array.from(event.target.selectedOptions).map((option) => option.value)
    setForm({ ...form, serviceIds: selected })
  }

  return (
    <div className="container py-4">
      <div className="d-flex justify-content-between align-items-center gap-2 mb-3 flex-wrap">
        <div><h1 className="h3 mb-1">Заказы</h1><p className="text-muted mb-0">Заказ связан с клиентом, сотрудником и несколькими услугами.</p></div>
        <AdminOnly><button className="btn btn-primary" onClick={() => { setShowForm(true); setEditingId(null); setForm(emptyForm) }}><i className="bi bi-plus-lg me-1" /> Добавить</button></AdminOnly>
      </div>

      <form className="card page-card mb-3" onSubmit={applySearch}>
        <div className="card-body row g-2 align-items-end">
          <div className="col-12 col-lg-3"><label className="form-label">Поиск</label><input className="form-control" placeholder="Название, описание, клиент" value={search} onChange={(e) => setSearch(e.target.value)} /></div>
          <div className="col-12 col-md-4 col-lg-2"><label className="form-label">Статус</label><select className="form-select" value={status} onChange={(e) => { setStatus(e.target.value); setPage(0) }}><option value="">Все</option>{statuses.map(([value, label]) => <option value={value} key={value}>{label}</option>)}</select></div>
          <div className="col-12 col-md-4 col-lg-3"><label className="form-label">Клиент</label><select className="form-select" value={clientId} onChange={(e) => { setClientId(e.target.value); setPage(0) }}><option value="">Все</option>{clients.map((c) => <option value={c.id} key={c.id}>{c.fullName}</option>)}</select></div>
          <div className="col-12 col-md-4 col-lg-2"><label className="form-label">Услуга</label><select className="form-select" value={serviceId} onChange={(e) => { setServiceId(e.target.value); setPage(0) }}><option value="">Все</option>{services.map((s) => <option value={s.id} key={s.id}>{s.title}</option>)}</select></div>
          <div className="col-12 col-lg-2"><button className="btn btn-outline-primary w-100">Найти</button></div>
        </div>
      </form>

      {showForm && <AdminOnly><form className="form-section p-3 mb-3" onSubmit={submit}><h2 className="h5">{editingId ? 'Редактирование заказа' : 'Новый заказ'}</h2><div className="row g-2"><div className="col-12 col-md-6"><input className="form-control" placeholder="Название заказа" value={form.title} onChange={(e) => setForm({ ...form, title: e.target.value })} required /></div><div className="col-12 col-md-3"><select className="form-select" value={form.status} onChange={(e) => setForm({ ...form, status: e.target.value })}>{statuses.map(([value, label]) => <option value={value} key={value}>{label}</option>)}</select></div><div className="col-12 col-md-3"><input className="form-control" type="date" value={form.dueDate} onChange={(e) => setForm({ ...form, dueDate: e.target.value })} /></div><div className="col-12 col-md-6"><select className="form-select" value={form.clientId} onChange={(e) => setForm({ ...form, clientId: e.target.value })} required><option value="">Выберите клиента</option>{clients.map((c) => <option value={c.id} key={c.id}>{c.fullName}</option>)}</select></div><div className="col-12 col-md-6"><select className="form-select" value={form.employeeId} onChange={(e) => setForm({ ...form, employeeId: e.target.value })}><option value="">Без сотрудника</option>{employees.map((emp) => <option value={emp.id} key={emp.id}>{emp.fullName} — {emp.position}</option>)}</select></div><div className="col-12"><label className="form-label">Услуги, связь многие-ко-многим</label><select className="form-select" multiple size="4" value={form.serviceIds.map(String)} onChange={handleServices}>{services.map((service) => <option value={service.id} key={service.id}>{service.title} · {Number(service.price).toLocaleString('ru-RU')} ₽</option>)}</select><div className="form-text">Можно выбрать несколько услуг через Ctrl/Command.</div></div><div className="col-12"><textarea className="form-control" placeholder="Описание" value={form.description} onChange={(e) => setForm({ ...form, description: e.target.value })} rows="3" /></div><div className="col-12 d-flex gap-2"><button className="btn btn-success">Сохранить</button><button type="button" className="btn btn-outline-secondary" onClick={resetForm}>Отмена</button></div></div></form></AdminOnly>}

      {error && <div className="alert alert-danger">{error}</div>}
      {loading ? <Loading /> : <><div className="card page-card desktop-table"><div className="table-responsive"><table className="table table-hover mb-0"><thead><tr><th>ID</th><th>Заказ</th><th>Клиент</th><th>Сотрудник</th><th>Услуги</th><th>Статус</th><th /></tr></thead><tbody>{items.map((item) => <tr key={item.id}><td>{item.id}</td><td><div className="fw-semibold">{item.title}</div><div className="small text-muted">до {item.dueDate || 'не указано'}</div></td><td>{item.clientName}</td><td>{item.employeeName || '—'}</td><td>{item.serviceTitles?.join(', ') || '—'}</td><td><span className="badge text-bg-primary">{statusText[item.status] || item.status}</span></td><td className="text-end"><AdminOnly><button className="btn btn-sm btn-outline-primary me-1" onClick={() => edit(item)}>Изм.</button><button className="btn btn-sm btn-outline-danger" onClick={() => remove(item.id)}>Удал.</button></AdminOnly></td></tr>)}</tbody></table></div></div><div className="mobile-cards row g-3">{items.map((item) => <div className="col-12" key={item.id}><div className="card entity-card"><div className="card-body"><div className="d-flex justify-content-between"><h2 className="h5">{item.title}</h2><span className="badge badge-soft">#{item.id}</span></div><p className="mb-1"><i className="bi bi-person me-2" />{item.clientName}</p><p className="mb-1"><i className="bi bi-person-badge me-2" />{item.employeeName || 'Без сотрудника'}</p><p className="small text-muted mb-2">Услуги: {item.serviceTitles?.join(', ') || '—'}</p><span className="badge text-bg-primary">{statusText[item.status] || item.status}</span><AdminOnly><div className="mt-3 d-flex gap-2"><button className="btn btn-sm btn-outline-primary" onClick={() => edit(item)}>Изменить</button><button className="btn btn-sm btn-outline-danger" onClick={() => remove(item.id)}>Удалить</button></div></AdminOnly></div></div></div>)}</div><Pagination page={pageData.number || 0} totalPages={pageData.totalPages || 0} totalElements={pageData.totalElements || 0} onPageChange={setPage} /></>}
    </div>
  )
}

export default OrdersPage
