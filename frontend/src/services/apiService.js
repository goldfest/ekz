import axiosClient from '../api/axiosClient'

const pageParams = ({ search = '', page = 0, size = 6, sort = 'id,desc', ...filters }) => {
  const params = { page, size, sort }
  if (search) params.search = search
  Object.entries(filters).forEach(([key, value]) => {
    if (value !== undefined && value !== null && value !== '') params[key] = value
  })
  return params
}

const crud = (resource) => ({
  list: (params) => axiosClient.get(`/${resource}`, { params: pageParams(params || {}) }).then((r) => r.data),
  get: (id) => axiosClient.get(`/${resource}/${id}`).then((r) => r.data),
  create: (data) => axiosClient.post(`/${resource}`, data).then((r) => r.data),
  update: (id, data) => axiosClient.put(`/${resource}/${id}`, data).then((r) => r.data),
  remove: (id) => axiosClient.delete(`/${resource}/${id}`),
})

export const authApi = {
  login: (data) => axiosClient.post('/auth/login', data).then((r) => r.data),
  register: (data) => axiosClient.post('/auth/register', data).then((r) => r.data),
}

export const clientsApi = crud('clients')
export const employeesApi = crud('employees')
export const servicesApi = crud('services')
export const ordersApi = crud('orders')

export const usersApi = {
  list: (params) => axiosClient.get('/users', { params: pageParams(params || {}) }).then((r) => r.data),
  updateRole: (id, role) => axiosClient.patch(`/users/${id}/role`, { role }).then((r) => r.data),
  setEnabled: (id, enabled) => axiosClient.patch(`/users/${id}/enabled`, null, { params: { enabled } }).then((r) => r.data),
  remove: (id) => axiosClient.delete(`/users/${id}`),
}

export const getErrorMessage = (error) => {
  if (error.response?.data?.errors) {
    return Object.values(error.response.data.errors).join('; ')
  }
  return error.response?.data?.message || 'Ошибка выполнения операции'
}
