import request from '@/utils/request'

export interface BannerItem {
  id: number
  title: string
  image: string
  link: string
  sort: number
  enabled: boolean
}

const safeList = (raw: any): BannerItem[] => {
  if (!Array.isArray(raw)) return []
  return raw.map((item: any, idx: number) => ({
    id: Number(item?.id ?? idx + 1),
    title: String(item?.title || ''),
    image: String(item?.image || item?.imageUrl || item?.bannerUrl || ''),
    link: String(item?.link || item?.jumpUrl || '/booking'),
    sort: Number(item?.sort ?? item?.sortNo ?? idx + 1),
    enabled: item?.enabled === undefined ? Number(item?.status ?? 1) === 1 : Boolean(item.enabled)
  }))
}

// 管理端：轮播图列表
export async function adminFetchBannerList() {
  try {
    const res: any = await request.get('/api/admin/banner/list')
    if (res && (res.code === 0 || res.code === 200)) {
      const list = safeList(res.data?.list || res.data)
      return { code: 200, data: list }
    }
  } catch (e) {
    console.warn('Failed to fetch admin banners:', e)
  }
  return { code: 200, data: [] }
}

// 管理端：新增
export async function adminCreateBanner(payload: Partial<BannerItem>) {
  const data: any = { ...payload }
  if (data.enabled !== undefined) data.enabled = data.enabled ? 1 : 0
  return await request.post('/api/admin/banner/add', data)
}

// 管理端：更新
export async function adminUpdateBanner(payload: Partial<BannerItem>) {
  const data: any = { ...payload }
  if (data.enabled !== undefined) data.enabled = data.enabled ? 1 : 0
  return await request.put('/api/admin/banner/update', data)
}

// 管理端：删除
export async function adminDeleteBanner(id: number) {
  return await request.delete(`/api/admin/banner/delete/${id}`)
}

// 前台：首页读取启用轮播
export async function fetchHomeBannerList() {
  try {
    const res: any = await request.get('/api/user/banner/list')
    if (res && (res.code === 0 || res.code === 200)) {
      const list = safeList(res.data?.list || res.data).filter((i) => i.enabled)
      return { code: 200, data: list.sort((a, b) => a.sort - b.sort) }
    }
  } catch (e) {
    console.warn('Failed to fetch banners:', e)
  }
  return { code: 200, data: [] }
}
