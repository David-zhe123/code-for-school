import { request } from '../http'

export const adminApi = {
  login(payload) {
    return request('/admin/login', {
      method: 'POST',
      body: JSON.stringify(payload)
    })
  },
  users() {
    return request('/admin/users')
  },
  updateUserStatus(id, payload) {
    return request(`/admin/users/${id}/status`, {
      method: 'PATCH',
      body: JSON.stringify(payload)
    })
  }
}
