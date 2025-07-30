import { RouteObject } from 'react-router-dom'
import { SuperAdminLayout } from '../layouts/SuperAdminLayout'

export const superAdminRoutes: RouteObject[] = [
  {
    path: '/super-admin',
    element: <SuperAdminLayout />,
    children: [
      {
        path: '',
        element: <div>Super Admin Dashboard</div>,
      },
    ],
  },
]
