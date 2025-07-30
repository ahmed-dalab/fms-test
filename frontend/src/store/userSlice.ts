import { createSlice } from '@reduxjs/toolkit'
import type { PayloadAction } from '@reduxjs/toolkit'

export interface UserState {
  isAuthenticated: boolean
  token: string | null
  user: {
    id: string
    name: string
    email: string
    role: string
  } | null
}

const initialState: UserState = {
  isAuthenticated: false,
  token: null,
  user: null,
}

export const userSlice = createSlice({
  name: 'user',
  initialState,
  reducers: {
    login: (state, action: PayloadAction<{ token: string; user: any }>) => {
      state.isAuthenticated = true
      state.token = action.payload.token
      state.user = action.payload.user
    },
    logout: (state) => {
      state.isAuthenticated = false
      state.token = null
      state.user = null
    },
  },
})

// Action creators are generated for each case reducer function
export const { login, logout } = userSlice.actions

export default userSlice.reducer
