import { computed, reactive, ref } from 'vue'
import { scenicApi } from '../api/modules/scenic'
import { authApi } from '../api/modules/auth'
import { itineraryApi } from '../api/modules/itinerary'
import { ordersApi } from '../api/modules/orders'
import { adminApi } from '../api/modules/admin'

// ============================================================
// 响应式状态
// ============================================================
const scenicSpots = ref([])

const state = reactive({
  user: JSON.parse(localStorage.getItem('userInfo') || 'null'),
  users: [],       // 管理员：用户列表
  itinerary: [],   // 我的行程
  orders: []       // 我的订单
})

// ============================================================
// 景点
// ============================================================
async function loadScenicSpots(params = {}) {
  try {
    const data = await scenicApi.list(params)
    scenicSpots.value = data || []
    return scenicSpots.value
  } catch {
    return []
  }
}

async function getScenicDetail(id) {
  try {
    return await scenicApi.detail(id)
  } catch {
    return null
  }
}

// ============================================================
// 认证
// ============================================================
async function register(username, password) {
  try {
    await authApi.register({ username, password })
    return { ok: true, message: '注册成功，请登录' }
  } catch (e) {
    return { ok: false, message: e.message || '注册失败' }
  }
}

async function login(username, password) {
  try {
    const data = await authApi.login({ username, password })
    // data = { token: '...' }
    if (data && data.token) {
      localStorage.setItem('userToken', data.token)
      localStorage.setItem('userInfo', JSON.stringify({ username }))
      state.user = { username }
    }
    return { ok: true, message: '登录成功' }
  } catch (e) {
    return { ok: false, message: e.message || '登录失败' }
  }
}

function logout() {
  localStorage.removeItem('userToken')
  localStorage.removeItem('userInfo')
  state.user = null
  state.itinerary = []
  state.orders = []
}

async function loadProfile() {
  try {
    const data = await authApi.getProfile()
    if (data) {
      state.user = { username: data.username, status: data.status }
    }
    return data
  } catch {
    return null
  }
}

// ============================================================
// 行程
// ============================================================
async function loadItinerary() {
  try {
    const data = await itineraryApi.list()
    state.itinerary = (data || []).map(item => ({
      id: item.id,
      scenicId: item.scenicId,
      name: item.scenicName || '',
      price: item.scenicPrice || 0,
      note: item.note || '',
      sortOrder: item.sortOrder || 1
    }))
    return state.itinerary
  } catch {
    return []
  }
}

async function addToItinerary(spot, note = '') {
  try {
    await itineraryApi.add({
      scenicId: spot.id,
      note
    })
    // 重新加载行程
    await loadItinerary()
  } catch (e) {
    throw new Error(e.message || '加入行程失败')
  }
}

async function removeFromItinerary(id) {
  try {
    await itineraryApi.removeItem(id)
    state.itinerary = state.itinerary.filter(item => item.id !== id)
  } catch (e) {
    throw new Error(e.message || '删除失败')
  }
}

async function moveItineraryItem(index, step) {
  const target = index + step
  if (target < 0 || target >= state.itinerary.length) return

  const list = [...state.itinerary]
  const [current] = list.splice(index, 1)
  list.splice(target, 0, current)
  state.itinerary = list

  // 同步到后端：批量更新 sortOrder
  try {
    for (let i = 0; i < list.length; i++) {
      await itineraryApi.updateItem(list[i].id, { sortOrder: i + 1 })
    }
  } catch {
    // 排序失败时重新加载
    await loadItinerary()
  }
}

async function updateNote(id, note) {
  const item = state.itinerary.find(x => x.id === id)
  if (item) item.note = note
  try {
    await itineraryApi.updateItem(id, { note })
  } catch {
    await loadItinerary()
  }
}

async function clearItinerary() {
  try {
    await itineraryApi.clear()
    state.itinerary = []
  } catch (e) {
    throw new Error(e.message || '清空失败')
  }
}

// ============================================================
// 订单
// ============================================================
async function loadMyOrders() {
  try {
    const data = await ordersApi.myList()
    state.orders = (data || []).map(order => ({
      id: order.id,
      username: state.user?.username || '',
      time: order.createdAt || '',
      totalPrice: order.totalPrice || 0,
      status: order.status || '待出行',
      items: (order.items || []).map(item => ({
        id: item.scenicId || item.id,
        name: item.scenicName || '',
        price: item.scenicPrice || 0,
        note: item.note || ''
      }))
    }))
    return state.orders
  } catch {
    return []
  }
}

async function createOrder() {
  if (!state.user || state.itinerary.length === 0) return false
  try {
    await ordersApi.create()
    await loadItinerary()
    await loadMyOrders()
    return true
  } catch (e) {
    throw new Error(e.message || '下单失败')
  }
}

async function updateOrderStatus(id, status) {
  try {
    await ordersApi.updateStatus(id, { status })
    const order = state.orders.find(x => x.id === id)
    if (order) order.status = status
  } catch (e) {
    throw new Error(e.message || '更新订单状态失败')
  }
}

// ============================================================
// 管理员
// ============================================================
async function adminLogin(username, password) {
  try {
    const data = await adminApi.login({ username, password })
    if (data && data.token) {
      localStorage.setItem('adminToken', data.token)
      localStorage.setItem('adminInfo', JSON.stringify({ username }))
      localStorage.setItem('adminLoggedIn', 'true')
    }
    return { ok: true, message: '管理员登录成功' }
  } catch (e) {
    return { ok: false, message: e.message || '管理员登录失败' }
  }
}

function adminLogout() {
  localStorage.removeItem('adminToken')
  localStorage.removeItem('adminInfo')
  localStorage.setItem('adminLoggedIn', 'false')
}

async function loadUsers() {
  try {
    const data = await adminApi.users()
    state.users = (data || []).map(u => ({
      id: u.id,
      username: u.username,
      status: u.status === 'NORMAL' ? '正常' : '禁用'
    }))
    return state.users
  } catch {
    return []
  }
}

async function toggleUserStatus(id) {
  const target = state.users.find(x => x.id === id)
  if (!target) return
  const newStatus = target.status === '正常' ? '禁用' : '正常'
  try {
    await adminApi.updateUserStatus(id, { status: newStatus === '正常' ? 'NORMAL' : 'DISABLED' })
    target.status = newStatus
  } catch (e) {
    throw new Error(e.message || '更新用户状态失败')
  }
}

async function addScenic(payload) {
  try {
    await scenicApi.create(payload)
    await loadScenicSpots()
  } catch (e) {
    throw new Error(e.message || '新增景点失败')
  }
}

async function updateScenic(id, payload) {
  try {
    await scenicApi.update(id, payload)
    await loadScenicSpots()
  } catch (e) {
    throw new Error(e.message || '更新景点失败')
  }
}

async function deleteScenic(id) {
  try {
    await scenicApi.remove(id)
    await loadScenicSpots()
  } catch (e) {
    throw new Error(e.message || '删除景点失败')
  }
}

async function loadAdminOrders() {
  try {
    const data = await ordersApi.adminList()
    return (data || []).map(order => ({
      id: order.id,
      username: order.username || '',
      time: order.createdAt || '',
      totalPrice: order.totalPrice || 0,
      status: order.status || '待出行',
      items: (order.items || []).map(item => ({
        id: item.scenicId || item.id,
        name: item.scenicName || '',
        price: item.scenicPrice || 0,
        note: item.note || ''
      }))
    }))
  } catch {
    return []
  }
}

// ============================================================
// 导出
// ============================================================
export function useAppStore() {
  const totalPrice = computed(() =>
    state.itinerary.reduce((sum, item) => sum + (item.price || 0), 0)
  )

  return {
    // 状态
    state,
    scenicSpots,

    // 景点
    loadScenicSpots,
    getScenicDetail,

    // 认证
    register,
    login,
    logout,
    loadProfile,

    // 行程
    loadItinerary,
    addToItinerary,
    removeFromItinerary,
    moveItineraryItem,
    updateNote,
    clearItinerary,
    totalPrice,

    // 订单
    loadMyOrders,
    createOrder,
    updateOrderStatus,

    // 管理员
    adminLogin,
    adminLogout,
    loadUsers,
    toggleUserStatus,
    addScenic,
    updateScenic,
    deleteScenic,
    loadAdminOrders
  }
}
