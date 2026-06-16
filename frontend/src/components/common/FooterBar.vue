<template>
  <footer class="bg-[var(--lh-navy)] text-white pt-16 pb-10 relative overflow-hidden font-serif">
    <div class="absolute inset-0 opacity-[0.02] pointer-events-none bg-[url('https://www.transparenttextures.com/patterns/oriental-tiles.png')]"></div>
    <div class="absolute -bottom-24 -right-10 text-[220px] font-black text-white/[0.04] select-none pointer-events-none">洛</div>

    <div class="max-w-7xl mx-auto px-6 relative z-10">
      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-10 text-center md:text-left">
        <div class="space-y-4 flex flex-col items-center md:items-start">
          <div class="flex items-center gap-3">
            <div class="w-11 h-11 border border-[var(--lh-gold)]/70 rounded-lg flex items-center justify-center text-[var(--lh-gold)]">
              <span class="text-xl font-black">裳</span>
            </div>
            <div class="flex flex-col">
              <span class="text-lg font-black tracking-[0.04em]">洛裳汉服</span>
              <span class="text-[11px] text-white/55">洛阳汉服租赁与预约</span>
            </div>
          </div>
          <p class="text-[12px] text-white/70 leading-6 max-w-xs">
            提供线上预约、到店试穿与订单管理服务。具体库存与营业安排以门店当日公示为准。
          </p>
        </div>

        <div>
          <h4 class="text-[12px] font-black tracking-[0.08em] text-[var(--lh-gold)] mb-4">网站导航</h4>
          <ul class="space-y-2 text-[13px] text-white/70">
            <li><router-link to="/hanfu" class="hover:text-[var(--lh-gold)] transition-colors">汉服列表</router-link></li>
            <li><router-link to="/booking" class="hover:text-[var(--lh-gold)] transition-colors">在线预约</router-link></li>

            <li><router-link to="/luoyang/guides" class="hover:text-[var(--lh-gold)] transition-colors">洛阳攻略</router-link></li>
          </ul>
        </div>

        <div>
          <h4 class="text-[12px] font-black tracking-[0.08em] text-[var(--lh-gold)] mb-4">服务支持</h4>
          <ul class="space-y-2 text-[13px] text-white/70">
            <li><router-link to="/agreement" class="hover:text-[var(--lh-gold)] transition-colors">用户协议</router-link></li>
            <li><router-link to="/privacy" class="hover:text-[var(--lh-gold)] transition-colors">隐私政策</router-link></li>
          </ul>
        </div>

        <div class="flex flex-col items-center md:items-start">
          <h4 class="text-[12px] font-black tracking-[0.08em] text-[var(--lh-gold)] mb-4">联系我们</h4>
          <p class="text-2xl font-black tracking-[0.02em] text-white/95">0379-8800-6622</p>
          <p class="text-[12px] text-white/65 mt-1">每日 09:00 - 22:00</p>
          <div class="flex gap-3 mt-5">
            <button
              v-for="i in 3"
              :key="i"
              class="w-9 h-9 border border-white/15 rounded-full flex items-center justify-center hover:bg-[var(--lh-gold)] hover:border-[var(--lh-gold)] hover:text-[var(--lh-navy)] transition-all"
              title="分享"
            >
              <el-icon size="16"><Share /></el-icon>
            </button>
          </div>
        </div>
      </div>

      <div class="mt-10 pt-6 border-t border-white/10 flex flex-col md:flex-row items-center justify-between gap-3">
        <p class="text-[12px] text-white/55 text-center md:text-left">
          © 2026 洛阳汉服预约系统 · 洛裳汉服 · 保留所有权利
        </p>
        <p class="text-[11px] text-white/50">建议预约前先电话确认到店时段</p>
      </div>
    </div>
  </footer>
</template>

<script setup lang="ts">
import { Share } from '@element-plus/icons-vue'
</script>
