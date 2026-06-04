import axios from 'axios'

const api = axios.create({
  baseURL: '/api',
  timeout: 5000
})

// 获取初试成绩
export function getInitialScore(examNumber) {
  return api.get(`/scores/initial/${examNumber}`)
}

// 保存初试成绩
export function saveInitialScore(data) {
  return api.post('/scores/initial', data)
}

// 获取复试成绩
export function getReexamScore(examNumber) {
  return api.get(`/scores/reexam/${examNumber}`)
}

// 保存复试成绩
export function saveReexamScore(data) {
  return api.post('/scores/reexam', data)
}