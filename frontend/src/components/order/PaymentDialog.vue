<template>
  <el-dialog
    v-model="visible"
    title="订单支付"
    width="600px"
    class="heritage-pay-dialog"
    :show-close="false"
    destroy-on-close
  >
    <div v-if="order" class="p-8">
      <div class="text-center mb-10">
         <h2 class="text-3xl font-serif font-black text-[var(--lh-navy)] tracking-wider">确认支付</h2>
         <p class="text-[10px] text-[var(--lh-text-secondary)] opacity-50 mt-2 font-black">请在 15 分钟内完成支付，超时订单可能失效。</p>
      </div>

      <!-- 订单信息 -->
      <div class="bg-[var(--lh-cream)]/50 rounded-2xl p-8 mb-8 border border-[var(--lh-border)]/40">
         <div class="flex justify-between items-center mb-6">
            <span class="text-[10px] font-black text-[var(--lh-text-secondary)] opacity-40">订单编号</span>
            <span class="font-mono text-sm font-black text-[var(--lh-navy)]">#{{ order.orderNo }}</span>
         </div>
         <div class="flex justify-between items-end">
            <span class="text-[10px] font-black text-[var(--lh-text-secondary)] opacity-40">应付金额</span>
            <div class="text-right">
               <span class="text-xs font-black text-[var(--lh-navy)] mr-1">¥</span>
               <span class="text-4xl font-serif font-black text-[var(--lh-red)]">{{ order.totalAmount.toFixed(2) }}</span>
            </div>
         </div>
      </div>

      <!-- 支付方式 -->
      <div class="space-y-4">
         <div 
           class="payment-method-item"
           :class="{ active: activeMethod === 'balance' }"
           @click="activeMethod = 'balance'"
         >
            <div class="flex items-center gap-5">
               <div class="w-12 h-12 rounded-xl bg-white shadow-sm flex items-center justify-center text-[var(--lh-gold)]">
                  <el-icon size="24"><Coin /></el-icon>
               </div>
               <div>
                  <h4 class="text-xs font-black text-[var(--lh-navy)]">账户余额</h4>
                  <p class="text-[9px] text-[var(--lh-text-secondary)] opacity-40 mt-0.5">当前可用：¥{{ userStore.userInfo?.newMoney?.toFixed(2) || '0.00' }}</p>
               </div>
            </div>
            <el-icon v-if="activeMethod === 'balance'" color="var(--lh-red)" size="20"><CircleCheck /></el-icon>
         </div>

         <div 
           class="payment-method-item"
           :class="{ active: activeMethod === 'offline' }"
           @click="activeMethod = 'offline'"
         >
            <div class="flex items-center gap-5">
               <div class="w-12 h-12 rounded-xl bg-white shadow-sm flex items-center justify-center text-[var(--lh-navy)]">
                  <el-icon size="24"><LocationFilled /></el-icon>
               </div>
               <div>
                  <h4 class="text-xs font-black text-[var(--lh-navy)]">到店支付</h4>
                  <p class="text-[9px] text-[var(--lh-text-secondary)] opacity-40 mt-0.5">到店后由门店收款</p>
               </div>
            </div>
            <el-icon v-if="activeMethod === 'offline'" color="var(--lh-red)" size="20"><CircleCheck /></el-icon>
         </div>
      </div>
    </div>
    <template #footer>
      <div class="flex gap-4 p-8 pt-0">
        <button class="flex-1 py-4 border-2 border-[var(--lh-border)] rounded-xl text-[10px] font-black tracking-[0.06em] text-[var(--lh-text-secondary)]" @click="visible = false">返回修改</button>
        <button 
          class="flex-[2] py-4 bg-[var(--lh-red)] text-white rounded-xl text-[10px] font-black tracking-[0.06em] hover:shadow-xl hover:shadow-red-200 transition-all disabled:opacity-50"
          :disabled="paying"
          @click="handlePay"
        >
           {{ paying ? '支付中...' : `确认支付 ¥${order?.totalAmount.toFixed(2)}` }}
        </button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { Coin, LocationFilled, CircleCheck } from '@element-plus/icons-vue'
import useUserStore from '@/stores/user'
import { payOrder } from '@/api/order'
import { ElMessage } from 'element-plus'

const props = defineProps<{
  modelValue: boolean
  order: any
}>()

const emits = defineEmits(['update:modelValue', 'success'])

const userStore = useUserStore()
const activeMethod = ref('balance')
const paying = ref(false)

const visible = computed({
  get: () => props.modelValue,
  set: (val) => emits('update:modelValue', val)
})

const handlePay = async () => {
  if (!props.order) return
  paying.value = true
  try {
    const res: any = await payOrder(props.order.id, activeMethod.value)
    if (!(res?.code === 200 || res?.code === 0)) {
      ElMessage.error(res?.message || '支付失败')
      return
    }
    
    ElMessage.success(activeMethod.value === 'offline' ? '已标记为到店支付' : '支付成功')
    emits('success', activeMethod.value)
    visible.value = false
  } catch (e: any) {
    ElMessage.error(e?.response?.data?.message || '支付异常')
  } finally {
    paying.value = false
  }
}
</script>

<style scoped>
.payment-method-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 24px;
  background: white;
  border: 2px solid var(--lh-border);
  border-radius: 20px;
  cursor: pointer;
  transition: all 0.3s;
}

.payment-method-item:hover {
  background: var(--lh-cream);
  border-color: var(--lh-gold);
}

.payment-method-item.active {
  border-color: var(--lh-red);
  background: #fffafa;
}
</style>
