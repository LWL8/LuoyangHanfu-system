import request from '@/utils/request'

export interface HotHanfuVO {
  name: string
  sales: number
}

export interface StatsVO {
  totalRevenue: number
  totalOrders: number
  activeUsers: number
  orderTrend: Record<string, number>
  hotHanfu: HotHanfuVO[]
}

/**
 * 获取仪表盘综合统计数据
 */
export function getDashboardStats() {
  return request.get('/api/admin/stats/dashboard')
}

/**
 * 导出统计报表
 */
export function exportStatsReport() {
  window.open('/api/admin/stats/export', '_blank')
}
