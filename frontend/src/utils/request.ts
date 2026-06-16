import axios, { type AxiosInstance, type AxiosRequestConfig } from 'axios'

/** 响应拦截器已返回 response.data，此处类型与运行时一致 */
export interface HttpClient {
  get<T = unknown>(url: string, config?: AxiosRequestConfig): Promise<T>
  post<T = unknown>(url: string, data?: unknown, config?: AxiosRequestConfig): Promise<T>
  put<T = unknown>(url: string, data?: unknown, config?: AxiosRequestConfig): Promise<T>
  delete<T = unknown>(url: string, config?: AxiosRequestConfig): Promise<T>
}

const service: AxiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '/api',
  timeout: 10000
})

// 请求拦截器
service.interceptors.request.use(
  (config) => {
    // 兼容已有代码里同时出现 "/api/xxx" 与 "xxx" 两种写法，避免拼成 /api/api/xxx
    if (config.url && config.url.startsWith('/api/')) {
      config.url = config.url.replace(/^\/api\//, '/')
    }
    const token = localStorage.getItem('hanfu_token')
    if (token) {
      config.headers['token'] = token
      config.headers['Authorization'] = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  (response) => {
    // 这里保持原来的逻辑，不做 R 对象解构，直接返回 data
    return response.data
  },
  (error) => {
    const status = error?.response?.status
    if (status === 401) {
      localStorage.removeItem('hanfu_token')
      localStorage.removeItem('user_info')
      const current = window.location.pathname + window.location.search
      if (!window.location.pathname.startsWith('/login')) {
        window.location.href = `/login?redirect=${encodeURIComponent(current)}`
      }
    }
    return Promise.reject(error)
  }
)

export default service as unknown as HttpClient
