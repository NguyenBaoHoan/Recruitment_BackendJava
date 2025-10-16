import { useState } from 'react'
import { AppContext } from './AppContext'

export default function AppProvider({ children }) {
  const [user, setUser] = useState(null)

  const value = {
    user,
    setUser,
  }

  return <AppContext.Provider value={value}>{children}</AppContext.Provider>
}