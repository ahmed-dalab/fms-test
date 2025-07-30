import { Outlet } from 'react-router-dom'

export function DriverLayout() {
  return (
    <div>
      <h1 className="text-2xl font-bold">Driver Layout</h1>
      <Outlet />
    </div>
  )
}
