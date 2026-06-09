<template>
  <div class="split">
    <aside class="card side">
      <h3>后台管理</h3>
      <nav class="admin-nav">
        <RouterLink to="/admin/scenic">景点管理</RouterLink>
        <RouterLink to="/admin/orders">订单管理</RouterLink>
        <RouterLink to="/admin/users">用户管理</RouterLink>
      </nav>
      <div class="admin-logout">
        <button @click="handleLogout">退出后台</button>
      </div>
    </aside>
    <section class="main">
      <RouterView />
    </section>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { useAppStore } from '../../stores/appStore'

const router = useRouter()
const { adminLogout } = useAppStore()

function handleLogout() {
  adminLogout()
  router.push('/admin/login')
}
</script>

<style scoped>
.admin-nav {
  display: flex;
  flex-direction: column;
  gap: 8px;
}
.admin-nav a {
  display: block;
  padding: 8px 12px;
  text-decoration: none;
  color: #333;
  border-radius: 4px;
}
.admin-nav a:hover {
  background: #f0f0f0;
}
.admin-nav a.router-link-active {
  background: #e8f4f8;
  color: #0077b6;
  font-weight: 600;
}
.admin-logout {
  margin-top: 24px;
  padding-top: 16px;
  border-top: 1px solid #eee;
}
.admin-logout button {
  width: 100%;
  padding: 6px;
  border: 1px solid #d32f2f;
  color: #d32f2f;
  background: transparent;
  border-radius: 4px;
  cursor: pointer;
}
.admin-logout button:hover {
  background: #d32f2f;
  color: #fff;
}
</style>
