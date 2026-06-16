<template>
  <div class="step3-services animate__animated animate__fadeIn">
    
    <!-- 顶部引导 -->
    <div class="header-section text-center mb-16">
      <h2 class="text-4xl font-serif text-[var(--lh-navy)] tracking-[0.08em] relative inline-block">
        选择附加服务
        <span class="block h-0.5 w-16 bg-[var(--lh-red)] mx-auto mt-6"></span>
      </h2>
      <p class="text-[var(--lh-text-secondary)] mt-6 tracking-[0.04em] text-sm">按需选择妆造、跟拍和道具，费用会自动计入总价</p>
    </div>

    <!-- 服务网格 -->
    <div class="grid grid-cols-1 lg:grid-cols-3 gap-10">
      
      <!-- 1. 妆造服务 (Peony Red Theme) -->
      <div class="service-column">
        <div class="flex flex-col mb-10 border-l-4 border-[var(--lh-red)] pl-6">
          <h3 class="text-2xl font-serif font-bold text-[var(--lh-navy)]">专业妆造</h3>
          <span class="text-[10px] tracking-[0.08em] text-[var(--lh-text-secondary)] mt-1.5 font-bold">妆造服务</span>
        </div>
        <div class="space-y-6">
          <div v-if="makeupServices.length === 0" class="py-10 text-center border-2 border-dashed border-gray-100 rounded-2xl opacity-40">
            <el-icon size="32"><Operation /></el-icon>
            <p class="text-[10px] mt-2">暂无可用妆造服务</p>
          </div>
          <div 
            v-for="s in makeupServices" 
            :key="s.id"
            class="service-card-premium group"
            :class="isServiceSelected(s.id) ? 'active-red' : ''"
            @click="store.toggleService(s)"
          >
            <div class="relative w-20 h-20 shrink-0 overflow-hidden rounded-xl border border-white/20 shadow-md">
              <img :src="s.image" class="w-full h-full object-cover transition-transform duration-700 group-hover:scale-110" />
              <div class="absolute inset-0 bg-black/10"></div>
            </div>
            
            <div class="flex-1 flex flex-col justify-center">
              <div class="flex justify-between items-start mb-1">
                <span class="text-base font-serif font-bold text-[var(--lh-navy)] group-hover:text-[var(--lh-red)] transition-colors">{{ s.name }}</span>
                <div v-if="isServiceSelected(s.id)" class="w-5 h-5 bg-[var(--lh-red)] rounded-full flex items-center justify-center text-white scale-110 shadow-lg">
                  <el-icon size="12"><Check /></el-icon>
                </div>
              </div>
              <p class="text-[10px] text-[var(--lh-text-secondary)] tracking-[0.04em] opacity-70 mb-3">{{ s.subtitle }}</p>
              <div class="text-lg font-bold text-[var(--lh-red)]">¥{{ s.price }}</div>
            </div>
          </div>
        </div>
      </div>

      <!-- 2. 摄影跟拍 (Heritage Gold Theme) -->
      <div class="service-column">
        <div class="flex flex-col mb-10 border-l-4 border-[var(--lh-gold)] pl-6">
          <h3 class="text-2xl font-serif font-bold text-[var(--lh-navy)]">摄影跟拍</h3>
          <span class="text-[10px] tracking-[0.08em] text-[var(--lh-text-secondary)] mt-1.5 font-bold">摄影跟拍</span>
        </div>
        <div class="space-y-6">
          <div v-if="photographyServices.length === 0" class="py-10 text-center border-2 border-dashed border-gray-100 rounded-2xl opacity-40">
            <el-icon size="32"><Camera /></el-icon>
            <p class="text-[10px] mt-2">暂无摄影服务项</p>
          </div>
          <div 
            v-for="s in photographyServices" 
            :key="s.id"
            class="service-card-premium group"
            :class="isServiceSelected(s.id) ? 'active-gold' : ''"
            @click="store.toggleService(s)"
          >
            <div class="relative w-20 h-20 shrink-0 overflow-hidden rounded-xl border border-white/20 shadow-md">
              <img :src="s.image" class="w-full h-full object-cover transition-transform duration-700 group-hover:scale-110" />
            </div>
            
            <div class="flex-1 flex flex-col justify-center">
              <div class="flex justify-between items-start mb-1">
                <span class="text-base font-serif font-bold text-[var(--lh-navy)] group-hover:text-[var(--lh-gold)] transition-colors">{{ s.name }}</span>
                <div v-if="isServiceSelected(s.id)" class="w-5 h-5 bg-[var(--lh-gold)] rounded-full flex items-center justify-center text-white scale-110 shadow-lg">
                  <el-icon size="12"><Check /></el-icon>
                </div>
              </div>
              <p class="text-[10px] text-[var(--lh-text-secondary)] tracking-[0.04em] opacity-70 mb-3">{{ s.subtitle }}</p>
              <div class="text-lg font-bold text-[var(--lh-gold)]">¥{{ s.price }}</div>
            </div>
          </div>
        </div>
      </div>

      <!-- 3. 精选道具 (Navy Classic Theme) -->
      <div class="service-column">
        <div class="flex flex-col mb-10 border-l-4 border-gray-400 pl-6">
          <h3 class="text-2xl font-serif font-bold text-[var(--lh-navy)]">精选道具</h3>
          <span class="text-[10px] tracking-[0.08em] text-[var(--lh-text-secondary)] mt-1.5 font-bold">道具租用</span>
        </div>
        <div class="grid grid-cols-2 gap-6">
          <div v-if="propServices.length === 0" class="col-span-2 py-10 text-center border-2 border-dashed border-gray-100 rounded-2xl opacity-40">
            <el-icon size="32"><Box /></el-icon>
            <p class="text-[10px] mt-2">道具库正在整理中</p>
          </div>
          <div 
            v-for="s in propServices" 
            :key="s.id"
            class="prop-card-premium group"
            :class="isServiceSelected(s.id) ? 'active-navy' : ''"
            @click="store.toggleService(s)"
          >
            <div class="relative aspect-square overflow-hidden rounded-lg mb-4 shadow-sm border border-black/5">
              <img :src="s.image" class="w-full h-full object-cover transition-transform duration-1000 group-hover:scale-125" />
              <div v-if="isServiceSelected(s.id)" class="absolute inset-0 bg-[var(--lh-navy)]/10 backdrop-blur-[1px]"></div>
            </div>
            <div class="font-serif font-bold text-sm text-[var(--lh-navy)] mb-1">{{ s.name }}</div>
            <div class="text-xs font-bold text-[var(--lh-red)]">¥{{ s.price }}</div>
            
            <div v-if="isServiceSelected(s.id)" class="absolute top-2 right-2 w-5 h-5 bg-[var(--lh-navy)] text-white rounded-full flex items-center justify-center shadow-md">
                <el-icon size="12"><Check /></el-icon>
            </div>
          </div>
        </div>

        <!-- 临时显示：未分类数据 (Debug only) -->
        <div v-if="otherServices.length > 0" class="mt-12 pt-6 border-t border-dashed border-gray-200">
           <p class="text-[9px] text-gray-400 mb-4 opacity-50 italic">以下为待分类服务项：</p>
           <div class="space-y-3">
             <div v-for="s in otherServices" :key="s.id" class="flex justify-between items-center text-xs text-gray-400" @click="store.toggleService(s)">
               <span>{{ s.name }} ({{ s.category }})</span>
               <span class="font-bold">¥{{ s.price }}</span>
             </div>
           </div>
        </div>
      </div>
    </div>

    <!-- 底部费用结算 (Floating Panel) -->
    <div class="summary-footer mt-24 card-premium p-10 flex flex-col md:flex-row items-center justify-between gap-12 bg-white relative overflow-hidden">
      <!-- 装饰背景 -->
      <div class="absolute right-0 top-0 text-[120px] font-serif opacity-5 select-none pointer-events-none transform translate-x-1/4 -translate-y-1/4">总计</div>
      
      <div class="flex flex-wrap items-center gap-12 relative z-10 w-full md:w-auto justify-center md:justify-start">
        <div class="stat-item text-center md:text-left">
           <p class="text-[10px] font-bold text-[var(--lh-text-secondary)] tracking-[0.06em] mb-2 font-serif">汉服费用</p>
           <p class="text-2xl font-serif font-bold text-[var(--lh-navy)]">¥{{ store.hanfuTotal.toFixed(2) }}</p>
        </div>
        <div class="hidden md:block w-px h-12 bg-[var(--lh-border)] opacity-60"></div>
        <div class="stat-item text-center md:text-left">
           <p class="text-[10px] font-bold text-[var(--lh-text-secondary)] tracking-[0.06em] mb-2 font-serif">附加服务</p>
           <p class="text-2xl font-serif font-bold text-[var(--lh-navy)]">¥{{ store.servicesTotal.toFixed(2) }}</p>
        </div>
        <div class="hidden md:block w-px h-12 bg-[var(--lh-border)] opacity-60"></div>
        <div class="stat-item text-center md:text-left">
           <p class="text-[10px] font-bold text-[var(--lh-red)] tracking-[0.08em] mb-2 font-serif">应付总额</p>
           <div class="flex items-baseline gap-1">
             <span class="text-sm font-bold text-[var(--lh-red)]">¥</span>
             <p class="text-5xl font-serif font-bold text-[var(--lh-red)] tracking-tighter">{{ store.grandTotal.toFixed(2) }}</p>
           </div>
        </div>
      </div>

      <div class="flex gap-6 w-full md:w-auto relative z-10">
        <button class="px-10 py-5 border-2 border-[var(--lh-border)] text-[var(--lh-navy)] hover:bg-[var(--lh-cream)] transition-all font-bold tracking-[0.06em] rounded-2xl text-xs" @click="$emit('prev')">上一步</button>
        <button class="btn-premium px-14 py-5 flex-1 md:flex-none shadow-2xl" @click="$emit('next')">
          下一步：确认订单 <el-icon class="ml-3"><ArrowRight /></el-icon>
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref, computed } from 'vue'
import { Check, ArrowRight, Operation, Camera, Box } from '@element-plus/icons-vue'
import { useBookingStore, type SelectedService } from '@/stores/booking'
import { fetchServiceList, type ServiceItem } from '@/api/service'

const store = useBookingStore()
defineEmits(['next', 'prev'])

const allServices = ref<ServiceItem[]>([])
const loading = ref(false)

const isServiceSelected = (id: string | number) => store.selectedServices.some((x: SelectedService) => x.id === id)

const defaultMakeup = 'https://images.unsplash.com/photo-1512496015851-a90fb38ba796?w=400&h=400&fit=crop'
const defaultPhoto = 'https://images.unsplash.com/photo-1493863641943-9b68992a8d07?w=400&h=400&fit=crop'
const defaultProp = 'https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=400&h=400&fit=crop'

const makeupServices = computed(() => {
  const list = allServices.value.filter(s => {
    const c = (s.category || '').toLowerCase().trim()
    return c === 'makeup' || c === '妆造' || c === '化妆'
  })
  return list.map(s => ({ ...s, image: s.image || defaultMakeup }))
})

const photographyServices = computed(() => {
  const list = allServices.value.filter(s => {
    const c = (s.category || '').toLowerCase().trim()
    return c === 'photo' || c === '摄影' || c === '跟拍' || c === 'photography'
  })
  return list.map(s => ({ ...s, image: s.image || defaultPhoto }))
})

const propServices = computed(() => {
  const list = allServices.value.filter(s => {
    const c = (s.category || '').toLowerCase().trim()
    return c === 'prop' || c === '道具' || c === 'props'
  })
  return list.map(s => ({ ...s, image: s.image || defaultProp }))
})

const otherServices = computed(() => {
  const standard = ['makeup', '妆造', '化妆', 'photo', '摄影', '跟拍', 'photography', 'prop', '道具', 'props']
  return allServices.value.filter(s => {
    const c = (s.category || '').toLowerCase().trim()
    return c && !standard.includes(c)
  })
})

onMounted(async () => {
  loading.value = true
  try {
    allServices.value = await fetchServiceList()
  } finally {
    loading.value = false
  }
})
</script>

<style scoped>
.service-card-premium {
  padding: 16px;
  background: white;
  border: 1px solid var(--lh-border);
  border-radius: 20px;
  cursor: pointer;
  display: flex;
  gap: 20px;
  transition: var(--transition-smooth);
}

.service-card-premium:hover {
  transform: translateX(8px);
  border-color: var(--lh-gold);
}

.active-red {
  background: var(--lh-red);
  border-color: var(--lh-red);
  transform: translateX(8px);
}
.active-red .text-base, .active-red .text-\[10px\], .active-red .text-lg {
  color: white !important;
}

.active-gold {
  background: var(--lh-gold);
  border-color: var(--lh-gold);
  transform: translateX(8px);
}
.active-gold .text-base, .active-gold .text-\[10px\], .active-gold .text-lg {
  color: white !important;
}

.prop-card-premium {
  position: relative;
  background: white;
  border: 1px solid var(--lh-border);
  padding: 12px;
  border-radius: 16px;
  cursor: pointer;
  transition: var(--transition-smooth);
  text-align: center;
}

.prop-card-premium:hover {
  transform: translateY(-8px);
  border-color: var(--lh-navy);
}

.active-navy {
  background: var(--lh-navy);
  border-color: var(--lh-navy);
}
.active-navy .font-serif, .active-navy .text-xs {
  color: white !important;
}
</style>
