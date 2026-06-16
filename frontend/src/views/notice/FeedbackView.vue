<template>
  <div class="feedback-view min-h-screen bg-[var(--lh-cream)] pb-32 animate__animated animate__fadeIn">
    <HeaderNav />
    
    <!-- 艺术横幅 (Hero Section) -->
    <section class="relative h-[440px] md:h-[500px] overflow-hidden flex items-center pt-24 mb-16 px-6 md:px-16">
      <div class="absolute inset-0 z-0">
        <img src="https://images.unsplash.com/photo-1518709268805-4e9042af9f23?q=80&w=1600" class="w-full h-full object-cover brightness-50 contrast-125" />
        <div class="absolute inset-0 bg-gradient-to-b from-[var(--lh-navy)]/60 via-transparent to-[var(--lh-cream)]"></div>
      </div>
      <div class="relative z-10 w-full">
        <div class="animate__animated animate__fadeInDown">
          <span class="text-[var(--lh-gold)] font-bold tracking-[0.4em] text-xs uppercase mb-4 block">Service Experience</span>
          <h1 class="text-5xl md:text-7xl font-serif font-black text-white tracking-[0.2em] mb-8 drop-shadow-2xl">意见反馈</h1>
          <div class="w-20 h-1 bg-[var(--lh-red)] mb-10 shadow-lg"></div>
          <p class="text-sm md:text-lg text-white/90 tracking-[0.15em] leading-relaxed italic font-medium drop-shadow-lg max-w-2xl">
            倾听您的心声，只为更完美的邂逅。欢迎反馈体验问题或建议。
            <span class="block mt-4 font-serif text-[var(--lh-gold)]">— 客户服务建议中心 —</span>
          </p>
        </div>
      </div>
    </section>

    <!-- 分栏布局 (Split Content) -->
    <div class="w-full px-6 md:px-16 pb-20">
      <div class="grid grid-cols-1 lg:grid-cols-12 gap-16 items-start">
        
        <!-- 左侧：宣传与联系方式 (Column Left) -->
        <div class="lg:col-span-4 space-y-12 animate__animated animate__fadeInLeft">
           <div class="p-10 bg-white/40 backdrop-blur-3xl border border-[var(--lh-gold)]/20 rounded-[40px] shadow-xl">
              <h3 class="text-2xl font-serif font-black text-[var(--lh-navy)] mb-6 tracking-widest">联系客服中心</h3>
              <p class="text-sm text-[var(--lh-text-secondary)] opacity-80 leading-relaxed mb-10 italic">
                除了在线提交反馈，您也可以通过以下渠道直接联系我们的专属管家。
              </p>
              
              <div class="space-y-8">
                 <div class="flex items-center gap-6 group">
                    <div class="w-14 h-14 bg-[var(--lh-navy)] rounded-2xl flex items-center justify-center text-[var(--lh-gold)] shadow-xl group-hover:bg-[var(--lh-red)] transition-all">
                       <el-icon size="28"><Phone /></el-icon>
                    </div>
                    <div class="flex flex-col">
                       <span class="text-[9px] font-black text-[var(--lh-text-secondary)] opacity-40 tracking-[0.06em] mb-1">客服电话 Line</span>
                       <span class="text-xl font-mono font-black text-[var(--lh-navy)] tracking-[0.06em]">0379-6688-5522</span>
                    </div>
                 </div>
                 <div class="flex items-center gap-6 group">
                    <div class="w-14 h-14 bg-[var(--lh-red)] rounded-2xl flex items-center justify-center text-white shadow-xl group-hover:bg-[var(--lh-navy)] transition-all">
                       <el-icon size="28"><ChatDotRound /></el-icon>
                    </div>
                    <div class="flex flex-col">
                       <span class="text-[9px] font-black text-[var(--lh-text-secondary)] opacity-40 tracking-[0.06em] mb-1">官方微信 Chat</span>
                       <span class="text-xl font-serif font-black text-[var(--lh-navy)] tracking-[0.06em]">LS_Hanfu_LY</span>
                    </div>
                 </div>
              </div>

              <div class="mt-12 pt-10 border-t border-[var(--lh-navy)]/10 text-[10px] text-[var(--lh-text-secondary)] font-black italic opacity-50 tracking-[0.1em]">
                Typically 1-3 Business Days for Reply
              </div>
           </div>

           <!-- 品牌装饰卡 -->
           <div class="hidden lg:block relative h-64 rounded-[40px] overflow-hidden shadow-2xl group">
             <img src="https://images.unsplash.com/photo-1518709268805-4e9042af9f23?q=80&w=400" class="w-full h-full object-cover transition-transform duration-10s group-hover:scale-125" />
             <div class="absolute inset-0 bg-gradient-to-t from-[var(--lh-navy)] to-transparent opacity-60"></div>
             <div class="absolute bottom-8 left-8 right-8 text-white">
                <span class="text-[var(--lh-gold)] font-serif italic text-lg opacity-90">— 洛裳美学 —</span>
                <p class="text-[10px] tracking-[0.2em] mt-3 uppercase font-black opacity-80">Heritage Experience</p>
             </div>
           </div>
        </div>

        <!-- 右侧：反馈表单 (Column Right) -->
        <div class="lg:col-span-8 animate__animated animate__fadeInRight">
           <div class="w-full card-premium bg-white p-10 lg:p-20 relative overflow-hidden transition-all duration-700 shadow-2xl border-[var(--lh-border)]">
              <!-- 装饰性背景印章 -->
              <div class="absolute -top-16 -right-16 opacity-[0.03] pointer-events-none rotate-12">
                 <el-icon size="320" class="text-[var(--lh-navy)]"><Service /></el-icon>
              </div>

              <el-form :model="form" label-position="top" class="heritage-form relative z-10">
                 <el-form-item label="反馈类别 Category" required>
                    <el-radio-group v-model="form.type" class="!flex flex-wrap gap-4">
                       <el-radio label="complaint" border class="heritage-radio">投诉反馈</el-radio>
                       <el-radio label="suggestion" border class="heritage-radio">改进建议</el-radio>
                       <el-radio label="other" border class="heritage-radio">其他咨询</el-radio>
                    </el-radio-group>
                 </el-form-item>

                 <div class="grid grid-cols-1 md:grid-cols-2 gap-10">
                    <el-form-item label="满意度评分 Rating" required>
                       <div class="flex flex-col gap-2">
                          <el-rate 
                            v-model="form.rating" 
                            :texts="['极差', '失望', '一般', '满意', '非常棒']" 
                            show-text 
                            class="premium-rate mt-2" 
                          />
                          <span class="text-[9px] text-[var(--lh-gold)] font-black tracking-[0.04em] opacity-50 uppercase">Your rating matters.</span>
                       </div>
                    </el-form-item>

                    <el-form-item label="联系方式 Contact">
                       <el-input 
                        v-model="form.phone" 
                        placeholder="留下联系方式，方便后续回访" 
                        maxlength="11" 
                        class="premium-input-field"
                       />
                    </el-form-item>
                 </div>

                 <el-form-item label="详细描述 Details" required>
                    <el-input 
                      v-model="form.content" 
                      type="textarea" 
                      :rows="6" 
                      placeholder="请详细描述您的问题、建议或咨询内容，便于我们为您提供最精准的回复" 
                      maxlength="500"
                      show-word-limit
                      class="premium-textarea"
                    />
                 </el-form-item>

                 <el-form-item label="相关图片 Attachments">
                    <div class="flex flex-col gap-4">
                       <el-upload
                         action="#"
                         list-type="picture-card"
                         :auto-upload="false"
                         :on-change="handleFileChange"
                         class="heritage-upload"
                       >
                         <div class="flex flex-col items-center justify-center gap-2">
                           <el-icon size="24"><Plus /></el-icon>
                           <span class="text-[9px] font-black tracking-[0.06em]">ADD MEDIA</span>
                         </div>
                       </el-upload>
                       <p class="text-[10px] text-[var(--lh-text-secondary)] font-bold opacity-50 tracking-[0.04em]">Supports JPG/PNG, Max 3 images</p>
                    </div>
                 </el-form-item>

                 <div class="mt-16 text-right">
                    <button 
                      class="btn-premium px-24 py-5 text-sm tracking-[0.1em] font-serif shadow-2xl hover:scale-105 active:scale-95 transition-all" 
                      :disabled="submitting"
                      @click.prevent="handleSubmit"
                    >
                      {{ submitting ? 'PROCESSING...' : '提交反馈 SUBMIT' }}
                    </button>
                 </div>
              </el-form>
           </div>
        </div>

      </div>
    </div>

    <!-- 提交成功对话框 -->
    <el-dialog
      v-model="showSuccess"
      width="450px"
      center
      :show-close="false"
      class="heritage-dialog-full overflow-hidden"
    >
      <div class="relative p-12 text-center bg-white">
         <div class="absolute -top-10 -left-10 text-9xl font-serif font-black text-[var(--lh-gold)] opacity-[0.05] select-none pointer-events-none">成</div>
         
         <div class="w-24 h-24 bg-[var(--lh-cream)] rounded-[30px] flex items-center justify-center mx-auto mb-10 text-[var(--lh-red)] border-4 border-[var(--lh-gold)]/20 shadow-2xl animate__animated animate__bounceIn">
            <el-icon size="48"><Check /></el-icon>
         </div>
         
         <h2 class="text-3xl font-serif font-black text-[var(--lh-navy)] mb-6 tracking-[0.08em]">提交成功</h2>
         <p class="text-sm text-[var(--lh-text-secondary)] font-medium tracking-[0.04em] opacity-70 mb-12 leading-relaxed">
           我们已收到你的反馈，预计 1-3 个工作日内处理。
         </p>
         
         <button 
           class="btn-premium w-full py-5 text-sm tracking-[0.08em] font-serif shadow-2xl hover:scale-[1.02] active:scale-95 transition-all" 
           @click="$router.push('/')"
         >
          返回首页
         </button>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { Service, Plus, Phone, ChatDotRound, Check } from '@element-plus/icons-vue'
import HeaderNav from '@/components/common/HeaderNav.vue'
import { ElMessage } from 'element-plus'
import { submitFeedback } from '@/api/notice'

const submitting = ref(false)
const showSuccess = ref(false)

const form = reactive<any>({
  type: 'MESSAGE',
  content: '',
  rating: 5,
  phone: '',
  images: []
})

const handleFileChange = () => {
  // Mock logic
}

const handleSubmit = async () => {
  if (!form.content) {
    ElMessage.warning('请填写反馈内容')
    return
  }

  submitting.value = true
  try {
    const payload = {
      type: form.type,
      content: form.content,
      rating: Number(form.rating || 5),
      phone: form.phone,
      images: form.images || []
    }
    await submitFeedback(payload as any)
    submitting.value = false
    showSuccess.value = true
    Object.assign(form, {
      type: 'MESSAGE',
      content: '',
      rating: 5,
      phone: '',
      images: []
    })
  } catch {
    submitting.value = false
    ElMessage.error('提交失败，请稍后再试')
  }
}
</script>

<style scoped>
.heritage-form :deep(.el-form-item__label) {
   font-family: var(--font-serif);
   font-weight: 900;
   color: var(--lh-navy);
   text-transform: uppercase;
   letter-spacing: 0.2em;
   font-size: 11px;
   margin-bottom: 12px;
}

.heritage-radio {
   border-radius: 16px !important;
   height: 54px !important;
   border: 2px solid var(--lh-border) !important;
   background: var(--lh-cream) !important;
   font-family: var(--font-serif) !important;
   font-weight: 900 !important;
   transition: all 0.4s !important;
}

.heritage-radio.is-checked {
   border-color: var(--lh-red) !important;
   background: white !important;
}

.heritage-radio :deep(.el-radio__input.is-checked + .el-radio__label) {
   color: var(--lh-red) !important;
}

.premium-rate :deep(.el-rate__text) {
   color: var(--lh-gold) !important;
   font-family: var(--font-serif);
   font-weight: 900;
   letter-spacing: 0.1em;
}

.premium-input-field :deep(.el-input__wrapper) {
   border-radius: 16px;
   background: var(--lh-cream);
   border: 2px solid var(--lh-border);
   box-shadow: none;
   height: 54px;
}

.premium-textarea :deep(.el-textarea__inner) {
   border-radius: 20px;
   background: var(--lh-cream);
   border: 2px solid var(--lh-border);
   box-shadow: none;
   padding: 20px;
   font-family: var(--font-serif);
   font-weight: 500;
   letter-spacing: 0.1em;
   line-height: 1.8;
}

.heritage-upload :deep(.el-upload--picture-card) {
   background-color: var(--lh-cream);
   border: 2px dashed var(--lh-gold);
   border-radius: 20px;
   width: 100px;
   height: 100px;
}

:deep(.heritage-dialog-full) {
  background: white !important;
  border-radius: 40px !important;
  box-shadow: 0 50px 100px -20px rgba(0,0,0,0.3) !important;
  padding: 0 !important;
}
:deep(.heritage-dialog-full .el-dialog__header) {
  display: none;
}
:deep(.heritage-dialog-full .el-dialog__body) {
  padding: 0;
}
</style>
