<template>
  <div class="admin-feedback-manage p-6 animate__animated animate__fadeIn">
    <!-- 头部区域 -->
    <div class="flex flex-col mb-8">
      <h2 class="text-2xl font-serif font-black text-slate-800 tracking-wider">意见反馈管理</h2>
      <p class="text-xs text-slate-400 mt-1 uppercase tracking-widest font-bold">Review user feedback & improve services</p>
    </div>

    <!-- 搜索栏 -->
    <div class="card-premium bg-white p-6 mb-6">
      <div class="flex gap-4">
        <el-select v-model="typeFilter" placeholder="反馈类别" clearable class="!w-40" @change="fetchData">
          <el-option label="意见/投诉" value="MESSAGE" />
          <el-option label="咨询服务" value="QA" />
        </el-select>
        <el-select v-model="statusFilter" placeholder="回复状态" clearable class="!w-40" @change="fetchData">
          <el-option label="待回复" value="pending" />
          <el-option label="已回复" value="replied" />
        </el-select>
        <el-button :icon="Search" @click="fetchData">搜索</el-button>
      </div>
    </div>

    <!-- 列表展示 -->
    <div v-loading="loading" class="space-y-6">
      <div v-if="feedbackList.length === 0" class="card-premium bg-white p-20 text-center text-slate-300">
         <el-icon size="60" class="opacity-20 mb-4"><ChatDotRound /></el-icon>
         <p class="text-sm font-bold tracking-widest">暂无相关反馈数据</p>
      </div>

      <div 
        v-for="item in feedbackList" 
        :key="item.id" 
        class="card-premium bg-white p-8 relative overflow-hidden group transition-all hover:shadow-xl"
        :class="item.replyContent ? 'opacity-60 grayscale-[0.5]' : ''"
      >
        <!-- 装饰性背景 -->
        <div class="absolute -top-10 -right-10 text-9xl font-black text-slate-100/50 -rotate-12 select-none group-hover:rotate-0 transition-transform">#{{ item.id }}</div>

        <div class="relative z-10">
          <div class="flex justify-between items-start mb-6">
             <div class="flex items-center gap-4">
                <el-tag :type="typeConfig[item.type]?.tagType" effect="dark" class="!rounded-md">{{ typeConfig[item.type]?.label }}</el-tag>
                <div class="flex text-amber-400">
                  <el-icon v-for="i in item.rating" :key="i" size="14"><StarFilled /></el-icon>
                </div>
                <span class="text-xs font-mono text-slate-400 font-bold">{{ item.createTime || '2026-04-09 12:00' }}</span>
             </div>
              <div class="flex items-center gap-3">
                <span v-if="item.replyContent" class="text-xs text-emerald-600 font-bold flex items-center gap-1">
                  <el-icon><CircleCheckFilled /></el-icon> 已处理回复
                </span>
                <el-button v-else type="primary" size="small" @click="handleProcess(item)">回复反馈</el-button>
              </div>
          </div>

          <div class="mb-8">
             <p class="text-slate-700 leading-relaxed font-medium tracking-wide bg-slate-50 p-6 rounded-2xl border-l-4 border-slate-200 group-hover:bg-slate-100 transition-colors">
               {{ item.content }}
             </p>
          </div>

          <div v-if="item.images && item.images.length > 0" class="flex gap-4 mb-8">
             <el-image 
               v-for="(img, idx) in item.images" 
               :key="idx" 
               :src="img" 
               class="w-24 h-24 rounded-xl shadow-sm cursor-pointer hover:scale-105 transition-transform object-cover" 
               :preview-src-list="item.images"
               :initial-index="idx"
               preview-teleported
             />
          </div>

          <div class="flex items-center justify-between pt-6 border-t border-slate-100">
             <div class="flex items-center gap-3 text-slate-500">
                <el-icon><Phone /></el-icon>
                <span class="text-sm font-mono font-bold tracking-tight">{{ item.phone || '未提供联系方式' }}</span>
             </div>
             <span class="text-[10px] text-slate-300 font-black tracking-widest uppercase">Feedback System v1.0</span>
          </div>
        </div>
      </div>

      <!-- 分页 -->
      <div class="flex justify-center mt-10">
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
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { Search, StarFilled, Phone, CircleCheckFilled, ChatDotRound } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { adminFetchFeedbackList, adminProcessFeedback } from '@/api/notice'

const loading = ref(false)
const feedbackList = ref<any[]>([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)
const typeFilter = ref('')
const statusFilter = ref<number | undefined>(undefined)

const typeConfig: any = {
  MESSAGE: { label: '意见/投诉', tagType: 'danger' },
  QA: { label: '咨询服务', tagType: 'primary' }
}

const fetchData = async () => {
  loading.value = true
  try {
    const res: any = await adminFetchFeedbackList({
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      type: typeFilter.value,
      status: statusFilter.value
    })
    if (res.code === 200 || res.code === 0) {
      feedbackList.value = res.data.records || []
      total.value = res.data.total || 0
    }
  } catch (e) {
    ElMessage.error('数据获取失败')
  } finally {
    loading.value = false
  }
}

const handleProcess = (item: any) => {
  ElMessageBox.prompt('请输入回复内容', '反馈回复', {
    confirmButtonText: '提交回复',
    cancelButtonText: '取消',
    inputPlaceholder: '回复内容将展示给用户...',
    inputValidator: (val) => !!val || '回复内容不能为空'
  }).then(async ({ value }) => {
    try {
      await adminProcessFeedback(item.id, { replyContent: value })
      ElMessage.success('已回复')
      fetchData()
    } catch (e) {
      ElMessage.error('处理失败')
    }
  }).catch(() => {})
}

onMounted(fetchData)
</script>

<style scoped>
.grayscale-\[0\.5\] {
  filter: grayscale(0.5);
}
</style>
