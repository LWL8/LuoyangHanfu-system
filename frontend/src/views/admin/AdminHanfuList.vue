<template>
  <div class="space-y-6">
    <div class="card p-6 flex items-center justify-between gap-4">
      <div>
        <h2 class="title">汉服库存</h2>
        <p class="sub">管理汉服资料、价格与上架状态</p>
      </div>
      <div class="flex flex-wrap items-center justify-between flex-1 gap-4 min-w-0">
        <!-- 筛选区 -->
        <div class="flex items-center gap-3">
          <el-select v-model="filterDynasty" clearable placeholder="朝代筛选" class="!w-36" @change="handleSearch(true)">
            <template #prefix><el-icon><Filter /></el-icon></template>
            <el-option label="唐朝" value="唐朝" />
            <el-option label="宋朝" value="宋朝" />
            <el-option label="明朝" value="明朝" />
          </el-select>
          <el-input v-model="searchQuery" placeholder="搜索汉服名称" :prefix-icon="Search" clearable class="!w-56" @keyup.enter="handleSearch(true)" />
          <el-button :icon="RefreshRight" @click="handleReset">重置</el-button>
        </div>
        
        <!-- 操作区 -->
        <div class="flex items-center gap-2">
          <el-button type="primary" :icon="Plus" class="font-bold" @click="handleAdd">新增汉服</el-button>
          <el-button-group>
            <el-button :icon="CircleCheck" @click="batchToggle(1)">一键上架</el-button>
            <el-button :icon="CircleClose" @click="batchToggle(2)">一键下架</el-button>
          </el-button-group>
        </div>
      </div>
    </div>

    <div class="card p-0 overflow-hidden">
      <el-table :data="hanfuList" v-loading="loading" style="width:100%">
        <el-table-column prop="id" label="编号" width="90" />
        <el-table-column label="封面" width="90">
          <template #default="{ row }">
            <el-image :src="row.hanfuPhoto" class="w-10 h-14 rounded" fit="cover" :preview-src-list="[row.hanfuPhoto]" />
          </template>
        </el-table-column>
        <el-table-column prop="hanfuName" label="名称" min-width="180" />
        <el-table-column prop="hanfuTypes" label="朝代" width="110" />
        <el-table-column label="日租金" width="110">
          <template #default="{ row }">¥ {{ row.hanfuPrice }}</template>
        </el-table-column>
        <el-table-column label="押金" width="110">
          <template #default="{ row }">¥ {{ row.hanfuNewMoney }}</template>
        </el-table-column>
        <el-table-column label="上架" width="100">
          <template #default="{ row }">
            <el-switch v-model="row.shangjiaTypes" :active-value="1" :inactive-value="2" @change="handleToggleActive(row)" />
          </template>
        </el-table-column>
        <el-table-column label="库存" width="120">
          <template #default="{ row }">
            <el-input-number v-model="row.stock" :min="0" :max="9999" size="small" @change="saveInlineStock(row)" />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="130" align="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
            <el-button link type="danger" @click="handleDelete(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="p-4 flex justify-end border-t border-[var(--lh-border)] bg-[var(--lh-cream)]/40">
        <el-pagination v-model:current-page="pageNum" v-model:page-size="pageSize" :total="total" layout="prev, pager, next, total" @current-change="handleSearch" />
      </div>
    </div>

    <el-drawer v-model="showDrawer" :title="isEdit ? '编辑汉服' : '新增汉服'" size="520px">
      <el-form :model="form" label-position="top">
        <el-form-item label="名称"><el-input v-model="form.hanfuName" /></el-form-item>
        <el-form-item label="朝代"><el-input v-model="form.hanfuTypes" /></el-form-item>
        <el-form-item label="封面图片 (支持直接粘贴图片地址)">
          <div class="premium-upload-area w-full">
            <el-upload
              action="#"
              :show-file-list="false"
              accept="image/*"
              :auto-upload="false"
              :on-change="handleHanfuImageChange"
              class="w-full"
            >
              <div v-if="!form.hanfuPhoto" class="border-2 border-dashed border-slate-200 rounded-2xl h-48 flex flex-col items-center justify-center hover:border-[var(--lh-red)] transition-colors cursor-pointer group">
                <el-icon size="40" class="text-slate-300 group-hover:text-[var(--lh-red)]"><Plus /></el-icon>
                <span class="text-xs font-bold text-slate-400 mt-2 uppercase tracking-widest">选择图片素材</span>
              </div>
              <div v-else class="relative w-full h-48 rounded-2xl overflow-hidden group cursor-pointer bg-gray-50 flex items-center justify-center border border-slate-100">
                <img :src="form.hanfuPhoto" class="h-full w-full object-contain transition-transform group-hover:scale-105" />
                <div class="absolute inset-0 bg-black/40 opacity-0 group-hover:opacity-100 transition-opacity flex items-center justify-center">
                  <span class="text-white text-xs font-bold ring-2 ring-white px-4 py-2 rounded-full">点击更换</span>
                </div>
              </div>
            </el-upload>
            <div class="mt-4 flex gap-2 items-center">
              <el-input v-model="form.hanfuPhoto" placeholder="粘贴外部图片 URL，或点击右侧上传" size="small" class="flex-1" />
              <el-upload
                action="#"
                :show-file-list="false"
                :auto-upload="false"
                :on-change="handleHanfuImageChange"
                accept="image/*"
                class="flex shrink-0"
              >
                <el-button type="primary" plain size="small">上传图片</el-button>
              </el-upload>
              <el-button v-if="form.hanfuPhoto" type="danger" link size="small" @click="form.hanfuPhoto = ''">清空图片</el-button>
            </div>
          </div>
        </el-form-item>
        <el-form-item label="日租金"><el-input-number v-model="form.hanfuPrice" :min="1" class="w-full" /></el-form-item>
        <el-form-item label="押金"><el-input-number v-model="form.hanfuNewMoney" :min="0" class="w-full" /></el-form-item>
        <el-form-item label="描述"><el-input v-model="form.hanfuContent" type="textarea" :rows="4" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showDrawer=false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="saveHanfu">保存</el-button>
      </template>
    </el-drawer>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { Search, Plus, Filter, RefreshRight, CircleCheck, CircleClose } from '@element-plus/icons-vue'
import type { UploadProps, UploadRawFile } from 'element-plus'
import { uploadAvatarFile } from '@/api/user'
import { ElMessage, ElMessageBox } from 'element-plus'
import { adminFetchHanfuList, adminAddHanfu, adminUpdateHanfu, adminDeleteHanfu, adminToggleActive, type HanfuItem } from '@/api/hanfu'

const loading = ref(false)
const saving = ref(false)
const hanfuList = ref<HanfuItem[]>([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)
const filterDynasty = ref('')
const searchQuery = ref('')
const showDrawer = ref(false)
const isEdit = ref(false)
const form = ref<Partial<HanfuItem>>({ hanfuName: '', hanfuTypes: '唐朝', hanfuPrice: 199, hanfuNewMoney: 500, hanfuPhoto: '', hanfuContent: '', shangjiaTypes: 1 })

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
    const res: any = await adminFetchHanfuList({ 
      pageNum: pageNum.value, 
      pageSize: pageSize.value, 
      hanfuName: searchQuery.value || undefined, 
      hanfuTypes: filterDynasty.value || undefined 
    })
    if (isOk(res)) {
      hanfuList.value = extractPageList(res.data) as HanfuItem[]
      total.value = extractPageTotal(res.data)
    }
  } finally {
    loading.value = false
  }
}

const handleReset = () => {
  searchQuery.value = ''
  filterDynasty.value = ''
  handleSearch(true)
}

const handleToggleActive = async (row: any) => {
  const prev = row.shangjiaTypes
  const res: any = await adminToggleActive(row.id)
  if (!isOk(res)) row.shangjiaTypes = prev === 1 ? 2 : 1
}

const handleAdd = () => {
  isEdit.value = false
  form.value = { hanfuName: '', hanfuTypes: '唐朝', hanfuPrice: 199, hanfuNewMoney: 500, hanfuPhoto: '', hanfuContent: '', shangjiaTypes: 1 }
  showDrawer.value = true
}
const handleEdit = (row: any) => { isEdit.value = true; form.value = { ...row }; showDrawer.value = true }

const handleDelete = (id: number) => {
  ElMessageBox.confirm('确定要删除这条汉服记录吗？删除后将无法恢复。', {
    type: 'warning',
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    showClose: false,
    alignCenter: true
  }).then(async () => {
    const res: any = await adminDeleteHanfu(id)
    if (isOk(res)) { ElMessage.success('删除成功'); handleSearch() }
  })
}
const handleHanfuImageChange: UploadProps['onChange'] = async (uploadFile) => {
  const raw = uploadFile.raw as UploadRawFile | undefined
  if (!raw) return
  if (raw.size / 1024 / 1024 > 10) {
    ElMessage.warning('图片素材过大，建议压缩至 10MB 以内')
    return
  }
  try {
    const reader = new FileReader()
    reader.onload = (e) => { form.value.hanfuPhoto = String(e.target?.result || '') }
    reader.readAsDataURL(raw)
    
    // 尝试上传至后端
    try {
      const url = await uploadAvatarFile(raw as unknown as File)
      form.value.hanfuPhoto = url
      ElMessage.success('图片已上传')
    } catch {
      ElMessage.info('暂存为本地预览，保存后将持久化')
    }
  } catch (error) {
    console.error('Upload failed:', error)
  }
}

const saveHanfu = async () => {
  saving.value = true
  try {
    const res: any = isEdit.value ? await adminUpdateHanfu(form.value) : await adminAddHanfu(form.value)
    if (isOk(res)) {
      ElMessage.success('保存成功')
      showDrawer.value = false
      handleSearch()
    }
  } finally { saving.value = false }
}

const saveInlineStock = async (row: HanfuItem) => {
  const res: any = await adminUpdateHanfu({ id: row.id, stock: Number((row as any).stock || 0) } as any)
  if (isOk(res)) ElMessage.success('库存已更新')
}

const batchToggle = async (target: 1 | 2) => {
  const list = hanfuList.value.filter((item) => item.shangjiaTypes !== target)
  if (!list.length) return
  saving.value = true
  try {
    await Promise.all(
      list.map((item) =>
        adminUpdateHanfu({ id: item.id, shangjiaTypes: target }).then(() => {
          item.shangjiaTypes = target
        })
      )
    )
    ElMessage.success(target === 1 ? '批量上架完成' : '批量下架完成')
  } finally {
    saving.value = false
  }
}

onMounted(handleSearch)
</script>

<style scoped>
.card { background: #fff; border: 1px solid var(--lh-border); border-radius: 16px; box-shadow: var(--shadow-sm); }
.title { font-size: 26px; font-weight: 800; color: var(--lh-navy); letter-spacing: 0.08em; }
.sub { margin-top: 6px; color: var(--lh-text-secondary); font-size: 13px; }
</style>
