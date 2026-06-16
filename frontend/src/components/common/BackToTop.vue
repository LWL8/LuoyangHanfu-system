<template>
  <transition name="heritage-slide">
    <div 
      v-show="visible" 
      class="back-to-top fixed right-8 bottom-8 lg:right-12 lg:bottom-12 z-[100] cursor-pointer group"
      @click="scrollToTop"
    >
      <div class="relative w-16 h-16 bg-white/10 backdrop-blur-3xl rounded-[20px] shadow-[0_20px_50px_rgba(0,0,0,0.1)] flex flex-col items-center justify-center border border-[var(--lh-border)] transition-all duration-700 group-hover:bg-[var(--lh-red)] group-hover:border-[var(--lh-red)] group-hover:scale-110 group-active:scale-95 group-hover:rotate-12 overflow-hidden">
        
        <!-- 进度环 (Ritual Progress) -->
        <svg class="absolute inset-0 w-full h-full -rotate-90 opacity-20 group-hover:opacity-100 transition-opacity">
          <circle
            cx="32"
            cy="32"
            r="30"
            fill="none"
            stroke="var(--lh-gold)"
            stroke-width="2"
            stroke-dasharray="188.4"
            :stroke-dashoffset="188.4 * (1 - scrollProgress)"
            class="transition-all duration-300"
          />
        </svg>

        <el-icon class="text-[var(--lh-navy)] transition-colors group-hover:text-white" size="24"><Top /></el-icon>
        <span class="text-[8px] font-serif font-black text-[var(--lh-gold)] tracking-widest mt-1 group-hover:text-white transition-colors uppercase">UP</span>
        
        <!-- 装饰底纹 -->
        <div class="absolute -bottom-2 -left-2 text-4xl font-serif font-black text-[var(--lh-navy)] opacity-[0.03] select-none pointer-events-none group-hover:opacity-0 transition-opacity">洛</div>
      </div>
      
      <!-- 提示文字 (Imperial Label) -->
      <div class="absolute right-20 top-1/2 -translate-y-1/2 px-4 py-2 bg-[var(--lh-navy)] text-white text-[9px] font-black tracking-[0.3em] rounded-xl opacity-0 group-hover:opacity-100 transition-all pointer-events-none whitespace-nowrap shadow-xl transform translate-x-4 group-hover:translate-x-0 uppercase italic">
        归云 · 返回顶部 (RISE)
      </div>
    </div>
  </transition>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'
import { Top } from '@element-plus/icons-vue'
import gsap from 'gsap'
import { ScrollToPlugin } from 'gsap/ScrollToPlugin'

gsap.registerPlugin(ScrollToPlugin)

const visible = ref(false)
const scrollProgress = ref(0)

const handleScroll = () => {
  const scrollTop = window.scrollY
  const docHeight = document.documentElement.scrollHeight - window.innerHeight
  visible.value = scrollTop > 500
  scrollProgress.value = Math.min(scrollTop / docHeight, 1)
}

const scrollToTop = () => {
  gsap.to(window, {
    duration: 1.5,
    scrollTo: 0,
    ease: 'expo.inOut'
  })
}

onMounted(() => {
  window.addEventListener('scroll', handleScroll)
})

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll)
})
</script>

<style scoped>
.heritage-slide-enter-active,
.heritage-slide-leave-active {
  transition: all 0.8s cubic-bezier(0.19, 1, 0.22, 1);
}

.heritage-slide-enter-from,
.heritage-slide-leave-to {
  opacity: 0;
  transform: translateY(100px) rotate(45deg);
}

.back-to-top {
  will-change: transform, opacity;
}
</style>
