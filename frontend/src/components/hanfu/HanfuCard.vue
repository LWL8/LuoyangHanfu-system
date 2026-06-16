<template>
  <div 
    class="card-premium hanfu-card group relative overflow-hidden transition-all duration-500"
    :class="viewMode === 'list' ? 'flex flex-row h-64 md:h-72 items-stretch' : 'flex flex-col'"
    @mouseenter="startHover"
    @mouseleave="stopHover"
  >
    <!-- 图片区域 -->
    <div 
      class="relative overflow-hidden cursor-pointer shrink-0" 
      :class="viewMode === 'list' ? 'w-48 md:w-56' : 'aspect-[3/4] w-full'"
      @click="goDetail"
    >
      <img
        :src="currentImage"
        class="w-full h-full object-cover transition-transform duration-1000 group-hover:scale-110"
        alt=""
      />
      
      <!-- 朝代标签 -->
      <div class="absolute top-0 right-0 z-10" v-if="viewMode === 'grid'">
        <div class="bg-[var(--lh-red)] text-white text-[10px] py-1.5 px-4 rounded-bl-xl font-bold tracking-widest shadow-sm">
          {{ item.hanfuTypes || item.dynasty }}
        </div>
      </div>

      <!-- 热度 (Grid mode) -->
      <div v-if="viewMode === 'grid'" class="absolute bottom-3 left-3 z-10 flex items-center gap-1.5 bg-black/40 backdrop-blur-md text-white text-[10px] py-1 px-3 rounded-full border border-white/20">
        <el-icon color="var(--lh-gold)"><StarFilled /></el-icon>
        <span>{{ item.heatCount || item.hotScore || 0 }}</span>
      </div>

      <!-- 快速预约 -->
      <div v-show="viewMode === 'grid'" class="absolute inset-x-0 bottom-0 top-1/2 bg-gradient-to-t from-black/60 to-transparent opacity-0 group-hover:opacity-100 transition-all duration-500 flex items-end justify-center pb-8 translate-y-4 group-hover:translate-y-0">
        <button class="btn-premium scale-90" @click.stop="goBooking">
          立即预约
        </button>
      </div>
    </div>

    <!-- 信息区域 -->
    <div 
      class="flex flex-col justify-between p-5 flex-1"
      @click="goDetail"
    >
      <div>
        <div v-if="viewMode === 'list'" class="flex items-center gap-3 mb-3">
          <span class="bg-[var(--lh-red)]/10 text-[var(--lh-red)] text-[10px] py-1 px-3 rounded-full font-black tracking-widest">{{ item.hanfuTypes || item.dynasty }}</span>
          <span class="text-[10px] text-gray-400 bg-gray-50 px-3 py-1 rounded-full flex items-center gap-1">
             <el-icon><StarFilled /></el-icon> {{ item.heatCount || item.hotScore || 0 }} 热度
          </span>
        </div>
        
        <h3 
          class="font-serif font-bold text-[var(--lh-navy)] line-clamp-2 group-hover:text-[var(--lh-red)] transition-colors cursor-pointer mb-2"
          :class="viewMode === 'list' ? 'text-2xl lg:text-3xl lg:leading-normal' : 'text-base line-clamp-1'"
        >
          {{ item.hanfuName || item.name }}
        </h3>
        
        <p v-if="viewMode === 'list'" class="text-sm text-[var(--lh-text-secondary)] opacity-70 line-clamp-2 mb-6 font-light leading-relaxed max-w-lg">
          {{ item.intro || item.description || '精选上乘面料，剪裁考究，完美还原神都风格与历史底蕴。' }}
        </p>
      </div>
      
      <div class="flex items-end justify-between mt-auto">
        <div class="flex flex-col">
          <span class="text-[10px] text-[var(--lh-text-secondary)] line-through opacity-60 mb-0.5">¥{{ (Number(item.hanfuPrice || item.rentPrice || 0) * 1.5).toFixed(0) }}</span>
          <div class="font-bold text-[var(--lh-red)] flex items-baseline leading-none" :class="viewMode === 'list' ? 'text-3xl' : 'text-xl'">
            <span class="text-xs mr-0.5">¥</span>
            <span>{{ Number(item.hanfuPrice || item.rentPrice || 0) }}</span>
            <span class="text-[10px] text-[var(--lh-text-secondary)] font-normal ml-1"> 元</span>
          </div>
        </div>
        
        <div class="flex items-center gap-4">
           <button v-if="viewMode === 'list'" class="btn-premium px-8" @click.stop="goBooking">预约体验</button>
           <button class="text-[var(--lh-gold)] text-xs font-bold flex items-center gap-1 hover:gap-2 transition-all p-2" @click.stop="goDetail">
            详情 <el-icon><ArrowRight /></el-icon>
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { StarFilled, ArrowRight } from '@element-plus/icons-vue'

const props = withDefaults(defineProps<{
  item: any
  viewMode?: string
}>(), {
  viewMode: 'grid'
})

const router = useRouter()
const hoverIndex = ref(0)
let timer: any = null

const getImages = () => {
  let images = props.item.images || props.item.imageList || props.item.image_list
  if (typeof images === 'string') {
    try {
      images = JSON.parse(images)
    } catch(e) {
      images = [images]
    }
  }
  if (!images || images.length === 0) {
    images = [props.item.hanfuPhoto, props.item.coverImage].filter(Boolean)
  }
  return Array.isArray(images) ? images : []
}

const currentImage = computed(() => {
  const images = getImages()
  if (timer && images.length > 1) {
    return images[hoverIndex.value]
  }
  return images[0] || ''
})

const startHover = () => {
  const images = getImages()
  if (images.length > 1) {
    timer = setInterval(() => {
      hoverIndex.value = (hoverIndex.value + 1) % images.length
    }, 1800)
  }
}

const stopHover = () => {
  if (timer) {
    clearInterval(timer)
    timer = null
    hoverIndex.value = 0
  }
}

const goDetail = () => {
  router.push(`/hanfu/${props.item.id}`)
}

const goBooking = () => {
  router.push({
    path: '/booking',
    query: { hanfuId: props.item.id }
  })
}

onUnmounted(() => {
  if (timer) clearInterval(timer)
})
</script>

<style scoped>
/* 可以在此处添加自定义样式 */
</style>
