import axios from 'axios'

const api = axios.create({
  baseURL: '/api',
  timeout: 5000
})

// 获取录取名单
export function getAdmissionList() {
  return api.get('/admission')
}

// 录取考生
export function admitStudent(examNumber, department) {
  return api.post(`/admission/${examNumber}`, null, { params: { department } })
}

// 更新录取信息
export function updateAdmission(data) {
  return api.put('/admission', data)
}

// 取消录取
export function deleteAdmission(examNumber) {
  return api.delete(`/admission/${examNumber}`)
}