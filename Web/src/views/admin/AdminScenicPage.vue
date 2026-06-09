<template>
  <section class="card">
    <h2>景点管理</h2>
    <div class="inline-form">
      <input v-model.trim="form.name" placeholder="景点名" />
      <select v-model="form.category">
        <option value="">选择分类</option>
        <option value="历史人文">历史人文</option>
        <option value="自然风光">自然风光</option>
        <option value="城市地标">城市地标</option>
        <option value="主题乐园">主题乐园</option>
      </select>
      <input v-model.number="form.price" placeholder="价格" type="number" />
      <input v-model.trim="form.address" placeholder="地址" />
      <input v-model.trim="form.image" placeholder="图片链接" />
      <button class="btn-primary" @click="save">{{ form.id ? '更新' : '新增' }}</button>
    </div>
    <div v-if="loading" class="loading">加载中...</div>
    <table v-else class="table">
      <thead>
        <tr>
          <th>ID</th>
          <th>名称</th>
          <th>分类</th>
          <th>价格</th>
          <th>地址</th>
          <th>操作</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="item in scenicSpots" :key="item.id">
          <td>{{ item.id }}</td>
          <td>{{ item.name }}</td>
          <td>{{ item.category }}</td>
          <td>￥{{ item.price }}</td>
          <td>{{ item.address }}</td>
          <td class="row">
            <button @click="edit(item)">编辑</button>
            <button @click="deleteHandler(item.id)">删除</button>
          </td>
        </tr>
      </tbody>
    </table>
    <p v-if="errorMsg" class="error">{{ errorMsg }}</p>
  </section>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { useAppStore } from '../../stores/appStore'

const { scenicSpots, loadScenicSpots, addScenic, updateScenic, deleteScenic } = useAppStore()
const loading = ref(false)
const errorMsg = ref('')
const form = reactive({
  id: null,
  name: '',
  category: '',
  price: 0,
  address: '',
  image: '',
  description: ''
})

function edit(item) {
  Object.assign(form, {
    id: item.id,
    name: item.name,
    category: item.category,
    price: item.price,
    address: item.address || '',
    image: item.image || '',
    description: item.description || ''
  })
}

function resetForm() {
  Object.assign(form, {
    id: null,
    name: '',
    category: '',
    price: 0,
    address: '',
    image: '',
    description: ''
  })
}

async function save() {
  if (!form.name || !form.category || !form.price) return
  errorMsg.value = ''
  try {
    const payload = {
      name: form.name,
      category: form.category,
      price: form.price,
      address: form.address,
      image: form.image,
      description: form.description || '管理员新增景点描述'
    }
    if (form.id) {
      await updateScenic(form.id, payload)
    } else {
      await addScenic(payload)
    }
    resetForm()
  } catch (e) {
    errorMsg.value = e.message
  }
}

async function deleteHandler(id) {
  if (!confirm('确定删除该景点？')) return
  errorMsg.value = ''
  try {
    await deleteScenic(id)
  } catch (e) {
    errorMsg.value = e.message
  }
}

onMounted(async () => {
  loading.value = true
  await loadScenicSpots()
  loading.value = false
})
</script>

<style scoped>
.error {
  color: #d32f2f;
  margin-top: 8px;
}
select {
  padding: 6px 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
}
</style>
