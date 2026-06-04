import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    redirect: '/students'
  },
  {
    path: '/students',
    name: 'Students',
    component: () => import('../views/StudentList.vue')
  },
  {
    path: '/scores',
    name: 'Scores',
    component: () => import('../views/ScoreManage.vue')
  },
  {
    path: '/admission',
    name: 'Admission',
    component: () => import('../views/AdmissionList.vue')
  },
  {
    path: '/statistics',
    name: 'Statistics',
    component: () => import('../views/Statistics.vue')
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router