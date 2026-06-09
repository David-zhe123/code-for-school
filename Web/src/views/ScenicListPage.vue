<template>
  <div class="split">
    <aside class="card side">
      <h3>分类</h3>
      <button
        class="text-btn"
        :class="{ active: activeCategory === '' }"
        @click="activeCategory = ''"
      >
        全部
      </button>
      <button
        v-for="cat in scenicCategories"
        :key="cat"
        class="text-btn"
        :class="{ active: activeCategory === cat }"
        @click="activeCategory = cat"
      >
        {{ cat }}
      </button>
    </aside>

    <section class="card main">
      <div class="inline-form">
        <input v-model.trim="keyword" placeholder="搜索景点名称/介绍" />
      </div>
      <div v-if="loading" class="loading">加载中...</div>
      <div v-else class="grid">
        <article v-for="spot in filteredList" :key="spot.id" class="mini-card">
          <img :src="spot.image" :alt="spot.name" />
          <div class="mini-body">
            <h3>{{ spot.name }}</h3>
            <p>{{ spot.category }} | ￥{{ spot.price }}</p>
            <div class="row">
              <RouterLink :to="`/scenic/${spot.id}`">查看详情</RouterLink>
              <button class="btn-primary" @click="add(spot)">加入行程</button>
            </div>
          </div>
        </article>
      </div>
    </section>
  </div>
</template>

<script setup>
import { computed, onMounted, ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import { scenicCategories } from '../data/mock'
import { useAppStore } from '../stores/appStore'

const route = useRoute()
const { scenicSpots, loadScenicSpots, addToItinerary } = useAppStore()
const activeCategory = ref(route.query.category || '')
const keyword = ref(route.query.keyword || '')
const loading = ref(false)

const filteredList = computed(() =>
  scenicSpots.value.filter((spot) => {
    const matchCategory = !activeCategory.value || spot.category === activeCategory.value
    const key = keyword.value.toLowerCase()
    const matchKeyword =
      !key ||
      spot.name.toLowerCase().includes(key) ||
      (spot.description && spot.description.toLowerCase().includes(key))
    return matchCategory && matchKeyword
  })
)

async function loadData() {
  loading.value = true
  try {
    const params = {}
    if (activeCategory.value) params.category = activeCategory.value
    if (keyword.value) params.keyword = keyword.value
    await loadScenicSpots(params)
  } finally {
    loading.value = false
  }
}

async function add(spot) {
  try {
    await addToItinerary(spot)
    alert('已加入我的行程')
  } catch (e) {
    alert(e.message || '加入行程失败，请先登录')
  }
}

// 监听分类变化重新加载
watch(activeCategory, () => loadData())

onMounted(() => loadData())
</script>
