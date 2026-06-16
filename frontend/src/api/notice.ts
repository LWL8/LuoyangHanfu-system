import request from '@/utils/request'

export type NoticeType = 'rule' | 'science' | 'activity'

export interface NoticeItem {
  id: number
  title: string
  type: NoticeType
  content: string
  publishTime: string
  viewCount: number
  cover?: string
}

export function fetchNotices(params?: { type?: NoticeType }) {
  return request.get('/api/common/notices', { params }) as Promise<NoticeItem[]>
}

export function fetchNoticeDetail(id: number | string) {
  return request.get(`/api/common/notices/${id}`) as Promise<NoticeItem>
}

export interface FeedbackForm {
  type: 'MESSAGE' | 'QA'
  content: string
  rating: number
  phone?: string
  images?: string | string[]
}

export function submitFeedback(data: any) {
  // Ensure images is stringified if array
  const payload = { ...data }
  if (Array.isArray(payload.images)) {
    payload.images = JSON.stringify(payload.images)
  }
  return request.post('/api/common/feedback/submit', payload)
}

// 管理端接口
export function adminFetchNoticeList(params: any) {
  return request.get('/api/admin/notice/list', { params })
}

export function adminAddNotice(data: any) {
  return request.post('/api/admin/notice/add', data)
}

export function adminUpdateNotice(data: any) {
  return request.put('/api/admin/notice/update', data)
}

export function adminDeleteNotice(id: number) {
  return request.delete(`/api/admin/notice/${id}`)
}

export function adminFetchFeedbackList(params: any) {
  return request.get('/api/admin/feedback/list', { params })
}

export function adminProcessFeedback(id: number) {
  return request.put(`/api/admin/feedback/process/${id}`)
}
