import { request } from '../http'

export const itineraryApi = {
  list() {
    return request('/itinerary')
  },
  add(payload) {
    return request('/itinerary/items', {
      method: 'POST',
      body: JSON.stringify(payload)
    })
  },
  updateItem(id, payload) {
    return request(`/itinerary/items/${id}`, {
      method: 'PUT',
      body: JSON.stringify(payload)
    })
  },
  removeItem(id) {
    return request(`/itinerary/items/${id}`, {
      method: 'DELETE'
    })
  },
  clear() {
    return request('/itinerary/clear', {
      method: 'POST'
    })
  }
}
