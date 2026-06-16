<template>
  <div class="step1-datetime animate__animated animate__fadeIn">
    <div class="grid grid-cols-1 lg:grid-cols-2 gap-12">
      
      <!-- 左侧：日期 -->
      <div class="card-premium p-8 h-fit relative overflow-hidden">
        <div class="absolute top-0 left-0 w-1 h-20 bg-[var(--lh-red)]"></div>
        <div class="flex items-center justify-between mb-10">
          <h3 class="text-xl font-serif font-bold flex items-center gap-3 text-[var(--lh-navy)]">
            <el-icon class="text-[var(--lh-red)]"><Calendar /></el-icon> 
            <span>壹 · 选择预约日期</span>
          </h3>
          <div class="flex items-center gap-2">
            <el-button-group size="small" class="calendar-controls">
              <el-button :icon="ArrowLeft" circle @click="moveMonth(-1)" />
              <el-button circle @click="moveToday">今</el-button>
              <el-button :icon="ArrowRight" circle @click="moveMonth(1)" />
            </el-button-group>
          </div>
        </div>
        
        <el-calendar ref="calendarRef" v-model="selectedDate" class="premium-calendar">
          <template #header>
             <div class="custom-calendar-header font-serif font-bold text-lg text-[var(--lh-navy)] tracking-widest pl-2">
                {{ dayjs(selectedDate).format('YYYY年 MM月') }}
             </div>
          </template>
          <template #date-cell="{ data }">
            <div class="calendar-day-cell relative h-full flex flex-col items-center justify-center group overflow-hidden">
              <span class="day-number text-sm font-serif transition-colors" :class="isDateFull(data.day) ? 'text-gray-300' : 'text-[var(--lh-navy)]'">
                {{ data.day.split('-').slice(-1)[0] }}
              </span>
              
              <!-- 状态标识 -->
              <div v-if="isDateFull(data.day)" class="absolute bottom-1 w-1 h-1 rounded-full bg-[var(--lh-red)] opacity-40"></div>
              <div v-else-if="isDateBusy(data.day)" class="absolute bottom-1 w-1.5 h-1.5 rounded-full bg-[var(--lh-gold)] shadow-[0_0_8px_var(--lh-gold)]"></div>
              
              <!-- 选中态 -->
              <div v-if="data.isSelected" class="selected-overlay absolute inset-0 bg-[var(--lh-red)]/5 border-2 border-[var(--lh-red)]/40 rounded-lg -z-10 shadow-inner"></div>
            </div>
          </template>
        </el-calendar>
        
        <div class="flex gap-8 mt-10 text-[11px] text-[var(--lh-text-secondary)] justify-center font-bold tracking-[0.1em] border-t pt-8 border-dashed">
          <div class="flex items-center gap-2"><span class="w-2.5 h-2.5 rounded-full bg-blue-100 border border-blue-300"></span> 充足</div>
          <div class="flex items-center gap-2"><span class="w-2.5 h-2.5 rounded-full bg-[var(--lh-gold)]"></span> 紧张</div>
          <div class="flex items-center gap-2"><span class="w-2.5 h-2.5 rounded-full bg-[var(--lh-red)]"></span> 约满</div>
        </div>
      </div>

      <!-- 右侧：时段 -->
      <div class="card-premium p-8 h-fit relative">
        <div class="absolute top-0 left-0 w-1 h-20 bg-[var(--lh-gold)]"></div>
        <h3 class="text-xl font-serif font-bold mb-10 flex items-center gap-3 text-[var(--lh-navy)]">
          <el-icon class="text-[var(--lh-gold)]"><Clock /></el-icon> 
          <span>贰 · 选择到店时段</span>
        </h3>

        <div v-if="selectedDateFormatted" class="space-y-8 animate__animated animate__fadeInRight">
          <div class="bg-[var(--lh-navy)] rounded-xl p-5 flex items-center justify-between shadow-lg">
             <div class="flex flex-col">
                <span class="text-[10px] uppercase font-bold text-[var(--lh-gold)] tracking-[0.2em] mb-1">Current Selected</span>
                <span class="text-xl font-serif font-bold text-white tracking-widest">{{ dayjs(selectedDate).format('YYYY / MM / DD') }}</span>
             </div>
             <div class="w-10 h-10 rounded-full border border-white/20 flex items-center justify-center">
                <el-icon color="white" size="20"><Calendar /></el-icon>
             </div>
          </div>
          
          <div class="grid grid-cols-1 gap-5">
            <div 
              v-for="slot in timeSlots" 
              :key="slot.id"
              class="time-slot-premium group"
              :class="[
                slot.remain === 0 ? 'slot-full' :
                store.timeSlot === slot.label ? 'slot-active' : ''
              ]"
              @click="slot.remain > 0 && store.setDateTime(selectedDateFormatted, slot.label)"
            >
              <div class="flex items-center justify-between w-full h-full relative z-10 transition-transform duration-300 group-hover:px-2">
                <div class="flex items-center gap-5">
                  <div class="icon-box w-12 h-12 rounded-xl bg-[var(--lh-cream)] flex items-center justify-center transition-all group-hover:rotate-12">
                     <el-icon size="24" class="text-[var(--lh-gold)]"><Timer /></el-icon>
                  </div>
                  <div>
                    <div class="font-serif font-bold text-lg text-[var(--lh-navy)] group-hover:text-[var(--lh-red)] transition-colors">{{ slot.label }}</div>
                    <div class="text-[11px] font-medium text-[var(--lh-text-secondary)] opacity-80 mt-0.5">{{ slot.desc }}</div>
                  </div>
                </div>
                
                <div class="text-right">
                  <div v-if="slot.remain === 0" class="text-sm font-bold text-gray-400">已筹满</div>
                  <div v-else class="flex flex-col items-end">
                    <span class="text-[10px] font-bold uppercase tracking-wider mb-1" :class="slot.remain <= 2 ? 'text-[var(--lh-red)]' : 'text-green-600'">
                      Available
                    </span>
                    <span class="text-xl font-serif font-bold leading-none" :class="slot.remain <= 2 ? 'text-[var(--lh-red)]' : 'text-[var(--lh-navy)]'">
                      {{ slot.remain }}
                    </span>
                  </div>
                </div>
              </div>
              <div class="absolute bottom-0 left-0 h-1 bg-[var(--lh-red)]/20 w-0 transition-all duration-500 group-hover:w-full"></div>
            </div>
          </div>

          <div class="pt-6">
            <button 
              class="btn-premium w-full py-5 text-lg tracking-[0.2em] font-serif disabled:opacity-30 flex items-center justify-center gap-3" 
              :disabled="!store.timeSlot"
              @click="$emit('next')"
            >
              <span>下一步：继续挑选</span>
              <el-icon><ArrowRight /></el-icon>
            </button>
          </div>
        </div>
        
        <div v-else class="h-[400px] flex flex-col items-center justify-center text-gray-200">
          <el-icon size="64" class="mb-4 opacity-10"><Calendar /></el-icon>
          <p class="font-serif tracking-[0.06em] text-[var(--lh-text-secondary)] opacity-60">请先在左侧选择预约日期</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, watch, onMounted } from 'vue'
import { Calendar, Clock, Timer, ArrowLeft, ArrowRight } from '@element-plus/icons-vue'
import { useBookingStore } from '@/stores/booking'
import dayjs from 'dayjs'

const store = useBookingStore()
defineEmits(['next'])

const calendarRef = ref<any>(null)
const selectedDate = ref(store.date ? new Date(store.date) : new Date())
const selectedDateFormatted = computed(() => dayjs(selectedDate.value).format('YYYY-MM-DD'))

// 日历控制
const moveMonth = (val: number) => {
  if (!calendarRef.value) return
  calendarRef.value.selectDate(val > 0 ? 'next-month' : 'prev-month')
}

const moveToday = () => {
  if (!calendarRef.value) return
  calendarRef.value.selectDate('today')
}

// 模拟数据: 已满或饱和日期
const fullDates = ['2026-04-20', '2026-04-25']
const busyDates = ['2026-04-12', '2026-04-18']

// 模拟数据: 时间段
const timeSlots = [
  { id: '1', label: '09:00-12:00', desc: '晨起妆造，避开高峰', remain: 5 },
  { id: '2', label: '13:00-17:00', desc: '午后拍摄，光影最佳', remain: 2 },
  { id: '3', label: '17:00-21:00', desc: '华灯初上，夜游神都', remain: 8 },
  { id: '4', label: '全天预约', desc: '深度体验，不限时长', remain: 10 }
]

const isDateFull = (day: string) => fullDates.includes(day)
const isDateBusy = (day: string) => busyDates.includes(day)

onMounted(() => {
  if (store.date) {
    selectedDate.value = new Date(store.date)
  }
})

watch(selectedDateFormatted, (newVal) => {
  store.date = newVal
  store.timeSlot = '' 
  store.saveToStorage()
})
</script>

<style lang="scss" scoped>
.premium-calendar {
  border: none;
  background: transparent;
  
  :deep(.el-calendar__header) {
    display: none; // 隐藏原生页头，使用自定义页头
  }
  
  :deep(.el-calendar-table) {
    thead th {
      color: var(--lh-text-secondary);
      font-family: var(--font-serif);
      font-weight: 800;
      padding: 15px 0;
      text-transform: uppercase;
      letter-spacing: 0.1em;
      font-size: 13px;
    }
  }

  :deep(.el-calendar-table .el-calendar-day) {
    height: 72px;
    padding: 0;
    border: none !important;
    transition: all 0.3s cubic-bezier(0.16, 1, 0.3, 1);
    
    &:hover {
      background: rgba(153, 27, 27, 0.02); // 浅红色高亮
    }
  }
  
  :deep(.el-calendar-table td.is-today) .day-number {
    color: var(--lh-red);
    font-weight: 900;
    position: relative;
    
    &::after {
      content: '';
      position: absolute;
      bottom: -4px;
      left: 50%;
      transform: translateX(-50%);
      width: 12px;
      height: 2px;
      background: var(--lh-red);
    }
  }
}

.custom-calendar-header {
  border-left: 4px solid var(--lh-red);
  margin-bottom: 25px;
}

.time-slot-premium {
  padding: 24px;
  background: white;
  border: 1px solid var(--lh-border);
  border-radius: 16px;
  cursor: pointer;
  transition: var(--transition-smooth);
  position: relative;
  overflow: hidden;
  box-shadow: var(--shadow-sm);
}

.slot-active {
  border-color: var(--lh-red);
  background: var(--lh-red);
  transform: scale(1.02);
  box-shadow: 0 10px 30px rgba(153, 27, 27, 0.15);
  
  .icon-box {
    background: white !important;
    .el-icon { color: var(--lh-red) !important; }
  }
  
  [class*="text-[var(--lh-navy)]"], [class*="text-[var(--lh-text-secondary)]"] {
    color: white !important;
  }
  
  [class*="text-[var(--lh-gold)]"] {
    color: var(--lh-gold) !important; // 保持金色高亮
  }

  .text-base, .text-lg, .text-xl {
    color: white !important;
  }
}

.calendar-controls {
  :deep(.el-button) {
    border: 1px solid var(--lh-border);
    &:hover {
      color: var(--lh-red);
      border-color: var(--lh-red);
    }
  }
}

.slot-full {
  opacity: 0.4;
  cursor: not-allowed;
  filter: grayscale(1);
}
</style>
