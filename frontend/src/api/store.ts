import request from '@/utils/request'

export interface StoreItem {
  id: number
  name: string
  address: string
  phone: string
  businessHours: string
  images: string[]
  facilities: string[]
  distance?: string
}

export function fetchStores() {
  return request.get('/stores') as Promise<StoreItem[]>
}

export function fetchStoreDetail(id: number | string) {
  return request.get(`/stores/${id}`) as Promise<StoreItem>
}
