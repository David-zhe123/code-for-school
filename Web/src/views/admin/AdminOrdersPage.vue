<template>
  <section class="card">
    <h2>订单管理</h2>
    <div v-if="loading" class="loading">加载中...</div>
    <table v-else-if="orders.length" class="table">
      <thead>
        <tr>
          <th>订单号</th>
          <th>用户</th>
          <th>总价</th>
          <th>状态</th>
          <th>操作</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="order in orders" :key="order.id">
          <td>{{ order.id }}</td>
          <td>{{ order.username }}</td>
          <td>￥{{ order.totalPrice }}</td>
          <td>
            <select v-model="statusMap[order.id]">
              <option value="待出行">待出行</option>
              <option value="已完成">已完成</option>
              <option value="已取消">已取消</option>
            </select>
          </td>
          <td>
            <button @click="updateHandler(order.id)">更新</button>
          </td>
        </tr>
      </tbody>
    </table>
    <p v-else>暂无订单，前台生成订单后会展示在这里。</p>
    <p v-if="errorMsg" class="error">{{ errorMsg }}</p>
  </section>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { useAppStore } from '../../stores/appStore'

const { loadAdminOrders, updateOrderStatus } = useAppStore()
const orders = ref([])
const loading = ref(false)
const errorMsg = ref('')
const statusMap = reactive({})

onMounted(async () => {
  loading.value = true
  try {
    orders.value = await loadAdminOrders()
    orders.value.forEach(o => { statusMap[o.id] = o.status })
  } finally {
    loading.value = false
  }
})

async function updateHandler(id) {
  errorMsg.value = ''
  try {
    await updateOrderStatus(id, statusMap[id])
    const order = orders.value.find(o => o.id === id)
    if (order) order.status = statusMap[id]
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
select {
  padding: 4px 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
}
</style>
