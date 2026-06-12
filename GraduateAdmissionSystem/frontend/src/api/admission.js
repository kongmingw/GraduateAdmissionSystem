import request from '../utils/request'

// 录取名单 API
export function getAdmissionList() {
  return request.get('/admission/list')
}

export function getAdmissionByExamId(examId) {
  return request.get(`/admission/${examId}`)
}

export function admitCandidate(data) {
  return request.post('/admission/admit', data)
}

export function cancelAdmission(examId) {
  return request.delete(`/admission/cancel/${examId}`)
}

export function getCandidates() {
  return request.get('/admission/candidates')
}
