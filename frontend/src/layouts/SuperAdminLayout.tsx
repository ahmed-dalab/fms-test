import { Outlet } from 'react-router-dom'

export function SuperAdminLayout() {
  return (
    <div>
      <h1 className="text-2xl font-bold">Super Admin Layout</h1>
      <Outlet />
    </div>
  )
}
