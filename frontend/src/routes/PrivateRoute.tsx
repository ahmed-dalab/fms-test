import { useSelector } from 'react-redux'
import { Navigate, Outlet } from 'react-router-dom'
import { UserState } from '../store/userSlice'

export function PrivateRoute() {
  const isAuthenticated = useSelector(
    (state: { user: UserState }) => state.user.isAuthenticated
  )

  return isAuthenticated ? <Outlet /> : <Navigate to="/login" />
}
