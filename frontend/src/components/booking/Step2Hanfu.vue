<template>
  <div class="step2-hanfu flex flex-col lg:flex-row gap-8 animate__animated animate__fadeIn">
    <!-- 左侧: 在线选衣 -->
    <div class="flex-1 min-w-0">
      <div class="header-tools mb-6 flex flex-wrap items-center justify-between gap-4 bg-white p-4 rounded-xl border">
        <div class="flex gap-2">
          <el-tag
            v-for="d in dynasties"
            :key="d"
            size="small"
            class="cursor-pointer"
            :type="activeDynasty === d ? 'danger' : 'info'"
            :effect="activeDynasty === d ? 'dark' : 'plain'"
            @click="activeDynasty = d"
          >
            {{ d }}
          </el-tag>
        </div>
        <el-input
          v-model="searchQuery" 
          placeholder="搜索款式名称"
          class="max-w-[240px]" 
          :prefix-icon="Search"
          clearable
        />
      </div>

      <div class="grid grid-cols-2 md:grid-cols-2 xl:grid-cols-3 gap-6">
        <div 
          v-for="h in filteredList" 
          :key="h.id"
          class="item-card bg-white rounded-xl border-2 transition-all relative group"
          :class="isItemSelected(h.id) ? 'border-[var(--lh-red)] shadow-lg' : 'border-transparent hover:border-gray-200'"
        >
          <div class="aspect-[3/4] relative overflow-hidden rounded-t-xl">
             <img :src="h.hanfuPhoto || h.coverImage || (h.images && h.images[0])" class="w-full h-full object-cover group-hover:scale-105 transition-transform duration-500" />
             <div v-if="isItemSelected(h.id)" class="absolute top-2 right-2 z-10 w-6 h-6 bg-[var(--lh-red)] text-white rounded-full flex items-center justify-center shadow">
                <el-icon><Check /></el-icon>
             </div>
             <div class="absolute inset-0 bg-black/0 group-hover:bg-black/20 transition-all"></div>
          </div>

          <div class="p-3">
             <h4 class="text-sm font-bold text-gray-800 mb-1 line-clamp-1">{{ h.hanfuName || h.name }}</h4>
             <div class="flex items-center justify-between">
                <span class="text-[var(--lh-red)] font-bold text-xs">¥{{ h.hanfuPrice || h.pricePerDay || 0 }} 元</span>
                <el-button v-if="!isItemSelected(h.id)" link type="primary" size="small" @click="selectItem(h)">
                  <el-icon class="mr-1"><Pointer /></el-icon> 挑选
                </el-button>
                <el-button v-else link type="danger" size="small" @click="removeItem(h.id)">
                  取消
                </el-button>
             </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 右侧/底部: 已选清单 -->
    <div class="w-full lg:w-[350px] shrink-0">
      <div class="selection-panel bg-white rounded-2xl border border-[var(--border)] shadow-xl overflow-hidden sticky top-24">
        <div class="bg-[var(--red)] p-5 text-white">
          <h3 class="text-lg font-bold font-[family-name:var(--font-title)] flex items-center gap-2">
            <el-icon><ShoppingBag /></el-icon> 已选汉服 ({{ store.selectedHanfuList.length }})
          </h3>
        </div>

        <div class="p-5 max-h-[500px] overflow-y-auto">
          <template v-if="store.selectedHanfuList.length > 0">
            <div v-for="item in store.selectedHanfuList" :key="item.id" class="flex gap-3 mb-6 pb-6 border-b last:border-0 border-dashed border-gray-100 animate__animated animate__fadeIn">
              <img :src="item.image" class="w-16 h-20 object-cover rounded shadow-sm" />
              <div class="flex-1 flex flex-col justify-between">
                <div>
                  <div class="flex justify-between items-start">
                    <span class="text-sm font-bold text-gray-800 line-clamp-1">{{ item.name }}</span>
                    <el-icon class="text-gray-300 hover:text-red-500 cursor-pointer" @click="store.removeHanfu(item.id, item.size)"><Delete /></el-icon>
                  </div>
                  <div class="flex gap-4 mt-2">
                    <el-select v-model="item.size" size="small" class="!w-20" placeholder="尺码">
                      <el-option label="XS" value="XS" />
                      <el-option label="S" value="S" />
                      <el-option label="M" value="M" />
                      <el-option label="L" value="L" />
                      <el-option label="XL" value="XL" />
                    </el-select>
                    <el-input-number v-model="item.quantity" :min="1" :max="5" size="small" class="!w-24" />
                  </div>
                </div>
                <div class="text-right text-[var(--red)] font-bold text-sm mt-2">
                  ¥{{ (item.price * item.quantity).toFixed(2) }}
                </div>
              </div>
            </div>
          </template>
          
          <div v-else class="h-40 flex flex-col items-center justify-center text-gray-300">
            <el-icon size="48" class="mb-2"><ShoppingBag /></el-icon>
            <p>请先在左侧选择汉服</p>
          </div>
        </div>

        <div class="p-5 bg-gray-50 border-t">
          <div class="flex justify-between items-center mb-6">
            <span class="text-gray-500 text-sm">租赁费用合计</span>
            <span class="text-xl font-bold text-[var(--lh-red)] font-[family-name:var(--font-title)]">¥{{ store.hanfuTotal.toFixed(2) }}</span>
          </div>

          <div class="grid grid-cols-2 gap-4">
             <el-button plain @click="$emit('prev')" class="h-11">上一步</el-button>
             <el-button 
               type="primary" 
               class="btn-primary flex-1 h-11" 
               :disabled="store.selectedHanfuList.length === 0"
               @click="$emit('next')"
              >
               下一步
             </el-button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { Search, ShoppingBag, Delete, Check, Pointer } from '@element-plus/icons-vue'
import { useBookingStore } from '@/stores/booking'
import { fetchHanfuList } from '@/api/hanfu'

const route = useRoute()
const store = useBookingStore()
defineEmits(['next', 'prev'])

const dynasties = ['全部', '唐制', '宋制', '魏晋', '明制']
const activeDynasty = ref('全部')
const searchQuery = ref('')
const hanfuList = ref<any[]>([])

const filteredList = computed(() => {
  return hanfuList.value.filter((h: any) => {
    const matchDynasty = activeDynasty.value === '全部' || h.hanfuTypes === activeDynasty.value
    const matchSearch = !searchQuery.value || h.hanfuName.includes(searchQuery.value)
    return matchDynasty && matchSearch
  })
})

const isItemSelected = (id: number) => store.selectedHanfuList.some((x: any) => x.id === id)

const selectItem = (h: any) => {
  store.addHanfu({
    id: h.id,
    name: h.hanfuName,
    image: h.hanfuPhoto,
    size: 'M',
    quantity: 1,
    price: h.hanfuPrice || 0
  })
}

const removeItem = (id: number) => {
  const target = store.selectedHanfuList.find((x: any) => x.id === id)
  if (target) {
    store.removeHanfu(id, target.size)
  }
}

onMounted(async () => {
  try {
    const res: any = await fetchHanfuList()
    if ((res.code === 0 || res.code === 200) && res.data) {
      const list = res.data.list || res.data
      hanfuList.value = list
      
      const qId = route.query.hanfuId
      if (qId && !isItemSelected(Number(qId))) {
        const target = list.find((x: any) => x.id === Number(qId))
        if (target) {
          selectItem(target)
        }
      }
    }
  } catch (e) {
    console.error(e)
  }
})
</script>

<style scoped>
.item-card:hover {
  transform: translateY(-5px);
}
.selection-panel {
  max-height: calc(100vh - 120px);
}
</style>
