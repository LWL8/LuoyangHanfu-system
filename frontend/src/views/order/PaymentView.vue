<template>
  <div class="payment-view min-h-screen bg-[var(--lh-cream)] pb-32 animate__animated animate__fadeIn">
    <HeaderNav />
    
    <div v-show="!isPaid" class="max-w-7xl mx-auto px-6 py-16 flex flex-col items-center">
      <div class="max-w-2xl w-full">
        <!-- 头部标题 -->
        <div class="text-center mb-16">
          <div class="inline-block px-4 py-1 border-2 border-[var(--lh-gold)] mb-6">
            <span class="text-[9px] font-black text-[var(--lh-gold)] tracking-[0.08em]">订单支付</span>
          </div>
          <h1 class="text-5xl font-serif font-black text-[var(--lh-navy)] tracking-[0.08em]">确认支付</h1>
          <div class="h-1 w-12 bg-[var(--lh-red)] mx-auto mt-6 mb-4"></div>
          <p class="text-[10px] text-[var(--lh-text-secondary)] font-black tracking-[0.06em] opacity-50">请在 15 分钟内完成支付，超时订单可能失效。</p>
        </div>

        <!-- 订单概览 -->
        <div class="card-premium bg-white p-12 mb-10 relative overflow-hidden">
           <div class="absolute top-0 right-0 p-8 opacity-[0.03] pointer-events-none transform translate-x-1/4 -translate-y-1/4 rotate-12">
             <el-icon size="200" class="text-[var(--lh-navy)]"><WalletFilled /></el-icon>
           </div>

           <div class="flex flex-col md:flex-row justify-between items-start md:items-center gap-10 mb-12 border-b border-dashed border-[var(--lh-border)] pb-10">
              <div>
                <p class="text-[9px] text-[var(--lh-text-secondary)] font-black tracking-[0.06em] mb-3 opacity-40">订单编号</p>
                <p class="font-mono text-lg text-[var(--lh-navy)] font-black tracking-widest">#{{ order?.orderNo || 'ORD-SYNC-2026' }}</p>
              </div>
              <div class="text-right">
                <p class="text-[9px] text-[var(--lh-text-secondary)] font-black tracking-[0.06em] mb-3 opacity-40">应付金额</p>
                <div class="flex items-baseline justify-end gap-2">
                   <span class="text-sm font-black text-[var(--lh-navy)]">¥</span>
                   <span class="text-5xl font-serif font-black text-[var(--lh-red)] shadow-red-100 text-shadow-sm tracking-tighter">{{ Number(order?.totalAmount || 0).toFixed(2) }}</span>
                </div>
              </div>
           </div>

           <el-collapse class="heritage-collapse">
              <el-collapse-item name="1">
                <template #title>
                   <span class="text-[9px] font-black tracking-[0.06em] text-[var(--lh-gold)]">查看费用明细</span>
                </template>
                <div class="space-y-4 pt-6">
                   <div v-for="h in order?.items || []" :key="h.id + h.size" class="flex justify-between items-center text-[11px] font-black tracking-[0.04em] text-[var(--lh-text-secondary)] group">
                      <span class="opacity-60 group-hover:opacity-100 transition-opacity">{{ h.name }}（{{ h.size }}）x {{ h.quantity }}</span>
                      <span class="text-[var(--lh-navy)]">¥{{ (h.price * h.quantity).toFixed(2) }}</span>
                   </div>
                   <div v-for="s in order?.services || []" :key="s.id" class="flex justify-between items-center text-[11px] font-black tracking-[0.04em] text-[var(--lh-text-secondary)] group">
                      <span class="opacity-60 group-hover:opacity-100 transition-opacity">{{ s.name }}</span>
                      <span class="text-[var(--lh-navy)]">¥{{ s.price.toFixed(2) }}</span>
                   </div>
                </div>
              </el-collapse-item>
           </el-collapse>
        </div>

        <!-- 支付方式 -->
        <div class="card-premium bg-white p-12">
           <h3 class="text-xs font-serif font-black text-[var(--lh-navy)] tracking-[0.1em] mb-10 flex items-center gap-4">
             <span class="w-1.5 h-6 bg-[var(--lh-gold)] rounded-full"></span> 支付方式
           </h3>
           
           <div class="grid grid-cols-1 gap-6">
              <!-- 余额支付 -->
              <div 
                class="p-8 rounded-[30px] border-2 cursor-pointer transition-all flex items-center justify-between group"
                :class="activeMethod === 'balance' ? 'border-[var(--lh-red)] bg-red-50/50 shadow-xl' : 'border-[var(--lh-border)] hover:bg-[var(--lh-cream)] hover:border-[var(--lh-gold)]/40'"
                @click="activeMethod = 'balance'"
              >
                 <div class="flex items-center gap-8">
                    <div class="w-16 h-16 bg-white rounded-2xl flex items-center justify-center text-[var(--lh-gold)] shadow-xl group-hover:scale-110 transition-transform">
                       <el-icon size="32"><Coin /></el-icon>
                    </div>
                    <div>
                       <p class="text-md font-serif font-black text-[var(--lh-navy)] tracking-[0.06em] group-hover:text-[var(--lh-red)] transition-colors">账户余额</p>
                       <p class="text-[9px] text-[var(--lh-text-secondary)] font-black tracking-[0.04em] mt-2 opacity-40">当前余额：¥{{ userStore.userInfo?.newMoney?.toFixed(2) || '888.50' }}</p>
                    </div>
                 </div>
                 <div v-if="activeMethod === 'balance'" class="w-8 h-8 bg-[var(--lh-red)] text-white rounded-full flex items-center justify-center animate-bounce">
                    <el-icon size="20"><Check /></el-icon>
                 </div>
              </div>

              <!-- 到店支付 -->
              <div 
                class="p-8 rounded-[30px] border-2 cursor-pointer transition-all flex items-center justify-between group"
                :class="activeMethod === 'offline' ? 'border-[var(--lh-red)] bg-red-50/50 shadow-xl' : 'border-[var(--lh-border)] hover:bg-[var(--lh-cream)] hover:border-[var(--lh-gold)]/40'"
                @click="activeMethod = 'offline'"
              >
                 <div class="flex items-center gap-8">
                    <div class="w-16 h-16 bg-white rounded-2xl flex items-center justify-center text-[var(--lh-navy)] shadow-xl group-hover:scale-110 transition-transform">
                       <el-icon size="32"><LocationFilled /></el-icon>
                    </div>
                    <div>
                       <p class="text-md font-serif font-black text-[var(--lh-navy)] tracking-[0.06em] group-hover:text-[var(--lh-red)] transition-colors">到店支付</p>
                       <p class="text-[9px] text-[var(--lh-text-secondary)] font-black tracking-[0.04em] mt-2 opacity-40">到店后由门店收款</p>
                    </div>
                 </div>
                 <div v-if="activeMethod === 'offline'" class="w-8 h-8 bg-[var(--lh-red)] text-white rounded-full flex items-center justify-center animate-bounce">
                    <el-icon size="20"><Check /></el-icon>
                 </div>
              </div>
           </div>

           <div class="mt-16 flex flex-col items-center gap-6">
              <button 
                class="btn-premium w-full py-7 text-sm tracking-[0.08em] shadow-[0_30px_60px_rgba(189,49,44,0.3)] hover:scale-[1.02] transition-transform active:scale-95" 
                :disabled="paying" 
                @click="handlePay"
              >
                {{ paying ? '支付处理中...' : `确认支付 ¥${Number(order?.totalAmount || 0).toFixed(2)}` }}
              </button>
              <button class="text-[10px] font-black tracking-[0.06em] text-[var(--lh-text-secondary)] opacity-40 hover:opacity-100 transition-opacity" @click="$router.back()">← 返回修改</button>
           </div>
        </div>
      </div>
    </div>

    <!-- 支付成功状态页 -->
    <div v-show="isPaid" class="max-w-7xl mx-auto h-[calc(100vh-100px)] flex flex-col items-center justify-center animate__animated animate__zoomIn">
      <div class="relative mb-12 group">
         <div class="absolute -inset-10 border-4 border-dashed border-[var(--lh-gold)] rounded-full animate-[spin_30s_linear_infinite] opacity-20"></div>
         <div class="w-40 h-40 bg-white rounded-full flex items-center justify-center text-[var(--lh-red)] shadow-2xl border-8 border-[var(--lh-cream)]">
            <el-icon size="80" class="animate-pulse"><Check /></el-icon>
         </div>
      </div>
      <h2 class="text-5xl font-serif font-black text-[var(--lh-navy)] tracking-[0.08em] mb-4">支付成功</h2>
      <div class="h-1 w-12 bg-[var(--lh-gold)] mb-6"></div>
      <p class="text-[10px] text-[var(--lh-text-secondary)] font-black tracking-[0.06em] max-w-md text-center leading-loose opacity-60 mb-20">
         订单已确认，请按预约时间到店并出示订单信息。
      </p>
      
      <div class="flex flex-col md:flex-row gap-6 w-full max-w-lg px-6">
         <button class="btn-premium flex-1 py-5 text-[10px] tracking-[0.08em] shadow-xl" @click="$router.push('/order')">查看订单</button>
         <button class="flex-1 py-5 border-2 border-[var(--lh-navy)] rounded-2xl text-[10px] font-black tracking-[0.06em] text-[var(--lh-navy)] hover:bg-[var(--lh-navy)] hover:text-white transition-all shadow-lg shadow-black/5" @click="$router.push('/')">返回首页</button>
      </div>

      <!-- 背景水印 -->
      <div class="absolute bottom-20 right-20 text-[200px] font-serif font-black text-[var(--lh-navy)] opacity-[0.02] select-none pointer-events-none italic">成</div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { WalletFilled, Coin, LocationFilled, Check } from '@element-plus/icons-vue'
import HeaderNav from '@/components/common/HeaderNav.vue'
import useUserStore from '@/stores/user'
import { ElMessage } from 'element-plus'
import { fetchOrderDetail, payOrder } from '@/api/order'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const loading = ref(false)
const order = ref<any>(null)

const activeMethod = ref('balance')
const paying = ref(false)
const isPaid = ref(false)

const mapOrderStatus = (status: string) => {
  const val = String(status || '').toUpperCase()
  if (val === 'RESERVED' || val === 'BOOKED' || val === 'CONFIRMED') return 'confirmed'
  if (val === 'USED' || val === 'COMPLETED') return 'used'
  if (val === 'CANCELLED' || val === 'VOID') return 'cancelled'
  if (val === 'PENDING' || val === 'UNPAID') return 'pending'
  return 'pending'
}

const normalizeOrder = (raw: any) => ({
  id: Number(raw.id),
  orderNo: raw.orderNo || `ORD-${raw.id}`,
  totalAmount: Number(raw.totalAmount || raw.totalPrice || 0),
  status: mapOrderStatus(raw.status || raw.payStatus),
  items: raw.items || [
    {
      id: Number(raw.hanfuId || raw.id || 0),
      name: raw.hanfuName || '汉服租赁',
      size: '均码',
      quantity: Number(raw.quantity || 1),
      price: Number(raw.totalAmount || 0)
    }
  ],
  services: raw.services || []
})

const handlePay = async () => {
  if (!order.value) return
  paying.value = true
  try {
    const payType = activeMethod.value === 'balance' ? 'balance' : 'offline'
    const res: any = await payOrder(order.value.id, payType)
    if (!(res?.code === 0 || res?.code === 200)) {
      ElMessage.error(res?.message || '支付失败')
      return
    }
    paying.value = false
    isPaid.value = true
    ElMessage.success(payType === 'offline' ? '已标记为到店支付' : '支付完成')
  } catch {
    ElMessage.error('支付失败')
  } finally {
    paying.value = false
  }
}

onMounted(async () => {
  if (!userStore.isLoggedIn) {
    router.push({ path: '/login', query: { redirect: route.path } })
    return
  }
  loading.value = true
  try {
    const res: any = await fetchOrderDetail(String(route.params.id))
    if ((res?.code === 0 || res?.code === 200) && res?.data) {
      order.value = normalizeOrder(res.data)
    } else {
      ElMessage.error(res?.message || '订单加载失败')
    }
  } catch {
    ElMessage.error('订单加载失败')
  } finally {
    loading.value = false
  }
})
</script>

<style scoped>
.heritage-collapse :deep(.el-collapse-item__header) {
  background: transparent;
  height: 60px;
  border-bottom: 1px dashed var(--lh-border);
}
.heritage-collapse :deep(.el-collapse-item__wrap) {
  background: transparent;
  border: none;
}
.heritage-collapse :deep(.el-collapse) {
  border: none;
}

.payment-view {
  background-attachment: fixed;
}
</style>
