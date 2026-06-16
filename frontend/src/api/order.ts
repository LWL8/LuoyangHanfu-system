import request from '@/utils/request'

export type OrderStatus = 'pending' | 'confirmed' | 'used' | 'cancelled'

export interface Order {
  id: number
  orderNo: string
  hanfuId?: number
  hanfuName?: string
  hanfuPhoto?: string
  reservationDate?: string
  bookingDate?: string
  timeSlot: string
  totalPrice?: number
  totalAmount: number
  status: OrderStatus
  customerName?: string
  customerPhone?: string
  items: Array<{
    id: number
    name: string
    image: string
    size: string
    quantity: number
    price: number
  }>
  services?: any[]
  createTime: string
}

/**
 * 提交预约订单
 */
export function createReservation(data: {
  hanfuId: number | string
  reservationDate: string
  timeSlot: string
  quantity: number
  serviceIds?: (number | string)[]
  customerName: string
  customerPhone: string
  remark?: string
  payType?: string
}) {
  return request.post('/api/user/reservation/submit', data).catch(() => {
    return request.post('/api/user/reservation/create', data)
  })
}

/**
 * 支付订单
 */
export function payOrder(id: number | string, payType: string) {
  return request.post(`/api/user/reservation/${id}/pay`, { payType })
}

/**
 * 获取我的预约列表 (分页)
 */
export function fetchMyReservations(params: any = {}) {
  return request.get('/api/user/reservation/my', { params }).catch(() => {
    return request.get('/api/user/reservation/page', { params })
  })
}

export const fetchOrders = fetchMyReservations

/**
 * 获取预约详情
 */
export function fetchReservationDetail(id: number | string) {
  return request.get(`/api/user/reservation/${id}`)
}

export const fetchOrderDetail = fetchReservationDetail

/**
 * 取消预约
 */
export function cancelReservation(id: number | string, reason?: string) {
  return request.post(`/api/user/reservation/cancel/${id}`, { reason }).catch(() => {
    return request.post(`/api/user/reservation/${id}/cancel`, { reason })
  })
}

export const cancelOrder = cancelReservation

/**
 * 查询余位
 */
export function fetchSlotRemaining(hanfuId: number | string, date: string) {
  return request.get('/api/user/reservation/slot-remaining', {
    params: { hanfuId, reservationDate: date }
  })
}

/**
 * 管理端 - 获取预约列表分页
 */
export function adminFetchReservations(params: any = {}) {
  return request.get('/api/admin/order/list', { params })
}

/**
 * 管理端 - 获取预约详情
 */
export function adminFetchReservationDetail(id: number | string) {
  return request.get(`/api/admin/order/${id}`)
}

/**
 * 管理端 - 更新预约状态
 */
export function adminUpdateReservationStatus(id: number | string, status: number) {
  return request.put(`/api/admin/order/${id}/status`, null, { 
    params: { status } 
  })
}

/**
 * 管理端 - 删除预约记录
 */
export function adminDeleteReservation(id: number | string) {
  return request.delete(`/api/admin/order/${id}`)
}
