<template>
  <div class="guide-view min-h-screen bg-[var(--lh-cream)] pb-32 overflow-x-hidden animate__animated animate__fadeIn">
    <HeaderNav />
    
    <!-- 艺术横幅 (Page Banner) -->
    <section class="banner-section relative h-[440px] md:h-[520px] overflow-hidden flex items-center pt-24 mb-16">
      <div class="absolute inset-0 z-0">
        <img src="/images/banner/guide_banner.png" class="w-full h-full object-cover brightness-50 contrast-125" />
        <div class="absolute inset-0 bg-gradient-to-b from-[var(--lh-navy)]/60 via-transparent to-[var(--lh-cream)]"></div>
      </div>
      <div class="container relative z-10 text-center max-w-4xl mx-auto px-6">
        <div class="animate__animated animate__fadeInDown">
          <span class="text-[var(--lh-gold)] font-bold tracking-[0.4em] text-xs uppercase mb-4 block">Luoyang Heritage</span>
          <h1 class="text-5xl md:text-7xl font-serif font-black text-white tracking-[0.2em] mb-8 drop-shadow-2xl">寻韵 · 神都穿搭志</h1>
          <div class="w-20 h-1 bg-[var(--lh-red)] mx-auto mb-10 shadow-lg"></div>
          <p class="text-sm md:text-lg text-white/90 tracking-[0.2em] leading-relaxed italic font-medium drop-shadow-lg">
            “什么样的风景配什么样的汉服”不仅是审美，更是文化的情绪共鸣。
            <span class="block mt-4 font-serif text-[var(--lh-gold)]">— 洛裳美学指南 —</span>
          </p>
        </div>
      </div>
    </section>

    <div class="max-w-7xl mx-auto px-6 pb-20">
      <!-- 搭配卡片列表 (Heritage Layout) -->
      <div class="space-y-40">
         <el-empty v-if="guides.length === 0" description="暂无汉服穿搭攻略推荐" class="py-20 mb-20 bg-white rounded-3xl shadow-sm" />
         <div 
           v-for="(g, idx) in guides" 
           :key="g.id"
           class="guide-section flex flex-col items-center gap-16 lg:gap-32 relative"
           :class="idx % 2 === 0 ? 'lg:flex-row' : 'lg:flex-row-reverse'"
         >
           <!-- 背景装饰 -->
           <div class="absolute -z-10 text-[20rem] font-serif font-black text-[var(--lh-navy)] opacity-[0.01] select-none pointer-events-none italic" :class="idx % 2 === 0 ? '-right-20' : '-left-20'">0{{ idx + 1 }}</div>

           <!-- 视觉呈现: 左右对比图 (Premium Duo Cards) -->
           <div class="image-pair relative w-full lg:w-3/5 flex flex-col md:flex-row gap-6">
              <div class="flex-1 relative h-[550px] rounded-[40px] overflow-hidden group shadow-2xl border-4 border-white transform transition-all hover:scale-[1.02] hover:-rotate-1">
                 <img :src="g.imageBefore" class="w-full h-full object-cover transition-transform duration-1000 group-hover:scale-110" />
                 <div class="absolute inset-0 bg-gradient-to-t from-[var(--lh-navy)]/90 via-[var(--lh-navy)]/20 to-transparent"></div>
                 <div class="absolute inset-x-0 bottom-0 p-10 text-white">
                    <p class="text-[9px] uppercase font-black tracking-[0.5em] text-[var(--lh-gold)] mb-3">ARTIFACT STYLE</p>
                    <h3 class="text-3xl font-serif font-black tracking-widest">{{ g.style }}</h3>
                 </div>
              </div>
              <div class="flex-1 relative h-[550px] rounded-[40px] overflow-hidden group shadow-2xl border-4 border-white md:translate-y-16 transform transition-all hover:scale-[1.02] hover:rotate-1">
                 <img :src="g.imageAfter" class="w-full h-full object-cover transition-transform duration-1000 group-hover:scale-110" />
                 <div class="absolute inset-0 bg-gradient-to-t from-[var(--lh-navy)]/90 via-[var(--lh-navy)]/20 to-transparent"></div>
                 <div class="absolute inset-x-0 bottom-0 p-10 text-white">
                    <p class="text-[9px] uppercase font-black tracking-[0.5em] text-[var(--lh-gold)] mb-3">DESTINATION</p>
                    <h3 class="text-3xl font-serif font-black tracking-widest">{{ g.scenic }}</h3>
                 </div>
              </div>
              
              <!-- 核心连接符 (Imperial Compass) -->
              <div class="hidden md:block absolute top-[40%] left-1/2 -translate-x-1/2 -translate-y-1/2 z-20">
                 <div class="w-20 h-20 bg-white rounded-full flex items-center justify-center shadow-[0_20px_50px_rgba(0,0,0,0.2)] border-4 border-[var(--lh-gold)] group-hover:rotate-180 transition-transform duration-700">
                    <el-icon class="text-[var(--lh-red)]" size="36"><Switch /></el-icon>
                 </div>
              </div>
           </div>

           <!-- 文字解读 (Heritage Content) -->
           <div class="content lg:w-2/5 space-y-10 group">
              <div class="relative pl-12 border-l-4 border-[var(--lh-red)]">
                 <div class="absolute -left-1 top-2 w-10 h-1 bg-[var(--lh-red)]"></div>
                 <h2 class="text-4xl font-serif font-black text-[var(--lh-navy)] mb-6 tracking-widest leading-tight">
                   方案建议：<br/>{{ g.style }}
                 </h2>
                 <p class="text-sm text-[var(--lh-text-secondary)] leading-loose font-medium tracking-widest text-justify opacity-80 group-hover:opacity-100 transition-opacity">
                   {{ g.reason }}
                 </p>
              </div>

              <!-- 拍摄贴士 (Scroll Card) -->
              <div class="card-premium bg-white p-10 relative overflow-hidden transition-all duration-500 hover:shadow-2xl">
                 <div class="absolute top-0 right-0 p-6 opacity-[0.05]">
                    <el-icon size="80" class="text-[var(--lh-navy)]"><CameraFilled /></el-icon>
                 </div>
                 
                 <h4 class="text-sm font-serif font-black text-[var(--lh-navy)] mb-8 flex items-center gap-3 tracking-[0.3em] uppercase">
                    <el-icon class="text-[var(--lh-red)]" size="20"><Compass /></el-icon> 拍摄构思 Chronicles
                 </h4>
                 <ul class="space-y-6 text-[11px] text-[var(--lh-text-secondary)] font-bold tracking-widest leading-relaxed">
                    <li v-for="(tip, tIdx) in g.photographyTips" :key="tIdx" class="flex items-start gap-4 group/tip">
                       <span class="text-[var(--lh-red)] font-serif font-black text-lg leading-none transition-transform group-hover/tip:scale-125">0{{ tIdx + 1 }}.</span>
                       <p class="mt-0.5 group-hover/tip:text-[var(--lh-navy)] transition-colors">{{ tip }}</p>
                    </li>
                 </ul>
              </div>

              <div class="pt-8 flex flex-col sm:flex-row gap-6">
                 <button 
                  class="btn-premium flex-1 py-5 text-sm tracking-[0.3em] font-serif shadow-2xl"
                  @click="goSearch(g.style)"
                 >
                    即刻挑选服饰
                 </button>
                 <button class="flex-1 py-5 border-2 border-[var(--lh-border)] rounded-2xl text-[10px] font-black tracking-[0.4em] text-[var(--lh-text-secondary)] hover:bg-[var(--lh-cream)] hover:text-[var(--lh-navy)] transition-all uppercase">
                    Share Inspiration
                 </button>
              </div>
           </div>
         </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { CameraFilled, Switch, Compass, Top } from '@element-plus/icons-vue'
import HeaderNav from '@/components/common/HeaderNav.vue'
import { fetchGuides, type GuideItem } from '@/api/luoyang'

const router = useRouter()

const guides = ref<GuideItem[]>([])

const goSearch = (style: string) => {
  router.push({ path: '/hanfu', query: { dynasty: style.includes('唐') ? '唐制' : style.includes('宋') ? '宋制' : '魏晋' }})
}

onMounted(async () => {
  try {
    const res: any = await fetchGuides()
    console.log('Fetch Guides Res:', res)
    // 兼容多种返回格式： 直接是数组，或者是包装在 R.data 中的数组
    const list = Array.isArray(res) ? res : (res?.data?.records || res?.records || res?.data || [])
    
    if (list && list.length > 0) {
      // 这里的属性名必须与后端实体类 Guide.java 保持一致 (camelCase)
      guides.value = list.map((item: any) => ({
        ...item,
        // 增加图片兜底，防止出现 FAILED 裂图
        imageBefore: item.imageBefore || '/images/guide_banner.png',
        imageAfter: item.imageAfter || '/images/guide_banner.png',
        photographyTips: Array.isArray(item.photographyTips) ? item.photographyTips : []
      }))
    } else {
      // 备选演示数据 (保持视觉精美)
      guides.value = [
        {
          id: 1,
          style: '初唐 · 齐胸襦裙',
          scenic: '应天门',
          reason: '应天门的宏大建筑语汇极具气势，初唐齐胸襦裙色彩明快、剪裁大气，二者结合能够完美还原神都武周时期的盛世繁华。',
          photographyTips: ['利用中心轴线对称构图', '夕阳余晖增加质感', '仰角拍摄体现气势'],
          imageBefore: 'https://images.unsplash.com/photo-1512496015851-a90fb38ba496?w=800',
          imageAfter: 'https://images.unsplash.com/photo-1543854589-32a26569eb21?w=800'
        },
        {
          id: 2,
          style: '魏晋 · 大袖衫',
          scenic: '龙门石窟',
          reason: '魏晋名士风度与龙门千年石刻的苍劲之美不谋而合。大袖衫的轻盈飘逸能中和石壁的厚重感。',
          photographyTips: ['利用自然光影雕琢褶皱', '动态捕捉袖摆飞扬', '在大佛下留白拍摄'],
          imageBefore: 'https://images.unsplash.com/photo-1522335789203-aabd1fc54bc9?w=800',
          imageAfter: 'https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=800'
        }
      ]
    }
  } catch (err) {
    console.error('Fetch Guides Error:', err)
    // 失败也显示备选数据
    guides.value = [
      {
        id: 1,
        style: '初唐 · 齐胸襦裙',
        scenic: '应天门',
        reason: '应天门的宏大建筑语汇极具气势，初唐齐胸襦裙色彩明快、剪裁大气，二者结合能够完美还原神都武周时期的盛世繁华。',
        photographyTips: ['利用中心轴线对称构图', '夕阳余晖增加质感', '仰角拍摄体现气势'],
        imageBefore: 'https://images.unsplash.com/photo-1512496015851-a90fb38ba496?w=800',
        imageAfter: 'https://images.unsplash.com/photo-1543854589-32a26569eb21?w=800'
      }
    ]
  }
})
</script>

<style scoped>
.image-pair:hover .image-pair-connector {
  transform: translate(-50%, -50%) rotate(180deg);
}

.guide-section {
  will-change: transform, opacity;
}
</style>
