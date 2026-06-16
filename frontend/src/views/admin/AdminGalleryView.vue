<template>
  <div class="space-y-6">
    <div class="card p-6 flex items-center justify-between gap-4">
      <div>
        <h2 class="title">素材图库</h2>
        <p class="sub">集中管理汉服与场景素材</p>
      </div>
      <div class="flex gap-3">
        <el-radio-group v-model="filterType">
          <el-radio-button label="all">全部</el-radio-button>
          <el-radio-button label="hanfu">汉服</el-radio-button>
          <el-radio-button label="scenic">景点</el-radio-button>
          <el-radio-button label="other">其他</el-radio-button>
        </el-radio-group>
        <el-button type="primary" @click="showUpload = true">批量上传</el-button>
      </div>
    </div>

    <div class="card p-6">
      <div class="grid grid-cols-2 md:grid-cols-4 lg:grid-cols-6 gap-4">
        <div v-for="(img, idx) in filteredGallery" :key="idx" class="item group">
          <el-image :src="img.url" fit="cover" class="w-full h-full" :preview-src-list="[img.url]" />
          <div class="mask">
            <el-button circle size="small" type="danger" @click.stop="handleDelete(idx)"><el-icon><Delete /></el-icon></el-button>
            <el-button circle size="small" @click.stop="copyUrl(img.url)"><el-icon><Link /></el-icon></el-button>
          </div>
          <el-tag size="small" class="tag">{{ img.type === 'hanfu' ? '汉服' : img.type === 'scenic' ? '景点' : '其他' }}</el-tag>
        </div>
      </div>
    </div>

    <el-dialog v-model="showUpload" title="批量上传素材" width="620px" append-to-body>
      <ImageUpload />
      <template #footer><el-button @click="showUpload = false">完成</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { Delete, Link } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import ImageUpload from '@/components/common/ImageUpload.vue'

const showUpload = ref(false)
const filterType = ref('all')

const gallery = ref([
  { url: 'https://images.unsplash.com/photo-1543854589-32a26569eb21?w=400', type: 'hanfu' },
  { url: 'https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=400', type: 'hanfu' },
  { url: 'https://images.unsplash.com/photo-1574169208507-84376144848b?w=400', type: 'scenic' },
  { url: 'https://images.unsplash.com/photo-1522335789203-aabd1fc54bc9?w=400', type: 'scenic' },
  { url: 'https://images.unsplash.com/photo-1512496015851-a90fb38ba496?w=400', type: 'hanfu' },
  { url: 'https://images.unsplash.com/photo-1492691527719-9d1e07e534b4?w=400', type: 'other' },
])

const filteredGallery = computed(() => filterType.value === 'all' ? gallery.value : gallery.value.filter(i => i.type === filterType.value))

const handleDelete = (index: number) => {
  ElMessageBox.confirm('确定要删除该素材吗？删除后将无法恢复。', {
    type: 'warning',
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    showClose: false,
    alignCenter: true
  }).then(() => {
    gallery.value.splice(index, 1)
    ElMessage.success('已删除')
  })
}

const copyUrl = (url: string) => {
  navigator.clipboard.writeText(url)
  ElMessage.success('链接已复制')
}
</script>

<style scoped>
.card { background: #fff; border: 1px solid var(--lh-border); border-radius: 16px; box-shadow: var(--shadow-sm); }
.title { font-size: 26px; font-weight: 800; color: var(--lh-navy); letter-spacing: 0.08em; }
.sub { margin-top: 6px; color: var(--lh-text-secondary); font-size: 13px; }
.item { position: relative; aspect-ratio: 1/1; border-radius: 12px; overflow: hidden; border: 1px solid var(--lh-border); }
.mask { position: absolute; inset: 0; display: flex; align-items: center; justify-content: center; gap: 8px; background: rgba(0,0,0,.35); opacity: 0; transition: .2s; }
.item:hover .mask { opacity: 1; }
.tag { position: absolute; left: 8px; bottom: 8px; border: none; }
</style>
