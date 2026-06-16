<template>
  <div class="space-y-6">
    <div class="card p-6 flex items-center justify-between gap-4">
      <div>
        <h2 class="title">新品推荐</h2>
        <p class="sub">配置首页新品推荐卡片，支持上架与推荐位控制</p>
      </div>
      <div class="flex gap-3 w-[420px] max-w-full">
        <el-input v-model="keyword" placeholder="搜索汉服名称" clearable />
        <el-button type="primary" @click="loadList">刷新</el-button>
        <el-button :loading="saving" @click="persistRecommend">保存配置</el-button>
      </div>
    </div>

    <div class="card p-0 overflow-hidden">
      <el-table :data="filteredList" v-loading="loading" style="width: 100%">
        <el-table-column prop="id" label="编号" width="90" />
        <el-table-column label="封面" width="90">
          <template #default="{ row }">
            <el-image :src="row.hanfuPhoto" class="w-10 h-14 rounded" fit="cover" :preview-src-list="[row.hanfuPhoto]" />
          </template>
        </el-table-column>
        <el-table-column prop="hanfuName" label="名称" min-width="180" />
        <el-table-column prop="hanfuTypes" label="朝代" width="120" />
        <el-table-column label="日租金" width="120">
          <template #default="{ row }">¥ {{ row.hanfuPrice }}</template>
        </el-table-column>
        <el-table-column label="新品" width="100">
          <template #default="{ row }">
            <el-switch v-model="row.isNew" @change="persistRecommend" />
          </template>
        </el-table-column>
        <el-table-column label="推荐" width="100">
          <template #default="{ row }">
            <el-switch v-model="row.recommended" @change="persistRecommend" />
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { fetchHanfuList, type HanfuItem } from '@/api/hanfu'
import { adminFetchRecommendConfig, adminSaveRecommendConfig } from '@/api/recommend'

type RecommendItem = HanfuItem & { isNew?: boolean; recommended?: boolean }

const loading = ref(false)
const keyword = ref('')
const list = ref<RecommendItem[]>([])
const saving = ref(false)
const isOk = (res: any) => res && (res.code === 200 || res.code === 0)

const filteredList = computed(() => {
  if (!keyword.value) return list.value
  return list.value.filter((item) => item.hanfuName?.includes(keyword.value))
})

const loadList = async () => {
  loading.value = true
  try {
    const res: any = await fetchHanfuList({ pageSize: 30 })
    if (res && (res.code === 0 || res.code === 200)) {
      const source = res.data?.list || []
      list.value = (source as RecommendItem[]).map((item) => ({
        ...item,
        isNew: false,
        recommended: false
      }))
      const confRes: any = await adminFetchRecommendConfig()
      if (isOk(confRes)) {
        const confMap = new Map<number, { isNew: boolean; recommended: boolean }>(
          (confRes.data || []).map((i: any) => [
            Number(i.hanfuId),
            { isNew: Boolean(i.isNew), recommended: Boolean(i.recommended) }
          ])
        )
        list.value = list.value.map((item) => {
          const conf = confMap.get(Number(item.id))
          if (!conf) return item
          return { ...item, isNew: conf.isNew, recommended: conf.recommended }
        })
      }
    }
  } finally {
    loading.value = false
  }
}

const persistRecommend = async () => {
  saving.value = true
  try {
    const payload = list.value.map((item) => ({
      hanfuId: Number(item.id),
      isNew: item.isNew ? 1 : 0,
      recommended: item.recommended ? 1 : 0
    }))
    const res: any = await adminSaveRecommendConfig(payload)
    if (isOk(res)) ElMessage.success('推荐配置已保存')
  } finally {
    saving.value = false
  }
}

onMounted(loadList)
</script>

<style scoped>
.card { background: #fff; border: 1px solid var(--lh-border); border-radius: 16px; box-shadow: var(--shadow-sm); }
.title { font-size: 26px; font-weight: 800; color: var(--lh-navy); letter-spacing: 0.08em; }
.sub { margin-top: 6px; color: var(--lh-text-secondary); font-size: 13px; }
</style>
