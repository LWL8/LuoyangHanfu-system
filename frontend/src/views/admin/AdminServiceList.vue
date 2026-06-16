<template>
  <div class="space-y-6">
    <div class="card p-6 flex items-center justify-between gap-4">
      <div>
        <h2 class="title">服务套餐</h2>
        <p class="sub">管理妆造、摄影、道具等服务的上下架与价格</p>
      </div>
      <div class="flex gap-3 w-[460px] max-w-full">
        <el-input v-model="searchQuery" placeholder="搜索套餐名称" clearable @keyup.enter="handleSearch" />
        <el-button type="primary" @click="openCreate">新增套餐</el-button>
      </div>
    </div>

    <div class="card p-0 overflow-hidden">
      <el-table :data="filteredList" style="width: 100%">
        <el-table-column prop="id" label="编号" width="90" />
        <el-table-column label="封面" width="100">
          <template #default="{ row }">
            <el-image 
              v-if="row.image"
              :src="formatImageUrl(row.image)" 
              class="w-12 h-12 rounded-lg shadow-sm border border-slate-100" 
              fit="cover" 
              :preview-src-list="[formatImageUrl(row.image)]" 
              preview-teleported
            />
            <div v-else class="w-12 h-12 bg-slate-50 rounded-lg flex flex-col items-center justify-center border border-dashed border-slate-200">
              <el-icon class="text-slate-300" :size="16"><Picture /></el-icon>
              <span class="text-[9px] text-slate-400 mt-1">无图</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="套餐名" min-width="200">
          <template #default="{ row }">
            <div class="font-bold">{{ row.name }}</div>
            <div class="text-[10px] text-slate-400 mt-0.5">{{ row.subtitle || '无副标题' }}</div>
          </template>
        </el-table-column>
        <el-table-column label="分类" width="120">
          <template #default="{ row }">
            <el-tag :type="tagType(row.category)">
              {{ row.category }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="价格" width="120">
          <template #default="{ row }">¥ {{ row.price }}</template>
        </el-table-column>
        <el-table-column label="状态" width="120">
          <template #default="{ row }">
            <el-switch v-model="row.enabled" :loading="row.updating" @change="handleToggleStatus(row)" />
          </template>
        </el-table-column>
        <el-table-column prop="updatedAt" label="更新时间" min-width="180" />
        <el-table-column label="操作" width="160" align="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
            <el-button link type="danger" @click="remove(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-drawer v-model="showDrawer" :title="isEdit ? '编辑套餐' : '新增套餐'" size="460px">
      <el-form :model="form" label-position="top">
        <el-form-item label="套餐名"><el-input v-model="form.name" /></el-form-item>
        <el-form-item label="副标题/简短描述">
          <el-input v-model="form.subtitle" placeholder="例如：约 50 分钟，基础发型整理" />
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="form.category" class="w-full" placeholder="请选择服务分类">
            <el-option value="妆造" label="妆造" />
            <el-option value="摄影" label="摄影" />
            <el-option value="道具" label="道具" />
          </el-select>
        </el-form-item>
        <el-form-item label="价格"><el-input-number v-model="form.price" :min="0" class="w-full" /></el-form-item>
        <el-form-item label="套餐封面">
          <el-upload
            action="#"
            :show-file-list="false"
            :auto-upload="false"
            :on-change="handleImageChange"
            accept="image/*"
            class="service-uploader"
            :disabled="uploading"
          >
            <div v-if="form.image" class="relative group w-36 h-36 rounded-xl overflow-hidden border border-slate-200 shadow-inner bg-slate-50">
              <img :src="formatImageUrl(form.image)" class="w-full h-full object-cover" />
              <div class="absolute inset-0 bg-black/50 opacity-0 group-hover:opacity-100 transition-opacity flex flex-col items-center justify-center text-white">
                <el-icon size="20"><Plus /></el-icon>
                <span class="text-[10px] mt-1 font-bold">点击更换</span>
              </div>
              <div v-if="uploading" class="absolute inset-0 bg-white/80 flex flex-col items-center justify-center">
                <el-icon class="is-loading text-primary" size="24"><Loading /></el-icon>
                <span class="text-[10px] text-primary mt-2">上传中...</span>
              </div>
            </div>
            <div v-else class="w-36 h-36 border-2 border-dashed border-slate-200 rounded-xl flex flex-col items-center justify-center text-slate-400 hover:border-primary transition-all hover:bg-slate-50 group">
              <el-icon size="32" class="group-hover:scale-110 transition-transform"><Picture /></el-icon>
              <span class="text-xs mt-2 font-bold uppercase tracking-wider">上传素材</span>
            </div>
          </el-upload>
          <div class="mt-3">
            <el-input v-model="form.image" placeholder="图片访问地址 (URL)" size="small">
              <template #prefix><el-icon><Link /></el-icon></template>
            </el-input>
          </div>
        </el-form-item>
        <el-form-item label="描述"><el-input v-model="form.description" type="textarea" :rows="4" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showDrawer = false">取消</el-button>
        <el-button type="primary" @click="save">保存</el-button>
      </template>
    </el-drawer>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Loading, Link, Picture } from '@element-plus/icons-vue'
import { uploadAvatarFile } from '@/api/user'
import {
  adminFetchServiceList,
  adminCreateService,
  adminUpdateService,
  adminDeleteService,
  type ServiceCategory,
  type ServiceItem
} from '@/api/service'

const showDrawer = ref(false)
const isEdit = ref(false)
const uploading = ref(false)
const saving = ref(false)
const searchQuery = ref('')
const serviceList = ref<ServiceItem[]>([])
const form = ref<Partial<ServiceItem>>({})
const isOk = (res: any) => res && (res.code === 200 || res.code === 0)

/** 格式化图片地址，解决相对路径显示问题 */
const formatImageUrl = (url?: string) => {
  if (!url) return ''
  if (url.startsWith('http') || url.startsWith('data:')) return url
  // 如果是相对路径，确保有前导斜杠
  const path = url.startsWith('/') ? url : `/${url}`
  return path
}

const filteredList = computed(() => {
  if (!searchQuery.value) return serviceList.value
  return serviceList.value.filter((item) => item.name.includes(searchQuery.value))
})

const handleSearch = () => {}

const categoryLabel = (category: ServiceCategory) =>
  category === 'makeup' ? '妆造' : category === 'photo' ? '摄影' : '道具'

const openCreate = () => {
  isEdit.value = false
  form.value = { 
    name: '', 
    category: '妆造', 
    price: 0, 
    description: '', 
    enabled: true,
    image: '',
    subtitle: '' 
  }
  showDrawer.value = true
}

const openEdit = (row: ServiceItem) => {
  isEdit.value = true
  form.value = { ...row }
  showDrawer.value = true
}

const handleImageChange = async (file: any) => {
  const raw = file.raw
  if (!raw) return
  
  // 1. 本地 Base64 快速预览
  const reader = new FileReader()
  reader.onload = (e) => {
    if (!uploading.value && form.value) {
      form.value.image = String(e.target?.result || '')
    }
  }
  reader.readAsDataURL(raw)
  
  // 2. 真正上传到服务器
  uploading.value = true
  try {
    const url = await uploadAvatarFile(raw)
    if (form.value) {
      form.value.image = url
      ElMessage.success('预览图已同步至服务器')
    }
  } catch (err) {
    ElMessage.info('暂存为本地预览，保存后尝试持久化')
  } finally {
    uploading.value = false
  }
}

const save = async () => {
  if (!form.value.name) {
    ElMessage.warning('请填写套餐名')
    return
  }
  if (uploading.value) {
    ElMessage.warning('请等待图片上传完成')
    return
  }
  
  saving.value = true
  try {
    let res: any
    // 确保数据结构完整
    const payload = {
      ...form.value,
      image: form.value.image || ''
    }
    
    if (isEdit.value) {
      res = await adminUpdateService(payload)
    } else {
      res = await adminCreateService(payload)
    }
    
    if (isOk(res)) {
      await loadList()
      showDrawer.value = false
      ElMessage.success('保存成功')
    } else {
      ElMessage.error(res?.message || '保存失败，请检查数据格式')
    }
  } catch (err: any) {
    ElMessage.error('服务器异常: ' + (err.message || '未知错误'))
  } finally {
    saving.value = false
  }
}

const remove = (id: number) => {
  ElMessageBox.confirm('确定要删除该服务套餐吗？删除后将无法恢复。', {
    type: 'warning',
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    showClose: false,
    alignCenter: true
  }).then(() => {
    adminDeleteService(id).then((res) => {
      if (!isOk(res)) return
      loadList()
      ElMessage.success('删除成功')
    })
  })
}

const loadList = async () => {
  const res: any = await adminFetchServiceList()
  if (isOk(res)) serviceList.value = res.data || []
}

const handleToggleStatus = async (row: any) => {
  row.updating = true
  try {
    const res = await adminUpdateService({
      id: row.id,
      enabled: row.enabled
    })
    if (isOk(res)) {
      ElMessage.success(row.enabled ? '套餐已上架' : '套餐已下架')
    } else {
      row.enabled = !row.enabled
      ElMessage.error('状态更新失败')
    }
  } catch {
    row.enabled = !row.enabled
    ElMessage.error('网络异常')
  } finally {
     row.updating = false
  }
}

const tagType = (cat: string) => {
  if (cat.includes('妆')) return 'danger'
  if (cat.includes('摄')) return 'warning'
  return 'info'
}

onMounted(loadList)
</script>

<style scoped>
.card { background: #fff; border: 1px solid var(--lh-border); border-radius: 16px; box-shadow: var(--shadow-sm); }
.title { font-size: 26px; font-weight: 800; color: var(--lh-navy); letter-spacing: 0.08em; }
.sub { margin-top: 6px; color: var(--lh-text-secondary); font-size: 13px; }
</style>
