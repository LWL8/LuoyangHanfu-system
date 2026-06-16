import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'
import gsap from 'gsap'
import { ScrollToPlugin } from 'gsap/ScrollToPlugin'
import 'animate.css'
import './assets/styles/tailwind.css'
import './assets/styles/global.scss'
import 'swiper/css'
import 'swiper/css/pagination'

gsap.registerPlugin(ScrollToPlugin)

const app = createApp(App)
app.use(createPinia())
app.use(router)
app.mount('#app')
