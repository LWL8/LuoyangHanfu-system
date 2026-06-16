<template>
  <div class="hanfu-list-view min-h-screen">
    <HeaderNav />
    
    <!-- 艺术横幅 (Page Banner) -->
    <section class="banner-section relative h-[280px] md:h-[340px] overflow-hidden flex items-center pt-16">
      <div class="absolute inset-0 z-0">
        <img src="/images/banners/list-banner.png" class="w-full h-full object-cover brightness-50" />
        <div class="absolute inset-0 bg-gradient-to-r from-black/60 to-transparent"></div>
      </div>
      <div class="container relative z-10 text-white">
        <div class="gsap-banner-content opacity-0">
          <span class="text-[var(--lh-gold)] font-bold tracking-[0.3em] text-sm uppercase mb-4 block">Luoyang Heritage</span>
          <h1 class="text-4xl md:text-5xl font-bold font-serif mb-6 leading-tight">
            神都霓裳 · <span class="text-[var(--lh-gold)]">汉服</span>选购
          </h1>
          <p class="text-gray-200 max-w-xl text-base opacity-90 leading-relaxed font-light">
            精选历代华服，重现千年气韵。在这里，每一件汉服都承载着神都洛阳的历史温度与审美追求。
          </p>
        </div>
      </div>
    </section>

    <!-- 主体内容区 -->
    <div class="container py-12">
      <div class="flex flex-col gap-10">
        <!-- 右侧内容区 -->
        <main class="flex-1">
          <!-- 筛选工具栏 -->
          <div class="toolbar-premium bg-white p-6 rounded-xl border border-[var(--lh-border)] mb-8 flex flex-wrap items-center justify-between gap-6 gsap-toolbar opacity-0 shadow-sm">
            <!-- 搜索框 -->
            <div class="search-box-premium flex-1 min-w-[300px]">
              <el-autocomplete
                v-model="store.filters.keyword"
                :fetch-suggestions="querySearch"
                placeholder="搜索汉服、朝代或关键词..."
                clearable
                class="w-full"
                @select="handleSelect"
                @clear="handleSearch"
              >
                <template #prefix>
                  <el-icon class="text-[var(--lh-red)]"><Search /></el-icon>
                </template>
              </el-autocomplete>
            </div>

            <!-- 排序与视图切换 -->
            <div class="flex items-center gap-8">
              <div class="sort-options flex items-center gap-2">
                <span class="text-xs text-[var(--lh-text-secondary)] font-bold">排序方式:</span>
                <el-radio-group v-model="store.filters.sort" size="small" @change="handleSearch">
                  <el-radio-button label="综合" />
                  <el-radio-button label="价格升序">价格</el-radio-button>
                  <el-radio-button label="热度" />
                </el-radio-group>
              </div>

              <div class="view-switch-premium flex items-center gap-1.5 border-l pl-8 border-[var(--lh-border)]">
                <div 
                  class="p-2 rounded-lg cursor-pointer transition-all active:scale-95" 
                  :class="viewMode === 'grid' ? 'bg-[var(--lh-red)] text-white shadow-lg' : 'text-gray-400 hover:bg-gray-100'"
                  @click="viewMode = 'grid'"
                >
                  <el-icon size="18"><Grid /></el-icon>
                </div>
                <div 
                  class="p-2 rounded-lg cursor-pointer transition-all active:scale-95" 
                  :class="viewMode === 'list' ? 'bg-[var(--lh-red)] text-white shadow-lg' : 'text-gray-400 hover:bg-gray-100'"
                  @click="viewMode = 'list'"
                >
                  <el-icon size="18"><Operation /></el-icon>
                </div>
              </div>
            </div>
          </div>

          <!-- 汉服列表/骨架屏 -->
          <div class="min-h-[600px] relative">
             <el-skeleton :loading="store.loading" animated :count="6">
                <template #template>
                   <div :class="viewMode === 'grid' ? 'mb-10 p-2' : 'flex gap-6 mb-8'">
                      <el-skeleton-item variant="image" :class="viewMode === 'grid' ? 'w-full aspect-[3/4] rounded-2xl shadow-sm' : 'w-56 h-72 rounded-xl'" />
                      <div class="flex-1 mt-6">
                         <el-skeleton-item variant="h3" style="width: 60%" />
                         <el-skeleton-item variant="text" style="margin-top: 15px; width: 80%" />
                         <el-skeleton-item variant="text" style="width: 40%; margin-top: 15px;" />
                      </div>
                   </div>
                </template>
                
                <template #default>
                  <div 
                    v-if="store.list.length > 0"
                    :class="viewMode === 'grid' ? 'grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-8' : 'flex flex-col gap-6'"
                    class="gsap-list-container"
                  >
                    <HanfuCard 
                      v-for="(h, idx) in store.list" 
                      :key="h.id" 
                      :item="h" 
                      :view-mode="viewMode"
                      class="hanfu-item-card opacity-0"
                    />
                  </div>
                  
                  <!-- 空状态 -->
                  <div v-else class="py-20 text-center bg-white rounded-3xl border border-dashed border-[var(--lh-border)]">
                    <el-empty description="未找到符合条件的汉服" :image-size="200">
                      <el-button class="btn-premium" @click="store.resetFilters(); handleSearch()">显示全部款式</el-button>
                    </el-empty>
                  </div>
                </template>
             </el-skeleton>
          </div>

          <!-- 分页 -->
          <div class="mt-16 pb-12 flex justify-center gsap-pagination opacity-0">
            <el-pagination
              v-model:current-page="store.filters.page"
              :page-size="store.filters.pageSize"
              layout="prev, pager, next"
              :total="store.total"
              background
              @current-change="handleSearch"
            />
          </div>
        </main>
      </div>
    </div>
    <FooterBar />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch, nextTick } from 'vue'
import { Search, Grid, Operation } from '@element-plus/icons-vue'
import gsap from 'gsap'
import HeaderNav from '@/components/common/HeaderNav.vue'
import HanfuFilter from '@/components/hanfu/HanfuFilter.vue'
import HanfuCard from '@/components/hanfu/HanfuCard.vue'
import { useHanfuStore } from '@/stores/hanfuStore'
import { useRoute } from 'vue-router'
import FooterBar from '@/components/common/FooterBar.vue'

const store = useHanfuStore()
const route = useRoute()
const viewMode = ref('grid') // 'grid' | 'list'

// 联想词逻辑
const suggestions = [
  { value: '牡丹霓裳-唐制' },
  { value: '上阳花月-唐制' },
  { value: '汴京清影-宋制' },
  { value: '金阙华章-明制' },
  { value: '魏晋风骨-飘逸款' },
  { value: '洛阳特色-联名推荐' }
]

const querySearch = (queryString: string, cb: any) => {
  const results = queryString
    ? suggestions.filter(s => s.value.toLowerCase().includes(queryString.toLowerCase()))
    : suggestions
  cb(results)
}

const initAnimations = () => {
  const tl = gsap.timeline({ defaults: { ease: 'power3.out', duration: 0.8 } })
  
  tl.to('.gsap-banner-content', { opacity: 1, y: 0, startAt: { y: 30 } }, 0.2)
    .to('.gsap-toolbar', { opacity: 1, y: 0, startAt: { y: 20 } }, 0.4)
    .to('.gsap-pagination', { opacity: 1, y: 0, startAt: { y: 20 } }, 0.5)
}

const animateItems = () => {
  nextTick(() => {
    gsap.to('.hanfu-item-card', {
      opacity: 1,
      y: 0,
      stagger: 0.1,
      duration: 0.6,
      ease: 'back.out(1.2)',
      startAt: { y: 40, opacity: 0 }
    })
  })
}

const handleSelect = () => {
  handleSearch()
}

const handleFilterApply = () => {
  window.scrollTo({ top: 380, behavior: 'smooth' })
}

const handleSearch = async () => {
  await store.getList()
  animateItems()
}

const applyKeywordFromRoute = () => {
  const keyword = String(route.query.keyword || '').trim()
  if (store.filters.keyword !== keyword) {
    store.filters.keyword = keyword
  }
}

onMounted(async () => {
  applyKeywordFromRoute()
  initAnimations()
  await store.getList()
  animateItems()
})

watch(
  () => route.query.keyword,
  async () => {
    applyKeywordFromRoute()
    await store.getList()
    animateItems()
  }
)

// 防抖搜索
let timer_search: any = null
watch(
  () => store.filters.keyword,
  () => {
    if (timer_search) clearTimeout(timer_search)
    timer_search = setTimeout(() => {
      handleSearch()
    }, 500)
  }
)
</script>

<style lang="scss" scoped>
.banner-section {
  background-color: var(--lh-navy);
}

.toolbar-premium {
  :deep(.el-radio-button__inner) {
    border-radius: 6px !important;
    margin: 0 4px;
    border: 1px solid var(--lh-border) !important;
    background: transparent;
    color: var(--lh-text-secondary);
    font-size: 13px;
    padding: 8px 16px;
    transition: all 0.3s;
    
    &:hover {
      color: var(--lh-red);
      border-color: var(--lh-red) !important;
    }
  }
  
  :deep(.el-radio-button__original-radio:checked + .el-radio-button__inner) {
    background-color: var(--lh-red) !important;
    border-color: var(--lh-red) !important;
    color: white !important;
    box-shadow: 0 4px 12px rgba(153, 27, 27, 0.2);
  }

  :deep(.el-input__wrapper) {
    box-shadow: none !important;
    border: 1px solid var(--lh-border);
    border-radius: 8px;
    padding: 0 15px;
    height: 44px;
    
    &.is-focus {
      border-color: var(--lh-red);
    }
  }
}

.search-box-premium :deep(.el-autocomplete) {
  width: 100%;
}

:deep(.el-pagination.is-background .el-pager li:not(.is-disabled).is-active) {
  background-color: var(--lh-red) !important;
}

:deep(.el-pagination.is-background .el-pager li:hover) {
  color: var(--lh-red) !important;
}
</style>
