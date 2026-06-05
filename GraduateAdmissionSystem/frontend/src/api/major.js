import request from '../utils/request'

// 专业字典 API
export function getMajorList() {
  return request.get('/major/list')
}

export function getMajorByCode(majorCode) {
  return request.get(`/major/${majorCode}`)
}

export function addMajor(data) {
  return request.post('/major/add', data)
}

export function updateMajor(data) {
  return request.put('/major/update', data)
}

export function deleteMajor(majorCode) {
  return request.delete(`/major/delete/${majorCode}`)
}