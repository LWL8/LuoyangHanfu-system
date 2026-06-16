<template>
  <div class="space-y-6">
    <div class="card p-6 flex items-center justify-between gap-4">
      <div>
        <h2 class="title">订单流水</h2>
        <p class="sub">查看预约订单、状态与金额</p>
      </div>
      <div class="flex flex-wrap items-center gap-3 w-auto min-w-0">
        <el-select v-model="filterStatus" placeholder="订单状态" clearable class="!w-36" @change="handleSearch(true)">
          <template #prefix><el-icon><Filter /></el-icon></template>
          <el-option :value="101" label="待确认" />
          <el-option :value="103" label="进行中" />
          <el-option :value="106" label="已完成" />
          <el-option :value="104" label="已取消" />
          <el-option :value="102" label="已拒绝" />
        </el-select>
        <el-date-picker
          v-model="dateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          value-format="YYYY-MM-DD"
          class="!w-[260px]"
          @change="handleSearch(true)"
        />
        <el-input v-model="searchQuery" placeholder="搜索订单号/姓名/手机号" :prefix-icon="Search" clearable class="!w-[300px]" @keyup.enter="handleSearch(true)" />
        <div class="flex gap-2">
          <el-button type="primary" class="font-bold px-6" @click="handleSearch(true)">查询</el-button>
          <el-button class="px-4" :icon="RefreshRight" @click="handleReset">重置</el-button>
        </div>
      </div>
    </div>

    <div class="card p-0 overflow-hidden">
      <el-table :data="orderList" v-loading="loading" style="width:100%">
        <el-table-column prop="id" label="订单号" width="90" />
        <el-table-column label="预约信息" min-width="260">
          <template #default="{ row }">
            <div class="flex items-center gap-3">
              <el-image :src="row.hanfuPhoto" class="w-10 h-14 rounded" fit="cover" />
              <div>
                <div class="font-semibold">{{ row.hanfuName || '-' }}</div>
                <div class="text-xs text-[var(--lh-text-secondary)]">{{ row.reservationDate || '-' }} {{ row.timeSlot || '' }}</div>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="customerName" label="客户" width="120" />
        <el-table-column prop="customerPhone" label="手机号" width="140" />
        <el-table-column label="金额" width="120">
          <template #default="{ row }">¥ {{ row.totalAmount }}</template>
        </el-table-column>
        <el-table-column label="状态" width="120">
          <template #default="{ row }">
            <el-tag :type="statusTagType(row.orderStatus)">{{ statusText(row.orderStatus) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" min-width="170" prop="createTime" />
        <el-table-column label="操作" width="220" align="right">
          <template #default="{ row }">
            <el-button link type="primary" :disabled="!canMarkUsed(row.orderStatus)" @click="handleUpdateStatus(row, 103)">标记使用</el-button>
            <el-button link type="success" :disabled="!canComplete(row.orderStatus)" @click="handleUpdateStatus(row, 106)">完成</el-button>
            <el-button link type="warning" :disabled="!canCancel(row.orderStatus)" @click="handleUpdateStatus(row, 104)">取消</el-button>
            <el-button link type="danger" @click="handleDelete(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="p-4 flex justify-end border-t border-[var(--lh-border)] bg-[var(--lh-cream)]/40">
        <el-pagination v-model:current-page="pageNum" v-model:page-size="pageSize" :total="total" layout="prev, pager, next, total" @current-change="handleSearch" />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { Search, Filter, RefreshRight } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { adminFetchReservations, adminUpdateReservationStatus, adminDeleteReservation } from '@/api/order'

const loading = ref(false)
const orderList = ref<any[]>([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)
const filterStatus = ref<number | ''>('')
const searchQuery = ref('')
const dateRange = ref<[string, string] | []>([])

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
  return extractPageList(data).length
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
      keyword: searchQuery.value || undefined,
      status: filterStatus.value || undefined,
      startDate: Array.isArray(dateRange.value) && dateRange.value.length === 2 ? dateRange.value[0] : undefined,
      endDate: Array.isArray(dateRange.value) && dateRange.value.length === 2 ? dateRange.value[1] : undefined,
    })
    if (isOk(res)) {
      orderList.value = extractPageList(res.data)
      total.value = extractPageTotal(res.data)
    }
  } finally {
    loading.value = false
  }
}

const handleReset = () => {
  searchQuery.value = ''
  filterStatus.value = ''
  dateRange.value = []
  handleSearch(true)
}

const handleUpdateStatus = async (row: any, status: number) => {
  const res: any = await adminUpdateReservationStatus(row.id, status)
  if (isOk(res)) {
    row.orderStatus = status
    ElMessage.success('状态已更新')
  }
}

const handleDelete = (id: number) => {
  ElMessageBox.confirm('确定要删除这条订单记录吗？删除后将无法恢复。', {
    type: 'warning',
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    showClose: false,
    alignCenter: true
  }).then(async () => {
    const res: any = await adminDeleteReservation(id)
    if (isOk(res)) {
      ElMessage.success('删除成功')
      handleSearch()
    }
  })
}

const statusText = (status: number) => {
  const map: Record<number, string> = {
    101: '待确认',
    102: '已拒绝',
    103: '进行中',
    104: '已取消',
    106: '已完成'
  }
  return map[status] || '未知'
}

const statusTagType = (status: number) => {
  const map: Record<number, 'warning' | 'danger' | 'primary' | 'info' | 'success'> = {
    101: 'warning',
    102: 'danger',
    103: 'primary',
    104: 'info',
    106: 'success'
  }
  return map[status] || 'info'
}

const canMarkUsed = (status: number) => status === 101
const canComplete = (status: number) => status === 103
const canCancel = (status: number) => status === 101 || status === 103

onMounted(handleSearch)
</script>

<style scoped>
.card { background: #fff; border: 1px solid var(--lh-border); border-radius: 16px; box-shadow: var(--shadow-sm); }
.title { font-size: 26px; font-weight: 800; color: var(--lh-navy); letter-spacing: 0.08em; }
.sub { margin-top: 6px; color: var(--lh-text-secondary); font-size: 13px; }
</style>
