const DEFAULT_TIMEOUT = 10000

function buildUrl(path) {
  const base = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api'
  return `${base}${path}`
}

function getToken() {
  // 优先取用户 token，其次取管理员 token
  return localStorage.getItem('userToken') || localStorage.getItem('adminToken') || ''
}

export async function request(path, options = {}) {
  const { timeout = DEFAULT_TIMEOUT, headers = {}, ...rest } = options
  const controller = new AbortController()
  const timer = setTimeout(() => controller.abort(), timeout)

  const token = getToken()
  const authHeaders = token ? { Authorization: `Bearer ${token}` } : {}

  try {
    const response = await fetch(buildUrl(path), {
      ...rest,
      headers: {
        'Content-Type': 'application/json',
        ...authHeaders,
        ...headers
      },
      signal: controller.signal
    })

    const text = await response.text()
    const data = text ? JSON.parse(text) : null

    if (!response.ok) {
      const message = data?.message || `请求失败: ${response.status}`
      throw new Error(message)
    }

    // 后端 ApiResponse 统一解包：{ code: 0, message, data }
    if (data && typeof data.code === 'number' && data.code !== 0) {
      throw new Error(data.message || '请求失败')
    }

    return data?.data !== undefined ? data.data : data
  } finally {
    clearTimeout(timer)
  }
}

export function withToken(token) {
  return token ? { Authorization: `Bearer ${token}` } : {}
}
