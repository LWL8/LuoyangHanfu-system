import request from '@/utils/request'

export interface RecommendConfigItem {
  hanfuId: number
  isNew: boolean
  recommended: boolean
}

export async function adminFetchRecommendConfig() {
  try {
    const res: any = await request.get('/api/admin/recommend/list')
    if (res && (res.code === 0 || res.code === 200)) {
      const source = Array.isArray(res.data?.list) ? res.data.list : (Array.isArray(res.data) ? res.data : [])
      const normalized = source.map((i: any) => ({
        hanfuId: Number(i?.hanfuId ?? i?.id ?? 0),
        isNew: Boolean(i?.isNew ?? i?.newFlag ?? false),
        recommended: Boolean(i?.recommended ?? i?.recommendFlag ?? false)
      })).filter((i: RecommendConfigItem) => i.hanfuId > 0)
      return { code: 200, data: normalized }
    }
  } catch (e) {
    console.warn('Failed to fetch recommend config:', e)
  }
  return { code: 200, data: [] }
}

export async function adminSaveRecommendConfig(payload: RecommendConfigItem[]) {
  return await request.put('/api/admin/recommend/update', payload)
}

/** 用户侧 - 获取热门推荐 */
export function fetchHotRecommends() {
  return request.get('/api/user/recommend/hot')
}
