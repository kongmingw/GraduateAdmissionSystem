import request from '../utils/request'

// 统计分析 API
export function getAdmissionStatistics() {
  return request.get('/statistics/admission')
}

export function getScoreStatistics() {
  return request.get('/statistics/scores')
}

export function getAllStatistics() {
  return request.get('/statistics/all')
}

export function getScreening(year) {
  return request.get('/statistics/screening', { params: { year } })
}
