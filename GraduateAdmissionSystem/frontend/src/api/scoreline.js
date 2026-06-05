import request from '../utils/request'

// 分数线 API
export function getScoreLineList() {
  return request.get('/score-line/list')
}

export function getScoreLineByYear(year) {
  return request.get(`/score-line/year/${year}`)
}

export function addScoreLine(data) {
  return request.post('/score-line/add', data)
}

export function updateScoreLine(data) {
  return request.put('/score-line/update', data)
}

export function deleteScoreLine(id) {
  return request.delete(`/score-line/delete/${id}`)
}
