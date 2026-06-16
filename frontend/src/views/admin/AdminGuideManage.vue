<template>
  <div class="space-y-6 animate__animated animate__fadeIn">
    <!-- 顶部状态栏 -->
    <div class="card p-6 flex items-center justify-between gap-4">
      <div>
        <h2 class="title flex items-center gap-3">
          <el-icon class="text-[var(--lh-red)]"><Compass /></el-icon>
          <span>洛阳攻略管理</span>
        </h2>
        <p class="sub">发布神都穿搭指南、景点搭配建议与拍摄技巧</p>
      </div>
      <div class="flex gap-3">
        <el-input 
          v-model="searchQuery" 
          placeholder="搜索风格名称..." 
          :prefix-icon="Search" 
          clearable 
          class="w-64"
          @keyup.enter="handleSearch" 
        />
        <el-button type="primary" class="premium-btn" @click="handleAdd">
          <el-icon class="mr-1"><Plus /></el-icon>发布新攻略
        </el-button>
      </div>
    </div>

    <!-- 数据表格 -->
    <div class="card p-0 overflow-hidden">
      <el-table :data="guideList" v-loading="loading" style="width:100%">
        <el-table-column prop="id" label="编号" width="80" align="center" />
        <el-table-column label="示例图" width="120">
          <template #default="{ row }">
            <div class="flex -space-x-4">
              <el-image :src="row.imageBefore" class="w-12 h-16 rounded-lg border-2 border-white shadow-sm hover:z-10 transition-all hover:scale-110" fit="cover" />
              <el-image :src="row.imageAfter" class="w-12 h-16 rounded-lg border-2 border-white shadow-sm hover:z-10 transition-all hover:scale-110" fit="cover" />
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="style" label="穿搭风格" width="180">
          <template #default="{ row }">
             <span class="font-serif font-black text-[var(--lh-navy)] tracking-wider">{{ row.style }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="scenic" label="推荐场景" width="140">
           <template #default="{ row }">
              <el-tag effect="plain" type="info" class="border-[var(--lh-gold)] text-[var(--lh-gold)]">{{ row.scenic }}</el-tag>
           </template>
        </el-table-column>
        <el-table-column prop="reason" label="搭配理由" min-width="240" show-overflow-tooltip />
        <el-table-column label="操作" width="150" align="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
            <el-button link type="danger" @click="handleDelete(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="p-6 flex justify-end border-t border-[var(--lh-border)] bg-[var(--lh-cream)]/20">
        <el-pagination 
          v-model:current-page="pageNum" 
          v-model:page-size="pageSize" 
          :total="total" 
          layout="prev, pager, next, total" 
          @current-change="handleSearch" 
        />
      </div>
    </div>

    <!-- 编辑抽屉 -->
    <el-drawer 
      v-model="showDrawer" 
      :title="isEdit ? '编辑攻略志' : '撰写新攻略'" 
      size="600px"
      custom-class="heritage-drawer"
    >
      <el-form :model="form" label-position="top" class="p-4">
        <div class="grid grid-cols-2 gap-6">
          <el-form-item label="穿搭风格名称">
            <el-input v-model="form.style" placeholder="如：初唐 · 齐胸襦裙" />
          </el-form-item>
          <el-form-item label="推荐拍摄地点">
            <el-input v-model="form.scenic" placeholder="如：应天门" />
          </el-form-item>
        </div>

        <el-form-item label="搭配理由 / 方案背景">
          <el-input v-model="form.reason" type="textarea" :rows="4" placeholder="写一点关于这套穿搭与景观的渊源..." />
        </el-form-item>

        <div class="grid grid-cols-2 gap-6">
          <el-form-item label="示例图 (风格)">
            <div class="premium-upload-area w-full">
              <el-upload
                action="#"
                :show-file-list="false"
                accept="image/*"
                :auto-upload="false"
                :on-change="handleImageBeforeChange"
                class="w-full"
              >
                <div v-if="!form.imageBefore" class="upload-placeholder h-32">
                  <el-icon size="30" class="text-slate-300"><Plus /></el-icon>
                  <span class="text-xs font-bold text-slate-400 mt-2 uppercase tracking-widest">选择风格素材</span>
                </div>
                <div v-else class="upload-preview h-32">
                  <img :src="form.imageBefore" class="w-full h-full object-cover" />
                  <div class="preview-overlay">
                    <span class="text-white text-xs font-bold border border-white px-3 py-1 rounded-full">更换</span>
                  </div>
                </div>
              </el-upload>
              <div class="mt-2 flex gap-2">
                <el-input v-model="form.imageBefore" placeholder="或输入 URL" size="small" />
              </div>
            </div>
          </el-form-item>
          <el-form-item label="示例图 (实景)">
            <div class="premium-upload-area w-full">
              <el-upload
                action="#"
                :show-file-list="false"
                accept="image/*"
                :auto-upload="false"
                :on-change="handleImageAfterChange"
                class="w-full"
              >
                <div v-if="!form.imageAfter" class="upload-placeholder h-32">
                  <el-icon size="30" class="text-slate-300"><Plus /></el-icon>
                  <span class="text-xs font-bold text-slate-400 mt-2 uppercase tracking-widest">选择实景素材</span>
                </div>
                <div v-else class="upload-preview h-32">
                  <img :src="form.imageAfter" class="w-full h-full object-cover" />
                  <div class="preview-overlay">
                    <span class="text-white text-xs font-bold border border-white px-3 py-1 rounded-full">更换</span>
                  </div>
                </div>
              </el-upload>
              <div class="mt-2 flex gap-2">
                <el-input v-model="form.imageAfter" placeholder="或输入 URL" size="small" />
              </div>
            </div>
          </el-form-item>
        </div>

        <el-form-item label="拍摄贴士 (Tips)">
          <div class="space-y-3 w-full">
            <div v-for="(tip, index) in form.photographyTips" :key="index" class="flex gap-2">
              <el-input v-model="form.photographyTips[index]" :placeholder="`贴士 ${index + 1}`" />
              <el-button type="danger" link @click="removeTip(index)" :disabled="form.photographyTips.length <= 1">
                <el-icon><Delete /></el-icon>
              </el-button>
            </div>
            <el-button type="primary" link @click="addTip" class="mt-2">
              <el-icon class="mr-1"><Plus /></el-icon> 添加拍摄技巧
            </el-button>
          </div>
        </el-form-item>
      </el-form>
      
      <template #footer>
        <div class="flex gap-3 px-4 pb-6">
          <el-button @click="showDrawer=false" class="flex-1 height-12">取消</el-button>
          <el-button type="primary" :loading="saving" @click="saveGuide" class="flex-1 height-12 premium-btn">保存发布</el-button>
        </div>
      </template>
    </el-drawer>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { Search, Plus, Delete, Compass } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox, type UploadProps, type UploadRawFile } from 'element-plus'
import { adminFetchGuideList, adminAddGuide, adminUpdateGuide, adminDeleteGuide, type GuideItem } from '@/api/luoyang'
import { uploadAvatarFile } from '@/api/user'

const loading = ref(false)
const saving = ref(false)
const uploading = ref(false)
const guideList = ref<GuideItem[]>([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)
const searchQuery = ref('')
const showDrawer = ref(false)
const isEdit = ref(false)

const form = ref<Partial<GuideItem>>({
  style: '',
  scenic: '',
  reason: '',
  photographyTips: [''],
  imageBefore: '',
  imageAfter: ''
})

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
  return extractPageList(data).length || 0
}

const handleSearch = async () => {
  loading.value = true
  try {
    const res: any = await adminFetchGuideList({
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      style: searchQuery.value || undefined
    })
    if (isOk(res)) {
      guideList.value = extractPageList(res.data)
      total.value = extractPageTotal(res.data)
    }
  } finally {
    loading.value = false
  }
}

const handleAdd = () => {
  isEdit.value = false
  form.value = {
    style: '',
    scenic: '',
    reason: '',
    photographyTips: [''],
    imageBefore: '',
    imageAfter: ''
  }
  showDrawer.value = true
}

const handleEdit = (row: GuideItem) => {
  isEdit.value = true
  // 深拷贝，防止直接修改数组
  form.value = JSON.parse(JSON.stringify(row))
  showDrawer.value = true
}

const handleDelete = (id: number) => {
  ElMessageBox.confirm('确定要移除这篇攻略吗？', {
    type: 'warning',
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    showClose: false,
    alignCenter: true
  }).then(async () => {
    const res: any = await adminDeleteGuide(id)
    if (res.code === 200 || res.code === 0) {
      ElMessage.success('已移除')
      handleSearch()
    }
  })
}

const addTip = () => form.value.photographyTips?.push('')
const removeTip = (index: number) => form.value.photographyTips?.splice(index, 1)

const handleImageBeforeChange: UploadProps['onChange'] = async (uploadFile) => {
  const raw = uploadFile.raw as UploadRawFile | undefined
  if (!raw) return
  uploading.value = true
  try {
    const url = await uploadAvatarFile(raw as unknown as File)
    form.value.imageBefore = url
    ElMessage.success('风格素材上传成功')
  } catch (e) {
    ElMessage.error('上传失败，请重试')
  } finally {
    uploading.value = false
  }
}

const handleImageAfterChange: UploadProps['onChange'] = async (uploadFile) => {
  const raw = uploadFile.raw as UploadRawFile | undefined
  if (!raw) return
  uploading.value = true
  try {
    const url = await uploadAvatarFile(raw as unknown as File)
    form.value.imageAfter = url
    ElMessage.success('实景素材上传成功')
  } catch (e) {
    ElMessage.error('上传失败，请重试')
  } finally {
    uploading.value = false
  }
}

const saveGuide = async () => {
  // 简单验证
  if (!form.value.style || !form.value.scenic) {
    return ElMessage.warning('请填写风格和景点名称')
  }
  
  saving.value = true
  try {
    const res: any = isEdit.value 
      ? await adminUpdateGuide(form.value) 
      : await adminAddGuide(form.value)
      
    if (res.code === 200 || res.code === 0) {
      ElMessage.success('发布成功')
      showDrawer.value = false
      handleSearch()
    }
  } finally {
    saving.value = false
  }
}

onMounted(handleSearch)
</script>

<style scoped>
.card {
  background: #fff;
  border: 1px solid var(--lh-border);
  border-radius: 20px;
  box-shadow: var(--shadow-sm);
}

.title {
  font-size: 24px;
  font-weight: 900;
  color: var(--lh-navy);
  letter-spacing: 0.1em;
}

.sub {
  margin-top: 4px;
  color: var(--lh-text-secondary);
  font-size: 13px;
  font-weight: 500;
}

.premium-btn {
  background-color: var(--lh-navy);
  border: none;
  padding: 12px 24px;
  border-radius: 12px;
  font-weight: 700;
  letter-spacing: 0.1em;
  transition: all 0.3s ease;
}

.premium-btn:hover {
  background-color: var(--lh-red);
  transform: translateY(-2px);
  box-shadow: 0 10px 20px rgba(153, 27, 27, 0.2);
}

:deep(.el-table) {
  --el-table-header-bg-color: var(--lh-cream);
  --el-table-row-hover-bg-color: #f8fafc;
}

:deep(.el-table th.el-table__cell) {
  font-weight: 800;
  color: var(--lh-navy);
  text-transform: uppercase;
  font-size: 12px;
  letter-spacing: 0.1em;
  padding: 16px 0;
}

:deep(.el-drawer__header) {
  margin-bottom: 0;
  padding: 24px;
  border-bottom: 1px solid var(--lh-border);
  font-family: var(--font-serif);
  font-weight: 900;
}

:deep(.el-form-item__label) {
  font-weight: 700;
  color: var(--lh-navy);
  padding-bottom: 4px;
}

.height-12 {
  height: 48px;
}

.upload-placeholder {
  border-width: 2px;
  border-style: dashed;
  border-color: #e2e8f0;
  border-radius: 0.75rem;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition-property: all;
  transition-timing-function: cubic-bezier(0.4, 0, 0.2, 1);
  transition-duration: 150ms;
}

.upload-placeholder:hover {
  border-color: var(--lh-red);
}

.upload-preview {
  position: relative;
  width: 100%;
  border-radius: 0.75rem;
  overflow: hidden;
  cursor: pointer;
}

.preview-overlay {
  position: absolute;
  inset: 0;
  background-color: rgba(0, 0, 0, 0.4);
  opacity: 0;
  transition-property: opacity;
  transition-timing-function: cubic-bezier(0.4, 0, 0.2, 1);
  transition-duration: 150ms;
  display: flex;
  align-items: center;
  justify-content: center;
}

.upload-preview:hover .preview-overlay {
  opacity: 1;
}
</style>
