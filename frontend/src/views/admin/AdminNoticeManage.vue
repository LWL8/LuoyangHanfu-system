<template>
  <div class="admin-notice-manage p-6 animate__animated animate__fadeIn">
    <!-- 头部区域 -->
    <div class="flex justify-between items-center mb-8">
      <div>
        <h2 class="text-2xl font-serif font-black text-slate-800 tracking-wider">公告通知管理</h2>
        <p class="text-xs text-slate-400 mt-1 uppercase tracking-widest font-bold">Manage system announcements & activities</p>
      </div>
      <el-button type="primary" :icon="Plus" class="premium-btn shadow-lg" @click="handleAdd">发布新公告</el-button>
    </div>

    <!-- 搜索栏 -->
    <div class="card-premium bg-white p-6 mb-6">
      <div class="flex gap-4">
        <el-input v-model="searchQuery" placeholder="搜索公告标题..." class="!w-72" @keyup.enter="fetchData" />
        <el-select v-model="typeFilter" placeholder="公告类别" clearable class="!w-40" @change="fetchData">
          <el-option label="预约必读" value="rule" />
          <el-option label="汉服科普" value="science" />
          <el-option label="活动快讯" value="activity" />
        </el-select>
        <el-button :icon="Search" @click="fetchData">搜索</el-button>
      </div>
    </div>

    <!-- 表格区域 -->
    <div class="card-premium bg-white overflow-hidden">
      <el-table :data="noticeList" v-loading="loading" stripe style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" align="center" />
        <el-table-column label="封面" width="120" align="center">
          <template #default="{ row }">
            <el-image :src="row.cover" class="w-16 h-10 rounded-md shadow-sm object-cover" :preview-src-list="[row.cover]" preview-teleported />
          </template>
        </el-table-column>
        <el-table-column prop="title" label="公告标题" min-width="200" />
        <el-table-column prop="type" label="类别" width="120">
          <template #default="{ row }">
            <el-tag :type="typeConfig[row.type].tagType">{{ typeConfig[row.type].label }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="发布时间" width="180" />
        <el-table-column prop="viewCount" label="阅读量" width="100" align="center" />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
            <el-button link type="danger" @click="handleDelete(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <div class="p-6 flex justify-end bg-slate-50/50">
        <el-pagination
          v-model:current-page="pageNum"
          v-model:page-size="pageSize"
          :total="total"
          layout="total, prev, pager, next"
          background
          @current-change="fetchData"
        />
      </div>
    </div>

    <!-- 编辑/新增弹窗 -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑公告' : '发布新公告'"
      width="800px"
      class="premium-dialog"
      destroy-on-close
    >
      <el-form :model="form" label-width="80px" class="mt-4">
        <el-form-item label="标题" required>
          <el-input v-model="form.title" placeholder="请输入公告标题" />
        </el-form-item>
        <div class="grid grid-cols-2 gap-4">
          <el-form-item label="类别" required>
            <el-select v-model="form.type" placeholder="请选择类别" class="w-full">
              <el-option label="预约必读" value="rule" />
              <el-option label="汉服科普" value="science" />
              <el-option label="活动快讯" value="activity" />
            </el-select>
          </el-form-item>
          <el-form-item label="封面图">
            <el-input v-model="form.cover" placeholder="封面图片 URL" />
          </el-form-item>
        </div>
        <el-form-item label="公告内容" required>
          <el-input
            v-model="form.content"
            type="textarea"
            :rows="12"
            placeholder="支持输入富文本内容（HTML）"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="flex gap-3">
          <el-button @click="dialogVisible = false" class="flex-1">取消</el-button>
          <el-button type="primary" :loading="saving" @click="handleSave" class="flex-1 premium-btn">保存发布</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { Plus, Search } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { adminFetchNoticeList, adminAddNotice, adminUpdateNotice, adminDeleteNotice } from '@/api/notice'

const loading = ref(false)
const saving = ref(false)
const noticeList = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)
const searchQuery = ref('')
const typeFilter = ref('')

const typeConfig: any = {
  rule: { label: '预约必读', tagType: 'danger' },
  science: { label: '汉服科普', tagType: 'info' },
  activity: { label: '活动快讯', tagType: 'success' }
}

const fetchData = async () => {
  loading.value = true
  try {
    const res: any = await adminFetchNoticeList({
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      title: searchQuery.value,
      type: typeFilter.value
    })
    if (res.code === 200 || res.code === 0) {
      noticeList.value = res.data.records || []
      total.value = res.data.total || 0
    }
  } catch (e) {
    ElMessage.error('数据获取失败')
  } finally {
    loading.value = false
  }
}

const dialogVisible = ref(false)
const isEdit = ref(false)
const form = ref<any>({
  id: undefined,
  title: '',
  type: 'rule',
  cover: '',
  content: ''
})

const handleAdd = () => {
  isEdit.value = false
  form.value = {
    id: undefined,
    title: '',
    type: 'rule',
    cover: '',
    content: ''
  }
  dialogVisible.value = true
}

const handleEdit = (row: any) => {
  isEdit.value = true
  form.value = { ...row }
  dialogVisible.value = true
}

const handleSave = async () => {
  if (!form.value.title || !form.value.content) {
    ElMessage.warning('请完善标题和内容')
    return
  }
  saving.value = true
  try {
    if (isEdit.value) {
      await adminUpdateNotice(form.value)
      ElMessage.success('更新成功')
    } else {
      await adminAddNotice(form.value)
      ElMessage.success('发布成功')
    }
    dialogVisible.value = false
    fetchData()
  } catch (e) {
    ElMessage.error('操作失败')
  } finally {
    saving.value = false
  }
}

const handleDelete = (id: number) => {
  ElMessageBox.confirm('确定要删除这条公告吗？', '重要提示', {
    type: 'warning',
    confirmButtonText: '确定删除',
    cancelButtonText: '取消',
  }).then(async () => {
    try {
      await adminDeleteNotice(id)
      ElMessage.success('已删除')
      fetchData()
    } catch (e) {
      ElMessage.error('删除失败')
    }
  }).catch(() => {})
}

onMounted(fetchData)
</script>

<style scoped>
.premium-btn {
  background: var(--lh-navy) !important;
  border-color: var(--lh-navy) !important;
  font-weight: 800;
  letter-spacing: 0.05em;
}
.premium-btn:hover {
  background: var(--lh-red) !important;
  border-color: var(--lh-red) !important;
}
</style>
