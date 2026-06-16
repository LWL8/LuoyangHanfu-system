<template>
  <div class="image-upload-wrapper w-full">
    <el-upload
      v-model:file-list="fileList"
      action="#"
      list-type="picture-card"
      multiple
      :auto-upload="false"
      :on-preview="handlePictureCardPreview"
      :on-remove="handleRemove"
      :before-upload="beforeUpload"
      class="custom-uploader"
    >
      <div class="flex flex-col items-center justify-center text-gray-400">
         <el-icon size="28 text-[var(--gold)]/40"><Plus /></el-icon>
         <p class="text-[10px] mt-2 group-hover:text-[var(--gold)]">批量添加</p>
      </div>
      <template #tip>
        <div class="el-upload__tip text-xs text-gray-400 mt-2">
          支持 JPG/PNG 格式，单张不超过 5MB，系统将自动进行压缩。
        </div>
      </template>
    </el-upload>

    <!-- 预览弹窗 -->
    <el-dialog v-model="dialogVisible">
      <img w-full :src="dialogImageUrl" alt="Preview Image" class="w-full" />
    </el-dialog>

    <div v-if="fileList.length > 0" class="mt-6 flex justify-end">
       <el-button type="primary" class="btn-primary px-8 h-10" :loading="uploading" @click="handleBatchUpload">
          开始批量上传 ({{ fileList.length }}张)
       </el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import type { UploadProps, UploadUserFile } from 'element-plus'

const fileList = ref<UploadUserFile[]>([])
const dialogImageUrl = ref('')
const dialogVisible = ref(false)
const uploading = ref(false)

const handleRemove: UploadProps['onRemove'] = (uploadFile, uploadFiles) => {
  console.log(uploadFile, uploadFiles)
}

const handlePictureCardPreview: UploadProps['onPreview'] = (uploadFile) => {
  dialogImageUrl.value = uploadFile.url!
  dialogVisible.value = true
}

const beforeUpload: UploadProps['beforeUpload'] = (rawFile) => {
  if (rawFile.size / 1024 / 1024 > 5) {
    // @ts-ignore
    window.ElMessage.error('单张图片不能超过 5MB!')
    return false
  }
  return true
}

const handleBatchUpload = async () => {
  uploading.value = true
  
  // 模拟压缩与分片上传进度
  for (let i = 0; i < fileList.value.length; i++) {
    fileList.value[i].status = 'uploading'
    fileList.value[i].percentage = 0
    
    // 模拟 Canvas 压缩过程 (此处省略具体实现，仅用延迟模拟)
    await new Promise(resolve => setTimeout(resolve, 300))
    
    let progress = 0
    const interval = setInterval(() => {
      progress += Math.floor(Math.random() * 30)
      if (progress >= 100) {
        progress = 100
        fileList.value[i].status = 'success'
        fileList.value[i].percentage = 100
        clearInterval(interval)
      } else {
        fileList.value[i].percentage = progress
      }
    }, 200)
    
    await new Promise(resolve => {
       const check = setInterval(() => {
          if (fileList.value[i].status === 'success') {
             clearInterval(check)
             resolve(true)
          }
       }, 100)
    })
  }

  uploading.value = false
  // @ts-ignore
  window.ElMessage.success('批量图片处理并同步成功')
}
</script>

<style scoped>
.custom-uploader :deep(.el-upload--picture-card) {
  border: 1px dashed var(--gold);
  background-color: #FAF6EF/30;
  border-radius: 16px;
  width: 100px;
  height: 100px;
}
.custom-uploader :deep(.el-upload-list--picture-card .el-upload-list__item) {
   width: 100px;
   height: 100px;
   border-radius: 16px;
}
</style>
