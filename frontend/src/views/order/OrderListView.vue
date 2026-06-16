<template>
  <div class="order-list-view min-h-screen bg-[var(--lh-cream)] pb-32 animate__animated animate__fadeIn">
    <HeaderNav />
    
    <div class="px-6 md:px-12 py-16">
      <div class="flex flex-col md:flex-row md:items-end justify-between mb-12 gap-6">
        <div>
          <div class="flex items-center gap-4 mb-4">
            <div class="w-10 h-1 bg-[var(--lh-red)]"></div>
            <span class="text-[9px] font-black tracking-[0.08em] text-[var(--lh-gold)]">订单中心</span>
          </div>
          <h1 class="text-5xl font-serif font-black text-[var(--lh-navy)] tracking-[0.08em]">我的订单</h1>
        </div>
        
        <!-- 状态筛选 (Heritage Tabs) -->
        <div class="bg-white/50 backdrop-blur-md p-1.5 rounded-2xl border border-[var(--lh-border)] shadow-sm">
          <el-tabs v-model="activeTab" class="heritage-tabs-minimal" @tab-change="handleTabChange">
            <el-tab-pane label="全部" name="all" />
            <el-tab-pane label="待支付" name="pending" />
            <el-tab-pane label="已预约" name="confirmed" />
            <el-tab-pane label="已完成" name="used" />
            <el-tab-pane label="已取消" name="cancelled" />
          </el-tabs>
        </div>
      </div>
      
      <!-- 订单展示区 (Scroll Layout) -->
      <div v-loading="loading" class="order-grid grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 xxl:grid-cols-5 gap-8 items-start">
        <div 
          v-for="order in orders" 
          :key="order.id" 
          class="order-item-anim"
        >
          <OrderCard 
            :item="order" 
            @cancel="showCancelDialog"
            @pay="showPayDialog"
          />
        </div>
      </div>
      
      <!-- 空状态 -->
      <div v-if="!loading && orders.length === 0" class="py-40 flex flex-col items-center justify-center text-center">
        <div class="relative mb-8 group">
           <div class="absolute -inset-8 border-2 border-dashed border-[var(--lh-border)] rounded-full animate-[spin_20s_linear_infinite] opacity-20"></div>
           <div class="w-32 h-32 bg-white rounded-full flex items-center justify-center text-[var(--lh-navy)] shadow-xl group-hover:scale-110 transition-transform">
              <el-icon size="48" class="opacity-10"><Document /></el-icon>
           </div>
        </div>
        <h3 class="text-2xl font-serif font-black text-[var(--lh-navy)] tracking-[0.08em] mb-4 opacity-60">暂无订单</h3>
        <p class="text-[10px] text-[var(--lh-text-secondary)] font-black tracking-[0.06em] opacity-60 mb-10">去挑选汉服并提交预约后，这里会显示订单记录。</p>
        <button 
          class="btn-premium px-12 py-4 text-[10px] tracking-[0.5em]" 
          @click="$router.push('/hanfu')"
        >
          去选汉服
        </button>
      </div>
    </div>

    <!-- 取消订单确认弹窗 -->
    <el-dialog
      v-model="cancelDialogVisible"
      title="取消订单"
      width="500px"
      class="heritage-dialog"
      :show-close="false"
    >
      <div class="p-6 relative">
        <div class="absolute top-0 left-0 w-1 h-full bg-[var(--lh-red)]"></div>
        <p class="text-xs font-serif font-black text-[var(--lh-navy)] tracking-[0.06em] mb-8 leading-relaxed opacity-70">
          取消订单后将释放该时段名额，请选择原因：
        </p>
        
        <el-radio-group v-model="cancelReason" class="heritage-radio-column w-full">
          <el-radio label="行程有变" class="premium-radio">
             <span class="text-[11px] font-black tracking-[0.06em]">行程有变</span>
          </el-radio>
          <el-radio label="信息填写错误" class="premium-radio">
             <span class="text-[11px] font-black tracking-[0.06em]">信息填写错误</span>
          </el-radio>
          <el-radio label="重新挑选款式" class="premium-radio">
             <span class="text-[11px] font-black tracking-[0.06em]">重新挑选款式</span>
          </el-radio>
        </el-radio-group>
      </div>
      <template #footer>
        <div class="flex gap-6 p-6">
          <button class="flex-1 py-4 border-2 border-[var(--lh-border)] rounded-xl text-[10px] font-black tracking-[0.06em] text-[var(--lh-text-secondary)] hover:bg-[var(--lh-cream)] transition-all" @click="cancelDialogVisible = false">暂不取消</button>
          <button class="flex-1 py-4 bg-[var(--lh-red)] text-white rounded-xl text-[10px] font-black tracking-[0.06em] hover:bg-[var(--lh-navy)] transition-all shadow-lg shadow-red-100" :disabled="cancelling" @click="confirmCancel">
             {{ cancelling ? '取消中...' : '确认取消' }}
          </button>
        </div>
      </template>
    </el-dialog>

    <PaymentDialog 
      v-model="payDialogVisible" 
      :order="currentToPay" 
      @success="handlePaySuccess"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { Document } from '@element-plus/icons-vue'
import HeaderNav from '@/components/common/HeaderNav.vue'
import OrderCard from '@/components/order/OrderCard.vue'
import PaymentDialog from '@/components/order/PaymentDialog.vue'
import gsap from 'gsap'
import { ElMessage } from 'element-plus'
import { fetchMyReservations, cancelReservation } from '@/api/order'

const activeTab = ref('all')
const loading = ref(false)
const cancelDialogVisible = ref(false)
const cancelReason = ref('行程有变')
const currentToCancel = ref<any>(null)
const cancelling = ref(false)

// 支付相关
const payDialogVisible = ref(false)
const currentToPay = ref<any>(null)

const allOrders = ref<any[]>([])

const fallbackOrders = [
  {
    id: 1,
    orderNo: 'SN-TRIBUTE-00482',
    hanfuName: '盛唐霓裳 · 绯红',
    hanfuImage: 'https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=400',
    status: 'confirmed',
    bookingDate: '2026-04-10',
    timeSlot: '17:00',
    storeName: '丽景门旗舰店',
    totalAmount: 299,
    items: [
       { id: 1, name: '盛唐霓裳 · 绯红', image: 'https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=400', size: 'M', quantity: 1, price: 299 }
    ],
    createTime: '2026-04-07 10:30'
  },
  {
    id: 2,
    orderNo: 'SN-TRIBUTE-00483',
    hanfuName: '洛水之灵 · 翠微',
    hanfuImage: 'https://images.unsplash.com/photo-1574169208507-84376144848b?w=400',
    status: 'pending',
    bookingDate: '2026-04-12',
    timeSlot: '10:00',
    storeName: '洛邑古城旗舰店',
    totalAmount: 399,
    items: [
       { id: 2, name: '洛水之灵 · 翠微', image: 'https://images.unsplash.com/photo-1574169208507-84376144848b?w=400', size: 'L', quantity: 1, price: 399 }
    ],
    createTime: '2026-04-07 11:15'
  }
]

const mapOrderStatus = (status: string) => {
  const val = String(status || '').toUpperCase()
  if (val === 'RESERVED' || val === 'BOOKED' || val === 'CONFIRMED') return 'confirmed'
  if (val === 'USED' || val === 'COMPLETED') return 'used'
  if (val === 'CANCELLED' || val === 'VOID') return 'cancelled'
  if (val === 'PENDING' || val === 'UNPAID') return 'pending'
  return 'pending'
}

const normalizeOrder = (raw: any) => {
  const status = mapOrderStatus(raw.status || raw.payStatus)
  return {
    id: Number(raw.id),
    orderNo: raw.orderNo || `ORD-${raw.id}`,
    hanfuName: raw.hanfuName || '汉服租赁',
    hanfuImage: raw.hanfuPhoto || 'https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=400',
    status,
    bookingDate: raw.reservationDate || raw.bookingDate || '',
    timeSlot: raw.timeSlot || '',
    storeName: raw.storeName || '洛阳门店',
    totalAmount: Number(raw.totalAmount || raw.totalPrice || 0),
    items: raw.items || [
      {
        id: Number(raw.hanfuId || raw.id || 0),
        name: raw.hanfuName || '汉服租赁',
        image: raw.hanfuPhoto || 'https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=400',
        size: '均码',
        quantity: Number(raw.quantity || 1),
        price: Number(raw.totalAmount || raw.totalPrice || 0)
      }
    ],
    createTime: raw.createTime || ''
  }
}

const orders = computed(() => {
  if (activeTab.value === 'all') return allOrders.value
  return allOrders.value.filter(o => o.status === activeTab.value)
})

const handleTabChange = () => {
  animateList()
}

const showPayDialog = (order: any) => {
  currentToPay.value = order
  payDialogVisible.value = true
}

const handlePaySuccess = () => {
  // 更新本地状态
  const idx = allOrders.value.findIndex(o => o.id === currentToPay.value.id)
  if (idx > -1) {
    allOrders.value[idx].status = 'confirmed'
  }
}

const confirmCancel = async () => {
  if (!currentToCancel.value) return
  cancelling.value = true
  try {
    const res: any = await cancelReservation(currentToCancel.value.id, cancelReason.value)
    if (!(res?.code === 200 || res?.code === 0)) {
      ElMessage.error(res?.message || '取消失败')
      cancelling.value = false
      return
    }
    const idx = allOrders.value.findIndex(o => o.id === currentToCancel.value.id)
    if (idx > -1) {
      allOrders.value[idx].status = 'cancelled'
    }
    cancelDialogVisible.value = false
    ElMessage.success('订单已取消')
  } catch (e: any) {
    ElMessage.error(e?.response?.data?.message || '取消失败')
  } finally {
    cancelling.value = false
  }
}

const animateList = () => {
  gsap.from('.order-item-anim', {
    y: 40,
    opacity: 0,
    stagger: 0.15,
    duration: 1,
    ease: 'expo.out'
  })
}

onMounted(() => {
  ;(async () => {
    loading.value = true
    try {
      const res: any = await fetchMyReservations()
      if ((res?.code === 200 || res?.code === 0) && Array.isArray(res?.data)) {
        allOrders.value = res.data.map(normalizeOrder)
      } else if ((res?.code === 200 || res?.code === 0) && res?.data?.list) {
        allOrders.value = res.data.list.map(normalizeOrder)
      } else {
        allOrders.value = fallbackOrders
      }
    } catch {
      allOrders.value = fallbackOrders
    } finally {
      loading.value = false
      animateList()
    }
  })()
})
</script>

<style scoped>
.heritage-tabs-minimal :deep(.el-tabs__header) {
  margin-bottom: 0;
  border-bottom: none;
}
.heritage-tabs-minimal :deep(.el-tabs__nav-wrap::after) {
  display: none;
}
.heritage-tabs-minimal :deep(.el-tabs__item) {
   font-family: var(--font-serif);
   font-weight: 900;
   font-size: 11px;
   letter-spacing: 0.15em;
   color: var(--lh-text-secondary);
   height: 48px;
   padding: 0 24px !important;
   transition: all 0.4s;
}
.heritage-tabs-minimal :deep(.el-tabs__item.is-active) {
   color: var(--lh-red) !important;
   transform: scale(1.1);
}
.heritage-tabs-minimal :deep(.el-tabs__active-bar) {
   background-color: var(--lh-red);
   height: 3px;
   border-radius: 2px;
}

.heritage-radio-column {
   display: flex;
   flex-direction: column;
   gap: 16px;
}
.premium-radio :deep(.el-radio__label) {
   padding-left: 12px;
}
.premium-radio :deep(.el-radio__input.is-checked .el-radio__inner) {
   background-color: var(--lh-red);
   border-color: var(--lh-red);
}

.order-list-view {
  background-attachment: fixed;
}

</style>
