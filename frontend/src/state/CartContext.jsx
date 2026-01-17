import { createContext, useContext, useMemo, useState } from 'react'

const CartContext = createContext(null)
const STORAGE_KEY = 'aiwb_cart'

export function CartProvider({ children }) {
  const [items, setItems] = useState(() => {
    const stored = localStorage.getItem(STORAGE_KEY)
    return stored ? JSON.parse(stored) : []
  })

  const value = useMemo(() => {
    const persist = (next) => {
      setItems(next)
      localStorage.setItem(STORAGE_KEY, JSON.stringify(next))
    }
    return {
      items,
      add(item) {
        const next = [...items, item]
        persist(next)
      },
      remove(index) {
        const next = items.filter((_, idx) => idx !== index)
        persist(next)
      },
      clear() {
        persist([])
      },
    }
  }, [items])

  return <CartContext.Provider value={value}>{children}</CartContext.Provider>
}

export function useCart() {
  return useContext(CartContext)
}
