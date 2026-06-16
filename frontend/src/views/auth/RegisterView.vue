<template>
  <div class="auth-page min-h-screen flex bg-[var(--lh-cream)] overflow-hidden font-serif">
    <aside class="hidden lg:flex lg:w-1/2 p-20 items-center justify-center relative overflow-hidden bg-[#0c1222]">
      <div class="absolute inset-0 bg-gradient-to-br from-[var(--lh-navy)]/44 via-[#1a1528]/28 to-[var(--lh-navy)]/40 z-10"></div>
      <div class="absolute inset-0 bg-gradient-to-t from-black/18 via-transparent to-amber-900/6 z-[11] pointer-events-none"></div>

      <img
        :src="REGISTER_HERO_IMAGE"
        alt="入册神都 · 汉服同袍"
        class="absolute inset-0 w-full h-full object-cover auth-bg-image opacity-80"
        loading="eager"
        decoding="async"
      />

      <div class="relative z-20 text-white text-center flex flex-col items-center px-8 lg:translate-y-10">
        <div class="mb-10 animate__animated animate__fadeInDown">
          <h2 class="hero-title auth-hero-title">入册神都</h2>
        </div>

        <p class="hero-subtitle auth-hero-subtitle">新用户注册</p>
        <div class="hero-divider auth-hero-divider register-divider"></div>
        <p class="hero-info auth-hero-info">创建账号后可在线预约和查看订单</p>
      </div>

    </aside>

    <main class="w-full lg:w-1/2 flex items-center justify-center p-6 sm:p-10 lg:p-16 xl:p-24 relative">
      <div class="auth-paper-bg"></div>
      <div class="auth-tiles-bg"></div>

      <div class="max-w-[28rem] w-full auth-card-container z-10">
        <section class="auth-card auth-card-register">
          <div class="auth-card-side-line register-side-line"></div>
          <div class="auth-card-top-line"></div>

          <header class="text-center mb-8 relative z-10 pt-1">
            <p class="auth-card-tag">用户注册</p>
            <h1 class="auth-card-title">创建账号</h1>
            <p class="auth-card-sub">请填写以下信息</p>
          </header>

          <el-form :model="form" class="space-y-5 relative z-10">
            <el-form-item class="m-0">
              <el-input v-model="form.nickname" placeholder="请输入昵称" class="premium-input-field">
                <template #prefix><el-icon size="18"><User /></el-icon></template>
              </el-input>
            </el-form-item>

            <el-form-item class="m-0">
              <el-input v-model="form.phone" placeholder="请输入手机号" maxlength="11" class="premium-input-field">
                <template #prefix><el-icon size="18"><Iphone /></el-icon></template>
              </el-input>
            </el-form-item>

            <el-form-item class="m-0">
              <div class="flex gap-3 w-full">
                <el-input v-model="form.code" placeholder="请输入短信验证码" maxlength="6" class="flex-1 premium-input-field">
                  <template #prefix><el-icon size="18"><Lock /></el-icon></template>
                </el-input>
                <button
                  :disabled="countdown > 0"
                  class="code-btn-premium"
                  @click.prevent="handleSendCode"
                >
                  {{ countdown > 0 ? `${countdown}s` : '获取验证码' }}
                </button>
              </div>
            </el-form-item>

            <el-form-item class="m-0">
              <el-input v-model="form.password" type="password" placeholder="请设置密码" show-password class="premium-input-field">
                <template #prefix><el-icon size="18"><Lock /></el-icon></template>
              </el-input>
            </el-form-item>

            <el-form-item class="m-0">
              <el-input
                v-model="form.confirmPassword"
                type="password"
                placeholder="请确认密码"
                show-password
                class="premium-input-field"
              >
                <template #prefix><el-icon size="18"><Lock /></el-icon></template>
              </el-input>
            </el-form-item>

            <div class="flex items-center gap-3 mt-2 mb-8 px-1 opacity-0 register-footer-anim">
              <el-checkbox v-model="agreed" class="premium-checkbox" />
              <span class="text-[12px] text-[var(--lh-text-secondary)] font-semibold tracking-[0.06em]">
                我已阅读并同意
                <button class="text-[var(--lh-gold)] font-black hover:text-[var(--lh-red)] transition-colors" type="button">《用户协议》</button>
              </span>
            </div>

            <button
              class="register-submit-btn"
              :disabled="loading"
              @click.prevent="handleRegister"
            >
              {{ loading ? '注册中...' : '注册并登录' }}
            </button>
          </el-form>

          <footer class="auth-card-footer register-footer-anim opacity-0">
            <span>已有账号？</span>
            <router-link to="/login" class="text-[var(--lh-red)] font-black tracking-[0.15em] hover:tracking-[0.22em] transition-all">
              去登录 →
            </router-link>
          </footer>

          <div class="auth-watermark" aria-hidden="true">册</div>
        </section>
      </div>
    </main>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onUnmounted, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { User, Iphone, Lock } from '@element-plus/icons-vue'
import useUserStore from '@/stores/user'
import gsap from 'gsap'
import { sendSmsCode, registerUser, loginByPassword, loginResponseFromTokenVO } from '@/api/user'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const REGISTER_HERO_IMAGE = '/images/register-bg.png'

const form = reactive({
  nickname: '',
  phone: '',
  code: '',
  password: '',
  confirmPassword: ''
})

const agreed = ref(false)
const loading = ref(false)
const countdown = ref(0)
const smsCodeServer = ref('')
let timer: ReturnType<typeof setInterval> | null = null

onMounted(() => {
  const qPhone = String(route.query.phone || '').replace(/\D/g, '').slice(0, 11)
  if (qPhone) {
    form.phone = qPhone
  }

  const timeline = gsap.timeline()

  gsap.to('.auth-bg-image', {
    scale: 1.09,
    duration: 50,
    ease: 'sine.inOut',
    repeat: -1,
    yoyo: true
  })

  timeline
    .from('.auth-hero-title', { y: 90, opacity: 0, duration: 1.35, ease: 'expo.out' })
    .from('.auth-hero-subtitle', { y: 40, opacity: 0, duration: 1.05, ease: 'expo.out' }, '-=0.9')
    .from('.auth-hero-divider', { scaleX: 0, duration: 1.0, ease: 'power4.inOut' }, '-=0.6')
    .from('.auth-hero-info', { y: 16, opacity: 0, duration: 1.0 }, '-=0.55')

  gsap.from('.auth-card-container', {
    x: -72,
    opacity: 0,
    duration: 1.7,
    ease: 'expo.out',
    delay: 0.2
  })

  gsap.to('.register-footer-anim', {
    opacity: 1,
    duration: 1.0,
    delay: 1.0,
    stagger: 0.2,
    ease: 'power2.out'
  })
})

const handleSendCode = async () => {
  if (!/^1[3-9]\d{9}$/.test(form.phone)) {
    return (window as any).ElMessage?.error('请输入正确的手机号')
  }

  try {
    const res: any = await sendSmsCode(form.phone)
    if (res?.code === 200 || res?.code === 0) {
      smsCodeServer.value = res?.data?.verifyCode || '123456'
      countdown.value = 60
      timer = setInterval(() => {
        countdown.value--
        if (countdown.value <= 0 && timer) {
          clearInterval(timer)
          timer = null
        }
      }, 1000)
      ;(window as any).ElMessage?.success('验证码已发送')
    } else {
      ;(window as any).ElMessage?.error(res?.message || '验证码发送失败')
    }
  } catch (e: any) {
    ;(window as any).ElMessage?.error(e?.response?.data?.message || '验证码发送失败')
  }
}

const handleRegister = async () => {
  if (!form.nickname || !form.phone || !form.code || !form.password) {
    return (window as any).ElMessage?.error('请完整填写注册信息')
  }
  if (form.password !== form.confirmPassword) {
    return (window as any).ElMessage?.error('两次密码输入不一致')
  }
  if (!smsCodeServer.value || form.code !== smsCodeServer.value) {
    return (window as any).ElMessage?.error('验证码错误')
  }
  if (!agreed.value) {
    return (window as any).ElMessage?.warning('请先同意《用户协议》')
  }

  loading.value = true
  try {
    const registerRes: any = await registerUser({
      username: form.phone,
      nickname: form.nickname,
      password: form.password,
      phone: form.phone,
      email: '',
    })
    if (!(registerRes?.code === 200 || registerRes?.code === 0)) {
      return (window as any).ElMessage?.error(registerRes?.message || '注册失败')
    }

    const loginRes: any = await loginByPassword(form.phone, form.password)
    if (loginRes?.code === 200 || loginRes?.code === 0) {
      userStore.loginSuccess(loginResponseFromTokenVO(loginRes.data))
      ;(window as any).ElMessage?.success('注册成功')
      router.push('/')
    } else {
      ;(window as any).ElMessage?.success('注册成功，请登录')
      router.push('/login')
    }
  } catch (e: any) {
    ;(window as any).ElMessage?.error(e?.response?.data?.message || '注册失败')
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
.auth-page {
  background-attachment: fixed;
}

.hero-badge {
  width: 5.1rem;
  height: 5.1rem;
  border-radius: 9999px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 1.5rem;
  backdrop-filter: blur(4px);
}

.hero-badge-register {
  box-shadow: 0 0 42px rgba(153, 27, 27, 0.32);
  border: 2px solid rgba(153, 27, 27, 0.5);
  background: rgba(15, 23, 42, 0.35);
}

.hero-title {
  font-size: clamp(3rem, 4vw, 4.5rem);
  font-weight: 900;
  letter-spacing: 0.35em;
  line-height: 1.05;
  filter: drop-shadow(0 8px 32px rgba(0, 0, 0, 0.45));
}

.hero-subtitle {
  color: var(--lh-gold);
  letter-spacing: 0.45em;
  font-size: 1.05rem;
  font-weight: 900;
}

.hero-divider {
  width: 9rem;
  height: 1px;
  margin: 3rem 0 2rem;
  background: linear-gradient(to right, transparent, var(--lh-gold), transparent);
}

.register-divider {
  background: linear-gradient(to right, transparent, var(--lh-red), transparent);
}

.hero-info {
  color: rgba(255, 255, 255, 0.75);
  font-size: 0.875rem;
  letter-spacing: 0.25em;
}

.auth-paper-bg,
.auth-tiles-bg {
  position: absolute;
  inset: 0;
  pointer-events: none;
}

.auth-paper-bg {
  opacity: 0.05;
  background-image: url('https://www.transparenttextures.com/patterns/natural-paper.png');
}

.auth-tiles-bg {
  opacity: 0.03;
  background-image: url('https://www.transparenttextures.com/patterns/oriental-tiles.png');
}

.auth-card {
  position: relative;
  overflow: hidden;
  border-radius: 1.75rem;
  border: 1px solid rgba(180, 83, 9, 0.2);
  background: linear-gradient(to bottom, #fff, #fffcf8 45%, #fff8f0);
  padding: 2.25rem 2.25rem 2.5rem;
  box-shadow:
    inset 0 1px 0 rgba(255, 255, 255, 0.85),
    0 32px 64px -24px rgba(15, 23, 42, 0.18);
}

.auth-card-register {
  border-color: rgba(153, 27, 27, 0.16);
}

.auth-card-side-line {
  position: absolute;
  left: 0;
  top: 0;
  bottom: 0;
  width: 3px;
  border-radius: 1.75rem 0 0 1.75rem;
}

.register-side-line {
  background: linear-gradient(to bottom, var(--lh-red), rgba(180, 83, 9, 0.65), var(--lh-red));
}

.auth-card-top-line {
  position: absolute;
  top: 0;
  left: 2rem;
  right: 2rem;
  height: 1px;
  background: linear-gradient(to right, transparent, rgba(180, 83, 9, 0.35), transparent);
}

.auth-card-tag {
  font-size: 10px;
  color: var(--lh-gold);
  font-weight: 900;
  letter-spacing: 0.45em;
  text-transform: uppercase;
  margin-bottom: 0.75rem;
}

.auth-card-title {
  font-size: clamp(1.65rem, 3vw, 2.25rem);
  font-weight: 900;
  color: var(--lh-navy);
  letter-spacing: 0.18em;
  margin-bottom: 0.5rem;
}

.auth-card-sub {
  margin-top: 0.75rem;
  font-size: 10px;
  color: var(--lh-text-secondary);
  font-weight: 700;
  letter-spacing: 0.35em;
  text-transform: uppercase;
  opacity: 0.6;
}

.premium-input-field :deep(.el-input__wrapper) {
  border-radius: 14px;
  background: #fefbf7;
  border: 1px solid #eed8c1;
  box-shadow: 0 2px 8px rgba(180, 83, 9, 0.04) !important;
  height: 52px;
  transition: border-color 0.25s ease, box-shadow 0.25s ease, background 0.25s ease;
}

.premium-input-field :deep(.el-input__wrapper.is-focus) {
  border-color: var(--lh-gold) !important;
  background: #fff;
  box-shadow: 0 6px 16px rgba(180, 83, 9, 0.12) !important;
}

.code-btn-premium {
  height: 52px;
  min-width: 110px;
  border-radius: 14px;
  border: 1px solid rgba(180, 83, 9, 0.45);
  background: #fff;
  color: var(--lh-gold);
  font-size: 12px;
  font-weight: 800;
  letter-spacing: 0.06em;
  transition: all 0.25s ease;
}

.code-btn-premium:hover:not(:disabled) {
  background: var(--lh-gold);
  color: #fff;
  box-shadow: 0 8px 16px rgba(180, 83, 9, 0.2);
}

.code-btn-premium:disabled {
  opacity: 0.55;
  cursor: not-allowed;
}

.premium-checkbox :deep(.el-checkbox__input.is-checked .el-checkbox__inner) {
  background-color: var(--lh-red);
  border-color: var(--lh-red);
}

.register-submit-btn {
  width: 100%;
  height: 54px;
  border: none;
  border-radius: 14px;
  background: linear-gradient(135deg, var(--lh-red), #a02824);
  color: #fff;
  font-size: 14px;
  font-weight: 800;
  letter-spacing: 0.3em;
  box-shadow: 0 14px 30px rgba(153, 27, 27, 0.3);
  transition: transform 0.25s ease, box-shadow 0.25s ease, filter 0.25s ease;
}

.register-submit-btn:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 18px 36px rgba(153, 27, 27, 0.36);
  filter: brightness(1.04);
}

.register-submit-btn:disabled {
  opacity: 0.65;
  cursor: not-allowed;
}

.auth-card-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: 2rem;
  padding-top: 1.5rem;
  border-top: 1px dashed rgba(226, 232, 240, 0.8);
  font-size: 11px;
  font-weight: 700;
  letter-spacing: 0.18em;
  color: var(--lh-text-secondary);
}

.auth-watermark {
  position: absolute;
  right: -0.5rem;
  bottom: -0.4rem;
  font-size: 7rem;
  line-height: 1;
  font-weight: 900;
  color: var(--lh-navy);
  opacity: 0.04;
  user-select: none;
  pointer-events: none;
}

@media (max-width: 640px) {
  .auth-card {
    padding: 2rem 1.25rem 2.2rem;
  }

  .auth-card-footer {
    letter-spacing: 0.06em;
  }
}
</style>
