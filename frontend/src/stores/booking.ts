import { defineStore } from 'pinia'

export interface SelectedService {
  id: string | number
  name: string
  price: number
  category: 'makeup' | 'photography' | 'props'
}

export interface BookingUserInfo {
  name: string
  phone: string
  remark: string
}

export interface SelectedHanfuItem {
  id: number
  name: string
  image: string
  size: string
  quantity: number
  price: number
}

export const useBookingStore = defineStore('booking', {
  state: () => {
    const saved = sessionStorage.getItem('lh_booking_state')
    if (saved) {
      try {
        return JSON.parse(saved)
      } catch (e) {
        console.error('Failed to parse booking state', e)
      }
    }

    return {
      step: 1,
      date: '',
      timeSlot: '', // String like "09:00-12:00"
      hanfuId: 0,
      selectedHanfuList: [] as SelectedHanfuItem[],
      selectedServices: [] as SelectedService[],
      userInfo: { name: '', phone: '', remark: '' } as BookingUserInfo,
    }
  },

  getters: {
    hanfuTotal: (state): number =>
      state.selectedHanfuList.reduce((sum: number, item: SelectedHanfuItem) => sum + item.price * item.quantity, 0),
    servicesTotal: (state): number => state.selectedServices.reduce((sum: number, item: SelectedService) => sum + item.price, 0),
    grandTotal(): number {
      return this.hanfuTotal + this.servicesTotal
    }
  },

  actions: {
    saveToStorage() {
      sessionStorage.setItem('lh_booking_state', JSON.stringify(this.$state))
    },

    setStep(n: number) {
      this.step = n
      this.saveToStorage()
    },

    setDateTime(date: string, timeSlot: string) {
      this.date = date
      this.timeSlot = timeSlot
      this.saveToStorage()
    },

    addHanfu(item: SelectedHanfuItem) {
      const existing = this.selectedHanfuList.find((x: SelectedHanfuItem) => x.id === item.id && x.size === item.size)
      if (existing) {
        existing.quantity += item.quantity
      } else {
        this.selectedHanfuList.push(item)
      }
      if (!this.hanfuId) {
        this.hanfuId = item.id
      }
      this.saveToStorage()
    },

    removeHanfu(id: number, size?: string) {
      if (size) {
        this.selectedHanfuList = this.selectedHanfuList.filter((x: SelectedHanfuItem) => !(x.id === id && x.size === size))
      } else {
        this.selectedHanfuList = this.selectedHanfuList.filter((x: SelectedHanfuItem) => x.id !== id)
      }
      if (this.selectedHanfuList.length === 0) {
        this.hanfuId = 0
      } else {
        this.hanfuId = this.selectedHanfuList[0].id
      }
      this.saveToStorage()
    },

    toggleService(service: SelectedService) {
      const idx = this.selectedServices.findIndex((x: SelectedService) => x.id === service.id)
      if (idx > -1) {
        this.selectedServices.splice(idx, 1)
      } else {
        this.selectedServices.push(service)
      }
      this.saveToStorage()
    },

    reset() {
      this.step = 1
      this.date = ''
      this.timeSlot = ''
      this.hanfuId = 0
      this.selectedHanfuList = []
      this.selectedServices = []
      this.userInfo = { name: '', phone: '', remark: '' }
      sessionStorage.removeItem('lh_booking_state')
    }
  }
})

export default useBookingStore
