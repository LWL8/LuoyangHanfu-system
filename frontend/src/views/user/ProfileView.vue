<template>
  <div class="min-h-screen bg-[var(--lh-cream)] pb-16">
    <HeaderNav />

    <div v-if="userStore.userInfo" class="w-full px-4 md:px-8 py-8 md:py-10 space-y-5">
      <section class="profile-head-card relative overflow-hidden">
        <!-- 背景装饰 -->
        <div class="absolute right-0 top-0 w-64 h-64 bg-[var(--lh-gold)] opacity-[0.03] rounded-full blur-3xl translate-x-1/2 -translate-y-1/2"></div>
        <div class="absolute left-1/4 bottom-0 w-48 h-48 bg-[var(--lh-red)] opacity-[0.03] rounded-full blur-3xl translate-y-1/2"></div>

        <div class="flex flex-col xl:flex-row xl:items-center gap-8 justify-between relative z-10">
          <div class="flex items-center gap-6">
            <el-upload
              class="avatar-uploader"
              action="#"
              :auto-upload="false"
              :show-file-list="false"
              :limit="1"
              accept="image/png,image/jpeg,image/jpg,image/webp"
              :disabled="avatarUploading"
              @change="handleDirectAvatarChange"
            >
              <div class="avatar-upload-wrap">
                <el-avatar :size="96" :src="avatarUrl" class="border-[4px] border-white shadow-xl bg-[var(--lh-cream)]" />
                <div class="avatar-mask">{{ avatarUploading ? '上传中...' : '更换头像' }}</div>
              </div>
            </el-upload>
            <div>
              <div class="flex items-center gap-3">
                 <h2 class="text-3xl font-serif font-black text-[var(--lh-navy)] tracking-wider">{{ userStore.userInfo.yonghuName || '未设置昵称' }}</h2>
                 <div class="px-3 py-1 rounded-sm bg-[var(--lh-navy)] text-[var(--lh-gold)] text-[10px] font-bold tracking-widest uppercase">VIP</div>
              </div>
              <div class="text-sm font-bold text-slate-400 mt-3 tracking-[0.1em] flex items-center gap-3">
                 <span>账号ID : {{ userStore.userInfo.username || '尚未登录' }}</span>
                 <span class="w-1.5 h-1.5 rounded-full bg-slate-300"></span>
                 <span>{{ maskPhone(userStore.userInfo.yonghuPhone || userStore.userInfo.phone || '') }}</span>
              </div>
            </div>
          </div>

          <div class="flex flex-wrap items-center gap-6 bg-slate-50/80 p-5 rounded-2xl border border-white shadow-sm backdrop-blur-sm self-start xl:self-auto">
            <div class="flex flex-col px-2">
              <span class="text-[11px] text-slate-400 font-bold uppercase tracking-widest mb-1">可用余额</span>
              <div class="text-[var(--lh-red)] font-serif font-black text-2xl flex items-baseline gap-1">
                <span class="text-sm">¥</span>{{ Number(userStore.userInfo.newMoney || 0).toFixed(2) }}
              </div>
            </div>
            <div class="w-px h-10 bg-slate-200 hidden sm:block"></div>
            <div class="flex flex-col px-2">
              <span class="text-[11px] text-slate-400 font-bold uppercase tracking-widest mb-1">会员等级</span>
              <div class="text-[var(--lh-navy)] font-serif font-black text-xl">
                洛裳雅客
              </div>
            </div>
            <div class="w-px h-10 bg-slate-200 hidden sm:block"></div>
            <el-button 
              type="primary" 
              class="rounded-xl font-bold bg-[var(--lh-navy)] border-none hover:bg-[var(--lh-red)] transition-all px-8 h-[44px] shadow-lg shadow-[var(--lh-navy)]/20 ml-2"
              @click="router.push('/recharge')"
            >
              去充值
            </el-button>
          </div>
        </div>
      </section>

      <section class="grid grid-cols-1 lg:grid-cols-[240px_minmax(0,1fr)] gap-5">
        <aside class="profile-menu-card">
          <p class="menu-title">个人中心</p>
          <button
            v-for="item in menuItems"
            :key="item.key"
            class="menu-item"
            :class="{ 'is-active': activeSection === item.key }"
            @click="activeSection = item.key"
          >
            {{ item.label }}
          </button>
        </aside>

        <div class="panel p-5 md:p-6">
          <section v-if="activeSection === 'reservation'">
            <div class="flex items-center justify-between mb-4">
              <h3 class="block-title mb-0">我的预约</h3>
              <el-button plain @click="router.push('/order')">查看全部</el-button>
            </div>
            <p class="text-xs text-[var(--lh-text-secondary)] mb-4">支持查看订单详情与取消预约</p>
            <div v-if="orderLoading" class="empty-box">正在加载预约列表...</div>
            <div v-else-if="myReservations.length === 0" class="empty-box">暂无预约记录，去挑选汉服开始预约吧</div>
            <div v-else class="space-y-3">
              <div v-for="item in myReservations" :key="item.id" class="order-item">
                <div class="flex flex-col md:flex-row md:items-center md:justify-between gap-3">
                  <div class="min-w-0">
                    <p class="font-semibold text-[var(--lh-navy)] truncate">
                      {{ item.hanfuName || '汉服租赁' }}
                      <span class="ml-2 text-xs text-[var(--lh-text-secondary)]">#{{ item.orderNo }}</span>
                    </p>
                    <p class="text-sm text-[var(--lh-text-secondary)] mt-1">
                      {{ item.bookingDate || '--' }} {{ item.timeSlot || '' }} · ¥{{ Number(item.totalAmount || 0).toFixed(2) }}
                    </p>
                  </div>
                  <div class="flex items-center gap-2 shrink-0">
                    <el-tag size="small" :type="statusTagType(item.status)">{{ statusLabel(item.status) }}</el-tag>
                    <el-button size="small" @click="router.push(`/order/${item.id}`)">订单详情</el-button>
                    <el-button
                      v-if="canCancel(item.status)"
                      size="small"
                      type="danger"
                      plain
                      :loading="cancelingOrderId === item.id"
                      @click="cancelMyReservation(item)"
                    >
                      取消预约
                    </el-button>
                  </div>
                </div>
              </div>
            </div>
          </section>

          <section v-else-if="activeSection === 'profile'" class="animate__animated animate__fadeIn animate__faster">
            <div class="flex items-center justify-between mb-8 pb-4 border-b border-[var(--lh-border)]">
              <h3 class="text-2xl font-serif font-black text-[var(--lh-navy)] mb-0 flex items-center gap-3">
                <el-icon class="text-[var(--lh-red)]"><UserFilled /></el-icon>
                基础资料
              </h3>
              <el-button type="primary" plain class="rounded-xl px-6 font-bold tracking-widest uppercase transition-transform hover:scale-105" @click="openEditDrawer">
                <el-icon class="mr-2"><EditPen /></el-icon>编辑信息
              </el-button>
            </div>
            
            <div class="bg-[var(--lh-cream)]/30 rounded-3xl p-8 border border-white shadow-sm relative overflow-hidden transition-all hover:shadow-md">
               <div class="absolute right-0 top-0 opacity-[0.02] scale-150 transform translate-x-1/3 -translate-y-1/3 pointer-events-none">
                 <el-icon size="300"><UserFilled /></el-icon>
               </div>
              <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-y-10 gap-x-12 relative z-10">
                <div class="info-group">
                  <div class="info-label">用户账号 / Account</div>
                  <div class="info-value font-mono">{{ userStore.userInfo.username || '--' }}</div>
                </div>
                
                <div class="info-group">
                  <div class="info-label">用户昵称 / Name</div>
                  <div class="info-value font-serif font-bold text-[var(--lh-navy)] tracking-widest text-lg">{{ userStore.userInfo.yonghuName || '--' }}</div>
                </div>

                <div class="info-group">
                  <div class="info-label">手机号码 / Phone</div>
                  <div class="info-value flex items-center gap-3">
                    {{ maskPhone(userStore.userInfo.yonghuPhone || userStore.userInfo.phone || '') || '--' }}
                    <el-tag size="small" type="success" effect="light" class="rounded-full border-none bg-green-100 text-green-700 font-bold scale-90 origin-left">已认证</el-tag>
                  </div>
                </div>

                <div class="info-group">
                  <div class="info-label">账户可用余额 / Balance</div>
                  <div class="info-value flex items-baseline gap-1 text-[var(--lh-red)] font-serif font-black text-2xl">
                    <span class="text-sm">¥</span>{{ Number(userStore.userInfo.newMoney || 0).toFixed(2) }}
                  </div>
                </div>

                <div class="info-group">
                  <div class="info-label">电子邮箱 / Email</div>
                  <div class="info-value">{{ (userStore.userInfo as any).email || '未绑定邮箱' }}</div>
                </div>

                <div class="info-group">
                  <div class="info-label">注册时间 / Registration</div>
                  <div class="info-value">{{ userStore.userInfo.createTime || '--' }}</div>
                </div>
                
                <div class="info-group">
                  <div class="info-label">当前状态 / Status</div>
                  <div class="info-value">
                     <div class="flex items-center gap-2">
                       <span class="relative flex h-2.5 w-2.5">
                         <span class="animate-ping absolute inline-flex h-full w-full rounded-full bg-green-400 opacity-75"></span>
                         <span class="relative inline-flex rounded-full h-2.5 w-2.5 bg-green-500"></span>
                       </span>
                       <span class="font-bold text-green-600 text-sm">正常运作</span>
                     </div>
                  </div>
                </div>
              </div>
            </div>
          </section>

          <section v-else-if="activeSection === 'address'">
            <div class="flex items-center justify-between mb-4">
              <h3 class="block-title mb-0">地址管理</h3>
              <el-button type="primary" plain @click="openAddressDialog()">新增地址</el-button>
            </div>
            <p class="text-sm text-[var(--lh-text-secondary)] mb-4">用于异地快递租赁收货与回寄</p>
            <div v-if="addresses.length === 0" class="empty-box">暂无地址，请先新增收货地址</div>
            <div v-else class="space-y-3">
              <div v-for="item in addresses" :key="item.id" class="addr-item">
                <div class="flex items-start justify-between gap-4">
                  <div class="min-w-0">
                    <p class="font-bold text-[var(--lh-navy)]">
                      {{ item.receiver }} {{ item.phone }}
                      <el-tag v-if="item.isDefault" size="small" type="danger" class="ml-2">默认</el-tag>
                    </p>
                    <p class="text-sm md:text-base text-[var(--lh-text-secondary)] mt-1 break-words">{{ item.region }} {{ item.detail }}</p>
                  </div>
                  <div class="shrink-0 flex gap-2">
                    <el-button size="small" @click="openAddressDialog(item)">编辑</el-button>
                    <el-button size="small" type="danger" plain @click="removeAddress(item.id)">删除</el-button>
                  </div>
                </div>
                <div class="mt-3">
                  <el-button v-if="!item.isDefault" size="small" plain @click="setDefaultAddress(item.id)">设为默认</el-button>
                </div>
              </div>
            </div>
          </section>

          <section v-else-if="activeSection === 'help'">
            <h3 class="block-title">客服与帮助</h3>
            <div class="space-y-3">
              <button class="help-item" @click="openHelp('教程')">汉服穿着教程</button>
              <button class="help-item" @click="openHelp('协议')">租赁协议</button>
              <button class="help-item" @click="openHelp('赔付')">污损赔付标准</button>
              <button class="help-item" @click="openHelp('客服')">在线客服</button>
            </div>
          </section>

          <section v-else>
            <h3 class="block-title">设置</h3>
            <div class="space-y-4">
              <div class="flex items-center justify-between">
                <div>
                  <p class="font-semibold text-[var(--lh-navy)] text-base">隐私设置</p>
                  <p class="text-sm text-[var(--lh-text-secondary)]">隐藏手机号中间四位</p>
                </div>
                <el-switch v-model="privacyMaskPhone" @change="persistSettings" />
              </div>
              <div class="flex gap-3 pt-2">
                <el-button @click="switchAccount">切换账号</el-button>
                <el-button type="danger" @click="handleLogout">退出登录</el-button>
              </div>
            </div>
          </section>
        </div>
      </section>
    </div>

    <el-drawer v-model="showEditDrawer" direction="rtl" size="440px" title="编辑个人资料" class="profile-drawer">
      <div v-if="editForm" class="p-0 overflow-y-auto h-full flex flex-col">
        <div class="bg-[var(--lh-navy)] p-10 text-white relative h-32 shrink-0">
          <div class="relative z-10">
            <h4 class="text-xl font-serif font-bold tracking-widest">完善资料</h4>
            <p class="text-white/60 text-xs mt-1">上传头像并更新您的常用联系方式</p>
          </div>
          <div class="absolute right-0 bottom-0 opacity-10 transform translate-x-1/4 translate-y-1/4">
             <el-icon size="120"><UserFilled /></el-icon>
          </div>
        </div>

        <div class="p-8 space-y-8 flex-1">
          <div class="flex flex-col items-center gap-4 mb-2">
            <div class="relative group cursor-pointer" @click="triggerAvatarUpload">
               <el-avatar :size="110" :src="editForm.yonghuPhoto || avatarUrl" class="border-4 border-white shadow-xl bg-[var(--lh-cream)]" />
               <div class="absolute inset-0 bg-black/40 rounded-full flex flex-col items-center justify-center opacity-0 group-hover:opacity-100 transition-all text-white">
                  <el-icon size="20"><Plus /></el-icon>
                  <span class="text-[10px] mt-1 font-bold">更换头像</span>
               </div>
               <el-upload
                 ref="avatarUploadRef"
                 class="hidden"
                 action="#"
                 :auto-upload="false"
                 :show-file-list="false"
                 @change="handleAvatarChange"
               >
               </el-upload>
            </div>
          </div>

          <el-form label-position="top" class="lh-edit-form">
            <el-form-item label="用户昵称 / Nickname">
              <el-input v-model="editForm.yonghuName" placeholder="输入您的神都雅称" maxlength="20">
                <template #prefix><el-icon><User /></el-icon></template>
              </el-input>
            </el-form-item>
            
            <el-form-item label="手机号码 / Phone Number">
              <el-input v-model="editForm.yonghuPhone" placeholder="输入11位手机号" maxlength="11">
                <template #prefix><el-icon><Iphone /></el-icon></template>
              </el-input>
            </el-form-item>

            <el-form-item label="电子邮箱 / Email Address">
              <el-input v-model="editForm.email" placeholder="输入常用邮箱（可选）">
                <template #prefix><el-icon><Message /></el-icon></template>
              </el-input>
            </el-form-item>
          </el-form>
        </div>

        <div class="p-6 border-t border-[var(--lh-border)] bg-slate-50 flex gap-3 shrink-0">
          <el-button class="flex-1 rounded-xl h-12 font-bold" @click="showEditDrawer = false">取消</el-button>
          <el-button type="primary" class="flex-1 rounded-xl h-12 font-bold bg-[var(--lh-navy)] border-none hover:opacity-90 transition-all" :loading="saving" @click="saveProfile">保存修改</el-button>
        </div>
      </div>
    </el-drawer>

    <el-dialog v-model="showAddressDialog" :title="addressForm.id ? '编辑地址' : '新增地址'" width="520px" destroy-on-close class="lh-address-dialog">
      <el-form ref="addressFormRef" :model="addressForm" :rules="addressRules" label-position="top">
        <el-form-item label="收货人" prop="receiver">
          <template #label>
            <div class="flex items-center gap-1.5 font-bold"><el-icon><User /></el-icon>收货人</div>
          </template>
          <el-input v-model="addressForm.receiver" placeholder="填写收货人姓名" maxlength="20" />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <template #label>
            <div class="flex items-center gap-1.5 font-bold"><el-icon><Iphone /></el-icon>手机号</div>
          </template>
          <el-input v-model="addressForm.phone" placeholder="填写11位手机号码" maxlength="11" />
        </el-form-item>
        <el-form-item label="所在地区" prop="region">
          <template #label>
            <div class="flex items-center gap-1.5 font-bold"><el-icon><Location /></el-icon>所在地区</div>
          </template>
          <el-input v-model="addressForm.region" placeholder="例如：河南省 洛阳市 洛龙区" />
        </el-form-item>
        <el-form-item label="详细地址" prop="detail">
          <template #label>
            <div class="flex items-center gap-1.5 font-bold"><el-icon><Edit /></el-icon>详细地址</div>
          </template>
          <el-input v-model="addressForm.detail" type="textarea" :rows="3" placeholder="填写道路、门牌号、楼层等信息" />
        </el-form-item>
        <el-form-item class="mb-0">
          <el-checkbox v-model="addressForm.isDefault" class="default-checkbox">设为默认地址</el-checkbox>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer p-2">
          <el-button @click="showAddressDialog = false">取 消</el-button>
          <el-button type="primary" class="font-bold px-8 bg-[var(--lh-navy)]" @click="saveAddress">保 存</el-button>
        </div>
      </template>
    </el-dialog>

    <el-drawer v-model="showHelpDrawer" direction="rtl" size="460px" :title="helpTitle" class="profile-drawer">
      <div class="p-6 text-sm leading-7 text-[var(--lh-text-secondary)] whitespace-pre-wrap">{{ helpContent }}</div>
    </el-drawer>
  </div>
</template>

<script setup lang="ts">
import { computed, reactive, ref, onMounted } from 'vue'
import { UserFilled, EditPen, User, Iphone, Location, Edit, Message, Plus } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'
import HeaderNav from '@/components/common/HeaderNav.vue'
import useUserStore from '@/stores/user'
import { ElMessage, ElMessageBox } from 'element-plus'
import { updateCurrentUserInfo, uploadAvatarFile } from '@/api/user'
import { fetchMyReservations, cancelReservation } from '@/api/order'

type AddressItem = {
  id: number
  receiver: string
  phone: string
  region: string
  detail: string
  isDefault: boolean
}

const router = useRouter()
const userStore = useUserStore()

const showEditDrawer = ref(false)
const showHelpDrawer = ref(false)
const showAddressDialog = ref(false)
const saving = ref(false)
const editForm = ref<any>(null)
const activeSection = ref<'reservation' | 'profile' | 'address' | 'help' | 'settings'>('reservation')
const privacyMaskPhone = ref(true)
const helpTitle = ref('')
const helpContent = ref('')
const avatarUploadRef = ref<any>(null)
const addressFormRef = ref<any>(null)
const addressRules = {
  receiver: [{ required: true, message: '请输入收货人', trigger: 'blur' }],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的11位手机号', trigger: 'blur' }
  ],
  region: [{ required: true, message: '请选择/输入所在地区', trigger: 'change' }],
  detail: [{ required: true, message: '请输入详细地址', trigger: 'blur' }]
}
const addresses = ref<AddressItem[]>([])
const orderLoading = ref(false)
const cancelingOrderId = ref<number | null>(null)
const myReservations = ref<any[]>([])
const avatarUploading = ref(false)
const localAvatar = ref('')
const menuItems = [
  { key: 'reservation', label: '我的预约' },
  { key: 'profile', label: '基础资料' },
  { key: 'address', label: '地址管理' },
  { key: 'help', label: '客服与帮助' },
  { key: 'settings', label: '设置' }
] as const

const avatarUrl = computed(() => userStore.userInfo?.yonghuPhoto || userStore.DEFAULT_AVATAR)

const addressForm = reactive<AddressItem>({
  id: 0,
  receiver: '',
  phone: '',
  region: '',
  detail: '',
  isDefault: false
})

const maskPhone = (p: string) => {
  if (!p) return ''
  if (!privacyMaskPhone.value) return p
  return p.replace(/(\d{3})\d{4}(\d{4})/, '$1****$2')
}

const persistSettings = () => {
  localStorage.setItem('profile_privacy_mask_phone', privacyMaskPhone.value ? '1' : '0')
}

const loadSettings = () => {
  privacyMaskPhone.value = localStorage.getItem('profile_privacy_mask_phone') !== '0'
}

const loadAddresses = () => {
  try {
    const raw = localStorage.getItem('profile_addresses')
    addresses.value = raw ? JSON.parse(raw) : []
  } catch {
    addresses.value = []
  }
}

const persistAddresses = () => {
  localStorage.setItem('profile_addresses', JSON.stringify(addresses.value))
}

const persistLocalAvatar = (url: string) => {
  localAvatar.value = url
  localStorage.setItem('profile_avatar_override', url)
}

const loadLocalAvatar = () => {
  localAvatar.value = localStorage.getItem('profile_avatar_override') || ''
}

const handleDirectAvatarChange = async (file: any) => {
  const raw = file?.raw as File | undefined
  if (!raw) return
  const allowedTypes = ['image/png', 'image/jpeg', 'image/jpg', 'image/webp']
  if (!allowedTypes.includes(raw.type)) {
    ElMessage.error('仅支持 JPG/PNG/WEBP 格式')
    return
  }
  if (raw.size > 3 * 1024 * 1024) {
    ElMessage.error('图片不能超过 3MB')
    return
  }
  if (avatarUploading.value) return

  avatarUploading.value = true
  const reader = new FileReader()
  reader.onload = async () => {
    const base64Preview = String(reader.result || '')
    let finalAvatar = base64Preview
    persistLocalAvatar(base64Preview)
    userStore.updateUserInfo({ yonghuPhoto: base64Preview, avatar: base64Preview })
    try {
      try {
        // 优先走文件上传接口，拿 URL 保存，避免仅 base64 本地化
        finalAvatar = await uploadAvatarFile(raw)
      } catch {
        finalAvatar = base64Preview
      }
      const currentPhone = String(userStore.userInfo?.yonghuPhone || userStore.userInfo?.phone || '').replace(/\D/g, '').slice(0, 11)
      const currentName = String(userStore.userInfo?.yonghuName || userStore.userInfo?.nickname || '')
      const payload = {
        username: userStore.userInfo?.username,
        nickname: currentName,
        yonghuName: currentName,
        avatar: finalAvatar,
        yonghuPhoto: finalAvatar,
        phone: currentPhone,
        yonghuPhone: currentPhone
      }

      const res: any = await updateCurrentUserInfo(payload)
      if (res?.code !== 200 && res?.code !== 0) {
        ElMessage.warning(res?.message || '头像已更新到本地，服务器同步失败')
        avatarUploading.value = false
        return
      }
      persistLocalAvatar(finalAvatar)
      userStore.updateUserInfo({ yonghuPhoto: finalAvatar, avatar: finalAvatar })
      // 同步资料时若后端未返回新头像，仍优先使用本地持久化头像
      await userStore.syncUserProfile()
      ElMessage.success('头像已更新')
    } catch (e: any) {
      ElMessage.warning(e?.response?.data?.message || '头像已更新到本地，服务器同步失败')
    } finally {
      avatarUploading.value = false
    }
  }
  reader.onerror = () => {
    avatarUploading.value = false
    ElMessage.error('读取图片失败，请重试')
  }
  reader.readAsDataURL(raw)
}

const mapOrderStatus = (status: string) => {
  const val = String(status || '').toUpperCase()
  if (val === 'RESERVED' || val === 'BOOKED' || val === 'CONFIRMED') return 'confirmed'
  if (val === 'USED' || val === 'COMPLETED') return 'used'
  if (val === 'CANCELLED' || val === 'VOID') return 'cancelled'
  if (val === 'PENDING' || val === 'UNPAID') return 'pending'
  return 'pending'
}

const statusLabel = (status: string) => {
  const map: Record<string, string> = {
    pending: '待支付',
    confirmed: '已预约',
    used: '已完成',
    cancelled: '已取消'
  }
  return map[status] || '处理中'
}

const statusTagType = (status: string) => {
  if (status === 'confirmed') return 'success'
  if (status === 'used') return 'info'
  if (status === 'cancelled') return 'info'
  return 'warning'
}

const canCancel = (status: string) => status === 'pending' || status === 'confirmed'

const normalizeOrder = (raw: any) => ({
  id: Number(raw.id),
  orderNo: raw.orderNo || `ORD-${raw.id}`,
  hanfuName: raw.hanfuName || '汉服租赁',
  bookingDate: raw.reservationDate || raw.bookingDate || '',
  timeSlot: raw.timeSlot || '',
  totalAmount: Number(raw.totalAmount || raw.totalPrice || 0),
  status: mapOrderStatus(raw.status || raw.payStatus)
})

const loadMyReservations = async () => {
  orderLoading.value = true
  try {
    const res: any = await fetchMyReservations({ pageNum: 1, pageSize: 5 })
    if ((res?.code === 200 || res?.code === 0) && Array.isArray(res?.data)) {
      myReservations.value = res.data.map(normalizeOrder)
    } else if ((res?.code === 200 || res?.code === 0) && Array.isArray(res?.data?.list)) {
      myReservations.value = res.data.list.map(normalizeOrder)
    } else {
      myReservations.value = []
    }
  } catch {
    myReservations.value = []
  } finally {
    orderLoading.value = false
  }
}

const cancelMyReservation = async (item: any) => {
  try {
    await ElMessageBox.confirm('确定取消该预约吗？', {
      type: 'warning',
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      showClose: false,
      alignCenter: true
    })
    cancelingOrderId.value = item.id
    const res: any = await cancelReservation(item.id, '个人中心取消预约')
    if (!(res?.code === 200 || res?.code === 0)) {
      ElMessage.error(res?.message || '取消失败')
      return
    }
    const idx = myReservations.value.findIndex((x) => x.id === item.id)
    if (idx >= 0) myReservations.value[idx].status = 'cancelled'
    ElMessage.success('预约已取消')
  } catch {
  } finally {
    cancelingOrderId.value = null
  }
}

const openEditDrawer = () => {
  if (!userStore.userInfo) return
  editForm.value = {
    yonghuName: userStore.userInfo.yonghuName || '',
    yonghuPhone: userStore.userInfo.yonghuPhone || '',
    yonghuPhoto: userStore.userInfo.yonghuPhoto || '',
    email: (userStore.userInfo as any).email || ''
  }
  showEditDrawer.value = true
}

const triggerAvatarUpload = () => {
  avatarUploadRef.value?.$el.querySelector('input')?.click()
}

const handleAvatarChange = (file: any) => {
  const raw = file?.raw as File | undefined
  if (!raw) return
  if (!raw.type.startsWith('image/')) {
    ElMessage.error('请上传图片文件')
    return
  }
  const reader = new FileReader()
  reader.onload = () => {
    const url = String(reader.result || '')
    if (editForm.value) editForm.value.yonghuPhoto = url
  }
  reader.readAsDataURL(raw)
}

const saveProfile = async () => {
  if (!editForm.value) return
  const phone = String(editForm.value.yonghuPhone || '').replace(/\D/g, '').slice(0, 11)
  if (!/^1[3-9]\d{9}$/.test(phone)) {
    ElMessage.error('请输入正确的手机号')
    return
  }
  saving.value = true
  try {
    const payload = {
      username: userStore.userInfo?.username,
      nickname: editForm.value.yonghuName || '',
      avatar: editForm.value.yonghuPhoto || '',
      phone,
      email: editForm.value.email || ''
    }
    const res: any = await updateCurrentUserInfo(payload)
    if (res?.code !== 200 && res?.code !== 0) {
      ElMessage.error(res?.message || '资料更新失败')
      return
    }
    userStore.updateUserInfo({
      yonghuName: payload.nickname,
      yonghuPhoto: payload.avatar,
      yonghuPhone: phone,
      phone
    })
    showEditDrawer.value = false
    ElMessage.success('资料更新成功')
  } catch (e: any) {
    ElMessage.error(e?.response?.data?.message || '资料更新失败')
  } finally {
    saving.value = false
  }
}

const resetAddressForm = () => {
  addressForm.id = 0
  addressForm.receiver = ''
  addressForm.phone = ''
  addressForm.region = ''
  addressForm.detail = ''
  addressForm.isDefault = false
}

const openAddressDialog = (item?: AddressItem) => {
  if (!item) {
    resetAddressForm()
  } else {
    addressForm.id = item.id
    addressForm.receiver = item.receiver
    addressForm.phone = item.phone
    addressForm.region = item.region
    addressForm.detail = item.detail
    addressForm.isDefault = item.isDefault
  }
  showAddressDialog.value = true
}

const saveAddress = async () => {
  if (!addressFormRef.value) return
  
  try {
    await addressFormRef.value.validate()
  } catch {
    return
  }

  const phone = String(addressForm.phone || '').trim()
  const next: AddressItem = {
    id: addressForm.id || Date.now(),
    receiver: addressForm.receiver.trim(),
    phone,
    region: addressForm.region.trim(),
    detail: addressForm.detail.trim(),
    isDefault: !!addressForm.isDefault
  }
  if (next.isDefault) {
    addresses.value = addresses.value.map((x) => ({ ...x, isDefault: false }))
  }
  const idx = addresses.value.findIndex((x) => x.id === next.id)
  if (idx >= 0) addresses.value[idx] = next
  else addresses.value.unshift(next)
  
  if (!addresses.value.some((x) => x.isDefault) && addresses.value.length > 0) {
    addresses.value[0].isDefault = true
  }
  persistAddresses()
  showAddressDialog.value = false
  ElMessage.success('地址已保存')
}

const setDefaultAddress = (id: number) => {
  addresses.value = addresses.value.map((x) => ({ ...x, isDefault: x.id === id }))
  persistAddresses()
  ElMessage.success('已设置默认地址')
}

const removeAddress = async (id: number) => {
  try {
    await ElMessageBox.confirm('确定删除该地址吗？', {
      type: 'warning',
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      showClose: false,
      alignCenter: true
    })
    addresses.value = addresses.value.filter((x) => x.id !== id)
    if (addresses.value.length > 0 && !addresses.value.some((x) => x.isDefault)) {
      addresses.value[0].isDefault = true
    }
    persistAddresses()
    ElMessage.success('地址已删除')
  } catch {}
}

const openHelp = (type: '教程' | '协议' | '赔付' | '客服') => {
  const map = {
    教程: {
      title: '汉服穿着教程',
      content:
        '1. 先穿中衣并整理衣领。\n2. 按款式穿主衣，系带打结后整理褶皱。\n3. 齐胸/齐腰裙需从后向前绕系，固定后再整理裙摆。\n4. 建议拍照前检查领口、袖口与发饰是否对称。'
    },
    协议: {
      title: '租赁协议',
      content:
        '租赁期间请妥善保管服饰与配件。\n如需延期，请在到期前联系客服处理。\n逾期未归还将按协议收取延期费用。'
    },
    赔付: {
      title: '污损赔付标准',
      content:
        '轻微污渍：按清洗难度收取清洗费。\n中度破损：按修复成本赔付。\n严重损坏或遗失：按服饰评估价赔付。'
    },
    客服: {
      title: '在线客服',
      content:
        '客服在线时间：09:00 - 21:00\n联系方式：400-888-8888\n你可以在“反馈”页面提交您的问题，我们会尽快处理。'
    }
  }
  helpTitle.value = map[type].title
  helpContent.value = map[type].content
  showHelpDrawer.value = true
}

const switchAccount = () => {
  userStore.logout()
  router.push({ path: '/login', query: { redirect: '/profile' } })
  ElMessage.success('请重新登录账号')
}

const handleLogout = () => {
  ElMessageBox.confirm('确定退出当前账号吗？', {
    type: 'warning',
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    showClose: false,
    alignCenter: true
  }).then(() => {
    userStore.logout()
    router.push('/')
    ElMessage.success('已退出登录')
  })
}

onMounted(() => {
  if (!userStore.isLoggedIn) {
    router.push({ path: '/login', query: { redirect: '/profile' } })
    return
  }
  loadSettings()
  loadAddresses()
  loadLocalAvatar()
  loadMyReservations()
  userStore.syncUserProfile()
})
</script>

<style scoped>
.profile-head-card {
  background: #fff;
  border: 1px solid var(--lh-border);
  border-radius: 12px;
  padding: 16px 20px;
}

.avatar-upload-wrap {
  position: relative;
  cursor: pointer;
}

.avatar-mask {
  position: absolute;
  inset: 0;
  border-radius: 9999px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  color: #fff;
  background: rgba(15, 23, 42, 0.55);
  opacity: 0;
  transition: opacity 0.2s;
}

.avatar-upload-wrap:hover .avatar-mask {
  opacity: 1;
}

.profile-menu-card {
  background: #fff;
  border: 1px solid var(--lh-border);
  border-radius: 12px;
  padding: 12px;
  height: fit-content;
}

.menu-title {
  font-size: 15px;
  font-weight: 700;
  color: var(--lh-navy);
  padding: 8px 10px 10px;
  border-bottom: 1px solid var(--lh-border);
  margin-bottom: 8px;
}

.menu-item {
  width: 100%;
  text-align: left;
  border-radius: 8px;
  padding: 10px;
  color: var(--lh-text-secondary);
  font-size: 14px;
  transition: all 0.2s;
}

.menu-item:hover {
  background: #f8fafc;
  color: var(--lh-navy);
}

.menu-item.is-active {
  background: #eff6ff;
  color: #1d4ed8;
  font-weight: 700;
}

.panel {
  background: #fff;
  border: 1px solid var(--lh-border);
  border-radius: 16px;
  box-shadow: 0 10px 28px rgba(15, 23, 42, 0.06);
}

.block-title {
  font-size: 20px;
  font-weight: 800;
  color: var(--lh-navy);
  margin-bottom: 16px;
}

.info-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 0;
  border-bottom: 1px dashed var(--lh-border);
  color: var(--lh-text-secondary);
}

.addr-item {
  border: 1px solid var(--lh-border);
  border-radius: 12px;
  padding: 12px;
  background: #fffdfa;
}

.empty-box {
  border: 1px dashed var(--lh-border);
  border-radius: 12px;
  padding: 22px;
  color: var(--lh-text-secondary);
  font-size: 14px;
}

.help-item {
  width: 100%;
  text-align: left;
  border: 1px solid var(--lh-border);
  border-radius: 10px;
  padding: 14px 16px;
  font-size: 15px;
  color: var(--lh-navy);
  background: #fff;
  transition: all 0.2s;
}

.help-item:hover {
  border-color: var(--lh-gold);
  background: #fff8ef;
}

.order-item {
  border: 1px solid var(--lh-border);
  border-radius: 12px;
  padding: 14px;
  background: #fff;
}

:deep(.profile-drawer .el-drawer__body) {
  padding: 0;
}

.info-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
}
.info-label {
  font-size: 11px;
  text-transform: uppercase;
  letter-spacing: 0.15em;
  color: var(--lh-text-secondary);
  font-weight: 800;
  opacity: 0.7;
}
.info-value {
  font-size: 16px;
  color: var(--lh-navy);
  font-weight: 600;
}

/* 编辑表单样式 */
.lh-edit-form :deep(.el-form-item__label) {
  font-family: inherit;
  font-size: 11px !important;
  text-transform: uppercase;
  letter-spacing: 0.1em;
  font-weight: 800;
  color: var(--lh-text-secondary);
  margin-bottom: 8px !important;
}

.lh-edit-form :deep(.el-input__wrapper) {
  background-color: #f8fafc;
  box-shadow: none !important;
  border: 1px solid #e2e8f0;
  border-radius: 12px;
  padding: 8px 12px;
  transition: all 0.2s;
}

.lh-edit-form :deep(.el-input__wrapper.is-focus) {
  background-color: #fff;
  border-color: var(--lh-navy);
  box-shadow: 0 0 0 1px var(--lh-navy) !important;
}

.lh-edit-form :deep(.el-input__inner) {
  font-weight: 600;
  color: var(--lh-navy);
}

.lh-edit-form :deep(.el-input__prefix-inner .el-icon) {
  font-size: 16px;
  color: #94a3b8;
}
</style>
