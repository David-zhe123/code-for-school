import { createRouter, createWebHistory } from 'vue-router'
import HomePage from '../views/HomePage.vue'
import ScenicListPage from '../views/ScenicListPage.vue'
import ScenicDetailPage from '../views/ScenicDetailPage.vue'
import ItineraryPage from '../views/ItineraryPage.vue'
import OrdersPage from '../views/OrdersPage.vue'
import AuthPage from '../views/AuthPage.vue'
import AdminLoginPage from '../views/admin/AdminLoginPage.vue'
import AdminLayout from '../views/admin/AdminLayout.vue'
import AdminScenicPage from '../views/admin/AdminScenicPage.vue'
import AdminOrdersPage from '../views/admin/AdminOrdersPage.vue'
import AdminUsersPage from '../views/admin/AdminUsersPage.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', component: HomePage },
    { path: '/scenic', component: ScenicListPage },
    { path: '/scenic/:id', component: ScenicDetailPage },
    { path: '/itinerary', component: ItineraryPage },
    { path: '/orders', component: OrdersPage },
    { path: '/auth', component: AuthPage },
    { path: '/admin/login', component: AdminLoginPage },
    {
      path: '/admin',
      component: AdminLayout,
      meta: { requiresAdmin: true },
      children: [
        { path: '', redirect: '/admin/scenic' },
        { path: 'scenic', component: AdminScenicPage },
        { path: 'orders', component: AdminOrdersPage },
        { path: 'users', component: AdminUsersPage }
      ]
    }
  ]
})

router.beforeEach((to) => {
  if (!to.matched.some((record) => record.meta.requiresAdmin)) return true

  const isAdminLoggedIn = localStorage.getItem('adminLoggedIn') === 'true'
  if (isAdminLoggedIn) return true

  return {
    path: '/admin/login',
    query: { redirect: to.fullPath }
  }
})

export default router
