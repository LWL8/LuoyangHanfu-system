<template>
  <div class="step-indicator w-full py-8 md:py-10 mb-12 bg-white/80 rounded-2xl shadow-premium border border-[var(--lh-border)] sticky top-24 z-30 backdrop-blur-xl">
    <div class="flex items-center justify-center max-w-5xl mx-auto px-6">
      <div v-for="(step, index) in steps" :key="index" class="flex items-center relative flex-1 last:flex-none">
        
        <!-- 步骤单元 -->
        <div class="flex flex-col items-center z-10 group min-w-[80px]">
          <div 
            class="w-11 h-11 md:w-13 md:h-13 rounded-full flex items-center justify-center border-2 transition-all duration-700 relative"
            :class="[
              currentStep > index + 1 ? 'bg-[var(--lh-red)] border-[var(--lh-red)] text-white' : 
              currentStep === index + 1 ? 'border-[var(--lh-red)] text-[var(--lh-red)] bg-white shadow-[0_0_20px_rgba(153,27,27,0.15)] scale-110' : 
              'border-[var(--lh-border)] text-[var(--lh-text-secondary)] bg-white/50'
            ]"
          >
            <!-- 激活状态的呼吸光环 -->
            <div v-if="currentStep === index + 1" class="absolute inset-0 rounded-full animate-ping bg-[var(--lh-red)]/10"></div>
            
            <el-icon v-if="currentStep > index + 1" size="20"><Check /></el-icon>
            <span v-else class="text-sm font-serif font-bold tracking-tighter">{{ index + 1 }}</span>
          </div>
          
          <span 
            class="mt-4 text-[11px] md:text-xs font-serif font-bold tracking-[0.1em] whitespace-nowrap transition-all duration-500 uppercase pb-1"
            :class="currentStep >= index + 1 ? 'text-[var(--lh-navy)] opacity-100' : 'text-[var(--lh-text-secondary)] opacity-40'"
          >
            {{ step }}
          </span>
        </div>

        <!-- 连接线 (Peony Gold Gradient) -->
        <div 
          v-if="index < steps.length - 1" 
          class="flex-1 h-[1px] mx-6 transition-all duration-1000 relative overflow-hidden"
          :class="currentStep > index + 1 ? 'bg-[var(--lh-red)]' : 'bg-[var(--lh-border)]'"
        >
          <div 
            v-if="currentStep === index + 1" 
            class="absolute inset-0 bg-gradient-to-r from-[var(--lh-red)] to-transparent animate-shimmer"
          ></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { Check } from '@element-plus/icons-vue'

defineProps<{
  currentStep: number
}>()

const steps = ['选日期时间', '选汉服款式', '选附加服务', '确认提交']
</script>

<style scoped>
@keyframes shimmer {
  0% { transform: translateX(-100%); }
  100% { transform: translateX(100%); }
}
.animate-shimmer {
  animation: shimmer 2s infinite linear;
}
</style>
