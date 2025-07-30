import { RouteObject } from 'react-router-dom'
import { DriverLayout } from '../layouts/DriverLayout'

export const driverRoutes: RouteObject[] = [
  {
    path: '/driver',
    element: <DriverLayout />,
    children: [
      {
        path: '',
        element: <div>Driver Dashboard</div>,
      },
    ],
  },
]
