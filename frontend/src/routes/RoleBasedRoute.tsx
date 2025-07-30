import { useSelector } from 'react-redux'
import { Navigate, Outlet } from 'react-router-dom'
import { UserState } from '../store/userSlice'

interface RoleBasedRouteProps {
  allowedRoles: string[]
}

export function RoleBasedRoute({ allowedRoles }: RoleBasedRouteProps) {
  const user = useSelector((state: { user: UserState }) => state.user.user)

  if (!user) {
    return <Navigate to="/login" />
  }

  return allowedRoles.includes(user.role) ? (
    <Outlet />
  ) : (
    <Navigate to="/" />
  )
}
