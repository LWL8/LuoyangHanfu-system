<template>
  <div class="auth-page min-h-screen flex bg-[var(--lh-cream)] overflow-hidden font-serif">
    <aside class="hidden lg:flex lg:w-1/2 p-20 items-center justify-center relative overflow-hidden bg-[#0c1222]">
      <div class="absolute inset-0 bg-gradient-to-br from-[var(--lh-navy)]/58 via-[#1a1528]/42 to-[var(--lh-navy)]/52 z-10"></div>
      <div class="absolute inset-0 bg-gradient-to-t from-black/28 via-transparent to-amber-900/10 z-[11] pointer-events-none"></div>

      <img
        :src="LOGIN_HERO_IMAGE"
        alt="洛裳汉服 · 唐风服饰意境"
        class="absolute inset-0 w-full h-full object-cover auth-bg-image opacity-70"
        loading="eager"
        decoding="async"
      />

      <div class="relative z-20 text-white text-center flex flex-col items-center px-8 lg:translate-y-10">
        <div class="mb-10 animate__animated animate__fadeInDown">
          <h2 class="hero-title auth-hero-title">洛裳汉服</h2>
        </div>

        <p class="hero-subtitle auth-hero-subtitle">洛阳汉服预约平台</p>
        <div class="hero-divider auth-hero-divider"></div>
        <p class="hero-info auth-hero-info">线上预约 · 到店体验 · 订单管理</p>
      </div>

    
    </aside>

    <main class="w-full lg:w-1/2 flex items-center justify-center p-6 sm:p-10 lg:p-16 xl:p-24 relative">
      <div class="auth-paper-bg"></div>
      <div class="auth-tiles-bg"></div>

      <div class="max-w-[26rem] w-full auth-card-container z-10">
        <section class="auth-card">
          <div class="auth-card-side-line"></div>
          <div class="auth-card-top-line"></div>

          <header class="text-center mb-10 relative z-10 pt-1">
            <p class="auth-card-tag">用户登录</p>
            <router-link to="/" class="inline-block group">
              <h1 class="auth-card-title group-hover:text-[var(--lh-red)] transition-colors">欢迎登录</h1>
            </router-link>
            <p class="auth-card-sub">请输入账号信息</p>
          </header>

          <el-tabs v-model="activeTab" class="heritage-tabs mb-2">
            <el-tab-pane label="手机号登录" name="phone">
              <PhoneLoginForm v-if="activeTab === 'phone'" />
            </el-tab-pane>
            <el-tab-pane label="账号登录" name="password">
              <PasswordLoginForm v-if="activeTab === 'password'" />
            </el-tab-pane>
          </el-tabs>

          <footer class="auth-card-footer">
            <span>还没有账号？</span>
            <router-link to="/register" class="text-[var(--lh-red)] font-black tracking-[0.15em] hover:tracking-[0.22em] transition-all">
              立即注册 →
            </router-link>
          </footer>

          <div class="auth-watermark" aria-hidden="true">洛</div>
        </section>
      </div>
    </main>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import PhoneLoginForm from '@/components/auth/PhoneLoginForm.vue'
import PasswordLoginForm from '@/components/auth/PasswordLoginForm.vue'
import gsap from 'gsap'

const LOGIN_HERO_IMAGE =
  '/images/login-bg.png'

const activeTab = ref('phone')

onMounted(() => {
  const timeline = gsap.timeline()

  gsap.to('.auth-bg-image', {
    scale: 1.08,
    duration: 48,
    ease: 'sine.inOut',
    repeat: -1,
    yoyo: true
  })

  timeline
    .from('.auth-hero-title', { y: 100, opacity: 0, duration: 1.4, ease: 'expo.out' })
    .from('.auth-hero-subtitle', { y: 45, opacity: 0, duration: 1.1, ease: 'expo.out' }, '-=0.95')
    .from('.auth-hero-divider', { scaleX: 0, duration: 1.1, ease: 'power4.inOut' }, '-=0.7')
    .from('.auth-hero-info', { y: 16, opacity: 0, duration: 1.0 }, '-=0.65')

  gsap.from('.auth-card-container', {
    x: 72,
    opacity: 0,
    duration: 1.7,
    ease: 'expo.out',
    delay: 0.2
  })
})
</script>

<style scoped>
.auth-page {
  background-attachment: fixed;
}

.hero-badge {
  width: 5.25rem;
  height: 5.25rem;
  border-radius: 9999px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 1.5rem;
  box-shadow: 0 0 40px rgba(180, 83, 9, 0.35);
  border: 2px solid rgba(180, 83, 9, 0.38);
  background: rgba(15, 23, 42, 0.3);
  backdrop-filter: blur(4px);
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
  letter-spacing: 0.55em;
  font-size: 1.05rem;
  font-weight: 900;
}

.hero-divider {
  width: 9rem;
  height: 1px;
  margin: 3rem 0 2rem;
  background: linear-gradient(to right, transparent, var(--lh-gold), transparent);
}

.hero-info {
  color: rgba(255, 255, 255, 0.75);
  font-size: 0.875rem;
  letter-spacing: 0.35em;
}

.auth-paper-bg,
.auth-tiles-bg {
  position: absolute;
  inset: 0;
  pointer-events: none;
}

.auth-paper-bg {
  opacity: 0.055;
  background-image: url('https://www.transparenttextures.com/patterns/handmade-paper.png');
}

.auth-tiles-bg {
  opacity: 0.035;
  background-image: url('https://www.transparenttextures.com/patterns/oriental-tiles.png');
}

.auth-card {
  position: relative;
  overflow: hidden;
  border-radius: 1.75rem;
  border: 1px solid rgba(180, 83, 9, 0.2);
  background: linear-gradient(to bottom, #fff, #fffcf8 45%, #fff8f0);
  padding: 2.5rem 2.5rem 2.75rem;
  box-shadow:
    inset 0 1px 0 rgba(255, 255, 255, 0.85),
    0 32px 64px -24px rgba(15, 23, 42, 0.16);
}

.auth-card-side-line {
  position: absolute;
  left: 0;
  top: 0;
  bottom: 0;
  width: 3px;
  border-radius: 1.75rem 0 0 1.75rem;
  background: linear-gradient(to bottom, var(--lh-gold), rgba(153, 27, 27, 0.5), rgba(180, 83, 9, 0.8));
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
  letter-spacing: 0.55em;
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
  letter-spacing: 0.4em;
  text-transform: uppercase;
  opacity: 0.6;
}

.auth-card-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: 2.5rem;
  padding-top: 2rem;
  border-top: 1px dashed rgba(226, 232, 240, 0.8);
  font-size: 11px;
  font-weight: 700;
  letter-spacing: 0.2em;
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

.heritage-tabs :deep(.el-tabs__header) {
  margin-bottom: 1.25rem;
  border-bottom: none;
}

.heritage-tabs :deep(.el-tabs__nav-wrap) {
  padding: 4px;
  border-radius: 9999px;
  background: rgba(15, 23, 42, 0.06);
}

.heritage-tabs :deep(.el-tabs__nav-wrap::after) {
  display: none;
}

.heritage-tabs :deep(.el-tabs__nav) {
  width: 100%;
}

.heritage-tabs :deep(.el-tabs__item) {
  font-family: var(--font-serif);
  font-weight: 800;
  color: var(--lh-text-secondary);
  font-size: 13px;
  letter-spacing: 0.12em;
  transition: color 0.35s ease, background 0.35s ease, box-shadow 0.35s ease;
  flex: 1;
  text-align: center;
  height: 44px;
  line-height: 44px;
  padding: 0;
  border-radius: 9999px;
}

.heritage-tabs :deep(.el-tabs__item.is-active) {
  color: var(--lh-navy) !important;
  background: #fff;
  box-shadow: 0 4px 14px rgba(15, 23, 42, 0.08);
}

.heritage-tabs :deep(.el-tabs__active-bar) {
  display: none;
}

@media (max-width: 640px) {
  .auth-card {
    padding: 2rem 1.5rem 2.25rem;
  }

  .auth-card-footer {
    letter-spacing: 0.08em;
  }
}
</style>
