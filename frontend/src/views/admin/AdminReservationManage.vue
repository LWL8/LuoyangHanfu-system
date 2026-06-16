<template>
  <div class="space-y-6">
    <div class="card p-6 flex items-center justify-between gap-4">
      <div>
        <h2 class="title">预约订单管理</h2>
        <p class="sub">审核预约、处理取消、追踪到店与完成状态</p>
      </div>
      <div class="flex flex-wrap items-center gap-3 w-auto min-w-0">
        <el-select v-model="status" clearable placeholder="状态" class="!w-32" @change="handleSearch(true)">
          <template #prefix><el-icon><Filter /></el-icon></template>
          <el-option :value="101" label="待确认" />
          <el-option :value="103" label="进行中" />
          <el-option :value="106" label="已完成" />
          <el-option :value="104" label="已取消" />
        </el-select>
        <el-date-picker
          v-model="dateRange"
          type="daterange"
          value-format="YYYY-MM-DD"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          class="!w-[260px]"
          @change="handleSearch(true)"
        />
        <el-input v-model="keyword" placeholder="搜索单号/客户/手机号" :prefix-icon="Search" clearable class="!w-[260px]" @keyup.enter="handleSearch(true)" />
        <div class="flex gap-2">
          <el-button type="primary" class="font-bold px-6" @click="handleSearch(true)">查询</el-button>
          <el-button class="px-4" :icon="RefreshRight" @click="handleReset">重置</el-button>
        </div>
      </div>
    </div>

    <div class="card p-0 overflow-hidden">
      <el-table :data="tableData" v-loading="loading" style="width: 100%">
        <el-table-column prop="id" label="预约单号" width="100" />
        <el-table-column prop="customerName" label="客户" width="120" />
        <el-table-column prop="customerPhone" label="手机号" width="140" />
        <el-table-column prop="reservationDate" label="预约日期" width="130" />
        <el-table-column prop="timeSlot" label="时段" width="120" />
        <el-table-column prop="hanfuName" label="汉服" min-width="170" />
        <el-table-column label="金额" width="110">
          <template #default="{ row }">¥ {{ row.totalAmount }}</template>
        </el-table-column>
        <el-table-column label="状态" width="120">
          <template #default="{ row }">
            <el-tag :type="statusType(row.orderStatus)">{{ statusText(row.orderStatus) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="220" align="right">
          <template #default="{ row }">
            <el-button link type="primary" :disabled="row.orderStatus !== 101" @click="updateStatus(row, 103)">审核通过</el-button>
            <el-button link type="success" :disabled="row.orderStatus !== 103" @click="updateStatus(row, 106)">标记完成</el-button>
            <el-button link type="warning" :disabled="![101, 103].includes(row.orderStatus)" @click="updateStatus(row, 104)">取消</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="p-4 flex justify-end border-t border-[var(--lh-border)] bg-[var(--lh-cream)]/40">
        <el-pagination 
          v-model:current-page="pageNum" 
          v-model:page-size="pageSize" 
          :total="total" 
          layout="prev, pager, next, total" 
          @current-change="handleSearch" 
        />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { Search, Filter, RefreshRight } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { adminFetchReservations, adminUpdateReservationStatus } from '@/api/order'

const loading = ref(false)
const tableData = ref<any[]>([])
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)
const keyword = ref('')
const status = ref('')
const dateRange = ref<string[]>([])

const isOk = (res: any) => res && (res.code === 200 || res.code === 0)

const extractPageList = (data: any) => {
  if (!data) return []
  if (Array.isArray(data.list)) return data.list
  if (Array.isArray(data.records)) return data.records
  if (Array.isArray(data)) return data
  return []
}

const extractPageTotal = (data: any) => {
  if (!data) return 0
  if (typeof data.total === 'number') return data.total
  const list = extractPageList(data)
  return list.length || 0
}

const handleSearch = async (resetPage = false) => {
  if (resetPage === true) {
    pageNum.value = 1
  }
  loading.value = true
  try {
    const res: any = await adminFetchReservations({
      current: pageNum.value,
      size: pageSize.value,
      keyword: keyword.value || undefined,
      status: status.value || undefined,
      startDate: Array.isArray(dateRange.value) && dateRange.value.length === 2 ? dateRange.value[0] : undefined,
      endDate: Array.isArray(dateRange.value) && dateRange.value.length === 2 ? dateRange.value[1] : undefined
    })
    if (isOk(res)) {
      tableData.value = extractPageList(res.data)
      total.value = extractPageTotal(res.data)
    }
  } finally {
    loading.value = false
  }
}

const handleReset = () => {
  keyword.value = ''
  status.value = ''
  dateRange.value = []
  handleSearch(true)
}

const updateStatus = async (row: any, target: number) => {
  const res: any = await adminUpdateReservationStatus(row.id, target)
  if (res && (res.code === 0 || res.code === 200)) {
    row.orderStatus = target
    ElMessage.success('状态已更新')
  }
}

const statusText = (val: number) => ({ 101: '待确认', 103: '进行中', 106: '已完成', 104: '已取消' } as Record<number, string>)[val] || '未知'
const statusType = (val: number) => ({ 101: 'warning', 103: 'primary', 106: 'success', 104: 'info' } as Record<number, 'warning' | 'primary' | 'success' | 'info'>)[val] || 'info'

onMounted(() => handleSearch())
</script>

<style scoped>
.card { background: #fff; border: 1px solid var(--lh-border); border-radius: 16px; box-shadow: var(--shadow-sm); }
.title { font-size: 26px; font-weight: 800; color: var(--lh-navy); letter-spacing: 0.08em; }
.sub { margin-top: 6px; color: var(--lh-text-secondary); font-size: 13px; }
</style>
