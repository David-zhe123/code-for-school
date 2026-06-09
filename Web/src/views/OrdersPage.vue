<template>
  <section class="card">
    <h2>我的订单</h2>
    <div v-if="loading" class="loading">加载中...</div>
    <table v-else-if="state.orders.length" class="table">
      <thead>
        <tr>
          <th>订单号</th>
          <th>下单时间</th>
          <th>总价</th>
          <th>状态</th>
          <th>景点数</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="order in state.orders" :key="order.id">
          <td>{{ order.id }}</td>
          <td>{{ order.time }}</td>
          <td>￥{{ order.totalPrice }}</td>
          <td>{{ order.status }}</td>
          <td>{{ order.items.length }}</td>
        </tr>
      </tbody>
    </table>
    <p v-else>暂无订单，请先创建行程并生成订单。</p>
  </section>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useAppStore } from '../stores/appStore'

const { state, loadMyOrders } = useAppStore()
const loading = ref(false)

onMounted(async () => {
  loading.value = true
  if (state.user) {
    try { await loadMyOrders() } catch {}
  }
  loading.value = false
})
</script>
