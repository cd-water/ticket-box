import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import AdminLayout from '@/layouts/AdminLayout.vue'
import LoginView from '@/views/LoginView.vue'
import OverviewView from '@/views/OverviewView.vue'
import MovieView from '@/views/MovieView.vue'
import CinemaView from '@/views/CinemaView.vue'
import ShowView from '@/views/ShowView.vue'
import PlayView from '@/views/PlayView.vue'
import OrderView from '@/views/OrderView.vue'
import UserView from '@/views/UserView.vue'
import SupportView from '@/views/SupportView.vue'
import KnowledgeView from '@/views/KnowledgeView.vue'
import ModelConfigView from '@/views/ModelConfigView.vue'
import SystemView from '@/views/SystemView.vue'

declare module 'vue-router' {
  interface RouteMeta {
    requiresAuth?: boolean
    title?: string
  }
}

const DEFAULT_REDIRECT = '/overview'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/login', component: LoginView },
    {
      path: '/',
      component: AdminLayout,
      redirect: DEFAULT_REDIRECT,
      meta: { requiresAuth: true },
      children: [
        { path: 'overview', component: OverviewView, meta: { title: '数据概览' } },
        { path: 'movies', component: MovieView, meta: { title: '电影管理' } },
        { path: 'cinemas', component: CinemaView, meta: { title: '影院管理' } },
        { path: 'shows', component: ShowView, meta: { title: '演出管理' } },
        { path: 'play', component: PlayView, meta: { title: '玩乐管理' } },
        { path: 'orders', component: OrderView, meta: { title: '订单管理' } },
        { path: 'users', component: UserView, meta: { title: '用户管理' } },
        { path: 'support', component: SupportView, meta: { title: '客服中心' } },
        { path: 'knowledge', component: KnowledgeView, meta: { title: '知识库' } },
        { path: 'models', component: ModelConfigView, meta: { title: '模型配置' } },
        { path: 'system', component: SystemView, meta: { title: '系统管理' } },
      ],
    },
  ],
})

router.beforeEach((to) => {
  const auth = useAuthStore()
  if (to.matched.some((record) => record.meta.requiresAuth) && !auth.isLoggedIn) {
    return { path: '/login', query: { redirect: to.fullPath } }
  }
  if (to.path === '/login' && auth.isLoggedIn) {
    return DEFAULT_REDIRECT
  }
})

export default router
