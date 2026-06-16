import { createRouter, createWebHistory } from 'vue-router'
import { ElMessage } from 'element-plus'
import useUserStore from '@/stores/user'
import NProgress from '@/utils/nprogress'

const routes = [
  { path: '/', redirect: '/home' },
  { path: '/home', name: 'home', component: () => import('@/views/HomeView.vue'), meta: { title: '首页' } },
  { path: '/hanfu', name: 'hanfu-list', component: () => import('@/views/hanfu/HanfuListView.vue'), meta: { title: '汉服品鉴' } },
  { path: '/hanfu/:id', name: 'hanfu-detail', component: () => import('@/views/hanfu/HanfuDetailView.vue'), meta: { title: '汉服详情' } },
  {
    path: '/booking',
    name: 'booking',
    component: () => import('@/views/booking/BookingView.vue'),
    meta: { requiresAuth: true, title: '在线预约' },
  },
  {
    path: '/order',
    name: 'order',
    component: () => import('@/views/order/OrderListView.vue'),
    meta: { requiresAuth: true, title: '我的订单' },
  },
  {
    path: '/order/:id',
    name: 'order-detail',
    component: () => import('@/views/order/OrderDetailView.vue'),
    meta: { requiresAuth: true, title: '订单详情' },
  },
  {
    path: '/payment/:id',
    name: 'payment',
    component: () => import('@/views/order/PaymentView.vue'),
    meta: { requiresAuth: true, title: '订单支付' },
  },
  { path: '/login', name: 'login', component: () => import('@/views/auth/LoginView.vue'), meta: { title: '登录' } },
  { path: '/register', name: 'register', component: () => import('@/views/auth/RegisterView.vue'), meta: { title: '注册' } },
  {
    path: '/profile',
    name: 'profile',
    component: () => import('@/views/user/ProfileView.vue'),
    meta: { requiresAuth: true, title: '个人中心' },
  },
  {
    path: '/recharge',
    name: 'recharge',
    component: () => import('@/views/user/RechargeView.vue'),
    meta: { requiresAuth: true, title: '充值中心' },
  },

  { path: '/agreement', name: 'agreement', component: () => import('@/views/notice/UserAgreementView.vue'), meta: { title: '用户协议' } },
  { path: '/privacy', name: 'privacy', component: () => import('@/views/notice/PrivacyPolicyView.vue'), meta: { title: '隐私政策' } },
  { path: '/luoyang/guides', name: 'luoyang-guides', component: () => import('@/views/luoyang/GuideView.vue'), meta: { title: '洛阳攻略' } },
  {
    path: '/admin',
    component: () => import('@/views/admin/AdminLayout.vue'),
    meta: { requiresAuth: true, isAdmin: true, title: '管理后台' },
    children: [
      { path: '', name: 'admin-dashboard', component: () => import('@/views/admin/AdminDashboard.vue'), meta: { title: '数据看板' } },
      { path: 'banner', name: 'admin-banner', component: () => import('@/views/admin/AdminBannerManage.vue'), meta: { title: '轮播图管理' } },
      { path: 'recommend', name: 'admin-recommend', component: () => import('@/views/admin/AdminRecommendManage.vue'), meta: { title: '新品推荐' } },
      { path: 'hanfu', name: 'admin-hanfu', component: () => import('@/views/admin/AdminHanfuList.vue'), meta: { title: '汉服管理' } },
      { path: 'orders', name: 'admin-orders', component: () => import('@/views/admin/AdminOrderList.vue'), meta: { title: '订单流水' } },
      { path: 'reservations', name: 'admin-reservations', component: () => import('@/views/admin/AdminReservationManage.vue'), meta: { title: '预约订单管理' } },
      { path: 'services', name: 'admin-services', component: () => import('@/views/admin/AdminServiceList.vue'), meta: { title: '服务套餐' } },
      { path: 'gallery', name: 'admin-gallery', component: () => import('@/views/admin/AdminGalleryView.vue'), meta: { title: '素材库' } },
      { path: 'users', name: 'admin-users', component: () => import('@/views/admin/AdminUserList.vue'), meta: { title: '用户中心' } },
      { path: 'guides', name: 'admin-guides', component: () => import('@/views/admin/AdminGuideManage.vue'), meta: { title: '攻略管理' } },
    ],
  },
  { path: '/:pathMatch(.*)*', redirect: '/home' },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

router.beforeEach((to) => {
  NProgress.start()
  const store = useUserStore()
  
  // 更新页面标题
  const baseTitle = '洛裳汉服'
  const subTitle = to.meta.title as string
  document.title = subTitle ? `${subTitle} | ${baseTitle}` : baseTitle

  if (to.meta.requiresAuth && !store.token) {
    NProgress.done()
    return { path: '/login', query: { redirect: to.fullPath } }
  }
  if (to.meta.isAdmin && !store.isAdmin) {
    ElMessage.error('需要管理员权限')
    NProgress.done()
    return '/home'
  }
  return true
})

router.afterEach(() => {
  NProgress.done()
})

export default router
