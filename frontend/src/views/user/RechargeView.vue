<template>
  <div class="min-h-screen bg-[var(--lh-cream)] pb-16">
    <HeaderNav />

    <main class="max-w-4xl mx-auto px-4 pt-12">
      <div class="mb-10 flex items-center gap-4">
        <el-button circle @click="router.back()" class="shadow-sm">
           <el-icon><ArrowLeft /></el-icon>
        </el-button>
        <div>
          <h1 class="text-3xl font-serif font-black text-[var(--lh-navy)] tracking-wider">充值中心</h1>
          <p class="text-sm text-slate-400 font-bold uppercase tracking-widest mt-1">Recharge Center</p>
        </div>
      </div>

      <div class="grid grid-cols-1 lg:grid-cols-[1fr_320px] gap-8">
        <!-- 左侧充值选项 -->
        <div class="space-y-8">
          <section class="recharge-card">
            <div class="flex items-center gap-3 mb-6">
              <div class="w-1 h-6 bg-[var(--lh-red)] rounded-full"></div>
              <h2 class="text-xl font-serif font-bold text-[var(--lh-navy)]">选择充值金额</h2>
            </div>

            <div class="grid grid-cols-2 sm:grid-cols-3 gap-4">
               <div 
                 v-for="amount in presetAmounts" 
                 :key="amount"
                 class="amount-item"
                 :class="{ 'is-active': selectedAmount === amount && !isCustom }"
                 @click="selectAmount(amount)"
               >
                 <div class="text-sm opacity-60 mb-1">充值</div>
                 <div class="text-2xl font-serif font-black">
                   <span class="text-base mr-0.5">¥</span>{{ amount }}
                 </div>
                 <div v-if="getBonus(amount)" class="bonus-tag">赠 ¥{{ getBonus(amount) }}</div>
               </div>

               <div 
                 class="amount-item"
                 :class="{ 'is-active': isCustom }"
                 @click="enableCustom"
               >
                 <div class="text-sm opacity-60 mb-1">自定义</div>
                 <div v-if="!isCustom" class="text-lg font-bold">输入金额</div>
                 <div v-else class="flex items-center">
                   <span class="text-base mr-1">¥</span>
                   <input 
                     ref="customInput"
                     type="number" 
                     v-model="customAmount" 
                     class="custom-input"
                     placeholder="0.00"
                     @blur="validateCustom"
                   />
                 </div>
               </div>
            </div>
          </section>

          <section class="recharge-card">
            <div class="flex items-center gap-3 mb-6">
              <div class="w-1 h-6 bg-[var(--lh-red)] rounded-full"></div>
              <h2 class="text-xl font-serif font-bold text-[var(--lh-navy)]">支付方式</h2>
            </div>

            <div class="space-y-3">
              <div 
                v-for="method in paymentMethods" 
                :key="method.id"
                class="pay-method-item"
                :class="{ 'is-active': selectedMethod === method.id }"
                @click="selectedMethod = method.id"
              >
                <div class="flex items-center gap-4">
                  <div class="w-10 h-10 rounded-full flex items-center justify-center" :style="{ backgroundColor: method.color + '15' }">
                    <img :src="method.icon" :alt="method.name" class="w-6 h-6 object-contain" />
                  </div>
                  <span class="font-bold text-slate-700">{{ method.name }}</span>
                </div>
                <div class="check-circle" :class="{ 'checked': selectedMethod === method.id }">
                  <el-icon v-if="selectedMethod === method.id"><Check /></el-icon>
                </div>
              </div>
            </div>
          </section>
        </div>

        <!-- 右侧结算面板 -->
        <aside>
          <div class="sticky-panel">
            <div class="p-6 border-b border-slate-100 bg-slate-50/50">
              <p class="text-[11px] text-slate-400 font-bold uppercase tracking-widest mb-4">当前账户余额</p>
              <div class="text-[var(--lh-navy)] font-serif font-black text-3xl">
                <span class="text-lg mr-1">¥</span>{{ Number(userStore.userInfo?.newMoney || 0).toFixed(2) }}
              </div>
            </div>

            <div class="p-6 space-y-4">
              <div class="flex justify-between items-center text-sm">
                <span class="text-slate-500">充值金额</span>
                <span class="font-bold text-slate-800">¥{{ displayAmount.toFixed(2) }}</span>
              </div>
              <div v-if="displayBonus > 0" class="flex justify-between items-center text-sm">
                <span class="text-slate-500">赠送金额</span>
                <span class="font-bold text-[var(--lh-red)]">+¥{{ displayBonus.toFixed(2) }}</span>
              </div>
              <div class="h-px bg-slate-100 my-2"></div>
              <div class="flex justify-between items-end">
                <span class="text-slate-700 font-bold">应付金额</span>
                <div class="text-[var(--lh-red)] font-serif font-black text-3xl">
                  <span class="text-lg">¥</span>{{ displayAmount.toFixed(2) }}
                </div>
              </div>

              <el-button 
                type="primary" 
                class="w-full recharge-btn h-[54px] mt-6"
                :loading="loading"
                @click="handleRecharge"
              >
                {{ loading ? '处理中...' : '立即支付' }}
              </el-button>
              
              <div class="flex items-center justify-center gap-2 text-[10px] text-slate-400 mt-4">
                <el-icon><Lock /></el-icon>
                <span>加密支付环境，保障资金安全</span>
              </div>
            </div>
          </div>

          <div class="mt-6 p-4 rounded-2xl bg-[var(--lh-navy)] text-[var(--lh-gold)] opacity-90">
             <div class="flex items-center gap-2 mb-2">
               <el-icon size="16"><Warning /></el-icon>
               <span class="text-xs font-bold uppercase tracking-wider">充值提示</span>
             </div>
             <p class="text-[10px] leading-relaxed opacity-70">
               1. 充值金额可用于平台汉服租赁、妆造预约等消费。<br/>
               2. 充值成功后一般立即到账，若延迟请联系客服：400-888-8888。<br/>
               3. 充值赠送金额不支持提现，优先扣除充值余额。
             </p>
          </div>
        </aside>
      </div>
    </main>

    <!-- 支付成功弹窗 -->
    <el-dialog
      v-model="successVisible"
      width="400px"
      center
      :show-close="false"
      class="success-dialog"
    >
      <div class="py-6 text-center">
        <div class="w-16 h-16 bg-green-100 text-green-600 rounded-full flex items-center justify-center mx-auto mb-4 animate__animated animate__zoomIn">
          <el-icon size="32"><Check /></el-icon>
        </div>
        <h3 class="text-2xl font-serif font-black text-[var(--lh-navy)]">充值成功</h3>
        <p class="text-slate-500 mt-2">您的余额已更新，可前往个人中心查看</p>
        
        <div class="mt-8 grid grid-cols-2 gap-3">
          <el-button @click="router.push('/profile')" class="w-full rounded-xl">个人中心</el-button>
          <el-button type="primary" @click="successVisible = false" class="w-full rounded-xl bg-[var(--lh-navy)]">继续充值</el-button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import { ArrowLeft, Check, Lock, Warning } from '@element-plus/icons-vue'
import HeaderNav from '@/components/common/HeaderNav.vue'
import useUserStore from '@/stores/user'
import { userRecharge } from '@/api/user'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()

const loading = ref(false)
const successVisible = ref(false)
const selectedAmount = ref(200)
const isCustom = ref(false)
const customAmount = ref('')
const customInput = ref<HTMLInputElement | null>(null)
const selectedMethod = ref('wechat')

const presetAmounts = [100, 200, 500, 1000, 2000, 5000]

const paymentMethods = [
  { id: 'wechat', name: '微信支付', icon: 'https://cdn-icons-png.flaticon.com/512/3670/3670300.png', color: '#07C160' },
  { id: 'alipay', name: '支付宝', icon: 'https://cdn-icons-png.flaticon.com/512/5968/5968163.png', color: '#1677FF' },
  { id: 'bank', name: '银行卡', icon: 'https://cdn-icons-png.flaticon.com/512/633/633611.png', color: '#FF9500' }
]

const getBonus = (amount: number) => {
  if (amount >= 5000) return 600
  if (amount >= 2000) return 200
  if (amount >= 1000) return 80
  if (amount >= 500) return 30
  return 0
}

const displayAmount = computed(() => {
  if (isCustom.value) return Number(customAmount.value) || 0
  return selectedAmount.value
})

const displayBonus = computed(() => {
  return getBonus(displayAmount.value)
})

const selectAmount = (amount: number) => {
  isCustom.value = false
  selectedAmount.value = amount
}

const enableCustom = () => {
  isCustom.value = true
  nextTick(() => {
    customInput.value?.focus()
  })
}

const validateCustom = () => {
  let val = Number(customAmount.value)
  if (isNaN(val) || val < 0) {
    customAmount.value = ''
  } else if (val > 50000) {
    ElMessage.warning('单次充值最高 50,000 元')
    customAmount.value = '50000'
  }
}

const handleRecharge = async () => {
  const amount = displayAmount.value
  if (amount <= 0) {
    ElMessage.warning('请输入有效充值金额')
    return
  }

  loading.value = true
  try {
    // 总额 = 充值金额 + 赠送金额
    const totalToAdd = amount + displayBonus.value
    const res: any = await userRecharge(totalToAdd)
    
    if (res?.code === 200 || res?.code === 0) {
      // 模拟支付延迟
      await new Promise(resolve => setTimeout(resolve, 1500))
      
      await userStore.syncUserProfile()
      successVisible.value = true
      customAmount.value = ''
    } else {
      ElMessage.error(res?.message || '充值失败')
    }
  } catch (e: any) {
    ElMessage.error(e?.response?.data?.message || '充值异常，请重试')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.recharge-card {
  background: #fff;
  border-radius: 24px;
  padding: 32px;
  border: 1px solid white;
  box-shadow: 0 10px 30px -10px rgba(15, 23, 42, 0.05);
}

.amount-item {
  border: 2px solid #f1f5f9;
  border-radius: 18px;
  padding: 20px 16px;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  overflow: hidden;
  display: flex;
  flex-col: column;
  align-items: center;
  justify-content: center;
  text-align: center;
  color: var(--lh-navy);
}

.amount-item:hover {
  border-color: var(--lh-gold);
  background: var(--lh-cream);
  transform: translateY(-2px);
}

.amount-item.is-active {
  border-color: var(--lh-navy);
  background: var(--lh-navy);
  color: var(--lh-gold);
}

.bonus-tag {
  position: absolute;
  top: 0;
  right: 0;
  background: var(--lh-red);
  color: #fff;
  font-size: 10px;
  font-weight: bold;
  padding: 2px 8px;
  border-bottom-left-radius: 10px;
}

.custom-input {
  background: transparent;
  border: none;
  font-family: inherit;
  font-weight: 900;
  font-size: 20px;
  width: 100%;
  color: inherit;
  outline: none;
  text-align: center;
}

.custom-input::placeholder {
  color: rgba(148, 163, 184, 0.5);
}

.pay-method-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16px 20px;
  border: 1px solid #f1f5f9;
  border-radius: 16px;
  cursor: pointer;
  transition: all 0.2s;
}

.pay-method-item:hover {
  background: #f8fafc;
  border-color: #e2e8f0;
}

.pay-method-item.is-active {
  border-color: var(--lh-navy);
  background: #f8fafc;
}

.check-circle {
  width: 22px;
  height: 22px;
  border: 2px solid #e2e8f0;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  transition: all 0.2s;
}

.check-circle.checked {
  background: var(--lh-navy);
  border-color: var(--lh-navy);
}

.sticky-panel {
  background: #fff;
  border-radius: 24px;
  border: 1px solid white;
  box-shadow: 0 20px 50px -20px rgba(15, 23, 42, 0.1);
  overflow: hidden;
  position: sticky;
  top: 100px;
}

.recharge-btn {
  border-radius: 16px;
  font-weight: 900;
  font-size: 16px;
  letter-spacing: 2px;
  background: var(--lh-navy);
  border: none;
  box-shadow: 0 10px 20px -5px rgba(15, 23, 42, 0.2);
  transition: all 0.3s;
}

.recharge-btn:hover {
  transform: scale(1.02);
  background: var(--lh-red);
}

:deep(.success-dialog) {
  border-radius: 24px;
  overflow: hidden;
}

@media (max-width: 640px) {
  .recharge-card {
    padding: 24px 16px;
  }
}
</style>
