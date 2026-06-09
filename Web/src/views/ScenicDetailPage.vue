<template>
  <section v-if="detail" class="card">
    <img class="detail-img" :src="detail.image" :alt="detail.name" />
    <h2>{{ detail.name }}</h2>
    <span class="category-tag">{{ detail.category }}</span>
    <p class="detail-desc">{{ detail.description }}</p>
    <p><strong>地址：</strong>{{ detail.address }}</p>
    <p><strong>门票：</strong>￥{{ detail.price }}</p>
    <div v-if="state.user" class="inline-form">
      <input v-model.trim="note" placeholder="备注（如：下午游玩）" />
      <button class="btn-primary" @click="addToTrip">加入行程</button>
    </div>
    <p v-else class="hint">请先登录后再加入行程</p>
  </section>
  <section v-else-if="loading" class="card">加载中...</section>
  <section v-else class="card">未找到景点。</section>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import { useAppStore } from '../stores/appStore'

const route = useRoute()
const { state, getScenicDetail, addToItinerary } = useAppStore()
const note = ref('')
const detail = ref(null)
const loading = ref(true)

onMounted(async () => {
  loading.value = true
  detail.value = await getScenicDetail(Number(route.params.id))
  loading.value = false
})

async function addToTrip() {
  if (!detail.value) return
  try {
    await addToItinerary(detail.value, note.value)
    alert('已加入行程')
  } catch (e) {
    alert(e.message || '加入行程失败，请先登录')
  }
}
</script>

<style scoped>
.category-tag {
  display: inline-block;
  background: #e8f4f8;
  color: #0077b6;
  padding: 2px 12px;
  border-radius: 12px;
  font-size: 0.85rem;
  margin-bottom: 12px;
}
.detail-desc {
  line-height: 1.8;
  color: #555;
  margin: 12px 0;
}
.hint {
  color: #999;
  font-style: italic;
}
</style>
