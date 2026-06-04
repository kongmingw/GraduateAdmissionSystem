import axios from 'axios'

const api = axios.create({
  baseURL: '/api',
  timeout: 5000
})

export function getStatistics() {
  return api.get('/statistics')
}