<template>
  <div class="space-y-6">
    <div class="card p-6 flex items-center justify-between gap-4">
      <div>
        <h2 class="title">用户管理</h2>
        <p class="sub">管理用户信息、余额与状态</p>
      </div>
      <div class="flex gap-3 w-[420px] max-w-full">
        <el-input v-model="searchQuery" placeholder="搜索用户名/姓名/手机号" :prefix-icon="Search" clearable @keyup.enter="handleSearch" />
        <el-button type="primary" @click="handleSearch">查询</el-button>
      </div>
    </div>

    <div class="card p-0 overflow-hidden">
      <el-table :data="userList" v-loading="loading" style="width: 100%">
        <el-table-column prop="id" label="编号" width="90" />
        <el-table-column label="头像" width="90">
          <template #default="{ row }">
            <el-avatar :src="row.yonghuPhoto || userStore.DEFAULT_AVATAR" :size="42" />
          </template>
        </el-table-column>
        <el-table-column prop="yonghuName" label="姓名" min-width="120" />
        <el-table-column prop="username" label="账号" min-width="140" />
        <el-table-column prop="yonghuPhone" label="手机号" min-width="140" />
        <el-table-column label="余额" width="130">
          <template #default="{ row }">¥ {{ row.newMoney }}</template>
        </el-table-column>
        <el-table-column prop="createTime" label="注册时间" min-width="180" />
        <el-table-column label="状态" width="120">
          <template #default="{ row }">
            <el-switch
              :model-value="row.status !== 0"
              inline-prompt
              active-text="启用"
              inactive-text="禁用"
              @change="(v) => handleToggleStatus(row, !!v)"
            />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="170" align="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleOpenRecharge(row)">充值</el-button>
            <el-button link type="danger" @click="handleDelete(row.id)">删除</el-button>
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

    <el-dialog v-model="showRecharge" title="余额充值" width="420px">
      <el-form label-position="top">
        <el-form-item label="用户">
          <el-input :model-value="rechargeTarget?.yonghuName || ''" disabled />
        </el-form-item>
        <el-form-item label="金额">
          <el-input-number v-model="rechargeAmount" :min="1" class="w-full" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showRecharge = false">取消</el-button>
        <el-button type="primary" :loading="recharging" @click="handleRecharge">确认充值</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { Search } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { adminFetchUserList, adminManualRecharge, adminDeleteUser, adminUpdateUserStatus } from '@/api/user'
import useUserStore from '@/stores/user'

const userStore = useUserStore()
const loading = ref(false)
const userList = ref<any[]>([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)
const searchQuery = ref('')

const showRecharge = ref(false)
const rechargeTarget = ref<any>(null)
const rechargeAmount = ref(100)
const recharging = ref(false)

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

const handleSearch = async () => {
  loading.value = true
  try {
    const res: any = await adminFetchUserList({
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      userName: searchQuery.value || undefined,
      yonghuPhone: searchQuery.value || undefined,
    })
    if (isOk(res)) {
      userList.value = extractPageList(res.data)
      total.value = extractPageTotal(res.data)
    }
  } finally {
    loading.value = false
  }
}

const handleOpenRecharge = (row: any) => {
  rechargeTarget.value = row
  rechargeAmount.value = 100
  showRecharge.value = true
}

const handleRecharge = async () => {
  if (!rechargeTarget.value) return
  recharging.value = true
  try {
    const res: any = await adminManualRecharge(rechargeTarget.value.id, rechargeAmount.value)
    if (isOk(res)) {
      ElMessage.success('充值成功')
      showRecharge.value = false
      handleSearch()
    }
  } finally {
    recharging.value = false
  }
}

const handleDelete = (id: number) => {
  ElMessageBox.confirm('确定要删除该用户吗？删除后将无法恢复。', {
    type: 'warning',
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    showClose: false,
    alignCenter: true
  }).then(async () => {
    const res: any = await adminDeleteUser(id)
    if (isOk(res)) {
      ElMessage.success('删除成功')
      handleSearch()
    }
  })
}

const handleToggleStatus = async (row: any, enabled: boolean) => {
  const prev = row.status
  row.status = enabled ? 1 : 0
  try {
    const res: any = await adminUpdateUserStatus(row.id, row.status)
    if (!isOk(res)) {
      row.status = prev
      ElMessage.warning('状态更新失败')
      return
    }
    ElMessage.success(enabled ? '用户已启用' : '用户已禁用')
  } catch {
    row.status = prev
    ElMessage.warning('状态更新失败')
  }
}

onMounted(handleSearch)
</script>

<style scoped>
.card { background: #fff; border: 1px solid var(--lh-border); border-radius: 16px; box-shadow: var(--shadow-sm); }
.title { font-size: 26px; font-weight: 800; color: var(--lh-navy); letter-spacing: 0.08em; }
.sub { margin-top: 6px; color: var(--lh-text-secondary); font-size: 13px; }
</style>
