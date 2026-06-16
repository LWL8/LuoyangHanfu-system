import { defineStore } from 'pinia'
import { ref } from 'vue'
import { fetchOrders, fetchOrderDetail, cancelOrder, payOrder, type Order, type OrderStatus } from '@/api/order'

export const useOrderStore = defineStore('order', () => {
  const orders = ref<Order[]>([])
  const currentOrder = ref<Order | null>(null)
  const loading = ref(false)

  const getOrders = async (status: OrderStatus | 'all' = 'all') => {
    loading.value = true
    try {
      const res: any = await fetchOrders(status === 'all' ? {} : { status })
      if (res.code === 0 && res.data) {
        orders.value = res.data.list || res.data
      }
    } finally {
      loading.value = false
    }
  }

  const getOrderDetail = async (id: string | number) => {
    loading.value = true
    try {
      const res: any = await fetchOrderDetail(id)
      if (res.code === 0 && res.data) {
        currentOrder.value = res.data
      }
    } finally {
      loading.value = false
    }
  }

  const cancel = async (id: string | number, reason: string) => {
    const res: any = await cancelOrder(id, reason)
    const updated = res.data || res
    const idx = orders.value.findIndex(x => x.id === Number(id))
    if (idx > -1) {
      orders.value[idx] = updated
    }
    if (currentOrder.value?.id === Number(id)) {
      currentOrder.value = updated
    }
  }

  const pay = async (id: string | number, method: string) => {
    const res: any = await payOrder(id, method)
    const updated = res.data || res
    const idx = orders.value.findIndex(x => x.id === Number(id))
    if (idx > -1) {
      orders.value[idx] = updated
    }
    if (currentOrder.value?.id === Number(id)) {
      currentOrder.value = updated
    }
  }

  return {
    orders,
    currentOrder,
    loading,
    getOrders,
    getOrderDetail,
    cancel,
    pay
  }
})

export default useOrderStore
