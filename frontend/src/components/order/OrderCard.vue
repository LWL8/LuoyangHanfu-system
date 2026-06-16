<template>
  <div class="order-card bg-white rounded-2xl border border-[var(--border)] overflow-hidden transition-all hover:shadow-md mb-4 bg-white relative">
    <div class="p-5">
      <div class="flex justify-between items-center mb-4">
        <span class="text-[10px] text-gray-400 font-mono tracking-tight">订单号: {{ item.orderNo }}</span>
        <el-tag :type="statusConfig[item.status].type" size="small" effect="dark" class="rounded-full px-3">
          {{ statusConfig[item.status].label }}
        </el-tag>
      </div>

      <div class="flex gap-4 cursor-pointer" @click="$router.push(`/order/${item.id}`)">
        <div class="relative w-16 h-20 shrink-0">
          <img :src="mainItem?.image" class="w-full h-full object-cover rounded-lg shadow-sm" />
          <div v-if="item.items.length > 1" class="absolute -bottom-1 -right-1 bg-black/60 text-white text-[8px] px-1 rounded">
            +{{ item.items.length - 1 }}
          </div>
        </div>
        
        <div class="flex-1 min-w-0">
          <h4 class="text-sm font-bold text-gray-800 line-clamp-1 mb-1">{{ mainItem?.name }}</h4>
          <div class="flex flex-col gap-1 text-[11px] text-gray-500">
             <div class="flex items-center gap-1">
                <el-icon><Calendar /></el-icon>
                <span>预约日期: {{ item.bookingDate }}</span>
             </div>
             <div class="flex items-center gap-1">
                <el-icon><Clock /></el-icon>
                <span>时段: {{ item.timeSlot }}</span>
             </div>
          </div>
        </div>

        <div class="text-right flex flex-col justify-between items-end shrink-0">
          <div class="text-xs text-gray-400">合计费用</div>
          <div class="text-lg font-bold text-[var(--lh-red)] font-[family-name:var(--font-title)]">¥{{ item.totalAmount.toFixed(2) }}</div>
        </div>
      </div>

      <div class="flex justify-end gap-3 mt-5 pt-4 border-t border-dashed border-gray-100">
        <!-- 动态操作按钮 -->
        <template v-if="item.status === 'pending'">
          <el-button link size="small" @click.stop="handleCancel">取消订单</el-button>
          <el-button type="primary" size="small" class="btn-primary px-6" @click.stop="goPayment">去支付</el-button>
        </template>
        
        <template v-else-if="item.status === 'confirmed'">
          <el-button link size="small" @click.stop="handleCancel">取消订单</el-button>
          <el-button plain size="small" @click.stop="$router.push(`/order/${item.id}`)">查看详情</el-button>
        </template>

        <template v-else-if="item.status === 'used'">
          <el-button type="primary" plain size="small" @click.stop="$router.push('/hanfu')">再次预约</el-button>
          <el-button link size="small" @click.stop="$router.push(`/order/${item.id}`)">查看详情</el-button>
        </template>

        <template v-else-if="item.status === 'cancelled'">
          <el-button link size="small" class="text-gray-300">删除记录</el-button>
        </template>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { Calendar, Clock } from '@element-plus/icons-vue'
import type { Order, OrderStatus } from '@/api/order'

const props = defineProps<{
  item: Order
}>()

const router = useRouter()
const emits = defineEmits(['cancel', 'pay'])

const mainItem = computed(() => props.item.items?.[0])

const statusConfig: Record<OrderStatus, { label: string, type: 'warning' | 'primary' | 'success' | 'info' }> = {
  pending: { label: '待支付', type: 'warning' },
  confirmed: { label: '已预约', type: 'primary' },
  used: { label: '已使用', type: 'success' },
  cancelled: { label: '已取消', type: 'info' }
}

const goPayment = () => emits('pay', props.item)
const handleCancel = () => emits('cancel', props.item)
</script>

<style scoped>
.order-card {
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif;
}
</style>
