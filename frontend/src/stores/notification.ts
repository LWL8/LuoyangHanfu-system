import { defineStore } from 'pinia'

export interface NotificationItem {
  id: number
  title: string
  content: string
  time: string
  type: 'order' | 'payment' | 'system'
  read: boolean
}

export const useNotificationStore = defineStore('notification', {
  state: () => ({
    notifications: JSON.parse(localStorage.getItem('ls_notifications') || '[]') as NotificationItem[]
  }),
  getters: {
    unreadCount: (state) => state.notifications.filter(n => !n.read).length,
    recentList: (state) => state.notifications.slice(0, 5)
  },
  actions: {
    saveToLocal() {
      localStorage.setItem('ls_notifications', JSON.stringify(this.notifications))
    },
    addNotification(title: string, content: string, type: NotificationItem['type'] = 'system') {
      const id = Date.now()
      this.notifications.unshift({
        id,
        title,
        content,
        time: new Date().toLocaleString(),
        type,
        read: false
      })
      if (this.notifications.length > 50) {
        this.notifications.pop()
      }
      this.saveToLocal()
    },
    markAsRead(id: number) {
      const index = this.notifications.findIndex(n => n.id === id)
      if (index !== -1) {
        this.notifications[index].read = true
        this.saveToLocal()
      }
    },
    markAllAsRead() {
      this.notifications.forEach(n => n.read = true)
      this.saveToLocal()
    },
    clearAll() {
      this.notifications = []
      this.saveToLocal()
    }
  }
})
