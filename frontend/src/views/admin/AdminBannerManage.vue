<template>
  <div class="space-y-6 animate__animated animate__fadeIn">
    <!-- 顶部状态栏 -->
    <div class="card p-6 flex items-center justify-between gap-4">
      <div>
        <h2 class="title flex items-center gap-3">
          <el-icon class="text-[var(--lh-red)]"><Picture /></el-icon>
          <span>轮播图管理</span>
        </h2>
        <p class="sub">配置首页视觉重心，引导用户发现热门汉服与活动</p>
      </div>
      <div class="flex gap-3">
        <el-button type="primary" class="premium-btn" @click="openCreate">
          <el-icon class="mr-1"><Plus /></el-icon>新增轮播图
        </el-button>
      </div>
    </div>

    <!-- 数据表格 -->
    <div class="card p-0 overflow-hidden">
      <el-table :data="tableData" v-loading="loading" style="width: 100%">
        <el-table-column prop="sort" label="排序" width="90" align="center" />
        <el-table-column label="预览" width="160">
          <template #default="{ row }">
            <div class="relative group cursor-zoom-in">
              <el-image 
                :src="row.image" 
                class="w-28 h-16 rounded-xl border-2 border-white shadow-md transition-transform group-hover:scale-105" 
                fit="cover" 
                :preview-src-list="[row.image]" 
              />
              <div class="absolute inset-0 bg-black/10 rounded-xl opacity-0 group-hover:opacity-100 transition-opacity flex items-center justify-center">
                <el-icon class="text-white text-xl"><ZoomIn /></el-icon>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="title" label="广告描述" min-width="200">
          <template #default="{ row }">
             <div class="font-serif font-black text-[var(--lh-navy)] text-lg mb-1">{{ row.title }}</div>
             <div class="text-[10px] text-slate-400 font-bold uppercase tracking-widest">{{ row.link }}</div>
          </template>
        </el-table-column>
        <el-table-column label="显示状态" width="120">
          <template #default="{ row }">
            <el-switch 
              v-model="row.enabled" 
              active-text="开启"
              inactive-text="关闭"
              inline-prompt
              style="--el-switch-on-color: var(--lh-red)"
              @change="(v) => handleToggleEnable(row, !!v)" 
            />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" align="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
            <el-popconfirm title="确定要删除?" width="160" confirm-button-text="删除" cancel-button-text="取消" @confirm="remove(row.id)">
              <template #reference>
                <el-button link type="danger">删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 编辑抽屉 -->
    <el-drawer 
      v-model="showDrawer" 
      :title="isEdit ? '编辑视觉位' : '极简新增轮播'" 
      size="500px"
      custom-class="heritage-drawer"
    >
      <el-form :model="form" label-position="top" class="p-4">
        <el-form-item label="轮播图标题">
          <el-input v-model="form.title" placeholder="如：唐制齐胸襦裙 · 盛世风华" />
        </el-form-item>

        <el-form-item label="跳转内容 / 路由">
          <div class="flex gap-2 w-full">
            <el-input v-model="form.link" placeholder="内部路径或外部 URL" class="flex-1" />
            <el-select 
              v-model="form.link" 
              placeholder="常用路径" 
              class="w-32"
              @change="(val) => form.link = val"
            >
              <el-option label="首页" value="/home" />
              <el-option label="预约页" value="/booking" />
              <el-option label="汉服品鉴" value="/hanfu" />
              <el-option label="穿搭攻略" value="/luoyang/guides" />
              <el-option label="周边景点" value="/luoyang" />
            </el-select>
          </div>
        </el-form-item>

        <el-form-item label="轮播素材 (推荐 1920x800)">
          <div class="premium-upload-area w-full">
            <el-upload
              action="#"
              :show-file-list="false"
              accept="image/*"
              :auto-upload="false"
              :on-change="handleBannerImageChange"
              class="w-full"
            >
              <div v-if="!form.image" class="border-2 border-dashed border-slate-200 rounded-2xl h-40 flex flex-col items-center justify-center hover:border-[var(--lh-red)] transition-colors cursor-pointer group">
                <el-icon size="40" class="text-slate-300 group-hover:text-[var(--lh-red)]"><Plus /></el-icon>
                <span class="text-xs font-bold text-slate-400 mt-2 uppercase tracking-widest">选择图片素材</span>
              </div>
              <div v-else class="relative w-full h-48 rounded-2xl overflow-hidden group cursor-pointer">
                <img :src="form.image" class="w-full h-full object-cover transition-transform group-hover:scale-105" />
                <div class="absolute inset-0 bg-black/40 opacity-0 group-hover:opacity-100 transition-opacity flex items-center justify-center">
                  <span class="text-white text-xs font-bold ring-2 ring-white px-4 py-2 rounded-full">点击更换</span>
                </div>
              </div>
            </el-upload>
            <div class="mt-4 flex gap-3">
              <el-input v-model="form.image" placeholder="或直接输入图片 URL" size="small" />
              <el-button v-if="form.image" type="danger" link @click="form.image = ''">清空图片</el-button>
            </div>
          </div>
        </el-form-item>

        <el-form-item label="全局排序优先级">
          <el-input-number v-model="form.sort" :min="1" class="w-full" />
          <p class="text-[10px] text-slate-400 mt-1 uppercase tracking-widest italic">数字越小展示位置越靠前</p>
        </el-form-item>
      </el-form>

      <template #footer>
        <div class="flex gap-4 px-4 pb-8">
          <el-button @click="showDrawer = false" class="flex-1 h-12 rounded-2xl">暂不发布</el-button>
          <el-button type="primary" :loading="saving" @click="save" class="flex-1 h-12 rounded-2xl premium-btn">
            确认发布
          </el-button>
        </div>
      </template>
    </el-drawer>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { UploadProps, UploadRawFile } from 'element-plus'
import { Plus, Picture, ZoomIn } from '@element-plus/icons-vue'
import { uploadAvatarFile } from '@/api/user'
import {
  adminFetchBannerList,
  adminCreateBanner,
  adminUpdateBanner,
  adminDeleteBanner,
  type BannerItem
} from '@/api/banner'

const showDrawer = ref(false)
const isEdit = ref(false)
const loading = ref(false)
const saving = ref(false)
const form = ref<Partial<BannerItem>>({})
const list = ref<BannerItem[]>([])
const uploading = ref(false)

const isOk = (res: any) => res && (res.code === 200 || res.code === 0)
const tableData = computed(() => [...list.value].sort((a, b) => a.sort - b.sort))

const loadData = async () => {
  loading.value = true
  try {
    const res: any = await adminFetchBannerList()
    if (isOk(res)) list.value = res.data || []
  } finally {
    loading.value = false
  }
}

const openCreate = () => {
  isEdit.value = false
  form.value = { title: '', image: '', link: '/booking', sort: list.value.length + 1, enabled: true }
  showDrawer.value = true
}

const openEdit = (row: BannerItem) => {
  isEdit.value = true
  form.value = { ...row }
  showDrawer.value = true
}

const save = async () => {
  if (!form.value.title || !form.value.image) {
    ElMessage.warning('请填写完整的标题和图片内容')
    return
  }
  saving.value = true
  try {
    const res: any = isEdit.value ? await adminUpdateBanner(form.value) : await adminCreateBanner(form.value)
    if (!isOk(res)) {
      if (form.value.image?.startsWith('data:')) {
        ElMessage.error('发布失败：图片素材过大，请尝试压缩图片后重新选择')
      } else {
        ElMessage.error('发布失败：' + (res.message || '网络异常'))
      }
      return
    }
    
    await loadData()
    showDrawer.value = false
    ElMessage.success(isEdit.value ? '已更新视觉配置' : '新轮播图已发布')
  } finally {
    saving.value = false
  }
}

const remove = async (id: number) => {
  const res: any = await adminDeleteBanner(id)
  if (!isOk(res)) return
  loadData()
  ElMessage.success('已移除')
}

const handleBannerImageChange: UploadProps['onChange'] = async (uploadFile) => {
  const raw = uploadFile.raw as UploadRawFile | undefined
  if (!raw) return
  if (raw.size / 1024 / 1024 > 10) {
    ElMessage.warning('素材过大，建议压缩至 10MB 以内')
    return
  }
  uploading.value = true
  try {
    // 简单 base64 预览或后端上传
    const reader = new FileReader()
    reader.onload = (e) => {
      form.value.image = String(e.target?.result || '')
    }
    reader.readAsDataURL(raw)
    
    // 如果需要后端存储，可以在这里调用上传接口，但不再自动 save()
    try {
      const url = await uploadAvatarFile(raw as unknown as File)
      form.value.image = url
      ElMessage.success('轮播素材已成功同步至云端')
    } catch (e: any) {
      console.error('Bannner upload failed:', e)
      ElMessage.info('云端同步失败，已切换至本地大字段模式（图片若过大可能导致发布失败，建议压缩）')
    }
  } finally {
    uploading.value = false
  }
}

const handleToggleEnable = async (row: BannerItem, enabled: boolean) => {
  const prev = row.enabled
  const res = await adminUpdateBanner({ id: row.id, enabled })
  if (!isOk(res)) {
    row.enabled = prev
    ElMessage.warning('权限同步失败')
    return
  }
  ElMessage.success(enabled ? '已将该内容投放至首页' : '已从首页下架')
}

onMounted(loadData)
</script>

<style scoped>
.card { background: #fff; border: 1px solid var(--lh-border); border-radius: 20px; box-shadow: var(--shadow-sm); }
.title { font-size: 26px; font-weight: 900; color: var(--lh-navy); letter-spacing: 0.1em; }
.sub { margin-top: 6px; color: var(--lh-text-secondary); font-size: 13px; font-weight: 500; }

.premium-btn {
  background-color: var(--lh-navy);
  border: none;
  padding: 12px 28px;
  border-radius: 14px;
  font-weight: 800;
  letter-spacing: 0.1em;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.premium-btn:hover {
  background-color: var(--lh-red);
  transform: translateY(-2px);
  box-shadow: 0 10px 25px rgba(153, 27, 27, 0.2);
}

:deep(.el-table) {
  --el-table-header-bg-color: var(--lh-cream);
  --el-table-row-hover-bg-color: #f1f5f9;
}

:deep(.el-table tr) {
  transition: all 0.3s ease;
}

:deep(.el-table th.el-table__cell) {
  font-weight: 900;
  color: var(--lh-navy);
  text-transform: uppercase;
  font-size: 11px;
  letter-spacing: 0.2em;
  padding: 20px 0;
}
</style>

