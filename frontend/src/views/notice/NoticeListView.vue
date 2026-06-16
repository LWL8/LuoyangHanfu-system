<template>
  <div class="notice-list-view min-h-screen bg-[var(--lh-cream)] pb-32 animate__animated animate__fadeIn">
    <HeaderNav />
    
    <!-- 艺术横幅 (Page Banner) -->
    <section class="banner-section relative h-[440px] md:h-[520px] overflow-hidden flex items-center pt-24 mb-16">
      <div class="absolute inset-0 z-0">
        <img src="https://images.unsplash.com/photo-1578301978255-73892f39281a?w=1600" class="w-full h-full object-cover brightness-50 contrast-125" />
        <div class="absolute inset-0 bg-gradient-to-b from-[var(--lh-navy)]/60 via-transparent to-[var(--lh-cream)]"></div>
      </div>
      <div class="container relative z-10 text-center max-w-4xl mx-auto px-6">
        <div class="animate__animated animate__fadeInDown">
          <span class="text-[var(--lh-gold)] font-bold tracking-[0.4em] text-xs uppercase mb-4 block">Official Notices</span>
          <h1 class="text-5xl md:text-7xl font-serif font-black text-white tracking-[0.2em] mb-8 drop-shadow-2xl">公告中心</h1>
          <div class="w-20 h-1 bg-[var(--lh-red)] mx-auto mb-10 shadow-lg"></div>
          <p class="text-sm md:text-lg text-white/90 tracking-[0.2em] leading-relaxed italic font-medium drop-shadow-lg text-center">
             为您传递最新的预约必读、汉服科普以及店内活动快讯。
             <span class="block mt-4 font-serif text-[var(--lh-gold)]">— 官方通知与活动信息 —</span>
          </p>
        </div>
      </div>
    </section>

    <div class="w-full px-6 md:px-16 pb-20">

      <!-- 搜索与分类 -->
      <div class="flex flex-col md:flex-row justify-between items-center gap-10 mb-16 relative z-10">
         <el-tabs v-model="activeTab" class="heritage-tabs" @tab-change="handleTabChange">
            <el-tab-pane label="全部" name="all" />
            <el-tab-pane label="预约必读" name="rule" />
            <el-tab-pane label="汉服科普" name="science" />
            <el-tab-pane label="活动快讯" name="activity" />
         </el-tabs>
         <div class="flex gap-4 w-full md:w-auto">
            <el-input 
              v-model="searchQuery" 
              placeholder="搜索公告关键词" 
              class="premium-search !w-full md:!w-72" 
              :prefix-icon="Search" 
              clearable
            />
         </div>
      </div>

      <!-- 公告列表 -->
      <div v-loading="loading" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-10">
         <div 
           v-for="n in filteredNotices" 
           :key="n.id"
           class="notice-card card-premium bg-white group flex flex-col sm:flex-row h-auto sm:h-52 overflow-hidden transition-all duration-700 hover:-translate-y-3 cursor-pointer"
           @click="showDetail(n)"
         >
           <!-- 图片区 (Heritage Frame) -->
           <div class="w-full sm:w-48 h-48 sm:h-full overflow-hidden shrink-0 relative">
              <img :src="n.cover || 'https://images.unsplash.com/photo-1543854589-32a26569eb21?w=400'" class="w-full h-full object-cover transition-transform duration-1000 group-hover:scale-110" />
              <div class="absolute inset-0 bg-gradient-to-r from-transparent to-white/10"></div>
              <!-- 装饰遮罩 -->
              <div class="absolute inset-x-0 bottom-0 p-4 bg-black/20 backdrop-blur-sm sm:hidden text-center">
                 <span class="text-[9px] font-black text-white tracking-[0.06em]">{{ typeConfig[n.type].label }}</span>
              </div>
           </div>
           
           <!-- 文字区 (Content Layout) -->
           <div class="p-8 flex-1 flex flex-col justify-between relative border-l-8 border-l-transparent group-hover:border-l-[var(--lh-red)] transition-all duration-500">
              <div>
                <div class="flex items-center justify-between mb-4">
                   <span class="text-[10px] text-[var(--lh-gold)] font-black tracking-[0.06em]">{{ typeConfig[n.type].label }}</span>
                   <span class="text-[9px] text-[var(--lh-text-secondary)] font-bold tracking-[0.04em] opacity-50">{{ n.publishTime }}</span>
                </div>
                <h3 class="text-xl font-serif font-black text-[var(--lh-navy)] line-clamp-1 group-hover:text-[var(--lh-red)] transition-colors tracking-[0.06em] mb-3">{{ n.title }}</h3>
                <p class="text-[11px] text-[var(--lh-text-secondary)] font-medium line-clamp-2 leading-relaxed opacity-70 group-hover:opacity-100 transition-opacity">{{ n.content.replace(/<[^>]*>?/gm, '') }}</p>
              </div>
              
              <div class="flex items-center justify-between pt-6 border-t border-[var(--lh-border)] border-dashed">
                 <div class="flex items-center gap-4 text-[9px] text-[var(--lh-text-secondary)] font-black tracking-[0.06em]">
                    <span class="flex items-center gap-1.5"><el-icon size="12"><View /></el-icon> 阅读 {{ n.viewCount }}</span>
                 </div>
                 <span class="text-[10px] font-serif font-black text-[var(--lh-red)] tracking-[0.06em] group-hover:mr-2 transition-all">查看全文 →</span>
              </div>
           </div>
         </div>
      </div>

      <!-- 空状态 -->
      <el-empty v-if="!loading && filteredNotices.length === 0" description="暂无公告内容" />
    </div>

    <!-- 详情弹窗 -->
    <el-dialog
      v-model="dialogVisible"
      width="850px"
      class="heritage-dialog-full"
      :show-close="false"
      destroy-on-close
    >
      <div v-if="activeNotice" class="relative">
         <!-- 关闭按钮 -->
         <button 
           class="absolute top-8 right-8 z-[100] w-12 h-12 rounded-full flex items-center justify-center bg-white/10 backdrop-blur-xl border border-white/20 text-white hover:bg-[var(--lh-red)] hover:border-[var(--lh-red)] transition-all transform hover:rotate-90 active:scale-90"
           @click="dialogVisible = false"
         >
           <el-icon size="24"><CloseBold /></el-icon>
         </button>

         <div class="h-64 overflow-hidden relative">
            <img :src="activeNotice.cover || 'https://images.unsplash.com/photo-1543854589-32a26569eb21?w=800'" class="w-full h-full object-cover" />
            <div class="absolute inset-0 bg-gradient-to-b from-[var(--lh-navy)]/60 via-transparent to-white"></div>
            <div class="absolute bottom-10 left-12">
               <span class="px-4 py-1 bg-[var(--lh-red)] text-white text-[10px] font-black tracking-[0.06em] mb-4 inline-block">{{ typeConfig[activeNotice.type].label }}</span>
               <h2 class="text-4xl font-serif font-black text-[var(--lh-navy)] tracking-[0.06em]">{{ activeNotice.title }}</h2>
            </div>
         </div>

         <div class="p-12 lg:p-20 bg-white">
            <div class="flex items-center gap-6 text-[10px] text-[var(--lh-text-secondary)] font-black tracking-[0.06em] mb-16 pb-6 border-b border-[var(--lh-border)] opacity-60">
               <span>发布时间：{{ activeNotice.publishTime }}</span>
               <span>阅读量：{{ activeNotice.viewCount }}</span>
               <div class="h-1 w-1 rounded-full bg-[var(--lh-gold)]"></div>
               <span class="text-[var(--lh-gold)]">官方发布</span>
            </div>
            
            <div class="prose prose-lg prose-red max-w-none font-serif leading-[2.2] text-[var(--lh-navy)] opacity-90 first-letter:text-6xl first-letter:font-black first-letter:mr-4 first-letter:float-left first-letter:text-[var(--lh-red)] italic">
               <div v-html="activeNotice.content" class="rich-content"></div>
            </div>

            <div class="mt-20 pt-10 border-t border-[var(--lh-border)] flex justify-center">
               <button 
                class="btn-premium px-16 py-5 text-sm tracking-[0.08em] font-serif shadow-2xl"
                 @click="dialogVisible = false"
               >
                 我知道了
               </button>
            </div>
         </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { Search, View, CloseBold } from '@element-plus/icons-vue'
import HeaderNav from '@/components/common/HeaderNav.vue'
import gsap from 'gsap'
import { fetchNotices } from '@/api/notice'

const loading = ref(false)
const activeTab = ref<string>('all')
const searchQuery = ref('')
const dialogVisible = ref(false)
const activeNotice = ref<any>(null)

const typeConfig: Record<string, { label: string, tagType: any }> = {
  rule: { label: '预约必读', tagType: 'danger' },
  science: { label: '汉服科普', tagType: 'info' },
  activity: { label: '活动快讯', tagType: 'success' }
}

const notices = ref<any[]>([])

const filteredNotices = computed(() => {
  return notices.value.filter(n => {
    const matchType = activeTab.value === 'all' || n.type === activeTab.value
    const matchQuery = !searchQuery.value || n.title.includes(searchQuery.value)
    return matchType && matchQuery
  })
})

const handleTabChange = () => {
  gsap.from('.notice-card', {
     y: 30,
     opacity: 0,
     stagger: 0.1,
     duration: 0.8,
     ease: 'power3.out'
  })
}

const showDetail = (n: any) => {
  activeNotice.value = n
  dialogVisible.value = true
}

onMounted(async () => {
  loading.value = true
  try {
    const res: any = await fetchNotices()
    const list = Array.isArray(res) ? res : (Array.isArray(res?.data) ? res.data : [])
    notices.value = list
  } catch {
    // Error
  } finally {
    loading.value = false
  }
  gsap.from('.notice-card', {
     y: 50,
     opacity: 0,
     stagger: 0.15,
     duration: 1.2,
     ease: 'expo.out'
  })
})
</script>

<style scoped>
.notice-card:hover {
  box-shadow: 0 40px 80px -20px rgba(0,0,0,0.15);
}

.heritage-tabs :deep(.el-tabs__header) {
  margin-bottom: 0;
  border-bottom: none;
}
.heritage-tabs :deep(.el-tabs__nav-wrap::after) {
  display: none;
}
.heritage-tabs :deep(.el-tabs__item) {
   font-family: var(--font-serif);
   font-weight: 900;
   color: var(--lh-text-secondary);
   font-size: 14px;
   letter-spacing: 0.2em;
   transition: all 0.4s;
   padding: 0 30px;
}
.heritage-tabs :deep(.el-tabs__item.is-active) {
   color: var(--lh-red) !important;
   transform: scale(1.1);
}
.heritage-tabs :deep(.el-tabs__active-bar) {
   background-color: var(--lh-red);
   height: 4px;
   border-radius: 2px;
}

.premium-search :deep(.el-input__wrapper) {
  border-radius: 40px;
  background: white;
  border: 1px solid var(--lh-border);
  box-shadow: 0 10px 30px rgba(0,0,0,0.03);
  padding-left: 20px;
}

.rich-content :deep(ul) {
   list-style: none;
   padding-left: 0;
}
.rich-content :deep(li) {
   position: relative;
   padding-left: 24px;
   margin-bottom: 12px;
}
.rich-content :deep(li::before) {
   content: '•';
   position: absolute;
   left: 0;
   color: var(--lh-red);
   font-weight: 900;
}

:deep(.heritage-dialog-full) {
  background: white;
  border-radius: 40px;
  overflow: hidden;
  box-shadow: 0 50px 100px -20px rgba(0,0,0,0.3);
  padding: 0;
}
:deep(.heritage-dialog-full .el-dialog__header) {
  display: none;
}
:deep(.heritage-dialog-full .el-dialog__body) {
  padding: 0;
}
</style>
