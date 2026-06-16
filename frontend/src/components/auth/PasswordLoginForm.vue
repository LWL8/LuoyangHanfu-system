<template>
  <el-form class="password-login-form animate__animated animate__fadeIn">
    <el-form-item class="custom-form-item">
      <el-input v-model="username" placeholder="手机号 / 用户名" clearable class="custom-input">
        <template #prefix><el-icon><User /></el-icon></template>
      </el-input>
    </el-form-item>

    <el-form-item class="custom-form-item">
      <el-input v-model="password" type="password" placeholder="请输入密码" show-password class="custom-input">
        <template #prefix><el-icon><Lock /></el-icon></template>
      </el-input>
    </el-form-item>

    <div class="flex justify-between items-center mb-6 px-1">
      <el-checkbox v-model="remember" class="custom-checkbox">记住我</el-checkbox>
      <el-button link class="text-xs text-gray-400 hover:text-[var(--lh-red)] transition-colors">忘记密码？</el-button>
    </div>

    <el-button
      type="primary"
      class="login-submit-btn w-full h-12 text-lg tracking-[0.2em]"
      :loading="loading"
      @click="handleLogin"
    >
      立即登录
    </el-button>
  </el-form>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { User, Lock } from '@element-plus/icons-vue'
import useUserStore from '@/stores/user'

import { loginByPassword, loginAsAdmin, loginResponseFromTokenVO } from '@/api/user'
import { ElMessage } from 'element-plus'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const username = ref('')
const password = ref('')
const remember = ref(false)
const loading = ref(false)

const handleLogin = async () => {
  if (!username.value || !password.value) return
  loading.value = true

  try {
    const rawUsername = username.value.trim()
    // 容错：常见误输入 amdin 自动按 admin 处理
    const normalizedUsername = rawUsername.toLowerCase() === 'amdin' ? 'admin' : rawUsername
    if (rawUsername.toLowerCase() === 'amdin') {
      ElMessage.warning('账号自动修正为 admin')
    }

    const loginFn = normalizedUsername.toLowerCase() === 'admin' ? loginAsAdmin : loginByPassword
    const res = await loginFn(normalizedUsername, password.value)

    if (res.code === 200 && res.data) {
      userStore.loginSuccess(loginResponseFromTokenVO(res.data))
      userStore.syncUserProfile()

      ElMessage.success('登录成功')

      if (userStore.isAdmin) {
        router.push('/admin')
      } else {
        const redirect = (route.query.redirect as string) || '/'
        router.push(redirect)
      }
    } else {
      ElMessage.error(res.message || '登录失败')
    }
  } catch (error: any) {
    const msg = error?.response?.data?.message || error?.message || '登录失败'
    if (msg.includes('管理员账号不存在')) {
      ElMessage.error('管理员账号不存在，请联系系统管理员')
    } else {
      ElMessage.error(msg)
    }
    console.error('Login error:', error)
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.custom-input :deep(.el-input__wrapper) {
  background-color: #fefbf7;
  box-shadow: 0 2px 8px rgba(212, 160, 23, 0.03) !important;
  border: 1px solid #eed8c1 !important;
  border-radius: 16px;
  height: 52px;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
}

.custom-input :deep(.el-input__wrapper.is-focus) {
  border-color: var(--lh-gold) !important;
  background-color: white;
  box-shadow: 0 4px 15px rgba(212, 160, 23, 0.1) !important;
}

.custom-checkbox :deep(.el-checkbox__input.is-checked .el-checkbox__inner) {
  background-color: var(--lh-gold);
  border-color: var(--lh-gold);
}

.custom-checkbox :deep(.el-checkbox__input.is-checked + .el-checkbox__label) {
  color: var(--lh-gold);
  font-weight: 600;
}

.login-submit-btn {
  color: #fff !important;
  background: linear-gradient(135deg, var(--lh-red), #a02824) !important;
  border: none !important;
  border-radius: 16px !important;
  box-shadow: 0 10px 25px rgba(189, 49, 44, 0.25) !important;
  font-weight: 600;
  letter-spacing: 0.3em;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1) !important;
}

.login-submit-btn:hover {
  transform: translateY(-3px);
  box-shadow: 0 15px 35px rgba(189, 49, 44, 0.35) !important;
  filter: brightness(1.1);
}

.login-submit-btn:active {
  transform: translateY(-1px);
}
</style>

