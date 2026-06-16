<template>
  <div v-bind="$attrs">
    <header class="sticky top-0 z-[100] border-b border-[var(--lh-border)] bg-white/80 backdrop-blur-3xl shadow-[0_5px_30px_rgba(0,0,0,0.02)] h-20 flex items-center transition-all duration-500">
      <div class="w-full px-6 md:px-8 grid grid-cols-[auto_1fr_auto] items-center">
        <!-- Logo -->
        <router-link to="/home" class="flex items-center gap-3 group">
          <div class="w-10 h-10 bg-[var(--lh-navy)] rounded-xl flex items-center justify-center text-white shadow-xl group-hover:bg-[var(--lh-red)] group-hover:rotate-12 transition-all duration-500">
             <span class="text-xl font-black font-serif">洛</span>
          </div>
          <div class="flex flex-col">
             <span class="text-lg font-serif font-black text-[var(--lh-navy)] tracking-[0.08em] group-hover:text-[var(--lh-red)] transition-colors">洛裳汉服</span>
             <span class="text-[10px] text-[var(--lh-text-secondary)] opacity-55 tracking-[0.04em] group-hover:opacity-80 transition-opacity">洛阳汉服预约</span>
          </div>
        </router-link>

        <!-- 主导航 -->
        <nav class="hidden lg:flex items-center justify-center gap-7 px-6 min-w-0">
          <router-link to="/home" class="nav-link">首页</router-link>
          <router-link to="/hanfu" class="nav-link">汉服列表</router-link>
          <router-link to="/booking" class="nav-link">在线预约</router-link>
          <router-link to="/luoyang/guides" class="nav-link">洛阳攻略</router-link>
        </nav>

        <!-- 右侧 -->
        <div class="flex items-center justify-end gap-4 md:gap-5 shrink-0">
          <!-- 搜索 -->
          <div class="hidden md:flex items-center search-inline">
            <el-input
              v-model="searchKeyword"
              placeholder="搜索汉服或朝代"
              clearable
              class="search-input"
              @keydown.enter="goSearch"
            >
              <template #prefix>
                <el-icon class="text-[var(--lh-text-secondary)]"><Search /></el-icon>
              </template>
            </el-input>
            <button class="search-btn" @click="goSearch">搜索</button>
          </div>
          
          <!-- 通知 -->
          <button
            v-if="user.token"
            class="relative cursor-pointer hover:text-[var(--lh-red)] text-[var(--lh-navy)] transition-all p-2 group"
            @click="isNotiDrawerShow = true"
          >
            <el-badge :value="notiStore.unreadCount" :hidden="notiStore.unreadCount === 0" :max="99" class="noti-badge">
              <el-icon class="text-xl group-hover:rotate-12 transition-transform"><Bell /></el-icon>
            </el-badge>
          </button>

          <!-- 个人中心 -->
          <el-dropdown v-if="user.token" placement="bottom-end" class="profile-dropdown">
            <div class="cursor-pointer flex items-center p-1 rounded-full border-2 border-transparent hover:border-[var(--lh-border)] transition-all">
              <el-avatar :size="38" :src="avatarUrl" class="border-2 border-white shadow-lg shadow-black/5" />
            </div>
            <template #dropdown>
              <el-dropdown-menu class="heritage-dropdown-menu profile-menu">
                 <div class="px-4 py-3 border-b border-[var(--lh-border)]/40 mb-1">
                    <p class="text-[10px] text-[var(--lh-text-secondary)] tracking-[0.03em] opacity-70 mb-1">当前用户</p>
                    <p class="text-xl font-serif font-black text-[var(--lh-navy)] leading-none">{{ user.userInfo?.yonghuName }}</p>
                 </div>
                <el-dropdown-item class="heritage-item" @click="$router.push('/profile')">个人中心</el-dropdown-item>
                <el-dropdown-item class="heritage-item" @click="$router.push('/order')">我的订单</el-dropdown-item>
                <el-dropdown-item v-if="user.isAdmin" class="heritage-item !text-[var(--lh-gold)] font-black" @click="$router.push('/admin')">后台管理</el-dropdown-item>
                <el-dropdown-item divided class="heritage-item logout-item" @click="user.logout()">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
          
          <!-- 未登录 -->
          <button 
            v-else 
            class="btn-premium px-8 py-3 !text-xs tracking-[0.1em]" 
            @click="$router.push('/login')"
          >
            登录
          </button>

          <!-- 移动端菜单按钮 -->
          <button class="lg:hidden p-2 text-[var(--lh-navy)]" @click="isMobileMenuShow = true">
             <el-icon size="24"><Menu /></el-icon>
          </button>
        </div>
      </div>
    </header>

    <!-- 移动端侧栏 -->
    <el-drawer
      v-model="isMobileMenuShow"
      direction="ltr"
      size="75%"
      :show-close="false"
      class="mobile-heritage-drawer"
    >
       <div class="p-10 h-full flex flex-col">
          <div class="mb-16">
             <h2 class="text-3xl font-serif font-black text-[var(--lh-navy)] tracking-[0.12em] mb-2">洛裳汉服</h2>
             <p class="text-[9px] text-[var(--lh-gold)] font-bold tracking-[0.08em]">洛阳汉服预约</p>
          </div>
          <nav class="flex-1 flex flex-col gap-6">
             <router-link v-for="link in navLinks" :key="link.path" :to="link.path" class="mobile-nav-link" @click="isMobileMenuShow = false">
               {{ link.title }}
             </router-link>
             <div class="h-px bg-[var(--lh-border)]/20 my-2"></div>
             <router-link to="/agreement" class="text-sm font-bold text-[var(--lh-text-secondary)]" @click="isMobileMenuShow = false">用户协议</router-link>
             <router-link to="/privacy" class="text-sm font-bold text-[var(--lh-text-secondary)]" @click="isMobileMenuShow = false">隐私政策</router-link>
          </nav>
          <div class="pt-20 border-t border-[var(--lh-border)]/30 opacity-50">
             <p class="text-[10px] text-[var(--lh-text-secondary)] tracking-[0.04em]">客服服务：0379-88888888 (09:00-21:00)</p>
          </div>
       </div>
    </el-drawer>

    <!-- 消息通知侧边栏 -->
    <el-drawer
      v-model="isNotiDrawerShow"
      direction="rtl"
      size="420px"
      :with-header="false"
      class="noti-side-drawer"
    >
      <div class="h-[calc(100%-16px)] mt-2 mx-2 overflow-hidden border border-[var(--lh-border)] bg-white/95 backdrop-blur-2xl flex flex-col">
        <div class="px-6 pt-7 pb-5 border-b border-[var(--lh-border)] bg-[var(--lh-cream)]/50 flex justify-between items-center">
          <span class="text-xs font-serif font-black text-[var(--lh-navy)] tracking-[0.06em]">消息通知</span>
          <button
            v-if="notiStore.unreadCount > 0"
            class="text-[10px] font-bold text-[var(--lh-red)] tracking-[0.04em] hover:underline"
            @click="notiStore.markAllAsRead()"
          >
            全部已读
          </button>
        </div>
        <div class="flex-1 overflow-y-auto">
          <div v-if="notiStore.notifications.length === 0" class="py-16 text-center text-gray-400">
            <el-icon size="60" class="opacity-10 mb-4"><ChatLineRound /></el-icon>
            <p class="text-[11px] text-[var(--lh-text-secondary)] tracking-[0.04em] opacity-80">暂无新消息</p>
          </div>
          <div
            v-for="n in notiStore.notifications.slice(0, 8)"
            :key="n.id"
            class="px-6 py-5 hover:bg-[var(--lh-cream)] transition-all cursor-pointer border-b border-[var(--lh-border)]/30 last:border-none group/item"
            @click="handleNotiClick(n)"
          >
            <div class="flex items-start gap-4 mb-2">
              <span v-if="!n.read" class="w-2 h-2 rounded-full bg-[var(--lh-red)] mt-1.5 shadow-[0_0_10px_var(--lh-red)]"></span>
              <div class="flex-1">
                <h4 class="text-sm font-serif font-black text-[var(--lh-navy)] tracking-wider group-hover/item:text-[var(--lh-red)] transition-colors">{{ n.title }}</h4>
                <p class="text-[11px] text-[var(--lh-text-secondary)] font-medium line-clamp-2 mt-2 opacity-75 leading-relaxed">{{ n.content }}</p>
              </div>
            </div>
            <div class="flex justify-between items-center mt-4">
              <span class="text-[9px] text-[var(--lh-text-secondary)] font-serif font-black tracking-widest opacity-45">{{ n.time }}</span>
              <el-tag size="small" :type="n.type === 'order' ? 'danger' : 'info'" effect="plain" class="!bg-transparent !border-[var(--lh-border)] !text-[var(--lh-text-secondary)] scale-75 opacity-40">{{ n.type === 'order' ? '订单' : '通知' }}</el-tag>
            </div>
          </div>
        </div>
        <div v-if="notiStore.notifications.length > 0" class="p-5 border-t border-[var(--lh-border)]/30 text-center">
          <button
            class="text-[10px] font-bold text-[var(--lh-gold)] tracking-[0.06em] hover:opacity-90 transition-all"
            @click="$router.push('/profile'); isNotiDrawerShow = false"
          >
            查看全部 →
          </button>
        </div>
      </div>
    </el-drawer>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { Search, Bell, ChatLineRound, Menu } from '@element-plus/icons-vue'
import useUserStore from '@/stores/user'
import { useNotificationStore } from '@/stores/notification'
import { useRouter } from 'vue-router'

const user = useUserStore()
const notiStore = useNotificationStore()
const router = useRouter()
const searchKeyword = ref('')
const avatarUrl = computed(() => user.userInfo?.yonghuPhoto || user.DEFAULT_AVATAR)

const isMobileMenuShow = ref(false)
const isNotiDrawerShow = ref(false)

const navLinks = [
  { title: '首页', path: '/home' },
  { title: '汉服列表', path: '/hanfu' },
  { title: '在线预约', path: '/booking' },
  { title: '洛阳攻略', path: '/luoyang/guides' }
]

const handleNotiClick = (n: any) => {
  notiStore.markAsRead(n.id)
  isNotiDrawerShow.value = false
  if (n.type === 'order' || n.type === 'payment') {
    router.push('/order')
  }
}

const goSearch = () => {
  const keyword = searchKeyword.value.trim()
  router.push({
    path: '/hanfu',
    query: keyword ? { keyword } : {}
  })
}

onMounted(() => {
  if (user.token) {
    user.syncUserProfile()
  }
})
</script>

<style scoped>
.nav-link {
   font-family: var(--font-serif);
   font-weight: 900;
   font-size: 12px;
   letter-spacing: 0.04em;
   color: var(--lh-navy);
   opacity: 0.6;
   transition: all 0.5s cubic-bezier(0.4, 0, 0.2, 1);
   position: relative;
   white-space: nowrap;
}
.nav-link:hover {
   opacity: 1;
   color: var(--lh-red);
   transform: translateY(-2px) scale(1.05);
}

.router-link-active.nav-link {
   opacity: 1;
   color: var(--lh-red);
}
.router-link-active.nav-link::after {
   content: '';
   position: absolute;
   bottom: -8px;
   left: 0;
   width: 100%;
   height: 3px;
   background: var(--lh-red);
   border-radius: 2px;
   box-shadow: 0 4px 10px rgba(189, 49, 44, 0.3);
}

.mobile-nav-link {
   font-family: var(--font-serif);
   font-size: 24px;
   font-weight: 900;
   letter-spacing: 0.08em;
   color: var(--lh-navy);
   transition: all 0.4s;
}
.mobile-nav-link:hover {
   color: var(--lh-red);
   padding-left: 10px;
}

.search-inline {
   gap: 8px;
   padding: 4px;
   border: 1px solid var(--lh-border);
   border-radius: 999px;
   background: rgba(255, 255, 255, 0.85);
   box-shadow: 0 8px 24px rgba(22, 35, 58, 0.06);
}

.search-input {
   width: 200px;
}

.search-input :deep(.el-input__wrapper) {
   box-shadow: none !important;
   background: transparent !important;
   border-radius: 999px;
}

.search-input :deep(.el-input__inner) {
   font-size: 12px;
   letter-spacing: 0.02em;
}

.search-btn {
   height: 34px;
   min-width: 64px;
   border-radius: 999px;
   background: var(--lh-red);
   color: #fff;
   font-size: 12px;
   font-weight: 700;
   letter-spacing: 0.04em;
   padding: 0 14px;
   transition: all 0.25s ease;
}

.search-btn:hover {
   background: var(--lh-navy);
}

:deep(.heritage-dropdown-menu) {
   border-radius: 24px !important;
   padding: 8px !important;
   border: 1px solid var(--lh-border) !important;
   box-shadow: 0 30px 60px rgba(0,0,0,0.1) !important;
}

:deep(.profile-menu) {
   min-width: 208px;
}

:deep(.heritage-item) {
   font-family: var(--font-serif);
   font-weight: 900;
   font-size: 15px;
   letter-spacing: 0.04em;
   line-height: 1.2;
   padding: 14px 14px !important;
   border-radius: 10px;
   color: var(--lh-navy);
   transition: all 0.3s;
   margin: 2px 0;
}
:deep(.heritage-item:hover) {
   background: var(--lh-cream) !important;
   color: var(--lh-red) !important;
}

:deep(.logout-item) {
   color: var(--lh-red) !important;
   opacity: 0.75;
}

.noti-badge :deep(.el-badge__content) {
   background-color: var(--lh-red);
   border: 2px solid white;
   font-family: var(--font-mono);
   font-weight: 900;
}
</style>
