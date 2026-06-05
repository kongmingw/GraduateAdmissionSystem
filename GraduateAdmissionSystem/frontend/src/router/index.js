import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    redirect: '/candidate'
  },
  {
    path: '/candidate',
    name: 'Candidate',
    component: () => import('../views/CandidateList.vue'),
    meta: { title: '考生管理' }
  },
  {
    path: '/major',
    name: 'Major',
    component: () => import('../views/MajorDict.vue'),
    meta: { title: '专业字典' }
  },
  {
    path: '/first-test',
    name: 'FirstTest',
    component: () => import('../views/FirstTestScore.vue'),
    meta: { title: '初试成绩' }
  },
  {
    path: '/second-test',
    name: 'SecondTest',
    component: () => import('../views/SecondTestScore.vue'),
    meta: { title: '复试成绩' }
  },
  {
    path: '/score-line',
    name: 'ScoreLine',
    component: () => import('../views/ScoreLine.vue'),
    meta: { title: '分数线' }
  },
  {
    path: '/admission',
    name: 'Admission',
    component: () => import('../views/AdmissionList.vue'),
    meta: { title: '录取管理' }
  },
  {
    path: '/statistics',
    name: 'Statistics',
    component: () => import('../views/Statistics.vue'),
    meta: { title: '统计分析' }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router