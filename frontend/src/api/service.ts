import request from '@/utils/request'

export type ServiceCategory = 'makeup' | 'photo' | 'prop' | '妆造' | '发型' | '摄影' | '道具'

export interface ServiceItem {
  id: number
  name: string
  subtitle?: string
  category: ServiceCategory
  price: number
  description: string
  image: string
  enabled: boolean
  updatedAt: string
}

const nowText = () => new Date().toLocaleString('zh-CN', { hour12: false })

const normalize = (list: any[]): ServiceItem[] =>
  list.map((item, idx) => ({
    id: Number(item?.id ?? idx + 1),
    name: String(item?.name || ''),
    subtitle: String(item?.subtitle || ''),
    category: (item?.category || item?.type || 'makeup') as ServiceCategory,
    price: Number(item?.price || 0),
    description: String(item?.description || ''),
    image: String(item?.image || ''),
    enabled: item?.enabled === undefined ? Number(item?.status ?? 1) === 1 : Boolean(item.enabled),
    updatedAt: String(item?.updatedAt || item?.updateTime || nowText())
  }))

export async function adminFetchServiceList() {
  try {
    const res: any = await request.get('/api/admin/service/list')
    if (res && (res.code === 0 || res.code === 200)) {
      const list = normalize(Array.isArray(res.data?.list) ? res.data.list : (Array.isArray(res.data) ? res.data : []))
      return { code: 200, data: list }
    }
  } catch (e) {
    console.warn('Failed to fetch services:', e)
  }
  return { code: 200, data: [] }
}

export async function adminCreateService(payload: Partial<ServiceItem>) {
  return await request.post('/api/admin/service/add', payload)
}

export async function fetchServiceList() {
  try {
    const res: any = await request.get('/api/common/service/list')
    if (res && (res.code === 0 || res.code === 200)) {
       return normalize(Array.isArray(res.data) ? res.data : [])
    }
  } catch (e) {
    console.warn('Failed to fetch public services:', e)
  }
  return []
}

export async function adminUpdateService(payload: Partial<ServiceItem>) {
  return await request.put('/api/admin/service/update', payload)
}

export async function adminDeleteService(id: number) {
  return await request.delete(`/api/admin/service/delete/${id}`)
}
