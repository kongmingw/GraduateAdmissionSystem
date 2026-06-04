import axios from 'axios'

const api = axios.create({
  baseURL: '/api',
  timeout: 5000
})

// 获取所有考生
export function getStudents() {
  return api.get('/students')
}

// 根据考号获取考生
export function getStudentByExamNumber(examNumber) {
  return api.get(`/students/${examNumber}`)
}

// 新增考生
export function addStudent(data) {
  return api.post('/students', data)
}

// 更新考生
export function updateStudent(data) {
  return api.put('/students', data)
}

// 删除考生
export function deleteStudent(examNumber) {
  return api.delete(`/students/${examNumber}`)
}