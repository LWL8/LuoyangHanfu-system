import NProgress from 'nprogress'
import 'nprogress/nprogress.css'

// NProgress 配置
NProgress.configure({ 
  easing: 'ease', 
  speed: 500, 
  showSpinner: false, // 隐藏圆形加载图标
  trickleSpeed: 200,
  minimum: 0.3
})

export default NProgress
