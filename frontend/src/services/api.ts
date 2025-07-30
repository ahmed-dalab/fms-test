import axios from 'axios'
import { store } from '../store/store'

const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL || 'http://localhost:3000/api',
})

api.interceptors.request.use((config) => {
  const token = store.getState().user.token
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  // a multi-tenant application, you might also want to add the tenant ID to the headers
  // const tenantId = store.getState().user.tenantId
  // if (tenantId) {
  //   config.headers['X-Tenant-ID'] = tenantId
  // }
  return config
})

export default api
