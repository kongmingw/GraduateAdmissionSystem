import request from '../utils/request'

// 复试成绩 API
export function getSecondTestList() {
  return request.get('/second-test/list')
}

export function getSecondTestByExamId(examId) {
  return request.get(`/second-test/${examId}`)
}

export function addSecondTest(data) {
  return request.post('/second-test/add', data)
}

export function updateSecondTest(data) {
  return request.put('/second-test/update', data)
}

export function deleteSecondTest(examId) {
  return request.delete(`/second-test/delete/${examId}`)
}
