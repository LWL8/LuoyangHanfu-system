<template>
  <div class="hanfu-detail-view min-h-screen bg-[var(--white)] pb-20">
    <HeaderNav />
    
    <!-- 骨架屏加载状态 -->
    <div v-if="store.loading && !store.currentItem" class="container py-12">
       <div class="grid grid-cols-1 lg:grid-cols-2 gap-12">
          <el-skeleton animated>
             <template #template>
                <el-skeleton-item variant="image" class="w-full aspect-[3/4] rounded-3xl" />
                <div class="mt-6 flex gap-4">
                   <el-skeleton-item v-for="i in 4" :key="i" variant="image" class="w-20 h-20 rounded-lg" />
                </div>
             </template>
          </el-skeleton>
          <div class="space-y-10">
             <el-skeleton animated :rows="8" />
             <el-skeleton animated :rows="4" />
          </div>
       </div>
    </div>

    <template v-else-if="store.currentItem">
      <!-- 页面顶部: 导航与背景 -->
      <div class="bg-white border-b border-[var(--border)] py-4 mb-8">
        <div class="container flex items-center justify-between">
          <el-breadcrumb separator="/">
            <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item :to="{ path: '/hanfu' }">汉服品鉴</el-breadcrumb-item>
            <el-breadcrumb-item>{{ store.currentItem.name }}</el-breadcrumb-item>
          </el-breadcrumb>
          <div class="flex items-center gap-4 text-xs text-gray-500">
            <span>浏览量: 1240</span>
            <span>收藏数: {{ store.currentItem.heatCount || 0 }}</span>
          </div>
        </div>
      </div>

      <div class="container">
        <div class="grid grid-cols-1 lg:grid-cols-2 gap-12 mb-16">
          <!-- 左侧: 图片画廊 -->
          <div class="gallery-section sticky top-28 h-fit">
            <div class="main-image relative aspect-[3/4] rounded-3xl overflow-hidden shadow-2xl mb-6 border border-[var(--border)] group bg-white">
              <el-image
                :src="store.currentItem.images?.[activeThumb]"
                :preview-src-list="store.currentItem.images || []"
                :initial-index="activeThumb"
                :preview-teleported="true"
                :hide-on-click-modal="true"
                class="w-full h-full object-cover transition-transform duration-700 group-hover:scale-105 cursor-zoom-in"
                fit="cover"
              >
                <template #placeholder>
                   <div class="w-full h-full bg-[#FAF6EF] animate-pulse flex items-center justify-center">
                      <el-icon class="is-loading" size="24"><Loading /></el-icon>
                   </div>
                </template>
              </el-image>
              <div v-if="store.currentItem.images" class="absolute bottom-6 right-6 bg-black/50 backdrop-blur-md px-4 py-2 rounded-full text-white text-[10px] font-mono tracking-widest uppercase">
                {{ activeThumb + 1 }} / {{ store.currentItem.images.length }}
              </div>
              <!-- 沉浸式标签 -->
              <div class="absolute top-6 left-6 flex items-center gap-2 opacity-0 group-hover:opacity-100 transition-opacity">
                 <div class="bg-white/80 backdrop-blur px-3 py-1 rounded-full text-[10px] font-bold text-gray-800 shadow-sm border border-white">
                    高清赏析
                 </div>
              </div>
            </div>
            
            <!-- 缩略图轮播 (增强样式) -->
            <div class="thumb-swiper relative px-8">
               <div class="absolute inset-y-0 left-0 w-8 flex items-center justify-start pointer-events-none z-10 bg-gradient-to-r from-white translate-x-1"></div>
               <div class="absolute inset-y-0 right-0 w-8 flex items-center justify-end pointer-events-none z-10 bg-gradient-to-l from-white -translate-x-1"></div>
              <swiper
                :slides-per-view="5"
                :space-between="10"
                class="thumb-container"
              >
                <swiper-slide v-for="(img, i) in (store.currentItem.images || [])" :key="i">
                  <div 
                    class="aspect-square rounded-lg overflow-hidden cursor-pointer border-2 transition-all p-1"
                    :class="activeThumb === i ? 'border-[var(--red)] bg-red-50' : 'border-transparent hover:border-gray-200'"
                    @click="activeThumb = i"
                  >
                    <img :src="img" class="w-full h-full object-cover rounded" />
                  </div>
                </swiper-slide>
              </swiper>
            </div>
          </div>

          <!-- 右侧: 购买信息 -->
          <div class="info-section">
            <div class="flex items-center gap-2 mb-4">
              <el-tag type="danger" effect="dark" size="small">{{ store.currentItem.dynasty }}</el-tag>
              <el-tag v-for="tag in store.currentItem.tags" :key="tag" type="info" size="small" plain>{{ tag }}</el-tag>
            </div>
            
            <h1 class="text-3xl font-bold font-[family-name:var(--font-title)] text-[#333] mb-4">
              {{ store.currentItem.name }}
            </h1>
            
            <div class="price-box bg-[#FAF6EF]/50 border-y border-[var(--gold)]/20 py-6 px-4 mb-8">
              <div class="flex items-baseline gap-2">
                <span class="text-sm text-[var(--red)]">租赁价:</span>
                <span class="text-4xl font-bold text-[var(--red)]">¥{{ store.currentItem.pricePerDay }}</span>
                <span class="text-sm text-gray-400"> 元</span>
              </div>
              <div class="mt-2 text-xs text-gray-500">
                <span>押金: ¥{{ store.currentItem.deposit || 200 }} </span>
                <span class="ml-4">(归还后实时退回账户余额或原路径)</span>
              </div>
            </div>

            <!-- 规格选择 -->
            <div class="options space-y-8">
              <!-- 尺码选择 -->
              <div class="option-group">
                <div class="flex items-center justify-between mb-3 text-sm">
                  <span class="font-semibold">规格尺码</span>
                  <el-button link type="primary" size="small">尺码对照表</el-button>
                </div>
                <div class="flex flex-wrap gap-3">
                  <template v-for="s in store.currentItem.sizes" :key="typeof s === 'string' ? s : s.size">
                    <div
                      class="relative border-2 px-6 py-2 rounded-lg cursor-pointer transition-all flex flex-col items-center"
                      :class="[
                        (typeof s === 'object' && s.stock === 0) ? 'bg-gray-50 text-gray-300 border-gray-100 cursor-not-allowed opacity-50' : 
                        selectedSize === (typeof s === 'string' ? s : s.size) ? 'border-[var(--red)] bg-red-50 text-[var(--red)]' : 'border-gray-100 hover:border-gray-200'
                      ]"
                      @click="(typeof s === 'string' || s.stock > 0) && (selectedSize = (typeof s === 'string' ? s : s.size))"
                    >
                      <span class="text-sm font-bold">{{ typeof s === 'string' ? s : s.size }}</span>
                      <span v-if="typeof s === 'object'" class="text-[10px] mt-1">剩余 {{ s.stock }}</span>
                      <el-tooltip v-if="typeof s === 'object' && s.stock === 0" content="该尺码已租完" placement="top">
                        <div class="absolute inset-0"></div>
                      </el-tooltip>
                    </div>
                  </template>
                </div>
              </div>

              <!-- 日历展示 (不可编辑) -->
              <div class="option-group">
                <h4 class="text-sm font-semibold mb-3">可选日期 (参考)</h4>
                <div class="p-2 border border-gray-100 rounded-xl bg-white shadow-sm overflow-hidden">
                   <el-date-picker
                    v-model="previewDate"
                    type="date"
                    placeholder="查看可预约日期"
                    readonly
                    class="!w-full"
                    :disabled-date="disabledDate"
                  />
                  <p class="text-[10px] text-gray-400 mt-2 text-center">* 灰色日期表示已被预约或不可供租赁</p>
                </div>
              </div>

              <!-- 提交按钮 -->
              <div class="actions flex gap-4 pt-6">
                <el-button color="var(--gold)" size="large" plain class="flex-1 !h-14 font-bold" @click="toggleWish">
                   <el-icon class="mr-2"><Star /></el-icon> 加入心愿单
                </el-button>
                <el-button color="var(--red)" size="large" class="flex-[1.5] !h-14 font-bold !text-white text-lg shadow-lg hover:shadow-[var(--red)]/40 transition-shadow" @click="handleBooking">
                  立即预约
                </el-button>
              </div>
            </div>
          </div>
        </div>

        <!-- 详情 Tab 区 -->
        <div class="detail-tabs bg-white rounded-2xl border border-[var(--border)] overflow-hidden mb-16 shadow-sm">
          <el-tabs v-model="activeTab" class="custom-tabs px-8">
            <el-tab-pane label="汉服详情" name="intro">
              <div class="py-8 prose prose-sm max-w-none prose-stone">
                <div class="grid grid-cols-2 gap-8 mb-12 bg-gray-50 p-6 rounded-xl border-l-4 border-[var(--gold)]">
                  <div>
                    <p class="font-bold text-[var(--gold)] mb-2">制式介绍</p>
                    <p class="text-gray-600 leading-relaxed">{{ store.currentItem.style || '暂无描述' }}</p>
                  </div>
                  <div>
                    <p class="font-bold text-[var(--gold)] mb-2">面料说明</p>
                    <p class="text-gray-600 leading-relaxed">甄选柔软透气的仿真丝面料，挺括垂坠，暗纹细腻，适合长久穿着及拍照。</p>
                  </div>
                </div>
                <div class="space-y-4">
                   <img v-for="(img, idx) in (store.currentItem.images || [])" :key="idx" :src="img" class="w-full rounded-lg" />
                </div>
              </div>
            </el-tab-pane>
            
            <el-tab-pane label="尺码指南" name="size">
              <div class="py-12 flex flex-col items-center">
                 <el-table :data="sizeTable" class="max-w-2xl border">
                    <el-table-column prop="size" label="尺码" width="100" align="center" />
                    <el-table-column prop="height" label="推荐身高 (cm)" align="center" />
                    <el-table-column prop="weight" label="推荐体重 (kg)" align="center" />
                 </el-table>
                 <p class="mt-6 text-xs text-gray-400">备注: 汉服版型较为宽松，请按身高优先选择，如有疑问请咨询客服。</p>
              </div>
            </el-tab-pane>

            <el-tab-pane label="搭配建议" name="style">
              <div class="py-8 grid grid-cols-1 md:grid-cols-2 gap-6">
                <div v-for="scenic in suggestedScenics" :key="scenic.name" class="flex gap-4 p-4 border border-[var(--border)]/50 rounded-xl hover:bg-red-50 transition-colors">
                  <img :src="scenic.img" class="w-20 h-20 object-cover rounded-lg" />
                  <div>
                    <h5 class="font-bold text-gray-800">{{ scenic.name }}</h5>
                    <p class="text-xs text-gray-500 mt-1">{{ scenic.reason }}</p>
                    <el-button link type="primary" size="small" class="mt-2" @click="$router.push('/luoyang')">查看线路</el-button>
                  </div>
                </div>
              </div>
            </el-tab-pane>
            
            <el-tab-pane label="用户评价" name="review">
              <div class="py-12 text-center text-gray-400">
                <el-icon size="48" class="mb-4 opacity-10"><ChatDotRound /></el-icon>
                <p>暂无评价，成为第一个穿上它的人吧～</p>
              </div>
            </el-tab-pane>
          </el-tabs>
        </div>

        <!-- 相似推荐 -->
         <div class="similar-section">
            <div class="flex items-center justify-between mb-8">
              <h2 class="text-2xl font-[family-name:var(--font-title)] text-[#333]">同朝代精选</h2>
              <el-button link @click="$router.push('/hanfu')">全部商品 <el-icon class="ml-1"><ArrowRight /></el-icon></el-button>
            </div>
            <div class="grid grid-cols-2 md:grid-cols-4 gap-6">
              <HanfuCard v-for="h in similarList" :key="h.id" :item="h" />
            </div>
         </div>
      </div>
    </template>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { Star, ChatDotRound, ArrowRight } from '@element-plus/icons-vue'
import { Swiper, SwiperSlide } from 'swiper/vue'
import 'swiper/css'
import HeaderNav from '@/components/common/HeaderNav.vue'
import HanfuCard from '@/components/hanfu/HanfuCard.vue'
import { useHanfuStore } from '@/stores/hanfuStore'
import { fetchSimilarHanfu, type HanfuListItem } from '@/api/hanfu'

const route = useRoute()
const router = useRouter()
const store = useHanfuStore()

const activeThumb = ref(0)
const selectedSize = ref('')
const previewDate = ref('')
const activeTab = ref('intro')
const similarList = ref<HanfuListItem[]>([])

const sizeTable = [
  { size: 'XS', height: '150-155', weight: '40-45' },
  { size: 'S', height: '155-160', weight: '45-52' },
  { size: 'M', height: '160-165', weight: '52-60' },
  { size: 'L', height: '165-170', weight: '60-68' },
  { size: 'XL', height: '170-175', weight: '68-75' }
]

const suggestedScenics = [
  { name: '隋唐洛阳城-明堂', reason: '金碧辉煌的宫殿建筑，与精致的汉服极度适配。', img: 'https://images.unsplash.com/photo-1548011283-0a7b4f535804?w=200' },
  { name: '洛邑古城', reason: '市井烟火气浓郁，穿汉服吃水席别有一番古都氛围。', img: 'https://images.unsplash.com/photo-1587391969018-86737f59d571?w=200' }
]

const disabledDate = (time: Date) => {
  // 模拟被租出的日期
  const date = time.getDate()
  return date === 15 || date === 20 || date === 25
}

const handleBooking = () => {
  if (!selectedSize.value) {
    return (window as any).ElMessage?.warning('请先选择所需的尺码')
  }
  router.push({
    path: '/booking',
    query: { hanfuId: store.currentItem?.id, size: selectedSize.value }
  })
}

const toggleWish = () => {
  (window as any).ElMessage?.success('已添加到心愿单')
}

const loadData = async () => {
  const id = route.params.id as string
  await store.getDetail(id)
  if (store.currentItem) {
    const res: any = await fetchSimilarHanfu(store.currentItem.hanfuTypes || '')
    if ((res.code === 0 || res.code === 200) && res.data) {
      similarList.value = res.data.list || res.data
    }
  }
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

onMounted(loadData)
watch(() => route.params.id, loadData)
</script>

<style scoped>
.custom-tabs :deep(.el-tabs__item) {
  height: 64px;
  line-height: 64px;
  font-size: 16px;
  font-weight: 500;
}
.custom-tabs :deep(.el-tabs__item.is-active) {
  color: var(--red);
}
.custom-tabs :deep(.el-tabs__active-bar) {
  background-color: var(--red);
  height: 3px;
}
</style>
