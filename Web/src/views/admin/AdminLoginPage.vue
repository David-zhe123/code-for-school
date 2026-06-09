<template>
  <section class="card narrow">
    <h2>管理员登录</h2>
    <div class="inline-form">
      <input v-model.trim="name" placeholder="管理员账号" />
      <input v-model.trim="password" placeholder="密码" type="password" />
      <button class="btn-primary" @click="submit" :disabled="submitting">进入后台</button>
    </div>
    <p v-if="errorMsg" class="error">{{ errorMsg }}</p>
  </section>
</template>

<script setup>
import { ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAppStore } from '../../stores/appStore'

const router = useRouter()
const route = useRoute()
const { adminLogin } = useAppStore()
const name = ref('')
const password = ref('')
const errorMsg = ref('')
const submitting = ref(false)

async function submit() {
  if (!name.value || !password.value) {
    errorMsg.value = '请填写账号和密码'
    return
  }
  errorMsg.value = ''
  submitting.value = true
  try {
    const result = await adminLogin(name.value.trim(), password.value.trim())
    if (result.ok) {
      const redirectPath = route.query.redirect || '/admin'
      router.push(redirectPath)
    } else {
      errorMsg.value = result.message
    }
  } catch {
    errorMsg.value = '登录失败，请重试'
  } finally {
    submitting.value = false
  }
}
</script>

<style scoped>
.error {
  color: #d32f2f;
  margin-top: 8px;
}
</style>
