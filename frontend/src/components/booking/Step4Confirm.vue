<template>
  <div class="step4-confirm animate__animated animate__fadeIn">
    <div class="grid grid-cols-1 lg:grid-cols-12 gap-10 items-start">
      
      <!-- 左侧: 订单预览 -->
      <div class="lg:col-span-8 flex flex-col gap-10">
        
        <!-- 核心摘要卡 -->
        <div class="card-premium p-10 bg-white relative overflow-hidden">
          <!-- 装饰水印 -->
          <div class="absolute top-0 right-0 text-[160px] font-serif opacity-[0.03] select-none pointer-events-none transform translate-x-1/4 -translate-y-1/4">订单</div>
          
          <h3 class="text-2xl font-serif font-bold mb-12 flex items-center gap-3 text-[var(--lh-navy)] relative z-10">
            <el-icon class="text-[var(--lh-gold)]"><Notebook /></el-icon> 
            <span>预订摘要</span>
          </h3>
          
          <!-- 预约行程 -->
          <div class="flex flex-wrap gap-12 mb-12 pb-10 border-b border-[var(--lh-border)] border-dashed relative z-10">
            <div class="flex items-center gap-5">
              <div class="w-14 h-14 rounded-2xl bg-[var(--lh-cream)] flex items-center justify-center text-[var(--lh-red)] shadow-sm border border-[var(--lh-border)]">
                <el-icon size="28"><Calendar /></el-icon>
              </div>
              <div>
                 <p class="text-[10px] text-[var(--lh-text-secondary)] tracking-[0.08em] font-bold mb-1">预约日期</p>
                 <p class="text-xl font-serif font-bold text-[var(--lh-navy)]">{{ store.date }}</p>
              </div>
            </div>
            <div class="flex items-center gap-5">
              <div class="w-14 h-14 rounded-2xl bg-[var(--lh-cream)] flex items-center justify-center text-[var(--lh-red)] shadow-sm border border-[var(--lh-border)]">
                <el-icon size="28"><Clock /></el-icon>
              </div>
              <div>
                 <p class="text-[10px] text-[var(--lh-text-secondary)] tracking-[0.08em] font-bold mb-1">预约时段</p>
                 <p class="text-xl font-serif font-bold text-[var(--lh-navy)]">{{ store.timeSlot || '未选择' }}</p>
              </div>
            </div>
          </div>

          <!-- 已选内容 -->
          <div class="space-y-8 mb-12 pb-10 border-b border-[var(--lh-border)] border-dashed relative z-10">
             <div v-if="primaryHanfu" class="flex gap-6 p-4 rounded-xl hover:bg-[var(--lh-cream)] transition-colors">
                <div class="w-20 h-28 shrink-0 overflow-hidden rounded-lg shadow-md border border-white">
                  <img :src="primaryHanfu.image" class="w-full h-full object-cover" />
                </div>
                <div class="flex-1 flex flex-col justify-center">
                  <div class="flex justify-between font-serif font-bold text-xl text-[var(--lh-navy)] mb-2">
                    <span>{{ primaryHanfu.name }}</span>
                    <span class="text-[var(--lh-red)]">¥{{ (primaryHanfu.price * primaryHanfu.quantity).toFixed(2) }}</span>
                  </div>
                  <div class="flex gap-6 text-[10px] text-[var(--lh-text-secondary)] font-bold tracking-[0.04em] opacity-70">
                    <span class="flex items-center gap-1"><span class="w-1.5 h-1.5 bg-[var(--lh-gold)] rounded-full"></span> 租期 1 天</span>
                    <span>门店常规租赁</span>
                  </div>
                </div>
             </div>
             
             <div v-if="store.selectedServices.length > 0" class="space-y-4">
               <p class="text-[10px] font-bold tracking-[0.08em] text-[var(--lh-text-secondary)] opacity-60 ml-4">附加服务</p>
               <div v-for="s in store.selectedServices" :key="s.id" class="flex items-center justify-between p-4 bg-[var(--lh-cream)]/30 rounded-xl hover:bg-[var(--lh-cream)] transition-colors border border-transparent hover:border-[var(--lh-border)]">
                  <span class="text-sm font-serif font-bold text-[var(--lh-navy)] flex items-center gap-3">
                    <el-icon class="text-[var(--lh-gold)]"><CircleCheckFilled /></el-icon>
                    {{ s.name }}
                  </span>
                  <span class="text-base font-bold text-[var(--lh-navy)]">¥{{ s.price }}</span>
               </div>
             </div>
          </div>

          <!-- 费用总结 -->
          <div class="space-y-4 p-6 bg-[var(--lh-navy)] rounded-2xl text-white relative shadow-2xl">
            <div class="flex justify-between items-center opacity-60">
              <span class="text-xs tracking-[0.06em] font-serif">小计</span>
              <span class="font-bold">¥{{ store.grandTotal.toFixed(2) }}</span>
            </div>
            <div class="flex justify-between items-center pt-6 border-t border-white/10">
              <span class="text-lg font-serif font-bold tracking-[0.08em]">应付总额</span>
              <div class="flex items-baseline gap-1">
                <span class="text-sm font-bold text-[var(--lh-gold)]">¥</span>
                <span class="text-4xl font-serif font-bold text-[var(--lh-gold)] tracking-tighter">{{ store.grandTotal.toFixed(2) }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 右侧: 使用人信息 -->
      <div class="lg:col-span-4 sticky top-24">
        <div class="card-premium p-8 bg-white border-t-8 border-t-[var(--lh-navy)]">
           <h3 class="text-xl font-serif font-bold mb-10 text-[var(--lh-navy)] tracking-widest text-center">使用人资料</h3>
           
           <el-form :model="store.userInfo" label-position="top" class="premium-form">
              <el-form-item label="姓名" required>
                <el-input v-model="store.userInfo.name" placeholder="取件人姓名" class="heritage-input" />
              </el-form-item>
              <el-form-item label="电话" required>
                <el-input v-model="store.userInfo.phone" placeholder="接收预约通知" class="heritage-input" />
              </el-form-item>
              <el-form-item label="备注">
                <el-input v-model="store.userInfo.remark" type="textarea" :rows="3" placeholder="特别尺码或其他需求..." class="heritage-textarea" />
              </el-form-item>
           </el-form>

           <div class="my-8">
              <el-collapse class="heritage-collapse">
                 <el-collapse-item title="预约须知（必读）" name="1">
                    <ul class="text-[10px] text-[var(--lh-text-secondary)] space-y-3 leading-relaxed tracking-[0.04em] font-bold">
                      <li class="flex items-start gap-2"><span class="text-[var(--lh-red)]">※</span> 服装归还时间为入店当日 21:00 前。</li>
                      <li class="flex items-start gap-2"><span class="text-[var(--lh-red)]">※</span> 衣物破损或配饰丢失需根据店规承担折旧费用。</li>
                      <li class="flex items-start gap-2"><span class="text-[var(--lh-red)]">※</span> 高峰期建议提前15分钟到店，以便挑选细节。</li>
                      <li class="flex items-start gap-2"><span class="text-[var(--lh-red)]">※</span> 取消政策：距预约时间 48 小时前可申请全额退订。</li>
                    </ul>
                 </el-collapse-item>
              </el-collapse>
           </div>

           <div class="mb-10 flex items-center gap-3">
              <el-checkbox v-model="agreed" />
              <span class="text-[10px] font-bold text-[var(--lh-text-secondary)] tracking-[0.04em] cursor-pointer hover:text-[var(--lh-navy)]" @click="agreed = !agreed">
                我已阅读并同意预约须知
              </span>
           </div>

           <div class="flex flex-col gap-4">
              <button 
                class="btn-premium w-full py-5 text-lg font-serif tracking-[0.08em] shadow-2xl disabled:opacity-30 disabled:cursor-not-allowed transform transition-all active:scale-95" 
                :disabled="!agreed || submitting"
                @click="handleSubmit"
              >
                {{ submitting ? '提交中...' : '确认预约' }}
              </button>
              <button class="text-[10px] font-bold tracking-[0.06em] text-[var(--lh-text-secondary)] hover:text-[var(--lh-red)] transition-colors py-2" @click="$emit('prev')">
                返回修改
              </button>
           </div>
        </div>
      </div>
    </div>

    <!-- 预约成功弹窗 -->
    <el-dialog
      v-model="showSuccess"
      width="500px"
      :show-close="false"
      center
      class="heritage-dialog rounded-[40px] overflow-hidden"
    >
      <div class="text-center py-10 px-8 relative bg-[var(--lh-cream)]">
        <!-- 成功动效 -->
        <div class="w-24 h-24 bg-white rounded-full flex items-center justify-center mx-auto mb-10 text-[var(--lh-gold)] border-2 border-[var(--lh-gold)] shadow-xl animate__animated animate__zoomIn">
          <el-icon size="56" class="font-bold"><Check /></el-icon>
        </div>
        
        <h2 class="text-4xl font-serif font-bold text-[var(--lh-navy)] mb-4 tracking-[0.2em]">预约成功</h2>
        <p class="text-[10px] font-bold text-[var(--lh-text-secondary)] tracking-[0.08em] mb-8">订单已生成，请按预约时间到店</p>
        
        <div class="bg-white/80 p-6 rounded-2xl border border-[var(--lh-border)] mb-10 text-left">
           <div class="flex justify-between items-center mb-3">
             <span class="text-xs opacity-40 font-bold tracking-[0.06em]">订单号</span>
             <span class="font-mono text-[var(--lh-navy)] font-bold">#{{ orderNo }}</span>
           </div>
           <p class="text-xs text-[var(--lh-navy)] opacity-60 leading-relaxed font-medium">
             预约信息已提交成功，请保留订单号，到店时出示即可。
           </p>
        </div>
        
        <div class="flex flex-col gap-4">
          <button class="btn-premium w-full py-5 text-base tracking-[0.3em] font-serif" @click="goOrders">
            查看我的订单
          </button>
          <button class="text-sm font-bold tracking-[0.06em] text-[var(--lh-text-secondary)] hover:text-[var(--lh-navy)] transition-colors pt-2" @click="goHome">
            返回首页
          </button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue'
import { useRouter } from 'vue-router'
import { Notebook, Calendar, Clock, Check, CircleCheckFilled } from '@element-plus/icons-vue'
import { useBookingStore } from '@/stores/booking'
import { createReservation } from '@/api/order'
import { ElMessage } from 'element-plus'

const router = useRouter()
const store = useBookingStore()
defineEmits(['prev'])

const agreed = ref(false)
const submitting = ref(false)
const showSuccess = ref(false)
const orderNo = ref('')
const primaryHanfu = computed(() => store.selectedHanfuList[0] || null)

const handleSubmit = async () => {
  if (!primaryHanfu.value) {
    ElMessage.error('请先选择汉服')
    return
  }
  if (!store.userInfo.name || !store.userInfo.phone) {
    ElMessage.error('请填写姓名和联系方式')
    return
  }
  if (!/^1[3-9]\d{9}$/.test(String(store.userInfo.phone || '').replace(/\D/g, '').slice(0, 11))) {
    ElMessage.error('手机号格式不正确')
    return
  }
  if (!agreed.value) {
    ElMessage.warning('请先同意预约须知')
    return
  }

  submitting.value = true
  try {
    const res = await createReservation({
      hanfuId: primaryHanfu.value.id,
      reservationDate: store.date,
      timeSlot: store.timeSlot,
      quantity: Number(primaryHanfu.value.quantity || 1),
      customerName: store.userInfo.name,
      customerPhone: String(store.userInfo.phone || '').replace(/\D/g, '').slice(0, 11),
      remark: store.userInfo.remark,
      serviceIds: store.selectedServices.map((s: any) => Number(s.id)).filter((x: number) => Number.isFinite(x))
    })
    
    if (res && ((res as any).code === 0 || (res as any).code === 200)) {
      orderNo.value = (res as any).data?.orderNo || (res as any).data || ''
      showSuccess.value = true
    } else {
      ElMessage.error((res as any).message || '预约失败，请稍后再试')
    }
  } catch {
    ElMessage.error('提交失败，请稍后再试')
  } finally {
    submitting.value = false
  }
}

const goOrders = () => {
  store.reset()
  router.push('/order')
}

const goHome = () => {
  store.reset()
  router.push('/')
}
</script>

<style scoped>
.heritage-input :deep(.el-input__wrapper),
.heritage-textarea :deep(.el-textarea__inner) {
  border-radius: 12px;
  background: var(--lh-cream);
  border: 1px solid var(--lh-border);
  box-shadow: none;
  font-family: var(--font-serif);
}

.premium-form :deep(.el-form-item__label) {
  font-family: var(--font-serif);
  font-weight: 900;
  color: var(--lh-navy);
  letter-spacing: 0.1em;
  padding-bottom: 8px;
}

.heritage-collapse :deep(.el-collapse-item__header) {
  background: transparent;
  color: var(--lh-red);
  font-family: var(--font-serif);
  font-weight: bold;
}
</style>
