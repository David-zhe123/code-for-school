import { adminApi } from './modules/admin'
import { authApi } from './modules/auth'
import { itineraryApi } from './modules/itinerary'
import { ordersApi } from './modules/orders'
import { scenicApi } from './modules/scenic'

export const api = {
  auth: authApi,
  scenic: scenicApi,
  itinerary: itineraryApi,
  orders: ordersApi,
  admin: adminApi
}
