const API_URL = '/api' // Sẽ được proxy bởi Vite

export const apiService = {
  async get(endpoint) {
    const response = await fetch(`${API_URL}${endpoint}`, {
      credentials: 'include', // Gửi cookies
    })
    if (!response.ok) throw new Error('API request failed')
    return response.json()
  },

  async post(endpoint, data) {
    const response = await fetch(`${API_URL}${endpoint}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      credentials: 'include',
      body: JSON.stringify(data),
    })
    if (!response.ok) throw new Error('API request failed')
    return response.json()
  },
}