import request from '@/utils/request'

export interface ScenicItem {
  id: number
  name: string
  image: string
  description: string
}

export interface RouteItem {
  id: number
  title: string
  tags: string[]
  duration: string
}

export interface GuideItem {
  id: number
  style: string // 唐制、宋制等
  scenic: string // 适合景点
  reason: string
  photographyTips: string[]
  imageBefore: string
  imageAfter: string
  createTime?: string
  updateTime?: string
}

export function fetchScenics() {
  return request.get('/api/common/scenics') as Promise<ScenicItem[]>
}

export function fetchRoutes() {
  return request.get('/api/common/routes') as Promise<RouteItem[]>
}

export function fetchGuides() {
  return request.get('/api/common/guides') as Promise<any>
}

// 管理员接口
export function adminFetchGuideList(params: any) {
  return request.get('/api/admin/guide/list', { params })
}

export function adminAddGuide(data: any) {
  return request.post('/api/admin/guide/add', data)
}

export function adminUpdateGuide(data: any) {
  return request.put('/api/admin/guide/update', data)
}

export function adminDeleteGuide(id: number) {
  return request.delete(`/api/admin/guide/${id}`)
}
