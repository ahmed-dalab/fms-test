import { useDispatch, useSelector } from 'react-redux'
import { logout, UserState } from '../store/userSlice'
import { useNavigate } from 'react-router-dom'

export function HomePage() {
  const dispatch = useDispatch()
  const navigate = useNavigate()
  const user = useSelector((state: { user: UserState }) => state.user.user)

  const handleLogout = () => {
    dispatch(logout())
    navigate('/login')
  }

  return (
    <div className="flex items-center justify-center h-screen">
      <div className="p-8 rounded shadow-md bg-gray-50 w-96">
        <h1 className="mb-4 text-2xl font-bold">Welcome, {user?.name}!</h1>
        <button
          onClick={handleLogout}
          className="w-full px-4 py-2 text-white bg-red-500 rounded"
        >
          Logout
        </button>
      </div>
    </div>
  )
}
