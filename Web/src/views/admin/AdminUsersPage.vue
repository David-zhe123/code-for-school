<template>
  <section class="card">
    <h2>用户管理</h2>
    <div v-if="loading" class="loading">加载中...</div>
    <table v-else-if="state.users.length" class="table">
      <thead>
        <tr>
          <th>ID</th>
          <th>用户名</th>
          <th>状态</th>
          <th>操作</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="user in state.users" :key="user.id">
          <td>{{ user.id }}</td>
          <td>{{ user.username }}</td>
          <td>{{ user.status }}</td>
          <td>
            <button @click="toggleHandler(user.id)">
              {{ user.status === '正常' ? '禁用' : '启用' }}
            </button>
          </td>
        </tr>
      </tbody>
    </table>
    <p v-else>暂无用户。</p>
    <p v-if="errorMsg" class="error">{{ errorMsg }}</p>
  </section>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useAppStore } from '../../stores/appStore'

const { state, loadUsers, toggleUserStatus } = useAppStore()
const loading = ref(false)
const errorMsg = ref('')

onMounted(async () => {
  loading.value = true
  try { await loadUsers() } catch {}
  loading.value = false
})

async function toggleHandler(id) {
  errorMsg.value = ''
  try {
    await toggleUserStatus(id)
  } catch (e) {
    errorMsg.value = e.message
  }
}
</script>

<style scoped>
.error {
  color: #d32f2f;
  margin-top: 8px;
}
</style>
