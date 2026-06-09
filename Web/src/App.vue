<script setup>
import { computed, onMounted, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAppStore } from './stores/appStore'

const { state, adminLogout: storeAdminLogout } = useAppStore()

const theme = ref('dark')
const route = useRoute()
const router = useRouter()
const adminLoggedIn = ref(false)
const portalLinks = [
  { to: '/', label: '首页总览', icon: '🏠' },
  { to: '/scenic', label: '景点浏览', icon: '🗺️' },
  { to: '/itinerary', label: '行程规划', icon: '🧳' },
  { to: '/orders', label: '我的订单', icon: '📄' },
  { to: '/auth', label: '登录注册', icon: '👤' }
]
const adminLinks = [
  { to: '/admin/scenic', label: '后台景点', icon: '🏞️' },
  { to: '/admin/orders', label: '后台订单', icon: '📊' },
  { to: '/admin/users', label: '后台用户', icon: '🛡️' }
]

const isAdminPage = computed(() => route.path.startsWith('/admin'))
const isAdminLoggedIn = computed(() => adminLoggedIn.value)
const isUserLoggedIn = computed(() => Boolean(state.user))
const showSingleGroup = computed(() => isAdminLoggedIn.value || isUserLoggedIn.value)
const primaryGroup = computed(() => {
  if (isAdminLoggedIn.value) return adminLinks
  if (isUserLoggedIn.value) return portalLinks
  return isAdminPage.value ? adminLinks : portalLinks
})
const secondaryGroup = computed(() => {
  if (showSingleGroup.value) return []
  return isAdminPage.value ? portalLinks : adminLinks
})
const primaryTitle = computed(() => {
  if (isAdminLoggedIn.value) return '后台分组'
  if (isUserLoggedIn.value) return '前台分组'
  return isAdminPage.value ? '后台分组' : '前台分组'
})
const secondaryTitle = computed(() => (isAdminPage.value ? '前台分组' : '后台分组'))

function applyTheme(nextTheme) {
  document.documentElement.setAttribute('data-theme', nextTheme)
  localStorage.setItem('theme', nextTheme)
  theme.value = nextTheme
}

function toggleTheme() {
  applyTheme(theme.value === 'light' ? 'dark' : 'light')
}

function adminLogout() {
  storeAdminLogout()
  adminLoggedIn.value = false
  router.push('/admin/login')
}

onMounted(() => {
  const saved = localStorage.getItem('theme')
  applyTheme(saved || 'dark')
  adminLoggedIn.value = localStorage.getItem('adminLoggedIn') === 'true'
})

watch(
  () => route.fullPath,
  () => {
    adminLoggedIn.value = localStorage.getItem('adminLoggedIn') === 'true'
  }
)
</script>

<template>
  <div class="layout">
    <aside class="quick-nav">
      <h3>快捷导航</h3>
      <p class="quick-nav-group-title">{{ primaryTitle }}</p>
      <RouterLink v-for="item in primaryGroup" :key="item.to" :to="item.to">
        <span class="nav-icon">{{ item.icon }}</span>
        <span>{{ item.label }}</span>
      </RouterLink>
      <template v-if="secondaryGroup.length">
        <p class="quick-nav-group-title muted">{{ secondaryTitle }}</p>
        <RouterLink v-for="item in secondaryGroup" :key="item.to" :to="item.to">
          <span class="nav-icon">{{ item.icon }}</span>
          <span>{{ item.label }}</span>
        </RouterLink>
      </template>
    </aside>

    <header class="topbar">
      <div class="topbar-head">
        <div>
          <h1>宁波旅游导航</h1>
          <p class="topbar-subtitle">发现宁波景点 · 规划行程 · 一站式管理</p>
        </div>
        <button class="theme-toggle" @click="toggleTheme">
          {{ theme === 'light' ? '🌙 深色模式' : '☀️ 浅色模式' }}
        </button>
      </div>
      <nav>
        <RouterLink to="/">首页</RouterLink>
        <RouterLink to="/scenic">景点列表</RouterLink>
        <RouterLink to="/itinerary">我的行程</RouterLink>
        <RouterLink to="/orders">我的订单</RouterLink>
        <RouterLink to="/auth">登录/注册</RouterLink>
        <RouterLink to="/admin/login">后台</RouterLink>
        <button v-if="isAdminLoggedIn" type="button" class="nav-logout" @click="adminLogout">
          管理员退出
        </button>
      </nav>
    </header>
    <main class="page-wrap">
      <RouterView v-slot="{ Component }">
        <Transition name="route-fade" mode="out-in">
          <div :key="route.fullPath" class="route-page">
            <component :is="Component" />
          </div>
        </Transition>
      </RouterView>
    </main>
  </div>
</template>
