import { request } from '../http'

export const scenicApi = {
  list(params = {}) {
    const query = new URLSearchParams(params).toString()
    return request(`/scenic${query ? `?${query}` : ''}`)
  },
  detail(id) {
    return request(`/scenic/${id}`)
  },
  create(payload) {
    return request('/admin/scenic', {
      method: 'POST',
      body: JSON.stringify(payload)
    })
  },
  update(id, payload) {
    return request(`/admin/scenic/${id}`, {
      method: 'PUT',
      body: JSON.stringify(payload)
    })
  },
  remove(id) {
    return request(`/admin/scenic/${id}`, {
      method: 'DELETE'
    })
  }
}
