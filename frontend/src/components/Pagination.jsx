const Pagination = ({ page, totalPages, totalElements, onPageChange }) => {
  if (!totalPages || totalPages <= 1) {
    return <div className="text-muted small">Найдено записей: {totalElements || 0}</div>
  }

  return (
    <div className="d-flex justify-content-between align-items-center flex-wrap gap-2 mt-3">
      <div className="text-muted small">Найдено записей: {totalElements}</div>
      <div className="btn-group">
        <button className="btn btn-outline-primary btn-sm" disabled={page <= 0} onClick={() => onPageChange(page - 1)}>
          Назад
        </button>
        <button className="btn btn-outline-primary btn-sm" disabled>
          {page + 1} / {totalPages}
        </button>
        <button className="btn btn-outline-primary btn-sm" disabled={page + 1 >= totalPages} onClick={() => onPageChange(page + 1)}>
          Вперед
        </button>
      </div>
    </div>
  )
}

export default Pagination
