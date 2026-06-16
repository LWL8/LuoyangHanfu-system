import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { fetchCurrentUserInfo, normalizeUserInfo, type UserInfo, type LoginResponse } from '@/api/user'

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('hanfu_token') || '')
  const userInfo = ref<UserInfo | null>(null)
  const AVATAR_OVERRIDE_KEY = 'profile_avatar_override'
  const DEFAULT_AVATAR = 'https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=300&h=300&fit=crop'

  const applyAvatarOverride = (info: UserInfo | null) => {
    if (!info) return info
    const localAvatar = localStorage.getItem(AVATAR_OVERRIDE_KEY) || ''
    if (!localAvatar) return info
    return { ...info, yonghuPhoto: localAvatar, avatar: localAvatar }
  }

  // 从 localStorage 恢复用户信息
  const savedUser = localStorage.getItem('user_info')
  if (savedUser) {
    try {
      userInfo.value = applyAvatarOverride(JSON.parse(savedUser))
    } catch (e) {
      console.error('Failed to parse user info', e)
    }
  }

  const isLoggedIn = computed(() => !!token.value)
  const isAdmin = computed(() => {
    const r = userInfo.value?.role
    return r === 'admin' || r === '管理员' || r === 'SUPER_ADMIN' || r === 'ADMIN'
  })

  const loginSuccess = (data: LoginResponse) => {
    token.value = data.token
    userInfo.value = applyAvatarOverride(data.user)
    localStorage.setItem('hanfu_token', data.token)
    localStorage.setItem('user_info', JSON.stringify(userInfo.value))
  }

  const syncUserProfile = async () => {
    if (!token.value) return
    try {
      const res: any = await fetchCurrentUserInfo()
      if ((res?.code === 200 || res?.code === 0) && res?.data) {
        const normalized = normalizeUserInfo({
          ...res.data,
          role: userInfo.value?.role || 'user'
        })
        userInfo.value = applyAvatarOverride(normalized)
        localStorage.setItem('user_info', JSON.stringify(userInfo.value))
      }
    } catch (e) {
      console.error('syncUserProfile failed', e)
    }
  }

  const logout = () => {
    token.value = ''
    userInfo.value = null
    localStorage.removeItem('hanfu_token')
    localStorage.removeItem('user_info')
  }

  const updateUserInfo = (info: Partial<UserInfo>) => {
    if (userInfo.value) {
      userInfo.value = { ...userInfo.value, ...info }
      const nextAvatar = String((info as any).yonghuPhoto || (info as any).avatar || '')
      if (nextAvatar) {
        localStorage.setItem(AVATAR_OVERRIDE_KEY, nextAvatar)
      }
      localStorage.setItem('user_info', JSON.stringify(userInfo.value))
    }
  }

  return {
    token,
    userInfo,
    isLoggedIn,
    isAdmin,
    DEFAULT_AVATAR,
    loginSuccess,
    logout,
    updateUserInfo,
    syncUserProfile
  }
})

export default useUserStore
