import { createBrowserRouter, RouterProvider } from 'react-router-dom'
import { LoginPage } from '../pages/LoginPage'
import { PrivateRoute } from './PrivateRoute'
import { adminRoutes } from './adminRoutes'
import { driverRoutes } from './driverRoutes'
import { superAdminRoutes } from './superAdminRoutes'
import { RoleBasedRoute } from './RoleBasedRoute'
import { HomePage } from '../pages/HomePage'

const router = createBrowserRouter([
  {
    path: '/',
    element: <PrivateRoute />,
    children: [
      {
        path: '/',
        element: <HomePage />,
      },
      {
        path: 'admin',
        element: <RoleBasedRoute allowedRoles={['admin']} />,
        children: adminRoutes,
      },
      {
        path: 'driver',
        element: <RoleBasedRoute allowedRoles={['driver']} />,
        children: driverRoutes,
      },
      {
        path: 'super-admin',
        element: <RoleBasedRoute allowedRoles={['super-admin']} />,
        children: superAdminRoutes,
      },
    ],
  },
  {
    path: '/login',
    element: <LoginPage />,
  },
])

export function Router() {
  return <RouterProvider router={router} />
}
