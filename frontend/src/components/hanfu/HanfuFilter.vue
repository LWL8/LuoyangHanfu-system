<template>
  <div class="hanfu-filter card-premium p-7 sticky top-24">
    <div class="flex items-center justify-between mb-6">
      <div class="flex items-center gap-0">
        <div class="w-2 h-8 bg-[#991B1B] mr-3"></div>
        <h3 class="text-3xl font-black font-serif text-[#1F2937] tracking-tighter">筛选条件</h3>
      </div>
      <el-button link class="text-xs text-gray-500 hover:text-[var(--lh-red)] font-medium" @click="handleReset">
        清除全部
      </el-button>
    </div>
    <div class="w-full h-px bg-gray-100 mb-8"></div>

    <!-- 朝代分类 -->
    <div class="filter-group mb-8">
      <div class="flex items-center gap-2 mb-4">
        <el-icon color="var(--lh-gold)"><Collection /></el-icon>
        <h4 class="text-[15px] font-bold text-[var(--lh-navy)]">朝代分类</h4>
      </div>
      <el-checkbox-group v-model="store.filters.dynasty" class="flex flex-col gap-2.5">
        <el-checkbox v-for="d in dynasties" :key="d" :label="d" :value="d">
          <span class="text-sm">{{ d }}</span>
        </el-checkbox>
      </el-checkbox-group>
    </div>

    <!-- 价格区间 -->
    <div class="filter-group mb-8">
      <div class="flex items-center gap-2 mb-4">
        <el-icon color="var(--lh-gold)"><Money /></el-icon>
        <h4 class="text-[15px] font-bold text-[var(--lh-navy)]">价格区间</h4>
      </div>
      <div class="px-2">
        <el-slider
          v-model="priceRange"
          range
          :max="500"
          :step="10"
          @change="handlePriceChange"
        />
        <div class="flex justify-between mt-3 text-[11px] font-medium text-[var(--lh-text-secondary)] uppercase tracking-wider">
          <span>¥0</span>
          <span>¥500</span>
        </div>
      </div>
    </div>

    <!-- 颜色筛选 -->
    <div class="filter-group mb-8">
      <div class="flex items-center gap-2 mb-4">
        <el-icon color="#A16207" size="20"><Sunny /></el-icon>
        <h4 class="text-[15px] font-bold text-[var(--lh-navy)]">色系倾向</h4>
      </div>
      <div class="flex flex-wrap gap-2.5">
        <div
          v-for="c in colorOptions"
          :key="c.name"
          class="w-8 h-8 rounded-full cursor-pointer border-2 transition-all flex items-center justify-center hover:scale-110 shadow-sm"
          :style="{ 
            backgroundColor: c.hex, 
            borderColor: isColorSelected(c.name) ? 'var(--lh-gold)' : 'rgba(0,0,0,0.05)',
            outline: isColorSelected(c.name) ? '1px solid var(--lh-gold)' : 'none',
            outlineOffset: '2px'
          }"
          :title="c.name"
          @click="toggleColor(c.name)"
        >
          <el-icon v-if="isColorSelected(c.name)" size="14" :color="c.name === '白' ? '#333' : '#fff'"><Check /></el-icon>
        </div>
      </div>
    </div>

    <!-- 尺码筛选 -->
    <div class="filter-group mb-8">
      <div class="flex items-center gap-2 mb-4">
        <el-icon color="var(--lh-gold)"><ScaleToOriginal /></el-icon>
        <h4 class="text-[15px] font-bold text-[var(--lh-navy)]">尺码规格</h4>
      </div>
      <div class="flex flex-wrap gap-2">
        <el-tag
          v-for="s in sizeOptions"
          :key="s"
          class="cursor-pointer transition-all px-4 h-8 flex items-center justify-center font-bold"
          :class="isSizeSelected(s) ? 'size-tag-active' : 'size-tag-inactive'"
          @click="toggleSize(s)"
        >
          {{ s }}
        </el-tag>
      </div>
    </div>

    <button class="btn-premium w-full mt-6 h-14 text-2xl font-bold tracking-[0.2em] bg-[#991B1B] hover:bg-[#7F1D1D] rounded-xl shadow-lg border-none text-white transition-all transform active:scale-95" @click="handleApply">
      立即筛选
    </button>
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { Check, Collection, Money, Sunny, ScaleToOriginal } from '@element-plus/icons-vue'
import { useHanfuStore } from '@/stores/hanfuStore'

const store = useHanfuStore()
const emits = defineEmits(['apply'])

const dynasties = ['唐制', '宋制', '魏晋', '明制', '洛阳特色', '汉制']
const priceRange = ref<[number, number]>([store.filters.minPrice || 0, store.filters.maxPrice || 500])

const colorOptions = [
  { name: '红', hex: '#991B1B' },
  { name: '蓝', hex: '#1E3A8A' },
  { name: '绿', hex: '#064E3B' },
  { name: '粉', hex: '#F472B6' },
  { name: '白', hex: '#FFFFFF' },
  { name: '黑', hex: '#0F172A' },
  { name: '金', hex: '#B45309' },
  { name: '紫', hex: '#581C87' }
]

const sizeOptions = ['XS', 'S', 'M', 'L', 'XL', 'XXL']
const serviceOptions = ['包含妆造', '包含摄影', '包含道具']

const isColorSelected = (name: string) => store.filters.colors?.includes(name)
const isSizeSelected = (name: string) => store.filters.sizes?.includes(name)

const toggleColor = (name: string) => {
  if (!store.filters.colors) store.filters.colors = []
  const index = store.filters.colors.indexOf(name)
  if (index > -1) {
    store.filters.colors.splice(index, 1)
  } else {
    store.filters.colors.push(name)
  }
}

const toggleSize = (name: string) => {
  if (!store.filters.sizes) store.filters.sizes = []
  const index = store.filters.sizes.indexOf(name)
  if (index > -1) {
    store.filters.sizes.splice(index, 1)
  } else {
    store.filters.sizes.push(name)
  }
}

const handlePriceChange = (val: any) => {
  if (Array.isArray(val)) {
    store.filters.minPrice = val[0]
    store.filters.maxPrice = val[1]
  }
}

const handleApply = () => {
  store.getList()
  emits('apply')
}

const handleReset = () => {
  store.resetFilters()
  priceRange.value = [0, 500]
  store.getList()
}

// 监听 store 重置
watch(() => store.filters.minPrice, (newVal) => {
  priceRange.value = [newVal || 0, store.filters.maxPrice || 500]
})
</script>

<style lang="scss" scoped>
.hanfu-filter {
  background: white;
  border-radius: 1.5rem;
  overflow: hidden;
  box-shadow: 0 10px 40px -10px rgba(0,0,0,0.08);
}

.size-tag-inactive {
  background: white;
  color: #4B5563;
  border: 1px solid #E5E7EB;
  border-radius: 6px;
  height: 38px;
  min-width: 58px;
  &:hover {
    border-color: #B45309;
    color: #B45309;
  }
}

.size-tag-active {
  background: white;
  color: #991B1B;
  border: 1.5px solid #991B1B !important;
  border-radius: 6px;
  font-weight: 800;
  height: 38px;
  min-width: 58px;
}

.filter-group :deep(.el-checkbox__label) {
  font-weight: 600;
  color: #374151;
  font-size: 1.125rem;
  padding-left: 12px;
}

.filter-group :deep(.el-checkbox__inner) {
  width: 20px;
  height: 20px;
  border-radius: 4px;
}

.filter-group :deep(.el-checkbox__inner::after) {
  height: 10px;
  width: 5px;
  left: 6px;
}

.filter-group :deep(.el-checkbox__input.is-checked + .el-checkbox__label) {
  color: #1F2937;
}

.filter-group :deep(.el-checkbox__input.is-checked .el-checkbox__inner) {
  background-color: #991B1B;
  border-color: #991B1B;
}

.filter-group :deep(.el-slider__runway) {
  height: 6px;
  background-color: #F3F4F6;
  border-radius: 4px;
}

.filter-group :deep(.el-slider__bar) {
  background-color: #A16207;
  height: 6px;
  border-radius: 4px;
}

.filter-group :deep(.el-slider__button) {
  width: 20px;
  height: 20px;
  border: 3px solid #A16207;
  background-color: white;
  box-shadow: 0 4px 10px rgba(0,0,0,0.15);
}

.filter-group h4 {
  letter-spacing: 0.05em;
  font-size: 1.35rem;
  color: #111827;
}
</style>
