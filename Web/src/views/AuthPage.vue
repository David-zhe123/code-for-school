<template>
  <section class="card">
    <h2>用户注册与登录</h2>
    <div class="auth-switch">
      <button
        type="button"
        :class="['auth-switch-btn', { active: mode === 'login' }]"
        @click="mode = 'login'"
      >
        登录
      </button>
      <button
        type="button"
        :class="['auth-switch-btn', { active: mode === 'register' }]"
        @click="mode = 'register'"
      >
        注册
      </button>
    </div>

    <div class="auth-panel">
      <template v-if="mode === 'register'">
        <h3>用户注册</h3>
        <div class="inline-form">
          <input v-model.trim="registerForm.username" placeholder="注册用户名" />
          <input v-model.trim="registerForm.password" placeholder="注册密码" type="password" />
          <button class="btn-primary" @click="submitRegister" :disabled="submitting">立即注册</button>
        </div>
      </template>

      <template v-else>
        <h3>用户登录</h3>
        <div class="inline-form">
          <input v-model.trim="loginForm.username" placeholder="登录用户名" />
          <input v-model.trim="loginForm.password" placeholder="登录密码" type="password" />
          <button class="btn-primary" @click="submitLogin" :disabled="submitting">立即登录</button>
          <button @click="logout">退出登录</button>
        </div>
      </template>
    </div>
    <p v-if="state.user">当前已登录：{{ state.user.username }}</p>
    <p v-if="errorMsg" class="error">{{ errorMsg }}</p>
  </section>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useAppStore } from '../stores/appStore'

const mode = ref('login')
const submitting = ref(false)
const errorMsg = ref('')
const registerForm = reactive({ username: '', password: '' })
const loginForm = reactive({ username: '', password: '' })
const { state, register, login, logout } = useAppStore()

async function submitRegister() {
  if (!registerForm.username || !registerForm.password) {
    errorMsg.value = '请填写用户名和密码'
    return
  }
  errorMsg.value = ''
  submitting.value = true
  try {
    const result = await register(registerForm.username, registerForm.password)
    alert(result.message)
    if (result.ok) {
      loginForm.username = registerForm.username
      mode.value = 'login'
      registerForm.username = ''
      registerForm.password = ''
    }
  } finally {
    submitting.value = false
  }
}

async function submitLogin() {
  if (!loginForm.username || !loginForm.password) {
    errorMsg.value = '请填写用户名和密码'
    return
  }
  errorMsg.value = ''
  submitting.value = true
  try {
    const result = await login(loginForm.username, loginForm.password)
    if (!result.ok) {
      errorMsg.value = result.message
      return
    }
    alert(result.message)
    loginForm.password = ''
    errorMsg.value = ''
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
