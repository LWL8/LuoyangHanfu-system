<template>
  <div class="booking-view min-h-screen pb-20">
    <HeaderNav />
    
    <!-- 顶部 Banner -->
    <div class="banner-premium relative h-[240px] md:h-[300px] overflow-hidden flex items-center pt-16">
      <div class="absolute inset-0 z-0">
        <img src="/images/banners/booking-banner.png" class="w-full h-full object-cover brightness-[0.45]" />
        <div class="absolute inset-0 bg-gradient-to-t from-[var(--lh-navy)]/60 to-transparent"></div>
      </div>
      
      <div class="container relative z-10 text-white">
        <div class="gsap-booking-title opacity-0">
          <h1 class="text-4xl md:text-5xl font-bold font-serif mb-3 tracking-widest">预约体验</h1>
          <p class="text-gray-300 text-base md:text-lg font-light tracking-widest opacity-90">
            简单四步，开启您的<span class="text-[var(--lh-gold)]">神都</span>焕变之旅
          </p>
        </div>
      </div>
    </div>

    <!-- 步骤指示器 -->
    <div class="container -mt-12 md:-mt-16 relative z-30">
      <StepIndicator :current-step="store.step" />
    </div>

    <div class="container pb-20">
      <div class="booking-content bg-transparent min-h-[600px] mt-8">
        <Step1DateTime v-if="store.step === 1" @next="next" />
        <Step2Hanfu v-else-if="store.step === 2" @next="next" @prev="prev" />
        <Step3Services v-else-if="store.step === 3" @next="next" @prev="prev" />
        <Step4Confirm v-else-if="store.step === 4" @prev="prev" />
      </div>
    </div>

  </div>
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import gsap from 'gsap'
import HeaderNav from '@/components/common/HeaderNav.vue'
import StepIndicator from '@/components/booking/StepIndicator.vue'
import Step1DateTime from '@/components/booking/Step1DateTime.vue'
import Step2Hanfu from '@/components/booking/Step2Hanfu.vue'
import Step3Services from '@/components/booking/Step3Services.vue'
import Step4Confirm from '@/components/booking/Step4Confirm.vue'
import { useBookingStore } from '@/stores/booking'

const store = useBookingStore()

function next() {
  store.setStep(Math.min(4, store.step + 1))
  window.scrollTo({ top: 200, behavior: 'smooth' })
}

function prev() {
  store.setStep(Math.max(1, store.step - 1))
  window.scrollTo({ top: 200, behavior: 'smooth' })
}

onMounted(() => {
  gsap.to('.gsap-booking-title', {
    opacity: 1,
    y: 0,
    startAt: { y: 20 },
    duration: 1,
    ease: 'power3.out',
    delay: 0.2
  })
})
</script>

<style lang="scss" scoped>
.booking-view {
  background-image: radial-gradient(var(--lh-border) 1px, transparent 1px);
  background-size: 32px 32px;
  background-color: var(--lh-cream);
}

.banner-premium {
  background-color: var(--lh-navy);
}
</style>
