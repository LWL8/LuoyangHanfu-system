<template>
  <el-container class="admin-layout min-h-screen">
    <el-aside width="220px" class="bg-[#0b2340] border-r border-white/10 shadow-xl z-50">
      <div class="flex flex-col h-full">
        <div class="h-16 flex items-center px-5 border-b border-white/10 relative overflow-hidden">
          <div class="w-8 h-8 bg-[#1d63b6] rounded-md flex items-center justify-center mr-3 shadow-lg relative z-10">
            <el-icon color="#fff" size="24"><Platform /></el-icon>
          </div>
          <div class="relative z-10">
            <h1 class="text-white font-black text-sm tracking-[0.06em]">后台管理系统</h1>
            <p class="text-[10px] text-white/60 tracking-[0.08em]">Luoyang Hanfu Admin</p>
          </div>
          <div class="absolute right-0 top-0 text-6xl text-white/5 font-black transform translate-x-1/2 -translate-y-1/2 select-none">洛</div>
        </div>

        <el-menu
          router
          :default-active="$route.path"
          background-color="transparent"
          text-color="rgba(255,255,255,0.6)"
          active-text-color="#fff"
          class="admin-menu flex-1 !border-none pt-4"
        >
          <el-menu-item v-if="can('dashboard:view')" index="/admin">
            <el-icon class="menu-icon"><TrendCharts /></el-icon>
            <template #title><span class="font-bold tracking-[0.08em]">运营看板</span></template>
          </el-menu-item>

          <el-menu-item v-if="can('banner:view')" index="/admin/banner">
            <el-icon class="menu-icon"><Picture /></el-icon>
            <template #title><span class="font-bold tracking-[0.08em]">轮播图管理</span></template>
          </el-menu-item>

          <el-menu-item v-if="can('recommend:view')" index="/admin/recommend">
            <el-icon class="menu-icon"><Star /></el-icon>
            <template #title><span class="font-bold tracking-[0.08em]">新品推荐</span></template>
          </el-menu-item>

          <el-menu-item v-if="can('order:view')" index="/admin/orders">
            <el-icon class="menu-icon"><Tickets /></el-icon>
            <template #title><span class="font-bold tracking-[0.08em]">订单流水</span></template>
          </el-menu-item>

          <el-menu-item v-if="can('reservation:view')" index="/admin/reservations">
            <el-icon class="menu-icon"><Memo /></el-icon>
            <template #title><span class="font-bold tracking-[0.08em]">预约订单管理</span></template>
          </el-menu-item>

          <el-menu-item v-if="can('hanfu:view')" index="/admin/hanfu">
            <el-icon class="menu-icon"><Box /></el-icon>
            <template #title><span class="font-bold tracking-[0.08em]">汉服管理</span></template>
          </el-menu-item>

          <el-menu-item v-if="can('user:view')" index="/admin/users">
            <el-icon class="menu-icon"><User /></el-icon>
            <template #title><span class="font-bold tracking-[0.08em]">用户管理</span></template>
          </el-menu-item>

          <el-menu-item v-if="can('service:view')" index="/admin/services">
            <el-icon class="menu-icon"><Tickets /></el-icon>
            <template #title><span class="font-bold tracking-[0.08em]">服务套餐</span></template>
          </el-menu-item>

          <el-menu-item v-if="can('guide:view')" index="/admin/guides">
            <el-icon class="menu-icon"><Compass /></el-icon>
            <template #title><span class="font-bold tracking-[0.08em]">洛阳攻略</span></template>
          </el-menu-item>
        </el-menu>

        <div class="p-4 border-t border-white/10 bg-black/10">
          <button
            class="w-full py-2 bg-white/5 border border-white/10 text-white font-bold text-xs tracking-[0.08em] rounded-md hover:bg-white hover:text-[#0b2340] transition-all"
            @click="$router.push('/')"
          >
            返回前台
          </button>
        </div>
      </div>
    </el-aside>

    <el-container class="flex-1 bg-[#f2f5f9]">
      <el-header height="56px" class="bg-white border-b border-[#e5e7eb] flex items-center justify-between px-5 sticky top-0 z-40">
        <el-breadcrumb separator="/">
          <el-breadcrumb-item class="font-serif font-black tracking-[0.08em] text-xs opacity-40">管理中心</el-breadcrumb-item>
          <el-breadcrumb-item class="font-serif font-black tracking-[0.08em] text-xs">{{ currentRouteName }}</el-breadcrumb-item>
        </el-breadcrumb>

        <div class="flex items-center gap-8">
          <el-badge is-dot class="cursor-pointer group">
            <el-icon size="22" class="text-[var(--lh-text-secondary)] group-hover:text-[var(--lh-navy)] transition-colors"><Bell /></el-icon>
          </el-badge>

          <div class="h-8 w-px bg-[var(--lh-border)]"></div>

          <el-dropdown>
            <div class="flex items-center gap-4 cursor-pointer group">
              <div class="text-right hidden sm:block">
                <p class="text-sm font-black text-slate-700 tracking-[0.04em]">{{ userStore.userInfo?.yonghuName || '管理员' }}</p>
                <p class="text-[10px] text-emerald-600 font-bold tracking-[0.08em]">在线</p>
              </div>
              <div class="relative">
                <el-avatar :size="36" :src="userStore.userInfo?.yonghuPhoto || userStore.DEFAULT_AVATAR" class="border border-white shadow group-hover:scale-105 transition-transform" />
                <div class="absolute -bottom-1 -right-1 w-4 h-4 bg-emerald-500 border-2 border-white rounded-full"></div>
              </div>
            </div>
            <template #dropdown>
              <el-dropdown-menu class="premium-dropdown">
                <el-dropdown-item @click="$router.push('/admin')">系统设置</el-dropdown-item>
                <el-dropdown-item divided @click="handleLogout" class="!text-[var(--lh-red)] font-bold">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <el-main class="p-0">
        <router-view v-slot="{ Component }">
          <transition name="heritage-fade" mode="out-in">
            <component :is="Component" :key="route.fullPath" />
          </transition>
        </router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { Platform, TrendCharts, Box, Tickets, User, Bell, Picture, Star, Memo, Compass, ChatDotRound, ChatLineRound } from '@element-plus/icons-vue'
import useUserStore from '@/stores/user'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const permissionMap: Record<string, string[]> = {
  SUPER_ADMIN: ['*'],
  ADMIN: [
    'dashboard:view',
    'banner:view',
    'recommend:view',
    'hanfu:view',
    'gallery:view',
    'order:view',
    'reservation:view',
    'order:update_status',
    'user:view',
    'service:view',
    'guide:view',
    'guide:view'
  ],
  admin: [
    'dashboard:view',
    'banner:view',
    'recommend:view',
    'hanfu:view',
    'gallery:view',
    'order:view',
    'reservation:view',
    'order:update_status',
    'user:view',
    'service:view',
    'guide:view'
  ]
}

const can = (perm: string) => {
  const role = String(userStore.userInfo?.role || 'ADMIN')
  const perms = permissionMap[role] || permissionMap.ADMIN
  return perms.includes('*') || perms.includes(perm)
}

const currentRouteName = computed(() => {
  const nameMap: Record<string, string> = {
    '/admin': '运营看板',
    '/admin/banner': '轮播图管理',
    '/admin/recommend': '新品推荐',
    '/admin/hanfu': '汉服库存',
    '/admin/orders': '订单流水',
    '/admin/reservations': '预约订单管理',
    '/admin/gallery': '素材图库',
    '/admin/users': '用户管理',
    '/admin/services': '服务套餐',
    '/admin/guides': '洛阳攻略管理',
  }
  return nameMap[route.path] || '控制台'
})

const handleLogout = () => {
  userStore.logout()
  router.push('/login')
}
</script>

<style scoped>
.admin-menu :deep(.el-menu-item) {
  height: 46px;
  margin: 4px 10px;
  border-radius: 8px;
  transition: all 0.3s ease;
  font-size: 13px;
}

.admin-menu :deep(.el-menu-item.is-active) {
  background-color: #1d63b6 !important;
  color: white !important;
  box-shadow: 0 10px 22px -14px rgba(29, 99, 182, 0.9);
}

.admin-menu :deep(.el-menu-item:hover:not(.is-active)) {
  background-color: rgba(255, 255, 255, 0.06) !important;
  color: var(--lh-gold) !important;
}

.admin-menu :deep(.el-sub-menu__title) {
  margin: 4px 10px;
  border-radius: 8px;
  height: 46px;
}

.sub-menu-item {
  padding-left: 42px !important;
  height: 42px !important;
  font-size: 12px !important;
  margin: 4px 10px !important;
}

.menu-icon {
  margin-right: 10px !important;
  font-size: 16px !important;
}

.el-menu-item.is-active .menu-icon {
  color: white !important;
}

.heritage-fade-enter-active,
.heritage-fade-leave-active {
  transition: all 0.35s ease;
}

.heritage-fade-enter-from {
  opacity: 0;
  transform: translateY(12px);
}

.heritage-fade-leave-to {
  opacity: 0;
  transform: translateY(-12px);
}

.premium-dropdown :deep(.el-dropdown-menu__item:hover) {
  background-color: var(--lh-cream) !important;
  color: var(--lh-navy) !important;
}

:global(.el-overlay.is-message-box) {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 16px;
}

:global(.el-overlay.is-message-box .centered-message-box) {
  margin-top: 0 !important;
  width: 420px;
  max-width: 92vw;
  border-radius: 12px;
  overflow: hidden;
}
</style>
