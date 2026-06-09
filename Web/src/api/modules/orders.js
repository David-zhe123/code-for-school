import { request } from '../http'

export const ordersApi = {
  create() {
    return request('/orders', {
      method: 'POST'
    })
  },
  myList() {
    return request('/orders/my')
  },
  adminList() {
    return request('/admin/orders')
  },
  updateStatus(id, payload) {
    return request(`/admin/orders/${id}/status`, {
      method: 'PATCH',
      body: JSON.stringify(payload)
    })
  }
}
