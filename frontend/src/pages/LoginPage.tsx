import { useForm } from 'react-hook-form'
import { yupResolver } from '@hookform/resolvers/yup'
import * as yup from 'yup'
import { useDispatch } from 'react-redux'
import { login } from '../store/userSlice'
import { useNavigate } from 'react-router-dom'

const schema = yup.object().shape({
  email: yup.string().email().required(),
  password: yup.string().required(),
})

export function LoginPage() {
  const dispatch = useDispatch()
  const navigate = useNavigate()
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm({
    resolver: yupResolver(schema),
  })

  const onSubmit = (data: any) => {
    // In a real application, you would make an API call to authenticate the user
    // and get a token and user object.
    // For now, we'll just simulate it based on the email address.
    let role = 'driver'
    if (data.email.includes('admin')) {
      role = 'admin'
    } else if (data.email.includes('super')) {
      role = 'super-admin'
    }

    const token = 'fake-token'
    const user = { id: '1', name: 'John Doe', email: data.email, role }
    dispatch(login({ token, user }))

    switch (role) {
      case 'admin':
        navigate('/admin')
        break
      case 'driver':
        navigate('/driver')
        break
      case 'super-admin':
        navigate('/super-admin')
        break
      default:
        navigate('/')
    }
  }

  return (
    <div className="flex items-center justify-center h-screen">
      <form onSubmit={handleSubmit(onSubmit)} className="p-8 rounded shadow-md bg-gray-50 w-96">
        <h1 className="mb-4 text-2xl font-bold">Login</h1>
        <div className="mb-4">
          <label className="block mb-1">Email</label>
          <input
            {...register('email')}
            className="w-full px-3 py-2 border rounded"
          />
          <p className="text-red-500">{errors.email?.message}</p>
        </div>
        <div className="mb-4">
          <label className="block mb-1">Password</label>
          <input
            {...register('password')}
            type="password"
            className="w-full px-3 py-2 border rounded"
          />
          <p className="text-red-500">{errors.password?.message}</p>
        </div>
        <button
          type="submit"
          className="w-full px-4 py-2 text-white bg-blue-500 rounded"
        >
          Login
        </button>
      </form>
    </div>
  )
}
