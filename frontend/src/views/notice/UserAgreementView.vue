<template>
  <div class="legal-view min-h-screen bg-[var(--lh-cream)] pb-32 animate__animated animate__fadeIn">
    <HeaderNav />
    
    <!-- 沉浸式横幅 (Legislative Hero) -->
    <section class="relative h-[400px] md:h-[460px] overflow-hidden flex items-center pt-24 mb-16 px-6 md:px-16">
      <div class="absolute inset-0 z-0">
        <img src="https://images.unsplash.com/photo-1579541814924-49fef17c5be5?q=80&w=1600" class="w-full h-full object-cover brightness-[0.4] contrast-125" />
        <div class="absolute inset-0 bg-gradient-to-b from-[var(--lh-navy)]/80 via-transparent to-[var(--lh-cream)]"></div>
      </div>
      <div class="relative z-10 w-full">
        <div class="animate__animated animate__fadeInDown">
          <span class="text-[var(--lh-gold)] font-bold tracking-[0.4em] text-xs uppercase mb-4 block">Platform Governance</span>
          <h1 class="text-5xl md:text-7xl font-serif font-black text-white tracking-[0.2em] mb-8 drop-shadow-2xl">用户协议</h1>
          <div class="w-20 h-1 bg-[var(--lh-red)] mb-10 shadow-lg"></div>
          <p class="text-sm md:text-lg text-white/80 tracking-[0.15em] leading-relaxed italic font-medium drop-shadow-lg max-w-2xl">
            服务洛裳汉服拥趸，共建礼仪之邦。请阅读并同意以下条款以继续服务。
            <span class="block mt-4 font-serif text-[var(--lh-gold)]">— 官方服务细则摘要 —</span>
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
             <div class="text-[10px] font-black text-[var(--lh-gold)] tracking-[0.2em] uppercase mb-6 opacity-60">Agreement Index</div>
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
                  Document Version: V2.1.04<br>
                  Effective Date: 2026-04-01
                </p>
             </div>
           </div>

           <!-- 品牌背书 -->
           <div class="p-8 border-l-4 border-[var(--lh-gold)] bg-white/30 backdrop-blur-xl">
              <p class="text-[11px] leading-relaxed text-[var(--lh-navy)] font-medium opacity-60 italic">
                "衣冠文明，礼仪之邦。洛裳汉服致力于通过规范的服务协议，为您提供安全、透明、专业的传统审美体验。"
              </p>
           </div>
         </div>

         <!-- 右侧：协议正文 (Main Content) -->
         <div class="lg:col-span-3 space-y-12 animate__animated animate__fadeIn">
            <div class="card-premium bg-white p-12 lg:p-24 shadow-2xl rounded-[60px] border border-[var(--lh-border)] relative overflow-hidden">
               <!-- 背景装饰 -->
               <div class="absolute top-0 right-0 p-12 pointer-events-none opacity-[0.03]">
                  <div class="w-96 h-96 border-[40px] border-[var(--lh-navy)] rounded-full -mr-48 -mt-48"></div>
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

               <!-- 签署确认区 -->
               <div class="mt-32 pt-16 border-t border-[var(--lh-navy)]/10 text-center flex flex-col items-center gap-10">
                 <div class="p-8 bg-[var(--lh-cream)] rounded-3xl border border-[var(--lh-border)] max-w-2xl">
                    <p class="text-sm text-[var(--lh-text-secondary)] italic leading-loose opacity-80">
                      洛裳汉服始终尊重并保护用户的合法权益。如您对本协议内容有任何疑问或需要进一步解释，请联系官方客服中心。
                    </p>
                 </div>
                 <button 
                  class="btn-premium px-24 py-6 text-sm tracking-[0.2em] font-serif shadow-2xl hover:scale-105 active:scale-95 transition-all" 
                  @click="$router.back()"
                 >
                   我已阅读并完全同意上述协议条款
                 </button>
               </div>
            </div>
         </div>

      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import HeaderNav from '@/components/common/HeaderNav.vue'

const sections = [
  {
    id: '01',
    title: '服务须知 Overview',
    content: '欢迎使用洛裳汉服官方预约系统。本协议是您（以下称“用户”）与洛裳汉服文化发展平台（以下称“本平台”）之间关于使用本平台各项服务所订立的官方合约。在您开始使用本服务前，请务必审慎阅读并充分理解本协议各项条款。您的预约行为将被视为对本细则的完整接受。'
  },
  {
    id: '02',
    title: '租用规则 Rental Rules',
    content: '<ul class="space-y-4"><li>1. <b>费用支付</b>：用户需按照本平台实时公示的租金及押金标准支付费用。汉服属于贵重文化资产，押金在衣物归还检查无误后，将于 24 小时内按原路全额退回。</li><li>2. <b>资产养护</b>：用户应当如同对待私人藏品般，妥善保管所租赁的汉服及配件。若出现不可修复的机械性损伤、顽固污渍或配件遗失，需按公示原价或相应比例进行修补或赔偿。</li><li>3. <b>时效管理</b>：归还时间需严格遵循预约时段。由于汉服需经过专业消杀和整烫，逾期行为将严重影响后续用户的体验，需按标准补交逾期费。</li></ul>'
  },
  {
    id: '03',
    title: '版权声明 Copyright',
    content: '本平台展示的所有汉服设计方案、高清素材图片、定制游玩攻略文章及视觉资产，其知识产权均归洛裳汉服所有。未经本平台正式权签授权，任何机构或个人不得以此进行二次商业开发、非法转授权或侵权利用。'
  },
  {
    id: '04',
    title: '免责条款 Disclaimer',
    content: '鉴于汉服游玩受环境因素影响较大，若发生恶劣天气、突发公共安全事件或景区临时管制等不可抗力因素，导致拍摄或预约无法按期进行，本平台将全额退还已收取的定金。洛裳汉服对此类由外部环境因素导致的变动，不承担额外的间接商业损失赔偿职责。'
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
