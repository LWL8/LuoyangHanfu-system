import request from '@/utils/request'

export interface UserInfo {
  id: number
  username: string
  password?: string
  role?: string
  yonghuName: string
  nickname?: string // Alias
  yonghuPhone: string
  phone?: string // Alias
  yonghuPhoto: string
  avatar?: string // Alias
  memberLevel?: string
  newMoney: number
  createTime: string
  createdAt?: string // Alias
}

export interface BackendUserInfo {
  id: number
  username: string
  nickname?: string
  phone: string
  avatar?: string
  balance?: number
  createTime?: string
  status?: number
}

/** 后端统一响应 R */
export interface ApiResult<T> {
  code: number
  message: string
  data: T
}

/** 后端 TokenVO */
export interface TokenVO {
  token: string
  tokenPrefix?: string
  userInfo?: {
    id: number
    username: string
    role: string
    avatar?: string
  }
}

export interface LoginResponse {
  token: string
  user: UserInfo
  role?: string
}

/** 将后端 TokenVO 转为前端 store 所需结构 */
export function loginResponseFromTokenVO(vo: TokenVO): LoginResponse {
  const ui = vo.userInfo
  if (!ui) {
    return { token: vo.token, user: {} as UserInfo }
  }
  const roleNorm =
    ui.role === 'SUPER_ADMIN' || ui.role === 'ADMIN' ? 'admin' : ui.role
  return {
    token: vo.token,
    role: roleNorm,
    user: {
      id: ui.id,
      username: ui.username,
      yonghuName: ui.username,
      yonghuPhone: '',
      phone: '',
      yonghuPhoto: ui.avatar || '',
      avatar: ui.avatar,
      memberLevel: '普通子民',
      newMoney: 0,
      createTime: new Date().toISOString().split('T')[0] + ' 10:00:00',
      role: roleNorm,
    },
  }
}

export function normalizeUserInfo(raw: Partial<BackendUserInfo> & Record<string, any>): UserInfo {
  const nickname = String(raw.nickname || raw.yonghuName || raw.username || '')
  return {
    id: Number(raw.id || 0),
    username: String(raw.username || ''),
    yonghuName: nickname,
    nickname,
    yonghuPhone: String(raw.yonghuPhone || raw.phone || ''),
    yonghuPhoto: String(raw.yonghuPhoto || raw.avatar || ''),
    newMoney: Number(raw.newMoney ?? raw.balance ?? 0),
    createTime: String(raw.createTime || ''),
    role: String(raw.role || 'user'),
    phone: String(raw.phone || raw.yonghuPhone || ''),
    avatar: String(raw.avatar || raw.yonghuPhoto || ''),
    memberLevel: String(raw.memberLevel || '普通子民')
  }
}

/**
 * 用户预约账本中的用户简报
 */
export interface UserListItem {
  id: number
  username: string
  yonghuName: string
  yonghuPhone: string
  yonghuPhoto: string
  newMoney: number
  createTime: string
}

/**
 * 用户密码登录 → POST /api/user/login
 */
export function loginByPassword(username: string, password: string) {
  return request.post<ApiResult<TokenVO>>('/user/login', { username, password })
}

/**
 * 管理员登录 → POST /api/admin/login
 */
export function loginAsAdmin(username: string, password: string) {
  return request.post<ApiResult<TokenVO>>('/admin/login', { username, password })
}

/**
 * 手机验证码登录
 */
export function loginByPhone(phone: string, code: string) {
  // 后端若未实现手机验证码登录，可在此对接真实接口
  return request.post<ApiResult<TokenVO>>('/user/loginByPhone', { phone, code })
}

/**
 * 发送短信验证码
 */
export function sendSmsCode(phone: string) {
  return request.post<ApiResult<{ verifyCode?: string }>>('/user/sendSms', null, { params: { phone } })
}

/**
 * 用户注册
 */
export function registerUser(data: {
  username: string
  nickname?: string
  password: string
  phone: string
  email?: string
}) {
  return request.post<ApiResult<void>>('/user/register', data)
}

/**
 * 获取个人 Session
 */
export function fetchSession() {
  return request.get('/user/info')
}

/**
 * 获取当前用户信息
 */
export function fetchCurrentUserInfo() {
  return request.get<ApiResult<BackendUserInfo>>('/user/info')
}

/**
 * 更新当前用户资料
 */
export function updateCurrentUserInfo(data: Partial<BackendUserInfo>) {
  return request.put<ApiResult<void>>('/user/update', data)
}

/**
 * 用户充值
 */
export function userRecharge(amount: number) {
  return request.post<ApiResult<void>>('/user/recharge', { amount })
}

/**
 * 上传头像，返回头像 URL（按常见接口路径依次尝试）
 */
export async function uploadAvatarFile(file: File): Promise<string> {
  const form = new FormData()
  form.append('file', file)
  const candidates = ['/user/upload-avatar', '/user/uploadAvatar', '/file/upload', '/common/upload']
  let lastErr: any
  for (const url of candidates) {
    try {
      const res: any = await request.post(url, form, {
        headers: { 'Content-Type': 'multipart/form-data' }
      })
      if (res?.code === 0 || res?.code === 200) {
        const data = res?.data
        const avatarUrl = data?.url || data?.avatar || data?.path || data
        if (typeof avatarUrl === 'string' && avatarUrl) {
          return avatarUrl
        }
      }
    } catch (e) {
      lastErr = e
    }
  }
  throw lastErr || new Error('upload avatar failed')
}

/**
 * 管理端 - 获取用户分页列表
 */
export function adminFetchUserList(params: any = {}) {
  return request.get('/api/admin/user/page', { params })
}

/**
 * 管理端 - 手动充值
 */
export function adminManualRecharge(userId: number | string, amount: number) {
  return request.post('/api/admin/user/recharge', null, { 
    params: { userId, amount } 
  })
}

/**
 * 管理端 - 删除用户
 */
export function adminDeleteUser(id: number | string) {
  return request.delete(`/api/admin/user/delete/${id}`)
}

/**
 * 管理端 - 用户启用/禁用
 */
export function adminUpdateUserStatus(id: number | string, status: number) {
  return request.put('/api/admin/user/status', null, {
    params: { id, status }
  })
}
