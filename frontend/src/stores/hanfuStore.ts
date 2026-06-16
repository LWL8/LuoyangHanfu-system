import { defineStore } from 'pinia'
import { ref, reactive } from 'vue'
import { fetchHanfuList, fetchHanfuDetail, type HanfuListItem, type HanfuQuery } from '@/api/hanfu'

export const useHanfuStore = defineStore('hanfu', () => {
  const list = ref<HanfuListItem[]>([])
  const currentItem = ref<HanfuListItem | null>(null)
  const loading = ref(false)
  const total = ref(0)

  // 筛选状态
  const filters = reactive<HanfuQuery>({
    keyword: '',
    dynasty: [],
    hanfuTypes: '',
    minPrice: 0,
    maxPrice: 500,
    colors: [],
    sizes: [],
    services: [],
    sort: '综合',
    page: 1,
    pageSize: 12
  })

  // 获取汉服列表
  const getList = async () => {
    loading.value = true
    try {
      const params: any = { ...filters }
      if (Array.isArray(filters.dynasty) && filters.dynasty.length > 0) {
        params.hanfuTypes = filters.dynasty[0]
      }
      if (filters.sort === '热度') params.sort = 'hot'
      if (filters.sort === '价格升序') params.sort = 'price_asc'
      const res: any = await fetchHanfuList(params)
      if ((res.code === 0 || res.code === 200) && res.data) {
        list.value = res.data.list || []
        total.value = res.data.total || list.value.length
      }
    } finally {
      loading.value = false
    }
  }

  // 获取汉服详情
  const getDetail = async (id: number | string) => {
    loading.value = true
    try {
      const res: any = await fetchHanfuDetail(id)
      if ((res.code === 0 || res.code === 200) && res.data) {
        currentItem.value = res.data
      }
    } finally {
      loading.value = false
    }
  }

  // 重置筛选
  const resetFilters = () => {
    filters.keyword = ''
    filters.dynasty = []
    filters.hanfuTypes = ''
    filters.minPrice = 0
    filters.maxPrice = 500
    filters.colors = []
    filters.sizes = []
    filters.services = []
    filters.sort = '综合'
    filters.page = 1
  }

  return {
    list,
    currentItem,
    loading,
    total,
    filters,
    getList,
    getDetail,
    resetFilters
  }
})

export default useHanfuStore
