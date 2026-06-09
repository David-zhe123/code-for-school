<template>
  <section class="kpi-grid">
    <article class="kpi-card">
      <p>景点总数</p>
      <h3>{{ scenicSpots.length }}</h3>
      <span>覆盖热门分类</span>
    </article>
    <article class="kpi-card">
      <p>行程中景点</p>
      <h3>{{ state.itinerary.length }}</h3>
      <span>可继续调整顺序</span>
    </article>
    <article class="kpi-card">
      <p>我的订单数</p>
      <h3>{{ myOrderCount }}</h3>
      <span>实时查看订单状态</span>
    </article>
    <article class="kpi-card">
      <p>景点均价</p>
      <h3>￥{{ avgPrice }}</h3>
      <span>帮助预算规划</span>
    </article>
  </section>

  <section class="card hero-visual">
    <Transition name="banner-fade" mode="out-in">
      <div
        :key="heroBanners[currentBanner]"
        class="hero-bg-layer"
        :style="{ backgroundImage: `url(${heroBanners[currentBanner]})` }"
      ></div>
    </Transition>
    <div class="hero-bg-mask"></div>
    <button type="button" class="hero-arrow left" @click="prevBanner">‹</button>
    <button type="button" class="hero-arrow right" @click="nextBanner">›</button>
    <div class="hero-content">
      <div class="hero-search">
      <p class="hero-search-tag">NINGBO TRAVEL</p>
      <h2>探索宁波，发现甬城之美</h2>
      <p class="hero-search-desc">天一阁的书香、东钱湖的波光、老外滩的夜色……输入关键词，开启你的宁波之旅。</p>
      </div>
      <div class="search-combo hero-search-combo">
        <input v-model.trim="keyword" placeholder="输入景点关键词" @keyup.enter="goSearch" />
        <RouterLink class="btn-primary search-btn" :to="`/scenic?keyword=${encodeURIComponent(keyword)}`">
          <span class="search-btn-icon"></span>
          <span>立即搜索</span>
        </RouterLink>
      </div>
      <div class="hot-keywords">
        <span>热门关键词：</span>
        <button
          v-for="item in hotKeywords"
          :key="item"
          type="button"
          class="hot-keyword"
          @click="goSearch(item)"
        >
          {{ item }}
        </button>
      </div>
      <div class="hero-dots">
        <button
          v-for="(item, index) in heroBanners"
          :key="item"
          type="button"
          :class="['hero-dot', { active: currentBanner === index }]"
          @click="currentBanner = index"
        ></button>
      </div>
    </div>
  </section>

  <section class="card">
    <h2>热门景点推荐</h2>
    <div class="hero-grid">
      <article v-for="spot in featuredSpots" :key="spot.id" class="mini-card">
        <img :src="spot.image" :alt="spot.name" />
        <div class="mini-body">
          <h3>{{ spot.name }}</h3>
          <p>{{ spot.description }}</p>
        </div>
      </article>
    </div>
  </section>

  <section class="card">
    <h2>分类导航</h2>
    <div class="pill-row">
      <RouterLink
        v-for="item in categories"
        :key="item"
        :to="`/scenic?category=${encodeURIComponent(item)}`"
        class="pill"
      >
        {{ item }}
      </RouterLink>
    </div>
  </section>
</template>

<script setup>
import { computed, onMounted, onUnmounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { scenicCategories } from '../data/mock'
import { useAppStore } from '../stores/appStore'

const router = useRouter()
const { scenicSpots, state, loadScenicSpots } = useAppStore()
const categories = scenicCategories
const keyword = ref('')
const hotKeywords = ['天一阁', '东钱湖', '老外滩', '鼓楼', '慈城']
const heroBanners = [
  'https://picsum.photos/seed/ningbo-hero1/1600/600',
  'https://picsum.photos/seed/ningbo-hero2/1600/600',
  'https://picsum.photos/seed/ningbo-hero3/1600/600'
]
const currentBanner = ref(0)
let bannerTimer = null

const featuredSpots = computed(() => scenicSpots.value.slice(0, 3))

const myOrderCount = computed(() => state.orders.length)

const avgPrice = computed(() => {
  if (!scenicSpots.value.length) return 0
  const total = scenicSpots.value.reduce((sum, item) => sum + item.price, 0)
  return Math.round(total / scenicSpots.value.length)
})

function goSearch(nextKeyword = keyword.value) {
  keyword.value = nextKeyword
  router.push(`/scenic?keyword=${encodeURIComponent(nextKeyword)}`)
}

function resetBannerTimer() {
  if (bannerTimer) clearInterval(bannerTimer)
  bannerTimer = setInterval(() => {
    currentBanner.value = (currentBanner.value + 1) % heroBanners.length
  }, 3500)
}

function prevBanner() {
  currentBanner.value = (currentBanner.value - 1 + heroBanners.length) % heroBanners.length
  resetBannerTimer()
}

function nextBanner() {
  currentBanner.value = (currentBanner.value + 1) % heroBanners.length
  resetBannerTimer()
}

onMounted(async () => {
  await loadScenicSpots()
  resetBannerTimer()
})

onUnmounted(() => {
  if (bannerTimer) {
    clearInterval(bannerTimer)
    bannerTimer = null
  }
})
</script>
