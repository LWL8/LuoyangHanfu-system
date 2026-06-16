<template>
  <div class="legal-view min-h-screen bg-[var(--lh-cream)] pb-32 animate__animated animate__fadeIn">
    <HeaderNav />
    
    <!-- 隐私安全横幅 (Security Hero) -->
    <section class="relative h-[400px] md:h-[460px] overflow-hidden flex items-center pt-24 mb-16 px-6 md:px-16">
      <div class="absolute inset-0 z-0">
        <img src="https://images.unsplash.com/photo-1550751827-4bd374c3f58b?q=80&w=1600" class="w-full h-full object-cover brightness-[0.35] contrast-125" />
        <div class="absolute inset-0 bg-gradient-to-b from-[var(--lh-navy)]/80 via-transparent to-[var(--lh-cream)]"></div>
      </div>
      <div class="relative z-10 w-full">
        <div class="animate__animated animate__fadeInDown">
          <span class="text-[var(--lh-gold)] font-bold tracking-[0.4em] text-xs uppercase mb-4 block">Data Protection</span>
          <h1 class="text-5xl md:text-7xl font-serif font-black text-white tracking-[0.2em] mb-8 drop-shadow-2xl">隐私政策</h1>
          <div class="w-20 h-1 bg-[var(--lh-red)] mb-10 shadow-lg"></div>
          <p class="text-sm md:text-lg text-white/80 tracking-[0.15em] leading-relaxed italic font-medium drop-shadow-lg max-w-2xl">
            洛裳汉服深知信息安全之重。我们承诺采用业界顶尖的加密技术，守护您的每一份信任与隐私。
            <span class="block mt-4 font-serif text-[var(--lh-gold)]">— 数字化资产安全白皮书 —</span>
          </p>
        </div>
      </div>
    </section>

    <!-- 全屏分栏布局 (Full-Width Split Layout) -->
    <div class="w-full px-6 md:px-16">
      <div class="grid grid-cols-1 lg:grid-cols-4 gap-12 items-start">
        
        <!-- 左侧：快速索引 (Side Navigation) -->
         <div class="lg:sticky lg:top-32 space-y-8 animate__animated animate__fadeInLeft">
           <div class="p-10 bg-white/50 backdrop-blur-3xl border border-[var(--lh-gold)]/20 rounded-[40px] shadow-xl">
             <div class="text-[10px] font-black text-[var(--lh-gold)] tracking-[0.2em] uppercase mb-6 opacity-60">Privacy Index</div>
             <nav class="flex flex-col gap-6">
                <a 
                  v-for="(item, index) in sections" 
                  :key="index"
                  :href="'#' + item.id"
                  class="group flex items-center gap-4 text-sm font-serif font-black text-[var(--lh-navy)] opacity-40 hover:opacity-100 transition-all"
                >
                  <span class="w-6 h-6 rounded-full bg-[var(--lh-navy)] text-white text-[9px] flex items-center justify-center group-hover:bg-[var(--lh-red)]">{{ index + 1 }}</span>
                  <span class="tracking-widest">{{ item.title }}</span>
                </a>
             </nav>
             <div class="mt-12 pt-8 border-t border-[var(--lh-navy)]/10">
                <p class="text-[9px] text-[var(--lh-text-secondary)] font-black italic opacity-40 tracking-[0.1em]">
                  Privacy Policy: V3.2.0<br>
                  Security Update: 2026-04-05
                </p>
             </div>
           </div>

           <!-- 信息安全承诺 -->
           <div class="p-8 border-l-4 border-[var(--lh-gold)] bg-white/30 backdrop-blur-xl space-y-4">
              <el-icon size="32" class="text-[var(--lh-gold)] opacity-50"><Lock /></el-icon>
              <p class="text-[11px] leading-relaxed text-[var(--lh-navy)] font-medium opacity-60 italic">
                “您的个人资料将存储于金融级加密服务器中，我们承诺永不向任何第三方机构出售或共享敏感数据。”
              </p>
           </div>
         </div>

         <!-- 右侧：协议正文 (Main Content) -->
         <div class="lg:col-span-3 space-y-12 animate__animated animate__fadeIn">
            <div class="card-premium bg-white p-12 lg:p-24 shadow-2xl rounded-[60px] border border-[var(--lh-border)] relative overflow-hidden">
               <!-- 背景装饰 -->
               <div class="absolute bottom-0 left-0 p-12 pointer-events-none opacity-[0.03]">
                  <el-icon size="400" class="text-[var(--lh-gold)] -ml-40 -mb-40"><View /></el-icon>
               </div>

               <div class="prose prose-slate max-w-none font-serif leading-relaxed text-[var(--lh-navy)] opacity-90 relative z-10">
                 <section v-for="section in sections" :key="section.id" :id="section.id" class="mb-20 last:mb-0">
                   <div class="flex flex-col gap-6 mb-10">
                      <div class="text-[9px] font-bold text-[var(--lh-red)] tracking-[0.3em] uppercase opacity-70 flex items-center gap-3">
                         <div class="h-px w-8 bg-[var(--lh-red)]"></div>
                         SECTION {{ section.id }}
                      </div>
                      <h2 class="text-3xl md:text-4xl font-black tracking-widest">{{ section.title }}</h2>
                   </div>
                   <div class="legal-content text-lg leading-[2.2] space-y-6" v-html="section.content"></div>
                 </section>
               </div>

               <!-- 确认区 -->
               <div class="mt-32 pt-16 border-t border-[var(--lh-navy)]/10 text-center flex flex-col items-center gap-10">
                 <div class="p-8 bg-[var(--lh-cream)] rounded-3xl border border-[var(--lh-border)] max-w-2xl">
                    <p class="text-sm text-[var(--lh-text-secondary)] italic leading-loose opacity-80">
                      洛裳汉服严格遵守《个人信息保护法》。如果您认为我们的数据处理行为侵犯了您的权利，请随时通过隐私反馈渠道联系我们。
                    </p>
                 </div>
                 <button 
                  class="btn-premium px-24 py-6 text-sm tracking-[0.2em] font-serif shadow-2xl hover:scale-105 active:scale-95 transition-all" 
                  @click="$router.back()"
                 >
                   确认隐私政策并返回
                 </button>
               </div>
            </div>
         </div>

      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { Lock, View } from '@element-plus/icons-vue'
import HeaderNav from '@/components/common/HeaderNav.vue'

const sections = [
  {
    id: '01',
    title: '信息采集 Data Collection',
    content: '为了协助您完成汉服预约、实名登记及个性化穿搭建议，我们需要收集您的以下信息：<b>真实姓名、联系电话、预约时段及身材尺寸参考</b>。我们承诺仅在法律法规允许的范围内，及为了提供精准汉服体验的目的下采集此类数据。'
  },
  {
    id: '02',
    title: '信息存储 Security Storage',
    content: '您的所有个人数据将存储在具备<b>金融级安全防护等级</b>的云计算平台中，所有敏感资料均通过 AES-256 高强度算法加密处理。除非根据司法执行要求或法律诉讼，本平台绝不会将您的个人隐私信息出售或共享给非关联的第三方商业机构。'
  },
  {
    id: '03',
    title: '肖像权保护 Portrait Rights',
    content: '<ul class="space-y-4"><li>1. <b>著作权归属</b>：在本店组织的拍摄项目中所产生的摄影作品，其著作权由摄影机构与本平台共同持有。</li><li>2. <b>宣传素材使用</b>：若本平台计划在官方自媒体（如小红书、官号等）使用含有您清晰面容的照片，将事先通过线上或现场征得您的授权。您拥有随时撤回该授权及要求下架相关素材的权利。</li></ul>'
  },
  {
    id: '04',
    title: '您的权利 User Rights',
    content: '作为数据的拥有者，您拥有对个人资料的<b>查询权、更正权、删除权及限制处理权</b>。您可通过“个人中心-账号设置”自助管理数据，或联系官方隐私官协助进行数据的永久性销毁。'
  }
]
</script>

<style scoped>
.legal-view {
  scroll-behavior: smooth;
}

.legal-content {
  text-align: justify;
  font-family: var(--font-serif);
}

.legal-content :deep(b) {
  color: var(--lh-red);
  font-weight: 900;
}

.legal-content :deep(li) {
  margin-bottom: 2rem;
  padding-left: 1.5rem;
  border-left: 2px solid var(--lh-border);
}

.card-premium {
  background-image: radial-gradient(var(--lh-border) 0.5px, transparent 0.5px);
  background-size: 30px 30px;
}
</style>
