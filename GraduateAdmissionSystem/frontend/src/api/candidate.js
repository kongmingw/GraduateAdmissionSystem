import request from '../utils/request'

// 考生档案 API
export function getCandidateList() {
  return request.get('/candidate/list')
}

export function getCandidateByExamId(examId) {
  return request.get(`/candidate/${examId}`)
}

export function getCandidateByMajor(majorCode) {
  return request.get(`/candidate/major/${majorCode}`)
}

export function registerCandidate(data) {
  return request.post('/candidate/register', data)
}

export function updateCandidate(data) {
  return request.put('/candidate/update', data)
}

export function deleteCandidate(examId) {
  return request.delete(`/candidate/delete/${examId}`)
}