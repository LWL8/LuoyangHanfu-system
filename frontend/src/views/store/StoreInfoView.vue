<template>
  <div class="store-view min-h-screen bg-[var(--lh-cream)] pb-32 animate__animated animate__fadeIn">
    <HeaderNav />
    
    <!-- 顶部介绍 -->
    <div class="relative h-[400px] flex items-center justify-center overflow-hidden mb-20 bg-[var(--lh-navy)]">
       <img 
         src="https://images.unsplash.com/photo-1548036328-c9fa89d128fa?q=80&w=1920&auto=format&fit=crop" 
         class="absolute inset-0 w-full h-full object-cover opacity-30 transform scale-110" 
       />
       <div class="absolute inset-0 bg-gradient-to-b from-transparent to-[var(--lh-cream)]"></div>
       <div class="relative z-10 text-center px-6">
          <div class="inline-block px-4 py-1 border-2 border-[var(--lh-gold)] mb-6 animate__animated animate__zoomIn">
            <span class="text-[9px] font-black text-[var(--lh-gold)] tracking-[0.08em]">门店网络</span>
          </div>
          <h1 class="text-5xl md:text-6xl font-serif font-black text-[var(--lh-navy)] mb-6 tracking-[0.1em] shadow-white text-shadow-sm">洛裳门店信息</h1>
          <p class="text-sm text-[var(--lh-text-secondary)] font-serif font-bold tracking-[0.06em] opacity-70">查看各门店地址、营业时间与可用设施</p>
       </div>
    </div>

    <div class="max-w-7xl mx-auto px-6">
      <div v-if="stores.length" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-10">
        <div 
          v-for="s in stores" 
          :key="s.id"
          class="store-card group card-premium bg-white group overflow-hidden transition-all duration-700 hover:-translate-y-4 cursor-pointer relative"
          @click="showDetail(s)"
        >
          <!-- 门店主图 (Heritage Frame) -->
          <div class="h-64 relative overflow-hidden">
             <img :src="s.images[0]" class="w-full h-full object-cover group-hover:scale-125 transition-transform duration-1000 ease-out" />
             <div class="absolute inset-0 bg-gradient-to-t from-[var(--lh-navy)]/60 via-transparent to-transparent opacity-60 group-hover:opacity-20 transition-all"></div>
             
             <div class="absolute top-6 left-6">
                <span class="px-4 py-1.5 bg-[var(--lh-gold)] text-white text-[9px] font-black tracking-widest uppercase rounded-full shadow-xl">
                   {{ s.distance || '附近门店' }}
                </span>
             </div>
             
             <div class="absolute bottom-6 right-6 w-12 h-12 bg-white/20 backdrop-blur-md rounded-full flex items-center justify-center text-white border border-white/20 opacity-0 group-hover:opacity-100 transition-all transform translate-y-4 group-hover:translate-y-0">
               <el-icon size="20"><View /></el-icon>
             </div>
          </div>

          <!-- 门店简述 (Scroll Layout) -->
          <div class="p-10 relative">
             <!-- 装饰水印 -->
             <div class="absolute bottom-4 right-4 text-7xl font-serif font-black text-[var(--lh-navy)] opacity-[0.02] select-none pointer-events-none group-hover:opacity-[0.05] transition-all italic">工</div>
             
             <div class="flex justify-between items-start mb-6">
                <h3 class="text-2xl font-serif font-black text-[var(--lh-navy)] tracking-[0.06em] group-hover:text-[var(--lh-red)] transition-colors line-clamp-1">{{ s.name }}</h3>
             </div>
             
             <div class="space-y-4 mb-10">
                <p class="text-[11px] text-[var(--lh-text-secondary)] flex items-center gap-3 font-bold opacity-60 group-hover:opacity-100 transition-opacity">
                   <el-icon class="text-[var(--lh-red)]" size="14"><Location /></el-icon> {{ s.address }}
                </p>
                <div class="flex flex-wrap gap-2 pt-2">
                   <span 
                    v-for="tag in s.facilities.slice(0, 3)" 
                    :key="tag" 
                    class="px-3 py-1 bg-[var(--lh-cream)] text-[9px] font-black text-[var(--lh-text-secondary)] border border-[var(--lh-border)] rounded-lg tracking-[0.04em]"
                   >
                      {{ tag }}
                   </span>
                </div>
             </div>

             <div class="pt-6 border-t border-[var(--lh-border)] border-dashed flex justify-between items-center">
                <span class="text-[9px] text-[var(--lh-gold)] font-black tracking-[0.06em] opacity-50">门店编号：0{{ s.id }}</span>
                <span class="text-10px font-serif font-black text-[var(--lh-red)] tracking-[0.06em] group-hover:mr-2 transition-all">查看详情 →</span>
             </div>
          </div>
        </div>
      </div>
      <el-empty v-else description="暂无门店信息" :image-size="200" class="card-premium py-20" />
    </div>

    <!-- 门店详情抽屉 -->
    <el-drawer
      v-model="drawerVisible"
      direction="rtl"
      size="600px"
      :show-close="false"
      class="heritage-drawer"
    >
      <div v-if="activeStore" class="h-full flex flex-col bg-white">
        <!-- 头部 Banner Overlay -->
        <div class="h-80 relative overflow-hidden shrink-0">
           <!-- 图片轮播 -->
           <swiper
             :modules="[Pagination, Autoplay]"
             :pagination="{ clickable: true }"
             :autoplay="{ delay: 5000 }"
             class="h-full heritage-swiper"
           >
             <swiper-slide v-for="img in activeStore.images" :key="img">
               <img :src="img" class="w-full h-full object-cover" />
             </swiper-slide>
           </swiper>
           <div class="absolute inset-0 bg-gradient-to-t from-white via-transparent to-black/20 pointer-events-none"></div>
           
           <button 
             class="absolute top-10 right-10 z-[100] w-12 h-12 rounded-full flex items-center justify-center bg-white/10 backdrop-blur-xl border border-white/20 text-white hover:bg-[var(--lh-red)] hover:border-[var(--lh-red)] transition-all transform hover:rotate-90"
             @click="drawerVisible = false"
           >
             <el-icon size="24"><CloseBold /></el-icon>
           </button>
        </div>

        <div class="flex-1 overflow-y-auto overflow-x-hidden p-12 lg:p-16 relative">
            <div class="absolute top-0 left-0 w-2 h-full bg-[var(--lh-navy)]"></div>
            
            <div class="inline-block px-4 py-1 border-2 border-[var(--lh-gold)] mb-6">
              <span class="text-[9px] font-black text-[var(--lh-gold)] tracking-[0.06em]">门店详情</span>
            </div>
            
            <h2 class="text-4xl font-serif font-black text-[var(--lh-navy)] mb-12 tracking-[0.06em] leading-tight">{{ activeStore.name }}</h2>

            <div class="space-y-12">
               <!-- 核心信息轴 (Data Scroll) -->
               <div class="grid grid-cols-1 gap-8">
                  <div class="flex items-start gap-6 group">
                     <div class="w-14 h-14 bg-[var(--lh-cream)] rounded-2xl flex items-center justify-center text-[var(--lh-red)] border border-[var(--lh-border)] shadow-xl group-hover:scale-110 transition-transform">
                        <el-icon size="24"><Shop /></el-icon>
                     </div>
                     <div>
                        <p class="text-[9px] text-[var(--lh-text-secondary)] font-black tracking-[0.06em] mb-2 opacity-40">营业时间</p>
                        <p class="text-lg font-serif font-black text-[var(--lh-navy)] tracking-[0.06em]">{{ activeStore.businessHours }}</p>
                     </div>
                  </div>
                  
                  <div class="flex items-start gap-6 group">
                     <div class="w-14 h-14 bg-[var(--lh-cream)] rounded-2xl flex items-center justify-center text-[var(--lh-gold)] border border-[var(--lh-border)] shadow-xl group-hover:scale-110 transition-transform">
                        <el-icon size="24"><Iphone /></el-icon>
                     </div>
                     <div>
                        <p class="text-[9px] text-[var(--lh-text-secondary)] font-black tracking-[0.06em] mb-2 opacity-40">联系电话</p>
                        <p class="text-lg font-mono font-black text-[var(--lh-navy)] tracking-[0.06em]">{{ activeStore.phone }}</p>
                     </div>
                  </div>

                  <div class="flex items-start gap-6 group">
                     <div class="w-14 h-14 bg-[var(--lh-cream)] rounded-2xl flex items-center justify-center text-[var(--lh-navy)] border border-[var(--lh-border)] shadow-xl group-hover:scale-110 transition-transform">
                        <el-icon size="24"><LocationFilled /></el-icon>
                     </div>
                     <div class="flex-1">
                        <p class="text-[9px] text-[var(--lh-text-secondary)] font-black tracking-[0.06em] mb-2 opacity-40">门店地址</p>
                        <p class="text-sm font-bold text-[var(--lh-navy)] mb-6 tracking-[0.04em] leading-loose">{{ activeStore.address }}</p>
                        <div class="flex gap-4">
                           <button class="px-6 py-2 border-2 border-[var(--lh-red)] rounded-xl text-[9px] font-black tracking-[0.06em] text-[var(--lh-red)] hover:bg-[var(--lh-red)] hover:text-white transition-all" @click="copyAddress(activeStore.address)">复制地址</button>
                           <button class="px-6 py-2 border-2 border-[var(--lh-navy)] rounded-xl text-[9px] font-black tracking-[0.06em] text-[var(--lh-navy)] hover:bg-[var(--lh-navy)] hover:text-white transition-all">导航到店</button>
                        </div>
                     </div>
                  </div>
               </div>

               <!-- 设施展示 (Imperial Grid) -->
               <div class="p-10 bg-[var(--lh-cream)] rounded-[40px] border border-[var(--lh-border)] relative overflow-hidden">
                  <div class="absolute -top-4 -right-4 opacity-[0.03]">
                     <el-icon size="120" class="text-[var(--lh-navy)]"><Key /></el-icon>
                  </div>
                  <h4 class="text-xs font-serif font-black text-[var(--lh-navy)] mb-8 flex items-center gap-3 tracking-[0.08em]">
                     <span class="w-2 h-6 bg-[var(--lh-gold)] rounded-full"></span> 门店设施
                  </h4>
                  <div class="grid grid-cols-2 gap-y-6 gap-x-8">
                     <div v-for="f in activeStore.facilities" :key="f" class="flex items-center gap-3 text-[10px] font-black text-[var(--lh-text-secondary)] tracking-[0.04em] opacity-80 group/item">
                        <el-icon color="var(--lh-red)" class="group-hover/item:scale-125 transition-transform"><CircleCheckFilled /></el-icon>
                        {{ f }}
                     </div>
                  </div>
               </div>

               <!-- 行为按钮 (Execution Trigger) -->
               <div class="pt-10 flex flex-col items-center gap-6">
                  <button 
                    class="btn-premium w-full py-6 text-sm tracking-[0.08em] font-serif shadow-[0_30px_60px_rgba(0,0,0,0.2)]"
                    @click="$router.push('/booking')"
                  >
                    立即预约
                  </button>
                  <p class="text-[9px] text-[var(--lh-text-secondary)] font-black tracking-[0.04em] opacity-50">建议先确认门店营业时间后再预约</p>
               </div>
            </div>
        </div>
      </div>
    </el-drawer>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { CloseBold, Location, LocationFilled, Shop, Iphone, CircleCheckFilled, View, Key } from '@element-plus/icons-vue'
import HeaderNav from '@/components/common/HeaderNav.vue'
import { Swiper, SwiperSlide } from 'swiper/vue'
import { Pagination, Autoplay } from 'swiper/modules'
import 'swiper/css'
import 'swiper/css/pagination'
import gsap from 'gsap'
import { ElMessage } from 'element-plus'
import { fetchStores, type StoreItem } from '@/api/store'

const drawerVisible = ref(false)
const activeStore = ref<StoreItem | null>(null)

const stores = ref<StoreItem[]>([])

const showDetail = (s: StoreItem) => {
  activeStore.value = s
  drawerVisible.value = true
}

const copyAddress = (addr: string) => {
  navigator.clipboard.writeText(addr)
  ElMessage.success('地址复制成功')
}

onMounted(async () => {
  try {
    const res: any = await fetchStores()
    const list = Array.isArray(res) ? res : (Array.isArray(res?.data) ? res.data : [])
    stores.value = list
  } catch {
    // Error
  }
  gsap.from('.store-card', {
    y: 50,
    opacity: 0,
    stagger: 0.2,
    duration: 1,
    ease: 'expo.out'
  })
})
</script>

<style scoped>
.store-card {
  will-change: transform;
}

:deep(.heritage-drawer) {
  border-radius: 40px 0 0 40px !important;
  overflow: hidden;
}

:deep(.heritage-drawer .el-drawer__body) {
  padding: 0;
}

.heritage-swiper :deep(.swiper-pagination-bullet-active) {
  background: var(--lh-red) !important;
}
</style>
