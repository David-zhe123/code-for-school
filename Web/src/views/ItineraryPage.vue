<template>
  <section class="card">
    <h2>我的行程</h2>
    <div v-if="loading" class="loading">加载中...</div>
    <table v-else-if="state.itinerary.length" class="table">
      <thead>
        <tr>
          <th>顺序</th>
          <th>景点</th>
          <th>价格</th>
          <th>备注</th>
          <th>操作</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(item, index) in state.itinerary" :key="item.id">
          <td>{{ index + 1 }}</td>
          <td>{{ item.name }}</td>
          <td>￥{{ item.price }}</td>
          <td>
            <input :value="item.note" @input="updateNoteHandler(item.id, $event.target.value)" />
          </td>
          <td class="row">
            <button @click="moveHandler(index, -1)">上移</button>
            <button @click="moveHandler(index, 1)">下移</button>
            <button @click="removeHandler(item.id)">删除</button>
          </td>
        </tr>
      </tbody>
    </table>
    <p v-else>行程为空，请先添加景点。</p>
    <div v-if="state.itinerary.length" class="row">
      <strong>总价：￥{{ totalPrice }}</strong>
      <button @click="clearHandler">清空行程</button>
      <button class="btn-primary" @click="create">生成订单</button>
    </div>
  </section>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAppStore } from '../stores/appStore'

const router = useRouter()
const loading = ref(false)
const {
  state,
  totalPrice,
  loadItinerary,
  removeFromItinerary,
  moveItineraryItem,
  updateNote,
  clearItinerary,
  createOrder
} = useAppStore()

onMounted(async () => {
  loading.value = true
  try { await loadItinerary() } catch {}
  loading.value = false
})

async function updateNoteHandler(id, value) {
  try { await updateNote(id, value) } catch {}
}

async function moveHandler(index, step) {
  try { await moveItineraryItem(index, step) } catch {}
}

async function removeHandler(id) {
  try { await removeFromItinerary(id) } catch (e) { alert(e.message) }
}

async function clearHandler() {
  try { await clearItinerary() } catch (e) { alert(e.message) }
}

async function create() {
  try {
    const ok = await createOrder()
    if (!ok) {
      alert('请先登录并至少添加一个景点')
      return
    }
    alert('订单创建成功')
    router.push('/orders')
  } catch (e) {
    alert(e.message || '下单失败')
  }
}
</script>
