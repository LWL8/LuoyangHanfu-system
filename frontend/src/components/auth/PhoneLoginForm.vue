<template>
  <el-form class="phone-login-form animate__animated animate__fadeIn" @submit.prevent>
    <el-form-item class="custom-form-item">
      <el-input
        v-model="phone"
        placeholder="请输入手机号"
        clearable
        maxlength="11"
        class="custom-input"
        @input="onPhoneInput"
        @keyup.enter="handleSendCode"
      >
        <template #prefix><el-icon><Iphone /></el-icon></template>
      </el-input>
    </el-form-item>

    <el-form-item class="custom-form-item">
      <div class="flex gap-3 w-full">
        <el-input
          v-model="code"
          placeholder="6 位验证码"
          maxlength="6"
          class="flex-1 custom-input"
          @input="onCodeInput"
          @keyup.enter="handleLogin"
        >
          <template #prefix><el-icon><Lock /></el-icon></template>
        </el-input>
        <el-button :disabled="sendCodeDisabled" :loading="sendingCode" class="code-btn" @click="handleSendCode">
          {{ countdown > 0 ? `${countdown}s` : '获取验证码' }}
        </el-button>
      </div>
    </el-form-item>

    <el-button
      type="primary"
      class="login-submit-btn w-full h-12 mt-6 text-lg tracking-[0.2em]"
      :loading="loading"
      :disabled="!canSubmit"
      @click="handleLogin"
    >
      立即登录
    </el-button>
  </el-form>
</template>

<script setup lang="ts">
import { computed, ref, onUnmounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { Iphone, Lock } from '@element-plus/icons-vue'
import useUserStore from '@/stores/user'

import { loginByPhone, sendSmsCode, loginResponseFromTokenVO } from '@/api/user'
import { ElMessage } from 'element-plus'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const phone = ref('')
const code = ref('')
const loading = ref(false)
const sendingCode = ref(false)
const countdown = ref(0)
let timer: ReturnType<typeof setInterval> | null = null

const phoneValid = computed(() => /^1[3-9]\d{9}$/.test(phone.value))
const codeValid = computed(() => /^\d{6}$/.test(code.value))
const sendCodeDisabled = computed(() => countdown.value > 0 || sendingCode.value || !phoneValid.value)
const canSubmit = computed(() => phoneValid.value && codeValid.value && !loading.value)

const onPhoneInput = (val: string) => {
  phone.value = String(val || '').replace(/\D/g, '').slice(0, 11)
}

const onCodeInput = (val: string) => {
  code.value = String(val || '').replace(/\D/g, '').slice(0, 6)
}

const startCountdown = () => {
  countdown.value = 60
  if (timer) {
    clearInterval(timer)
  }
  timer = setInterval(() => {
    countdown.value -= 1
    if (countdown.value <= 0 && timer) {
      clearInterval(timer)
      timer = null
    }
  }, 1000)
}

const handleSendCode = async () => {
  if (!phoneValid.value) {
    return ElMessage.error('请输入正确的手机号')
  }
  if (sendingCode.value || countdown.value > 0) {
    return
  }

  sendingCode.value = true
  try {
    const res = await sendSmsCode(phone.value)
    if (res.code === 200 || res.code === 0) {
      const data = res.data as { verifyCode?: string } | void
      if (data && 'verifyCode' in data && data.verifyCode) {
        code.value = data.verifyCode
        ElMessage.success('验证码已发送（开发环境已自动填充）')
      } else {
        ElMessage.success('验证码已发送')
      }
      startCountdown()
    } else {
      ElMessage.error(res.message || '发送失败')
    }
  } catch (error: any) {
    ElMessage.error(error?.response?.data?.message || '短信服务不可用')
    console.error('Send SMS error:', error)
  } finally {
    sendingCode.value = false
  }
}

const handleLogin = async () => {
  if (!phoneValid.value) return ElMessage.error('请输入正确的手机号')
  if (!codeValid.value) return ElMessage.error('请输入 6 位数字验证码')
  if (loading.value) return
  loading.value = true

  try {
    const res = await loginByPhone(phone.value, code.value)
    if (res.code === 200 && res.data) {
      userStore.loginSuccess(loginResponseFromTokenVO(res.data))
      userStore.syncUserProfile()

      ElMessage.success('登录成功')
      const redirect = (route.query.redirect as string) || '/'
      router.push(redirect)
    } else {
      const msg = res.message || '登录失败'
      ElMessage.error(msg)
      if (msg.includes('未注册')) {
        router.push({ path: '/register', query: { phone: phone.value } })
      }
    }
  } catch (error: any) {
    const msg = error?.response?.data?.message || '登录服务不可用'
    ElMessage.error(msg)
    if (String(msg).includes('未注册')) {
      router.push({ path: '/register', query: { phone: phone.value } })
    }
    console.error('Login error:', error)
  } finally {
    loading.value = false
  }
}

onUnmounted(() => {
  if (timer) {
    clearInterval(timer)
    timer = null
  }
})
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

.code-btn {
  height: 52px;
  border-radius: 16px;
  border: 1px solid #eed8c1;
  color: var(--lh-gold);
  font-weight: 600;
  background: white;
  letter-spacing: 0.05em;
  transition: all 0.4s;
}

.code-btn:hover:not(:disabled) {
  border-color: var(--lh-gold);
  color: var(--lh-red);
  background: #fefbf7;
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(212, 160, 23, 0.08);
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

