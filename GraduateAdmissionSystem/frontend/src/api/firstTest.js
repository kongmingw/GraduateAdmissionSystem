import request from '../utils/request'

// 初试成绩 API
export function getFirstTestList() {
  return request.get('/first-test/list')
}

export function getFirstTestByExamId(examId) {
  return request.get(`/first-test/${examId}`)
}

export function addFirstTest(data) {
  return request.post('/first-test/add', data)
}

export function updateFirstTest(data) {
  return request.put('/first-test/update', data)
}

export function deleteFirstTest(examId) {
  return request.delete(`/first-test/delete/${examId}`)
}
