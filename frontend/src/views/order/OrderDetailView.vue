<template>
  <div class="order-detail-view min-h-screen bg-[var(--lh-cream)] pb-32 animate__animated animate__fadeIn">
    <HeaderNav />
    
    <div v-if="loading && !order" class="max-w-7xl mx-auto py-40 flex flex-col items-center justify-center gap-6">
      <div class="relative">
         <div class="w-16 h-16 border-4 border-[var(--lh-border)] rounded-full border-t-[var(--lh-red)] animate-spin"></div>
         <div class="absolute inset-0 flex items-center justify-center text-[10px] font-black text-[var(--lh-navy)]">洛</div>
      </div>
      <p class="text-[10px] font-black tracking-[0.06em] text-[var(--lh-text-secondary)] opacity-50">正在加载订单详情...</p>
    </div>

    <template v-else-if="order">
      <div class="max-w-7xl mx-auto px-6 py-12">
        <!-- 导航与标题 -->
        <div class="mb-12 flex flex-col md:flex-row md:items-end justify-between gap-6">
          <div>
             <div class="flex items-center gap-4 mb-4 opacity-40">
                <router-link to="/order" class="text-[10px] font-black tracking-[0.06em] text-[var(--lh-navy)] hover:text-[var(--lh-red)] transition-colors">← 返回订单列表</router-link>
             </div>
             <h1 class="text-4xl font-serif font-black text-[var(--lh-navy)] tracking-[0.08em]">订单详情</h1>
          </div>
          <div class="flex items-center gap-4">
             <span class="text-[10px] font-mono font-black text-[var(--lh-text-secondary)] opacity-40">订单ID: {{ order.id }}</span>
          </div>
        </div>

        <div class="grid grid-cols-1 lg:grid-cols-12 gap-10 items-start">
          <!-- 左侧: 状态演进 (Status Scroll) (4/12) -->
          <div class="lg:col-span-4 space-y-8 sticky top-24">
            <div class="card-premium bg-white p-10 relative overflow-hidden border-t-8 border-t-[var(--lh-red)]">
              <!-- 背景装饰 -->
              <div class="absolute top-4 right-4 text-6xl font-serif font-black text-[var(--lh-navy)] opacity-[0.03] select-none italic">状</div>
              
              <div class="text-center mb-10">
                 <p class="text-[9px] font-black tracking-[0.06em] text-[var(--lh-text-secondary)] mb-3 opacity-50">当前状态</p>
                 <h2 class="text-3xl font-serif font-black tracking-[0.08em]" :class="statusColor">
                   {{ statusLabel }}
                 </h2>
              </div>
              
              <!-- 流程轴 (Imperial Timeline) -->
              <div class="space-y-10 relative pl-8 before:absolute before:left-2.5 before:top-2 before:bottom-2 before:w-[1px] before:bg-[var(--lh-border)] before:bg-dashed">
                 <div v-for="(step, idx) in timelineSteps" :key="idx" class="relative group">
                    <!-- 节点圆点 -->
                    <div 
                      class="absolute -left-8 top-1.5 w-5 h-5 rounded-full border-2 bg-white transition-all duration-500 z-10" 
                      :class="step.completed ? 'border-[var(--lh-red)] scale-110 shadow-[0_0_10px_var(--lh-red)]' : 'border-[var(--lh-border)] opacity-30'"
                    >
                       <div v-if="step.completed" class="absolute inset-1 bg-[var(--lh-red)] rounded-full"></div>
                    </div>
                    
                    <div :class="step.completed ? 'opacity-100' : 'opacity-30'">
                       <h4 class="text-xs font-serif font-black text-[var(--lh-navy)] tracking-[0.06em] mb-1">{{ step.title }}</h4>
                       <p class="text-[9px] font-black tracking-[0.04em] text-[var(--lh-text-secondary)]">{{ step.time }}</p>
                    </div>
                 </div>
              </div>
            </div>
            
            <!-- 行动指令 (Action Triggers) -->
            <div class="space-y-4">
               <button v-if="order.status === 'pending'" class="btn-premium w-full py-6 text-sm tracking-[0.08em] shadow-[0_20px_50px_rgba(189,49,44,0.3)]" @click="goPayment">立即支付</button>
               <button v-if="['pending', 'confirmed'].includes(order.status)" class="w-full py-5 border-2 border-[var(--lh-red)] rounded-2xl text-[10px] font-black tracking-[0.06em] text-[var(--lh-red)] hover:bg-[var(--lh-red)] hover:text-white transition-all" @click="showCancelDialog">取消订单</button>
               <button v-if="order.status === 'used'" class="w-full py-5 border-2 border-[var(--lh-navy)] rounded-2xl text-[10px] font-black tracking-[0.06em] text-[var(--lh-navy)] hover:bg-[var(--lh-navy)] hover:text-white transition-all" @click="$router.push('/hanfu')">再次下单</button>
            </div>
          </div>

          <!-- 右侧: 卷宗详情 (Order Dossier) (8/12) -->
          <div class="lg:col-span-8 space-y-10">
            <!-- 预约核心信息 -->
            <div class="card-premium bg-white p-12">
               <h3 class="text-xs font-serif font-black text-[var(--lh-navy)] tracking-[0.1em] mb-10 flex items-center gap-4">
                 <span class="w-1.5 h-6 bg-[var(--lh-red)] rounded-full"></span> 预约信息
               </h3>
               
               <div class="grid grid-cols-1 md:grid-cols-2 gap-12">
                  <div class="flex items-start gap-6 group">
                     <div class="w-14 h-14 bg-[var(--lh-cream)] rounded-2xl flex items-center justify-center text-[var(--lh-red)] border border-[var(--lh-border)] shadow-xl group-hover:scale-110 transition-transform">
                        <el-icon size="24"><Shop /></el-icon>
                     </div>
                     <div>
                        <p class="text-[9px] text-[var(--lh-text-secondary)] font-black tracking-[0.06em] mb-2 opacity-50">门店地址</p>
                        <p class="text-lg font-serif font-black text-[var(--lh-navy)] tracking-[0.06em] mb-1">{{ order.storeName }}</p>
                        <p class="text-[10px] font-bold text-[var(--lh-text-secondary)] opacity-60 leading-relaxed">{{ order.storeAddress || '洛阳市老城区丽景门旗舰店' }}</p>
                     </div>
                  </div>
                  
                  <div class="bg-[var(--lh-cream)] p-8 rounded-3xl border border-[var(--lh-border)] grid grid-cols-2 gap-8 relative overflow-hidden">
                     <div class="absolute -bottom-4 -right-4 opacity-[0.03] rotate-12">
                        <el-icon size="80" class="text-[var(--lh-navy)]"><Clock /></el-icon>
                     </div>
                     <div>
                        <p class="text-[9px] text-[var(--lh-text-secondary)] font-black tracking-[0.06em] mb-2 opacity-50">预约日期</p>
                        <p class="text-md font-serif font-black text-[var(--lh-navy)] tracking-[0.06em]">{{ order.bookingDate }}</p>
                     </div>
                     <div>
                        <p class="text-[9px] text-[var(--lh-text-secondary)] font-black tracking-[0.06em] mb-2 opacity-50">预约时段</p>
                        <p class="text-md font-serif font-black text-[var(--lh-red)] tracking-[0.06em]">{{ order.timeSlot }}</p>
                     </div>
                  </div>
               </div>

               <div class="mt-12 p-6 bg-[var(--lh-navy)] rounded-2xl flex items-center justify-between group cursor-help">
                  <div class="flex items-center gap-4">
                     <el-icon class="text-[var(--lh-gold)] animate-pulse" size="20"><Notification /></el-icon>
                     <p class="text-[10px] font-black tracking-[0.06em] text-white/80">温馨提示：请按预约时段准时到店。</p>
                  </div>
                  <el-icon class="text-white/20 group-hover:text-white transition-colors" size="14"><QuestionFilled /></el-icon>
               </div>
            </div>

            <!-- 费用与清单 (Manifest) -->
            <div class="card-premium bg-white p-12 relative overflow-hidden">
               <!-- 水印 -->
               <div class="absolute -top-10 -right-10 text-9xl font-serif font-black text-[var(--lh-navy)] opacity-[0.02] select-none pointer-events-none italic">财</div>
               
               <h3 class="text-xs font-serif font-black text-[var(--lh-navy)] tracking-[0.1em] mb-10 flex items-center gap-4">
                 <span class="w-1.5 h-6 bg-[var(--lh-gold)] rounded-full"></span> 费用清单
               </h3>
               
               <div class="space-y-10">
                  <div class="flex gap-8 group">
                     <div class="w-24 h-32 relative rounded-2xl overflow-hidden shadow-2xl border-2 border-[var(--lh-border)] group-hover:rotate-3 transition-transform">
                        <img :src="order.hanfuImage" class="w-full h-full object-cover" />
                        <div class="absolute inset-0 bg-gradient-to-t from-[var(--lh-navy)]/40 to-transparent"></div>
                     </div>
                     <div class="flex-1 flex flex-col justify-between py-2">
                        <div>
                           <h4 class="text-xl font-serif font-black text-[var(--lh-navy)] tracking-[0.06em] mb-3 group-hover:text-[var(--lh-red)] transition-colors">{{ order.hanfuName }}</h4>
                           <div class="flex gap-4">
                              <span class="px-3 py-1 bg-[var(--lh-cream)] rounded-lg text-[9px] font-black tracking-[0.04em] text-[var(--lh-text-secondary)]">尺码：均码</span>
                              <span class="px-3 py-1 bg-[var(--lh-cream)] rounded-lg text-[9px] font-black tracking-[0.04em] text-[var(--lh-text-secondary)]">数量：1</span>
                           </div>
                        </div>
                        <p class="text-lg font-serif font-black text-[var(--lh-red)]">¥{{ Number(order.totalAmount || 0).toFixed(2) }}</p>
                     </div>
                  </div>
                  
                  <div class="pt-10 border-t border-dashed border-[var(--lh-border)] flex justify-between items-end">
                     <div>
                        <p class="text-[9px] text-[var(--lh-text-secondary)] font-black tracking-[0.06em] opacity-40 mb-3">总金额</p>
                        <span class="text-xs font-black text-[var(--lh-navy)] tracking-[0.04em] opacity-40">已含基础服务费用</span>
                     </div>
                     <div class="flex items-baseline gap-3">
                        <span class="text-xs font-black text-[var(--lh-navy)]">¥</span>
                        <span class="text-5xl font-serif font-black text-[var(--lh-red)] shadow-red-100 text-shadow-sm tracking-tighter">{{ Number(order.totalAmount || 0).toFixed(2) }}</span>
                     </div>
                  </div>
               </div>
            </div>

            <!-- 使用人资料 (Resident Identity) -->
            <div class="card-premium bg-white p-12">
               <h3 class="text-xs font-serif font-black text-[var(--lh-navy)] tracking-[0.1em] mb-10 flex items-center gap-4">
                 <span class="w-1.5 h-6 bg-[var(--lh-navy)] rounded-full"></span> 使用人信息
               </h3>
               
               <div class="grid grid-cols-1 md:grid-cols-2 gap-10">
                  <div class="p-8 bg-[var(--lh-cream)] rounded-3xl border-2 border-dashed border-[var(--lh-border)] group">
                     <p class="text-[9px] text-[var(--lh-text-secondary)] font-black tracking-[0.06em] mb-3 opacity-40">姓名</p>
                     <p class="text-lg font-serif font-black text-[var(--lh-navy)] tracking-[0.06em] group-hover:text-[var(--lh-red)] transition-colors">{{ userStore.userInfo?.yonghuName }}</p>
                  </div>
                  <div class="p-8 bg-[var(--lh-cream)] rounded-3xl border-2 border-dashed border-[var(--lh-border)] group">
                     <p class="text-[9px] text-[var(--lh-text-secondary)] font-black tracking-[0.06em] mb-3 opacity-40">联系电话</p>
                     <p class="text-lg font-mono font-black text-[var(--lh-navy)] tracking-[0.06em] group-hover:text-[var(--lh-red)] transition-colors">{{ userStore.userInfo?.yonghuPhone }}</p>
                  </div>
                  <div class="md:col-span-2 p-8 bg-[var(--lh-cream)] rounded-3xl border-2 border-dashed border-[var(--lh-border)] group">
                     <p class="text-[9px] text-[var(--lh-text-secondary)] font-black tracking-[0.06em] mb-3 opacity-40">备注</p>
                     <p class="text-xs font-serif font-black text-[var(--lh-navy)]/60 leading-relaxed tracking-[0.04em]">暂无特别需求</p>
                  </div>
               </div>
            </div>
          </div>
        </div>
      </div>
    </template>

    <!-- 取消确认 (Revoke Decree) -->
    <el-dialog v-model="cancelDialogVisible" title="撤回确定" width="450px" class="heritage-dialog" :show-close="false">
      <div class="p-8 text-center">
         <div class="w-16 h-16 bg-red-50 rounded-full flex items-center justify-center mx-auto mb-8 text-[var(--lh-red)]">
            <el-icon size="32"><Warning /></el-icon>
         </div>
         <h4 class="text-xl font-serif font-black text-[var(--lh-navy)] tracking-widest mb-4">确定撤回此预约吗？</h4>
         <p class="text-[10px] font-black tracking-[0.06em] text-[var(--lh-text-secondary)] opacity-60 mb-10 leading-loose">
            取消后该时段将释放给其他用户，请确认是否继续。
         </p>
         
         <div class="flex gap-4">
            <button class="flex-1 py-4 border-2 border-[var(--lh-border)] rounded-xl text-[10px] font-black tracking-[0.06em] text-[var(--lh-text-secondary)] hover:bg-[var(--lh-cream)] transition-all" @click="cancelDialogVisible = false">暂不取消</button>
            <button class="flex-1 py-4 bg-[var(--lh-red)] text-white rounded-xl text-[10px] font-black tracking-[0.06em] hover:bg-[var(--lh-navy)] transition-all shadow-xl" :disabled="cancelling" @click="confirmCancel">确认取消</button>
         </div>
      </div>
    </el-dialog>

    <PaymentDialog 
      v-model="payDialogVisible"
      :order="order"
      @success="handlePaySuccess"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { Shop, Clock, Notification, QuestionFilled, Warning } from '@element-plus/icons-vue'
import HeaderNav from '@/components/common/HeaderNav.vue'
import PaymentDialog from '@/components/order/PaymentDialog.vue'
import useUserStore from '@/stores/user'
import { ElMessage } from 'element-plus'
import { fetchReservationDetail, cancelReservation } from '@/api/order'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const loading = ref(true)
const order = ref<any>(null)
const cancelDialogVisible = ref(false)
const payDialogVisible = ref(false)
const cancelling = ref(false)

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
  hanfuName: raw.hanfuName || '汉服租赁',
  hanfuImage: raw.hanfuPhoto || raw.items?.[0]?.image || 'https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=800',
  status: mapOrderStatus(raw.status || raw.payStatus),
  bookingDate: raw.reservationDate || raw.bookingDate || '',
  timeSlot: raw.timeSlot || '',
  storeName: raw.storeName || '洛阳门店',
  storeAddress: raw.storeAddress || '洛阳市老城区丽景门旗舰店',
  totalAmount: Number(raw.totalAmount || raw.totalPrice || 0),
  createdAt: raw.createTime || ''
})

const loadData = async () => {
  loading.value = true
  try {
    const res: any = await fetchReservationDetail(String(route.params.id))
    if ((res?.code === 0 || res?.code === 200) && res?.data) {
      order.value = normalizeOrder(res.data)
    } else {
      ElMessage.error(res?.message || '订单详情加载失败')
    }
  } catch {
    ElMessage.error('订单详情加载失败')
  } finally {
    loading.value = false
  }
}

const statusLabel = computed(() => {
  const labels: any = { pending: '待支付', confirmed: '已预约', used: '已完成', cancelled: '已取消' }
  return labels[order.value?.status] || '解析中...'
})

const statusColor = computed(() => {
  const colors: any = { pending: 'text-[var(--lh-gold)]', confirmed: 'text-[var(--lh-red)]', used: 'text-[var(--lh-navy)]', cancelled: 'text-[var(--lh-text-secondary)] opacity-40' }
  return colors[order.value?.status] || ''
})

const timelineSteps = computed(() => {
  if (!order.value) return []
  const s = order.value.status
  return [
    { title: '订单创建', time: order.value.createdAt, completed: true },
    { title: '完成支付', time: s !== 'pending' ? '支付成功' : '待支付', completed: s !== 'pending' && s !== 'cancelled' },
    { title: '预约确认', time: s === 'confirmed' || s === 'used' ? '门店已确认' : '待确认', completed: s === 'confirmed' || s === 'used' },
    { title: '到店完成', time: s === 'used' ? '已完成' : '待到店', completed: s === 'used' }
  ]
})

const goPayment = () => {
  payDialogVisible.value = true
}

const handlePaySuccess = () => {
  order.value.status = 'confirmed'
}
const showCancelDialog = () => cancelDialogVisible.value = true
const confirmCancel = () => {
  cancelling.value = true
  cancelReservation(order.value.id, '订单详情页取消').then((res: any) => {
    if (!(res?.code === 0 || res?.code === 200)) {
      ElMessage.error(res?.message || '取消失败')
      return
    }
    order.value.status = 'cancelled'
    ElMessage.success('订单已取消')
  }).catch(() => {
    ElMessage.error('取消失败')
  }).finally(() => {
    cancelling.value = false
    cancelDialogVisible.value = false
  })
}

onMounted(loadData)
</script>

<style scoped>
.order-detail-view {
  background-attachment: fixed;
}
</style>
