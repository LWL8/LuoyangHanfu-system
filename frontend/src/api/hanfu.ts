import request from '@/utils/request'

export interface HanfuItem {
  id: number
  hanfuName: string
  name?: string // Alias
  hanfuTypes: string // Dynasty
  dynasty?: string // Alias
  hanfuPhoto: string
  coverImage?: string // Alias
  images?: string[] // Alias
  hanfuPrice: number // Rent price
  pricePerDay?: number // Alias
  hanfuNewMoney: number // Deposit or current price
  hanfuContent: string // Description
  shangjiaTypes: number // 1: Active, 2: Inactive
  createTime?: string
  heatCount?: number
  deposit?: number
  sizes?: any[] // Support both string[] and {size, stock}[]
  tags?: string[]
  style?: string
}

export type HanfuListItem = HanfuItem

export function normalizeHanfuItem(raw: any): HanfuItem {
  const images = Array.isArray(raw?.images) && raw.images.length
    ? raw.images
    : (Array.isArray(raw?.imageList) && raw.imageList.length ? raw.imageList : (raw?.hanfuPhoto ? [raw.hanfuPhoto] : raw?.coverImage ? [raw.coverImage] : []))
  const hanfuName = String(raw?.hanfuName || raw?.name || '')
  const hanfuTypes = String(raw?.hanfuTypes || raw?.dynasty || '')
  const hanfuPhoto = String(raw?.hanfuPhoto || raw?.coverImage || images[0] || '')
  const hanfuPrice = Number(raw?.hanfuPrice ?? raw?.pricePerDay ?? raw?.rentPrice ?? 0)
  const heatCount = Number(raw?.heatCount ?? raw?.hotScore ?? 0)
  const stock = Number(raw?.stock ?? 99)
  return {
    ...raw,
    hanfuName,
    name: hanfuName,
    hanfuTypes,
    dynasty: hanfuTypes,
    hanfuPhoto,
    coverImage: hanfuPhoto,
    images,
    hanfuPrice,
    pricePerDay: hanfuPrice,
    rentPrice: hanfuPrice,
    heatCount,
    hotScore: heatCount,
    stock,
    hanfuNewMoney: Number(raw?.hanfuNewMoney ?? raw?.deposit ?? 0),
    hanfuContent: String(raw?.hanfuContent || raw?.style || ''),
    shangjiaTypes: Number(raw?.shangjiaTypes ?? 1)
  } as HanfuItem
}

// Removed fallbackHanfuList to ensure data authenticity
export interface HanfuQuery {
  hanfuName?: string
  hanfuTypes?: string
  dynasty?: string[] // Alias for multi-select
  keyword?: string
  minPrice?: number
  maxPrice?: number
  colors?: string[]
  sizes?: string[]
  services?: string[]
  sort?: string
  page?: number
  limit?: number
  pageSize?: number
}

/**
 * 公共接口 - 获取汉服列表
 */
export function fetchHanfuList(params: any = {}) {
  const backendParams = {
    current: params.page || params.current || 1,
    size: params.pageSize || params.limit || params.size || 10,
    dynasty: params.dynasty || params.hanfuTypes,
    keyword: params.keyword || params.hanfuName
  }
  return request.get('/api/user/hanfu/list', { params: backendParams }).then((res: any) => {
    if ((res?.code === 0 || res?.code === 200) && res?.data) {
      const list = Array.isArray(res.data?.list) ? res.data.list : (Array.isArray(res.data?.records) ? res.data.records : (Array.isArray(res.data) ? res.data : []))
      return {
        ...res,
        data: {
          ...(Array.isArray(res.data) ? {} : res.data),
          list: list.map(normalizeHanfuItem),
          total: Number(res.data?.total ?? list.length)
        }
      }
    }
    return res
  })
}

/**
 * 公共接口 - 获取汉服详情
 */
export function fetchHanfuDetail(id: number | string) {
  return request.get(`/api/user/hanfu/${id}`).then((res: any) => {
    if ((res?.code === 0 || res?.code === 200) && res?.data) {
      return { ...res, data: normalizeHanfuItem(res.data) }
    }
    return res
  })
}

/**
 * 获取相似汉服
 */
export function fetchSimilarHanfu(id: number | string) {
  return request.get('/api/user/hanfu/similar', { params: { id } }).then((res: any) => {
    if ((res?.code === 0 || res?.code === 200) && res?.data) {
      const list = Array.isArray(res.data?.list) ? res.data.list : (Array.isArray(res.data) ? res.data : [])
      return {
        ...res,
        data: Array.isArray(res.data)
          ? list.map(normalizeHanfuItem)
          : { ...res.data, list: list.map(normalizeHanfuItem) }
      }
    }
    return res
  })
}

/**
 * 管理端 - 获取汉服分页列表
 */
export function adminFetchHanfuList(params: any = {}) {
  return request.get('/api/admin/hanfu/list', { params })
}

/**
 * 管理端 - 新增汉服
 */
export function adminAddHanfu(data: Partial<HanfuItem>) {
  return request.post('/api/admin/hanfu/add', data)
}

/**
 * 管理端 - 更新汉服
 */
export function adminUpdateHanfu(data: Partial<HanfuItem>) {
  return request.put('/api/admin/hanfu/update', data)
}

/**
 * 管理端 - 删除汉服
 */
export function adminDeleteHanfu(id: number | string) {
  return request.delete(`/api/admin/hanfu/${id}`)
}

/**
 * 管理端 - 上下架开关
 */
export function adminToggleActive(id: number | string) {
  return request.post(`/api/admin/hanfu/toggle/${id}`)
}
