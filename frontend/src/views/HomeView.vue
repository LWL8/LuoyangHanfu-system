<template>
  <div class="home min-h-screen bg-[var(--lh-cream)] overflow-x-hidden">
    <HeaderNav class="glass-nav" />
    
    <!-- 1. 首页轮播 -->
    <div class="banner relative h-[90vh] w-full">
      <swiper
        :modules="[Autoplay, Pagination, EffectFade, Navigation, Keyboard]"
        effect="fade"
        :fade-effect="{ crossFade: true }"
        :slides-per-view="1"
        :loop="true"
        :autoplay="{ delay: 6000, disableOnInteraction: false }"
        :pagination="{ clickable: true, el: '.banner-pagination' }"
        :navigation="{ prevEl: '.banner-prev', nextEl: '.banner-next' }"
        :keyboard="{ enabled: true }"
        class="h-full w-full group"
        @autoplay-time-left="onAutoplayTimeLeft"
      >
        <swiper-slide v-for="(s, i) in slides" :key="i" v-slot="{ isActive }">
          <div v-if="s.image" class="relative flex h-full w-full items-center justify-center overflow-hidden">
            <!-- 优化：使用真实的 img 标签替代 background-image，提升浏览器在高分屏下的缩放锐度 -->
            <img 
              :src="s.image" 
              class="absolute inset-0 w-full h-full object-cover object-center select-none ken-burns" 
              style="image-rendering: -webkit-optimize-contrast; transform: translateZ(0); backface-visibility: hidden;"
              alt="banner"
            />
            
            <!-- 沉浸式渐变遮罩 -->
            <div class="absolute inset-0 bg-gradient-to-b from-black/10 via-black/30 to-black/70 mix-blend-multiply"></div>
            
            <div
              class="relative z-10 text-center text-white px-6 max-w-4xl"
              v-if="isActive"
              :key="`content-${i}-${isActive}`"
            >
              <div class="animate__animated animate__fadeInDown animate__faster text-[11px] tracking-[0.08em] border border-white/25 bg-white/5 backdrop-blur-sm rounded-full px-5 py-1.5 inline-block mb-5 font-medium uppercase">
                {{ s.dynasty }}
              </div>
              <h1 class="animate__animated animate__fadeInUp text-4xl md:text-7xl lg:text-8xl font-serif mb-5 tracking-[0.06em] leading-[1.15] drop-shadow-2xl font-black">
                {{ s.title }}
              </h1>
              <p class="animate__animated animate__fadeInUp animate__delay-1s text-[13px] md:text-lg font-light opacity-90 mb-9 tracking-[0.03em] leading-7 max-w-2xl mx-auto drop-shadow-md">
                {{ s.sub || '神都洛阳，千年神韵，领略华夏衣冠之美。' }}
              </p>
              <button class="btn-premium px-10 py-4 text-base tracking-[0.15em] font-bold transform transition-all hover:scale-105 hover:shadow-[0_0_30px_rgba(255,255,255,0.3)] rounded-full bg-white text-[var(--lh-navy)] hover:bg-[var(--lh-gold)] hover:text-white shadow-xl" @click="$router.push('/booking')">
                立即开启穿越之旅
              </button>
            </div>
          </div>
        </swiper-slide>
      </swiper>

      <!-- 轮播交互元素 -->
      <div class="absolute bottom-16 left-1/2 -translate-x-1/2 z-30 flex items-center gap-6">
        <button class="banner-prev w-12 h-12 rounded-full border border-white/20 bg-black/10 backdrop-blur-md text-white flex items-center justify-center hover:bg-[var(--lh-gold)] hover:border-[var(--lh-gold)] transition-all opacity-0 group-hover:opacity-100 -translate-x-10 group-hover:translate-x-0">
          <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="m15 18-6-6 6-6"/></svg>
        </button>
        <div class="banner-pagination !relative !bottom-0 !w-auto !flex items-center gap-3"></div>
        <button class="banner-next w-12 h-12 rounded-full border border-white/20 bg-black/10 backdrop-blur-md text-white flex items-center justify-center hover:bg-[var(--lh-gold)] hover:border-[var(--lh-gold)] transition-all opacity-0 group-hover:opacity-100 translate-x-10 group-hover:translate-x-0">
          <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="m9 18 6-6-6-6"/></svg>
        </button>
      </div>

      <!-- 自动轮播进度条 -->
      <div class="absolute bottom-0 left-0 w-full h-[3px] bg-white/10 z-30">
        <div class="progress-bar h-full bg-[var(--lh-gold)] w-0 transition-all duration-100 ease-linear"></div>
      </div>
      
      <!-- 向下滚动提示 -->
      <div class="absolute bottom-10 left-1/2 -translate-x-1/2 z-20 text-white/70 animate-bounce cursor-pointer flex flex-col items-center drop-shadow-[0_2px_4px_rgba(0,0,0,0.5)]">
        <span class="text-[10px] mb-2 tracking-[0.16em] font-black uppercase">Scroll Down</span>
        <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><path d="m7 13 5 5 5-5"></path><path d="m7 6 5 5 5-5"></path></svg>
      </div>
    </div>

    <!-- 2. 品类导航 -->
    <div class="container py-24 gsap-fade-up">
      <div class="text-center mb-16">
        <div class="flex flex-col items-center">
          <span class="text-[10px] font-black text-[var(--lh-gold)] tracking-[0.4em] uppercase mb-3 opacity-60">Categories</span>
          <h2 class="text-4xl md:text-5xl font-serif text-[var(--lh-navy)] tracking-[0.15em] relative">
            汉服分类
            <span class="absolute -bottom-6 left-1/2 -translate-x-1/2 h-0.5 w-12 bg-[var(--lh-gold)]"></span>
          </h2>
        </div>
      </div>
      
      <div class="flex gap-8 md:gap-14 overflow-x-auto pb-10 px-4 snap-x hide-scrollbar justify-start md:justify-center">
        <div 
          v-for="cat in categories" 
          :key="cat.name" 
          class="group min-w-[140px] flex-shrink-0 snap-center cursor-pointer text-center"
          @click="$router.push(`/hanfu?category=${cat.name}`)"
        >
          <div class="relative mx-auto h-28 w-28 md:h-36 md:w-36 rounded-full p-1.5 border border-[var(--lh-border)] mb-6 transition-all duration-700 group-hover:border-[var(--lh-gold)] group-hover:shadow-[0_20px_50px_rgba(180,83,9,0.2)]">
             <div class="w-full h-full rounded-full overflow-hidden relative">
               <img :src="cat.img" class="w-full h-full object-cover transition-transform duration-1000 group-hover:scale-110" alt="" />
               <div class="absolute inset-0 bg-black/0 group-hover:bg-black/10 transition-colors duration-500"></div>
             </div>
          </div>
          <h3 class="font-serif text-xl text-[var(--lh-navy)] font-black tracking-[0.1em] group-hover:text-[var(--lh-red)] transition-all">{{ cat.name }}</h3>
          <p class="text-[9px] text-[var(--lh-text-secondary)] opacity-40 font-bold uppercase tracking-widest mt-1 group-hover:opacity-100 transition-opacity">Style Guide</p>
        </div>
      </div>
    </div>

    <!-- 3. 热门推荐 -->
    <div class="bg-white/50 backdrop-blur py-20 md:py-24 border-y border-[var(--lh-border)]">
      <div class="container gsap-fade-up">
        <div class="text-center mb-12 md:mb-16">
          <h2 class="text-3xl md:text-4xl font-serif text-[var(--lh-navy)] tracking-[0.12em] relative inline-block">
            热门推荐
            <span class="block h-0.5 w-16 bg-[var(--lh-red)] mx-auto mt-6"></span>
          </h2>
          <p class="mt-4 section-desc">根据近期预约与咨询整理，便于快速浏览</p>
        </div>
        
        <div class="min-h-[400px]">
           <el-skeleton :loading="loading" animated :count="3">
              <template #template>
                 <div class="flex flex-col gap-6 p-4">
                    <el-skeleton-item variant="image" class="w-full aspect-[3/4] rounded-xl" />
                    <el-skeleton-item variant="h3" style="width: 70%" />
                    <el-skeleton-item variant="text" style="width: 40%" />
                 </div>
              </template>
              <template #default>
                <div class="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-5 md:gap-7">
                  <HanfuCard v-for="h in hot" :key="h.id" :item="h" class="hot-card-item opacity-0" />
                </div>
              </template>
           </el-skeleton>
        </div>
        
        <div class="text-center mt-14 md:mt-16">
          <button class="px-10 py-3 border-2 border-[var(--lh-red)] text-[var(--lh-red)] hover:bg-[var(--lh-red)] hover:text-white transition-all font-bold tracking-widest rounded-full" @click="$router.push('/hanfu')">
            查看全部汉服
          </button>
        </div>
      </div>
    </div>

    <!-- 4. 景点与搭配参考 -->
    <div class="container py-20 md:py-24 gsap-fade-up">
      <div class="text-center mb-12 md:mb-16">
        <h2 class="text-3xl md:text-4xl font-serif text-[var(--lh-navy)] tracking-[0.12em] relative inline-block">
          洛阳景点 · 搭配参考
          <span class="block h-0.5 w-16 bg-[var(--lh-gold)] mx-auto mt-6"></span>
        </h2>
        <p class="mt-4 section-desc">以下为常见游览点与服装风格参考，实际以门店库存与妆造安排为准</p>
      </div>

      <div class="grid grid-cols-1 md:grid-cols-3 gap-6 md:gap-8">
        <div v-for="s in scenics" :key="s.name" class="scenic-card relative rounded-2xl overflow-hidden h-[360px] md:h-[390px] cursor-pointer group shadow-premium opacity-0" @click="$router.push('/luoyang/guides')">
          <img :src="s.img" class="w-full h-full object-cover transition-transform duration-1000 group-hover:scale-110" alt="" />
          <div class="absolute inset-0 bg-gradient-to-t from-black/85 via-black/25 to-transparent group-hover:from-black/95 transition-all"></div>
          <div class="absolute inset-0 border-[var(--lh-gold)]/0 group-hover:border-2 group-hover:inset-4 transition-all duration-500 rounded-xl pointer-events-none"></div>
          
          <div class="absolute bottom-0 left-0 p-6 md:p-7 text-white w-full">
            <h3 class="text-2xl md:text-3xl font-serif mb-2 translate-y-4 group-hover:translate-y-0 transition-all duration-500">{{ s.name }}</h3>
            <p class="text-[12px] md:text-sm font-light opacity-85 translate-y-4 group-hover:translate-y-0 transition-all duration-500 delay-100">参考搭配：<span class="text-[var(--lh-gold)] font-bold">{{ s.match }}</span></p>
          </div>
        </div>
      </div>
    </div>

    <!-- 5. 预约流程 -->
    <div class="bg-[var(--lh-navy)] py-24 text-white relative overflow-hidden mt-12">
      <div class="absolute inset-0 opacity-5 pointer-events-none grayscale contrast-125 mix-blend-overlay">
        <!-- 背景纹理 -->
        <img src="https://www.transparenttextures.com/patterns/oriental-tiles.png" class="w-full h-full object-repeat" alt="" />
      </div>
      
      <div class="container relative z-10 gsap-fade-up">
        <div class="text-center mb-16 md:mb-20">
          <h2 class="text-3xl md:text-4xl font-serif tracking-[0.06em] font-medium">预约流程（四步）</h2>
          <p class="mt-4 text-[13px] leading-7 tracking-[0.03em] text-white/65">按步骤提交即可完成预约，页面会自动保存已选信息</p>
          <div class="w-16 h-px bg-[var(--lh-gold)] mx-auto mt-6 opacity-50"></div>
        </div>
        
        <div class="grid grid-cols-2 md:grid-cols-4 gap-8 md:gap-10 text-center relative">
          <!-- 步骤连接线（桌面端） -->
          <div class="hidden md:block absolute top-[42px] left-[16%] right-[16%] h-[0.5px] bg-[var(--lh-gold)] opacity-15"></div>
          
          <div v-for="(step, index) in steps" :key="index" class="relative group">
            <div class="w-20 h-20 md:w-22 md:h-22 mx-auto rounded-full bg-white/5 backdrop-blur-lg border border-white/20 flex items-center justify-center mb-6 relative z-10 transition-all duration-500 group-hover:bg-[var(--lh-gold)] group-hover:text-[var(--lh-navy)] group-hover:scale-105 shadow-lg">
              <el-icon class="text-3xl"><component :is="step.icon" /></el-icon>
            </div>
            <h3 class="text-xl md:text-2xl font-serif mb-3 tracking-[0.08em] text-[var(--lh-gold)] group-hover:text-white transition-colors">{{ step.title }}</h3>
            <p class="text-[12px] md:text-sm opacity-70 group-hover:opacity-100 transition-opacity tracking-[0.04em] leading-6 max-w-[220px] mx-auto">{{ step.desc }}</p>
            <div class="absolute -top-3 right-2 md:-right-2 text-6xl md:text-7xl font-serif opacity-[0.04] font-bold pointer-events-none">0{{ index + 1 }}</div>
          </div>
        </div>
      </div>
    </div>

    <FooterBar />
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { Swiper, SwiperSlide } from 'swiper/vue'
import { Autoplay, Pagination, EffectFade, Navigation, Keyboard } from 'swiper/modules'
import 'swiper/css'
import 'swiper/css/pagination'
import 'swiper/css/navigation'
// @ts-ignore
import 'swiper/css/effect-fade'
import gsap from 'gsap'
import { ScrollTrigger } from 'gsap/ScrollTrigger'

import { Pointer, Calendar, Coordinate, UserFilled } from '@element-plus/icons-vue'
import HeaderNav from '@/components/common/HeaderNav.vue'
import FooterBar from '@/components/common/FooterBar.vue'
import HanfuCard from '@/components/hanfu/HanfuCard.vue'
import { fetchHomeBannerList } from '@/api/banner'
import { fetchHotRecommends } from '@/api/recommend'
import { fetchGuides } from '@/api/luoyang'

gsap.registerPlugin(ScrollTrigger)

const progressRate = ref(0)
const onAutoplayTimeLeft = (s: any, time: number, progress: number) => {
  progressRate.value = (1 - progress) * 100
  const bar = document.querySelector('.progress-bar') as HTMLElement
  if (bar) {
    bar.style.width = `${progressRate.value}%`
  }
}

const defaultSlides = [
  {
    title: '洛裳汉服 · 神都之约',
    sub: '穿越千年，在古都遇见最美自己',
    dynasty: '神都欢迎您',
    image: '/images/scenic_placeholder.png'
  }
]
const slides = ref<any[]>([])

const loadSlidesFromAdmin = async () => {
  try {
    const res: any = await fetchHomeBannerList()
    if (res && (res.code === 0 || res.code === 200)) {
      const normalized = (res.data || [])
        .map((item: any) => {
          const title = String(item.title || '')
          const dynasty = title.includes('·') ? title.split('·')[0].trim() : (item.enabled === 1 ? '推荐' : '回顾')
          return {
            title: title || '汉服美学',
            sub: '',
            dynasty,
            image: String(item.image || '')
          }
        })
        .filter((item: any) => !!item.image)
      slides.value = normalized.length ? normalized : defaultSlides
    } else {
      slides.value = defaultSlides
    }
  } catch {
    slides.value = defaultSlides
  }
}

const categories = [
  { name: '唐制', img: '/images/category/tang.png' },
  { name: '宋制', img: '/images/category/song.png' },
  { name: '魏晋', img: '/images/category/weijin.png' },
  { name: '明制', img: '/images/category/ming.png' },
  { name: '洛阳特色', img: '/images/category/luoyang.png' }
]

const scenics = ref<any[]>([])
const loadScenics = async () => {
  try {
    const res: any = await fetchGuides()
    console.log('Home fetchGuides res:', res)
    const list = Array.isArray(res) ? res : (res?.data?.records || res?.records || res?.data || [])
    
    if (list && list.length > 0) {
      scenics.value = list.slice(0, 3).map((g: any) => ({
        name: g.scenic || '洛阳景点',
        match: g.style || '汉服穿搭',
        img: g.imageAfter || g.imageBefore || '/images/guide_banner.png'
      }))
    } else {
      // 备选演示数据 (保持视觉精美)
      scenics.value = [
        { name: '应天门', match: '唐制 · 齐胸襦裙', img: 'https://images.unsplash.com/photo-1512496015851-a90fb38ba496?w=800' },
        { name: '龙门石窟', match: '魏晋 · 大袖衫', img: 'https://images.unsplash.com/photo-1522335789203-aabd1fc54bc9?w=800' },
        { name: '明堂天堂', match: '唐制 · 圆领袍', img: 'https://images.unsplash.com/photo-1543854589-32a26569eb21?w=800' }
      ]
    }
  } catch (err) {
    console.error('Home loadScenics err:', err)
    scenics.value = [
        { name: '应天门', match: '唐制 · 齐胸襦裙', img: 'https://images.unsplash.com/photo-1512496015851-a90fb38ba496?w=800' },
        { name: '龙门石窟', match: '魏晋 · 大袖衫', img: 'https://images.unsplash.com/photo-1522335789203-aabd1fc54bc9?w=800' },
        { name: '明堂天堂', match: '唐制 · 圆领袍', img: 'https://images.unsplash.com/photo-1543854589-32a26569eb21?w=800' }
    ]
  }
}

const steps = [
  { title: '挑选款式', desc: '在列表中查看尺码、租金与押金说明，选定后再进入预约。', icon: Pointer },
  { title: '选择时间', desc: '选择到店日期与时段，热门日期建议尽早提交。', icon: Calendar },
  { title: '填写信息', desc: '填写联系方式与人数，如有妆造、跟拍等需求请备注清楚。', icon: UserFilled },
  { title: '到店体验', desc: '按预约时间到店核对订单，试穿、妆造与归还按门店流程办理。', icon: Coordinate }
]

const hot = ref<any[]>([])
const loading = ref(false)

onMounted(async () => {
  await Promise.all([
    loadSlidesFromAdmin(),
    loadScenics()
  ])
  loading.value = true
  try {
    const res: any = await fetchHotRecommends()
    if ((res.code === 0 || res.code === 200) && res.data) {
      hot.value = res.data.slice(0, 4)
    } else if (Array.isArray(res)) {
      hot.value = res.slice(0, 4)
    }
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }

  setTimeout(() => {
    gsap.utils.toArray<HTMLElement>('.gsap-fade-up').forEach(el => {
      gsap.fromTo(el, 
        { y: 80, opacity: 0 }, 
        { 
          y: 0, 
          opacity: 1, 
          duration: 1.5, 
          ease: 'power3.out',
          scrollTrigger: {
            trigger: el,
            start: 'top 90%',
          }
        }
      )
    })

    const hotSection = document.querySelector('.hot-card-item')?.closest('.container');
    if (hotSection) {
      gsap.fromTo('.hot-card-item',
        { y: 60, opacity: 0, scale: 0.9 },
        {
          y: 0,
          opacity: 1,
          scale: 1,
          duration: 1.2,
          stagger: 0.1,
          ease: 'expo.out',
          scrollTrigger: {
            trigger: hotSection,
            start: 'top 80%',
          }
        }
      )
    }

    const scenicSection = document.querySelector('.scenic-card')?.closest('.container');
    if (scenicSection) {
      gsap.fromTo('.scenic-card',
        { y: 40, opacity: 0, x: -20 },
        {
          y: 0, opacity: 1, x: 0,
          duration: 1.2,
          stagger: 0.3,
          ease: 'power3.out',
          scrollTrigger: {
            trigger: scenicSection,
            start: 'top 85%',
          }
        }
      )
    }
  }, 300)
})
</script>

<style scoped>
.hide-scrollbar::-webkit-scrollbar {
  display: none;
}
.hide-scrollbar {
  -ms-overflow-style: none;
  scrollbar-width: none;
}

/* Ken Burns Effect */
@keyframes kenburns {
  0% { transform: scale(1) translate(0, 0); }
  100% { transform: scale(1.15) translate(-2%, -1%); }
}
.ken-burns {
  animation: kenburns 12s ease-out infinite alternate;
}

/* Premium Pagination */
:deep(.banner-pagination .swiper-pagination-bullet) {
  width: 8px;
  height: 8px;
  background: white;
  opacity: 0.3;
  margin: 0 4px !important;
  transition: all 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275);
  border-radius: 4px;
}
:deep(.banner-pagination .swiper-pagination-bullet-active) {
  opacity: 1;
  width: 32px;
  background: var(--lh-gold);
}

.banner-prev, .banner-next {
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
}
</style>
