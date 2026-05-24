import { Link } from 'react-router-dom'
import { useAuth } from '../context/authContext'

const DashboardPage = () => {
  const { username, role, isAdmin } = useAuth()

  return (
    <div className="container py-4">
      <section className="hero p-4 p-md-5 mb-4 shadow-sm">
        <div className="row align-items-center g-4">
          <div className="col-12 col-lg-8">
            <h1 className="display-6 fw-bold">Учет клиентов сервисного центра</h1>
            <p className="fs-5 mb-3">
              Минимальное экзаменационное SPA-приложение: CRUD, поиск, фильтрация, пагинация,
              таблицы, карточки, роли и связь many-to-many между заказами и услугами.
            </p>
            <div className="badge bg-light text-primary fs-6">Вы вошли как {username}, роль: {role}</div>
          </div>
          <div className="col-12 col-lg-4">
            <div className="bg-white text-dark rounded-4 p-3">
              <div className="fw-semibold mb-2">Права доступа</div>
              <div className="small text-muted">
                {isAdmin ? 'Администратор может создавать, редактировать и удалять записи.' : 'Пользователь может просматривать таблицы и карточки без изменения данных.'}
              </div>
            </div>
          </div>
        </div>
      </section>

      <div className="row g-3">
        {[
          ['Клиенты', 'Регистрация клиента, контакты, активность.', '/clients', 'bi-people'],
          ['Сотрудники', 'Назначение ответственных сотрудников.', '/employees', 'bi-person-badge'],
          ['Услуги', 'Каталог работ и стоимость услуг.', '/services', 'bi-card-checklist'],
          ['Заказы', 'Оформление заказа и выбор нескольких услуг.', '/orders', 'bi-clipboard-check'],
        ].map(([title, text, link, icon]) => (
          <div className="col-12 col-md-6 col-xl-3" key={title}>
            <div className="card entity-card h-100">
              <div className="card-body">
                <div className="fs-2 text-primary"><i className={`bi ${icon}`} /></div>
                <h2 className="h5 mt-2">{title}</h2>
                <p className="text-muted">{text}</p>
                <Link className="btn btn-outline-primary btn-sm" to={link}>Открыть</Link>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  )
}

export default DashboardPage
